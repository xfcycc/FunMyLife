import { computed, nextTick, ref, shallowRef } from 'vue';
import type { RouteRecordRaw } from 'vue-router';
import { defineStore } from 'pinia';
import { useBoolean } from '@sa/hooks';
import type { ElegantConstRoute, LastLevelRouteKey, RouteKey } from '@elegant-router/types';
import { router } from '@/router';
import { SetupStoreId } from '@/enum';
import { createFmlConstantRoutes, getAuthVueRoutes } from '@/router/routes';
import { useTabStore } from '../tab';
import {
  getBreadcrumbsByRoute,
  getCacheRouteNames,
  getGlobalMenusByAuthRoutes,
  getSelectedMenuKeyPathByKey,
  sortRoutesByOrder,
  transformMenuToSearchMenus,
  updateLocaleOfGlobalMenus
} from './shared';

export const useRouteStore = defineStore(SetupStoreId.Route, () => {
  const tabStore = useTabStore();
  const { bool: isInitConstantRoute, setBool: setIsInitConstantRoute } = useBoolean();

  /**
   * 兼容旧调用方保留的初始化标记。
   *
   * FML 已经不再初始化后台权限路由；这里在前台常量路由就绪后置为 true，
   * 让布局、页签等还没拆掉的公共模块不会误以为路由尚未准备好。
   */
  const isInitAuthRoute = ref(false);

  function setIsInitAuthRoute(value: boolean) {
    isInitAuthRoute.value = value;
  }

  /** FML 首页路由名，用于页签和根路由回退。 */
  const routeHome = ref((import.meta.env.VITE_ROUTE_HOME || 'home') as LastLevelRouteKey);

  /** 已注册的 FML 常量路由集合。 */
  const constantRoutes = shallowRef<ElegantConstRoute[]>([]);

  function addConstantRoutes(routes: ElegantConstRoute[]) {
    const constantRoutesMap = new Map<string, ElegantConstRoute>([]);

    routes.forEach(route => {
      constantRoutesMap.set(route.name, route);
    });

    constantRoutes.value = Array.from(constantRoutesMap.values());
  }

  const removeRouteFns: (() => void)[] = [];

  /** 全局菜单数据。FML 页面暂时隐藏菜单，但布局组件仍会读取这个字段。 */
  const menus = ref<App.Global.Menu[]>([]);
  const searchMenus = computed(() => transformMenuToSearchMenus(menus.value));

  /** 根据当前 FML 常量路由生成菜单缓存。 */
  function getGlobalMenus(routes: ElegantConstRoute[]) {
    menus.value = getGlobalMenusByAuthRoutes(routes);
  }

  /** 语言切换时同步更新菜单文案。 */
  function updateGlobalMenusByLocale() {
    menus.value = updateLocaleOfGlobalMenus(menus.value);
  }

  /** KeepAlive 路由缓存名单。 */
  const cacheRoutes = ref<RouteKey[]>([]);

  /**
   * 临时排除缓存的路由名单。
   *
   * 关闭页签或刷新页签时会短暂写入当前路由名，下一轮渲染后清空。
   */
  const excludeCacheRoutes = ref<RouteKey[]>([]);

  /**
   * 从 Vue Router 路由记录中提取需要 KeepAlive 的页面名。
   *
   * @param routes Vue Router 路由记录
   */
  function getCacheRoutes(routes: RouteRecordRaw[]) {
    cacheRoutes.value = getCacheRouteNames(routes);
  }

  /**
   * 重置指定页面的 KeepAlive 缓存。
   *
   * @default router.currentRoute.value.name 当前路由名
   * @param routeKey 需要重置缓存的路由名
   */
  async function resetRouteCache(routeKey?: RouteKey) {
    const routeName = routeKey || (router.currentRoute.value.name as RouteKey);

    excludeCacheRoutes.value.push(routeName);

    await nextTick();

    excludeCacheRoutes.value = [];
  }

  /** 当前页面面包屑。FML 页面大多自行渲染导航，但公共壳层仍依赖这个字段。 */
  const breadcrumbs = computed(() => getBreadcrumbsByRoute(router.currentRoute.value, menus.value));

  /** 重置路由 store，并重新注册 FML 常量路由。 */
  async function resetStore() {
    const routeStore = useRouteStore();

    routeStore.$reset();

    resetVueRoutes();

    // store 清空后需要重新注册前台路由，否则刷新后的目标地址会落到 404。
    await initConstantRoute();
  }

  /** 移除本 store 动态挂到 Vue Router 上的路由记录。 */
  function resetVueRoutes() {
    removeRouteFns.forEach(fn => fn());
    removeRouteFns.length = 0;
  }

  /** 初始化并注册 FML 前台常量路由。 */
  async function initConstantRoute() {
    if (isInitConstantRoute.value) return;

    const { constantRoutes: fmlRoutes } = createFmlConstantRoutes();

    // 只注册前台常量路由。后台管理系统原来的动态路由、角色菜单和远端路由表都不再进入启动链路。
    addConstantRoutes(fmlRoutes);

    handleConstantRoutes();

    setIsInitConstantRoute(true);
    setIsInitAuthRoute(true);

    tabStore.initHomeTab();
  }

  /**
   * Init auth route
   *
   * FML 当前不再使用后台权限路由。保留这个方法只是为了兼容少量旧调用方，避免它们触发
   * RuoYi 平台的用户信息与远端路由接口。
   */
  async function initAuthRoute() {
    if (!isInitConstantRoute.value) {
      await initConstantRoute();
    }
    setIsInitAuthRoute(true);
    tabStore.initHomeTab();
  }

  /** 把 FML 常量路由转换成 Vue Router 可识别的记录，并刷新菜单与缓存。 */
  function handleConstantRoutes() {
    const sortRoutes = sortRoutesByOrder([...constantRoutes.value]);

    const vueRoutes = getAuthVueRoutes(sortRoutes);

    resetVueRoutes();

    addRoutesToVueRouter(vueRoutes);

    getGlobalMenus(sortRoutes);

    getCacheRoutes(vueRoutes);
  }

  /**
   * 向 Vue Router 添加路由。
   *
   * @param routes Vue Router 路由记录
   */
  function addRoutesToVueRouter(routes: RouteRecordRaw[]) {
    routes.forEach(route => {
      const removeFn = router.addRoute(route);
      addRemoveRouteFn(removeFn);
    });
  }

  /**
   * 记录 Vue Router 返回的移除函数，后续重置 store 时统一清理。
   *
   * @param fn 单条路由的移除函数
   */
  function addRemoveRouteFn(fn: () => void) {
    removeRouteFns.push(fn);
  }

  /**
   * 查询后台权限路由是否存在。
   *
   * @param routePath 路由路径
   */
  async function getIsAuthRouteExist(_routePath: string) {
    // 后台权限路由已从 FML 启动链路移除，旧的 403 判定统一视为不存在。
    return false;
  }

  /**
   * 获取菜单选中项的父级路径。
   *
   * @param selectedKey 当前选中的菜单 key
   */
  function getSelectedMenuKeyPath(selectedKey: string) {
    return getSelectedMenuKeyPathByKey(selectedKey, menus.value);
  }

  async function onRouteSwitchWhenLoggedIn() {
    // FML 不再区分登录态，这里保留为空实现，避免旧布局调用时报错。
  }

  async function onRouteSwitchWhenNotLoggedIn() {
    // FML 页面直接访问即可，这里保留为空实现，作为后续前台初始化扩展点。
  }

  return {
    resetStore,
    routeHome,
    menus,
    searchMenus,
    updateGlobalMenusByLocale,
    cacheRoutes,
    excludeCacheRoutes,
    resetRouteCache,
    breadcrumbs,
    initConstantRoute,
    isInitConstantRoute,
    initAuthRoute,
    isInitAuthRoute,
    setIsInitAuthRoute,
    getIsAuthRouteExist,
    getSelectedMenuKeyPath,
    onRouteSwitchWhenLoggedIn,
    onRouteSwitchWhenNotLoggedIn
  };
});

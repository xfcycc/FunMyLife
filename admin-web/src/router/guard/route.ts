import type { RouteLocationNormalized, RouteLocationRaw, Router } from 'vue-router';
import type { RouteKey } from '@elegant-router/types';
import { useRouteStore } from '@/store/modules/route';

/**
 * 创建路由守卫。
 *
 * FML 当前只保留 Life Manager 前台页面入口，不再进入 RuoYi 后台的登录、权限和远端菜单流程。
 *
 * @param router Vue Router 实例
 */
export function createRouteGuard(router: Router) {
  router.beforeEach(async (to, from) => {
    const location = await initRoute(to);

    if (location) {
      return location;
    }

    const rootRoute: RouteKey = 'root';

    // Life Manager 页面已经作为 constant route 注册，直接放行，不初始化后台用户与菜单。
    if (to.meta.constant) {
      return handleRouteSwitch(to, from);
    }

    // 后台管理路由暂不再作为产品入口暴露，任何受保护路由都回到 FML 首页。
    return { name: rootRoute };
  });
}

/**
 * 初始化前台常量路由。
 *
 * 首次进入页面时，Vue Router 只带有内置根路由和 404，需要先把 FML 页面路由注册进去；
 * 注册完成后再回到原目标地址，避免 `/life/home` 被临时识别成 404。
 *
 * @param to 即将进入的目标路由
 */
async function initRoute(to: RouteLocationNormalized): Promise<RouteLocationRaw | null> {
  const routeStore = useRouteStore();

  const notFoundRoute: RouteKey = 'not-found';
  const isNotFoundRoute = to.name === notFoundRoute;

  // 首次进入任意页面时，先补齐 FML 常量路由，再让路由重新解析一次当前地址。
  if (!routeStore.isInitConstantRoute) {
    await routeStore.initConstantRoute();

    const path = to.fullPath;
    const location: RouteLocationRaw = {
      path,
      replace: true,
      query: to.query,
      hash: to.hash
    };

    return location;
  }

  // FML 前台页面属于 constant route；无论本地是否残留 token，都不再触发后台用户和菜单接口。
  if (to.meta.constant && !isNotFoundRoute) {
    routeStore.onRouteSwitchWhenNotLoggedIn();
    return null;
  }

  // 不再初始化动态后台路由；未知路径或后台保护路径统一回到 FML 首页。
  const rootRoute: RouteKey = 'root';
  return { name: rootRoute };
}

function handleRouteSwitch(to: RouteLocationNormalized, from: RouteLocationNormalized) {
  // 少数外链路由仍沿用原有 href 处理方式，打开后留在当前页面。
  if (to.meta.href) {
    window.open(to.meta.href, '_blank');

    return { path: from.fullPath, replace: true, query: from.query, hash: to.hash };
  }
}

import type { CustomRoute, ElegantConstRoute, ElegantRoute } from '@elegant-router/types';
import { generatedRoutes } from '../elegant/routes';
import { layouts, views } from '../elegant/imports';
import { transformElegantRoutesToVueRoutes } from '../elegant/transform';

/**
 * custom routes
 *
 * @link https://github.com/soybeanjs/elegant-router?tab=readme-ov-file#custom-route
 */
const customRoutes: CustomRoute[] = [];

/** create routes when the auth route mode is static */
export function createStaticRoutes() {
  const constantRoutes: ElegantRoute[] = [];

  const authRoutes: ElegantRoute[] = [];

  [...customRoutes, ...generatedRoutes].forEach(item => {
    if (item.meta?.constant) {
      constantRoutes.push(item);
    } else {
      authRoutes.push(item);
    }
  });

  return {
    constantRoutes,
    authRoutes
  };
}

const dynamicConstantRoutes = [
  {
    name: 'home',
    path: '/life/home',
    component: 'layout.blank$view.home',
    meta: {
      title: 'Life Manager',
      icon: 'material-symbols:dashboard-customize-outline-rounded',
      order: 10,
      hideInMenu: true
    }
  },
  {
    name: 'life-legacy',
    path: '/life',
    redirect: '/life/home',
    meta: {
      title: 'Life Manager',
      hideInMenu: true
    }
  },
  {
    name: 'projects',
    path: '/life/projects',
    component: 'layout.blank$view.projects',
    meta: {
      title: '项目管理',
      icon: 'material-symbols:folder-outline-rounded',
      order: 11,
      hideInMenu: true
    }
  },
  {
    name: 'infinity-nikki',
    path: '/life/project/infinity-nikki',
    component: 'layout.blank$view.infinity-nikki',
    meta: {
      title: '无限暖暖',
      icon: 'material-symbols:stadia-controller-outline-rounded',
      order: 12,
      hideInMenu: true
    }
  },
  {
    name: 'infinity-nikki-manage',
    path: '/life/project/infinity-nikki/manage',
    component: 'layout.blank$view.infinity-nikki-manage',
    meta: {
      title: '无限暖暖管理',
      icon: 'material-symbols:settings-outline-rounded',
      hideInMenu: true,
      activeMenu: 'infinity-nikki'
    }
  },
  {
    name: 'japan-travel',
    path: '/life/project/japan-travel',
    component: 'layout.blank$view.japan-travel',
    meta: {
      title: '日本旅行 2026',
      icon: 'material-symbols:flight-takeoff-rounded',
      order: 13,
      hideInMenu: true
    }
  },
  {
    name: 'japan-travel-manage',
    path: '/life/project/japan-travel/manage',
    component: 'layout.blank$view.japan-travel-manage',
    meta: {
      title: '日本旅行管理',
      icon: 'material-symbols:travel-explore-rounded',
      hideInMenu: true,
      activeMenu: 'japan-travel'
    }
  },
  {
    name: '403',
    path: '/403',
    component: 'layout.blank$view.403',
    meta: {
      title: '403',
      i18nKey: 'route.403',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: '404',
    path: '/404',
    component: 'layout.blank$view.404',
    meta: {
      title: '404',
      i18nKey: 'route.404',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: '500',
    path: '/500',
    component: 'layout.blank$view.500',
    meta: {
      title: '500',
      i18nKey: 'route.500',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: 'login',
    path: '/login/:module(pwd-login|code-login|register|reset-pwd|bind-wechat)?',
    component: 'layout.blank$view.login',
    props: true,
    meta: {
      title: 'login',
      i18nKey: 'route.login',
      constant: true,
      hideInMenu: true
    }
  },
  {
    name: 'iframe-page',
    path: '/iframe-page/:url',
    component: 'layout.base$view.iframe-page',
    props: true,
    meta: {
      title: 'iframe-page',
      i18nKey: 'route.iframe-page',
      constant: true,
      hideInMenu: true,
      keepAlive: true,
      icon: 'material-symbols:iframe-outline'
    }
  },
  {
    name: 'social-callback',
    path: '/social-callback',
    component: 'layout.blank$view.social-callback',
    meta: {
      title: 'social-callback',
      i18nKey: 'route.social-callback',
      constant: true,
      hideInMenu: true,
      icon: 'simple-icons:authy'
    }
  },
  {
    name: 'user-center',
    path: '/user-center',
    component: 'layout.base$view.user-center',
    meta: {
      title: 'user-center',
      i18nKey: 'route.user-center',
      icon: 'material-symbols:account-circle-full',
      hideInMenu: true
    }
  }
] as unknown as ElegantRoute[];

/** create routes when the auth route mode is static */
export function createDynamicRoutes() {
  const constantRoutes: ElegantConstRoute[] = [];

  const authRoutes: ElegantConstRoute[] = [];

  [...customRoutes, ...dynamicConstantRoutes].forEach(item => {
    if (item.meta?.constant) {
      constantRoutes.push(item);
    } else {
      authRoutes.push(item);
    }
  });

  return {
    constantRoutes,
    authRoutes
  };
}

/**
 * Get auth vue routes
 *
 * @param routes Elegant routes
 */
export function getAuthVueRoutes(routes: ElegantConstRoute[]) {
  return transformElegantRoutesToVueRoutes(routes, layouts, views);
}

<script setup lang="ts">
import { computed } from 'vue';
import type { RouteKey } from '@elegant-router/types';
import { useRoute } from 'vue-router';
import { lifeUnavailableFeedback } from '@/hooks/business/lifeFeedback';
import { useRouterPush } from '@/hooks/common/router';

defineOptions({
  name: 'LifeAppShell'
});

const props = withDefaults(
  defineProps<{
    active?: string;
    showProjects?: boolean;
    avatar?: 'default' | 'nikki';
  }>(),
  {
    active: '首页',
    showProjects: false,
    avatar: 'default'
  }
);

interface NavItem {
  label: string;
  icon: string;
  routeKey?: RouteKey;
}

interface ProjectItem {
  label: string;
  icon: string;
  routeKey?: RouteKey;
}

const route = useRoute();
const { routerPushByKey } = useRouterPush();

const navItems = [
  { label: '首页', icon: 'material-symbols:home-outline-rounded', routeKey: 'home' },
  { label: '项目', icon: 'material-symbols:calendar-month-outline-rounded', routeKey: 'projects' },
  { label: '日程', icon: 'material-symbols:calendar-today-outline-rounded' },
  { label: '清单', icon: 'material-symbols:checklist-rounded' },
  { label: '待办清单', icon: 'material-symbols:edit-square-outline-rounded' },
  { label: '提醒/倒计时', icon: 'material-symbols:alarm-outline-rounded' },
  { label: '资产', icon: 'material-symbols:inventory-2-outline-rounded' },
  { label: '记录', icon: 'material-symbols:history-rounded' },
  { label: '笔记', icon: 'material-symbols:edit-note-outline-rounded' },
  { label: '图册', icon: 'material-symbols:photo-library-outline-rounded' },
  { label: '学习', icon: 'material-symbols:school-outline-rounded' },
  { label: '游戏', icon: 'material-symbols:stadia-controller-outline-rounded' },
  { label: '旅行', icon: 'material-symbols:flight-takeoff-rounded' },
  { label: 'AI 助手', icon: 'material-symbols:robot-2-outline-rounded' },
  { label: '集成中心', icon: 'material-symbols:hive-outline-rounded' },
  { label: '统计', icon: 'material-symbols:donut-large-outline-rounded' },
  { label: '设置', icon: 'material-symbols:settings-outline-rounded' }
] satisfies NavItem[];

const defaultNav = ['首页', '项目', '日程', '清单', '资产', '记录', '笔记', '图册', 'AI 助手', '设置'];
const nikkiNav = ['首页', '项目', '日程', '清单', '倒计时', '资产', '记录', '笔记', '图册', 'AI 助手', '设置'];
const manageNav = ['首页', '项目', '日程', '待办', '资产', '记录', '笔记', '图册', 'AI 助手', '设置'];

const visibleLabels = computed(() => {
  if (props.active === '无限暖暖') return nikkiNav;
  if (props.active === '管理') return manageNav;
  return defaultNav;
});

const visibleNavItems = computed(() => navItems.filter(item => visibleLabels.value.includes(item.label)));

const projectItems = [
  { label: '无限暖暖', icon: '🌸', routeKey: 'infinity-nikki' },
  { label: '日本旅行 2026', icon: '⛩️', routeKey: 'japan-travel' },
  { label: '原神', icon: '🎮' },
  { label: '家庭保险', icon: '🛡️' },
  { label: 'OpenAI 学习计划', icon: '🧠' }
] satisfies ProjectItem[];

function navigateByRouteKey(routeKey?: RouteKey) {
  if (!routeKey) return;
  routerPushByKey(routeKey);
}

function getUnavailableHint(label: string) {
  return lifeUnavailableFeedback.hint(label);
}

function isNavItemActive(item: NavItem) {
  const path = route.path;

  if (item.routeKey === 'home') return path === '/life/home';
  if (item.routeKey === 'projects') return path === '/life/projects' || path.startsWith('/life/project/');

  return false;
}

function isProjectItemActive(item: ProjectItem) {
  const path = route.path;

  if (item.routeKey === 'infinity-nikki') return path.startsWith('/life/project/infinity-nikki');
  if (item.routeKey === 'japan-travel') return path.startsWith('/life/project/japan-travel');

  return false;
}
</script>

<template>
  <main class="lm-page">
    <aside class="lm-sidebar">
      <div class="lm-brand">
        <div class="lm-logo"></div>
        <div>
          <strong>Life Manager</strong>
          <span>{{ active === '管理' ? '你的生活，由你掌控' : '新鲜生活，掌控节奏' }}</span>
        </div>
      </div>

      <nav class="lm-nav" aria-label="主导航">
        <button
          v-for="item in visibleNavItems"
          :key="item.label"
          class="lm-nav-item"
          :class="{ active: isNavItemActive(item) }"
          :disabled="!item.routeKey"
          :title="!item.routeKey ? getUnavailableHint(item.label) : undefined"
          :aria-label="!item.routeKey ? getUnavailableHint(item.label) : item.label"
          type="button"
          @click="navigateByRouteKey(item.routeKey)"
        >
          <SvgIcon :icon="item.icon" />
          <span>{{ item.label }}</span>
        </button>
      </nav>

      <section v-if="showProjects" class="lm-project-rail">
        <div class="lm-rail-title">
          <span>我的项目</span>
          <button type="button" :title="getUnavailableHint('新增项目')" aria-label="新增项目暂未接入，后续开放" disabled>+</button>
        </div>
        <button
          v-for="item in projectItems"
          :key="item.label"
          :class="{ active: isProjectItemActive(item) }"
          :disabled="!item.routeKey"
          :title="!item.routeKey ? getUnavailableHint(item.label) : undefined"
          :aria-label="!item.routeKey ? getUnavailableHint(item.label) : item.label"
          type="button"
          @click="navigateByRouteKey(item.routeKey)"
        >
          <span>{{ item.icon }}</span>
          {{ item.label }}
        </button>
      </section>

      <div class="lm-profile">
        <div class="lm-profile-card">
          <div class="lm-avatar" :class="avatar"></div>
          <div>
            <strong>夏目悠然 <span>Pro</span></strong>
            <p>专注生活的每一天 ✨</p>
          </div>
        </div>
      </div>
    </aside>

    <section class="lm-main">
      <slot />
    </section>
  </main>
</template>

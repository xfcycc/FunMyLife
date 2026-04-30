<template>
  <aside class="w-64 bg-slate-50/80 backdrop-blur-xl border-r border-slate-200 flex-col justify-between hidden md:flex shrink-0">
    <div>
      <div class="flex items-center space-x-2 px-6 py-8">
        <div class="w-8 h-8 bg-gradient-to-tr from-indigo-600 to-purple-400 rounded-lg shadow-lg flex items-center justify-center transform rotate-3">
          <span class="text-white font-bold text-lg transform -rotate-3">L</span>
        </div>
        <div>
          <h1 class="font-bold text-xl tracking-tight text-slate-800">Life Manager</h1>
          <p class="text-[10px] text-slate-400 mt-0.5">你的生活，由你掌控</p>
        </div>
      </div>

      <div class="px-3 overflow-y-auto" style="max-height: calc(100vh - 200px)">
        <template v-for="(group, groupIndex) in resolvedMenuGroups" :key="groupIndex">
          <div v-if="groupIndex > 0" class="my-4 border-t border-slate-200/60 mx-4"></div>
          <button
            v-for="item in group"
            :key="item.label"
            :class="[
              'w-full appearance-none border-0 flex items-center justify-between px-4 py-3 mb-1 rounded-xl transition-colors text-left',
              isItemActive(item)
                ? 'bg-indigo-500 text-white'
                : item.routeKey
                  ? 'bg-transparent text-slate-500 hover:bg-slate-100 hover:text-slate-800 cursor-pointer'
                  : 'bg-transparent text-slate-400 cursor-not-allowed opacity-60'
            ]"
            type="button"
            :disabled="!item.routeKey"
            @click="navigateByRouteKey(item.routeKey)"
          >
            <div class="flex items-center space-x-3">
              <component :is="item.icon" :size="18" :class="isItemActive(item) ? 'text-white' : 'text-slate-400'" />
              <span class="font-medium text-sm">{{ item.label }}</span>
            </div>
            <span v-if="item.badge" class="bg-rose-500 text-white text-[10px] font-bold px-1.5 py-0.5 rounded-full">
              {{ item.badge }}
            </span>
          </button>
        </template>
      </div>
    </div>

    <div class="p-4 mb-4">
      <div class="bg-white border border-slate-200 rounded-2xl p-3 flex items-center space-x-3 hover:bg-slate-50 transition-colors cursor-pointer">
        <img :src="avatarSrc" alt="User" class="w-10 h-10 rounded-full object-cover border-2 border-white shadow-sm" />
        <div class="flex-1 min-w-0">
          <div class="flex items-center space-x-1">
            <p class="text-sm font-bold text-slate-800 truncate">{{ userName }}</p>
            <span class="bg-indigo-100 text-indigo-600 text-[9px] font-bold px-1.5 py-0.5 rounded">{{ userBadge }}</span>
          </div>
          <p class="text-xs text-slate-400 truncate">{{ userDescription }}</p>
        </div>
      </div>
    </div>
  </aside>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import { BookOpen, Bot, CalendarDays, FolderKanban, Gamepad2, Home, ListTodo, PenSquare, Plane, Settings, Wallet } from 'lucide-vue-next';
import type { RouteKey } from '@elegant-router/types';
import { useRoute } from 'vue-router';
import { useRouterPush } from '@/hooks/common/router';
import type { LifeGeminiMenuItem } from './types';

defineOptions({
  name: 'LifeGeminiSidebar'
});

const props = withDefaults(
  defineProps<{
    menuGroups?: LifeGeminiMenuItem[][];
    userName?: string;
    userBadge?: string;
    userDescription?: string;
    avatarSrc?: string;
  }>(),
  {
    userName: '夏目悠然',
    userBadge: 'Pro',
    userDescription: '专注生活的每一天 ✨',
    avatarSrc: 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=64&h=64&q=80'
  }
);

const route = useRoute();
const { routerPushByKey } = useRouterPush();

const defaultMenuGroups: LifeGeminiMenuItem[][] = [
  [
    { icon: Home, label: '首页', routeKey: 'home' },
    { icon: FolderKanban, label: '项目', routeKey: 'projects' },
    { icon: CalendarDays, label: '日程', badge: '2' },
    { icon: ListTodo, label: '清单' },
    { icon: PenSquare, label: '记录' }
  ],
  [
    { icon: Wallet, label: '资产' },
    { icon: BookOpen, label: '学习' },
    { icon: Gamepad2, label: '游戏' },
    { icon: Plane, label: '旅行' }
  ],
  [
    { icon: Bot, label: 'AI 助手' },
    { icon: Settings, label: '设置' }
  ]
];

const resolvedMenuGroups = computed(() => props.menuGroups?.length ? props.menuGroups : defaultMenuGroups);

function navigateByRouteKey(routeKey?: RouteKey) {
  if (!routeKey) return;
  routerPushByKey(routeKey);
}

function isItemActive(item: LifeGeminiMenuItem) {
  if (item.active) return true;

  if (item.routeKey === 'home') return route.path === '/life/home';
  if (item.routeKey === 'projects') return route.path === '/life/projects' || route.path.startsWith('/life/project/');

  return false;
}
</script>

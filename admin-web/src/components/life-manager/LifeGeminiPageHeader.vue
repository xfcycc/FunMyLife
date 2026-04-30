<template>
  <header class="min-h-20 px-8 pt-5 pb-4 shrink-0">
    <div class="flex items-start justify-between gap-6">
      <div class="min-w-0">
        <nav v-if="breadcrumbs.length" class="flex items-center gap-2 text-xs text-slate-400 mb-2" aria-label="面包屑导航">
          <template v-for="(item, index) in breadcrumbs" :key="item.label">
            <button
              v-if="item.routeKey"
              class="appearance-none bg-transparent border-0 p-0 text-slate-500 hover:text-indigo-600 font-medium"
              type="button"
              @click="navigateByRouteKey(item.routeKey)"
            >
              {{ item.label }}
            </button>
            <span v-else class="text-slate-800 font-semibold">{{ item.label }}</span>
            <span v-if="index < breadcrumbs.length - 1" class="text-slate-300">/</span>
          </template>
        </nav>
        <h1 v-if="title" class="m-0 text-2xl font-extrabold tracking-tight text-slate-900">{{ title }}</h1>
        <p v-if="description" class="mt-1 text-sm text-slate-500">{{ description }}</p>
      </div>

      <div class="flex items-center justify-end gap-3 shrink-0">
        <slot name="actions">
          <LifeGeminiTopActions />
        </slot>
      </div>
    </div>
  </header>
</template>

<script setup lang="ts">
import type { RouteKey } from '@elegant-router/types';
import { useRouterPush } from '@/hooks/common/router';
import LifeGeminiTopActions from './LifeGeminiTopActions.vue';
import type { LifeGeminiBreadcrumbItem } from './types';

defineOptions({
  name: 'LifeGeminiPageHeader'
});

withDefaults(
  defineProps<{
    title?: string;
    description?: string;
    breadcrumbs?: LifeGeminiBreadcrumbItem[];
  }>(),
  {
    title: '',
    description: '',
    breadcrumbs: () => []
  }
);

const { routerPushByKey } = useRouterPush();

function navigateByRouteKey(routeKey: RouteKey) {
  routerPushByKey(routeKey);
}
</script>

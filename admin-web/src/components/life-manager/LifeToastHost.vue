<script setup lang="ts">
import type { LifeToastItem, LifeToastType } from './types';

defineOptions({
  name: 'LifeToastHost'
});

withDefaults(
  defineProps<{
    items: LifeToastItem[];
    placement?: 'top-right' | 'bottom-right';
  }>(),
  {
    placement: 'top-right'
  }
);

const emit = defineEmits<{
  close: [id: number | string];
}>();

const iconMap: Record<LifeToastType, string> = {
  success: 'material-symbols:check-circle-outline-rounded',
  warning: 'material-symbols:warning-outline-rounded',
  error: 'material-symbols:error-outline-rounded',
  info: 'material-symbols:info-outline-rounded'
};
</script>

<template>
  <Teleport to="body">
    <TransitionGroup name="life-toast" tag="div" class="life-toast-host" :class="`life-toast-host-${placement}`">
      <article v-for="item in items" :key="item.id" class="life-toast-card" :class="`life-toast-${item.type || 'info'}`">
        <span class="life-toast-icon">
          <SvgIcon :icon="iconMap[item.type || 'info']" />
        </span>
        <div>
          <strong>{{ item.title }}</strong>
          <p v-if="item.message">{{ item.message }}</p>
        </div>
        <button type="button" aria-label="关闭提示" @click="emit('close', item.id)">
          <SvgIcon icon="material-symbols:close-rounded" />
        </button>
      </article>
    </TransitionGroup>
  </Teleport>
</template>

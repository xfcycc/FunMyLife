<template>
  <div class="flex items-center space-x-4">
    <button
      class="w-10 h-10 rounded-full bg-white border border-slate-200 flex items-center justify-center text-slate-500 hover:bg-slate-50"
      type="button"
      :aria-label="searchLabel"
      @click="$emit('search')"
    >
      <Search :size="18" />
    </button>
    <button
      class="w-10 h-10 rounded-full bg-white border border-slate-200 flex items-center justify-center text-slate-500 hover:bg-slate-50 relative"
      type="button"
      :aria-label="notificationLabel"
      @click="$emit('notification')"
    >
      <Bell :size="18" />
      <span v-if="showNotificationDot" class="absolute top-2.5 right-2.5 w-2 h-2 bg-rose-500 rounded-full border-2 border-white"></span>
    </button>
    <button
      v-if="backText"
      class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-full hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5"
      type="button"
      @click="$emit('back')"
    >
      <span>{{ backText }}</span>
    </button>
    <button
      class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-full font-medium text-sm flex items-center space-x-1.5 transition-colors"
      type="button"
      @click="$emit('create')"
    >
      <Plus :size="16" />
      <span>{{ createText }}</span>
    </button>
  </div>
</template>

<script setup lang="ts">
import { Bell, Plus, Search } from 'lucide-vue-next';

defineOptions({
  name: 'LifeGeminiTopActions'
});

withDefaults(
  defineProps<{
    backText?: string;
    createText?: string;
    searchLabel?: string;
    notificationLabel?: string;
    showNotificationDot?: boolean;
  }>(),
  {
    backText: '',
    createText: '新建',
    searchLabel: '搜索',
    notificationLabel: '查看提醒',
    showNotificationDot: true
  }
);

defineEmits<{
  search: [];
  notification: [];
  back: [];
  create: [];
}>();
</script>

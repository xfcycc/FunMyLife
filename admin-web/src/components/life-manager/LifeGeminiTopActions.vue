<template>
  <header class="h-16 flex items-center justify-end px-8 shrink-0 relative z-10">
    <div class="flex items-center space-x-4">
      <button class="w-10 h-10 rounded-full bg-white border border-slate-200 flex items-center justify-center text-slate-500 hover:bg-slate-50" type="button">
        <Search :size="18" />
      </button>
      <button class="w-10 h-10 rounded-full bg-white border border-slate-200 flex items-center justify-center text-slate-500 hover:bg-slate-50 relative" type="button">
        <Bell :size="18" />
        <span class="absolute top-2.5 right-2.5 w-2 h-2 bg-rose-500 rounded-full border-2 border-white"></span>
      </button>
      <button class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-full font-medium text-sm flex items-center space-x-1.5 transition-colors" type="button">
        <Plus :size="16" />
        <span>新建</span>
      </button>
    </div>
  </header>
</template>

<script setup lang="ts">
import { Bell, Plus, Search } from 'lucide-vue-next';

defineOptions({
  name: 'LifeGeminiTopActions'
});
</script>

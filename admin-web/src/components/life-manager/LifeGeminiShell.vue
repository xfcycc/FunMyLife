<template>
  <div class="life-gemini-page flex h-screen bg-slate-50/50 font-sans text-slate-800 overflow-hidden">
    <LifeGeminiSidebar :menu-groups="menuGroups" />
    <main class="flex-1 flex flex-col h-screen overflow-hidden relative">
      <LifeGeminiTopActions />
      <div class="flex-1 overflow-y-auto px-8 pb-8 custom-scrollbar">
        <slot />
      </div>
    </main>
  </div>
</template>

<script setup lang="ts">
import LifeGeminiSidebar from './LifeGeminiSidebar.vue';
import LifeGeminiTopActions from './LifeGeminiTopActions.vue';
import type { LifeGeminiMenuItem } from './types';

defineOptions({
  name: 'LifeGeminiShell'
});

defineProps<{
  menuGroups?: LifeGeminiMenuItem[][];
}>();
</script>

<style scoped>
.life-gemini-page :deep(button) {
  box-shadow: none;
}

.life-gemini-page :deep(button:focus),
.life-gemini-page :deep(button:focus-visible) {
  outline: none;
  box-shadow: none;
}

.custom-scrollbar::-webkit-scrollbar {
  width: 6px;
}

.custom-scrollbar::-webkit-scrollbar-track {
  background: transparent;
}

.custom-scrollbar::-webkit-scrollbar-thumb {
  background-color: #e2e8f0;
  border-radius: 20px;
}
</style>

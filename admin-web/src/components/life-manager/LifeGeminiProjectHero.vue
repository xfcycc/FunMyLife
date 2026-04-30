<template>
  <section class="bg-white rounded-[2rem] p-4 flex flex-col xl:flex-row gap-6 shadow-[0_4px_20px_-4px_rgba(0,0,0,0.05)] border border-slate-100 mb-6">
    <div class="w-full xl:w-80 h-48 rounded-[1.5rem] overflow-hidden relative shrink-0">
      <img :src="coverSrc" :alt="coverAlt || title" class="w-full h-full object-cover" />
      <div class="absolute inset-0 bg-gradient-to-t from-black/30 to-transparent"></div>
    </div>

    <div class="flex-1 flex flex-col justify-between py-2">
      <div class="flex justify-between items-start gap-4">
        <div>
          <div class="flex items-center space-x-3 mb-2">
            <h2 class="text-3xl font-extrabold text-slate-800 tracking-tight">{{ title }}</h2>
            <slot name="title-icon" />
          </div>
          <div class="flex items-center space-x-2 mb-4">
            <span
              v-for="tag in tags"
              :key="tag.label"
              :class="[
                'px-2.5 py-1 rounded-md text-xs font-medium flex items-center',
                tag.tone === 'success' ? 'bg-emerald-50 text-emerald-600' : 'bg-slate-100 text-slate-600'
              ]"
            >
              <span v-if="tag.tone === 'success'" class="w-1.5 h-1.5 rounded-full bg-emerald-500 mr-1.5"></span>
              {{ tag.label }}
            </span>
          </div>
          <p class="text-slate-500 text-sm">{{ description }}</p>
        </div>

        <div class="flex flex-wrap justify-end gap-2">
          <slot name="actions" />
        </div>
      </div>

      <div class="flex items-center flex-wrap gap-x-10 gap-y-4 border-t border-slate-100 pt-5 mt-4">
        <div v-for="stat in stats" :key="stat.label">
          <p class="text-xs text-slate-400 mb-1">{{ stat.label }}</p>
          <p class="text-sm font-bold text-slate-800">{{ stat.value }}</p>
        </div>
      </div>
    </div>
  </section>
</template>

<script setup lang="ts">
import type { LifeGeminiProjectStat, LifeGeminiProjectTag } from './types';

defineOptions({
  name: 'LifeGeminiProjectHero'
});

defineProps<{
  title: string;
  description: string;
  coverSrc: string;
  coverAlt?: string;
  tags: LifeGeminiProjectTag[];
  stats: LifeGeminiProjectStat[];
}>();
</script>

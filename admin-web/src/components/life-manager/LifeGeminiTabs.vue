<template>
  <nav class="flex gap-14 border-b border-slate-200 mb-6 px-4 overflow-x-auto">
    <button
      v-for="tab in tabs"
      :key="tab"
      :class="[
        'appearance-none bg-transparent border-0 px-0 pb-3 text-lg font-extrabold transition-colors relative whitespace-nowrap',
        modelValue === tab ? 'text-indigo-600' : 'text-slate-500 hover:text-slate-800'
      ]"
      type="button"
      @click="$emit('update:modelValue', tab)"
    >
      {{ tab }}
      <span v-if="modelValue === tab" class="absolute bottom-0 left-0 right-0 h-1 bg-indigo-600 rounded-t-full"></span>
    </button>
  </nav>
</template>

<script setup lang="ts">
defineOptions({
  name: 'LifeGeminiTabs'
});

defineProps<{
  modelValue: string;
  tabs: string[];
}>();

defineEmits<{
  'update:modelValue': [value: string];
}>();
</script>

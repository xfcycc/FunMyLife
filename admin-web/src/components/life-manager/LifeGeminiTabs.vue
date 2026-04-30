<template>
  <nav class="flex space-x-8 border-b border-slate-200 mb-6 px-2">
    <button
      v-for="tab in tabs"
      :key="tab"
      :class="[
        'appearance-none bg-transparent border-0 px-0 pb-3 text-sm font-medium transition-colors relative',
        modelValue === tab ? 'text-indigo-600' : 'text-slate-500 hover:text-slate-800'
      ]"
      type="button"
      @click="$emit('update:modelValue', tab)"
    >
      {{ tab }}
      <span v-if="modelValue === tab" class="absolute bottom-0 left-0 right-0 h-0.5 bg-indigo-600 rounded-t-full"></span>
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

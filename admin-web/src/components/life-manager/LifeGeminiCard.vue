<template>
  <article class="bg-white rounded-3xl p-5 shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100">
    <div class="flex justify-between items-center mb-4">
      <h3 class="font-bold text-slate-800 text-base">
        <slot name="title">
          {{ title }}
        </slot>
      </h3>
      <slot name="action">
        <button
          v-if="actionText"
          class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium"
          type="button"
          @click="$emit('action')"
        >
          {{ actionText }}
        </button>
      </slot>
    </div>
    <slot />
  </article>
</template>

<script setup lang="ts">
defineOptions({
  name: 'LifeGeminiCard'
});

defineProps<{
  title?: string;
  actionText?: string;
}>();

defineEmits<{
  action: [];
}>();
</script>

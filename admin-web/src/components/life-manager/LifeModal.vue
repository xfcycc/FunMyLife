<script setup lang="ts">
defineOptions({
  name: 'LifeModal'
});

const props = withDefaults(
  defineProps<{
    show: boolean;
    title: string;
    description?: string;
    confirmText?: string;
    cancelText?: string;
    width?: number;
    tone?: 'default' | 'danger';
    closeOnBackdrop?: boolean;
    showFooter?: boolean;
  }>(),
  {
    description: '',
    confirmText: '确认',
    cancelText: '取消',
    width: 420,
    tone: 'default',
    closeOnBackdrop: true,
    showFooter: true
  }
);

const emit = defineEmits<{
  'update:show': [value: boolean];
  confirm: [];
  cancel: [];
  close: [];
}>();

function close() {
  emit('update:show', false);
  emit('close');
}

function cancel() {
  emit('cancel');
  close();
}

function confirm() {
  emit('confirm');
}

function handleBackdrop() {
  if (props.closeOnBackdrop) close();
}
</script>

<template>
  <Teleport to="body">
    <Transition name="life-modal-fade">
      <div v-if="show" class="life-modal-layer" role="presentation" @click.self="handleBackdrop">
        <section
          class="life-modal"
          :class="`life-modal-${tone}`"
          role="dialog"
          aria-modal="true"
          :aria-label="title"
          :style="{ width: `${width}px` }"
        >
          <header class="life-modal-head">
            <div>
              <h2>{{ title }}</h2>
              <p v-if="description">{{ description }}</p>
            </div>
            <button class="life-modal-close" type="button" aria-label="关闭" @click="close">
              <SvgIcon icon="material-symbols:close-rounded" />
            </button>
          </header>

          <div class="life-modal-body">
            <slot />
          </div>

          <footer v-if="showFooter" class="life-modal-footer">
            <slot name="footer">
              <button class="lm-plain-btn" type="button" @click="cancel">{{ cancelText }}</button>
              <button class="lm-purple-btn" type="button" @click="confirm">{{ confirmText }}</button>
            </slot>
          </footer>
        </section>
      </div>
    </Transition>
  </Teleport>
</template>

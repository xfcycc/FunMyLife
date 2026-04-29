import { ref } from 'vue';
import type { LifeToastItem, LifeToastType } from '@/components/life-manager/types';

export interface LifeToastOptions {
  type?: LifeToastType;
  title: string;
  message?: string;
  duration?: number;
}

let seed = 0;

export function useLifeToast() {
  const toasts = ref<LifeToastItem[]>([]);

  function removeToast(id: LifeToastItem['id']) {
    toasts.value = toasts.value.filter(item => item.id !== id);
  }

  function showToast(options: LifeToastOptions) {
    const id = ++seed;

    toasts.value.push({
      id,
      type: options.type || 'info',
      title: options.title,
      message: options.message
    });

    window.setTimeout(() => removeToast(id), options.duration ?? 3200);

    return id;
  }

  return {
    toasts,
    removeToast,
    showToast,
    success: (title: string, message?: string) => showToast({ type: 'success', title, message }),
    warning: (title: string, message?: string) => showToast({ type: 'warning', title, message }),
    error: (title: string, message?: string) => showToast({ type: 'error', title, message }),
    info: (title: string, message?: string) => showToast({ type: 'info', title, message })
  };
}

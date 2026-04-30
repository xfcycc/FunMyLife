import type { Component } from 'vue';

export type LifeToastType = 'success' | 'warning' | 'error' | 'info';

export interface LifeToastItem {
  id: number | string;
  type?: LifeToastType;
  title: string;
  message?: string;
}

export interface LifeGeminiMenuItem {
  icon: Component;
  label: string;
  active?: boolean;
  badge?: number | string;
}

export interface LifeGeminiProjectTag {
  label: string;
  tone?: 'default' | 'success';
}

export interface LifeGeminiProjectStat {
  label: string;
  value: number | string;
}

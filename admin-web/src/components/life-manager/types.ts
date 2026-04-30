import type { Component } from 'vue';
import type { RouteKey } from '@elegant-router/types';

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
  routeKey?: RouteKey;
  active?: boolean;
  badge?: number | string;
}

export interface LifeGeminiBreadcrumbItem {
  label: string;
  routeKey?: RouteKey;
}

export interface LifeGeminiProjectTag {
  label: string;
  tone?: 'default' | 'success';
}

export interface LifeGeminiProjectStat {
  label: string;
  value: number | string;
}

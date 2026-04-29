export type LifeToastType = 'success' | 'warning' | 'error' | 'info';

export interface LifeToastItem {
  id: number | string;
  type?: LifeToastType;
  title: string;
  message?: string;
}

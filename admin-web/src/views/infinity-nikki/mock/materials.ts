import type { MaterialOverview } from '../types';

export const mockMaterialOverview: MaterialOverview = {
  total: 4,
  completed: 1,
  materials: [
    {
      id: 'mat-outfit-garden',
      projectId: 'nikki-001',
      name: '晨曦花园套装',
      type: 'outfit',
      status: 'collecting',
      current: 7,
      target: 10,
      versionId: 'ver-5-1',
      targetId: 'custom-photo-plan',
      note: '本周拍照计划的主套装'
    },
    {
      id: 'mat-flower-petal',
      projectId: 'nikki-001',
      name: '繁花花瓣',
      type: 'material',
      status: 'collecting',
      current: 38,
      target: 100,
      versionId: 'ver-5-1',
      activityId: 'evt2',
      targetId: 'e2'
    },
    {
      id: 'mat-dream-token',
      projectId: 'nikki-001',
      name: '心愿梦境代币',
      type: 'currency',
      status: 'planning',
      current: 120,
      target: 300,
      versionId: 'ver-5-1',
      note: '优先换限定染色材料'
    },
    {
      id: 'mat-5-0-star',
      projectId: 'nikki-001',
      name: '5.0 星愿收集',
      type: 'collection',
      status: 'completed',
      current: 42,
      target: 42,
      versionId: 'ver-5-0'
    }
  ]
};

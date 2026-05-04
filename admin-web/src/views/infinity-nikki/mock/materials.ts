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
      description: '5.1版本新增五星套装',
      status: 'collecting',
      current: 7,
      target: 10,
      versionId: 'ver-5-1',
      targetId: 'custom-photo-plan',
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      note: '本周拍照计划的主套装',
      createdAt: '2026-04-20T08:00:00',
      updatedAt: '2026-04-29T10:00:00'
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
      targetId: 'e2',
      timelineRule: { mode: 'daily_summary', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-20T08:00:00',
      updatedAt: '2026-04-29T10:00:00'
    },
    {
      id: 'mat-dream-token',
      projectId: 'nikki-001',
      name: '心愿梦境代币',
      type: 'currency',
      status: 'collecting',
      current: 120,
      target: 300,
      versionId: 'ver-5-1',
      timelineRule: { mode: 'daily_summary', displayInOverview: true, aiReadable: true },
      note: '优先换限定染色材料',
      createdAt: '2026-04-20T08:00:00',
      updatedAt: '2026-04-29T10:00:00'
    },
    {
      id: 'mat-5-0-star',
      projectId: 'nikki-001',
      name: '5.0 星愿收集',
      type: 'collection',
      status: 'completed',
      current: 42,
      target: 42,
      versionId: 'ver-5-0',
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      createdAt: '2026-03-01T08:00:00',
      updatedAt: '2026-04-15T10:00:00'
    }
  ]
};

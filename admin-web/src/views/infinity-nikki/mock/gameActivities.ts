import type { GameActivity } from '../types';

export const mockGameActivities: GameActivity[] = [
  {
    id: 'evt1',
    projectId: 'nikki-001',
    versionId: 'ver-5-1',
    title: '奇迹之旅·第二章',
    description: '完成奇迹之旅第二章全部关卡，获取限定套装奖励。',
    startAt: '2026-04-24T05:00:00',
    endAt: '2026-05-02T05:00:00',
    status: 'ending',
    priority: 'high',
    cover: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=100&h=100&q=80',
    targetIds: ['e1'],
    photoIds: ['p3'],
    noteIds: ['n1'],
    reminderRule: {
      enabled: true,
      channels: ['app', 'feishu'],
      beforeMinutes: [4320, 1440, 120]
    }
  },
  {
    id: 'evt2',
    projectId: 'nikki-001',
    versionId: 'ver-5-1',
    title: '繁花季节 限时活动',
    description: '春季限定活动，收集花瓣兑换专属奖励。',
    startAt: '2026-04-28T05:00:00',
    endAt: '2026-05-12T05:00:00',
    status: 'active',
    priority: 'high',
    cover: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=100&h=100&q=80',
    targetIds: ['e2', 'e3'],
    materialIds: ['mat-flower-petal'],
    photoIds: ['p4'],
    noteIds: ['n4'],
    reminderRule: {
      enabled: true,
      channels: ['app'],
      beforeMinutes: [4320, 1440]
    }
  },
  {
    id: 'evt3',
    projectId: 'nikki-001',
    versionId: 'ver-5-1',
    title: '累计充值返利',
    description: '累计充值达到指定金额可领取返利奖励。',
    startAt: '2026-04-18T05:00:00',
    endAt: '2026-05-20T05:00:00',
    status: 'active',
    priority: 'normal',
    cover: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=100&h=100&q=80',
    targetIds: [],
    reminderRule: {
      enabled: false,
      channels: ['app'],
      beforeMinutes: [1440]
    }
  }
];

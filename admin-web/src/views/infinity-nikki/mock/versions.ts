import type { GameVersion } from '../types';

export const mockGameVersions: GameVersion[] = [
  {
    id: 'ver-5-1',
    projectId: 'nikki-001',
    name: '5.1',
    title: '5.1 繁花季节',
    startAt: '2026-04-18T05:00:00',
    endAt: '2026-05-20T05:00:00',
    status: 'active',
    highlights: ['繁花季节限时活动', '奇迹之旅第二章', '晨曦花园套装'],
    activityIds: ['evt1', 'evt2', 'evt3']
  },
  {
    id: 'ver-5-2',
    projectId: 'nikki-001',
    name: '5.2',
    title: '5.2 心愿巡礼',
    startAt: '2026-05-20T05:00:00',
    endAt: '2026-06-26T05:00:00',
    status: 'upcoming',
    highlights: ['前瞻特别节目', '新区域预告', '搭配主题更新'],
    activityIds: []
  },
  {
    id: 'ver-5-0',
    projectId: 'nikki-001',
    name: '5.0',
    title: '5.0 星愿启程',
    startAt: '2026-03-12T05:00:00',
    endAt: '2026-04-18T05:00:00',
    status: 'archived',
    highlights: ['主线剧情更新', '星愿套装收集', '心愿原野探索'],
    activityIds: [],
    summary: '完成主线剧情与心愿原野探索，保留 42 张截图和 3 篇攻略笔记。',
    archivedAt: '2026-04-18T22:30:00'
  }
];

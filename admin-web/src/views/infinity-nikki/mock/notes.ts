import type { NoteOverview } from '../types';

export const mockNoteOverview: NoteOverview = {
  total: 12,
  notes: [
    {
      id: 'n1',
      projectId: 'nikki-001',
      title: '5.1版本新增套装分析与抽取建议',
      content: '5.1版本新增了3套五星套装和5套四星套装，其中「晨曦花园」套装性价比最高...',
      type: 'guide',
      category: 'strategy',
      categoryLabel: '攻略',
      versionId: 'ver-5-1',
      pinnedToOverview: true,
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-29T10:00:00',
      updatedAt: '2026-04-29T10:00:00',
      relativeTime: '2小时前'
    },
    {
      id: 'n2',
      projectId: 'nikki-001',
      title: '心愿原野探索路线规划',
      content: '心愿原野共有5个区域，推荐从北向南探索，每个区域有3-5个奇想星...',
      type: 'note',
      category: 'explore',
      categoryLabel: '探索',
      versionId: 'ver-5-1',
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-28T14:00:00',
      updatedAt: '2026-04-28T14:00:00',
      relativeTime: '昨天'
    },
    {
      id: 'n3',
      projectId: 'nikki-001',
      title: '搭配赛主题：春日茶会灵感整理',
      content: '本周搭配赛主题为「春日茶会」，推荐使用浅色系搭配，配饰选择花朵元素...',
      type: 'note',
      category: 'coordination',
      categoryLabel: '搭配',
      activityId: 'evt2',
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-27T09:00:00',
      updatedAt: '2026-04-27T09:00:00',
      relativeTime: '2天前'
    },
    {
      id: 'n4',
      projectId: 'nikki-001',
      title: '素材收集清单汇总',
      content: '当前版本需要收集的主要素材：晨露花瓣x100、星光碎片x50、梦境之羽x30...',
      type: 'note',
      category: 'material',
      categoryLabel: '素材',
      versionId: 'ver-5-1',
      timelineRule: { mode: 'daily_summary', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-26T16:00:00',
      updatedAt: '2026-04-26T16:00:00',
      relativeTime: '3天前'
    },
    {
      id: 'n5',
      projectId: 'nikki-001',
      title: '5.0版本剧情回顾与彩蛋整理',
      content: '5.0版本主线剧情中隐藏了多个彩蛋，包括与前作联动的场景...',
      type: 'review',
      category: 'story',
      categoryLabel: '剧情',
      versionId: 'ver-5-0',
      timelineRule: { mode: 'detail', displayInOverview: true, aiReadable: true },
      createdAt: '2026-04-20T11:00:00',
      updatedAt: '2026-04-25T20:00:00',
      relativeTime: '5天前'
    }
  ]
};

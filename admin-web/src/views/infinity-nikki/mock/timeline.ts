import type { TimelineEvent } from '../types';

export const mockTimelineEvents: TimelineEvent[] = [
  {
    id: 'tl1',
    projectId: 'nikki-001',
    occurredAt: '2026-04-30T09:00:00',
    type: 'target_done',
    title: '完成了每日目标',
    description: '搭配竞技场挑战',
    sourceBlockKey: 'targets',
    targetId: 't3',
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  },
  {
    id: 'tl2',
    projectId: 'nikki-001',
    occurredAt: '2026-04-30T08:00:00',
    type: 'note_created',
    title: '新增攻略笔记',
    description: '5.1 版本新增套装分析',
    sourceBlockKey: 'timeline',
    versionId: 'ver-5-1',
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  },
  {
    id: 'tl3',
    projectId: 'nikki-001',
    occurredAt: '2026-04-29T21:40:00',
    type: 'photo_uploaded',
    title: '上传了 6 张截图',
    description: '心愿原野探索记录',
    sourceBlockKey: 'gallery',
    versionId: 'ver-5-1',
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  },
  {
    id: 'tl4',
    projectId: 'nikki-001',
    occurredAt: '2026-04-29T18:30:00',
    type: 'activity_started',
    title: '活动进度更新',
    description: '奇迹之旅·第二章进入最后阶段',
    sourceBlockKey: 'version_activity',
    versionId: 'ver-5-1',
    activityId: 'evt1',
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  },
  {
    id: 'tl5',
    projectId: 'nikki-001',
    occurredAt: '2026-04-18T22:30:00',
    type: 'version_archived',
    title: '归档 5.0 星愿启程',
    description: '保留 42 张截图、3 篇笔记和 1 份版本总结',
    sourceBlockKey: 'timeline',
    versionId: 'ver-5-0',
    sensitivity: 'normal',
    displayInOverview: false,
    aiReadable: true
  }
];

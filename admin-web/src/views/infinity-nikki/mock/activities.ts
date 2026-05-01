import type { NikkiActivity } from '../types';

export const mockActivities: NikkiActivity[] = [
  {
    id: 'act1',
    type: 'task_complete',
    title: '完成了每日任务',
    description: '搭配竞技场挑战',
    icon: 'CheckCircle2',
    iconColor: 'text-emerald-500',
    bg: 'bg-emerald-50',
    time: '1 小时前',
    createdAt: '2026-04-30T09:00:00'
  },
  {
    id: 'act2',
    type: 'note_added',
    title: '新增笔记',
    description: '5.1版本新增套装分析',
    icon: 'PenSquare',
    iconColor: 'text-blue-500',
    bg: 'bg-blue-50',
    time: '2 小时前',
    createdAt: '2026-04-30T08:00:00'
  },
  {
    id: 'act3',
    type: 'screenshot_uploaded',
    title: '上传了 6 张截图',
    description: '心愿原野探索记录',
    icon: 'Camera',
    iconColor: 'text-rose-500',
    bg: 'bg-rose-50',
    time: '昨天 21:40',
    createdAt: '2026-04-29T21:40:00'
  },
  {
    id: 'act4',
    type: 'event_progress',
    title: '活动进度更新',
    description: '奇迹之旅·第二章',
    icon: 'Map',
    iconColor: 'text-purple-500',
    bg: 'bg-purple-50',
    time: '昨天 18:30',
    createdAt: '2026-04-29T18:30:00'
  },
  {
    id: 'act5',
    type: 'version_announcement',
    title: '版本公告',
    description: '5.2版本前瞻公告',
    icon: 'Star',
    iconColor: 'text-amber-500',
    bg: 'bg-amber-50',
    time: '04-27 12:00',
    createdAt: '2026-04-27T12:00:00'
  }
];

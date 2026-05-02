import type { GameTarget } from '../types';

const dailyTimelineRule = {
  mode: 'daily_summary',
  displayInOverview: true,
  aiReadable: true
} as const;

const detailTimelineRule = {
  mode: 'detail',
  displayInOverview: true,
  aiReadable: true
} as const;

export const mockGameTargets: GameTarget[] = [
  {
    id: 't1',
    projectId: 'nikki-001',
    type: 'daily',
    title: '每日签到',
    status: 'done',
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't2',
    projectId: 'nikki-001',
    type: 'daily',
    title: '心愿原野探索',
    status: 'done',
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't3',
    projectId: 'nikki-001',
    type: 'daily',
    title: '搭配竞技场挑战',
    status: 'done',
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't4',
    projectId: 'nikki-001',
    type: 'daily',
    title: '每日灵感任务',
    status: 'done',
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't5',
    projectId: 'nikki-001',
    type: 'daily',
    title: '采集素材',
    status: 'todo',
    progressCurrent: 25,
    progressTarget: 50,
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't6',
    projectId: 'nikki-001',
    type: 'daily',
    title: '捕捉奇想星',
    status: 'todo',
    progressCurrent: 0,
    progressTarget: 3,
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'normal',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't7',
    projectId: 'nikki-001',
    type: 'daily',
    title: '愿望梦境挑战',
    status: 'todo',
    progressCurrent: 0,
    progressTarget: 2,
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'high',
    timelineRule: dailyTimelineRule
  },
  {
    id: 't8',
    projectId: 'nikki-001',
    type: 'daily',
    title: '体力消耗',
    status: 'todo',
    progressCurrent: 120,
    progressTarget: 180,
    resetRule: { type: 'daily', time: '04:00' },
    priority: 'high',
    timelineRule: dailyTimelineRule
  },
  {
    id: 'w1',
    projectId: 'nikki-001',
    type: 'weekly',
    title: '完成搭配评选 3次',
    status: 'done',
    progressCurrent: 3,
    progressTarget: 3,
    resetRule: { type: 'weekly', weekday: 1, time: '04:00' },
    priority: 'normal',
    timelineRule: { mode: 'weekly_summary', displayInOverview: true, aiReadable: true }
  },
  {
    id: 'w2',
    projectId: 'nikki-001',
    type: 'weekly',
    title: '通关心愿梦境 10次',
    status: 'todo',
    progressCurrent: 6,
    progressTarget: 10,
    resetRule: { type: 'weekly', weekday: 1, time: '04:00' },
    priority: 'normal',
    timelineRule: { mode: 'weekly_summary', displayInOverview: true, aiReadable: true }
  },
  {
    id: 'w5',
    projectId: 'nikki-001',
    type: 'weekly',
    title: '参与搭配赛 2次',
    status: 'todo',
    progressCurrent: 1,
    progressTarget: 2,
    resetRule: { type: 'weekly', weekday: 1, time: '04:00' },
    priority: 'high',
    timelineRule: { mode: 'exception_only', displayInOverview: true, aiReadable: true }
  },
  {
    id: 'e1',
    projectId: 'nikki-001',
    type: 'activity',
    title: '奇迹之旅第二章·完成5个关卡',
    status: 'done',
    progressCurrent: 5,
    progressTarget: 5,
    versionId: 'ver-5-1',
    activityId: 'evt1',
    resetRule: { type: 'activity', inheritFromActivity: true },
    priority: 'high',
    timelineRule: detailTimelineRule
  },
  {
    id: 'e2',
    projectId: 'nikki-001',
    type: 'activity',
    title: '繁花季节·收集花瓣',
    status: 'todo',
    progressCurrent: 38,
    progressTarget: 100,
    versionId: 'ver-5-1',
    activityId: 'evt2',
    resetRule: { type: 'activity', inheritFromActivity: true },
    priority: 'high',
    timelineRule: detailTimelineRule
  },
  {
    id: 'e3',
    projectId: 'nikki-001',
    type: 'activity',
    title: '繁花季节·完成挑战',
    status: 'todo',
    progressCurrent: 2,
    progressTarget: 5,
    versionId: 'ver-5-1',
    activityId: 'evt2',
    resetRule: { type: 'activity', inheritFromActivity: true },
    priority: 'high',
    timelineRule: detailTimelineRule
  },
  {
    id: 'custom-photo-plan',
    projectId: 'nikki-001',
    type: 'custom',
    title: '本周拍照计划：晨曦花园套装',
    status: 'todo',
    progressCurrent: 1,
    progressTarget: 3,
    versionId: 'ver-5-1',
    priority: 'normal',
    pinnedToOverview: true,
    timelineRule: detailTimelineRule
  }
];

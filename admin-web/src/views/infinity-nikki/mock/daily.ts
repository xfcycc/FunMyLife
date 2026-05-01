import type { DailyTaskOverview } from '../types';

export const mockDailyTaskOverview: DailyTaskOverview = {
  completed: 5,
  total: 8,
  taskGroups: [
    {
      type: 'daily',
      label: '今日任务',
      tasks: [
        { id: 't1', text: '每日签到', done: true, group: 'daily' },
        { id: 't2', text: '心愿原野探索', done: true, group: 'daily' },
        { id: 't3', text: '搭配竞技场挑战', done: true, group: 'daily' },
        { id: 't4', text: '每日灵感任务', done: true, group: 'daily' },
        { id: 't5', text: '采集素材', done: false, detail: '25/50', group: 'daily' },
        { id: 't6', text: '捕捉奇想星', done: false, detail: '0/3', group: 'daily' },
        { id: 't7', text: '愿望梦境挑战', done: false, detail: '0/2', group: 'daily' },
        { id: 't8', text: '体力消耗', done: false, detail: '120/180', group: 'daily' }
      ]
    },
    {
      type: 'weekly',
      label: '周常任务',
      tasks: [
        { id: 'w1', text: '完成搭配评选 3次', done: true, detail: '3/3', group: 'weekly' },
        { id: 'w2', text: '通关心愿梦境 10次', done: true, detail: '10/10', group: 'weekly' },
        { id: 'w3', text: '收集奇想星 80个', done: true, detail: '80/80', group: 'weekly' },
        { id: 'w4', text: '完成灵感任务 14次', done: true, detail: '14/14', group: 'weekly' },
        { id: 'w5', text: '参与搭配赛 2次', done: false, detail: '1/2', group: 'weekly' },
        { id: 'w6', text: '提升搭配师等级', done: false, detail: '38/50', group: 'weekly' },
        { id: 'w7', text: '收集套装部件 30个', done: false, detail: '22/30', group: 'weekly' }
      ]
    },
    {
      type: 'event',
      label: '活动任务',
      tasks: [
        { id: 'e1', text: '奇迹之旅第二章·完成5个关卡', done: true, detail: '5/5', group: 'event' },
        { id: 'e2', text: '繁花季节·收集花瓣', done: false, detail: '38/100', group: 'event' },
        { id: 'e3', text: '繁花季节·完成挑战', done: false, detail: '2/5', group: 'event' }
      ]
    }
  ],
  weeklyGoals: [
    { id: 'g1', text: '完成搭配评选 3次', current: 3, max: 3, progress: 100, status: 'completed', color: 'bg-indigo-500' },
    { id: 'g2', text: '通关心愿梦境 10次', current: 6, max: 10, progress: 60, status: 'on_track', color: 'bg-indigo-400' },
    { id: 'g3', text: '收集奇想星 80个', current: 52, max: 80, progress: 65, status: 'on_track', color: 'bg-indigo-400' },
    { id: 'g4', text: '完成灵感任务 14次', current: 14, max: 14, progress: 100, status: 'completed', color: 'bg-indigo-500' },
    { id: 'g5', text: '参与搭配赛 2次', current: 1, max: 2, progress: 50, status: 'needs_attention', color: 'bg-rose-400' },
    { id: 'g6', text: '提升搭配师等级', current: 38, max: 50, progress: 76, status: 'needs_attention', color: 'bg-rose-400' },
    { id: 'g7', text: '收集套装部件 30个', current: 22, max: 30, progress: 73, status: 'needs_attention', color: 'bg-rose-400' }
  ]
};

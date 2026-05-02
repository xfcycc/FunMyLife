import type { AiOverview } from '../types';

export const mockAiOverview: AiOverview = {
  currentSuggestion: {
    id: 'ai1',
    content: '根据你当前的任务进度和活动状态，AI 为你生成了以下建议：',
    highlights: [
      { text: '你今天还有 3个任务目标未完成', type: 'danger' },
      { text: '活动「奇迹之旅·第二章」还有 3天结束，别错过奖励', type: 'info' },
      { text: '根据你的搭配风格，推荐优先收集「晨曦花园」套装', type: 'success' },
      { text: '本周素材收集进度偏慢，建议安排集中采集时间', type: 'warning' }
    ],
    generatedAt: '2026-04-30T08:00:00',
    status: 'generated'
  },
  suggestionCount: 5
};

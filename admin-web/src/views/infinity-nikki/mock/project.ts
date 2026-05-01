import type { NikkiProject } from '../types';

export const mockProject: NikkiProject = {
  id: 'nikki-001',
  name: '无限暖暖',
  description: '收集美好的瞬间，搭配无限的可能',
  coverSrc: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&q=80&w=800&h=400',
  coverAlt: 'Infinity Nikki',
  status: 'active',
  createdAt: '2024-12-15',
  tags: [
    { label: '游戏' },
    { label: '进行中', tone: 'success' }
  ],
  stats: [
    { label: '创建时间', value: '2024-12-15' },
    { label: '今日任务进度', value: '5/8' },
    { label: '本周完成率', value: '62%' },
    { label: '项目笔记', value: '12' },
    { label: '图册数量', value: '8' }
  ]
};

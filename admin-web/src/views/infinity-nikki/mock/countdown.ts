import type { CountdownOverview } from '../types';

export const mockCountdownOverview: CountdownOverview = {
  events: [
    {
      id: 'evt1',
      title: '奇迹之旅·第二章',
      description: '完成奇迹之旅第二章全部关卡，获取限定套装奖励',
      endTime: '2026-05-02 05:00',
      remainingDays: 3,
      img: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=100&h=100&q=80',
      reminded: true,
      checkedIn: false
    },
    {
      id: 'evt2',
      title: '繁花季节 限时活动',
      description: '春季限定活动，收集花瓣兑换专属奖励',
      endTime: '2026-05-12 05:00',
      remainingDays: 13,
      img: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=100&h=100&q=80',
      reminded: false,
      checkedIn: false
    },
    {
      id: 'evt3',
      title: '累计充值返利',
      description: '累计充值达到指定金额可领取返利奖励',
      endTime: '2026-05-20 05:00',
      remainingDays: 21,
      img: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=100&h=100&q=80',
      reminded: false,
      checkedIn: false
    }
  ],
  versionNodes: [
    {
      id: 'vn1',
      type: 'live',
      title: '5.2版本前瞻特别节目',
      startTime: '2026-04-30 19:30',
      daysUntil: 2,
      reminded: false,
      checkedIn: false
    },
    {
      id: 'vn2',
      type: 'maintenance',
      title: '5.2版本更新维护',
      startTime: '2026-05-02 06:00',
      endTime: '2026-05-02 12:00',
      daysUntil: 3,
      reminded: false,
      checkedIn: false
    }
  ]
};

import type { AbilityInstanceConfig } from '../types';

export const mockAbilityInstanceConfigs: AbilityInstanceConfig[] = [
  {
    id: 'cfg-overview',
    projectId: 'nikki-001',
    blockKey: 'overview',
    displayName: '概览',
    enabled: true,
    navigation: { visible: true, order: 1 },
    summaryRules: [
      {
        id: 'sum-current-version',
        projectId: 'nikki-001',
        source: 'version',
        enabled: true,
        title: '当前版本',
        maxItems: 1,
        priority: 1,
        filters: {},
        displayMode: 'metric'
      },
      {
        id: 'sum-today-targets',
        projectId: 'nikki-001',
        source: 'targets',
        enabled: true,
        title: '今日目标',
        maxItems: 3,
        priority: 2,
        filters: { targetTypes: ['daily'] },
        displayMode: 'metric'
      },
      {
        id: 'sum-weekly-targets',
        projectId: 'nikki-001',
        source: 'targets',
        enabled: true,
        title: '本周目标',
        maxItems: 3,
        priority: 3,
        filters: { targetTypes: ['weekly'] },
        displayMode: 'compact'
      },
      {
        id: 'sum-ending-activities',
        projectId: 'nikki-001',
        source: 'activities',
        enabled: true,
        title: '即将结束活动',
        maxItems: 2,
        priority: 4,
        filters: { activityStatuses: ['ending'], withinHours: 72 },
        displayMode: 'list'
      },
      {
        id: 'sum-recent-timeline',
        projectId: 'nikki-001',
        source: 'timeline',
        enabled: true,
        title: '最近时间轴',
        maxItems: 3,
        priority: 5,
        filters: {},
        displayMode: 'timeline'
      }
    ]
  },
  {
    id: 'cfg-targets',
    projectId: 'nikki-001',
    blockKey: 'targets',
    displayName: '任务',
    enabled: true,
    navigation: { visible: true, order: 2 },
    summaryRules: [],
    behavior: {
      resetRules: [
        { type: 'daily', time: '04:00' },
        { type: 'weekly', weekday: 1, time: '04:00' },
        { type: 'activity', inheritFromActivity: true }
      ],
      archiveRule: {
        type: 'on_version_end',
        includeTargets: true,
        includePhotos: false,
        includeNotes: false,
        generateTimelineSummary: true
      }
    },
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'daily_summary', displayInOverview: true, aiReadable: true }
    }
  },
  {
    id: 'cfg-version-activity',
    projectId: 'nikki-001',
    blockKey: 'version_activity',
    displayName: '活动与版本',
    enabled: true,
    navigation: { visible: true, order: 3 },
    summaryRules: [],
    behavior: {
      reminderRules: [
        { enabled: true, channels: ['app', 'feishu'], beforeMinutes: [4320, 1440, 120] }
      ],
      archiveRule: {
        type: 'on_activity_end',
        includeTargets: true,
        includePhotos: true,
        includeNotes: true,
        generateTimelineSummary: true
      }
    },
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
    }
  },
  {
    id: 'cfg-timeline',
    projectId: 'nikki-001',
    blockKey: 'timeline',
    displayName: '时间轴',
    enabled: true,
    navigation: { visible: true, order: 7 },
    summaryRules: [],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
    }
  }
];

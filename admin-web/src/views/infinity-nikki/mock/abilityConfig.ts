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
        filters: { activityStatuses: ['ending', 'pending_archive'], withinHours: 72 },
        displayMode: 'list'
      },
      {
        id: 'sum-recent-timeline',
        projectId: 'nikki-001',
        source: 'timeline',
        enabled: true,
        title: '最近时间轴',
        maxItems: 3,
        priority: 8,
        filters: {},
        displayMode: 'timeline'
      },
      {
        id: 'sum-material-progress',
        projectId: 'nikki-001',
        source: 'materials',
        enabled: true,
        title: '素材收集',
        maxItems: 3,
        priority: 5,
        filters: {},
        displayMode: 'compact'
      },
      {
        id: 'sum-gallery-recent',
        projectId: 'nikki-001',
        source: 'gallery',
        enabled: true,
        title: '图册记录',
        maxItems: 3,
        priority: 6,
        filters: {},
        displayMode: 'compact'
      },
      {
        id: 'sum-asset-risk',
        projectId: 'nikki-001',
        source: 'assets',
        enabled: true,
        title: '资产风险',
        maxItems: 2,
        priority: 7,
        filters: {},
        displayMode: 'metric'
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
    id: 'cfg-materials',
    projectId: 'nikki-001',
    blockKey: 'materials',
    displayName: '素材收集',
    enabled: true,
    navigation: { visible: false, order: 4 },
    summaryRules: [],
    fields: [
      { key: 'name', label: '素材/套装名称', type: 'text', required: true },
      { key: 'type', label: '收集类型', type: 'select', required: true, options: ['套装', '素材', '代币', '收集项'] },
      { key: 'progress', label: '收集进度', type: 'progress', required: true }
    ],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
    }
  },
  {
    id: 'cfg-gallery',
    projectId: 'nikki-001',
    blockKey: 'gallery',
    displayName: '图册',
    enabled: true,
    navigation: { visible: true, order: 4 },
    summaryRules: [],
    fields: [
      { key: 'albumId', label: '所属图册', type: 'select', required: true },
      { key: 'caption', label: '图片说明', type: 'text' },
      { key: 'takenAt', label: '拍摄时间', type: 'date' }
    ],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
    }
  },
  {
    id: 'cfg-assets',
    projectId: 'nikki-001',
    blockKey: 'assets',
    displayName: '账号资产',
    enabled: true,
    navigation: { visible: true, order: 6 },
    summaryRules: [],
    fields: [
      { key: 'name', label: '资产名称', type: 'text', required: true },
      { key: 'type', label: '资产类型', type: 'select', required: true, options: ['官方账号', '辅助账号', 'Switch 账号', '支付凭证', '兑换码'] },
      { key: 'status', label: '安全状态', type: 'select', required: true, options: ['已保护', '已绑定', '待处理', '已过期'] }
    ],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'exception_only', displayInOverview: false, aiReadable: false }
    }
  },
  {
    id: 'cfg-timeline',
    projectId: 'nikki-001',
    blockKey: 'timeline',
    displayName: '时间轴',
    enabled: true,
    navigation: { visible: true, order: 5 },
    summaryRules: [],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
    }
  },
  {
    id: 'cfg-ai',
    projectId: 'nikki-001',
    blockKey: 'ai',
    displayName: 'AI 助手',
    enabled: true,
    navigation: { visible: false, order: 99 },
    summaryRules: [],
    timeline: {
      enabled: true,
      defaultWriteRule: { mode: 'exception_only', displayInOverview: true, aiReadable: true }
    }
  }
];

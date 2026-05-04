import type { SchemeTemplate } from './types';

const overviewRules = [
  {
    id: 'sum-current-version',
    source: 'version',
    enabled: true,
    title: '当前版本',
    maxItems: 1,
    priority: 1,
    filters: {},
    displayMode: 'metric',
    targetTab: 'version_activity'
  },
  {
    id: 'sum-today-targets',
    source: 'targets',
    enabled: true,
    title: '今日目标',
    maxItems: 3,
    priority: 2,
    filters: { targetTypes: ['daily'] },
    displayMode: 'metric',
    targetTab: 'targets'
  },
  {
    id: 'sum-weekly-targets',
    source: 'targets',
    enabled: true,
    title: '本周目标',
    maxItems: 3,
    priority: 3,
    filters: { targetTypes: ['weekly'] },
    displayMode: 'compact',
    targetTab: 'targets'
  },
  {
    id: 'sum-ending-activities',
    source: 'activities',
    enabled: true,
    title: '即将结束活动',
    maxItems: 2,
    priority: 4,
    filters: { activityStatuses: ['ending', 'pending_archive'], withinHours: 72 },
    displayMode: 'list',
    targetTab: 'version_activity'
  },
  {
    id: 'sum-material-progress',
    source: 'materials',
    enabled: true,
    title: '素材收集',
    maxItems: 3,
    priority: 5,
    filters: {},
    displayMode: 'compact',
    targetTab: 'targets'
  },
  {
    id: 'sum-gallery-recent',
    source: 'gallery',
    enabled: true,
    title: '图册记录',
    maxItems: 3,
    priority: 6,
    filters: {},
    displayMode: 'compact',
    targetTab: 'gallery'
  },
  {
    id: 'sum-asset-risk',
    source: 'assets',
    enabled: true,
    title: '资产风险',
    maxItems: 2,
    priority: 7,
    filters: {},
    displayMode: 'metric',
    targetTab: 'assets'
  },
  {
    id: 'sum-recent-timeline',
    source: 'timeline',
    enabled: true,
    title: '最近时间轴',
    maxItems: 3,
    priority: 8,
    filters: {},
    displayMode: 'timeline',
    targetTab: 'timeline'
  }
] satisfies SchemeTemplate['overviewRules'];

export const infinityNikkiSchemeTemplate: SchemeTemplate = {
  schemeId: 'game.infinity-nikki',
  schemeName: '无限暖暖方案',
  type: { key: 'game', name: '游戏' },
  cycleType: 'long_running',
  version: '0.1.0',
  status: 'available',
  source: 'official',
  summary: '面向长期游玩无限暖暖的项目方案，重点管理任务、版本活动、素材图册、账号资产和长期时间轴。',
  targetUser: '长期玩无限暖暖，希望把日常、活动、拍照、素材和账号资料放在一个项目里管理的用户。',
  usageFrequency: '每日查看，活动和版本节点前高频使用。',
  complexity: 'standard',
  focusPoints: [
    {
      id: 'today-and-weekly-targets',
      title: '今日和本周应该完成什么',
      priority: 'high',
      description: '日常、周常、活动目标需要分开管理，但在概览里以摘要出现。'
    },
    {
      id: 'version-activity-deadline',
      title: '版本活动和倒计时',
      priority: 'high',
      description: '限时活动有开始结束时间、关联目标和归档规则，不能只作为普通倒计时卡片。'
    },
    {
      id: 'photos-and-materials',
      title: '素材、搭配和图册记录',
      priority: 'medium',
      description: '素材收集和搭配照片需要能关联版本、活动和目标，并沉淀到时间轴。'
    },
    {
      id: 'account-assets',
      title: '账号资产安全',
      priority: 'medium',
      description: '账号、UID、兑换码和支付凭证需要安全保存，异常才进入概览。'
    },
    {
      id: 'long-term-timeline',
      title: '长期游玩回顾',
      priority: 'high',
      description: '按天、周、版本回顾完成记录、截图、活动归档和 AI 总结。'
    }
  ],
  capabilities: [
    { key: 'summary_reminder', name: '摘要提醒', reason: '概览只看当前重点，避免堆完整列表。' },
    { key: 'target_system', name: '目标系统', reason: '承载日常、周常、活动目标和自定义目标。' },
    { key: 'cycle_version', name: '周期与版本', reason: '长期游戏需要版本、活动和归档。' },
    { key: 'activity_countdown', name: '活动与倒计时', reason: '限时活动需要结束提醒和待归档状态。' },
    { key: 'collection_growth', name: '收集与养成', reason: '管理素材、套装、搭配计划和拍照目标。' },
    { key: 'media_record', name: '媒体记录', reason: '保存截图、搭配照片和活动照片。' },
    { key: 'asset_profile', name: '资料资产', reason: '保存账号、UID、兑换码和外部链接。' },
    { key: 'timeline_review', name: '时间轴回顾', reason: '长期项目需要按天、周、版本回看。' },
    { key: 'ai_suggestion', name: 'AI 总结建议', reason: '基于摘要和时间轴生成建议，关键写入需确认。' }
  ],
  blockInstances: [
    {
      instanceId: 'cfg-overview',
      blockKey: 'overview',
      displayName: '概览',
      enabled: true,
      capabilities: ['summary_reminder', 'ai_suggestion'],
      navigation: { visible: true, order: 1 },
      summaryRules: overviewRules
    },
    {
      instanceId: 'cfg-targets',
      blockKey: 'targets',
      displayName: '任务',
      enabled: true,
      capabilities: ['target_system', 'summary_reminder', 'timeline_review'],
      navigation: { visible: true, order: 2 },
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
      },
      aiRules: {
        readable: true,
        writableAfterConfirm: true,
        allowedUse: ['今日建议', '周回顾', '活动目标提醒']
      }
    },
    {
      instanceId: 'cfg-version-activity',
      blockKey: 'version_activity',
      displayName: '活动与版本',
      enabled: true,
      capabilities: ['cycle_version', 'activity_countdown', 'target_system', 'timeline_review'],
      navigation: { visible: true, order: 3 },
      behavior: {
        reminderRules: [{ enabled: true, channels: ['app', 'feishu'], beforeMinutes: [4320, 1440, 120] }],
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
      instanceId: 'cfg-materials',
      blockKey: 'materials',
      displayName: '素材收集',
      enabled: true,
      capabilities: ['collection_growth', 'summary_reminder', 'timeline_review'],
      navigation: { visible: false, order: 4 },
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
      instanceId: 'cfg-gallery',
      blockKey: 'gallery',
      displayName: '图册',
      enabled: true,
      capabilities: ['media_record', 'timeline_review', 'ai_suggestion'],
      navigation: { visible: true, order: 4 },
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
      instanceId: 'cfg-timeline',
      blockKey: 'timeline',
      displayName: '时间轴',
      enabled: true,
      capabilities: ['timeline_review', 'ai_suggestion'],
      navigation: { visible: true, order: 5 },
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
      }
    },
    {
      instanceId: 'cfg-assets',
      blockKey: 'assets',
      displayName: '账号资产',
      enabled: true,
      capabilities: ['asset_profile', 'timeline_review'],
      navigation: { visible: true, order: 6 },
      fields: [
        { key: 'name', label: '资产名称', type: 'text', required: true },
        { key: 'type', label: '资产类型', type: 'select', required: true, options: ['官方账号', '辅助账号', 'Switch 账号', '支付凭证', '兑换码'] },
        { key: 'status', label: '安全状态', type: 'select', required: true, options: ['已保护', '已绑定', '待处理', '已过期'] }
      ],
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'exception_only', displayInOverview: false, aiReadable: false }
      },
      security: {
        sensitivity: 'sensitive',
        maskInOverview: true,
        requireConfirmBeforeExternalWrite: true
      }
    },
    {
      instanceId: 'cfg-ai',
      blockKey: 'ai',
      displayName: 'AI 助手',
      enabled: true,
      capabilities: ['ai_suggestion', 'summary_reminder', 'timeline_review'],
      navigation: { visible: false, order: 99 },
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'exception_only', displayInOverview: true, aiReadable: true }
      },
      aiRules: {
        readable: true,
        writableAfterConfirm: true,
        allowedUse: ['概览建议', '时间轴总结', '版本复盘']
      }
    }
  ],
  navigation: [
    { blockKey: 'overview', label: '概览', order: 1, visible: true },
    { blockKey: 'targets', label: '任务', order: 2, visible: true },
    { blockKey: 'version_activity', label: '活动与版本', order: 3, visible: true },
    { blockKey: 'gallery', label: '图册', order: 4, visible: true },
    { blockKey: 'timeline', label: '时间轴', order: 5, visible: true },
    { blockKey: 'assets', label: '账号资产', order: 6, visible: true },
    { blockKey: 'materials', label: '素材收集', order: 7, visible: false },
    { blockKey: 'ai', label: 'AI 助手', order: 99, visible: false }
  ],
  overviewRules,
  managementSections: [
    { id: 'ability-configs', title: '功能块实例配置', description: '启停、导航显示、摘要规则和时间轴写入策略。', source: 'block_instance' },
    { id: 'project-base', title: '项目基础配置', description: '项目名、封面、状态、默认提醒渠道和敏感信息策略。', source: 'project' },
    { id: 'game-rules', title: '游戏重置和归档', description: '每日/每周重置、活动待归档和版本归档规则。', source: 'business', blockKeys: ['targets', 'version_activity'] },
    { id: 'media-ai', title: '图册与 AI 自动化', description: '图册同步、AI 复盘、外部候选内容确认。', source: 'business', blockKeys: ['gallery', 'ai'] }
  ],
  timelineRules: [
    { id: 'target-daily-summary', sourceBlockKey: 'targets', event: 'target_done', writeMode: 'daily_summary', displayInOverview: true, aiReadable: true },
    { id: 'activity-archive-detail', sourceBlockKey: 'version_activity', event: 'activity_archived', writeMode: 'detail', displayInOverview: true, aiReadable: true },
    { id: 'photo-upload-detail', sourceBlockKey: 'gallery', event: 'photo_uploaded', writeMode: 'detail', displayInOverview: true, aiReadable: true },
    { id: 'asset-exception-only', sourceBlockKey: 'assets', event: 'asset_changed', writeMode: 'exception_only', displayInOverview: false, aiReadable: false }
  ],
  seedData: {
    projectName: '无限暖暖',
    projectDescription: '记录任务目标、活动版本、素材收集、时间轴与账号资产。',
    sampleOverviewTitles: ['当前版本', '今日目标', '即将结束活动'],
    sampleTimelineTitles: ['完成今日目标汇总', '归档花愿镇活动', '上传搭配照片']
  },
  safety: {
    sensitiveFields: ['账号', 'UID', '支付凭证', '兑换码', '外部渠道 Token'],
    aiReadableByDefault: false,
    externalWritePolicy: 'confirm_required',
    notes: ['账号资产只在异常时进入概览。', 'AI 可以总结目标和活动，但不能无确认写入敏感资产。']
  }
};

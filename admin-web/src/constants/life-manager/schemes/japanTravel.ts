import type { SchemeTemplate } from './types';

export const japanTravelSchemeTemplate: SchemeTemplate = {
  schemeId: 'travel.japan-free-trip',
  schemeName: '日本自由行方案',
  type: { key: 'travel', name: '旅行' },
  cycleType: 'stage_planned',
  version: '0.1.0',
  status: 'draft',
  source: 'official',
  summary: '面向一次日本自由行的阶段计划型方案，重点管理出发倒计时、行程、清单、资料、预算和旅行回顾。',
  targetUser: '正在规划一次日本旅行，希望集中管理行程、预订、证件、预算和同行信息的用户。',
  usageFrequency: '出发前集中使用，旅行中每日查看，旅行后归档回顾。',
  complexity: 'standard',
  focusPoints: [
    {
      id: 'departure-countdown',
      title: '出发前还有什么必须完成',
      priority: 'high',
      description: '出发倒计时、行前清单、证件保险和预订确认需要进入概览。'
    },
    {
      id: 'daily-itinerary',
      title: '每天去哪里、怎么去',
      priority: 'high',
      description: '行程需要按日期、城市、地点和交通方式组织。'
    },
    {
      id: 'documents-and-bookings',
      title: '关键资料是否安全可查',
      priority: 'high',
      description: '护照、签证、机票、酒店、保险和票据属于敏感资料，需要脱敏和确认。'
    },
    {
      id: 'budget-risk',
      title: '预算是否超支',
      priority: 'medium',
      description: '预算和支出用于提醒风险，但不一定每笔都进时间轴。'
    },
    {
      id: 'trip-memory',
      title: '旅行后能留下回顾',
      priority: 'medium',
      description: '照片、票据、笔记和路线可以在旅行后生成时间轴总结。'
    }
  ],
  capabilities: [
    { key: 'summary_reminder', name: '摘要提醒', reason: '概览展示出发倒计时、清单进度和风险提醒。' },
    { key: 'stage_itinerary', name: '阶段与行程', reason: '旅行按准备期、每天行程和旅行后归档推进。' },
    { key: 'target_system', name: '目标系统', reason: '承载行前清单、行李清单和待预订事项。' },
    { key: 'activity_countdown', name: '活动与倒计时', reason: '出发、预约、退改签和保险到期需要提醒。' },
    { key: 'asset_profile', name: '资料资产', reason: '保存证件、机票、酒店、保险和票据。' },
    { key: 'budget_amount', name: '预算金额', reason: '管理预算、支出和已支付款项。' },
    { key: 'contact_person', name: '联系人', reason: '管理同行人、紧急联系人和酒店/保险联系人。' },
    { key: 'media_record', name: '媒体记录', reason: '保存旅行照片、票据图片和地点素材。' },
    { key: 'timeline_review', name: '时间轴回顾', reason: '按准备阶段、旅行日期和地点生成回顾。' },
    { key: 'ai_suggestion', name: 'AI 总结建议', reason: '生成行前提醒、天气建议和旅行总结。' }
  ],
  blockInstances: [
    {
      instanceId: 'travel-overview',
      blockKey: 'overview',
      displayName: '概览',
      enabled: true,
      capabilities: ['summary_reminder', 'activity_countdown', 'ai_suggestion'],
      navigation: { visible: true, order: 1 },
      summaryRules: [
        {
          id: 'travel-departure-countdown',
          source: 'countdown',
          enabled: true,
          title: '出发倒计时',
          maxItems: 1,
          priority: 1,
          filters: { dateType: 'departure' },
          displayMode: 'metric',
          targetTab: 'itinerary'
        },
        {
          id: 'travel-checklist-progress',
          source: 'checklist',
          enabled: true,
          title: '行前清单',
          maxItems: 4,
          priority: 2,
          filters: { onlyUnfinished: true },
          displayMode: 'compact',
          targetTab: 'checklist'
        },
        {
          id: 'travel-document-risk',
          source: 'assets',
          enabled: true,
          title: '资料风险',
          maxItems: 3,
          priority: 3,
          filters: { statuses: ['missing', 'pending'] },
          displayMode: 'list',
          targetTab: 'documents'
        }
      ]
    },
    {
      instanceId: 'travel-itinerary',
      blockKey: 'itinerary',
      displayName: '行程',
      enabled: true,
      capabilities: ['stage_itinerary', 'activity_countdown', 'timeline_review'],
      navigation: { visible: true, order: 2 },
      fields: [
        { key: 'date', label: '日期', type: 'date', required: true },
        { key: 'city', label: '城市', type: 'select', options: ['东京', '京都', '大阪'] },
        { key: 'place', label: '地点', type: 'text' },
        { key: 'transport', label: '交通方式', type: 'text' }
      ],
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
      }
    },
    {
      instanceId: 'travel-checklist',
      blockKey: 'checklist',
      displayName: '清单',
      enabled: true,
      capabilities: ['target_system', 'summary_reminder'],
      navigation: { visible: true, order: 3 },
      behavior: {
        statusFlow: ['todo', 'done', 'skipped']
      },
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'daily_summary', displayInOverview: true, aiReadable: true }
      }
    },
    {
      instanceId: 'travel-documents',
      blockKey: 'documents',
      displayName: '资料',
      enabled: true,
      capabilities: ['asset_profile', 'contact_person'],
      navigation: { visible: true, order: 4 },
      security: {
        sensitivity: 'sensitive',
        maskInOverview: true,
        requireConfirmBeforeExternalWrite: true
      }
    },
    {
      instanceId: 'travel-budget',
      blockKey: 'budget',
      displayName: '预算',
      enabled: true,
      capabilities: ['budget_amount', 'summary_reminder'],
      navigation: { visible: true, order: 5 },
      fields: [
        { key: 'category', label: '预算分类', type: 'select', required: true, options: ['交通', '住宿', '餐饮', '购物', '门票', '其他'] },
        { key: 'amount', label: '金额', type: 'number', required: true },
        { key: 'paid', label: '是否已支付', type: 'boolean' }
      ]
    },
    {
      instanceId: 'travel-gallery',
      blockKey: 'gallery',
      displayName: '图册',
      enabled: true,
      capabilities: ['media_record', 'timeline_review'],
      navigation: { visible: true, order: 6 },
      timeline: {
        enabled: true,
        defaultWriteRule: { mode: 'detail', displayInOverview: true, aiReadable: true }
      }
    },
    {
      instanceId: 'travel-timeline',
      blockKey: 'timeline',
      displayName: '时间轴',
      enabled: true,
      capabilities: ['timeline_review', 'ai_suggestion'],
      navigation: { visible: true, order: 7 }
    },
    {
      instanceId: 'travel-weather',
      blockKey: 'weather_place',
      displayName: '天气地点',
      enabled: true,
      capabilities: ['summary_reminder', 'stage_itinerary'],
      navigation: { visible: false, order: 20 }
    }
  ],
  navigation: [
    { blockKey: 'overview', label: '概览', order: 1, visible: true },
    { blockKey: 'itinerary', label: '行程', order: 2, visible: true },
    { blockKey: 'checklist', label: '清单', order: 3, visible: true },
    { blockKey: 'documents', label: '资料', order: 4, visible: true },
    { blockKey: 'budget', label: '预算', order: 5, visible: true },
    { blockKey: 'gallery', label: '图册', order: 6, visible: true },
    { blockKey: 'timeline', label: '时间轴', order: 7, visible: true },
    { blockKey: 'weather_place', label: '天气地点', order: 20, visible: false }
  ],
  overviewRules: [
    {
      id: 'travel-departure-countdown',
      source: 'countdown',
      enabled: true,
      title: '出发倒计时',
      maxItems: 1,
      priority: 1,
      filters: { dateType: 'departure' },
      displayMode: 'metric',
      targetTab: 'itinerary'
    },
    {
      id: 'travel-checklist-progress',
      source: 'checklist',
      enabled: true,
      title: '行前清单',
      maxItems: 4,
      priority: 2,
      filters: { onlyUnfinished: true },
      displayMode: 'compact',
      targetTab: 'checklist'
    },
    {
      id: 'travel-document-risk',
      source: 'assets',
      enabled: true,
      title: '资料风险',
      maxItems: 3,
      priority: 3,
      filters: { statuses: ['missing', 'pending'] },
      displayMode: 'list',
      targetTab: 'documents'
    }
  ],
  managementSections: [
    { id: 'date-stage', title: '日期与阶段', description: '出发日期、旅行日期、准备期和旅行后归档。', source: 'project' },
    { id: 'block-configs', title: '功能块实例配置', description: '行程、清单、资料、预算、图册的导航和摘要规则。', source: 'block_instance' },
    { id: 'document-groups', title: '资料分组和敏感策略', description: '证件、预订、保险和票据的脱敏与确认规则。', source: 'business', blockKeys: ['documents'] },
    { id: 'budget-categories', title: '预算分类', description: '交通、住宿、餐饮、购物等预算分类。', source: 'business', blockKeys: ['budget'] }
  ],
  timelineRules: [
    { id: 'itinerary-detail', sourceBlockKey: 'itinerary', event: 'itinerary_done', writeMode: 'detail', displayInOverview: true, aiReadable: true },
    { id: 'checklist-daily-summary', sourceBlockKey: 'checklist', event: 'checklist_done', writeMode: 'daily_summary', displayInOverview: true, aiReadable: true },
    { id: 'document-confirmed', sourceBlockKey: 'documents', event: 'document_changed', writeMode: 'detail', displayInOverview: false, aiReadable: false, requireConfirm: true },
    { id: 'gallery-detail', sourceBlockKey: 'gallery', event: 'photo_uploaded', writeMode: 'detail', displayInOverview: true, aiReadable: true }
  ],
  seedData: {
    projectName: '日本旅行 2026',
    projectDescription: '规划东京、京都、大阪的自由行行程、资料、预算和旅行回顾。',
    sampleOverviewTitles: ['出发倒计时', '行前清单', '资料风险'],
    sampleTimelineTitles: ['确认东京酒店', '完成护照检查', '上传浅草寺照片']
  },
  safety: {
    sensitiveFields: ['护照号', '签证资料', '机票订单号', '酒店订单号', '保险保单', '紧急联系人'],
    aiReadableByDefault: false,
    externalWritePolicy: 'confirm_required',
    notes: ['证件和订单默认脱敏展示。', 'AI 可以做行前提醒和资料完整性检查，但不能无确认修改资料。']
  }
};

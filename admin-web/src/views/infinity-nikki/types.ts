/**
 * Infinity Nikki 项目详情页类型定义
 * 与后端接口字段一一对应
 */

// ========== 项目基础信息 ==========

/** 项目基础信息 */
export interface NikkiProject {
  id: string;
  name: string;
  description: string;
  coverSrc: string;
  coverAlt: string;
  status: 'active' | 'paused' | 'archived';
  createdAt: string;
  tags: NikkiProjectTag[];
  stats: NikkiProjectStat[];
}

export interface NikkiProjectTag {
  label: string;
  tone?: 'default' | 'success';
}

export interface NikkiProjectStat {
  label: string;
  value: number | string;
}

// ========== 资产 ==========

/** 资产类型 */
export type AssetType = 'official_account' | 'sub_account' | 'switch_account' | 'payment' | 'redeem_code';

/** 账号资产 */
export interface NikkiAsset {
  id: string;
  name: string;
  type: AssetType;
  status: 'protected' | 'bound' | 'pending' | 'expired' | 'archived';
  statusLabel: string;
  icon?: string;
  description?: string;
  archivedAt?: string;
}

/** 资产概览 */
export interface AssetOverview {
  total: number;
  assets: NikkiAsset[];
}

// ========== 笔记 ==========

/** 笔记分类 */
export type NoteCategory = 'strategy' | 'explore' | 'coordination' | 'material' | 'story';

/** 笔记 */
export interface NikkiNote {
  id: string;
  title: string;
  category: NoteCategory;
  categoryLabel: string;
  content?: string;
  createdAt: string;
  updatedAt: string;
  relativeTime: string;
}

/** 笔记概览 */
export interface NoteOverview {
  total: number;
  notes: NikkiNote[];
}

// ========== 图册 ==========

/** 图册 */
export interface NikkiAlbum {
  id: string;
  name: string;
  coverUrl: string;
  photoCount: number;
}

/** 图片 */
export interface NikkiPhoto {
  id: string;
  albumId: string;
  url: string;
  thumbnail: string;
  caption?: string;
  takenAt: string;
  versionId?: string;
  activityId?: string;
  targetId?: string;
}

/** 图册概览 */
export interface GalleryOverview {
  albumCount: number;
  photoCount: number;
  albums: NikkiAlbum[];
  recentPhotos: NikkiPhoto[];
}

// ========== AI 建议 ==========

/** AI 建议 */
export interface AiSuggestion {
  id: string;
  content: string;
  highlights: AiHighlight[];
  generatedAt: string;
  status: 'generated' | 'generating' | 'pending' | 'failed';
}

export interface AiHighlight {
  text: string;
  type: 'danger' | 'info' | 'success' | 'warning';
}

/** AI 建议概览 */
export interface AiOverview {
  currentSuggestion: AiSuggestion;
  suggestionCount: number;
}

// ========== 详情弹框 ==========

/** 详情弹框数据 */
export interface DetailModalData {
  show: boolean;
  title: string;
  type: string;
  icon: string;
  meta: DetailMeta[];
  content?: string;
}

export interface DetailMeta {
  label: string;
  value: string;
}

// ========== 游戏闭环模型 ==========

/** 游戏版本状态 */
export type GameVersionStatus = 'upcoming' | 'active' | 'ending' | 'ended' | 'archived';

/** 游戏版本 */
export interface GameVersion {
  id: string;
  projectId: string;
  name: string;
  title: string;
  startAt: string;
  endAt: string;
  status: GameVersionStatus;
  highlights: string[];
  activityIds: string[];
  summary?: string;
  archivedAt?: string;
}

/** 游戏活动状态 */
export type GameActivityStatus = 'upcoming' | 'active' | 'ending' | 'ended' | 'pending_archive' | 'archived';

/** 游戏活动 */
export interface GameActivity {
  id: string;
  projectId: string;
  versionId: string;
  title: string;
  description?: string;
  startAt: string;
  endAt: string;
  status: GameActivityStatus;
  priority: 'low' | 'normal' | 'high';
  cover?: string;
  targetIds: string[];
  materialIds?: string[];
  photoIds?: string[];
  noteIds?: string[];
  reminderRule?: ReminderRule;
  archivedAt?: string;
}

/** 游戏目标类型 */
export type GameTargetType = 'daily' | 'weekly' | 'activity' | 'custom';

/** 游戏目标状态 */
export type GameTargetStatus = 'todo' | 'done' | 'skipped' | 'expired' | 'archived';

/** 游戏目标 */
export interface GameTarget {
  id: string;
  projectId: string;
  type: GameTargetType;
  title: string;
  description?: string;
  status: GameTargetStatus;
  progressCurrent?: number;
  progressTarget?: number;
  resetRule?: ResetRule;
  versionId?: string;
  activityId?: string;
  dueAt?: string;
  priority: 'low' | 'normal' | 'high';
  pinnedToOverview?: boolean;
  timelineRule: TimelineWriteRule;
  archivedAt?: string;
}

/** 素材/收集项类型 */
export type NikkiMaterialType = 'outfit' | 'material' | 'currency' | 'collection';

/** 素材/收集项状态 */
export type NikkiMaterialStatus = 'planning' | 'collecting' | 'completed' | 'archived';

/** 素材/收集项 */
export interface NikkiMaterial {
  id: string;
  projectId: string;
  name: string;
  type: NikkiMaterialType;
  status: NikkiMaterialStatus;
  current: number;
  target: number;
  versionId?: string;
  activityId?: string;
  targetId?: string;
  note?: string;
}

/** 素材/收集概览 */
export interface MaterialOverview {
  total: number;
  completed: number;
  materials: NikkiMaterial[];
}

/** 概览摘要规则 */
export interface OverviewSummaryRule {
  id: string;
  projectId: string;
  source: 'targets' | 'activities' | 'version' | 'materials' | 'gallery' | 'assets' | 'timeline' | 'ai';
  enabled: boolean;
  title: string;
  maxItems: number;
  priority: number;
  filters: {
    targetTypes?: GameTargetType[];
    activityStatuses?: GameActivityStatus[];
    onlyPinned?: boolean;
    onlyHighPriority?: boolean;
    withinHours?: number;
  };
  displayMode: 'metric' | 'list' | 'compact' | 'timeline';
}

/** 功能块 key */
export type AbilityBlockKey =
  | 'overview'
  | 'targets'
  | 'version_activity'
  | 'materials'
  | 'gallery'
  | 'assets'
  | 'timeline'
  | 'ai';

/** 功能块实例配置 */
export interface AbilityInstanceConfig {
  id: string;
  projectId: string;
  blockKey: AbilityBlockKey;
  displayName: string;
  enabled: boolean;
  capabilities: string[];
  navigation: {
    visible: boolean;
    order: number;
  };
  summaryRules: OverviewSummaryRule[];
  fields?: FieldConfig[];
  behavior?: {
    resetRules?: ResetRule[];
    reminderRules?: ReminderRule[];
    archiveRule?: ArchiveRule;
  };
  timeline?: {
    enabled: boolean;
    defaultWriteRule: TimelineWriteRule;
  };
}

/** 时间轴事件类型 */
export type TimelineEventType =
  | 'target_done'
  | 'target_skipped'
  | 'target_expired'
  | 'activity_started'
  | 'activity_ending'
  | 'activity_archived'
  | 'version_archived'
  | 'material_completed'
  | 'photo_uploaded'
  | 'note_created'
  | 'ai_summary_generated';

/** 时间轴事件 */
export interface TimelineEvent {
  id: string;
  projectId: string;
  occurredAt: string;
  type: TimelineEventType;
  title: string;
  description?: string;
  sourceBlockKey: AbilityBlockKey;
  versionId?: string;
  activityId?: string;
  targetId?: string;
  sensitivity: 'normal' | 'private';
  displayInOverview: boolean;
  aiReadable: boolean;
}

/** 重置规则 */
export interface ResetRule {
  type: 'none' | 'daily' | 'weekly' | 'activity' | 'custom';
  time?: string;
  weekday?: number;
  inheritFromActivity?: boolean;
}

/** 提醒规则 */
export interface ReminderRule {
  enabled: boolean;
  channels: ('app' | 'feishu' | 'webhook')[];
  beforeMinutes: number[];
  quietHours?: {
    start: string;
    end: string;
  };
}

/** 归档规则 */
export interface ArchiveRule {
  type: 'manual' | 'on_activity_end' | 'on_version_end' | 'after_days';
  afterDays?: number;
  includeTargets: boolean;
  includePhotos: boolean;
  includeNotes: boolean;
  generateTimelineSummary: boolean;
}

/** 时间轴写入规则 */
export interface TimelineWriteRule {
  mode: 'none' | 'detail' | 'daily_summary' | 'weekly_summary' | 'exception_only';
  displayInOverview: boolean;
  aiReadable: boolean;
}

/** 字段配置 */
export interface FieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'select' | 'boolean' | 'progress';
  required?: boolean;
  options?: string[];
}

/** 概览摘要 */
export interface OverviewSummary {
  id: string;
  ruleId: string;
  title: string;
  value?: string;
  description?: string;
  items?: Array<{
    id: string;
    label: string;
    status?: string;
    targetRoute?: string;
  }>;
  targetRoute?: string;
}

// ========== API 响应 ==========

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

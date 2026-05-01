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

// ========== 日常任务 ==========

/** 任务分组类型 */
export type TaskGroupType = 'daily' | 'weekly' | 'event';

/** 任务分组 */
export interface TaskGroup {
  type: TaskGroupType;
  label: string;
  tasks: DailyTask[];
}

/** 日常任务 */
export interface DailyTask {
  id: string;
  text: string;
  done: boolean;
  detail?: string;
  group: TaskGroupType;
}

/** 周常目标 */
export interface WeeklyGoal {
  id: string;
  text: string;
  current: number;
  max: number;
  progress: number;
  status: 'on_track' | 'needs_attention' | 'completed';
  color: string;
}

/** 日常任务概览 */
export interface DailyTaskOverview {
  completed: number;
  total: number;
  taskGroups: TaskGroup[];
  weeklyGoals: WeeklyGoal[];
}

// ========== 倒计时 ==========

/** 活动倒计时 */
export interface EventCountdown {
  id: string;
  title: string;
  description?: string;
  endTime: string;
  remainingDays: number;
  img: string;
  reminded: boolean;
  checkedIn: boolean;
}

/** 版本节点 */
export interface VersionNode {
  id: string;
  type: 'live' | 'maintenance' | 'update';
  title: string;
  startTime: string;
  endTime?: string;
  daysUntil: number;
  reminded: boolean;
  checkedIn: boolean;
}

/** 倒计时概览 */
export interface CountdownOverview {
  events: EventCountdown[];
  versionNodes: VersionNode[];
}

// ========== 资产 ==========

/** 资产类型 */
export type AssetType = 'official_account' | 'sub_account' | 'switch_account' | 'payment' | 'redeem_code';

/** 账号资产 */
export interface NikkiAsset {
  id: string;
  name: string;
  type: AssetType;
  status: 'protected' | 'bound' | 'pending' | 'expired';
  statusLabel: string;
  icon?: string;
  description?: string;
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

// ========== 最近动态 ==========

/** 动态类型 */
export type ActivityType = 'task_complete' | 'note_added' | 'screenshot_uploaded' | 'event_progress' | 'version_announcement';

/** 动态 */
export interface NikkiActivity {
  id: string;
  type: ActivityType;
  title: string;
  description: string;
  icon: string;
  iconColor: string;
  bg: string;
  time: string;
  createdAt: string;
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

// ========== API 响应 ==========

export interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}

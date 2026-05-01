/**
 * Infinity Nikki 项目服务层
 * 所有接口调用统一走此文件，当前使用 mock 数据，后续替换为真实 API
 */
import type {
  AiOverview,
  ApiResponse,
  AssetOverview,
  CountdownOverview,
  DailyTaskOverview,
  GalleryOverview,
  NikkiActivity,
  NikkiAsset,
  NikkiNote,
  NikkiProject,
  NoteOverview,
  WeeklyGoal
} from './types';
import {
  mockActivities,
  mockAiOverview,
  mockAssetOverview,
  mockCountdownOverview,
  mockDailyTaskOverview,
  mockGalleryOverview,
  mockNoteOverview,
  mockProject
} from './mock';

/** 模拟网络延迟 */
function delay(ms = 300): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/** 包装为标准响应 */
function wrap<T>(data: T): ApiResponse<T> {
  return { code: 200, message: 'success', data };
}

// ========== 项目基础信息 ==========

/** 获取项目基础信息 */
export async function fetchProject(): Promise<ApiResponse<NikkiProject>> {
  await delay();
  return wrap({ ...mockProject });
}

// ========== 日常任务 ==========

/** 获取日常任务概览（含分组和周常目标） */
export async function fetchDailyTaskOverview(): Promise<ApiResponse<DailyTaskOverview>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockDailyTaskOverview)));
}

/** 切换任务完成状态 */
export async function toggleTask(taskId: string, done: boolean): Promise<ApiResponse<{ id: string; done: boolean }>> {
  await delay(200);
  return wrap({ id: taskId, done });
}

/** 更新周常目标进度 */
export async function updateWeeklyGoalProgress(
  goalId: string,
  current: number
): Promise<ApiResponse<{ id: string; current: number }>> {
  await delay(200);
  return wrap({ id: goalId, current });
}

/** 切换周常目标状态 */
export async function toggleWeeklyGoalStatus(
  goalId: string,
  status: WeeklyGoal['status']
): Promise<ApiResponse<{ id: string; status: WeeklyGoal['status'] }>> {
  await delay(200);
  return wrap({ id: goalId, status });
}

// ========== 倒计时 ==========

/** 获取倒计时概览 */
export async function fetchCountdownOverview(): Promise<ApiResponse<CountdownOverview>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockCountdownOverview)));
}

/** 设置/取消活动提醒 */
export async function toggleEventReminder(
  eventId: string,
  reminded: boolean
): Promise<ApiResponse<{ id: string; reminded: boolean }>> {
  await delay(200);
  return wrap({ id: eventId, reminded });
}

/** 活动打卡/撤回 */
export async function toggleEventCheckIn(
  eventId: string,
  checkedIn: boolean
): Promise<ApiResponse<{ id: string; checkedIn: boolean }>> {
  await delay(200);
  return wrap({ id: eventId, checkedIn });
}

/** 设置/取消版本节点提醒 */
export async function toggleVersionReminder(
  nodeId: string,
  reminded: boolean
): Promise<ApiResponse<{ id: string; reminded: boolean }>> {
  await delay(200);
  return wrap({ id: nodeId, reminded });
}

/** 版本节点打卡/撤回 */
export async function toggleVersionCheckIn(
  nodeId: string,
  checkedIn: boolean
): Promise<ApiResponse<{ id: string; checkedIn: boolean }>> {
  await delay(200);
  return wrap({ id: nodeId, checkedIn });
}

// ========== 资产 ==========

/** 获取资产概览 */
export async function fetchAssetOverview(): Promise<ApiResponse<AssetOverview>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockAssetOverview)));
}

/** 获取资产详情 */
export async function fetchAssetDetail(assetId: string): Promise<ApiResponse<NikkiAsset | undefined>> {
  await delay(200);
  const asset = mockAssetOverview.assets.find(a => a.id === assetId);
  return wrap(asset ? { ...asset } : undefined);
}

// ========== 笔记 ==========

/** 获取笔记概览 */
export async function fetchNoteOverview(): Promise<ApiResponse<NoteOverview>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockNoteOverview)));
}

/** 获取笔记详情 */
export async function fetchNoteDetail(noteId: string): Promise<ApiResponse<NikkiNote | undefined>> {
  await delay(200);
  const note = mockNoteOverview.notes.find(n => n.id === noteId);
  return wrap(note ? { ...note } : undefined);
}

// ========== 图册 ==========

/** 获取图册概览 */
export async function fetchGalleryOverview(): Promise<ApiResponse<GalleryOverview>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockGalleryOverview)));
}

// ========== AI 建议 ==========

/** 获取 AI 建议 */
export async function fetchAiOverview(): Promise<ApiResponse<AiOverview>> {
  await delay(500);
  return wrap(JSON.parse(JSON.stringify(mockAiOverview)));
}

/** 刷新 AI 建议 */
export async function refreshAiSuggestion(): Promise<ApiResponse<AiOverview>> {
  await delay(800);
  return wrap(JSON.parse(JSON.stringify(mockAiOverview)));
}

// ========== 最近动态 ==========

/** 获取最近动态 */
export async function fetchActivities(): Promise<ApiResponse<NikkiActivity[]>> {
  await delay();
  return wrap(JSON.parse(JSON.stringify(mockActivities)));
}

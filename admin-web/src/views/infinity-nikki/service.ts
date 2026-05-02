/**
 * Infinity Nikki 项目服务层
 * 所有接口调用统一走此文件，当前使用 mock 数据，后续替换为真实 API
 */
import type {
  AbilityInstanceConfig,
  AiOverview,
  ApiResponse,
  AssetOverview,
  CountdownOverview,
  DailyTaskOverview,
  GalleryOverview,
  GameActivity,
  GameTarget,
  GameVersion,
  NikkiActivity,
  NikkiAsset,
  NikkiNote,
  NikkiProject,
  NoteOverview,
  OverviewSummary,
  TimelineEvent,
  WeeklyGoal
} from './types';
import {
  mockActivities,
  mockAbilityInstanceConfigs,
  mockAiOverview,
  mockAssetOverview,
  mockCountdownOverview,
  mockDailyTaskOverview,
  mockGameActivities,
  mockGameTargets,
  mockGameVersions,
  mockGalleryOverview,
  mockNoteOverview,
  mockProject,
  mockTimelineEvents
} from './mock';

/** 模拟网络延迟 */
function delay(ms = 300): Promise<void> {
  return new Promise(resolve => setTimeout(resolve, ms));
}

/** 包装为标准响应 */
function wrap<T>(data: T): ApiResponse<T> {
  return { code: 200, message: 'success', data };
}

/** 深拷贝 mock 数据，避免页面演示时改到源数据 */
function clone<T>(data: T): T {
  if (data === undefined || data === null) {
    return data;
  }
  return JSON.parse(JSON.stringify(data)) as T;
}

function buildOverviewSummaries(): OverviewSummary[] {
  const overviewConfig = mockAbilityInstanceConfigs.find(config => config.blockKey === 'overview');
  const rules = [...(overviewConfig?.summaryRules ?? [])]
    .filter(rule => rule.enabled)
    .sort((prev, next) => prev.priority - next.priority);

  return rules.map(rule => {
    if (rule.id === 'sum-current-version') {
      const currentVersion = mockGameVersions.find(version => version.status === 'active');
      return {
        id: 'overview-current-version',
        ruleId: rule.id,
        title: rule.title,
        value: currentVersion?.name ?? '暂无当前版本',
        ...(currentVersion ? { description: `${currentVersion.title}，结束于 ${currentVersion.endAt.slice(0, 10)}` } : {}),
        targetRoute: 'version_activity'
      };
    }

    if (rule.id === 'sum-today-targets') {
      const dailyTargets = mockGameTargets.filter(target => target.type === 'daily');
      const doneCount = dailyTargets.filter(target => target.status === 'done').length;
      return {
        id: 'overview-today-targets',
        ruleId: rule.id,
        title: rule.title,
        value: `${doneCount}/${dailyTargets.length}`,
        description: '日常目标由任务功能块的展示规则控制',
        items: dailyTargets
          .filter(target => target.status !== 'done')
          .slice(0, rule.maxItems)
          .map(target => ({ id: target.id, label: target.title, status: target.status, targetRoute: 'targets' })),
        targetRoute: 'targets'
      };
    }

    if (rule.id === 'sum-weekly-targets') {
      const weeklyTargets = mockGameTargets.filter(target => target.type === 'weekly');
      const doneCount = weeklyTargets.filter(target => target.status === 'done').length;
      return {
        id: 'overview-weekly-targets',
        ruleId: rule.id,
        title: rule.title,
        value: `${doneCount}/${weeklyTargets.length}`,
        items: weeklyTargets
          .slice(0, rule.maxItems)
          .map(target => ({ id: target.id, label: target.title, status: target.status, targetRoute: 'targets' })),
        targetRoute: 'targets'
      };
    }

    if (rule.id === 'sum-ending-activities') {
      const endingActivities = mockGameActivities.filter(activity => {
        return rule.filters.activityStatuses?.includes(activity.status) ?? true;
      });
      return {
        id: 'overview-ending-activities',
        ruleId: rule.id,
        title: rule.title,
        value: `${endingActivities.length}个`,
        description: '活动结束后会触发归档规则，并写入项目时间轴',
        items: endingActivities
          .slice(0, rule.maxItems)
          .map(activity => ({ id: activity.id, label: activity.title, status: activity.status, targetRoute: 'version_activity' })),
        targetRoute: 'version_activity'
      };
    }

    if (rule.id === 'sum-recent-timeline') {
      const timelineEvents = [...mockTimelineEvents]
        .filter(event => event.displayInOverview)
        .sort((prev, next) => Date.parse(next.occurredAt) - Date.parse(prev.occurredAt));
      return {
        id: 'overview-recent-timeline',
        ruleId: rule.id,
        title: rule.title,
        value: `${timelineEvents.length}条`,
        items: timelineEvents
          .slice(0, rule.maxItems)
          .map(event => ({ id: event.id, label: event.title, status: event.type, targetRoute: 'timeline' })),
        targetRoute: 'timeline'
      };
    }

    return {
      id: `overview-${rule.id}`,
      ruleId: rule.id,
      title: rule.title,
      value: '待配置'
    };
  });
}

// ========== 项目基础信息 ==========

/** 获取项目基础信息 */
export async function fetchProject(): Promise<ApiResponse<NikkiProject>> {
  await delay();
  return wrap({ ...mockProject });
}

// ========== 游戏闭环模型 ==========

/** 获取游戏版本列表 */
export async function fetchGameVersions(): Promise<ApiResponse<GameVersion[]>> {
  await delay();
  return wrap(clone(mockGameVersions));
}

/** 获取当前游戏版本 */
export async function fetchCurrentGameVersion(): Promise<ApiResponse<GameVersion | undefined>> {
  await delay();
  const currentVersion = mockGameVersions.find(version => version.status === 'active');
  return wrap(clone(currentVersion));
}

/** 获取版本活动列表 */
export async function fetchGameActivities(): Promise<ApiResponse<GameActivity[]>> {
  await delay();
  return wrap(clone(mockGameActivities));
}

/** 获取游戏目标列表 */
export async function fetchGameTargets(): Promise<ApiResponse<GameTarget[]>> {
  await delay();
  return wrap(clone(mockGameTargets));
}

/** 获取项目时间轴 */
export async function fetchTimelineEvents(): Promise<ApiResponse<TimelineEvent[]>> {
  await delay();
  return wrap(clone(mockTimelineEvents));
}

/** 获取功能块实例配置 */
export async function fetchAbilityInstanceConfigs(): Promise<ApiResponse<AbilityInstanceConfig[]>> {
  await delay();
  return wrap(clone(mockAbilityInstanceConfigs));
}

/** 获取概览摘要 */
export async function fetchOverviewSummaries(): Promise<ApiResponse<OverviewSummary[]>> {
  await delay();
  return wrap(clone(buildOverviewSummaries()));
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

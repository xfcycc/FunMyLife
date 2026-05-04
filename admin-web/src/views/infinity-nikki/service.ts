/**
 * Infinity Nikki 项目服务层
 * 所有接口调用统一走此文件，当前使用 mock 数据，后续替换为真实 API
 */
import type {
  AbilityInstanceConfig,
  AiOverview,
  ApiResponse,
  ArchiveRule,
  AssetOverview,
  GalleryOverview,
  GameActivity,
  GameTarget,
  GameVersion,
  MaterialOverview,
  NikkiAsset,
  NikkiNote,
  NikkiProject,
  NoteOverview,
  OverviewSummary,
  OverviewSummaryRule,
  TimelineEvent
} from './types';
import {
  mockAbilityInstanceConfigs,
  mockAiOverview,
  mockAssetOverview,
  mockGameActivities,
  mockGameTargets,
  mockGameVersions,
  mockGalleryOverview,
  mockMaterialOverview,
  mockNoteOverview,
  mockProject,
  mockTimelineEvents
} from './mock';

const ABILITY_CONFIG_STORAGE_KEY = 'life-manager.infinity-nikki.ability-configs';

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

function mergeOverviewRules(defaultRules: OverviewSummaryRule[], storedRules?: OverviewSummaryRule[]): OverviewSummaryRule[] {
  if (!storedRules?.length) {
    return defaultRules;
  }

  const storedRuleMap = new Map(storedRules.map(rule => [rule.id, rule]));
  return defaultRules.map(rule => ({ ...rule, ...storedRuleMap.get(rule.id) }));
}

function mergeArchiveRule(defaultRule?: ArchiveRule, storedRule?: ArchiveRule): ArchiveRule | undefined {
  if (!defaultRule) {
    return storedRule;
  }

  return storedRule ? { ...defaultRule, ...storedRule } : defaultRule;
}

function mergeAbilityConfigs(storedConfigs: AbilityInstanceConfig[]): AbilityInstanceConfig[] {
  const storedConfigMap = new Map(storedConfigs.map(config => [config.id, config]));
  const storedBlockMap = new Map(storedConfigs.map(config => [config.blockKey, config]));

  return clone(mockAbilityInstanceConfigs).map(defaultConfig => {
    const storedConfig = storedConfigMap.get(defaultConfig.id) ?? storedBlockMap.get(defaultConfig.blockKey);

    if (!storedConfig) {
      return defaultConfig;
    }

    const timeline = defaultConfig.timeline || storedConfig.timeline;

    return {
      ...defaultConfig,
      ...storedConfig,
      capabilities: storedConfig.capabilities ?? defaultConfig.capabilities,
      navigation: {
        ...defaultConfig.navigation,
        ...storedConfig.navigation
      },
      fields: storedConfig.fields ?? defaultConfig.fields,
      summaryRules: mergeOverviewRules(defaultConfig.summaryRules, storedConfig.summaryRules),
      behavior: {
        ...defaultConfig.behavior,
        ...storedConfig.behavior,
        resetRules: storedConfig.behavior?.resetRules ?? defaultConfig.behavior?.resetRules,
        reminderRules: storedConfig.behavior?.reminderRules ?? defaultConfig.behavior?.reminderRules,
        archiveRule: mergeArchiveRule(defaultConfig.behavior?.archiveRule, storedConfig.behavior?.archiveRule)
      },
      ...(timeline
        ? {
            timeline: {
              enabled: storedConfig.timeline?.enabled ?? defaultConfig.timeline?.enabled ?? timeline.enabled,
              defaultWriteRule: {
                mode: storedConfig.timeline?.defaultWriteRule.mode ?? defaultConfig.timeline?.defaultWriteRule.mode ?? timeline.defaultWriteRule.mode,
                displayInOverview:
                  storedConfig.timeline?.defaultWriteRule.displayInOverview ??
                  defaultConfig.timeline?.defaultWriteRule.displayInOverview ??
                  timeline.defaultWriteRule.displayInOverview,
                aiReadable:
                  storedConfig.timeline?.defaultWriteRule.aiReadable ??
                  defaultConfig.timeline?.defaultWriteRule.aiReadable ??
                  timeline.defaultWriteRule.aiReadable
              }
            }
          }
        : {})
    };
  });
}

function readStoredAbilityConfigs(): AbilityInstanceConfig[] {
  if (typeof window === 'undefined') {
    return clone(mockAbilityInstanceConfigs);
  }

  const raw = window.localStorage.getItem(ABILITY_CONFIG_STORAGE_KEY);
  if (!raw) {
    return clone(mockAbilityInstanceConfigs);
  }

  try {
    const parsed = JSON.parse(raw) as AbilityInstanceConfig[];
    return Array.isArray(parsed) ? mergeAbilityConfigs(parsed) : clone(mockAbilityInstanceConfigs);
  } catch {
    return clone(mockAbilityInstanceConfigs);
  }
}

function writeStoredAbilityConfigs(configs: AbilityInstanceConfig[]) {
  if (typeof window === 'undefined') {
    return;
  }

  window.localStorage.setItem(ABILITY_CONFIG_STORAGE_KEY, JSON.stringify(configs));
}

function buildOverviewSummaries(): OverviewSummary[] {
  const abilityConfigs = readStoredAbilityConfigs();
  const overviewConfig = abilityConfigs.find(config => config.blockKey === 'overview');
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
        description: `今日还有 ${dailyTargets.length - doneCount} 项待完成`,
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
        description: '优先处理临近结束的活动目标',
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

    if (rule.id === 'sum-material-progress') {
      const activeMaterials = mockMaterialOverview.materials.filter(material => material.status !== 'archived');
      const completedCount = activeMaterials.filter(material => material.status === 'completed').length;
      return {
        id: 'overview-material-progress',
        ruleId: rule.id,
        title: rule.title,
        value: `${completedCount}/${activeMaterials.length}`,
        description: '套装、素材和代币收集进度',
        items: activeMaterials
          .filter(material => material.status !== 'completed')
          .slice(0, rule.maxItems)
          .map(material => ({ id: material.id, label: material.name, status: material.status, targetRoute: 'targets' })),
        targetRoute: 'targets'
      };
    }

    if (rule.id === 'sum-gallery-recent') {
      return {
        id: 'overview-gallery-recent',
        ruleId: rule.id,
        title: rule.title,
        value: `${mockGalleryOverview.photoCount}张`,
        description: `${mockGalleryOverview.albumCount} 个图册`,
        items: mockGalleryOverview.recentPhotos
          .slice(0, rule.maxItems)
          .map(photo => ({ id: photo.id, label: photo.caption ?? '照片记录', status: 'photo_uploaded', targetRoute: 'gallery' })),
        targetRoute: 'gallery'
      };
    }

    if (rule.id === 'sum-asset-risk') {
      const riskAssets = mockAssetOverview.assets.filter(asset => ['pending', 'expired'].includes(asset.status));
      return {
        id: 'overview-asset-risk',
        ruleId: rule.id,
        title: rule.title,
        value: riskAssets.length ? `${riskAssets.length}个` : '正常',
        description: riskAssets.length ? '存在待处理或过期资产' : '账号资产暂无异常',
        items: riskAssets
          .slice(0, rule.maxItems)
          .map(asset => ({ id: asset.id, label: asset.name, status: asset.status, targetRoute: 'assets' })),
        targetRoute: 'assets'
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

/** 切换游戏目标状态 */
export async function toggleGameTargetStatus(
  targetId: string,
  status: GameTarget['status']
): Promise<ApiResponse<{ id: string; status: GameTarget['status'] }>> {
  await delay(200);
  return wrap({ id: targetId, status });
}

/** 更新游戏目标进度 */
export async function updateGameTargetProgress(
  targetId: string,
  current: number
): Promise<ApiResponse<{ id: string; current: number }>> {
  await delay(200);
  return wrap({ id: targetId, current });
}

/** 获取项目时间轴 */
export async function fetchTimelineEvents(): Promise<ApiResponse<TimelineEvent[]>> {
  await delay();
  return wrap(clone(mockTimelineEvents));
}

/** 获取功能块实例配置 */
export async function fetchAbilityInstanceConfigs(): Promise<ApiResponse<AbilityInstanceConfig[]>> {
  await delay();
  return wrap(readStoredAbilityConfigs());
}

/** 保存功能块实例配置 */
export async function saveAbilityInstanceConfigs(configs: AbilityInstanceConfig[]): Promise<ApiResponse<AbilityInstanceConfig[]>> {
  await delay(120);
  const nextConfigs = clone(configs);
  writeStoredAbilityConfigs(nextConfigs);
  return wrap(nextConfigs);
}

/** 获取概览摘要 */
export async function fetchOverviewSummaries(): Promise<ApiResponse<OverviewSummary[]>> {
  await delay();
  return wrap(clone(buildOverviewSummaries()));
}

/** 获取素材/收集概览 */
export async function fetchMaterialOverview(): Promise<ApiResponse<MaterialOverview>> {
  await delay();
  return wrap(clone(mockMaterialOverview));
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
  const pendingTargets = mockGameTargets.filter(target => target.status === 'todo').length;
  const archiveReadyActivities = mockGameActivities.filter(activity => ['ending', 'pending_archive'].includes(activity.status));
  const collectingMaterials = mockMaterialOverview.materials.filter(material => material.status === 'collecting');
  return wrap({
    ...clone(mockAiOverview),
    currentSuggestion: {
      ...clone(mockAiOverview.currentSuggestion),
      highlights: [
        { text: `你还有 ${pendingTargets} 个任务目标未完成`, type: pendingTargets > 5 ? 'danger' : 'info' },
        { text: archiveReadyActivities.length ? `有 ${archiveReadyActivities.length} 个活动需要结束前处理或归档` : '当前没有临近结束的活动', type: archiveReadyActivities.length ? 'warning' : 'success' },
        { text: `素材收集中 ${collectingMaterials.length} 项，建议优先推进关联活动素材`, type: collectingMaterials.length ? 'warning' : 'success' },
        { text: '最近截图、笔记和活动记录已进入时间轴，可用于版本复盘', type: 'info' }
      ]
    }
  });
}

/** 刷新 AI 建议 */
export async function refreshAiSuggestion(): Promise<ApiResponse<AiOverview>> {
  await delay(800);
  return wrap(JSON.parse(JSON.stringify(mockAiOverview)));
}

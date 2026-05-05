/**
 * Infinity Nikki 项目服务层
 * 通过真实 HTTP 请求对接 ruoyi-life 后端模块
 */
import { request } from '@/service/request';
import type {
  AbilityInstanceConfig,
  AiOverview,
  AssetOverview,
  GameActivity,
  GameTarget,
  GameVersion,
  GalleryOverview,
  MaterialOverview,
  NikkiAsset,
  NikkiNote,
  NikkiProject,
  NoteOverview,
  TimelineEvent
} from './types';

const PROJECT_ID = '1001';
const EMPTY_TIMELINE_RULE = { mode: 'none', displayInOverview: false, aiReadable: false } as const;

// ========== 后端响应适配器 ==========

function parseJson<T>(val: T | string | undefined | null): T | undefined {
  if (val === undefined || val === null) return undefined;
  if (typeof val === 'string') {
    try {
      return JSON.parse(val) as T;
    } catch {
      return undefined;
    }
  }
  return val;
}

function charToBool(val: string | boolean | undefined): boolean {
  if (typeof val === 'boolean') return val;
  return val === '1' || val === 'Y' || val === 'true';
}

function boolToChar(val: boolean): string {
  return val ? '1' : '0';
}

function toJsonString(val: unknown): string | undefined {
  return val === undefined ? undefined : JSON.stringify(val);
}

function adaptProject(raw: any): NikkiProject {
  return {
    id: String(raw.projectId),
    name: raw.projectName,
    description: raw.description ?? '',
    coverSrc: raw.coverSrc ?? '',
    coverAlt: raw.coverAlt ?? '',
    status: raw.status,
    createdAt: raw.createTime ?? '',
    tags: parseJson(raw.tags) ?? [],
    stats: parseJson(raw.stats) ?? []
  };
}

function adaptVersion(raw: any): GameVersion {
  return {
    id: String(raw.versionId),
    projectId: String(raw.projectId),
    name: raw.name,
    title: raw.title,
    startAt: raw.startAt ?? '',
    endAt: raw.endAt ?? '',
    status: raw.status,
    highlights: parseJson(raw.highlights) ?? [],
    activityIds: [],
    summary: raw.summary,
    archivedAt: raw.archivedAt
  };
}

function adaptActivity(raw: any): GameActivity {
  return {
    id: String(raw.activityId),
    projectId: String(raw.projectId),
    versionId: String(raw.versionId),
    title: raw.title,
    description: raw.description,
    startAt: raw.startAt ?? '',
    endAt: raw.endAt ?? '',
    status: raw.status,
    priority: raw.priority ?? 'normal',
    cover: raw.cover,
    targetIds: parseJson<string[]>(raw.targetIds) ?? [],
    materialIds: parseJson<string[]>(raw.materialIds),
    photoIds: parseJson<string[]>(raw.photoIds),
    noteIds: parseJson<string[]>(raw.noteIds),
    reminderRule: parseJson(raw.reminderRule),
    archivedAt: raw.archivedAt
  };
}

function adaptTarget(raw: any): GameTarget {
  return {
    id: String(raw.targetId),
    projectId: String(raw.projectId),
    type: raw.type,
    title: raw.title,
    description: raw.description,
    status: raw.status,
    progressCurrent: raw.progressCurrent,
    progressTarget: raw.progressTarget,
    resetRule: parseJson(raw.resetRule),
    versionId: raw.versionId ? String(raw.versionId) : undefined,
    activityId: raw.activityId ? String(raw.activityId) : undefined,
    dueAt: raw.dueAt,
    priority: raw.priority ?? 'normal',
    pinnedToOverview: charToBool(raw.pinnedToOverview),
    timelineRule: parseJson(raw.timelineRule) ?? EMPTY_TIMELINE_RULE,
    archivedAt: raw.archivedAt
  };
}

function adaptTimelineEvent(raw: any): TimelineEvent {
  return {
    id: String(raw.eventId),
    projectId: String(raw.projectId),
    occurredAt: raw.occurredAt,
    type: raw.type,
    title: raw.title,
    description: raw.description,
    sourceBlockKey: raw.sourceBlockKey,
    versionId: raw.versionId ? String(raw.versionId) : undefined,
    activityId: raw.activityId ? String(raw.activityId) : undefined,
    targetId: raw.targetId ? String(raw.targetId) : undefined,
    sensitivity: raw.sensitivity ?? 'normal',
    displayInOverview: charToBool(raw.displayInOverview),
    aiReadable: charToBool(raw.aiReadable)
  };
}

function adaptAbilityConfig(raw: any): AbilityInstanceConfig {
  return {
    id: String(raw.configId),
    projectId: String(raw.projectId),
    blockKey: raw.blockKey,
    displayName: raw.displayName,
    enabled: charToBool(raw.enabled),
    capabilities: parseJson(raw.capabilities) ?? [],
    navigation: parseJson(raw.navigation) ?? { visible: true, order: 0 },
    summaryRules: parseJson(raw.summaryRules) ?? [],
    fields: parseJson(raw.fields),
    behavior: parseJson(raw.behavior),
    timeline: parseJson(raw.timeline),
    aiRules: parseJson(raw.aiRules),
    security: parseJson(raw.security)
  };
}

function adaptMaterial(raw: any) {
  return {
    id: String(raw.materialId),
    projectId: String(raw.projectId),
    name: raw.name,
    type: raw.type,
    description: raw.description,
    status: raw.status,
    current: raw.current ?? 0,
    target: raw.target ?? 0,
    versionId: raw.versionId ? String(raw.versionId) : undefined,
    activityId: raw.activityId ? String(raw.activityId) : undefined,
    targetId: raw.targetId ? String(raw.targetId) : undefined,
    timelineRule: parseJson(raw.timelineRule) ?? EMPTY_TIMELINE_RULE,
    note: raw.note,
    createdAt: raw.createTime ?? '',
    updatedAt: raw.updateTime ?? ''
  };
}

function adaptNote(raw: any): NikkiNote {
  return {
    id: String(raw.noteId),
    projectId: String(raw.projectId),
    title: raw.title,
    content: raw.content ?? '',
    type: raw.type ?? 'note',
    category: raw.category ?? 'note',
    categoryLabel: raw.categoryLabel ?? '',
    versionId: raw.versionId ? String(raw.versionId) : undefined,
    activityId: raw.activityId ? String(raw.activityId) : undefined,
    targetId: raw.targetId ? String(raw.targetId) : undefined,
    pinnedToOverview: charToBool(raw.pinnedToOverview),
    timelineRule: parseJson(raw.timelineRule) ?? EMPTY_TIMELINE_RULE,
    createdAt: raw.createTime ?? '',
    updatedAt: raw.updateTime ?? '',
    relativeTime: ''
  };
}

function adaptAsset(raw: any): NikkiAsset {
  return {
    id: String(raw.assetId),
    projectId: String(raw.projectId),
    name: raw.name,
    type: raw.type,
    category: raw.category,
    value: raw.value,
    status: raw.status,
    statusLabel: raw.statusLabel ?? '',
    sensitivity: raw.sensitivity ?? 'normal',
    icon: raw.icon,
    description: raw.description,
    linkedUrl: raw.linkedUrl,
    expiresAt: raw.expiresAt,
    notes: raw.notes,
    timelineRule: parseJson(raw.timelineRule) ?? EMPTY_TIMELINE_RULE,
    archivedAt: raw.archivedAt,
    createdAt: raw.createTime ?? '',
    updatedAt: raw.updateTime ?? ''
  };
}

function attachTargetIdsToActivities(activities: GameActivity[], targets: GameTarget[]): GameActivity[] {
  return activities.map(activity => {
    const relatedTargetIds = targets
      .filter(target => target.activityId === activity.id)
      .map(target => target.id);

    return {
      ...activity,
      targetIds: Array.from(new Set([...activity.targetIds, ...relatedTargetIds]))
    };
  });
}

function toAbilityConfigBo(config: AbilityInstanceConfig) {
  return {
    configId: Number(config.id) || undefined,
    projectId: Number(config.projectId) || Number(PROJECT_ID),
    blockKey: config.blockKey,
    displayName: config.displayName,
    enabled: boolToChar(config.enabled),
    capabilities: toJsonString(config.capabilities),
    navigation: toJsonString(config.navigation),
    summaryRules: toJsonString(config.summaryRules),
    fields: toJsonString(config.fields),
    behavior: toJsonString(config.behavior),
    timeline: toJsonString(config.timeline),
    aiRules: toJsonString(config.aiRules),
    security: toJsonString(config.security)
  };
}

// ========== 请求+适配封装 ==========

function get<T>(url: string): Promise<T> {
  return request<any>({ url, method: 'get' }).then(res => res.data as T);
}

// ========== 项目基础信息 ==========

/** 获取项目基础信息 */
export function fetchProject() {
  return get<any>(`/life/project/${PROJECT_ID}`).then(adaptProject);
}

// ========== 游戏闭环模型 ==========

/** 获取游戏版本列表 */
export function fetchGameVersions() {
  return get<any[]>(`/life/project/${PROJECT_ID}/game-versions`).then(list => list.map(adaptVersion));
}

/** 获取当前游戏版本 */
export function fetchCurrentGameVersion() {
  return get<any>(`/life/project/${PROJECT_ID}/game-versions/current`).then(raw => raw ? adaptVersion(raw) : undefined);
}

/** 获取版本活动列表 */
export function fetchGameActivities() {
  return get<any[]>(`/life/project/${PROJECT_ID}/game-activities`).then(list => list.map(adaptActivity));
}

/** 获取游戏目标列表 */
export function fetchGameTargets() {
  return get<any[]>(`/life/project/${PROJECT_ID}/targets`).then(list => list.map(adaptTarget));
}

/** 切换游戏目标状态 */
export function toggleGameTargetStatus(targetId: string, status: GameTarget['status']) {
  return request<boolean>({
    url: `/life/project/${PROJECT_ID}/targets/${targetId}/status`,
    method: 'patch',
    data: { status }
  }).then(() => ({ id: targetId, status }));
}

/** 更新游戏目标进度 */
export function updateGameTargetProgress(targetId: string, current: number) {
  return request<boolean>({
    url: `/life/project/${PROJECT_ID}/targets/${targetId}/progress`,
    method: 'patch',
    data: { current }
  }).then(() => ({ id: targetId, current }));
}

/** 获取项目时间轴 */
export function fetchTimelineEvents() {
  return get<any[]>(`/life/project/${PROJECT_ID}/timeline-events`).then(list => list.map(adaptTimelineEvent));
}

/** 获取功能块实例配置 */
export function fetchAbilityInstanceConfigs() {
  return get<any[]>(`/life/project/${PROJECT_ID}/ability-configs`).then(list => list.map(adaptAbilityConfig));
}

/** 保存功能块实例配置 */
export function saveAbilityInstanceConfigs(configs: AbilityInstanceConfig[]) {
  return request<void>({
    url: `/life/project/${PROJECT_ID}/ability-configs`,
    method: 'put',
    data: configs.map(toAbilityConfigBo)
  }).then(() => fetchAbilityInstanceConfigs());
}

/** 获取概览摘要 */
export function fetchOverviewSummaries() {
  return get<any[]>(`/life/project/${PROJECT_ID}/overview-summaries`).then(list =>
    (list ?? []).map((raw: any) => ({
      id: String(raw.id ?? ''),
      ruleId: String(raw.ruleId ?? ''),
      title: raw.title ?? '',
      value: raw.value,
      description: raw.description,
      items: parseJson(raw.items) ?? [],
      targetRoute: raw.targetRoute
    }))
  );
}

/** 获取素材/收集概览 */
export function fetchMaterialOverview() {
  return get<any>(`/life/project/${PROJECT_ID}/materials`).then(data => {
    const list = data?.list ?? data?.records ?? [];
    const materials = list.map(adaptMaterial);
    const completed = materials.filter((m: any) => m.status === 'completed').length;
    return { total: data?.total ?? materials.length, completed, materials } as MaterialOverview;
  });
}

// ========== 资产 ==========

/** 获取资产概览 */
export function fetchAssetOverview() {
  return get<any>(`/life/project/${PROJECT_ID}/assets`).then(data => {
    const list = data?.list ?? data?.records ?? [];
    const assets = list.map(adaptAsset);
    return { total: data?.total ?? assets.length, assets } as AssetOverview;
  });
}

/** 获取资产详情 */
export function fetchAssetDetail(assetId: string) {
  return get<any>(`/life/project/${PROJECT_ID}/assets/${assetId}`).then(raw => raw ? adaptAsset(raw) : undefined);
}

// ========== 笔记 ==========

/** 获取笔记概览 */
export function fetchNoteOverview() {
  return get<any>(`/life/project/${PROJECT_ID}/notes`).then(data => {
    const list = data?.list ?? data?.records ?? [];
    const notes = list.map(adaptNote);
    return { total: data?.total ?? notes.length, notes } as NoteOverview;
  });
}

/** 获取笔记详情 */
export function fetchNoteDetail(noteId: string) {
  return get<any>(`/life/project/${PROJECT_ID}/notes/${noteId}`).then(raw => raw ? adaptNote(raw) : undefined);
}

// ========== 图册 ==========

/** 获取图册概览 */
export function fetchGalleryOverview() {
  return get<any>(`/life/project/${PROJECT_ID}/gallery`).then(data => {
    const albums = (data?.albums ?? []).map((a: any) => ({
      id: String(a.albumId),
      name: a.name,
      coverUrl: a.coverUrl ?? '',
      photoCount: a.photoCount ?? 0
    }));
    const recentPhotos = (data?.photos ?? []).map((p: any) => ({
      id: String(p.photoId),
      albumId: String(p.albumId),
      url: p.url,
      thumbnail: p.thumbnail ?? '',
      caption: p.caption,
      takenAt: p.takenAt ?? '',
      versionId: p.versionId ? String(p.versionId) : undefined,
      activityId: p.activityId ? String(p.activityId) : undefined,
      targetId: p.targetId ? String(p.targetId) : undefined
    }));
    return {
      albumCount: albums.length,
      photoCount: recentPhotos.length,
      albums,
      recentPhotos
    } as GalleryOverview;
  });
}

// ========== AI 建议 ==========

/** 获取 AI 建议 */
export function fetchAiOverview() {
  return get<any>(`/life/project/${PROJECT_ID}/ai/overview`).then(data => ({
    currentSuggestion: {
      id: 'ai-placeholder',
      content: data?.currentSuggestion ?? '',
      highlights: (data?.highlights ?? []).map((h: any) => ({
        text: h.text ?? String(h),
        type: h.type ?? 'info'
      })),
      generatedAt: new Date().toISOString(),
      status: 'generated' as const
    },
    suggestionCount: data?.suggestionCount ?? 0
  }) as AiOverview);
}

/** 刷新 AI 建议 */
export function refreshAiSuggestion() {
  return request<any>({
    url: `/life/project/${PROJECT_ID}/ai/summaries`,
    method: 'post'
  }).then(res => {
    const data = res.data;
    return {
      currentSuggestion: {
        id: 'ai-placeholder',
        content: data?.currentSuggestion ?? '',
        highlights: (data?.highlights ?? []).map((h: any) => ({
          text: h.text ?? String(h),
          type: h.type ?? 'info'
        })),
        generatedAt: new Date().toISOString(),
        status: 'generated' as const
      },
      suggestionCount: data?.suggestionCount ?? 0
    } as AiOverview;
  });
}

export { attachTargetIdsToActivities };

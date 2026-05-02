<script setup lang="ts">
import { computed, onMounted, reactive, ref } from 'vue';
import { ChevronRight, MoreHorizontal, Plus, Settings, Share, Star } from 'lucide-vue-next';
import { useMessage } from 'naive-ui';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import { useRouterPush } from '@/hooks/common/router';
import type {
  AiOverview,
  AssetOverview,
  AbilityBlockKey,
  AbilityInstanceConfig,
  DetailModalData,
  GalleryOverview,
  GameActivity,
  GameTarget,
  GameTargetStatus,
  GameVersion,
  MaterialOverview,
  NikkiAsset,
  NikkiPhoto,
  NikkiProject,
  NoteOverview,
  OverviewSummary,
  TimelineEvent
} from './types';
import {
  fetchAbilityInstanceConfigs,
  fetchAiOverview,
  fetchAssetOverview,
  fetchGalleryOverview,
  fetchGameActivities,
  fetchGameTargets,
  fetchGameVersions,
  fetchMaterialOverview,
  fetchNoteOverview,
  fetchOverviewSummaries,
  fetchProject,
  fetchTimelineEvents,
  toggleGameTargetStatus as apiToggleGameTargetStatus,
  updateGameTargetProgress as apiUpdateGameTargetProgress
} from './service';
import OverviewTab from './tabs/OverviewTab.vue';
import DailyTab from './tabs/DailyTab.vue';
import CountdownTab from './tabs/CountdownTab.vue';
import AssetsTab from './tabs/AssetsTab.vue';
import GalleryTab from './tabs/GalleryTab.vue';
import TimelineTab from './tabs/TimelineTab.vue';

defineOptions({
  name: 'InfinityNikki'
});

const message = useMessage();
const { routerPushByKey } = useRouterPush();

// ========== 状态 ==========

const activeTab = ref('概览');
const fallbackTabs = ['概览', '任务', '活动与版本', '图册', '时间轴', '账号资产'];

const project = ref<NikkiProject>({
  id: '',
  name: '无限暖暖',
  description: '',
  coverSrc: '',
  coverAlt: '',
  status: 'active',
  createdAt: '',
  tags: [],
  stats: []
});

const assetData = ref<AssetOverview>({
  total: 0,
  assets: []
});

const noteData = ref<NoteOverview>({
  total: 0,
  notes: []
});

const galleryData = ref<GalleryOverview>({
  albumCount: 0,
  photoCount: 0,
  albums: [],
  recentPhotos: []
});

const materialData = ref<MaterialOverview>({
  total: 0,
  completed: 0,
  materials: []
});

const aiData = ref<AiOverview>({
  currentSuggestion: {
    id: '',
    content: '',
    highlights: [],
    generatedAt: '',
    status: 'pending'
  },
  suggestionCount: 0
});

const overviewSummaries = ref<OverviewSummary[]>([]);
const abilityConfigs = ref<AbilityInstanceConfig[]>([]);
const gameTargets = ref<GameTarget[]>([]);
const gameVersions = ref<GameVersion[]>([]);
const gameActivities = ref<GameActivity[]>([]);
const timelineEvents = ref<TimelineEvent[]>([]);
const tabs = computed(() => {
  const visibleTabs = abilityConfigs.value
    .filter(config => config.enabled && config.navigation.visible)
    .sort((prev, next) => prev.navigation.order - next.navigation.order)
    .map(config => config.displayName);

  return visibleTabs.length ? visibleTabs : fallbackTabs;
});

// ========== 弹框状态 ==========

const showQuickRecord = ref(false);
const quickRecordContent = ref('');

const detailModal = reactive<DetailModalData>({
  show: false,
  title: '',
  type: '',
  icon: '',
  meta: [],
  content: ''
});

// ========== 数据获取 ==========

async function fetchProjectData() {
  const res = await fetchProject();
  project.value = res.data;
}

async function fetchAssets() {
  const res = await fetchAssetOverview();
  assetData.value = res.data;
}

async function fetchNotes() {
  const res = await fetchNoteOverview();
  noteData.value = res.data;
}

async function fetchGallery() {
  const res = await fetchGalleryOverview();
  galleryData.value = res.data;
}

async function fetchMaterials() {
  const res = await fetchMaterialOverview();
  materialData.value = res.data;
}

async function fetchAi() {
  const res = await fetchAiOverview();
  aiData.value = res.data;
}

async function fetchOverview() {
  const res = await fetchOverviewSummaries();
  overviewSummaries.value = res.data;
}

async function fetchAbilityConfigs() {
  const res = await fetchAbilityInstanceConfigs();
  abilityConfigs.value = res.data;
}

async function fetchTargets() {
  const res = await fetchGameTargets();
  gameTargets.value = res.data;
}

async function fetchVersionActivity() {
  const [versionsRes, activitiesRes] = await Promise.all([
    fetchGameVersions(),
    fetchGameActivities()
  ]);
  gameVersions.value = versionsRes.data;
  gameActivities.value = activitiesRes.data;
}

async function fetchTimeline() {
  const res = await fetchTimelineEvents();
  timelineEvents.value = res.data;
}

function getAbilityConfig(blockKey: AbilityBlockKey) {
  return abilityConfigs.value.find(config => config.blockKey === blockKey);
}

// ========== 事件处理 ==========

function patchTargetSummaries(targets: GameTarget[]) {
  overviewSummaries.value = overviewSummaries.value.map(summary => {
    if (summary.ruleId === 'sum-today-targets') {
      const dailyTargets = targets.filter(target => target.type === 'daily');
      const doneCount = dailyTargets.filter(target => target.status === 'done').length;
      return {
        ...summary,
        value: `${doneCount}/${dailyTargets.length}`,
        description: `今日还有 ${dailyTargets.length - doneCount} 项待完成`,
        items: dailyTargets
          .filter(target => target.status !== 'done')
          .slice(0, 3)
          .map(target => ({ id: target.id, label: target.title, status: target.status, targetRoute: 'targets' }))
      };
    }

    if (summary.ruleId === 'sum-weekly-targets') {
      const weeklyTargets = targets.filter(target => target.type === 'weekly');
      const doneCount = weeklyTargets.filter(target => target.status === 'done').length;
      return {
        ...summary,
        value: `${doneCount}/${weeklyTargets.length}`,
        items: weeklyTargets
          .slice(0, 3)
          .map(target => ({ id: target.id, label: target.title, status: target.status, targetRoute: 'targets' }))
      };
    }

    return summary;
  });
}

function patchActivitySummaries(activitiesData: GameActivity[]) {
  overviewSummaries.value = overviewSummaries.value.map(summary => {
    if (summary.ruleId !== 'sum-ending-activities') {
      return summary;
    }

    const endingActivities = activitiesData.filter(activity => ['ending', 'pending_archive'].includes(activity.status));
    return {
      ...summary,
      value: `${endingActivities.length}个`,
      items: endingActivities
        .slice(0, 2)
        .map(activity => ({ id: activity.id, label: activity.title, status: activity.status, targetRoute: 'version_activity' }))
    };
  });
}

function patchTimelineSummaries(events: TimelineEvent[]) {
  overviewSummaries.value = overviewSummaries.value.map(summary => {
    if (summary.ruleId !== 'sum-recent-timeline') {
      return summary;
    }

    const visibleEvents = [...events]
      .filter(event => event.displayInOverview)
      .sort((prev, next) => Date.parse(next.occurredAt) - Date.parse(prev.occurredAt));

    return {
      ...summary,
      value: `${visibleEvents.length}条`,
      items: visibleEvents
        .slice(0, 3)
        .map(event => ({ id: event.id, label: event.title, status: event.type, targetRoute: 'timeline' }))
    };
  });
}

function patchGallerySummaries(data: GalleryOverview) {
  overviewSummaries.value = overviewSummaries.value.map(summary => {
    if (summary.ruleId !== 'sum-gallery-recent') {
      return summary;
    }

    return {
      ...summary,
      value: `${data.photoCount}张`,
      description: `${data.albumCount} 个图册`,
      items: data.recentPhotos
        .slice(0, 3)
        .map(photo => ({ id: photo.id, label: photo.caption ?? '照片记录', status: 'photo_uploaded', targetRoute: 'gallery' }))
    };
  });
}

function patchAssetSummaries(data: AssetOverview) {
  overviewSummaries.value = overviewSummaries.value.map(summary => {
    if (summary.ruleId !== 'sum-asset-risk') {
      return summary;
    }

    const riskAssets = data.assets.filter(asset => ['pending', 'expired'].includes(asset.status));
    return {
      ...summary,
      value: riskAssets.length ? `${riskAssets.length}个` : '正常',
      description: riskAssets.length ? '存在待处理或过期资产' : '账号资产暂无异常',
      items: riskAssets
        .slice(0, 2)
        .map(asset => ({ id: asset.id, label: asset.name, status: asset.status, targetRoute: 'assets' }))
    };
  });
}

function handleCreateGameTarget(target: GameTarget) {
  gameTargets.value = [target, ...gameTargets.value];
  if (target.type === 'activity' && target.activityId) {
    gameActivities.value = gameActivities.value.map(activity => {
      if (activity.id !== target.activityId || activity.targetIds.includes(target.id)) {
        return activity;
      }

      return {
        ...activity,
        targetIds: [...activity.targetIds, target.id]
      };
    });
  }
  patchTargetSummaries(gameTargets.value);
  handleAppendTimeline({
    id: `target-created-${Date.now()}`,
    projectId: target.projectId,
    occurredAt: new Date().toISOString(),
    type: 'note_created',
    title: '新增任务目标',
    description: target.title,
    sourceBlockKey: 'targets',
    targetId: target.id,
    sensitivity: 'normal',
    displayInOverview: target.timelineRule.displayInOverview,
    aiReadable: target.timelineRule.aiReadable
  });
  message.success('目标已添加');
}

function handleUpdateGameTarget(updatedTarget: GameTarget) {
  gameTargets.value = gameTargets.value.map(target => (target.id === updatedTarget.id ? updatedTarget : target));
  patchTargetSummaries(gameTargets.value);
  message.success('目标已更新');
}

function handleDeleteGameTarget(targetId: string) {
  const target = gameTargets.value.find(item => item.id === targetId);
  gameTargets.value = gameTargets.value.filter(item => item.id !== targetId);
  gameActivities.value = gameActivities.value.map(activity => {
    if (!activity.targetIds.includes(targetId)) {
      return activity;
    }

    return {
      ...activity,
      targetIds: activity.targetIds.filter(id => id !== targetId)
    };
  });
  patchTargetSummaries(gameTargets.value);
  if (target) {
    handleAppendTimeline({
      id: `target-deleted-${Date.now()}`,
      projectId: target.projectId,
      occurredAt: new Date().toISOString(),
      type: 'note_created',
      title: '删除任务目标',
      description: target.title,
      sourceBlockKey: 'targets',
      versionId: target.versionId,
      activityId: target.activityId,
      targetId: target.id,
      sensitivity: 'normal',
      displayInOverview: false,
      aiReadable: true
    });
  }
  message.success('目标已删除');
}

async function handleToggleGameTargetStatus(targetId: string, status: GameTargetStatus) {
  await apiToggleGameTargetStatus(targetId, status);
  const originalTarget = gameTargets.value.find(target => target.id === targetId);
  const nextTargets = gameTargets.value.map(target => {
    if (target.id !== targetId) {
      return target;
    }

    return {
      ...target,
      status,
      progressCurrent: status === 'done' ? (target.progressTarget ?? target.progressCurrent) : target.progressCurrent
    };
  });
  gameTargets.value = nextTargets;
  patchTargetSummaries(nextTargets);
  if (originalTarget && ['done', 'skipped', 'expired', 'archived'].includes(status)) {
    const timelineTypeMap: Partial<Record<GameTargetStatus, TimelineEvent['type']>> = {
      done: 'target_done',
      skipped: 'target_skipped',
      expired: 'target_expired'
    };
    handleAppendTimeline({
      id: `target-${status}-${Date.now()}`,
      projectId: originalTarget.projectId,
      occurredAt: new Date().toISOString(),
      type: timelineTypeMap[status] ?? 'note_created',
      title: status === 'done' ? '完成任务目标' : status === 'skipped' ? '跳过任务目标' : status === 'expired' ? '任务目标过期' : '归档任务目标',
      description: originalTarget.title,
      sourceBlockKey: 'targets',
      versionId: originalTarget.versionId,
      activityId: originalTarget.activityId,
      targetId: originalTarget.id,
      sensitivity: 'normal',
      displayInOverview: originalTarget.timelineRule.displayInOverview && status !== 'archived',
      aiReadable: originalTarget.timelineRule.aiReadable
    });
  }
  const statusTextMap: Record<GameTargetStatus, string> = {
    todo: '目标已恢复',
    done: '目标已完成',
    skipped: '目标已跳过',
    expired: '目标已标记过期',
    archived: '目标已归档'
  };
  message.success(statusTextMap[status]);
}

async function handleUpdateGameTargetProgress(targetId: string, current: number) {
  await apiUpdateGameTargetProgress(targetId, current);
  const nextTargets = gameTargets.value.map(target => {
    if (target.id !== targetId) {
      return target;
    }

    const nextStatus: GameTargetStatus =
      target.progressTarget && current >= target.progressTarget ? 'done' : target.status === 'done' ? 'todo' : target.status;
    return {
      ...target,
      status: nextStatus,
      progressCurrent: current
    };
  });
  gameTargets.value = nextTargets;
  patchTargetSummaries(nextTargets);
  message.success('目标进度已更新');
}

function handleCreateGameVersion(version: GameVersion) {
  gameVersions.value = [version, ...gameVersions.value];
  message.success('版本已添加');
}

function handleCreateGameActivity(activity: GameActivity) {
  gameActivities.value = [activity, ...gameActivities.value];
  gameVersions.value = gameVersions.value.map(version => {
    if (version.id !== activity.versionId) {
      return version;
    }

    return {
      ...version,
      activityIds: [...version.activityIds, activity.id]
    };
  });
  patchActivitySummaries(gameActivities.value);
  handleAppendTimeline({
    id: `activity-created-${Date.now()}`,
    projectId: activity.projectId,
    occurredAt: new Date().toISOString(),
    type: 'activity_started',
    title: '新增版本活动',
    description: activity.title,
    sourceBlockKey: 'version_activity',
    versionId: activity.versionId,
    activityId: activity.id,
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  });
  message.success('活动已添加');
}

function handleToggleGameActivityReminder(activityId: string, enabled: boolean) {
  gameActivities.value = gameActivities.value.map(activity => {
    if (activity.id !== activityId) {
      return activity;
    }

    return {
      ...activity,
      reminderRule: {
        enabled,
        channels: activity.reminderRule?.channels ?? ['app'],
        beforeMinutes: activity.reminderRule?.beforeMinutes ?? [1440]
      }
    };
  });
  message.success(enabled ? '活动提醒已开启' : '活动提醒已关闭');
}

function handleArchiveGameActivity(activityId: string) {
  const archivedAt = new Date().toISOString();
  const archivedActivity = gameActivities.value.find(activity => activity.id === activityId);
  gameActivities.value = gameActivities.value.map(activity => {
    if (activity.id !== activityId) {
      return activity;
    }

    return {
      ...activity,
      status: 'archived',
      archivedAt
    };
  });
  patchActivitySummaries(gameActivities.value);
  if (archivedActivity) {
    handleAppendTimeline({
      id: `activity-archived-${Date.now()}`,
      projectId: archivedActivity.projectId,
      occurredAt: archivedAt,
      type: 'activity_archived',
      title: `归档活动：${archivedActivity.title}`,
      description: `保留 ${archivedActivity.targetIds.length} 个目标、${archivedActivity.photoIds?.length ?? 0} 张截图和 ${archivedActivity.noteIds?.length ?? 0} 条笔记。`,
      sourceBlockKey: 'version_activity',
      versionId: archivedActivity.versionId,
      activityId: archivedActivity.id,
      sensitivity: 'normal',
      displayInOverview: true,
      aiReadable: true
    });
  }
  message.success('活动已归档');
}

function handleArchiveGameVersion(versionId: string) {
  const archivedAt = new Date().toISOString();
  const archivedVersion = gameVersions.value.find(version => version.id === versionId);
  const relatedActivities = gameActivities.value.filter(activity => activity.versionId === versionId);
  const relatedTargets = gameTargets.value.filter(target => target.versionId === versionId);
  gameVersions.value = gameVersions.value.map(version => {
    if (version.id !== versionId) {
      return version;
    }

    return {
      ...version,
      status: 'archived',
      archivedAt
    };
  });
  gameActivities.value = gameActivities.value.map(activity => {
    if (activity.versionId !== versionId) {
      return activity;
    }

    return {
      ...activity,
      status: 'archived',
      archivedAt
    };
  });
  gameTargets.value = gameTargets.value.map(target => {
    if (target.versionId !== versionId) {
      return target;
    }

    return {
      ...target,
      status: 'archived',
      archivedAt
    };
  });
  patchTargetSummaries(gameTargets.value);
  patchActivitySummaries(gameActivities.value);
  if (archivedVersion) {
    handleAppendTimeline({
      id: `version-archived-${Date.now()}`,
      projectId: archivedVersion.projectId,
      occurredAt: archivedAt,
      type: 'version_archived',
      title: `归档版本：${archivedVersion.title}`,
      description: `汇总 ${relatedActivities.length} 个活动和 ${relatedTargets.length} 个目标，作为下个版本前的回顾记录。`,
      sourceBlockKey: 'version_activity',
      versionId: archivedVersion.id,
      sensitivity: 'normal',
      displayInOverview: true,
      aiReadable: true
    });
  }
  message.success('版本已归档');
}

function handleCreateGalleryPhoto(photo: NikkiPhoto) {
  const nextGalleryData = {
    ...galleryData.value,
    photoCount: galleryData.value.photoCount + 1,
    albums: galleryData.value.albums.map(album => {
      if (album.id !== photo.albumId) {
        return album;
      }

      return {
        ...album,
        photoCount: album.photoCount + 1,
        coverUrl: photo.thumbnail || album.coverUrl
      };
    }),
    recentPhotos: [photo, ...galleryData.value.recentPhotos]
  };
  galleryData.value = nextGalleryData;
  patchGallerySummaries(nextGalleryData);
  message.success('照片已添加');
}

function adjustAlbumPhotoCount(albumId: string, delta: number) {
  return galleryData.value.albums.map(album => {
    if (album.id !== albumId) {
      return album;
    }

    return {
      ...album,
      photoCount: Math.max(0, album.photoCount + delta)
    };
  });
}

function handleUpdateGalleryPhoto(photo: NikkiPhoto) {
  const oldPhoto = galleryData.value.recentPhotos.find(item => item.id === photo.id);
  let albums = galleryData.value.albums;
  if (oldPhoto && oldPhoto.albumId !== photo.albumId) {
    albums = albums.map(album => {
      if (album.id === oldPhoto.albumId) {
        return { ...album, photoCount: Math.max(0, album.photoCount - 1) };
      }
      if (album.id === photo.albumId) {
        return { ...album, photoCount: album.photoCount + 1, coverUrl: photo.thumbnail || album.coverUrl };
      }
      return album;
    });
  }
  const nextGalleryData = {
    ...galleryData.value,
    albums,
    recentPhotos: galleryData.value.recentPhotos.map(item => (item.id === photo.id ? photo : item))
  };
  galleryData.value = nextGalleryData;
  patchGallerySummaries(nextGalleryData);
  message.success('照片已更新');
}

function handleDeleteGalleryPhoto(photoId: string) {
  const photo = galleryData.value.recentPhotos.find(item => item.id === photoId);
  const nextGalleryData = {
    ...galleryData.value,
    photoCount: Math.max(0, galleryData.value.photoCount - (photo ? 1 : 0)),
    albums: photo ? adjustAlbumPhotoCount(photo.albumId, -1) : galleryData.value.albums,
    recentPhotos: galleryData.value.recentPhotos.filter(item => item.id !== photoId)
  };
  galleryData.value = nextGalleryData;
  patchGallerySummaries(nextGalleryData);
  message.success('照片已删除');
}

function handleCreateAsset(asset: NikkiAsset) {
  const nextAssetData = {
    total: assetData.value.total + 1,
    assets: [asset, ...assetData.value.assets]
  };
  assetData.value = nextAssetData;
  patchAssetSummaries(nextAssetData);
  message.success('资产已添加');
}

function handleUpdateAsset(asset: NikkiAsset) {
  const nextAssetData = {
    total: assetData.value.total,
    assets: assetData.value.assets.map(item => (item.id === asset.id ? asset : item))
  };
  assetData.value = nextAssetData;
  patchAssetSummaries(nextAssetData);
  message.success('资产已更新');
}

function handleUpdateAssetStatus(assetId: string, status: NikkiAsset['status']) {
  const statusLabelMap: Record<NikkiAsset['status'], string> = {
    protected: '已保护',
    bound: '已绑定',
    pending: '待处理',
    expired: '已过期',
    archived: '已归档'
  };
  const nextAssetData = {
    ...assetData.value,
    assets: assetData.value.assets.map(asset => {
      if (asset.id !== assetId) {
        return asset;
      }

      return {
        ...asset,
        status,
        statusLabel: statusLabelMap[status],
        ...(status === 'archived' ? { archivedAt: new Date().toISOString() } : {})
      };
    })
  };
  assetData.value = nextAssetData;
  patchAssetSummaries(nextAssetData);
  message.success('资产状态已更新');
}

function handleDeleteAsset(assetId: string) {
  const nextAssetData = {
    total: Math.max(0, assetData.value.total - 1),
    assets: assetData.value.assets.filter(asset => asset.id !== assetId)
  };
  assetData.value = nextAssetData;
  patchAssetSummaries(nextAssetData);
  message.success('资产已删除');
}

function handleOpenDetail(payload: { type: string; id: string }) {
  const { type, id } = payload;

  if (type === 'asset') {
    const asset = assetData.value.assets.find(a => a.id === id);
    if (asset) {
      detailModal.title = asset.name;
      detailModal.content = asset.description ?? '';
      detailModal.meta = [
        { label: '安全状态', value: asset.statusLabel },
        { label: '资产类型', value: asset.type }
      ];
      detailModal.show = true;
    }
  } else if (type === 'note') {
    const note = noteData.value.notes.find(n => n.id === id);
    if (note) {
      detailModal.title = note.title;
      detailModal.content = note.content ?? '';
      detailModal.meta = [
        { label: '分类', value: note.categoryLabel },
        { label: '创建时间', value: note.createdAt },
        { label: '更新时间', value: note.updatedAt }
      ];
      detailModal.show = true;
    }
  } else if (type === 'photo') {
    const photo = galleryData.value.recentPhotos.find(p => p.id === id);
    if (photo) {
      detailModal.title = photo.caption ?? '照片详情';
      detailModal.meta = [
        { label: '拍摄时间', value: photo.takenAt }
      ];
      detailModal.show = true;
    }
  } else if (type === 'gameVersion') {
    const version = gameVersions.value.find(item => item.id === id);
    if (version) {
      detailModal.title = version.title;
      detailModal.content = version.summary ?? version.highlights.join('、');
      detailModal.meta = [
        { label: '版本号', value: version.name },
        { label: '开始时间', value: version.startAt },
        { label: '结束时间', value: version.endAt },
        { label: '状态', value: version.status }
      ];
      detailModal.show = true;
    }
  } else if (type === 'gameActivity') {
    const activity = gameActivities.value.find(item => item.id === id);
    if (activity) {
      detailModal.title = activity.title;
      detailModal.content = activity.description ?? '';
      detailModal.meta = [
        { label: '开始时间', value: activity.startAt },
        { label: '结束时间', value: activity.endAt },
        { label: '状态', value: activity.status },
        { label: '关联目标', value: `${activity.targetIds.length} 个` }
      ];
      detailModal.show = true;
    }
  } else if (type === 'timeline') {
    const item = timelineEvents.value.find(event => event.id === id);
    if (item) {
      detailModal.title = item.title;
      detailModal.content = item.description ?? '';
      detailModal.meta = [
        { label: '发生时间', value: item.occurredAt },
        { label: '来源功能块', value: item.sourceBlockKey },
        { label: 'AI 可读', value: item.aiReadable ? '是' : '否' }
      ];
      detailModal.show = true;
    }
  } else if (type === 'ai') {
    detailModal.title = 'AI 项目建议';
    detailModal.content = aiData.value.currentSuggestion.content;
    detailModal.meta = [
      { label: '建议数量', value: `${aiData.value.suggestionCount} 条` },
      { label: '生成时间', value: aiData.value.currentSuggestion.generatedAt || '未生成' },
      { label: '状态', value: aiData.value.currentSuggestion.status }
    ];
    detailModal.show = true;
  }
}

function handleShare() {
  message.info('分享链接已复制到剪贴板');
}

function handleSettings() {
  routerPushByKey('infinity-nikki-manage');
}

function handleAppendTimeline(event: TimelineEvent) {
  timelineEvents.value = [event, ...timelineEvents.value];
  patchTimelineSummaries(timelineEvents.value);
}

function handleQuickRecordConfirm() {
  if (!quickRecordContent.value.trim()) {
    message.warning('请先输入记录内容');
    return;
  }
  const newEvent: TimelineEvent = {
    id: `local-${Date.now()}`,
    projectId: project.value.id || 'nikki-001',
    occurredAt: new Date().toISOString(),
    type: 'note_created',
    title: '快速记录',
    description: quickRecordContent.value.trim(),
    sourceBlockKey: 'timeline',
    versionId: gameVersions.value.find(version => version.status === 'active')?.id,
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  };
  handleAppendTimeline(newEvent);
  message.success('记录已保存');
  quickRecordContent.value = '';
  showQuickRecord.value = false;
}

// ========== 初始化 ==========

onMounted(() => {
  fetchProjectData();
  fetchAssets();
  fetchNotes();
  fetchGallery();
  fetchMaterials();
  fetchAi();
  fetchAbilityConfigs();
  fetchOverview();
  fetchTargets();
  fetchVersionActivity();
  fetchTimeline();
});
</script>

<template>
  <LifeGeminiShell
    title="无限暖暖"
    description="项目概览、任务目标、活动版本和长期时间轴。"
    :breadcrumbs="[
      { label: '首页', routeKey: 'home' },
      { label: '项目', routeKey: 'projects' },
      { label: '无限暖暖' }
    ]"
  >
    <LifeGeminiProjectHero
      :title="project.name"
      :description="project.description"
      :cover-src="project.coverSrc"
      :cover-alt="project.coverAlt"
      :tags="project.tags"
      :stats="project.stats"
    >
      <template #title-icon>
        <Star class="text-amber-400 fill-amber-400 w-5 h-5" />
      </template>

      <template #actions>
        <button
          class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-xl font-medium text-sm flex items-center space-x-1.5"
          type="button"
          @click="showQuickRecord = true"
        >
          <Plus :size="16" />
          <span>快速记录</span>
          <ChevronRight :size="14" class="ml-1 opacity-70" />
        </button>
        <button
          class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5"
          type="button"
          @click="handleShare"
        >
          <Share :size="14" />
          <span>分享</span>
        </button>
        <button
          class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5"
          type="button"
          @click="handleSettings"
        >
          <Settings :size="14" />
          <span>设置</span>
        </button>
        <button
          class="w-10 h-10 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 flex items-center justify-center"
          type="button"
        >
          <MoreHorizontal :size="16" />
        </button>
      </template>
    </LifeGeminiProjectHero>

    <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

    <!-- 概览 -->
    <OverviewTab
      v-if="activeTab === '概览'"
      :asset-data="assetData"
      :note-data="noteData"
      :gallery-data="galleryData"
      :material-data="materialData"
      :ai-data="aiData"
      :timeline-events="timelineEvents"
      :overview-summaries="overviewSummaries"
      @switch-tab="activeTab = $event"
      @refresh-ai="fetchAi"
      @open-detail="handleOpenDetail"
    />

    <!-- 任务 -->
    <DailyTab
      v-if="activeTab === '任务'"
      :game-targets="gameTargets"
      :game-activities="gameActivities"
      :ability-config="getAbilityConfig('targets')"
      @create-target="handleCreateGameTarget"
      @update-target="handleUpdateGameTarget"
      @delete-target="handleDeleteGameTarget"
      @toggle-target-status="handleToggleGameTargetStatus"
      @update-target-progress="handleUpdateGameTargetProgress"
    />

    <!-- 活动与版本 -->
    <CountdownTab
      v-if="activeTab === '活动与版本'"
      :game-versions="gameVersions"
      :game-activities="gameActivities"
      :game-targets="gameTargets"
      :ability-config="getAbilityConfig('version_activity')"
      @create-version="handleCreateGameVersion"
      @create-activity="handleCreateGameActivity"
      @toggle-activity-reminder="handleToggleGameActivityReminder"
      @archive-activity="handleArchiveGameActivity"
      @archive-version="handleArchiveGameVersion"
      @open-detail="handleOpenDetail"
    />

    <!-- 图册 -->
    <GalleryTab
      v-if="activeTab === '图册'"
      :gallery-data="galleryData"
      :game-versions="gameVersions"
      :game-activities="gameActivities"
      :game-targets="gameTargets"
      :ability-config="getAbilityConfig('gallery')"
      @create-photo="handleCreateGalleryPhoto"
      @update-photo="handleUpdateGalleryPhoto"
      @delete-photo="handleDeleteGalleryPhoto"
      @append-timeline="handleAppendTimeline"
    />

    <!-- 时间轴 -->
    <TimelineTab
      v-if="activeTab === '时间轴'"
      :timeline-events="timelineEvents"
      :game-versions="gameVersions"
      :game-activities="gameActivities"
      :game-targets="gameTargets"
      :note-data="noteData"
      :ai-data="aiData"
      :ability-config="getAbilityConfig('timeline')"
      @append-timeline="handleAppendTimeline"
      @open-detail="handleOpenDetail"
    />

    <!-- 账号资产 -->
    <AssetsTab
      v-if="activeTab === '账号资产'"
      :asset-data="assetData"
      :ability-config="getAbilityConfig('assets')"
      @create-asset="handleCreateAsset"
      @update-asset="handleUpdateAsset"
      @update-asset-status="handleUpdateAssetStatus"
      @delete-asset="handleDeleteAsset"
    />

    <!-- 快速记录弹框 -->
    <LifeModal
      v-model:show="showQuickRecord"
      title="快速记录"
      description="记录一条游戏动态或备忘"
      :width="480"
      @confirm="handleQuickRecordConfirm"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">记录内容</label>
          <textarea
            v-model="quickRecordContent"
            class="w-full border border-slate-200 rounded-xl px-3 py-2 text-sm text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-200 focus:border-indigo-300 resize-none"
            rows="3"
            placeholder="输入要记录的内容..."
          ></textarea>
        </div>
      </div>
    </LifeModal>

    <!-- 详情弹框 -->
    <LifeModal
      v-model:show="detailModal.show"
      :title="detailModal.title"
      :width="520"
      :show-footer="false"
    >
      <div v-if="detailModal.content" class="text-sm text-slate-600 leading-relaxed mb-4">
        {{ detailModal.content }}
      </div>
      <div class="nikki-modal-meta">
        <div v-for="meta in detailModal.meta" :key="meta.label" class="meta-row">
          <span class="meta-label">{{ meta.label }}</span>
          <span class="meta-value">{{ meta.value }}</span>
        </div>
      </div>
    </LifeModal>
  </LifeGeminiShell>
</template>

<style lang="scss" src="./styles.scss"></style>

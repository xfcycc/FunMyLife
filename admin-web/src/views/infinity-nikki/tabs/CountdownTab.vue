<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { Archive, Bell, BellOff, CalendarDays, CheckCircle2, Clock, Flag, Plus, Radio, Sparkles, Timer } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { AbilityInstanceConfig, GameActivity, GameActivityStatus, GameTarget, GameVersion, GameVersionStatus } from '../types';

defineOptions({ name: 'CountdownTab' });

const props = defineProps<{
  gameVersions: GameVersion[];
  gameActivities: GameActivity[];
  gameTargets: GameTarget[];
  abilityConfig?: AbilityInstanceConfig;
}>();

const emit = defineEmits<{
  createVersion: [version: GameVersion];
  createActivity: [activity: GameActivity];
  toggleActivityReminder: [id: string, enabled: boolean];
  archiveActivity: [id: string];
  archiveVersion: [id: string];
  openDetail: [payload: { type: string; id: string }];
}>();

const selectedVersionId = ref('');
const showCreateVersion = ref(false);
const showCreateActivity = ref(false);
const versionForm = reactive({
  name: '',
  title: '',
  startAt: '',
  endAt: ''
});
const activityForm = reactive({
  title: '',
  description: '',
  endAt: '',
  priority: 'normal' as GameActivity['priority']
});

const versionStatusLabelMap: Record<GameVersionStatus, string> = {
  upcoming: '未开始',
  active: '当前版本',
  ending: '即将结束',
  ended: '已结束',
  archived: '已归档'
};

const activityStatusLabelMap: Record<GameActivityStatus, string> = {
  upcoming: '未开始',
  active: '进行中',
  ending: '即将结束',
  ended: '已结束',
  pending_archive: '待归档',
  archived: '已归档'
};

const currentVersion = computed(() => {
  return props.gameVersions.find(version => version.status === 'active') ?? props.gameVersions[0];
});

const orderedVersions = computed(() => {
  const statusOrder: Record<GameVersionStatus, number> = { active: 1, ending: 2, upcoming: 3, ended: 4, archived: 5 };
  return [...props.gameVersions].sort((prev, next) => {
    return statusOrder[prev.status] - statusOrder[next.status] || Date.parse(next.startAt) - Date.parse(prev.startAt);
  });
});

const selectedVersion = computed(() => {
  return props.gameVersions.find(version => version.id === selectedVersionId.value) ?? currentVersion.value;
});

const selectedVersionActivities = computed(() => {
  if (!selectedVersion.value) {
    return [];
  }

  return props.gameActivities.filter(activity => activity.versionId === selectedVersion.value?.id);
});

const activeActivities = computed(() => {
  return props.gameActivities.filter(activity => ['active', 'ending'].includes(activity.status));
});

const archiveReadyActivities = computed(() => {
  return props.gameActivities.filter(activity => ['ending', 'ended', 'pending_archive'].includes(activity.status));
});

const reminderRules = computed(() => props.abilityConfig?.behavior?.reminderRules ?? []);

function formatDateTime(value: string) {
  return value.replace('T', ' ').slice(0, 16);
}

function getRemainingText(endAt: string) {
  const diff = Date.parse(endAt) - Date.now();
  if (diff <= 0) {
    return '已结束';
  }

  const days = Math.ceil(diff / 1000 / 60 / 60 / 24);
  if (days > 1) {
    return `${days} 天`;
  }

  const hours = Math.max(1, Math.ceil(diff / 1000 / 60 / 60));
  return `${hours} 小时`;
}

function getVersionStatusClass(status: GameVersionStatus) {
  const map: Record<GameVersionStatus, string> = {
    upcoming: 'bg-blue-50 text-blue-600',
    active: 'bg-indigo-50 text-indigo-600',
    ending: 'bg-rose-50 text-rose-600',
    ended: 'bg-slate-100 text-slate-500',
    archived: 'bg-slate-100 text-slate-400'
  };
  return map[status];
}

function getActivityStatusClass(status: GameActivityStatus) {
  const map: Record<GameActivityStatus, string> = {
    upcoming: 'bg-blue-50 text-blue-600',
    active: 'bg-emerald-50 text-emerald-600',
    ending: 'bg-rose-50 text-rose-600',
    ended: 'bg-slate-100 text-slate-500',
    pending_archive: 'bg-amber-50 text-amber-600',
    archived: 'bg-slate-100 text-slate-400'
  };
  return map[status];
}

function getActivityTargets(activity: GameActivity) {
  return props.gameTargets.filter(target => activity.targetIds.includes(target.id));
}

function getDoneTargetCount(activity: GameActivity) {
  return getActivityTargets(activity).filter(target => target.status === 'done').length;
}

function getActivityProgress(activity: GameActivity) {
  const targets = getActivityTargets(activity);
  if (targets.length === 0) {
    return 0;
  }

  return Math.round((getDoneTargetCount(activity) / targets.length) * 100);
}

function getReminderEnabled(activity: GameActivity) {
  return Boolean(activity.reminderRule?.enabled);
}

function canArchiveVersion(version: GameVersion) {
  return ['active', 'ending', 'ended'].includes(version.status);
}

function resetVersionForm() {
  versionForm.name = '';
  versionForm.title = '';
  versionForm.startAt = '';
  versionForm.endAt = '';
}

function resetActivityForm() {
  activityForm.title = '';
  activityForm.description = '';
  activityForm.endAt = '';
  activityForm.priority = 'normal';
}

function handleCreateVersion() {
  if (!versionForm.name.trim() || !versionForm.title.trim()) {
    return;
  }

  const id = `ver-${Date.now()}`;
  emit('createVersion', {
    id,
    projectId: 'nikki-001',
    name: versionForm.name.trim(),
    title: versionForm.title.trim(),
    startAt: versionForm.startAt || new Date().toISOString(),
    endAt: versionForm.endAt || new Date(Date.now() + 30 * 24 * 60 * 60 * 1000).toISOString(),
    status: 'upcoming',
    highlights: [],
    activityIds: []
  });
  selectedVersionId.value = id;
  resetVersionForm();
  showCreateVersion.value = false;
}

function handleCreateActivity() {
  const version = selectedVersion.value;
  if (!version || !activityForm.title.trim()) {
    return;
  }

  emit('createActivity', {
    id: `activity-${Date.now()}`,
    projectId: 'nikki-001',
    versionId: version.id,
    title: activityForm.title.trim(),
    description: activityForm.description.trim() || undefined,
    startAt: new Date().toISOString(),
    endAt: activityForm.endAt || version.endAt,
    status: 'active',
    priority: activityForm.priority,
    targetIds: [],
    reminderRule: {
      enabled: true,
      channels: ['app'],
      beforeMinutes: [1440]
    }
  });
  resetActivityForm();
  showCreateActivity.value = false;
}
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 gap-6 xl:grid-cols-3">
      <LifeGeminiCard title="当前版本" :action-text="currentVersion ? getRemainingText(currentVersion.endAt) : ''">
        <div v-if="currentVersion" class="space-y-4">
          <div class="rounded-2xl bg-indigo-50/60 p-4">
            <div class="mb-3 flex items-start justify-between gap-4">
              <div>
                <span class="rounded-full px-2 py-0.5 text-[10px] font-medium" :class="getVersionStatusClass(currentVersion.status)">
                  {{ versionStatusLabelMap[currentVersion.status] }}
                </span>
                <h3 class="mt-2 text-lg font-bold text-slate-800">{{ currentVersion.title }}</h3>
                <p class="mt-1 text-xs text-slate-500">{{ formatDateTime(currentVersion.startAt) }} - {{ formatDateTime(currentVersion.endAt) }}</p>
              </div>
              <CalendarDays :size="24" class="shrink-0 text-indigo-400" />
            </div>
            <div class="flex flex-wrap gap-2">
              <span
                v-for="highlight in currentVersion.highlights"
                :key="highlight"
                class="rounded-full bg-white/80 px-2 py-1 text-[10px] text-indigo-600"
              >
                {{ highlight }}
              </span>
            </div>
          </div>

          <button
            v-if="canArchiveVersion(currentVersion)"
            class="nikki-action-btn outline-indigo"
            type="button"
            @click="$emit('archiveVersion', currentVersion.id)"
          >
            归档当前版本
          </button>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="活动状态" :action-text="`进行中 ${activeActivities.length}`">
        <div class="space-y-3">
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="inline-flex items-center text-xs text-slate-500">
              <Timer :size="12" class="mr-1" /> 临近结束
            </span>
            <span class="text-sm font-bold text-rose-500">{{ archiveReadyActivities.length }}</span>
          </div>
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="inline-flex items-center text-xs text-slate-500">
              <CheckCircle2 :size="12" class="mr-1" /> 活动目标
            </span>
            <span class="text-sm font-bold text-slate-700">
              {{ gameTargets.filter(target => target.type === 'activity' && target.status === 'done').length }}/{{ gameTargets.filter(target => target.type === 'activity').length }}
            </span>
          </div>
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="inline-flex items-center text-xs text-slate-500">
              <Archive :size="12" class="mr-1" /> 已归档版本
            </span>
            <span class="text-sm font-bold text-slate-700">{{ gameVersions.filter(version => version.status === 'archived').length }}</span>
          </div>
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="inline-flex items-center text-xs text-slate-500">
              <Bell :size="12" class="mr-1" /> 提醒规则
            </span>
            <span class="text-sm font-bold text-slate-700">{{ reminderRules.length }}</span>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="版本列表" action-text="新增版本" @action="showCreateVersion = true">
        <div class="space-y-3 max-h-64 overflow-y-auto pr-1">
          <div
            v-for="version in orderedVersions"
            :key="version.id"
            class="cursor-pointer rounded-2xl border px-3 py-3 transition-colors hover:bg-slate-50"
            :class="[selectedVersion?.id === version.id ? 'border-indigo-100 bg-indigo-50/40' : 'border-slate-100 bg-white']"
            @click="selectedVersionId = version.id"
          >
            <div class="flex items-start justify-between gap-3">
              <div class="min-w-0">
                <p class="truncate text-sm font-bold text-slate-800">{{ version.title }}</p>
                <p class="mt-1 text-[10px] text-slate-400">{{ formatDateTime(version.startAt) }} - {{ formatDateTime(version.endAt) }}</p>
              </div>
              <span class="shrink-0 rounded-full px-2 py-0.5 text-[10px]" :class="getVersionStatusClass(version.status)">
                {{ versionStatusLabelMap[version.status] }}
              </span>
            </div>
          </div>
        </div>
      </LifeGeminiCard>
    </div>

    <div class="grid grid-cols-1 gap-6 xl:grid-cols-2">
      <LifeGeminiCard :title="selectedVersion ? `${selectedVersion.name} 活动` : '版本活动'">
        <template #action>
          <button
            class="appearance-none border-0 bg-transparent p-0 text-xs font-medium text-indigo-500 hover:text-indigo-600"
            type="button"
            @click="showCreateActivity = true"
          >
            <Plus :size="12" class="mr-1 inline" />新增活动
          </button>
        </template>
        <div class="space-y-4 max-h-[620px] overflow-y-auto pr-1">
          <div
            v-for="activity in selectedVersionActivities"
            :key="activity.id"
            class="rounded-2xl border border-slate-100 bg-white p-4 transition-colors hover:bg-slate-50"
            @click="$emit('openDetail', { type: 'gameActivity', id: activity.id })"
          >
            <div class="flex items-start gap-4">
              <img v-if="activity.cover" :src="activity.cover" alt="" class="h-16 w-16 shrink-0 rounded-xl object-cover shadow-sm" />
              <div class="min-w-0 flex-1">
                <div class="flex flex-wrap items-center gap-2">
                  <p class="text-sm font-bold text-slate-800">{{ activity.title }}</p>
                  <span class="rounded-full px-2 py-0.5 text-[10px]" :class="getActivityStatusClass(activity.status)">
                    {{ activityStatusLabelMap[activity.status] }}
                  </span>
                </div>
                <p v-if="activity.description" class="mt-1 line-clamp-1 text-xs text-slate-500">{{ activity.description }}</p>
                <p class="mt-2 inline-flex items-center text-[10px] text-slate-400">
                  <Clock :size="10" class="mr-1" /> {{ formatDateTime(activity.endAt) }} 结束，剩余 {{ getRemainingText(activity.endAt) }}
                </p>
              </div>
            </div>

            <div class="mt-4 flex items-center gap-3">
              <div class="h-1.5 flex-1 overflow-hidden rounded-full bg-slate-100">
                <div class="h-full rounded-full bg-indigo-500" :style="{ width: `${getActivityProgress(activity)}%` }"></div>
              </div>
              <span class="text-xs text-slate-500">{{ getDoneTargetCount(activity) }}/{{ getActivityTargets(activity).length }}</span>
            </div>

            <div class="mt-4 flex flex-wrap items-center justify-between gap-3">
              <div class="flex flex-wrap gap-2">
                <span
                  v-for="target in getActivityTargets(activity)"
                  :key="target.id"
                  class="rounded-full bg-slate-50 px-2 py-1 text-[10px] text-slate-500"
                >
                  {{ target.title }}
                </span>
              </div>
              <div class="flex gap-2">
                <button
                  class="nikki-action-btn"
                  :class="[getReminderEnabled(activity) ? 'outline' : 'outline-rose']"
                  type="button"
                  @click.stop="$emit('toggleActivityReminder', activity.id, !getReminderEnabled(activity))"
                >
                  <BellOff v-if="getReminderEnabled(activity)" :size="12" class="mr-1" />
                  <Bell v-else :size="12" class="mr-1" />
                  {{ getReminderEnabled(activity) ? '关闭提醒' : '开启提醒' }}
                </button>
                <button
                  v-if="activity.status === 'ending' || activity.status === 'ended' || activity.status === 'pending_archive'"
                  class="nikki-action-btn outline-indigo"
                  type="button"
                  @click.stop="$emit('archiveActivity', activity.id)"
                >
                  <Archive :size="12" class="mr-1" />归档
                </button>
              </div>
            </div>
          </div>

          <div v-if="selectedVersionActivities.length === 0" class="nikki-empty-state">
            <Radio :size="32" />
            <p>该版本暂无活动</p>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="版本归档预览">
        <div class="space-y-4">
          <div
            v-for="version in gameVersions.filter(item => item.status === 'archived')"
            :key="version.id"
            class="rounded-2xl bg-slate-50 px-4 py-3"
            @click="$emit('openDetail', { type: 'gameVersion', id: version.id })"
          >
            <div class="flex items-start justify-between gap-4">
              <div>
                <p class="text-sm font-bold text-slate-800">{{ version.title }}</p>
                <p v-if="version.summary" class="mt-1 text-xs leading-5 text-slate-500">{{ version.summary }}</p>
              </div>
              <Sparkles :size="16" class="shrink-0 text-slate-300" />
            </div>
          </div>

          <div v-if="gameVersions.filter(item => item.status === 'archived').length === 0" class="nikki-empty-state">
            <Archive :size="32" />
            <p>暂无归档版本</p>
          </div>

          <div class="rounded-2xl border border-dashed border-indigo-100 bg-indigo-50/40 px-4 py-3">
            <div class="mb-2 flex items-center text-xs font-medium text-indigo-600">
              <Flag :size="13" class="mr-1.5" /> 归档会保存什么
            </div>
            <p class="text-xs leading-5 text-slate-500">版本结束后会保留活动目标、截图、笔记和时间轴摘要，下个版本重新生成新的活动与目标。</p>
          </div>
        </div>
      </LifeGeminiCard>
    </div>

    <LifeModal
      v-model:show="showCreateVersion"
      title="新增版本"
      description="版本数据在活动与版本页管理"
      confirm-text="添加版本"
      :width="520"
      @confirm="handleCreateVersion"
    >
      <div class="space-y-4">
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">版本号</label>
            <input v-model="versionForm.name" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="5.3" />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">版本名称</label>
            <input v-model="versionForm.title" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="5.3 星海漫游" />
          </div>
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">开始时间</label>
            <input v-model="versionForm.startAt" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" type="datetime-local" />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">结束时间</label>
            <input v-model="versionForm.endAt" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" type="datetime-local" />
          </div>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      v-model:show="showCreateActivity"
      title="新增活动"
      :description="selectedVersion ? `添加到 ${selectedVersion.title}` : '请先选择版本'"
      confirm-text="添加活动"
      :width="520"
      @confirm="handleCreateActivity"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">活动名称</label>
          <input v-model="activityForm.title" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="限时活动名称" />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <textarea v-model="activityForm.description" class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm" rows="2"></textarea>
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">结束时间</label>
            <input v-model="activityForm.endAt" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" type="datetime-local" />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">优先级</label>
            <select v-model="activityForm.priority" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="low">低</option>
              <option value="normal">普通</option>
              <option value="high">高</option>
            </select>
          </div>
        </div>
      </div>
    </LifeModal>
  </div>
</template>

<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { AlertTriangle, Archive, CheckCircle2, Circle, CircleDot, Clock3, Flag, History, ListChecks, Plus, RotateCcw, Undo2 } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { AbilityInstanceConfig, GameActivity, GameTarget, GameTargetStatus, GameTargetType, ResetRule, TimelineWriteRule } from '../types';

defineOptions({ name: 'DailyTab' });

const props = defineProps<{
  gameTargets: GameTarget[];
  gameActivities: GameActivity[];
  abilityConfig?: AbilityInstanceConfig;
}>();

const emit = defineEmits<{
  createTarget: [target: GameTarget];
  updateTarget: [target: GameTarget];
  deleteTarget: [id: string];
  toggleTargetStatus: [id: string, status: GameTargetStatus];
  updateTargetProgress: [id: string, current: number];
}>();

type TargetViewKey = GameTargetType | 'history';

const activeGroup = ref<TargetViewKey>('daily');
const selectedTargetId = ref<string>('');
const showCreateTarget = ref(false);
const showEditTarget = ref(false);
const pendingTargetAction = ref<{ mode: 'status' | 'delete'; target: GameTarget; status?: GameTargetStatus } | null>(null);
const targetForm = reactive({
  title: '',
  description: '',
  progressTarget: 1,
  priority: 'normal' as GameTarget['priority'],
  pinnedToOverview: true,
  activityId: ''
});
const editTargetForm = reactive({
  id: '',
  title: '',
  description: '',
  progressCurrent: 0,
  progressTarget: 1,
  priority: 'normal' as GameTarget['priority'],
  pinnedToOverview: true
});

const groupLabelMap: Record<GameTargetType, string> = {
  daily: '日常',
  weekly: '周常',
  activity: '版本活动',
  custom: '自定义'
};

const groupTitleMap: Record<GameTargetType, string> = {
  daily: '今日目标',
  weekly: '本周目标',
  activity: '版本限时目标',
  custom: '个人目标'
};

const viewTitleMap: Record<TargetViewKey, string> = {
  ...groupTitleMap,
  history: '任务历史'
};

const priorityLabelMap: Record<GameTarget['priority'], string> = {
  low: '低',
  normal: '普通',
  high: '高'
};

const statusLabelMap: Record<GameTargetStatus, string> = {
  todo: '待完成',
  done: '已完成',
  skipped: '已跳过',
  expired: '已过期',
  archived: '已归档'
};

const timelineModeLabelMap: Record<TimelineWriteRule['mode'], string> = {
  none: '不写入',
  detail: '详细记录',
  daily_summary: '每日汇总',
  weekly_summary: '每周汇总',
  exception_only: '只记异常'
};

const groupOptions = computed(() => {
  const targetGroups = (Object.keys(groupLabelMap) as GameTargetType[]).map(type => ({
    value: type,
    label: groupLabelMap[type],
    count: props.gameTargets.filter(target => target.type === type).length
  }));
  return [
    ...targetGroups,
    {
      value: 'history' as const,
      label: '历史',
      count: props.gameTargets.filter(target => ['done', 'skipped', 'expired', 'archived'].includes(target.status)).length
    }
  ];
});

const sortedTargets = computed(() => {
  const priorityOrder: Record<GameTarget['priority'], number> = { high: 1, normal: 2, low: 3 };
  const statusOrder: Record<GameTargetStatus, number> = { todo: 1, skipped: 2, expired: 3, done: 4, archived: 5 };
  return [...props.gameTargets].sort((prev, next) => {
    return priorityOrder[prev.priority] - priorityOrder[next.priority] || statusOrder[prev.status] - statusOrder[next.status];
  });
});

const currentGroupTargets = computed(() => {
  if (activeGroup.value === 'history') {
    return sortedTargets.value.filter(target => ['done', 'skipped', 'expired', 'archived'].includes(target.status));
  }

  return sortedTargets.value.filter(target => target.type === activeGroup.value && target.status !== 'archived');
});

const visibleTargets = computed(() => {
  return props.gameTargets.filter(target => target.status !== 'archived');
});

const overallProgress = computed(() => {
  if (visibleTargets.value.length === 0) {
    return 0;
  }

  const doneCount = visibleTargets.value.filter(target => target.status === 'done').length;
  return Math.round((doneCount / visibleTargets.value.length) * 100);
});

const currentGroupCompleted = computed(() => {
  return currentGroupTargets.value.filter(target => target.status === 'done').length;
});

const currentGroupOverviewCount = computed(() => {
  return currentGroupTargets.value.filter(target => target.pinnedToOverview || target.timelineRule.displayInOverview).length;
});

const displayGroups = computed(() => {
  if (activeGroup.value === 'activity') {
    const groups = new Map<string, GameTarget[]>();
    currentGroupTargets.value.forEach(target => {
      const key = target.activityId ?? 'none';
      groups.set(key, [...(groups.get(key) ?? []), target]);
    });
    return Array.from(groups.entries()).map(([activityId, targets]) => ({
      id: activityId,
      label: getActivityLabel(activityId),
      targets
    }));
  }

  if (activeGroup.value === 'history') {
    const groups = new Map<GameTargetStatus, GameTarget[]>();
    currentGroupTargets.value.forEach(target => {
      groups.set(target.status, [...(groups.get(target.status) ?? []), target]);
    });
    return Array.from(groups.entries()).map(([status, targets]) => ({
      id: status,
      label: statusLabelMap[status],
      targets
    }));
  }

  return [{
    id: activeGroup.value,
    label: viewTitleMap[activeGroup.value],
    targets: currentGroupTargets.value
  }];
});

const configuredResetRules = computed(() => props.abilityConfig?.behavior?.resetRules ?? []);

const overviewCandidates = computed(() => {
  return sortedTargets.value
    .filter(target => target.pinnedToOverview || target.timelineRule.displayInOverview)
    .slice(0, 5);
});

const timelineSummary = computed(() => {
  const countMap = currentGroupTargets.value.reduce<Record<TimelineWriteRule['mode'], number>>(
    (acc, target) => {
      acc[target.timelineRule.mode] += 1;
      return acc;
    },
    { none: 0, detail: 0, daily_summary: 0, weekly_summary: 0, exception_only: 0 }
  );

  return Object.entries(countMap)
    .filter(([, count]) => count > 0)
    .map(([mode, count]) => `${timelineModeLabelMap[mode as TimelineWriteRule['mode']]} ${count}`)
    .join(' / ');
});

const pendingTargetActionTitle = computed(() => {
  if (!pendingTargetAction.value) {
    return '';
  }

  if (pendingTargetAction.value.mode === 'delete') {
    return '删除目标';
  }

  const status = pendingTargetAction.value.status;
  if (status === 'todo') {
    return '恢复目标';
  }
  if (status === 'archived') {
    return '归档目标';
  }
  if (status === 'skipped') {
    return '跳过目标';
  }
  return '标记目标过期';
});

const pendingTargetActionDescription = computed(() => {
  if (!pendingTargetAction.value) {
    return '';
  }

  return `确认处理「${pendingTargetAction.value.target.title}」？`;
});

function getProgressPercent(target: GameTarget) {
  if (!target.progressTarget) {
    return target.status === 'done' ? 100 : 0;
  }

  return Math.min(100, Math.round(((target.progressCurrent ?? 0) / target.progressTarget) * 100));
}

function getResetLabel(resetRule?: ResetRule) {
  if (!resetRule || resetRule.type === 'none') {
    return '不重置';
  }

  if (resetRule.type === 'daily') {
    return `每日 ${resetRule.time ?? '00:00'}`;
  }

  if (resetRule.type === 'weekly') {
    return `每周${resetRule.weekday ?? 1} ${resetRule.time ?? '00:00'}`;
  }

  if (resetRule.type === 'activity') {
    return resetRule.inheritFromActivity ? '跟随活动' : '活动周期';
  }

  return '自定义周期';
}

function getStatusClass(status: GameTargetStatus) {
  const map: Record<GameTargetStatus, string> = {
    todo: 'bg-slate-100 text-slate-500',
    done: 'bg-indigo-50 text-indigo-600',
    skipped: 'bg-amber-50 text-amber-600',
    expired: 'bg-rose-50 text-rose-600',
    archived: 'bg-slate-100 text-slate-400'
  };
  return map[status];
}

function getPriorityClass(priority: GameTarget['priority']) {
  const map: Record<GameTarget['priority'], string> = {
    low: 'bg-slate-50 text-slate-400',
    normal: 'bg-blue-50 text-blue-600',
    high: 'bg-rose-50 text-rose-600'
  };
  return map[priority];
}

function canAdjustProgress(target: GameTarget) {
  return typeof target.progressCurrent === 'number' && typeof target.progressTarget === 'number';
}

function getActivityLabel(activityId?: string) {
  if (!activityId || activityId === 'none') {
    return '未绑定活动';
  }

  return props.gameActivities.find(activity => activity.id === activityId)?.title ?? activityId;
}

function getActivityVersionId(activityId?: string) {
  return props.gameActivities.find(activity => activity.id === activityId)?.versionId;
}

function handleToggleTarget(target: GameTarget) {
  const nextStatus: GameTargetStatus = target.status === 'done' ? 'todo' : 'done';
  emit('toggleTargetStatus', target.id, nextStatus);
}

function handleAdjustTarget(target: GameTarget, delta: number) {
  if (!canAdjustProgress(target) || !target.progressTarget) {
    return;
  }

  const next = Math.max(0, Math.min(target.progressTarget, (target.progressCurrent ?? 0) + delta));
  if (next !== target.progressCurrent) {
    emit('updateTargetProgress', target.id, next);
  }
}

function openEditTarget(target: GameTarget) {
  editTargetForm.id = target.id;
  editTargetForm.title = target.title;
  editTargetForm.description = target.description ?? '';
  editTargetForm.progressCurrent = target.progressCurrent ?? 0;
  editTargetForm.progressTarget = target.progressTarget ?? 1;
  editTargetForm.priority = target.priority;
  editTargetForm.pinnedToOverview = Boolean(target.pinnedToOverview);
  showEditTarget.value = true;
}

function handleUpdateTarget() {
  const target = props.gameTargets.find(item => item.id === editTargetForm.id);
  if (!target || !editTargetForm.title.trim()) {
    return;
  }

  emit('updateTarget', {
    ...target,
    title: editTargetForm.title.trim(),
    description: editTargetForm.description.trim() || undefined,
    progressCurrent: Math.max(0, editTargetForm.progressCurrent),
    progressTarget: Math.max(1, editTargetForm.progressTarget),
    priority: editTargetForm.priority,
    pinnedToOverview: editTargetForm.pinnedToOverview
  });
  showEditTarget.value = false;
}

function openTargetStatusConfirm(target: GameTarget, status: GameTargetStatus) {
  pendingTargetAction.value = { mode: 'status', target, status };
}

function openTargetDeleteConfirm(target: GameTarget) {
  pendingTargetAction.value = { mode: 'delete', target };
}

function handleConfirmTargetAction() {
  if (!pendingTargetAction.value) {
    return;
  }

  if (pendingTargetAction.value.mode === 'delete') {
    emit('deleteTarget', pendingTargetAction.value.target.id);
  } else if (pendingTargetAction.value.status) {
    emit('toggleTargetStatus', pendingTargetAction.value.target.id, pendingTargetAction.value.status);
  }

  pendingTargetAction.value = null;
}

function getDefaultResetRule(type: GameTargetType): ResetRule {
  if (type === 'daily') {
    return { type: 'daily', time: '04:00' };
  }

  if (type === 'weekly') {
    return { type: 'weekly', weekday: 1, time: '04:00' };
  }

  if (type === 'activity') {
    return { type: 'activity', inheritFromActivity: true };
  }

  return { type: 'custom' };
}

function getDefaultTimelineRule(type: GameTargetType): TimelineWriteRule {
  if (type === 'daily') {
    return { mode: 'daily_summary', displayInOverview: true, aiReadable: true };
  }

  if (type === 'weekly') {
    return { mode: 'weekly_summary', displayInOverview: true, aiReadable: true };
  }

  return { mode: 'detail', displayInOverview: true, aiReadable: true };
}

function resetTargetForm() {
  targetForm.title = '';
  targetForm.description = '';
  targetForm.progressTarget = 1;
  targetForm.priority = 'normal';
  targetForm.pinnedToOverview = true;
  targetForm.activityId = '';
}

function handleCreateTarget() {
  if (activeGroup.value === 'history' || !targetForm.title.trim()) {
    return;
  }

  const activityId = activeGroup.value === 'activity' ? targetForm.activityId || props.gameActivities[0]?.id : undefined;

  emit('createTarget', {
    id: `target-${Date.now()}`,
    projectId: 'nikki-001',
    type: activeGroup.value,
    title: targetForm.title.trim(),
    description: targetForm.description.trim() || undefined,
    status: 'todo',
    progressCurrent: 0,
    progressTarget: targetForm.progressTarget,
    resetRule: getDefaultResetRule(activeGroup.value),
    versionId: getActivityVersionId(activityId),
    activityId,
    priority: targetForm.priority,
    pinnedToOverview: targetForm.pinnedToOverview,
    timelineRule: getDefaultTimelineRule(activeGroup.value)
  });
  resetTargetForm();
  showCreateTarget.value = false;
}
</script>

<template>
  <div class="space-y-6">
    <div class="flex flex-col gap-4 lg:flex-row lg:items-center lg:justify-between">
      <div class="nikki-group-switch">
        <button
          v-for="g in groupOptions"
          :key="g.value"
          class="group-btn"
          :class="[activeGroup === g.value ? 'active' : '']"
          type="button"
          @click="activeGroup = g.value"
        >
          {{ g.label }}
          <span class="ml-1 text-[10px] opacity-60">({{ g.count }})</span>
        </button>
      </div>

      <div class="flex items-center gap-2">
        <button
          v-if="activeGroup !== 'history'"
          class="nikki-action-btn outline-indigo"
          type="button"
          @click="showCreateTarget = true"
        >
          <Plus :size="12" class="mr-1" />新增目标
        </button>
        <span class="text-xs text-slate-500">总进度</span>
        <div class="h-2 w-32 overflow-hidden rounded-full bg-slate-100">
          <div class="h-full rounded-full bg-indigo-500 transition-all duration-500" :style="{ width: `${overallProgress}%` }"></div>
        </div>
        <span class="text-xs font-medium text-indigo-600">{{ overallProgress }}%</span>
      </div>
    </div>

    <div class="grid grid-cols-1 gap-6 lg:grid-cols-3">
      <div class="lg:col-span-2">
        <LifeGeminiCard :title="viewTitleMap[activeGroup]">
          <template #action>
            <span class="text-xs text-slate-400">{{ currentGroupCompleted }}/{{ currentGroupTargets.length }} 已完成</span>
          </template>

          <div class="space-y-3 max-h-[520px] overflow-y-auto pr-1">
            <div
              v-for="group in displayGroups"
              :key="group.id"
              class="space-y-3"
            >
              <div v-if="displayGroups.length > 1" class="flex items-center gap-2 px-1 text-xs font-medium text-slate-500">
                <span class="h-px flex-1 bg-slate-100"></span>
                {{ group.label }} · {{ group.targets.length }}
                <span class="h-px flex-1 bg-slate-100"></span>
              </div>

              <div
                v-for="target in group.targets"
                :key="target.id"
                class="rounded-2xl border border-slate-100 px-4 py-3 transition-colors hover:bg-slate-50"
                :class="[selectedTargetId === target.id ? 'bg-indigo-50/40 border-indigo-100' : 'bg-white']"
                @click="selectedTargetId = target.id"
              >
                <div class="flex items-start justify-between gap-4">
                  <div class="flex min-w-0 items-start gap-3">
                    <button
                      class="mt-0.5 shrink-0 text-slate-300 transition-colors hover:text-indigo-400"
                      type="button"
                      @click.stop="handleToggleTarget(target)"
                    >
                      <CheckCircle2 v-if="target.status === 'done'" :size="20" class="text-indigo-500" />
                      <Circle v-else :size="20" />
                    </button>

                    <div class="min-w-0">
                      <div class="flex flex-wrap items-center gap-2">
                        <p
                          class="text-sm font-medium"
                          :class="[target.status === 'done' ? 'text-slate-400 line-through' : 'text-slate-800']"
                        >
                          {{ target.title }}
                        </p>
                        <span class="rounded-full px-2 py-0.5 text-[10px]" :class="getPriorityClass(target.priority)">
                          {{ priorityLabelMap[target.priority] }}优先级
                        </span>
                      </div>
                      <p v-if="target.description" class="mt-1 text-xs leading-5 text-slate-500">{{ target.description }}</p>
                      <div class="mt-2 flex flex-wrap gap-2 text-[10px] text-slate-400">
                        <span class="rounded-full px-2 py-0.5" :class="getStatusClass(target.status)">
                          {{ statusLabelMap[target.status] }}
                        </span>
                        <span class="inline-flex items-center rounded-full bg-slate-50 px-2 py-0.5">
                          <RotateCcw :size="10" class="mr-1" />{{ getResetLabel(target.resetRule) }}
                        </span>
                        <span class="inline-flex items-center rounded-full bg-slate-50 px-2 py-0.5">
                          <History :size="10" class="mr-1" />{{ timelineModeLabelMap[target.timelineRule.mode] }}
                        </span>
                        <span v-if="target.activityId" class="inline-flex items-center rounded-full bg-rose-50 px-2 py-0.5 text-rose-500">
                          <Clock3 :size="10" class="mr-1" />{{ getActivityLabel(target.activityId) }}
                        </span>
                      </div>
                    </div>
                  </div>

                  <div v-if="target.progressTarget" class="shrink-0 text-right">
                    <p class="text-xs font-medium text-slate-700">{{ target.progressCurrent ?? 0 }}/{{ target.progressTarget }}</p>
                    <p class="mt-0.5 text-[10px] text-slate-400">{{ getProgressPercent(target) }}%</p>
                  </div>
                </div>

                <div v-if="target.progressTarget" class="mt-3 flex items-center gap-3">
                  <div class="h-1.5 flex-1 overflow-hidden rounded-full bg-slate-100">
                    <div class="h-full rounded-full bg-indigo-500 transition-all duration-500" :style="{ width: `${getProgressPercent(target)}%` }"></div>
                  </div>
                  <div v-if="selectedTargetId === target.id && canAdjustProgress(target)" class="flex items-center gap-1">
                    <button
                      class="flex h-6 w-6 items-center justify-center rounded-full bg-slate-100 text-xs font-bold text-slate-600 transition-colors hover:bg-slate-200"
                      type="button"
                      @click.stop="handleAdjustTarget(target, -1)"
                    >
                      -
                    </button>
                    <button
                      class="flex h-6 w-6 items-center justify-center rounded-full bg-indigo-100 text-xs font-bold text-indigo-600 transition-colors hover:bg-indigo-200"
                      type="button"
                      @click.stop="handleAdjustTarget(target, 1)"
                    >
                      +
                    </button>
                  </div>
                </div>

                <div v-if="selectedTargetId === target.id" class="mt-3 flex flex-wrap gap-2">
                  <button
                    class="nikki-action-btn outline-indigo"
                    type="button"
                    @click.stop="openEditTarget(target)"
                  >
                    编辑
                  </button>
                  <button
                    v-if="target.status !== 'skipped'"
                    class="nikki-action-btn"
                    type="button"
                    @click.stop="openTargetStatusConfirm(target, 'skipped')"
                  >
                    <Undo2 :size="12" class="mr-1" />跳过
                  </button>
                  <button
                    v-if="target.status !== 'expired'"
                    class="nikki-action-btn outline-rose"
                    type="button"
                    @click.stop="openTargetStatusConfirm(target, 'expired')"
                  >
                    <AlertTriangle :size="12" class="mr-1" />过期
                  </button>
                  <button
                    v-if="target.status !== 'todo'"
                    class="nikki-action-btn outline-indigo"
                    type="button"
                    @click.stop="openTargetStatusConfirm(target, 'todo')"
                  >
                    <RotateCcw :size="12" class="mr-1" />恢复
                  </button>
                  <button
                    v-if="target.status !== 'archived'"
                    class="nikki-action-btn outline"
                    type="button"
                    @click.stop="openTargetStatusConfirm(target, 'archived')"
                  >
                    <Archive :size="12" class="mr-1" />归档
                  </button>
                  <button
                    class="nikki-action-btn outline-rose"
                    type="button"
                    @click.stop="openTargetDeleteConfirm(target)"
                  >
                    删除
                  </button>
                </div>
              </div>
            </div>

            <div v-if="currentGroupTargets.length === 0" class="nikki-empty-state">
              <CircleDot :size="32" />
              <p>该分组暂无目标</p>
            </div>
          </div>
        </LifeGeminiCard>
      </div>

      <div class="space-y-6">
        <LifeGeminiCard :title="`${abilityConfig?.displayName ?? '任务'}设置`">
          <div class="space-y-3 text-xs">
            <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
              <span class="text-slate-500">当前分组</span>
              <span class="font-medium text-slate-700">{{ viewTitleMap[activeGroup] }}</span>
            </div>
            <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
              <span class="text-slate-500">概览候选</span>
              <span class="font-medium text-slate-700">{{ currentGroupOverviewCount }} 项</span>
            </div>
            <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
              <span class="text-slate-500">重置规则</span>
              <span class="font-medium text-slate-700">{{ configuredResetRules.length }} 条</span>
            </div>
            <div class="rounded-xl bg-slate-50 px-3 py-2">
              <div class="mb-1 flex items-center justify-between">
                <span class="text-slate-500">时间轴</span>
                <History :size="12" class="text-slate-300" />
              </div>
              <p class="text-slate-700">{{ timelineSummary || '暂无写入规则' }}</p>
            </div>
          </div>
        </LifeGeminiCard>

        <LifeGeminiCard title="概览摘要候选">
          <div class="space-y-3">
            <div
              v-for="target in overviewCandidates"
              :key="target.id"
              class="flex items-start justify-between gap-3 rounded-xl bg-slate-50 px-3 py-2"
            >
              <div class="min-w-0">
                <p class="truncate text-xs font-medium text-slate-700">{{ target.title }}</p>
                <p class="mt-0.5 text-[10px] text-slate-400">{{ groupLabelMap[target.type] }} · {{ timelineModeLabelMap[target.timelineRule.mode] }}</p>
              </div>
              <Flag v-if="target.pinnedToOverview" :size="13" class="shrink-0 text-indigo-500" />
              <ListChecks v-else :size="13" class="shrink-0 text-slate-300" />
            </div>
          </div>
        </LifeGeminiCard>
      </div>
    </div>

    <LifeModal
      v-model:show="showCreateTarget"
      title="新增目标"
      :description="`添加到${viewTitleMap[activeGroup]}`"
      confirm-text="添加"
      :width="520"
      @confirm="handleCreateTarget"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">目标名称</label>
          <input
            v-model="targetForm.title"
            class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
            placeholder="例如：完成新活动挑战"
          />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <textarea
            v-model="targetForm.description"
            class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
            rows="2"
            placeholder="补充这个目标的用途或完成条件"
          ></textarea>
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">目标进度</label>
            <input
              v-model.number="targetForm.progressTarget"
              class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
              min="1"
              type="number"
            />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">优先级</label>
            <select
              v-model="targetForm.priority"
              class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
            >
              <option value="low">低</option>
              <option value="normal">普通</option>
              <option value="high">高</option>
            </select>
          </div>
          <label class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-xs text-slate-600">
            <input v-model="targetForm.pinnedToOverview" type="checkbox" />
            进入概览候选
          </label>
        </div>
        <div v-if="activeGroup === 'activity'">
          <label class="block text-xs font-medium text-slate-600 mb-1.5">绑定活动</label>
          <select
            v-model="targetForm.activityId"
            class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
          >
            <option value="">默认选择第一个活动</option>
            <option v-for="activity in gameActivities" :key="activity.id" :value="activity.id">
              {{ activity.title }}
            </option>
          </select>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      v-model:show="showEditTarget"
      title="编辑目标"
      confirm-text="保存"
      :width="520"
      @confirm="handleUpdateTarget"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">目标名称</label>
          <input
            v-model="editTargetForm.title"
            class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
          />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <textarea
            v-model="editTargetForm.description"
            class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm text-slate-700 focus:border-indigo-300 focus:outline-none focus:ring-2 focus:ring-indigo-100"
            rows="2"
          ></textarea>
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">当前进度</label>
            <input v-model.number="editTargetForm.progressCurrent" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" min="0" type="number" />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">目标进度</label>
            <input v-model.number="editTargetForm.progressTarget" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" min="1" type="number" />
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">优先级</label>
            <select v-model="editTargetForm.priority" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="low">低</option>
              <option value="normal">普通</option>
              <option value="high">高</option>
            </select>
          </div>
        </div>
        <label class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-xs text-slate-600">
          <input v-model="editTargetForm.pinnedToOverview" type="checkbox" />
          进入概览候选
        </label>
      </div>
    </LifeModal>

    <LifeModal
      :show="Boolean(pendingTargetAction)"
      :title="pendingTargetActionTitle"
      :description="pendingTargetActionDescription"
      confirm-text="确认"
      cancel-text="取消"
      :width="420"
      @update:show="value => { if (!value) pendingTargetAction = null; }"
      @confirm="handleConfirmTargetAction"
    />
  </div>
</template>

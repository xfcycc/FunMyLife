<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { Bot, Camera, CheckCircle2, FileText, History, Layers, Plus, Sparkles, Timer } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type {
  AbilityBlockKey,
  AbilityInstanceConfig,
  AiOverview,
  GameActivity,
  GameTarget,
  GameVersion,
  NoteOverview,
  TimelineEvent,
  TimelineEventType
} from '../types';

defineOptions({ name: 'TimelineTab' });

const props = defineProps<{
  timelineEvents: TimelineEvent[];
  gameVersions: GameVersion[];
  gameActivities: GameActivity[];
  gameTargets: GameTarget[];
  noteData: NoteOverview;
  aiData: AiOverview;
  abilityConfig?: AbilityInstanceConfig;
}>();

const emit = defineEmits<{
  appendTimeline: [event: TimelineEvent];
  openDetail: [payload: { type: string; id: string }];
}>();

type TimelineViewMode = 'day' | 'week' | 'version';

const activeSource = ref<'all' | AbilityBlockKey>('all');
const activeView = ref<TimelineViewMode>('day');
const showCreateEvent = ref(false);
const eventForm = reactive({
  title: '',
  description: '',
  sourceBlockKey: 'timeline' as AbilityBlockKey,
  displayInOverview: true,
  aiReadable: true
});

const sourceLabelMap: Record<AbilityBlockKey, string> = {
  overview: '概览',
  targets: '任务',
  version_activity: '活动与版本',
  materials: '素材',
  gallery: '图册',
  assets: '账号资产',
  timeline: '时间轴',
  ai: 'AI'
};

const eventTypeLabelMap: Record<TimelineEventType, string> = {
  target_done: '目标完成',
  target_skipped: '目标跳过',
  target_expired: '目标过期',
  activity_started: '活动开始',
  activity_ending: '活动临近结束',
  activity_archived: '活动归档',
  version_archived: '版本归档',
  material_completed: '素材完成',
  photo_uploaded: '图册更新',
  note_created: '笔记记录',
  ai_summary_generated: 'AI 摘要'
};

const viewOptions: Array<{ value: TimelineViewMode; label: string }> = [
  { value: 'day', label: '按天' },
  { value: 'week', label: '按周' },
  { value: 'version', label: '按版本' }
];

const normalizedEvents = computed<TimelineEvent[]>(() => {
  const noteEvents = props.noteData.notes.map<TimelineEvent>(note => ({
    id: `note-${note.id}`,
    projectId: 'nikki-001',
    occurredAt: note.updatedAt,
    type: 'note_created',
    title: note.title,
    description: note.content,
    sourceBlockKey: 'timeline',
    versionId: props.gameVersions.find(version => version.status === 'active')?.id,
    sensitivity: 'normal',
    displayInOverview: true,
    aiReadable: true
  }));

  const aiEvent: TimelineEvent | undefined = props.aiData.currentSuggestion.generatedAt
    ? {
        id: `ai-${props.aiData.currentSuggestion.id}`,
        projectId: 'nikki-001',
        occurredAt: props.aiData.currentSuggestion.generatedAt,
        type: 'ai_summary_generated',
        title: 'AI 项目建议',
        description: props.aiData.currentSuggestion.content,
        sourceBlockKey: 'ai',
        versionId: props.gameVersions.find(version => version.status === 'active')?.id,
        sensitivity: 'normal',
        displayInOverview: true,
        aiReadable: true
      }
    : undefined;

  return [...props.timelineEvents, ...noteEvents, ...(aiEvent ? [aiEvent] : [])];
});

const sourceOptions = computed(() => {
  const sources = Array.from(new Set(normalizedEvents.value.map(event => event.sourceBlockKey)));
  return [
    { value: 'all' as const, label: '全部', count: normalizedEvents.value.length },
    ...sources.map(source => ({
      value: source,
      label: sourceLabelMap[source],
      count: normalizedEvents.value.filter(event => event.sourceBlockKey === source).length
    }))
  ];
});

const filteredEvents = computed(() => {
  const events = activeSource.value === 'all'
    ? normalizedEvents.value
    : normalizedEvents.value.filter(event => event.sourceBlockKey === activeSource.value);

  return [...events].sort((prev, next) => Date.parse(next.occurredAt) - Date.parse(prev.occurredAt));
});

const groupedEvents = computed(() => {
  return filteredEvents.value.reduce<Array<{ key: string; label: string; events: TimelineEvent[] }>>((groups, event) => {
    const key = getGroupKey(event);
    const existing = groups.find(group => group.key === key);
    if (existing) {
      existing.events.push(event);
      return groups;
    }

    groups.push({
      key,
      label: getGroupLabel(event),
      events: [event]
    });
    return groups;
  }, []);
});

const aiReadableCount = computed(() => normalizedEvents.value.filter(event => event.aiReadable).length);
const overviewVisibleCount = computed(() => normalizedEvents.value.filter(event => event.displayInOverview).length);
const activeVersion = computed(() => props.gameVersions.find(version => version.status === 'active'));

function formatDateLabel(value: string) {
  const date = new Date(value);
  return `${date.getMonth() + 1}月${date.getDate()}日`;
}

function getWeekKey(value: string) {
  const date = new Date(value);
  const day = date.getDay() || 7;
  date.setDate(date.getDate() - day + 1);
  return date.toISOString().slice(0, 10);
}

function getGroupKey(event: TimelineEvent) {
  if (activeView.value === 'week') {
    return getWeekKey(event.occurredAt);
  }

  if (activeView.value === 'version') {
    return event.versionId ?? 'no-version';
  }

  return event.occurredAt.slice(0, 10);
}

function getGroupLabel(event: TimelineEvent) {
  if (activeView.value === 'week') {
    return `${formatDateLabel(getWeekKey(event.occurredAt))} 那周`;
  }

  if (activeView.value === 'version') {
    return getVersionTitle(event.versionId) ?? '未关联版本';
  }

  return formatDateLabel(event.occurredAt);
}

function formatTime(value: string) {
  return value.replace('T', ' ').slice(11, 16);
}

function getEventIcon(type: TimelineEventType) {
  const map: Partial<Record<TimelineEventType, typeof History>> = {
    target_done: CheckCircle2,
    target_skipped: CheckCircle2,
    target_expired: Timer,
    activity_started: Sparkles,
    activity_ending: Timer,
    activity_archived: Layers,
    version_archived: Layers,
    photo_uploaded: Camera,
    note_created: FileText,
    ai_summary_generated: Bot
  };
  return map[type] ?? History;
}

function getEventTone(type: TimelineEventType) {
  if (type.includes('archived')) {
    return 'bg-slate-100 text-slate-500';
  }

  if (type.includes('activity')) {
    return 'bg-rose-50 text-rose-500';
  }

  if (type.includes('photo')) {
    return 'bg-emerald-50 text-emerald-500';
  }

  if (type.includes('note')) {
    return 'bg-blue-50 text-blue-500';
  }

  return 'bg-indigo-50 text-indigo-500';
}

function getVersionTitle(versionId?: string) {
  return props.gameVersions.find(version => version.id === versionId)?.title;
}

function getActivityTitle(activityId?: string) {
  return props.gameActivities.find(activity => activity.id === activityId)?.title;
}

function getTargetTitle(targetId?: string) {
  return props.gameTargets.find(target => target.id === targetId)?.title;
}

function openEventDetail(event: TimelineEvent) {
  if (event.id.startsWith('note-')) {
    emit('openDetail', { type: 'note', id: event.id.replace('note-', '') });
    return;
  }

  if (event.id.startsWith('ai-')) {
    emit('openDetail', { type: 'ai', id: event.id.replace('ai-', '') });
    return;
  }

  emit('openDetail', { type: 'timeline', id: event.id });
}

function resetEventForm() {
  eventForm.title = '';
  eventForm.description = '';
  eventForm.sourceBlockKey = 'timeline';
  eventForm.displayInOverview = true;
  eventForm.aiReadable = true;
}

function handleCreateEvent() {
  if (!eventForm.title.trim()) {
    return;
  }

  emit('appendTimeline', {
    id: `timeline-${Date.now()}`,
    projectId: 'nikki-001',
    occurredAt: new Date().toISOString(),
    type: 'note_created',
    title: eventForm.title.trim(),
    description: eventForm.description.trim() || undefined,
    sourceBlockKey: eventForm.sourceBlockKey,
    versionId: activeVersion.value?.id,
    sensitivity: 'normal',
    displayInOverview: eventForm.displayInOverview,
    aiReadable: eventForm.aiReadable
  });
  resetEventForm();
  showCreateEvent.value = false;
}
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 gap-6 lg:grid-cols-4">
      <LifeGeminiCard title="时间轴概况">
        <div class="space-y-3 text-xs">
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="text-slate-500">总记录</span>
            <span class="font-bold text-slate-800">{{ normalizedEvents.length }}</span>
          </div>
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="text-slate-500">概览可见</span>
            <span class="font-bold text-indigo-600">{{ overviewVisibleCount }}</span>
          </div>
          <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
            <span class="text-slate-500">AI 可读</span>
            <span class="font-bold text-emerald-600">{{ aiReadableCount }}</span>
          </div>
          <div class="rounded-xl bg-indigo-50/50 px-3 py-2">
            <span class="text-slate-500">当前版本</span>
            <p class="mt-1 font-medium text-slate-700">{{ activeVersion?.title ?? '暂无当前版本' }}</p>
          </div>
          <div class="rounded-xl bg-slate-50 px-3 py-2">
            <span class="text-slate-500">默认写入</span>
            <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.timeline?.defaultWriteRule.mode ?? 'detail' }}</p>
          </div>
        </div>
      </LifeGeminiCard>

      <div class="lg:col-span-3">
        <div class="nikki-group-switch mb-4 w-fit">
          <button
            v-for="option in viewOptions"
            :key="option.value"
            class="group-btn"
            :class="[activeView === option.value ? 'active' : '']"
            type="button"
            @click="activeView = option.value"
          >
            {{ option.label }}
          </button>
        </div>

        <div class="nikki-group-switch mb-4 w-fit">
          <button
            v-for="option in sourceOptions"
            :key="option.value"
            class="group-btn"
            :class="[activeSource === option.value ? 'active' : '']"
            type="button"
            @click="activeSource = option.value"
          >
            {{ option.label }}
            <span class="ml-1 text-[10px] opacity-60">({{ option.count }})</span>
          </button>
        </div>

        <LifeGeminiCard title="长期记录" action-text="新增记录" @action="showCreateEvent = true">
          <div class="space-y-6 max-h-[680px] overflow-y-auto pr-1">
            <div v-for="group in groupedEvents" :key="group.key" class="relative pl-6">
              <div class="absolute bottom-0 left-2 top-8 w-px bg-slate-100"></div>
              <div class="mb-3 flex items-center gap-2">
                <span class="flex h-4 w-4 items-center justify-center rounded-full bg-indigo-500 ring-4 ring-indigo-50"></span>
                <h3 class="text-sm font-bold text-slate-800">{{ group.label }}</h3>
                <span class="text-xs text-slate-400">{{ group.events.length }} 条记录</span>
              </div>

              <div class="space-y-3">
                <div
                  v-for="event in group.events"
                  :key="event.id"
                  class="rounded-2xl border border-slate-100 bg-white px-4 py-3 transition-colors hover:bg-slate-50"
                  @click="openEventDetail(event)"
                >
                  <div class="flex items-start gap-3">
                    <div class="mt-0.5 flex h-9 w-9 shrink-0 items-center justify-center rounded-2xl" :class="getEventTone(event.type)">
                      <component :is="getEventIcon(event.type)" :size="16" />
                    </div>
                    <div class="min-w-0 flex-1">
                      <div class="flex flex-wrap items-center gap-2">
                        <p class="text-sm font-bold text-slate-800">{{ event.title }}</p>
                        <span class="rounded-full bg-slate-50 px-2 py-0.5 text-[10px] text-slate-500">
                          {{ eventTypeLabelMap[event.type] }}
                        </span>
                        <span class="text-[10px] text-slate-400">{{ formatTime(event.occurredAt) }}</span>
                      </div>
                      <p v-if="event.description" class="mt-1 text-xs leading-5 text-slate-500">{{ event.description }}</p>
                      <div class="mt-2 flex flex-wrap gap-2 text-[10px] text-slate-400">
                        <span class="rounded-full bg-slate-50 px-2 py-0.5">{{ sourceLabelMap[event.sourceBlockKey] }}</span>
                        <span v-if="getVersionTitle(event.versionId)" class="rounded-full bg-indigo-50 px-2 py-0.5 text-indigo-500">
                          {{ getVersionTitle(event.versionId) }}
                        </span>
                        <span v-if="getActivityTitle(event.activityId)" class="rounded-full bg-rose-50 px-2 py-0.5 text-rose-500">
                          {{ getActivityTitle(event.activityId) }}
                        </span>
                        <span v-if="getTargetTitle(event.targetId)" class="rounded-full bg-emerald-50 px-2 py-0.5 text-emerald-500">
                          {{ getTargetTitle(event.targetId) }}
                        </span>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <div v-if="groupedEvents.length === 0" class="nikki-empty-state">
              <History :size="32" />
              <p>暂无时间轴记录</p>
            </div>
          </div>
        </LifeGeminiCard>
      </div>
    </div>

    <LifeModal
      v-model:show="showCreateEvent"
      title="新增时间轴记录"
      description="笔记、复盘、异常和 AI 可读记录都可以在这里新增"
      confirm-text="添加记录"
      :width="520"
      @confirm="handleCreateEvent"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">标题</label>
          <input v-model="eventForm.title" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="今天记录了什么" />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">内容</label>
          <textarea v-model="eventForm.description" class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm" rows="3"></textarea>
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">来源</label>
            <select v-model="eventForm.sourceBlockKey" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="timeline">时间轴</option>
              <option value="targets">任务</option>
              <option value="version_activity">活动与版本</option>
              <option value="gallery">图册</option>
              <option value="assets">账号资产</option>
              <option value="ai">AI</option>
            </select>
          </div>
          <label class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-xs text-slate-600">
            <input v-model="eventForm.displayInOverview" type="checkbox" />
            概览显示
          </label>
          <label class="flex items-center gap-2 rounded-xl bg-slate-50 px-3 py-2 text-xs text-slate-600">
            <input v-model="eventForm.aiReadable" type="checkbox" />
            AI 可读
          </label>
        </div>
      </div>
    </LifeModal>
  </div>
</template>

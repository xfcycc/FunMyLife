<script setup lang="ts">
import { computed } from 'vue';
import {
  BookOpen,
  CalendarDays,
  Camera,
  CheckCircle2,
  ChevronRight,
  Clock,
  History,
  PackageCheck,
  ListChecks,
  RefreshCw,
  ShieldCheck,
  Star
} from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import type {
  AiOverview,
  AssetOverview,
  GalleryOverview,
  MaterialOverview,
  NoteOverview,
  OverviewSummary,
  TimelineEvent,
  TimelineEventType
} from '../types';

defineOptions({ name: 'OverviewTab' });

const props = defineProps<{
  assetData: AssetOverview;
  noteData: NoteOverview;
  galleryData: GalleryOverview;
  materialData: MaterialOverview;
  aiData: AiOverview;
  timelineEvents: TimelineEvent[];
  overviewSummaries: OverviewSummary[];
}>();

const emit = defineEmits<{
  switchTab: [tab: string];
  refreshAi: [];
  openDetail: [payload: { type: string; id: string }];
}>();

const summaryIconMap: Record<string, typeof CheckCircle2> = {
  'sum-current-version': CalendarDays,
  'sum-today-targets': CheckCircle2,
  'sum-weekly-targets': ListChecks,
  'sum-ending-activities': Clock,
  'sum-material-progress': PackageCheck,
  'sum-gallery-recent': Camera,
  'sum-asset-risk': ShieldCheck,
  'sum-recent-timeline': History
};

const summaryTabMap: Record<string, string> = {
  targets: '任务',
  version_activity: '活动与版本',
  gallery: '图册',
  assets: '账号资产',
  timeline: '时间轴'
};

const statusLabelMap: Record<string, string> = {
  active: '进行中',
  ending: '即将结束',
  todo: '待完成',
  done: '已完成',
  skipped: '已跳过',
  expired: '已过期',
  target_done: '目标完成',
  note_created: '笔记',
  photo_uploaded: '图册',
  activity_started: '活动开始',
  activity_archived: '活动归档',
  version_archived: '版本归档',
  local_note: '快速记录',
  collecting: '收集中',
  completed: '已完成',
  archived: '已归档',
  protected: '已保护',
  bound: '已绑定',
  pending: '待处理'
};

const recentTimelineEvents = computed(() => {
  return [...props.timelineEvents]
    .sort((prev, next) => Date.parse(next.occurredAt) - Date.parse(prev.occurredAt))
    .slice(0, 6);
});

function getSummaryIcon(ruleId: string) {
  return summaryIconMap[ruleId] ?? Star;
}

function getSummaryItems(summary: OverviewSummary) {
  return summary.items ?? [];
}

function getSummaryStatusLabel(status?: string) {
  return status ? (statusLabelMap[status] ?? status) : '';
}

function getSummaryActionText(summary: OverviewSummary) {
  return summary.targetRoute && summaryTabMap[summary.targetRoute] ? '查看' : undefined;
}

function handleSummaryAction(summary: OverviewSummary) {
  const tab = summary.targetRoute ? summaryTabMap[summary.targetRoute] : undefined;
  if (tab) {
    emit('switchTab', tab);
  }
}

function getTimelineIcon(type: TimelineEventType) {
  const map: Partial<Record<TimelineEventType, typeof CheckCircle2>> = {
    target_done: CheckCircle2,
    target_skipped: CheckCircle2,
    target_expired: Clock,
    activity_started: Star,
    activity_ending: Clock,
    activity_archived: History,
    version_archived: History,
    material_completed: PackageCheck,
    photo_uploaded: Camera,
    note_created: BookOpen,
    ai_summary_generated: RefreshCw
  };
  return map[type] ?? History;
}

function formatTimelineTime(value: string) {
  return value.replace('T', ' ').slice(5, 16);
}
</script>

<template>
  <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-5 gap-4 mb-6">
    <LifeGeminiCard
      v-for="summary in overviewSummaries"
      :key="summary.id"
      :title="summary.title"
      :action-text="getSummaryActionText(summary)"
      @action="handleSummaryAction(summary)"
    >
      <div class="min-h-36 flex flex-col justify-between">
        <div>
          <div class="w-10 h-10 rounded-2xl bg-indigo-50 text-indigo-500 flex items-center justify-center mb-4">
            <component :is="getSummaryIcon(summary.ruleId)" :size="18" />
          </div>
          <p class="text-2xl font-bold text-slate-800">{{ summary.value }}</p>
          <p v-if="summary.description" class="mt-1 text-xs leading-5 text-slate-500">{{ summary.description }}</p>
        </div>

        <div v-if="getSummaryItems(summary).length" class="mt-4 space-y-2">
          <div
            v-for="item in getSummaryItems(summary)"
            :key="item.id"
            class="flex items-center justify-between gap-3 rounded-xl bg-slate-50 px-3 py-2"
          >
            <span class="min-w-0 truncate text-xs text-slate-700">{{ item.label }}</span>
            <span v-if="item.status" class="shrink-0 text-[10px] text-slate-400">{{ getSummaryStatusLabel(item.status) }}</span>
          </div>
        </div>
      </div>
    </LifeGeminiCard>
  </div>

  <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-5 gap-6">
    <!-- 账号资产 -->
    <LifeGeminiCard title="账号资产" action-text="管理" @action="$emit('switchTab', '账号资产')">
      <div class="flex items-start space-x-6">
        <div class="w-24 h-24 bg-gradient-to-br from-indigo-300 to-indigo-500 rounded-2xl shadow-lg relative flex items-center justify-center flex-shrink-0 border-b-4 border-indigo-600">
          <div class="w-16 h-16 border-2 border-indigo-200/50 rounded-xl relative flex items-center justify-center">
            <div class="w-8 h-8 rounded-full border-[3px] border-white/80 flex items-center justify-center bg-indigo-400">
              <div class="w-2 h-2 rounded-full bg-white"></div>
            </div>
          </div>
        </div>
        <div class="flex-1 space-y-3">
          <div
            v-for="asset in assetData.assets"
            :key="asset.id"
            class="nikki-asset-item"
            @click="$emit('openDetail', { type: 'asset', id: asset.id })"
          >
            <div class="flex items-center space-x-2">
              <div class="asset-icon">
                <ShieldCheck :size="12" />
              </div>
              <span class="text-xs font-medium text-slate-700">{{ asset.name }}</span>
            </div>
            <span class="text-[10px] text-slate-400">{{ asset.statusLabel }}</span>
          </div>
          <div class="pt-2 flex justify-between items-center">
            <span class="text-xs text-slate-500">共 {{ assetData.total }} 个资产</span>
            <button
              class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium"
              type="button"
              @click="$emit('switchTab', '账号资产')"
            >
              查看全部
            </button>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 最近笔记 -->
    <LifeGeminiCard title="最近笔记" action-text="查看时间轴" @action="$emit('switchTab', '时间轴')">
      <div class="space-y-4">
        <div
          v-for="note in noteData.notes.slice(0, 4)"
          :key="note.id"
          class="nikki-note-item"
          @click="$emit('openDetail', { type: 'note', id: note.id })"
        >
          <div class="flex items-start space-x-2 pr-4">
            <div class="mt-0.5">
              <BookOpen :size="14" class="text-slate-300 group-hover:text-indigo-400 transition-colors" />
            </div>
            <div>
              <p class="note-title text-xs font-medium text-slate-700 transition-colors line-clamp-1">{{ note.title }}</p>
              <span class="note-category">{{ note.categoryLabel }}</span>
            </div>
          </div>
          <span class="text-[10px] text-slate-400 whitespace-nowrap pt-0.5">{{ note.relativeTime }}</span>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 素材收集 -->
    <LifeGeminiCard title="素材收集" action-text="查看任务" @action="$emit('switchTab', '任务')">
      <div class="space-y-3">
        <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
          <span class="inline-flex items-center text-xs text-slate-500">
            <PackageCheck :size="13" class="mr-1" />收集进度
          </span>
          <span class="text-sm font-bold text-indigo-600">{{ materialData.completed }}/{{ materialData.total }}</span>
        </div>
        <div
          v-for="material in materialData.materials.slice(0, 4)"
          :key="material.id"
          class="rounded-xl bg-slate-50 px-3 py-2"
        >
          <div class="flex items-center justify-between gap-3">
            <span class="min-w-0 truncate text-xs font-medium text-slate-700">{{ material.name }}</span>
            <span class="shrink-0 text-[10px] text-slate-400">{{ getSummaryStatusLabel(material.status) }}</span>
          </div>
          <div class="mt-2 h-1.5 overflow-hidden rounded-full bg-white">
            <div class="h-full rounded-full bg-indigo-500" :style="{ width: `${Math.min(100, Math.round((material.current / material.target) * 100))}%` }"></div>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 截图图册 -->
    <LifeGeminiCard title="截图图册" action-text="查看全部" @action="$emit('switchTab', '图册')">
      <div class="nikki-gallery-grid">
        <div
          v-for="photo in galleryData.recentPhotos.slice(0, 6)"
          :key="photo.id"
          class="gallery-thumb"
          @click="$emit('openDetail', { type: 'photo', id: photo.id })"
        >
          <img :src="photo.thumbnail" alt="Screenshot" />
          <div class="gallery-overlay"></div>
        </div>
      </div>
      <p class="text-[10px] text-slate-400">共 {{ galleryData.albumCount }} 个图册，{{ galleryData.photoCount }} 张图片</p>
    </LifeGeminiCard>

    <!-- AI 项目建议 -->
    <LifeGeminiCard class="nikki-ai-card">
      <template #title>
        ✨ AI 项目建议
      </template>
      <template #action>
        <button
          class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium"
          type="button"
          @click="$emit('refreshAi')"
        >
          <RefreshCw :size="14" />
        </button>
      </template>
      <div class="h-full min-h-48 flex flex-col justify-between overflow-hidden">
        <ul class="space-y-2 text-xs text-slate-600 relative z-10">
          <li v-for="(h, i) in aiData.currentSuggestion.highlights" :key="i" class="flex items-start">
            <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
            <span><strong class="nikki-ai-highlight" :class="h.type">{{ h.text }}</strong></span>
          </li>
        </ul>
        <div class="mt-4 flex items-end justify-between gap-4">
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs font-medium text-indigo-600 hover:text-indigo-700 flex min-w-0 items-center"
            type="button"
            @click="$emit('switchTab', '时间轴')"
          >
            查看可供 AI 复盘的时间轴 <ChevronRight :size="12" class="ml-0.5" />
          </button>
          <div class="w-14 h-14 shrink-0 opacity-90 drop-shadow-md">
            <svg viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg">
              <rect x="25" y="30" width="50" height="45" rx="15" fill="#818cf8" />
              <rect x="35" y="45" width="10" height="8" rx="4" fill="white" />
              <rect x="55" y="45" width="10" height="8" rx="4" fill="white" />
              <path d="M40 65 Q50 70 60 65" stroke="white" stroke-width="3" stroke-linecap="round" />
              <circle cx="50" cy="20" r="6" fill="#818cf8" />
              <line x1="50" y1="26" x2="50" y2="30" stroke="#818cf8" stroke-width="4" />
              <path d="M25 50 Q15 55 20 65" stroke="#818cf8" stroke-width="6" stroke-linecap="round" />
              <path d="M75 50 Q85 55 80 65" stroke="#818cf8" stroke-width="6" stroke-linecap="round" />
            </svg>
          </div>
        </div>
      </div>
    </LifeGeminiCard>
  </div>

  <!-- 最近动态 -->
  <section class="mt-6 bg-white rounded-[2rem] p-6 shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100">
    <div class="flex justify-between items-center mb-6">
      <h3 class="font-bold text-slate-800 text-base">最近动态</h3>
      <button
        class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium flex items-center"
        type="button"
      >
        查看全部动态 <ChevronRight :size="14" class="ml-0.5" />
      </button>
    </div>
    <div class="nikki-activity-timeline">
      <div
        v-for="item in recentTimelineEvents"
        :key="item.id"
        class="activity-node"
        @click="$emit('openDetail', { type: 'timeline', id: item.id })"
      >
        <div class="activity-icon bg-indigo-50">
          <component :is="getTimelineIcon(item.type)" :size="20" class="text-indigo-500" />
        </div>
        <p class="text-xs font-bold text-slate-700 text-center">{{ item.title }}</p>
        <p class="text-[10px] text-slate-500 text-center mt-0.5">{{ item.description }}</p>
        <p class="text-[9px] text-slate-400 text-center mt-1">{{ formatTimelineTime(item.occurredAt) }}</p>
      </div>
    </div>
  </section>
</template>

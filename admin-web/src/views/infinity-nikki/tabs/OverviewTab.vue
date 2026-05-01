<script setup lang="ts">
import { computed } from 'vue';
import { BookOpen, Camera, CheckCircle2, ChevronRight, Circle, Clock, Map, MoreVertical, PenSquare, ShieldCheck, Star } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import type { AiOverview, AssetOverview, CountdownOverview, DailyTaskOverview, GalleryOverview, NikkiActivity, NoteOverview } from '../types';

defineOptions({ name: 'OverviewTab' });

const props = defineProps<{
  dailyData: DailyTaskOverview;
  weeklyGoals: DailyTaskOverview['weeklyGoals'];
  countdownData: CountdownOverview;
  assetData: AssetOverview;
  noteData: NoteOverview;
  galleryData: GalleryOverview;
  aiData: AiOverview;
  activities: NikkiActivity[];
}>();

defineEmits<{
  switchTab: [tab: string];
  toggleTask: [id: string, done: boolean];
  toggleVersionReminder: [id: string, reminded: boolean];
  openDetail: [payload: { type: string; id: string }];
}>();

const iconMap: Record<string, typeof CheckCircle2> = {
  CheckCircle2,
  PenSquare,
  Camera,
  Map,
  Star
};

const radius = 36;
const circumference = 2 * Math.PI * radius;
const strokeDashoffset = computed(() => {
  return circumference - (props.dailyData.completed / props.dailyData.total) * circumference;
});

const weeklyCompletedText = computed(() => {
  const completed = props.weeklyGoals.filter(g => g.status === 'completed').length;
  return `${completed}/${props.weeklyGoals.length} 已完成`;
});
</script>

<template>
  <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
    <!-- 今日游戏日常 -->
    <LifeGeminiCard title="今日游戏日常" action-text="查看全部" @action="$emit('switchTab', '日常')">
      <div class="flex gap-4">
        <div class="w-1/3 flex flex-col items-center justify-center bg-slate-50/50 rounded-2xl py-4">
          <div class="nikki-progress-ring">
            <svg class="w-24 h-24">
              <circle cx="48" cy="48" :r="radius" stroke="currentColor" stroke-width="6" fill="transparent" class="ring-track" />
              <circle
                cx="48"
                cy="48"
                :r="radius"
                stroke="currentColor"
                stroke-width="6"
                fill="transparent"
                :stroke-dasharray="circumference"
                :stroke-dashoffset="strokeDashoffset"
                class="ring-fill"
              />
            </svg>
            <div class="ring-label">
              <span class="text-xl font-bold text-slate-800">{{ dailyData.completed }}/{{ dailyData.total }}</span>
              <span class="text-[10px] text-slate-400 mt-0.5">已完成</span>
            </div>
          </div>
        </div>
        <div class="w-2/3 space-y-3">
          <div
            v-for="task in dailyData.taskGroups[0]?.tasks.slice(0, 6)"
            :key="task.id"
            class="nikki-task-item"
            @click="$emit('toggleTask', task.id, !task.done)"
          >
            <div class="flex items-center space-x-2">
              <CheckCircle2 v-if="task.done" :size="16" class="text-indigo-500 shrink-0" />
              <Circle v-else :size="16" class="text-slate-300 shrink-0" />
              <span class="task-text" :class="[task.done ? 'text-slate-500' : 'text-slate-700']">{{ task.text }}</span>
            </div>
            <span v-if="task.detail" class="task-detail">{{ task.detail }}</span>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 本周任务 -->
    <LifeGeminiCard title="本周任务" :action-text="weeklyCompletedText">
      <div class="space-y-4">
        <div v-for="goal in weeklyGoals.slice(0, 5)" :key="goal.id">
          <div class="flex justify-between items-center mb-1.5">
            <div class="flex items-center space-x-2">
              <div
                v-if="goal.status === 'completed'"
                class="w-4 h-4 rounded-full bg-indigo-50 flex items-center justify-center"
              >
                <CheckCircle2 :size="12" class="text-indigo-500" />
              </div>
              <div v-else class="w-4 h-4 rounded-full bg-rose-50 flex items-center justify-center">
                <span class="w-1.5 h-1.5 bg-rose-400 rounded-sm"></span>
              </div>
              <span class="text-xs" :class="[goal.status === 'completed' ? 'text-slate-600' : 'text-slate-800 font-medium']">
                {{ goal.text }}
              </span>
            </div>
            <span class="text-[10px] text-slate-400">{{ goal.current }}/{{ goal.max }}</span>
          </div>
          <div class="w-full h-1.5 bg-slate-100 rounded-full overflow-hidden">
            <div class="h-full rounded-full" :class="[goal.color]" :style="{ width: `${goal.progress}%` }"></div>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 活动倒计时 -->
    <LifeGeminiCard title="活动倒计时" action-text="查看全部" @action="$emit('switchTab', '倒计时')">
      <div class="space-y-4">
        <div
          v-for="event in countdownData.events"
          :key="event.id"
          class="nikki-countdown-item"
          @click="$emit('openDetail', { type: 'event', id: event.id })"
        >
          <div class="flex items-center space-x-3">
            <img :src="event.img" alt="" class="w-12 h-12 rounded-lg object-cover shadow-sm" />
            <div>
              <p class="text-sm font-bold text-slate-800 group-hover:text-indigo-600 transition-colors">{{ event.title }}</p>
              <p class="text-[10px] text-slate-400 mt-0.5 flex items-center">
                <Clock :size="10" class="mr-1" /> {{ event.endTime }} 结束
              </p>
            </div>
          </div>
          <div class="countdown-days">
            <span class="text-2xl font-bold">{{ event.remainingDays }}</span>
            <span class="text-xs">天</span>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 直播 / 版本更新 -->
    <LifeGeminiCard title="直播 / 版本更新" action-text="查看全部" @action="$emit('switchTab', '倒计时')">
      <div class="space-y-5">
        <div
          v-for="node in countdownData.versionNodes"
          :key="node.id"
          class="nikki-version-card" :class="[node.type === 'live' ? 'live' : 'maintenance']"
        >
          <div class="flex justify-between items-start mb-2">
            <div>
              <span class="version-tag" :class="[node.type === 'live' ? 'bg-rose-100 text-rose-500' : 'bg-indigo-100 text-indigo-500']">
                {{ node.type === 'live' ? '直播预告' : '版本更新' }}
              </span>
              <p class="text-sm font-bold text-slate-800">{{ node.title }}</p>
              <p class="text-xs text-slate-500 mt-1">{{ node.startTime }}</p>
            </div>
            <div class="text-right">
              <p class="text-xs font-medium text-slate-600 mb-2">{{ node.daysUntil }} 天后开始</p>
              <button
                class="nikki-action-btn" :class="[node.type === 'live' ? 'outline-rose' : 'outline-indigo']"
                type="button"
                @click.stop="$emit('toggleVersionReminder', node.id, !node.reminded)"
              >
                {{ node.reminded ? '取消提醒' : '设置提醒' }}
              </button>
            </div>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 账号资产 -->
    <LifeGeminiCard title="账号资产" action-text="管理" @action="$emit('switchTab', '资产')">
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
              @click="$emit('switchTab', '资产')"
            >
              查看全部
            </button>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 最近笔记 -->
    <LifeGeminiCard title="最近笔记" action-text="查看全部" @action="$emit('switchTab', '笔记')">
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
          @click="$emit('switchTab', 'AI')"
        >
          <MoreVertical :size="14" class="text-slate-400" />
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
            @click="$emit('switchTab', 'AI')"
          >
            查看完整分析报告 <ChevronRight :size="12" class="ml-0.5" />
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
        v-for="item in activities"
        :key="item.id"
        class="activity-node"
        @click="$emit('openDetail', { type: 'activity', id: item.id })"
      >
        <div class="activity-icon" :class="[item.bg]">
          <component :is="iconMap[item.icon]" :size="20" :class="item.iconColor" />
        </div>
        <p class="text-xs font-bold text-slate-700 text-center">{{ item.title }}</p>
        <p class="text-[10px] text-slate-500 text-center mt-0.5">{{ item.description }}</p>
        <p class="text-[9px] text-slate-400 text-center mt-1">{{ item.time }}</p>
      </div>
    </div>
  </section>
</template>

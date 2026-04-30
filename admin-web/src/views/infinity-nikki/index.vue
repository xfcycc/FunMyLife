<template>
  <LifeGeminiShell>
    <LifeGeminiProjectHero
      title="无限暖暖"
      description="收集美好的瞬间，搭配无限的可能"
      cover-src="https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&q=80&w=800&h=400"
      cover-alt="Infinity Nikki"
      :tags="projectTags"
      :stats="projectStats"
    >
      <template #title-icon>
        <Star class="text-amber-400 fill-amber-400 w-5 h-5" />
      </template>

      <template #actions>
        <button class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-xl font-medium text-sm flex items-center space-x-1.5" type="button">
          <Plus :size="16" />
          <span>快速记录</span>
          <ChevronRight :size="14" class="ml-1 opacity-70" />
        </button>
        <button class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5" type="button">
          <Share :size="14" />
          <span>分享</span>
        </button>
        <button class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5" type="button">
          <Settings :size="14" />
          <span>设置</span>
        </button>
        <button class="w-10 h-10 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 flex items-center justify-center" type="button">
          <MoreHorizontal :size="16" />
        </button>
      </template>
    </LifeGeminiProjectHero>

    <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
      <LifeGeminiCard title="今日游戏日常" action-text="查看全部">
        <div class="flex gap-4">
          <div class="w-1/3 flex flex-col items-center justify-center bg-slate-50/50 rounded-2xl py-4">
            <div class="relative flex items-center justify-center">
              <svg class="transform -rotate-90 w-24 h-24">
                <circle cx="48" cy="48" :r="radius" stroke="currentColor" stroke-width="6" fill="transparent" class="text-slate-100" />
                <circle
                  cx="48"
                  cy="48"
                  :r="radius"
                  stroke="currentColor"
                  stroke-width="6"
                  fill="transparent"
                  :stroke-dasharray="circumference"
                  :stroke-dashoffset="strokeDashoffset"
                  class="text-indigo-500 transition-all duration-1000 ease-in-out"
                  stroke-linecap="round"
                />
              </svg>
              <div class="absolute flex flex-col items-center justify-center">
                <span class="text-xl font-bold text-slate-800">{{ progressValue }}/{{ progressMax }}</span>
                <span class="text-[10px] text-slate-400 mt-0.5">已完成</span>
              </div>
            </div>
          </div>
          <div class="w-2/3 space-y-3">
            <div v-for="(task, index) in dailyTasks" :key="index" class="flex items-center justify-between group">
              <div class="flex items-center space-x-2">
                <CheckCircle2 v-if="task.done" :size="16" class="text-indigo-500 shrink-0" />
                <Circle v-else :size="16" class="text-slate-300 group-hover:text-indigo-300 shrink-0" />
                <span :class="['text-xs', task.done ? 'text-slate-500' : 'text-slate-700']">{{ task.text }}</span>
              </div>
              <span v-if="task.detail" class="text-[10px] text-slate-400 font-medium">{{ task.detail }}</span>
            </div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="本周任务" action-text="4/7 已完成">
        <div class="space-y-4">
          <div v-for="(task, index) in weeklyTasks" :key="index">
            <div class="flex justify-between items-center mb-1.5">
              <div class="flex items-center space-x-2">
                <div v-if="task.done" class="w-4 h-4 rounded-full bg-indigo-50 flex items-center justify-center">
                  <CheckCircle2 :size="12" class="text-indigo-500" />
                </div>
                <div v-else class="w-4 h-4 rounded-full bg-rose-50 flex items-center justify-center">
                  <span class="w-1.5 h-1.5 bg-rose-400 rounded-sm"></span>
                </div>
                <span :class="['text-xs', task.done ? 'text-slate-600' : 'text-slate-800 font-medium']">{{ task.text }}</span>
              </div>
              <span class="text-[10px] text-slate-400">{{ task.current }}/{{ task.max }}</span>
            </div>
            <div class="w-full h-1.5 bg-slate-100 rounded-full overflow-hidden">
              <div :class="['h-full rounded-full', task.color]" :style="{ width: `${task.progress}%` }"></div>
            </div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="活动倒计时" action-text="查看全部">
        <div class="space-y-4">
          <div
            v-for="(event, index) in events"
            :key="index"
            class="flex items-center justify-between group cursor-pointer hover:bg-slate-50 p-2 -mx-2 rounded-xl transition-colors"
          >
            <div class="flex items-center space-x-3">
              <img :src="event.img" alt="" class="w-12 h-12 rounded-lg object-cover shadow-sm" />
              <div>
                <p class="text-sm font-bold text-slate-800 group-hover:text-indigo-600 transition-colors">{{ event.title }}</p>
                <p class="text-[10px] text-slate-400 mt-0.5 flex items-center">
                  <Clock :size="10" class="mr-1" /> {{ event.end }}
                </p>
              </div>
            </div>
            <div class="flex items-baseline space-x-1 text-rose-500">
              <span class="text-2xl font-bold">{{ event.days }}</span>
              <span class="text-xs">天</span>
            </div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="直播 / 版本更新" action-text="查看全部">
        <div class="space-y-5">
          <div class="bg-rose-50/50 rounded-2xl p-4 border border-rose-100">
            <div class="flex justify-between items-start mb-2">
              <div>
                <span class="inline-block px-2 py-0.5 bg-rose-100 text-rose-500 text-[10px] font-bold rounded mb-1.5">直播预告</span>
                <p class="text-sm font-bold text-slate-800">5.2版本前瞻特别节目</p>
                <p class="text-xs text-slate-500 mt-1">04-30 19:30</p>
              </div>
              <div class="text-right">
                <p class="text-xs font-medium text-slate-600 mb-2">2 天后开始</p>
                <button class="px-3 py-1 bg-white border border-rose-200 text-rose-500 text-[10px] font-medium rounded-full hover:bg-rose-50" type="button">
                  设置提醒
                </button>
              </div>
            </div>
          </div>

          <div class="bg-indigo-50/50 rounded-2xl p-4 border border-indigo-100">
            <div class="flex justify-between items-start mb-2">
              <div>
                <span class="inline-block px-2 py-0.5 bg-indigo-100 text-indigo-500 text-[10px] font-bold rounded mb-1.5">版本更新</span>
                <p class="text-sm font-bold text-slate-800">5.2版本更新维护</p>
                <p class="text-xs text-slate-500 mt-1">05-02 06:00 - 12:00</p>
              </div>
              <div class="text-right">
                <p class="text-xs font-medium text-slate-600 mb-2">3 天后开始</p>
                <button class="px-3 py-1 bg-white border border-indigo-200 text-indigo-500 text-[10px] font-medium rounded-full hover:bg-indigo-50" type="button">
                  设置提醒
                </button>
              </div>
            </div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="账号资产" action-text="管理">
        <div class="flex items-start space-x-6">
          <div class="w-24 h-24 bg-gradient-to-br from-indigo-300 to-indigo-500 rounded-2xl shadow-lg relative flex items-center justify-center flex-shrink-0 border-b-4 border-indigo-600">
            <div class="w-16 h-16 border-2 border-indigo-200/50 rounded-xl relative flex items-center justify-center">
              <div class="w-8 h-8 rounded-full border-[3px] border-white/80 flex items-center justify-center bg-indigo-400">
                <div class="w-2 h-2 rounded-full bg-white"></div>
              </div>
              <div class="absolute top-2 left-2 w-2 h-0.5 bg-white/40 rounded"></div>
              <div class="absolute top-2 right-2 w-2 h-0.5 bg-white/40 rounded"></div>
            </div>
          </div>

          <div class="flex-1 space-y-3">
            <div v-for="(acc, index) in assets" :key="index" class="flex justify-between items-center pb-2 border-b border-slate-50 last:border-0 last:pb-0">
              <div class="flex items-center space-x-2">
                <div class="w-5 h-5 rounded bg-emerald-100 flex items-center justify-center text-emerald-500">
                  <ShieldCheck :size="12" />
                </div>
                <span class="text-xs font-medium text-slate-700">{{ acc.name }}</span>
              </div>
              <span class="text-[10px] text-slate-400">{{ acc.status }}</span>
            </div>
            <div class="pt-2 flex justify-between items-center">
              <span class="text-xs text-slate-500">共 3 个资产</span>
              <button class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium" type="button">查看全部</button>
            </div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="最近笔记" action-text="查看全部">
        <div class="space-y-4">
          <div v-for="(note, index) in notes" :key="index" class="flex items-start justify-between group cursor-pointer">
            <div class="flex items-start space-x-2 pr-4">
              <div class="mt-0.5">
                <BookOpen :size="14" class="text-slate-300 group-hover:text-indigo-400 transition-colors" />
              </div>
              <div>
                <p class="text-xs font-medium text-slate-700 group-hover:text-indigo-600 transition-colors line-clamp-1">{{ note.text }}</p>
                <span class="inline-block mt-1 px-1.5 py-0.5 bg-slate-100 text-slate-500 text-[9px] rounded">{{ note.tag }}</span>
              </div>
            </div>
            <span class="text-[10px] text-slate-400 whitespace-nowrap pt-0.5">{{ note.time }}</span>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="截图图册" action-text="查看全部">
        <div class="grid grid-cols-3 gap-2 mb-3">
          <div v-for="(img, index) in galleries" :key="index" class="aspect-[4/3] rounded-lg overflow-hidden bg-slate-100 cursor-pointer group relative">
            <img :src="img" alt="Screenshot" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300" />
            <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors"></div>
          </div>
        </div>
        <p class="text-[10px] text-slate-400">共 8 个图册，126 张图片</p>
      </LifeGeminiCard>

      <LifeGeminiCard class="bg-gradient-to-br from-indigo-50/50 to-purple-50/50">
        <template #title>
          ✨ AI 项目建议
        </template>
        <template #action>
          <button class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium" type="button">
            <MoreVertical :size="14" class="text-slate-400" />
          </button>
        </template>
        <div class="relative h-full flex flex-col justify-between">
          <ul class="space-y-2 text-xs text-slate-600 relative z-10">
            <li class="flex items-start">
              <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
              <span>你今天还有 <strong class="text-rose-500">3个日常任务未完成</strong>，建议优先处理！</span>
            </li>
            <li class="flex items-start">
              <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
              <span>活动「奇迹之旅·第二章」还有 <strong class="text-indigo-600">3天结束</strong>，别错过奖励。</span>
            </li>
            <li class="flex items-start">
              <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
              <span>根据你的搭配风格，推荐优先收集「晨曦花园」套装。</span>
            </li>
            <li class="flex items-start">
              <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
              <span>本周素材收集进度偏慢，建议安排集中采集时间。</span>
            </li>
          </ul>

          <div class="mt-4 flex justify-between items-end relative z-10">
            <button class="appearance-none bg-transparent border-0 p-0 text-xs font-medium text-indigo-600 hover:text-indigo-700 flex items-center" type="button">
              查看完整分析报告 <ChevronRight :size="12" class="ml-0.5" />
            </button>

            <div class="w-16 h-16 absolute right-0 bottom-[-10px] opacity-90 drop-shadow-md">
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

    <section class="mt-6 bg-white rounded-[2rem] p-6 shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100">
      <div class="flex justify-between items-center mb-6">
        <h3 class="font-bold text-slate-800 text-base">最近动态</h3>
        <button class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium flex items-center" type="button">
          查看全部动态 <ChevronRight :size="14" class="ml-0.5" />
        </button>
      </div>

      <div class="flex items-center justify-between relative">
        <div class="absolute left-6 right-6 top-6 h-0.5 bg-slate-100 -z-10"></div>

        <div v-for="(item, index) in activities" :key="index" class="flex flex-col items-center bg-white px-2 group cursor-pointer">
          <div :class="['w-12 h-12 rounded-2xl flex items-center justify-center mb-3 shadow-sm border border-white ring-4 ring-white group-hover:scale-110 transition-transform', item.bg]">
            <component :is="item.icon" :size="20" :class="item.iconColor" />
          </div>
          <p class="text-xs font-bold text-slate-700 text-center">{{ item.title }}</p>
          <p class="text-[10px] text-slate-500 text-center mt-0.5">{{ item.desc }}</p>
          <p class="text-[9px] text-slate-400 text-center mt-1">{{ item.time }}</p>
        </div>
      </div>
    </section>
  </LifeGeminiShell>
</template>

<script setup lang="ts">
import { computed, ref } from 'vue';
import { BookOpen, CheckCircle2, ChevronRight, Circle, Clock, Info, Map, MoreHorizontal, MoreVertical, PenSquare, Plus, Settings, Share, ShieldCheck, Star } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import type { LifeGeminiProjectStat, LifeGeminiProjectTag } from '@/components/life-manager/types';

defineOptions({
  name: 'InfinityNikki'
});

const activeTab = ref('概览');
const tabs = ['概览', '日常', '倒计时', '资产', '笔记', '图册', 'AI'];

const projectTags: LifeGeminiProjectTag[] = [
  { label: '游戏' },
  { label: '进行中', tone: 'success' }
];

const projectStats: LifeGeminiProjectStat[] = [
  { label: '创建时间', value: '2024-12-15' },
  { label: '今日任务进度', value: '5/8' },
  { label: '本周完成率', value: '62%' },
  { label: '项目笔记', value: '12' },
  { label: '图册数量', value: '8' }
];

const radius = 36;
const circumference = 2 * Math.PI * radius;
const progressValue = ref(5);
const progressMax = ref(8);
const strokeDashoffset = computed(() => circumference - (progressValue.value / progressMax.value) * circumference);

const dailyTasks = [
  { text: '每日签到', done: true },
  { text: '心愿原野探索', done: true },
  { text: '搭配竞技场挑战', done: true },
  { text: '每日灵感任务', done: true },
  { text: '采集素材', done: false, detail: '25/50' },
  { text: '捕捉奇想星', done: false, detail: '0/3' },
  { text: '愿望梦境挑战', done: false, detail: '0/2' },
  { text: '体力消耗', done: false, detail: '120/180' }
];

const weeklyTasks = [
  { text: '完成搭配评选 3次', progress: 100, current: 3, max: 3, color: 'bg-indigo-500', done: true },
  { text: '通关心愿梦境 10次', progress: 60, current: 6, max: 10, color: 'bg-indigo-400', done: true },
  { text: '收集奇想星 80个', progress: 65, current: 52, max: 80, color: 'bg-indigo-400', done: true },
  { text: '完成灵感任务 14次', progress: 100, current: 14, max: 14, color: 'bg-indigo-500', done: true },
  { text: '参与搭配赛 2次', progress: 50, current: 1, max: 2, color: 'bg-rose-400', done: false },
  { text: '提升搭配师等级', progress: 76, current: 38, max: 50, color: 'bg-rose-400', done: false },
  { text: '收集套装部件 30个', progress: 73, current: 22, max: 30, color: 'bg-rose-400', done: false }
];

const events = [
  {
    title: '奇迹之旅·第二章',
    end: '2026-05-02 05:00 结束',
    days: '3',
    img: 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=100&h=100&q=80'
  },
  {
    title: '繁花季节 限时活动',
    end: '2026-05-12 05:00 结束',
    days: '13',
    img: 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=100&h=100&q=80'
  },
  {
    title: '累计充值返利',
    end: '2026-05-20 05:00 结束',
    days: '21',
    img: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=100&h=100&q=80'
  }
];

const assets = [
  { name: '官方账号 (主)', status: '已保护' },
  { name: '小号-搭配测试', status: '已保护' },
  { name: 'Nintendo Switch 账号', status: '已保护' }
];

const notes = [
  { text: '5.1版本新增套装分析与抽取建议', tag: '攻略', time: '2小时前' },
  { text: '心愿原野探索路线规划', tag: '探索', time: '昨天' },
  { text: '搭配赛主题：春日茶会灵感整理', tag: '搭配', time: '2天前' },
  { text: '素材收集清单汇总', tag: '自建', time: '3天前' }
];

const galleries = [
  'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80',
  'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80',
  'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80',
  'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80',
  'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=150&h=100&q=80',
  'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80'
];

const activities = [
  { icon: CheckCircle2, iconColor: 'text-emerald-500', bg: 'bg-emerald-50', title: '完成了每日任务', desc: '搭配竞技场挑战', time: '1 小时前' },
  { icon: PenSquare, iconColor: 'text-blue-500', bg: 'bg-blue-50', title: '新增笔记', desc: '5.1版本新增套装分析', time: '2 小时前' },
  { icon: Info, iconColor: 'text-rose-500', bg: 'bg-rose-50', title: '上传了 6 张截图', desc: '心愿原野探索记录', time: '昨天 21:40' },
  { icon: Map, iconColor: 'text-purple-500', bg: 'bg-purple-50', title: '活动进度更新', desc: '奇迹之旅·第二章', time: '昨天 18:30' },
  { icon: Star, iconColor: 'text-amber-500', bg: 'bg-amber-50', title: '版本公告', desc: '5.2版本前瞻公告', time: '04-27 12:00' }
];
</script>

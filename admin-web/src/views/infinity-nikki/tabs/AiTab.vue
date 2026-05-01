<script setup lang="ts">
import { ref } from 'vue';
import { BarChart3, BookOpen, Calendar, ChevronRight, Image, Sparkles, Target, Zap } from 'lucide-vue-next';
import { useMessage } from 'naive-ui';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import type { AiOverview } from '../types';
import { refreshAiSuggestion } from '../service';

defineOptions({ name: 'AiTab' });

defineProps<{
  aiData: AiOverview;
}>();

const emit = defineEmits<{
  refresh: [];
}>();

const message = useMessage();
const refreshing = ref(false);

const dimensions = [
  { label: '任务完成度', desc: '基于日常、周常、活动任务', status: '已分析', icon: Target, bg: 'bg-indigo-50', iconColor: 'text-indigo-500' },
  { label: '活动倒计时', desc: '基于活动结束时间和进度', status: '已分析', icon: Calendar, bg: 'bg-rose-50', iconColor: 'text-rose-500' },
  { label: '素材收集', desc: '基于素材收集进度', status: '已分析', icon: Zap, bg: 'bg-amber-50', iconColor: 'text-amber-500' },
  { label: '搭配风格', desc: '基于搭配赛和套装收集', status: '已分析', icon: Sparkles, bg: 'bg-purple-50', iconColor: 'text-purple-500' },
  { label: '笔记记录', desc: '基于攻略和探索笔记', status: '已分析', icon: BookOpen, bg: 'bg-emerald-50', iconColor: 'text-emerald-500' },
  { label: '截图图册', desc: '基于截图数量和分类', status: '已分析', icon: Image, bg: 'bg-blue-50', iconColor: 'text-blue-500' },
  { label: '项目统计', desc: '基于整体项目数据', status: '已分析', icon: BarChart3, bg: 'bg-slate-100', iconColor: 'text-slate-500' }
];

const historyItems = [
  { id: 'h1', title: '5.1版本活动总结', time: '04-28' },
  { id: 'h2', title: '本周日常复盘', time: '04-27' },
  { id: 'h3', title: '搭配赛回顾与建议', time: '04-25' },
  { id: 'h4', title: '素材收集效率分析', time: '04-22' }
];

function highlightBg(type: string) {
  const map = {
    danger: 'bg-rose-100 text-rose-600',
    info: 'bg-indigo-100 text-indigo-600',
    success: 'bg-emerald-100 text-emerald-600',
    warning: 'bg-amber-100 text-amber-600'
  };
  return map[type as keyof typeof map] ?? 'bg-slate-100 text-slate-600';
}

async function handleRefresh() {
  refreshing.value = true;
  try {
    await refreshAiSuggestion();
    message.success('AI 建议已刷新');
    emit('refresh');
  } catch {
    message.error('刷新失败，请重试');
  } finally {
    refreshing.value = false;
  }
}
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- AI 建议主卡片 -->
      <div class="lg:col-span-2">
        <LifeGeminiCard class="nikki-ai-card">
          <template #title>
            <div class="flex items-center gap-2">
              <span>✨ AI 项目建议</span>
              <span class="px-2 py-0.5 bg-indigo-100 text-indigo-600 text-[10px] font-medium rounded-full">
                {{ aiData.suggestionCount }} 条建议
              </span>
            </div>
          </template>
          <template #action>
            <button
              class="nikki-action-btn outline-indigo"
              type="button"
              :disabled="refreshing"
              @click="handleRefresh"
            >
              {{ refreshing ? '生成中...' : '刷新建议' }}
            </button>
          </template>
          <div class="space-y-4">
            <p class="text-sm text-slate-600">{{ aiData.currentSuggestion.content }}</p>
            <ul class="space-y-3">
              <li
                v-for="(h, i) in aiData.currentSuggestion.highlights"
                :key="i"
                class="flex items-start p-3 bg-white/60 rounded-xl"
              >
                <span class="shrink-0 w-6 h-6 rounded-full flex items-center justify-center text-xs font-bold mr-3" :class="highlightBg(h.type)">
                  {{ i + 1 }}
                </span>
                <div>
                  <p class="text-sm text-slate-700">{{ h.text }}</p>
                </div>
              </li>
            </ul>
            <div class="text-[10px] text-slate-400 text-right">
              生成于 {{ aiData.currentSuggestion.generatedAt }}
            </div>
          </div>
        </LifeGeminiCard>
      </div>

      <!-- AI 信息面板 -->
      <div class="space-y-6">
        <LifeGeminiCard title="AI 分析维度">
          <div class="space-y-3">
            <div v-for="dim in dimensions" :key="dim.label" class="flex items-center gap-3 p-2 -mx-2 rounded-lg hover:bg-slate-50 transition-colors">
              <div class="w-8 h-8 rounded-lg flex items-center justify-center" :class="[dim.bg]">
                <component :is="dim.icon" :size="14" :class="dim.iconColor" />
              </div>
              <div class="flex-1 min-w-0">
                <p class="text-xs font-medium text-slate-700">{{ dim.label }}</p>
                <p class="text-[10px] text-slate-400 truncate">{{ dim.desc }}</p>
              </div>
              <span class="text-[10px] px-1.5 py-0.5 rounded bg-slate-100 text-slate-500">{{ dim.status }}</span>
            </div>
          </div>
        </LifeGeminiCard>

        <LifeGeminiCard title="历史建议">
          <div class="space-y-3">
            <div
              v-for="item in historyItems"
              :key="item.id"
              class="flex items-center gap-3 p-2 -mx-2 rounded-lg hover:bg-slate-50 cursor-pointer transition-colors"
            >
              <div class="w-8 h-8 rounded-lg bg-indigo-50 flex items-center justify-center">
                <Sparkles :size="14" class="text-indigo-400" />
              </div>
              <div class="flex-1 min-w-0">
                <p class="text-xs font-medium text-slate-700 truncate">{{ item.title }}</p>
                <p class="text-[10px] text-slate-400">{{ item.time }}</p>
              </div>
              <ChevronRight :size="12" class="text-slate-300" />
            </div>
          </div>
        </LifeGeminiCard>
      </div>
    </div>
  </div>
</template>

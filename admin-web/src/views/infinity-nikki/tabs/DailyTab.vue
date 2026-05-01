<script setup lang="ts">
import { computed, ref } from 'vue';
import { CheckCircle2, Circle, CircleDot } from 'lucide-vue-next';
import { useMessage } from 'naive-ui';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import type { DailyTask, DailyTaskOverview, WeeklyGoal } from '../types';
import { toggleTask, toggleWeeklyGoalStatus, updateWeeklyGoalProgress } from '../service';

defineOptions({ name: 'DailyTab' });

const props = defineProps<{
  dailyData: DailyTaskOverview;
}>();

const emit = defineEmits<{
  refresh: [];
}>();

const message = useMessage();
const activeGroup = ref<'daily' | 'weekly' | 'event'>('daily');
const selectedGoalId = ref<string>('');

const groupOptions = computed(() => [
  { value: 'daily' as const, label: '今日', count: props.dailyData.taskGroups.filter(g => g.type === 'daily').reduce((s, g) => s + g.tasks.length, 0) },
  { value: 'weekly' as const, label: '周常', count: props.dailyData.taskGroups.filter(g => g.type === 'weekly').reduce((s, g) => s + g.tasks.length, 0) },
  { value: 'event' as const, label: '活动', count: props.dailyData.taskGroups.filter(g => g.type === 'event').reduce((s, g) => s + g.tasks.length, 0) }
]);

const currentGroupTasks = computed(() => {
  return props.dailyData.taskGroups.filter(g => g.type === activeGroup.value).flatMap(g => g.tasks);
});

const currentGroupLabel = computed(() => {
  const map = { daily: '今日任务', weekly: '周常任务', event: '活动任务' };
  return map[activeGroup.value];
});

const currentGroupCompleted = computed(() => currentGroupTasks.value.filter(t => t.done).length);

const weeklyGoals = computed(() => props.dailyData.weeklyGoals);

const weeklyCompletedText = computed(() => {
  const c = weeklyGoals.value.filter(g => g.status === 'completed').length;
  return `${c}/${weeklyGoals.value.length} 已完成`;
});

const overallProgress = computed(() => {
  if (props.dailyData.total === 0) return 0;
  return Math.round((props.dailyData.completed / props.dailyData.total) * 100);
});

function statusLabel(s: WeeklyGoal['status']) {
  const map = { on_track: '推进中', needs_attention: '需关注', completed: '已完成' };
  return map[s];
}

function statusBtnClass(s: WeeklyGoal['status']) {
  const map = {
    on_track: 'bg-blue-50 text-blue-600 border-blue-200',
    needs_attention: 'bg-rose-50 text-rose-600 border-rose-200',
    completed: 'bg-indigo-50 text-indigo-600 border-indigo-200'
  };
  return map[s];
}

async function handleToggleTask(task: DailyTask) {
  await toggleTask(task.id, !task.done);
  message.success(task.done ? '任务已恢复' : '任务已完成');
  emit('refresh');
}

async function handleAdjustGoal(goal: WeeklyGoal, delta: number) {
  const next = Math.max(0, Math.min(goal.max, goal.current + delta));
  if (next === goal.current) {
    message.warning(next >= goal.max ? '目标已达上限' : '目标已归零');
    return;
  }
  await updateWeeklyGoalProgress(goal.id, next);
  message.success(`进度已更新：${next}/${goal.max}`);
  emit('refresh');
}

async function handleToggleStatus(goal: WeeklyGoal) {
  const cycle: WeeklyGoal['status'][] = ['on_track', 'needs_attention', 'completed'];
  const next = cycle[(cycle.indexOf(goal.status) + 1) % cycle.length];
  await toggleWeeklyGoalStatus(goal.id, next);
  message.info(`状态已切换为「${statusLabel(next)}」`);
  emit('refresh');
}
</script>

<template>
  <div class="space-y-6">
    <!-- 分组切换 -->
    <div class="flex items-center justify-between">
      <div class="nikki-group-switch">
        <button
          v-for="g in groupOptions"
          :key="g.value"
          class="group-btn" :class="[activeGroup === g.value ? 'active' : '']"
          type="button"
          @click="activeGroup = g.value"
        >
          {{ g.label }}
          <span class="ml-1 text-[10px] opacity-60">({{ g.count }})</span>
        </button>
      </div>
      <div class="flex items-center gap-2">
        <span class="text-xs text-slate-500">进度</span>
        <div class="w-32 h-2 bg-slate-100 rounded-full overflow-hidden">
          <div class="h-full bg-indigo-500 rounded-full transition-all duration-500" :style="{ width: `${overallProgress}%` }"></div>
        </div>
        <span class="text-xs font-medium text-indigo-600">{{ overallProgress }}%</span>
      </div>
    </div>

    <div class="grid grid-cols-1 lg:grid-cols-3 gap-6">
      <!-- 任务列表 -->
      <div class="lg:col-span-2">
        <LifeGeminiCard :title="currentGroupLabel">
          <template #action>
            <span class="text-xs text-slate-400">{{ currentGroupCompleted }}/{{ currentGroupTasks.length }} 已完成</span>
          </template>
          <div class="space-y-3 max-h-[400px] overflow-y-auto pr-1">
            <div
              v-for="task in currentGroupTasks"
              :key="task.id"
              class="nikki-task-item py-2 px-3 -mx-3 rounded-xl hover:bg-slate-50 transition-colors"
              @click="handleToggleTask(task)"
            >
              <div class="flex items-center space-x-3">
                <CheckCircle2 v-if="task.done" :size="18" class="text-indigo-500 shrink-0" />
                <Circle v-else :size="18" class="text-slate-300 shrink-0" />
                <span class="text-sm" :class="[task.done ? 'text-slate-400 line-through' : 'text-slate-700']">{{ task.text }}</span>
              </div>
              <span v-if="task.detail" class="text-xs text-slate-400 font-medium bg-slate-50 px-2 py-0.5 rounded">{{ task.detail }}</span>
            </div>
            <div v-if="currentGroupTasks.length === 0" class="nikki-empty-state">
              <CircleDot :size="32" />
              <p>该分组暂无任务</p>
            </div>
          </div>
        </LifeGeminiCard>
      </div>

      <!-- 周常目标面板 -->
      <div>
        <LifeGeminiCard title="周常目标" :action-text="weeklyCompletedText">
          <div class="space-y-4 max-h-[400px] overflow-y-auto pr-1">
            <div
              v-for="goal in weeklyGoals"
              :key="goal.id"
              class="p-3 -mx-3 rounded-xl cursor-pointer transition-colors" :class="[selectedGoalId === goal.id ? 'bg-indigo-50/50' : 'hover:bg-slate-50']"
              @click="selectedGoalId = goal.id"
            >
              <div class="flex justify-between items-center mb-2">
                <div class="flex items-center space-x-2">
                  <div
                    v-if="goal.status === 'completed'"
                    class="w-5 h-5 rounded-full bg-indigo-100 flex items-center justify-center"
                  >
                    <CheckCircle2 :size="12" class="text-indigo-500" />
                  </div>
                  <div
                    v-else-if="goal.status === 'needs_attention'"
                    class="w-5 h-5 rounded-full bg-rose-50 flex items-center justify-center"
                  >
                    <span class="w-2 h-2 bg-rose-400 rounded-sm"></span>
                  </div>
                  <div v-else class="w-5 h-5 rounded-full bg-blue-50 flex items-center justify-center">
                    <span class="w-2 h-2 bg-blue-400 rounded-full"></span>
                  </div>
                  <span class="text-sm" :class="[goal.status === 'completed' ? 'text-slate-500' : 'text-slate-800 font-medium']">
                    {{ goal.text }}
                  </span>
                </div>
                <button
                  class="text-[10px] px-2 py-0.5 rounded-full border transition-colors"
                  :class="statusBtnClass(goal.status)"
                  type="button"
                  @click.stop="handleToggleStatus(goal)"
                >
                  {{ statusLabel(goal.status) }}
                </button>
              </div>
              <div class="flex items-center gap-3">
                <div class="flex-1 h-1.5 bg-slate-100 rounded-full overflow-hidden">
                  <div class="h-full rounded-full transition-all duration-500" :class="[goal.color]" :style="{ width: `${goal.progress}%` }"></div>
                </div>
                <div v-if="selectedGoalId === goal.id" class="flex items-center gap-1">
                  <button
                    class="w-6 h-6 rounded-full bg-slate-100 hover:bg-slate-200 flex items-center justify-center text-slate-600 text-xs font-bold transition-colors"
                    type="button"
                    @click.stop="handleAdjustGoal(goal, -1)"
                  >
                    -
                  </button>
                  <span class="text-xs font-medium text-slate-700 min-w-[40px] text-center">{{ goal.current }}/{{ goal.max }}</span>
                  <button
                    class="w-6 h-6 rounded-full bg-indigo-100 hover:bg-indigo-200 flex items-center justify-center text-indigo-600 text-xs font-bold transition-colors"
                    type="button"
                    @click.stop="handleAdjustGoal(goal, 1)"
                  >
                    +
                  </button>
                </div>
                <span v-else class="text-xs text-slate-400">{{ goal.current }}/{{ goal.max }}</span>
              </div>
            </div>
          </div>
        </LifeGeminiCard>
      </div>
    </div>
  </div>
</template>

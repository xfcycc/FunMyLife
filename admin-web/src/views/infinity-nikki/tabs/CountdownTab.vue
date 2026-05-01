<script setup lang="ts">
import { Clock, Radio, Timer } from 'lucide-vue-next';
import { useMessage } from 'naive-ui';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import type { CountdownOverview, EventCountdown, VersionNode } from '../types';
import { toggleEventCheckIn, toggleEventReminder, toggleVersionCheckIn, toggleVersionReminder } from '../service';

defineOptions({ name: 'CountdownTab' });

const props = defineProps<{
  countdownData: CountdownOverview;
}>();

const emit = defineEmits<{
  refresh: [];
  openDetail: [payload: { type: string; id: string }];
}>();

const message = useMessage();

const events = props.countdownData.events;
const versionNodes = props.countdownData.versionNodes;

async function handleToggleReminder(event: EventCountdown) {
  await toggleEventReminder(event.id, !event.reminded);
  message.success(event.reminded ? '已取消提醒' : '已设置提醒');
  emit('refresh');
}

async function handleToggleCheckIn(event: EventCountdown) {
  await toggleEventCheckIn(event.id, !event.checkedIn);
  message.success(event.checkedIn ? '已撤回打卡' : '打卡成功');
  emit('refresh');
}

async function handleToggleVersionReminder(node: VersionNode) {
  await toggleVersionReminder(node.id, !node.reminded);
  message.success(node.reminded ? '已取消提醒' : '已设置提醒');
  emit('refresh');
}

async function handleToggleVersionCheckIn(node: VersionNode) {
  await toggleVersionCheckIn(node.id, !node.checkedIn);
  message.success(node.checkedIn ? '已撤回打卡' : '打卡成功');
  emit('refresh');
}
</script>

<template>
  <div class="space-y-6">
    <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
      <!-- 活动倒计时 -->
      <LifeGeminiCard title="活动倒计时" :action-text="`共 ${events.length} 个活动`">
        <div class="space-y-4 max-h-[500px] overflow-y-auto pr-1">
          <div
            v-for="event in events"
            :key="event.id"
            class="p-4 -mx-4 rounded-2xl hover:bg-slate-50 transition-colors cursor-pointer"
            @click="$emit('openDetail', { type: 'event', id: event.id })"
          >
            <div class="flex items-center justify-between">
              <div class="flex items-center space-x-3">
                <img :src="event.img" alt="" class="w-14 h-14 rounded-xl object-cover shadow-sm" />
                <div>
                  <p class="text-sm font-bold text-slate-800">{{ event.title }}</p>
                  <p v-if="event.description" class="text-xs text-slate-500 mt-0.5 line-clamp-1">{{ event.description }}</p>
                  <p class="text-[10px] text-slate-400 mt-1 flex items-center">
                    <Clock :size="10" class="mr-1" /> {{ event.endTime }} 结束
                  </p>
                </div>
              </div>
              <div class="text-right flex flex-col items-end gap-2">
                <div class="flex items-baseline space-x-1 text-rose-500">
                  <span class="text-3xl font-bold">{{ event.remainingDays }}</span>
                  <span class="text-sm">天</span>
                </div>
                <div class="flex gap-2">
                  <button
                    class="nikki-action-btn" :class="[event.reminded ? 'outline' : 'outline-rose']"
                    type="button"
                    @click.stop="handleToggleReminder(event)"
                  >
                    {{ event.reminded ? '已提醒' : '设置提醒' }}
                  </button>
                  <button
                    class="nikki-action-btn" :class="[event.checkedIn ? 'primary' : 'outline']"
                    type="button"
                    @click.stop="handleToggleCheckIn(event)"
                  >
                    {{ event.checkedIn ? '已打卡' : '打卡' }}
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div v-if="events.length === 0" class="nikki-empty-state">
            <Timer :size="32" />
            <p>暂无活动倒计时</p>
          </div>
        </div>
      </LifeGeminiCard>

      <!-- 直播 / 版本更新 -->
      <LifeGeminiCard title="直播 / 版本更新" :action-text="`共 ${versionNodes.length} 个节点`">
        <div class="space-y-5 max-h-[500px] overflow-y-auto pr-1">
          <div
            v-for="node in versionNodes"
            :key="node.id"
            class="nikki-version-card" :class="[node.type === 'live' ? 'live' : 'maintenance']"
            @click="$emit('openDetail', { type: 'version', id: node.id })"
          >
            <div class="flex justify-between items-start">
              <div>
                <span class="version-tag" :class="[node.type === 'live' ? 'bg-rose-100 text-rose-500' : 'bg-indigo-100 text-indigo-500']">
                  {{ node.type === 'live' ? '直播预告' : node.type === 'maintenance' ? '版本维护' : '版本更新' }}
                </span>
                <p class="text-sm font-bold text-slate-800">{{ node.title }}</p>
                <p class="text-xs text-slate-500 mt-1">
                  {{ node.startTime }}
                  <template v-if="node.endTime"> - {{ node.endTime.split(' ')[1] }}</template>
                </p>
              </div>
              <div class="text-right">
                <p class="text-lg font-bold text-slate-700 mb-1">{{ node.daysUntil }} <span class="text-xs font-normal text-slate-400">天后</span></p>
                <div class="flex gap-2">
                  <button
                    class="nikki-action-btn" :class="[node.type === 'live' ? 'outline-rose' : 'outline-indigo']"
                    type="button"
                    @click.stop="handleToggleVersionReminder(node)"
                  >
                    {{ node.reminded ? '取消提醒' : '设置提醒' }}
                  </button>
                  <button
                    class="nikki-action-btn" :class="[node.checkedIn ? 'primary' : 'outline']"
                    type="button"
                    @click.stop="handleToggleVersionCheckIn(node)"
                  >
                    {{ node.checkedIn ? '已打卡' : '打卡' }}
                  </button>
                </div>
              </div>
            </div>
          </div>
          <div v-if="versionNodes.length === 0" class="nikki-empty-state">
            <Radio :size="32" />
            <p>暂无版本节点</p>
          </div>
        </div>
      </LifeGeminiCard>
    </div>
  </div>
</template>

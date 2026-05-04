<script setup lang="ts">
import { computed, onMounted, ref } from 'vue';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import LifeGeminiTopActions from '@/components/life-manager/LifeGeminiTopActions.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import type { LifeGeminiProjectStat, LifeGeminiProjectTag } from '@/components/life-manager/types';
import { useLifeToast } from '@/hooks/business/lifeFeedback';
import { useRouterPush } from '@/hooks/common/router';
import { fetchAbilityInstanceConfigs, saveAbilityInstanceConfigs } from '../infinity-nikki/service';
import type { AbilityBlockKey, AbilityInstanceConfig, ArchiveRule, OverviewSummaryRule, TimelineWriteRule } from '../infinity-nikki/types';

defineOptions({
  name: 'InfinityNikkiManage'
});

type TabLabel = '功能块实例' | '概览摘要' | '时间轴规则' | '归档规则' | '基础信息' | '自动化集成';
type Tone = 'indigo' | 'orange' | 'rose' | 'amber' | 'emerald' | 'purple' | 'blue' | 'slate' | 'pink';
type ArchiveFlagKey = 'includeTargets' | 'includePhotos' | 'includeNotes' | 'generateTimelineSummary';

interface StatItem {
  label: string;
  value: string;
  icon: string;
  tone: Tone;
}

interface ReminderRule {
  title: string;
  desc: string;
  icon: string;
  tone: Tone;
  enabled: boolean;
}

interface TaskTemplate {
  title: string;
  desc: string;
  cycle: '每日' | '每周';
  reset: string;
  icon: string;
  tone: Tone;
}

interface AssetItem {
  name: string;
  tag?: string;
  status: string;
  avatar: string;
}

interface IntegrationItem {
  name: string;
  icon: string;
  tone: Tone;
  status: string;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();
const { routerPushByKey } = useRouterPush();

const coverUrl = 'https://images.unsplash.com/photo-1614088921132-15949673da0e?auto=format&fit=crop&w=360&q=80';
const activeTab = ref<TabLabel>('功能块实例');
const dailyResetAt = ref('04:00');
const weeklyResetDay = ref('一');
const versionReminder = ref('提前 1 天 09:00');
const syncEnabled = ref(true);
const syncRunning = ref(false);
const modalShow = ref(false);
const modalTitle = ref('新建配置');
const modalDescription = ref('当前为演示配置流程，后续可接入真实表单与接口。');
const abilityConfigs = ref<AbilityInstanceConfig[]>([]);
const selectedAbilityId = ref('');

const tabs = ['功能块实例', '概览摘要', '时间轴规则', '归档规则', '基础信息', '自动化集成'] satisfies TabLabel[];

const projectTags: LifeGeminiProjectTag[] = [
  { label: '游戏项目' },
  { label: '进行中', tone: 'success' }
];

const metaInfo = [
  { label: '创建时间', value: '2025-04-10' },
  { label: '开始时间', value: '2024-12-05' },
  { label: '类型', value: '游戏' },
  { label: '时区', value: 'Asia/Shanghai' },
  { label: '备注', value: '编辑备注', action: true }
];

const reminders = ref<ReminderRule[]>([
  {
    title: '活动结束提醒',
    desc: '活动结束前 3 天、1 天提醒',
    icon: 'material-symbols:event-busy-outline-rounded',
    tone: 'indigo',
    enabled: true
  },
  {
    title: '直播开始提醒',
    desc: '直播开始前 15 分钟提醒',
    icon: 'material-symbols:videocam-outline-rounded',
    tone: 'purple',
    enabled: true
  },
  {
    title: '版本维护提醒',
    desc: '维护开始前 1 小时提醒',
    icon: 'material-symbols:construction-rounded',
    tone: 'blue',
    enabled: true
  },
  {
    title: '体力/资源溢出提醒',
    desc: '体力即将溢出时提醒（自定义阈值）',
    icon: 'material-symbols:battery-charging-60-outline-rounded',
    tone: 'emerald',
    enabled: true
  }
]);

const dailyTasks = ref<TaskTemplate[]>([
  {
    title: '每日基础任务',
    desc: '完成日常活跃、领取奖励等',
    cycle: '每日',
    reset: '每日 04:00',
    icon: 'material-symbols:my-location-outline-rounded',
    tone: 'rose'
  },
  {
    title: '采集与材料收集',
    desc: '采集、材料、鱼类等',
    cycle: '每日',
    reset: '每日 04:00',
    icon: 'material-symbols:psychiatry-outline-rounded',
    tone: 'emerald'
  },
  {
    title: '挑战关卡',
    desc: '主线/支线/秘境挑战等',
    cycle: '每日',
    reset: '每日 04:00',
    icon: 'material-symbols:swords-outline-rounded',
    tone: 'amber'
  },
  {
    title: '周常任务',
    desc: '周常挑战、周活跃等',
    cycle: '每周',
    reset: '每周一 04:00',
    icon: 'material-symbols:calendar-month-outline-rounded',
    tone: 'purple'
  }
]);

const assets = ref<AssetItem[]>([
  {
    name: '主账号（国服）',
    tag: '默认',
    status: '已绑定',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix&backgroundColor=b6e3f4'
  },
  {
    name: '收集号（小号）',
    status: '已绑定',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Aneka&backgroundColor=ffdfbf'
  },
  {
    name: '充值账号（备用）',
    status: '已绑定',
    avatar: 'https://api.dicebear.com/7.x/avataaars/svg?seed=Molly&backgroundColor=c0aede'
  }
]);

const aiAutomationConfigs = computed(() =>
  abilityConfigs.value.filter(config => config.aiRules)
);

const integrations = ref<IntegrationItem[]>([
  { name: '飞书机器人', icon: 'material-symbols:send-outline-rounded', tone: 'blue', status: '已连接' },
  { name: 'OpenClaw', icon: 'material-symbols:smart-toy-outline-rounded', tone: 'slate', status: '已连接' },
  { name: 'Webhook', icon: 'material-symbols:extension-outline-rounded', tone: 'pink', status: '已连接' }
]);

const blockToneMap: Record<AbilityBlockKey, Tone> = {
  overview: 'indigo',
  targets: 'orange',
  version_activity: 'rose',
  materials: 'emerald',
  gallery: 'blue',
  assets: 'amber',
  timeline: 'purple',
  ai: 'slate'
};

const timelineModeLabelMap: Record<TimelineWriteRule['mode'], string> = {
  none: '不写入',
  detail: '详细记录',
  daily_summary: '每日汇总',
  weekly_summary: '每周汇总',
  exception_only: '只记异常'
};

const summaryDisplayModeLabelMap: Record<OverviewSummaryRule['displayMode'], string> = {
  metric: '指标',
  list: '列表',
  compact: '紧凑',
  timeline: '时间轴'
};

const archiveTypeLabelMap: Record<ArchiveRule['type'], string> = {
  manual: '手动归档',
  on_activity_end: '活动结束归档',
  on_version_end: '版本结束归档',
  after_days: '延迟归档'
};

const archiveFlagLabelMap: Record<ArchiveFlagKey, string> = {
  includeTargets: '带上目标记录',
  includePhotos: '带上图册照片',
  includeNotes: '带上笔记攻略',
  generateTimelineSummary: '生成时间轴总结'
};

const timelineModeOptions = [
  { value: 'none', label: '不写入' },
  { value: 'detail', label: '详细记录' },
  { value: 'daily_summary', label: '每日汇总' },
  { value: 'weekly_summary', label: '每周汇总' },
  { value: 'exception_only', label: '只记异常' }
] satisfies Array<{ value: TimelineWriteRule['mode']; label: string }>;

const archiveFlagOptions = [
  { key: 'includeTargets', label: '带上目标记录' },
  { key: 'includePhotos', label: '带上图册照片' },
  { key: 'includeNotes', label: '带上笔记攻略' },
  { key: 'generateTimelineSummary', label: '生成时间轴总结' }
] satisfies Array<{ key: ArchiveFlagKey; label: string }>;

const summarySourceBlockMap: Partial<Record<OverviewSummaryRule['source'], AbilityBlockKey>> = {
  targets: 'targets',
  activities: 'version_activity',
  version: 'version_activity',
  materials: 'materials',
  gallery: 'gallery',
  assets: 'assets',
  timeline: 'timeline',
  ai: 'ai'
};

const enabledAbilityConfigs = computed(() => abilityConfigs.value.filter(config => config.enabled));
const visibleAbilityConfigs = computed(() => abilityConfigs.value.filter(config => config.enabled && config.navigation.visible));
const overviewConfig = computed(() => abilityConfigs.value.find(config => config.blockKey === 'overview'));
const overviewRules = computed<OverviewSummaryRule[]>(() => overviewConfig.value?.summaryRules ?? []);
const allSummaryRules = computed(() => {
  return overviewRules.value
    .map(rule => {
      const sourceBlockKey = summarySourceBlockMap[rule.source];
      const sourceConfig = abilityConfigs.value.find(config => config.blockKey === sourceBlockKey);
      return { config: sourceConfig ?? overviewConfig.value ?? abilityConfigs.value[0], rule };
    })
    .filter((entry): entry is { config: AbilityInstanceConfig; rule: OverviewSummaryRule } => Boolean(entry.config));
});
const timelineAbilityConfigs = computed(() => abilityConfigs.value.filter(config => config.timeline));
const archiveAbilityConfigs = computed(() => abilityConfigs.value.filter(config => config.behavior?.archiveRule));
const selectedAbilityConfig = computed(() => {
  return abilityConfigs.value.find(config => config.id === selectedAbilityId.value) ?? abilityConfigs.value[0];
});

const stats = computed<StatItem[]>(() => [
  { label: '功能块', value: String(enabledAbilityConfigs.value.length || dailyTasks.value.length + 8), icon: 'material-symbols:widgets-outline-rounded', tone: 'orange' },
  { label: '提醒规则', value: String(reminders.value.length + 4), icon: 'material-symbols:timer-outline-rounded', tone: 'rose' },
  { label: '关联资产', value: String(assets.value.length), icon: 'material-symbols:paid-outline-rounded', tone: 'amber' },
  { label: '图册数量', value: '5', icon: 'material-symbols:photo-library-outline-rounded', tone: 'emerald' },
  { label: '概览规则', value: String(overviewRules.value.length || aiAutomationConfigs.value.length), icon: 'material-symbols:auto-awesome-outline-rounded', tone: 'purple' }
]);

const projectHeroStats = computed<LifeGeminiProjectStat[]>(() => [
  ...stats.value.map(item => ({ label: item.label, value: item.value })),
  ...metaInfo.slice(0, 3).map(item => ({ label: item.label, value: item.value }))
]);

function backToProject() {
  routerPushByKey('infinity-nikki');
}

function switchTab(label: TabLabel) {
  activeTab.value = label;
  info('管理分区已切换', label);
}

function selectAbility(config: AbilityInstanceConfig, tab: TabLabel = '功能块实例') {
  selectedAbilityId.value = config.id;
  activeTab.value = tab;
}

function toggleReminder(rule: ReminderRule) {
  rule.enabled = !rule.enabled;
  info(rule.enabled ? '提醒规则已启用' : '提醒规则已停用', rule.title);
}

async function toggleAiConfig(config: AbilityInstanceConfig) {
  if (!config.aiRules) {
    return;
  }

  config.aiRules.readable = !config.aiRules.readable;
  await persistAbilityConfigState(config.aiRules.readable ? 'AI 可读已开启' : 'AI 可读已关闭', config.displayName);
}

async function persistAbilityConfigState(message: string, detail: string) {
  const res = await saveAbilityInstanceConfigs(abilityConfigs.value);
  abilityConfigs.value = res.data;
  success(message, detail);
}

async function toggleAbility(config: AbilityInstanceConfig) {
  config.enabled = !config.enabled;
  await persistAbilityConfigState(config.enabled ? '功能块已启用' : '功能块已停用', config.displayName);
}

async function toggleAbilityNavigation(config: AbilityInstanceConfig) {
  config.navigation.visible = !config.navigation.visible;
  await persistAbilityConfigState(config.navigation.visible ? '已显示在项目导航' : '已从项目导航隐藏', config.displayName);
}

async function toggleAbilityTimeline(config: AbilityInstanceConfig) {
  if (!config.timeline) {
    return;
  }

  config.timeline.enabled = !config.timeline.enabled;
  await persistAbilityConfigState(config.timeline.enabled ? '时间轴写入已开启' : '时间轴写入已关闭', config.displayName);
}

async function toggleSummaryRule(rule: OverviewSummaryRule) {
  rule.enabled = !rule.enabled;
  await persistAbilityConfigState(rule.enabled ? '概览规则已启用' : '概览规则已停用', rule.title);
}

async function updateTimelineMode(config: AbilityInstanceConfig, mode: TimelineWriteRule['mode']) {
  if (!config.timeline) {
    return;
  }

  config.timeline.defaultWriteRule.mode = mode;
  await persistAbilityConfigState('时间轴写入模式已更新', `${config.displayName}：${timelineModeLabelMap[mode]}`);
}

async function toggleTimelineFlag(config: AbilityInstanceConfig, key: 'displayInOverview' | 'aiReadable') {
  if (!config.timeline) {
    return;
  }

  config.timeline.defaultWriteRule[key] = !config.timeline.defaultWriteRule[key];
  await persistAbilityConfigState('时间轴规则已更新', config.displayName);
}

async function toggleArchiveFlag(config: AbilityInstanceConfig, key: ArchiveFlagKey) {
  const archiveRule = config.behavior?.archiveRule;
  if (!archiveRule) {
    return;
  }

  archiveRule[key] = !archiveRule[key];
  await persistAbilityConfigState('归档规则已更新', `${config.displayName}：${archiveFlagLabelMap[key]}`);
}

function openDemoModal(title: string, description = '当前为演示配置流程，后续可接入真实表单与接口。') {
  modalTitle.value = title;
  modalDescription.value = description;
  modalShow.value = true;
}

function runSync() {
  if (!syncEnabled.value) {
    warning('同步未开启', '请先开启自动同步');
    return;
  }

  syncRunning.value = true;
  window.setTimeout(() => {
    syncRunning.value = false;
    success('图册同步完成', '截图、活动图、视频已完成同步');
  }, 600);
}

async function saveConfig() {
  await persistAbilityConfigState('配置已保存', `已保存 ${abilityConfigs.value.length} 个功能块实例和 ${overviewRules.value.length} 条概览规则`);
}

function resetConfig() {
  dailyResetAt.value = '04:00';
  weeklyResetDay.value = '一';
  versionReminder.value = '提前 1 天 09:00';
  syncEnabled.value = true;
  reminders.value.forEach(item => {
    item.enabled = true;
  });
  success('配置已恢复默认值', '无限暖暖项目配置已回到演示初始状态');
}

onMounted(async () => {
  const res = await fetchAbilityInstanceConfigs();
  abilityConfigs.value = res.data;
  selectedAbilityId.value = res.data[0]?.id ?? '';
});
</script>

<template>
  <LifeGeminiShell
    title="项目配置"
    description="以功能块实例为核心，管理导航显示、概览摘要、时间轴写入和归档规则。"
    :breadcrumbs="[
      { label: '工作台', routeKey: 'home' },
      { label: '项目', routeKey: 'projects' },
      { label: '无限暖暖', routeKey: 'infinity-nikki' },
      { label: '管理' }
    ]"
  >
    <LifeToastHost :items="toasts" @close="removeToast" />

    <template #actions>
      <LifeGeminiTopActions
        back-text="返回详情"
        search-label="搜索配置"
        notification-label="查看时间轴规则"
        @search="info('搜索配置', '可继续接入配置项搜索')"
        @notification="switchTab('时间轴规则')"
        @back="backToProject"
        @create="openDemoModal('新建配置')"
      />
    </template>

    <div class="nikki-config-page">
      <LifeGeminiProjectHero
        title="无限暖暖"
        description="记录游戏日常、活动、版本与养成进度，管理账号与相关资源。"
        :cover-src="coverUrl"
        cover-alt="无限暖暖项目封面"
        :tags="projectTags"
        :stats="projectHeroStats"
      >
        <template #actions>
          <button class="hero-action-btn" type="button" @click="success('封面已更新', '已应用当前演示封面')">
            <SvgIcon icon="material-symbols:image-outline-rounded" />
            更换封面
          </button>
          <button class="hero-action-btn" type="button" @click="openDemoModal('编辑备注', '可在这里记录版本、账号或素材管理备注。')">
            <SvgIcon icon="material-symbols:edit-note-outline-rounded" />
            编辑备注
          </button>
        </template>
      </LifeGeminiProjectHero>

      <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

      <section v-if="activeTab === '功能块实例'" class="grid grid-cols-1 gap-5 xl:grid-cols-[minmax(0,1.35fr)_minmax(320px,0.65fr)]">
        <LifeGeminiCard title="功能块实例配置" :action-text="`${visibleAbilityConfigs.length} 个显示在项目导航`">
          <p class="mb-4 text-sm text-slate-500">
            这里决定每个功能块在无限暖暖项目里的名称、启停、导航位置和后续规则入口。页面字段不再直接从传统分项设置开始。
          </p>
          <div class="grid grid-cols-1 gap-3 md:grid-cols-2">
            <div
              v-for="config in abilityConfigs"
              :key="config.id"
              class="rounded-xl border border-slate-100 bg-white p-4 text-left transition-colors hover:bg-slate-50"
              :class="{ 'border-indigo-100 bg-indigo-50/40': selectedAbilityConfig?.id === config.id }"
              role="button"
              tabindex="0"
              @click="selectAbility(config)"
            >
              <div class="flex items-start justify-between gap-3">
                <div class="flex min-w-0 items-start gap-3">
                  <span class="tone-icon" :class="blockToneMap[config.blockKey]">
                    <SvgIcon icon="material-symbols:widgets-outline-rounded" />
                  </span>
                  <span class="min-w-0">
                    <strong class="block truncate text-sm text-slate-800">{{ config.displayName }}</strong>
                    <small class="mt-1 block text-xs text-slate-500">
                      {{ config.navigation.visible ? `导航第 ${config.navigation.order} 位` : '不显示在项目导航' }}
                    </small>
                  </span>
                </div>
                <em class="shrink-0 rounded-full px-2 py-0.5 text-[10px]" :class="config.enabled ? 'bg-emerald-50 text-emerald-600' : 'bg-slate-100 text-slate-400'">
                  {{ config.enabled ? '启用' : '停用' }}
                </em>
              </div>
              <div class="mt-3 flex flex-wrap gap-2">
                <button class="text-action" type="button" @click.stop="toggleAbility(config)">
                  {{ config.enabled ? '停用' : '启用' }}
                </button>
                <button class="text-action" type="button" @click.stop="toggleAbilityNavigation(config)">
                  {{ config.navigation.visible ? '隐藏导航' : '显示导航' }}
                </button>
                <button v-if="config.timeline" class="text-action" type="button" @click.stop="toggleAbilityTimeline(config)">
                  {{ config.timeline.enabled ? '关闭时间轴' : '开启时间轴' }}
                </button>
              </div>
              <div class="mt-3 flex flex-wrap gap-1.5">
                <span v-for="capability in config.capabilities.slice(0, 3)" :key="capability" class="rounded bg-slate-100 px-1.5 py-0.5 text-[10px] text-slate-500">
                  {{ capability }}
                </span>
              </div>
            </div>
          </div>
        </LifeGeminiCard>

        <div class="grid gap-5">
          <LifeGeminiCard :title="selectedAbilityConfig ? `${selectedAbilityConfig.displayName}实例规则` : '功能块规则'">
            <div v-if="selectedAbilityConfig" class="space-y-3 text-xs">
              <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">导航显示</span>
                <span class="font-medium text-slate-700">
                  {{ selectedAbilityConfig.navigation.visible ? `第 ${selectedAbilityConfig.navigation.order} 位` : '不进入导航' }}
                </span>
              </div>
              <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">字段配置</span>
                <span class="font-medium text-slate-700">{{ selectedAbilityConfig.fields?.length ?? 0 }} 个字段</span>
              </div>
              <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">概览规则</span>
                <span class="font-medium text-slate-700">{{ selectedAbilityConfig.summaryRules.length }} 条</span>
              </div>
              <div class="rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">时间轴策略</span>
                <p class="mt-1 font-medium text-slate-700">
                  {{ selectedAbilityConfig.timeline ? timelineModeLabelMap[selectedAbilityConfig.timeline.defaultWriteRule.mode] : '未接入时间轴' }}
                </p>
              </div>
              <div class="rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">行为规则</span>
                <p class="mt-1 font-medium text-slate-700">
                  重置 {{ selectedAbilityConfig.behavior?.resetRules?.length ?? 0 }} 条 · 提醒 {{ selectedAbilityConfig.behavior?.reminderRules?.length ?? 0 }} 条 ·
                  归档 {{ selectedAbilityConfig.behavior?.archiveRule ? archiveTypeLabelMap[selectedAbilityConfig.behavior.archiveRule.type] : '未配置' }}
                </p>
              </div>
              <div class="rounded-xl bg-slate-50 px-3 py-2">
                <span class="text-slate-500">底层能力</span>
                <p class="mt-1 font-medium text-slate-700">{{ selectedAbilityConfig.capabilities.join('、') || '未配置' }}</p>
              </div>
            </div>
          </LifeGeminiCard>

          <LifeGeminiCard title="概览摘要规则">
            <div class="space-y-2">
              <button
                v-for="rule in overviewRules"
                :key="rule.id"
                class="flex w-full items-center justify-between gap-3 rounded-xl bg-slate-50 px-3 py-2 text-left"
                type="button"
                @click="toggleSummaryRule(rule)"
              >
                <span class="min-w-0">
                  <strong class="block truncate text-xs text-slate-700">{{ rule.title }}</strong>
                  <small class="text-[10px] text-slate-400">{{ rule.source }} · 最多 {{ rule.maxItems }} 条 · 优先级 {{ rule.priority }}</small>
                </span>
                <em class="shrink-0 rounded-full px-2 py-0.5 text-[10px]" :class="rule.enabled ? 'bg-indigo-50 text-indigo-600' : 'bg-slate-100 text-slate-400'">
                  {{ rule.enabled ? '显示' : '隐藏' }}
                </em>
              </button>
            </div>
          </LifeGeminiCard>
        </div>
      </section>

      <section v-if="activeTab === '概览摘要'" class="grid grid-cols-1 gap-5 xl:grid-cols-[minmax(0,1.25fr)_minmax(320px,0.75fr)]">
        <LifeGeminiCard title="概览摘要规则" :action-text="`${allSummaryRules.length} 条规则`">
          <p class="mb-4 text-sm text-slate-500">
            概览页只展示摘要。这里控制每条摘要来自哪个功能块、是否显示、最多展示几条，以及点击后进入哪个 tab。
          </p>
          <div class="space-y-3">
            <button
              v-for="{ config, rule } in allSummaryRules"
              :key="`${config.id}-${rule.id}`"
              class="flex w-full items-center justify-between gap-4 rounded-xl border border-slate-100 bg-white px-4 py-3 text-left transition hover:bg-slate-50"
              type="button"
              @click="selectAbility(config, '概览摘要')"
            >
              <span class="min-w-0">
                <strong class="block truncate text-sm text-slate-800">{{ rule.title }}</strong>
                <small class="mt-1 block text-xs text-slate-500">
                  来源 {{ config.displayName }} · {{ summaryDisplayModeLabelMap[rule.displayMode] }} · 最多 {{ rule.maxItems }} 条 · 优先级 {{ rule.priority }}
                </small>
              </span>
              <span class="flex shrink-0 items-center gap-2">
                <em class="rounded-full px-2 py-0.5 text-[10px]" :class="rule.enabled ? 'bg-indigo-50 text-indigo-600' : 'bg-slate-100 text-slate-400'">
                  {{ rule.enabled ? '显示' : '隐藏' }}
                </em>
                <button class="text-action" type="button" @click.stop="toggleSummaryRule(rule)">
                  {{ rule.enabled ? '隐藏' : '显示' }}
                </button>
              </span>
            </button>
          </div>
        </LifeGeminiCard>

        <LifeGeminiCard :title="selectedAbilityConfig ? `${selectedAbilityConfig.displayName}摘要接入` : '摘要接入'">
          <div v-if="selectedAbilityConfig" class="space-y-3">
            <div class="rounded-xl bg-slate-50 px-3 py-2 text-xs">
              <span class="text-slate-500">功能块</span>
              <p class="mt-1 font-medium text-slate-700">{{ selectedAbilityConfig.displayName }}</p>
            </div>
            <div class="rounded-xl bg-slate-50 px-3 py-2 text-xs">
              <span class="text-slate-500">摘要规则</span>
              <p class="mt-1 font-medium text-slate-700">{{ selectedAbilityConfig.summaryRules.length }} 条</p>
            </div>
            <div class="rounded-xl bg-slate-50 px-3 py-2 text-xs">
              <span class="text-slate-500">导航入口</span>
              <p class="mt-1 font-medium text-slate-700">{{ selectedAbilityConfig.navigation.visible ? `显示，第 ${selectedAbilityConfig.navigation.order} 位` : '不显示，仅参与摘要或后台配置' }}</p>
            </div>
          </div>
        </LifeGeminiCard>
      </section>

      <section v-if="activeTab === '时间轴规则'" class="grid grid-cols-1 gap-5 xl:grid-cols-2">
        <LifeGeminiCard v-for="config in timelineAbilityConfigs" :key="config.id" :title="`${config.displayName}时间轴规则`">
          <div v-if="config.timeline" class="space-y-4">
            <div class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2 text-xs">
              <span class="text-slate-500">写入状态</span>
              <button class="text-action" type="button" @click="toggleAbilityTimeline(config)">
                {{ config.timeline.enabled ? '已开启，点击关闭' : '已关闭，点击开启' }}
              </button>
            </div>

            <div>
              <p class="mb-2 text-xs text-slate-500">写入模式</p>
              <div class="flex flex-wrap gap-2">
                <button
                  v-for="mode in timelineModeOptions"
                  :key="mode.value"
                  class="rounded-lg border px-3 py-1.5 text-xs"
                  :class="config.timeline.defaultWriteRule.mode === mode.value ? 'border-indigo-200 bg-indigo-50 text-indigo-600' : 'border-slate-200 bg-white text-slate-500'"
                  type="button"
                  @click="updateTimelineMode(config, mode.value)"
                >
                  {{ mode.label }}
                </button>
              </div>
            </div>

            <div class="grid grid-cols-1 gap-2 sm:grid-cols-2">
              <button class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2 text-xs" type="button" @click="toggleTimelineFlag(config, 'displayInOverview')">
                <span class="text-slate-500">进入概览摘要</span>
                <em class="font-medium text-slate-700">{{ config.timeline.defaultWriteRule.displayInOverview ? '是' : '否' }}</em>
              </button>
              <button class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2 text-xs" type="button" @click="toggleTimelineFlag(config, 'aiReadable')">
                <span class="text-slate-500">允许 AI 读取</span>
                <em class="font-medium text-slate-700">{{ config.timeline.defaultWriteRule.aiReadable ? '是' : '否' }}</em>
              </button>
            </div>
          </div>
        </LifeGeminiCard>
      </section>

      <section v-if="activeTab === '归档规则'" class="grid grid-cols-1 gap-5 xl:grid-cols-2">
        <LifeGeminiCard v-for="config in archiveAbilityConfigs" :key="config.id" :title="`${config.displayName}归档规则`">
          <div v-if="config.behavior?.archiveRule" class="space-y-4">
            <div class="rounded-xl bg-slate-50 px-3 py-2 text-xs">
              <span class="text-slate-500">归档时机</span>
              <p class="mt-1 font-medium text-slate-700">{{ archiveTypeLabelMap[config.behavior.archiveRule.type] }}</p>
            </div>
            <div class="grid grid-cols-1 gap-2 sm:grid-cols-2">
              <button
                v-for="option in archiveFlagOptions"
                :key="option.key"
                class="flex items-center justify-between rounded-xl bg-slate-50 px-3 py-2 text-xs"
                type="button"
                @click="toggleArchiveFlag(config, option.key)"
              >
                <span class="text-slate-500">{{ option.label }}</span>
                <em class="font-medium text-slate-700">{{ config.behavior.archiveRule[option.key] ? '是' : '否' }}</em>
              </button>
            </div>
          </div>
        </LifeGeminiCard>
      </section>

      <section v-if="activeTab === '基础信息'" class="top-grid">
        <LifeGeminiCard class="config-card basic-card" title="基本信息">
          <dl class="info-list">
            <dt>项目名称</dt>
            <dd>无限暖暖</dd>
            <dt>项目类型</dt>
            <dd>游戏项目</dd>
            <dt>状态</dt>
            <dd><span class="status-dot"></span>进行中</dd>
            <dt>简介</dt>
            <dd>记录日常任务、活动提醒、版本更新、抽卡计划、搭配笔记等内容。</dd>
            <dt>封面</dt>
            <dd class="cover-edit">
              <img :src="coverUrl" alt="无限暖暖封面缩略图" />
              <button type="button" @click="success('封面已更新', '已应用当前演示封面')">更换封面</button>
            </dd>
            <dt>标签</dt>
            <dd class="tag-row">
              <span>换装</span>
              <span>日常</span>
              <span>活动</span>
              <span>收集</span>
              <button type="button" @click="openDemoModal('添加标签')">
                <SvgIcon icon="material-symbols:add-rounded" />
                添加
              </button>
            </dd>
          </dl>
        </LifeGeminiCard>

        <LifeGeminiCard class="config-card reset-card" title="游戏重置设置">
          <div class="setting-row">
            <span>每日重置时间</span>
            <button type="button" @click="openDemoModal('设置每日重置时间')">
              {{ dailyResetAt }}
              <SvgIcon icon="material-symbols:schedule-outline-rounded" />
            </button>
          </div>

          <div class="weekly-reset">
            <div>
              <span>每周重置日</span>
              <strong>周{{ weeklyResetDay }}</strong>
            </div>
            <div class="week-axis">
              <button
                v-for="day in ['一', '二', '三', '四', '五', '六', '日']"
                :key="day"
                :class="{ active: weeklyResetDay === day }"
                type="button"
                @click="weeklyResetDay = day; info('每周重置日已切换', `周${day}`)"
              >
                <i v-if="weeklyResetDay === day">{{ dailyResetAt }}</i>
                <span></span>
                {{ day }}
              </button>
            </div>
          </div>

          <div class="setting-row version-row">
            <span>版本更新时间提醒</span>
            <button type="button" @click="openDemoModal('设置版本提醒时间')">
              {{ versionReminder }}
              <SvgIcon icon="material-symbols:schedule-outline-rounded" />
            </button>
          </div>
          <p class="hint-line"><SvgIcon icon="material-symbols:notifications-active-outline-rounded" />将在版本维护或更新前提醒你</p>
        </LifeGeminiCard>

        <LifeGeminiCard class="config-card reminder-card">
          <template #title>
            提醒规则 <span>（用于生成倒计时与通知）</span>
          </template>
          <div class="reminder-list">
            <button
              v-for="item in reminders"
              :key="item.title"
              class="reminder-item"
              :class="{ disabled: !item.enabled }"
              type="button"
              @click="toggleReminder(item)"
            >
              <span class="tone-icon" :class="item.tone">
                <SvgIcon :icon="item.icon" />
              </span>
              <span>
                <strong>{{ item.title }}</strong>
                <small>{{ item.desc }}</small>
              </span>
              <em>{{ item.enabled ? '启用中' : '已停用' }}</em>
              <SvgIcon icon="material-symbols:more-horiz-rounded" />
            </button>
          </div>
          <button class="dash-button" type="button" @click="openDemoModal('新建提醒规则')">
            <SvgIcon icon="material-symbols:add-rounded" />
            新建提醒规则
          </button>
        </LifeGeminiCard>
      </section>

      <section v-if="activeTab === '自动化集成'" class="module-grid">
        <LifeGeminiCard class="config-card compact-card" title="日常任务模板">
          <div class="mini-head"><span>类型</span><span>重置</span></div>
          <div class="task-list">
            <div v-for="item in dailyTasks" :key="item.title" class="task-item">
              <span class="tone-icon" :class="item.tone">
                <SvgIcon :icon="item.icon" />
              </span>
              <span>
                <strong>{{ item.title }}</strong>
                <small>{{ item.desc }}</small>
              </span>
              <em :class="{ weekly: item.cycle === '每周' }">{{ item.cycle }}</em>
              <time>{{ item.reset }}</time>
            </div>
          </div>
          <button class="text-action" type="button" @click="openDemoModal('添加任务模板')">
            <SvgIcon icon="material-symbols:add-rounded" />
            添加模板
          </button>
        </LifeGeminiCard>

        <LifeGeminiCard class="config-card compact-card">
          <template #title>
            关联资产 <span>（用于账号与资源管理）</span>
          </template>
          <div class="asset-list">
            <button v-for="item in assets" :key="item.name" type="button" @click="openDemoModal('编辑关联资产', item.name)">
              <img :src="item.avatar" :alt="item.name" />
              <span>
                <strong>{{ item.name }}</strong>
                <i v-if="item.tag">{{ item.tag }}</i>
              </span>
              <em>{{ item.status }}</em>
              <SvgIcon icon="material-symbols:lock-outline-rounded" />
            </button>
          </div>
          <button class="text-action" type="button" @click="openDemoModal('添加账号/资产')">
            <SvgIcon icon="material-symbols:add-rounded" />
            添加账号/资产
          </button>
        </LifeGeminiCard>

        <LifeGeminiCard class="config-card compact-card oss-card" title="图册与 OSS 同步">
          <div class="oss-provider">
            <span class="tone-icon orange"><SvgIcon icon="material-symbols:cloud-upload-outline-rounded" /></span>
            <strong>阿里云 OSS</strong>
            <em>{{ syncEnabled ? '已连接' : '已停用' }}</em>
          </div>
          <dl>
            <dt>同步状态</dt>
            <dd><span class="status-dot"></span>{{ syncEnabled ? '正常' : '暂停' }}</dd>
            <dt>最近同步</dt>
            <dd>2025-05-20 10:30</dd>
            <dt>自动同步</dt>
            <dd>{{ syncEnabled ? '已开启（每日 02:30）' : '已关闭' }}</dd>
            <dt>同步内容</dt>
            <dd>截图、活动图、视频</dd>
          </dl>
          <div class="split-actions">
            <button type="button" @click="runSync">{{ syncRunning ? '同步中...' : '立即同步' }}</button>
            <button type="button" @click="openDemoModal('设置同步规则')">设置同步规则</button>
          </div>
        </LifeGeminiCard>

        <LifeGeminiCard class="config-card compact-card" title="AI 自动化设置">
          <div class="ai-list">
            <div v-for="config in aiAutomationConfigs" :key="config.id" class="ai-item">
              <span>
                <strong>{{ config.displayName }}</strong>
                <small>{{ config.aiRules?.allowedUse?.join('、') ?? '未配置' }}</small>
              </span>
              <button class="toggle-switch" :class="{ active: config.aiRules?.readable }" type="button" :aria-label="`切换${config.displayName} AI`" @click="toggleAiConfig(config)">
                <i></i>
              </button>
            </div>
          </div>
          <button class="text-action" type="button" @click="openDemoModal('AI 任务历史', '这里可查看自动总结、复盘和版本解读记录。')">
            查看 AI 任务历史
          </button>
        </LifeGeminiCard>
      </section>

      <section v-if="activeTab === '自动化集成'" class="integration-bar">
        <div class="integration-title">
          <strong>集成与通知</strong>
          <span>将提醒与信息推送到你常用的平台</span>
        </div>
        <button v-for="item in integrations" :key="item.name" type="button" @click="openDemoModal(`${item.name} 设置`)">
          <span class="tone-icon" :class="item.tone"><SvgIcon :icon="item.icon" /></span>
          {{ item.name }}
          <em>{{ item.status }}</em>
        </button>
        <button class="add-integration" type="button" @click="openDemoModal('添加集成')">
          <SvgIcon icon="material-symbols:add-rounded" />
          添加集成
        </button>
        <div class="footer-actions">
          <button type="button" @click="resetConfig">取消</button>
          <button type="button" @click="saveConfig">保存配置</button>
        </div>
      </section>
    </div>

    <LifeModal
      v-model:show="modalShow"
      :title="modalTitle"
      :description="modalDescription"
      confirm-text="确定"
      cancel-text="取消"
      @confirm="success('操作已记录', modalTitle)"
    >
      <div class="demo-modal-content">
        <SvgIcon icon="material-symbols:settings-outline-rounded" />
        <p>这里保留为页面演示交互，后续接入真实配置表单和后端接口。</p>
      </div>
    </LifeModal>
  </LifeGeminiShell>
</template>

<style scoped>
.nikki-config-page {
  --primary: #6366f1;
  --surface: #f8fafc;
  --border: #e8ecf4;
  --muted: #64748b;
  color: #1e293b;
}

.nikki-config-page button {
  border: 0;
  font: inherit;
  cursor: pointer;
}

.integration-bar {
  border: 1px solid #f1f5f9;
  border-radius: 24px;
  background: #fff;
  box-shadow: 0 2px 10px -4px rgba(0, 0, 0, 0.05);
}

.cover-edit img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.tag-row span {
  border: 1px solid #dfe4ff;
  background: #eef2ff;
  color: var(--primary);
}

.status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #10b981;
}

.mini-head,
.task-item small,
.ai-item small,
.integration-title span,
.reminder-item small {
  color: var(--muted);
  font-size: 12px;
}

.tone-icon {
  display: inline-grid;
  width: 36px;
  height: 36px;
  flex: 0 0 36px;
  place-items: center;
  border-radius: 8px;
}

.tone-icon .svg-icon {
  font-size: 20px;
}

.tone-icon.indigo {
  background: #eef2ff;
  color: #6366f1;
}

.tone-icon.orange {
  background: #fff7ed;
  color: #f97316;
}

.tone-icon.rose {
  background: #fff1f2;
  color: #f43f5e;
}

.tone-icon.amber {
  background: #fffbeb;
  color: #d97706;
}

.tone-icon.emerald {
  background: #ecfdf5;
  color: #059669;
}

.tone-icon.purple {
  background: #faf5ff;
  color: #9333ea;
}

.tone-icon.blue {
  background: #eff6ff;
  color: #2563eb;
}

.tone-icon.slate {
  background: #f1f5f9;
  color: #334155;
}

.tone-icon.pink {
  background: #fdf2f8;
  color: #db2777;
}

.top-grid {
  display: grid;
  grid-template-columns: minmax(280px, 1fr) minmax(340px, 1.12fr) minmax(380px, 1.2fr);
  gap: 20px;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.config-card {
  min-width: 0;
}

.config-card :deep(h3 span) {
  color: #94a3b8;
  font-size: 12px;
  font-weight: 500;
}

.hero-action-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  min-height: 40px;
  padding: 0 14px;
  border: 1px solid #e2e8f0 !important;
  border-radius: 12px;
  background: #fff;
  color: #475569;
  font-size: 14px;
  font-weight: 650;
}

.info-list,
.oss-card dl {
  display: grid;
  grid-template-columns: 70px minmax(0, 1fr);
  gap: 16px 12px;
  margin: 0;
  color: #334155;
  font-size: 14px;
}

.info-list dt,
.oss-card dt {
  color: #94a3b8;
}

.info-list dd,
.oss-card dd {
  min-width: 0;
  margin: 0;
}

.info-list dd:nth-of-type(3),
.oss-card dd:first-of-type {
  display: inline-flex;
  align-items: center;
  gap: 7px;
  color: #059669;
}

.cover-edit {
  display: flex;
  align-items: center;
  gap: 12px;
}

.cover-edit img {
  width: 48px;
  height: 48px;
  border-radius: 8px;
}

.cover-edit button,
.tag-row button {
  display: inline-flex;
  align-items: center;
  gap: 5px;
  min-height: 30px;
  padding: 0 11px;
  border: 1px solid #e2e8f0;
  border-radius: 6px;
  background: #f8fafc;
  color: #475569;
  font-size: 12px;
  font-weight: 650;
}

.tag-row {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.tag-row span {
  display: inline-flex;
  align-items: center;
  min-height: 28px;
  padding: 0 10px;
  border-radius: 4px;
  font-size: 12px;
}

.tag-row button {
  border-style: dashed;
  color: var(--muted);
}

.setting-row,
.weekly-reset > div:first-child {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 14px;
}

.setting-row span,
.weekly-reset span {
  color: #475569;
  font-size: 14px;
}

.setting-row button {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  min-height: 36px;
  padding: 0 12px;
  border: 1px solid #edf2f7;
  border-radius: 8px;
  background: #f8fafc;
  color: #1e293b;
  font-size: 14px;
  font-weight: 700;
}

.weekly-reset {
  margin: 24px 0;
}

.weekly-reset strong {
  color: #1e293b;
}

.week-axis {
  position: relative;
  display: grid;
  grid-template-columns: repeat(7, minmax(0, 1fr));
  gap: 4px;
  margin-top: 18px;
  padding: 10px 8px 0;
}

.week-axis::before {
  content: '';
  position: absolute;
  top: 20px;
  right: 20px;
  left: 20px;
  height: 4px;
  border-radius: 999px;
  background: #f1f5f9;
}

.week-axis button {
  position: relative;
  z-index: 1;
  display: grid;
  justify-items: center;
  gap: 8px;
  min-width: 0;
  background: transparent;
  color: #94a3b8;
  font-size: 12px;
}

.week-axis button span {
  width: 10px;
  height: 10px;
  margin-top: 6px;
  border: 4px solid #fff;
  border-radius: 50%;
  background: #cbd5e1;
  box-sizing: content-box;
}

.week-axis button.active {
  color: var(--primary);
  font-weight: 800;
}

.week-axis button.active span {
  width: 18px;
  height: 18px;
  margin-top: 2px;
  border-width: 4px;
  background: var(--primary);
}

.week-axis i {
  position: absolute;
  top: -18px;
  padding: 2px 8px;
  border-radius: 4px;
  background: #e0e7ff;
  color: var(--primary);
  font-size: 11px;
  font-style: normal;
  white-space: nowrap;
}

.version-row {
  padding-top: 16px;
  border-top: 1px solid #f1f5f9;
}

.hint-line {
  display: flex;
  align-items: center;
  gap: 7px;
  margin: 12px 0 0;
  padding: 10px;
  border-radius: 8px;
  background: #eef2ff;
  color: var(--primary);
  font-size: 12px;
}

.reminder-list,
.task-list,
.asset-list,
.ai-list {
  display: grid;
  gap: 10px;
}

.reminder-list {
  max-height: 246px;
  overflow: auto;
  padding-right: 2px;
}

.reminder-item,
.asset-list button {
  display: grid;
  width: 100%;
  align-items: center;
  border: 1px solid #edf2f7;
  border-radius: 8px;
  background: #fff;
  color: #334155;
  text-align: left;
  transition:
    border-color 0.18s ease,
    box-shadow 0.18s ease;
}

.reminder-item {
  grid-template-columns: 36px minmax(0, 1fr) auto 24px;
  gap: 12px;
  padding: 12px;
}

.reminder-item:hover,
.asset-list button:hover {
  border-color: #dbe2ee;
  box-shadow: 0 8px 18px rgba(15, 23, 42, 0.05);
}

.reminder-item.disabled {
  opacity: 0.58;
}

.reminder-item strong,
.task-item strong,
.asset-list strong,
.ai-item strong {
  display: block;
  overflow: hidden;
  color: #1e293b;
  font-size: 14px;
  font-weight: 750;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.reminder-item small,
.task-item small,
.ai-item small {
  display: block;
  overflow: hidden;
  margin-top: 4px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.reminder-item em {
  color: #10b981;
  font-size: 12px;
  font-style: normal;
  font-weight: 700;
  white-space: nowrap;
}

.dash-button {
  display: inline-flex;
  width: 100%;
  align-items: center;
  justify-content: center;
  gap: 6px;
  min-height: 42px;
  margin-top: 14px;
  border: 1px dashed #cbd5e1 !important;
  border-radius: 8px;
  background: #fff;
  color: var(--muted);
  font-size: 14px;
  font-weight: 700;
}

.compact-card {
  display: flex;
  min-height: 320px;
  flex-direction: column;
}

.mini-head,
.task-item {
  display: grid;
  grid-template-columns: 36px minmax(0, 1fr) 42px 80px;
  gap: 10px;
  align-items: center;
}

.mini-head {
  grid-template-columns: minmax(0, 1fr) 124px;
  margin-bottom: 10px;
}

.mini-head span:last-child {
  text-align: right;
}

.task-list,
.asset-list,
.ai-list {
  flex: 1;
  overflow: auto;
}

.task-item {
  min-height: 48px;
}

.task-item em {
  justify-self: end;
  padding: 3px 8px;
  border: 1px solid #bfdbfe;
  border-radius: 4px;
  background: #eff6ff;
  color: #2563eb;
  font-size: 11px;
  font-style: normal;
  font-weight: 700;
}

.task-item em.weekly {
  border-color: #fbcfe8;
  background: #fdf2f8;
  color: #db2777;
}

.task-item time {
  color: #94a3b8;
  font-size: 11px;
  text-align: right;
}

.asset-list button {
  grid-template-columns: 34px minmax(0, 1fr) auto 16px;
  gap: 10px;
  min-height: 54px;
  padding: 10px;
  background: #f8fafc;
}

.asset-list img {
  width: 34px;
  height: 34px;
  border: 1px solid #e2e8f0;
  border-radius: 50%;
  background: #fff;
}

.asset-list span {
  display: flex;
  min-width: 0;
  align-items: center;
  gap: 6px;
}

.asset-list i {
  padding: 2px 6px;
  border: 1px solid #dfe4ff;
  border-radius: 4px;
  background: #eef2ff;
  color: var(--primary);
  font-size: 10px;
  font-style: normal;
  white-space: nowrap;
}

.asset-list em {
  color: #64748b;
  font-size: 12px;
  font-style: normal;
  white-space: nowrap;
}

.text-action {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  width: fit-content;
  margin-top: 14px;
  background: transparent;
  color: var(--primary);
  font-size: 14px;
  font-weight: 750;
}

.oss-provider {
  display: grid;
  grid-template-columns: 36px minmax(0, 1fr) auto;
  align-items: center;
  gap: 10px;
  margin-bottom: 18px;
  padding: 12px;
  border: 1px solid #edf2f7;
  border-radius: 8px;
  background: #f8fafc;
}

.oss-provider strong {
  color: #1e293b;
  font-size: 14px;
}

.oss-provider em {
  color: #10b981;
  font-size: 12px;
  font-style: normal;
  font-weight: 700;
}

.oss-card dl {
  gap: 12px;
  font-size: 13px;
}

.oss-card dd {
  text-align: right;
}

.split-actions {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 10px;
  margin-top: 18px;
}

.split-actions button {
  min-height: 38px;
  border-radius: 8px;
  background: var(--primary);
  color: #fff;
  font-size: 13px;
  font-weight: 750;
}

.split-actions button:last-child {
  border: 1px solid #e2e8f0;
  background: #fff;
  color: #334155;
}

.ai-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
}

.toggle-switch {
  position: relative;
  display: inline-flex;
  width: 38px;
  height: 22px;
  flex: 0 0 38px;
  align-items: center;
  padding: 2px;
  border-radius: 999px;
  background: #cbd5e1;
  transition: background 0.18s ease;
}

.toggle-switch.active {
  background: var(--primary);
}

.toggle-switch i {
  width: 18px;
  height: 18px;
  transform: translateX(0);
  border-radius: 50%;
  background: #fff;
  box-shadow: 0 2px 6px rgba(15, 23, 42, 0.18);
  transition: transform 0.18s ease;
}

.toggle-switch.active i {
  transform: translateX(16px);
}

.integration-bar {
  display: flex;
  flex-wrap: wrap;
  align-items: center;
  gap: 12px;
  margin-top: 20px;
  padding: 16px;
}

.integration-title {
  display: grid;
  gap: 4px;
  min-width: 160px;
  margin-right: 6px;
}

.integration-title strong {
  color: #1e293b;
  font-size: 14px;
}

.integration-bar > button {
  display: inline-flex;
  align-items: center;
  gap: 8px;
  min-height: 38px;
  padding: 0 12px;
  border: 1px solid #edf2f7;
  border-radius: 8px;
  background: #f8fafc;
  color: #334155;
  font-size: 13px;
  font-weight: 700;
}

.integration-bar .tone-icon {
  width: 26px;
  height: 26px;
  flex-basis: 26px;
  border-radius: 6px;
}

.integration-bar .tone-icon .svg-icon {
  font-size: 16px;
}

.integration-bar em {
  color: #10b981;
  font-size: 12px;
  font-style: normal;
  font-weight: 700;
}

.add-integration {
  border-style: dashed !important;
  color: var(--muted) !important;
}

.footer-actions {
  display: flex;
  gap: 10px;
  margin-left: auto;
}

.footer-actions button {
  min-width: 92px;
  min-height: 40px;
  border-radius: 8px;
  background: #fff;
  color: #475569;
  font-weight: 750;
}

.footer-actions button:first-child {
  border: 1px solid #e2e8f0;
}

.footer-actions button:last-child {
  background: var(--primary);
  color: #fff;
  box-shadow: 0 10px 20px rgba(99, 102, 241, 0.18);
}

.demo-modal-content {
  display: flex;
  gap: 12px;
  align-items: flex-start;
  color: #475569;
}

.demo-modal-content .svg-icon {
  flex: 0 0 auto;
  color: var(--primary);
  font-size: 24px;
}

.demo-modal-content p {
  margin: 0;
  font-size: 13px;
  line-height: 1.7;
}

@media (max-width: 1360px) {
  .top-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .reminder-card {
    grid-column: 1 / -1;
  }

  .module-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 860px) {
  .top-grid,
  .module-grid {
    grid-template-columns: 1fr;
  }

  .integration-bar,
  .footer-actions {
    align-items: stretch;
  }

  .footer-actions {
    width: 100%;
    margin-left: 0;
  }

  .footer-actions button {
    flex: 1;
  }
}
</style>

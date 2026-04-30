<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useRouterPush } from '@/hooks/common/router';
import { useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'InfinityNikki'
});

type TabLabel = '概览' | '日常' | '倒计时' | '资产' | '笔记' | '图册' | 'AI';
type TaskGroup = '今日' | '周常' | '活动';
type GoalState = '推进中' | '需关注' | '已完成';
type DetailKind = 'task' | 'countdown' | 'asset' | 'note' | 'gallery' | 'ai';

interface TabItem {
  label: TabLabel;
  icon: string;
}

interface DailyTask {
  title: string;
  group: TaskGroup;
  time: string;
  done: boolean;
}

interface WeekTask {
  title: string;
  current: number;
  target: number;
  state: GoalState;
}

interface CountdownItem {
  title: string;
  end: string;
  remain: string;
  remind: boolean;
  checked: boolean;
}

interface DetailState {
  show: boolean;
  title: string;
  description: string;
  kind: DetailKind;
  meta: string[];
}

const { toasts, removeToast, success, info, warning } = useLifeToast();
const { routerPushByKey } = useRouterPush();

function openManagePage() {
  routerPushByKey('infinity-nikki-manage');
}

const tabs = [
  { label: '概览', icon: 'material-symbols:calendar-month-outline-rounded' },
  { label: '日常', icon: 'material-symbols:event-available-outline-rounded' },
  { label: '倒计时', icon: 'material-symbols:timer-outline-rounded' },
  { label: '资产', icon: 'material-symbols:inventory-2-outline-rounded' },
  { label: '笔记', icon: 'material-symbols:edit-note-outline-rounded' },
  { label: '图册', icon: 'material-symbols:photo-library-outline-rounded' },
  { label: 'AI', icon: 'material-symbols:auto-awesome-rounded' }
] satisfies TabItem[];

const taskGroups = ['今日', '周常', '活动'] as const;

const activeTab = ref<TabLabel>('概览');
const activeTaskGroup = ref<TaskGroup>('今日');
const selectedGoalIndex = ref(0);
const taskListRef = ref<HTMLElement | null>(null);
const activityListRef = ref<HTMLElement | null>(null);
const galleryListRef = ref<HTMLElement | null>(null);

const dailyTasks = ref<DailyTask[]>([
  { title: '每日签到', group: '今日', time: '10:00 重置', done: true },
  { title: '心愿原野探索', group: '今日', time: '采集 15 分钟', done: true },
  { title: '搭配竞技场挑战', group: '今日', time: '剩余 2 次', done: true },
  { title: '每日灵感任务', group: '今日', time: '推荐优先', done: false },
  { title: '采集素材', group: '今日', time: '花愿镇路线', done: false },
  { title: '周常梦境挑战', group: '周常', time: '本周剩余 4 次', done: false },
  { title: '完成搭配评选复盘', group: '周常', time: '周日 22:00 前', done: true },
  { title: '奇迹之旅章节奖励', group: '活动', time: '3 天后结束', done: false },
  { title: '繁花季素材兑换', group: '活动', time: '每日 05:00 刷新', done: false }
]);

const weekTasks = ref<WeekTask[]>([
  { title: '完成搭配评选 3次', current: 3, target: 3, state: '已完成' },
  { title: '通关心愿梦境 10次', current: 6, target: 10, state: '推进中' },
  { title: '收集奇想星 80个', current: 52, target: 80, state: '推进中' },
  { title: '完成灵感任务 14次', current: 14, target: 14, state: '已完成' },
  { title: '参与搭配赛 2次', current: 1, target: 2, state: '需关注' },
  { title: '提升搭配师等级', current: 38, target: 50, state: '推进中' },
  { title: '收集套装部件 30个', current: 22, target: 30, state: '推进中' }
]);

const countdowns = ref<CountdownItem[]>([
  { title: '奇迹之旅 · 第二章', end: '2026-05-02 06:00 结束', remain: '3 天', remind: true, checked: false },
  { title: '繁花季节 限时活动', end: '2026-05-12 05:00 结束', remain: '13 天', remind: false, checked: false },
  { title: '累计充值返利', end: '2026-05-20 05:00 结束', remain: '21 天', remind: false, checked: true }
]);

const versionEvents = ref([
  { title: '5.2版本前瞻特别节目', start: '04-30 19:30', distance: '2 天后开始', remind: false, checked: false },
  { title: '5.2版本更新维护', start: '05-02 06:00 - 12:00', distance: '3 天后开始', remind: true, checked: false }
]);

const detail = ref<DetailState>({
  show: false,
  title: '',
  description: '',
  kind: 'task',
  meta: []
});

const visibleDailyTasks = computed(() => dailyTasks.value.filter(item => item.group === activeTaskGroup.value));
const dailyDoneCount = computed(() => dailyTasks.value.filter(item => item.done).length);
const dailyTotal = computed(() => dailyTasks.value.length);
const dailyProgress = computed(() => Math.round((dailyDoneCount.value / dailyTotal.value) * 100));
const weekDoneCount = computed(() => weekTasks.value.filter(item => item.state === '已完成').length);
const selectedGoal = computed(() => weekTasks.value[selectedGoalIndex.value]);
const selectedGoalProgress = computed(() => {
  const goal = selectedGoal.value;
  return Math.min(100, Math.round((goal.current / goal.target) * 100));
});
const remindedCount = computed(
  () => countdowns.value.filter(item => item.remind).length + versionEvents.value.filter(item => item.remind).length
);
const checkedCount = computed(
  () => countdowns.value.filter(item => item.checked).length + versionEvents.value.filter(item => item.checked).length
);

function switchTab(label: TabLabel) {
  activeTab.value = label;
  info('项目视图已切换', label);
}

function setTaskGroup(group: TaskGroup) {
  activeTaskGroup.value = group;
  info('任务分组已切换', `${group}任务`);
}

function toggleDailyTask(task: DailyTask) {
  task.done = !task.done;
  success(task.done ? '打卡完成' : '已恢复待打卡', task.title);
}

function selectGoal(index: number) {
  selectedGoalIndex.value = index;
  info('目标已选中', weekTasks.value[index].title);
}

function cycleGoalState(task: WeekTask) {
  const nextMap: Record<GoalState, GoalState> = {
    推进中: '需关注',
    需关注: '已完成',
    已完成: '推进中'
  };

  task.state = nextMap[task.state];
  if (task.state === '已完成') task.current = task.target;
  if (task.state === '推进中' && task.current === task.target) task.current = Math.max(task.target - 1, 0);

  info('目标状态已更新', `${task.title} · ${task.state}`);
}

function adjustGoalProgress(delta: number) {
  const goal = selectedGoal.value;
  const next = Math.min(goal.target, Math.max(0, goal.current + delta));

  if (next === goal.current) {
    warning(delta > 0 ? '目标已达上限' : '目标已归零', goal.title);
    return;
  }

  goal.current = next;
  goal.state = goal.current === goal.target ? '已完成' : '推进中';
  success(delta > 0 ? '进度已增加' : '进度已回退', `${goal.title} ${goal.current}/${goal.target}`);
}

function toggleCountdownReminder(item: CountdownItem) {
  item.remind = !item.remind;
  info(item.remind ? '已设置活动提醒' : '已取消活动提醒', item.title);
}

function toggleCountdownCheck(item: CountdownItem) {
  item.checked = !item.checked;
  success(item.checked ? '活动已打卡' : '活动打卡已撤回', item.title);
}

function toggleVersionReminder(item: (typeof versionEvents.value)[number]) {
  item.remind = !item.remind;
  info(item.remind ? '已设置版本提醒' : '已取消版本提醒', item.title);
}

function toggleVersionCheck(item: (typeof versionEvents.value)[number]) {
  item.checked = !item.checked;
  success(item.checked ? '版本节点已打卡' : '版本节点已恢复', item.title);
}

function openDetail(kind: DetailKind, title: string, description: string, meta: string[] = []) {
  detail.value = {
    show: true,
    title,
    description,
    kind,
    meta
  };
}

function confirmDetail() {
  detail.value.show = false;
  success('详情已确认', detail.value.title);
}

function scrollList(target: 'task' | 'activity' | 'gallery', direction: 'left' | 'right' | 'up' | 'down') {
  const targetMap = {
    task: taskListRef.value,
    activity: activityListRef.value,
    gallery: galleryListRef.value
  };
  const el = targetMap[target];

  el?.scrollBy({
    top: direction === 'up' ? -120 : direction === 'down' ? 120 : 0,
    left: direction === 'left' ? -180 : direction === 'right' ? 180 : 0,
    behavior: 'smooth'
  });

  info('页面滑动已触发', direction === 'left' || direction === 'right' ? '横向查看内容' : '纵向查看内容');
}
</script>

<template>
  <LifeAppShell active="无限暖暖" avatar="nikki">
    <LifeToastHost :items="toasts" @close="removeToast" />

    <header class="nikki-head lm-card">
      <div class="nikki-hero-img lm-hero-art nikki"></div>
      <div class="lm-detail-title">
        <h1>无限暖暖 <span>★</span></h1>
        <p><span class="lm-tag pink">游戏</span> <span class="lm-tag green">进行中</span></p>
        <p>收集美好的瞬间，搭配无限的可能</p>
        <div class="nikki-meta">
          <span>创建时间 <b>2024-12-15</b></span>
          <span>任务进度 <b>{{ dailyDoneCount }}/{{ dailyTotal }}</b></span>
          <span>本周完成 <b>{{ weekDoneCount }}/{{ weekTasks.length }}</b></span>
          <span>项目笔记 <b>12</b></span>
          <span>图册数量 <b>8</b></span>
        </div>
      </div>
      <div class="lm-actions">
        <button
          class="lm-purple-btn"
          type="button"
          @click="openDetail('task', '快速记录', '记录今天的无限暖暖项目进展，可继续接入真实表单。', ['默认归类：今日', `当前打卡：${dailyDoneCount}/${dailyTotal}`])"
        >
          <SvgIcon icon="material-symbols:add-rounded" />快速记录
        </button>
        <button class="lm-plain-btn" type="button" @click="success('分享链接已生成', '无限暖暖项目概览')">
          <SvgIcon icon="material-symbols:ios-share-rounded" />分享
        </button>
        <button class="lm-plain-btn" type="button" @click="openManagePage">
          <SvgIcon icon="material-symbols:settings-outline-rounded" />设置
        </button>
        <button class="lm-icon-btn" type="button" aria-label="更多操作" @click="scrollList('activity', 'down')">
          <SvgIcon icon="material-symbols:more-horiz-rounded" />
        </button>
      </div>
    </header>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item.label" :class="{ active: item.label === activeTab }" type="button" @click="switchTab(item.label)">
        <SvgIcon :icon="item.icon" />{{ item.label }}
      </button>
    </nav>

    <section class="lm-grid nikki-grid">
      <article class="lm-card daily-ring">
        <div class="lm-card-title">
          <h2>今日游戏日常</h2>
          <button type="button" @click="scrollList('task', 'down')">查看全部</button>
        </div>
        <div class="nikki-segment">
          <button v-for="item in taskGroups" :key="item" :class="{ active: item === activeTaskGroup }" type="button" @click="setTaskGroup(item)">
            {{ item }}
          </button>
        </div>
        <div class="daily-content">
          <div class="daily-circle" :style="{ '--daily-progress': `${dailyProgress}%` }">
            <strong>{{ dailyDoneCount }}/{{ dailyTotal }}</strong><span>已完成</span>
          </div>
          <ul ref="taskListRef">
            <li v-for="item in visibleDailyTasks" :key="item.title" :class="{ done: item.done }">
              <button type="button" @click="toggleDailyTask(item)">
                <span>{{ item.title }}</span>
                <em>{{ item.time }}</em>
              </button>
            </li>
          </ul>
        </div>
        <div class="nikki-scroll-actions">
          <button type="button" aria-label="向上查看任务" @click="scrollList('task', 'up')">
            <SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" />
          </button>
          <button type="button" aria-label="向下查看任务" @click="scrollList('task', 'down')">
            <SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" />
          </button>
        </div>
      </article>

      <article class="lm-card week-card">
        <div class="lm-card-title">
          <h2>本周任务</h2>
          <button type="button" @click="adjustGoalProgress(1)">推进目标</button>
        </div>
        <div
          v-for="(item, index) in weekTasks"
          :key="item.title"
          class="week-row"
          :class="{ selected: index === selectedGoalIndex, done: item.state === '已完成', warning: item.state === '需关注' }"
        >
          <button type="button" @click="selectGoal(index)">{{ item.title }}</button>
          <div class="lm-progress-line"><i :style="{ width: `${Math.min(100, Math.round((item.current / item.target) * 100))}%` }"></i></div>
          <b>{{ item.current }}/{{ item.target }}</b>
          <button type="button" @click="cycleGoalState(item)">{{ item.state }}</button>
        </div>
        <div class="goal-panel">
          <div>
            <span>当前目标</span>
            <strong>{{ selectedGoal.title }}</strong>
            <p>{{ selectedGoal.current }}/{{ selectedGoal.target }} · {{ selectedGoalProgress }}%</p>
          </div>
          <div class="goal-actions">
            <button type="button" @click="adjustGoalProgress(-1)"><SvgIcon icon="material-symbols:remove-rounded" /></button>
            <button type="button" @click="adjustGoalProgress(1)"><SvgIcon icon="material-symbols:add-rounded" /></button>
          </div>
        </div>
      </article>

      <article class="lm-card countdown-card">
        <div class="lm-card-title">
          <h2>活动倒计时</h2>
          <button type="button" @click="info('提醒统计', `已设置 ${remindedCount} 个提醒，已打卡 ${checkedCount} 个节点`)">
            {{ remindedCount }} 个提醒
          </button>
        </div>
        <div v-for="item in countdowns" :key="item.title" class="countdown-row" :class="{ checked: item.checked }">
          <button
            class="mini-scene lm-cover nikki"
            type="button"
            aria-label="查看活动详情"
            @click="openDetail('countdown', item.title, '活动节点、奖励与提醒状态演示。', [item.end, item.remind ? '已设置提醒' : '未设置提醒'])"
          ></button>
          <div>
            <strong>{{ item.title }}</strong>
            <p>{{ item.end }}</p>
            <div class="row-actions">
              <button type="button" :class="{ active: item.remind }" @click="toggleCountdownReminder(item)">
                <SvgIcon :icon="item.remind ? 'material-symbols:notifications-active-rounded' : 'material-symbols:notifications-outline-rounded'" />
                {{ item.remind ? '已提醒' : '提醒' }}
              </button>
              <button type="button" :class="{ active: item.checked }" @click="toggleCountdownCheck(item)">
                <SvgIcon icon="material-symbols:check-circle-outline-rounded" />
                {{ item.checked ? '已打卡' : '打卡' }}
              </button>
            </div>
          </div>
          <em>{{ item.remain }}</em>
        </div>
      </article>

      <article class="lm-card live-card">
        <div class="lm-card-title">
          <h2>直播 / 版本更新</h2>
          <button type="button" @click="openDetail('countdown', '版本时间线', '集中查看直播、维护、活动开放和补偿领取。', ['前瞻：04-30 19:30', '维护：05-02 06:00'])">
            查看全部
          </button>
        </div>
        <div v-for="(item, index) in versionEvents" :key="item.title" class="live-row" :class="{ blue: index === 1, checked: item.checked }">
          <strong>{{ item.title }}</strong>
          <span>{{ item.distance }}</span>
          <p>{{ item.start }}</p>
          <div class="live-actions">
            <button type="button" :class="{ active: item.remind }" @click="toggleVersionReminder(item)">
              {{ item.remind ? '取消提醒' : '设置提醒' }}
            </button>
            <button type="button" :class="{ active: item.checked }" @click="toggleVersionCheck(item)">
              {{ item.checked ? '已打卡' : '打卡' }}
            </button>
          </div>
        </div>
      </article>

      <article class="lm-card account-card">
        <div class="lm-card-title">
          <h2>账号资产</h2>
          <button type="button" @click="openManagePage">
            管理
          </button>
        </div>
        <div class="safe-box"></div>
        <div class="account-list">
          <p>官方账号（主）<span>已保护</span></p>
          <p>小号-搭配测试<span>已保护</span></p>
          <p>Nintendo Switch 账号<span>已保护</span></p>
        </div>
        <footer>
          共 3 个资产
          <button type="button" @click="info('资产列表', '可继续接入账号、兑换码和支付凭证')">查看全部</button>
        </footer>
      </article>

      <article class="lm-card note-card">
        <div class="lm-card-title">
          <h2>最近笔记</h2>
          <button type="button" @click="openDetail('note', '最近笔记', '按项目聚合剧情、路线、素材与搭配赛记录。', ['当前展示 4 条', '项目笔记共 12 条'])">
            查看全部
          </button>
        </div>
        <button type="button" @click="openDetail('note', '5.1版本剧情音乐彩蛋分析与搭配建议', '记录剧情音乐彩蛋、推荐套装与搭配建议。', ['2小时前', '标签：剧情 / 搭配'])">
          5.1版本剧情音乐彩蛋分析与搭配建议 <span>2小时前</span>
        </button>
        <button type="button" @click="openDetail('note', '心愿原野探索路线规划', '整理采集、奇想星与隐藏拍照点路线。', ['昨天', '标签：探索'])">
          心愿原野探索路线规划 <span>昨天</span>
        </button>
        <button type="button" @click="openDetail('note', '搭配赛主题：春日余念风格整理', '按主题整理发型、外套、饰品和评分倾向。', ['2天前', '标签：搭配赛'])">
          搭配赛主题：春日余念风格整理 <span>2天前</span>
        </button>
        <button type="button" @click="openDetail('note', '素材收集清单汇总', '汇总本周重点材料和推荐采集时间。', ['3天前', '标签：素材'])">
          素材收集清单汇总 <span>3天前</span>
        </button>
      </article>

      <article class="lm-card gallery-card">
        <div class="lm-card-title">
          <h2>截图图册</h2>
          <button type="button" @click="scrollList('gallery', 'right')">滑动</button>
        </div>
        <div ref="galleryListRef" class="gallery-grid">
          <button
            v-for="index in 8"
            :key="index"
            type="button"
            :aria-label="`查看图册第 ${index} 张`"
            @click="openDetail('gallery', `图册照片 ${index}`, '展示截图详情、拍摄地点和关联笔记。', ['相册：花愿镇', `序号：${index}/8`])"
          ></button>
        </div>
        <p>共 8 个图册，126 张照片</p>
      </article>

      <article class="lm-card ai-nikki">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 项目建议</h2>
          <button type="button" @click="info('AI 建议已刷新', '根据当前任务和活动状态重新生成')">
            <SvgIcon icon="material-symbols:refresh-rounded" />
          </button>
        </div>
        <ul>
          <li>你今天还有 {{ dailyTotal - dailyDoneCount }} 个任务未完成，建议优先处理。</li>
          <li>活动「奇迹之旅第二章」还有 3 天结束，别错过奖励。</li>
          <li>根据你的搭配风格，推荐优先收集「晨曦花园」套装。</li>
          <li>本周素材收集进度偏慢，建议安排集中采集时间。</li>
        </ul>
        <button
          class="lm-plain-btn"
          type="button"
          @click="openDetail('ai', 'AI 项目分析报告', '汇总日常、活动倒计时、素材路线与搭配目标的演示报告。', [`任务完成：${dailyDoneCount}/${dailyTotal}`, `提醒：${remindedCount}`])"
        >
          查看完整分析报告 ›
        </button>
        <div class="lm-robot"></div>
      </article>
    </section>

    <section class="lm-card nikki-activity">
      <div class="lm-card-title">
        <h2>最近动态</h2>
        <div class="nikki-scroll-actions compact">
          <button type="button" aria-label="向左滑动动态" @click="scrollList('activity', 'left')">
            <SvgIcon icon="material-symbols:keyboard-arrow-left-rounded" />
          </button>
          <button type="button" aria-label="向右滑动动态" @click="scrollList('activity', 'right')">
            <SvgIcon icon="material-symbols:keyboard-arrow-right-rounded" />
          </button>
        </div>
      </div>
      <div ref="activityListRef">
        <button type="button" @click="openDetail('task', '完成了每日任务', '今日游戏日常完成记录。', ['1小时前', `${dailyDoneCount}/${dailyTotal}`])">
          <SvgIcon icon="material-symbols:check-box-rounded" />完成了每日任务 <span>1小时前</span>
        </button>
        <button type="button" @click="openDetail('note', '新增笔记', '新增剧情音乐彩蛋分析与搭配建议。', ['2小时前', '笔记'])">
          <SvgIcon icon="material-symbols:article-outline-rounded" />新增笔记 <span>2小时前</span>
        </button>
        <button type="button" @click="openDetail('gallery', '上传了 6 张截图', '图册新增截图，可关联到花愿镇路线。', ['昨天 21:40', '图册'])">
          <SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" />上传了 6 张截图 <span>昨天 21:40</span>
        </button>
        <button type="button" @click="openDetail('countdown', '活动进度更新', '奇迹之旅第二章奖励领取状态已更新。', ['昨天 18:30', '活动'])">
          <SvgIcon icon="material-symbols:event-upcoming-outline-rounded" />活动进度更新 <span>昨天 18:30</span>
        </button>
        <button type="button" @click="openDetail('countdown', '版本公告', '5.2 版本前瞻与维护时间已同步。', ['04-27 12:00', '公告'])">
          <SvgIcon icon="material-symbols:campaign-outline-rounded" />版本公告 <span>04-27 12:00</span>
        </button>
      </div>
    </section>

    <LifeModal
      v-model:show="detail.show"
      :title="detail.title"
      :description="detail.description"
      confirm-text="知道了"
      cancel-text="关闭"
      :width="460"
      @confirm="confirmDetail"
    >
      <div class="nikki-detail-modal" :class="`kind-${detail.kind}`">
        <span class="detail-kind">
          <SvgIcon
            :icon="
              detail.kind === 'task'
                ? 'material-symbols:check-circle-outline-rounded'
                : detail.kind === 'countdown'
                  ? 'material-symbols:timer-outline-rounded'
                  : detail.kind === 'asset'
                    ? 'material-symbols:inventory-2-outline-rounded'
                    : detail.kind === 'gallery'
                      ? 'material-symbols:photo-library-outline-rounded'
                      : detail.kind === 'ai'
                        ? 'material-symbols:auto-awesome-rounded'
                        : 'material-symbols:edit-note-outline-rounded'
            "
          />
        </span>
        <ul>
          <li v-for="item in detail.meta" :key="item">{{ item }}</li>
        </ul>
      </div>
    </LifeModal>
  </LifeAppShell>
</template>

<style scoped>
.nikki-head {
  display: grid;
  grid-template-columns: 244px minmax(0, 1fr) auto;
  gap: 24px;
  align-items: center;
  padding: 0;
  overflow: hidden;
}

.nikki-hero-img {
  height: 144px;
  border-radius: 8px;
}

.lm-detail-title h1 {
  font-size: 24px;
}

.lm-detail-title h1 span {
  color: #f3bc47;
  font-size: 16px;
}

.nikki-meta {
  display: grid;
  grid-template-columns: repeat(5, auto);
  gap: 0;
  margin-top: 18px;
}

.nikki-meta span {
  min-width: 82px;
  padding-right: 20px;
  border-right: 1px solid #edf0f6;
  color: #8b91a1;
  font-size: 10px;
}

.nikki-meta b {
  display: block;
  margin-top: 7px;
  color: #171d2c;
  font-size: 12px;
}

.nikki-head .lm-actions {
  align-self: start;
  margin: 44px 15px 0 0;
}

.nikki-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  margin-top: 14px;
}

.nikki-segment {
  display: inline-grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 4px;
  width: 100%;
  margin: 0 0 14px;
  padding: 4px;
  border-radius: 8px;
  background: #f6f7fc;
}

.nikki-segment button {
  min-height: 28px;
  border: 0;
  border-radius: 7px;
  background: transparent;
  color: #7d8495;
  font-size: 10px;
  cursor: pointer;
}

.nikki-segment button.active {
  background: #fff;
  color: #6d56e8;
  box-shadow: 0 8px 18px rgba(93, 78, 180, 0.08);
}

.daily-content {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  gap: 18px;
}

.daily-circle {
  width: 94px;
  height: 94px;
  display: grid;
  place-items: center;
  align-content: center;
  border-radius: 50%;
  background:
    radial-gradient(circle, #fff 0 56%, transparent 57%),
    conic-gradient(#765ce8 0 var(--daily-progress, 0%), #eef0f7 var(--daily-progress, 0%));
}

.daily-circle strong {
  font-size: 18px;
}

.daily-circle span {
  color: #8b91a1;
  font-size: 10px;
}

.daily-content ul {
  max-height: 132px;
  margin: 0;
  padding: 0;
  overflow: auto;
  list-style: none;
  color: #5a6275;
  font-size: 10px;
}

.daily-content li {
  position: relative;
  margin-bottom: 8px;
  padding-left: 16px;
}

.daily-content li::before {
  content: '';
  position: absolute;
  top: 5px;
  left: 0;
  display: block;
  width: 8px;
  height: 8px;
  border: 1px solid #cfd4e1;
  border-radius: 50%;
}

.daily-content li.done::before {
  border-color: #765ce8;
  background: #765ce8;
}

.daily-content li button {
  display: grid;
  width: 100%;
  padding: 0;
  border: 0;
  background: transparent;
  color: inherit;
  text-align: left;
  cursor: pointer;
}

.daily-content li em {
  margin-top: 3px;
  color: #9aa1b1;
  font-size: 9px;
  font-style: normal;
}

.nikki-scroll-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
  margin-top: 12px;
}

.nikki-scroll-actions button {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border: 0;
  border-radius: 7px;
  background: #f6f7fc;
  color: #765ce8;
  cursor: pointer;
}

.nikki-scroll-actions.compact {
  margin-top: 0;
}

.week-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 126px 34px 64px;
  gap: 10px;
  align-items: center;
  margin-bottom: 8px;
  padding: 7px 8px;
  border-radius: 8px;
  color: #485165;
  font-size: 10px;
}

.week-row.selected {
  background: #f7f5ff;
}

.week-row.done {
  color: #31a978;
}

.week-row.warning {
  color: #e17b2e;
}

.week-row button {
  min-width: 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: inherit;
  font-size: 10px;
  text-align: left;
  cursor: pointer;
}

.week-row button:last-child {
  padding: 5px 8px;
  border-radius: 999px;
  background: #fff;
  color: #765ce8;
  text-align: center;
}

.week-row b {
  color: #7d8495;
  font-weight: 500;
}

.goal-panel {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  align-items: center;
  gap: 12px;
  margin-top: 12px;
  padding: 12px;
  border-radius: 8px;
  background: #f8f8fd;
}

.goal-panel span {
  display: block;
  color: #8b91a1;
  font-size: 9px;
}

.goal-panel strong {
  display: block;
  overflow: hidden;
  margin-top: 4px;
  color: #242b3c;
  font-size: 11px;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.goal-panel p {
  margin: 5px 0 0;
  color: #765ce8;
  font-size: 10px;
}

.goal-actions {
  display: flex;
  gap: 6px;
}

.goal-actions button {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border: 0;
  border-radius: 7px;
  background: #fff;
  color: #765ce8;
  cursor: pointer;
}

.countdown-row {
  display: grid;
  grid-template-columns: 54px minmax(0, 1fr) auto;
  gap: 11px;
  align-items: center;
  margin-bottom: 12px;
}

.countdown-row.checked {
  opacity: 0.78;
}

.mini-scene {
  height: 54px;
  border: 0;
  border-radius: 7px;
  cursor: pointer;
}

.countdown-row strong,
.live-row strong {
  display: block;
  color: #242b3c;
  font-size: 11px;
}

.countdown-row p,
.live-row p {
  margin: 5px 0 0;
  color: #8b91a1;
  font-size: 9px;
}

.countdown-row em {
  color: #e66591;
  font-size: 18px;
  font-style: normal;
}

.row-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 7px;
}

.row-actions button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  padding: 4px 7px;
  border: 0;
  border-radius: 999px;
  background: #f7f8fd;
  color: #7d8495;
  font-size: 9px;
  cursor: pointer;
}

.row-actions button.active {
  background: #f2edff;
  color: #765ce8;
}

.live-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 4px 12px;
  padding: 11px 0;
  border-bottom: 1px solid #edf0f6;
}

.live-row.checked strong {
  color: #31a978;
}

.live-row span {
  color: #8b91a1;
  font-size: 10px;
}

.live-actions {
  grid-row: 2;
  grid-column: 2;
  display: flex;
  gap: 6px;
}

.live-actions button {
  padding: 5px 10px;
  border: 0;
  border-radius: 999px;
  background: #fafafe;
  color: #765ce8;
  font-size: 10px;
  cursor: pointer;
}

.live-actions button.active {
  background: #f2edff;
}

.safe-box {
  width: 95px;
  height: 92px;
  float: left;
  margin-right: 15px;
  border-radius: 15px;
  background:
    radial-gradient(circle at 50% 50%, #f8f8ff 0 18%, #7e6ee6 19% 24%, transparent 25%),
    linear-gradient(135deg, #d7d1ff, #9d90f0);
  box-shadow: 0 14px 25px rgba(114, 94, 214, 0.18);
}

.account-list p,
.note-card > button {
  display: flex;
  justify-content: space-between;
  margin: 9px 0;
  color: #444c5f;
  font-size: 10px;
}

.account-list span,
.note-card span {
  color: #31a978;
}

.note-card > button {
  width: 100%;
  padding: 0;
  border: 0;
  background: transparent;
  text-align: left;
  cursor: pointer;
}

.account-card footer {
  clear: both;
  padding-top: 12px;
  color: #7f8696;
  font-size: 10px;
}

.account-card footer button {
  float: right;
  border: 0;
  background: transparent;
  color: #765ce8;
  cursor: pointer;
}

.gallery-grid {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 72px;
  gap: 6px;
  overflow-x: auto;
  padding-bottom: 2px;
}

.gallery-grid button {
  height: 48px;
  border: 0;
  border-radius: 7px;
  background: linear-gradient(135deg, #d5e8ff, #ffd8e5);
  cursor: pointer;
}

.gallery-grid button:nth-child(even) {
  background: linear-gradient(135deg, #c7e3b8, #f1e1c8);
}

.gallery-card p {
  margin: 12px 0 0;
  color: #7f8696;
  font-size: 10px;
}

.ai-nikki {
  position: relative;
}

.ai-nikki ul {
  margin: 0;
  padding-left: 16px;
  color: #596174;
  font-size: 10px;
  line-height: 2;
}

.ai-nikki .lm-robot {
  position: absolute;
  right: 8px;
  bottom: -8px;
  transform: scale(0.72);
}

.nikki-activity {
  margin-top: 14px;
}

.nikki-activity > div:last-child {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: minmax(170px, 1fr);
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 2px;
}

.nikki-activity button {
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr);
  gap: 8px;
  margin: 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: #495164;
  font-size: 10px;
  text-align: left;
  cursor: pointer;
}

.nikki-activity .svg-icon {
  color: #63bd96;
  font-size: 21px;
}

.nikki-activity span {
  color: #8b91a1;
}

.nikki-detail-modal {
  display: grid;
  grid-template-columns: 48px minmax(0, 1fr);
  gap: 14px;
  align-items: start;
}

.detail-kind {
  display: inline-grid;
  width: 48px;
  height: 48px;
  place-items: center;
  border-radius: 8px;
  background: #f2edff;
  color: #765ce8;
  font-size: 24px;
}

.nikki-detail-modal ul {
  display: grid;
  gap: 8px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.nikki-detail-modal li {
  padding: 10px 12px;
  border-radius: 8px;
  background: #f8f8fd;
  color: #596174;
  font-size: 12px;
}

@media (max-width: 1180px) {
  .nikki-head,
  .nikki-grid,
  .nikki-activity > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

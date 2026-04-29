<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'Home'
});

interface TodoItem {
  title: string;
  tag: string;
  time: string;
  done: boolean;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();

const summaryRange = ref<'today' | 'week'>('today');
const weatherMode = ref<'today' | 'trip'>('today');
const projectFilter = ref<'全部' | '游戏' | '旅行' | '学习'>('全部');
const aiIndex = ref(0);
const showTodoModal = ref(false);
const showEventModal = ref(false);
const newTodoTitle = ref('');
const newTodoTag = ref('生活');
const newTodoTime = ref('21:30');
const realtimeCardRef = ref<HTMLElement | null>(null);
const statusListRef = ref<HTMLElement | null>(null);

const todos = ref<TodoItem[]>([
  { title: '完成项目文档初稿', tag: '工作', time: '10:00', done: true },
  { title: '健身 30 分钟', tag: '生活', time: '18:30', done: false },
  { title: '阅读《深度工作》第 3 章', tag: '学习', time: '20:00', done: false },
  { title: '整理一周开销', tag: '生活', time: '21:00', done: false }
]);

const expiring = [
  ['腾讯视频 VIP', '6月2日 还有 13 天', 'material-symbols:play-circle-outline-rounded'],
  ['ChatGPT Plus 会员', '6月5日 还有 16 天', 'material-symbols:all-inclusive-rounded'],
  ['域名 - xiamuyouran.com', '6月18日 还有 29 天', 'material-symbols:language-rounded'],
  ['平安百万医疗险', '6月25日 还有 36 天', 'material-symbols:health-and-safety-outline-rounded']
] as const;

const projectItems = ref([
  { name: '无限暖暖', type: '游戏', summary: '今日任务 5/8', extraA: '待办 3', extraB: '倒计时 2', cover: 'nikki', pinned: true },
  { name: '原神', type: '游戏', summary: '今日任务 4/9', extraA: '待办 2', extraB: '倒计时 1', cover: 'genshin', pinned: false },
  { name: '日本旅行 2026', type: '旅行', summary: '待办 12', extraA: '行程 6', extraB: '倒计时 3', cover: 'japan', pinned: false },
  { name: 'OpenAI 学习计划', type: '学习', summary: '本周目标 3/5', extraA: '待办 4', extraB: '笔记 7', cover: 'openai', pinned: false }
]);

const status = ref([
  ['AI 总结完成：日本旅行行前清单', '10:42'],
  ['文件上传完成：IMG_2024.jpg', '10:31'],
  ['提醒触发：腾讯视频 VIP 即将到期', '09:00'],
  ['数据同步完成', '08:20'],
  ['AI 总结完成：本周学习复盘', '昨天 22:15']
]);

const aiSuggestions = [
  '你昨天的日记情绪较稳定，建议继续保持晨间记录。',
  '日本旅行项目有 3 个待办即将开始，记得安排时间。',
  '无限暖暖有活动提醒，别错过奖励。',
  '本周学习计划只差 2 次打卡，今晚完成会很轻松。'
];

const quickActions = [
  ['添加待办', 'material-symbols:check-box-rounded'],
  ['记录日记', 'material-symbols:article-outline-rounded'],
  ['写笔记', 'material-symbols:edit-note-outline-rounded'],
  ['上传图册', 'material-symbols:add-photo-alternate-outline-rounded'],
  ['记账', 'material-symbols:history-rounded'],
  ['搜索', 'material-symbols:search-rounded'],
  ['导入数据', 'material-symbols:upload-file-outline-rounded'],
  ['更多', 'material-symbols:more-horiz-rounded']
] as const;

const filteredProjects = computed(() => {
  if (projectFilter.value === '全部') return projectItems.value;
  return projectItems.value.filter(item => item.type === projectFilter.value);
});

const doneCount = computed(() => todos.value.filter(item => item.done).length);
const todoCount = computed(() => todos.value.length - doneCount.value);
const scheduleCount = computed(() => (summaryRange.value === 'today' ? 3 : 9));
const pendingAlertCount = computed(() => (weatherMode.value === 'today' ? 1 : 2));
const expiringCount = computed(() => expiring.length);
const selectedSuggestion = computed(() => aiSuggestions[aiIndex.value]);
const currentWeatherLabel = computed(() =>
  weatherMode.value === 'today' ? '5月20日 周二' : '日本旅行模式 · 东京'
);
const currentWeatherValue = computed(() =>
  weatherMode.value === 'today' ? '多云 18℃ - 25℃' : '晴 21℃ - 27℃'
);

function toggleTodo(index: number) {
  const item = todos.value[index];
  item.done = !item.done;
  success(item.done ? '待办已完成' : '待办已恢复', item.title);
}

function openTodoModal() {
  showTodoModal.value = true;
}

function createTodo() {
  const title = newTodoTitle.value.trim();
  const time = newTodoTime.value;

  if (!title) {
    warning('请先输入待办', '标题不能为空');
    return;
  }

  todos.value.unshift({
    title,
    tag: newTodoTag.value,
    time,
    done: false
  });

  newTodoTitle.value = '';
  newTodoTag.value = '生活';
  newTodoTime.value = '21:30';
  showTodoModal.value = false;
  success('已添加待办', `${title} · ${time}`);
}

function cycleSuggestion() {
  aiIndex.value = (aiIndex.value + 1) % aiSuggestions.length;
  info('AI 建议已刷新', `当前建议 ${aiIndex.value + 1}/${aiSuggestions.length}`);
}

function toggleProjectPin(name: string) {
  const target = projectItems.value.find(item => item.name === name);
  if (!target) return;
  target.pinned = !target.pinned;
  info(target.pinned ? '已置顶项目' : '已取消置顶', target.name);
}

function runQuickAction(label: string) {
  if (label === '添加待办') {
    openTodoModal();
    return;
  }

  if (label === '记录日记') {
    showEventModal.value = true;
    return;
  }

  info('快捷入口演示', `${label} 交互已触发`);
}

function switchRange(range: 'today' | 'week') {
  summaryRange.value = range;
  info(range === 'today' ? '切回今日概览' : '已切换到本周视角');
}

function switchWeather() {
  weatherMode.value = weatherMode.value === 'today' ? 'trip' : 'today';
  info(weatherMode.value === 'today' ? '显示本地天气' : '切换到旅行天气卡片');
}

function scrollToRealtime() {
  realtimeCardRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  info('已定位到实时状态', '查看最新同步与提醒');
}

function scrollStatusList(direction: 'up' | 'down') {
  statusListRef.value?.scrollBy({
    top: direction === 'up' ? -88 : 88,
    behavior: 'smooth'
  });
  info(direction === 'up' ? '向上查看动态' : '向下查看动态');
}

function setProjectFilter(filter: '全部' | '游戏' | '旅行' | '学习') {
  projectFilter.value = filter;
  info('项目筛选已切换', filter);
}

function addTimelineEvent() {
  status.value.unshift(['手动记录：晚间复盘已加入时间线', '刚刚']);
  showEventModal.value = false;
  success('已加入实时状态', '晚间复盘事件已写入');
}
</script>

<template>
  <LifeAppShell active="首页">
    <LifeToastHost :items="toasts" @close="removeToast" />

    <header class="lm-topbar home-top">
      <div class="lm-title">
        <h1>上午好，夏目悠然 ☀️</h1>
        <p>新的一天，从有序开始</p>
      </div>
      <div class="lm-actions">
        <button class="lm-pill weather-pill" type="button" @click="switchWeather">
          <span>{{ currentWeatherLabel }}</span>
          <strong>{{ currentWeatherValue }}</strong>
          <SvgIcon :icon="weatherMode === 'today' ? 'material-symbols:partly-cloudy-day-rounded' : 'material-symbols:luggage-rounded'" />
        </button>
        <button class="lm-icon-btn" type="button" aria-label="搜索" @click="runQuickAction('搜索')">
          <SvgIcon icon="material-symbols:search-rounded" />
        </button>
        <button class="lm-icon-btn dot" type="button" aria-label="定位实时状态" @click="scrollToRealtime">
          <SvgIcon icon="material-symbols:notifications-outline-rounded" />
        </button>
        <button class="lm-purple-btn round-only" type="button" @click="openTodoModal">
          <SvgIcon icon="material-symbols:add-rounded" />
        </button>
      </div>
    </header>

    <section class="summary-switch">
      <button :class="{ active: summaryRange === 'today' }" type="button" @click="switchRange('today')">今日</button>
      <button :class="{ active: summaryRange === 'week' }" type="button" @click="switchRange('week')">本周</button>
    </section>

    <section class="lm-grid home-summary">
      <article class="lm-card overview-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:calendar-month-rounded" /></span>今日概览</h2>
          <button type="button" @click="switchRange(summaryRange === 'today' ? 'week' : 'today')">
            {{ summaryRange === 'today' ? '切换本周' : '切换今日' }}
          </button>
        </div>
        <div class="overview-numbers">
          <div><strong>{{ todoCount }}</strong><span>待办事项</span></div>
          <div><strong>{{ scheduleCount }}</strong><span>{{ summaryRange === 'today' ? '今日日程' : '本周日程' }}</span></div>
          <div><strong>{{ expiringCount }}</strong><span>即将到期</span></div>
          <div><strong>{{ pendingAlertCount }}</strong><span>待处理提醒</span></div>
        </div>
        <div class="soft-chart">
          <i></i><i></i><i></i><i></i><i></i>
        </div>
        <p class="overview-note">已完成 {{ doneCount }} 项，继续保持这个节奏就很好啦 💜</p>
      </article>

      <article class="lm-card todo-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:edit-calendar-outline-rounded" /></span>今日待办</h2>
          <button type="button" @click="openTodoModal">添加</button>
        </div>
        <div class="todo-list">
          <button
            v-for="(item, index) in todos"
            :key="item.title + item.time"
            class="todo-item"
            :class="{ done: item.done }"
            type="button"
            @click="toggleTodo(index)"
          >
            <span class="check"></span>
            <strong>{{ item.title }}</strong>
            <em>{{ item.tag }}</em>
            <time>{{ item.time }}</time>
            <span class="circle"></span>
          </button>
        </div>
        <button class="add-todo" type="button" @click="openTodoModal">＋ 添加待办</button>
      </article>

      <article class="lm-card expire-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon pink"><SvgIcon icon="material-symbols:event-upcoming-outline-rounded" /></span>即将到期（30 天内）</h2>
          <button type="button" @click="info('到期提醒', '这里可以继续下钻到完整列表')">查看全部</button>
        </div>
        <div class="expire-list">
          <div v-for="item in expiring" :key="item[0]">
            <span class="lm-soft-icon"><SvgIcon :icon="item[2]" /></span>
            <strong>{{ item[0] }}</strong>
            <time>{{ item[1] }}</time>
          </div>
        </div>
      </article>
    </section>

    <section class="lm-card project-strip">
      <div class="lm-card-title project-strip-head">
        <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:folder-outline-rounded" /></span>项目总览</h2>
        <div class="project-filters">
          <button
            v-for="item in ['全部', '游戏', '旅行', '学习']"
            :key="item"
            :class="{ active: projectFilter === item }"
            type="button"
            @click="setProjectFilter(item as '全部' | '游戏' | '旅行' | '学习')"
          >
            {{ item }}
          </button>
        </div>
      </div>
      <div class="home-projects">
        <article v-for="item in filteredProjects" :key="item.name">
          <button class="pin-btn" type="button" @click="toggleProjectPin(item.name)">
            <SvgIcon :icon="item.pinned ? 'material-symbols:star-rounded' : 'material-symbols:star-outline-rounded'" />
          </button>
          <div class="home-project-cover" :class="item.cover"></div>
          <h3>{{ item.name }} <span>{{ item.type }}</span></h3>
          <div class="home-project-meta">
            <span>{{ item.summary }}</span>
            <span>{{ item.extraA }}</span>
            <span>{{ item.extraB }}</span>
          </div>
          <footer>版本更新 5月28日 <b>还有 8 天</b></footer>
        </article>
      </div>
    </section>

    <section class="lm-grid cols-4 home-bottom">
      <article class="lm-card ai-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 今日建议</h2>
          <button type="button" @click="cycleSuggestion">换一条</button>
        </div>
        <ul>
          <li>{{ selectedSuggestion }}</li>
          <li>如果今天只做一件事，优先完成最晚那条待办会更轻松。</li>
          <li>睡前花 5 分钟收一下桌面，明早会更舒服。</li>
        </ul>
        <button class="lm-plain-btn" type="button" @click="cycleSuggestion">查看完整建议 ›</button>
        <div class="lm-robot"></div>
      </article>

      <article class="lm-card month-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:donut-large-rounded" /></span>本月统计</h2>
        </div>
        <div class="donut"></div>
        <div class="legend">
          <span><i></i>工作 32%</span>
          <span><i></i>学习 24%</span>
          <span><i></i>生活 21%</span>
          <span><i></i>娱乐 15%</span>
          <span><i></i>旅行 8%</span>
        </div>
        <p>总时长 <strong>128 小时</strong>　较上月 <b>+12%</b></p>
      </article>

      <article class="lm-card quick-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:verified-user-outline-rounded" /></span>快捷入口</h2>
        </div>
        <div class="quick-grid">
          <button v-for="item in quickActions" :key="item[0]" type="button" @click="runQuickAction(item[0])">
            <SvgIcon :icon="item[1]" />{{ item[0] }}
          </button>
        </div>
      </article>

      <article ref="realtimeCardRef" class="lm-card realtime-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:sensors-rounded" /></span>实时状态（SSE）</h2>
          <div class="status-actions">
            <button class="lm-icon-btn" type="button" aria-label="向上滑动动态" @click="scrollStatusList('up')">
              <SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" />
            </button>
            <button class="lm-icon-btn" type="button" aria-label="向下滑动动态" @click="scrollStatusList('down')">
              <SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" />
            </button>
            <button type="button" @click="showEventModal = true">新增事件 ›</button>
          </div>
        </div>
        <div ref="statusListRef" class="status-list">
          <div v-for="item in status" :key="item[0] + item[1]">
            <span></span>
            <strong>{{ item[0] }}</strong>
            <time>{{ item[1] }}</time>
          </div>
        </div>
      </article>
    </section>

    <LifeModal
      v-model:show="showTodoModal"
      title="新增待办"
      description="快速补一条今天想完成的小事情。"
      confirm-text="保存待办"
      @confirm="createTodo"
    >
      <div class="demo-form">
        <label>
          <span>待办标题</span>
          <input v-model="newTodoTitle" type="text" maxlength="30" placeholder="例如：整理旅行证件" />
        </label>
        <div class="demo-form-grid">
          <label>
            <span>分类</span>
            <select v-model="newTodoTag">
              <option>生活</option>
              <option>工作</option>
              <option>学习</option>
              <option>旅行</option>
            </select>
          </label>
          <label>
            <span>时间</span>
            <input v-model="newTodoTime" type="time" />
          </label>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      v-model:show="showEventModal"
      title="新增动态记录"
      description="把刚刚发生的小进展记进时间线。"
      confirm-text="加入动态"
      @confirm="addTimelineEvent"
    >
      <div class="demo-event-card">
        <strong>晚间复盘 · 21:30</strong>
        <p>用于演示搜索 / 通知 / SSE 状态补录这类轻量交互。</p>
      </div>
    </LifeModal>
  </LifeAppShell>
</template>

<style scoped>
.home-summary {
  grid-template-columns: 1.05fr 1fr 1.35fr;
}

.summary-switch {
  display: inline-flex;
  gap: 6px;
  margin-bottom: 14px;
  padding: 4px;
  border: 1px solid #eceef6;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.86);
}

.summary-switch button,
.project-filters button,
.pin-btn,
.todo-item {
  border: 0;
}

.summary-switch button,
.project-filters button {
  height: 28px;
  padding: 0 14px;
  border-radius: 999px;
  background: transparent;
  color: #697183;
  font-size: 11px;
}

.summary-switch button.active,
.project-filters button.active {
  background: linear-gradient(135deg, #7258ed, #765ce8);
  color: #fff;
  box-shadow: 0 10px 20px rgba(111, 83, 231, 0.18);
}

.round-only {
  width: 38px;
  height: 38px;
  padding: 0;
  border-radius: 50%;
}

.weather-pill {
  height: 48px;
  align-items: flex-start;
  flex-direction: column;
  position: relative;
  min-width: 143px;
  padding: 8px 50px 8px 13px;
  text-align: left;
}

.weather-pill span {
  color: #626a7d;
  font-size: 10px;
}

.weather-pill strong {
  color: #1c2232;
  font-size: 11px;
}

.weather-pill .svg-icon {
  position: absolute;
  right: 13px;
  top: 12px;
  color: #f2bd47;
  font-size: 26px;
}

.overview-numbers {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
}

.overview-numbers strong {
  display: block;
  font-size: 25px;
  line-height: 1;
}

.overview-numbers span {
  display: block;
  margin-top: 10px;
  color: #7e8495;
  font-size: 10px;
}

.soft-chart {
  height: 74px;
  display: flex;
  align-items: end;
  gap: 0;
  margin-top: 14px;
  background: linear-gradient(180deg, transparent 0 68%, #f4f0ff 69%);
  clip-path: polygon(0 58%, 18% 72%, 35% 47%, 52% 70%, 70% 28%, 86% 64%, 100% 42%, 100% 100%, 0 100%);
}

.soft-chart i {
  flex: 1;
}

.overview-note {
  margin: 9px 0 0;
  color: #7b8192;
  font-size: 10px;
}

.todo-list,
.expire-list,
.status-list {
  display: grid;
  gap: 12px;
}

.todo-item {
  display: grid;
  grid-template-columns: 13px minmax(0, 1fr) auto auto 13px;
  gap: 9px;
  align-items: center;
  padding: 0;
  background: transparent;
  color: #343b4d;
  font-size: 11px;
  text-align: left;
}

.todo-item.done strong {
  color: #97a0b3;
  text-decoration: line-through;
}

.todo-item.done em {
  background: #f0ecff;
  color: #745de8;
}

.todo-item.done .check {
  border-color: #765ce8;
  background: #765ce8;
  box-shadow: inset 0 0 0 2px #fff;
}

.todo-item strong,
.expire-list strong,
.status-list strong {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.todo-item em {
  padding: 2px 6px;
  border-radius: 5px;
  background: #eef8f4;
  color: #4d9a83;
  font-style: normal;
  font-size: 9px;
}

.todo-item time,
.expire-list time,
.status-list time {
  color: #7c8394;
  font-size: 10px;
}

.check,
.circle {
  width: 11px;
  height: 11px;
  border: 1px solid #cfd4e1;
  border-radius: 3px;
}

.circle {
  border-radius: 50%;
}

.add-todo {
  width: 100%;
  margin-top: 16px;
  padding-top: 13px;
  border-top: 1px solid #edf0f7;
  background: transparent;
  color: #7660e6;
  font-size: 11px;
}

.expire-list div {
  display: grid;
  grid-template-columns: 31px minmax(0, 1fr) auto;
  align-items: center;
  gap: 12px;
}

.project-strip {
  margin-top: 14px;
}

.project-strip-head {
  display: flex;
  justify-content: space-between;
  gap: 12px;
}

.project-filters {
  display: inline-flex;
  gap: 6px;
  flex-wrap: wrap;
}

.home-projects {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.home-projects article {
  position: relative;
  overflow: hidden;
  border: 1px solid #edf0f6;
  border-radius: 8px;
  background: #fff;
}

.pin-btn {
  position: absolute;
  top: 9px;
  right: 9px;
  z-index: 2;
  display: grid;
  place-items: center;
  width: 24px;
  height: 24px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  color: #745de8;
  box-shadow: 0 8px 18px rgba(54, 61, 92, 0.12);
}

.home-project-cover {
  height: 70px;
}

.home-project-cover.nikki,
.home-project-cover.japan {
  position: relative;
}

.home-project-cover.nikki {
  background: linear-gradient(135deg, #dae8ff, #ffddea);
}

.home-project-cover.genshin {
  background: linear-gradient(135deg, #c8e8ff 0 45%, #8dbf82 46%);
}

.home-project-cover.japan {
  background: linear-gradient(180deg, #aad4f2 0 50%, #ffd9df 51%, #91aa7e 52%);
}

.home-project-cover.openai {
  background: linear-gradient(135deg, #c7b4ff, #eadfff);
}

.home-projects h3 {
  margin: 9px 10px 8px;
  color: #171d2c;
  font-size: 13px;
}

.home-projects h3 span {
  margin-left: 5px;
  padding: 1px 5px;
  border-radius: 5px;
  background: #eef8f4;
  color: #519b87;
  font-size: 9px;
  font-weight: 500;
}

.home-project-meta {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
  color: #596174;
  font-size: 10px;
}

.home-projects footer {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
  padding: 9px 10px;
  border-top: 1px solid #edf0f6;
  color: #8b91a1;
  font-size: 9px;
}

.home-projects footer b {
  color: #7660e6;
}

.home-bottom {
  grid-template-columns: 1fr 0.92fr 0.95fr 1fr;
  margin-top: 14px;
}

.ai-card {
  position: relative;
  min-height: 178px;
}

.ai-card ul {
  margin: 0;
  padding-left: 15px;
  color: #4d5568;
  font-size: 10px;
  line-height: 2;
}

.ai-card .lm-robot {
  position: absolute;
  right: 20px;
  bottom: 14px;
}

.donut {
  width: 92px;
  height: 92px;
  margin: 4px auto 10px;
  border-radius: 50%;
  background: conic-gradient(#765ce8 0 32%, #5b8df0 32% 56%, #62cfa8 56% 77%, #f1ad43 77% 92%, #eb6d91 92%);
  position: relative;
}

.donut::after {
  content: '';
  position: absolute;
  inset: 24px;
  border-radius: 50%;
  background: #fff;
}

.legend {
  display: grid;
  gap: 6px;
  color: #596174;
  font-size: 10px;
}

.legend i {
  display: inline-block;
  width: 7px;
  height: 7px;
  margin-right: 6px;
  border-radius: 50%;
  background: #765ce8;
}

.month-card p {
  margin: 12px 0 0;
  color: #7f8596;
  font-size: 10px;
}

.month-card b {
  color: #39ad7e;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 9px;
}

.quick-grid button {
  display: grid;
  place-items: center;
  gap: 6px;
  min-height: 48px;
  border-radius: 8px;
  background: #f8f8fe;
  color: #626a7b;
  font-size: 10px;
}

.quick-grid .svg-icon {
  color: #725ae8;
  font-size: 21px;
}

.status-list div {
  display: grid;
  grid-template-columns: 7px minmax(0, 1fr) auto;
  align-items: center;
  gap: 9px;
}

.status-list {
  max-height: 124px;
  overflow-y: auto;
  padding-right: 4px;
  scroll-behavior: smooth;
}

.status-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.status-actions .lm-icon-btn {
  width: 28px;
  height: 28px;
  font-size: 18px;
}

.status-list span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #765ce8;
}

.demo-form {
  display: grid;
  gap: 14px;
}

.demo-form label {
  display: grid;
  gap: 8px;
}

.demo-form span {
  color: #697183;
  font-size: 12px;
}

.demo-form input,
.demo-form select {
  height: 38px;
  border: 1px solid #e7e9f2;
  border-radius: 8px;
  background: #fff;
  padding: 0 12px;
  color: #1d2332;
  outline: none;
}

.demo-form-grid {
  display: grid;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  gap: 12px;
}

.demo-event-card {
  padding: 14px;
  border: 1px solid #eceef6;
  border-radius: 8px;
  background: #fafafe;
}

.demo-event-card strong {
  display: block;
  color: #202636;
  font-size: 13px;
}

.demo-event-card p {
  margin: 8px 0 0;
  color: #7b8294;
  font-size: 12px;
  line-height: 1.6;
}

@media (max-width: 1180px) {
  .home-summary,
  .home-bottom,
  .home-projects {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

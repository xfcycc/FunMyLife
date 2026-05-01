<script setup lang="ts">
import { computed, ref } from 'vue';
import type { RouteKey } from '@elegant-router/types';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useLifeToast } from '@/hooks/business/lifeFeedback';
import { useRouterPush } from '@/hooks/common/router';

defineOptions({
  name: 'Home'
});

type TodoTag = '工作' | '生活' | '学习' | '旅行';
type ProjectTag = '游戏' | '旅行' | '学习';
type Tone = 'primary' | 'blue' | 'cyan' | 'green' | 'orange' | 'pink' | 'purple' | 'red' | 'slate';

interface TodoItem {
  title: string;
  tag: TodoTag;
  time: string;
  done: boolean;
}

interface ExpiringItem {
  title: string;
  date: string;
  days: number;
  icon: string;
  tone: Tone;
  tag?: string;
  urgent?: boolean;
}

interface ProjectItem {
  title: string;
  tag: ProjectTag;
  image: string;
  stats: { label: string; value: string }[];
  bottomLeft: string;
  daysLeft: number;
  urgent?: boolean;
  routeKey?: RouteKey;
}

interface StatItem {
  label: string;
  percent: number;
  tone: Tone;
}

interface ShortcutItem {
  label: string;
  icon: string;
  tone: Tone;
}

interface EventItem {
  prefix: string;
  content: string;
  time: string;
  tone: Tone;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();
const { routerPushByKey } = useRouterPush();

const weatherMode = ref<'today' | 'trip'>('today');
const showTodoModal = ref(false);
const showEventModal = ref(false);
const newTodoTitle = ref('');
const newTodoTag = ref<TodoTag>('生活');
const newTodoTime = ref('21:30');
const realtimeCardRef = ref<HTMLElement | null>(null);
const statusListRef = ref<HTMLElement | null>(null);

const todos = ref<TodoItem[]>([
  { title: '完成项目文档初稿', tag: '工作', time: '10:00', done: false },
  { title: '健身 30 分钟', tag: '生活', time: '18:30', done: false },
  { title: '阅读《深度工作》第 3 章', tag: '学习', time: '20:00', done: false },
  { title: '整理一周开销', tag: '生活', time: '21:00', done: false }
]);

const expiringItems: ExpiringItem[] = [
  { title: '腾讯视频 VIP', date: '6月2日', days: 13, icon: 'material-symbols:play-circle-outline-rounded', tone: 'orange' },
  { title: 'ChatGPT Plus 会员', date: '6月5日', days: 16, icon: 'material-symbols:all-inclusive-rounded', tone: 'slate' },
  { title: '域名 - xiamuyouran.com', date: '6月18日', days: 29, icon: 'material-symbols:language-rounded', tone: 'blue', tag: '域名' },
  { title: '平安百万医疗险', date: '6月25日', days: 36, icon: 'material-symbols:health-and-safety-outline-rounded', tone: 'orange', tag: '保险' }
];

const projects: ProjectItem[] = [
  {
    title: '无限暖暖',
    tag: '游戏',
    image: 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=800&q=80',
    stats: [
      { label: '今日任务', value: '5/8' },
      { label: '待办', value: '3' },
      { label: '倒计时', value: '2' }
    ],
    bottomLeft: '版本更新 5月28日',
    daysLeft: 8,
    urgent: true,
    routeKey: 'infinity-nikki'
  },
  {
    title: '原神',
    tag: '游戏',
    image: 'https://images.unsplash.com/photo-1550053912-421714856f61?auto=format&fit=crop&w=800&q=80',
    stats: [
      { label: '今日任务', value: '4/9' },
      { label: '待办', value: '2' },
      { label: '倒计时', value: '1' }
    ],
    bottomLeft: '版本更新 6月12日',
    daysLeft: 23
  },
  {
    title: '日本旅行 2026',
    tag: '旅行',
    image: 'https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&w=800&q=80',
    stats: [
      { label: '待办', value: '12' },
      { label: '行程', value: '6' },
      { label: '倒计时', value: '3' }
    ],
    bottomLeft: '出发时间 2026年3月18日',
    daysLeft: 302,
    routeKey: 'japan-travel'
  },
  {
    title: 'OpenAI 学习计划',
    tag: '学习',
    image: 'https://images.unsplash.com/photo-1677442136019-21780ecad995?auto=format&fit=crop&w=800&q=80',
    stats: [
      { label: '本周目标', value: '3/5' },
      { label: '待办', value: '4' },
      { label: '笔记', value: '7' }
    ],
    bottomLeft: '下次复盘 5月25日',
    daysLeft: 5,
    urgent: true
  }
];

const statsData: StatItem[] = [
  { label: '工作', percent: 32, tone: 'primary' },
  { label: '学习', percent: 24, tone: 'blue' },
  { label: '生活', percent: 21, tone: 'green' },
  { label: '娱乐', percent: 15, tone: 'orange' },
  { label: '旅行', percent: 8, tone: 'pink' }
];

const shortcuts: ShortcutItem[] = [
  { label: '添加待办', icon: 'material-symbols:check-box-rounded', tone: 'primary' },
  { label: '记录日记', icon: 'material-symbols:article-outline-rounded', tone: 'purple' },
  { label: '写笔记', icon: 'material-symbols:edit-note-rounded', tone: 'cyan' },
  { label: '上传图册', icon: 'material-symbols:add-photo-alternate-rounded', tone: 'blue' },
  { label: '记账', icon: 'material-symbols:percent-rounded', tone: 'green' },
  { label: '搜索', icon: 'material-symbols:search-rounded', tone: 'orange' },
  { label: '导入数据', icon: 'material-symbols:download-rounded', tone: 'primary' },
  { label: '更多', icon: 'material-symbols:more-horiz-rounded', tone: 'slate' }
];

const events = ref<EventItem[]>([
  { prefix: 'AI 总结完成', content: '日本旅行行前清单', time: '10:42', tone: 'primary' },
  { prefix: '文件上传完成', content: 'IMG_2024.jpg', time: '10:31', tone: 'purple' },
  { prefix: '提醒触发', content: '腾讯视频 VIP 即将到期', time: '09:00', tone: 'pink' },
  { prefix: '数据同步完成', content: '', time: '08:20', tone: 'green' },
  { prefix: 'AI 总结完成', content: '本周学习复盘', time: '昨天 22:15', tone: 'primary' }
]);

const overviewStats = computed(() => [
  { label: '待办事项', value: 8 },
  { label: '今日日程', value: 3 },
  { label: '即将到期', value: 2 },
  { label: '待处理提醒', value: weatherMode.value === 'today' ? 1 : 2 }
]);

const currentWeatherLabel = computed(() => (weatherMode.value === 'today' ? '5月20日 周二' : '日本旅行模式 · 东京'));
const currentWeatherValue = computed(() => (weatherMode.value === 'today' ? '多云 18℃ - 25℃' : '晴 21℃ - 27℃'));

function getTagTone(tag: TodoTag | ProjectTag) {
  const toneMap: Record<TodoTag | ProjectTag, Tone> = {
    工作: 'blue',
    生活: 'purple',
    学习: 'green',
    旅行: 'orange',
    游戏: 'pink'
  };

  return toneMap[tag];
}

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

function openProject(project: ProjectItem) {
  if (project.routeKey) {
    routerPushByKey(project.routeKey);
    return;
  }

  info('项目详情待接入', project.title);
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

  if (label === '搜索') {
    info('搜索入口演示', '后续可接入全局搜索');
    return;
  }

  info('快捷入口演示', `${label} 交互已触发`);
}

function switchWeather() {
  weatherMode.value = weatherMode.value === 'today' ? 'trip' : 'today';
  info(weatherMode.value === 'today' ? '显示本地天气' : '切换到旅行天气卡片');
}

function scrollToRealtime() {
  realtimeCardRef.value?.scrollIntoView({ behavior: 'smooth', block: 'start' });
  info('已定位到实时状态', '查看最新同步与提醒');
}

function addTimelineEvent() {
  events.value.unshift({ prefix: '手动记录', content: '晚间复盘已加入时间线', time: '刚刚', tone: 'primary' });
  showEventModal.value = false;
  success('已加入实时状态', '晚间复盘事件已写入');
}
</script>

<template>
  <LifeGeminiShell
    title="上午好，夏目悠然 ☀️"
    description="新的一天，从有序开始"
    :breadcrumbs="[{ label: '首页' }]"
  >
    <LifeToastHost :items="toasts" @close="removeToast" />

    <template #actions>
      <button class="home-weather" type="button" @click="switchWeather">
        <span>{{ currentWeatherLabel }}</span>
        <strong>{{ currentWeatherValue }}</strong>
        <SvgIcon :icon="weatherMode === 'today' ? 'material-symbols:partly-cloudy-day-rounded' : 'material-symbols:luggage-rounded'" />
      </button>
      <button class="home-icon-btn" type="button" aria-label="搜索" @click="runQuickAction('搜索')">
        <SvgIcon icon="material-symbols:search-rounded" />
      </button>
      <button class="home-icon-btn has-dot" type="button" aria-label="定位实时状态" @click="scrollToRealtime">
        <SvgIcon icon="material-symbols:notifications-outline-rounded" />
      </button>
      <button class="home-add-btn" type="button" aria-label="新增待办" @click="openTodoModal">
        <SvgIcon icon="material-symbols:add-rounded" />
      </button>
    </template>

    <div class="home-dashboard">
      <section class="home-row home-summary">
        <article class="home-card overview-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon primary"><SvgIcon icon="material-symbols:event-available-outline-rounded" /></span>今日概览</h2>
            <button type="button" @click="info('今日概览', '后续可进入完整概览')">更多 <SvgIcon icon="material-symbols:chevron-right-rounded" /></button>
          </div>

          <div class="overview-numbers">
            <div v-for="item in overviewStats" :key="item.label">
              <strong>{{ item.value }}</strong>
              <span>{{ item.label }}</span>
            </div>
          </div>

          <div class="overview-chart" aria-hidden="true">
            <svg viewBox="0 0 240 72" preserveAspectRatio="none">
              <path class="chart-fill" d="M0 72 L0 42 C25 26 48 58 74 36 C100 14 121 55 148 30 C174 6 195 45 218 20 C229 9 240 26 240 26 L240 72 Z" />
              <path class="chart-line" d="M0 42 C25 26 48 58 74 36 C100 14 121 55 148 30 C174 6 195 45 218 20 C229 9 240 26 240 26" />
              <circle cx="74" cy="36" r="4" />
              <circle cx="148" cy="30" r="4" />
              <circle cx="218" cy="20" r="4" />
            </svg>
            <div><span>5/16</span><span>5/17</span><span>5/18</span><span>5/19</span><span>5/20</span></div>
          </div>

          <p class="overview-note"><SvgIcon icon="material-symbols:sentiment-satisfied-outline-rounded" />保持节奏，你已经很棒了！ 💜</p>
        </article>

        <article class="home-card todo-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon blue"><SvgIcon icon="material-symbols:edit-calendar-outline-rounded" /></span>今日待办</h2>
            <button type="button" @click="openTodoModal">查看全部</button>
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
              <span class="todo-check"></span>
              <strong>{{ item.title }}</strong>
              <em :class="`tone-${getTagTone(item.tag)}`">{{ item.tag }}</em>
              <time>{{ item.time }}</time>
              <span class="todo-circle"></span>
            </button>
          </div>

          <button class="add-todo" type="button" @click="openTodoModal"><SvgIcon icon="material-symbols:add-rounded" />添加待办</button>
        </article>

        <article class="home-card expire-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon pink"><SvgIcon icon="material-symbols:alarm-on-rounded" /></span>即将到期 <small>（30天内）</small></h2>
            <button type="button" @click="info('到期提醒', '这里可以继续下钻到完整列表')">查看全部</button>
          </div>

          <div class="expire-list">
            <div v-for="item in expiringItems" :key="item.title">
              <span class="expire-icon" :class="`tone-${item.tone}`"><SvgIcon :icon="item.icon" /></span>
              <strong>{{ item.title }} <em v-if="item.tag" :class="`tone-${item.tone}`">{{ item.tag }}</em></strong>
              <time><b>{{ item.date }}</b> 还有 {{ item.days }} 天</time>
            </div>
          </div>
        </article>
      </section>

      <section class="home-card project-strip">
        <div class="home-card-head project-strip-head">
          <h2><span class="home-title-icon primary"><SvgIcon icon="material-symbols:folder-special-rounded" /></span>项目总览</h2>
        </div>

        <div class="project-grid">
          <article
            v-for="item in projects"
            :key="item.title"
            class="project-card"
            role="button"
            tabindex="0"
            @click="openProject(item)"
            @keydown.enter="openProject(item)"
            @keydown.space.prevent="openProject(item)"
          >
            <div class="project-cover" :style="{ backgroundImage: `url(${item.image})` }"></div>
            <div class="project-body">
              <h3>{{ item.title }} <span :class="`tone-${getTagTone(item.tag)}`">{{ item.tag }}</span></h3>
              <div class="project-meta">
                <span v-for="stat in item.stats" :key="stat.label">{{ stat.label }} <b>{{ stat.value }}</b></span>
              </div>
              <footer>
                <span>{{ item.bottomLeft }}</span>
                <b :class="{ urgent: item.urgent }">还有 {{ item.daysLeft }} 天</b>
              </footer>
            </div>
          </article>
        </div>
      </section>

      <section class="home-row home-bottom">
        <article class="home-card ai-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon primary"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 今日建议</h2>
          </div>
          <ul>
            <li>你昨天的日记情绪整体积极，建议继续保持哦～</li>
            <li>日本旅行项目有 3 个待办即将开始，记得安排时间</li>
            <li>无限暖暖有直播活动提醒，别错过奖励</li>
            <li>本周学习计划完成度 60%，继续加油 💪</li>
          </ul>
          <button type="button" @click="info('AI 建议', '后续可打开完整建议列表')">查看完整建议 <SvgIcon icon="material-symbols:chevron-right-rounded" /></button>
          <div class="home-robot"></div>
        </article>

        <article class="home-card month-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon blue"><SvgIcon icon="material-symbols:pie-chart-rounded" /></span>本月统计</h2>
          </div>
          <div class="stats-wrap">
            <div class="donut"><span>128h</span></div>
            <div class="legend">
              <span v-for="item in statsData" :key="item.label"><i :class="`tone-${item.tone}`"></i>{{ item.label }} <b>{{ item.percent }}%</b></span>
            </div>
          </div>
          <p>总时长 <strong>128 小时</strong><b>较上月 +12% ↗</b></p>
        </article>

        <article class="home-card quick-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon primary"><SvgIcon icon="material-symbols:diamond-rounded" /></span>快捷入口</h2>
          </div>
          <div class="quick-grid">
            <button v-for="item in shortcuts" :key="item.label" type="button" @click="runQuickAction(item.label)">
              <span :class="`tone-${item.tone}`"><SvgIcon :icon="item.icon" /></span>
              {{ item.label }}
            </button>
          </div>
        </article>

        <article ref="realtimeCardRef" class="home-card realtime-card">
          <div class="home-card-head">
            <h2><span class="home-title-icon slate"><SvgIcon icon="material-symbols:format-list-bulleted-rounded" /></span>实时状态 <small>（SSE）</small></h2>
            <button type="button" @click="scrollToRealtime">全部事件 <SvgIcon icon="material-symbols:chevron-right-rounded" /></button>
          </div>
          <div ref="statusListRef" class="event-list">
            <div v-for="item in events" :key="item.prefix + item.content + item.time">
              <span :class="`tone-${item.tone}`"></span>
              <p><b>{{ item.prefix }}</b>{{ item.content ? `：${item.content}` : '' }}</p>
              <time>{{ item.time }}</time>
            </div>
          </div>
        </article>
      </section>
    </div>

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
  </LifeGeminiShell>
</template>

<style scoped>
.home-dashboard {
  display: grid;
  width: 100%;
  min-width: 0;
  gap: 24px;
}

.home-row {
  display: grid;
  gap: 24px;
}

.home-summary {
  grid-template-columns: repeat(auto-fit, minmax(min(360px, 100%), 1fr));
}

.home-bottom {
  grid-template-columns: repeat(auto-fit, minmax(min(280px, 100%), 1fr));
}

.home-card {
  min-width: 0;
  border: 1px solid #eef1f6;
  border-radius: 24px;
  background: #fff;
  box-shadow: 0 14px 34px rgba(21, 31, 56, 0.05);
}

.home-summary > .home-card,
.home-bottom > .home-card {
  min-height: 300px;
  padding: 24px;
}

.home-card-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 22px;
}

.home-card-head h2 {
  display: flex;
  align-items: center;
  gap: 10px;
  min-width: 0;
  margin: 0;
  color: #1f2633;
  font-size: 20px;
  font-weight: 800;
  line-height: 1.2;
}

.home-card-head h2 small {
  color: #6f7688;
  font-size: 15px;
  font-weight: 500;
}

.home-card-head button {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  border: 0;
  background: transparent;
  color: #8770ee;
  font-size: 13px;
  font-weight: 700;
  white-space: nowrap;
}

.home-title-icon {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border-radius: 8px;
  font-size: 20px;
}

.home-weather,
.home-icon-btn,
.home-add-btn {
  border: 0;
  box-shadow: 0 10px 24px rgba(21, 31, 56, 0.08);
}

.home-weather {
  position: relative;
  display: grid;
  min-width: 160px;
  min-height: 48px;
  padding: 7px 44px 7px 12px;
  border-radius: 16px;
  background: #fff;
  color: #5b6473;
  text-align: right;
}

.home-weather span {
  font-size: 13px;
  font-weight: 700;
}

.home-weather strong {
  margin-top: 2px;
  font-size: 11px;
  font-weight: 500;
}

.home-weather .svg-icon {
  position: absolute;
  right: 13px;
  top: 12px;
  color: #f0b646;
  font-size: 24px;
}

.home-icon-btn,
.home-add-btn {
  position: relative;
  display: inline-grid;
  width: 40px;
  height: 40px;
  place-items: center;
  border-radius: 50%;
  background: #fff;
  color: #5f6878;
  font-size: 22px;
}

.home-icon-btn.has-dot::after {
  content: '';
  position: absolute;
  top: 10px;
  right: 10px;
  width: 8px;
  height: 8px;
  border: 2px solid #fff;
  border-radius: 50%;
  background: #ef4d79;
}

.home-add-btn {
  background: #6b57ff;
  color: #fff;
  box-shadow: 0 12px 28px rgba(107, 87, 255, 0.26);
}

.overview-numbers {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
  text-align: center;
}

.overview-numbers strong {
  display: block;
  color: #111827;
  font-size: 40px;
  line-height: 1;
  font-weight: 800;
}

.overview-numbers span {
  display: block;
  margin-top: 12px;
  color: #768093;
  font-size: 13px;
  font-weight: 650;
}

.overview-chart {
  margin-top: 28px;
}

.overview-chart svg {
  width: 100%;
  height: 88px;
}

.chart-fill {
  fill: rgba(107, 87, 255, 0.12);
}

.chart-line {
  fill: none;
  stroke: #8a78ff;
  stroke-width: 2.8;
}

.overview-chart circle {
  fill: #fff;
  stroke: #8a78ff;
  stroke-width: 2.4;
}

.overview-chart div {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
  color: #9aa2b2;
  font-size: 12px;
}

.overview-note {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 18px 0 0;
  color: #737b8e;
  font-size: 13px;
}

.todo-list,
.expire-list {
  display: grid;
  gap: 18px;
}

.todo-item {
  display: grid;
  grid-template-columns: 18px minmax(0, 1fr) auto auto 18px;
  gap: 14px;
  align-items: center;
  border: 0;
  background: transparent;
  color: #394150;
  font-size: 14px;
  text-align: left;
}

.todo-item strong,
.expire-list strong,
.event-list p {
  min-width: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.todo-item.done strong {
  color: #98a1b2;
  text-decoration: line-through;
}

.todo-check,
.todo-circle {
  width: 18px;
  height: 18px;
  border: 1px solid #c9d0dc;
  border-radius: 5px;
}

.todo-circle {
  border-radius: 50%;
}

.todo-item.done .todo-check {
  border-color: #6b57ff;
  background: #6b57ff;
  box-shadow: inset 0 0 0 3px #fff;
}

.todo-item em,
.project-card h3 span,
.expire-list em {
  display: inline-flex;
  align-items: center;
  padding: 2px 8px;
  border-radius: 7px;
  font-size: 11px;
  font-style: normal;
  font-weight: 700;
}

.todo-item time,
.expire-list time {
  color: #6f7688;
  font-size: 13px;
}

.add-todo {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  width: 100%;
  min-height: 42px;
  margin-top: 24px;
  border: 1px dashed #dce1ea;
  border-radius: 14px;
  background: #fff;
  color: #6b57ff;
  font-size: 14px;
  font-weight: 750;
}

.expire-list div {
  display: grid;
  grid-template-columns: 42px minmax(0, 1fr) auto;
  gap: 18px;
  align-items: center;
}

.expire-icon {
  display: grid;
  width: 38px;
  height: 38px;
  place-items: center;
  border-radius: 50%;
  font-size: 24px;
}

.expire-list strong {
  color: #2d3442;
  font-size: 15px;
}

.expire-list time {
  text-align: right;
  white-space: nowrap;
}

.expire-list time b {
  color: #303847;
  font-weight: 600;
}

.project-strip {
  padding: 24px 28px 28px;
}

.project-strip-head {
  margin-bottom: 20px;
}

.project-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(min(260px, 100%), 1fr));
  gap: 20px;
}

.project-card {
  overflow: hidden;
  border: 1px solid #eef1f6;
  border-radius: 18px;
  background: #fff;
  box-shadow: 0 12px 26px rgba(51, 58, 88, 0.08);
  cursor: pointer;
  transition:
    transform 0.2s ease,
    box-shadow 0.2s ease;
}

.project-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 18px 34px rgba(51, 58, 88, 0.12);
}

.project-cover {
  height: 112px;
  background-position: center;
  background-size: cover;
}

.project-body {
  padding: 14px 18px 0;
}

.project-card h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 14px;
  color: #1f2633;
  font-size: 17px;
  line-height: 1.25;
  font-weight: 800;
}

.project-meta {
  display: flex;
  justify-content: space-between;
  gap: 8px;
  color: #687184;
  font-size: 13px;
}

.project-meta b {
  margin-left: 4px;
  color: #263041;
}

.project-card footer {
  display: flex;
  justify-content: space-between;
  gap: 10px;
  margin-top: 18px;
  padding: 14px 0;
  border-top: 1px solid #eef1f6;
  color: #8d95a5;
  font-size: 13px;
}

.project-card footer b {
  color: #6b57ff;
}

.project-card footer b.urgent {
  color: #ef6b8d;
}

.ai-card {
  position: relative;
  overflow: hidden;
}

.ai-card ul {
  position: relative;
  z-index: 1;
  display: grid;
  gap: 12px;
  margin: 0;
  padding-left: 18px;
  color: #4f586b;
  font-size: 13px;
  line-height: 1.65;
}

.ai-card > button {
  position: relative;
  z-index: 1;
  display: inline-flex;
  align-items: center;
  gap: 6px;
  width: max-content;
  min-height: 34px;
  margin-top: 24px;
  padding: 0 16px;
  border: 1px solid #edf0f6;
  border-radius: 999px;
  background: #fafbff;
  color: #7760e8;
  font-size: 13px;
  font-weight: 750;
}

.home-robot {
  position: absolute;
  right: 34px;
  bottom: 24px;
  width: 78px;
  height: 78px;
  border-radius: 50%;
  background:
    radial-gradient(circle at 36% 45%, #42d6ff 0 5%, transparent 6%),
    radial-gradient(circle at 64% 45%, #42d6ff 0 5%, transparent 6%),
    radial-gradient(ellipse at 50% 47%, #263b72 0 30%, transparent 31%),
    linear-gradient(135deg, #f8fbff, #dce4ff);
  box-shadow: 0 18px 36px rgba(107, 87, 255, 0.14);
  opacity: 0.88;
}

.stats-wrap {
  display: flex;
  align-items: center;
  gap: 26px;
}

.donut {
  position: relative;
  display: grid;
  width: 112px;
  height: 112px;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 50%;
  background: conic-gradient(#6b57ff 0 32%, #38bdf8 32% 56%, #62cfa8 56% 77%, #f8b84e 77% 92%, #f06b98 92%);
}

.donut::after {
  content: '';
  position: absolute;
  inset: 30px;
  border-radius: 50%;
  background: #fff;
}

.donut span {
  position: relative;
  z-index: 1;
  color: #263041;
  font-size: 13px;
  font-weight: 800;
}

.legend {
  display: grid;
  flex: 1;
  gap: 9px;
  color: #596174;
  font-size: 13px;
}

.legend span {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
}

.legend i,
.event-list > div > span {
  display: inline-block;
  width: 9px;
  height: 9px;
  border-radius: 50%;
}

.legend i {
  margin-right: 8px;
}

.month-card p {
  display: flex;
  justify-content: space-between;
  gap: 12px;
  margin: 20px 0 0;
  padding-top: 14px;
  border-top: 1px solid #eef1f6;
  color: #7f8798;
  font-size: 13px;
}

.month-card p b {
  color: #27a36f;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(64px, 1fr));
  gap: 18px 14px;
}

.quick-grid button {
  display: grid;
  justify-items: center;
  gap: 9px;
  border: 0;
  background: transparent;
  color: #5f6878;
  font-size: 12px;
  white-space: nowrap;
}

.quick-grid button span {
  display: grid;
  width: 44px;
  height: 44px;
  place-items: center;
  border-radius: 16px;
  font-size: 24px;
  transition: transform 0.18s ease;
}

.quick-grid button:hover span {
  transform: scale(1.05);
}

.event-list {
  position: relative;
  display: grid;
  gap: 16px;
  max-height: 216px;
  overflow-y: auto;
  padding-left: 14px;
  scroll-behavior: smooth;
}

.event-list::before {
  content: '';
  position: absolute;
  top: 8px;
  bottom: 8px;
  left: 4px;
  width: 1px;
  background: #eef1f6;
}

.event-list div {
  position: relative;
  display: grid;
  grid-template-columns: 9px minmax(0, 1fr) auto;
  gap: 14px;
  align-items: start;
  color: #596174;
  font-size: 13px;
}

.event-list p {
  margin: 0;
  line-height: 1.45;
}

.event-list p b {
  color: #303847;
}

.event-list time {
  color: #8d95a5;
  font-size: 12px;
  white-space: nowrap;
}

.tone-primary {
  background: #f0efff;
  color: #6b57ff;
}

.tone-blue {
  background: #eef6ff;
  color: #3385e5;
}

.tone-cyan {
  background: #ecfeff;
  color: #11a9bd;
}

.tone-green {
  background: #edf9f2;
  color: #31a876;
}

.tone-orange {
  background: #fff7e9;
  color: #e39a28;
}

.tone-pink {
  background: #fff0f6;
  color: #eb5f90;
}

.tone-purple {
  background: #f5f0ff;
  color: #8a5fe8;
}

.tone-red {
  background: #fff1f2;
  color: #ef4d5f;
}

.tone-slate {
  background: #f3f5f8;
  color: #687184;
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

@media (max-width: 760px) {
  .stats-wrap {
    align-items: flex-start;
    flex-direction: column;
  }

  .home-card {
    border-radius: 18px;
  }

  .home-summary > .home-card,
  .home-bottom > .home-card,
  .project-strip {
    padding: 18px;
  }
}
</style>

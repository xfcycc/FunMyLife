<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'JapanTravel'
});

type TabLabel = '概览' | '行程' | '清单' | '资产' | '记录' | '图册' | 'AI';
type DateFilter = '全部' | '东京' | '京都' | '大阪';
type ChecklistGroup = '行前' | '行李' | '预订' | '其他';
type BudgetState = '正常' | '需关注' | '超预算';
type DetailKind = 'schedule' | 'checklist' | 'budget' | 'asset' | 'note' | 'photo' | 'ai';

interface TabItem {
  label: TabLabel;
  icon: string;
}

interface ScheduleItem {
  id: number;
  time: string;
  title: string;
  place: string;
  city: DateFilter;
  status: '进行中' | '计划中' | '待开始' | '已完成';
  checked: boolean;
}

interface ChecklistItem {
  id: number;
  title: string;
  group: ChecklistGroup;
  done: boolean;
}

interface AssetItem {
  title: string;
  desc: string;
  status: string;
  icon: string;
}

interface DetailState {
  show: boolean;
  title: string;
  description: string;
  kind: DetailKind;
  meta: string[];
}

const { toasts, removeToast, success, info, warning } = useLifeToast();

const tabs = [
  { label: '概览', icon: 'material-symbols:calendar-month-outline-rounded' },
  { label: '行程', icon: 'material-symbols:business-center-outline-rounded' },
  { label: '清单', icon: 'material-symbols:checklist-rounded' },
  { label: '资产', icon: 'material-symbols:inventory-2-outline-rounded' },
  { label: '记录', icon: 'material-symbols:edit-square-outline-rounded' },
  { label: '图册', icon: 'material-symbols:photo-library-outline-rounded' },
  { label: 'AI', icon: 'material-symbols:auto-awesome-rounded' }
] satisfies TabItem[];

const dateFilters = ['全部', '东京', '京都', '大阪'] as const;
const checklistGroups = ['行前', '行李', '预订', '其他'] as const;
const budgetStates: BudgetState[] = ['正常', '需关注', '超预算'];
const weatherCities = [
  { name: '东京', date: '5月28日', temp: '18℃ - 25℃', weather: '晴转多云', current: '22℃' },
  { name: '京都', date: '5月29日', temp: '19℃ - 27℃', weather: '多云', current: '24℃' },
  { name: '大阪', date: '5月30日', temp: '20℃ - 28℃', weather: '小雨', current: '23℃' }
] as const;

const activeTab = ref<TabLabel>('概览');
const activeDate = ref<DateFilter>('全部');
const activeChecklist = ref<ChecklistGroup>('行前');
const budgetState = ref<BudgetState>('正常');
const budgetUsed = ref(7850);
const weatherIndex = ref(0);
const routeListRef = ref<HTMLElement | null>(null);
const photoListRef = ref<HTMLElement | null>(null);
const activityListRef = ref<HTMLElement | null>(null);

const schedule = ref<ScheduleItem[]>([
  { id: 1, time: '09:30', title: '浅草寺', place: '东京 · 台东区', city: '东京', status: '进行中', checked: false },
  { id: 2, time: '13:00', title: '银座购物', place: '东京 · 中央区', city: '东京', status: '计划中', checked: false },
  { id: 3, time: '18:00', title: '新宿晚餐', place: '东京 · 新宿区', city: '东京', status: '计划中', checked: false },
  { id: 4, time: '全天', title: '前往京都（新干线）', place: '东京 → 京都', city: '京都', status: '待开始', checked: false },
  { id: 5, time: '10:00', title: '伏见稻荷大社', place: '京都 · 伏见区', city: '京都', status: '待开始', checked: false },
  { id: 6, time: '15:00', title: '心斋桥与道顿堀', place: '大阪 · 中央区', city: '大阪', status: '待开始', checked: false }
]);

const checklist = ref<ChecklistItem[]>([
  { id: 1, title: '护照有效期确认', group: '行前', done: true },
  { id: 2, title: '签证资料归档', group: '行前', done: true },
  { id: 3, title: '交通 IC 卡充值', group: '行前', done: false },
  { id: 4, title: '转换插头', group: '行李', done: true },
  { id: 5, title: '薄外套与雨具', group: '行李', done: false },
  { id: 6, title: '常用药品', group: '行李', done: false },
  { id: 7, title: '去程机票确认', group: '预订', done: true },
  { id: 8, title: '东京酒店确认', group: '预订', done: true },
  { id: 9, title: '京都餐厅预约', group: '预订', done: false },
  { id: 10, title: '离线地图下载', group: '其他', done: false },
  { id: 11, title: '旅行保险保单备份', group: '其他', done: true }
]);

const assets = ref<AssetItem[]>([
  { title: '去程机票', desc: '6月15日 CA929 上海 → 东京', status: '已出票', icon: 'material-symbols:flight-takeoff-rounded' },
  { title: '东京酒店', desc: '6月15日 - 6月18日（3晚）', status: '已确认', icon: 'material-symbols:apartment-rounded' },
  { title: 'JR Pass 预约', desc: '东京 → 京都 → 大阪', status: '待确认', icon: 'material-symbols:train-rounded' }
]);

const detail = ref<DetailState>({
  show: false,
  title: '',
  description: '',
  kind: 'schedule',
  meta: []
});

const totalBudget = 18620;
const filteredSchedule = computed(() => {
  if (activeDate.value === '全部') return schedule.value;
  return schedule.value.filter(item => item.city === activeDate.value);
});
const visibleChecklist = computed(() => checklist.value.filter(item => item.group === activeChecklist.value));
const checklistDoneCount = computed(() => checklist.value.filter(item => item.done).length);
const checklistProgress = computed(() => Math.round((checklistDoneCount.value / checklist.value.length) * 100));
const groupDoneText = computed(() => {
  const list = visibleChecklist.value;
  return `${list.filter(item => item.done).length}/${list.length}`;
});
const budgetPercent = computed(() => Math.min(100, Math.round((budgetUsed.value / totalBudget) * 100)));
const remainingBudget = computed(() => totalBudget - budgetUsed.value);
const checkedSpotCount = computed(() => schedule.value.filter(item => item.checked).length);
const currentWeather = computed(() => weatherCities[weatherIndex.value]);

function switchTab(label: TabLabel) {
  activeTab.value = label;
  info('旅行项目视图已切换', label);
}

function switchDate(filter: DateFilter) {
  activeDate.value = filter;
  info('行程日期/城市已切换', filter);
}

function switchChecklist(group: ChecklistGroup) {
  activeChecklist.value = group;
  info('清单分组已切换', `${group}清单`);
}

function toggleSchedule(item: ScheduleItem) {
  item.checked = !item.checked;
  item.status = item.checked ? '已完成' : item.id === 1 ? '进行中' : '计划中';
  success(item.checked ? '景点已打卡' : '景点打卡已撤回', item.title);
}

function toggleChecklist(item: ChecklistItem) {
  item.done = !item.done;
  success(item.done ? '清单项已完成' : '清单项已恢复', item.title);
}

function cycleBudgetState() {
  const index = budgetStates.indexOf(budgetState.value);
  budgetState.value = budgetStates[(index + 1) % budgetStates.length];
  info('预算状态已切换', budgetState.value);
}

function adjustBudget(delta: number) {
  const next = Math.max(0, Math.min(totalBudget + 1800, budgetUsed.value + delta));
  if (next === budgetUsed.value) {
    warning(delta > 0 ? '预算已到演示上限' : '预算已归零');
    return;
  }
  budgetUsed.value = next;
  budgetState.value = budgetUsed.value > totalBudget ? '超预算' : budgetPercent.value > 75 ? '需关注' : '正常';
  success(delta > 0 ? '预算支出已增加' : '预算支出已减少', `已使用 ¥${budgetUsed.value.toLocaleString()}`);
}

function switchWeather(index?: number) {
  weatherIndex.value = typeof index === 'number' ? index : (weatherIndex.value + 1) % weatherCities.length;
  info('天气城市已切换', currentWeather.value.name);
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

function scrollList(target: 'route' | 'photo' | 'activity', direction: 'left' | 'right' | 'up' | 'down') {
  const map = {
    route: routeListRef.value,
    photo: photoListRef.value,
    activity: activityListRef.value
  };
  map[target]?.scrollBy({
    top: direction === 'up' ? -120 : direction === 'down' ? 120 : 0,
    left: direction === 'left' ? -180 : direction === 'right' ? 180 : 0,
    behavior: 'smooth'
  });
  info('页面滑动已触发', direction === 'left' || direction === 'right' ? '横向查看内容' : '纵向查看内容');
}
</script>

<template>
  <LifeAppShell active="日本旅行 2026" show-projects>
    <LifeToastHost :items="toasts" @close="removeToast" />

    <header class="travel-head lm-topbar">
      <div class="breadcrumb">← 日本旅行 2026 <span>旅行</span> ★</div>
      <div class="lm-actions">
        <button class="lm-plain-btn" type="button" @click="success('分享链接已生成', '日本旅行 2026')">
          <SvgIcon icon="material-symbols:ios-share-rounded" />分享
        </button>
        <button
          class="lm-plain-btn"
          type="button"
          @click="openDetail('schedule', '编辑旅行项目', '这里可继续接入真实项目编辑表单。', ['目的地：东京 / 京都 / 大阪', '日期：2026.06.15 - 2026.06.26'])"
        >
          <SvgIcon icon="material-symbols:edit-outline-rounded" />编辑
        </button>
        <button class="lm-purple-btn" type="button" @click="openDetail('checklist', '新建旅行事项', '新增行程、清单、预算或笔记的演示入口。', [`当前清单：${checklistDoneCount}/${checklist.length}`])">
          <SvgIcon icon="material-symbols:add-box-outline-rounded" />新建
        </button>
        <button class="lm-icon-btn" type="button" aria-label="查看提醒" @click="scrollList('route', 'down')">
          <SvgIcon icon="material-symbols:notifications-outline-rounded" />
        </button>
        <button class="lm-icon-btn" type="button" aria-label="更多操作" @click="scrollList('activity', 'right')">
          <SvgIcon icon="material-symbols:more-horiz-rounded" />
        </button>
      </div>
    </header>

    <section class="travel-hero lm-hero-art japan">
      <div class="hero-copy">
        <h1>日本旅行 2026 <span>旅行</span></h1>
        <p>东京 · 京都 · 大阪 · 奈良 · 富士山</p>
        <p>2026.06.15 - 2026.06.26（共 12 天）</p>
      </div>
      <div class="countdown-box">
        <span>出发倒计时</span>
        <strong>48<em>天</em> 08<em>时</em> 32<em>分</em> 16<em>秒</em></strong>
        <p>2026-06-15（周一）出发</p>
      </div>
      <div class="weather-box">
        <span>{{ currentWeather.name }} {{ currentWeather.date }}</span>
        <strong><SvgIcon icon="material-symbols:sunny-rounded" />{{ currentWeather.temp }}</strong>
        <p>{{ currentWeather.weather }}</p>
      </div>
    </section>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item.label" :class="{ active: item.label === activeTab }" type="button" @click="switchTab(item.label)">
        <SvgIcon :icon="item.icon" />{{ item.label }}
      </button>
    </nav>

    <section class="lm-grid travel-grid">
      <article class="lm-card">
        <div class="lm-card-title">
          <h2>今日/近期行程</h2>
          <div class="travel-scroll-actions compact">
            <button type="button" aria-label="向上查看行程" @click="scrollList('route', 'up')">
              <SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" />
            </button>
            <button type="button" aria-label="向下查看行程" @click="scrollList('route', 'down')">
              <SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" />
            </button>
          </div>
        </div>
        <div class="travel-segment">
          <button v-for="item in dateFilters" :key="item" :class="{ active: item === activeDate }" type="button" @click="switchDate(item)">
            {{ item }}
          </button>
        </div>
        <div ref="routeListRef" class="route-list">
          <div v-for="item in filteredSchedule" :key="item.id" :class="{ checked: item.checked }">
            <time>{{ item.time }}</time>
            <span></span>
            <button
              type="button"
              @click="openDetail('schedule', item.title, '行程详情、交通方式、预约状态与备注演示。', [item.time, item.place, item.status])"
            >
              <strong>{{ item.title }}</strong>
              <p>{{ item.place }}</p>
            </button>
            <em>{{ item.status }}</em>
            <button class="check-btn" type="button" @click="toggleSchedule(item)">
              <SvgIcon :icon="item.checked ? 'material-symbols:check-circle-rounded' : 'material-symbols:radio-button-unchecked-rounded'" />
            </button>
          </div>
        </div>
      </article>

      <article class="lm-card checklist-progress">
        <div class="lm-card-title">
          <h2>行前清单进度</h2>
          <button type="button" @click="openDetail('checklist', '行前清单', `当前分组 ${activeChecklist} 已完成 ${groupDoneText}。`, [`总进度：${checklistProgress}%`, `已完成：${checklistDoneCount}/${checklist.length}`])">
            查看清单
          </button>
        </div>
        <div class="ring" :style="{ '--checklist-progress': `${checklistProgress}%` }">
          <strong>{{ checklistProgress }}%</strong><span>已完成</span>
        </div>
        <div class="progress-items">
          <div class="travel-segment small">
            <button v-for="item in checklistGroups" :key="item" :class="{ active: item === activeChecklist }" type="button" @click="switchChecklist(item)">
              {{ item }}
            </button>
          </div>
          <button v-for="item in visibleChecklist" :key="item.id" type="button" :class="{ done: item.done }" @click="toggleChecklist(item)">
            <i></i>{{ item.title }} <b>{{ item.done ? '完成' : '待办' }}</b>
          </button>
        </div>
      </article>

      <article class="lm-card">
        <div class="lm-card-title">
          <h2>机票与酒店资产</h2>
          <button type="button" @click="openDetail('asset', '机票与酒店资产', '集中查看机票、酒店、交通票券和保险资料。', [`资产数量：${assets.length}`, '全部资产 8 项'])">
            查看全部
          </button>
        </div>
        <div class="asset-list">
          <div v-for="item in assets" :key="item.title">
            <span class="lm-soft-icon"><SvgIcon :icon="item.icon" /></span>
            <button type="button" @click="openDetail('asset', item.title, '资产详情、订单号、附件与提醒状态演示。', [item.desc, item.status])">
              <strong>{{ item.title }}</strong>
              <p>{{ item.desc }}</p>
            </button>
            <em>{{ item.status }}</em>
          </div>
        </div>
        <footer><button type="button" @click="info('资产列表', '这里可跳转到完整旅行资产资料')">全部资产 8 项 →</button></footer>
      </article>

      <article class="lm-card budget-card">
        <div class="lm-card-title">
          <h2>预算概览</h2>
          <button type="button" @click="cycleBudgetState">{{ budgetState }}</button>
        </div>
        <strong>¥ {{ totalBudget.toLocaleString() }}.00 <span>总预算</span></strong>
        <div class="budget-row"><span>已使用</span><b>¥ {{ budgetUsed.toLocaleString() }}.00</b><em>{{ budgetPercent }}%</em></div>
        <div class="lm-progress-line"><i :style="{ width: `${budgetPercent}%` }"></i></div>
        <div class="budget-row"><span>剩余</span><b>¥ {{ remainingBudget.toLocaleString() }}.00</b><em>{{ 100 - budgetPercent }}%</em></div>
        <div class="budget-actions">
          <button type="button" @click="adjustBudget(-500)"><SvgIcon icon="material-symbols:remove-rounded" /></button>
          <button type="button" @click="adjustBudget(500)"><SvgIcon icon="material-symbols:add-rounded" /></button>
          <button type="button" @click="openDetail('budget', '预算明细', '预算支出、剩余额度与状态演示。', [`状态：${budgetState}`, `已使用：${budgetPercent}%`])">明细</button>
        </div>
      </article>

      <article class="lm-card weather-card">
        <div class="lm-card-title">
          <h2>天气与地点</h2>
          <button type="button" @click="switchWeather()">切换城市</button>
        </div>
        <div class="city-tabs">
          <button v-for="(item, index) in weatherCities" :key="item.name" :class="{ active: index === weatherIndex }" type="button" @click="switchWeather(index)">
            {{ item.name }}
          </button>
        </div>
        <div class="big-weather"><SvgIcon icon="material-symbols:partly-cloudy-day-rounded" /><strong>{{ currentWeather.current }}</strong><span>{{ currentWeather.weather }}</span></div>
        <div class="forecast"><span>5/30<br />23°/19°</span><span>5/31<br />24°/18°</span><span>6/1<br />25°/19°</span></div>
      </article>

      <article class="lm-card album-card">
        <div class="lm-card-title">
          <h2>图册预览</h2>
          <button type="button" @click="scrollList('photo', 'right')">滑动</button>
        </div>
        <div ref="photoListRef" class="photo-row">
          <button
            v-for="index in 6"
            :key="index"
            type="button"
            :aria-label="`查看旅行图册 ${index}`"
            @click="openDetail('photo', `旅行图册 ${index}`, '图册详情、拍摄城市和关联笔记演示。', [`照片序号：${index}`, '相册：日本旅行 2026'])"
          ></button>
        </div>
        <p>共 128 张照片，12 个视频</p>
      </article>

      <article class="lm-card notes-card">
        <div class="lm-card-title">
          <h2>旅行笔记 / 攻略</h2>
          <button type="button" @click="openDetail('note', '旅行笔记 / 攻略', '整理景点、交通、美食和购物攻略。', ['当前展示 3 条', '共 18 条笔记'])">查看全部</button>
        </div>
        <button type="button" @click="openDetail('note', '东京必去景点清单', '东京景点、预约、交通和排队时间整理。', ['5月20日', '标签：东京'])">
          东京必去景点清单 <time>5月20日</time>
        </button>
        <button type="button" @click="openDetail('note', '京都交通与景点攻略', '京都巴士、地铁和景点路线规划。', ['5月18日', '标签：京都'])">
          京都交通与景点攻略 <time>5月18日</time>
        </button>
        <button type="button" @click="openDetail('note', '日本旅行必备 APP 推荐', '地图、翻译、交通和支付类 App 清单。', ['5月15日', '标签：工具'])">
          日本旅行必备 APP 推荐 <time>5月15日</time>
        </button>
        <button class="add-note" type="button" @click="success('已创建笔记草稿', '日本旅行新笔记')">＋ 新建笔记</button>
      </article>

      <article class="lm-card ai-travel">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 行前建议</h2>
          <button type="button" @click="info('AI 建议已重新生成', '已结合行程、清单与预算状态')">重新生成</button>
        </div>
        <ul>
          <li>东京 6 月中旬天气舒适，建议携带薄外套和雨具。</li>
          <li>新宿周边晚间人流较大，注意随身物品安全。</li>
          <li>你有 {{ schedule.length - checkedSpotCount }} 个景点尚未打卡，别忘记安排时间。</li>
          <li>建议在京都安排 1 天体验传统抹茶和和服拍照。</li>
        </ul>
        <button type="button" @click="openDetail('ai', 'AI 行前建议', '根据行程、清单、预算和天气生成的行前建议。', [`清单进度：${checklistProgress}%`, `预算状态：${budgetState}`])">
          查看全部建议 ›
        </button>
        <div class="lm-robot"></div>
      </article>
    </section>

    <section class="lm-card travel-activity">
      <div class="lm-card-title">
        <h2>最近动态</h2>
        <div class="travel-scroll-actions compact">
          <button type="button" aria-label="向左滑动动态" @click="scrollList('activity', 'left')">
            <SvgIcon icon="material-symbols:keyboard-arrow-left-rounded" />
          </button>
          <button type="button" aria-label="向右滑动动态" @click="scrollList('activity', 'right')">
            <SvgIcon icon="material-symbols:keyboard-arrow-right-rounded" />
          </button>
        </div>
      </div>
      <div ref="activityListRef">
        <button type="button" @click="openDetail('asset', '去程机票已出票', '机票资产状态更新。', ['5月25日 14:32', 'CA929 上海 → 东京'])">
          <SvgIcon icon="material-symbols:flight-takeoff-rounded" /> 去程机票已出票 <span>5月25日 14:32</span>
        </button>
        <button type="button" @click="openDetail('asset', '东京酒店预订成功', '酒店资产状态更新。', ['5月24日 11:08', '东京 3 晚'])">
          <SvgIcon icon="material-symbols:apartment-rounded" /> 东京酒店预订成功 <span>5月24日 11:08</span>
        </button>
        <button type="button" @click="openDetail('checklist', '行李清单更新', '行李分组新增或完成清单项。', ['5月24日 09:45', `清单进度：${checklistProgress}%`])">
          <SvgIcon icon="material-symbols:check-box-rounded" /> 行李清单更新 <span>5月24日 09:45</span>
        </button>
        <button type="button" @click="openDetail('photo', '上传了 12 张照片', '图册新增旅行素材。', ['5月23日 18:20', '图册'])">
          <SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" /> 上传了 12 张照片 <span>5月23日 18:20</span>
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
      <div class="travel-detail-modal" :class="`kind-${detail.kind}`">
        <span class="detail-kind">
          <SvgIcon
            :icon="
              detail.kind === 'schedule'
                ? 'material-symbols:map-outline-rounded'
                : detail.kind === 'checklist'
                  ? 'material-symbols:checklist-rounded'
                  : detail.kind === 'budget'
                    ? 'material-symbols:payments-outline-rounded'
                    : detail.kind === 'photo'
                      ? 'material-symbols:photo-library-outline-rounded'
                      : detail.kind === 'ai'
                        ? 'material-symbols:auto-awesome-rounded'
                        : detail.kind === 'asset'
                          ? 'material-symbols:inventory-2-outline-rounded'
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
.travel-head {
  margin-bottom: 8px;
}

.breadcrumb {
  color: #151927;
  font-size: 17px;
  font-weight: 800;
}

.breadcrumb span {
  margin-left: 8px;
  padding: 2px 7px;
  border-radius: 5px;
  background: #ffedf4;
  color: #e56b93;
  font-size: 10px;
}

.travel-hero {
  height: 181px;
  border-radius: 8px;
  color: #fff;
  box-shadow: 0 16px 34px rgba(52, 79, 116, 0.2);
}

.travel-hero::before {
  inset: 18% 45% 28% 18%;
}

.hero-copy {
  position: relative;
  z-index: 1;
  padding: 24px;
  text-shadow: 0 2px 12px rgba(28, 43, 64, 0.22);
}

.hero-copy h1 {
  margin: 0 0 8px;
  font-size: 29px;
}

.hero-copy h1 span {
  padding: 2px 7px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.75);
  color: #725ae8;
  font-size: 11px;
}

.hero-copy p {
  margin: 6px 0;
  font-size: 12px;
}

.countdown-box,
.weather-box {
  position: absolute;
  z-index: 2;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.9);
  color: #131827;
  box-shadow: 0 12px 26px rgba(37, 47, 78, 0.16);
}

.countdown-box {
  left: 19px;
  bottom: 13px;
  width: 190px;
  padding: 13px 16px;
}

.countdown-box span,
.weather-box span,
.countdown-box p,
.weather-box p {
  color: #7a8293;
  font-size: 10px;
}

.countdown-box strong {
  display: block;
  margin: 8px 0;
  font-size: 19px;
}

.countdown-box em {
  margin: 0 5px 0 2px;
  color: #626a7c;
  font-size: 10px;
  font-style: normal;
}

.countdown-box p,
.weather-box p {
  margin: 0;
}

.weather-box {
  right: 17px;
  bottom: 12px;
  width: 128px;
  padding: 13px;
}

.weather-box strong {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0 5px;
}

.weather-box .svg-icon {
  color: #f5bb43;
  font-size: 25px;
}

.travel-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  margin-top: 14px;
}

.travel-segment {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 4px;
  margin-bottom: 13px;
  padding: 4px;
  border-radius: 8px;
  background: #f6f7fc;
}

.travel-segment.small {
  margin-bottom: 10px;
}

.travel-segment button,
.travel-scroll-actions button,
.budget-actions button {
  border: 0;
  cursor: pointer;
}

.travel-segment button {
  min-height: 28px;
  border-radius: 7px;
  background: transparent;
  color: #7d8495;
  font-size: 10px;
}

.travel-segment button.active {
  background: #fff;
  color: #765ce8;
  box-shadow: 0 8px 18px rgba(93, 78, 180, 0.08);
}

.travel-scroll-actions {
  display: flex;
  justify-content: flex-end;
  gap: 6px;
}

.travel-scroll-actions.compact {
  margin: 0;
}

.travel-scroll-actions button {
  display: inline-grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border-radius: 7px;
  background: #f6f7fc;
  color: #765ce8;
}

.route-list {
  display: grid;
  gap: 13px;
  max-height: 238px;
  overflow: auto;
  padding-right: 2px;
}

.route-list div {
  display: grid;
  grid-template-columns: 40px 10px minmax(0, 1fr) auto 24px;
  gap: 9px;
  align-items: start;
}

.route-list div.checked {
  opacity: 0.72;
}

.route-list time,
.route-list p,
.asset-list p,
.travel-activity span {
  color: #8b91a1;
  font-size: 10px;
}

.route-list span {
  width: 8px;
  height: 8px;
  margin-top: 4px;
  border-radius: 50%;
  background: #775fe8;
  box-shadow: 0 0 0 4px #f0ecff;
}

.route-list strong,
.asset-list strong {
  display: block;
  color: #202636;
  font-size: 12px;
}

.route-list button,
.asset-list button {
  min-width: 0;
  padding: 0;
  border: 0;
  background: transparent;
  text-align: left;
  cursor: pointer;
}

.route-list p,
.asset-list p {
  margin: 4px 0 0;
}

.route-list em,
.asset-list em {
  padding: 2px 7px;
  border-radius: 5px;
  background: #eaf9f2;
  color: #31a978;
  font-size: 9px;
  font-style: normal;
}

.route-list .check-btn {
  display: inline-grid;
  width: 22px;
  height: 22px;
  place-items: center;
  color: #765ce8;
  font-size: 18px;
}

.checklist-progress {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  align-items: center;
}

.checklist-progress .lm-card-title {
  grid-column: 1 / -1;
}

.ring {
  width: 88px;
  height: 88px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  background: conic-gradient(#765ce8 0 var(--checklist-progress, 0%), #eef0f7 var(--checklist-progress, 0%));
  color: #171d2c;
  font-size: 20px;
  position: relative;
}

.ring::after {
  content: '';
  position: absolute;
  inset: 12px;
  border-radius: 50%;
  background: #fff;
}

.ring {
  isolation: isolate;
}

.ring span {
  display: block;
  color: #8b91a1;
  font-size: 10px;
}

.ring > * {
  z-index: 1;
}

.progress-items button,
.notes-card > button:not(.add-note) {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin: 9px 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: #596174;
  font-size: 11px;
  text-align: left;
  cursor: pointer;
}

.progress-items button.done {
  color: #31a978;
}

.progress-items i {
  display: inline-block;
  width: 8px;
  height: 8px;
  margin-right: 8px;
  border-radius: 50%;
  background: #58c99c;
}

.progress-items button:not(.done) i {
  background: #d7dce8;
}

.asset-list {
  display: grid;
  gap: 14px;
}

.asset-list div {
  display: grid;
  grid-template-columns: 30px minmax(0, 1fr) auto;
  gap: 10px;
  align-items: center;
}

.asset-list footer {
  margin-top: 11px;
}

.asset-list footer button {
  border: 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.budget-card > strong {
  display: block;
  margin: 7px 0 18px;
  color: #101624;
  font-size: 22px;
}

.budget-card > strong span {
  color: #8b91a1;
  font-size: 10px;
  font-weight: 500;
}

.budget-row {
  display: grid;
  grid-template-columns: 1fr auto auto;
  margin: 11px 0 6px;
  color: #71788a;
  font-size: 11px;
}

.budget-actions {
  display: flex;
  gap: 8px;
  margin-top: 14px;
}

.budget-actions button {
  display: inline-grid;
  min-width: 30px;
  height: 28px;
  place-items: center;
  padding: 0 10px;
  border-radius: 7px;
  background: #f6f7fc;
  color: #765ce8;
  font-size: 10px;
}

.city-tabs {
  display: flex;
  justify-content: space-around;
  gap: 6px;
}

.city-tabs button {
  flex: 1;
  min-height: 26px;
  border: 0;
  border-radius: 7px;
  background: #f6f7fc;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.city-tabs button.active {
  background: #f2edff;
}

.big-weather {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
  margin: 18px 0;
}

.big-weather .svg-icon {
  color: #f3bf49;
  font-size: 28px;
}

.big-weather strong {
  font-size: 24px;
}

.forecast {
  display: flex;
  justify-content: space-around;
  color: #7b8294;
  font-size: 10px;
  text-align: center;
}

.photo-row {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: 84px;
  gap: 6px;
  overflow-x: auto;
  padding-bottom: 2px;
}

.photo-row button {
  height: 79px;
  border: 0;
  border-radius: 7px;
  background: linear-gradient(135deg, #b9d9f1, #ffd8dc);
  cursor: pointer;
}

.photo-row button:nth-child(2) {
  background: linear-gradient(135deg, #9ad0f4, #805c43);
}

.photo-row button:nth-child(3) {
  background: linear-gradient(135deg, #f6a6a3, #f4d9aa);
}

.photo-row button:nth-child(4) {
  background: linear-gradient(135deg, #8bbd89, #ebd3b4);
}

.album-card p {
  margin: 11px 0 0;
  color: #7b8294;
  font-size: 10px;
}

.notes-card time {
  color: #8b91a1;
}

.add-note {
  margin-top: 7px;
  border: 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.ai-travel {
  position: relative;
}

.ai-travel ul {
  margin: 0;
  padding-left: 16px;
  color: #596174;
  font-size: 10px;
  line-height: 2;
}

.ai-travel .lm-robot {
  position: absolute;
  right: 12px;
  bottom: -7px;
  transform: scale(0.72);
}

.ai-travel > button,
.ai-travel .lm-card-title button {
  border: 0;
  background: transparent;
  color: #765ce8;
  cursor: pointer;
}

.travel-activity {
  margin-top: 14px;
}

.travel-activity > div:last-child {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: minmax(190px, 1fr);
  gap: 12px;
  overflow-x: auto;
  padding-bottom: 2px;
}

.travel-activity button {
  display: grid;
  grid-template-columns: 25px minmax(0, 1fr);
  gap: 8px;
  margin: 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: #495164;
  font-size: 11px;
  text-align: left;
  cursor: pointer;
}

.travel-activity .svg-icon {
  color: #765ce8;
  font-size: 22px;
}

.travel-detail-modal {
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

.travel-detail-modal ul {
  display: grid;
  gap: 8px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.travel-detail-modal li {
  padding: 10px 12px;
  border-radius: 8px;
  background: #f8f8fd;
  color: #596174;
  font-size: 12px;
}

@media (max-width: 1180px) {
  .travel-grid,
  .travel-activity > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

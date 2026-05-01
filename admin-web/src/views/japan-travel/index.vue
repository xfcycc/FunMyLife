<script setup lang="ts">
import { computed, ref, watch } from 'vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import type { LifeGeminiProjectStat, LifeGeminiProjectTag } from '@/components/life-manager/types';
import { useRouterPush } from '@/hooks/common/router';
import { useLifeToast } from '@/hooks/business/lifeFeedback';
import {
  Calendar,
  CheckCircle,
  CheckSquare,
  CheckSquare2,
  ChevronDown,
  ChevronLeft,
  ChevronRight,
  ChevronUp,
  Circle,
  CreditCard,
  Image,
  MapPin,
  Minus,
  MoreHorizontal,
  Package,
  Plane,
  Plus,
  BookOpen,
  Sparkles,
  Share2,
  Sun,
  Camera,
  Building
} from 'lucide-vue-next';

defineOptions({
  name: 'JapanTravel'
});

type TabLabel = '概览' | '行程' | '清单' | '资产' | '记录' | '图册' | 'AI';
type DateFilter = '全部' | '东京' | '京都' | '大阪';
type ChecklistGroup = '行前' | '行李' | '预订' | '其他';
type BudgetState = '正常' | '需关注' | '超预算';
type DetailKind = 'schedule' | 'checklist' | 'budget' | 'asset' | 'note' | 'photo' | 'ai';

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
  routerPushByKey('japan-travel-manage');
}

const tabs = ['概览', '行程', '清单', '资产', '记录', '图册', 'AI'];

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
  { title: '去程机票', desc: '6月15日 CA929 上海 → 东京', status: '已出票' },
  { title: '东京酒店', desc: '6月15日 - 6月18日（3晚）', status: '已确认' },
  { title: 'JR Pass 预约', desc: '东京 → 京都 → 大阪', status: '待确认' }
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

const projectTags: LifeGeminiProjectTag[] = [
  { label: '旅行' },
  { label: '进行中', tone: 'success' }
];

const projectStats = computed<LifeGeminiProjectStat[]>(() => [
  { label: '出发日期', value: '2026-06-15' },
  { label: '目的地', value: '东京 · 京都 · 大阪' },
  { label: '行程天数', value: '12 天' },
  { label: '总预算', value: `¥${totalBudget.toLocaleString()}` },
  { label: '清单进度', value: `${checklistProgress.value}%` }
]);

const detailIconComponent = computed(() => {
  const map: Record<DetailKind, typeof MapPin> = {
    schedule: MapPin,
    checklist: CheckSquare,
    budget: CreditCard,
    photo: Image,
    ai: Sparkles,
    asset: Package,
    note: BookOpen
  };
  return map[detail.value.kind];
});

const activityItems = [
  { icon: Plane, title: '去程机票已出票', time: '5月25日 14:32', kind: 'asset' as DetailKind },
  { icon: Building, title: '东京酒店预订成功', time: '5月24日 11:08', kind: 'asset' as DetailKind },
  { icon: CheckSquare2, title: '行李清单更新', time: '5月24日 09:45', kind: 'checklist' as DetailKind },
  { icon: Camera, title: '上传了 12 张照片', time: '5月23日 18:20', kind: 'photo' as DetailKind }
];

watch(activeTab, (newTab) => {
  info('旅行项目视图已切换', newTab);
});

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
  <LifeGeminiShell
    title="日本旅行 2026"
    description="东京 · 京都 · 大阪 · 奈良 · 富士山 | 2026.06.15 - 2026.06.26（共 12 天）"
    :breadcrumbs="[
      { label: '首页', routeKey: 'home' },
      { label: '项目', routeKey: 'projects' },
      { label: '日本旅行 2026' }
    ]"
  >
    <LifeToastHost :items="toasts" @close="removeToast" />

    <LifeGeminiProjectHero
      title="日本旅行 2026"
      description="东京 · 京都 · 大阪 · 奈良 · 富士山"
      cover-src="https://images.unsplash.com/photo-1493976040374-85c8e12f0c0e?auto=format&fit=crop&q=80&w=800&h=400"
      cover-alt="日本旅行 2026"
      :tags="projectTags"
      :stats="projectStats"
    >
      <template #title-icon>
        <Plane class="text-indigo-500 w-5 h-5" />
      </template>

      <template #actions>
        <div class="flex items-center space-x-2 px-4 py-2 bg-indigo-50 rounded-xl">
          <Calendar :size="14" class="text-indigo-500" />
          <span class="text-xs font-medium text-indigo-600">出发倒计时 48 天</span>
        </div>
        <div class="flex items-center space-x-2 px-4 py-2 bg-amber-50 rounded-xl">
          <Sun :size="14" class="text-amber-500" />
          <span class="text-xs font-medium text-amber-600">{{ currentWeather.name }} {{ currentWeather.current }}</span>
        </div>
      </template>
    </LifeGeminiProjectHero>

    <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

    <div class="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-4 gap-6">
      <LifeGeminiCard title="今日/近期行程">
        <template #action>
          <div class="flex gap-1">
            <button class="w-7 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center" type="button" @click="scrollList('route', 'up')">
              <ChevronUp :size="14" />
            </button>
            <button class="w-7 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center" type="button" @click="scrollList('route', 'down')">
              <ChevronDown :size="14" />
            </button>
          </div>
        </template>
        <div class="grid grid-cols-4 gap-1 p-1 rounded-lg bg-slate-50 mb-3">
          <button
            v-for="item in dateFilters"
            :key="item"
            :class="['min-h-7 rounded-md text-xs border-0 cursor-pointer transition-colors', item === activeDate ? 'bg-white text-indigo-500 shadow-sm' : 'bg-transparent text-slate-500']"
            type="button"
            @click="switchDate(item)"
          >
            {{ item }}
          </button>
        </div>
        <div ref="routeListRef" class="space-y-3 max-h-60 overflow-auto">
          <div
            v-for="item in filteredSchedule"
            :key="item.id"
            :class="['grid items-start gap-2', item.checked ? 'opacity-70' : '']"
            style="grid-template-columns: 40px 8px minmax(0,1fr) auto 22px"
          >
            <time class="text-xs text-slate-400">{{ item.time }}</time>
            <span class="w-2 h-2 mt-1 rounded-full bg-indigo-500 shadow-[0_0_0_4px_rgba(99,102,241,0.1)]"></span>
            <button
              class="min-w-0 p-0 border-0 bg-transparent text-left cursor-pointer"
              type="button"
              @click="openDetail('schedule', item.title, '行程详情、交通方式、预约状态与备注演示。', [item.time, item.place, item.status])"
            >
              <strong class="block text-xs font-medium text-slate-800">{{ item.title }}</strong>
              <p class="text-xs text-slate-400 mt-1">{{ item.place }}</p>
            </button>
            <em class="px-2 py-0.5 rounded bg-emerald-50 text-emerald-600 text-[10px] not-italic">{{ item.status }}</em>
            <button class="w-5 h-5 flex items-center justify-center text-indigo-500" type="button" @click="toggleSchedule(item)">
              <CheckCircle v-if="item.checked" :size="16" />
              <Circle v-else :size="16" />
            </button>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard>
        <template #title>
          <span class="text-base font-bold text-slate-800">行前清单进度</span>
        </template>
        <template #action>
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium"
            type="button"
            @click="openDetail('checklist', '行前清单', `当前分组 ${activeChecklist} 已完成 ${groupDoneText}。`, [`总进度：${checklistProgress}%`, `已完成：${checklistDoneCount}/${checklist.length}`])"
          >
            查看清单
          </button>
        </template>
        <div class="flex items-center gap-6">
          <div class="relative flex items-center justify-center shrink-0">
            <svg class="transform -rotate-90 w-20 h-20">
              <circle cx="40" cy="40" r="36" stroke="currentColor" stroke-width="6" fill="transparent" class="text-slate-100" />
              <circle
                cx="40"
                cy="40"
                r="36"
                stroke="currentColor"
                stroke-width="6"
                fill="transparent"
                :stroke-dasharray="2 * Math.PI * 36"
                :stroke-dashoffset="2 * Math.PI * 36 - (checklistProgress / 100) * 2 * Math.PI * 36"
                class="text-indigo-500 transition-all duration-1000 ease-in-out"
                stroke-linecap="round"
              />
            </svg>
            <div class="absolute flex flex-col items-center justify-center">
              <span class="text-lg font-bold text-slate-800">{{ checklistProgress }}%</span>
              <span class="text-[10px] text-slate-400">已完成</span>
            </div>
          </div>
          <div class="flex-1 space-y-2">
            <div class="grid grid-cols-4 gap-1 p-1 rounded-lg bg-slate-50 mb-2">
              <button
                v-for="item in checklistGroups"
                :key="item"
                :class="['min-h-6 rounded-md text-xs border-0 cursor-pointer', item === activeChecklist ? 'bg-white text-indigo-500 shadow-sm' : 'bg-transparent text-slate-500']"
                type="button"
                @click="switchChecklist(item)"
              >
                {{ item }}
              </button>
            </div>
            <button
              v-for="item in visibleChecklist"
              :key="item.id"
              type="button"
              :class="['flex justify-between w-full p-0 border-0 bg-transparent text-left text-xs cursor-pointer', item.done ? 'text-emerald-500' : 'text-slate-600']"
              @click="toggleChecklist(item)"
            >
              <span class="flex items-center">
                <span :class="['w-2 h-2 rounded-full mr-2', item.done ? 'bg-emerald-400' : 'bg-slate-200']"></span>
                {{ item.title }}
              </span>
              <span class="text-[10px]">{{ item.done ? '完成' : '待办' }}</span>
            </button>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="机票与酒店资产" action-text="查看全部">
        <div class="space-y-4">
          <div v-for="item in assets" :key="item.title" class="grid items-center gap-3" style="grid-template-columns: 30px minmax(0,1fr) auto">
            <div class="w-7 h-7 rounded-lg bg-indigo-50 flex items-center justify-center text-indigo-500">
              <Package :size="14" />
            </div>
            <button
              class="min-w-0 p-0 border-0 bg-transparent text-left cursor-pointer"
              type="button"
              @click="openDetail('asset', item.title, '资产详情、订单号、附件与提醒状态演示。', [item.desc, item.status])"
            >
              <strong class="block text-xs font-medium text-slate-800">{{ item.title }}</strong>
              <p class="text-xs text-slate-400 mt-1">{{ item.desc }}</p>
            </button>
            <em class="px-2 py-0.5 rounded bg-emerald-50 text-emerald-600 text-[10px] not-italic">{{ item.status }}</em>
          </div>
        </div>
        <div class="mt-4 pt-3 border-t border-slate-100">
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
            type="button"
            @click="info('资产列表', '这里可跳转到完整旅行资产资料')"
          >
            全部资产 8 项 →
          </button>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="预算概览">
        <template #action>
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
            type="button"
            @click="cycleBudgetState"
          >
            {{ budgetState }}
          </button>
        </template>
        <div class="block text-xl font-bold text-slate-800 mb-4">
          ¥ {{ totalBudget.toLocaleString() }}.00
          <span class="text-xs text-slate-400 font-normal ml-1">总预算</span>
        </div>
        <div class="flex justify-between items-center mb-2">
          <span class="text-xs text-slate-500">已使用</span>
          <span class="text-xs font-medium text-slate-700">¥ {{ budgetUsed.toLocaleString() }}.00</span>
          <span class="text-[10px] text-slate-400">{{ budgetPercent }}%</span>
        </div>
        <div class="w-full h-1.5 bg-slate-100 rounded-full overflow-hidden mb-3">
          <div class="h-full bg-indigo-500 rounded-full transition-all duration-300" :style="{ width: `${budgetPercent}%` }"></div>
        </div>
        <div class="flex justify-between items-center mb-4">
          <span class="text-xs text-slate-500">剩余</span>
          <span class="text-xs font-medium text-slate-700">¥ {{ remainingBudget.toLocaleString() }}.00</span>
          <span class="text-[10px] text-slate-400">{{ 100 - budgetPercent }}%</span>
        </div>
        <div class="flex gap-2">
          <button
            class="w-8 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center border-0 cursor-pointer"
            type="button"
            @click="adjustBudget(-500)"
          >
            <Minus :size="14" />
          </button>
          <button
            class="w-8 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center border-0 cursor-pointer"
            type="button"
            @click="adjustBudget(500)"
          >
            <Plus :size="14" />
          </button>
          <button
            class="flex-1 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center border-0 cursor-pointer text-xs"
            type="button"
            @click="openDetail('budget', '预算明细', '预算支出、剩余额度与状态演示。', [`状态：${budgetState}`, `已使用：${budgetPercent}%`])"
          >
            明细
          </button>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="天气与地点">
        <template #action>
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
            type="button"
            @click="switchWeather()"
          >
            切换城市
          </button>
        </template>
        <div class="flex gap-1 p-1 rounded-lg bg-slate-50 mb-4">
          <button
            v-for="(item, index) in weatherCities"
            :key="item.name"
            :class="['flex-1 min-h-7 rounded-md text-xs border-0 cursor-pointer transition-colors', index === weatherIndex ? 'bg-white text-indigo-500 shadow-sm' : 'bg-transparent text-slate-500']"
            type="button"
            @click="switchWeather(index)"
          >
            {{ item.name }}
          </button>
        </div>
        <div class="flex items-center justify-center gap-3 mb-4">
          <Sun :size="28" class="text-amber-400" />
          <span class="text-2xl font-bold text-slate-800">{{ currentWeather.current }}</span>
          <span class="text-sm text-slate-500">{{ currentWeather.weather }}</span>
        </div>
        <div class="flex justify-around text-center">
          <div class="text-xs text-slate-400">
            <div>5/30</div>
            <div>23°/19°</div>
          </div>
          <div class="text-xs text-slate-400">
            <div>5/31</div>
            <div>24°/18°</div>
          </div>
          <div class="text-xs text-slate-400">
            <div>6/1</div>
            <div>25°/19°</div>
          </div>
        </div>
      </LifeGeminiCard>

      <LifeGeminiCard title="图册预览">
        <template #action>
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
            type="button"
            @click="scrollList('photo', 'right')"
          >
            滑动
          </button>
        </template>
        <div ref="photoListRef" class="flex gap-2 overflow-x-auto pb-1">
          <button
            v-for="index in 6"
            :key="index"
            type="button"
            :aria-label="`查看旅行图册 ${index}`"
            class="w-20 h-20 rounded-lg bg-gradient-to-br cursor-pointer shrink-0 border-0"
            :class="[
              index === 1 ? 'from-blue-200 to-pink-200' :
              index === 2 ? 'from-blue-300 to-amber-700' :
              index === 3 ? 'from-red-200 to-amber-200' :
              index === 4 ? 'from-green-300 to-amber-200' :
              index === 5 ? 'from-purple-200 to-pink-200' :
              'from-indigo-200 to-blue-200'
            ]"
            @click="openDetail('photo', `旅行图册 ${index}`, '图册详情、拍摄城市和关联笔记演示。', [`照片序号：${index}`, '相册：日本旅行 2026'])"
          ></button>
        </div>
        <p class="text-xs text-slate-400 mt-3">共 128 张照片，12 个视频</p>
      </LifeGeminiCard>

      <LifeGeminiCard title="旅行笔记 / 攻略" action-text="查看全部">
        <div class="space-y-3">
          <button
            v-for="(note, index) in [
              { text: '东京必去景点清单', time: '5月20日', tag: '东京' },
              { text: '京都交通与景点攻略', time: '5月18日', tag: '京都' },
              { text: '日本旅行必备 APP 推荐', time: '5月15日', tag: '工具' }
            ]"
            :key="index"
            type="button"
            class="flex items-start justify-between w-full p-0 border-0 bg-transparent text-left cursor-pointer group"
            @click="openDetail('note', note.text, '旅行笔记详情。', [note.time, `标签：${note.tag}`])"
          >
            <div class="flex items-start space-x-2 pr-4">
              <BookOpen :size="14" class="text-slate-300 group-hover:text-indigo-400 transition-colors mt-0.5" />
              <div>
                <p class="text-xs font-medium text-slate-700 group-hover:text-indigo-600 transition-colors">{{ note.text }}</p>
                <span class="inline-block mt-1 px-1.5 py-0.5 bg-slate-100 text-slate-500 text-[9px] rounded">{{ note.tag }}</span>
              </div>
            </div>
            <span class="text-[10px] text-slate-400 whitespace-nowrap pt-0.5">{{ note.time }}</span>
          </button>
        </div>
        <button
          class="mt-3 appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
          type="button"
          @click="success('已创建笔记草稿', '日本旅行新笔记')"
        >
          ＋ 新建笔记
        </button>
      </LifeGeminiCard>

      <LifeGeminiCard class="bg-gradient-to-br from-indigo-50/50 to-purple-50/50">
        <template #title>
          <span class="flex items-center">
            <Sparkles :size="16" class="text-indigo-500 mr-1.5" />
            AI 行前建议
          </span>
        </template>
        <template #action>
          <button
            class="appearance-none bg-transparent border-0 p-0 text-xs text-indigo-500 hover:text-indigo-600 font-medium cursor-pointer"
            type="button"
            @click="info('AI 建议已重新生成', '已结合行程、清单与预算状态')"
          >
            重新生成
          </button>
        </template>
        <ul class="space-y-2 text-xs text-slate-600 m-0 pl-4">
          <li class="flex items-start">
            <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
            <span>东京 6 月中旬天气舒适，建议携带薄外套和雨具。</span>
          </li>
          <li class="flex items-start">
            <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
            <span>新宿周边晚间人流较大，注意随身物品安全。</span>
          </li>
          <li class="flex items-start">
            <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
            <span>你有 <strong class="text-rose-500">{{ schedule.length - checkedSpotCount }} 个景点尚未打卡</strong>，别忘记安排时间。</span>
          </li>
          <li class="flex items-start">
            <span class="text-indigo-500 mr-1.5 mt-0.5">•</span>
            <span>建议在京都安排 1 天体验传统抹茶和和服拍照。</span>
          </li>
        </ul>
        <button
          class="mt-3 appearance-none bg-transparent border-0 p-0 text-xs font-medium text-indigo-600 hover:text-indigo-700 cursor-pointer flex items-center"
          type="button"
          @click="openDetail('ai', 'AI 行前建议', '根据行程、清单、预算和天气生成的行前建议。', [`清单进度：${checklistProgress}%`, `预算状态：${budgetState}`])"
        >
          查看全部建议
          <ChevronRight :size="12" class="ml-0.5" />
        </button>
      </LifeGeminiCard>
    </div>

    <section class="mt-6 bg-white rounded-[2rem] p-6 shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)] border border-slate-100">
      <div class="flex justify-between items-center mb-6">
        <h3 class="font-bold text-slate-800 text-base">最近动态</h3>
        <div class="flex gap-1">
          <button
            class="w-7 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center border-0 cursor-pointer"
            type="button"
            @click="scrollList('activity', 'left')"
          >
            <ChevronLeft :size="14" />
          </button>
          <button
            class="w-7 h-7 bg-slate-100 text-indigo-500 rounded-lg flex items-center justify-center border-0 cursor-pointer"
            type="button"
            @click="scrollList('activity', 'right')"
          >
            <ChevronRight :size="14" />
          </button>
        </div>
      </div>
      <div ref="activityListRef" class="flex gap-4 overflow-x-auto pb-1">
        <button
          v-for="(act, index) in activityItems"
          :key="index"
          type="button"
          class="flex flex-col items-center bg-white p-3 group cursor-pointer border-0 shrink-0 hover:scale-105 transition-transform"
          @click="openDetail(act.kind, act.title, '动态详情。', [act.time])"
        >
          <div class="w-12 h-12 rounded-2xl flex items-center justify-center mb-2 shadow-sm border border-white ring-4 ring-white bg-indigo-50">
            <component :is="act.icon" :size="20" class="text-indigo-500" />
          </div>
          <p class="text-xs font-bold text-slate-700 text-center whitespace-nowrap">{{ act.title }}</p>
          <p class="text-[10px] text-slate-400 text-center mt-0.5 whitespace-nowrap">{{ act.time }}</p>
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
      <div class="grid grid-cols-[48px_minmax(0,1fr)] gap-4 items-start">
        <span class="inline-grid w-12 h-12 place-items-center rounded-xl bg-indigo-50 text-indigo-500">
          <component :is="detailIconComponent" :size="24" />
        </span>
        <ul class="grid gap-2 m-0 p-0 list-none">
          <li
            v-for="item in detail.meta"
            :key="item"
            class="p-3 rounded-lg bg-slate-50 text-slate-600 text-xs"
          >
            {{ item }}
          </li>
        </ul>
      </div>
    </LifeModal>
  </LifeGeminiShell>
</template>

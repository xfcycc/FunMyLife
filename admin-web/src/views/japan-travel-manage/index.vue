<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import LifeGeminiTopActions from '@/components/life-manager/LifeGeminiTopActions.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import type { LifeGeminiProjectStat, LifeGeminiProjectTag } from '@/components/life-manager/types';
import { useRouterPush } from '@/hooks/common/router';
import { useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'JapanTravelManage'
});

type TabLabel = '基础信息' | '行程规划' | '同行人员' | '清单模板' | '资产资料' | '图册同步' | 'AI 设置';
type ConfigSection = '日期配置' | '行程模板' | '预算分类' | '证件资料';
type ModalMode = 'create' | 'edit' | 'delete';
type ModalTarget = 'day' | 'person' | 'checklist' | 'doc' | 'budget' | 'notice';

interface ManageItem {
  id: number;
  name: string;
  desc: string;
  enabled: boolean;
  selected: boolean;
}

interface DayPlan extends ManageItem {
  date: string;
  day: string;
  tasks: string[];
}

interface PersonItem extends ManageItem {
  role: string;
}

interface ChecklistTemplate extends ManageItem {
  count: number;
}

interface DocItem extends ManageItem {
  status: string;
}

interface BudgetCategory extends ManageItem {
  amount: number;
}

interface NoticeRule extends ManageItem {
  channel: string;
}

interface ModalState {
  show: boolean;
  mode: ModalMode;
  target: ModalTarget;
  id: number | null;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();
const { routerPushByKey } = useRouterPush();

function backToProject() {
  routerPushByKey('japan-travel');
}

const tabs: TabLabel[] = ['基础信息', '行程规划', '同行人员', '清单模板', '资产资料', '图册同步', 'AI 设置'];
const configSections: ConfigSection[] = ['日期配置', '行程模板', '预算分类', '证件资料'];
const tripTypes = ['自由行', '亲友同行', '摄影旅行'] as const;
const coverUrl = 'https://images.unsplash.com/photo-1490806843957-31f4c9a91c65?auto=format&fit=crop&w=800&q=80';
const galleryCoverUrl = 'https://images.unsplash.com/photo-1490806843957-31f4c9a91c65?auto=format&fit=crop&w=240&q=80';
const companionAvatars = [
  'https://api.dicebear.com/7.x/avataaars/svg?seed=Mia&backgroundColor=e0e7ff',
  'https://api.dicebear.com/7.x/avataaars/svg?seed=Felix&backgroundColor=fef3c7',
  'https://api.dicebear.com/7.x/avataaars/svg?seed=Alex&backgroundColor=dcfce7',
  'https://api.dicebear.com/7.x/avataaars/svg?seed=Luna&backgroundColor=fce7f3'
];
const checklistIcons = [
  'material-symbols:check-box-outline-rounded',
  'material-symbols:business-center-outline-rounded',
  'material-symbols:event-available-outline-rounded',
  'material-symbols:medication-outline-rounded'
];
const assetIcons = [
  { icon: 'material-symbols:contact-page-outline-rounded', tone: 'red' },
  { icon: 'material-symbols:flight-takeoff-rounded', tone: 'blue' },
  { icon: 'material-symbols:bed-outline-rounded', tone: 'purple' },
  { icon: 'material-symbols:shield-outline-rounded', tone: 'orange' },
  { icon: 'material-symbols:description-outline-rounded', tone: 'indigo' },
  { icon: 'material-symbols:directions-car-outline-rounded', tone: 'emerald' },
  { icon: 'material-symbols:garage-home-outline-rounded', tone: 'slate' }
];

const activeTab = ref<TabLabel>('基础信息');
const activeSection = ref<ConfigSection>('日期配置');
const tripType = ref<(typeof tripTypes)[number]>('自由行');
const projectEnabled = ref(true);
const autoSave = ref(true);
const syncEnabled = ref(true);
const syncRunning = ref(false);
const dayListRef = ref<HTMLElement | null>(null);
const docListRef = ref<HTMLElement | null>(null);
const noticeListRef = ref<HTMLElement | null>(null);

const modal = ref<ModalState>({
  show: false,
  mode: 'create',
  target: 'day',
  id: null
});
const formName = ref('');
const formDesc = ref('');
const formDate = ref('5/24 周日');
const formAmount = ref(1000);

const days = ref<DayPlan[]>([
  {
    id: 1,
    date: '5/20 周三',
    day: 'DAY 1',
    name: '上海 → 东京',
    desc: '抵达与入住',
    tasks: ['抵达成田机场', '入住酒店', '新宿逛街'],
    enabled: true,
    selected: true
  },
  {
    id: 2,
    date: '5/21 周四',
    day: 'DAY 2',
    name: '东京市区',
    desc: '东京城市观光',
    tasks: ['浅草寺', '天空树', '银座'],
    enabled: true,
    selected: false
  },
  {
    id: 3,
    date: '5/22 周五',
    day: 'DAY 3',
    name: '镰仓一日游',
    desc: '海岸线与江之电',
    tasks: ['镰仓高校前', '江之电', '长谷寺'],
    enabled: true,
    selected: false
  },
  {
    id: 4,
    date: '5/23 周六',
    day: 'DAY 4',
    name: '富士山周边',
    desc: '温泉与湖区',
    tasks: ['河口湖', '网红打卡', '温泉酒店'],
    enabled: false,
    selected: false
  }
]);

const people = ref<PersonItem[]>([
  { id: 1, name: '我（夏目悠然）', desc: '项目负责人', role: '主理人', enabled: true, selected: true },
  { id: 2, name: '小悠', desc: '住宿与餐厅确认', role: '同行者', enabled: true, selected: false },
  { id: 3, name: '阿澄', desc: '交通路线确认', role: '同行者', enabled: true, selected: false },
  { id: 4, name: '小白', desc: '预算记录', role: '同行者', enabled: false, selected: false }
]);

const checklists = ref<ChecklistTemplate[]>([
  { id: 1, name: '行前准备清单', desc: '证件、保险、通信和交通', count: 32, enabled: true, selected: true },
  { id: 2, name: '行李打包清单', desc: '衣物、药品、电子设备', count: 56, enabled: true, selected: false },
  { id: 3, name: '每日必备清单', desc: '随身物品和当日票券', count: 18, enabled: true, selected: false },
  { id: 4, name: '应急药品清单', desc: '常用药与过敏药', count: 15, enabled: false, selected: false }
]);

const docs = ref<DocItem[]>([
  { id: 1, name: '护照信息', desc: '证件资料', status: '已上传', enabled: true, selected: true },
  { id: 2, name: '机票行程单', desc: '航班订单', status: '已上传', enabled: true, selected: false },
  { id: 3, name: '酒店预订单', desc: '住宿确认', status: '已上传', enabled: true, selected: false },
  { id: 4, name: '旅行保险单', desc: '保单附件', status: '已上传', enabled: true, selected: false },
  { id: 5, name: '签证资料', desc: '签证附件', status: '待补充', enabled: true, selected: false },
  { id: 6, name: '接送机信息', desc: '交通资料', status: '待确认', enabled: false, selected: false },
  { id: 7, name: '租车订单', desc: '备用交通', status: '未上传', enabled: false, selected: false }
]);

const budgetCategories = ref<BudgetCategory[]>([
  { id: 1, name: '机票交通', desc: '国际航班、JR 与市内交通', amount: 6200, enabled: true, selected: true },
  { id: 2, name: '住宿酒店', desc: '东京、京都、大阪住宿', amount: 5600, enabled: true, selected: false },
  { id: 3, name: '餐饮购物', desc: '餐厅、便利店与伴手礼', amount: 4200, enabled: true, selected: false },
  { id: 4, name: '门票体验', desc: '景点门票与体验预约', amount: 2000, enabled: true, selected: false }
]);

const noticeRules = ref<NoticeRule[]>([
  { id: 1, name: '行程提醒', desc: '出发前 7 天提醒', channel: '邮件 + 飞书', enabled: true, selected: true },
  { id: 2, name: '航班提醒', desc: '起飞前 24 小时', channel: '邮件 + 浏览器通知', enabled: true, selected: false },
  { id: 3, name: '酒店入住提醒', desc: '入住当天 10:00', channel: '飞书', enabled: true, selected: false },
  { id: 4, name: '证件到期提醒', desc: '护照到期前 60 天', channel: '邮件', enabled: false, selected: false }
]);

const aiRules = ref([
  { name: '旅行总结', desc: '旅行结束后自动生成图文总结与回顾', enabled: true },
  { name: '行前建议', desc: '根据行程和目的地生成准备建议', enabled: true },
  { name: '行程风险检查', desc: '识别潜在风险并提供调整建议', enabled: true },
  { name: '每日回顾', desc: '每天行程结束生成简短回顾', enabled: false }
]);

const integrations = [
  { name: '飞书机器人', icon: 'material-symbols:send-outline-rounded', tone: 'blue', status: '已连接' },
  { name: 'OpenClaw', icon: 'material-symbols:smart-toy-outline-rounded', tone: 'slate', status: '已连接' },
  { name: 'Webhook', icon: 'material-symbols:extension-outline-rounded', tone: 'pink', status: '已连接' }
];

const projectProgress = computed(() => Math.round(((days.value.filter(item => item.enabled).length + checklists.value.filter(item => item.enabled).length) / (days.value.length + checklists.value.length)) * 100));
const totalBudget = computed(() => budgetCategories.value.filter(item => item.enabled).reduce((sum, item) => sum + item.amount, 0));
const selectedDayCount = computed(() => days.value.filter(item => item.selected).length);
const selectedDocCount = computed(() => docs.value.filter(item => item.selected).length);
const selectedChecklistCount = computed(() => checklists.value.filter(item => item.selected).length);
const projectTags = computed<LifeGeminiProjectTag[]>(() => [
  { label: '旅行项目' },
  projectEnabled.value ? { label: '进行中', tone: 'success' } : { label: '已停用' }
]);
const projectHeroStats = computed<LifeGeminiProjectStat[]>(() => [
  { label: '旅行日期', value: '2026.05.20 - 06.02' },
  { label: '同行人数', value: `${people.value.filter(item => item.enabled).length} 人` },
  { label: '目的地', value: '东京 / 京都 / 大阪' },
  { label: '预算', value: `¥${totalBudget.value.toLocaleString()} /人` },
  { label: '项目进度', value: `${projectProgress.value}%` },
  { label: '图册', value: '328 张' }
]);
const modalTitle = computed(() => {
  if (modal.value.mode === 'delete') return '删除确认';
  const action = modal.value.mode === 'create' ? '新增' : '编辑';
  const targetName: Record<ModalTarget, string> = {
    day: '行程日',
    person: '同行人员',
    checklist: '清单分组',
    doc: '证件资料',
    budget: '预算分类',
    notice: '通知规则'
  };
  return `${action}${targetName[modal.value.target]}`;
});
const modalDescription = computed(() =>
  modal.value.mode === 'delete' ? '此操作只会移除当前演示项，不会影响真实后端数据。' : '填写名称和说明，用于演示日本旅行管理配置流程。'
);
const modalConfirmText = computed(() => (modal.value.mode === 'delete' ? '确认删除' : '保存'));

function switchTab(tab: TabLabel) {
  activeTab.value = tab;
  info('管理页签已切换', tab);
}

function switchSection(section: ConfigSection) {
  activeSection.value = section;
  info('配置分区已切换', section);
}

function toggleProject() {
  projectEnabled.value = !projectEnabled.value;
  info(projectEnabled.value ? '项目已启用' : '项目已停用', '日本旅行 2026');
}

function toggleItem(item: ManageItem, label: string) {
  item.enabled = !item.enabled;
  info(item.enabled ? `${label}已启用` : `${label}已停用`, item.name);
}

function toggleSelect(item: ManageItem) {
  item.selected = !item.selected;
}

function selectOnly(list: ManageItem[], item: ManageItem) {
  list.forEach(row => {
    row.selected = row.id === item.id;
  });
  info('已选择配置项', item.name);
}

function moveDay(index: number, direction: -1 | 1) {
  const next = index + direction;
  if (next < 0 || next >= days.value.length) {
    warning('无法继续排序', '已经到达行程边界');
    return;
  }
  [days.value[index], days.value[next]] = [days.value[next], days.value[index]];
  success('行程排序已更新', days.value[next].name);
}

function batchSetDocs(enabled: boolean) {
  const selected = docs.value.filter(item => item.selected);
  if (!selected.length) {
    warning('请先选择证件资料');
    return;
  }
  selected.forEach(item => {
    item.enabled = enabled;
  });
  success(enabled ? '已批量启用资料' : '已批量停用资料', `${selected.length} 项`);
}

function batchSetChecklists(enabled: boolean) {
  const selected = checklists.value.filter(item => item.selected);
  if (!selected.length) {
    warning('请先选择清单分组');
    return;
  }
  selected.forEach(item => {
    item.enabled = enabled;
  });
  success(enabled ? '已批量启用清单' : '已批量停用清单', `${selected.length} 项`);
}

function resetConfig() {
  projectEnabled.value = true;
  tripType.value = '自由行';
  autoSave.value = true;
  syncEnabled.value = true;
  days.value.forEach((item, index) => {
    item.enabled = index < 3;
    item.selected = index === 0;
  });
  docs.value.forEach((item, index) => {
    item.selected = index === 0;
  });
  success('配置已重置', '已恢复本页演示默认值');
}

function saveConfig() {
  success('配置已保存', `行程 ${days.value.length} 天，预算 ¥${totalBudget.value.toLocaleString()}`);
}

function openModal(
  target: ModalTarget,
  mode: ModalMode,
  source?: DayPlan | PersonItem | ChecklistTemplate | DocItem | BudgetCategory | NoticeRule
) {
  modal.value = {
    show: true,
    mode,
    target,
    id: source?.id ?? null
  };
  formName.value = source?.name ?? '';
  formDesc.value = source?.desc ?? '';
  formDate.value = source && 'date' in source ? source.date : '5/24 周日';
  formAmount.value = source && 'amount' in source ? source.amount : 1000;
}

function getList(target: ModalTarget) {
  const listMap = {
    day: days.value,
    person: people.value,
    checklist: checklists.value,
    doc: docs.value,
    budget: budgetCategories.value,
    notice: noticeRules.value
  };
  return listMap[target];
}

function saveModal() {
  const name = formName.value.trim();
  const desc = formDesc.value.trim();
  const list = getList(modal.value.target);

  if (modal.value.mode === 'delete') {
    const index = list.findIndex(item => item.id === modal.value.id);
    if (index >= 0) {
      const [removed] = list.splice(index, 1);
      success('已删除配置项', removed.name);
    }
    modal.value.show = false;
    return;
  }

  if (!name) {
    warning('请填写名称');
    return;
  }

  if (modal.value.mode === 'edit') {
    const target = list.find(item => item.id === modal.value.id);
    if (target) {
      target.name = name;
      target.desc = desc || '已更新说明';
      if ('date' in target) target.date = formDate.value;
      if ('amount' in target) target.amount = formAmount.value;
      success('配置项已保存', name);
    }
    modal.value.show = false;
    return;
  }

  const id = Date.now();
  if (modal.value.target === 'day') {
    days.value.push({
      id,
      date: formDate.value,
      day: `DAY ${days.value.length + 1}`,
      name,
      desc: desc || '新建行程日',
      tasks: ['待补充行程'],
      enabled: true,
      selected: true
    });
  } else if (modal.value.target === 'person') {
    people.value.push({ id, name, desc: desc || '新建同行人员', role: '同行者', enabled: true, selected: true });
  } else if (modal.value.target === 'checklist') {
    checklists.value.push({ id, name, desc: desc || '新建清单分组', count: 0, enabled: true, selected: true });
  } else if (modal.value.target === 'doc') {
    docs.value.push({ id, name, desc: desc || '新建证件资料', status: '待上传', enabled: true, selected: true });
  } else if (modal.value.target === 'budget') {
    budgetCategories.value.push({ id, name, desc: desc || '新建预算分类', amount: formAmount.value, enabled: true, selected: true });
  } else {
    noticeRules.value.push({ id, name, desc: desc || '新建提醒规则', channel: '飞书', enabled: true, selected: true });
  }

  modal.value.show = false;
  success('配置项已新增', name);
}

function runSync() {
  if (!syncEnabled.value) {
    warning('图册同步未启用');
    return;
  }
  syncRunning.value = true;
  window.setTimeout(() => {
    syncRunning.value = false;
    success('图册同步完成', '已同步日本旅行素材');
  }, 600);
}

function scrollList(target: 'day' | 'doc' | 'notice', direction: 'left' | 'right' | 'up' | 'down') {
  const map = {
    day: dayListRef.value,
    doc: docListRef.value,
    notice: noticeListRef.value
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
    title="旅行项目配置"
    description="管理日本旅行 2026 的基础信息、行程、同行人员、资料、图册同步与 AI 自动化。"
    :breadcrumbs="[
      { label: '首页', routeKey: 'home' },
      { label: '项目', routeKey: 'projects' },
      { label: '日本旅行 2026', routeKey: 'japan-travel' },
      { label: '管理' }
    ]"
  >
    <LifeToastHost :items="toasts" @close="removeToast" />

    <template #actions>
      <LifeGeminiTopActions
        back-text="返回详情"
        search-label="搜索配置"
        notification-label="查看提醒"
        @search="info('搜索配置', '可继续接入行程、预算和资料搜索')"
        @notification="scrollList('notice', 'down')"
        @back="backToProject"
        @create="openModal('day', 'create')"
      />
    </template>

    <div class="travel-config-page">
      <LifeGeminiProjectHero
        title="日本旅行 2026"
        description="初夏日本自由行，串联东京、京都、大阪、奈良、富士山与镰仓，把行程、资料和自动化提醒集中管理。"
        :cover-src="coverUrl"
        cover-alt="日本旅行 2026 项目封面"
        :tags="projectTags"
        :stats="projectHeroStats"
      >
        <template #actions>
          <button class="hero-action-btn" type="button" @click="success('封面已更新', '已应用当前旅行封面')">
            <SvgIcon icon="material-symbols:image-outline-rounded" />
            更换封面
          </button>
          <button class="hero-action-btn" type="button" @click="info('编辑备注', '可在这里记录旅行偏好、签证状态和特别提醒')">
            <SvgIcon icon="material-symbols:edit-note-outline-rounded" />
            编辑备注
          </button>
        </template>
      </LifeGeminiProjectHero>

      <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

      <section class="travel-bento-grid">
        <article class="travel-card basic-info span-3">
          <div class="travel-card-title">
            <div>
              <h3>基本信息</h3>
            </div>
          </div>
          <dl class="project-info-list">
            <dt>项目名称</dt><dd>日本旅行 2026</dd>
            <dt>旅行类型</dt><dd>{{ tripType }}</dd>
            <dt>出发城市</dt><dd>上海</dd>
            <dt>目的地</dt><dd>东京 / 京都 / 大阪 / 奈良 / 富士山</dd>
            <dt>预算</dt><dd>¥ {{ totalBudget.toLocaleString() }} <small>/人</small></dd>
            <dt>备注</dt><dd>初夏赏樱 + 美食 + 购物 + 温泉...</dd>
          </dl>
          <div class="card-bottom-action">
            <button class="text-action" type="button" @click="info(autoSave ? '自动保存已开启' : '自动保存已关闭')">编辑信息</button>
          </div>
        </article>

        <article class="travel-card itinerary-card span-6">
          <div class="travel-card-title">
            <div>
              <h3>行程规划</h3>
            </div>
            <button type="button" @click="backToProject">查看完整行程</button>
          </div>
          <div ref="dayListRef" class="itinerary-strip">
            <button
              v-for="(item, index) in days"
              :key="item.id"
              class="day-preview"
              :class="{ disabled: !item.enabled }"
              type="button"
              @click="selectOnly(days, item)"
            >
              <time>{{ item.date }}</time>
              <small>{{ item.day }}</small>
              <strong>{{ item.name }}</strong>
              <em v-for="task in item.tasks" :key="task">{{ task }}</em>
              <i>
                <SvgIcon icon="material-symbols:keyboard-arrow-left-rounded" @click.stop="moveDay(index, -1)" />
                <SvgIcon icon="material-symbols:keyboard-arrow-right-rounded" @click.stop="moveDay(index, 1)" />
              </i>
            </button>
            <button class="scroll-more" type="button" aria-label="横向查看更多行程" @click="scrollList('day', 'right')">
              <SvgIcon icon="material-symbols:chevron-right-rounded" />
            </button>
          </div>
          <div class="card-bottom-action">
            <button class="dash-action" type="button" @click="openModal('day', 'create')">
              <SvgIcon icon="material-symbols:add-rounded" />
              添加行程日
            </button>
          </div>
        </article>

        <article class="travel-card companion-card span-3">
          <div class="travel-card-title">
            <div>
              <h3>同行人员</h3>
            </div>
            <button type="button" @click="openModal('person', 'create')">管理人员</button>
          </div>
          <div class="companion-list">
            <button v-for="(item, index) in people" :key="item.id" type="button" :class="{ disabled: !item.enabled }" @click="selectOnly(people, item)">
              <img :src="companionAvatars[index]" :alt="item.name" />
              <strong>{{ item.name }}</strong>
              <span>{{ item.enabled ? item.role : '已停用' }}</span>
            </button>
          </div>
          <div class="card-bottom-action">
            <button class="text-action" type="button" @click="openModal('person', 'create')">
              <SvgIcon icon="material-symbols:add-rounded" />
              添加同行人员
            </button>
          </div>
        </article>

        <article class="travel-card checklist-card span-3">
          <div class="travel-card-title">
            <div>
              <h3>清单模板</h3>
            </div>
            <button type="button" @click="openModal('checklist', 'create')">管理模板</button>
          </div>
          <div class="checklist-list">
            <button v-for="(item, index) in checklists" :key="item.id" type="button" :class="{ disabled: !item.enabled }" @click="toggleSelect(item)">
              <i><SvgIcon :icon="checklistIcons[index]" /></i>
              <strong>{{ item.name }}</strong>
              <span>{{ item.count }} 项</span>
            </button>
          </div>
          <div class="card-bottom-action">
            <button class="text-action" type="button" @click="openModal('checklist', 'create')">
              <SvgIcon icon="material-symbols:add-rounded" />
              新建清单模板
            </button>
          </div>
        </article>

        <article class="travel-card asset-card span-6">
          <div class="travel-card-title">
            <div>
              <h3>资产资料</h3>
            </div>
            <button type="button" @click="scrollList('doc', 'down')">管理资料</button>
          </div>
          <div ref="docListRef" class="asset-grid">
            <button v-for="(item, index) in docs" :key="item.id" type="button" :class="{ disabled: !item.enabled }" @click="toggleSelect(item)">
              <i :class="assetIcons[index].tone"><SvgIcon :icon="assetIcons[index].icon" /></i>
              <strong>{{ item.name }}</strong>
              <span>{{ item.status }}</span>
            </button>
            <button class="upload-card" type="button" @click="openModal('doc', 'create')">
              <SvgIcon icon="material-symbols:add-rounded" />
              <strong>上传资料</strong>
            </button>
          </div>
        </article>

        <article class="travel-card gallery-card span-3">
          <div class="travel-card-title">
            <div>
              <h3>图册同步 <small>(阿里云 OSS)</small></h3>
            </div>
            <button type="button" @click="info('查看图册', '可进入日本旅行 2026 图册')">查看图册</button>
          </div>
          <div class="gallery-summary">
            <img :src="galleryCoverUrl" alt="日本旅行图册封面" />
            <div>
              <strong>日本旅行 2026 图册</strong>
              <p>已同步 328 张 / 12.6 GB</p>
              <span><SvgIcon icon="material-symbols:check-circle-outline-rounded" />同步成功 <small>今天 10:30</small></span>
            </div>
          </div>
          <div class="storage-row">
            <p><span>存储使用: <b>12.6 GB</b> / 100 GB</span><em>12.6%</em></p>
            <div><i style="width: 12.6%"></i></div>
          </div>
          <div class="card-bottom-action">
            <div class="split-actions">
              <button type="button" @click="runSync">
                <SvgIcon icon="material-symbols:sync-rounded" />
                {{ syncRunning ? '同步中...' : '立即同步' }}
              </button>
              <button type="button" @click="syncEnabled = !syncEnabled; info(syncEnabled ? '图册同步已启用' : '图册同步已停用')">
                <SvgIcon icon="material-symbols:settings-outline-rounded" />
                同步设置
              </button>
            </div>
          </div>
        </article>

        <article class="travel-card notice-card span-5">
          <div class="travel-card-title">
            <div>
              <i class="bell-icon"><SvgIcon icon="material-symbols:notifications-active-outline-rounded" /></i>
              <h3>提醒与通知规则</h3>
            </div>
            <button type="button" @click="openModal('notice', 'create')">管理规则</button>
          </div>
          <div ref="noticeListRef" class="notice-table">
            <button v-for="item in noticeRules" :key="item.id" type="button" :class="{ disabled: !item.enabled }" @click="selectOnly(noticeRules, item)">
              <strong><SvgIcon icon="material-symbols:check-rounded" />{{ item.name }}</strong>
              <span>{{ item.desc }}</span>
              <em>{{ item.channel }}</em>
            </button>
          </div>
          <div class="card-bottom-action">
            <button class="text-action" type="button" @click="openModal('notice', 'create')">
              <SvgIcon icon="material-symbols:add-rounded" />
              添加规则
            </button>
          </div>
        </article>

        <article class="travel-card ai-card span-7">
          <div class="travel-card-title">
            <div>
              <i class="spark-icon"><SvgIcon icon="material-symbols:auto-awesome-outline-rounded" /></i>
              <h3>AI 自动化设置</h3>
            </div>
            <button type="button" @click="info('AI 规则管理', '这里可进入完整 AI 自动化规则')">管理 AI 规则</button>
          </div>
          <div class="ai-grid">
            <section v-for="item in aiRules" :key="item.name" :class="{ disabled: !item.enabled }">
              <strong>{{ item.name }}</strong>
              <p>{{ item.desc }}</p>
              <button class="toggle-switch" :class="{ active: item.enabled }" type="button" @click="item.enabled = !item.enabled; info(item.enabled ? 'AI 规则已启用' : 'AI 规则已停用', item.name)">
                <i></i>
              </button>
              <span>{{ item.enabled ? '已开启' : '已关闭' }}</span>
            </section>
            <section class="more-ai" @click="info('更多 AI 能力', '持续解锁中')">
              <strong>更多 AI 能力</strong>
              <p>持续解锁中...</p>
              <SvgIcon icon="material-symbols:chevron-right-rounded" />
            </section>
          </div>
        </article>
      </section>

      <footer class="integration-bar">
        <div class="integration-title">
          <strong>集成与通知</strong>
          <span>将提醒与信息推送到你常用的平台</span>
        </div>
        <button v-for="item in integrations" :key="item.name" type="button" @click="info(`${item.name} 设置`, item.status)">
          <i :class="item.tone"><SvgIcon :icon="item.icon" /></i>
          <strong>{{ item.name }}</strong>
          <em>{{ item.status }}</em>
        </button>
        <button class="add-integration" type="button" @click="info('添加集成', '后续可接入更多通知渠道')">
          <SvgIcon icon="material-symbols:add-rounded" />
          添加集成
        </button>
        <div class="footer-actions">
          <button type="button" @click="resetConfig">取消</button>
          <button type="button" @click="saveConfig">保存配置</button>
        </div>
      </footer>
    </div>

    <LifeModal
      v-model:show="modal.show"
      :title="modalTitle"
      :description="modalDescription"
      :confirm-text="modalConfirmText"
      cancel-text="取消"
      :tone="modal.mode === 'delete' ? 'danger' : 'default'"
      @confirm="saveModal"
    >
      <div v-if="modal.mode === 'delete'" class="delete-confirm">
        <SvgIcon icon="material-symbols:warning-outline-rounded" />
        <p>确认删除「{{ formName }}」？删除后会从当前演示列表中移除。</p>
      </div>
      <div v-else class="manage-form">
        <label>
          <span>名称</span>
          <input v-model="formName" maxlength="24" placeholder="请输入名称" />
        </label>
        <label>
          <span>说明</span>
          <input v-model="formDesc" maxlength="48" placeholder="请输入说明" />
        </label>
        <label v-if="modal.target === 'day'">
          <span>日期</span>
          <input v-model="formDate" maxlength="16" placeholder="例如：5/24 周日" />
        </label>
        <label v-if="modal.target === 'budget'">
          <span>预算金额</span>
          <input v-model.number="formAmount" min="0" type="number" />
        </label>
      </div>
    </LifeModal>
  </LifeGeminiShell>
</template>

<style scoped>
.travel-config-page {
  color: #1e293b;
}

.travel-config-page :deep(.lm-card > div:first-child:not(.lm-card-title)) {
  display: none;
}

.hero-action-btn {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  min-height: 40px;
  padding: 0 14px;
  border: 1px solid #e2e8f0;
  border-radius: 12px;
  background: #fff;
  color: #475569;
  font-size: 14px;
  font-weight: 650;
  cursor: pointer;
}

.hero-action-btn:hover {
  background: #f8fafc;
  color: #4f46e5;
}

.integration-bar {
  display: grid;
  grid-template-columns: minmax(180px, 230px) repeat(4, max-content) minmax(210px, 1fr);
  gap: 12px;
  align-items: center;
  margin-top: 24px;
  padding: 20px;
  border: 1px solid #e8edf5;
  border-radius: 26px;
  background: rgba(255, 255, 255, 0.96);
  box-shadow: 0 14px 36px rgba(15, 23, 42, 0.08);
}

.integration-title {
  display: grid;
  gap: 8px;
}

.integration-title strong {
  color: #172033;
  font-size: 16px;
  font-weight: 800;
}

.integration-title span {
  color: #64748b;
  font-size: 13px;
}

.integration-bar > button {
  display: inline-flex;
  align-items: center;
  gap: 10px;
  min-height: 46px;
  padding: 0 16px;
  border: 1px solid #e8edf5;
  border-radius: 8px;
  background: #f8fafc;
  color: #334155;
  font-size: 14px;
  font-weight: 800;
  white-space: nowrap;
}

.integration-bar > button i {
  display: inline-grid;
  width: 31px;
  height: 31px;
  place-items: center;
  border-radius: 8px;
  font-style: normal;
}

.integration-bar > button i.blue {
  background: #eef4ff;
  color: #2563eb;
}

.integration-bar > button i.slate {
  background: #f1f5f9;
  color: #334155;
}

.integration-bar > button i.pink {
  background: #fdf2f8;
  color: #db2777;
}

.integration-bar > button em {
  color: #10b981;
  font-size: 13px;
  font-style: normal;
  font-weight: 800;
}

.integration-bar .add-integration {
  border-style: dashed;
  background: #fff;
  color: #64748b;
  font-weight: 700;
}

.footer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
}

.footer-actions button {
  min-width: 112px;
  min-height: 48px;
  border: 1px solid #dbe3ef;
  border-radius: 8px;
  background: #fff;
  color: #334155;
  font-size: 15px;
  font-weight: 800;
}

.footer-actions button:last-child {
  border-color: transparent;
  background: #6366f1;
  color: #fff;
  box-shadow: 0 10px 20px rgba(99, 102, 241, 0.24);
}

.round-only {
  width: 36px;
  padding: 0;
  border-radius: 50%;
}

.manage-hero {
  position: relative;
  display: grid;
  grid-template-columns: 190px minmax(0, 1fr) 130px;
  gap: 19px;
  align-items: center;
  min-height: 119px;
  padding: 13px;
  overflow: hidden;
}

.manage-hero::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  width: 145px;
  height: 100%;
  background: radial-gradient(circle at 80% 10%, rgba(255, 200, 220, 0.7), transparent 45%);
  pointer-events: none;
}

.manage-cover {
  height: 90px;
  border-radius: 7px;
}

.manage-hero h2 {
  margin: 0 0 18px;
  font-size: 18px;
}

.manage-meta {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.manage-meta p {
  display: grid;
  grid-template-columns: 20px minmax(0, 1fr);
  gap: 7px;
  margin: 0;
  color: #6b7283;
  font-size: 10px;
}

.manage-meta b,
.manage-meta span {
  grid-column: 2;
}

.manage-meta b {
  color: #161c2b;
  font-size: 12px;
}

.project-progress {
  position: relative;
  z-index: 1;
  text-align: center;
}

.project-progress div {
  width: 64px;
  height: 64px;
  display: grid;
  place-items: center;
  margin: auto;
  border-radius: 50%;
  background:
    radial-gradient(circle, #fff 0 58%, transparent 59%),
    conic-gradient(#765ce8 0 var(--project-progress, 0%), #eef0f7 var(--project-progress, 0%));
  color: #161c2b;
  font-weight: 800;
}

.project-progress span {
  color: #7f8696;
  font-size: 10px;
}

.edit-plan {
  position: absolute;
  right: 8px;
  top: 48px;
  z-index: 2;
  padding: 5px 9px;
  border: 0;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: #555d70;
  font-size: 10px;
  cursor: pointer;
}

.manage-grid {
  grid-template-columns: 1.1fr 2.3fr 1.35fr;
  margin-top: 12px;
}

.lm-card-title h2 span {
  padding: 2px 5px;
  border-radius: 5px;
  background: #f0ecff;
  color: #765ce8;
  font-size: 9px;
}

.basic-info dl {
  display: grid;
  grid-template-columns: 70px minmax(0, 1fr);
  gap: 10px;
  margin: 0 0 14px;
  font-size: 14px;
}

.basic-info dt {
  color: #8b91a1;
}

.basic-info dd {
  margin: 0;
  color: #31384b;
}

.basic-info select {
  width: 100%;
  height: 26px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #31384b;
  font-size: 11px;
}

.manage-segment {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 4px;
  margin-bottom: 14px;
  padding: 4px;
  border-radius: 8px;
  background: #f6f7fc;
}

.manage-segment button,
.card-tools button,
.batch-bar button,
.inline-actions button,
.day-cards footer button,
.budget-card > button,
.template-card > button,
.people-list button,
.notice-list button {
  border: 0;
  cursor: pointer;
}

.manage-segment button {
  min-height: 28px;
  border-radius: 7px;
  background: transparent;
  color: #7d8495;
  font-size: 10px;
}

.manage-segment button.active {
  background: #fff;
  color: #765ce8;
  box-shadow: 0 8px 18px rgba(93, 78, 180, 0.08);
}

.card-tools,
.inline-actions {
  display: flex;
  gap: 6px;
}

.card-tools button,
.day-cards footer button {
  display: inline-grid;
  width: 24px;
  height: 24px;
  place-items: center;
  border-radius: 6px;
  background: #f6f7fc;
  color: #765ce8;
}

.batch-bar {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 10px;
  color: #8b91a1;
  font-size: 10px;
}

.batch-bar button,
.inline-actions button {
  padding: 5px 9px;
  border-radius: 999px;
  background: #f6f7fc;
  color: #765ce8;
  font-size: 10px;
}

.day-cards {
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: minmax(154px, 1fr);
  gap: 10px;
  overflow-x: auto;
  padding-bottom: 2px;
}

.day-cards div {
  min-height: 101px;
  padding: 10px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
}

.day-cards div.selected {
  border-color: rgba(118, 92, 232, 0.32);
  background: #f8f6ff;
}

.day-cards div.disabled,
.people-list button.disabled,
.template-card > button.disabled,
.asset-config button.disabled,
.notice-list button.disabled,
.automation-card section.disabled,
.budget-card > button.disabled {
  opacity: 0.58;
}

.day-cards time,
.day-cards small {
  display: block;
  color: #7f8696;
  font-size: 9px;
}

.day-cards strong {
  display: block;
  margin: 8px 0;
  color: #202636;
  font-size: 12px;
}

.day-cards p {
  margin: 4px 0;
  color: #596174;
  font-size: 9px;
}

.day-cards footer {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 10px;
}

.add-line {
  display: block;
  margin: 13px auto 0;
  border: 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.people-list button {
  display: grid;
  grid-template-columns: 22px minmax(0, 1fr) auto;
  width: 100%;
  align-items: center;
  gap: 8px;
  margin: 9px 0;
  padding: 0;
  background: transparent;
  color: #333a4d;
  font-size: 11px;
  text-align: left;
}

.people-list .lm-avatar {
  width: 21px;
  height: 21px;
}

.people-list em {
  padding: 2px 6px;
  border-radius: 5px;
  background: #eaf9f2;
  color: #31a978;
  font-size: 9px;
  font-style: normal;
}

.people-list button.selected,
.template-card > button.selected,
.notice-list button.selected,
.budget-card > button.selected {
  color: #765ce8;
}

.template-card > button,
.notice-list button,
.budget-card > button {
  display: flex;
  justify-content: space-between;
  width: 100%;
  margin: 10px 0;
  padding: 0;
  background: transparent;
  color: #41495c;
  font-size: 11px;
  text-align: left;
}

.template-card b {
  padding: 2px 7px;
  border-radius: 5px;
  background: #f0ecff;
  color: #765ce8;
  font-size: 10px;
}

.budget-card {
  grid-column: span 1;
}

.budget-card > button {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto auto;
  align-items: center;
  gap: 8px;
}

.budget-card b {
  color: #202636;
}

.budget-card em {
  color: #7f8696;
  font-size: 10px;
  font-style: normal;
}

.asset-config {
  grid-column: span 1;
}

.asset-config .doc-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 9px;
  max-height: 182px;
  overflow: auto;
  padding-right: 2px;
}

.asset-config .doc-grid button {
  min-height: 52px;
  display: grid;
  place-items: center;
  gap: 4px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #363e51;
  font-size: 10px;
  cursor: pointer;
}

.asset-config .doc-grid button.selected {
  border-color: rgba(118, 92, 232, 0.32);
  background: #f8f6ff;
  color: #765ce8;
}

.asset-config .doc-grid span {
  color: #8b91a1;
  font-size: 9px;
}

.oss-inner {
  display: grid;
  grid-template-columns: 96px minmax(0, 1fr);
  gap: 12px;
  margin-bottom: 11px;
}

.oss-cover {
  height: 62px;
  border-radius: 7px;
}

.oss-inner strong {
  color: #202636;
  font-size: 12px;
}

.oss-inner p {
  margin: 6px 0;
  color: #7f8696;
  font-size: 10px;
}

.oss-card .lm-purple-btn,
.oss-card .lm-plain-btn {
  height: 27px;
  margin-top: 10px;
}

.notice-card {
  grid-column: span 1;
}

.notice-list {
  max-height: 150px;
  overflow: auto;
}

.notice-list button {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 4px 8px;
  padding: 8px;
  border-radius: 7px;
  background: #fafafe;
}

.notice-list strong {
  color: #202636;
  font-size: 11px;
}

.notice-list span,
.notice-list em {
  color: #7f8696;
  font-style: normal;
}

.notice-list em {
  grid-row: span 2;
  align-self: center;
  font-size: 10px;
}

.automation-card {
  grid-column: span 2;
}

.automation-card > div:last-child {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 10px;
}

.automation-card section {
  min-height: 80px;
  padding: 11px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
}

.automation-card strong {
  color: #202636;
  font-size: 11px;
}

.automation-card p {
  min-height: 28px;
  margin: 7px 0 9px;
  color: #7f8696;
  font-size: 9px;
}

.automation-card .lm-switch {
  position: relative;
  display: inline-flex;
  width: 34px;
  height: 18px;
  padding: 2px;
  border: 0;
  border-radius: 999px;
  background: #765ce8;
  cursor: pointer;
}

.automation-card .lm-switch i {
  width: 14px;
  height: 14px;
  transform: translateX(16px);
  border-radius: 50%;
  background: #fff;
  transition: transform 0.18s ease;
}

.automation-card .lm-switch.off {
  background: #d8dce8;
}

.automation-card .lm-switch.off i {
  transform: translateX(0);
}

.lm-bottom-bar span {
  margin-right: auto;
  color: #58ad86;
  font-size: 11px;
}

.delete-confirm {
  display: grid;
  grid-template-columns: 36px minmax(0, 1fr);
  gap: 12px;
  align-items: start;
  color: #596174;
}

.delete-confirm .svg-icon {
  color: #e66591;
  font-size: 28px;
}

.delete-confirm p {
  margin: 0;
  font-size: 13px;
  line-height: 1.7;
}

.manage-form {
  display: grid;
  gap: 12px;
}

.manage-form label {
  display: grid;
  gap: 6px;
}

.manage-form span {
  color: #7f8696;
  font-size: 12px;
}

.manage-form input {
  width: 100%;
  height: 36px;
  border: 1px solid #e6e9f3;
  border-radius: 8px;
  background: #fff;
  color: #202636;
  font-size: 13px;
}

@media (max-width: 1180px) {
  .manage-hero,
  .manage-grid,
  .manage-meta,
  .automation-card > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .automation-card {
    grid-column: span 1;
  }
}

.travel-bento-grid {
  display: grid;
  grid-template-columns: repeat(12, minmax(0, 1fr));
  gap: 20px;
  align-items: stretch;
}

.travel-card {
  display: flex;
  flex-direction: column;
  min-height: 220px;
  padding: 20px;
  border: 1px solid #eef2f7;
  border-radius: 16px;
  background: #fff;
  box-shadow: 0 4px 20px -8px rgba(15, 23, 42, 0.12);
  overflow: hidden;
}

.travel-card button {
  border: 0;
  font: inherit;
  cursor: pointer;
}

.span-3 {
  grid-column: span 3;
}

.span-4 {
  grid-column: span 4;
}

.span-5,
.notice-card.span-5 {
  grid-column: span 5;
}

.span-6 {
  grid-column: span 6;
}

.span-7 {
  grid-column: span 7;
}

.travel-card-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-bottom: 18px;
}

.travel-card-title > div {
  display: flex;
  align-items: center;
  min-width: 0;
  gap: 8px;
}

.travel-card-title h3 {
  margin: 0;
  color: #111827;
  font-size: 18px;
  font-weight: 800;
}

.travel-card-title small {
  color: #9ca3af;
  font-size: 12px;
  font-weight: 500;
}

.bell-icon,
.spark-icon {
  display: inline-grid;
  width: 24px;
  height: 24px;
  place-items: center;
  flex: 0 0 24px;
  border-radius: 6px;
  background: #eef2ff;
  color: #4f46e5;
  font-size: 12px;
  font-weight: 800;
}

.bell-icon {
  background: #fff1f2;
  color: #f43f5e;
}

.spark-icon {
  background: #eef2ff;
  color: #4f46e5;
}

.travel-card-title > button,
.text-action {
  display: inline-flex;
  align-items: center;
  gap: 4px;
  background: transparent;
  color: #4f46e5;
  font-size: 14px;
  font-weight: 700;
}

.card-bottom-action {
  display: flex;
  justify-content: center;
  margin-top: auto;
  padding-top: 16px;
  border-top: 1px solid #eef2f7;
}

.card-bottom-action .text-action {
  justify-content: center;
}

.project-info-list {
  display: grid;
  grid-template-columns: 92px minmax(0, 1fr);
  gap: 20px 18px;
  margin: 0 0 22px;
  color: #334155;
  font-size: 18px;
  line-height: 1.55;
}

.project-info-list dt {
  color: #94a3b8;
  font-weight: 750;
}

.project-info-list dd {
  margin: 0;
  color: #243044;
  font-weight: 750;
}

.project-info-list small {
  color: #94a3b8;
  font-weight: 550;
}

.itinerary-strip {
  position: relative;
  display: grid;
  grid-auto-flow: column;
  grid-auto-columns: minmax(132px, 1fr);
  gap: 12px;
  overflow-x: auto;
  padding: 0 34px 8px 0;
}

.day-preview {
  min-height: 156px;
  padding: 14px;
  border: 1px solid #eef2f7;
  border-radius: 14px;
  background: #f8fafc;
  color: #111827;
  text-align: left;
}

.day-preview.disabled,
.companion-list button.disabled,
.checklist-list button.disabled,
.asset-grid button.disabled,
.notice-table button.disabled,
.ai-grid section.disabled {
  opacity: 0.58;
}

.day-preview time,
.day-preview small,
.day-preview em {
  display: block;
  color: #6b7280;
  font-size: 13px;
  font-style: normal;
}

.day-preview small {
  margin: 4px 0 10px;
  color: #9ca3af;
  font-weight: 800;
}

.day-preview strong {
  display: block;
  margin-bottom: 12px;
  color: #111827;
  font-size: 16px;
}

.day-preview em {
  position: relative;
  margin-top: 6px;
  padding-left: 10px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.day-preview em::before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background: #d1d5db;
}

.day-preview i {
  display: flex;
  gap: 6px;
  margin-top: 12px;
  color: #4f46e5;
  font-style: normal;
}

.scroll-more {
  position: absolute;
  right: 3px;
  top: 50%;
  display: grid;
  width: 28px;
  height: 28px;
  place-items: center;
  transform: translateY(-50%);
  border-radius: 50%;
  background: #fff;
  color: #6b7280;
  box-shadow: 0 8px 24px rgba(15, 23, 42, 0.14);
}

.dash-action {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  width: 100%;
  min-height: 38px;
  border: 1px dashed #dbe1ea;
  border-radius: 12px;
  background: #fff;
  color: #4f46e5;
  font-size: 14px;
  font-weight: 700;
}

.companion-list,
.checklist-list {
  display: grid;
  gap: 14px;
}

.companion-list button,
.checklist-list button {
  display: grid;
  grid-template-columns: 32px minmax(0, 1fr) auto;
  align-items: center;
  gap: 10px;
  background: transparent;
  color: #111827;
  text-align: left;
}

.companion-list img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  border: 1px solid #e5e7eb;
  background: #f3f4f6;
}

.companion-list strong,
.checklist-list strong {
  min-width: 0;
  overflow: hidden;
  color: #111827;
  font-size: 15px;
  font-weight: 700;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.companion-list span,
.checklist-list span {
  padding: 2px 8px;
  border-radius: 999px;
  background: #ecfdf5;
  color: #059669;
  font-size: 13px;
  font-weight: 700;
}

.checklist-list i {
  display: grid;
  width: 28px;
  height: 28px;
  place-items: center;
  border: 1px solid #eef2f7;
  border-radius: 8px;
  background: #f8fafc;
  color: #9ca3af;
  font-style: normal;
}

.asset-grid {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.asset-grid button {
  display: grid;
  min-height: 94px;
  place-items: center;
  gap: 5px;
  padding: 12px 8px;
  border: 1px solid #eef2f7;
  border-radius: 14px;
  background: #fff;
  color: #374151;
  text-align: center;
}

.asset-grid i {
  display: grid;
  width: 34px;
  height: 34px;
  place-items: center;
  border-radius: 9px;
  font-style: normal;
}

.asset-grid i.red {
  background: #fef2f2;
  color: #ef4444;
}

.asset-grid i.blue {
  background: #eff6ff;
  color: #3b82f6;
}

.asset-grid i.purple {
  background: #faf5ff;
  color: #9333ea;
}

.asset-grid i.orange {
  background: #fff7ed;
  color: #f97316;
}

.asset-grid i.indigo {
  background: #eef2ff;
  color: #4f46e5;
}

.asset-grid i.emerald {
  background: #ecfdf5;
  color: #059669;
}

.asset-grid i.slate {
  background: #f1f5f9;
  color: #64748b;
}

.asset-grid strong {
  color: #374151;
  font-size: 14px;
  font-weight: 800;
}

.asset-grid span {
  color: #9ca3af;
  font-size: 12px;
}

.asset-grid .upload-card {
  border-style: dashed;
  color: #4f46e5;
}

.gallery-summary {
  display: grid;
  grid-template-columns: 64px minmax(0, 1fr);
  gap: 12px;
  margin-bottom: 16px;
  padding: 8px;
  border: 1px solid #f3f4f6;
  border-radius: 14px;
  background: #f9fafb;
}

.gallery-summary img {
  width: 64px;
  height: 52px;
  border-radius: 10px;
  object-fit: cover;
}

.gallery-summary strong {
  display: block;
  color: #111827;
  font-size: 14px;
}

.gallery-summary p {
  margin: 4px 0;
  color: #6b7280;
  font-size: 13px;
}

.gallery-summary span {
  display: flex;
  align-items: center;
  gap: 4px;
  color: #059669;
  font-size: 12px;
}

.gallery-summary small {
  color: #9ca3af;
}

.storage-row p {
  display: flex;
  justify-content: space-between;
  margin: 0 0 7px;
  color: #6b7280;
  font-size: 13px;
}

.storage-row b {
  color: #374151;
}

.storage-row em {
  color: #9ca3af;
  font-style: normal;
}

.storage-row div {
  height: 6px;
  overflow: hidden;
  border-radius: 999px;
  background: #f3f4f6;
}

.storage-row i {
  display: block;
  height: 100%;
  border-radius: inherit;
  background: #6366f1;
}

.split-actions {
  display: flex;
  width: 100%;
  gap: 8px;
}

.split-actions button {
  display: inline-flex;
  flex: 1;
  align-items: center;
  justify-content: center;
  gap: 6px;
  min-height: 32px;
  border-radius: 10px;
  background: #6366f1;
  color: #fff;
  font-size: 13px;
  font-weight: 700;
}

.split-actions button + button {
  border: 1px solid #e5e7eb;
  background: #fff;
  color: #374151;
}

.notice-table {
  display: grid;
  gap: 2px;
}

.notice-table button {
  display: grid;
  grid-template-columns: 1.1fr 1.25fr 1fr;
  gap: 10px;
  align-items: center;
  min-height: 38px;
  padding: 0 8px;
  border-bottom: 1px solid #f3f4f6;
  border-radius: 8px;
  background: transparent;
  color: #6b7280;
  text-align: left;
}

.notice-table button:hover {
  background: #f9fafb;
}

.notice-table strong {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #374151;
  font-size: 14px;
}

.notice-table strong .svg-icon {
  color: #6366f1;
}

.notice-table span,
.notice-table em {
  font-size: 13px;
  font-style: normal;
}

.notice-table em {
  text-align: right;
}

.ai-card {
  position: relative;
}

.ai-card::after {
  content: '';
  position: absolute;
  top: -56px;
  right: -36px;
  width: 132px;
  height: 132px;
  border-radius: 50%;
  background: rgba(99, 102, 241, 0.09);
  pointer-events: none;
}

.ai-grid {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 12px;
}

.ai-grid section {
  min-height: 118px;
  padding: 12px;
  border: 1px solid #eef2f7;
  border-radius: 14px;
  background: #f8fafc;
}

.ai-grid strong {
  display: block;
  color: #1f2937;
  font-size: 14px;
  font-weight: 800;
}

.ai-grid p {
  min-height: 34px;
  margin: 8px 0 12px;
  color: #6b7280;
  font-size: 12px;
  line-height: 1.45;
}

.toggle-switch {
  display: inline-flex;
  width: 34px;
  height: 18px;
  padding: 2px;
  vertical-align: middle;
  border-radius: 999px;
  background: #d1d5db;
}

.toggle-switch i {
  width: 14px;
  height: 14px;
  border-radius: 50%;
  background: #fff;
  transition: transform 0.18s ease;
}

.toggle-switch.active {
  background: #10b981;
}

.toggle-switch.active i {
  transform: translateX(16px);
}

.ai-grid section > span {
  margin-left: 8px;
  color: #10b981;
  font-size: 12px;
  font-weight: 700;
}

.ai-grid section.disabled > span {
  color: #9ca3af;
}

.more-ai {
  display: grid;
  place-items: center;
  text-align: center;
  cursor: pointer;
}

.more-ai .svg-icon {
  color: #9ca3af;
  font-size: 20px;
}

@media (max-width: 1280px) {
  .travel-bento-grid {
    grid-template-columns: repeat(6, minmax(0, 1fr));
  }

  .integration-bar {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .integration-title,
  .footer-actions {
    grid-column: 1 / -1;
  }

  .span-3,
  .span-4,
  .span-5,
  .span-6,
  .span-7,
  .notice-card.span-5 {
    grid-column: span 6;
  }

  .asset-grid,
  .ai-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 760px) {
  .travel-bento-grid,
  .asset-grid,
  .ai-grid,
  .integration-bar {
    grid-template-columns: 1fr;
  }

  .span-3,
  .span-4,
  .span-5,
  .span-6,
  .span-7,
  .notice-card.span-5 {
    grid-column: 1;
  }

  .notice-table button {
    grid-template-columns: 1fr;
    padding: 8px;
  }

  .notice-table em {
    text-align: left;
  }

  .footer-actions {
    flex-direction: column;
  }

  .footer-actions button {
    width: 100%;
  }
}
</style>

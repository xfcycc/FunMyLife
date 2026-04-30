<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
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

const projectProgress = computed(() => Math.round(((days.value.filter(item => item.enabled).length + checklists.value.filter(item => item.enabled).length) / (days.value.length + checklists.value.length)) * 100));
const totalBudget = computed(() => budgetCategories.value.filter(item => item.enabled).reduce((sum, item) => sum + item.amount, 0));
const selectedDayCount = computed(() => days.value.filter(item => item.selected).length);
const selectedDocCount = computed(() => docs.value.filter(item => item.selected).length);
const selectedChecklistCount = computed(() => checklists.value.filter(item => item.selected).length);
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
  <LifeAppShell active="管理" avatar="default">
    <LifeToastHost :items="toasts" @close="removeToast" />

    <header class="manage-top lm-topbar">
      <div class="lm-title">
        <p>项目　/　日本旅行 2026　/　管理</p>
        <button class="lm-plain-btn" type="button" @click="backToProject">← 返回项目详情</button>
        <h1>项目管理 <span>🌸</span></h1>
      </div>
      <div class="lm-actions">
        <button class="lm-icon-btn" type="button" aria-label="搜索配置" @click="info('搜索配置', '可继续接入行程、预算和资料搜索')">
          <SvgIcon icon="material-symbols:search-rounded" />
        </button>
        <button class="lm-icon-btn" type="button" aria-label="查看提醒" @click="scrollList('notice', 'down')">
          <SvgIcon icon="material-symbols:notifications-outline-rounded" />
        </button>
        <button class="lm-purple-btn round-only" type="button" @click="openModal('day', 'create')">
          <SvgIcon icon="material-symbols:add-rounded" />
        </button>
      </div>
    </header>

    <section class="manage-hero lm-card">
      <div class="manage-cover lm-cover japan"></div>
      <div>
        <h2>日本旅行 2026 <span class="lm-tag" :class="projectEnabled ? 'green' : 'amber'">{{ projectEnabled ? '进行中' : '已停用' }}</span></h2>
        <div class="manage-meta">
          <p><SvgIcon icon="material-symbols:calendar-month-outline-rounded" />旅行日期 <b>2026.05.20 - 2026.06.02</b><span>共 14 天</span></p>
          <p><SvgIcon icon="material-symbols:groups-outline-rounded" />同行人数 <b>{{ people.filter(item => item.enabled).length }} 人</b><span>已启用同行者</span></p>
          <p><SvgIcon icon="material-symbols:location-on-outline-rounded" />目的地 <b>东京 · 京都 · 大阪 · 奈良</b><span>富士山 · 镰仓</span></p>
        </div>
      </div>
      <div class="project-progress">
        <div :style="{ '--project-progress': `${projectProgress}%` }">{{ projectProgress }}%</div>
        <span>项目进度</span>
      </div>
      <button class="edit-plan" type="button" @click="success('封面已更新', '已应用当前旅行封面')">编辑封面</button>
    </section>

    <nav class="lm-tabs manage-tabs">
      <button v-for="item in tabs" :key="item" :class="{ active: item === activeTab }" type="button" @click="switchTab(item)">{{ item }}</button>
    </nav>

    <section class="lm-grid manage-grid">
      <article class="lm-card basic-info">
        <div class="lm-card-title">
          <h2><span>01</span>基础信息</h2>
          <button type="button" @click="toggleProject">{{ projectEnabled ? '停用' : '启用' }}</button>
        </div>
        <div class="manage-segment">
          <button v-for="item in configSections" :key="item" :class="{ active: item === activeSection }" type="button" @click="switchSection(item)">
            {{ item }}
          </button>
        </div>
        <dl>
          <dt>项目名称</dt><dd>日本旅行 2026</dd>
          <dt>旅行类型</dt>
          <dd>
            <select v-model="tripType" @change="info('旅行类型已切换', tripType)">
              <option v-for="item in tripTypes" :key="item">{{ item }}</option>
            </select>
          </dd>
          <dt>出发城市</dt><dd>上海</dd>
          <dt>目的地</dt><dd>东京 / 京都 / 大阪 / 奈良 / 富士山</dd>
          <dt>预算</dt><dd>￥{{ totalBudget.toLocaleString() }} <span class="lm-tag">个人</span></dd>
          <dt>备注</dt><dd>初夏赏樱 · 美食 · 购物 · 温泉</dd>
        </dl>
        <button class="lm-plain-btn" type="button" @click="info(autoSave ? '自动保存已开启' : '自动保存已关闭')">编辑信息</button>
      </article>

      <article class="lm-card itinerary-config">
        <div class="lm-card-title">
          <h2><span>02</span>行程规划</h2>
          <div class="card-tools">
            <button type="button" @click="scrollList('day', 'left')"><SvgIcon icon="material-symbols:keyboard-arrow-left-rounded" /></button>
            <button type="button" @click="scrollList('day', 'right')"><SvgIcon icon="material-symbols:keyboard-arrow-right-rounded" /></button>
          </div>
        </div>
        <div class="batch-bar">
          <span>已选 {{ selectedDayCount }}</span>
          <button type="button" @click="openModal('day', 'create')">新增行程日</button>
        </div>
        <div ref="dayListRef" class="day-cards">
          <div v-for="(item, index) in days" :key="item.id" :class="{ selected: item.selected, disabled: !item.enabled }">
            <time>{{ item.date }}</time>
            <small>{{ item.day }}</small>
            <strong>{{ item.name }}</strong>
            <p v-for="task in item.tasks" :key="task">• {{ task }}</p>
            <footer>
              <button type="button" @click="selectOnly(days, item)"><SvgIcon icon="material-symbols:ads-click-rounded" /></button>
              <button type="button" @click="moveDay(index, -1)"><SvgIcon icon="material-symbols:keyboard-arrow-left-rounded" /></button>
              <button type="button" @click="moveDay(index, 1)"><SvgIcon icon="material-symbols:keyboard-arrow-right-rounded" /></button>
              <button type="button" @click="toggleItem(item, '行程日')">
                <SvgIcon :icon="item.enabled ? 'material-symbols:toggle-on-outline-rounded' : 'material-symbols:toggle-off-outline-rounded'" />
              </button>
              <button type="button" @click="openModal('day', 'edit', item)"><SvgIcon icon="material-symbols:edit-outline-rounded" /></button>
              <button type="button" @click="openModal('day', 'delete', item)"><SvgIcon icon="material-symbols:delete-outline-rounded" /></button>
            </footer>
          </div>
        </div>
      </article>

      <article class="lm-card people-card">
        <div class="lm-card-title"><h2><span>03</span>同行人员</h2><button type="button" @click="openModal('person', 'create')">添加</button></div>
        <div class="people-list">
          <button v-for="item in people" :key="item.id" type="button" :class="{ selected: item.selected, disabled: !item.enabled }" @click="selectOnly(people, item)">
            <span class="lm-avatar"></span>{{ item.name }} <em>{{ item.enabled ? item.role : '已停用' }}</em>
          </button>
        </div>
        <div class="inline-actions">
          <button type="button" @click="people.find(item => item.selected) && toggleItem(people.find(item => item.selected)!, '同行人员')">启停选中</button>
          <button type="button" @click="people.find(item => item.selected) && openModal('person', 'edit', people.find(item => item.selected)!)">编辑</button>
          <button type="button" @click="people.find(item => item.selected) && openModal('person', 'delete', people.find(item => item.selected)!)">删除</button>
        </div>
      </article>

      <article class="lm-card template-card">
        <div class="lm-card-title"><h2><span>04</span>清单模板</h2><button type="button" @click="openModal('checklist', 'create')">新增</button></div>
        <div class="batch-bar">
          <span>已选 {{ selectedChecklistCount }}</span>
          <button type="button" @click="batchSetChecklists(true)">批量启用</button>
          <button type="button" @click="batchSetChecklists(false)">批量停用</button>
        </div>
        <button v-for="item in checklists" :key="item.id" type="button" :class="{ selected: item.selected, disabled: !item.enabled }" @click="toggleSelect(item)">
          {{ item.name }} <b>{{ item.count }} 项</b>
        </button>
        <div class="inline-actions">
          <button type="button" @click="checklists.find(item => item.selected) && openModal('checklist', 'edit', checklists.find(item => item.selected)!)">编辑选中</button>
          <button type="button" @click="checklists.find(item => item.selected) && openModal('checklist', 'delete', checklists.find(item => item.selected)!)">删除选中</button>
        </div>
      </article>

      <article class="lm-card asset-config">
        <div class="lm-card-title">
          <h2><span>05</span>资产资料</h2>
          <div class="card-tools">
            <button type="button" @click="scrollList('doc', 'up')"><SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" /></button>
            <button type="button" @click="scrollList('doc', 'down')"><SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" /></button>
          </div>
        </div>
        <div class="batch-bar">
          <span>已选 {{ selectedDocCount }}</span>
          <button type="button" @click="batchSetDocs(true)">批量启用</button>
          <button type="button" @click="batchSetDocs(false)">批量停用</button>
        </div>
        <div ref="docListRef" class="doc-grid">
          <button v-for="item in docs" :key="item.id" type="button" :class="{ selected: item.selected, disabled: !item.enabled }" @click="toggleSelect(item)">
            <SvgIcon icon="material-symbols:folder-outline-rounded" />{{ item.name }}<span>{{ item.enabled ? item.status : '已停用' }}</span>
          </button>
          <button type="button" @click="openModal('doc', 'create')"><SvgIcon icon="material-symbols:add-rounded" />上传资料</button>
        </div>
      </article>

      <article class="lm-card oss-card">
        <div class="lm-card-title">
          <h2><span>06</span>图册同步（阿里云 OSS）</h2>
          <button type="button" @click="syncEnabled = !syncEnabled; info(syncEnabled ? '图册同步已启用' : '图册同步已停用')">
            {{ syncEnabled ? '停用' : '启用' }}
          </button>
        </div>
        <div class="oss-inner">
          <div class="oss-cover lm-cover japan"></div>
          <div>
            <strong>日本旅行 2026 图册</strong>
            <p>已同步 328 张 / 12.6 GB</p>
            <span class="lm-tag" :class="syncEnabled ? 'green' : 'amber'">{{ syncEnabled ? '同步成功' : '已停用' }}</span>
          </div>
        </div>
        <div class="lm-progress-line"><i style="width: 12.6%"></i></div>
        <button class="lm-purple-btn" type="button" @click="runSync">{{ syncRunning ? '同步中...' : '立即同步' }}</button>
        <button class="lm-plain-btn" type="button" @click="info('同步设置', '这里可配置 OSS 路径和素材分类')">同步设置</button>
      </article>

      <article class="lm-card notice-card">
        <div class="lm-card-title"><h2>提醒与通知规则</h2><button type="button" @click="openModal('notice', 'create')">新增</button></div>
        <div ref="noticeListRef" class="notice-list">
          <button v-for="item in noticeRules" :key="item.id" type="button" :class="{ selected: item.selected, disabled: !item.enabled }" @click="selectOnly(noticeRules, item)">
            <strong>{{ item.name }}</strong><span>{{ item.desc }}</span><em>{{ item.channel }}</em>
          </button>
        </div>
        <div class="inline-actions">
          <button type="button" @click="noticeRules.find(item => item.selected) && toggleItem(noticeRules.find(item => item.selected)!, '通知规则')">启停选中</button>
          <button type="button" @click="noticeRules.find(item => item.selected) && openModal('notice', 'edit', noticeRules.find(item => item.selected)!)">编辑</button>
          <button type="button" @click="noticeRules.find(item => item.selected) && openModal('notice', 'delete', noticeRules.find(item => item.selected)!)">删除</button>
        </div>
      </article>

      <article class="lm-card automation-card">
        <div class="lm-card-title"><h2>AI 自动化设置</h2><button type="button" @click="info('AI 规则管理', '这里可进入完整 AI 自动化规则')">管理 AI 规则 ›</button></div>
        <div>
          <section v-for="item in aiRules" :key="item.name" :class="{ disabled: !item.enabled }">
            <strong>{{ item.name }}</strong>
            <p>{{ item.desc }}</p>
            <button class="lm-switch" :class="{ off: !item.enabled }" type="button" @click="item.enabled = !item.enabled; info(item.enabled ? 'AI 规则已启用' : 'AI 规则已停用', item.name)">
              <i></i>
            </button>
          </section>
          <section><strong>更多 AI 能力</strong><p>持续解锁中...</p><b>›</b></section>
        </div>
      </article>

      <article class="lm-card budget-card">
        <div class="lm-card-title"><h2>预算分类</h2><button type="button" @click="openModal('budget', 'create')">新增分类</button></div>
        <button v-for="item in budgetCategories" :key="item.id" type="button" :class="{ selected: item.selected, disabled: !item.enabled }" @click="selectOnly(budgetCategories, item)">
          <span>{{ item.name }}</span><b>￥{{ item.amount.toLocaleString() }}</b><em>{{ item.enabled ? '启用中' : '已停用' }}</em>
        </button>
        <div class="inline-actions">
          <button type="button" @click="budgetCategories.find(item => item.selected) && toggleItem(budgetCategories.find(item => item.selected)!, '预算分类')">启停选中</button>
          <button type="button" @click="budgetCategories.find(item => item.selected) && openModal('budget', 'edit', budgetCategories.find(item => item.selected)!)">编辑</button>
          <button type="button" @click="budgetCategories.find(item => item.selected) && openModal('budget', 'delete', budgetCategories.find(item => item.selected)!)">删除</button>
        </div>
      </article>
    </section>

    <footer class="lm-bottom-bar">
      <button class="lm-plain-btn" type="button" @click="resetConfig">重置配置</button>
      <span>{{ autoSave ? '✓ 自动保存已开启' : '自动保存已关闭' }}</span>
      <button class="lm-plain-btn" type="button" @click="backToProject">预览项目概览</button>
      <button class="lm-plain-btn" type="button" @click="success('配置已导出', '日本旅行 2026')">导出项目配置</button>
      <button class="lm-purple-btn" type="button" @click="saveConfig">保存配置</button>
    </footer>

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
  </LifeAppShell>
</template>

<style scoped>
.round-only {
  width: 36px;
  padding: 0;
  border-radius: 50%;
}

.manage-top {
  margin-bottom: 12px;
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
  font-size: 11px;
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
</style>

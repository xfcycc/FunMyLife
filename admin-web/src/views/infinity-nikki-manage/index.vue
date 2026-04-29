<script setup lang="ts">
import { computed, ref } from 'vue';
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'InfinityNikkiManage'
});

type TabLabel = '基础信息' | '日常模板' | '提醒规则' | '资产关联' | '图册同步' | 'AI 设置';
type ManageSection = '基础配置' | '日常任务' | '素材分区' | '提醒规则';
type ModalMode = 'create' | 'edit' | 'delete';
type ModalTarget = 'reminder' | 'template' | 'asset' | 'material' | 'integration';

interface TabItem {
  label: TabLabel;
  icon: string;
}

interface ManageItem {
  id: number;
  name: string;
  desc: string;
  enabled: boolean;
  selected: boolean;
}

interface ReminderRule extends ManageItem {
  advance: string;
}

interface TaskTemplate extends ManageItem {
  cycle: '每日' | '每周';
  resetAt: string;
}

interface AssetItem extends ManageItem {
  status: string;
}

interface MaterialGroup extends ManageItem {
  count: number;
}

interface IntegrationItem extends ManageItem {
  icon: string;
}

interface ManageModalState {
  show: boolean;
  mode: ModalMode;
  target: ModalTarget;
  id: number | null;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();

const tabs = [
  { label: '基础信息', icon: 'material-symbols:info-outline-rounded' },
  { label: '日常模板', icon: 'material-symbols:calendar-month-outline-rounded' },
  { label: '提醒规则', icon: 'material-symbols:notifications-outline-rounded' },
  { label: '资产关联', icon: 'material-symbols:inventory-2-outline-rounded' },
  { label: '图册同步', icon: 'material-symbols:photo-library-outline-rounded' },
  { label: 'AI 设置', icon: 'material-symbols:auto-awesome-rounded' }
] satisfies TabItem[];

const sectionOptions = ['基础配置', '日常任务', '素材分区', '提醒规则'] as const;
const resetOptions = ['04:00', '05:00', '10:00'] as const;

const activeTab = ref<TabLabel>('基础信息');
const activeSection = ref<ManageSection>('基础配置');
const projectEnabled = ref(true);
const resetTime = ref<(typeof resetOptions)[number]>('04:00');
const remindAdvance = ref('提前 1 天　09:00');
const syncEnabled = ref(true);
const syncRunning = ref(false);
const ruleListRef = ref<HTMLElement | null>(null);
const templateListRef = ref<HTMLElement | null>(null);
const materialListRef = ref<HTMLElement | null>(null);

const modal = ref<ManageModalState>({
  show: false,
  mode: 'create',
  target: 'template',
  id: null
});
const formName = ref('');
const formDesc = ref('');
const formCycle = ref<'每日' | '每周'>('每日');

const weekdays = ref([
  { label: '一', active: true },
  { label: '二', active: true },
  { label: '三', active: true },
  { label: '四', active: true },
  { label: '五', active: true },
  { label: '六', active: false },
  { label: '日', active: false }
]);

const reminderRules = ref<ReminderRule[]>([
  { id: 1, name: '活动结束提醒', desc: '活动结束前推送奖励清单', advance: '提前 1 天', enabled: true, selected: true },
  { id: 2, name: '直播开始提醒', desc: '前瞻直播开播前提醒', advance: '提前 30 分钟', enabled: true, selected: false },
  { id: 3, name: '版本维护提醒', desc: '维护前提醒清体力与领取邮件', advance: '提前 1 天', enabled: true, selected: false },
  { id: 4, name: '体力/资源溢出提醒', desc: '资源接近上限时推送', advance: '提前 2 小时', enabled: false, selected: false }
]);

const taskTemplates = ref<TaskTemplate[]>([
  { id: 1, name: '每日基础任务', desc: '签到、活跃与日常奖励', cycle: '每日', resetAt: '每日 04:00', enabled: true, selected: true },
  { id: 2, name: '采集与材料收集', desc: '花愿镇路线与素材清单', cycle: '每日', resetAt: '每日 04:00', enabled: true, selected: false },
  { id: 3, name: '挑战关卡', desc: '梦境、搭配竞技与评分复盘', cycle: '每日', resetAt: '每日 04:00', enabled: true, selected: false },
  { id: 4, name: '周常任务', desc: '周常挑战与奖励兑换', cycle: '每周', resetAt: '每周一 04:00', enabled: true, selected: false }
]);

const assets = ref<AssetItem[]>([
  { id: 1, name: '主账号（国服）', desc: '官方账号与通行证', status: '已绑定', enabled: true, selected: true },
  { id: 2, name: '攻略号（小号）', desc: '搭配测试与素材采集', status: '已绑定', enabled: true, selected: false },
  { id: 3, name: '充值账号（备用）', desc: '支付记录与月卡提醒', status: '已绑定', enabled: true, selected: false }
]);

const materialGroups = ref<MaterialGroup[]>([
  { id: 1, name: '服装材料', desc: '套装制作、升级和染色材料', count: 36, enabled: true, selected: true },
  { id: 2, name: '活动兑换', desc: '限时活动货币与奖励兑换', count: 18, enabled: true, selected: false },
  { id: 3, name: '拍照地点', desc: '图册同步与地点索引', count: 12, enabled: true, selected: false }
]);

const integrations = ref<IntegrationItem[]>([
  { id: 1, name: '飞书机器人', desc: '提醒推送', icon: 'material-symbols:send-outline-rounded', enabled: true, selected: false },
  { id: 2, name: 'OpenClaw', desc: '自动化执行', icon: 'material-symbols:hub-outline-rounded', enabled: true, selected: false },
  { id: 3, name: 'Webhook', desc: '外部通知', icon: 'material-symbols:share-outline-rounded', enabled: true, selected: false }
]);

const aiSettings = ref([
  { name: '活动总结', enabled: true },
  { name: '日常复盘', enabled: true },
  { name: '攻略笔记整理', enabled: true },
  { name: '版本更新解读', enabled: true }
]);

const stats = computed(() => [
  ['任务模板', String(taskTemplates.value.length), 'material-symbols:checklist-rounded'],
  ['提醒规则', String(reminderRules.value.length), 'material-symbols:alarm-outline-rounded'],
  ['关联资产', String(assets.value.length), 'material-symbols:account-balance-wallet-outline-rounded'],
  ['素材分区', String(materialGroups.value.length), 'material-symbols:inventory-2-outline-rounded'],
  ['AI 任务', String(aiSettings.value.filter(item => item.enabled).length), 'material-symbols:auto-awesome-rounded']
]);
const selectedRuleCount = computed(() => reminderRules.value.filter(item => item.selected).length);
const selectedTemplateCount = computed(() => taskTemplates.value.filter(item => item.selected).length);
const enabledRuleCount = computed(() => reminderRules.value.filter(item => item.enabled).length);
const enabledTemplateCount = computed(() => taskTemplates.value.filter(item => item.enabled).length);
const modalTitle = computed(() => {
  if (modal.value.mode === 'delete') return '删除确认';
  const action = modal.value.mode === 'create' ? '新增' : '编辑';
  const targetMap: Record<ModalTarget, string> = {
    reminder: '提醒规则',
    template: '任务模板',
    asset: '关联资产',
    material: '素材分区',
    integration: '集成'
  };
  return `${action}${targetMap[modal.value.target]}`;
});
const modalDescription = computed(() =>
  modal.value.mode === 'delete' ? '此操作仅删除演示数据，不会影响真实后端配置。' : '填写名称和说明，用于演示管理页配置流程。'
);
const modalConfirmText = computed(() => (modal.value.mode === 'delete' ? '确认删除' : '保存'));

function switchTab(label: TabLabel) {
  activeTab.value = label;
  info('管理分区已切换', label);
}

function switchSection(section: ManageSection) {
  activeSection.value = section;
  info('配置分区已切换', section);
}

function toggleWeekday(index: number) {
  weekdays.value[index].active = !weekdays.value[index].active;
  info(weekdays.value[index].active ? '已启用重置日' : '已停用重置日', `星期${weekdays.value[index].label}`);
}

function changeResetTime(value: (typeof resetOptions)[number]) {
  resetTime.value = value;
  info('每日重置时间已切换', value);
}

function toggleItem(item: ManageItem, label = '配置') {
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

function moveTemplate(index: number, direction: -1 | 1) {
  const next = index + direction;
  if (next < 0 || next >= taskTemplates.value.length) {
    warning('无法继续排序', '已经到达列表边界');
    return;
  }

  const list = taskTemplates.value;
  [list[index], list[next]] = [list[next], list[index]];
  success('模板排序已更新', list[next].name);
}

function batchSetTemplates(enabled: boolean) {
  const selected = taskTemplates.value.filter(item => item.selected);
  if (!selected.length) {
    warning('请先选择任务模板');
    return;
  }
  selected.forEach(item => {
    item.enabled = enabled;
  });
  success(enabled ? '已批量启用模板' : '已批量停用模板', `${selected.length} 项`);
}

function batchSetRules(enabled: boolean) {
  const selected = reminderRules.value.filter(item => item.selected);
  if (!selected.length) {
    warning('请先选择提醒规则');
    return;
  }
  selected.forEach(item => {
    item.enabled = enabled;
  });
  success(enabled ? '已批量启用提醒' : '已批量停用提醒', `${selected.length} 项`);
}

function resetLocalConfig() {
  projectEnabled.value = true;
  resetTime.value = '04:00';
  remindAdvance.value = '提前 1 天　09:00';
  weekdays.value.forEach((item, index) => {
    item.active = index < 5;
  });
  info('配置已重置', '已恢复本页演示默认值');
}

function saveConfig() {
  success('配置已保存', `模板 ${enabledTemplateCount.value} 个，提醒 ${enabledRuleCount.value} 个`);
}

function openModal(
  target: ModalTarget,
  mode: ModalMode,
  source?: ReminderRule | TaskTemplate | AssetItem | MaterialGroup | IntegrationItem
) {
  modal.value = {
    show: true,
    mode,
    target,
    id: source?.id ?? null
  };
  formName.value = source?.name ?? '';
  formDesc.value = source?.desc ?? '';
  formCycle.value = source && 'cycle' in source ? source.cycle : '每日';
}

function getList(target: ModalTarget) {
  const listMap = {
    reminder: reminderRules.value,
    template: taskTemplates.value,
    asset: assets.value,
    material: materialGroups.value,
    integration: integrations.value
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
      if (modal.value.target === 'template' && 'cycle' in target) {
        target.cycle = formCycle.value;
        target.resetAt = formCycle.value === '每周' ? `每周一 ${resetTime.value}` : `每日 ${resetTime.value}`;
      }
      success('配置项已保存', name);
    }
    modal.value.show = false;
    return;
  }

  const id = Date.now();
  if (modal.value.target === 'template') {
    taskTemplates.value.unshift({
      id,
      name,
      desc: desc || '新建任务模板',
      cycle: formCycle.value,
      resetAt: formCycle.value === '每周' ? `每周一 ${resetTime.value}` : `每日 ${resetTime.value}`,
      enabled: true,
      selected: true
    });
  } else if (modal.value.target === 'reminder') {
    reminderRules.value.unshift({
      id,
      name,
      desc: desc || '新建提醒规则',
      advance: remindAdvance.value,
      enabled: true,
      selected: true
    });
  } else if (modal.value.target === 'asset') {
    assets.value.unshift({ id, name, desc: desc || '新建关联资产', status: '待绑定', enabled: true, selected: true });
  } else if (modal.value.target === 'material') {
    materialGroups.value.unshift({ id, name, desc: desc || '新建素材分区', count: 0, enabled: true, selected: true });
  } else {
    integrations.value.push({
      id,
      name,
      desc: desc || '新建集成',
      icon: 'material-symbols:extension-outline-rounded',
      enabled: true,
      selected: false
    });
  }

  modal.value.show = false;
  success('配置项已新增', name);
}

function runSync() {
  if (!syncEnabled.value) {
    warning('同步未启用', '请先开启自动同步');
    return;
  }

  syncRunning.value = true;
  window.setTimeout(() => {
    syncRunning.value = false;
    success('图册同步完成', '已同步截图、活动图、视频');
  }, 600);
}

function scrollList(target: 'rule' | 'template' | 'material', direction: 'up' | 'down') {
  const map = {
    rule: ruleListRef.value,
    template: templateListRef.value,
    material: materialListRef.value
  };
  map[target]?.scrollBy({
    top: direction === 'up' ? -120 : 120,
    behavior: 'smooth'
  });
  info('页面滚动已触发', direction === 'up' ? '向上查看列表' : '向下查看列表');
}
</script>

<template>
  <LifeAppShell active="无限暖暖" avatar="nikki">
    <LifeToastHost :items="toasts" @close="removeToast" />

    <header class="nikki-manage-top lm-topbar">
      <div class="lm-title">
        <h1>←　项目 / 无限暖暖 / 管理</h1>
      </div>
      <div class="lm-actions">
        <button class="lm-icon-btn" type="button" aria-label="搜索配置" @click="info('搜索配置', '可继续接入配置项搜索')">
          <SvgIcon icon="material-symbols:search-rounded" />
        </button>
        <button class="lm-icon-btn" type="button" aria-label="查看提醒" @click="scrollList('rule', 'down')">
          <SvgIcon icon="material-symbols:notifications-outline-rounded" />
        </button>
        <button class="lm-purple-btn" type="button" @click="openModal('template', 'create')">
          <SvgIcon icon="material-symbols:add-rounded" />新建
        </button>
      </div>
    </header>

    <section class="nikki-manage-hero lm-card">
      <div class="top-avatar lm-hero-art nikki"></div>
      <div class="lm-detail-title">
        <h1>无限暖暖 <span class="lm-tag pink">游戏项目</span> <span class="lm-tag green">进行中</span></h1>
        <p>记录游戏日常、活动、版本与养成进度，管理账号与相关资源。</p>
        <div class="hero-fields">
          <span>创建时间 <b>2025-04-10</b></span>
          <span>开始时间 <b>2024-12-05</b></span>
          <span>类型 <b>游戏</b></span>
          <span>项目状态 <b>{{ projectEnabled ? '启用中' : '已停用' }}</b></span>
        </div>
      </div>
      <div class="stat-strip">
        <div v-for="item in stats" :key="item[0]">
          <SvgIcon :icon="item[2]" />
          <span>{{ item[0] }}</span>
          <strong>{{ item[1] }}</strong>
        </div>
      </div>
    </section>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item.label" :class="{ active: item.label === activeTab }" type="button" @click="switchTab(item.label)">
        <SvgIcon :icon="item.icon" />{{ item.label }}
      </button>
    </nav>

    <section class="lm-grid nikki-manage-grid">
      <article class="lm-card info-card">
        <div class="lm-card-title">
          <h2>基本信息</h2>
          <button type="button" @click="projectEnabled = !projectEnabled; info(projectEnabled ? '项目已启用' : '项目已停用', '无限暖暖')">
            {{ projectEnabled ? '停用' : '启用' }}
          </button>
        </div>
        <div class="manage-segment">
          <button v-for="item in sectionOptions" :key="item" :class="{ active: item === activeSection }" type="button" @click="switchSection(item)">
            {{ item }}
          </button>
        </div>
        <dl>
          <dt>项目名称</dt><dd>无限暖暖</dd>
          <dt>项目类型</dt><dd>游戏项目</dd>
          <dt>状态</dt><dd><span class="lm-tag" :class="projectEnabled ? 'green' : 'amber'">{{ projectEnabled ? '进行中' : '已停用' }}</span></dd>
          <dt>简介</dt><dd>记录日常任务、活动提醒、版本更新、抽卡计划、搭配笔记等内容。</dd>
          <dt>封面</dt>
          <dd>
            <span class="small-cover lm-cover nikki"></span>
            <button class="lm-plain-btn" type="button" @click="success('封面已更新', '已应用当前演示封面')">更换封面</button>
          </dd>
          <dt>标签</dt><dd><span class="lm-tag">换装</span> <span class="lm-tag blue">日常</span> <span class="lm-tag amber">活动</span> <span class="lm-tag pink">收集</span></dd>
        </dl>
      </article>

      <article class="lm-card reset-card">
        <div class="lm-card-title">
          <h2>游戏重置设置</h2>
          <button type="button" @click="resetLocalConfig">重置</button>
        </div>
        <div class="reset-time">
          <span>每日重置时间</span>
          <select :value="resetTime" @change="changeResetTime(($event.target as HTMLSelectElement).value as (typeof resetOptions)[number])">
            <option v-for="item in resetOptions" :key="item">{{ item }}</option>
          </select>
          <SvgIcon icon="material-symbols:alarm-outline-rounded" />
        </div>
        <div class="time-axis"><i></i><i></i><i></i><i></i><i></i><i></i><em>{{ resetTime }}</em></div>
        <div class="week-axis">
          <button
            v-for="(item, index) in weekdays"
            :key="item.label"
            :class="{ active: item.active }"
            type="button"
            @click="toggleWeekday(index)"
          >
            {{ item.label }}
          </button>
        </div>
        <div class="reset-time">
          <span>版本更新时段提醒</span>
          <strong>{{ remindAdvance }}</strong>
          <SvgIcon icon="material-symbols:alarm-outline-rounded" />
        </div>
        <p>将在版本维护或更新前提醒你</p>
      </article>

      <article class="lm-card reminder-card">
        <div class="lm-card-title">
          <h2>提醒规则 <span>（用于生成倒计时与通知）</span></h2>
          <div class="card-tools">
            <button type="button" @click="scrollList('rule', 'up')"><SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" /></button>
            <button type="button" @click="scrollList('rule', 'down')"><SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" /></button>
          </div>
        </div>
        <div class="batch-bar">
          <span>已选 {{ selectedRuleCount }}</span>
          <button type="button" @click="batchSetRules(true)">批量启用</button>
          <button type="button" @click="batchSetRules(false)">批量停用</button>
        </div>
        <div ref="ruleListRef" class="manage-list">
          <div v-for="item in reminderRules" :key="item.id" class="rule-row" :class="{ selected: item.selected, disabled: !item.enabled }">
            <button class="select-dot" type="button" :aria-label="`选择${item.name}`" @click="toggleSelect(item)">
              <SvgIcon :icon="item.selected ? 'material-symbols:check-circle-rounded' : 'material-symbols:radio-button-unchecked-rounded'" />
            </button>
            <div><strong>{{ item.name }}</strong><p>{{ item.desc }} · {{ item.advance }}</p></div>
            <em>{{ item.enabled ? '启用中' : '已停用' }}</em>
            <div class="row-tools">
              <button type="button" @click="toggleItem(item, '提醒规则')">
                <SvgIcon :icon="item.enabled ? 'material-symbols:pause-circle-outline-rounded' : 'material-symbols:play-circle-outline-rounded'" />
              </button>
              <button type="button" @click="openModal('reminder', 'edit', item)">
                <SvgIcon icon="material-symbols:edit-outline-rounded" />
              </button>
              <button type="button" @click="openModal('reminder', 'delete', item)">
                <SvgIcon icon="material-symbols:delete-outline-rounded" />
              </button>
            </div>
          </div>
        </div>
        <button class="add-line" type="button" @click="openModal('reminder', 'create')">＋ 新建提醒规则</button>
      </article>

      <article class="lm-card template-card">
        <div class="lm-card-title">
          <h2>日常任务模板</h2>
          <button type="button" @click="openModal('template', 'create')">添加</button>
        </div>
        <div class="batch-bar">
          <span>已选 {{ selectedTemplateCount }}</span>
          <button type="button" @click="batchSetTemplates(true)">批量启用</button>
          <button type="button" @click="batchSetTemplates(false)">批量停用</button>
        </div>
        <div ref="templateListRef" class="manage-list">
          <div v-for="(item, index) in taskTemplates" :key="item.id" class="template-row" :class="{ selected: item.selected, disabled: !item.enabled }">
            <button class="select-dot" type="button" :aria-label="`选择${item.name}`" @click="selectOnly(taskTemplates, item)">
              <SvgIcon :icon="item.selected ? 'material-symbols:check-circle-rounded' : 'material-symbols:radio-button-unchecked-rounded'" />
            </button>
            <div><strong>{{ item.name }}</strong><p>{{ item.desc }}</p></div>
            <em>{{ item.cycle }}</em>
            <time>{{ item.resetAt }}</time>
            <div class="row-tools">
              <button type="button" @click="moveTemplate(index, -1)"><SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" /></button>
              <button type="button" @click="moveTemplate(index, 1)"><SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" /></button>
              <button type="button" @click="toggleItem(item, '任务模板')">
                <SvgIcon :icon="item.enabled ? 'material-symbols:toggle-on-outline-rounded' : 'material-symbols:toggle-off-outline-rounded'" />
              </button>
              <button type="button" @click="openModal('template', 'edit', item)"><SvgIcon icon="material-symbols:edit-outline-rounded" /></button>
              <button type="button" @click="openModal('template', 'delete', item)"><SvgIcon icon="material-symbols:delete-outline-rounded" /></button>
            </div>
          </div>
        </div>
      </article>

      <article class="lm-card asset-card">
        <div class="lm-card-title">
          <h2>关联资产 <span>（用于账号与充值管理）</span></h2>
          <button type="button" @click="openModal('asset', 'create')">添加</button>
        </div>
        <button
          v-for="item in assets"
          :key="item.id"
          class="asset-row"
          :class="{ selected: item.selected, disabled: !item.enabled }"
          type="button"
          @click="selectOnly(assets, item)"
        >
          <span class="lm-avatar"></span>
          <span>{{ item.name }}<small>{{ item.desc }}</small></span>
          <em>{{ item.enabled ? item.status : '已停用' }}</em>
        </button>
        <div class="inline-actions">
          <button type="button" @click="assets.find(item => item.selected) && toggleItem(assets.find(item => item.selected)!, '资产')">启停选中</button>
          <button type="button" @click="assets.find(item => item.selected) && openModal('asset', 'edit', assets.find(item => item.selected)!)">编辑</button>
          <button type="button" @click="assets.find(item => item.selected) && openModal('asset', 'delete', assets.find(item => item.selected)!)">删除</button>
        </div>
      </article>

      <article class="lm-card oss-card">
        <div class="lm-card-title">
          <h2>图册与 OSS 同步</h2>
          <button type="button" @click="syncEnabled = !syncEnabled; info(syncEnabled ? '自动同步已开启' : '自动同步已关闭')">
            {{ syncEnabled ? '关闭' : '开启' }}
          </button>
        </div>
        <h3><SvgIcon icon="material-symbols:cloud-done-outline-rounded" />阿里云 OSS <span class="lm-tag" :class="syncEnabled ? 'green' : 'amber'">{{ syncEnabled ? '已连接' : '已停用' }}</span></h3>
        <dl>
          <dt>同步状态</dt><dd>{{ syncEnabled ? '正常' : '暂停' }}</dd>
          <dt>最近同步</dt><dd>2025-05-20 10:30</dd>
          <dt>自动同步</dt><dd>{{ syncEnabled ? '已开启（每日 02:30）' : '已关闭' }}</dd>
          <dt>同步内容</dt><dd>截图、活动图、视频</dd>
        </dl>
        <button class="lm-plain-btn" type="button" @click="runSync">{{ syncRunning ? '同步中...' : '立即同步' }}</button>
        <button class="lm-plain-btn" type="button" @click="openModal('material', 'create')">设置同步规则</button>
      </article>

      <article class="lm-card material-card">
        <div class="lm-card-title">
          <h2>素材分区</h2>
          <div class="card-tools">
            <button type="button" @click="scrollList('material', 'up')"><SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" /></button>
            <button type="button" @click="scrollList('material', 'down')"><SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" /></button>
          </div>
        </div>
        <div ref="materialListRef" class="manage-list compact-list">
          <div v-for="item in materialGroups" :key="item.id" class="material-row" :class="{ selected: item.selected, disabled: !item.enabled }">
            <button class="select-dot" type="button" :aria-label="`选择${item.name}`" @click="selectOnly(materialGroups, item)">
              <SvgIcon :icon="item.selected ? 'material-symbols:check-circle-rounded' : 'material-symbols:radio-button-unchecked-rounded'" />
            </button>
            <div><strong>{{ item.name }}</strong><p>{{ item.desc }}</p></div>
            <em>{{ item.count }} 项</em>
            <div class="row-tools">
              <button type="button" @click="toggleItem(item, '素材分区')"><SvgIcon icon="material-symbols:power-settings-new-rounded" /></button>
              <button type="button" @click="openModal('material', 'edit', item)"><SvgIcon icon="material-symbols:edit-outline-rounded" /></button>
              <button type="button" @click="openModal('material', 'delete', item)"><SvgIcon icon="material-symbols:delete-outline-rounded" /></button>
            </div>
          </div>
        </div>
        <button class="add-line" type="button" @click="openModal('material', 'create')">＋ 添加素材分区</button>
      </article>

      <article class="lm-card ai-setting-card">
        <div class="lm-card-title"><h2>AI 自动化设置</h2></div>
        <p v-for="item in aiSettings" :key="item.name">
          {{ item.name }}
          <button class="lm-switch" :class="{ off: !item.enabled }" type="button" :aria-label="`切换${item.name}`" @click="item.enabled = !item.enabled; info(item.enabled ? 'AI 设置已启用' : 'AI 设置已停用', item.name)">
            <i></i>
          </button>
        </p>
        <button type="button" @click="info('AI 任务历史', '这里可查看自动总结、复盘和版本解读记录')">查看 AI 任务历史</button>
      </article>

      <article class="lm-card integration-card">
        <div class="lm-card-title">
          <h2>集成与通知</h2>
          <button type="button" @click="openModal('integration', 'create')">添加集成</button>
        </div>
        <div>
          <button
            v-for="item in integrations"
            :key="item.id"
            type="button"
            :class="{ disabled: !item.enabled }"
            @click="toggleItem(item, '集成')"
          >
            <SvgIcon :icon="item.icon" />{{ item.name }} <span>{{ item.enabled ? '已连接' : '已停用' }}</span>
          </button>
          <button type="button" @click="openModal('integration', 'create')">＋ 添加集成</button>
        </div>
      </article>
    </section>

    <footer class="lm-bottom-bar">
      <button class="lm-plain-btn" type="button" @click="resetLocalConfig">重置</button>
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
          <input v-model="formName" maxlength="24" placeholder="请输入配置名称" />
        </label>
        <label>
          <span>说明</span>
          <input v-model="formDesc" maxlength="48" placeholder="请输入配置说明" />
        </label>
        <label v-if="modal.target === 'template'">
          <span>周期</span>
          <select v-model="formCycle">
            <option>每日</option>
            <option>每周</option>
          </select>
        </label>
      </div>
    </LifeModal>
  </LifeAppShell>
</template>

<style scoped>
.nikki-manage-top {
  margin-bottom: 12px;
}

.nikki-manage-hero {
  display: grid;
  grid-template-columns: 80px minmax(0, 1fr) 440px;
  gap: 18px;
  align-items: center;
  min-height: 118px;
}

.top-avatar {
  width: 80px;
  height: 80px;
  border-radius: 8px;
}

.hero-fields {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  margin-top: 17px;
}

.hero-fields span {
  padding: 9px 11px;
  border-radius: 6px;
  background: #fafafe;
  color: #8b91a1;
  font-size: 10px;
}

.hero-fields b {
  display: block;
  margin-top: 6px;
  color: #222839;
}

.stat-strip {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 0;
  align-self: stretch;
}

.stat-strip div {
  display: grid;
  place-items: center;
  align-content: center;
  gap: 5px;
  border-left: 1px solid #edf0f6;
  color: #7f8696;
  font-size: 10px;
}

.stat-strip .svg-icon {
  color: #d7992f;
  font-size: 18px;
}

.stat-strip strong {
  color: #1b2130;
  font-size: 16px;
}

.nikki-manage-grid {
  grid-template-columns: 1.3fr 1.7fr 1.9fr;
  margin-top: 14px;
}

.info-card dl {
  display: grid;
  grid-template-columns: 66px minmax(0, 1fr);
  gap: 11px;
  margin: 0;
  color: #4f5769;
  font-size: 11px;
}

.info-card dt {
  color: #8b91a1;
}

.info-card dd {
  margin: 0;
}

.manage-segment {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 4px;
  margin-bottom: 16px;
  padding: 4px;
  border-radius: 8px;
  background: #f6f7fc;
}

.manage-segment button,
.batch-bar button,
.card-tools button,
.inline-actions button {
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

.small-cover {
  display: inline-block;
  width: 42px;
  height: 42px;
  margin-right: 9px;
  border-radius: 6px;
  vertical-align: middle;
}

.reset-time {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto 18px;
  align-items: center;
  gap: 10px;
  color: #7f8696;
  font-size: 11px;
}

.reset-time strong {
  color: #202636;
  font-size: 13px;
}

.reset-time select {
  width: 82px;
  height: 28px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #202636;
  font-size: 12px;
}

.time-axis {
  position: relative;
  display: flex;
  justify-content: space-between;
  margin: 25px 0 17px;
  padding: 0 8px;
}

.time-axis::before {
  content: '';
  position: absolute;
  left: 16px;
  right: 16px;
  top: 5px;
  height: 2px;
  background: #e8eaf3;
}

.time-axis i {
  position: relative;
  z-index: 1;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #d8d5f5;
}

.time-axis i:first-child {
  background: #765ce8;
}

.time-axis em {
  position: absolute;
  right: 33px;
  top: -18px;
  color: #765ce8;
  font-size: 10px;
  font-style: normal;
}

.week-axis {
  display: flex;
  justify-content: space-around;
  margin-bottom: 21px;
  color: #9aa0ae;
  font-size: 11px;
}

.week-axis button {
  display: grid;
  place-items: center;
  width: 18px;
  height: 18px;
  border: 0;
  border-radius: 50%;
  background: transparent;
  color: inherit;
  cursor: pointer;
}

.week-axis button.active {
  background: #765ce8;
  color: #fff;
}

.reset-card > p {
  color: #765ce8;
  font-size: 10px;
}

.card-tools {
  display: flex;
  gap: 6px;
}

.card-tools button {
  display: inline-grid;
  width: 26px;
  height: 26px;
  place-items: center;
  border-radius: 7px;
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

.batch-bar button {
  padding: 5px 9px;
  border-radius: 999px;
  background: #f6f7fc;
  color: #765ce8;
  font-size: 10px;
}

.manage-list {
  max-height: 238px;
  overflow: auto;
  padding-right: 2px;
}

.compact-list {
  max-height: 188px;
}

.rule-row,
.template-row,
.material-row {
  display: grid;
  grid-template-columns: 26px minmax(0, 1fr) auto 96px;
  gap: 10px;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 7px;
  background: #fafafe;
}

.template-row {
  grid-template-columns: 26px minmax(0, 1fr) auto 72px 136px;
}

.material-row {
  grid-template-columns: 26px minmax(0, 1fr) 46px 90px;
}

.rule-row.selected,
.template-row.selected,
.material-row.selected {
  background: #f7f5ff;
  box-shadow: inset 0 0 0 1px rgba(118, 92, 232, 0.14);
}

.rule-row.disabled,
.template-row.disabled,
.material-row.disabled,
.asset-row.disabled,
.integration-card button.disabled {
  opacity: 0.58;
}

.rule-row strong,
.template-row strong,
.material-row strong {
  display: block;
  color: #252b3c;
  font-size: 11px;
}

.rule-row p,
.template-row p,
.material-row p {
  margin: 4px 0 0;
  color: #8b91a1;
  font-size: 9px;
}

.rule-row em,
.template-row em,
.material-row em {
  color: #31a978;
  font-size: 10px;
  font-style: normal;
}

.template-row time {
  color: #7f8696;
  font-size: 10px;
}

.select-dot,
.row-tools button {
  display: inline-grid;
  place-items: center;
  border: 0;
  background: transparent;
  color: #765ce8;
  cursor: pointer;
}

.select-dot {
  width: 24px;
  height: 24px;
  font-size: 18px;
}

.row-tools {
  display: flex;
  justify-content: flex-end;
  gap: 4px;
}

.row-tools button {
  width: 23px;
  height: 23px;
  border-radius: 6px;
  background: #fff;
  color: #7d8495;
}

.add-line {
  border: 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.template-card,
.integration-card {
  grid-column: span 1;
}

.asset-row {
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr) auto;
  width: 100%;
  align-items: center;
  gap: 9px;
  margin: 12px 0;
  padding: 0;
  border: 0;
  background: transparent;
  color: #343b4d;
  font-size: 11px;
  text-align: left;
  cursor: pointer;
}

.asset-row.selected {
  color: #765ce8;
}

.asset-card .lm-avatar {
  width: 22px;
  height: 22px;
}

.asset-card em {
  color: #7f8696;
  font-style: normal;
}

.asset-row small {
  display: block;
  margin-top: 3px;
  color: #8b91a1;
  font-size: 9px;
}

.inline-actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.inline-actions button {
  padding: 6px 10px;
  border-radius: 999px;
  background: #f6f7fc;
  color: #765ce8;
  font-size: 10px;
}

.oss-card h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 15px;
  color: #222839;
  font-size: 13px;
}

.oss-card dl {
  display: grid;
  grid-template-columns: 70px minmax(0, 1fr);
  gap: 10px;
  color: #596174;
  font-size: 11px;
}

.oss-card dt {
  color: #8b91a1;
}

.oss-card dd {
  margin: 0;
  text-align: right;
}

.ai-setting-card p {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 12px 0;
  color: #343b4d;
  font-size: 11px;
}

.ai-setting-card button {
  border: 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
  cursor: pointer;
}

.ai-setting-card .lm-switch {
  position: relative;
  display: inline-flex;
  width: 34px;
  height: 18px;
  padding: 2px;
  border-radius: 999px;
  background: #765ce8;
}

.ai-setting-card .lm-switch i {
  width: 14px;
  height: 14px;
  transform: translateX(16px);
  border-radius: 50%;
  background: #fff;
  transition: transform 0.18s ease;
}

.ai-setting-card .lm-switch.off {
  background: #d8dce8;
}

.ai-setting-card .lm-switch.off i {
  transform: translateX(0);
}

.integration-card {
  grid-column: 1 / -1;
}

.integration-card > div:last-child {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
}

.integration-card button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-height: 40px;
  margin: 0;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #343b4d;
  font-size: 11px;
  cursor: pointer;
}

.integration-card span {
  color: #31a978;
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

.manage-form input,
.manage-form select {
  width: 100%;
  height: 36px;
  border: 1px solid #e6e9f3;
  border-radius: 8px;
  background: #fff;
  color: #202636;
  font-size: 13px;
}

@media (max-width: 1180px) {
  .nikki-manage-hero,
  .nikki-manage-grid,
  .integration-card > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .stat-strip,
  .hero-fields {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}
</style>

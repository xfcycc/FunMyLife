<script setup lang="ts">
import { computed, ref } from 'vue';
import type { RouteKey } from '@elegant-router/types';
import { useRoute } from 'vue-router';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import LifeToastHost from '@/components/life-manager/LifeToastHost.vue';
import { useRouterPush } from '@/hooks/common/router';
import { lifeUnavailableFeedback, useLifeToast } from '@/hooks/business/lifeFeedback';

defineOptions({
  name: 'Projects'
});

interface ProjectItem {
  name: string;
  type: '游戏' | '旅行' | '学习' | '生活';
  coverClass: string;
  activity: string;
  remind: string;
  todo: string;
  note: string;
  file: string;
  next: string;
  ai: string;
  aiState: 'done' | 'pending' | 'todo';
  icon: string;
  favorited: boolean;
  archived?: boolean;
}

interface NavItem {
  label: string;
  icon: string;
  routeKey?: RouteKey;
}

const { toasts, removeToast, success, info, warning } = useLifeToast();
const route = useRoute();
const { routerPushByKey } = useRouterPush();

const navItems = [
  { label: '首页', icon: 'material-symbols:home-outline-rounded', routeKey: 'home' },
  { label: '项目', icon: 'material-symbols:calendar-month-outline-rounded', routeKey: 'projects' },
  { label: '日程', icon: 'material-symbols:calendar-today-outline-rounded' },
  { label: '清单', icon: 'material-symbols:select-check-box-outline-rounded' },
  { label: '记录', icon: 'material-symbols:edit-square-outline-rounded' },
  { label: '资产', icon: 'material-symbols:inventory-2-outline-rounded' },
  { label: 'AI 助手', icon: 'material-symbols:smart-toy-outline-rounded' },
  { label: '数据统计', icon: 'material-symbols:donut-large-outline-rounded' },
  { label: '设置', icon: 'material-symbols:settings-outline-rounded' }
] satisfies NavItem[];

const statCards = [
  {
    label: '全部项目',
    value: '12',
    note: '进行中 9　已归档 3',
    icon: 'material-symbols:folder-outline-rounded',
    tone: 'purple'
  },
  {
    label: '待处理提醒',
    value: '28',
    note: '今天 8　未来7天 20',
    icon: 'material-symbols:alarm-outline-rounded',
    tone: 'coral'
  },
  {
    label: '待办任务',
    value: '56',
    note: '已完成 103',
    icon: 'material-symbols:fact-check-outline-rounded',
    tone: 'green'
  },
  {
    label: '笔记记录',
    value: '138',
    note: '本周新增 16',
    icon: 'material-symbols:article-outline-rounded',
    tone: 'blue'
  },
  {
    label: '图册/文件',
    value: '342',
    note: '本月新增 48',
    icon: 'material-symbols:add-photo-alternate-outline-rounded',
    tone: 'violet'
  },
  {
    label: 'AI 总结',
    value: '24',
    note: '待生成 5',
    icon: 'material-symbols:robot-2-outline-rounded',
    tone: 'mint'
  }
] as const;

const filters = ['全部', '游戏', '旅行', '学习', '生活', '归档'] as const;
const typeOptions = ['全部', '游戏', '旅行', '学习', '生活'] as const;
const statusOptions = ['全部', '进行中', '待整理', '需关注'] as const;
const sortOptions = ['最近更新', '提醒优先', '待办优先'] as const;

const projectItems = ref<ProjectItem[]>([
  {
    name: '无限暖暖',
    type: '游戏',
    coverClass: 'nikki',
    activity: '花愿镇奇遇季 4.1 版本',
    remind: '12',
    todo: '8/15',
    note: '24',
    file: '56',
    next: '每日任务重置 10:00',
    ai: '已更新',
    aiState: 'done',
    icon: 'material-symbols:stadia-controller-outline-rounded',
    favorited: true
  },
  {
    name: '原神',
    type: '游戏',
    coverClass: 'genshin',
    activity: '4.6 版本「两界为火」',
    remind: '9',
    todo: '6/12',
    note: '18',
    file: '42',
    next: '树脂恢复提醒 18:00',
    ai: '已更新',
    aiState: 'done',
    icon: 'material-symbols:stadia-controller-outline-rounded',
    favorited: false
  },
  {
    name: '日本旅行 2026',
    type: '旅行',
    coverClass: 'japan',
    activity: '机票预订已确认',
    remind: '15',
    todo: '11/20',
    note: '32',
    file: '128',
    next: '签证材料整理 5月25日',
    ai: '生成中',
    aiState: 'pending',
    icon: 'material-symbols:flight-takeoff-rounded',
    favorited: false
  },
  {
    name: 'OpenAI 学习计划',
    type: '学习',
    coverClass: 'openai',
    activity: '学习周报已生成',
    remind: '6',
    todo: '7/14',
    note: '19',
    file: '8',
    next: '每日学习打卡 20:30',
    ai: '已更新',
    aiState: 'done',
    icon: 'material-symbols:menu-book-outline-rounded',
    favorited: false
  },
  {
    name: '家庭保险',
    type: '生活',
    coverClass: 'family',
    activity: '重疾险即将自动续费',
    remind: '4',
    todo: '3/6',
    note: '12',
    file: '6',
    next: '自动扣费提醒 6月10日',
    ai: '已更新',
    aiState: 'done',
    icon: 'material-symbols:health-and-safety-outline-rounded',
    favorited: false
  },
  {
    name: '年度健康管理',
    type: '生活',
    coverClass: 'health',
    activity: '体检报告已记录',
    remind: '5',
    todo: '6/10',
    note: '15',
    file: '10',
    next: '每日运动打卡 21:00',
    ai: '待生成',
    aiState: 'todo',
    icon: 'material-symbols:favorite-outline-rounded',
    favorited: false
  }
]);

const extraProjects = ref<ProjectItem[]>([
  {
    name: '摄影灵感收集',
    type: '学习',
    coverClass: 'openai',
    activity: '新增构图参考图 12 张',
    remind: '2',
    todo: '3/8',
    note: '9',
    file: '26',
    next: '周末外拍计划 5月1日',
    ai: '待生成',
    aiState: 'todo',
    icon: 'material-symbols:photo-camera-outline-rounded',
    favorited: false
  },
  {
    name: '年度收纳计划',
    type: '生活',
    coverClass: 'family',
    activity: '卧室收纳清单已更新',
    remind: '3',
    todo: '4/11',
    note: '6',
    file: '11',
    next: '五月整理复盘 5月30日',
    ai: '已更新',
    aiState: 'done',
    icon: 'material-symbols:inventory-2-outline-rounded',
    favorited: false,
    archived: true
  }
]);

const recentUpdates = ref([
  {
    title: '日本旅行 2026',
    desc: '新增笔记：京都行程规划',
    time: '2 小时前',
    icon: 'material-symbols:folder-open-outline-rounded',
    tone: 'blue'
  },
  {
    title: '无限暖暖',
    desc: '完成每日任务 7/15',
    time: '3 小时前',
    icon: 'material-symbols:stadia-controller-outline-rounded',
    tone: 'coral'
  },
  {
    title: 'OpenAI 学习计划',
    desc: 'AI 生成学习周报',
    time: '昨天 22:15',
    icon: 'material-symbols:neurology-outline-rounded',
    tone: 'mint'
  },
  {
    title: '家庭保险',
    desc: '更新了保单资料',
    time: '昨天 18:30',
    icon: 'material-symbols:shield-outline-rounded',
    tone: 'violet'
  },
  {
    title: '原神',
    desc: '新增待办：周常 BOSS 击败',
    time: '昨天 17:45',
    icon: 'material-symbols:stars-outline-rounded',
    tone: 'amber'
  }
]);

const templates = [
  {
    title: '游戏项目模板',
    desc: '日常笔记、活动提醒、版本追踪',
    icon: 'material-symbols:stadia-controller-outline-rounded',
    tone: 'amber'
  },
  {
    title: '旅行项目模板',
    desc: '行程规划、预订管理、旅行记录',
    icon: 'material-symbols:flight-takeoff-rounded',
    tone: 'blue'
  },
  {
    title: '学习项目模板',
    desc: '学习计划、打卡任务、笔记整理',
    icon: 'material-symbols:school-outline-rounded',
    tone: 'mint'
  },
  {
    title: '生活项目模板',
    desc: '提醒清单、资产管理、记录整理',
    icon: 'material-symbols:home-outline-rounded',
    tone: 'rose'
  }
] as const;

const activeFilter = ref<(typeof filters)[number]>('全部');
const selectedType = ref<(typeof typeOptions)[number]>('全部');
const selectedStatus = ref<(typeof statusOptions)[number]>('全部');
const selectedSort = ref<(typeof sortOptions)[number]>('最近更新');
const searchKeyword = ref('');
const viewMode = ref<'card' | 'list'>('card');
const showCreateModal = ref(false);
const editingProjectName = ref('');
const projectFormName = ref('');
const projectFormType = ref<'游戏' | '旅行' | '学习' | '生活'>('生活');
const projectFormActivity = ref('');
const loadedMore = ref(false);
const projectListRef = ref<HTMLElement | null>(null);

const projectModalTitle = computed(() => (editingProjectName.value ? '编辑项目' : '新建项目'));
const projectModalDescription = computed(() =>
  editingProjectName.value ? '调整项目名称、类型和最近活动。' : '先补一个名字和项目类型，后续再慢慢完善内容。'
);
const projectModalConfirmText = computed(() => (editingProjectName.value ? '保存项目' : '创建项目'));

const visibleProjects = computed(() => {
  let list = [...projectItems.value, ...(loadedMore.value ? extraProjects.value : [])];

  if (activeFilter.value !== '全部') {
    if (activeFilter.value === '归档') {
      list = list.filter(item => item.archived);
    } else {
      list = list.filter(item => item.type === activeFilter.value);
    }
  }

  if (selectedType.value !== '全部') {
    list = list.filter(item => item.type === selectedType.value);
  }

  if (searchKeyword.value.trim()) {
    const keyword = searchKeyword.value.trim().toLowerCase();
    list = list.filter(item => item.name.toLowerCase().includes(keyword) || item.activity.toLowerCase().includes(keyword));
  }

  if (selectedStatus.value === '进行中') {
    list = list.filter(item => !item.archived);
  }

  if (selectedStatus.value === '需关注') {
    list = list.filter(item => item.aiState !== 'done');
  }

  if (selectedStatus.value === '待整理') {
    list = list.filter(item => item.todo.startsWith('3/') || item.todo.startsWith('4/'));
  }

  if (selectedSort.value === '提醒优先') {
    list = list.sort((a, b) => Number(b.remind) - Number(a.remind));
  }

  if (selectedSort.value === '待办优先') {
    list = list.sort((a, b) => Number(b.todo.split('/')[1]) - Number(a.todo.split('/')[1]));
  }

  return list;
});

function setFilter(filter: (typeof filters)[number]) {
  activeFilter.value = filter;
  info('项目分组已切换', filter);
}

function notifyTypeFilter() {
  info('类型筛选已切换', selectedType.value);
}

function notifyStatusFilter() {
  info('状态筛选已切换', selectedStatus.value);
}

function notifySort() {
  info('项目排序已切换', selectedSort.value);
}

function applySearch() {
  const keyword = searchKeyword.value.trim();
  if (!keyword) {
    warning('请输入搜索关键词');
    return;
  }

  info('项目搜索已应用', keyword);
}

function getProjectSource(name: string) {
  return projectItems.value.find(item => item.name === name) || extraProjects.value.find(item => item.name === name);
}

function navigateByRouteKey(routeKey?: RouteKey) {
  if (!routeKey) return;
  routerPushByKey(routeKey);
}

function getUnavailableHint(label: string) {
  return lifeUnavailableFeedback.hint(label);
}

function showUnavailable(label: string) {
  info(lifeUnavailableFeedback.title, lifeUnavailableFeedback.message(label));
}

function isNavItemActive(item: NavItem) {
  const path = route.path;

  if (item.routeKey === 'home') return path === '/life/home';
  if (item.routeKey === 'projects') return path === '/life/projects' || path.startsWith('/life/project/');

  return false;
}

const projectRouteKeyMap = {
  无限暖暖: 'infinity-nikki',
  '日本旅行 2026': 'japan-travel'
} as const;

const projectManageRouteKeyMap = {
  无限暖暖: 'infinity-nikki-manage',
  '日本旅行 2026': 'japan-travel-manage'
} as const;

function getProjectRouteKey(name: string) {
  return projectRouteKeyMap[name as keyof typeof projectRouteKeyMap];
}

function getProjectManageRouteKey(name: string) {
  return projectManageRouteKeyMap[name as keyof typeof projectManageRouteKeyMap];
}

function openProject(item: ProjectItem) {
  const routeKey = getProjectRouteKey(item.name);

  if (routeKey) {
    routerPushByKey(routeKey);
    return;
  }

  showUnavailable(`${item.name}详情`);
}

function openProjectManage(item: ProjectItem) {
  const routeKey = getProjectManageRouteKey(item.name);

  if (routeKey) {
    routerPushByKey(routeKey);
    return;
  }

  openEditModal(item);
}

function toggleFavorite(name: string) {
  const target = getProjectSource(name);
  if (!target) return;
  target.favorited = !target.favorited;
  success(target.favorited ? '已收藏项目' : '已取消收藏', target.name);
}

function toggleArchive(name: string) {
  const target = getProjectSource(name);
  if (!target) return;
  target.archived = !target.archived;
  info(target.archived ? '项目已归档' : '项目已恢复', target.name);
}

function changeView(mode: 'card' | 'list') {
  viewMode.value = mode;
  info(mode === 'card' ? '已切换卡片视图' : '已切换列表视图');
}

function loadMoreProjects() {
  if (loadedMore.value) {
    info('没有更多项目啦', '演示数据已经全部展示');
    return;
  }

  loadedMore.value = true;
  success('已加载更多项目', '新增 2 个项目卡片');
}

function scrollProjectList(direction: 'up' | 'down') {
  projectListRef.value?.scrollBy({
    top: direction === 'up' ? -320 : 320,
    behavior: 'smooth'
  });
  info(direction === 'up' ? '向上滑动项目列表' : '向下滑动项目列表');
}

function resetProjectForm() {
  editingProjectName.value = '';
  projectFormName.value = '';
  projectFormType.value = '生活';
  projectFormActivity.value = '';
}

function openCreateModal() {
  resetProjectForm();
  showCreateModal.value = true;
}

function openEditModal(item: ProjectItem) {
  editingProjectName.value = item.name;
  projectFormName.value = item.name;
  projectFormType.value = item.type;
  projectFormActivity.value = item.activity;
  showCreateModal.value = true;
}

function getProjectLook(type: ProjectItem['type']) {
  if (type === '旅行') {
    return {
      coverClass: 'japan',
      icon: 'material-symbols:flight-takeoff-rounded'
    };
  }

  if (type === '游戏') {
    return {
      coverClass: 'nikki',
      icon: 'material-symbols:stadia-controller-outline-rounded'
    };
  }

  if (type === '学习') {
    return {
      coverClass: 'openai',
      icon: 'material-symbols:menu-book-outline-rounded'
    };
  }

  return {
    coverClass: 'family',
    icon: 'material-symbols:home-outline-rounded'
  };
}

function saveProject() {
  const name = projectFormName.value.trim();
  const activity = projectFormActivity.value.trim() || '刚刚更新项目卡片';

  if (!name) {
    warning('请填写项目名称');
    return;
  }

  const look = getProjectLook(projectFormType.value);

  if (editingProjectName.value) {
    const target = getProjectSource(editingProjectName.value);
    if (!target) return;

    target.name = name;
    target.type = projectFormType.value;
    target.activity = activity;
    target.coverClass = look.coverClass;
    target.icon = look.icon;

    recentUpdates.value.unshift({
      title: name,
      desc: '项目资料已更新',
      time: '刚刚',
      icon: 'material-symbols:edit-note-outline-rounded',
      tone: 'blue'
    });

    showCreateModal.value = false;
    success('项目已保存', name);
    resetProjectForm();
    return;
  }

  projectItems.value.unshift({
    name,
    type: projectFormType.value,
    coverClass: look.coverClass,
    activity,
    remind: '1',
    todo: '0/3',
    note: '0',
    file: '0',
    next: '请补充首个提醒',
    ai: '待生成',
    aiState: 'todo',
    icon: look.icon,
    favorited: false
  });

  recentUpdates.value.unshift({
    title: name,
    desc: '新建项目成功，等待补充内容',
    time: '刚刚',
    icon: 'material-symbols:add-circle-outline-rounded',
    tone: 'mint'
  });

  showCreateModal.value = false;
  success('项目已创建', name);
  resetProjectForm();
}
</script>

<template>
  <main class="pm-page">
    <LifeToastHost :items="toasts" @close="removeToast" />

    <aside class="pm-sidebar">
      <div class="pm-brand">
        <div class="pm-logo"></div>
        <div>
          <strong>Life Manager</strong>
          <span>你的生活，由你掌控</span>
        </div>
      </div>

      <nav class="pm-nav" aria-label="主导航">
        <button
          v-for="item in navItems"
          :key="item.label"
          class="pm-nav-item"
          :class="{ active: isNavItemActive(item) }"
          :disabled="!item.routeKey"
          :title="!item.routeKey ? getUnavailableHint(item.label) : undefined"
          :aria-label="!item.routeKey ? getUnavailableHint(item.label) : item.label"
          type="button"
          @click="navigateByRouteKey(item.routeKey)"
        >
          <SvgIcon :icon="item.icon" />
          <span>{{ item.label }}</span>
        </button>
      </nav>

      <div class="pm-profile">
        <div class="pm-profile-main">
          <div class="pm-avatar"></div>
          <div>
            <strong>夏目悠然 <span>Pro</span></strong>
            <p>专注生活的每一天 ✨</p>
          </div>
        </div>
        <div class="pm-energy">
          <div>
            <span>今日能量</span>
            <strong>87 / 100</strong>
          </div>
          <div class="pm-energy-bar"><i></i></div>
        </div>
      </div>

      <div class="pm-sidebar-tools">
        <button aria-label="夜间模式" type="button" @click="showUnavailable('夜间模式')"><SvgIcon icon="material-symbols:dark-mode-outline-rounded" /></button>
        <button aria-label="通知" type="button" @click="showUnavailable('通知中心')"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></button>
        <button aria-label="帮助" type="button" @click="showUnavailable('帮助中心')"><SvgIcon icon="material-symbols:help-outline-rounded" /></button>
        <button aria-label="扩展" type="button" @click="showUnavailable('扩展中心')"><SvgIcon icon="material-symbols:dashboard-customize-outline-rounded" /></button>
      </div>
    </aside>

    <section class="pm-main">
      <header class="pm-header">
        <div>
          <h1>项目管理</h1>
          <p>用项目聚合生活中的一切，让每件事井井有条</p>
        </div>
        <div class="pm-header-actions">
          <label class="pm-search">
            <SvgIcon icon="material-symbols:search-rounded" />
            <input v-model="searchKeyword" placeholder="搜索项目名称或内容" @keyup.enter="applySearch" />
          </label>
          <label class="pm-select pm-select-wrap">
            <span>类型</span>
            <select v-model="selectedType" @change="notifyTypeFilter">
              <option v-for="item in typeOptions" :key="item">{{ item }}</option>
            </select>
          </label>
          <label class="pm-select pm-select-wrap">
            <span>状态</span>
            <select v-model="selectedStatus" @change="notifyStatusFilter">
              <option v-for="item in statusOptions" :key="item">{{ item }}</option>
            </select>
          </label>
          <label class="pm-select pm-select-wrap">
            <span>排序</span>
            <select v-model="selectedSort" @change="notifySort">
              <option v-for="item in sortOptions" :key="item">{{ item }}</option>
            </select>
          </label>
          <button class="pm-primary" type="button" @click="openCreateModal"><SvgIcon icon="material-symbols:add-rounded" />新建项目</button>
          <button class="pm-icon-button has-dot" aria-label="通知" type="button" @click="showUnavailable('项目通知')">
            <SvgIcon icon="material-symbols:notifications-outline-rounded" />
          </button>
          <button class="pm-user" aria-label="用户头像" type="button" @click="showUnavailable('个人中心')"></button>
        </div>
      </header>

      <section class="pm-stats" aria-label="项目统计">
        <article v-for="item in statCards" :key="item.label" class="pm-stat-card">
          <span class="pm-stat-icon" :class="item.tone"><SvgIcon :icon="item.icon" /></span>
          <div>
            <span>{{ item.label }}</span>
            <strong>{{ item.value }}</strong>
            <p>{{ item.note }}</p>
          </div>
        </article>
      </section>

      <div class="pm-content-grid">
        <section class="pm-projects">
          <div class="pm-toolbar">
            <div class="pm-tabs">
              <button v-for="item in filters" :key="item" :class="{ active: item === activeFilter }" type="button" @click="setFilter(item)">
                {{ item }}
              </button>
            </div>
            <div class="pm-view-switch">
              <button :class="{ active: viewMode === 'card' }" type="button" @click="changeView('card')">
                <SvgIcon icon="material-symbols:grid-view-rounded" />卡片视图
              </button>
              <button :class="{ active: viewMode === 'list' }" type="button" @click="changeView('list')">
                <SvgIcon icon="material-symbols:format-list-bulleted-rounded" />列表视图
              </button>
            </div>
            <div class="pm-list-scroll">
              <button type="button" aria-label="向上滑动项目列表" @click="scrollProjectList('up')">
                <SvgIcon icon="material-symbols:keyboard-arrow-up-rounded" />
              </button>
              <button type="button" aria-label="向下滑动项目列表" @click="scrollProjectList('down')">
                <SvgIcon icon="material-symbols:keyboard-arrow-down-rounded" />
              </button>
            </div>
          </div>

          <div ref="projectListRef" class="pm-card-grid" :class="{ 'is-list': viewMode === 'list' }">
            <article
              v-for="item in visibleProjects"
              :key="item.name"
              class="pm-project-card"
              :class="{ archived: item.archived }"
              role="button"
              tabindex="0"
              @click="openProject(item)"
              @keydown.enter="openProject(item)"
              @keydown.space.prevent="openProject(item)"
            >
              <div class="pm-cover" :class="item.coverClass">
                <button class="pm-card-action" aria-label="收藏" type="button" @click.stop="toggleFavorite(item.name)">
                  <SvgIcon :icon="item.favorited ? 'material-symbols:star-rounded' : 'material-symbols:star-outline-rounded'" />
                </button>
              </div>
              <div class="pm-card-body">
                <div class="pm-title-line">
                  <span class="pm-project-icon"><SvgIcon :icon="item.icon" /></span>
                  <h2>{{ item.name }}</h2>
                  <span class="pm-type">{{ item.type }}</span>
                  <span v-if="item.archived" class="pm-archive-badge">归档</span>
                </div>
                <p class="pm-activity">最近活动：{{ item.activity }}</p>
                <div class="pm-metrics">
                  <div>
                    <SvgIcon icon="material-symbols:notifications-active-outline-rounded" />
                    <span>提醒</span>
                    <strong>{{ item.remind }}</strong>
                  </div>
                  <div>
                    <SvgIcon icon="material-symbols:check-circle-outline-rounded" />
                    <span>待办</span>
                    <strong>{{ item.todo }}</strong>
                  </div>
                  <div>
                    <SvgIcon icon="material-symbols:article-outline-rounded" />
                    <span>笔记</span>
                    <strong>{{ item.note }}</strong>
                  </div>
                  <div>
                    <SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" />
                    <span>图册</span>
                    <strong>{{ item.file }}</strong>
                  </div>
                </div>
              </div>
              <footer class="pm-card-footer">
                <span>下次提醒：{{ item.next }}</span>
                <span class="pm-ai" :class="item.aiState"><i></i>AI 总结：{{ item.ai }}</span>
              </footer>
              <div class="pm-card-actions">
                <button type="button" @click.stop="openProjectManage(item)">
                  {{ getProjectManageRouteKey(item.name) ? '管理' : '编辑' }}
                </button>
                <button type="button" :class="{ active: item.archived }" @click.stop="toggleArchive(item.name)">
                  {{ item.archived ? '恢复项目' : '归档项目' }}
                </button>
              </div>
            </article>
          </div>

          <button class="pm-load-more" type="button" @click="loadMoreProjects">加载更多项目 <SvgIcon icon="material-symbols:expand-more-rounded" /></button>

          <div class="pm-tip">
            <span><SvgIcon icon="material-symbols:shield-outline-rounded" /></span>
            <p>
              <strong>项目是你的生活容器</strong>
              将分散在日程、清单、资产、笔记、图册、AI 等模块的内容汇聚在一起，形成完整的项目视图。
            </p>
            <button type="button" @click="showUnavailable('项目说明')">了解更多</button>
          </div>
        </section>

        <aside class="pm-right">
          <section class="pm-panel">
            <div class="pm-panel-head">
              <h2>最近更新</h2>
              <button type="button" @click="showUnavailable('最近更新')">查看全部</button>
            </div>
            <div class="pm-update-list">
              <article v-for="item in recentUpdates" :key="item.title + item.time" class="pm-update">
                <span class="pm-update-icon" :class="item.tone"><SvgIcon :icon="item.icon" /></span>
                <div>
                  <strong>{{ item.title }}</strong>
                  <p>{{ item.desc }}</p>
                </div>
                <time>{{ item.time }}</time>
              </article>
            </div>
          </section>

          <section class="pm-panel">
            <div class="pm-panel-head">
              <h2>项目模板</h2>
              <button type="button" @click="showUnavailable('新建模板')">新建模板</button>
            </div>
            <div class="pm-template-list">
              <article v-for="item in templates" :key="item.title" class="pm-template">
                <span class="pm-update-icon" :class="item.tone"><SvgIcon :icon="item.icon" /></span>
                <div>
                  <strong>{{ item.title }}</strong>
                  <p>{{ item.desc }}</p>
                </div>
              </article>
            </div>
            <button class="pm-all-template" type="button" @click="showUnavailable('模板中心')">
              查看全部模板 <SvgIcon icon="material-symbols:arrow-forward-rounded" />
            </button>
          </section>
        </aside>
      </div>
    </section>

    <LifeModal
      v-model:show="showCreateModal"
      :title="projectModalTitle"
      :description="projectModalDescription"
      :confirm-text="projectModalConfirmText"
      @confirm="saveProject"
    >
      <div class="pm-demo-form">
        <label>
          <span>项目名称</span>
          <input v-model="projectFormName" type="text" maxlength="24" placeholder="例如：夏日摄影计划" />
        </label>
        <label>
          <span>项目类型</span>
          <select v-model="projectFormType">
            <option>游戏</option>
            <option>旅行</option>
            <option>学习</option>
            <option>生活</option>
          </select>
        </label>
        <label>
          <span>最近活动</span>
          <input v-model="projectFormActivity" type="text" maxlength="36" placeholder="例如：完成资料整理" />
        </label>
      </div>
    </LifeModal>
  </main>
</template>

<style scoped>
.pm-page {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 216px minmax(0, 1fr);
  overflow: hidden;
  background:
    linear-gradient(180deg, rgba(255, 255, 255, 0.8), rgba(248, 249, 254, 0.92)),
    #f6f7fb;
  color: #141927;
  font-family:
    Inter,
    'PingFang SC',
    'Microsoft YaHei',
    Arial,
    sans-serif;
}

.pm-page * {
  box-sizing: border-box;
  letter-spacing: 0;
}

button,
input,
select {
  font: inherit;
}

button {
  border: 0;
  cursor: pointer;
}

.pm-sidebar {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 32px 14px 18px;
  border-right: 1px solid #e9ebf4;
  background: rgba(255, 255, 255, 0.78);
  box-shadow: 10px 0 35px rgba(42, 47, 78, 0.03);
}

.pm-brand {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 0 14px 34px;
}

.pm-logo {
  width: 28px;
  height: 28px;
  transform: rotate(45deg);
  border-radius: 4px;
  background: linear-gradient(135deg, #a69bff 0%, #6d56e8 54%, #4e39d2 100%);
  box-shadow: 0 10px 22px rgba(111, 90, 232, 0.28);
}

.pm-brand strong {
  display: block;
  font-size: 17px;
  line-height: 1.15;
  color: #101422;
}

.pm-brand span {
  display: block;
  margin-top: 7px;
  font-size: 11px;
  color: #8c91a3;
}

.pm-nav {
  display: grid;
  gap: 5px;
}

.pm-nav-item {
  display: flex;
  align-items: center;
  gap: 11px;
  height: 32px;
  padding: 0 9px;
  border-radius: 6px;
  background: transparent;
  color: #4c5366;
  font-size: 12px;
  text-align: left;
}

.pm-nav-item .svg-icon {
  width: 15px;
  height: 15px;
  color: #5b6476;
}

.pm-nav-item.active {
  background: linear-gradient(135deg, #7259ee, #7c5dfa);
  color: #fff;
  box-shadow: 0 12px 24px rgba(111, 82, 234, 0.32);
}

.pm-nav-item.active .svg-icon {
  color: #fff;
}

.pm-nav-item:disabled:not(.active) {
  cursor: not-allowed;
  opacity: 0.46;
}

.pm-profile {
  margin-top: auto;
  padding: 0 6px 18px;
}

.pm-profile-main {
  display: flex;
  align-items: center;
  gap: 10px;
  padding-bottom: 18px;
}

.pm-avatar,
.pm-user {
  background:
    radial-gradient(circle at 52% 36%, #ffd9de 0 12%, transparent 13%),
    radial-gradient(circle at 47% 30%, #221e36 0 15%, transparent 16%),
    radial-gradient(circle at 50% 62%, #f5b2c3 0 19%, transparent 20%),
    linear-gradient(135deg, #c7b6ff, #ffd0df);
}

.pm-avatar {
  width: 42px;
  height: 42px;
  border: 2px solid #fff;
  border-radius: 50%;
  box-shadow: 0 9px 20px rgba(116, 98, 190, 0.22);
}

.pm-profile-main strong {
  display: block;
  color: #34384a;
  font-size: 13px;
}

.pm-profile-main strong span {
  margin-left: 4px;
  padding: 1px 6px;
  border-radius: 999px;
  background: #7861ef;
  color: #fff;
  font-size: 9px;
}

.pm-profile-main p {
  margin: 5px 0 0;
  color: #8a8fa1;
  font-size: 11px;
}

.pm-energy {
  border-top: 1px solid #edf0f7;
  padding: 12px 2px 0;
}

.pm-energy div:first-child {
  display: flex;
  justify-content: space-between;
  color: #8a8fa1;
  font-size: 11px;
}

.pm-energy strong {
  color: #4f5365;
}

.pm-energy-bar {
  height: 7px;
  margin-top: 10px;
  overflow: hidden;
  border-radius: 999px;
  background: #edf0fa;
}

.pm-energy-bar i {
  display: block;
  width: 87%;
  height: 100%;
  border-radius: inherit;
  background: linear-gradient(90deg, #8564ef, #7756e7);
}

.pm-sidebar-tools {
  display: flex;
  justify-content: space-between;
  padding: 14px 8px 0;
  border-top: 1px solid #edf0f7;
}

.pm-sidebar-tools button {
  width: 28px;
  height: 28px;
  display: grid;
  place-items: center;
  border-radius: 8px;
  background: transparent;
  color: #687084;
}

.pm-main {
  min-width: 0;
  min-height: 100vh;
  padding: 31px 28px 14px 27px;
}

.pm-header {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 18px;
}

.pm-header h1 {
  margin: 0;
  font-size: 26px;
  line-height: 1.18;
  font-weight: 800;
  color: #0d111d;
}

.pm-header p {
  margin: 9px 0 0;
  color: #676d7f;
  font-size: 14px;
}

.pm-header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
  min-width: 0;
}

.pm-search,
.pm-select,
.pm-primary,
.pm-icon-button,
.pm-user {
  height: 38px;
  border: 1px solid #e7e9f2;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.84);
  box-shadow: 0 8px 24px rgba(70, 75, 112, 0.04);
}

.pm-search {
  display: flex;
  align-items: center;
  gap: 9px;
  width: 231px;
  padding: 0 14px;
  color: #7b8192;
}

.pm-search input {
  width: 100%;
  min-width: 0;
  border: 0;
  outline: 0;
  background: transparent;
  color: #42495a;
  font-size: 12px;
}

.pm-search input::placeholder {
  color: #a5aaba;
}

.pm-select,
.pm-primary {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: 7px;
  padding: 0 14px;
  color: #343b4f;
  font-size: 13px;
  white-space: nowrap;
}

.pm-select-wrap {
  position: relative;
  gap: 8px;
}

.pm-select-wrap select {
  border: 0;
  background: transparent;
  color: #343b4f;
  outline: none;
}

.pm-primary {
  min-width: 97px;
  border: 0;
  background: linear-gradient(135deg, #7359ef, #7055e7);
  color: #fff;
  box-shadow: 0 12px 24px rgba(112, 85, 231, 0.28);
}

.pm-icon-button,
.pm-user {
  position: relative;
  width: 38px;
  display: grid;
  place-items: center;
  color: #70778a;
}

.pm-icon-button.has-dot::after {
  content: '';
  position: absolute;
  top: 8px;
  right: 8px;
  width: 7px;
  height: 7px;
  border: 2px solid #fff;
  border-radius: 50%;
  background: #ef5b84;
}

.pm-user {
  border-radius: 50%;
}

.pm-stats {
  display: grid;
  grid-template-columns: repeat(6, minmax(0, 1fr));
  margin-top: 37px;
  border: 1px solid #eceef5;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.86);
  box-shadow: 0 15px 36px rgba(56, 62, 95, 0.08);
}

.pm-stat-card {
  display: flex;
  align-items: flex-start;
  gap: 17px;
  min-height: 120px;
  padding: 28px 20px 20px;
}

.pm-stat-card + .pm-stat-card {
  border-left: 1px solid #eef0f6;
}

.pm-stat-icon,
.pm-update-icon {
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  border-radius: 8px;
}

.pm-stat-icon {
  width: 36px;
  height: 36px;
  font-size: 24px;
}

.pm-stat-icon.purple,
.pm-update-icon.violet {
  background: #f1ecff;
  color: #755ce8;
}

.pm-stat-icon.coral,
.pm-update-icon.coral {
  background: #fff0ef;
  color: #ef776f;
}

.pm-stat-icon.green,
.pm-update-icon.mint {
  background: #e9fbf4;
  color: #31bd8b;
}

.pm-stat-icon.blue,
.pm-update-icon.blue {
  background: #eaf4ff;
  color: #4a8df0;
}

.pm-stat-icon.violet {
  background: #f0eaff;
  color: #8b62ec;
}

.pm-stat-icon.mint {
  background: #e7fbf3;
  color: #2abf91;
}

.pm-update-icon.amber {
  background: #fff4dc;
  color: #edae32;
}

.pm-update-icon.rose {
  background: #ffeaf2;
  color: #ef6e99;
}

.pm-stat-card span:not(.pm-stat-icon) {
  color: #596071;
  font-size: 13px;
}

.pm-stat-card strong {
  display: block;
  margin-top: 8px;
  color: #111725;
  font-size: 28px;
  line-height: 1;
  font-weight: 700;
}

.pm-stat-card p {
  margin: 14px 0 0;
  color: #8f95a5;
  font-size: 11px;
}

.pm-content-grid {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 292px;
  gap: 42px;
  margin-top: 31px;
}

.pm-projects {
  min-width: 0;
}

.pm-toolbar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 16px;
  margin-bottom: 18px;
}

.pm-tabs,
.pm-view-switch {
  display: inline-flex;
  align-items: center;
  height: 37px;
  border: 1px solid #edf0f6;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.78);
  box-shadow: 0 9px 22px rgba(60, 65, 96, 0.04);
}

.pm-tabs {
  padding: 3px 5px;
  gap: 4px;
}

.pm-tabs button,
.pm-view-switch button {
  height: 29px;
  border-radius: 8px;
  background: transparent;
  color: #4e5567;
  font-size: 13px;
  white-space: nowrap;
}

.pm-tabs button {
  min-width: 58px;
}

.pm-tabs button.active {
  background: linear-gradient(135deg, #7459ef, #7459ef);
  color: #fff;
  box-shadow: 0 8px 17px rgba(112, 86, 232, 0.24);
}

.pm-view-switch {
  padding: 4px;
}

.pm-view-switch button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  padding: 0 12px;
  color: #767d8f;
}

.pm-view-switch button.active {
  background: #f3efff;
  color: #6d55de;
}

.pm-list-scroll {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.pm-list-scroll button {
  display: grid;
  place-items: center;
  width: 31px;
  height: 31px;
  border: 1px solid #edf0f6;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.86);
  color: #735ce8;
  font-size: 20px;
  box-shadow: 0 8px 18px rgba(60, 65, 96, 0.04);
}

.pm-card-grid {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
  max-height: 642px;
  overflow-y: auto;
  padding-right: 4px;
  scroll-behavior: smooth;
}

.pm-card-grid.is-list {
  grid-template-columns: 1fr;
}

.pm-card-grid.is-list .pm-project-card {
  display: grid;
  grid-template-columns: 240px minmax(0, 1fr);
}

.pm-card-grid.is-list .pm-card-footer {
  grid-column: 2;
}

.pm-card-grid.is-list .pm-card-actions {
  grid-column: 2;
}

.pm-project-card {
  overflow: hidden;
  min-width: 0;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.92);
  box-shadow: 0 13px 30px rgba(60, 67, 100, 0.1);
}

.pm-project-card.archived {
  border-color: #dde2ee;
  background: rgba(247, 248, 252, 0.94);
}

.pm-cover {
  position: relative;
  height: 119px;
  overflow: hidden;
  background-color: #dfe9f6;
}

.pm-cover::before,
.pm-cover::after {
  content: '';
  position: absolute;
  inset: 0;
}

.pm-cover.nikki {
  background:
    radial-gradient(circle at 68% 39%, #ffdae8 0 4%, transparent 4.6%),
    radial-gradient(circle at 65% 62%, rgba(255, 255, 255, 0.88) 0 12%, transparent 12.8%),
    linear-gradient(135deg, rgba(111, 170, 218, 0.65), rgba(251, 210, 224, 0.7)),
    url('https://images.unsplash.com/photo-1516541196182-6bdb0516ed27?auto=format&fit=crop&w=900&q=80') center/cover;
}

.pm-cover.nikki::after {
  width: 34%;
  height: 125%;
  left: 42%;
  top: 2%;
  border-radius: 52% 52% 36% 36%;
  background:
    radial-gradient(circle at 50% 16%, #ffe1dc 0 14%, transparent 15%),
    linear-gradient(90deg, transparent 0 35%, #f29ab0 36% 47%, #ffd5df 48% 54%, #f29ab0 55% 67%, transparent 68%),
    linear-gradient(180deg, #fff 32%, #f7b8cf 33% 54%, #fff6fb 55%);
  filter: drop-shadow(0 10px 12px rgba(98, 72, 120, 0.18));
}

.pm-cover.genshin {
  background:
    radial-gradient(circle at 43% 94%, rgba(94, 163, 91, 0.9) 0 17%, transparent 18%),
    radial-gradient(circle at 22% 88%, rgba(117, 184, 111, 0.85) 0 20%, transparent 21%),
    linear-gradient(165deg, #9ed3ff 0 33%, #cce9ff 34% 42%, #8dc58a 43% 100%);
}

.pm-cover.genshin::before {
  clip-path: polygon(9% 100%, 33% 23%, 55% 100%);
  background: linear-gradient(180deg, #e8f5ff, #7fa789);
  opacity: 0.9;
}

.pm-cover.genshin::after {
  width: 22px;
  height: 58px;
  left: 68%;
  top: 43px;
  border-radius: 999px 999px 6px 6px;
  background: linear-gradient(180deg, #20283d 0 22%, #ecd9bb 23% 42%, #293044 43% 100%);
  box-shadow: 0 18px 0 -8px #283146;
}

.pm-cover.japan {
  background:
    radial-gradient(circle at 45% 23%, #fbfcff 0 14%, transparent 14.4%),
    linear-gradient(180deg, #b9d9ef 0 48%, #f2d1d4 49% 58%, #a4b57f 59% 100%);
}

.pm-cover.japan::before {
  clip-path: polygon(16% 70%, 45% 17%, 73% 70%);
  background: linear-gradient(180deg, #fbfbff 0 33%, #7e9ab2 34% 100%);
}

.pm-cover.japan::after {
  width: 74px;
  height: 92px;
  right: 32px;
  bottom: 0;
  background:
    linear-gradient(90deg, transparent 0 9%, #583b28 10% 17%, transparent 18% 82%, #583b28 83% 90%, transparent 91%),
    linear-gradient(180deg, transparent 0 13%, #c43d32 14% 22%, transparent 23% 38%, #2d2a26 39% 44%, #e8dfd1 45% 60%, #be3c31 61% 68%, #e9dfcf 69% 100%);
  clip-path: polygon(50% 0, 100% 15%, 82% 18%, 92% 31%, 78% 34%, 87% 48%, 13% 48%, 22% 34%, 8% 31%, 18% 18%, 0 15%);
}

.pm-cover.openai {
  display: grid;
  place-items: center;
  background:
    radial-gradient(circle at 78% 15%, rgba(255, 255, 255, 0.54), transparent 26%),
    linear-gradient(135deg, #c9b7ff, #e9dcff 45%, #d5b6ff);
}

.pm-cover.openai::before {
  position: static;
  content: '⌬';
  display: grid;
  place-items: center;
  width: 68px;
  height: 68px;
  border-radius: 50%;
  color: rgba(255, 255, 255, 0.86);
  font-size: 70px;
  line-height: 1;
  filter: drop-shadow(0 7px 14px rgba(93, 69, 182, 0.24));
}

.pm-cover.family {
  background:
    radial-gradient(circle at 38% 88%, rgba(97, 151, 68, 0.65) 0 27%, transparent 28%),
    radial-gradient(circle at 83% 7%, rgba(255, 255, 255, 0.75) 0 17%, transparent 18%),
    linear-gradient(180deg, #d8edbc 0 38%, #7aa85f 39% 100%);
}

.pm-cover.family::after {
  width: 102px;
  height: 56px;
  left: 35%;
  bottom: 5px;
  background:
    radial-gradient(circle at 14% 26%, #f5d2b3 0 9%, transparent 10%),
    radial-gradient(circle at 45% 17%, #f7d9bb 0 9%, transparent 10%),
    radial-gradient(circle at 76% 27%, #f0c8a8 0 9%, transparent 10%),
    linear-gradient(90deg, transparent 0 7%, #f5f0e9 8% 27%, transparent 28% 38%, #fff4ed 39% 58%, transparent 59% 69%, #eef4ff 70% 90%, transparent 91%);
}

.pm-cover.health {
  background:
    radial-gradient(circle at 67% 56%, #35bfd0 0 16%, transparent 17%),
    linear-gradient(90deg, transparent 0 56%, #41c4d1 57% 86%, transparent 87%),
    linear-gradient(180deg, #dff0f6 0 48%, #f3f7fa 49% 100%);
}

.pm-cover.health::before {
  width: 44px;
  height: 88px;
  left: 33%;
  bottom: 12px;
  border-radius: 9px 9px 8px 8px;
  background:
    linear-gradient(180deg, #35bebd 0 10%, transparent 11% 22%, #7ee1df 23% 100%),
    linear-gradient(90deg, transparent 0 20%, rgba(255, 255, 255, 0.46) 21% 33%, transparent 34%);
  opacity: 0.92;
}

.pm-card-action {
  position: absolute;
  right: 13px;
  top: 13px;
  z-index: 1;
  display: grid;
  place-items: center;
  width: 20px;
  height: 20px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.9);
  color: #8d93a3;
  box-shadow: 0 5px 10px rgba(50, 55, 80, 0.12);
}

.pm-card-body {
  padding: 16px 13px 12px;
}

.pm-title-line {
  display: flex;
  align-items: center;
  gap: 8px;
  min-width: 0;
}

.pm-project-icon {
  display: grid;
  place-items: center;
  flex: 0 0 auto;
  width: 22px;
  height: 22px;
  color: #7560e9;
  font-size: 22px;
}

.pm-title-line h2 {
  min-width: 0;
  margin: 0;
  overflow: hidden;
  color: #121724;
  font-size: 17px;
  line-height: 1.25;
  font-weight: 760;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pm-type {
  flex: 0 0 auto;
  padding: 2px 7px;
  border-radius: 6px;
  background: #eef8f4;
  color: #4f9b8a;
  font-size: 11px;
}

.pm-archive-badge {
  flex: 0 0 auto;
  padding: 2px 7px;
  border-radius: 6px;
  background: #f1f3f8;
  color: #7e8494;
  font-size: 11px;
}

.pm-activity {
  margin: 9px 0 14px;
  color: #6c7282;
  font-size: 12px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pm-metrics {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 4px;
}

.pm-metrics div {
  display: grid;
  grid-template-columns: 1fr;
  justify-items: center;
  gap: 5px;
  color: #6f7688;
  font-size: 11px;
}

.pm-metrics .svg-icon {
  color: #826de5;
}

.pm-metrics strong {
  color: #202535;
  font-size: 12px;
  font-weight: 650;
}

.pm-card-footer {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  min-height: 40px;
  padding: 0 13px;
  border-top: 1px solid #edf0f6;
  color: #6f7688;
  font-size: 11px;
}

.pm-ai {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  white-space: nowrap;
}

.pm-ai i {
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #3ec8a2;
}

.pm-ai.pending i {
  background: #f2b64d;
}

.pm-ai.todo i {
  background: #ef5b84;
}

.pm-card-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 10px 13px 13px;
}

.pm-card-actions button {
  height: 28px;
  padding: 0 12px;
  border: 1px solid #e6e9f2;
  border-radius: 8px;
  background: #fff;
  color: #61697b;
  font-size: 12px;
}

.pm-card-actions button.active {
  border-color: #ded7ff;
  background: #f3efff;
  color: #735ce8;
}

.pm-load-more {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 5px;
  margin: 16px auto 18px;
  background: transparent;
  color: #765ee8;
  font-size: 13px;
}

.pm-tip {
  display: flex;
  align-items: center;
  gap: 13px;
  min-height: 55px;
  padding: 10px 14px;
  border: 1px solid #e8ebf3;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.76);
  color: #6d7484;
  box-shadow: 0 9px 23px rgba(70, 75, 110, 0.04);
}

.pm-tip span {
  display: grid;
  place-items: center;
  width: 27px;
  height: 27px;
  border-radius: 8px;
  background: #e6f6ff;
  color: #3a9cea;
}

.pm-tip p {
  flex: 1;
  margin: 0;
  font-size: 12px;
  line-height: 1.5;
}

.pm-tip strong {
  display: block;
  margin-bottom: 2px;
  color: #4a5062;
}

.pm-tip button {
  height: 28px;
  padding: 0 14px;
  border: 1px solid #e5e8f0;
  border-radius: 8px;
  background: #fff;
  color: #6a7081;
  font-size: 12px;
}

.pm-right {
  display: grid;
  align-content: start;
  gap: 18px;
}

.pm-panel {
  padding: 18px 15px 16px;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.88);
  box-shadow: 0 13px 30px rgba(60, 67, 100, 0.08);
}

.pm-panel-head {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 10px;
  margin-bottom: 17px;
}

.pm-panel-head h2 {
  margin: 0;
  color: #151a27;
  font-size: 16px;
  line-height: 1.25;
  font-weight: 760;
}

.pm-panel-head button,
.pm-all-template {
  background: transparent;
  color: #8068eb;
  font-size: 12px;
  white-space: nowrap;
}

.pm-update-list,
.pm-template-list {
  display: grid;
  gap: 18px;
}

.pm-update,
.pm-template {
  display: grid;
  grid-template-columns: 32px minmax(0, 1fr) auto;
  gap: 11px;
  align-items: center;
}

.pm-template {
  grid-template-columns: 36px minmax(0, 1fr);
}

.pm-update-icon {
  width: 32px;
  height: 32px;
  font-size: 18px;
}

.pm-template .pm-update-icon {
  width: 36px;
  height: 36px;
  font-size: 19px;
}

.pm-update strong,
.pm-template strong {
  display: block;
  overflow: hidden;
  color: #2c3142;
  font-size: 12px;
  line-height: 1.25;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pm-update p,
.pm-template p {
  margin: 5px 0 0;
  overflow: hidden;
  color: #8b91a1;
  font-size: 11px;
  line-height: 1.3;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.pm-update time {
  color: #8b91a1;
  font-size: 11px;
  white-space: nowrap;
}

.pm-all-template {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  margin-top: 21px;
}

.pm-demo-form {
  display: grid;
  gap: 14px;
}

.pm-demo-form label {
  display: grid;
  gap: 8px;
}

.pm-demo-form span {
  color: #697183;
  font-size: 12px;
}

.pm-demo-form input,
.pm-demo-form select {
  height: 38px;
  border: 1px solid #e7e9f2;
  border-radius: 8px;
  background: #fff;
  padding: 0 12px;
  color: #1d2332;
  outline: none;
}

@media (max-width: 1320px) {
  .pm-page {
    grid-template-columns: 196px minmax(0, 1fr);
  }

  .pm-header {
    flex-direction: column;
  }

  .pm-header-actions {
    width: 100%;
    flex-wrap: wrap;
  }

  .pm-content-grid {
    grid-template-columns: 1fr;
  }

  .pm-right {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 1080px) {
  .pm-stats {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .pm-stat-card:nth-child(4) {
    border-left: 0;
  }

  .pm-card-grid {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}

@media (max-width: 820px) {
  .pm-page {
    display: block;
    overflow: visible;
  }

  .pm-sidebar {
    min-height: auto;
    padding: 18px;
  }

  .pm-nav {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }

  .pm-nav-item {
    justify-content: center;
  }

  .pm-nav-item span {
    display: none;
  }

  .pm-profile,
  .pm-sidebar-tools {
    display: none;
  }

  .pm-main {
    padding: 22px 16px;
  }

  .pm-search {
    width: 100%;
  }

  .pm-stats,
  .pm-card-grid,
  .pm-right,
  .pm-card-grid.is-list .pm-project-card {
    grid-template-columns: 1fr;
  }

  .pm-stat-card + .pm-stat-card,
  .pm-stat-card:nth-child(4) {
    border-left: 0;
    border-top: 1px solid #eef0f6;
  }

  .pm-toolbar {
    align-items: flex-start;
    flex-direction: column;
  }

  .pm-tabs {
    width: 100%;
    overflow-x: auto;
  }

  .pm-card-footer,
  .pm-tip {
    align-items: flex-start;
    flex-direction: column;
  }
}
</style>

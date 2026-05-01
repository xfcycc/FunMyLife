<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue';
import { ChevronRight, MoreHorizontal, Plus, Settings, Share, Star } from 'lucide-vue-next';
import { useMessage } from 'naive-ui';
import LifeGeminiProjectHero from '@/components/life-manager/LifeGeminiProjectHero.vue';
import LifeGeminiShell from '@/components/life-manager/LifeGeminiShell.vue';
import LifeGeminiTabs from '@/components/life-manager/LifeGeminiTabs.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type {
  AiOverview,
  AssetOverview,
  CountdownOverview,
  DailyTaskOverview,
  DetailModalData,
  GalleryOverview,
  NikkiActivity,
  NikkiProject,
  NoteOverview
} from './types';
import {
  fetchActivities,
  fetchAiOverview,
  fetchAssetOverview,
  fetchCountdownOverview,
  fetchDailyTaskOverview,
  fetchGalleryOverview,
  fetchNoteOverview,
  fetchProject,
  toggleTask as apiToggleTask,
  toggleVersionReminder as apiToggleVersionReminder
} from './service';
import OverviewTab from './tabs/OverviewTab.vue';
import DailyTab from './tabs/DailyTab.vue';
import CountdownTab from './tabs/CountdownTab.vue';
import AssetsTab from './tabs/AssetsTab.vue';
import NotesTab from './tabs/NotesTab.vue';
import GalleryTab from './tabs/GalleryTab.vue';
import AiTab from './tabs/AiTab.vue';

defineOptions({
  name: 'InfinityNikki'
});

const message = useMessage();

// ========== 状态 ==========

const activeTab = ref('概览');
const tabs = ['概览', '日常', '倒计时', '资产', '笔记', '图册', 'AI'];

const project = ref<NikkiProject>({
  id: '',
  name: '无限暖暖',
  description: '',
  coverSrc: '',
  coverAlt: '',
  status: 'active',
  createdAt: '',
  tags: [],
  stats: []
});

const dailyData = ref<DailyTaskOverview>({
  completed: 0,
  total: 0,
  taskGroups: [],
  weeklyGoals: []
});

const countdownData = ref<CountdownOverview>({
  events: [],
  versionNodes: []
});

const assetData = ref<AssetOverview>({
  total: 0,
  assets: []
});

const noteData = ref<NoteOverview>({
  total: 0,
  notes: []
});

const galleryData = ref<GalleryOverview>({
  albumCount: 0,
  photoCount: 0,
  albums: [],
  recentPhotos: []
});

const aiData = ref<AiOverview>({
  currentSuggestion: {
    id: '',
    content: '',
    highlights: [],
    generatedAt: '',
    status: 'pending'
  },
  suggestionCount: 0
});

const activities = ref<NikkiActivity[]>([]);

// ========== 弹框状态 ==========

const showQuickRecord = ref(false);
const quickRecordContent = ref('');

const detailModal = reactive<DetailModalData>({
  show: false,
  title: '',
  type: '',
  icon: '',
  meta: [],
  content: ''
});

// ========== 数据获取 ==========

async function fetchProjectData() {
  const res = await fetchProject();
  project.value = res.data;
}

async function fetchDaily() {
  const res = await fetchDailyTaskOverview();
  dailyData.value = res.data;
}

async function fetchCountdown() {
  const res = await fetchCountdownOverview();
  countdownData.value = res.data;
}

async function fetchAssets() {
  const res = await fetchAssetOverview();
  assetData.value = res.data;
}

async function fetchNotes() {
  const res = await fetchNoteOverview();
  noteData.value = res.data;
}

async function fetchGallery() {
  const res = await fetchGalleryOverview();
  galleryData.value = res.data;
}

async function fetchAi() {
  const res = await fetchAiOverview();
  aiData.value = res.data;
}

async function fetchActivityList() {
  const res = await fetchActivities();
  activities.value = res.data;
}

// ========== 事件处理 ==========

async function handleToggleTask(taskId: string, done: boolean) {
  await apiToggleTask(taskId, done);
  message.success(done ? '任务已完成' : '任务已恢复');
  fetchDaily();
  fetchProjectData();
}

async function handleToggleVersionReminder(nodeId: string, reminded: boolean) {
  await apiToggleVersionReminder(nodeId, reminded);
  message.success(reminded ? '已取消提醒' : '已设置提醒');
  fetchCountdown();
}

function handleOpenDetail(payload: { type: string; id: string }) {
  const { type, id } = payload;

  if (type === 'event') {
    const event = countdownData.value.events.find(e => e.id === id);
    if (event) {
      detailModal.title = event.title;
      detailModal.content = event.description ?? '';
      detailModal.meta = [
        { label: '结束时间', value: event.endTime },
        { label: '剩余天数', value: `${event.remainingDays} 天` },
        { label: '提醒状态', value: event.reminded ? '已设置' : '未设置' },
        { label: '打卡状态', value: event.checkedIn ? '已打卡' : '未打卡' }
      ];
      detailModal.show = true;
    }
  } else if (type === 'version') {
    const node = countdownData.value.versionNodes.find(n => n.id === id);
    if (node) {
      detailModal.title = node.title;
      detailModal.meta = [
        { label: '类型', value: node.type === 'live' ? '直播预告' : '版本维护' },
        { label: '开始时间', value: node.startTime },
        { label: '距离开始', value: `${node.daysUntil} 天` },
        { label: '提醒状态', value: node.reminded ? '已设置' : '未设置' }
      ];
      detailModal.show = true;
    }
  } else if (type === 'asset') {
    const asset = assetData.value.assets.find(a => a.id === id);
    if (asset) {
      detailModal.title = asset.name;
      detailModal.content = asset.description ?? '';
      detailModal.meta = [
        { label: '安全状态', value: asset.statusLabel },
        { label: '资产类型', value: asset.type }
      ];
      detailModal.show = true;
    }
  } else if (type === 'note') {
    const note = noteData.value.notes.find(n => n.id === id);
    if (note) {
      detailModal.title = note.title;
      detailModal.content = note.content ?? '';
      detailModal.meta = [
        { label: '分类', value: note.categoryLabel },
        { label: '创建时间', value: note.createdAt },
        { label: '更新时间', value: note.updatedAt }
      ];
      detailModal.show = true;
    }
  } else if (type === 'photo') {
    const photo = galleryData.value.recentPhotos.find(p => p.id === id);
    if (photo) {
      detailModal.title = photo.caption ?? '照片详情';
      detailModal.meta = [
        { label: '拍摄时间', value: photo.takenAt }
      ];
      detailModal.show = true;
    }
  }
}

function handleShare() {
  message.info('分享链接已复制到剪贴板');
}

function handleSettings() {
  message.info('请前往项目管理页进行设置');
}

function handleQuickRecordConfirm() {
  if (!quickRecordContent.value.trim()) {
    message.warning('请先输入记录内容');
    return;
  }
  message.success('记录已保存');
  quickRecordContent.value = '';
  showQuickRecord.value = false;
  fetchActivityList();
}

// ========== 初始化 ==========

onMounted(() => {
  fetchProjectData();
  fetchDaily();
  fetchCountdown();
  fetchAssets();
  fetchNotes();
  fetchGallery();
  fetchAi();
  fetchActivityList();
});
</script>

<template>
  <LifeGeminiShell
    title="无限暖暖"
    description="项目概览、日常进度、活动倒计时和最近动态。"
    :breadcrumbs="[
      { label: '首页', routeKey: 'home' },
      { label: '项目', routeKey: 'projects' },
      { label: '无限暖暖' }
    ]"
  >
    <LifeGeminiProjectHero
      :title="project.name"
      :description="project.description"
      :cover-src="project.coverSrc"
      :cover-alt="project.coverAlt"
      :tags="project.tags"
      :stats="project.stats"
    >
      <template #title-icon>
        <Star class="text-amber-400 fill-amber-400 w-5 h-5" />
      </template>

      <template #actions>
        <button
          class="bg-indigo-500 hover:bg-indigo-600 text-white px-4 py-2 rounded-xl font-medium text-sm flex items-center space-x-1.5"
          type="button"
          @click="showQuickRecord = true"
        >
          <Plus :size="16" />
          <span>快速记录</span>
          <ChevronRight :size="14" class="ml-1 opacity-70" />
        </button>
        <button
          class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5"
          type="button"
          @click="handleShare"
        >
          <Share :size="14" />
          <span>分享</span>
        </button>
        <button
          class="px-4 py-2 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 text-sm font-medium flex items-center space-x-1.5"
          type="button"
          @click="handleSettings"
        >
          <Settings :size="14" />
          <span>设置</span>
        </button>
        <button
          class="w-10 h-10 bg-white border border-slate-200 text-slate-600 rounded-xl hover:bg-slate-50 flex items-center justify-center"
          type="button"
        >
          <MoreHorizontal :size="16" />
        </button>
      </template>
    </LifeGeminiProjectHero>

    <LifeGeminiTabs v-model="activeTab" :tabs="tabs" />

    <!-- 概览 -->
    <OverviewTab
      v-if="activeTab === '概览'"
      :daily-data="dailyData"
      :weekly-goals="dailyData.weeklyGoals"
      :countdown-data="countdownData"
      :asset-data="assetData"
      :note-data="noteData"
      :gallery-data="galleryData"
      :ai-data="aiData"
      :activities="activities"
      @switch-tab="activeTab = $event"
      @toggle-task="handleToggleTask"
      @toggle-version-reminder="handleToggleVersionReminder"
      @open-detail="handleOpenDetail"
    />

    <!-- 日常 -->
    <DailyTab
      v-if="activeTab === '日常'"
      :daily-data="dailyData"
      @refresh="fetchDaily"
    />

    <!-- 倒计时 -->
    <CountdownTab
      v-if="activeTab === '倒计时'"
      :countdown-data="countdownData"
      @refresh="fetchCountdown"
      @open-detail="handleOpenDetail"
    />

    <!-- 资产 -->
    <AssetsTab
      v-if="activeTab === '资产'"
      :asset-data="assetData"
    />

    <!-- 笔记 -->
    <NotesTab
      v-if="activeTab === '笔记'"
      :note-data="noteData"
    />

    <!-- 图册 -->
    <GalleryTab
      v-if="activeTab === '图册'"
      :gallery-data="galleryData"
    />

    <!-- AI -->
    <AiTab
      v-if="activeTab === 'AI'"
      :ai-data="aiData"
      @refresh="fetchAi"
    />

    <!-- 快速记录弹框 -->
    <LifeModal
      v-model:show="showQuickRecord"
      title="快速记录"
      description="记录一条游戏动态或备忘"
      :width="480"
      @confirm="handleQuickRecordConfirm"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">记录内容</label>
          <textarea
            v-model="quickRecordContent"
            class="w-full border border-slate-200 rounded-xl px-3 py-2 text-sm text-slate-700 placeholder-slate-400 focus:outline-none focus:ring-2 focus:ring-indigo-200 focus:border-indigo-300 resize-none"
            rows="3"
            placeholder="输入要记录的内容..."
          ></textarea>
        </div>
      </div>
    </LifeModal>

    <!-- 详情弹框 -->
    <LifeModal
      v-model:show="detailModal.show"
      :title="detailModal.title"
      :width="520"
      :show-footer="false"
    >
      <div v-if="detailModal.content" class="text-sm text-slate-600 leading-relaxed mb-4">
        {{ detailModal.content }}
      </div>
      <div class="nikki-modal-meta">
        <div v-for="meta in detailModal.meta" :key="meta.label" class="meta-row">
          <span class="meta-label">{{ meta.label }}</span>
          <span class="meta-value">{{ meta.value }}</span>
        </div>
      </div>
    </LifeModal>
  </LifeGeminiShell>
</template>

<style lang="scss" src="./styles.scss"></style>

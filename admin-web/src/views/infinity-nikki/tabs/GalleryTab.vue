<script setup lang="ts">
import { reactive, ref } from 'vue';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { AbilityInstanceConfig, GameActivity, GameTarget, GameVersion, NikkiAlbum, NikkiPhoto, GalleryOverview, TimelineEvent } from '../types';

defineOptions({ name: 'GalleryTab' });

const props = defineProps<{
  galleryData: GalleryOverview;
  gameVersions: GameVersion[];
  gameActivities: GameActivity[];
  gameTargets: GameTarget[];
  abilityConfig?: AbilityInstanceConfig;
}>();

const emit = defineEmits<{
  createPhoto: [photo: NikkiPhoto];
  updatePhoto: [photo: NikkiPhoto];
  deletePhoto: [id: string];
  appendTimeline: [event: TimelineEvent];
}>();

const showPhotoDetail = ref(false);
const detailPhoto = ref<NikkiPhoto | null>(null);
const showCreatePhoto = ref(false);
const showEditPhoto = ref(false);
const pendingDeletePhoto = ref<NikkiPhoto | null>(null);
const photoForm = reactive({
  albumId: '',
  caption: '',
  url: '',
  versionId: '',
  activityId: '',
  targetId: ''
});
const editPhotoForm = reactive({
  id: '',
  albumId: '',
  caption: '',
  url: '',
  versionId: '',
  activityId: '',
  targetId: ''
});

function albumName(albumId: string) {
  return props.galleryData.albums.find(a => a.id === albumId)?.name ?? '未知图册';
}

function openAlbum(_album: NikkiAlbum) {
  // TODO: 打开图册详情（当前仅展示）
}

function openPhoto(photo: NikkiPhoto) {
  detailPhoto.value = photo;
  showPhotoDetail.value = true;
}

function appendPhotoTimeline(photo: NikkiPhoto) {
  emit('appendTimeline', {
    id: `photo-${photo.id}-${Date.now()}`,
    projectId: 'nikki-001',
    occurredAt: new Date().toISOString(),
    type: 'photo_uploaded',
    title: '图册记录',
    description: photo.caption ?? albumName(photo.albumId),
    sourceBlockKey: 'gallery',
    versionId: photo.versionId,
    activityId: photo.activityId,
    targetId: photo.targetId,
    sensitivity: 'normal',
    displayInOverview: props.abilityConfig?.timeline?.defaultWriteRule.displayInOverview ?? true,
    aiReadable: props.abilityConfig?.timeline?.defaultWriteRule.aiReadable ?? true
  });
}

function resetPhotoForm() {
  photoForm.albumId = props.galleryData.albums[0]?.id ?? '';
  photoForm.caption = '';
  photoForm.url = '';
  photoForm.versionId = '';
  photoForm.activityId = '';
  photoForm.targetId = '';
}

function openEditPhoto(photo: NikkiPhoto) {
  editPhotoForm.id = photo.id;
  editPhotoForm.albumId = photo.albumId;
  editPhotoForm.caption = photo.caption ?? '';
  editPhotoForm.url = photo.url;
  editPhotoForm.versionId = photo.versionId ?? '';
  editPhotoForm.activityId = photo.activityId ?? '';
  editPhotoForm.targetId = photo.targetId ?? '';
  showEditPhoto.value = true;
}

function handleUpdatePhoto() {
  const photo = props.galleryData.recentPhotos.find(item => item.id === editPhotoForm.id);
  if (!photo) {
    return;
  }

  emit('updatePhoto', {
    ...photo,
    albumId: editPhotoForm.albumId || photo.albumId,
    caption: editPhotoForm.caption.trim() || undefined,
    url: editPhotoForm.url.trim() || photo.url,
    thumbnail: editPhotoForm.url.trim() || photo.thumbnail,
    versionId: editPhotoForm.versionId || undefined,
    activityId: editPhotoForm.activityId || undefined,
    targetId: editPhotoForm.targetId || undefined
  });
  detailPhoto.value = null;
  showPhotoDetail.value = false;
  showEditPhoto.value = false;
}

function openDeletePhotoConfirm(photo: NikkiPhoto) {
  pendingDeletePhoto.value = photo;
}

function handleDeletePhoto() {
  if (!pendingDeletePhoto.value) {
    return;
  }

  emit('deletePhoto', pendingDeletePhoto.value.id);
  if (detailPhoto.value?.id === pendingDeletePhoto.value.id) {
    detailPhoto.value = null;
    showPhotoDetail.value = false;
  }
  pendingDeletePhoto.value = null;
}

function handleCreatePhoto() {
  const albumId = photoForm.albumId || props.galleryData.albums[0]?.id;
  if (!albumId) {
    return;
  }

  const photo: NikkiPhoto = {
    id: `photo-${Date.now()}`,
    albumId,
    url: photoForm.url.trim() || props.galleryData.albums.find(album => album.id === albumId)?.coverUrl || '',
    thumbnail: photoForm.url.trim() || props.galleryData.albums.find(album => album.id === albumId)?.coverUrl || '',
    caption: photoForm.caption.trim() || '新照片',
    takenAt: new Date().toISOString(),
    versionId: photoForm.versionId || undefined,
    activityId: photoForm.activityId || undefined,
    targetId: photoForm.targetId || undefined
  };
  emit('createPhoto', photo);
  appendPhotoTimeline(photo);
  resetPhotoForm();
  showCreatePhoto.value = false;
}
</script>

<template>
  <div class="space-y-6">
    <!-- 图册列表 -->
    <LifeGeminiCard :title="abilityConfig?.displayName ?? '图册'" :action-text="`共 ${galleryData.albumCount} 个图册，${galleryData.photoCount} 张`">
      <div class="grid grid-cols-2 sm:grid-cols-3 md:grid-cols-4 gap-4">
        <div
          v-for="album in galleryData.albums"
          :key="album.id"
          class="group cursor-pointer"
          @click="openAlbum(album)"
        >
          <div class="aspect-[4/3] rounded-xl overflow-hidden bg-slate-100 relative mb-2">
            <img :src="album.coverUrl" :alt="album.name" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300" />
            <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors"></div>
            <span class="absolute bottom-2 right-2 text-[10px] text-white bg-black/50 px-1.5 py-0.5 rounded">
              {{ album.photoCount }} 张
            </span>
          </div>
          <p class="text-xs font-medium text-slate-700 group-hover:text-indigo-600 transition-colors truncate">{{ album.name }}</p>
        </div>
      </div>
    </LifeGeminiCard>

    <LifeGeminiCard title="图册配置">
      <div class="grid grid-cols-1 gap-3 text-xs sm:grid-cols-3">
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">字段</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.fields?.length ?? 0 }} 个</p>
        </div>
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">写入时间轴</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.timeline?.enabled ? '开启' : '关闭' }}</p>
        </div>
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">AI 可读</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.timeline?.defaultWriteRule.aiReadable ? '是' : '否' }}</p>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 最近照片 -->
    <LifeGeminiCard title="最近照片" action-text="新增照片" @action="showCreatePhoto = true">
      <div class="grid grid-cols-3 sm:grid-cols-4 md:grid-cols-6 gap-3">
        <div
          v-for="photo in galleryData.recentPhotos"
          :key="photo.id"
          class="aspect-[4/3] rounded-lg overflow-hidden bg-slate-100 cursor-pointer group relative"
          @click="openPhoto(photo)"
        >
          <img :src="photo.thumbnail" :alt="photo.caption" class="w-full h-full object-cover group-hover:scale-110 transition-transform duration-300" />
          <div class="absolute inset-0 bg-black/0 group-hover:bg-black/10 transition-colors"></div>
          <div v-if="photo.caption" class="absolute bottom-0 left-0 right-0 bg-gradient-to-t from-black/50 to-transparent p-1.5 opacity-0 group-hover:opacity-100 transition-opacity">
            <p class="text-[10px] text-white truncate">{{ photo.caption }}</p>
          </div>
        </div>
      </div>
    </LifeGeminiCard>

    <LifeModal
      v-model:show="showCreatePhoto"
      title="新增照片"
      description="照片会进入图册，并按配置写入时间轴"
      confirm-text="添加照片"
      :width="520"
      @confirm="handleCreatePhoto"
      @close="resetPhotoForm"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">所属图册</label>
          <select v-model="photoForm.albumId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
            <option value="">默认图册</option>
            <option v-for="album in galleryData.albums" :key="album.id" :value="album.id">{{ album.name }}</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <input v-model="photoForm.caption" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="照片说明" />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">图片地址</label>
          <input v-model="photoForm.url" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="可先留空，使用图册封面占位" />
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联版本</label>
            <select v-model="photoForm.versionId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="version in gameVersions" :key="version.id" :value="version.id">{{ version.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联活动</label>
            <select v-model="photoForm.activityId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="activity in gameActivities" :key="activity.id" :value="activity.id">{{ activity.title }}</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联目标</label>
            <select v-model="photoForm.targetId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="target in gameTargets" :key="target.id" :value="target.id">{{ target.title }}</option>
            </select>
          </div>
        </div>
      </div>
    </LifeModal>

    <!-- 照片详情弹框 -->
    <LifeModal
      v-model:show="showPhotoDetail"
      :title="detailPhoto?.caption ?? '照片详情'"
      :width="600"
    >
      <div v-if="detailPhoto" class="space-y-4">
        <div class="rounded-xl overflow-hidden bg-slate-100">
          <img :src="detailPhoto.url" :alt="detailPhoto.caption" class="w-full object-contain max-h-[400px]" />
        </div>
        <div class="nikki-modal-meta">
          <div class="meta-row">
            <span class="meta-label">所属图册</span>
            <span class="meta-value">{{ albumName(detailPhoto.albumId) }}</span>
          </div>
          <div class="meta-row">
            <span class="meta-label">拍摄时间</span>
            <span class="meta-value">{{ detailPhoto.takenAt }}</span>
          </div>
        </div>
        <button
          class="nikki-action-btn outline-indigo"
          type="button"
          @click="appendPhotoTimeline(detailPhoto)"
        >
          写入时间轴
        </button>
        <div class="flex flex-wrap gap-2">
          <button class="nikki-action-btn outline-indigo" type="button" @click="openEditPhoto(detailPhoto)">
            编辑照片
          </button>
          <button class="nikki-action-btn outline-rose" type="button" @click="openDeletePhotoConfirm(detailPhoto)">
            删除照片
          </button>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      v-model:show="showEditPhoto"
      title="编辑照片"
      confirm-text="保存"
      :width="520"
      @confirm="handleUpdatePhoto"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">所属图册</label>
          <select v-model="editPhotoForm.albumId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
            <option v-for="album in galleryData.albums" :key="album.id" :value="album.id">{{ album.name }}</option>
          </select>
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <input v-model="editPhotoForm.caption" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" />
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">图片地址</label>
          <input v-model="editPhotoForm.url" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" />
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-3">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联版本</label>
            <select v-model="editPhotoForm.versionId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="version in gameVersions" :key="version.id" :value="version.id">{{ version.name }}</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联活动</label>
            <select v-model="editPhotoForm.activityId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="activity in gameActivities" :key="activity.id" :value="activity.id">{{ activity.title }}</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">关联目标</label>
            <select v-model="editPhotoForm.targetId" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="">不关联</option>
              <option v-for="target in gameTargets" :key="target.id" :value="target.id">{{ target.title }}</option>
            </select>
          </div>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      :show="Boolean(pendingDeletePhoto)"
      title="删除照片"
      :description="pendingDeletePhoto ? `确认删除「${pendingDeletePhoto.caption ?? '照片记录'}」？` : ''"
      confirm-text="删除"
      cancel-text="取消"
      :width="420"
      @update:show="value => { if (!value) pendingDeletePhoto = null; }"
      @confirm="handleDeletePhoto"
    />
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { NikkiAlbum, NikkiPhoto, GalleryOverview } from '../types';

defineOptions({ name: 'GalleryTab' });

const props = defineProps<{
  galleryData: GalleryOverview;
}>();

const showPhotoDetail = ref(false);
const detailPhoto = ref<NikkiPhoto | null>(null);

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
</script>

<template>
  <div class="space-y-6">
    <!-- 图册列表 -->
    <LifeGeminiCard title="图册" :action-text="`共 ${galleryData.albumCount} 个图册，${galleryData.photoCount} 张`">
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

    <!-- 最近照片 -->
    <LifeGeminiCard title="最近照片" action-text="查看全部">
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
      </div>
    </LifeModal>
  </div>
</template>

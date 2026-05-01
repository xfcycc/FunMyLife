<script setup lang="ts">
import { computed, ref } from 'vue';
import { BookOpen, Compass, FileText, Palette, ScrollText, Swords } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { NikkiNote, NoteCategory, NoteOverview } from '../types';

defineOptions({ name: 'NotesTab' });

const props = defineProps<{
  noteData: NoteOverview;
}>();

const notes = computed(() => props.noteData.notes);

const activeCategory = ref<NoteCategory | 'all'>('all');
const showDetail = ref(false);
const detailNote = ref<NikkiNote | null>(null);

const categoryOptions = [
  { value: 'all' as const, label: '全部' },
  { value: 'strategy' as const, label: '攻略' },
  { value: 'explore' as const, label: '探索' },
  { value: 'coordination' as const, label: '搭配' },
  { value: 'material' as const, label: '素材' },
  { value: 'story' as const, label: '剧情' }
];

const filteredNotes = computed(() => {
  if (activeCategory.value === 'all') return notes.value;
  return notes.value.filter(n => n.category === activeCategory.value);
});

function openNoteDetail(note: NikkiNote) {
  detailNote.value = note;
  showDetail.value = true;
}

function categoryIcon(cat: NoteCategory) {
  const map = {
    strategy: Swords,
    explore: Compass,
    coordination: Palette,
    material: ScrollText,
    story: FileText
  };
  return map[cat] ?? BookOpen;
}

function categoryColor(cat: NoteCategory) {
  const map = {
    strategy: 'text-rose-400',
    explore: 'text-emerald-400',
    coordination: 'text-purple-400',
    material: 'text-amber-400',
    story: 'text-blue-400'
  };
  return map[cat] ?? 'text-slate-400';
}
</script>

<template>
  <div class="space-y-6">
    <LifeGeminiCard title="项目笔记" :action-text="`共 ${notes.length} 篇`">
      <template #action>
        <div class="flex items-center gap-3">
          <div class="nikki-group-switch">
            <button
              v-for="cat in categoryOptions"
              :key="cat.value"
              class="group-btn" :class="[activeCategory === cat.value ? 'active' : '']"
              type="button"
              @click="activeCategory = cat.value"
            >
              {{ cat.label }}
            </button>
          </div>
          <span class="text-xs text-slate-400">共 {{ filteredNotes.length }} 篇</span>
        </div>
      </template>
      <div class="space-y-4 max-h-[500px] overflow-y-auto pr-1">
        <div
          v-for="note in filteredNotes"
          :key="note.id"
          class="nikki-note-item p-3 -mx-3 rounded-xl hover:bg-slate-50 transition-colors"
          @click="openNoteDetail(note)"
        >
          <div class="flex items-start space-x-3 flex-1 min-w-0">
            <div class="mt-0.5 shrink-0">
              <component :is="categoryIcon(note.category)" :size="16" :class="categoryColor(note.category)" />
            </div>
            <div class="min-w-0 flex-1">
              <p class="note-title text-sm font-medium text-slate-700 transition-colors line-clamp-1">{{ note.title }}</p>
              <p v-if="note.content" class="text-xs text-slate-500 mt-1 line-clamp-2">{{ note.content }}</p>
              <div class="flex items-center gap-2 mt-2">
                <span class="note-category">{{ note.categoryLabel }}</span>
                <span class="text-[10px] text-slate-400">{{ note.relativeTime }}</span>
              </div>
            </div>
          </div>
        </div>
        <div v-if="filteredNotes.length === 0" class="nikki-empty-state">
          <BookOpen :size="32" />
          <p>{{ activeCategory === 'all' ? '暂无项目笔记' : '该分类暂无笔记' }}</p>
        </div>
      </div>
    </LifeGeminiCard>

    <!-- 笔记详情弹框 -->
    <LifeModal
      v-model:show="showDetail"
      :title="detailNote?.title ?? '笔记详情'"
      :width="560"
    >
      <div v-if="detailNote" class="space-y-4">
        <div class="flex items-center gap-2">
          <span class="note-category">{{ detailNote.categoryLabel }}</span>
          <span class="text-xs text-slate-400">{{ detailNote.relativeTime }}</span>
        </div>
        <div v-if="detailNote.content" class="text-sm text-slate-600 leading-relaxed whitespace-pre-wrap bg-slate-50 rounded-xl p-4">
          {{ detailNote.content }}
        </div>
        <div class="nikki-modal-meta">
          <div class="meta-row">
            <span class="meta-label">分类</span>
            <span class="meta-value">{{ detailNote.categoryLabel }}</span>
          </div>
          <div class="meta-row">
            <span class="meta-label">创建时间</span>
            <span class="meta-value">{{ detailNote.createdAt }}</span>
          </div>
          <div class="meta-row">
            <span class="meta-label">更新时间</span>
            <span class="meta-value">{{ detailNote.updatedAt }}</span>
          </div>
        </div>
      </div>
    </LifeModal>
  </div>
</template>

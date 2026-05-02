<script setup lang="ts">
import { computed, reactive, ref } from 'vue';
import { ChevronRight, Gamepad2, Plus, Shield, Smartphone, Wallet } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { AbilityInstanceConfig, NikkiAsset, AssetOverview } from '../types';

defineOptions({ name: 'AssetsTab' });

const props = defineProps<{
  assetData: AssetOverview;
  abilityConfig?: AbilityInstanceConfig;
}>();

const emit = defineEmits<{
  createAsset: [asset: NikkiAsset];
  updateAsset: [asset: NikkiAsset];
  updateAssetStatus: [id: string, status: NikkiAsset['status']];
  deleteAsset: [id: string];
}>();

const assets = computed(() => props.assetData.assets);

const showDetail = ref(false);
const detailAsset = ref<NikkiAsset | null>(null);
const showCreateAsset = ref(false);
const showEditAsset = ref(false);
const pendingAssetAction = ref<{ mode: 'restore' | 'delete'; asset: NikkiAsset } | null>(null);
const assetForm = reactive({
  name: '',
  type: 'official_account' as NikkiAsset['type'],
  status: 'bound' as NikkiAsset['status'],
  description: ''
});
const editAssetForm = reactive({
  id: '',
  name: '',
  type: 'official_account' as NikkiAsset['type'],
  status: 'bound' as NikkiAsset['status'],
  description: ''
});

function openAssetDetail(asset: NikkiAsset) {
  detailAsset.value = asset;
  showDetail.value = true;
}

function assetIcon(type: NikkiAsset['type']) {
  const map = {
    official_account: Shield,
    sub_account: Smartphone,
    switch_account: Gamepad2,
    payment: Wallet,
    redeem_code: Wallet
  };
  return map[type] ?? Shield;
}

function assetTypeLabel(type: NikkiAsset['type']) {
  const map = {
    official_account: '官方账号',
    sub_account: '辅助账号',
    switch_account: 'Switch 账号',
    payment: '支付凭证',
    redeem_code: '兑换码'
  };
  return map[type] ?? '未知';
}

function statusBg(s: NikkiAsset['status']) {
  const map = { protected: 'bg-emerald-50', bound: 'bg-blue-50', pending: 'bg-amber-50', expired: 'bg-slate-100', archived: 'bg-slate-100' };
  return map[s];
}

function statusColor(s: NikkiAsset['status']) {
  const map = { protected: 'text-emerald-500', bound: 'text-blue-500', pending: 'text-amber-500', expired: 'text-slate-400', archived: 'text-slate-400' };
  return map[s];
}

function statusBadgeClass(s: NikkiAsset['status']) {
  const map = {
    protected: 'bg-emerald-50 text-emerald-600',
    bound: 'bg-blue-50 text-blue-600',
    pending: 'bg-amber-50 text-amber-600',
    expired: 'bg-slate-100 text-slate-500',
    archived: 'bg-slate-100 text-slate-400'
  };
  return map[s];
}

function statusLabel(status: NikkiAsset['status']) {
  const map: Record<NikkiAsset['status'], string> = {
    protected: '已保护',
    bound: '已绑定',
    pending: '待处理',
    expired: '已过期',
    archived: '已归档'
  };
  return map[status];
}

function updateDetailStatus(status: NikkiAsset['status']) {
  if (!detailAsset.value) {
    return;
  }

  emit('updateAssetStatus', detailAsset.value.id, status);
  detailAsset.value = {
    ...detailAsset.value,
    status,
    statusLabel: statusLabel(status),
    ...(status === 'archived' ? { archivedAt: new Date().toISOString() } : {})
  };
}

function openEditAsset(asset: NikkiAsset) {
  editAssetForm.id = asset.id;
  editAssetForm.name = asset.name;
  editAssetForm.type = asset.type;
  editAssetForm.status = asset.status;
  editAssetForm.description = asset.description ?? '';
  showEditAsset.value = true;
}

function handleUpdateAsset() {
  const asset = props.assetData.assets.find(item => item.id === editAssetForm.id);
  if (!asset || !editAssetForm.name.trim()) {
    return;
  }

  const nextAsset: NikkiAsset = {
    ...asset,
    name: editAssetForm.name.trim(),
    type: editAssetForm.type,
    status: editAssetForm.status,
    statusLabel: statusLabel(editAssetForm.status),
    description: editAssetForm.description.trim() || undefined,
    ...(editAssetForm.status === 'archived' ? { archivedAt: asset.archivedAt ?? new Date().toISOString() } : { archivedAt: undefined })
  };
  emit('updateAsset', nextAsset);
  detailAsset.value = nextAsset;
  showEditAsset.value = false;
}

function openAssetActionConfirm(asset: NikkiAsset, mode: 'restore' | 'delete') {
  pendingAssetAction.value = { mode, asset };
}

function handleConfirmAssetAction() {
  if (!pendingAssetAction.value) {
    return;
  }

  if (pendingAssetAction.value.mode === 'delete') {
    emit('deleteAsset', pendingAssetAction.value.asset.id);
    if (detailAsset.value?.id === pendingAssetAction.value.asset.id) {
      detailAsset.value = null;
      showDetail.value = false;
    }
  } else {
    emit('updateAssetStatus', pendingAssetAction.value.asset.id, 'protected');
    detailAsset.value = {
      ...pendingAssetAction.value.asset,
      status: 'protected',
      statusLabel: '已保护',
      archivedAt: undefined
    };
  }

  pendingAssetAction.value = null;
}

function resetAssetForm() {
  assetForm.name = '';
  assetForm.type = 'official_account';
  assetForm.status = 'bound';
  assetForm.description = '';
}

function handleCreateAsset() {
  if (!assetForm.name.trim()) {
    return;
  }

  emit('createAsset', {
    id: `asset-${Date.now()}`,
    name: assetForm.name.trim(),
    type: assetForm.type,
    status: assetForm.status,
    statusLabel: statusLabel(assetForm.status),
    description: assetForm.description.trim() || undefined
  });
  resetAssetForm();
  showCreateAsset.value = false;
}
</script>

<template>
  <div class="space-y-6">
    <LifeGeminiCard :title="abilityConfig?.displayName ?? '账号资产'">
      <template #action>
        <button
          class="appearance-none border-0 bg-transparent p-0 text-xs font-medium text-indigo-500 hover:text-indigo-600"
          type="button"
          @click="showCreateAsset = true"
        >
          <Plus :size="12" class="mr-1 inline" />新增资产
        </button>
      </template>
      <div class="space-y-3">
        <div
          v-for="asset in assets"
          :key="asset.id"
          class="flex items-center justify-between p-4 -mx-4 rounded-2xl hover:bg-slate-50 transition-colors cursor-pointer"
          @click="openAssetDetail(asset)"
        >
          <div class="flex items-center space-x-4">
            <div class="w-10 h-10 rounded-xl flex items-center justify-center" :class="[statusBg(asset.status)]">
              <component :is="assetIcon(asset.type)" :size="18" :class="statusColor(asset.status)" />
            </div>
            <div>
              <p class="text-sm font-medium text-slate-800">{{ asset.name }}</p>
              <p v-if="asset.description" class="text-xs text-slate-500 mt-0.5">{{ asset.description }}</p>
            </div>
          </div>
          <div class="flex items-center gap-3">
            <span class="px-2 py-0.5 rounded-full text-[10px] font-medium" :class="[statusBadgeClass(asset.status)]">
              {{ asset.statusLabel }}
            </span>
            <ChevronRight :size="14" class="text-slate-300" />
          </div>
        </div>
        <div v-if="assets.length === 0" class="nikki-empty-state">
          <Wallet :size="32" />
          <p>暂无关联资产</p>
        </div>
      </div>
    </LifeGeminiCard>

    <LifeGeminiCard title="功能块配置">
      <div class="grid grid-cols-1 gap-3 text-xs sm:grid-cols-3">
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">字段</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.fields?.length ?? 0 }} 个</p>
        </div>
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">导航顺序</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.navigation.order ?? '-' }}</p>
        </div>
        <div class="rounded-xl bg-slate-50 px-3 py-2">
          <span class="text-slate-500">时间轴</span>
          <p class="mt-1 font-medium text-slate-700">{{ abilityConfig?.timeline?.defaultWriteRule.mode ?? '未配置' }}</p>
        </div>
      </div>
    </LifeGeminiCard>

    <LifeModal
      v-model:show="showCreateAsset"
      title="新增账号资产"
      description="账号、平台、支付凭证和兑换码都在这里管理"
      confirm-text="添加资产"
      :width="520"
      @confirm="handleCreateAsset"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">资产名称</label>
          <input v-model="assetForm.name" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" placeholder="例如：官方账号" />
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">资产类型</label>
            <select v-model="assetForm.type" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="official_account">官方账号</option>
              <option value="sub_account">辅助账号</option>
              <option value="switch_account">Switch 账号</option>
              <option value="payment">支付凭证</option>
              <option value="redeem_code">兑换码</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">状态</label>
            <select v-model="assetForm.status" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="protected">已保护</option>
              <option value="bound">已绑定</option>
              <option value="pending">待处理</option>
              <option value="expired">已过期</option>
              <option value="archived">已归档</option>
            </select>
          </div>
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <textarea v-model="assetForm.description" class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm" rows="2"></textarea>
        </div>
      </div>
    </LifeModal>

    <!-- 资产详情弹框 -->
    <LifeModal
      v-model:show="showDetail"
      :title="detailAsset?.name ?? '资产详情'"
      :description="detailAsset?.description"
      :width="480"
    >
      <div v-if="detailAsset" class="nikki-modal-meta">
        <div class="meta-row">
          <span class="meta-label">资产类型</span>
          <span class="meta-value">{{ assetTypeLabel(detailAsset.type) }}</span>
        </div>
        <div class="meta-row">
          <span class="meta-label">安全状态</span>
          <span class="meta-value" :class="[statusColor(detailAsset.status)]">{{ detailAsset.statusLabel }}</span>
        </div>
        <div class="meta-row">
          <span class="meta-label">资产 ID</span>
          <span class="meta-value font-mono text-[11px]">{{ detailAsset.id }}</span>
        </div>
        <div class="mt-4 flex flex-wrap gap-2">
          <button class="nikki-action-btn outline-indigo" type="button" @click="openEditAsset(detailAsset)">
            编辑资产
          </button>
          <button class="nikki-action-btn outline-indigo" type="button" @click="updateDetailStatus('protected')">
            标记已保护
          </button>
          <button class="nikki-action-btn outline-rose" type="button" @click="updateDetailStatus('pending')">
            标记待处理
          </button>
          <button class="nikki-action-btn outline" type="button" @click="updateDetailStatus('archived')">
            归档资产
          </button>
          <button
            v-if="detailAsset.status === 'archived'"
            class="nikki-action-btn outline-indigo"
            type="button"
            @click="openAssetActionConfirm(detailAsset, 'restore')"
          >
            恢复资产
          </button>
          <button class="nikki-action-btn outline-rose" type="button" @click="openAssetActionConfirm(detailAsset, 'delete')">
            删除资产
          </button>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      v-model:show="showEditAsset"
      title="编辑账号资产"
      confirm-text="保存"
      :width="520"
      @confirm="handleUpdateAsset"
    >
      <div class="space-y-4">
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">资产名称</label>
          <input v-model="editAssetForm.name" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm" />
        </div>
        <div class="grid grid-cols-1 gap-3 sm:grid-cols-2">
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">资产类型</label>
            <select v-model="editAssetForm.type" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="official_account">官方账号</option>
              <option value="sub_account">辅助账号</option>
              <option value="switch_account">Switch 账号</option>
              <option value="payment">支付凭证</option>
              <option value="redeem_code">兑换码</option>
            </select>
          </div>
          <div>
            <label class="block text-xs font-medium text-slate-600 mb-1.5">状态</label>
            <select v-model="editAssetForm.status" class="w-full rounded-xl border border-slate-200 px-3 py-2 text-sm">
              <option value="protected">已保护</option>
              <option value="bound">已绑定</option>
              <option value="pending">待处理</option>
              <option value="expired">已过期</option>
              <option value="archived">已归档</option>
            </select>
          </div>
        </div>
        <div>
          <label class="block text-xs font-medium text-slate-600 mb-1.5">说明</label>
          <textarea v-model="editAssetForm.description" class="w-full resize-none rounded-xl border border-slate-200 px-3 py-2 text-sm" rows="2"></textarea>
        </div>
      </div>
    </LifeModal>

    <LifeModal
      :show="Boolean(pendingAssetAction)"
      :title="pendingAssetAction?.mode === 'delete' ? '删除资产' : '恢复资产'"
      :description="pendingAssetAction ? `确认${pendingAssetAction.mode === 'delete' ? '删除' : '恢复'}「${pendingAssetAction.asset.name}」？` : ''"
      confirm-text="确认"
      cancel-text="取消"
      :width="420"
      @update:show="value => { if (!value) pendingAssetAction = null; }"
      @confirm="handleConfirmAssetAction"
    />
  </div>
</template>

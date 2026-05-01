<script setup lang="ts">
import { computed, ref } from 'vue';
import { ChevronRight, Gamepad2, Shield, Smartphone, Wallet } from 'lucide-vue-next';
import LifeGeminiCard from '@/components/life-manager/LifeGeminiCard.vue';
import LifeModal from '@/components/life-manager/LifeModal.vue';
import type { NikkiAsset, AssetOverview } from '../types';

defineOptions({ name: 'AssetsTab' });

const props = defineProps<{
  assetData: AssetOverview;
}>();

const assets = computed(() => props.assetData.assets);

const showDetail = ref(false);
const detailAsset = ref<NikkiAsset | null>(null);

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
  const map = { protected: 'bg-emerald-50', bound: 'bg-blue-50', pending: 'bg-amber-50', expired: 'bg-slate-100' };
  return map[s];
}

function statusColor(s: NikkiAsset['status']) {
  const map = { protected: 'text-emerald-500', bound: 'text-blue-500', pending: 'text-amber-500', expired: 'text-slate-400' };
  return map[s];
}

function statusBadgeClass(s: NikkiAsset['status']) {
  const map = {
    protected: 'bg-emerald-50 text-emerald-600',
    bound: 'bg-blue-50 text-blue-600',
    pending: 'bg-amber-50 text-amber-600',
    expired: 'bg-slate-100 text-slate-500'
  };
  return map[s];
}
</script>

<template>
  <div class="space-y-6">
    <LifeGeminiCard title="账号资产" :action-text="`共 ${assets.length} 个资产`">
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
      </div>
    </LifeModal>
  </div>
</template>

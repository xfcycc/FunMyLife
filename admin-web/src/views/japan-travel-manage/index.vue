<script setup lang="ts">
defineOptions({
  name: 'JapanTravelManage'
});

const tabs = ['基础信息', '行程规划', '同行人员', '清单模板', '资产资料', '图册同步', 'AI 设置'];

const days = [
  ['Day 1', '抵达东京，入住新宿'],
  ['Day 2', '浅草寺、上野、秋叶原'],
  ['Day 3', '镰仓一日游'],
  ['Day 4', '东京前往京都']
];

const documents = ['护照签证', '机票订单', '酒店确认函', '旅行保险', '预约凭证', '紧急联系人'];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-hero">
        <div class="life-card-subtitle">项目 / 日本旅行 2026 / 管理</div>
        <h1 class="life-hero-title">日本旅行项目管理</h1>
        <p class="life-hero-subtitle">管理旅行基础信息、行程、同行人员、行前清单、资料附件、OSS 图册同步和 AI 自动化。</p>
        <div class="life-hero-actions">
          <RouterLink to="/japan-travel">
            <NButton round secondary>返回项目首页</NButton>
          </RouterLink>
          <NButton type="primary" round>保存配置</NButton>
        </div>
      </section>

      <div class="life-tabs">
        <span v-for="item in tabs" :key="item" class="life-tab" :class="{ active: item === '基础信息' }">{{ item }}</span>
      </div>

      <section class="life-grid">
        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon blue"><SvgIcon icon="material-symbols:travel-explore-rounded" /></span>
              基础信息
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>项目名称</strong><span>日本旅行 2026</span></div>
              <span class="life-tag blue">旅行</span>
            </div>
            <div class="life-setting-row">
              <div><strong>旅行日期</strong><span>2026.06.15 - 2026.06.24</span></div>
              <NButton size="small" quaternary>调整</NButton>
            </div>
            <div class="life-setting-row">
              <div><strong>当前阶段</strong><span>出发前准备</span></div>
              <span class="life-tag amber">48 天</span>
            </div>
          </div>
        </div>

        <div class="life-card life-span-8">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:route-outline-rounded" /></span>
              行程规划
            </h2>
            <NButton size="small" type="primary" secondary round>新增一天</NButton>
          </div>
          <div class="life-grid">
            <div v-for="item in days" :key="item[0]" class="life-mini-cell life-span-3">
              <span>{{ item[0] }}</span>
              <strong>{{ item[1] }}</strong>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:group-outline-rounded" /></span>
              同行人员
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>夏目悠然</strong><span>主要联系人</span></div>
              <span class="life-tag">本人</span>
            </div>
            <div class="life-setting-row">
              <div><strong>妈妈</strong><span>同行人，资料待补充</span></div>
              <span class="life-tag amber">待确认</span>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:inventory-2-outline-rounded" /></span>
              资产资料
            </h2>
          </div>
          <div class="life-mini-grid">
            <div v-for="item in documents" :key="item" class="life-mini-cell">
              <span>已关联</span>
              <strong>{{ item }}</strong>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:cloud-sync-outline-rounded" /></span>
              图册与 AI
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>阿里云 OSS</strong><span>旅行图册自动同步</span></div>
              <NSwitch :value="true" />
            </div>
            <div class="life-setting-row">
              <div><strong>行前建议</strong><span>根据行程和清单自动生成</span></div>
              <NSwitch :value="true" />
            </div>
            <div class="life-setting-row">
              <div><strong>旅行总结</strong><span>旅行结束后自动生成回顾</span></div>
              <NSwitch :value="true" />
            </div>
          </div>
        </div>
      </section>

      <div class="life-action-bar">
        <NButton round>取消</NButton>
        <NButton type="primary" round>保存配置</NButton>
      </div>
    </div>
  </div>
</template>

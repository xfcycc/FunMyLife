<script setup lang="ts">
defineOptions({
  name: 'InfinityNikkiManage'
});

const tabs = ['基础信息', '日常模板', '提醒规则', '资产关联', '图册同步', 'AI 设置'];

const routines = [
  ['每日灵感任务', '每日 04:00 重置'],
  ['搭配竞技场挑战', '每日提醒'],
  ['周常任务清单', '每周一 04:00 重置'],
  ['活动兑换检查', '活动结束前 3 天提醒']
];

const integrations = ['飞书提醒', 'OpenClaw', 'Webhook', 'OSS 图册同步'];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-hero">
        <div class="life-card-subtitle">项目 / 无限暖暖 / 管理</div>
        <h1 class="life-hero-title">无限暖暖项目管理</h1>
        <p class="life-hero-subtitle">配置游戏日常、周常、活动提醒、资产关联、图册同步和 AI 自动整理。</p>
        <div class="life-hero-actions">
          <RouterLink to="/infinity-nikki">
            <NButton round secondary>返回项目首页</NButton>
          </RouterLink>
          <NButton type="primary" round>保存配置</NButton>
        </div>
      </section>

      <div class="life-tabs">
        <span v-for="item in tabs" :key="item" class="life-tab" :class="{ active: item === '基础信息' }">{{ item }}</span>
      </div>

      <section class="life-grid">
        <div class="life-card life-span-5">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:info-outline-rounded" /></span>
              基础信息
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>项目名称</strong><span>无限暖暖</span></div>
              <span class="life-tag rose">游戏</span>
            </div>
            <div class="life-setting-row">
              <div><strong>项目状态</strong><span>活跃，每日展示在首页</span></div>
              <NSwitch :value="true" />
            </div>
            <div class="life-setting-row">
              <div><strong>默认重置时间</strong><span>每日 04:00，周一 04:00</span></div>
              <NButton size="small" quaternary>调整</NButton>
            </div>
          </div>
        </div>

        <div class="life-card life-span-7">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:checklist-rounded" /></span>
              日常与周常模板
            </h2>
            <NButton size="small" type="primary" secondary round>新增模板</NButton>
          </div>
          <div class="life-grid">
            <div v-for="item in routines" :key="item[0]" class="life-mini-cell life-span-6">
              <span>{{ item[1] }}</span>
              <strong>{{ item[0] }}</strong>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon blue"><SvgIcon icon="material-symbols:lock-outline-rounded" /></span>
              资产关联
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>游戏账号</strong><span>账号密码已加密保存</span></div>
              <NButton size="small" secondary>查看</NButton>
            </div>
            <div class="life-setting-row">
              <div><strong>充值记录</strong><span>关联 3 条资产记录</span></div>
              <NButton size="small" secondary>管理</NButton>
            </div>
            <div class="life-setting-row">
              <div><strong>常用链接</strong><span>攻略、社区、兑换入口</span></div>
              <NButton size="small" secondary>编辑</NButton>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:cloud-sync-outline-rounded" /></span>
              图册同步
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>OSS 同步</strong><span>截图图册自动归档</span></div>
              <NSwitch :value="true" />
            </div>
            <div class="life-setting-row">
              <div><strong>最近同步</strong><span>42 张图片，状态正常</span></div>
              <span class="life-tag teal">正常</span>
            </div>
            <div class="life-setting-row">
              <div><strong>自动图册</strong><span>按活动和日期分组</span></div>
              <NSwitch :value="true" />
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon"><SvgIcon icon="material-symbols:robot-2-outline-rounded" /></span>
              AI 与外部通知
            </h2>
          </div>
          <div class="life-panel-list">
            <div class="life-setting-row">
              <div><strong>AI 周报</strong><span>每周自动总结游戏进度</span></div>
              <NSwitch :value="true" />
            </div>
            <div v-for="item in integrations" :key="item" class="life-setting-row">
              <div><strong>{{ item }}</strong><span>用于提醒和自动化联动</span></div>
              <span class="life-tag">{{ item === 'Webhook' ? '未启用' : '已启用' }}</span>
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

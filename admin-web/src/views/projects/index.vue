<script setup lang="ts">
defineOptions({
  name: 'Projects'
});

const filters = ['全部', '游戏', '旅行', '学习', '生活', '归档'];

const projects = [
  {
    name: '无限暖暖',
    type: '游戏',
    desc: '日常、周常、活动倒计时、账号资产、截图图册和攻略笔记。',
    route: '/infinity-nikki',
    manage: '/infinity-nikki-manage',
    tone: 'rose',
    next: '活动结束还有 3 天',
    status: 'AI 建议已更新'
  },
  {
    name: '日本旅行 2026',
    type: '旅行',
    desc: '行程、机票酒店、行前清单、证件资料、预算和旅行图册。',
    route: '/japan-travel',
    manage: '/japan-travel-manage',
    tone: 'blue',
    next: '出发倒计时 48 天',
    status: '图册同步正常'
  },
  {
    name: '原神',
    type: '游戏',
    desc: '版本更新、深渊周期、活动任务、账号资料和抽卡计划。',
    route: '/projects',
    manage: '/projects',
    tone: 'amber',
    next: '周常刷新 2 天后',
    status: '待整理笔记'
  },
  {
    name: 'OpenAI 学习计划',
    type: '学习',
    desc: '课程记录、阅读笔记、实践清单、学习复盘和 AI 摘要。',
    route: '/projects',
    manage: '/projects',
    tone: 'teal',
    next: '今晚 20:00 复习',
    status: '连续 23 天'
  },
  {
    name: '家庭保险',
    type: '生活',
    desc: '保单、扣费提醒、覆盖范围、家庭成员和理赔资料。',
    route: '/projects',
    manage: '/projects',
    tone: 'amber',
    next: '扣费还有 27 天',
    status: '资料完整'
  },
  {
    name: '年度健康管理',
    type: '生活',
    desc: '体检报告、复诊提醒、运动记录和年度健康总结。',
    route: '/projects',
    manage: '/projects',
    tone: 'teal',
    next: '体检预约待确认',
    status: 'AI 摘要待生成'
  }
];

const updates = [
  '无限暖暖新增 1 条直播提醒',
  '日本旅行 2026 上传 18 张照片',
  'OpenAI 学习计划生成了周报',
  '家庭保险保单资料已归档'
];

const templates = ['游戏项目', '旅行项目', '学习计划', '保险资料', '订阅服务', '自定义项目'];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-hero">
        <h1 class="life-hero-title">项目管理</h1>
        <p class="life-hero-subtitle">
          项目是 FunMyLife 的生活容器。它把提醒、资产、笔记、图册和 AI 总结收拢到同一个生活对象下。
        </p>
        <div class="life-hero-actions">
          <NInput round placeholder="搜索项目、标签或近期动态" style="max-width: 360px">
            <template #prefix>
              <SvgIcon icon="material-symbols:search-rounded" />
            </template>
          </NInput>
          <NButton type="primary" round>
            <template #icon>
              <SvgIcon icon="material-symbols:add-rounded" />
            </template>
            新建项目
          </NButton>
        </div>
      </section>

      <section class="life-card">
        <div class="life-card-head">
          <div class="life-tabs">
            <span v-for="item in filters" :key="item" class="life-tab" :class="{ active: item === '全部' }">
              {{ item }}
            </span>
          </div>
          <NButton quaternary circle>
            <template #icon>
              <SvgIcon icon="material-symbols:tune-rounded" />
            </template>
          </NButton>
        </div>
        <div class="life-grid">
          <article v-for="item in projects" :key="item.name" class="life-project-card life-span-4">
            <div class="flex items-start justify-between gap-12px">
              <div class="life-project-cover">{{ item.name.slice(0, 1) }}</div>
              <span class="life-tag" :class="item.tone">{{ item.type }}</span>
            </div>
            <h3 class="life-project-title">{{ item.name }}</h3>
            <p class="life-project-desc">{{ item.desc }}</p>
            <div class="life-mini-grid">
              <div class="life-mini-cell">
                <span>下一个提醒</span>
                <strong>{{ item.next }}</strong>
              </div>
              <div class="life-mini-cell">
                <span>整理状态</span>
                <strong>{{ item.status }}</strong>
              </div>
            </div>
            <div class="mt-16px flex gap-10px">
              <RouterLink :to="item.route">
                <NButton size="small" type="primary" secondary round>进入项目</NButton>
              </RouterLink>
              <RouterLink :to="item.manage">
                <NButton size="small" quaternary round>管理</NButton>
              </RouterLink>
            </div>
          </article>
        </div>
      </section>

      <section class="life-grid">
        <div class="life-card life-span-7">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:update-rounded" /></span>
              最近更新
            </h2>
          </div>
          <div class="life-list">
            <div v-for="item in updates" :key="item" class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">{{ item }}</div>
                <div class="life-list-meta">刚刚同步到项目时间线</div>
              </div>
            </div>
          </div>
        </div>

        <div class="life-card life-span-5">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:dashboard-customize-outline-rounded" /></span>
              项目模板
            </h2>
          </div>
          <div class="life-mini-grid">
            <button v-for="item in templates" :key="item" class="life-mini-cell cursor-pointer text-left">
              <SvgIcon icon="material-symbols:add-box-outline-rounded" class="mb-8px text-22px color-#7568ff" />
              <strong>{{ item }}</strong>
            </button>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

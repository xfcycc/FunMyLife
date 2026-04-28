<script setup lang="ts">
defineOptions({
  name: 'Home'
});

const stats = [
  { label: '待办事项', value: '8' },
  { label: '今日日程', value: '3' },
  { label: '即将到期', value: '5' },
  { label: '活跃项目', value: '6' }
];

const todos = [
  { title: '完成项目文档初稿', tag: '工作', time: '10:00', done: false, tone: 'blue' },
  { title: '无限暖暖每日任务', tag: '游戏', time: '18:30', done: true, tone: 'rose' },
  { title: '阅读《深度工作》第 3 章', tag: '学习', time: '20:00', done: false, tone: 'teal' },
  { title: '整理一周开销', tag: '生活', time: '21:00', done: false, tone: 'amber' }
];

const expiring = [
  { name: 'OpenAI Plus', type: 'AI 服务', date: '5月12日', left: '14 天', tone: '' },
  { name: '腾讯视频 VIP', type: '订阅服务', date: '5月20日', left: '22 天', tone: 'blue' },
  { name: '家庭意外险', type: '保险', date: '5月25日', left: '27 天', tone: 'amber' },
  { name: '域名 funmylife.cn', type: '域名', date: '6月08日', left: '41 天', tone: 'teal' }
];

const projects = [
  { name: '无限暖暖', type: '游戏', summary: '日常 5/8，活动 3 天后结束', route: '/infinity-nikki', tone: 'rose' },
  { name: '日本旅行 2026', type: '旅行', summary: '出发倒计时 48 天，清单 12/20', route: '/japan-travel', tone: 'blue' },
  { name: 'OpenAI 学习计划', type: '学习', summary: '连续学习 23 天，本周复盘待生成', route: '/projects', tone: 'teal' },
  { name: '家庭保险', type: '生活', summary: '下次扣费 27 天后，资料已归档', route: '/projects', tone: 'amber' }
];

const schedules = [
  { date: '5月1日', title: '无限暖暖版本前瞻直播', tag: '游戏', tone: 'rose' },
  { date: '5月3日', title: '项目需求评审会', tag: '工作', tone: 'blue' },
  { date: '5月8日', title: '旅行酒店尾款提醒', tag: '旅行', tone: 'amber' },
  { date: '5月12日', title: 'OpenAI Plus 续费', tag: '资产', tone: 'teal' }
];

const quickActions = [
  { label: '添加待办', icon: 'material-symbols:check-box-outline-rounded' },
  { label: '写日记', icon: 'material-symbols:edit-note-rounded' },
  { label: '新建提醒', icon: 'material-symbols:notifications-active-outline-rounded' },
  { label: '上传图册', icon: 'material-symbols:add-photo-alternate-outline-rounded' },
  { label: '整理笔记', icon: 'material-symbols:ink-pen-outline-rounded' },
  { label: 'AI 复盘', icon: 'material-symbols:robot-2-outline-rounded' }
];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-hero">
        <h1 class="life-hero-title">上午好，夏目悠然</h1>
        <p class="life-hero-subtitle">新的一天，从规划开始。FunMyLife 已为你整理今日事项、近期到期和项目状态。</p>
        <div class="life-hero-actions">
          <NButton type="primary" round>
            <template #icon>
              <SvgIcon icon="material-symbols:add-rounded" />
            </template>
            新建记录
          </NButton>
          <NButton round secondary>
            <template #icon>
              <SvgIcon icon="material-symbols:view-quilt-outline-rounded" />
            </template>
            编辑布局
          </NButton>
        </div>
        <div class="life-weather-chip">
          <SvgIcon icon="material-symbols:partly-cloudy-day-outline-rounded" class="text-28px color-#f5a623" />
          <div>
            <strong>4月28日 周二</strong>
            <div class="text-12px color-#7b8195">多云 18℃ - 25℃</div>
          </div>
        </div>
      </section>

      <section class="life-grid">
        <div class="life-card life-card-soft life-span-4">
          <div class="life-card-head">
            <div>
              <h2 class="life-card-title">
                <span class="life-icon"><SvgIcon icon="material-symbols:calendar-month-outline-rounded" /></span>
                今日概览
              </h2>
              <p class="life-card-subtitle">保持节奏，你已经很棒了</p>
            </div>
            <span class="life-link">今日</span>
          </div>
          <div class="life-stat-row">
            <div v-for="item in stats" :key="item.label" class="life-stat">
              <strong>{{ item.value }}</strong>
              <span>{{ item.label }}</span>
            </div>
          </div>
          <div class="mt-18px">
            <div class="mb-8px flex justify-between text-13px color-#72788c">
              <span>整体完成度</span>
              <strong class="color-#7568ff">62%</strong>
            </div>
            <div class="life-progress"><span style="width: 62%" /></div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:checklist-rounded" /></span>
              今日待办
            </h2>
            <span class="life-link">查看全部</span>
          </div>
          <div class="life-list">
            <div v-for="item in todos" :key="item.title" class="life-list-item">
              <span class="life-check" :class="{ done: item.done }">
                <SvgIcon v-if="item.done" icon="material-symbols:check-rounded" class="text-14px" />
              </span>
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
                <div class="life-list-meta">
                  <span class="life-tag" :class="item.tone">{{ item.tag }}</span>
                </div>
              </div>
              <span class="life-time">{{ item.time }}</span>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:event-upcoming-outline-rounded" /></span>
              即将到期
            </h2>
            <span class="life-link">30 天内</span>
          </div>
          <div class="life-list">
            <div v-for="item in expiring" :key="item.name" class="life-list-item">
              <span class="life-icon" :class="item.tone">
                <SvgIcon icon="material-symbols:lock-clock-outline-rounded" />
              </span>
              <div class="life-list-main">
                <div class="life-list-title">{{ item.name }}</div>
                <div class="life-list-meta">
                  <span class="life-tag" :class="item.tone">{{ item.type }}</span>
                </div>
              </div>
              <div class="text-right">
                <div class="life-time">{{ item.date }}</div>
                <div class="life-list-meta">还有 {{ item.left }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="life-card life-span-6">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:auto-awesome-outline-rounded" /></span>
              项目总览
            </h2>
            <RouterLink to="/projects" class="life-link">项目管理</RouterLink>
          </div>
          <div class="life-grid">
            <RouterLink v-for="item in projects" :key="item.name" :to="item.route" class="life-project-card life-span-6">
              <div class="life-project-cover">{{ item.name.slice(0, 1) }}</div>
              <h3 class="life-project-title">{{ item.name }}</h3>
              <p class="life-project-desc">{{ item.summary }}</p>
              <div class="mt-12px"><span class="life-tag" :class="item.tone">{{ item.type }}</span></div>
            </RouterLink>
          </div>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon blue"><SvgIcon icon="material-symbols:timeline-rounded" /></span>
              未来 7 天
            </h2>
          </div>
          <div class="life-timeline">
            <div v-for="item in schedules" :key="item.title" class="life-timeline-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
                <div class="life-list-meta">{{ item.date }}</div>
              </div>
              <span class="life-tag" :class="item.tone">{{ item.tag }}</span>
            </div>
          </div>
        </div>

        <div class="life-card life-card-soft life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon"><SvgIcon icon="material-symbols:robot-2-outline-rounded" /></span>
              AI 今日建议
            </h2>
          </div>
          <div class="life-list">
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">优先处理 2 个续费提醒，避免自动扣费遗漏。</div>
            </div>
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">无限暖暖活动剩余 3 天，建议今晚完成收集任务。</div>
            </div>
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">学习连续打卡稳定，可以生成本周复盘。</div>
            </div>
          </div>
          <NButton class="mt-18px" secondary round type="primary">查看全部建议</NButton>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:donut-large-outline-rounded" /></span>
              本月统计
            </h2>
          </div>
          <div class="life-mini-grid">
            <div class="life-mini-cell"><span>工作</span><strong>40%</strong></div>
            <div class="life-mini-cell"><span>学习</span><strong>25%</strong></div>
            <div class="life-mini-cell"><span>生活</span><strong>20%</strong></div>
            <div class="life-mini-cell"><span>娱乐</span><strong>15%</strong></div>
          </div>
        </div>

        <div class="life-card life-span-5">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:bolt-outline-rounded" /></span>
              快捷入口
            </h2>
          </div>
          <div class="life-mini-grid">
            <button v-for="item in quickActions" :key="item.label" class="life-mini-cell cursor-pointer text-left">
              <SvgIcon :icon="item.icon" class="mb-8px text-24px color-#7568ff" />
              <strong>{{ item.label }}</strong>
            </button>
          </div>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:sensors-outline-rounded" /></span>
              实时状态
            </h2>
          </div>
          <div class="life-list">
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">OSS 图册同步完成</div>
                <div class="life-list-meta">日本旅行 2026，新上传 18 张</div>
              </div>
            </div>
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">AI 摘要已生成</div>
                <div class="life-list-meta">OpenAI 学习计划周报</div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

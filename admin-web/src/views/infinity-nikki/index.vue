<script setup lang="ts">
defineOptions({
  name: 'InfinityNikki'
});

const tabs = ['概览', '日常', '倒计时', '资产', '笔记', '图册', 'AI'];

const dailyTasks = [
  { title: '搭配竞技场挑战', progress: '已完成', done: true },
  { title: '完成每日灵感任务', progress: '已完成', done: true },
  { title: '收集奇想星', progress: '10/15', done: false },
  { title: '完成心愿原野探索', progress: '0/1', done: false }
];

const countdowns = [
  { title: '版本前瞻直播', time: '5月1日 19:00', left: '3 天', tone: 'rose' },
  { title: '花愿镇活动结束', time: '5月4日 04:00', left: '6 天', tone: 'amber' },
  { title: '周常任务刷新', time: '5月5日 04:00', left: '7 天', tone: 'teal' }
];

const notes = ['活动兑换优先级', '套装染色方案', '奇想星收集路线'];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-cover-banner">
        <div class="life-cover-meta">
          <span class="life-tag rose">游戏项目</span>
          <span class="life-tag">活跃</span>
          <RouterLink to="/infinity-nikki-manage">
            <NButton size="small" round secondary>项目管理</NButton>
          </RouterLink>
        </div>
        <h1>无限暖暖</h1>
        <p>把日常、周常、活动倒计时、直播提醒、账号资产、截图图册和攻略笔记集中到一个项目里。</p>
      </section>

      <div class="life-tabs">
        <span v-for="item in tabs" :key="item" class="life-tab" :class="{ active: item === '概览' }">{{ item }}</span>
      </div>

      <section class="life-grid">
        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:stadia-controller-outline-rounded" /></span>
              今日游戏日常
            </h2>
            <span class="life-link">5/8 完成</span>
          </div>
          <div class="mb-16px">
            <div class="mb-8px flex justify-between text-13px color-#72788c">
              <span>每日任务进度</span>
              <strong class="color-#7568ff">62%</strong>
            </div>
            <div class="life-progress"><span style="width: 62%" /></div>
          </div>
          <div class="life-list">
            <div v-for="item in dailyTasks" :key="item.title" class="life-list-item">
              <span class="life-check" :class="{ done: item.done }">
                <SvgIcon v-if="item.done" icon="material-symbols:check-rounded" class="text-14px" />
              </span>
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
              </div>
              <span class="life-time">{{ item.progress }}</span>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:timer-outline-rounded" /></span>
              活动与版本
            </h2>
          </div>
          <div class="life-list">
            <div v-for="item in countdowns" :key="item.title" class="life-list-item">
              <span class="life-icon" :class="item.tone">
                <SvgIcon icon="material-symbols:event-upcoming-outline-rounded" />
              </span>
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
                <div class="life-list-meta">{{ item.time }}</div>
              </div>
              <span class="life-tag" :class="item.tone">{{ item.left }}</span>
            </div>
          </div>
        </div>

        <div class="life-card life-card-soft life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon"><SvgIcon icon="material-symbols:robot-2-outline-rounded" /></span>
              AI 项目建议
            </h2>
          </div>
          <div class="life-list">
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">今晚优先完成活动兑换任务，剩余时间不足一周。</div>
            </div>
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">截图图册最近新增较多，建议自动生成一次搭配回顾。</div>
            </div>
          </div>
          <NButton class="mt-18px" type="primary" secondary round>生成游戏周报</NButton>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon blue"><SvgIcon icon="material-symbols:lock-outline-rounded" /></span>
              账号资产
            </h2>
          </div>
          <div class="life-mini-grid">
            <div class="life-mini-cell"><span>账号</span><strong>已加密保存</strong></div>
            <div class="life-mini-cell"><span>充值记录</span><strong>3 条</strong></div>
            <div class="life-mini-cell"><span>相关链接</span><strong>5 个</strong></div>
            <div class="life-mini-cell"><span>安全状态</span><strong>正常</strong></div>
          </div>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:edit-note-outline-rounded" /></span>
              最近笔记
            </h2>
          </div>
          <div class="life-list">
            <div v-for="item in notes" :key="item" class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">{{ item }}</div>
                <div class="life-list-meta">已归属到无限暖暖</div>
              </div>
            </div>
          </div>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:photo-library-outline-rounded" /></span>
              截图图册
            </h2>
          </div>
          <div class="life-mini-grid">
            <div class="life-mini-cell"><span>本周新增</span><strong>42 张</strong></div>
            <div class="life-mini-cell"><span>OSS 同步</span><strong>正常</strong></div>
            <div class="life-mini-cell"><span>图册</span><strong>6 个</strong></div>
            <div class="life-mini-cell"><span>AI 回顾</span><strong>可生成</strong></div>
          </div>
        </div>

        <div class="life-card life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:history-rounded" /></span>
              最近动态
            </h2>
          </div>
          <div class="life-timeline">
            <div class="life-timeline-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">完成每日任务 5 项</div>
                <div class="life-list-meta">18:35</div>
              </div>
            </div>
            <div class="life-timeline-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">新增活动倒计时</div>
                <div class="life-list-meta">上午</div>
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>

<script setup lang="ts">
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';

defineOptions({
  name: 'Home'
});

const todos = [
  ['完成项目文档初稿', '工作', '10:00'],
  ['健身 30 分钟', '生活', '18:30'],
  ['阅读《深度工作》第 3 章', '学习', '20:00'],
  ['整理一周开销', '生活', '21:00']
];

const expiring = [
  ['腾讯视频 VIP', '6月2日 还有 13 天', 'material-symbols:play-circle-outline-rounded'],
  ['ChatGPT Plus 会员', '6月5日 还有 16 天', 'material-symbols:all-inclusive-rounded'],
  ['域名 - xiamuyouran.com', '6月18日 还有 29 天', 'material-symbols:language-rounded'],
  ['平安百万医疗险', '6月25日 还有 36 天', 'material-symbols:health-and-safety-outline-rounded']
];

const projects = [
  ['无限暖暖', '游戏', '今日任务 5/8', '待办 3', '倒计时 2', 'nikki'],
  ['原神', '游戏', '今日任务 4/9', '待办 2', '倒计时 1', 'genshin'],
  ['日本旅行 2026', '旅行', '待办 12', '行程 6', '倒计时 3', 'japan'],
  ['OpenAI 学习计划', '学习', '本周目标 3/5', '待办 4', '笔记 7', 'openai']
];

const status = [
  ['AI 总结完成：日本旅行行前清单', '10:42'],
  ['文件上传完成：IMG_2024.jpg', '10:31'],
  ['提醒触发：腾讯视频 VIP 即将到期', '09:00'],
  ['数据同步完成', '08:20'],
  ['AI 总结完成：本周学习复盘', '昨天 22:15']
];
</script>

<template>
  <LifeAppShell active="首页">
    <header class="lm-topbar home-top">
      <div class="lm-title">
        <h1>上午好，夏目悠然 ☀️</h1>
        <p>新的一天，从有序开始</p>
      </div>
      <div class="lm-actions">
        <div class="lm-pill weather-pill">
          <span>5月20日 周二</span>
          <strong>多云 18℃ - 25℃</strong>
          <SvgIcon icon="material-symbols:partly-cloudy-day-rounded" />
        </div>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:search-rounded" /></button>
        <button class="lm-icon-btn dot"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></button>
        <button class="lm-purple-btn round-only"><SvgIcon icon="material-symbols:add-rounded" /></button>
      </div>
    </header>

    <section class="lm-grid home-summary">
      <article class="lm-card overview-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:calendar-month-rounded" /></span>今日概览</h2>
          <button>更多 ›</button>
        </div>
        <div class="overview-numbers">
          <div><strong>8</strong><span>待办事项</span></div>
          <div><strong>3</strong><span>今日日程</span></div>
          <div><strong>2</strong><span>即将到期</span></div>
          <div><strong>1</strong><span>待处理提醒</span></div>
        </div>
        <div class="soft-chart">
          <i></i><i></i><i></i><i></i><i></i>
        </div>
        <p class="overview-note">保持节奏，你已经很棒了！ 💜</p>
      </article>

      <article class="lm-card todo-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:edit-calendar-outline-rounded" /></span>今日待办</h2>
          <button>查看全部</button>
        </div>
        <div class="todo-list">
          <div v-for="item in todos" :key="item[0]">
            <span class="check"></span>
            <strong>{{ item[0] }}</strong>
            <em>{{ item[1] }}</em>
            <time>{{ item[2] }}</time>
            <span class="circle"></span>
          </div>
        </div>
        <button class="add-todo">＋ 添加待办</button>
      </article>

      <article class="lm-card expire-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon pink"><SvgIcon icon="material-symbols:event-upcoming-outline-rounded" /></span>即将到期（30 天内）</h2>
          <button>查看全部</button>
        </div>
        <div class="expire-list">
          <div v-for="item in expiring" :key="item[0]">
            <span class="lm-soft-icon"><SvgIcon :icon="item[2]" /></span>
            <strong>{{ item[0] }}</strong>
            <time>{{ item[1] }}</time>
          </div>
        </div>
      </article>
    </section>

    <section class="lm-card project-strip">
      <div class="lm-card-title">
        <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:folder-outline-rounded" /></span>项目总览</h2>
      </div>
      <div class="home-projects">
        <article v-for="item in projects" :key="item[0]">
          <div class="home-project-cover" :class="item[5]"></div>
          <h3>{{ item[0] }} <span>{{ item[1] }}</span></h3>
          <div class="home-project-meta">
            <span>{{ item[2] }}</span>
            <span>{{ item[3] }}</span>
            <span>{{ item[4] }}</span>
          </div>
          <footer>版本更新 5月28日 <b>还有 8 天</b></footer>
        </article>
      </div>
    </section>

    <section class="lm-grid cols-4 home-bottom">
      <article class="lm-card ai-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 今日建议</h2>
        </div>
        <ul>
          <li>你昨天的日记情绪较稳定，建议继续保持晨间记录。</li>
          <li>日本旅行项目有 3 个待办即将开始，记得安排时间。</li>
          <li>无限暖暖有活动提醒，别错过奖励。</li>
        </ul>
        <button class="lm-plain-btn">查看完整建议 ›</button>
        <div class="lm-robot"></div>
      </article>

      <article class="lm-card month-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:donut-large-rounded" /></span>本月统计</h2>
        </div>
        <div class="donut"></div>
        <div class="legend">
          <span><i></i>工作 32%</span>
          <span><i></i>学习 24%</span>
          <span><i></i>生活 21%</span>
          <span><i></i>娱乐 15%</span>
          <span><i></i>旅行 8%</span>
        </div>
        <p>总时长 <strong>128 小时</strong>　较上月 <b>+12%</b></p>
      </article>

      <article class="lm-card quick-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:verified-user-outline-rounded" /></span>快捷入口</h2>
        </div>
        <div class="quick-grid">
          <button><SvgIcon icon="material-symbols:check-box-rounded" />添加待办</button>
          <button><SvgIcon icon="material-symbols:article-outline-rounded" />记录日记</button>
          <button><SvgIcon icon="material-symbols:edit-note-outline-rounded" />写笔记</button>
          <button><SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" />上传图册</button>
          <button><SvgIcon icon="material-symbols:history-rounded" />记账</button>
          <button><SvgIcon icon="material-symbols:search-rounded" />搜索</button>
          <button><SvgIcon icon="material-symbols:upload-file-outline-rounded" />导入数据</button>
          <button><SvgIcon icon="material-symbols:more-horiz-rounded" />更多</button>
        </div>
      </article>

      <article class="lm-card realtime-card">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:sensors-rounded" /></span>实时状态（SSE）</h2>
          <button>全部事件 ›</button>
        </div>
        <div class="status-list">
          <div v-for="item in status" :key="item[0]">
            <span></span>
            <strong>{{ item[0] }}</strong>
            <time>{{ item[1] }}</time>
          </div>
        </div>
      </article>
    </section>
  </LifeAppShell>
</template>

<style scoped>
.home-summary {
  grid-template-columns: 1.05fr 1fr 1.35fr;
}

.round-only {
  width: 38px;
  height: 38px;
  padding: 0;
  border-radius: 50%;
}

.weather-pill {
  height: 48px;
  align-items: flex-start;
  flex-direction: column;
  position: relative;
  min-width: 143px;
  padding: 8px 50px 8px 13px;
}

.weather-pill span {
  color: #626a7d;
  font-size: 10px;
}

.weather-pill strong {
  color: #1c2232;
  font-size: 11px;
}

.weather-pill .svg-icon {
  position: absolute;
  right: 13px;
  top: 12px;
  color: #f2bd47;
  font-size: 26px;
}

.overview-numbers {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  text-align: center;
}

.overview-numbers strong {
  display: block;
  font-size: 25px;
  line-height: 1;
}

.overview-numbers span {
  display: block;
  margin-top: 10px;
  color: #7e8495;
  font-size: 10px;
}

.soft-chart {
  height: 74px;
  display: flex;
  align-items: end;
  gap: 0;
  margin-top: 14px;
  background: linear-gradient(180deg, transparent 0 68%, #f4f0ff 69%);
  clip-path: polygon(0 58%, 18% 72%, 35% 47%, 52% 70%, 70% 28%, 86% 64%, 100% 42%, 100% 100%, 0 100%);
}

.soft-chart i {
  flex: 1;
}

.overview-note {
  margin: 9px 0 0;
  color: #7b8192;
  font-size: 10px;
}

.todo-list,
.expire-list,
.status-list {
  display: grid;
  gap: 12px;
}

.todo-list div {
  display: grid;
  grid-template-columns: 13px minmax(0, 1fr) auto auto 13px;
  gap: 9px;
  align-items: center;
  color: #343b4d;
  font-size: 11px;
}

.todo-list strong,
.expire-list strong,
.status-list strong {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.todo-list em {
  padding: 2px 6px;
  border-radius: 5px;
  background: #eef8f4;
  color: #4d9a83;
  font-style: normal;
  font-size: 9px;
}

.todo-list time,
.expire-list time,
.status-list time {
  color: #7c8394;
  font-size: 10px;
}

.check,
.circle {
  width: 11px;
  height: 11px;
  border: 1px solid #cfd4e1;
  border-radius: 3px;
}

.circle {
  border-radius: 50%;
}

.add-todo {
  width: 100%;
  margin-top: 16px;
  padding-top: 13px;
  border-top: 1px solid #edf0f7;
  background: transparent;
  color: #7660e6;
  font-size: 11px;
}

.expire-list div {
  display: grid;
  grid-template-columns: 31px minmax(0, 1fr) auto;
  align-items: center;
  gap: 12px;
}

.project-strip {
  margin-top: 14px;
}

.home-projects {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 12px;
}

.home-projects article {
  overflow: hidden;
  border: 1px solid #edf0f6;
  border-radius: 8px;
  background: #fff;
}

.home-project-cover {
  height: 70px;
}

.home-project-cover.nikki,
.home-project-cover.japan {
  position: relative;
}

.home-project-cover.nikki {
  background: linear-gradient(135deg, #dae8ff, #ffddea);
}

.home-project-cover.genshin {
  background: linear-gradient(135deg, #c8e8ff 0 45%, #8dbf82 46%);
}

.home-project-cover.japan {
  background: linear-gradient(180deg, #aad4f2 0 50%, #ffd9df 51%, #91aa7e 52%);
}

.home-project-cover.openai {
  background: linear-gradient(135deg, #c7b4ff, #eadfff);
}

.home-projects h3 {
  margin: 9px 10px 8px;
  color: #171d2c;
  font-size: 13px;
}

.home-projects h3 span {
  margin-left: 5px;
  padding: 1px 5px;
  border-radius: 5px;
  background: #eef8f4;
  color: #519b87;
  font-size: 9px;
  font-weight: 500;
}

.home-project-meta {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
  color: #596174;
  font-size: 10px;
}

.home-projects footer {
  display: flex;
  justify-content: space-between;
  margin-top: 12px;
  padding: 9px 10px;
  border-top: 1px solid #edf0f6;
  color: #8b91a1;
  font-size: 9px;
}

.home-projects footer b {
  color: #7660e6;
}

.home-bottom {
  grid-template-columns: 1fr 0.92fr 0.95fr 1fr;
  margin-top: 14px;
}

.ai-card {
  position: relative;
  min-height: 178px;
}

.ai-card ul {
  margin: 0;
  padding-left: 15px;
  color: #4d5568;
  font-size: 10px;
  line-height: 2;
}

.ai-card .lm-robot {
  position: absolute;
  right: 20px;
  bottom: 14px;
}

.donut {
  width: 92px;
  height: 92px;
  margin: 4px auto 10px;
  border-radius: 50%;
  background: conic-gradient(#765ce8 0 32%, #5b8df0 32% 56%, #62cfa8 56% 77%, #f1ad43 77% 92%, #eb6d91 92%);
  position: relative;
}

.donut::after {
  content: '';
  position: absolute;
  inset: 24px;
  border-radius: 50%;
  background: #fff;
}

.legend {
  display: grid;
  gap: 6px;
  color: #596174;
  font-size: 10px;
}

.legend i {
  display: inline-block;
  width: 7px;
  height: 7px;
  margin-right: 6px;
  border-radius: 50%;
  background: #765ce8;
}

.month-card p {
  margin: 12px 0 0;
  color: #7f8596;
  font-size: 10px;
}

.month-card b {
  color: #39ad7e;
}

.quick-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 9px;
}

.quick-grid button {
  display: grid;
  place-items: center;
  gap: 6px;
  min-height: 48px;
  border-radius: 8px;
  background: #f8f8fe;
  color: #626a7b;
  font-size: 10px;
}

.quick-grid .svg-icon {
  color: #725ae8;
  font-size: 21px;
}

.status-list div {
  display: grid;
  grid-template-columns: 7px minmax(0, 1fr) auto;
  align-items: center;
  gap: 9px;
}

.status-list span {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: #765ce8;
}

@media (max-width: 1180px) {
  .home-summary,
  .home-bottom,
  .home-projects {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

<script setup lang="ts">
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';

defineOptions({
  name: 'InfinityNikki'
});

const tabs = [
  ['概览', 'material-symbols:calendar-month-outline-rounded'],
  ['日常', 'material-symbols:event-available-outline-rounded'],
  ['倒计时', 'material-symbols:timer-outline-rounded'],
  ['资产', 'material-symbols:inventory-2-outline-rounded'],
  ['笔记', 'material-symbols:edit-note-outline-rounded'],
  ['图册', 'material-symbols:photo-library-outline-rounded'],
  ['AI', 'material-symbols:auto-awesome-rounded']
];

const weekTasks = [
  ['完成搭配评选 3次', '3/3'],
  ['通关心愿梦境 10次', '6/10'],
  ['收集奇想星 80个', '52/80'],
  ['完成灵感任务 14次', '14/14'],
  ['参与搭配赛 2次', '1/2'],
  ['提升搭配师等级', '38/50'],
  ['收集套装部件 30个', '22/30']
];

const countdowns = [
  ['奇迹之旅 · 第二章', '2026-05-02 06:00 结束', '3 天'],
  ['繁花季节 限时活动', '2026-05-12 05:00 结束', '13 天'],
  ['累计充值返利', '2026-05-20 05:00 结束', '21 天']
];
</script>

<template>
  <LifeAppShell active="无限暖暖" avatar="nikki">
    <header class="nikki-head lm-card">
      <div class="nikki-hero-img lm-hero-art nikki"></div>
      <div class="lm-detail-title">
        <h1>无限暖暖 <span>★</span></h1>
        <p><span class="lm-tag pink">游戏</span> <span class="lm-tag green">进行中</span></p>
        <p>收集美好的瞬间，搭配无限的可能</p>
        <div class="nikki-meta">
          <span>创建时间 <b>2024-12-15</b></span>
          <span>今日任务进度 <b>5/8</b></span>
          <span>本周完成率 <b>62%</b></span>
          <span>项目笔记 <b>12</b></span>
          <span>图册数量 <b>8</b></span>
        </div>
      </div>
      <div class="lm-actions">
        <button class="lm-purple-btn"><SvgIcon icon="material-symbols:add-rounded" />快速记录</button>
        <button class="lm-plain-btn"><SvgIcon icon="material-symbols:ios-share-rounded" />分享</button>
        <button class="lm-plain-btn"><SvgIcon icon="material-symbols:settings-outline-rounded" />设置</button>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:more-horiz-rounded" /></button>
      </div>
    </header>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item[0]" :class="{ active: item[0] === '概览' }">
        <SvgIcon :icon="item[1]" />{{ item[0] }}
      </button>
    </nav>

    <section class="lm-grid nikki-grid">
      <article class="lm-card daily-ring">
        <div class="lm-card-title">
          <h2>今日游戏日常</h2>
          <button>查看全部</button>
        </div>
        <div class="daily-content">
          <div class="daily-circle"><strong>5/8</strong><span>已完成</span></div>
          <ul>
            <li class="done">每日签到</li>
            <li class="done">心愿原野探索</li>
            <li class="done">搭配竞技场挑战</li>
            <li>每日灵感任务</li>
            <li>采集素材</li>
            <li>周常梦境挑战</li>
            <li>体力消耗</li>
          </ul>
        </div>
      </article>

      <article class="lm-card week-card">
        <div class="lm-card-title">
          <h2>本周任务</h2>
          <button>4/7 已完成</button>
        </div>
        <div v-for="item in weekTasks" :key="item[0]" class="week-row">
          <span>{{ item[0] }}</span>
          <div class="lm-progress-line"><i :style="{ width: item[1].split('/')[0] === item[1].split('/')[1] ? '100%' : '64%' }"></i></div>
          <b>{{ item[1] }}</b>
        </div>
      </article>

      <article class="lm-card countdown-card">
        <div class="lm-card-title">
          <h2>活动倒计时</h2>
          <button>查看全部</button>
        </div>
        <div v-for="item in countdowns" :key="item[0]" class="countdown-row">
          <div class="mini-scene lm-cover nikki"></div>
          <div><strong>{{ item[0] }}</strong><p>{{ item[1] }}</p></div>
          <em>{{ item[2] }}</em>
        </div>
      </article>

      <article class="lm-card live-card">
        <div class="lm-card-title">
          <h2>直播 / 版本更新</h2>
          <button>查看全部</button>
        </div>
        <div class="live-row">
          <strong>5.2版本前瞻特别节目</strong>
          <span>2 天后开始</span>
          <p>04-30 19:30</p>
          <button>设置提醒</button>
        </div>
        <div class="live-row blue">
          <strong>5.2版本更新维护</strong>
          <span>3 天后开始</span>
          <p>05-02 06:00 - 12:00</p>
          <button>设置提醒</button>
        </div>
      </article>

      <article class="lm-card account-card">
        <div class="lm-card-title">
          <h2>账号资产</h2>
          <button>管理</button>
        </div>
        <div class="safe-box"></div>
        <div class="account-list">
          <p>官方账号（主）<span>已保护</span></p>
          <p>小号-搭配测试<span>已保护</span></p>
          <p>Nintendo Switch 账号<span>已保护</span></p>
        </div>
        <footer>共 3 个资产 <button>查看全部</button></footer>
      </article>

      <article class="lm-card note-card">
        <div class="lm-card-title">
          <h2>最近笔记</h2>
          <button>查看全部</button>
        </div>
        <p>5.1版本剧情音乐彩蛋分析与搭配建议 <span>2小时前</span></p>
        <p>心愿原野探索路线规划 <span>昨天</span></p>
        <p>搭配赛主题：春日余念风格整理 <span>2天前</span></p>
        <p>素材收集清单汇总 <span>3天前</span></p>
      </article>

      <article class="lm-card gallery-card">
        <div class="lm-card-title">
          <h2>截图图册</h2>
          <button>查看全部</button>
        </div>
        <div class="gallery-grid">
          <i></i><i></i><i></i><i></i><i></i><i></i>
        </div>
        <p>共 8 个图册，126 张照片</p>
      </article>

      <article class="lm-card ai-nikki">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 项目建议</h2>
          <button><SvgIcon icon="material-symbols:refresh-rounded" /></button>
        </div>
        <ul>
          <li>你今天还有 3 个日常任务未完成，建议优先处理。</li>
          <li>活动「奇迹之旅第二章」还有 3 天结束，别错过奖励。</li>
          <li>根据你的搭配风格，推荐优先收集「晨曦花园」套装。</li>
          <li>本周素材收集进度偏慢，建议安排集中采集时间。</li>
        </ul>
        <button class="lm-plain-btn">查看完整分析报告 ›</button>
        <div class="lm-robot"></div>
      </article>
    </section>

    <section class="lm-card nikki-activity">
      <div class="lm-card-title">
        <h2>最近动态</h2>
        <button>查看全部动态 ›</button>
      </div>
      <div>
        <p><SvgIcon icon="material-symbols:check-box-rounded" />完成了每日任务 <span>1小时前</span></p>
        <p><SvgIcon icon="material-symbols:article-outline-rounded" />新增笔记 <span>2小时前</span></p>
        <p><SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" />上传了 6 张截图 <span>昨天 21:40</span></p>
        <p><SvgIcon icon="material-symbols:event-upcoming-outline-rounded" />活动进度更新 <span>昨天 18:30</span></p>
        <p><SvgIcon icon="material-symbols:campaign-outline-rounded" />版本公告 <span>04-27 12:00</span></p>
      </div>
    </section>
  </LifeAppShell>
</template>

<style scoped>
.nikki-head {
  display: grid;
  grid-template-columns: 244px minmax(0, 1fr) auto;
  gap: 24px;
  align-items: center;
  padding: 0;
  overflow: hidden;
}

.nikki-hero-img {
  height: 144px;
  border-radius: 8px;
}

.lm-detail-title h1 {
  font-size: 24px;
}

.lm-detail-title h1 span {
  color: #f3bc47;
  font-size: 16px;
}

.nikki-meta {
  display: grid;
  grid-template-columns: repeat(5, auto);
  gap: 0;
  margin-top: 18px;
}

.nikki-meta span {
  min-width: 82px;
  padding-right: 20px;
  border-right: 1px solid #edf0f6;
  color: #8b91a1;
  font-size: 10px;
}

.nikki-meta b {
  display: block;
  margin-top: 7px;
  color: #171d2c;
  font-size: 12px;
}

.nikki-head .lm-actions {
  align-self: start;
  margin: 44px 15px 0 0;
}

.nikki-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  margin-top: 14px;
}

.daily-content {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  gap: 18px;
}

.daily-circle {
  width: 94px;
  height: 94px;
  display: grid;
  place-items: center;
  align-content: center;
  border-radius: 50%;
  background:
    radial-gradient(circle, #fff 0 56%, transparent 57%),
    conic-gradient(#765ce8 0 62%, #eef0f7 62%);
}

.daily-circle strong {
  font-size: 18px;
}

.daily-circle span {
  color: #8b91a1;
  font-size: 10px;
}

.daily-content ul {
  margin: 0;
  padding: 0;
  list-style: none;
  color: #5a6275;
  font-size: 10px;
}

.daily-content li {
  margin-bottom: 8px;
}

.daily-content li::before {
  content: '';
  display: inline-block;
  width: 8px;
  height: 8px;
  margin-right: 8px;
  border: 1px solid #cfd4e1;
  border-radius: 50%;
}

.daily-content li.done::before {
  border-color: #765ce8;
  background: #765ce8;
}

.week-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) 150px 32px;
  gap: 10px;
  align-items: center;
  margin-bottom: 10px;
  color: #485165;
  font-size: 10px;
}

.week-row b {
  color: #7d8495;
  font-weight: 500;
}

.countdown-row {
  display: grid;
  grid-template-columns: 54px minmax(0, 1fr) auto;
  gap: 11px;
  align-items: center;
  margin-bottom: 12px;
}

.mini-scene {
  height: 54px;
  border-radius: 7px;
}

.countdown-row strong,
.live-row strong {
  display: block;
  color: #242b3c;
  font-size: 11px;
}

.countdown-row p,
.live-row p {
  margin: 5px 0 0;
  color: #8b91a1;
  font-size: 9px;
}

.countdown-row em {
  color: #e66591;
  font-size: 18px;
  font-style: normal;
}

.live-row {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto;
  gap: 4px 12px;
  padding: 11px 0;
  border-bottom: 1px solid #edf0f6;
}

.live-row span {
  color: #8b91a1;
  font-size: 10px;
}

.live-row button {
  grid-row: 2;
  grid-column: 2;
  padding: 5px 10px;
  border-radius: 999px;
  background: #fafafe;
  color: #765ce8;
  font-size: 10px;
}

.safe-box {
  width: 95px;
  height: 92px;
  float: left;
  margin-right: 15px;
  border-radius: 15px;
  background:
    radial-gradient(circle at 50% 50%, #f8f8ff 0 18%, #7e6ee6 19% 24%, transparent 25%),
    linear-gradient(135deg, #d7d1ff, #9d90f0);
  box-shadow: 0 14px 25px rgba(114, 94, 214, 0.18);
}

.account-list p,
.note-card p {
  display: flex;
  justify-content: space-between;
  margin: 9px 0;
  color: #444c5f;
  font-size: 10px;
}

.account-list span,
.note-card span {
  color: #31a978;
}

.account-card footer {
  clear: both;
  padding-top: 12px;
  color: #7f8696;
  font-size: 10px;
}

.account-card footer button {
  float: right;
  background: transparent;
  color: #765ce8;
}

.gallery-grid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 6px;
}

.gallery-grid i {
  height: 48px;
  border-radius: 7px;
  background: linear-gradient(135deg, #d5e8ff, #ffd8e5);
}

.gallery-grid i:nth-child(even) {
  background: linear-gradient(135deg, #c7e3b8, #f1e1c8);
}

.gallery-card p {
  margin: 12px 0 0;
  color: #7f8696;
  font-size: 10px;
}

.ai-nikki {
  position: relative;
}

.ai-nikki ul {
  margin: 0;
  padding-left: 16px;
  color: #596174;
  font-size: 10px;
  line-height: 2;
}

.ai-nikki .lm-robot {
  position: absolute;
  right: 8px;
  bottom: -8px;
  transform: scale(0.72);
}

.nikki-activity {
  margin-top: 14px;
}

.nikki-activity > div:last-child {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 10px;
}

.nikki-activity p {
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr);
  gap: 8px;
  margin: 0;
  color: #495164;
  font-size: 10px;
}

.nikki-activity .svg-icon {
  color: #63bd96;
  font-size: 21px;
}

.nikki-activity span {
  color: #8b91a1;
}

@media (max-width: 1180px) {
  .nikki-head,
  .nikki-grid,
  .nikki-activity > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

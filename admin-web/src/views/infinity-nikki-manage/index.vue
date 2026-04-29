<script setup lang="ts">
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';

defineOptions({
  name: 'InfinityNikkiManage'
});

const tabs = [
  ['基础信息', 'material-symbols:info-outline-rounded'],
  ['日常模板', 'material-symbols:calendar-month-outline-rounded'],
  ['提醒规则', 'material-symbols:notifications-outline-rounded'],
  ['资产关联', 'material-symbols:inventory-2-outline-rounded'],
  ['图册同步', 'material-symbols:photo-library-outline-rounded'],
  ['AI 设置', 'material-symbols:auto-awesome-rounded']
];

const stats = [
  ['任务模板', '12', 'material-symbols:checklist-rounded'],
  ['提醒规则', '8', 'material-symbols:alarm-outline-rounded'],
  ['关联资产', '3', 'material-symbols:account-balance-wallet-outline-rounded'],
  ['图册数量', '5', 'material-symbols:image-outline-rounded'],
  ['AI 任务', '2', 'material-symbols:auto-awesome-rounded']
];

const reminders = ['活动结束提醒', '直播开始提醒', '版本维护提醒', '体力/资源溢出提醒'];
const templates = ['每日基础任务', '采集与材料收集', '挑战关卡', '周常任务'];
</script>

<template>
  <LifeAppShell active="无限暖暖" avatar="nikki">
    <header class="nikki-manage-top lm-topbar">
      <div class="lm-title">
        <h1>←　项目 / 无限暖暖 / 管理</h1>
      </div>
      <div class="lm-actions">
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:search-rounded" /></button>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></button>
        <button class="lm-purple-btn"><SvgIcon icon="material-symbols:add-rounded" />新建</button>
      </div>
    </header>

    <section class="nikki-manage-hero lm-card">
      <div class="top-avatar lm-hero-art nikki"></div>
      <div class="lm-detail-title">
        <h1>无限暖暖 <span class="lm-tag pink">游戏项目</span> <span class="lm-tag green">进行中</span></h1>
        <p>记录游戏日常、活动、版本与养成进度，管理账号与相关资源。</p>
        <div class="hero-fields">
          <span>创建时间 <b>2025-04-10</b></span>
          <span>开始时间 <b>2024-12-05</b></span>
          <span>类型 <b>游戏</b></span>
          <span>时区 <b>Asia/Shanghai</b></span>
        </div>
      </div>
      <div class="stat-strip">
        <div v-for="item in stats" :key="item[0]">
          <SvgIcon :icon="item[2]" />
          <span>{{ item[0] }}</span>
          <strong>{{ item[1] }}</strong>
        </div>
      </div>
    </section>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item[0]" :class="{ active: item[0] === '基础信息' }">
        <SvgIcon :icon="item[1]" />{{ item[0] }}
      </button>
    </nav>

    <section class="lm-grid nikki-manage-grid">
      <article class="lm-card info-card">
        <div class="lm-card-title"><h2>基本信息</h2></div>
        <dl>
          <dt>项目名称</dt><dd>无限暖暖</dd>
          <dt>项目类型</dt><dd>游戏项目</dd>
          <dt>状态</dt><dd><span class="lm-tag green">进行中</span></dd>
          <dt>简介</dt><dd>记录日常任务、活动提醒、版本更新、抽卡计划、搭配笔记等内容。</dd>
          <dt>封面</dt><dd><span class="small-cover lm-cover nikki"></span><button class="lm-plain-btn">更换封面</button></dd>
          <dt>标签</dt><dd><span class="lm-tag">换装</span> <span class="lm-tag blue">日常</span> <span class="lm-tag amber">活动</span> <span class="lm-tag pink">收集</span></dd>
        </dl>
      </article>

      <article class="lm-card reset-card">
        <div class="lm-card-title"><h2>游戏重置设置</h2></div>
        <div class="reset-time">
          <span>每日重置时间</span>
          <strong>04:00</strong>
          <SvgIcon icon="material-symbols:alarm-outline-rounded" />
        </div>
        <div class="time-axis"><i></i><i></i><i></i><i></i><i></i><i></i><em>04:00</em></div>
        <div class="week-axis"><b>一</b><span>二</span><span>三</span><span>四</span><span>五</span><span>六</span><span>日</span></div>
        <div class="reset-time">
          <span>版本更新时段提醒</span>
          <strong>提前 1 天　09:00</strong>
          <SvgIcon icon="material-symbols:alarm-outline-rounded" />
        </div>
        <p>将在版本维护或更新前提醒你</p>
      </article>

      <article class="lm-card reminder-card">
        <div class="lm-card-title"><h2>提醒规则 <span>（用于生成倒计时与通知）</span></h2></div>
        <div v-for="item in reminders" :key="item" class="rule-row">
          <span class="lm-mini-icon"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></span>
          <div><strong>{{ item }}</strong><p>提前开启相关提醒（自定义编辑）</p></div>
          <em>启用中</em>
          <button>···</button>
        </div>
        <button class="add-line">＋ 新建提醒规则</button>
      </article>

      <article class="lm-card template-card">
        <div class="lm-card-title"><h2>日常任务模板</h2><button>类型　重置</button></div>
        <div v-for="item in templates" :key="item" class="template-row">
          <span class="lm-mini-icon"><SvgIcon icon="material-symbols:event-available-outline-rounded" /></span>
          <div><strong>{{ item }}</strong><p>完成日常活跃、提醒攻略与收集</p></div>
          <em>{{ item === '周常任务' ? '每周' : '每日' }}</em>
          <time>{{ item === '周常任务' ? '每周一 04:00' : '每日 04:00' }}</time>
        </div>
        <button class="add-line">＋ 添加模板</button>
      </article>

      <article class="lm-card asset-card">
        <div class="lm-card-title"><h2>关联资产 <span>（用于账号与充值管理）</span></h2></div>
        <p><span class="lm-avatar"></span>主账号（国服）<em>已绑定</em></p>
        <p><span class="lm-avatar"></span>攻略号（小号）<em>已绑定</em></p>
        <p><span class="lm-avatar"></span>充值账号（备用）<em>已绑定</em></p>
        <button class="add-line">＋ 添加账号 / 资产</button>
      </article>

      <article class="lm-card oss-card">
        <div class="lm-card-title"><h2>图册与 OSS 同步</h2></div>
        <h3><SvgIcon icon="material-symbols:cloud-done-outline-rounded" />阿里云 OSS <span class="lm-tag green">已连接</span></h3>
        <dl>
          <dt>同步状态</dt><dd>正常</dd>
          <dt>最近同步</dt><dd>2025-05-20 10:30</dd>
          <dt>自动同步</dt><dd>已开启（每日 02:30）</dd>
          <dt>同步内容</dt><dd>截图、活动图、视频</dd>
        </dl>
        <button class="lm-plain-btn">立即同步</button>
        <button class="lm-plain-btn">设置同步规则</button>
      </article>

      <article class="lm-card ai-setting-card">
        <div class="lm-card-title"><h2>AI 自动化设置</h2></div>
        <p>活动总结<span class="lm-switch"><i></i></span></p>
        <p>日常复盘<span class="lm-switch"><i></i></span></p>
        <p>攻略笔记整理<span class="lm-switch"><i></i></span></p>
        <p>版本更新解读<span class="lm-switch"><i></i></span></p>
        <button>查看 AI 任务历史</button>
      </article>

      <article class="lm-card integration-card">
        <div class="lm-card-title"><h2>集成与通知</h2></div>
        <div>
          <p><SvgIcon icon="material-symbols:send-outline-rounded" />飞书机器人 <span>已连接</span></p>
          <p><SvgIcon icon="material-symbols:hub-outline-rounded" />OpenClaw <span>已连接</span></p>
          <p><SvgIcon icon="material-symbols:share-outline-rounded" />Webhook <span>已连接</span></p>
          <button>＋ 添加集成</button>
        </div>
      </article>
    </section>

    <footer class="lm-bottom-bar">
      <button class="lm-plain-btn">取消</button>
      <button class="lm-purple-btn">保存配置</button>
    </footer>
  </LifeAppShell>
</template>

<style scoped>
.nikki-manage-top {
  margin-bottom: 12px;
}

.nikki-manage-hero {
  display: grid;
  grid-template-columns: 80px minmax(0, 1fr) 440px;
  gap: 18px;
  align-items: center;
  min-height: 118px;
}

.top-avatar {
  width: 80px;
  height: 80px;
  border-radius: 8px;
}

.hero-fields {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 8px;
  margin-top: 17px;
}

.hero-fields span {
  padding: 9px 11px;
  border-radius: 6px;
  background: #fafafe;
  color: #8b91a1;
  font-size: 10px;
}

.hero-fields b {
  display: block;
  margin-top: 6px;
  color: #222839;
}

.stat-strip {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 0;
  align-self: stretch;
}

.stat-strip div {
  display: grid;
  place-items: center;
  align-content: center;
  gap: 5px;
  border-left: 1px solid #edf0f6;
  color: #7f8696;
  font-size: 10px;
}

.stat-strip .svg-icon {
  color: #d7992f;
  font-size: 18px;
}

.stat-strip strong {
  color: #1b2130;
  font-size: 16px;
}

.nikki-manage-grid {
  grid-template-columns: 1.3fr 1.7fr 1.9fr;
  margin-top: 14px;
}

.info-card dl {
  display: grid;
  grid-template-columns: 66px minmax(0, 1fr);
  gap: 11px;
  margin: 0;
  color: #4f5769;
  font-size: 11px;
}

.info-card dt {
  color: #8b91a1;
}

.info-card dd {
  margin: 0;
}

.small-cover {
  display: inline-block;
  width: 42px;
  height: 42px;
  margin-right: 9px;
  border-radius: 6px;
  vertical-align: middle;
}

.reset-time {
  display: grid;
  grid-template-columns: minmax(0, 1fr) auto 18px;
  align-items: center;
  gap: 10px;
  color: #7f8696;
  font-size: 11px;
}

.reset-time strong {
  color: #202636;
  font-size: 13px;
}

.time-axis {
  position: relative;
  display: flex;
  justify-content: space-between;
  margin: 25px 0 17px;
  padding: 0 8px;
}

.time-axis::before {
  content: '';
  position: absolute;
  left: 16px;
  right: 16px;
  top: 5px;
  height: 2px;
  background: #e8eaf3;
}

.time-axis i {
  position: relative;
  z-index: 1;
  width: 7px;
  height: 7px;
  border-radius: 50%;
  background: #d8d5f5;
}

.time-axis i:first-child {
  background: #765ce8;
}

.time-axis em {
  position: absolute;
  right: 33px;
  top: -18px;
  color: #765ce8;
  font-size: 10px;
  font-style: normal;
}

.week-axis {
  display: flex;
  justify-content: space-around;
  margin-bottom: 21px;
  color: #9aa0ae;
  font-size: 11px;
}

.week-axis b {
  display: grid;
  place-items: center;
  width: 18px;
  height: 18px;
  border-radius: 50%;
  background: #765ce8;
  color: #fff;
}

.reset-card > p {
  color: #765ce8;
  font-size: 10px;
}

.rule-row,
.template-row {
  display: grid;
  grid-template-columns: 26px minmax(0, 1fr) auto 24px;
  gap: 10px;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border-radius: 7px;
  background: #fafafe;
}

.template-row {
  grid-template-columns: 26px minmax(0, 1fr) auto 72px;
}

.rule-row strong,
.template-row strong {
  display: block;
  color: #252b3c;
  font-size: 11px;
}

.rule-row p,
.template-row p {
  margin: 4px 0 0;
  color: #8b91a1;
  font-size: 9px;
}

.rule-row em,
.template-row em {
  color: #31a978;
  font-size: 10px;
  font-style: normal;
}

.template-row time {
  color: #7f8696;
  font-size: 10px;
}

.add-line {
  background: transparent;
  color: #765ce8;
  font-size: 11px;
}

.template-card,
.integration-card {
  grid-column: span 1;
}

.asset-card p {
  display: grid;
  grid-template-columns: 24px minmax(0, 1fr) auto;
  align-items: center;
  gap: 9px;
  margin: 12px 0;
  color: #343b4d;
  font-size: 11px;
}

.asset-card .lm-avatar {
  width: 22px;
  height: 22px;
}

.asset-card em {
  color: #7f8696;
  font-style: normal;
}

.oss-card h3 {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 0 0 15px;
  color: #222839;
  font-size: 13px;
}

.oss-card dl {
  display: grid;
  grid-template-columns: 70px minmax(0, 1fr);
  gap: 10px;
  color: #596174;
  font-size: 11px;
}

.oss-card dt {
  color: #8b91a1;
}

.oss-card dd {
  margin: 0;
  text-align: right;
}

.ai-setting-card p {
  display: flex;
  justify-content: space-between;
  margin: 12px 0;
  color: #343b4d;
  font-size: 11px;
}

.ai-setting-card button {
  background: transparent;
  color: #765ce8;
  font-size: 11px;
}

.integration-card {
  grid-column: 1 / -1;
}

.integration-card > div:last-child {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 15px;
}

.integration-card p,
.integration-card button {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  min-height: 40px;
  margin: 0;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #343b4d;
  font-size: 11px;
}

.integration-card span {
  color: #31a978;
}

@media (max-width: 1180px) {
  .nikki-manage-hero,
  .nikki-manage-grid,
  .integration-card > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .stat-strip,
  .hero-fields {
    grid-template-columns: repeat(3, minmax(0, 1fr));
  }
}
</style>

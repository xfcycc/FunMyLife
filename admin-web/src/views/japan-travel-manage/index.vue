<script setup lang="ts">
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';

defineOptions({
  name: 'JapanTravelManage'
});

const tabs = ['基础信息', '行程规划', '同行人员', '清单模板', '资产资料', '图册同步', 'AI 设置'];

const days = [
  { date: '5/20 周三', day: 'DAY 1', title: '上海 → 东京', tasks: ['抵达成田机场', '入住酒店', '新宿逛街'] },
  { date: '5/21 周四', day: 'DAY 2', title: '东京市区', tasks: ['浅草寺', '天空树', '银座'] },
  { date: '5/22 周五', day: 'DAY 3', title: '镰仓一日游', tasks: ['镰仓高校前', '江之电', '长谷寺'] },
  { date: '5/23 周六', day: 'DAY 4', title: '富士山周边', tasks: ['河口湖', '网红打卡', '温泉酒店'] }
];

const people = ['我（夏目悠然）', '小悠', '阿澄', '小白'];
const docs = ['护照信息', '机票行程单', '酒店预订单', '旅行保险单', '签证资料', '接送机信息', '租车订单'];
</script>

<template>
  <LifeAppShell active="管理" avatar="default">
    <header class="manage-top lm-topbar">
      <div class="lm-title">
        <p>项目　/　日本旅行 2026　/　管理</p>
        <h1>项目管理 <span>🌸</span></h1>
      </div>
      <div class="lm-actions">
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:search-rounded" /></button>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></button>
        <button class="lm-purple-btn round-only"><SvgIcon icon="material-symbols:add-rounded" /></button>
      </div>
    </header>

    <section class="manage-hero lm-card">
      <div class="manage-cover lm-cover japan"></div>
      <div>
        <h2>日本旅行 2026 <span class="lm-tag green">进行中</span></h2>
        <div class="manage-meta">
          <p><SvgIcon icon="material-symbols:calendar-month-outline-rounded" />旅行日期 <b>2026.05.20 - 2026.06.02</b><span>共 14 天</span></p>
          <p><SvgIcon icon="material-symbols:groups-outline-rounded" />同行人数 <b>3 人</b><span>👩🏻‍🦰 👩🏻 👧🏻 +1</span></p>
          <p><SvgIcon icon="material-symbols:location-on-outline-rounded" />目的地 <b>东京 · 京都 · 大阪 · 奈良</b><span>富士山 · 镰仓</span></p>
        </div>
      </div>
      <div class="project-progress">
        <div>68%</div>
        <span>项目进度</span>
      </div>
      <button class="edit-plan">编辑封面</button>
    </section>

    <nav class="lm-tabs manage-tabs">
      <button v-for="item in tabs" :key="item" :class="{ active: item === '基础信息' }">{{ item }}</button>
    </nav>

    <section class="lm-grid manage-grid">
      <article class="lm-card basic-info">
        <div class="lm-card-title"><h2><span>01</span>基础信息</h2></div>
        <dl>
          <dt>项目名称</dt><dd>日本旅行 2026</dd>
          <dt>旅行类型</dt><dd>自由行</dd>
          <dt>出发城市</dt><dd>上海</dd>
          <dt>目的地</dt><dd>东京 / 京都 / 大阪 / 奈良 / 富士山</dd>
          <dt>预算</dt><dd>￥18,000 <span class="lm-tag">个人</span></dd>
          <dt>备注</dt><dd>初夏赏樱 · 美食 · 购物 · 温泉</dd>
        </dl>
        <button class="lm-plain-btn">编辑信息</button>
      </article>

      <article class="lm-card itinerary-config">
        <div class="lm-card-title"><h2><span>02</span>行程规划</h2><button>查看完整行程</button></div>
        <div class="day-cards">
          <div v-for="item in days" :key="item.day">
            <time>{{ item.date }}</time>
            <small>{{ item.day }}</small>
            <strong>{{ item.title }}</strong>
            <p v-for="task in item.tasks" :key="task">• {{ task }}</p>
          </div>
        </div>
        <button class="add-line">＋ 添加行程日</button>
      </article>

      <article class="lm-card people-card">
        <div class="lm-card-title"><h2><span>03</span>同行人员</h2><button>管理人员 ›</button></div>
        <div class="people-list">
          <p v-for="item in people" :key="item"><span class="lm-avatar"></span>{{ item }} <em>同行者</em></p>
        </div>
        <button class="add-line">＋ 添加同行人员</button>
      </article>

      <article class="lm-card template-card">
        <div class="lm-card-title"><h2><span>04</span>清单模板</h2><button>管理模板</button></div>
        <p>行前准备清单 <b>32 项</b></p>
        <p>行李打包清单 <b>56 项</b></p>
        <p>每日必备清单 <b>18 项</b></p>
        <p>应急药品清单 <b>15 项</b></p>
        <button class="add-line">＋ 新建清单模板</button>
      </article>

      <article class="lm-card asset-config">
        <div class="lm-card-title"><h2><span>05</span>资产资料</h2><button>管理资料 ›</button></div>
        <div>
          <button v-for="item in docs" :key="item"><SvgIcon icon="material-symbols:folder-outline-rounded" />{{ item }}<span>已上传</span></button>
          <button><SvgIcon icon="material-symbols:add-rounded" />上传资料</button>
        </div>
      </article>

      <article class="lm-card oss-card">
        <div class="lm-card-title"><h2><span>06</span>图册同步（阿里云 OSS）</h2><button>查看相册</button></div>
        <div class="oss-inner">
          <div class="oss-cover lm-cover japan"></div>
          <div>
            <strong>日本旅行 2026 图册</strong>
            <p>已同步 328 张 / 12.6 GB</p>
            <span class="lm-tag green">同步成功</span>
          </div>
        </div>
        <div class="lm-progress-line"><i style="width: 12.6%"></i></div>
        <button class="lm-purple-btn">立即同步</button>
        <button class="lm-plain-btn">同步设置</button>
      </article>

      <article class="lm-card notice-card">
        <div class="lm-card-title"><h2>提醒与通知规则</h2><button>管理规则</button></div>
        <p>行程提醒 <span>出发前 7 天提醒</span><em>邮件 + 飞书</em></p>
        <p>航班提醒 <span>起飞前 24 小时</span><em>邮件 + 浏览器通知</em></p>
        <p>酒店入住提醒 <span>入住当天 10:00</span><em>飞书</em></p>
        <p>证件到期提醒 <span>护照到期前 60 天</span><em>邮件</em></p>
      </article>

      <article class="lm-card automation-card">
        <div class="lm-card-title"><h2>AI 自动化设置</h2><button>管理 AI 规则 ›</button></div>
        <div>
          <section><strong>旅行总结</strong><p>旅行结束后自动生成图文总结与回顾</p><span class="lm-switch"><i></i></span></section>
          <section><strong>行前建议</strong><p>根据行程和目的地生成准备建议</p><span class="lm-switch"><i></i></span></section>
          <section><strong>行程风险检查</strong><p>识别潜在风险并提供调整建议</p><span class="lm-switch"><i></i></span></section>
          <section><strong>每日回顾</strong><p>每天行程结束生成简短回顾</p><span class="lm-switch"><i></i></span></section>
          <section><strong>更多 AI 能力</strong><p>持续解锁中...</p><b>›</b></section>
        </div>
      </article>
    </section>

    <footer class="lm-bottom-bar">
      <button class="lm-plain-btn">返回项目首页</button>
      <span>✓ 所有更改已自动保存</span>
      <button class="lm-plain-btn">预览项目概览</button>
      <button class="lm-plain-btn">导出项目配置</button>
      <button class="lm-purple-btn">保存配置</button>
    </footer>
  </LifeAppShell>
</template>

<style scoped>
.round-only {
  width: 36px;
  padding: 0;
  border-radius: 50%;
}

.manage-top {
  margin-bottom: 12px;
}

.manage-hero {
  position: relative;
  display: grid;
  grid-template-columns: 190px minmax(0, 1fr) 130px;
  gap: 19px;
  align-items: center;
  min-height: 119px;
  padding: 13px;
  overflow: hidden;
}

.manage-hero::after {
  content: '';
  position: absolute;
  right: 0;
  top: 0;
  width: 145px;
  height: 100%;
  background: radial-gradient(circle at 80% 10%, rgba(255, 200, 220, 0.7), transparent 45%);
  pointer-events: none;
}

.manage-cover {
  height: 90px;
  border-radius: 7px;
}

.manage-hero h2 {
  margin: 0 0 18px;
  font-size: 18px;
}

.manage-meta {
  display: grid;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  gap: 18px;
}

.manage-meta p {
  display: grid;
  grid-template-columns: 20px minmax(0, 1fr);
  gap: 7px;
  margin: 0;
  color: #6b7283;
  font-size: 10px;
}

.manage-meta b,
.manage-meta span {
  grid-column: 2;
}

.manage-meta b {
  color: #161c2b;
  font-size: 12px;
}

.project-progress {
  position: relative;
  z-index: 1;
  text-align: center;
}

.project-progress div {
  width: 64px;
  height: 64px;
  display: grid;
  place-items: center;
  margin: auto;
  border-radius: 50%;
  background:
    radial-gradient(circle, #fff 0 58%, transparent 59%),
    conic-gradient(#765ce8 0 68%, #eef0f7 68%);
  color: #161c2b;
  font-weight: 800;
}

.project-progress span {
  color: #7f8696;
  font-size: 10px;
}

.edit-plan {
  position: absolute;
  right: 8px;
  top: 48px;
  z-index: 2;
  padding: 5px 9px;
  border-radius: 999px;
  background: rgba(255, 255, 255, 0.78);
  color: #555d70;
  font-size: 10px;
}

.manage-grid {
  grid-template-columns: 1.1fr 2.3fr 1.35fr;
  margin-top: 12px;
}

.lm-card-title h2 span {
  padding: 2px 5px;
  border-radius: 5px;
  background: #f0ecff;
  color: #765ce8;
  font-size: 9px;
}

.basic-info dl {
  display: grid;
  grid-template-columns: 70px minmax(0, 1fr);
  gap: 10px;
  margin: 0 0 14px;
  font-size: 11px;
}

.basic-info dt {
  color: #8b91a1;
}

.basic-info dd {
  margin: 0;
  color: #31384b;
}

.day-cards {
  display: grid;
  grid-template-columns: repeat(4, minmax(0, 1fr));
  gap: 10px;
}

.day-cards div {
  min-height: 101px;
  padding: 10px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
}

.day-cards time,
.day-cards small {
  display: block;
  color: #7f8696;
  font-size: 9px;
}

.day-cards strong {
  display: block;
  margin: 8px 0;
  color: #202636;
  font-size: 12px;
}

.day-cards p {
  margin: 4px 0;
  color: #596174;
  font-size: 9px;
}

.add-line {
  display: block;
  margin: 13px auto 0;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
}

.people-list p {
  display: grid;
  grid-template-columns: 22px minmax(0, 1fr) auto;
  align-items: center;
  gap: 8px;
  margin: 9px 0;
  color: #333a4d;
  font-size: 11px;
}

.people-list .lm-avatar {
  width: 21px;
  height: 21px;
}

.people-list em {
  padding: 2px 6px;
  border-radius: 5px;
  background: #eaf9f2;
  color: #31a978;
  font-size: 9px;
  font-style: normal;
}

.template-card p,
.notice-card p {
  display: flex;
  justify-content: space-between;
  margin: 10px 0;
  color: #41495c;
  font-size: 11px;
}

.template-card b {
  padding: 2px 7px;
  border-radius: 5px;
  background: #f0ecff;
  color: #765ce8;
  font-size: 10px;
}

.asset-config {
  grid-column: span 1;
}

.asset-config > div:last-child {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 9px;
}

.asset-config button {
  min-height: 52px;
  display: grid;
  place-items: center;
  gap: 4px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
  color: #363e51;
  font-size: 10px;
}

.asset-config span {
  color: #8b91a1;
  font-size: 9px;
}

.oss-inner {
  display: grid;
  grid-template-columns: 96px minmax(0, 1fr);
  gap: 12px;
  margin-bottom: 11px;
}

.oss-cover {
  height: 62px;
  border-radius: 7px;
}

.oss-inner strong {
  color: #202636;
  font-size: 12px;
}

.oss-inner p {
  margin: 6px 0;
  color: #7f8696;
  font-size: 10px;
}

.oss-card .lm-purple-btn,
.oss-card .lm-plain-btn {
  height: 27px;
  margin-top: 10px;
}

.notice-card {
  grid-column: span 1;
}

.notice-card span,
.notice-card em {
  color: #7f8696;
  font-style: normal;
}

.automation-card {
  grid-column: span 2;
}

.automation-card > div:last-child {
  display: grid;
  grid-template-columns: repeat(5, minmax(0, 1fr));
  gap: 10px;
}

.automation-card section {
  min-height: 80px;
  padding: 11px;
  border: 1px solid #edf0f6;
  border-radius: 7px;
  background: #fff;
}

.automation-card strong {
  color: #202636;
  font-size: 11px;
}

.automation-card p {
  min-height: 28px;
  margin: 7px 0 9px;
  color: #7f8696;
  font-size: 9px;
}

.lm-bottom-bar span {
  margin-right: auto;
  color: #58ad86;
  font-size: 11px;
}

@media (max-width: 1180px) {
  .manage-hero,
  .manage-grid,
  .manage-meta,
  .automation-card > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }

  .automation-card {
    grid-column: span 1;
  }
}
</style>

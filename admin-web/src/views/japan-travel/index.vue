<script setup lang="ts">
import LifeAppShell from '@/components/life-manager/LifeAppShell.vue';

defineOptions({
  name: 'JapanTravel'
});

const tabs = [
  ['概览', 'material-symbols:calendar-month-outline-rounded'],
  ['行程', 'material-symbols:business-center-outline-rounded'],
  ['清单', 'material-symbols:checklist-rounded'],
  ['资产', 'material-symbols:inventory-2-outline-rounded'],
  ['记录', 'material-symbols:edit-square-outline-rounded'],
  ['图册', 'material-symbols:photo-library-outline-rounded'],
  ['AI', 'material-symbols:auto-awesome-rounded']
];

const schedule = [
  ['09:30', '浅草寺', '东京 · 台东区', '进行中'],
  ['13:00', '银座购物', '东京 · 中央区', '计划中'],
  ['18:00', '新宿晚餐', '东京 · 新宿区', '计划中'],
  ['全天', '前往京都（新干线）', '东京 → 京都', '待开始']
];

const assets = [
  ['去程机票', '6月15日 CA929 上海 → 东京', '已出票'],
  ['东京酒店', '6月15日 - 6月18日（3晚）', '已确认']
];
</script>

<template>
  <LifeAppShell active="日本旅行 2026" show-projects>
    <header class="travel-head lm-topbar">
      <div class="breadcrumb">← 日本旅行 2026 <span>旅行</span> ★</div>
      <div class="lm-actions">
        <button class="lm-plain-btn"><SvgIcon icon="material-symbols:ios-share-rounded" />分享</button>
        <button class="lm-plain-btn"><SvgIcon icon="material-symbols:edit-outline-rounded" />编辑</button>
        <button class="lm-purple-btn"><SvgIcon icon="material-symbols:add-box-outline-rounded" />新建</button>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:notifications-outline-rounded" /></button>
        <button class="lm-icon-btn"><SvgIcon icon="material-symbols:more-horiz-rounded" /></button>
      </div>
    </header>

    <section class="travel-hero lm-hero-art japan">
      <div class="hero-copy">
        <h1>日本旅行 2026 <span>旅行</span></h1>
        <p>东京 · 京都 · 大阪 · 奈良 · 富士山</p>
        <p>2026.06.15 - 2026.06.26（共 12 天）</p>
      </div>
      <div class="countdown-box">
        <span>出发倒计时</span>
        <strong>48<em>天</em> 08<em>时</em> 32<em>分</em> 16<em>秒</em></strong>
        <p>2026-06-15（周一）出发</p>
      </div>
      <div class="weather-box">
        <span>东京 5月28日</span>
        <strong><SvgIcon icon="material-symbols:sunny-rounded" />18℃ - 25℃</strong>
        <p>晴转多云</p>
      </div>
    </section>

    <nav class="lm-tabs">
      <button v-for="item in tabs" :key="item[0]" :class="{ active: item[0] === '概览' }">
        <SvgIcon :icon="item[1]" />{{ item[0] }}
      </button>
    </nav>

    <section class="lm-grid travel-grid">
      <article class="lm-card">
        <div class="lm-card-title">
          <h2>今日/近期行程</h2>
          <button>查看全部 ›</button>
        </div>
        <div class="route-list">
          <div v-for="item in schedule" :key="item[1]">
            <time>{{ item[0] }}</time>
            <span></span>
            <div><strong>{{ item[1] }}</strong><p>{{ item[2] }}</p></div>
            <em>{{ item[3] }}</em>
          </div>
        </div>
      </article>

      <article class="lm-card checklist-progress">
        <div class="lm-card-title">
          <h2>行前清单进度</h2>
          <button>查看清单</button>
        </div>
        <div class="ring">72%<span>已完成</span></div>
        <div class="progress-items">
          <p><i></i>证件材料 <b>8/8</b></p>
          <p><i></i>行李物品 <b>14/20</b></p>
          <p><i></i>预订确认 <b>6/7</b></p>
          <p><i></i>其他事项 <b>5/8</b></p>
        </div>
      </article>

      <article class="lm-card">
        <div class="lm-card-title">
          <h2>机票与酒店资产</h2>
          <button>查看全部</button>
        </div>
        <div class="asset-list">
          <div v-for="item in assets" :key="item[0]">
            <span class="lm-soft-icon"><SvgIcon icon="material-symbols:flight-takeoff-rounded" /></span>
            <div><strong>{{ item[0] }}</strong><p>{{ item[1] }}</p></div>
            <em>{{ item[2] }}</em>
          </div>
        </div>
        <footer>全部资产 8 项 →</footer>
      </article>

      <article class="lm-card budget-card">
        <div class="lm-card-title">
          <h2>预算概览</h2>
          <button>查看明细</button>
        </div>
        <strong>¥ 18,620.00 <span>总预算</span></strong>
        <div class="budget-row"><span>已使用</span><b>¥ 7,850.00</b><em>42%</em></div>
        <div class="lm-progress-line"><i style="width: 42%"></i></div>
        <div class="budget-row"><span>剩余</span><b>¥ 10,770.00</b><em>58%</em></div>
      </article>

      <article class="lm-card weather-card">
        <div class="lm-card-title">
          <h2>天气与地点</h2>
          <button>切换城市</button>
        </div>
        <div class="city-tabs"><span>东京</span><span>京都</span><span>大阪</span></div>
        <div class="big-weather"><SvgIcon icon="material-symbols:partly-cloudy-day-rounded" /><strong>22℃</strong><span>多云</span></div>
        <div class="forecast"><span>5/30<br />23°/19°</span><span>5/31<br />24°/18°</span><span>6/1<br />25°/19°</span></div>
      </article>

      <article class="lm-card album-card">
        <div class="lm-card-title">
          <h2>图册预览</h2>
          <button>查看全部</button>
        </div>
        <div class="photo-row">
          <i></i><i></i><i></i><i></i>
        </div>
        <p>共 128 张照片，12 个视频</p>
      </article>

      <article class="lm-card notes-card">
        <div class="lm-card-title">
          <h2>旅行笔记 / 攻略</h2>
          <button>查看全部</button>
        </div>
        <p>东京必去景点清单 <time>5月20日</time></p>
        <p>京都交通与景点攻略 <time>5月18日</time></p>
        <p>日本旅行必备 APP 推荐 <time>5月15日</time></p>
        <button class="add-note">＋ 新建笔记</button>
      </article>

      <article class="lm-card ai-travel">
        <div class="lm-card-title">
          <h2><span class="lm-mini-icon"><SvgIcon icon="material-symbols:auto-awesome-rounded" /></span>AI 行前建议</h2>
          <button>重新生成</button>
        </div>
        <ul>
          <li>东京 6 月中旬天气舒适，建议携带薄外套和雨具。</li>
          <li>新宿周边晚间人流较大，注意随身物品安全。</li>
          <li>你有 2 个景点需要提前预约，别忘记安排时间。</li>
          <li>建议在京都安排 1 天体验传统抹茶和和服拍照。</li>
        </ul>
        <button>查看全部建议 ›</button>
        <div class="lm-robot"></div>
      </article>
    </section>

    <section class="lm-card travel-activity">
      <div class="lm-card-title">
        <h2>最近动态</h2>
        <button>查看全部动态 ›</button>
      </div>
      <div>
        <p><SvgIcon icon="material-symbols:flight-takeoff-rounded" /> 去程机票已出票 <span>5月25日 14:32</span></p>
        <p><SvgIcon icon="material-symbols:apartment-rounded" /> 东京酒店预订成功 <span>5月24日 11:08</span></p>
        <p><SvgIcon icon="material-symbols:check-box-rounded" /> 行李清单更新 <span>5月24日 09:45</span></p>
        <p><SvgIcon icon="material-symbols:add-photo-alternate-outline-rounded" /> 上传了 12 张照片 <span>5月23日 18:20</span></p>
      </div>
    </section>
  </LifeAppShell>
</template>

<style scoped>
.travel-head {
  margin-bottom: 8px;
}

.breadcrumb {
  color: #151927;
  font-size: 17px;
  font-weight: 800;
}

.breadcrumb span {
  margin-left: 8px;
  padding: 2px 7px;
  border-radius: 5px;
  background: #ffedf4;
  color: #e56b93;
  font-size: 10px;
}

.travel-hero {
  height: 181px;
  border-radius: 8px;
  color: #fff;
  box-shadow: 0 16px 34px rgba(52, 79, 116, 0.2);
}

.travel-hero::before {
  inset: 18% 45% 28% 18%;
}

.hero-copy {
  position: relative;
  z-index: 1;
  padding: 24px;
  text-shadow: 0 2px 12px rgba(28, 43, 64, 0.22);
}

.hero-copy h1 {
  margin: 0 0 8px;
  font-size: 29px;
}

.hero-copy h1 span {
  padding: 2px 7px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.75);
  color: #725ae8;
  font-size: 11px;
}

.hero-copy p {
  margin: 6px 0;
  font-size: 12px;
}

.countdown-box,
.weather-box {
  position: absolute;
  z-index: 2;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.9);
  color: #131827;
  box-shadow: 0 12px 26px rgba(37, 47, 78, 0.16);
}

.countdown-box {
  left: 19px;
  bottom: 13px;
  width: 190px;
  padding: 13px 16px;
}

.countdown-box span,
.weather-box span,
.countdown-box p,
.weather-box p {
  color: #7a8293;
  font-size: 10px;
}

.countdown-box strong {
  display: block;
  margin: 8px 0;
  font-size: 19px;
}

.countdown-box em {
  margin: 0 5px 0 2px;
  color: #626a7c;
  font-size: 10px;
  font-style: normal;
}

.countdown-box p,
.weather-box p {
  margin: 0;
}

.weather-box {
  right: 17px;
  bottom: 12px;
  width: 128px;
  padding: 13px;
}

.weather-box strong {
  display: flex;
  align-items: center;
  gap: 8px;
  margin: 8px 0 5px;
}

.weather-box .svg-icon {
  color: #f5bb43;
  font-size: 25px;
}

.travel-grid {
  grid-template-columns: repeat(4, minmax(0, 1fr));
  margin-top: 14px;
}

.route-list {
  display: grid;
  gap: 13px;
}

.route-list div {
  display: grid;
  grid-template-columns: 40px 10px minmax(0, 1fr) auto;
  gap: 9px;
  align-items: start;
}

.route-list time,
.route-list p,
.asset-list p,
.travel-activity span {
  color: #8b91a1;
  font-size: 10px;
}

.route-list span {
  width: 8px;
  height: 8px;
  margin-top: 4px;
  border-radius: 50%;
  background: #775fe8;
  box-shadow: 0 0 0 4px #f0ecff;
}

.route-list strong,
.asset-list strong {
  display: block;
  color: #202636;
  font-size: 12px;
}

.route-list p,
.asset-list p {
  margin: 4px 0 0;
}

.route-list em,
.asset-list em {
  padding: 2px 7px;
  border-radius: 5px;
  background: #eaf9f2;
  color: #31a978;
  font-size: 9px;
  font-style: normal;
}

.checklist-progress {
  display: grid;
  grid-template-columns: 112px minmax(0, 1fr);
  align-items: center;
}

.checklist-progress .lm-card-title {
  grid-column: 1 / -1;
}

.ring {
  width: 88px;
  height: 88px;
  display: grid;
  place-items: center;
  border-radius: 50%;
  background: conic-gradient(#765ce8 0 72%, #eef0f7 72%);
  color: #171d2c;
  font-size: 20px;
  position: relative;
}

.ring::after {
  content: '';
  position: absolute;
  inset: 12px;
  border-radius: 50%;
  background: #fff;
}

.ring {
  isolation: isolate;
}

.ring span {
  display: block;
  color: #8b91a1;
  font-size: 10px;
}

.ring > * {
  z-index: 1;
}

.progress-items p,
.notes-card p {
  display: flex;
  justify-content: space-between;
  margin: 9px 0;
  color: #596174;
  font-size: 11px;
}

.progress-items i {
  display: inline-block;
  width: 8px;
  height: 8px;
  margin-right: 8px;
  border-radius: 50%;
  background: #58c99c;
}

.asset-list {
  display: grid;
  gap: 14px;
}

.asset-list div {
  display: grid;
  grid-template-columns: 30px minmax(0, 1fr) auto;
  gap: 10px;
  align-items: center;
}

.asset-list footer {
  margin-top: 11px;
}

.budget-card > strong {
  display: block;
  margin: 7px 0 18px;
  color: #101624;
  font-size: 22px;
}

.budget-card > strong span {
  color: #8b91a1;
  font-size: 10px;
  font-weight: 500;
}

.budget-row {
  display: grid;
  grid-template-columns: 1fr auto auto;
  margin: 11px 0 6px;
  color: #71788a;
  font-size: 11px;
}

.city-tabs {
  display: flex;
  justify-content: space-around;
  color: #765ce8;
  font-size: 11px;
}

.big-weather {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 9px;
  margin: 18px 0;
}

.big-weather .svg-icon {
  color: #f3bf49;
  font-size: 28px;
}

.big-weather strong {
  font-size: 24px;
}

.forecast {
  display: flex;
  justify-content: space-around;
  color: #7b8294;
  font-size: 10px;
  text-align: center;
}

.photo-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 6px;
}

.photo-row i {
  height: 79px;
  border-radius: 7px;
  background: linear-gradient(135deg, #b9d9f1, #ffd8dc);
}

.photo-row i:nth-child(2) {
  background: linear-gradient(135deg, #9ad0f4, #805c43);
}

.photo-row i:nth-child(3) {
  background: linear-gradient(135deg, #f6a6a3, #f4d9aa);
}

.photo-row i:nth-child(4) {
  background: linear-gradient(135deg, #8bbd89, #ebd3b4);
}

.album-card p {
  margin: 11px 0 0;
  color: #7b8294;
  font-size: 10px;
}

.notes-card time {
  color: #8b91a1;
}

.add-note {
  margin-top: 7px;
  background: transparent;
  color: #765ce8;
  font-size: 11px;
}

.ai-travel {
  position: relative;
}

.ai-travel ul {
  margin: 0;
  padding-left: 16px;
  color: #596174;
  font-size: 10px;
  line-height: 2;
}

.ai-travel .lm-robot {
  position: absolute;
  right: 12px;
  bottom: -7px;
  transform: scale(0.72);
}

.travel-activity {
  margin-top: 14px;
}

.travel-activity > div:last-child {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.travel-activity p {
  display: grid;
  grid-template-columns: 25px minmax(0, 1fr);
  gap: 8px;
  margin: 0;
  color: #495164;
  font-size: 11px;
}

.travel-activity .svg-icon {
  color: #765ce8;
  font-size: 22px;
}

@media (max-width: 1180px) {
  .travel-grid,
  .travel-activity > div:last-child {
    grid-template-columns: repeat(2, minmax(0, 1fr));
  }
}
</style>

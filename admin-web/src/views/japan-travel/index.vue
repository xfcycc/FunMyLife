<script setup lang="ts">
defineOptions({
  name: 'JapanTravel'
});

const tabs = ['概览', '行程', '清单', '资产', '记录', '图册', 'AI'];

const itinerary = [
  { date: '6月15日', title: '抵达东京，入住新宿酒店', tag: '交通' },
  { date: '6月16日', title: '浅草寺、上野、秋叶原', tag: '行程' },
  { date: '6月17日', title: '镰仓一日游', tag: '行程' },
  { date: '6月18日', title: '前往京都，确认新干线', tag: '交通' }
];

const checklist = [
  { title: '护照与签证资料', done: true },
  { title: '机票酒店确认单', done: true },
  { title: '交通卡与现金准备', done: false },
  { title: '行李与药品清单', done: false }
];

const assets = ['机票订单', '酒店确认函', '旅行保险', '签证资料'];
</script>

<template>
  <div class="life-page">
    <div class="life-shell">
      <section class="life-cover-banner travel">
        <div class="life-cover-meta">
          <span class="life-tag blue">旅行项目</span>
          <span class="life-tag amber">出发前</span>
          <RouterLink to="/japan-travel-manage">
            <NButton size="small" round secondary>项目管理</NButton>
          </RouterLink>
        </div>
        <h1>日本旅行 2026</h1>
        <p>把行程、清单、机票酒店、证件保险、旅行图册、日记和 AI 建议都收在这一次旅行里。</p>
      </section>

      <div class="life-tabs">
        <span v-for="item in tabs" :key="item" class="life-tab" :class="{ active: item === '概览' }">{{ item }}</span>
      </div>

      <section class="life-grid">
        <div class="life-card life-card-soft life-span-3">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon blue"><SvgIcon icon="material-symbols:flight-takeoff-rounded" /></span>
              出发倒计时
            </h2>
          </div>
          <div class="text-center">
            <div class="text-56px font-800 color-#7568ff leading-none">48</div>
            <div class="mt-8px color-#72788c">天后出发</div>
          </div>
          <div class="mt-18px life-progress"><span style="width: 70%" /></div>
        </div>

        <div class="life-card life-span-5">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:route-outline-rounded" /></span>
              近期行程
            </h2>
            <span class="life-link">查看全部</span>
          </div>
          <div class="life-timeline">
            <div v-for="item in itinerary" :key="item.title" class="life-timeline-item">
              <span class="life-dot" />
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
                <div class="life-list-meta">{{ item.date }}</div>
              </div>
              <span class="life-tag blue">{{ item.tag }}</span>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon amber"><SvgIcon icon="material-symbols:checklist-rounded" /></span>
              行前清单
            </h2>
            <span class="life-link">12/20</span>
          </div>
          <div class="mb-16px life-progress"><span style="width: 60%" /></div>
          <div class="life-list">
            <div v-for="item in checklist" :key="item.title" class="life-list-item">
              <span class="life-check" :class="{ done: item.done }">
                <SvgIcon v-if="item.done" icon="material-symbols:check-rounded" class="text-14px" />
              </span>
              <div class="life-list-main">
                <div class="life-list-title">{{ item.title }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon rose"><SvgIcon icon="material-symbols:folder-special-outline-rounded" /></span>
              资产资料
            </h2>
          </div>
          <div class="life-mini-grid">
            <div v-for="item in assets" :key="item" class="life-mini-cell">
              <span>已归档</span>
              <strong>{{ item }}</strong>
            </div>
          </div>
        </div>

        <div class="life-card life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon teal"><SvgIcon icon="material-symbols:photo-library-outline-rounded" /></span>
              图册预览
            </h2>
          </div>
          <div class="life-mini-grid">
            <div class="life-mini-cell"><span>攻略截图</span><strong>26 张</strong></div>
            <div class="life-mini-cell"><span>酒店资料</span><strong>8 张</strong></div>
            <div class="life-mini-cell"><span>旅行照片</span><strong>待上传</strong></div>
            <div class="life-mini-cell"><span>OSS 同步</span><strong>已开启</strong></div>
          </div>
        </div>

        <div class="life-card life-card-soft life-span-4">
          <div class="life-card-head">
            <h2 class="life-card-title">
              <span class="life-icon"><SvgIcon icon="material-symbols:robot-2-outline-rounded" /></span>
              AI 行前建议
            </h2>
          </div>
          <div class="life-list">
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">建议本周确认交通卡和现金准备，避免临近出发集中处理。</div>
            </div>
            <div class="life-list-item">
              <span class="life-dot" />
              <div class="life-list-main">京都段酒店确认函已归档，可生成离线资料包。</div>
            </div>
          </div>
          <NButton class="mt-18px" type="primary" secondary round>生成行前清单</NButton>
        </div>
      </section>
    </div>
  </div>
</template>

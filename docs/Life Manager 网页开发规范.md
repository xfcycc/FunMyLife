# Life Manager 网页开发规范

本文档用于约束 Life Manager 后续页面开发，重点记录当前“无限暖暖”概览页与管理页已经沉淀出的可复用组件、布局节奏和视觉规范。

适用范围：

- `admin-web/src/views/infinity-nikki/index.vue`
- `admin-web/src/views/infinity-nikki-manage/index.vue`
- 后续 Life Manager 项目详情页、项目配置页、项目类工作台页面

## 1. 基本原则

1. 页面整体布局优先统一，不只复用局部小组件。
2. 能复用 Gemini 组件时，不在页面里手写一份相似样式。
3. 新页面如果有更合适的结构，应抽成共享组件，再替换旧页面。
4. 页面本地样式只处理业务内容布局，不覆盖共享组件的圆角、阴影、边距和按钮基础样式。
5. 左侧导航、页头、右上角操作区、tabs、卡片外壳属于页面 chrome，应保持统一。

## 2. 页面外壳

统一使用：

```vue
<LifeGeminiShell
  title="无限暖暖"
  description="项目概览、日常进度、活动倒计时和最近动态。"
  :breadcrumbs="[
    { label: '首页', routeKey: 'home' },
    { label: '项目', routeKey: 'projects' },
    { label: '无限暖暖' }
  ]"
>
  页面内容
</LifeGeminiShell>
```

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiShell.vue`

职责：

- 承接 Gemini 左侧导航。
- 承接统一页头。
- 提供主内容滚动容器。
- 控制页面全屏布局、背景色和基础字体。

不要做：

- 不要在页面内部再写一套左侧 sidebar。
- 不要在页面 body 里重复写独立顶栏。
- 不要把 `LifeAppShell` 和 `LifeGeminiShell` 混用在同一类 Gemini 页面里。

## 3. 左侧导航

统一使用：

- `LifeGeminiSidebar`
- 通过 `LifeGeminiShell` 自动接入

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiSidebar.vue`

当前规范：

- 宽度：`w-64`
- 背景：`bg-slate-50/80 backdrop-blur-xl`
- 分组之间使用浅色分割线。
- 当前激活项使用 indigo 背景和白色文字。
- 未接入真实路由的菜单项保持 disabled 观感，不做假跳转。
- 已接入路由的菜单项需要使用 `routeKey`，由组件内部处理跳转和 active 状态。

当前已接真实路由：

- 首页：`home`
- 项目：`projects`

后续新增菜单时：

```ts
{ icon: SomeIcon, label: '菜单名', routeKey: 'route-key' }
```

如果页面还没有实现，不要传 `routeKey`。

## 4. 页头与面包屑

统一使用：

- `LifeGeminiPageHeader`
- 通过 `LifeGeminiShell` 自动接入

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiPageHeader.vue`

职责：

- 面包屑导航。
- 页面标题。
- 页面描述。
- 右上角 actions 插槽。

面包屑规范：

- 首页和项目层级应使用真实 `routeKey`。
- 当前页最后一级不传 `routeKey`。
- 项目配置页示例：

```ts
[
  { label: '首页', routeKey: 'home' },
  { label: '项目', routeKey: 'projects' },
  { label: '无限暖暖', routeKey: 'infinity-nikki' },
  { label: '管理' }
]
```

页头标题规范：

- 详情页标题用项目名，例如“无限暖暖”。
- 配置页标题用页面任务，例如“项目配置”。
- 描述文案说明当前页面能完成什么，不写过长说明。

## 5. 右上角操作区

统一使用：

- `LifeGeminiTopActions`

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiTopActions.vue`

默认能力：

- 搜索按钮
- 提醒按钮
- 新建按钮

可选能力：

- `backText`：显示返回按钮，例如“返回详情”
- `createText`：替换新建按钮文案
- `showNotificationDot`：是否显示提醒红点
- `@search`
- `@notification`
- `@back`
- `@create`

管理页示例：

```vue
<template #actions>
  <LifeGeminiTopActions
    back-text="返回详情"
    search-label="搜索配置"
    notification-label="查看提醒"
    @search="info('搜索配置', '可继续接入配置项搜索')"
    @notification="switchTab('提醒规则')"
    @back="backToProject"
    @create="openDemoModal('新建配置')"
  />
</template>
```

不要做：

- 不要在页面里手写一套 `header-icon-action`、`header-primary-action` 之类的仿写样式。
- 不要为了一个页面复制概览页的按钮 class。需要差异时扩展 `LifeGeminiTopActions` 的 props 和事件。

## 6. Tabs 菜单

统一使用：

- `LifeGeminiTabs`

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiTabs.vue`

当前视觉规范：

- 文字较重：`text-lg font-extrabold`
- 横向间距较宽：`gap-14`
- 底部分割线：`border-b border-slate-200`
- 当前项：`text-indigo-600`
- 当前指示线：`h-1 bg-indigo-600 rounded-t-full`
- 上下节奏：使用按钮 `pb-3` 和组件 `mb-6`，不要再在页面外层叠加额外 gap。

使用示例：

```vue
<LifeGeminiTabs v-model="activeTab" :tabs="tabs" />
```

页面外层注意：

- 不要给包裹 `ProjectHero + Tabs + Grid` 的父容器加统一 `gap`。
- `LifeGeminiProjectHero` 自带 `mb-6`。
- `LifeGeminiTabs` 自带 `mb-6`。
- 如果父容器再加 `gap: 20px`，菜单上下距离会明显偏大。

推荐结构：

```vue
<div class="page-content">
  <LifeGeminiProjectHero />
  <LifeGeminiTabs />
  <section class="top-grid">...</section>
  <section class="module-grid">...</section>
</div>
```

其中 `page-content` 不要设置 `display: grid; gap: ...`。

## 7. 项目 Hero

统一使用：

- `LifeGeminiProjectHero`

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiProjectHero.vue`

适用场景：

- 项目概览页头部。
- 项目配置页头部。
- 任何项目详情类页面的上方项目信息区。

规范：

- 组件本身已经包含白底卡片、圆角、封面图、标签、统计信息和底部统计区。
- 页面只传数据和 actions，不重写 hero 外壳。
- 管理页可以复用同一 hero，但 stats 可展示配置相关数据，例如任务模板、提醒规则、关联资产、创建时间、开始时间、类型。

示例：

```vue
<LifeGeminiProjectHero
  title="无限暖暖"
  description="记录游戏日常、活动、版本与养成进度，管理账号与相关资源。"
  :cover-src="coverUrl"
  cover-alt="无限暖暖项目封面"
  :tags="projectTags"
  :stats="projectHeroStats"
>
  <template #actions>
    ...
  </template>
</LifeGeminiProjectHero>
```

## 8. 卡片

统一使用：

- `LifeGeminiCard`

组件位置：

- `admin-web/src/components/life-manager/LifeGeminiCard.vue`

当前视觉规范：

- 背景：白色
- 圆角：`rounded-3xl`
- 内边距：`p-5`
- 阴影：`shadow-[0_2px_10px_-4px_rgba(0,0,0,0.05)]`
- 边框：`border border-slate-100`
- 标题区：左标题，右 action 插槽或 `actionText`

使用示例：

```vue
<LifeGeminiCard title="基本信息">
  ...
</LifeGeminiCard>
```

不要做：

- 不要在页面 `.config-card` 里覆盖 `border-radius`、`box-shadow`、`border`、`background`。
- 不要把卡片圆角改回 8px。
- 页面局部 class 只能控制内容布局，例如表格、列表、按钮组、滚动区域。

可接受的本地样式：

```css
.config-card {
  min-width: 0;
}
```

不推荐：

```css
.config-card {
  border-radius: 8px;
  box-shadow: ...;
  border: ...;
}
```

## 9. 页面间距

当前推荐节奏：

- `LifeGeminiShell` 内容区：`px-8 pb-8`
- `LifeGeminiProjectHero` 与后续内容：组件自带 `mb-6`
- `LifeGeminiTabs` 与卡片网格：组件自带 `mb-6`
- 同一网格内部卡片：`gap-5` 或 `gap-6`，优先沿用当前页面
- 不同模块区块之间：可使用 `margin-top: 20px`

重要规则：

- 不要同时使用“组件自带 margin”和“父容器统一 gap”控制同一段距离。
- 如果截图里发现 tabs 上下距离不一致，先检查父容器是否额外设置了 `display: grid; gap: ...`。
- 页面第一屏区域应尽量由组件自带节奏控制，避免每页手写不同 top/bottom spacing。

## 10. 管理页布局

管理页推荐结构：

```vue
<LifeGeminiShell ...>
  <template #actions>
    <LifeGeminiTopActions ... />
  </template>

  <div class="page-content">
    <LifeGeminiProjectHero />
    <LifeGeminiTabs />

    <section class="top-grid">
      <LifeGeminiCard title="基本信息" />
      <LifeGeminiCard title="游戏重置设置" />
      <LifeGeminiCard title="提醒规则" />
    </section>

    <section class="module-grid">
      <LifeGeminiCard title="日常任务模板" />
      <LifeGeminiCard title="关联资产" />
      <LifeGeminiCard title="图册与 OSS 同步" />
      <LifeGeminiCard title="AI 自动化设置" />
    </section>
  </div>
</LifeGeminiShell>
```

管理页可以有自己的业务内容样式，但 chrome 必须复用：

- 左侧导航：`LifeGeminiShell`
- 页头/面包屑：`LifeGeminiShell` + `LifeGeminiPageHeader`
- 右上角操作区：`LifeGeminiTopActions`
- 项目头：`LifeGeminiProjectHero`
- Tabs：`LifeGeminiTabs`
- 卡片：`LifeGeminiCard`

## 11. 溢出与内容安全

近期已出现的问题：

- AI 建议卡片里的“查看完整分析报告”和机器人使用绝对定位，导致超出卡片边界。

规范：

- 卡片内装饰元素优先使用正常布局，不使用负值 absolute 定位。
- 如果确实需要装饰元素贴边，父容器必须有明确尺寸和 `overflow-hidden`。
- 文字按钮需要 `min-width: 0`，避免挤压时溢出。
- 图标或插画需要 `shrink-0`，避免被压扁。

推荐：

```vue
<div class="h-full min-h-48 flex flex-col justify-between overflow-hidden">
  <ul>...</ul>
  <div class="mt-4 flex items-end justify-between gap-4">
    <button class="min-w-0">查看完整分析报告</button>
    <div class="w-14 h-14 shrink-0">...</div>
  </div>
</div>
```

避免：

```vue
<div class="absolute right-0 bottom-[-10px]">...</div>
```

## 12. 类型与数据约定

共享类型位置：

- `admin-web/src/components/life-manager/types.ts`

当前可复用类型：

- `LifeGeminiMenuItem`
- `LifeGeminiBreadcrumbItem`
- `LifeGeminiProjectTag`
- `LifeGeminiProjectStat`
- `LifeToastItem`

新增共享组件 props 时：

- 优先在 `types.ts` 里补充跨组件共用类型。
- 页面私有数据结构可以留在页面内部。
- 如果两个以上页面复用同一种数据结构，再抽到共享类型。

## 13. 后续开发检查清单

开发新页面前确认：

- 是否能使用 `LifeGeminiShell`？
- 是否需要面包屑？如果需要，是否传入 `breadcrumbs`？
- 左侧导航是否已有真实路由？没有真实页面就不要传 `routeKey`。
- 右上角 actions 是否能使用 `LifeGeminiTopActions`？
- 项目头是否能使用 `LifeGeminiProjectHero`？
- tabs 是否使用 `LifeGeminiTabs`？
- 卡片是否使用 `LifeGeminiCard`？
- 页面父容器是否错误叠加了 `gap`？
- 卡片内是否有负值 absolute 定位导致溢出？

提交前验证：

```bash
pnpm -C admin-web typecheck
```

视觉自查：

- 概览页和管理页左侧导航一致。
- 页头面包屑、标题和右上角操作区一致。
- tabs 上下距离一致。
- 卡片圆角和阴影一致。
- 内容没有超出卡片边界。
- 移动端或窄屏下文字不互相遮挡。

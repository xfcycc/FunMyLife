# Life Manager 无限暖暖数据模型与页面闭环设计

## 1. 文档定位

本文是“无限暖暖项目最小闭环”的可开发规格。

它不继续扩展 Life Manager 的全局概念，也不讨论所有游戏类型。它只回答一个问题：如果先把无限暖暖这个长期游戏项目做扎实，版本、活动、目标、概览摘要、项目配置和时间轴应该如何串起来。

本文衔接当前已有实现：

| 当前内容 | 位置 | 本文处理方式 |
| --- | --- | --- |
| 无限暖暖详情页类型 | `admin-web/src/views/infinity-nikki/types.ts` | 保留已有结构，作为第一版 mock 基础 |
| 日常任务 mock | `admin-web/src/views/infinity-nikki/mock/daily.ts` | 升级为目标系统的一个视角 |
| 活动倒计时 mock | `admin-web/src/views/infinity-nikki/mock/countdown.ts` | 升级为活动与版本对象 |
| 最近动态 mock | `admin-web/src/views/infinity-nikki/mock/activities.ts` | 升级为时间轴事件 |
| 接口文档 | `docs/infinity-nikki-api.md` | 后续可按本文逐步补充新接口 |

本文先服务前端 mock 和页面演示，不要求第一步就设计数据库表。

## 2. 最小闭环

无限暖暖的第一阶段不要再泛化到所有页面，而是先跑通下面这条链路：

```text
当前版本
  -> 版本活动
      -> 活动目标
      -> 活动倒计时
      -> 活动素材 / 截图 / 笔记
  -> 概览摘要
  -> 用户完成 / 跳过 / 过期
  -> 活动或版本归档
  -> 时间轴回顾
```

这条链路有两个价值。

第一，它能验证“活动任务不是普通任务列表里的一行”。活动目标属于目标系统，但上层归属是活动，倒计时来自活动结束时间，归档跟随活动或版本。

第二，它能验证“长期项目不是只看今天”。用户每天做了什么、每周漏了什么、每个版本留下了什么，都应该进入时间轴和回顾。

## 3. 核心对象

第一版建议先定义 7 个核心对象。

| 对象 | 作用 | 当前已有结构对应 |
| --- | --- | --- |
| `NikkiProject` | 项目基础信息 | 已存在 |
| `GameVersion` | 当前版本和历史版本 | 当前缺失，部分散落在版本节点 |
| `GameActivity` | 限时活动容器 | 当前 `EventCountdown` 的上层对象 |
| `GameTarget` | 日常、周常、活动、自定义目标 | 当前 `DailyTask` / `WeeklyGoal` |
| `OverviewSummaryRule` | 概览摘要生成规则 | 当前缺失，概览写死 |
| `AbilityInstanceConfig` | 功能块实例配置 | 当前管理页配置的目标形态 |
| `TimelineEvent` | 时间轴事件 | 当前 `NikkiActivity` 的升级版 |

这里的重点不是命名，而是关系。

```text
GameVersion
  -> GameActivity
      -> GameTarget
      -> TimelineEvent

NikkiProject
  -> AbilityInstanceConfig
      -> OverviewSummaryRule
      -> TimelineEvent 写入规则
```

## 4. 数据模型草案

下面是前端 mock 可以先使用的 TypeScript 形态。它不等同于后端最终表结构。

### 4.1 版本

```ts
type GameVersionStatus = 'upcoming' | 'active' | 'ending' | 'ended' | 'archived';

interface GameVersion {
  id: string;
  projectId: string;
  name: string;
  title: string;
  startAt: string;
  endAt: string;
  status: GameVersionStatus;
  highlights: string[];
  activityIds: string[];
  summary?: string;
  archivedAt?: string;
}
```

版本承载一段游戏内容周期。当前版本应该进入概览和进行中页；历史版本默认只在时间轴或归档视图里出现。

### 4.2 活动

```ts
type GameActivityStatus = 'upcoming' | 'active' | 'ending' | 'ended' | 'archived';

interface GameActivity {
  id: string;
  projectId: string;
  versionId: string;
  title: string;
  description?: string;
  startAt: string;
  endAt: string;
  status: GameActivityStatus;
  priority: 'low' | 'normal' | 'high';
  cover?: string;
  targetIds: string[];
  materialIds?: string[];
  photoIds?: string[];
  noteIds?: string[];
  reminderRule?: ReminderRule;
  archivedAt?: string;
}
```

活动不是倒计时卡片。活动是容器，倒计时只是它的一个展示结果。活动下可以挂目标、素材、截图和笔记。

### 4.3 目标

```ts
type GameTargetType = 'daily' | 'weekly' | 'activity' | 'custom';
type GameTargetStatus = 'todo' | 'done' | 'skipped' | 'expired' | 'archived';

interface GameTarget {
  id: string;
  projectId: string;
  type: GameTargetType;
  title: string;
  description?: string;
  status: GameTargetStatus;
  progressCurrent?: number;
  progressTarget?: number;
  resetRule?: ResetRule;
  versionId?: string;
  activityId?: string;
  dueAt?: string;
  priority: 'low' | 'normal' | 'high';
  pinnedToOverview?: boolean;
  timelineRule: TimelineWriteRule;
  archivedAt?: string;
}
```

日常、周常、活动目标、自定义目标都属于目标系统，但它们的上层归属和摘要规则不同。

活动目标必须允许关联 `activityId`。如果关联了活动，默认继承活动结束时间作为倒计时来源；如果用户单独设置 `dueAt`，则以目标自己的截止时间作为更细粒度提醒。

### 4.4 摘要规则

```ts
interface OverviewSummaryRule {
  id: string;
  projectId: string;
  source: 'targets' | 'activities' | 'version' | 'materials' | 'gallery' | 'timeline' | 'ai';
  enabled: boolean;
  title: string;
  maxItems: number;
  priority: number;
  filters: {
    targetTypes?: GameTargetType[];
    activityStatuses?: GameActivityStatus[];
    onlyPinned?: boolean;
    onlyHighPriority?: boolean;
    withinHours?: number;
  };
  displayMode: 'metric' | 'list' | 'compact' | 'timeline';
}
```

概览不直接读取全部任务或全部活动。概览读取摘要规则，再由规则生成摘要。

例如任务摘要可以有三条规则：

```text
今日目标：daily，显示未完成数量，最多展开 3 条
本周目标：weekly，显示完成进度，不展开列表
活动目标：activity，只显示即将结束活动下的未完成目标
```

### 4.5 功能块实例配置

```ts
interface AbilityInstanceConfig {
  id: string;
  projectId: string;
  blockKey: 'overview' | 'targets' | 'version_activity' | 'materials' | 'gallery' | 'assets' | 'timeline' | 'ai';
  displayName: string;
  enabled: boolean;
  navigation: {
    visible: boolean;
    order: number;
  };
  summaryRules: OverviewSummaryRule[];
  fields?: FieldConfig[];
  behavior?: {
    resetRules?: ResetRule[];
    reminderRules?: ReminderRule[];
    archiveRule?: ArchiveRule;
  };
  timeline?: {
    enabled: boolean;
    defaultWriteRule: TimelineWriteRule;
  };
}
```

配置页不应该只有“启用/停用”。每个功能块实例都应该能配置显示名、导航顺序、摘要规则、提醒规则、归档规则和时间轴写入规则。

### 4.6 时间轴事件

```ts
type TimelineEventType =
  | 'target_done'
  | 'target_skipped'
  | 'target_expired'
  | 'activity_started'
  | 'activity_ending'
  | 'activity_archived'
  | 'version_archived'
  | 'material_completed'
  | 'photo_uploaded'
  | 'note_created'
  | 'ai_summary_generated';

interface TimelineEvent {
  id: string;
  projectId: string;
  occurredAt: string;
  type: TimelineEventType;
  title: string;
  description?: string;
  sourceBlockKey: AbilityInstanceConfig['blockKey'];
  versionId?: string;
  activityId?: string;
  targetId?: string;
  sensitivity: 'normal' | 'private';
  displayInOverview: boolean;
  aiReadable: boolean;
}
```

### 4.7 辅助类型

```ts
interface ResetRule {
  type: 'none' | 'daily' | 'weekly' | 'activity' | 'custom';
  time?: string;
  weekday?: number;
  inheritFromActivity?: boolean;
}

interface ReminderRule {
  enabled: boolean;
  channels: ('app' | 'feishu' | 'webhook')[];
  beforeMinutes: number[];
  quietHours?: {
    start: string;
    end: string;
  };
}

interface ArchiveRule {
  type: 'manual' | 'on_activity_end' | 'on_version_end' | 'after_days';
  afterDays?: number;
  includeTargets: boolean;
  includePhotos: boolean;
  includeNotes: boolean;
  generateTimelineSummary: boolean;
}

interface TimelineWriteRule {
  mode: 'none' | 'detail' | 'daily_summary' | 'weekly_summary' | 'exception_only';
  displayInOverview: boolean;
  aiReadable: boolean;
}

interface FieldConfig {
  key: string;
  label: string;
  type: 'text' | 'number' | 'date' | 'select' | 'boolean' | 'progress';
  required?: boolean;
  options?: string[];
}

interface OverviewSummary {
  id: string;
  ruleId: string;
  title: string;
  value?: string;
  description?: string;
  items?: Array<{
    id: string;
    label: string;
    status?: string;
    targetRoute?: string;
  }>;
  targetRoute?: string;
}
```

当前 `NikkiActivity` 可以迁移为 `TimelineEvent`。旧的 `type: 'task_complete'` 可以映射为 `target_done`，`screenshot_uploaded` 可以映射为 `photo_uploaded`，`version_announcement` 可以映射为 `activity_started` 或版本相关事件。

## 5. 页面闭环

### 5.1 概览页

概览页只展示摘要，不展示全部数据。

概览应读取这些摘要：

```text
当前版本摘要
今日目标摘要
本周目标摘要
即将结束活动摘要
素材目标摘要
最近时间轴摘要
AI 建议
```

概览里的每个卡片都需要能追溯到来源：

| 概览内容 | 来源对象 | 点击后进入 |
| --- | --- | --- |
| 当前版本剩余时间 | `GameVersion` | 活动与版本 |
| 今日 5/8 | `GameTarget` + 摘要规则 | 任务页今日视角 |
| 本周 3/6 | `GameTarget` + 摘要规则 | 任务页本周视角 |
| 1 个活动 48 小时内结束 | `GameActivity` | 活动详情 |
| 最近完成 5 项 | `TimelineEvent` | 时间轴 |

概览不直接提供复杂编辑。高频动作可以保留“完成目标”“查看活动”“生成总结”，但完整管理必须进入对应页面。

### 5.2 进行中页

进行中页是比概览更细的当前状态页。

它只看当前版本和当前活动，不看历史归档。它可以展示当前版本、进行中活动、置顶目标、临近结束活动、最近截图和本周重点。

如果第一阶段页面数量要少，进行中页可以暂时不单独做；但数据模型里应保留它的能力，因为它能避免概览页变成大杂烩。

### 5.3 任务页

任务页是目标系统的完整维护入口。

任务页建议提供这些视角：

```text
今日
本周
活动
自定义
历史
```

每个视角本质上是对 `GameTarget` 的过滤。

| 视角 | 过滤条件 | 主要操作 |
| --- | --- | --- |
| 今日 | `type = daily` | 完成、跳过、恢复、编辑 |
| 本周 | `type = weekly` | 更新进度、完成、标记需关注 |
| 活动 | `type = activity` | 按活动分组、完成、过期、归档 |
| 自定义 | `type = custom` | 新增、置顶、调整规则 |
| 历史 | `status = archived / expired` | 查看、恢复、进入时间轴 |

活动目标不应该只平铺在任务列表里。任务页可以展示它，但要按活动分组，并提供进入活动详情的入口。

### 5.4 活动与版本页

活动与版本页负责管理版本和活动。

它需要展示：

```text
当前版本
版本剩余时间
进行中活动
即将结束活动
已结束待归档活动
历史版本入口
```

活动详情需要展示：

```text
活动基础信息
倒计时
关联目标
关联素材
关联截图
关联笔记
归档状态
```

活动结束后不应直接消失。它进入“已结束待归档”状态。用户确认归档后，系统把活动目标完成情况、截图、笔记、素材进展写入时间轴。

### 5.5 配置页

配置页分成项目基础配置和功能块实例配置。

项目基础配置管理：

```text
项目名称
封面
状态
当前版本
默认提醒渠道
敏感信息策略
AI 是否启用
```

功能块实例配置管理：

```text
显示名称
导航顺序
概览摘要规则
字段配置
重置规则
提醒规则
归档规则
时间轴写入规则
AI 可读规则
```

以目标系统为例，配置页必须能回答：

```text
日常目标几点重置
周常目标哪天重置
活动目标是否必须绑定活动
活动目标倒计时来自哪里
概览是否展示今日、本周、活动目标
完成目标是否写入时间轴
版本归档时目标如何处理
```

### 5.6 时间轴页

时间轴页负责长期回顾。

它至少需要三个视角：

```text
按天
按周
按版本
```

按天看今天完成了什么，按周看本周持续性，按版本看一段游戏内容周期留下了什么。

时间轴事件不能全部写入。第一阶段建议默认规则如下：

| 来源 | 默认写入规则 |
| --- | --- |
| 日常目标完成 | 每日汇总 |
| 周常目标完成 | 周汇总 |
| 周常遗漏 | 逐条事件 |
| 活动目标完成 | 逐条事件 |
| 活动过期 | 逐条事件 |
| 活动归档 | 逐条事件 |
| 版本归档 | 逐条事件 |
| 截图上传 | 每日汇总 |
| 笔记新增 | 逐条事件 |
| AI 总结 | 用户确认后写入 |

## 6. 状态流转

### 6.1 版本状态

```text
未开始 -> 进行中 -> 即将结束 -> 已结束 -> 已归档
```

`即将结束` 可以由规则计算，例如结束前 3 天。`已归档` 必须是明确动作或自动归档规则产生。

### 6.2 活动状态

```text
未开始 -> 进行中 -> 即将结束 -> 已结束待归档 -> 已归档
```

活动结束后保留在活动与版本页，直到归档。归档后进入历史版本或时间轴。

### 6.3 目标状态

```text
待完成 -> 已完成
待完成 -> 已跳过
待完成 -> 已过期
已完成 / 已跳过 / 已过期 -> 已归档
```

日常目标通常按天汇总后重置。周常目标按周汇总后重置。活动目标跟随活动归档。

## 7. 当前 mock 迁移建议

当前页面已有数据不需要推倒重来，可以逐步迁移。

| 当前结构 | 迁移目标 | 说明 |
| --- | --- | --- |
| `DailyTask` | `GameTarget` | `group` 映射为 `type` |
| `WeeklyGoal` | `GameTarget` | `current/max/progress` 映射为目标进度 |
| `EventCountdown` | `GameActivity` | 结束时间和提醒规则进入活动 |
| `VersionNode` | `GameVersion` 或版本事件 | 直播、维护、更新可作为版本节点 |
| `NikkiActivity` | `TimelineEvent` | 最近动态升级为可筛选时间轴 |
| `NikkiPhoto` | 媒体记录 | 可增加 `activityId`、`versionId` |
| `NikkiNote` | 笔记攻略 | 可增加活动或版本归属 |

第一步可以不改接口，只在 mock 层增加新数据结构，并让概览页先读取新结构生成摘要。等页面关系稳定后，再调整 `docs/infinity-nikki-api.md`。

## 8. MVP 开发顺序

第一步，新增 mock 数据模型。

```text
mock/versions.ts
mock/gameActivities.ts
mock/gameTargets.ts
mock/timeline.ts
mock/abilityConfig.ts
```

第二步，新增摘要生成方法。

```text
根据 AbilityInstanceConfig.summaryRules
从 GameVersion / GameActivity / GameTarget / TimelineEvent
生成 OverviewSummary
```

第三步，改概览页。

概览页先不写死今日任务和活动倒计时，而是读取摘要。卡片点击进入任务页、活动与版本页、时间轴页。

第四步，新增或改造活动与版本页。

先支持查看当前版本、活动列表、活动详情、已结束待归档活动。不急着做复杂编辑器。

第五步，改任务页。

任务页按今日、本周、活动、自定义、历史拆视角。活动目标按活动分组。

第六步，补时间轴页。

先支持按天和按版本查看事件，能看到活动归档和目标汇总。

第七步，改配置页。

先支持目标系统、活动与版本、时间轴这三个功能块实例配置。

## 9. 验收标准

这个闭环完成后，用户应该能完成以下流程：

```text
查看当前版本
查看进行中活动
看到活动倒计时
查看活动下的目标
完成或跳过目标
在概览看到摘要变化
活动结束后进入待归档
归档活动或版本
在时间轴看到回顾记录
在配置页调整摘要和时间轴规则
```

页面层面的验收是：

概览页不再把全部任务堆在一个卡片里，而是展示由规则生成的摘要。

任务页能区分日常、周常、活动、自定义和历史，不把版本活动平铺成普通任务。

活动与版本页能承载活动倒计时、活动目标和活动归档。

时间轴页能按天、周、版本回顾长期记录。

配置页能进入功能块实例配置，而不是只有项目基础信息。

## 10. 暂缓范围

第一阶段暂缓这些内容：

```text
真实后端表结构
复杂权限和多用户协作
完整低代码配置器
自动识别官方活动
自动导入游戏截图目录
社区方案市场
多游戏通用 UI 一次性抽象
```

这些可以等无限暖暖最小闭环跑通后，再判断哪些能力值得抽象到游戏类型通用层。

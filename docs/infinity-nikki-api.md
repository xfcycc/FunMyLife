# 无限暖暖项目详情页接口文档

本文档描述 `/life/project/infinity-nikki` 与 `/life/project/infinity-nikki/manage` 当前页面所需的接口口径。当前前端仍使用 mock 数据，真实后端接入时应以这里的新模型为准，不再沿用旧的 `daily-tasks`、`countdown`、`notes tab`、`ai tab` 拆法。

Base URL：`/api/life/projects/{projectId}`

通用响应：

```ts
interface ApiResponse<T> {
  code: number;
  message: string;
  data: T;
}
```

## 1. 项目与功能块配置

项目详情页的 tab 来自功能块实例配置，而不是页面硬编码。

`GET /api/life/projects/{projectId}`

返回项目基础信息：名称、描述、封面、状态、标签、统计数据。

`GET /api/life/projects/{projectId}/ability-configs`

返回 `AbilityInstanceConfig[]`。核心字段包括：

| 字段 | 说明 |
| --- | --- |
| `blockKey` | 功能块类型，如 `overview`、`targets`、`version_activity`、`materials`、`gallery`、`assets`、`timeline`、`ai` |
| `displayName` | 项目内显示名称 |
| `enabled` | 是否启用 |
| `navigation.visible` | 是否显示为项目详情页 tab |
| `navigation.order` | tab 顺序 |
| `summaryRules` | 概览摘要规则 |
| `fields` | 当前功能块实例的字段配置 |
| `behavior` | 重置、提醒、归档等行为规则 |
| `timeline` | 是否写入时间轴及默认写入策略 |

`PATCH /api/life/projects/{projectId}/ability-configs/{configId}`

用于项目管理页修改功能块实例配置。第一阶段至少支持启停、导航显示、导航顺序、时间轴写入策略和概览摘要规则启停。

`PATCH /api/life/projects/{projectId}/ability-configs`

批量保存功能块实例配置。项目管理页通常一次调整多个功能块的启停、导航顺序、概览规则和时间轴规则，因此后端需要支持按项目整体保存，避免前端逐条提交后出现中间态。

## 2. 概览摘要

`GET /api/life/projects/{projectId}/overview-summaries`

返回 `OverviewSummary[]`，由 `overview` 功能块中的 `summaryRules` 生成。无限暖暖当前默认摘要包括当前版本、今日目标、本周目标、即将结束活动、素材收集、图册记录、资产风险和最近时间轴。

概览只负责摘要、跳转和 AI 建议，不直接承担完整数据管理。用户要新增或管理数据，应进入对应 tab。

## 3. 任务目标

`GET /api/life/projects/{projectId}/targets`

返回 `GameTarget[]`。任务目标覆盖日常、周常、版本活动目标和用户自定义目标。

关键字段：

| 字段 | 说明 |
| --- | --- |
| `type` | `daily`、`weekly`、`activity`、`custom` |
| `status` | `todo`、`done`、`skipped`、`expired`、`archived` |
| `progressCurrent/progressTarget` | 进度型目标 |
| `versionId/activityId` | 版本活动目标的归属 |
| `resetRule` | 重置规则 |
| `pinnedToOverview` | 是否进入概览候选 |
| `timelineRule` | 完成、跳过、过期等动作如何写入时间轴 |

`POST /api/life/projects/{projectId}/targets`

新增目标。活动目标应带 `activityId`，后端需同步维护活动的 `targetIds`。

`PATCH /api/life/projects/{projectId}/targets/{targetId}`

编辑目标基础信息，例如标题、说明、类型、进度目标、归属版本、归属活动、是否置顶到概览和时间轴写入规则。

`PATCH /api/life/projects/{projectId}/targets/{targetId}/status`

更新目标状态。完成、跳过、过期、归档动作需要按 `timelineRule` 生成时间轴事件。

`PATCH /api/life/projects/{projectId}/targets/{targetId}/progress`

更新目标进度。达到目标值时可自动变为 `done`。

`DELETE /api/life/projects/{projectId}/targets/{targetId}`

删除目标。第一阶段可直接删除 mock 数据；真实后端建议保留审计记录，并在删除活动目标时同步维护活动的 `targetIds`。

## 4. 版本与活动

`GET /api/life/projects/{projectId}/game-versions`

返回 `GameVersion[]`。版本状态为 `upcoming`、`active`、`ending`、`ended`、`archived`。

`GET /api/life/projects/{projectId}/game-activities`

返回 `GameActivity[]`。活动状态为 `upcoming`、`active`、`ending`、`ended`、`pending_archive`、`archived`。

`POST /api/life/projects/{projectId}/game-versions`

新增版本。

`POST /api/life/projects/{projectId}/game-activities`

新增版本活动。

`PATCH /api/life/projects/{projectId}/game-activities/{activityId}/reminder`

启停活动提醒。

`POST /api/life/projects/{projectId}/game-activities/{activityId}/archive`

归档活动，并生成 `activity_archived` 时间轴事件。归档摘要应包含关联目标、截图、笔记、素材。

`POST /api/life/projects/{projectId}/game-versions/{versionId}/archive`

归档版本，并生成 `version_archived` 时间轴事件。版本归档会同步归档版本下活动和目标。

## 5. 时间轴

`GET /api/life/projects/{projectId}/timeline-events`

返回 `TimelineEvent[]`。时间轴是长期项目回顾的主数据，不只是动态列表。

关键字段：

| 字段 | 说明 |
| --- | --- |
| `type` | `target_done`、`target_skipped`、`target_expired`、`activity_started`、`activity_archived`、`version_archived`、`material_completed`、`photo_uploaded`、`note_created`、`ai_summary_generated` |
| `sourceBlockKey` | 来源功能块 |
| `versionId/activityId/targetId` | 可选关联 |
| `displayInOverview` | 是否进入概览摘要 |
| `aiReadable` | 是否允许 AI 读取 |
| `sensitivity` | 普通或私密 |

`POST /api/life/projects/{projectId}/timeline-events`

新增时间轴记录。页面需支持按天、按周、按版本查看。

## 6. 素材、笔记、图册与资产

`GET /api/life/projects/{projectId}/materials`

返回素材/套装/代币/收集项概览。素材能力默认不一定显示为 tab，但会进入概览摘要、活动归档和 AI 复盘。

`GET /api/life/projects/{projectId}/notes`

返回 `NikkiNote[]`。笔记攻略第一阶段不作为独立 tab，但需要有数据模型和接口，可关联 `versionId`、`activityId`、`targetId`，并按配置写入时间轴。

`POST /api/life/projects/{projectId}/notes`

新增笔记、攻略或复盘。新增后可按 `timelineRule` 生成 `note_created` 时间轴事件。

`PATCH /api/life/projects/{projectId}/notes/{noteId}`

编辑笔记标题、内容、类型、归属版本、归属活动、归属目标和概览置顶规则。

`GET /api/life/projects/{projectId}/gallery`

返回图册和照片。照片应支持 `versionId`、`activityId`、`targetId` 关联，并可按图册配置写入时间轴。

`POST /api/life/projects/{projectId}/gallery/photos`

新增照片。

`PATCH /api/life/projects/{projectId}/gallery/photos/{photoId}`

编辑照片说明、所属图册、图片地址、关联版本、关联活动和关联目标。

`DELETE /api/life/projects/{projectId}/gallery/photos/{photoId}`

删除照片。真实后端需要区分删除业务记录和删除 OSS 文件，默认不应直接清理原始文件。

`GET /api/life/projects/{projectId}/assets`

返回账号资产。资产状态包括 `protected`、`bound`、`pending`、`expired`、`archived`。

`POST /api/life/projects/{projectId}/assets`

新增资产。

`PATCH /api/life/projects/{projectId}/assets/{assetId}`

编辑资产名称、类型、状态、摘要、关联链接和备注。敏感字段后续应独立加密和授权查看。

`PATCH /api/life/projects/{projectId}/assets/{assetId}/status`

更新资产状态。敏感资产内容后续需要增加查看确认和权限控制。

`DELETE /api/life/projects/{projectId}/assets/{assetId}`

删除资产。真实后端建议默认软删除，避免误删账号、兑换码、支付凭证等重要资料。

## 7. AI 建议

`GET /api/life/projects/{projectId}/ai/overview`

返回基于目标、活动、素材、时间轴生成的 AI 建议。AI 不作为独立 tab，而是作为概览建议、时间轴摘要和项目管理自动化配置出现。

`POST /api/life/projects/{projectId}/ai/summaries`

生成 AI 总结。用户确认后写入 `ai_summary_generated` 时间轴事件。

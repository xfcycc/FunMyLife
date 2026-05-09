# 无限暖暖项目接口文档

本文档描述 `/life/project/infinity-nikki` 与 `/life/project/infinity-nikki/manage` 当前接入 `fml-service` 的接口口径。

当前后端已经从 `admin-service` 拆出为独立服务 `fml-service`，默认端口为 `8082`。前端通过 `VITE_SERVICE_BASE_URL` 指向后端，例如本地联调时设置为 `http://localhost:8082`。

## 1. 接口约定

`fml-service` 当前约定：

- 所有 Life Manager 接口使用 `POST + JSON body`。
- `projectId` 统一放在请求体中，不再出现在 URL path 中。
- 查询接口也使用 POST，避免同一业务同时维护 GET/path 和 POST/body 两套入参。
- 响应使用统一 `R<T>` 包装。

通用请求体：

```ts
interface ProjectScopedRequest {
  projectId: number;
}
```

通用响应：

```ts
interface ApiResponse<T> {
  code: number;
  msg: string;
  data: T;
}
```

前端当前无限暖暖项目 ID 固定为 `1001`，对应请求体示例：

```json
{
  "projectId": 1001
}
```

## 2. 已实现接口

### 2.1 项目基础信息

`POST /life/project/detail`

请求体：

```json
{
  "projectId": 1001
}
```

返回项目基础信息，包含项目名称、描述、封面、状态、方案、标签和统计数据。

### 2.2 功能块实例

`POST /life/project/block-instances/list`

返回项目下全部功能块实例配置。项目详情页的 tab、显示名称、概览摘要、时间轴规则和 AI 规则都应来自功能块实例，而不是页面硬编码。

核心字段：

| 字段 | 说明 |
| --- | --- |
| `blockKey` | 功能块类型，如 `overview`、`targets`、`version_activity`、`materials`、`gallery`、`assets`、`timeline`、`ai` |
| `displayName` | 项目内显示名称 |
| `enabled` | 是否启用，当前返回 `"1"` / `"0"` |
| `capabilities` | 当前功能块组合的底层能力配置 |
| `navigation` | 是否进入项目导航、排序和展示规则 |
| `summaryRules` | 概览摘要规则 |
| `fields` | 字段扩展配置 |
| `behavior` | 重置、提醒、归档等行为规则 |
| `timeline` | 时间轴写入策略 |
| `aiRules` | AI 可读、摘要和建议规则 |
| `security` | 敏感信息展示和访问规则 |

`POST /life/project/block-instances/save-batch`

批量保存功能块实例配置。该接口使用 upsert 语义：同一项目下 `blockKey` 已存在则更新，不存在则插入。

请求体：

```ts
interface BlockInstanceBatchSaveRequest {
  projectId: number;
  blockInstances: BlockInstanceSaveItemRequest[];
}
```

### 2.3 能力列表

`POST /life/project/capabilities/list`

返回后端注册的 Life Capability 列表。该列表用于解释功能块背后的能力组合，例如目标系统、活动倒计时、素材收集、媒体记录、账号资产、时间轴回顾、AI 建议等。

### 2.4 概览摘要

`POST /life/project/overview-summaries/list`

返回项目概览摘要。摘要由功能块实例的 `summaryRules` 和后端能力模型共同生成，当前无限暖暖默认覆盖当前版本、今日目标、本周目标、即将结束活动、素材收集、图册记录、资产风险和最近时间轴。

概览只负责摘要、跳转和 AI 建议，不直接承担完整数据管理。用户要新增或管理数据，应进入对应 tab 或项目管理页。

### 2.5 任务目标

`POST /life/project/targets/list`

返回项目下所有任务目标，覆盖日常、周常、版本活动目标和用户自定义目标。

关键字段：

| 字段 | 说明 |
| --- | --- |
| `type` | `daily`、`weekly`、`activity`、`custom` |
| `status` | `todo`、`done`、`skipped`、`expired`、`archived` |
| `progressCurrent` / `progressTarget` | 进度型目标 |
| `versionId` / `activityId` | 版本活动目标的归属 |
| `resetRule` | 重置规则 |
| `pinnedToOverview` | 是否进入概览候选 |
| `timelineRule` | 完成、跳过、过期等动作如何写入时间轴 |

`POST /life/project/targets/status`

更新目标状态。

请求体：

```json
{
  "projectId": 1001,
  "targetId": 1,
  "status": "done"
}
```

`POST /life/project/targets/progress`

更新目标当前进度。

请求体：

```json
{
  "projectId": 1001,
  "targetId": 1,
  "current": 3
}
```

### 2.6 版本与活动

`POST /life/project/game-versions/list`

返回游戏版本列表。

`POST /life/project/game-versions/current`

返回当前版本。当前后端使用 `status = active` 判断当前版本。

`POST /life/project/game-activities/list`

返回版本活动列表。

### 2.7 时间轴

`POST /life/project/timeline-events/list`

返回项目时间轴事件，按发生时间倒序展示。时间轴是长期项目回顾的主数据，不只是动态列表。

关键字段：

| 字段 | 说明 |
| --- | --- |
| `type` | 事件类型，如目标完成、活动开始、素材完成、照片上传、AI 总结生成 |
| `sourceBlockKey` | 来源功能块 |
| `versionId` / `activityId` / `targetId` | 可选关联 |
| `displayInOverview` | 是否进入概览摘要 |
| `aiReadable` | 是否允许 AI 读取 |
| `sensitivity` | 普通或私密 |

### 2.8 素材、笔记、图册与资产

`POST /life/project/materials/overview`

返回素材、套装、代币和收集项概览。

`POST /life/project/notes/overview`

返回笔记概览。

`POST /life/project/notes/detail`

请求体：

```json
{
  "projectId": 1001,
  "noteId": 1
}
```

返回单条笔记详情，并校验笔记是否属于当前项目。

`POST /life/project/gallery/overview`

返回图册和最近照片。

`POST /life/project/assets/overview`

返回账号资产概览。敏感资产后续需要继续补查看确认、加密和权限控制。

`POST /life/project/assets/detail`

请求体：

```json
{
  "projectId": 1001,
  "assetId": 1
}
```

返回单条资产详情，并校验资产是否属于当前项目。

### 2.9 AI 建议

`POST /life/project/ai/overview`

返回基于目标、活动、素材、时间轴生成的 AI 建议摘要。当前 AI 仍是占位实现，但接口形态已固定。

`POST /life/project/ai/summaries/refresh`

刷新 AI 建议摘要。后续接入真实大模型后，该接口应只生成候选建议，不应无确认写入关键资料。

## 3. 后续待扩展

当前 `fml-service` 已覆盖无限暖暖页面的主要读取和少量写入能力，但以下接口仍属于后续扩展范围：

- 新增、编辑、删除任务目标。
- 新增版本和版本活动。
- 活动提醒启停和活动归档。
- 手动新增时间轴事件。
- 新增、编辑、删除素材、笔记、照片和资产。
- OSS 文件上传、图片归档和敏感资产加密。
- 真实 AI 总结、待确认收件箱和外部通知。

扩展这些接口时仍保持当前原则：`POST + JSON body`、`projectId` 放请求体、Controller 返回 VO，不直接暴露持久化 Entity。

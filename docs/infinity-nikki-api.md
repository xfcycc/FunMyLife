# 无限暖暖项目详情页 - 接口文档

## 概述

本文档定义无限暖暖项目详情页（`/life/project/infinity-nikki`）所需的全部 API 接口。当前所有接口使用 mock 数据，后续替换为真实后端 API。

**Base URL**: `/api/life/project/infinity-nikki`

**通用响应格式**:
```typescript
interface ApiResponse<T> {
  code: number;      // 200 成功
  message: string;   // 响应消息
  data: T;           // 响应数据
}
```

---

## 1. 项目基础信息

### 1.1 获取项目信息

**接口**: `GET /api/life/project/infinity-nikki/project`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 项目 ID |
| name | string | 项目名称 |
| description | string | 项目简介 |
| coverSrc | string | 封面图片 URL |
| coverAlt | string | 封面图片替代文本 |
| status | `'active' \| 'paused' \| 'archived'` | 项目状态 |
| createdAt | string | 创建时间 (ISO 8601) |
| tags | NikkiProjectTag[] | 项目标签 |
| stats | NikkiProjectStat[] | 项目统计数据 |

**NikkiProjectTag**:

| 字段 | 类型 | 说明 |
|------|------|------|
| label | string | 标签文本 |
| tone | `'default' \| 'success'` | 标签样式（可选） |

**NikkiProjectStat**:

| 字段 | 类型 | 说明 |
|------|------|------|
| label | string | 统计项名称 |
| value | `number \| string` | 统计值 |

**示例响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "id": "nikki-001",
    "name": "无限暖暖",
    "description": "收集美好的瞬间，搭配无限的可能",
    "coverSrc": "https://...",
    "coverAlt": "Infinity Nikki",
    "status": "active",
    "createdAt": "2024-12-15",
    "tags": [
      { "label": "游戏" },
      { "label": "进行中", "tone": "success" }
    ],
    "stats": [
      { "label": "创建时间", "value": "2024-12-15" },
      { "label": "今日任务进度", "value": "5/8" },
      { "label": "本周完成率", "value": "62%" },
      { "label": "项目笔记", "value": "12" },
      { "label": "图册数量", "value": "8" }
    ]
  }
}
```

---

## 2. 日常任务

### 2.1 获取日常任务概览

**接口**: `GET /api/life/project/infinity-nikki/daily-tasks`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| completed | number | 已完成任务数 |
| total | number | 总任务数 |
| taskGroups | TaskGroup[] | 任务分组列表 |
| weeklyGoals | WeeklyGoal[] | 周常目标列表 |

**TaskGroup**:

| 字段 | 类型 | 说明 |
|------|------|------|
| type | `'daily' \| 'weekly' \| 'event'` | 分组类型 |
| label | string | 分组名称 |
| tasks | DailyTask[] | 任务列表 |

**DailyTask**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 任务 ID |
| text | string | 任务文本 |
| done | boolean | 是否完成 |
| detail | string (可选) | 进度详情，如 "25/50" |
| group | `'daily' \| 'weekly' \| 'event'` | 所属分组 |

**WeeklyGoal**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 目标 ID |
| text | string | 目标文本 |
| current | number | 当前进度值 |
| max | number | 目标值 |
| progress | number | 进度百分比 (0-100) |
| status | `'on_track' \| 'needs_attention' \| 'completed'` | 状态 |
| color | string | 进度条颜色 class |

### 2.2 切换任务完成状态

**接口**: `POST /api/life/project/infinity-nikki/daily-tasks/toggle`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 任务 ID |
| done | boolean | 目标完成状态 |

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 任务 ID |
| done | boolean | 更新后的完成状态 |

### 2.3 更新周常目标进度

**接口**: `POST /api/life/project/infinity-nikki/weekly-goals/progress`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 目标 ID |
| current | number | 新的当前值 |

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 目标 ID |
| current | number | 更新后的当前值 |

### 2.4 切换周常目标状态

**接口**: `POST /api/life/project/infinity-nikki/weekly-goals/status`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 目标 ID |
| status | `'on_track' \| 'needs_attention' \| 'completed'` | 目标状态 |

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 目标 ID |
| status | string | 更新后的状态 |

---

## 3. 倒计时

### 3.1 获取倒计时概览

**接口**: `GET /api/life/project/infinity-nikki/countdown`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| events | EventCountdown[] | 活动倒计时列表 |
| versionNodes | VersionNode[] | 版本节点列表 |

**EventCountdown**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 活动 ID |
| title | string | 活动标题 |
| description | string (可选) | 活动描述 |
| endTime | string | 结束时间 |
| remainingDays | number | 剩余天数 |
| img | string | 活动图片 URL |
| reminded | boolean | 是否已设置提醒 |
| checkedIn | boolean | 是否已打卡 |

**VersionNode**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 节点 ID |
| type | `'live' \| 'maintenance' \| 'update'` | 节点类型 |
| title | string | 节点标题 |
| startTime | string | 开始时间 |
| endTime | string (可选) | 结束时间 |
| daysUntil | number | 距离开始天数 |
| reminded | boolean | 是否已设置提醒 |
| checkedIn | boolean | 是否已打卡 |

### 3.2 设置/取消活动提醒

**接口**: `POST /api/life/project/infinity-nikki/events/reminder`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 活动 ID |
| reminded | boolean | 目标提醒状态 |

### 3.3 活动打卡/撤回

**接口**: `POST /api/life/project/infinity-nikki/events/checkin`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 活动 ID |
| checkedIn | boolean | 目标打卡状态 |

### 3.4 设置/取消版本节点提醒

**接口**: `POST /api/life/project/infinity-nikki/version-nodes/reminder`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 节点 ID |
| reminded | boolean | 目标提醒状态 |

### 3.5 版本节点打卡/撤回

**接口**: `POST /api/life/project/infinity-nikki/version-nodes/checkin`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 节点 ID |
| checkedIn | boolean | 目标打卡状态 |

---

## 4. 资产

### 4.1 获取资产概览

**接口**: `GET /api/life/project/infinity-nikki/assets`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| total | number | 资产总数 |
| assets | NikkiAsset[] | 资产列表 |

**NikkiAsset**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 资产 ID |
| name | string | 资产名称 |
| type | `'official_account' \| 'sub_account' \| 'switch_account' \| 'payment' \| 'redeem_code'` | 资产类型 |
| status | `'protected' \| 'bound' \| 'pending' \| 'expired'` | 安全状态 |
| statusLabel | string | 状态显示文本 |
| description | string (可选) | 资产描述 |

### 4.2 获取资产详情

**接口**: `GET /api/life/project/infinity-nikki/assets/:id`

**路径参数**: `id` - 资产 ID

**响应**: 单个 `NikkiAsset` 对象

---

## 5. 笔记

### 5.1 获取笔记概览

**接口**: `GET /api/life/project/infinity-nikki/notes`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| total | number | 笔记总数 |
| notes | NikkiNote[] | 笔记列表 |

**NikkiNote**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 笔记 ID |
| title | string | 笔记标题 |
| category | `'strategy' \| 'explore' \| 'coordination' \| 'material' \| 'story'` | 分类 |
| categoryLabel | string | 分类显示文本 |
| content | string (可选) | 笔记内容摘要 |
| createdAt | string | 创建时间 |
| updatedAt | string | 更新时间 |
| relativeTime | string | 相对时间显示 |

### 5.2 获取笔记详情

**接口**: `GET /api/life/project/infinity-nikki/notes/:id`

**路径参数**: `id` - 笔记 ID

**响应**: 单个 `NikkiNote` 对象

---

## 6. 图册

### 6.1 获取图册概览

**接口**: `GET /api/life/project/infinity-nikki/gallery`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| albumCount | number | 图册总数 |
| photoCount | number | 照片总数 |
| albums | NikkiAlbum[] | 图册列表 |
| recentPhotos | NikkiPhoto[] | 最近照片列表 |

**NikkiAlbum**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 图册 ID |
| name | string | 图册名称 |
| coverUrl | string | 封面图片 URL |
| photoCount | number | 照片数量 |

**NikkiPhoto**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 照片 ID |
| albumId | string | 所属图册 ID |
| url | string | 原图 URL |
| thumbnail | string | 缩略图 URL |
| caption | string (可选) | 照片说明 |
| takenAt | string | 拍摄时间 |

---

## 7. AI 建议

### 7.1 获取 AI 建议

**接口**: `GET /api/life/project/infinity-nikki/ai`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| currentSuggestion | AiSuggestion | 当前建议 |
| suggestionCount | number | 建议总数 |

**AiSuggestion**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 建议 ID |
| content | string | 建议内容 |
| highlights | AiHighlight[] | 重点建议列表 |
| generatedAt | string | 生成时间 |
| status | `'generated' \| 'generating' \| 'pending' \| 'failed'` | 状态 |

**AiHighlight**:

| 字段 | 类型 | 说明 |
|------|------|------|
| text | string | 建议文本 |
| type | `'danger' \| 'info' \| 'success' \| 'warning'` | 建议类型 |

### 7.2 刷新 AI 建议

**接口**: `POST /api/life/project/infinity-nikki/ai/refresh`

**响应**: 同 7.1

---

## 8. 最近动态

### 8.1 获取最近动态

**接口**: `GET /api/life/project/infinity-nikki/activities`

**响应字段**:

| 字段 | 类型 | 说明 |
|------|------|------|
| (数组) | NikkiActivity[] | 动态列表 |

**NikkiActivity**:

| 字段 | 类型 | 说明 |
|------|------|------|
| id | string | 动态 ID |
| type | `'task_complete' \| 'note_added' \| 'screenshot_uploaded' \| 'event_progress' \| 'version_announcement'` | 动态类型 |
| title | string | 动态标题 |
| description | string | 动态描述 |
| icon | string | 图标名称 (lucide) |
| iconColor | string | 图标颜色 class |
| bg | string | 背景颜色 class |
| time | string | 相对时间显示 |
| createdAt | string | 创建时间 |

---

## 9. 快速记录

### 9.1 创建快速记录

**接口**: `POST /api/life/project/infinity-nikki/quick-record`

**请求参数**:

| 字段 | 类型 | 说明 |
|------|------|------|
| content | string | 记录内容（必填，最长 500 字） |

**响应**: `null`（成功即返回）

---

## 接口汇总

| 方法 | 接口路径 | 说明 |
|------|----------|------|
| GET | `/project` | 获取项目基础信息 |
| GET | `/daily-tasks` | 获取日常任务概览 |
| POST | `/daily-tasks/toggle` | 切换任务完成状态 |
| POST | `/weekly-goals/progress` | 更新周常目标进度 |
| POST | `/weekly-goals/status` | 切换周常目标状态 |
| GET | `/countdown` | 获取倒计时概览 |
| POST | `/events/reminder` | 设置/取消活动提醒 |
| POST | `/events/checkin` | 活动打卡/撤回 |
| POST | `/version-nodes/reminder` | 设置/取消版本节点提醒 |
| POST | `/version-nodes/checkin` | 版本节点打卡/撤回 |
| GET | `/assets` | 获取资产概览 |
| GET | `/assets/:id` | 获取资产详情 |
| GET | `/notes` | 获取笔记概览 |
| GET | `/notes/:id` | 获取笔记详情 |
| GET | `/gallery` | 获取图册概览 |
| GET | `/ai` | 获取 AI 建议 |
| POST | `/ai/refresh` | 刷新 AI 建议 |
| GET | `/activities` | 获取最近动态 |
| POST | `/quick-record` | 创建快速记录 |

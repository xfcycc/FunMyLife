package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 时间轴事件表 — 跨功能块的中央事件日志，由各块在写入时同步生成
 */
@Data
public class LmTimelineEventVo {

    /** 事件 ID */
    private Long eventId;

    /** 所属项目 ID */
    private Long projectId;

    /** 事件发生时间 */
    private LocalDateTime occurredAt;

    /** 事件类型 */
    private String type;

    /** 事件标题 */
    private String title;

    /** 事件描述 */
    private String description;

    /** 来源功能块标识（如 targets / gallery / assets） */
    private String sourceBlockKey;

    /** 关联版本 ID（可选） */
    private Long versionId;

    /** 关联活动 ID（可选） */
    private Long activityId;

    /** 关联目标 ID（可选） */
    private Long targetId;

    /** 敏感度：normal / private */
    private String sensitivity;

    /** 是否在概览页展示：1=是 0=否 */
    private String displayInOverview;

    /** 是否允许 AI 读取：1=是 0=否 */
    private String aiReadable;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----
    private Date createTime;
    private Date updateTime;
}

package com.funmylife.fml.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏目标表 — 用户追踪的每日/每周/活动/自定义目标，
 * 可选关联到版本（version_id）和活动（activity_id）
 */
@Data
@TableName("lm_game_target")
public class LmGameTarget {

    /** 目标 ID */
    @TableId(value = "target_id")
    private Long targetId;

    /** 所属项目 ID */
    private Long projectId;

    /** 目标类型：daily / weekly / activity / custom */
    private String type;

    /** 目标标题 */
    private String title;

    /** 目标描述 */
    private String description;

    /** 状态：todo / done / skipped / expired / archived */
    private String status;

    /** 当前进度 */
    private Integer progressCurrent;

    /** 目标进度 */
    private Integer progressTarget;

    /** 重置规则，JSON：{type, time, weekday} */
    private String resetRule;

    /** 关联版本 ID（可选） */
    private Long versionId;

    /** 关联活动 ID（可选） */
    private Long activityId;

    /** 截止时间 */
    private LocalDateTime dueAt;

    /** 优先级：low / normal / high */
    private String priority;

    /** 是否置顶到概览：1=是 0=否 */
    private String pinnedToOverview;

    /** 时间线规则，JSON */
    private String timelineRule;

    /** 归档时间 */
    private LocalDateTime archivedAt;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----

    private String tenantId;
    private Long createDept;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
}

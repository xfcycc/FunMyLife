package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏活动表 — 版本内的活动，通过 version_id 关联到 lm_game_version
 */
@Data
public class LmGameActivityVo {

    /** 活动 ID */
    private Long activityId;

    /** 所属项目 ID */
    private Long projectId;

    /** 所属版本 ID */
    private Long versionId;

    /** 活动标题 */
    private String title;

    /** 活动描述 */
    private String description;

    /** 活动开始时间 */
    private LocalDateTime startAt;

    /** 活动结束时间 */
    private LocalDateTime endAt;

    /** 状态：upcoming / active / ending / ended / archived */
    private String status;

    /** 优先级：low / normal / high */
    private String priority;

    /** 活动封面图 URL */
    private String cover;

    /** 提醒规则，JSON：{enabled, channels, beforeMinutes} */
    private String reminderRule;

    /** 归档时间 */
    private LocalDateTime archivedAt;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----
    private Date createTime;
    private Date updateTime;
}

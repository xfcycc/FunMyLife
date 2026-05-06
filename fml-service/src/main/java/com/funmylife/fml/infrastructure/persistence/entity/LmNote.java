package com.funmylife.fml.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 笔记表 — 攻略、评测、随笔等文本内容
 */
@Data
@TableName("lm_note")
public class LmNote {

    /** 笔记 ID */
    @TableId(value = "note_id")
    private Long noteId;

    /** 所属项目 ID */
    private Long projectId;

    /** 笔记标题 */
    private String title;

    /** 笔记正文 */
    private String content;

    /** 类型：note / guide / review */
    private String type;

    /** 分类标识 */
    private String category;

    /** 分类展示标签 */
    private String categoryLabel;

    /** 关联版本 ID（可选） */
    private Long versionId;

    /** 关联活动 ID（可选） */
    private Long activityId;

    /** 关联目标 ID（可选） */
    private Long targetId;

    /** 是否置顶到概览：1=是 0=否 */
    private String pinnedToOverview;

    /** 时间线规则，JSON */
    private String timelineRule;

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

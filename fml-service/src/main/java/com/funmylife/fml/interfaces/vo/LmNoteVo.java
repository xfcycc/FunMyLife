package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * 笔记表 — 攻略、评测、随笔等文本内容
 */
@Data
public class LmNoteVo {

    /** 笔记 ID */
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
    private Date createTime;
    private Date updateTime;
}

package com.funmylife.fml.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * 项目表 — Life Manager 的顶层容器，所有其他 lm_* 表通过 project_id 关联到此
 */
@Data
public class LmProject {

    /** 项目 ID（雪花算法生成） */
    private Long projectId;

    /** 项目名称 */
    private String projectName;

    /** 项目描述 */
    private String description;

    /** 封面图片地址 */
    private String coverSrc;

    /** 封面图片替代文本 */
    private String coverAlt;

    /** 状态：active / paused / archived */
    private String status;

    /** 方案模板 ID（如 infinity-nikki-game） */
    private String schemeId;

    /** 方案类型：game / travel / study */
    private String schemeType;

    /** 标签，JSON 数组：[{label, tone}] */
    private String tags;

    /** 统计数据，JSON 数组：[{label, value}] */
    private String stats;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列（保留字段兼容数据库，fml-service 不主动填充） ----

    private String tenantId;
    private Long createDept;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
}

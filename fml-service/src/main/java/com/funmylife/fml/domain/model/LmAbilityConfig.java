package com.funmylife.fml.domain.model;

import lombok.Data;

import java.util.Date;

/**
 * 功能块实例配置表 — 每行代表一个项目下的一个功能块（overview、targets、gallery 等），
 * 通过 JSON 字段定义该块的能力、导航、摘要规则、行为、时间线、AI 规则和安全策略。
 * 唯一约束：uk_project_block(project_id, block_key)
 */
@Data
public class LmAbilityConfig {

    /** 配置 ID（雪花算法生成） */
    private Long configId;

    /** 所属项目 ID */
    private Long projectId;

    /** 区块标识，如 overview / targets / gallery / assets / timeline / ai */
    private String blockKey;

    /** 前端展示名称 */
    private String displayName;

    /** 是否启用：1=启用 0=禁用 */
    private String enabled;

    /** 能力配置，JSON */
    private String capabilities;

    /** 导航配置，JSON：{visible, order} */
    private String navigation;

    /** 摘要规则，JSON 数组 — 概览页根据此规则聚合各表数据生成摘要卡片 */
    private String summaryRules;

    /** 字段配置，JSON */
    private String fields;

    /** 行为配置，JSON：{resetRules, reminderRules, archiveRule} */
    private String behavior;

    /** 时间线配置，JSON：{enabled, defaultWriteRule} */
    private String timeline;

    /** AI 规则，JSON：{readable, writableAfterConfirm, allowedUse} */
    private String aiRules;

    /** 安全配置，JSON：{sensitivity, maskInOverview} */
    private String security;

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

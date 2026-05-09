package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * 功能块实例视图对象。
 *
 * <p>该 VO 是前端管理页和详情页导航读取的配置契约。它不会暴露持久化 Entity，
 * 也不会把 Java 内部的 CapabilityRegistry 或能力实现类返回给前端。</p>
 */
@Data
public class BlockInstanceVo {

    /** 功能块实例 ID；来自 lm_block_instance_config.config_id。 */
    private Long blockInstanceId;

    /** 所属项目 ID。 */
    private Long projectId;

    /** 功能块 key，例如 overview、targets、gallery。 */
    private String blockKey;

    /** 当前项目内展示给用户看的功能块名称。 */
    private String displayName;

    /** 是否启用：1=启用 0=禁用。 */
    private String enabled;

    /** 能力引用列表 JSON，前端可解析为能力 key 或 CapabilityRef。 */
    private String capabilities;

    /** 导航配置 JSON。 */
    private String navigation;

    /** 概览摘要规则 JSON。 */
    private String summaryRules;

    /** 字段配置 JSON。 */
    private String fields;

    /** 行为配置 JSON。 */
    private String behavior;

    /** 时间轴配置 JSON。 */
    private String timeline;

    /** AI 读写规则 JSON。 */
    private String aiRules;

    /** 安全策略 JSON。 */
    private String security;

    /** 备注。 */
    private String remark;

    /** 创建人 ID，来自旧审计列，仅用于排查和展示。 */
    private Long createBy;

    /** 创建时间，来自旧审计列，仅用于排查和展示。 */
    private Date createTime;
}

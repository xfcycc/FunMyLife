package com.funmylife.fml.interfaces.request;

import lombok.Data;

/**
 * 功能块实例保存请求项。
 *
 * <p>每一项代表某个项目里的一个功能块实例。功能块通过 capabilities 引用一个或多个能力，
 * 再通过 summaryRules、timeline、aiRules、security 等配置决定这些能力在当前项目中的行为。</p>
 */
@Data
public class BlockInstanceSaveItemRequest {

    /** 功能块实例 ID；更新时传入，新建时为空。 */
    private Long blockInstanceId;

    /** 兼容前端 payload 的项目 ID；实际保存以外层 BlockInstanceBatchSaveRequest.projectId 为准。 */
    private Long projectId;

    /** 功能块唯一 key，例如 overview、targets、version_activity、gallery。 */
    private String blockKey;

    /** 当前项目内展示给用户看的功能块名称。 */
    private String displayName;

    /** 是否启用：1=启用 0=禁用。 */
    private String enabled;

    /** 能力引用列表 JSON；每一项应能转换为 CapabilityRef。 */
    private String capabilities;

    /** 导航配置 JSON，例如 {visible, order}。 */
    private String navigation;

    /** 概览摘要规则 JSON 数组。 */
    private String summaryRules;

    /** 字段配置 JSON。 */
    private String fields;

    /** 行为配置 JSON，例如 resetRules、reminderRules、archiveRule。 */
    private String behavior;

    /** 时间轴配置 JSON，例如 {enabled, defaultWriteRule}。 */
    private String timeline;

    /** AI 读写规则 JSON。 */
    private String aiRules;

    /** 安全策略 JSON。 */
    private String security;

    /** 备注。 */
    private String remark;
}

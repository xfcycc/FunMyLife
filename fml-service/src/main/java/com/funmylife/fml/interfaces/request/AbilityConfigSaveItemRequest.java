package com.funmylife.fml.interfaces.request;

import lombok.Data;

/**
 * 功能块配置保存请求项。
 *
 * <p>每一项对应项目下的一个功能块实例，例如 overview、targets、gallery。
 * JSON 配置字段先保持字符串形态，和当前前端适配器、数据库字段保持一致。</p>
 */
@Data
public class AbilityConfigSaveItemRequest {

    /** 配置 ID（更新时传入，新建时为空） */
    private Long configId;

    /** 兼容旧前端 payload 的项目 ID；实际保存以外层 AbilityConfigBatchSaveRequest.projectId 为准。 */
    private Long projectId;

    /** 功能块唯一 key，如 overview / targets / gallery。 */
    private String blockKey;

    /** 前端展示名称。 */
    private String displayName;

    /** 是否启用：1=启用 0=禁用 */
    private String enabled;

    /** 以下均为 JSON 字符串 */
    private String capabilities;
    private String navigation;
    private String summaryRules;
    private String fields;
    private String behavior;
    private String timeline;
    private String aiRules;
    private String security;

    /** 备注。 */
    private String remark;
}

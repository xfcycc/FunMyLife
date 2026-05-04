package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 能力配置表 lm_ability_config
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_ability_config")
public class LmAbilityConfig extends TenantEntity {

    /**
     * 配置ID
     */
    @TableId(value = "config_id")
    private Long configId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 区块标识
     */
    private String blockKey;

    /**
     * 显示名称
     */
    private String displayName;

    /**
     * 是否启用（Y是 N否）
     */
    private String enabled;

    /**
     * 能力配置（JSON）
     */
    private String capabilities;

    /**
     * 导航配置（JSON）
     */
    private String navigation;

    /**
     * 摘要规则（JSON）
     */
    private String summaryRules;

    /**
     * 字段配置（JSON）
     */
    private String fields;

    /**
     * 行为配置（JSON）
     */
    private String behavior;

    /**
     * 时间线配置（JSON）
     */
    private String timeline;

    /**
     * AI规则（JSON）
     */
    private String aiRules;

    /**
     * 安全配置（JSON）
     */
    private String security;

    /**
     * 备注
     */
    private String remark;

}

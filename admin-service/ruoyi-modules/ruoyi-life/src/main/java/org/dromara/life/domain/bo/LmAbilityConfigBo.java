package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmAbilityConfig;

/**
 * 能力配置业务对象 lm_ability_config
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmAbilityConfig.class, reverseConvertGenerate = false)
public class LmAbilityConfigBo extends BaseEntity {

    /**
     * 配置ID
     */
    private Long configId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 区块标识
     */
    @NotBlank(message = "区块标识不能为空")
    private String blockKey;

    /**
     * 显示名称
     */
    @NotBlank(message = "显示名称不能为空")
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

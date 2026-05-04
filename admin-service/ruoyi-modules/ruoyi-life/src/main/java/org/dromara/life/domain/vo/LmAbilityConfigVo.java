package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmAbilityConfig;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 能力配置视图对象 lm_ability_config
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmAbilityConfig.class)
public class LmAbilityConfigVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 配置ID
     */
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

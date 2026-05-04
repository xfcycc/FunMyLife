package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmMaterial;

/**
 * 素材业务对象 lm_material
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmMaterial.class, reverseConvertGenerate = false)
public class LmMaterialBo extends BaseEntity {

    /**
     * 素材ID
     */
    private Long materialId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 素材名称
     */
    @NotBlank(message = "素材名称不能为空")
    private String name;

    /**
     * 素材类型
     */
    private String type;

    /**
     * 素材描述
     */
    private String description;

    /**
     * 状态
     */
    private String status;

    /**
     * 当前数量
     */
    private Integer current;

    /**
     * 目标数量
     */
    private Integer target;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 时间线规则（JSON）
     */
    private String timelineRule;

    /**
     * 备注说明
     */
    private String note;

    /**
     * 备注
     */
    private String remark;

}

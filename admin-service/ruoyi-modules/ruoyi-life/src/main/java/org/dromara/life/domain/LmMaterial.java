package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 素材表 lm_material
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_material")
public class LmMaterial extends TenantEntity {

    /**
     * 素材ID
     */
    @TableId(value = "material_id")
    private Long materialId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 素材名称
     */
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

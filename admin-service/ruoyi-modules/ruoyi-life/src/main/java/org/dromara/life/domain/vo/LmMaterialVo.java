package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmMaterial;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 素材视图对象 lm_material
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmMaterial.class)
public class LmMaterialVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 素材ID
     */
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

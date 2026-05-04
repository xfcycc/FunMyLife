package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmProject;

/**
 * 项目业务对象 lm_project
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmProject.class, reverseConvertGenerate = false)
public class LmProjectBo extends BaseEntity {

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
    @NotBlank(message = "项目名称不能为空")
    private String projectName;

    /**
     * 项目描述
     */
    private String description;

    /**
     * 封面图片地址
     */
    private String coverSrc;

    /**
     * 封面图片替代文本
     */
    private String coverAlt;

    /**
     * 状态
     */
    private String status;

    /**
     * 方案ID
     */
    private String schemeId;

    /**
     * 方案类型
     */
    private String schemeType;

    /**
     * 标签（JSON）
     */
    private String tags;

    /**
     * 统计数据（JSON）
     */
    private String stats;

    /**
     * 备注
     */
    private String remark;

}

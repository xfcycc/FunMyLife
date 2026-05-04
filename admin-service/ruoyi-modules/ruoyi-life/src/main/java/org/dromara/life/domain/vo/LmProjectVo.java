package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmProject;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 项目视图对象 lm_project
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmProject.class)
public class LmProjectVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 项目名称
     */
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

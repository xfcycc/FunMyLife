package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 项目表 lm_project
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_project")
public class LmProject extends TenantEntity {

    /**
     * 项目ID
     */
    @TableId(value = "project_id")
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

}

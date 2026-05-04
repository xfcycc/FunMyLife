package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmAsset;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 资产视图对象 lm_asset
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmAsset.class)
public class LmAssetVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 资产名称
     */
    private String name;

    /**
     * 资产类型
     */
    private String type;

    /**
     * 资产分类
     */
    private String category;

    /**
     * 资产价值
     */
    private String value;

    /**
     * 状态
     */
    private String status;

    /**
     * 状态标签
     */
    private String statusLabel;

    /**
     * 敏感度
     */
    private String sensitivity;

    /**
     * 图标
     */
    private String icon;

    /**
     * 资产描述
     */
    private String description;

    /**
     * 关联链接
     */
    private String linkedUrl;

    /**
     * 过期时间
     */
    private LocalDateTime expiresAt;

    /**
     * 备注说明
     */
    private String notes;

    /**
     * 时间线规则（JSON）
     */
    private String timelineRule;

    /**
     * 归档时间
     */
    private LocalDateTime archivedAt;

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

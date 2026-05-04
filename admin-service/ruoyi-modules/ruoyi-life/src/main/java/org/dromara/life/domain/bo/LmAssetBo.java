package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmAsset;

import java.time.LocalDateTime;

/**
 * 资产业务对象 lm_asset
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmAsset.class, reverseConvertGenerate = false)
public class LmAssetBo extends BaseEntity {

    /**
     * 资产ID
     */
    private Long assetId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 资产名称
     */
    @NotBlank(message = "资产名称不能为空")
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

}

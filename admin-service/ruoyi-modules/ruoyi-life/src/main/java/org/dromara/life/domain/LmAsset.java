package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 资产表 lm_asset
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_asset")
public class LmAsset extends TenantEntity {

    /**
     * 资产ID
     */
    @TableId(value = "asset_id")
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

}

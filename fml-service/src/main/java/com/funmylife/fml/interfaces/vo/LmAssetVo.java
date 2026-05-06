package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 账号资产表 — 跟踪需要保护或绑定的资产（兑换码、密钥、链接等）
 */
@Data
public class LmAssetVo {

    /** 资产 ID */
    private Long assetId;

    /** 所属项目 ID */
    private Long projectId;

    /** 资产名称 */
    private String name;

    /** 资产类型 */
    private String type;

    /** 资产分类 */
    private String category;

    /** 资产值（如兑换码内容） */
    private String value;

    /** 状态：protected / bound / pending / expired / archived */
    private String status;

    /** 状态标签（前端展示用） */
    private String statusLabel;

    /** 敏感度：normal / sensitive */
    private String sensitivity;

    /** 图标标识 */
    private String icon;

    /** 描述 */
    private String description;

    /** 关联链接 */
    private String linkedUrl;

    /** 过期时间 */
    private LocalDateTime expiresAt;

    /** 附加备注 */
    private String notes;

    /** 时间线规则，JSON */
    private String timelineRule;

    /** 归档时间 */
    private LocalDateTime archivedAt;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----
    private Date createTime;
    private Date updateTime;
}

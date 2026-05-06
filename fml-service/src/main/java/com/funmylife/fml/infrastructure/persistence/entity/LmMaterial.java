package com.funmylife.fml.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 素材/资源表 — 跟踪可收集的素材（套装、材料、代币等），
 * 可选关联到版本、活动、目标
 */
@Data
@TableName("lm_material")
public class LmMaterial {

    /** 素材 ID */
    @TableId(value = "material_id")
    private Long materialId;

    /** 所属项目 ID */
    private Long projectId;

    /** 素材名称 */
    private String name;

    /** 素材类型：outfit / material / currency / collection / recipe / other */
    private String type;

    /** 描述 */
    private String description;

    /** 状态：collecting / completed / archived */
    private String status;

    /** 当前数量 */
    private Integer current;

    /** 目标数量 */
    private Integer target;

    /** 关联版本 ID（可选） */
    private Long versionId;

    /** 关联活动 ID（可选） */
    private Long activityId;

    /** 关联目标 ID（可选） */
    private Long targetId;

    /** 时间线规则，JSON */
    private String timelineRule;

    /** 备注文本 */
    private String note;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----

    private String tenantId;
    private Long createDept;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
}

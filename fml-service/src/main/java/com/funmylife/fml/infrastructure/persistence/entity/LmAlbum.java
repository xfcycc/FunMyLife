package com.funmylife.fml.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 图册表 — 照片相册容器，一个项目下可有多个图册
 */
@Data
@TableName("lm_album")
public class LmAlbum {

    /** 图册 ID */
    @TableId(value = "album_id")
    private Long albumId;

    /** 所属项目 ID */
    private Long projectId;

    /** 图册名称 */
    private String name;

    /** 图册封面 URL */
    private String coverUrl;

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

package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * 图册表 — 照片相册容器，一个项目下可有多个图册
 */
@Data
public class LmAlbumVo {

    /** 图册 ID */
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
    private Date createTime;
    private Date updateTime;
}

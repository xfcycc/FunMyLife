package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 照片表 — 图册内的单张照片，通过 album_id 关联到 lm_album
 */
@Data
public class LmPhotoVo {

    /** 照片 ID */
    private Long photoId;

    /** 所属项目 ID */
    private Long projectId;

    /** 所属图册 ID */
    private Long albumId;

    /** 照片原图 URL */
    private String url;

    /** 缩略图 URL */
    private String thumbnail;

    /** 照片说明 */
    private String caption;

    /** 拍摄时间 */
    private LocalDateTime takenAt;

    /** 关联版本 ID（可选） */
    private Long versionId;

    /** 关联活动 ID（可选） */
    private Long activityId;

    /** 关联目标 ID（可选） */
    private Long targetId;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----
    private Date createTime;
    private Date updateTime;
}

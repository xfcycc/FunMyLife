package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmPhoto;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 照片视图对象 lm_photo
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmPhoto.class)
public class LmPhotoVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 照片ID
     */
    private Long photoId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 照片URL
     */
    private String url;

    /**
     * 缩略图URL
     */
    private String thumbnail;

    /**
     * 照片说明
     */
    private String caption;

    /**
     * 拍摄时间
     */
    private LocalDateTime takenAt;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 目标ID
     */
    private Long targetId;

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

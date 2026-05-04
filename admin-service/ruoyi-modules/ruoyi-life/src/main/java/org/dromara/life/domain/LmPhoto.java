package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 照片表 lm_photo
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_photo")
public class LmPhoto extends TenantEntity {

    /**
     * 照片ID
     */
    @TableId(value = "photo_id")
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

}

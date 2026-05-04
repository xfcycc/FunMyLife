package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmPhoto;

import java.time.LocalDateTime;

/**
 * 照片业务对象 lm_photo
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmPhoto.class, reverseConvertGenerate = false)
public class LmPhotoBo extends BaseEntity {

    /**
     * 照片ID
     */
    private Long photoId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 相册ID
     */
    @NotNull(message = "相册ID不能为空")
    private Long albumId;

    /**
     * 照片URL
     */
    @NotBlank(message = "照片URL不能为空")
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

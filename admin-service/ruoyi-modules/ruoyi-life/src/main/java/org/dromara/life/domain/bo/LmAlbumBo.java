package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmAlbum;

/**
 * 相册业务对象 lm_album
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmAlbum.class, reverseConvertGenerate = false)
public class LmAlbumBo extends BaseEntity {

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 相册名称
     */
    @NotBlank(message = "相册名称不能为空")
    private String name;

    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 备注
     */
    private String remark;

}

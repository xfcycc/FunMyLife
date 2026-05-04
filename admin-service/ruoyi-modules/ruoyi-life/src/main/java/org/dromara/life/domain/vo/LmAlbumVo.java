package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmAlbum;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 相册视图对象 lm_album
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmAlbum.class)
public class LmAlbumVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 相册ID
     */
    private Long albumId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 相册名称
     */
    private String name;

    /**
     * 封面URL
     */
    private String coverUrl;

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

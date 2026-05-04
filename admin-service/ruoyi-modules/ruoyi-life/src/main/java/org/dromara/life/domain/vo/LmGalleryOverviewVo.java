package org.dromara.life.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 图库概览视图对象
 *
 * @author caiguoyu
 */
@Data
public class LmGalleryOverviewVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 相册数
     */
    private Integer albumCount;

    /**
     * 照片数
     */
    private Integer photoCount;

    /**
     * 相册列表（JSON）
     */
    private String albums;

    /**
     * 最近照片列表（JSON）
     */
    private String recentPhotos;

}

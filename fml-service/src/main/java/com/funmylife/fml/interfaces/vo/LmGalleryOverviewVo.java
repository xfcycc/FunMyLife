package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * 图册概览视图对象。
 *
 * <p>图册页需要同时拿到相册列表和照片列表。这里用明确字段替代 Map，
 * 方便前端和后续接口文档直接识别响应结构。</p>
 */
@Data
public class LmGalleryOverviewVo {

    /** 项目下的相册列表。 */
    private List<LmAlbumVo> albums;

    /** 项目下的照片列表，默认按创建时间倒序。 */
    private List<LmPhotoVo> photos;
}

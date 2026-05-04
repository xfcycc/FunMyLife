package org.dromara.life.service;

import org.dromara.life.domain.bo.LmAlbumBo;
import org.dromara.life.domain.vo.LmAlbumVo;

import java.util.List;

/**
 * 图册 服务层
 */
public interface ILmAlbumService {

    /**
     * 查询图册列表
     *
     * @param projectId 项目ID
     * @return 图册列表
     */
    List<LmAlbumVo> queryList(Long projectId);

    /**
     * 查询图册详情
     *
     * @param albumId 图册ID
     * @return 图册信息
     */
    LmAlbumVo queryById(Long albumId);

    /**
     * 新增图册
     *
     * @param bo 图册信息
     */
    void insert(LmAlbumBo bo);

    /**
     * 修改图册
     *
     * @param bo 图册信息
     */
    void update(LmAlbumBo bo);

    /**
     * 删除图册
     *
     * @param albumId 图册ID
     */
    void deleteWithValid(Long albumId);
}

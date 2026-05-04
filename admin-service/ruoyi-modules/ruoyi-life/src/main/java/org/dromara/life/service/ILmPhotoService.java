package org.dromara.life.service;

import org.dromara.life.domain.bo.LmPhotoBo;
import org.dromara.life.domain.vo.LmPhotoVo;

import java.util.List;

/**
 * 照片 服务层
 */
public interface ILmPhotoService {

    /**
     * 查询照片列表
     *
     * @param projectId 项目ID
     * @return 照片列表
     */
    List<LmPhotoVo> queryList(Long projectId);

    /**
     * 查询照片详情
     *
     * @param photoId 照片ID
     * @return 照片信息
     */
    LmPhotoVo queryById(Long photoId);

    /**
     * 新增照片
     *
     * @param bo 照片信息
     */
    void insert(LmPhotoBo bo);

    /**
     * 修改照片
     *
     * @param bo 照片信息
     */
    void update(LmPhotoBo bo);

    /**
     * 删除照片
     *
     * @param photoId 照片ID
     */
    void deleteWithValid(Long photoId);
}

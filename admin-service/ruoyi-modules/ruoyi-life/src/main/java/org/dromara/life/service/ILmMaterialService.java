package org.dromara.life.service;

import org.dromara.life.domain.bo.LmMaterialBo;
import org.dromara.life.domain.vo.LmMaterialVo;

import java.util.List;

/**
 * 素材 服务层
 */
public interface ILmMaterialService {

    /**
     * 查询素材列表
     *
     * @param projectId 项目ID
     * @return 素材列表
     */
    List<LmMaterialVo> queryList(Long projectId);

    /**
     * 查询素材详情
     *
     * @param materialId 素材ID
     * @return 素材信息
     */
    LmMaterialVo queryById(Long materialId);

    /**
     * 新增素材
     *
     * @param bo 素材信息
     */
    void insert(LmMaterialBo bo);

    /**
     * 修改素材
     *
     * @param bo 素材信息
     */
    void update(LmMaterialBo bo);

    /**
     * 删除素材
     *
     * @param materialId 素材ID
     */
    void deleteWithValid(Long materialId);
}

package org.dromara.life.service;

import org.dromara.life.domain.bo.LmProjectBo;
import org.dromara.life.domain.vo.LmProjectVo;

/**
 * 项目 服务层
 */
public interface ILmProjectService {

    /**
     * 查询项目详情
     *
     * @param projectId 项目ID
     * @return 项目信息
     */
    LmProjectVo queryById(Long projectId);

    /**
     * 新增项目
     *
     * @param bo 项目信息
     */
    void insert(LmProjectBo bo);

    /**
     * 修改项目
     *
     * @param bo 项目信息
     */
    void update(LmProjectBo bo);

    /**
     * 删除项目
     *
     * @param projectId 项目ID
     */
    void deleteWithValid(Long projectId);
}

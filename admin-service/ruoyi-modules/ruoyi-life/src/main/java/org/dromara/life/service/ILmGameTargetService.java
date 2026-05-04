package org.dromara.life.service;

import org.dromara.life.domain.bo.LmGameTargetBo;
import org.dromara.life.domain.vo.LmGameTargetVo;

import java.util.List;

/**
 * 游戏目标 服务层
 */
public interface ILmGameTargetService {

    /**
     * 查询目标列表
     *
     * @param projectId 项目ID
     * @return 目标列表
     */
    List<LmGameTargetVo> queryList(Long projectId);

    /**
     * 查询目标详情
     *
     * @param targetId 目标ID
     * @return 目标信息
     */
    LmGameTargetVo queryById(Long targetId);

    /**
     * 新增目标
     *
     * @param bo 目标信息
     */
    void insert(LmGameTargetBo bo);

    /**
     * 修改目标
     *
     * @param bo 目标信息
     */
    void update(LmGameTargetBo bo);

    /**
     * 删除目标
     *
     * @param targetId 目标ID
     */
    void deleteWithValid(Long targetId);

    /**
     * 更新目标状态
     *
     * @param targetId 目标ID
     * @param status   新状态
     */
    void updateStatus(Long targetId, String status);

    /**
     * 更新目标进度
     *
     * @param targetId 目标ID
     * @param current  当前进度
     */
    void updateProgress(Long targetId, Integer current);
}

package org.dromara.life.service;

import org.dromara.life.domain.bo.LmGameVersionBo;
import org.dromara.life.domain.vo.LmGameVersionVo;

import java.util.List;

/**
 * 游戏版本 服务层
 */
public interface ILmGameVersionService {

    /**
     * 查询版本列表
     *
     * @param projectId 项目ID
     * @return 版本列表
     */
    List<LmGameVersionVo> queryList(Long projectId);

    /**
     * 查询版本详情
     *
     * @param versionId 版本ID
     * @return 版本信息
     */
    LmGameVersionVo queryById(Long versionId);

    /**
     * 查询当前活跃版本
     *
     * @param projectId 项目ID
     * @return 当前版本信息
     */
    LmGameVersionVo queryCurrent(Long projectId);

    /**
     * 新增版本
     *
     * @param bo 版本信息
     */
    void insert(LmGameVersionBo bo);

    /**
     * 修改版本
     *
     * @param bo 版本信息
     */
    void update(LmGameVersionBo bo);

    /**
     * 删除版本
     *
     * @param versionId 版本ID
     */
    void deleteWithValid(Long versionId);
}

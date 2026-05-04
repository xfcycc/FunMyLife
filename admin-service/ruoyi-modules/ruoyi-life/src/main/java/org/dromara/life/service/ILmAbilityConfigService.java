package org.dromara.life.service;

import org.dromara.life.domain.bo.LmAbilityConfigBo;
import org.dromara.life.domain.vo.LmAbilityConfigVo;

import java.util.List;

/**
 * 功能块实例配置 服务层
 */
public interface ILmAbilityConfigService {

    /**
     * 查询功能块配置列表
     *
     * @param projectId 项目ID
     * @return 配置列表
     */
    List<LmAbilityConfigVo> queryList(Long projectId);

    /**
     * 查询功能块配置详情
     *
     * @param configId 配置ID
     * @return 配置信息
     */
    LmAbilityConfigVo queryById(Long configId);

    /**
     * 新增功能块配置
     *
     * @param bo 配置信息
     */
    void insert(LmAbilityConfigBo bo);

    /**
     * 修改功能块配置
     *
     * @param bo 配置信息
     */
    void update(LmAbilityConfigBo bo);

    /**
     * 删除功能块配置
     *
     * @param configId 配置ID
     */
    void deleteWithValid(Long configId);

    /**
     * 批量保存功能块配置（先删后增）
     *
     * @param projectId 项目ID
     * @param boList    配置列表
     */
    void batchSave(Long projectId, List<LmAbilityConfigBo> boList);
}

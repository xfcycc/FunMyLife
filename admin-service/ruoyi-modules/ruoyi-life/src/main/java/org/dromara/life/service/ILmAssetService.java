package org.dromara.life.service;

import org.dromara.life.domain.bo.LmAssetBo;
import org.dromara.life.domain.vo.LmAssetVo;

import java.util.List;

/**
 * 资产 服务层
 */
public interface ILmAssetService {

    /**
     * 查询资产列表
     *
     * @param projectId 项目ID
     * @return 资产列表
     */
    List<LmAssetVo> queryList(Long projectId);

    /**
     * 查询资产详情
     *
     * @param assetId 资产ID
     * @return 资产信息
     */
    LmAssetVo queryById(Long assetId);

    /**
     * 新增资产
     *
     * @param bo 资产信息
     */
    void insert(LmAssetBo bo);

    /**
     * 修改资产
     *
     * @param bo 资产信息
     */
    void update(LmAssetBo bo);

    /**
     * 删除资产
     *
     * @param assetId 资产ID
     */
    void deleteWithValid(Long assetId);

    /**
     * 更新资产状态
     *
     * @param assetId 资产ID
     * @param status  新状态
     */
    void updateStatus(Long assetId, String status);
}

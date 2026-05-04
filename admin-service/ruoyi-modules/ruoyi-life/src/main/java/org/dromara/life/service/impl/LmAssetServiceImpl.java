package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmAsset;
import org.dromara.life.domain.bo.LmAssetBo;
import org.dromara.life.domain.vo.LmAssetVo;
import org.dromara.life.mapper.LmAssetMapper;
import org.dromara.life.service.ILmAssetService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资产 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmAssetServiceImpl implements ILmAssetService {

    private final LmAssetMapper baseMapper;

    @Override
    public List<LmAssetVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmAsset> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAsset::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmAssetVo queryById(Long assetId) {
        return baseMapper.selectVoById(assetId);
    }

    @Override
    public void insert(LmAssetBo bo) {
        LmAsset entity = MapstructUtils.convert(bo, LmAsset.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmAssetBo bo) {
        LmAsset entity = MapstructUtils.convert(bo, LmAsset.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long assetId) {
        baseMapper.deleteById(assetId);
    }

    @Override
    public void updateStatus(Long assetId, String status) {
        LmAsset entity = baseMapper.selectById(assetId);
        if (entity != null) {
            entity.setStatus(status);
            baseMapper.updateById(entity);
        }
    }
}

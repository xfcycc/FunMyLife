package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmAbilityConfig;
import org.dromara.life.domain.bo.LmAbilityConfigBo;
import org.dromara.life.domain.vo.LmAbilityConfigVo;
import org.dromara.life.mapper.LmAbilityConfigMapper;
import org.dromara.life.service.ILmAbilityConfigService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能块实例配置 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmAbilityConfigServiceImpl implements ILmAbilityConfigService {

    private final LmAbilityConfigMapper baseMapper;

    @Override
    public List<LmAbilityConfigVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmAbilityConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAbilityConfig::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmAbilityConfigVo queryById(Long configId) {
        return baseMapper.selectVoById(configId);
    }

    @Override
    public void insert(LmAbilityConfigBo bo) {
        LmAbilityConfig entity = MapstructUtils.convert(bo, LmAbilityConfig.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmAbilityConfigBo bo) {
        LmAbilityConfig entity = MapstructUtils.convert(bo, LmAbilityConfig.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long configId) {
        baseMapper.deleteById(configId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long projectId, List<LmAbilityConfigBo> boList) {
        // 删除该项目下所有旧配置
        LambdaQueryWrapper<LmAbilityConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAbilityConfig::getProjectId, projectId);
        baseMapper.delete(lqw);
        // 批量插入新配置
        if (boList != null && !boList.isEmpty()) {
            List<LmAbilityConfig> entities = MapstructUtils.convert(boList, LmAbilityConfig.class);
            for (LmAbilityConfig entity : entities) {
                entity.setProjectId(projectId);
                baseMapper.insert(entity);
            }
        }
    }
}

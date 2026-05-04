package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmGameTarget;
import org.dromara.life.domain.bo.LmGameTargetBo;
import org.dromara.life.domain.vo.LmGameTargetVo;
import org.dromara.life.mapper.LmGameTargetMapper;
import org.dromara.life.service.ILmGameTargetService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏目标 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmGameTargetServiceImpl implements ILmGameTargetService {

    private final LmGameTargetMapper baseMapper;

    @Override
    public List<LmGameTargetVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmGameTarget> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameTarget::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmGameTargetVo queryById(Long targetId) {
        return baseMapper.selectVoById(targetId);
    }

    @Override
    public void insert(LmGameTargetBo bo) {
        LmGameTarget entity = MapstructUtils.convert(bo, LmGameTarget.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmGameTargetBo bo) {
        LmGameTarget entity = MapstructUtils.convert(bo, LmGameTarget.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long targetId) {
        baseMapper.deleteById(targetId);
    }

    @Override
    public void updateStatus(Long targetId, String status) {
        LmGameTarget entity = baseMapper.selectById(targetId);
        if (entity != null) {
            entity.setStatus(status);
            baseMapper.updateById(entity);
        }
    }

    @Override
    public void updateProgress(Long targetId, Integer current) {
        LmGameTarget entity = baseMapper.selectById(targetId);
        if (entity != null) {
            entity.setProgressCurrent(current);
            // 如果进度达到目标值，自动标记为完成
            if (entity.getProgressTarget() != null && entity.getProgressTarget() > 0
                && current != null && current >= entity.getProgressTarget()) {
                entity.setStatus("done");
            }
            baseMapper.updateById(entity);
        }
    }
}

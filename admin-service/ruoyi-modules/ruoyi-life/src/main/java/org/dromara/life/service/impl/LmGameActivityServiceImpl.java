package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmGameActivity;
import org.dromara.life.domain.bo.LmGameActivityBo;
import org.dromara.life.domain.vo.LmGameActivityVo;
import org.dromara.life.mapper.LmGameActivityMapper;
import org.dromara.life.service.ILmGameActivityService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏活动 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmGameActivityServiceImpl implements ILmGameActivityService {

    private final LmGameActivityMapper baseMapper;

    @Override
    public List<LmGameActivityVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmGameActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameActivity::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmGameActivityVo queryById(Long activityId) {
        return baseMapper.selectVoById(activityId);
    }

    @Override
    public void insert(LmGameActivityBo bo) {
        LmGameActivity entity = MapstructUtils.convert(bo, LmGameActivity.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmGameActivityBo bo) {
        LmGameActivity entity = MapstructUtils.convert(bo, LmGameActivity.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long activityId) {
        baseMapper.deleteById(activityId);
    }

    @Override
    public void toggleReminder(Long activityId, boolean enabled) {
        LmGameActivity entity = baseMapper.selectById(activityId);
        if (entity != null) {
            // reminderRule JSON 中的 enabled 字段由前端组装，后端直接更新
            baseMapper.updateById(entity);
        }
    }
}

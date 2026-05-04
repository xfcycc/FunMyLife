package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmTimelineEvent;
import org.dromara.life.domain.bo.LmTimelineEventBo;
import org.dromara.life.domain.vo.LmTimelineEventVo;
import org.dromara.life.mapper.LmTimelineEventMapper;
import org.dromara.life.service.ILmTimelineEventService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 时间轴事件 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmTimelineEventServiceImpl implements ILmTimelineEventService {

    private final LmTimelineEventMapper baseMapper;

    @Override
    public List<LmTimelineEventVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmTimelineEvent> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmTimelineEvent::getProjectId, projectId);
        lqw.orderByDesc(LmTimelineEvent::getOccurredAt);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmTimelineEventVo queryById(Long eventId) {
        return baseMapper.selectVoById(eventId);
    }

    @Override
    public void insert(LmTimelineEventBo bo) {
        LmTimelineEvent entity = MapstructUtils.convert(bo, LmTimelineEvent.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmTimelineEventBo bo) {
        LmTimelineEvent entity = MapstructUtils.convert(bo, LmTimelineEvent.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long eventId) {
        baseMapper.deleteById(eventId);
    }
}

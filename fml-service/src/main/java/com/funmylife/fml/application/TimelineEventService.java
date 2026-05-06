package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmTimelineEvent;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmTimelineEventVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 时间轴事件服务 — 查询跨功能块的事件日志
 */
@Service
@RequiredArgsConstructor
public class TimelineEventService {

    private final LifeDataRepository lifeDataRepository;

    /** 查询指定项目下所有时间轴事件，按发生时间倒序 */
    public List<LmTimelineEventVo> queryList(Long projectId) {
        return lifeDataRepository.findTimelineEvents(projectId).stream().map(this::toVo).toList();
    }

    private LmTimelineEventVo toVo(LmTimelineEvent entity) {
        LmTimelineEventVo vo = new LmTimelineEventVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

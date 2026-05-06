package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmTimelineEvent;
import org.apache.ibatis.annotations.Mapper;

/** lm_timeline_event 表 Mapper */
@Mapper
public interface LmTimelineEventMapper extends BaseMapper<LmTimelineEvent> {
}

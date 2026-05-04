package org.dromara.life.service;

import org.dromara.life.domain.bo.LmTimelineEventBo;
import org.dromara.life.domain.vo.LmTimelineEventVo;

import java.util.List;

/**
 * 时间轴事件 服务层
 */
public interface ILmTimelineEventService {

    /**
     * 查询时间轴事件列表
     *
     * @param projectId 项目ID
     * @return 事件列表
     */
    List<LmTimelineEventVo> queryList(Long projectId);

    /**
     * 查询事件详情
     *
     * @param eventId 事件ID
     * @return 事件信息
     */
    LmTimelineEventVo queryById(Long eventId);

    /**
     * 新增事件
     *
     * @param bo 事件信息
     */
    void insert(LmTimelineEventBo bo);

    /**
     * 修改事件
     *
     * @param bo 事件信息
     */
    void update(LmTimelineEventBo bo);

    /**
     * 删除事件
     *
     * @param eventId 事件ID
     */
    void deleteWithValid(Long eventId);
}

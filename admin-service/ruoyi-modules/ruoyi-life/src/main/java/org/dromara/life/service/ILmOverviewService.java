package org.dromara.life.service;

import org.dromara.life.domain.vo.LmOverviewSummaryVo;

import java.util.List;

/**
 * 概览摘要 服务层
 */
public interface ILmOverviewService {

    /**
     * 获取概览摘要列表
     *
     * @param projectId 项目ID
     * @return 摘要列表
     */
    List<LmOverviewSummaryVo> getOverviewSummaries(Long projectId);
}

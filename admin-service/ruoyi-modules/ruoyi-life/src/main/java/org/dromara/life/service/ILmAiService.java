package org.dromara.life.service;

import org.dromara.life.domain.vo.LmAiOverviewVo;

/**
 * AI 建议 服务层
 */
public interface ILmAiService {

    /**
     * 获取AI概览建议
     *
     * @param projectId 项目ID
     * @return AI概览信息
     */
    LmAiOverviewVo getAiOverview(Long projectId);

    /**
     * 刷新AI建议
     *
     * @param projectId 项目ID
     * @return 刷新后的AI概览信息
     */
    LmAiOverviewVo refreshAiSuggestion(Long projectId);
}

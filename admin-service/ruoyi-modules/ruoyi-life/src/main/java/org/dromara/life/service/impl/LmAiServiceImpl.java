package org.dromara.life.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.life.domain.vo.LmAiOverviewVo;
import org.dromara.life.service.ILmAiService;
import org.springframework.stereotype.Service;

/**
 * AI 建议 服务层实现
 *
 * Phase 1: 返回 mock/占位数据。
 * 后续阶段将接入真实 AI 服务，基于目标、活动、素材、时间轴生成建议。
 */
@RequiredArgsConstructor
@Service
public class LmAiServiceImpl implements ILmAiService {

    @Override
    public LmAiOverviewVo getAiOverview(Long projectId) {
        // Phase 1: 返回占位数据
        LmAiOverviewVo vo = new LmAiOverviewVo();
        vo.setCurrentSuggestion("AI 暂未接入，此为占位内容。");
        vo.setSuggestionCount(0);
        return vo;
    }

    @Override
    public LmAiOverviewVo refreshAiSuggestion(Long projectId) {
        // Phase 1: 模拟刷新，返回同样的占位数据
        return getAiOverview(projectId);
    }
}

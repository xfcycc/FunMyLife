package com.funmylife.fml.application;

import com.funmylife.fml.interfaces.vo.LmAiOverviewVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI 服务 — Phase 1 临时 stub，返回空占位数据
 * 与现有 RuoYi 版本的 LmAiService 行为一致（Phase 1 即为占位）
 */
@Service
public class AiService {

    /**
     * 获取 AI 概览建议（stub）
     */
    public LmAiOverviewVo getOverview(Long projectId) {
        LmAiOverviewVo vo = new LmAiOverviewVo();
        vo.setCurrentSuggestion("");
        vo.setHighlights(List.of());
        vo.setSuggestionCount(0);
        return vo;
    }
}

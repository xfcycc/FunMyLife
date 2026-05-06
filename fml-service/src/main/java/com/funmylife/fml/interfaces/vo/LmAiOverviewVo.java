package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * AI 概览视图对象。
 *
 * <p>用于 /life/project/ai/overview 与 /life/project/ai/summaries/refresh。
 * 当前值可能是占位数据，但接口结构按正式 VO 保持稳定。</p>
 */
@Data
public class LmAiOverviewVo {

    /** 当前建议正文。 */
    private String currentSuggestion;

    /** 建议中的重点提示。 */
    private List<LmAiHighlightVo> highlights;

    /** 当前项目累计建议数量。 */
    private Integer suggestionCount;
}

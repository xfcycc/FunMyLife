package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.AiService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmAiOverviewVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * AI 建议接口。
 *
 * <p>当前 AI 能力还是占位实现，但接口契约先按正式形态收口：入参是实体 request，
 * 出参是 LmAiOverviewVo，不使用 Map 临时拼装。</p>
 */
@RestController
@RequestMapping("/life/project/ai")
@RequiredArgsConstructor
public class LmAiController {

    private final AiService aiService;

    /** 查询当前 AI 建议概览。 */
    @PostMapping("/overview")
    public R<LmAiOverviewVo> overview(@RequestBody ProjectScopedRequest request) {
        return R.ok(aiService.getOverview(request.getProjectId()));
    }

    /** 刷新 AI 建议；现阶段复用概览占位逻辑，后续可在 application 层接入真实生成。 */
    @PostMapping("/summaries/refresh")
    public R<LmAiOverviewVo> refresh(@RequestBody ProjectScopedRequest request) {
        return R.ok(aiService.getOverview(request.getProjectId()));
    }
}

package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.vo.LmAiOverviewVo;
import org.dromara.life.service.ILmAiService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * AI 建议 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/ai")
public class LmAiController extends BaseController {

    private final ILmAiService aiService;

    /**
     * 获取AI概览建议
     */
    @SaCheckPermission("life:ai:query")
    @GetMapping("/overview")
    public R<LmAiOverviewVo> getAiOverview(@PathVariable Long projectId) {
        return R.ok(aiService.getAiOverview(projectId));
    }

    /**
     * 刷新AI建议
     */
    @SaCheckPermission("life:ai:edit")
    @Log(title = "AI建议", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PostMapping("/summaries")
    public R<LmAiOverviewVo> refreshAiSuggestion(@PathVariable Long projectId) {
        return R.ok(aiService.refreshAiSuggestion(projectId));
    }
}

package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.vo.LmOverviewSummaryVo;
import org.dromara.life.service.ILmOverviewService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 概览摘要 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/overview-summaries")
public class LmOverviewController extends BaseController {

    private final ILmOverviewService overviewService;

    /**
     * 获取概览摘要列表
     */
    @SaCheckPermission("life:overview:list")
    @GetMapping
    public R<List<LmOverviewSummaryVo>> list(@PathVariable Long projectId) {
        return R.ok(overviewService.getOverviewSummaries(projectId));
    }
}

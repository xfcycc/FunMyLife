package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.OverviewService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmOverviewSummaryVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 概览摘要接口。
 *
 * <p>概览摘要由 application 层读取功能块 summaryRules 后聚合生成。Controller
 * 不直接拼装任何数据，也不向前端返回 Map 或持久化 Entity。</p>
 */
@RestController
@RequestMapping("/life/project/overview-summaries")
@RequiredArgsConstructor
public class LmOverviewController {

    private final OverviewService overviewService;

    /** 查询项目概览页摘要卡片列表。 */
    @PostMapping("/list")
    public R<List<LmOverviewSummaryVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(overviewService.getOverviewSummaries(request.getProjectId()));
    }
}

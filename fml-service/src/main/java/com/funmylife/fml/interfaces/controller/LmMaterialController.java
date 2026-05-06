package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.MaterialService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmMaterialOverviewVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 素材收集接口。
 *
 * <p>素材概览返回 total + list 的强类型 VO，保留前端现有 overview 消费方式，
 * 但不再用 Map 临时拼装响应。</p>
 */
@RestController
@RequestMapping("/life/project/materials")
@RequiredArgsConstructor
public class LmMaterialController {

    private final MaterialService materialService;

    /** 查询素材收集概览。 */
    @PostMapping("/overview")
    public R<LmMaterialOverviewVo> overview(@RequestBody ProjectScopedRequest request) {
        return R.ok(materialService.queryOverview(request.getProjectId()));
    }
}

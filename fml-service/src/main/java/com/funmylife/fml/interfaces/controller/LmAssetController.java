package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.AssetService;
import com.funmylife.fml.interfaces.request.AssetDetailRequest;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmAssetOverviewVo;
import com.funmylife.fml.interfaces.vo.LmAssetVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 账号资产接口。
 *
 * <p>资产可能包含敏感字段，所以 Controller 只返回 Asset VO 或 Overview VO。
 * 详情接口同时接收 projectId 和 assetId，并在 application 层校验资产归属。</p>
 */
@RestController
@RequestMapping("/life/project/assets")
@RequiredArgsConstructor
public class LmAssetController {

    private final AssetService assetService;

    /** 查询资产概览，返回 total + list 的强类型对象。 */
    @PostMapping("/overview")
    public R<LmAssetOverviewVo> overview(@RequestBody ProjectScopedRequest request) {
        return R.ok(assetService.queryOverview(request.getProjectId()));
    }

    /** 查询资产详情，assetId 放在 JSON body 中，不再放在 URL path。 */
    @PostMapping("/detail")
    public R<LmAssetVo> detail(@RequestBody AssetDetailRequest request) {
        return R.ok(assetService.queryById(request.getProjectId(), request.getAssetId()));
    }
}

package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmAssetBo;
import org.dromara.life.domain.vo.LmAssetVo;
import org.dromara.life.service.ILmAssetService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 资产 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/assets")
public class LmAssetController extends BaseController {

    private final ILmAssetService assetService;

    /**
     * 获取资产概览（数量 + 列表）
     */
    @SaCheckPermission("life:asset:list")
    @GetMapping
    public R<Map<String, Object>> overview(@PathVariable Long projectId) {
        List<LmAssetVo> list = assetService.queryList(projectId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", list.size());
        result.put("list", list);
        return R.ok(result);
    }

    /**
     * 查询资产详情
     */
    @SaCheckPermission("life:asset:query")
    @GetMapping("/{assetId}")
    public R<LmAssetVo> getInfo(@PathVariable Long projectId,
                                @PathVariable Long assetId) {
        return R.ok(assetService.queryById(assetId));
    }

    /**
     * 新增资产
     */
    @SaCheckPermission("life:asset:add")
    @Log(title = "资产", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmAssetBo bo) {
        bo.setProjectId(projectId);
        assetService.insert(bo);
        return R.ok();
    }

    /**
     * 修改资产
     */
    @SaCheckPermission("life:asset:edit")
    @Log(title = "资产", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{assetId}")
    public R<Void> edit(@PathVariable Long projectId,
                        @PathVariable Long assetId,
                        @Validated @RequestBody LmAssetBo bo) {
        bo.setAssetId(assetId);
        bo.setProjectId(projectId);
        assetService.update(bo);
        return R.ok();
    }

    /**
     * 更新资产状态
     */
    @SaCheckPermission("life:asset:edit")
    @Log(title = "资产", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{assetId}/status")
    public R<Void> updateStatus(@PathVariable Long projectId,
                                @PathVariable Long assetId,
                                @RequestBody Map<String, String> body) {
        assetService.updateStatus(assetId, body.get("status"));
        return R.ok();
    }
}

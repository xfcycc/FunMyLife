package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmGameTargetBo;
import org.dromara.life.domain.vo.LmGameTargetVo;
import org.dromara.life.service.ILmGameTargetService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 游戏目标 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/targets")
public class LmGameTargetController extends BaseController {

    private final ILmGameTargetService gameTargetService;

    /**
     * 查询目标列表
     */
    @SaCheckPermission("life:target:list")
    @GetMapping
    public R<List<LmGameTargetVo>> list(@PathVariable Long projectId) {
        return R.ok(gameTargetService.queryList(projectId));
    }

    /**
     * 新增目标
     */
    @SaCheckPermission("life:target:add")
    @Log(title = "游戏目标", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmGameTargetBo bo) {
        bo.setProjectId(projectId);
        gameTargetService.insert(bo);
        return R.ok();
    }

    /**
     * 修改目标
     */
    @SaCheckPermission("life:target:edit")
    @Log(title = "游戏目标", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{targetId}")
    public R<Void> edit(@PathVariable Long projectId,
                        @PathVariable Long targetId,
                        @Validated @RequestBody LmGameTargetBo bo) {
        bo.setTargetId(targetId);
        bo.setProjectId(projectId);
        gameTargetService.update(bo);
        return R.ok();
    }

    /**
     * 更新目标状态
     */
    @SaCheckPermission("life:target:edit")
    @Log(title = "游戏目标", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{targetId}/status")
    public R<Void> updateStatus(@PathVariable Long projectId,
                                @PathVariable Long targetId,
                                @RequestBody Map<String, String> body) {
        gameTargetService.updateStatus(targetId, body.get("status"));
        return R.ok();
    }

    /**
     * 更新目标进度
     */
    @SaCheckPermission("life:target:edit")
    @Log(title = "游戏目标", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{targetId}/progress")
    public R<Void> updateProgress(@PathVariable Long projectId,
                                  @PathVariable Long targetId,
                                  @RequestBody Map<String, Integer> body) {
        gameTargetService.updateProgress(targetId, body.get("current"));
        return R.ok();
    }
}

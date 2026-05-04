package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmGameActivityBo;
import org.dromara.life.domain.vo.LmGameActivityVo;
import org.dromara.life.service.ILmGameActivityService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 游戏活动 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/game-activities")
public class LmGameActivityController extends BaseController {

    private final ILmGameActivityService gameActivityService;

    /**
     * 查询活动列表
     */
    @SaCheckPermission("life:activity:list")
    @GetMapping
    public R<List<LmGameActivityVo>> list(@PathVariable Long projectId) {
        return R.ok(gameActivityService.queryList(projectId));
    }

    /**
     * 新增活动
     */
    @SaCheckPermission("life:activity:add")
    @Log(title = "游戏活动", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmGameActivityBo bo) {
        bo.setProjectId(projectId);
        gameActivityService.insert(bo);
        return R.ok();
    }

    /**
     * 切换活动提醒状态
     */
    @SaCheckPermission("life:activity:edit")
    @Log(title = "游戏活动", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{activityId}/reminder")
    public R<Void> toggleReminder(@PathVariable Long projectId,
                                  @PathVariable Long activityId,
                                  @RequestBody java.util.Map<String, Boolean> body) {
        Boolean enabled = body.get("enabled");
        gameActivityService.toggleReminder(activityId, enabled != null && enabled);
        return R.ok();
    }
}

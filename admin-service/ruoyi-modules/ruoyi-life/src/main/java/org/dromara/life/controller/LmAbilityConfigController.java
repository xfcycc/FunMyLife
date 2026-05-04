package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmAbilityConfigBo;
import org.dromara.life.domain.vo.LmAbilityConfigVo;
import org.dromara.life.service.ILmAbilityConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 功能块实例配置 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/ability-configs")
public class LmAbilityConfigController extends BaseController {

    private final ILmAbilityConfigService abilityConfigService;

    /**
     * 查询功能块配置列表
     */
    @SaCheckPermission("life:config:list")
    @GetMapping
    public R<List<LmAbilityConfigVo>> list(@PathVariable Long projectId) {
        return R.ok(abilityConfigService.queryList(projectId));
    }

    /**
     * 批量保存功能块配置
     */
    @SaCheckPermission("life:config:edit")
    @Log(title = "功能块配置", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PutMapping
    public R<Void> batchSave(@PathVariable Long projectId,
                             @Validated @RequestBody List<LmAbilityConfigBo> boList) {
        abilityConfigService.batchSave(projectId, boList);
        return R.ok();
    }
}

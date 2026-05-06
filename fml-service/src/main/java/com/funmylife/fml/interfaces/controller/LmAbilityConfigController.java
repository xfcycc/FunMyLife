package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.AbilityConfigService;
import com.funmylife.fml.interfaces.request.AbilityConfigBatchSaveRequest;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmAbilityConfigVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能块实例配置接口。
 *
 * <p>功能块配置是 Life Manager 管理页的核心入口。这里的读取和保存都强制使用
 * POST + JSON body，保存时 request 内同时携带 projectId 与配置数组，避免把项目上下文放到 path。</p>
 */
@RestController
@RequestMapping("/life/project/ability-configs")
@RequiredArgsConstructor
public class LmAbilityConfigController {

    private final AbilityConfigService abilityConfigService;

    /** 查询项目下所有功能块实例配置，供详情页导航和管理页配置面板使用。 */
    @PostMapping("/list")
    public R<List<LmAbilityConfigVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(abilityConfigService.queryList(request.getProjectId()));
    }

    /** 批量保存功能块实例配置；application 层按 projectId + blockKey 执行 upsert。 */
    @PostMapping("/save-batch")
    public R<Void> batchSave(@RequestBody AbilityConfigBatchSaveRequest request) {
        abilityConfigService.batchSave(request.getProjectId(), request.getConfigs());
        return R.ok();
    }
}

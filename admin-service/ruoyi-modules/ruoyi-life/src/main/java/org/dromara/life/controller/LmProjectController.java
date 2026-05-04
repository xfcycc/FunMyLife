package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.vo.LmProjectVo;
import org.dromara.life.service.ILmProjectService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 项目 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}")
public class LmProjectController extends BaseController {

    private final ILmProjectService projectService;

    /**
     * 查询项目详情
     */
    @SaCheckPermission("life:project:query")
    @GetMapping
    public R<LmProjectVo> getInfo(@PathVariable Long projectId) {
        return R.ok(projectService.queryById(projectId));
    }
}

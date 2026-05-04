package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmGameVersionBo;
import org.dromara.life.domain.vo.LmGameVersionVo;
import org.dromara.life.service.ILmGameVersionService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 游戏版本 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/game-versions")
public class LmGameVersionController extends BaseController {

    private final ILmGameVersionService gameVersionService;

    /**
     * 查询版本列表
     */
    @SaCheckPermission("life:version:list")
    @GetMapping
    public R<List<LmGameVersionVo>> list(@PathVariable Long projectId) {
        return R.ok(gameVersionService.queryList(projectId));
    }

    /**
     * 查询当前活跃版本
     */
    @SaCheckPermission("life:version:query")
    @GetMapping("/current")
    public R<LmGameVersionVo> current(@PathVariable Long projectId) {
        return R.ok(gameVersionService.queryCurrent(projectId));
    }

    /**
     * 新增版本
     */
    @SaCheckPermission("life:version:add")
    @Log(title = "游戏版本", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmGameVersionBo bo) {
        bo.setProjectId(projectId);
        gameVersionService.insert(bo);
        return R.ok();
    }
}

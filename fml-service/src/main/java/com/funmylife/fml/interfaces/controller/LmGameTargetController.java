package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.GameTargetService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.request.TargetProgressUpdateRequest;
import com.funmylife.fml.interfaces.request.TargetStatusUpdateRequest;
import com.funmylife.fml.interfaces.vo.LmGameTargetVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 游戏目标接口。
 *
 * <p>目标列表、状态切换、进度更新都使用 POST。写操作不直接接收 Map，
 * 而是使用 TargetStatusUpdateRequest / TargetProgressUpdateRequest 让字段含义固定下来。</p>
 */
@RestController
@RequestMapping("/life/project/targets")
@RequiredArgsConstructor
public class LmGameTargetController {

    private final GameTargetService gameTargetService;

    /** 查询项目下所有目标任务。 */
    @PostMapping("/list")
    public R<List<LmGameTargetVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(gameTargetService.queryList(request.getProjectId()));
    }

    /** 更新目标状态，例如 todo -> done。 */
    @PostMapping("/status")
    public R<Void> updateStatus(@RequestBody TargetStatusUpdateRequest request) {
        gameTargetService.updateStatus(request.getProjectId(), request.getTargetId(), request.getStatus());
        return R.ok();
    }

    /** 更新目标当前进度值。 */
    @PostMapping("/progress")
    public R<Void> updateProgress(@RequestBody TargetProgressUpdateRequest request) {
        gameTargetService.updateProgress(request.getProjectId(), request.getTargetId(), request.getCurrent());
        return R.ok();
    }
}

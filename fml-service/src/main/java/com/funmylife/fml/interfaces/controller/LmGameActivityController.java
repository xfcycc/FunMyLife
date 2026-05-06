package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.GameActivityService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmGameActivityVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 游戏活动接口。
 *
 * <p>活动属于项目上下文，因此 projectId 从请求体进入。Controller 只做协议适配，
 * 查询和 VO 转换都交给 application 层。</p>
 */
@RestController
@RequestMapping("/life/project/game-activities")
@RequiredArgsConstructor
public class LmGameActivityController {

    private final GameActivityService gameActivityService;

    /** 查询项目下版本活动列表。 */
    @PostMapping("/list")
    public R<List<LmGameActivityVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(gameActivityService.queryList(request.getProjectId()));
    }
}

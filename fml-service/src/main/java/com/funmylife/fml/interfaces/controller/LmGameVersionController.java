package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.GameVersionService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmGameVersionVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 游戏版本接口。
 *
 * <p>版本列表和当前版本都是查询语义，但仍统一使用 POST，以符合 fml-service
 * “所有接口强制 POST + JSON body”的协议要求。</p>
 */
@RestController
@RequestMapping("/life/project/game-versions")
@RequiredArgsConstructor
public class LmGameVersionController {

    private final GameVersionService gameVersionService;

    /** 查询项目下所有游戏版本。 */
    @PostMapping("/list")
    public R<List<LmGameVersionVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(gameVersionService.queryList(request.getProjectId()));
    }

    /** 查询当前 active 版本，未找到时 data 为 null。 */
    @PostMapping("/current")
    public R<LmGameVersionVo> current(@RequestBody ProjectScopedRequest request) {
        return R.ok(gameVersionService.queryCurrent(request.getProjectId()));
    }
}

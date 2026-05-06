package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.ProjectService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmProjectVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 项目接口。
 *
 * <p>本服务约定所有接口都使用 POST + JSON body。projectId 不再出现在 URL path 中，
 * 避免同一套接口同时维护 path 参数和 body 参数两种入参来源。</p>
 */
@RestController
@RequestMapping("/life/project")
@RequiredArgsConstructor
public class LmProjectController {

    private final ProjectService projectService;

    /** 查询项目基础信息，返回给前端的对象必须是 VO，不能直接暴露持久化 Entity。 */
    @PostMapping("/detail")
    public R<LmProjectVo> getInfo(@RequestBody ProjectScopedRequest request) {
        return R.ok(projectService.queryById(request.getProjectId()));
    }
}

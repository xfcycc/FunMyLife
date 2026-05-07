package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.capability.CapabilityApplicationService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.CapabilityVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Life Manager 能力元数据接口。
 *
 * <p>该 Controller 只暴露系统支持的能力定义，帮助功能块实例配置页把“能力组成功能块”的模型
 * 显示出来。入参仍使用 JSON body，保持所有接口都是 POST 的约束。</p>
 */
@RestController
@RequestMapping("/life/project/capabilities")
@RequiredArgsConstructor
public class LmCapabilityController {

    private final CapabilityApplicationService capabilityApplicationService;

    /**
     * 查询系统已注册能力列表。
     *
     * @param request 项目范围请求；当前接口不依赖 projectId，但保留 JSON body 形态，便于后续按项目类型过滤
     * @return 能力元数据列表
     */
    @PostMapping("/list")
    public R<List<CapabilityVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(capabilityApplicationService.listCapabilities());
    }
}

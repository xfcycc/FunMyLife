package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.block.BlockInstanceApplicationService;
import com.funmylife.fml.interfaces.request.BlockInstanceBatchSaveRequest;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.BlockInstanceVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 功能块实例接口。
 *
 * <p>该接口直接承接“能力组成功能块”的新领域语义。前端读取的是功能块实例配置，
 * 每个实例通过 capabilities 字段引用一个或多个能力，再用规则字段决定这些能力的项目内行为。</p>
 */
@RestController
@RequestMapping("/life/project/block-instances")
@RequiredArgsConstructor
public class LmBlockInstanceController {

    private final BlockInstanceApplicationService blockInstanceApplicationService;

    /**
     * 查询项目下所有功能块实例。
     *
     * @param request 项目范围请求，projectId 必须放在 JSON body 中
     * @return 功能块实例 VO 列表
     */
    @PostMapping("/list")
    public R<List<BlockInstanceVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(blockInstanceApplicationService.queryList(request.getProjectId()));
    }

    /**
     * 批量保存项目下的功能块实例配置。
     *
     * @param request 批量保存请求，包含 projectId 和功能块实例数组
     * @return 保存成功时返回空响应体
     */
    @PostMapping("/save-batch")
    public R<Void> batchSave(@RequestBody BlockInstanceBatchSaveRequest request) {
        blockInstanceApplicationService.batchSave(request.getProjectId(), request.getBlockInstances());
        return R.ok();
    }
}

package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.vo.LmMaterialVo;
import org.dromara.life.service.ILmMaterialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 素材 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/materials")
public class LmMaterialController extends BaseController {

    private final ILmMaterialService materialService;

    /**
     * 获取素材概览（数量 + 列表）
     */
    @SaCheckPermission("life:material:list")
    @GetMapping
    public R<Map<String, Object>> overview(@PathVariable Long projectId) {
        List<LmMaterialVo> list = materialService.queryList(projectId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", list.size());
        result.put("list", list);
        return R.ok(result);
    }
}

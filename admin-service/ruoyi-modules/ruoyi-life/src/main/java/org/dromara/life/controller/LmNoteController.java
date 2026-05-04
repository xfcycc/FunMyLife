package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmNoteBo;
import org.dromara.life.domain.vo.LmNoteVo;
import org.dromara.life.service.ILmNoteService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 笔记 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/notes")
public class LmNoteController extends BaseController {

    private final ILmNoteService noteService;

    /**
     * 获取笔记概览（数量 + 列表）
     */
    @SaCheckPermission("life:note:list")
    @GetMapping
    public R<Map<String, Object>> overview(@PathVariable Long projectId) {
        List<LmNoteVo> list = noteService.queryList(projectId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", list.size());
        result.put("list", list);
        return R.ok(result);
    }

    /**
     * 查询笔记详情
     */
    @SaCheckPermission("life:note:query")
    @GetMapping("/{noteId}")
    public R<LmNoteVo> getInfo(@PathVariable Long projectId,
                               @PathVariable Long noteId) {
        return R.ok(noteService.queryById(noteId));
    }

    /**
     * 新增笔记
     */
    @SaCheckPermission("life:note:add")
    @Log(title = "笔记", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmNoteBo bo) {
        bo.setProjectId(projectId);
        noteService.insert(bo);
        return R.ok();
    }

    /**
     * 修改笔记
     */
    @SaCheckPermission("life:note:edit")
    @Log(title = "笔记", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/{noteId}")
    public R<Void> edit(@PathVariable Long projectId,
                        @PathVariable Long noteId,
                        @Validated @RequestBody LmNoteBo bo) {
        bo.setNoteId(noteId);
        bo.setProjectId(projectId);
        noteService.update(bo);
        return R.ok();
    }
}

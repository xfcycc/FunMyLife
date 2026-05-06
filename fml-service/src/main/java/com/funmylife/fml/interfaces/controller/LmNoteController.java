package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.NoteService;
import com.funmylife.fml.interfaces.request.NoteDetailRequest;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmNoteOverviewVo;
import com.funmylife.fml.interfaces.vo.LmNoteVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 笔记接口。
 *
 * <p>笔记列表和详情都通过 request body 传参。详情查询会在 application 层校验
 * noteId 是否属于传入的 projectId，避免跨项目读取。</p>
 */
@RestController
@RequestMapping("/life/project/notes")
@RequiredArgsConstructor
public class LmNoteController {

    private final NoteService noteService;

    /** 查询笔记概览，返回 total + list 的强类型对象。 */
    @PostMapping("/overview")
    public R<LmNoteOverviewVo> overview(@RequestBody ProjectScopedRequest request) {
        return R.ok(noteService.queryOverview(request.getProjectId()));
    }

    /** 查询单篇笔记详情，noteId 放在 JSON body 中。 */
    @PostMapping("/detail")
    public R<LmNoteVo> detail(@RequestBody NoteDetailRequest request) {
        return R.ok(noteService.queryById(request.getProjectId(), request.getNoteId()));
    }
}

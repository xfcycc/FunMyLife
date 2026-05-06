package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.GalleryService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmGalleryOverviewVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 图册接口。
 *
 * <p>图册概览包含 albums 和 photos 两组数据，使用 LmGalleryOverviewVo 明确表达结构，
 * 不再通过 Map 返回临时对象。</p>
 */
@RestController
@RequestMapping("/life/project/gallery")
@RequiredArgsConstructor
public class LmGalleryController {

    private final GalleryService galleryService;

    /** 查询项目图册概览。 */
    @PostMapping("/overview")
    public R<LmGalleryOverviewVo> overview(@RequestBody ProjectScopedRequest request) {
        return R.ok(galleryService.queryOverview(request.getProjectId()));
    }
}

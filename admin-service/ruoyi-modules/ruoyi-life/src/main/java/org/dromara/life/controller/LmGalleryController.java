package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmPhotoBo;
import org.dromara.life.domain.vo.LmAlbumVo;
import org.dromara.life.domain.vo.LmPhotoVo;
import org.dromara.life.service.ILmAlbumService;
import org.dromara.life.service.ILmPhotoService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 图册与照片 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/gallery")
public class LmGalleryController extends BaseController {

    private final ILmAlbumService albumService;
    private final ILmPhotoService photoService;

    /**
     * 获取图册概览（图册列表 + 照片列表）
     */
    @SaCheckPermission("life:gallery:list")
    @GetMapping
    public R<Map<String, Object>> overview(@PathVariable Long projectId) {
        List<LmAlbumVo> albums = albumService.queryList(projectId);
        List<LmPhotoVo> photos = photoService.queryList(projectId);
        Map<String, Object> result = new HashMap<>();
        result.put("albums", albums);
        result.put("photos", photos);
        result.put("photoCount", photos.size());
        return R.ok(result);
    }

    /**
     * 新增照片
     */
    @SaCheckPermission("life:gallery:add")
    @Log(title = "照片", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping("/photos")
    public R<Void> addPhoto(@PathVariable Long projectId,
                            @Validated @RequestBody LmPhotoBo bo) {
        bo.setProjectId(projectId);
        photoService.insert(bo);
        return R.ok();
    }

    /**
     * 修改照片
     */
    @SaCheckPermission("life:gallery:edit")
    @Log(title = "照片", businessType = BusinessType.UPDATE)
    @RepeatSubmit()
    @PatchMapping("/photos/{photoId}")
    public R<Void> editPhoto(@PathVariable Long projectId,
                             @PathVariable Long photoId,
                             @Validated @RequestBody LmPhotoBo bo) {
        bo.setPhotoId(photoId);
        bo.setProjectId(projectId);
        photoService.update(bo);
        return R.ok();
    }

    /**
     * 删除照片
     */
    @SaCheckPermission("life:gallery:remove")
    @Log(title = "照片", businessType = BusinessType.DELETE)
    @DeleteMapping("/photos/{photoId}")
    public R<Void> removePhoto(@PathVariable Long projectId,
                               @PathVariable Long photoId) {
        photoService.deleteWithValid(photoId);
        return R.ok();
    }
}

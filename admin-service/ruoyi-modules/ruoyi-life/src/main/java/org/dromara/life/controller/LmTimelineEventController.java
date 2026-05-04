package org.dromara.life.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.domain.R;
import org.dromara.common.idempotent.annotation.RepeatSubmit;
import org.dromara.common.log.annotation.Log;
import org.dromara.common.log.enums.BusinessType;
import org.dromara.common.web.core.BaseController;
import org.dromara.life.domain.bo.LmTimelineEventBo;
import org.dromara.life.domain.vo.LmTimelineEventVo;
import org.dromara.life.service.ILmTimelineEventService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 时间轴事件 信息操作处理
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/life/project/{projectId}/timeline-events")
public class LmTimelineEventController extends BaseController {

    private final ILmTimelineEventService timelineEventService;

    /**
     * 查询时间轴事件列表
     */
    @SaCheckPermission("life:timeline:list")
    @GetMapping
    public R<List<LmTimelineEventVo>> list(@PathVariable Long projectId) {
        return R.ok(timelineEventService.queryList(projectId));
    }

    /**
     * 新增时间轴事件
     */
    @SaCheckPermission("life:timeline:add")
    @Log(title = "时间轴事件", businessType = BusinessType.INSERT)
    @RepeatSubmit()
    @PostMapping
    public R<Void> add(@PathVariable Long projectId,
                       @Validated @RequestBody LmTimelineEventBo bo) {
        bo.setProjectId(projectId);
        timelineEventService.insert(bo);
        return R.ok();
    }
}

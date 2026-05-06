package com.funmylife.fml.interfaces.controller;

import com.funmylife.fml.application.TimelineEventService;
import com.funmylife.fml.interfaces.request.ProjectScopedRequest;
import com.funmylife.fml.interfaces.vo.LmTimelineEventVo;
import com.funmylife.fml.shared.core.R;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 时间轴事件接口。
 *
 * <p>时间轴是跨功能块的只读聚合视图。这里统一返回 LmTimelineEventVo，
 * 避免把数据库审计字段随 Entity 直接暴露给前端。</p>
 */
@RestController
@RequestMapping("/life/project/timeline-events")
@RequiredArgsConstructor
public class LmTimelineEventController {

    private final TimelineEventService timelineEventService;

    /** 查询项目时间轴事件列表，仓储实现按发生时间倒序返回。 */
    @PostMapping("/list")
    public R<List<LmTimelineEventVo>> list(@RequestBody ProjectScopedRequest request) {
        return R.ok(timelineEventService.queryList(request.getProjectId()));
    }
}

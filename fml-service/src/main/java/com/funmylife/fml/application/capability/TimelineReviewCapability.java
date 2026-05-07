package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmTimelineEvent;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 时间轴回顾能力。
 *
 * <p>该能力负责解释长期项目的事件沉淀和回顾数据，目前从 lm_timeline_event 读取时间轴事件。
 * 在概览场景下，它支持 source=timeline 的摘要规则，例如 sum-recent-timeline。</p>
 *
 * <p>暂不在这里创建新的时间轴事件；目标完成、照片上传和活动归档写入时间轴的逻辑后续应通过
 * TimelineContributor 继续迁移。</p>
 */
@Component
@RequiredArgsConstructor
public class TimelineReviewCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("timeline_review");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("时间轴回顾", "按天、周和版本回看长期项目事件。", List.of("summary", "timeline", "ai-context"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmTimelineEvent> timelineEvents = lifeDataRepository.findTimelineEvents(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "timeline"))
            .map(rule -> buildTimelineSummary(rule, timelineEvents))
            .toList();
    }

    private CapabilitySummary buildTimelineSummary(SummaryRule rule, List<LmTimelineEvent> timelineEvents) {
        List<LmTimelineEvent> visibleEvents = timelineEvents.stream()
            .filter(event -> isTrue(event.getDisplayInOverview()))
            .toList();

        CapabilitySummary summary = baseSummary(rule, "timeline");
        summary.setValue(visibleEvents.size() + "条");
        summary.setItems(visibleEvents.stream()
            .limit(maxItems(rule))
            .map(event -> item(String.valueOf(event.getEventId()), event.getTitle(), event.getType(), "timeline"))
            .toList());
        return summary;
    }

    private boolean isTrue(String value) {
        return "1".equals(value) || "Y".equals(value) || "true".equalsIgnoreCase(value);
    }
}

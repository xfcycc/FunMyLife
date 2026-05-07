package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmGameActivity;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 活动与倒计时能力。
 *
 * <p>该能力负责解释限时活动、版本活动和活动结束前提醒相关数据，目前从 lm_game_activity
 * 读取活动列表。在概览场景下，它支持 source=activities 的摘要规则，例如
 * sum-ending-activities。</p>
 *
 * <p>暂不在这里发送提醒，也不归档活动；提醒和归档后续分别通过 ReminderSupport、
 * ArchiveSupport 扩展。</p>
 */
@Component
@RequiredArgsConstructor
public class ActivityCountdownCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("activity_countdown");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("活动与倒计时", "读取限时活动并为概览提供即将结束活动摘要。", List.of("summary", "reminder", "archive"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmGameActivity> activities = lifeDataRepository.findGameActivities(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "activities"))
            .map(rule -> buildEndingActivitySummary(rule, activities))
            .toList();
    }

    private CapabilitySummary buildEndingActivitySummary(SummaryRule rule, List<LmGameActivity> activities) {
        Set<String> endingStatuses = Set.of("ending", "pending_archive");
        List<LmGameActivity> endingActivities = activities.stream()
            .filter(activity -> endingStatuses.contains(activity.getStatus()))
            .toList();

        CapabilitySummary summary = baseSummary(rule, "version_activity");
        summary.setValue(endingActivities.size() + "个");
        summary.setDescription("优先处理临近结束的活动目标");
        summary.setItems(endingActivities.stream()
            .limit(maxItems(rule))
            .map(activity -> item(String.valueOf(activity.getActivityId()), activity.getTitle(), activity.getStatus(), "version_activity"))
            .toList());
        return summary;
    }
}

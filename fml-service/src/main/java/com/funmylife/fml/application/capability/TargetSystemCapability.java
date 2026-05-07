package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmGameTarget;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 目标系统能力。
 *
 * <p>该能力负责解释项目中的目标数据，包括日常、周常、活动目标和自定义目标。目前从
 * lm_game_target 读取目标列表。在概览场景下，它支持 source=targets 的摘要规则，
 * 例如 sum-today-targets 和 sum-weekly-targets。</p>
 *
 * <p>暂不在这里直接保存目标状态，也不写时间轴；目标更新仍由 GameTargetService 处理，
 * 时间轴写入后续应通过 TimelineContributor 扩展。</p>
 */
@Component
@RequiredArgsConstructor
public class TargetSystemCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("target_system");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("目标系统", "管理日常、周常、活动目标和自定义目标。", List.of("summary", "timeline"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmGameTarget> targets = lifeDataRepository.findGameTargets(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "targets"))
            .map(rule -> buildTargetSummary(rule, targets))
            .toList();
    }

    private CapabilitySummary buildTargetSummary(SummaryRule rule, List<LmGameTarget> targets) {
        String targetType = "sum-weekly-targets".equals(rule.getId()) ? "weekly" : "daily";
        List<LmGameTarget> filteredTargets = targets.stream()
            .filter(target -> targetType.equals(target.getType()))
            .toList();
        long doneCount = filteredTargets.stream().filter(target -> "done".equals(target.getStatus())).count();

        CapabilitySummary summary = baseSummary(rule, "targets");
        summary.setValue(doneCount + "/" + filteredTargets.size());
        if ("daily".equals(targetType)) {
            summary.setDescription("今日还有 " + (filteredTargets.size() - doneCount) + " 项待完成");
        }
        summary.setItems(filteredTargets.stream()
            .filter(target -> !"done".equals(target.getStatus()))
            .limit(maxItems(rule))
            .map(target -> item(String.valueOf(target.getTargetId()), target.getTitle(), target.getStatus(), "targets"))
            .toList());
        return summary;
    }
}

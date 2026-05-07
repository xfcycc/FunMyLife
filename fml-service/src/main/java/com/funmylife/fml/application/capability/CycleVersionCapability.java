package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmGameVersion;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 周期与版本能力。
 *
 * <p>该能力负责解释长期游戏项目里的版本周期数据，目前从 lm_game_version 读取当前 active
 * 版本。在概览场景下，它支持 source=version 的摘要规则，例如 sum-current-version。</p>
 *
 * <p>暂不在这里处理版本归档、版本切换和跨版本统计；这些行为后续应通过 ArchiveSupport
 * 或专门的版本应用服务继续扩展。</p>
 */
@Component
@RequiredArgsConstructor
public class CycleVersionCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("cycle_version");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("周期与版本", "读取游戏版本周期并为概览提供当前版本摘要。", List.of("summary"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "version"))
            .map(rule -> buildCurrentVersionSummary(context.getProjectId(), rule))
            .toList();
    }

    private CapabilitySummary buildCurrentVersionSummary(Long projectId, SummaryRule rule) {
        LmGameVersion version = lifeDataRepository.findCurrentGameVersion(projectId);

        CapabilitySummary summary = baseSummary(rule, "version_activity");
        summary.setValue(version == null ? "暂无当前版本" : version.getName());
        if (version != null) {
            summary.setDescription(version.getTitle());
        }
        return summary;
    }
}

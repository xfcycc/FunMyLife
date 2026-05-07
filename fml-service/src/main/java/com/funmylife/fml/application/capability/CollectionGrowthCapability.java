package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmMaterial;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 收集与养成能力。
 *
 * <p>该能力负责解释素材、套装、代币和收集项进度，目前从 lm_material 读取素材收集记录。
 * 在概览场景下，它支持 source=materials 的摘要规则，例如 sum-material-progress。</p>
 *
 * <p>暂不在这里处理素材来源规划、套装合成或跨活动统计；这些属于后续更细的素材能力配置。</p>
 */
@Component
@RequiredArgsConstructor
public class CollectionGrowthCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("collection_growth");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("收集与养成", "管理素材、套装、代币和收集项进度。", List.of("summary", "timeline"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmMaterial> materials = lifeDataRepository.findMaterials(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "materials"))
            .map(rule -> buildMaterialSummary(rule, materials))
            .toList();
    }

    private CapabilitySummary buildMaterialSummary(SummaryRule rule, List<LmMaterial> materials) {
        List<LmMaterial> activeMaterials = materials.stream()
            .filter(material -> !"archived".equals(material.getStatus()))
            .toList();
        long completedCount = activeMaterials.stream().filter(material -> "completed".equals(material.getStatus())).count();

        CapabilitySummary summary = baseSummary(rule, "targets");
        summary.setValue(completedCount + "/" + activeMaterials.size());
        summary.setDescription("套装、素材和代币收集进度");
        summary.setItems(activeMaterials.stream()
            .filter(material -> !"completed".equals(material.getStatus()))
            .limit(maxItems(rule))
            .map(material -> item(String.valueOf(material.getMaterialId()), material.getName(), material.getStatus(), "targets"))
            .toList());
        return summary;
    }
}

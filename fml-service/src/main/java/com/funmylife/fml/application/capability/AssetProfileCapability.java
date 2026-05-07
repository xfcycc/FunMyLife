package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmAsset;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

/**
 * 资料资产能力。
 *
 * <p>该能力负责解释账号、UID、兑换码、支付凭证和外部链接等资产资料，目前从 lm_asset
 * 读取资产列表。在概览场景下，它支持 source=assets 的摘要规则，例如 sum-asset-risk。</p>
 *
 * <p>暂不在这里返回敏感字段明文，也不执行外部写入；资产脱敏和外部写入确认由 security
 * 规则和后续专门应用服务负责。</p>
 */
@Component
@RequiredArgsConstructor
public class AssetProfileCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("asset_profile");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("资料资产", "保存账号、UID、兑换码和外部链接，并为概览提供资产风险摘要。", List.of("summary", "timeline", "security"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmAsset> assets = lifeDataRepository.findAssets(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "assets"))
            .map(rule -> buildAssetSummary(rule, assets))
            .toList();
    }

    private CapabilitySummary buildAssetSummary(SummaryRule rule, List<LmAsset> assets) {
        Set<String> riskStatuses = Set.of("pending", "expired");
        List<LmAsset> riskAssets = assets.stream()
            .filter(asset -> riskStatuses.contains(asset.getStatus()))
            .toList();

        CapabilitySummary summary = baseSummary(rule, "assets");
        summary.setValue(riskAssets.isEmpty() ? "正常" : riskAssets.size() + "个");
        summary.setDescription(riskAssets.isEmpty() ? "账号资产暂无异常" : "存在待处理或过期资产");
        summary.setItems(riskAssets.stream()
            .limit(maxItems(rule))
            .map(asset -> item(String.valueOf(asset.getAssetId()), asset.getName(), asset.getStatus(), "assets"))
            .toList());
        return summary;
    }
}

package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.CapabilityMetadata;
import com.funmylife.fml.domain.capability.CapabilitySummary;
import com.funmylife.fml.domain.capability.CapabilitySummaryItem;
import com.funmylife.fml.domain.rule.SummaryRule;

import java.util.List;

/**
 * 概览摘要能力的基础支持类。
 *
 * <p>多个能力都会把 SummaryRule 转成 CapabilitySummary，并且都需要创建摘要明细、处理
 * maxItems、填写 targetRoute。这里仅收敛这些纯对象构造逻辑，不放业务分派规则，避免每个
 * 能力类重复样板代码。</p>
 */
abstract class AbstractSummaryCapability {

    /**
     * 构建能力元数据。
     *
     * @param name 能力名称
     * @param description 能力说明
     * @param behaviors 能力提供的行为扩展点
     * @return 能力元数据对象
     */
    protected CapabilityMetadata metadata(String name, String description, List<String> behaviors) {
        CapabilityMetadata metadata = new CapabilityMetadata();
        metadata.setName(name);
        metadata.setDescription(description);
        metadata.setSupportedProjectTypes(List.of("game"));
        metadata.setProvidedBehaviors(behaviors);
        return metadata;
    }

    /**
     * 判断摘要规则是否启用且来源匹配当前能力。
     *
     * @param rule 摘要规则
     * @param source 当前能力负责解释的规则来源
     * @return true 表示该能力应该消费这条规则
     */
    protected boolean acceptsSource(SummaryRule rule, String source) {
        return rule != null && rule.isEnabled() && source.equals(rule.getSource());
    }

    /**
     * 创建摘要基础对象。
     *
     * @param rule 摘要规则
     * @param targetRoute 前端点击后进入的功能块 key
     * @return 已填好公共字段的领域摘要
     */
    protected CapabilitySummary baseSummary(SummaryRule rule, String targetRoute) {
        CapabilitySummary summary = new CapabilitySummary();
        summary.setId("overview-" + rule.getId());
        summary.setRuleId(rule.getId());
        summary.setTitle(rule.getTitle());
        summary.setTargetRoute(rule.getTargetTab() == null ? targetRoute : rule.getTargetTab());
        summary.setPriority(rule.getPriority());
        return summary;
    }

    /**
     * 创建摘要明细项。
     *
     * @param id 明细 ID
     * @param label 展示文案
     * @param status 状态
     * @param targetRoute 点击后进入的功能块 key
     * @return 领域摘要明细
     */
    protected CapabilitySummaryItem item(String id, String label, String status, String targetRoute) {
        CapabilitySummaryItem item = new CapabilitySummaryItem();
        item.setId(id);
        item.setLabel(label == null ? "" : label);
        item.setStatus(status);
        item.setTargetRoute(targetRoute);
        return item;
    }

    /**
     * 读取规则中的最大展示条数。
     *
     * @param rule 摘要规则
     * @return 大于 0 的 maxItems；配置异常时使用 3
     */
    protected int maxItems(SummaryRule rule) {
        return rule.getMaxItems() > 0 ? rule.getMaxItems() : 3;
    }
}

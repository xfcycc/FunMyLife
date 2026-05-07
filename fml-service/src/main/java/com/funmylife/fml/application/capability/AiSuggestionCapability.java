package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.CapabilityKey;
import com.funmylife.fml.domain.capability.CapabilityMetadata;
import com.funmylife.fml.domain.capability.LifeCapability;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * AI 总结建议能力。
 *
 * <p>该能力表示项目可以把摘要、时间轴和用户确认后的候选内容交给 AI 生成建议。当前阶段它主要
 * 暴露为能力元数据，并由现有 AiService 继续提供 AI 概览接口。</p>
 *
 * <p>暂不在这里直接写入用户数据；所有 AI 写入仍应遵守 aiRules 和 security 配置中的确认策略。</p>
 */
@Component
public class AiSuggestionCapability extends AbstractSummaryCapability implements LifeCapability {

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("ai_suggestion");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("AI 总结建议", "基于摘要和时间轴生成建议，关键写入需要用户确认。", List.of("ai-context", "suggestion"));
    }
}

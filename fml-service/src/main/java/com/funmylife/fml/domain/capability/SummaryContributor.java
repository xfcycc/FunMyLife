package com.funmylife.fml.domain.capability;

import java.util.List;

/**
 * 可贡献概览摘要的能力扩展点。
 *
 * <p>OverviewService 不应该知道每个业务能力的摘要生成细节。它只负责加载项目上下文、
 * 功能块实例和摘要规则，然后把上下文交给实现了 SummaryContributor 的能力。</p>
 */
public interface SummaryContributor {

    /**
     * 根据功能块实例中的摘要规则生成概览卡片。
     *
     * @param context 当前摘要构建上下文，包含项目、功能块实例和规则
     * @return 当前能力贡献的领域摘要列表；没有可展示内容时返回空列表，不返回 null
     */
    List<CapabilitySummary> buildSummaries(SummaryBuildContext context);
}

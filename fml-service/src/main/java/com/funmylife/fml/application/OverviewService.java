package com.funmylife.fml.application;

import com.funmylife.fml.domain.block.BlockInstance;
import com.funmylife.fml.domain.block.CapabilityRef;
import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import com.funmylife.fml.interfaces.vo.LmOverviewSummaryItemVo;
import com.funmylife.fml.interfaces.vo.LmOverviewSummaryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 概览摘要应用服务。
 *
 * <p>该服务只负责用例编排：读取 overview 功能块实例、读取项目内启用的功能块实例、
 * 通过 CapabilityRegistry 找到能力实现，再把能力产出的领域摘要转换成前端 VO。</p>
 *
 * <p>具体业务摘要不再写在这里的 ruleId switch 中，而是拆到 TargetSystemCapability、
 * MediaRecordCapability、AssetProfileCapability 等能力类。这样“方案由功能块组成，
 * 功能块由能力组成”的模型会体现在 Java 接口和实现类上，而不是只体现在配置表字段里。</p>
 */
@Service
@RequiredArgsConstructor
public class OverviewService {

    private final LifeDataRepository lifeDataRepository;
    private final CapabilityRegistry capabilityRegistry;

    /**
     * 获取项目概览摘要列表。
     *
     * <p>流程：
     * 1. 读取 blockKey=overview 的功能块实例，拿到 summaryRules。
     * 2. 读取项目下所有启用的功能块实例，逐个解析其 capabilities。
     * 3. 通过 CapabilityRegistry 找到能力实现；如果能力实现 SummaryContributor，就交给它生成摘要。
     * 4. 汇总能力产出的领域摘要，按 SummaryRule.priority 排序后转换为 VO。</p>
     *
     * @param projectId 项目 ID
     * @return 概览摘要 VO 列表；没有配置或没有启用规则时返回空列表
     */
    public List<LmOverviewSummaryVo> getOverviewSummaries(Long projectId) {
        BlockInstance overviewBlock = lifeDataRepository.findBlockInstance(projectId, "overview");
        if (overviewBlock == null || !overviewBlock.isEnabled()) {
            return List.of();
        }

        List<SummaryRule> rules = enabledRules(overviewBlock.getSummaryRules());
        if (rules.isEmpty()) {
            return List.of();
        }

        List<CapabilitySummary> summaries = new ArrayList<>();
        Set<String> handledCapabilityKeys = new HashSet<>();
        for (BlockInstance blockInstance : enabledBlockInstances(projectId)) {
            summaries.addAll(buildSummariesByBlockCapability(projectId, blockInstance, rules, handledCapabilityKeys));
        }

        return summaries.stream()
            .sorted(Comparator.comparingInt(CapabilitySummary::getPriority))
            .map(this::toVo)
            .toList();
    }

    /**
     * 根据一个功能块实例引用的能力生成摘要。
     *
     * <p>同一个能力可能被多个功能块引用，例如 timeline_review 会被任务、图册、AI 功能块复用。
     * 概览只需要每个能力贡献一次摘要，所以这里会用 handledCapabilityKeys 去重。</p>
     */
    private List<CapabilitySummary> buildSummariesByBlockCapability(Long projectId,
                                                                    BlockInstance blockInstance,
                                                                    List<SummaryRule> rules,
                                                                    Set<String> handledCapabilityKeys) {
        List<CapabilitySummary> summaries = new ArrayList<>();
        for (CapabilityRef capabilityRef : blockInstance.getCapabilities()) {
            if (!isUsable(capabilityRef) || !handledCapabilityKeys.add(capabilityRef.getCapabilityKey().asString())) {
                continue;
            }

            capabilityRegistry.find(capabilityRef.getCapabilityKey())
                .filter(capability -> capability.supports(blockInstance))
                .filter(SummaryContributor.class::isInstance)
                .map(SummaryContributor.class::cast)
                .ifPresent(contributor -> summaries.addAll(contributor.buildSummaries(context(projectId, blockInstance, rules))));
        }
        return summaries;
    }

    /**
     * 查询启用的功能块实例。
     *
     * @param projectId 项目 ID
     * @return 项目内启用的功能块实例列表
     */
    private List<BlockInstance> enabledBlockInstances(Long projectId) {
        return lifeDataRepository.findBlockInstances(projectId).stream()
            .filter(BlockInstance::isEnabled)
            .toList();
    }

    /**
     * 过滤启用的摘要规则。
     *
     * @param rules 功能块实例上的摘要规则列表
     * @return 启用状态的规则列表
     */
    private List<SummaryRule> enabledRules(List<SummaryRule> rules) {
        if (rules == null || rules.isEmpty()) {
            return List.of();
        }
        return rules.stream().filter(SummaryRule::isEnabled).toList();
    }

    /**
     * 创建能力摘要构建上下文。
     *
     * @param projectId 项目 ID
     * @param blockInstance 当前功能块实例
     * @param rules overview 功能块上的摘要规则
     * @return 概览摘要构建上下文
     */
    private SummaryBuildContext context(Long projectId, BlockInstance blockInstance, List<SummaryRule> rules) {
        SummaryBuildContext context = new SummaryBuildContext();
        context.setProjectId(projectId);
        context.setBlockInstance(blockInstance);
        context.setRules(rules);
        return context;
    }

    /**
     * 判断功能块能力引用是否可参与本次概览构建。
     *
     * @param capabilityRef 功能块中的能力引用
     * @return true 表示能力 key 有效且引用已启用
     */
    private boolean isUsable(CapabilityRef capabilityRef) {
        return capabilityRef != null && capabilityRef.isEnabled() && capabilityRef.getCapabilityKey() != null;
    }

    /**
     * 把领域摘要转换为 Controller 可返回的 VO。
     *
     * @param summary 能力贡献的领域摘要
     * @return 前端概览摘要 VO
     */
    private LmOverviewSummaryVo toVo(CapabilitySummary summary) {
        LmOverviewSummaryVo vo = new LmOverviewSummaryVo();
        vo.setId(summary.getId());
        vo.setRuleId(summary.getRuleId());
        vo.setTitle(summary.getTitle());
        vo.setValue(summary.getValue());
        vo.setDescription(summary.getDescription());
        vo.setTargetRoute(summary.getTargetRoute());
        if (summary.getItems() != null && !summary.getItems().isEmpty()) {
            vo.setItems(summary.getItems().stream().map(this::toItemVo).toList());
        }
        return vo;
    }

    /**
     * 把领域摘要明细转换为 VO。
     *
     * @param item 能力贡献的摘要明细
     * @return 前端摘要明细 VO
     */
    private LmOverviewSummaryItemVo toItemVo(CapabilitySummaryItem item) {
        LmOverviewSummaryItemVo vo = new LmOverviewSummaryItemVo();
        vo.setId(item.getId());
        vo.setLabel(item.getLabel());
        vo.setStatus(item.getStatus());
        vo.setTargetRoute(item.getTargetRoute());
        return vo;
    }
}

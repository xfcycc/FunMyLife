package com.funmylife.fml.domain.block;

import com.funmylife.fml.domain.rule.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 功能块实例领域对象。
 *
 * <p>功能块实例表示某个项目中实际启用和配置的功能入口，例如无限暖暖项目里的
 * “任务”“图册”“账号资产”。它不是能力本身，而是能力在项目内的组合和呈现方式。</p>
 *
 * <p>当前版本从 lm_block_instance_config 表读取数据，领域语义为 BlockInstance。</p>
 */
@Data
public class BlockInstance {

    /** 功能块实例 ID；对应 lm_block_instance_config.config_id。 */
    private Long blockInstanceId;

    /** 所属项目 ID。 */
    private Long projectId;

    /** 功能块 key，例如 overview、targets、gallery；同一项目内应唯一。 */
    private BlockKey blockKey;

    /** 当前项目内展示给用户看的功能块名称。 */
    private String displayName;

    /** 当前功能块实例是否启用；禁用后不应贡献导航、摘要、时间轴或 AI 上下文。 */
    private boolean enabled;

    /** 功能块导航配置；隐藏但启用的功能块仍可贡献摘要或时间轴。 */
    private BlockNavigation navigation;

    /** 当前功能块实例引用的能力列表，这是“能力组成功能块”的核心关系。 */
    private List<CapabilityRef> capabilities = new ArrayList<>();

    /** 功能块字段配置原始 JSON；后续可按能力逐步类型化。 */
    private String fieldsRaw;

    /** 功能块行为配置原始 JSON；包含 resetRules、reminderRules、archiveRule 等组合配置。 */
    private String behaviorRaw;

    /** 概览摘要规则列表，由实现 SummaryContributor 的能力消费。 */
    private List<SummaryRule> summaryRules = new ArrayList<>();

    /** 时间轴写入规则列表，由实现 TimelineContributor 的能力消费。 */
    private List<TimelineRule> timelineRules = new ArrayList<>();

    /** 归档规则，决定功能块关联数据如何在阶段结束时收束。 */
    private ArchiveRule archiveRule;

    /** AI 读写规则，决定当前功能块是否能进入 AI 上下文。 */
    private AiRule aiRule;

    /** 安全规则，决定概览脱敏、AI 读取和外部写入确认策略。 */
    private SecurityRule securityRule;

    /** 备注。 */
    private String remark;

    /** capabilities 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String capabilitiesRaw;

    /** navigation 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String navigationRaw;

    /** summaryRules 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String summaryRulesRaw;

    /** timeline 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String timelineRaw;

    /** aiRules 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String aiRulesRaw;

    /** security 原始 JSON，用于第一阶段保持接口返回和保存格式稳定。 */
    private String securityRaw;

    /** 创建人 ID，沿用旧表审计列，只用于接口展示和排查。 */
    private Long createBy;

    /** 创建时间，沿用旧表审计列，只用于接口展示和排查。 */
    private Date createTime;
}

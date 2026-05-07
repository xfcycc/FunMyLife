package com.funmylife.fml.domain.rule;

import lombok.Data;

/**
 * 归档规则。
 *
 * <p>归档规则描述版本、活动、旅行阶段或资料条目在结束后如何收束。
 * 它只表达策略，不直接执行数据库更新。</p>
 */
@Data
public class ArchiveRule {

    /** 归档触发条件，例如 activity_end、version_end、manual。 */
    private String trigger;

    /** 归档影响范围，例如 current_block、linked_targets、whole_project。 */
    private String scope;

    /** 归档后是否保留摘要。 */
    private boolean keepSummary;

    /** 规则来源，用于后续方案升级合并。 */
    private RuleSource ruleSource = RuleSource.SCHEME_DEFAULT;

    /** 原始 JSON 字符串，第一阶段用于保持配置可追踪。 */
    private String rawJson;
}

package com.funmylife.fml.domain.rule;

import lombok.Data;

/**
 * 提醒规则。
 *
 * <p>提醒规则用于表达某个功能块实例希望产生哪些提醒候选。
 * 它不直接发送通知，真正的通知渠道由后续提醒应用服务统一处理。</p>
 */
@Data
public class ReminderRule {

    /** 提醒规则 ID。 */
    private String id;

    /** 提醒触发条件，例如 due_soon、daily_reset、activity_ending。 */
    private String trigger;

    /** 提前提醒的小时数。 */
    private Integer beforeHours;

    /** 规则来源，用于区分方案默认和用户覆盖。 */
    private RuleSource ruleSource = RuleSource.SCHEME_DEFAULT;

    /** 原始 JSON 字符串，第一阶段用于保持配置可追踪。 */
    private String rawJson;
}

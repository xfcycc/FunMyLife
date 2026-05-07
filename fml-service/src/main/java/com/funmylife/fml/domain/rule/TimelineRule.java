package com.funmylife.fml.domain.rule;

import lombok.Data;

/**
 * 时间轴写入规则。
 *
 * <p>时间轴规则决定某个能力动作发生后是否生成时间轴草稿，以及生成后的展示和 AI 可读边界。
 * 该规则不负责保存事件，保存动作必须由应用服务统一完成。</p>
 */
@Data
public class TimelineRule {

    /** 规则稳定 ID。 */
    private String id;

    /** 规则所属功能块 key。 */
    private String sourceBlockKey;

    /** 触发规则的业务事件，例如 target_done、photo_uploaded。 */
    private String event;

    /** 写入模式，例如 detail、daily_summary、exception_only。 */
    private String writeMode;

    /** 是否允许展示在概览中。 */
    private boolean displayInOverview;

    /** 是否允许进入 AI 可读上下文。 */
    private boolean aiReadable;

    /** 是否需要用户确认后才能写入。 */
    private boolean requireConfirm;

    /** 规则来源，用于区分方案默认和用户覆盖。 */
    private RuleSource ruleSource = RuleSource.SCHEME_DEFAULT;

    /** 原始 JSON 字符串，第一阶段用于保持配置可追踪。 */
    private String rawJson;
}

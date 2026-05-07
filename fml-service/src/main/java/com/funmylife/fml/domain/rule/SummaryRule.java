package com.funmylife.fml.domain.rule;

import lombok.Data;

/**
 * 概览摘要规则。
 *
 * <p>规则本身只描述“什么内容应该出现在概览中”，不直接读取数据库。
 * 真正的数据读取和摘要构建应由实现 SummaryContributor 的能力完成。</p>
 */
@Data
public class SummaryRule {

    /** 摘要规则稳定 ID，例如 sum-today-targets。 */
    private String id;

    /** 规则来源功能或业务域，例如 targets、activities、gallery。 */
    private String source;

    /** 是否启用这条规则。 */
    private boolean enabled = true;

    /** 概览卡片标题。 */
    private String title;

    /** 概览最多展示多少条明细。 */
    private int maxItems = 3;

    /** 概览排序优先级，数值越小越靠前。 */
    private int priority = 99;

    /** 过滤条件原始 JSON；第一阶段用于保留复杂过滤表达式，后续可按能力细化为专属条件类。 */
    private String filtersRaw;

    /** 展示模式，例如 metric、list、compact、timeline。 */
    private String displayMode;

    /** 前端点击摘要后应进入的功能块 key。 */
    private String targetTab;

    /** 规则来源，用于后续方案升级和用户覆盖合并。 */
    private RuleSource ruleSource = RuleSource.SCHEME_DEFAULT;

    /** 原始 JSON 字符串，用于排查配置问题和第一阶段保持读写兼容。 */
    private String rawJson;
}

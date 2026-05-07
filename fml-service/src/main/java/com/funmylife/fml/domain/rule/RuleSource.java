package com.funmylife.fml.domain.rule;

/**
 * 规则来源。
 *
 * <p>同一条摘要、时间轴、归档或安全规则可能来自方案默认值，也可能来自用户在项目管理页的覆盖。
 * 记录来源是后续做方案升级合并的前置条件。</p>
 */
public enum RuleSource {

    /** 来自方案模板默认配置。 */
    SCHEME_DEFAULT,

    /** 用户在项目内修改过的配置。 */
    USER_OVERRIDE,

    /** AI 生成的建议，可能还需要用户确认。 */
    AI_SUGGESTED,

    /** 从外部文件或第三方渠道导入的配置。 */
    IMPORTED
}

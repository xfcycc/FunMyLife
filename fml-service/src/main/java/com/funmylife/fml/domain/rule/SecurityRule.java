package com.funmylife.fml.domain.rule;

import lombok.Data;

/**
 * 功能块安全规则。
 *
 * <p>安全规则定义功能块实例内数据的敏感级别，以及在概览、AI 和外部写入场景下的保护策略。</p>
 */
@Data
public class SecurityRule {

    /** 敏感级别，例如 normal、private、sensitive。 */
    private String sensitivity = "normal";

    /** 是否在概览中脱敏展示。 */
    private boolean maskInOverview;

    /** 是否要求外部渠道写入前必须确认。 */
    private boolean requireConfirmBeforeExternalWrite = true;

    /** 原始 JSON 字符串，第一阶段用于保持配置可追踪。 */
    private String rawJson;
}

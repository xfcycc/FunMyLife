package com.funmylife.fml.domain.rule;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * AI 读写规则。
 *
 * <p>AI 规则决定某个功能块实例是否允许进入 AI 上下文，以及 AI 建议是否必须经过用户确认。
 * 能力实现不能绕过该规则直接暴露敏感数据。</p>
 */
@Data
public class AiRule {

    /** 当前功能块实例的数据是否允许被 AI 读取。 */
    private boolean readable;

    /** AI 写入或修改建议是否必须经过用户确认。 */
    private boolean writableAfterConfirm = true;

    /** 允许 AI 使用的场景，例如 summary、suggestion、review。 */
    private List<String> allowedUse = new ArrayList<>();

    /** 原始 JSON 字符串，第一阶段用于保持配置可追踪。 */
    private String rawJson;
}

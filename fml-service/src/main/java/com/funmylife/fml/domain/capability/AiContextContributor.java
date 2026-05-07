package com.funmylife.fml.domain.capability;

/**
 * 可贡献 AI 上下文的能力扩展点。
 *
 * <p>能力实现必须遵守功能块实例上的 AiRule 和 SecurityRule。涉及敏感数据时，
 * 不允许绕过安全规则直接暴露给 AI 上下文。</p>
 */
public interface AiContextContributor {
}

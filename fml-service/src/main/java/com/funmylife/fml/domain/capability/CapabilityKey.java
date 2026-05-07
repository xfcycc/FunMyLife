package com.funmylife.fml.domain.capability;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 能力唯一标识。
 *
 * <p>能力 key 用来连接“功能块实例配置”和“Java 能力实现类”。例如功能块实例
 * targets 可以引用 target-system 能力，概览功能块可以引用 summary-reminder 能力。</p>
 *
 * <p>这里使用值对象而不是裸 String，是为了让领域层明确区分 blockKey 和 capabilityKey，
 * 避免后续又把“功能块”和“能力”混成同一个概念。</p>
 */
@Getter
@EqualsAndHashCode
public class CapabilityKey {

    /** 能力稳定 key，例如 target-system、activity-management、media-record。 */
    private final String value;

    private CapabilityKey(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("capabilityKey must not be blank");
        }
        this.value = value;
    }

    /**
     * 根据字符串创建能力 key。
     *
     * @param value 能力稳定标识
     * @return 能力 key 值对象
     */
    public static CapabilityKey of(String value) {
        return new CapabilityKey(value);
    }

    /**
     * 返回可持久化和可序列化的字符串值。
     *
     * @return 能力 key 字符串
     */
    public String asString() {
        return value;
    }
}

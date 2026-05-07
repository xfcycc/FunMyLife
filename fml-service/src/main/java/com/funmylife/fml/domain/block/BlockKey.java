package com.funmylife.fml.domain.block;

import lombok.EqualsAndHashCode;
import lombok.Getter;

/**
 * 功能块实例 key。
 *
 * <p>BlockKey 表示用户在项目中看到或配置的功能块入口，例如 overview、targets、
 * version_activity、gallery。它不是能力 key；一个功能块可以通过 CapabilityRef 引用多个能力。</p>
 */
@Getter
@EqualsAndHashCode
public class BlockKey {

    /** 功能块稳定 key，同一项目内应保持唯一。 */
    private final String value;

    private BlockKey(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("blockKey must not be blank");
        }
        this.value = value;
    }

    /**
     * 根据字符串创建功能块 key。
     *
     * @param value 功能块稳定标识
     * @return 功能块 key 值对象
     */
    public static BlockKey of(String value) {
        return new BlockKey(value);
    }

    /**
     * 返回可持久化和可序列化的字符串值。
     *
     * @return 功能块 key 字符串
     */
    public String asString() {
        return value;
    }
}

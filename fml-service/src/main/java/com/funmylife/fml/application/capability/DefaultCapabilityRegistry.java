package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.CapabilityKey;
import com.funmylife.fml.domain.capability.CapabilityRegistry;
import com.funmylife.fml.domain.capability.LifeCapability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Spring 环境下的能力注册表实现。
 *
 * <p>所有实现 LifeCapability 的能力类都会被 Spring 注入到这里，并按 CapabilityKey 建立索引。
 * application 层后续只通过 CapabilityRegistry 查找能力，不直接 new 某个具体能力类。</p>
 *
 * <p>这里使用 Map 只是 Java 进程内的查找结构，不属于 Controller 入参或出参 JSON 契约。</p>
 */
@Component
public class DefaultCapabilityRegistry implements CapabilityRegistry {

    private final Map<CapabilityKey, LifeCapability> capabilities;

    /**
     * 创建能力注册表。
     *
     * @param capabilityList Spring 容器中全部能力实现
     */
    public DefaultCapabilityRegistry(List<LifeCapability> capabilityList) {
        this.capabilities = capabilityList.stream()
            .collect(Collectors.toUnmodifiableMap(LifeCapability::key, Function.identity()));
    }

    @Override
    public Optional<LifeCapability> find(CapabilityKey capabilityKey) {
        return Optional.ofNullable(capabilities.get(capabilityKey));
    }

    @Override
    public List<LifeCapability> list() {
        return capabilities.values().stream().toList();
    }
}

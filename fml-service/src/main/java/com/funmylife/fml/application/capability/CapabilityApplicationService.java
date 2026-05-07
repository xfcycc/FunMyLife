package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.CapabilityMetadata;
import com.funmylife.fml.domain.capability.CapabilityRegistry;
import com.funmylife.fml.domain.capability.LifeCapability;
import com.funmylife.fml.interfaces.vo.CapabilityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

/**
 * 能力元数据应用服务。
 *
 * <p>该服务用于把 Java 里已经注册的 LifeCapability 暴露给接口层，方便前端功能块实例配置页
 * 知道系统真实支持哪些能力。它只返回能力定义，不返回任何用户项目数据。</p>
 */
@Service
@RequiredArgsConstructor
public class CapabilityApplicationService {

    private final CapabilityRegistry capabilityRegistry;

    /**
     * 查询系统已经注册的全部能力。
     *
     * @return 能力元数据 VO 列表，按能力 key 排序
     */
    public List<CapabilityVo> listCapabilities() {
        return capabilityRegistry.list().stream()
            .sorted(Comparator.comparing(capability -> capability.key().asString()))
            .map(this::toVo)
            .toList();
    }

    /**
     * 把领域能力定义转换为接口 VO。
     *
     * @param capability Java 能力实现
     * @return 能力元数据 VO
     */
    private CapabilityVo toVo(LifeCapability capability) {
        CapabilityMetadata metadata = capability.metadata();

        CapabilityVo vo = new CapabilityVo();
        vo.setCapabilityKey(capability.key().asString());
        vo.setName(metadata.getName());
        vo.setDescription(metadata.getDescription());
        vo.setSupportedProjectTypes(metadata.getSupportedProjectTypes());
        vo.setProvidedBehaviors(metadata.getProvidedBehaviors());
        return vo;
    }
}

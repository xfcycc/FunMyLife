package com.funmylife.fml.domain.capability;

import java.util.List;
import java.util.Optional;

/**
 * 能力注册表。
 *
 * <p>注册表是 application 层查找能力实现的唯一入口。它只负责注册和查找系统支持的
 * LifeCapability，不保存任何用户业务数据，也不参与 Controller 入参出参序列化。</p>
 */
public interface CapabilityRegistry {

    /**
     * 按能力 key 查找能力实现。
     *
     * @param capabilityKey 能力 key
     * @return 能力实现；未知能力返回 Optional.empty()
     */
    Optional<LifeCapability> find(CapabilityKey capabilityKey);

    /**
     * 查询当前系统注册的全部能力。
     *
     * @return 能力实现列表
     */
    List<LifeCapability> list();
}

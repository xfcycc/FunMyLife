package com.funmylife.fml.domain.capability;

import com.funmylife.fml.domain.block.BlockInstance;

/**
 * Life Manager 可复用能力的基础接口。
 *
 * <p>能力表示系统能执行的领域行为，例如目标系统、媒体记录、资料资产或时间轴回顾。
 * 它不是功能块实例；功能块实例通过 CapabilityRef 引用能力，并给能力附加项目内配置。</p>
 */
public interface LifeCapability {

    /**
     * 返回能力稳定 key。
     *
     * @return 能力 key，用于 CapabilityRegistry 查找能力实现
     */
    CapabilityKey key();

    /**
     * 返回能力元数据。
     *
     * @return 能力名称、说明、适用类型和提供行为
     */
    CapabilityMetadata metadata();

    /**
     * 判断当前能力是否支持指定功能块实例。
     *
     * @param blockInstance 当前项目中的功能块实例
     * @return true 表示能力可以在该功能块实例下执行
     */
    default boolean supports(BlockInstance blockInstance) {
        return blockInstance != null && blockInstance.isEnabled();
    }
}

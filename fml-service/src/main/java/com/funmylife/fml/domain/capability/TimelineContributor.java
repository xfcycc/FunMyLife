package com.funmylife.fml.domain.capability;

/**
 * 可贡献时间轴事件草稿的能力扩展点。
 *
 * <p>该接口用于后续阶段：能力根据功能块实例中的 TimelineRule 生成事件草稿，
 * 但不直接保存数据库。真正保存动作应由 TimelineApplicationService 统一处理。</p>
 */
public interface TimelineContributor {
}

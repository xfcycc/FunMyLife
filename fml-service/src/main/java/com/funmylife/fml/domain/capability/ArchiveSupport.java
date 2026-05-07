package com.funmylife.fml.domain.capability;

/**
 * 支持归档行为的能力扩展点。
 *
 * <p>该接口用于后续阶段：活动、版本、旅行阶段等能力可以根据 ArchiveRule
 * 生成归档计划或执行归档。第一轮先定义边界，不在这里直接落数据库操作。</p>
 */
public interface ArchiveSupport {
}

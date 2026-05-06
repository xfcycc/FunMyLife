package com.funmylife.fml.interfaces.request;

import lombok.Data;

import java.util.List;

/**
 * 功能块配置批量保存请求。
 *
 * <p>保存接口采用整体提交：projectId 表示目标项目，configs 表示该项目下需要 upsert
 * 的功能块实例配置。Controller 不接收裸 List，避免请求体缺少项目上下文。</p>
 */
@Data
public class AbilityConfigBatchSaveRequest {

    /** 目标项目 ID。 */
    private Long projectId;

    /** 待保存的功能块配置项列表。 */
    private List<AbilityConfigSaveItemRequest> configs;
}

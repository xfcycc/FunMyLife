package com.funmylife.fml.interfaces.request;

import lombok.Data;

import java.util.List;

/**
 * 功能块实例批量保存请求。
 *
 * <p>功能块实例是项目管理页的核心配置对象。请求体必须显式携带 projectId，
 * 不能把项目上下文放到 URL path，也不能直接提交裸 List。</p>
 */
@Data
public class BlockInstanceBatchSaveRequest {

    /** 目标项目 ID。 */
    private Long projectId;

    /** 待保存的功能块实例列表。 */
    private List<BlockInstanceSaveItemRequest> blockInstances;
}

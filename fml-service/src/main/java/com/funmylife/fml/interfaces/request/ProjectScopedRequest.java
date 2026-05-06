package com.funmylife.fml.interfaces.request;

import lombok.Data;

/**
 * 项目范围内的查询请求。
 *
 * <p>所有项目级查询都通过 JSON body 传入 projectId，不再使用
 * /life/project/{projectId} 这类 path 参数。</p>
 */
@Data
public class ProjectScopedRequest {

    /** Life Manager 项目 ID，例如无限暖暖项目 1001。 */
    private Long projectId;
}

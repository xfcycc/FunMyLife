package com.funmylife.fml.interfaces.request;

import lombok.Data;

@Data
public class TargetProgressUpdateRequest {

    /** 目标所属项目 ID。 */
    private Long projectId;

    /** 需要更新进度的目标 ID。 */
    private Long targetId;

    /** 更新后的当前进度值。 */
    private Integer current;
}

package com.funmylife.fml.interfaces.request;

import lombok.Data;

@Data
public class TargetStatusUpdateRequest {

    /** 目标所属项目 ID。 */
    private Long projectId;

    /** 需要更新状态的目标 ID。 */
    private Long targetId;

    /** 更新后的目标状态，例如 todo / done / skipped。 */
    private String status;
}

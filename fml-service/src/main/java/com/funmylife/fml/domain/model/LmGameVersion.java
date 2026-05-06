package com.funmylife.fml.domain.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏版本表 — 跟踪游戏版本/赛季，一个项目下可有多个版本
 */
@Data
public class LmGameVersion {

    /** 版本 ID */
    private Long versionId;

    /** 所属项目 ID */
    private Long projectId;

    /** 版本号，如 "1.5" */
    private String name;

    /** 版本标题，如 "暖暖生日庆典" */
    private String title;

    /** 版本开始时间 */
    private LocalDateTime startAt;

    /** 版本结束时间 */
    private LocalDateTime endAt;

    /** 状态：upcoming / active / ending / ended / archived */
    private String status;

    /** 版本亮点，JSON */
    private String highlights;

    /** 归档摘要（版本结束后填写） */
    private String summary;

    /** 归档时间 */
    private LocalDateTime archivedAt;

    /** 备注 */
    private String remark;

    // ---- RuoYi 审计列 ----

    private String tenantId;
    private Long createDept;
    private Long createBy;
    private Date createTime;
    private Long updateBy;
    private Date updateTime;
}

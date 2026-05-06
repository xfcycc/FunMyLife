package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * 项目视图对象 — 返回给前端的项目信息（不含租户等内部字段）
 */
@Data
public class LmProjectVo {

    private Long projectId;
    private String projectName;
    private String description;
    private String coverSrc;
    private String coverAlt;
    private String status;
    private String schemeId;
    private String schemeType;

    /** 标签，JSON 字符串 */
    private String tags;

    /** 统计数据，JSON 字符串 */
    private String stats;

    private String remark;
    private Long createBy;
    private Date createTime;
}

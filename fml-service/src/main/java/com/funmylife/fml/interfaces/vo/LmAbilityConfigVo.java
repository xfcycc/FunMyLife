package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.Date;

/**
 * 功能块配置视图对象 — 返回给前端的配置信息
 */
@Data
public class LmAbilityConfigVo {

    private Long configId;
    private Long projectId;
    private String blockKey;
    private String displayName;

    /** 是否启用：1=启用 0=禁用 */
    private String enabled;

    /** 以下均为 JSON 字符串，前端自行解析 */
    private String capabilities;
    private String navigation;
    private String summaryRules;
    private String fields;
    private String behavior;
    private String timeline;
    private String aiRules;
    private String security;

    private String remark;
    private Long createBy;
    private Date createTime;
}

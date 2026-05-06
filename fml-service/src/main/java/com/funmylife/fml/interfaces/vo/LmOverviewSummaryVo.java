package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * 概览摘要视图对象 — 由 OverviewService 根据 summaryRules 聚合各表数据生成，
 * 每个实例对应概览页上的一张摘要卡片
 */
@Data
public class LmOverviewSummaryVo {

    /** 摘要 ID，格式 "overview-{ruleId}" */
    private String id;

    /** 规则 ID，如 sum-current-version / sum-today-targets */
    private String ruleId;

    /** 卡片标题 */
    private String title;

    /** 卡片主值，如 "3/5"、"1.5" */
    private String value;

    /** 卡片描述 */
    private String description;

    /** 条目列表，空时返回 null，前端按空数组兜底处理。 */
    private List<LmOverviewSummaryItemVo> items;

    /** 点击卡片后的目标路由标识，如 "targets" / "gallery" */
    private String targetRoute;
}

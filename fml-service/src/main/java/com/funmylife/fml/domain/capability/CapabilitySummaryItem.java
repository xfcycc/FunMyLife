package com.funmylife.fml.domain.capability;

import lombok.Data;

/**
 * 能力摘要明细项。
 *
 * <p>该对象用于表达某个能力在概览里贡献的单条明细，例如一个未完成目标、
 * 一个即将结束活动或一条最近时间轴事件。</p>
 */
@Data
public class CapabilitySummaryItem {

    /** 明细项 ID，通常来自对应业务数据主键。 */
    private String id;

    /** 明细项展示文案。 */
    private String label;

    /** 明细项状态，例如 done、ending、photo_uploaded。 */
    private String status;

    /** 点击该明细后进入的功能块 key。 */
    private String targetRoute;
}

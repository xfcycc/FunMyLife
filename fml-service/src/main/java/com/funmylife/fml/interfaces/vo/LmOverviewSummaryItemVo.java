package com.funmylife.fml.interfaces.vo;

import lombok.Data;

/**
 * 概览摘要卡片条目。
 *
 * <p>替代旧实现中的临时键值结构，让 item 的字段在接口契约中固定下来。</p>
 */
@Data
public class LmOverviewSummaryItemVo {

    /** 条目 ID，通常来自目标、活动、照片、资产或时间轴事件 ID。 */
    private String id;

    /** 条目展示文案。 */
    private String label;

    /** 条目状态，用于前端决定标签样式。 */
    private String status;

    /** 点击条目后应切换到的前端功能块路由标识。 */
    private String targetRoute;
}

package com.funmylife.fml.domain.block;

import lombok.Data;

/**
 * 功能块导航配置。
 *
 * <p>导航配置只决定功能块是否出现在项目详情页导航中，以及出现时的排序。
 * 它不决定功能块是否启用；隐藏但启用的功能块仍然可以贡献概览、时间轴或 AI 上下文。</p>
 */
@Data
public class BlockNavigation {

    /** 是否显示在项目导航中。 */
    private boolean visible;

    /** 导航排序，数值越小越靠前。 */
    private int order;

    /** 原始 JSON 字符串，用于第一阶段保持接口返回格式稳定。 */
    private String rawJson;
}

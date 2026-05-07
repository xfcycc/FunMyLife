package com.funmylife.fml.domain.capability;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力贡献的领域摘要。
 *
 * <p>该对象位于 domain 层，表示能力在概览场景下产出的摘要结果。
 * Controller 返回给前端的 VO 应由 application 层转换，domain 层不能依赖 interfaces 包。</p>
 */
@Data
public class CapabilitySummary {

    /** 摘要 ID。 */
    private String id;

    /** 生成该摘要的规则 ID。 */
    private String ruleId;

    /** 摘要标题。 */
    private String title;

    /** 摘要主值，例如 3/5、正常、2 个。 */
    private String value;

    /** 摘要说明。 */
    private String description;

    /** 点击后进入的功能块 key。 */
    private String targetRoute;

    /** 摘要排序优先级，通常来自 SummaryRule.priority。 */
    private int priority = 99;

    /** 摘要明细列表。 */
    private List<CapabilitySummaryItem> items = new ArrayList<>();
}

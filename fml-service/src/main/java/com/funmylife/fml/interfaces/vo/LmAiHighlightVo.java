package com.funmylife.fml.interfaces.vo;

import lombok.Data;

/**
 * AI 建议高亮信息。
 *
 * <p>虽然当前 AI 能力还是占位实现，仍然提前使用实体类固定响应结构，
 * 避免接口层退回到 Map 或任意 JSON。</p>
 */
@Data
public class LmAiHighlightVo {

    /** 高亮文本。 */
    private String text;

    /** 高亮类型，例如 info / warning / success。 */
    private String type;
}

package org.dromara.life.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * AI概览视图对象
 *
 * @author caiguoyu
 */
@Data
public class LmAiOverviewVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 当前建议（JSON）
     */
    private String currentSuggestion;

    /**
     * 建议数量
     */
    private Integer suggestionCount;

}

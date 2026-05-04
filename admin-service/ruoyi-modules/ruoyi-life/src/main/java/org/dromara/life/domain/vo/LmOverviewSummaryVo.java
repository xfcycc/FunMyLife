package org.dromara.life.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 概览摘要视图对象
 *
 * @author caiguoyu
 */
@Data
public class LmOverviewSummaryVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 摘要ID
     */
    private String id;

    /**
     * 规则ID
     */
    private String ruleId;

    /**
     * 标题
     */
    private String title;

    /**
     * 值
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 条目（JSON）
     */
    private String items;

    /**
     * 目标路由
     */
    private String targetRoute;

}

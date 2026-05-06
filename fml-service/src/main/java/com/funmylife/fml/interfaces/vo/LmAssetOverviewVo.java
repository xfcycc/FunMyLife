package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * 资产概览视图对象。
 *
 * <p>用于替代原来的 {total, list} Map 响应，让接口结构在 Java 类型里明确表达。</p>
 */
@Data
public class LmAssetOverviewVo {

    /** 当前项目下资产总数。 */
    private Integer total;

    /** 资产列表，列表元素同样是 VO，不直接暴露持久化 Entity。 */
    private List<LmAssetVo> list;
}

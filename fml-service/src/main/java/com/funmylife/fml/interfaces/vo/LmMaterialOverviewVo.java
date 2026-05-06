package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * 素材概览视图对象。
 *
 * <p>用于替代原来的 {total, list} Map 响应。</p>
 */
@Data
public class LmMaterialOverviewVo {

    /** 当前项目下素材总数。 */
    private Integer total;

    /** 素材列表。 */
    private List<LmMaterialVo> list;
}

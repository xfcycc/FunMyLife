package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.List;

/**
 * 笔记概览视图对象。
 *
 * <p>用于替代原来的 {total, list} Map 响应。</p>
 */
@Data
public class LmNoteOverviewVo {

    /** 当前项目下笔记总数。 */
    private Integer total;

    /** 笔记列表。 */
    private List<LmNoteVo> list;
}

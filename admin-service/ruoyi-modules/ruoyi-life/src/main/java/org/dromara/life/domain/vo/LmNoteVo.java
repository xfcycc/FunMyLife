package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmNote;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

/**
 * 笔记视图对象 lm_note
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmNote.class)
public class LmNoteVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 笔记ID
     */
    private Long noteId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 笔记标题
     */
    private String title;

    /**
     * 笔记内容
     */
    private String content;

    /**
     * 笔记类型
     */
    private String type;

    /**
     * 分类
     */
    private String category;

    /**
     * 分类标签
     */
    private String categoryLabel;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 是否置顶概览（Y是 N否）
     */
    private String pinnedToOverview;

    /**
     * 时间线规则（JSON）
     */
    private String timelineRule;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

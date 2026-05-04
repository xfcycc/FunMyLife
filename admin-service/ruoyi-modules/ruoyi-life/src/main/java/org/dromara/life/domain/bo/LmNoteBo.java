package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmNote;

/**
 * 笔记业务对象 lm_note
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmNote.class, reverseConvertGenerate = false)
public class LmNoteBo extends BaseEntity {

    /**
     * 笔记ID
     */
    private Long noteId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 笔记标题
     */
    @NotBlank(message = "笔记标题不能为空")
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

}

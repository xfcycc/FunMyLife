package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 笔记表 lm_note
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_note")
public class LmNote extends TenantEntity {

    /**
     * 笔记ID
     */
    @TableId(value = "note_id")
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

}

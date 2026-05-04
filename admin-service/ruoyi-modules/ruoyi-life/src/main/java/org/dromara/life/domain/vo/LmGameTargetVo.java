package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmGameTarget;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏目标视图对象 lm_game_target
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmGameTarget.class)
public class LmGameTargetVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 目标类型
     */
    private String type;

    /**
     * 目标标题
     */
    private String title;

    /**
     * 目标描述
     */
    private String description;

    /**
     * 状态
     */
    private String status;

    /**
     * 当前进度
     */
    private Integer progressCurrent;

    /**
     * 目标进度
     */
    private Integer progressTarget;

    /**
     * 重置规则（JSON）
     */
    private String resetRule;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 截止时间
     */
    private LocalDateTime dueAt;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 是否置顶概览（Y是 N否）
     */
    private String pinnedToOverview;

    /**
     * 时间线规则（JSON）
     */
    private String timelineRule;

    /**
     * 归档时间
     */
    private LocalDateTime archivedAt;

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

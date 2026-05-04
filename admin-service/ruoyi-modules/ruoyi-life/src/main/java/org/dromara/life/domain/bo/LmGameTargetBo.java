package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmGameTarget;

import java.time.LocalDateTime;

/**
 * 游戏目标业务对象 lm_game_target
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmGameTarget.class, reverseConvertGenerate = false)
public class LmGameTargetBo extends BaseEntity {

    /**
     * 目标ID
     */
    private Long targetId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 目标类型
     */
    @NotBlank(message = "目标类型不能为空")
    private String type;

    /**
     * 目标标题
     */
    @NotBlank(message = "目标标题不能为空")
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

}

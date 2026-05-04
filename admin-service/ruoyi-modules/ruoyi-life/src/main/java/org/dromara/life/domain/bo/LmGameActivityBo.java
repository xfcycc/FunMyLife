package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmGameActivity;

import java.time.LocalDateTime;

/**
 * 游戏活动业务对象 lm_game_activity
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmGameActivity.class, reverseConvertGenerate = false)
public class LmGameActivityBo extends BaseEntity {

    /**
     * 活动ID
     */
    private Long activityId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 版本ID
     */
    @NotNull(message = "版本ID不能为空")
    private Long versionId;

    /**
     * 活动标题
     */
    @NotBlank(message = "活动标题不能为空")
    private String title;

    /**
     * 活动描述
     */
    private String description;

    /**
     * 开始时间
     */
    private LocalDateTime startAt;

    /**
     * 结束时间
     */
    private LocalDateTime endAt;

    /**
     * 状态
     */
    private String status;

    /**
     * 优先级
     */
    private String priority;

    /**
     * 封面图
     */
    private String cover;

    /**
     * 提醒规则（JSON）
     */
    private String reminderRule;

    /**
     * 归档时间
     */
    private LocalDateTime archivedAt;

    /**
     * 备注
     */
    private String remark;

}

package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 游戏目标表 lm_game_target
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_game_target")
public class LmGameTarget extends TenantEntity {

    /**
     * 目标ID
     */
    @TableId(value = "target_id")
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

}

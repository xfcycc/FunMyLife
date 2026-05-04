package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 游戏活动表 lm_game_activity
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_game_activity")
public class LmGameActivity extends TenantEntity {

    /**
     * 活动ID
     */
    @TableId(value = "activity_id")
    private Long activityId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 活动标题
     */
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

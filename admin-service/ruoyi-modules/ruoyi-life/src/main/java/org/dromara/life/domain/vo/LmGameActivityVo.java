package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmGameActivity;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏活动视图对象 lm_game_activity
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmGameActivity.class)
public class LmGameActivityVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 活动ID
     */
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

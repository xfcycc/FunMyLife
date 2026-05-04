package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 时间线事件表 lm_timeline_event
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_timeline_event")
public class LmTimelineEvent extends TenantEntity {

    /**
     * 事件ID
     */
    @TableId(value = "event_id")
    private Long eventId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 发生时间
     */
    private LocalDateTime occurredAt;

    /**
     * 事件类型
     */
    private String type;

    /**
     * 事件标题
     */
    private String title;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 来源区块标识
     */
    private String sourceBlockKey;

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
     * 敏感度
     */
    private String sensitivity;

    /**
     * 是否在概览显示（Y是 N否）
     */
    private String displayInOverview;

    /**
     * AI是否可读（Y是 N否）
     */
    private String aiReadable;

    /**
     * 备注
     */
    private String remark;

}

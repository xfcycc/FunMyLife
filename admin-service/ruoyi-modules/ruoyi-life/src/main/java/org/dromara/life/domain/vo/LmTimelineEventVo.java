package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmTimelineEvent;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 时间线事件视图对象 lm_timeline_event
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmTimelineEvent.class)
public class LmTimelineEventVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 事件ID
     */
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

    /**
     * 创建者
     */
    private Long createBy;

    /**
     * 创建时间
     */
    private Date createTime;

}

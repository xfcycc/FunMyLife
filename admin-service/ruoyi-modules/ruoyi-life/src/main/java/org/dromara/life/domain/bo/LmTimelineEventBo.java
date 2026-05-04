package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmTimelineEvent;

import java.time.LocalDateTime;

/**
 * 时间线事件业务对象 lm_timeline_event
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmTimelineEvent.class, reverseConvertGenerate = false)
public class LmTimelineEventBo extends BaseEntity {

    /**
     * 事件ID
     */
    private Long eventId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 发生时间
     */
    @NotNull(message = "发生时间不能为空")
    private LocalDateTime occurredAt;

    /**
     * 事件类型
     */
    @NotBlank(message = "事件类型不能为空")
    private String type;

    /**
     * 事件标题
     */
    @NotBlank(message = "事件标题不能为空")
    private String title;

    /**
     * 事件描述
     */
    private String description;

    /**
     * 来源区块标识
     */
    @NotBlank(message = "来源区块标识不能为空")
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

package org.dromara.life.domain.vo;

import io.github.linpeilie.annotations.AutoMapper;
import lombok.Data;
import org.dromara.life.domain.LmGameVersion;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * 游戏版本视图对象 lm_game_version
 *
 * @author caiguoyu
 */
@Data
@AutoMapper(target = LmGameVersion.class)
public class LmGameVersionVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 版本名称
     */
    private String name;

    /**
     * 版本标题
     */
    private String title;

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
     * 亮点（JSON）
     */
    private String highlights;

    /**
     * 摘要
     */
    private String summary;

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

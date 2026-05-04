package org.dromara.life.domain.bo;

import io.github.linpeilie.annotations.AutoMapper;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.life.domain.LmGameVersion;

import java.time.LocalDateTime;

/**
 * 游戏版本业务对象 lm_game_version
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@AutoMapper(target = LmGameVersion.class, reverseConvertGenerate = false)
public class LmGameVersionBo extends BaseEntity {

    /**
     * 版本ID
     */
    private Long versionId;

    /**
     * 项目ID
     */
    @NotNull(message = "项目ID不能为空")
    private Long projectId;

    /**
     * 版本名称
     */
    @NotBlank(message = "版本名称不能为空")
    private String name;

    /**
     * 版本标题
     */
    @NotBlank(message = "版本标题不能为空")
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

}

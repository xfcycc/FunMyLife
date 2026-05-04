package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 游戏版本表 lm_game_version
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_game_version")
public class LmGameVersion extends TenantEntity {

    /**
     * 版本ID
     */
    @TableId(value = "version_id")
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

}

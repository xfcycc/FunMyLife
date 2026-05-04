package org.dromara.life.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import org.dromara.common.tenant.core.TenantEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 相册表 lm_album
 *
 * @author caiguoyu
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("lm_album")
public class LmAlbum extends TenantEntity {

    /**
     * 相册ID
     */
    @TableId(value = "album_id")
    private Long albumId;

    /**
     * 项目ID
     */
    private Long projectId;

    /**
     * 相册名称
     */
    private String name;

    /**
     * 封面URL
     */
    private String coverUrl;

    /**
     * 备注
     */
    private String remark;

}

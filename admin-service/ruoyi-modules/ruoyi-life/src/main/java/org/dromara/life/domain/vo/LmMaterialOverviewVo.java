package org.dromara.life.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 素材概览视图对象
 *
 * @author caiguoyu
 */
@Data
public class LmMaterialOverviewVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 已完成数
     */
    private Integer completed;

    /**
     * 素材列表（JSON）
     */
    private String materials;

}

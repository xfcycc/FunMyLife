package org.dromara.life.domain.vo;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * 资产概览视图对象
 *
 * @author caiguoyu
 */
@Data
public class LmAssetOverviewVo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 总数
     */
    private Integer total;

    /**
     * 资产列表（JSON）
     */
    private String assets;

}

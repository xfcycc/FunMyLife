package com.funmylife.fml.interfaces.request;

import lombok.Data;

/**
 * 资产详情请求。
 *
 * <p>projectId 和 assetId 都放在 body，application 层会用 projectId 校验资产归属。</p>
 */
@Data
public class AssetDetailRequest {

    /** 目标项目 ID。 */
    private Long projectId;

    /** 资产 ID。 */
    private Long assetId;
}

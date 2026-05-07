package com.funmylife.fml.interfaces.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力元数据视图对象。
 *
 * <p>该 VO 表示后端当前注册的一个可复用能力，例如目标系统、媒体记录、资料资产。
 * 它用于功能块实例配置页选择能力，不包含任何项目业务数据或持久化 Entity 字段。</p>
 */
@Data
public class CapabilityVo {

    /** 能力稳定 key，例如 target_system、media_record、asset_profile。 */
    private String capabilityKey;

    /** 能力名称。 */
    private String name;

    /** 能力说明。 */
    private String description;

    /** 支持的项目类型，例如 game；空列表表示暂不限制。 */
    private List<String> supportedProjectTypes = new ArrayList<>();

    /** 能力提供的行为，例如 summary、timeline、archive、ai-context。 */
    private List<String> providedBehaviors = new ArrayList<>();
}

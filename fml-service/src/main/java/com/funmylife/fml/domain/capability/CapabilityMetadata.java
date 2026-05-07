package com.funmylife.fml.domain.capability;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 能力元数据。
 *
 * <p>能力元数据用于向方案、功能块实例配置页和开发者说明某个能力能解决什么问题、
 * 适用于哪些项目类型，以及它提供哪些行为扩展点。它不包含用户真实数据。</p>
 */
@Data
public class CapabilityMetadata {

    /** 能力名称，例如“目标系统”。 */
    private String name;

    /** 能力说明，用于管理页或后续能力列表接口展示。 */
    private String description;

    /** 该能力适用的项目类型，例如 game、travel。空列表表示暂不限制。 */
    private List<String> supportedProjectTypes = new ArrayList<>();

    /** 能力提供的行为，例如 summary、timeline、archive、ai-context。 */
    private List<String> providedBehaviors = new ArrayList<>();
}

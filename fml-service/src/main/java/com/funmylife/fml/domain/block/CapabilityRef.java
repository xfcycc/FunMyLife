package com.funmylife.fml.domain.block;

import com.funmylife.fml.domain.capability.CapabilityKey;
import com.funmylife.fml.domain.rule.RuleSource;
import lombok.Data;

/**
 * 功能块实例对能力的引用。
 *
 * <p>CapabilityRef 不代表能力实现类本身，而是说明某个功能块实例启用了哪个能力、
 * 这个能力在当前功能块中承担什么角色，以及当前项目对该能力的配置覆盖。</p>
 */
@Data
public class CapabilityRef {

    /** 能力 key，例如 target-system、activity-management、media-record。 */
    private CapabilityKey capabilityKey;

    /** 当前功能块内展示给用户看的能力名称，可覆盖能力默认名称。 */
    private String displayName;

    /** 当前功能块实例是否启用这个能力引用。 */
    private boolean enabled = true;

    /** 能力在当前功能块中的角色说明，例如“活动倒计时来源”或“概览摘要贡献者”。 */
    private String roleInBlock;

    /** 当前功能块对能力的实例级配置；第一阶段先保留 JSON 字符串，后续再细化成能力专属配置类。 */
    private String configRaw;

    /** 配置来源，用于区分方案默认、用户覆盖、AI 建议或导入配置。 */
    private RuleSource source = RuleSource.SCHEME_DEFAULT;
}

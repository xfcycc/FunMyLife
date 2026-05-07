package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.CapabilityKey;
import com.funmylife.fml.domain.capability.CapabilityMetadata;
import com.funmylife.fml.domain.capability.LifeCapability;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 摘要提醒能力。
 *
 * <p>该能力表示“把多个业务能力产出的重点内容收束到概览和提醒”的组合能力。当前阶段它主要
 * 作为功能块实例配置中的能力元数据存在，真正的摘要内容仍由 target_system、media_record
 * 等业务能力分别贡献。</p>
 *
 * <p>暂不在这里发送实际通知；后续接入提醒渠道时可让它实现 ReminderSupport。</p>
 */
@Component
public class SummaryReminderCapability extends AbstractSummaryCapability implements LifeCapability {

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("summary_reminder");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("摘要提醒", "把不同业务能力的重点内容收束到概览和提醒。", List.of("summary", "reminder"));
    }
}

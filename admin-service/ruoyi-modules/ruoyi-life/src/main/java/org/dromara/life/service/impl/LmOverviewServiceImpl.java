package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.life.domain.LmAbilityConfig;
import org.dromara.life.domain.vo.LmAbilityConfigVo;
import org.dromara.life.domain.vo.LmOverviewSummaryVo;
import org.dromara.life.mapper.LmAbilityConfigMapper;
import org.dromara.life.service.ILmOverviewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 概览摘要 服务层实现
 *
 * Phase 1: 基于 ability_config 中 blockKey='overview' 的 summaryRules 生成摘要。
 * 解析 summaryRules JSON，为每个启用的规则创建一个 LmOverviewSummaryVo。
 * 后续阶段将接入真实数据源（目标、活动、时间轴等）填充摘要内容。
 */
@RequiredArgsConstructor
@Service
public class LmOverviewServiceImpl implements ILmOverviewService {

    private final LmAbilityConfigMapper abilityConfigMapper;

    @Override
    public List<LmOverviewSummaryVo> getOverviewSummaries(Long projectId) {
        // 查询 overview 功能块配置
        LambdaQueryWrapper<LmAbilityConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAbilityConfig::getProjectId, projectId);
        lqw.eq(LmAbilityConfig::getBlockKey, "overview");
        LmAbilityConfigVo config = abilityConfigMapper.selectVoOne(lqw);

        List<LmOverviewSummaryVo> summaries = new ArrayList<>();
        if (config == null) {
            return summaries;
        }

        // Phase 1: 基于 summaryRules JSON 生成摘要占位
        // summaryRules 为 JSON 数组，每项包含 source、enabled、title、maxItems、priority 等
        // 此处先返回空列表，待 summaryRules JSON 结构确定后再解析
        // 后续实现将遍历 enabled 的规则，按 source 查询对应数据生成摘要
        return summaries;
    }
}

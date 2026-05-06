package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmAbilityConfig;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.request.AbilityConfigSaveItemRequest;
import com.funmylife.fml.interfaces.vo.LmAbilityConfigVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能块配置服务 — 查询和保存项目的能力块配置
 */
@Service
@RequiredArgsConstructor
public class AbilityConfigService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询指定项目下所有功能块配置
     */
    public List<LmAbilityConfigVo> queryList(Long projectId) {
        List<LmAbilityConfig> entities = lifeDataRepository.findAbilityConfigs(projectId);
        return entities.stream().map(this::toVo).toList();
    }

    /**
     * 批量保存功能块配置（upsert 语义）
     * 按 projectId + blockKey 匹配：已存在则更新，不存在则插入
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long projectId, List<AbilityConfigSaveItemRequest> configs) {
        if (configs == null || configs.isEmpty()) {
            return;
        }
        for (AbilityConfigSaveItemRequest config : configs) {
            LmAbilityConfig existing = lifeDataRepository.findAbilityConfig(projectId, config.getBlockKey());

            if (existing != null) {
                // 已存在 → 更新
                existing.setDisplayName(config.getDisplayName());
                existing.setEnabled(config.getEnabled());
                existing.setCapabilities(config.getCapabilities());
                existing.setNavigation(config.getNavigation());
                existing.setSummaryRules(config.getSummaryRules());
                existing.setFields(config.getFields());
                existing.setBehavior(config.getBehavior());
                existing.setTimeline(config.getTimeline());
                existing.setAiRules(config.getAiRules());
                existing.setSecurity(config.getSecurity());
                existing.setRemark(config.getRemark());
                lifeDataRepository.updateAbilityConfig(existing);
            } else {
                // 不存在 → 插入
                LmAbilityConfig entity = new LmAbilityConfig();
                entity.setProjectId(projectId);
                entity.setBlockKey(config.getBlockKey());
                entity.setDisplayName(config.getDisplayName());
                entity.setEnabled(config.getEnabled());
                entity.setCapabilities(config.getCapabilities());
                entity.setNavigation(config.getNavigation());
                entity.setSummaryRules(config.getSummaryRules());
                entity.setFields(config.getFields());
                entity.setBehavior(config.getBehavior());
                entity.setTimeline(config.getTimeline());
                entity.setAiRules(config.getAiRules());
                entity.setSecurity(config.getSecurity());
                entity.setRemark(config.getRemark());
                lifeDataRepository.insertAbilityConfig(entity);
            }
        }
    }

    /** 实体转视图对象 */
    private LmAbilityConfigVo toVo(LmAbilityConfig entity) {
        LmAbilityConfigVo vo = new LmAbilityConfigVo();
        vo.setConfigId(entity.getConfigId());
        vo.setProjectId(entity.getProjectId());
        vo.setBlockKey(entity.getBlockKey());
        vo.setDisplayName(entity.getDisplayName());
        vo.setEnabled(entity.getEnabled());
        vo.setCapabilities(entity.getCapabilities());
        vo.setNavigation(entity.getNavigation());
        vo.setSummaryRules(entity.getSummaryRules());
        vo.setFields(entity.getFields());
        vo.setBehavior(entity.getBehavior());
        vo.setTimeline(entity.getTimeline());
        vo.setAiRules(entity.getAiRules());
        vo.setSecurity(entity.getSecurity());
        vo.setRemark(entity.getRemark());
        vo.setCreateBy(entity.getCreateBy());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}

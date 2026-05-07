package com.funmylife.fml.application.block;

import com.funmylife.fml.domain.block.BlockInstance;
import com.funmylife.fml.domain.block.BlockKey;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.request.BlockInstanceSaveItemRequest;
import com.funmylife.fml.interfaces.vo.BlockInstanceVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 功能块实例应用服务。
 *
 * <p>该服务是管理页读取和保存功能块实例配置的用例入口。它使用 BlockInstance 领域对象，
 * 不再让 application 层直接围绕 LmAbilityConfig 旧表语义编排业务。</p>
 */
@Service
@RequiredArgsConstructor
public class BlockInstanceApplicationService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询项目下所有功能块实例。
     *
     * @param projectId 项目 ID
     * @return 功能块实例 VO 列表；没有配置时返回空列表
     */
    public List<BlockInstanceVo> queryList(Long projectId) {
        return lifeDataRepository.findBlockInstances(projectId).stream().map(this::toVo).toList();
    }

    /**
     * 批量保存功能块实例配置。
     *
     * <p>保存采用 upsert 语义：同一项目下 blockKey 已存在则更新，不存在则插入。
     * 该方法会写数据库，但不会触发时间轴、提醒或 AI 上下文更新。</p>
     *
     * @param projectId 项目 ID
     * @param blockInstances 待保存的功能块实例请求项
     */
    @Transactional(rollbackFor = Exception.class)
    public void batchSave(Long projectId, List<BlockInstanceSaveItemRequest> blockInstances) {
        if (blockInstances == null || blockInstances.isEmpty()) {
            return;
        }

        for (BlockInstanceSaveItemRequest request : blockInstances) {
            BlockInstance existing = lifeDataRepository.findBlockInstance(projectId, request.getBlockKey());
            BlockInstance next = toDomain(projectId, request);

            if (existing != null) {
                next.setBlockInstanceId(existing.getBlockInstanceId());
                lifeDataRepository.updateBlockInstance(next);
            } else {
                lifeDataRepository.insertBlockInstance(next);
            }
        }
    }

    private BlockInstance toDomain(Long projectId, BlockInstanceSaveItemRequest request) {
        BlockInstance block = new BlockInstance();
        block.setBlockInstanceId(request.getBlockInstanceId());
        block.setProjectId(projectId);
        block.setBlockKey(BlockKey.of(request.getBlockKey()));
        block.setDisplayName(request.getDisplayName());
        block.setEnabled(isTrue(request.getEnabled()));
        block.setCapabilitiesRaw(request.getCapabilities());
        block.setNavigationRaw(request.getNavigation());
        block.setSummaryRulesRaw(request.getSummaryRules());
        block.setFieldsRaw(request.getFields());
        block.setBehaviorRaw(request.getBehavior());
        block.setTimelineRaw(request.getTimeline());
        block.setAiRulesRaw(request.getAiRules());
        block.setSecurityRaw(request.getSecurity());
        block.setRemark(request.getRemark());
        return block;
    }

    private BlockInstanceVo toVo(BlockInstance block) {
        BlockInstanceVo vo = new BlockInstanceVo();
        vo.setBlockInstanceId(block.getBlockInstanceId());
        vo.setProjectId(block.getProjectId());
        vo.setBlockKey(block.getBlockKey() == null ? null : block.getBlockKey().asString());
        vo.setDisplayName(block.getDisplayName());
        vo.setEnabled(block.isEnabled() ? "1" : "0");
        vo.setCapabilities(block.getCapabilitiesRaw());
        vo.setNavigation(block.getNavigationRaw());
        vo.setSummaryRules(block.getSummaryRulesRaw());
        vo.setFields(block.getFieldsRaw());
        vo.setBehavior(block.getBehaviorRaw());
        vo.setTimeline(block.getTimelineRaw());
        vo.setAiRules(block.getAiRulesRaw());
        vo.setSecurity(block.getSecurityRaw());
        vo.setRemark(block.getRemark());
        vo.setCreateBy(block.getCreateBy());
        vo.setCreateTime(block.getCreateTime());
        return vo;
    }

    private boolean isTrue(String value) {
        return "1".equals(value) || "Y".equals(value) || "true".equalsIgnoreCase(value);
    }
}

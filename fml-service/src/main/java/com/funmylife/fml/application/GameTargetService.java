package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmGameTarget;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmGameTargetVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏目标服务 — 查询每日/每周/活动/自定义目标
 */
@Service
@RequiredArgsConstructor
public class GameTargetService {

    private final LifeDataRepository lifeDataRepository;

    /** 查询指定项目下所有目标 */
    public List<LmGameTargetVo> queryList(Long projectId) {
        return lifeDataRepository.findGameTargets(projectId).stream().map(this::toVo).toList();
    }

    public void updateStatus(Long projectId, Long targetId, String status) {
        LmGameTarget entity = lifeDataRepository.findGameTargetById(targetId);
        if (entity == null || !projectId.equals(entity.getProjectId())) {
            return;
        }
        entity.setStatus(status);
        lifeDataRepository.updateGameTarget(entity);
    }

    public void updateProgress(Long projectId, Long targetId, Integer current) {
        LmGameTarget entity = lifeDataRepository.findGameTargetById(targetId);
        if (entity == null || !projectId.equals(entity.getProjectId())) {
            return;
        }
        entity.setProgressCurrent(current);
        lifeDataRepository.updateGameTarget(entity);
    }

    private LmGameTargetVo toVo(LmGameTarget entity) {
        LmGameTargetVo vo = new LmGameTargetVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

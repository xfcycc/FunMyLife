package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmProject;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmProjectVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 项目服务 — 查询项目基本信息
 */
@Service
@RequiredArgsConstructor
public class ProjectService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 按项目 ID 查询详情
     *
     * @param projectId 项目 ID
     * @return 项目视图对象，未找到时返回 null
     */
    public LmProjectVo queryById(Long projectId) {
        LmProject entity = lifeDataRepository.findProjectById(projectId);
        if (entity == null) {
            return null;
        }
        return toVo(entity);
    }

    /** 实体转视图对象（去除租户等内部字段） */
    private LmProjectVo toVo(LmProject entity) {
        LmProjectVo vo = new LmProjectVo();
        vo.setProjectId(entity.getProjectId());
        vo.setProjectName(entity.getProjectName());
        vo.setDescription(entity.getDescription());
        vo.setCoverSrc(entity.getCoverSrc());
        vo.setCoverAlt(entity.getCoverAlt());
        vo.setStatus(entity.getStatus());
        vo.setSchemeId(entity.getSchemeId());
        vo.setSchemeType(entity.getSchemeType());
        vo.setTags(entity.getTags());
        vo.setStats(entity.getStats());
        vo.setRemark(entity.getRemark());
        vo.setCreateBy(entity.getCreateBy());
        vo.setCreateTime(entity.getCreateTime());
        return vo;
    }
}

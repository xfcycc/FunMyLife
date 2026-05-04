package org.dromara.life.service.impl;

import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmProject;
import org.dromara.life.domain.bo.LmProjectBo;
import org.dromara.life.domain.vo.LmProjectVo;
import org.dromara.life.mapper.LmProjectMapper;
import org.dromara.life.service.ILmProjectService;
import org.springframework.stereotype.Service;

/**
 * 项目 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmProjectServiceImpl implements ILmProjectService {

    private final LmProjectMapper baseMapper;

    @Override
    public LmProjectVo queryById(Long projectId) {
        return baseMapper.selectVoById(projectId);
    }

    @Override
    public void insert(LmProjectBo bo) {
        LmProject entity = MapstructUtils.convert(bo, LmProject.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmProjectBo bo) {
        LmProject entity = MapstructUtils.convert(bo, LmProject.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long projectId) {
        baseMapper.deleteById(projectId);
    }
}

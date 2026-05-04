package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmGameVersion;
import org.dromara.life.domain.bo.LmGameVersionBo;
import org.dromara.life.domain.vo.LmGameVersionVo;
import org.dromara.life.mapper.LmGameVersionMapper;
import org.dromara.life.service.ILmGameVersionService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏版本 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmGameVersionServiceImpl implements ILmGameVersionService {

    private final LmGameVersionMapper baseMapper;

    @Override
    public List<LmGameVersionVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmGameVersion> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameVersion::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmGameVersionVo queryById(Long versionId) {
        return baseMapper.selectVoById(versionId);
    }

    @Override
    public LmGameVersionVo queryCurrent(Long projectId) {
        LambdaQueryWrapper<LmGameVersion> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameVersion::getProjectId, projectId);
        lqw.eq(LmGameVersion::getStatus, "active");
        return baseMapper.selectVoOne(lqw);
    }

    @Override
    public void insert(LmGameVersionBo bo) {
        LmGameVersion entity = MapstructUtils.convert(bo, LmGameVersion.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmGameVersionBo bo) {
        LmGameVersion entity = MapstructUtils.convert(bo, LmGameVersion.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long versionId) {
        baseMapper.deleteById(versionId);
    }
}

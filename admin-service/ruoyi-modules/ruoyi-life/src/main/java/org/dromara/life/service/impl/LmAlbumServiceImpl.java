package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmAlbum;
import org.dromara.life.domain.bo.LmAlbumBo;
import org.dromara.life.domain.vo.LmAlbumVo;
import org.dromara.life.mapper.LmAlbumMapper;
import org.dromara.life.service.ILmAlbumService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图册 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmAlbumServiceImpl implements ILmAlbumService {

    private final LmAlbumMapper baseMapper;

    @Override
    public List<LmAlbumVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmAlbum> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAlbum::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmAlbumVo queryById(Long albumId) {
        return baseMapper.selectVoById(albumId);
    }

    @Override
    public void insert(LmAlbumBo bo) {
        LmAlbum entity = MapstructUtils.convert(bo, LmAlbum.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmAlbumBo bo) {
        LmAlbum entity = MapstructUtils.convert(bo, LmAlbum.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long albumId) {
        baseMapper.deleteById(albumId);
    }
}

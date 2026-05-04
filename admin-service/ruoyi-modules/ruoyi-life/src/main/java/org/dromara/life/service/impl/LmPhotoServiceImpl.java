package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmPhoto;
import org.dromara.life.domain.bo.LmPhotoBo;
import org.dromara.life.domain.vo.LmPhotoVo;
import org.dromara.life.mapper.LmPhotoMapper;
import org.dromara.life.service.ILmPhotoService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 照片 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmPhotoServiceImpl implements ILmPhotoService {

    private final LmPhotoMapper baseMapper;

    @Override
    public List<LmPhotoVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmPhoto> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmPhoto::getProjectId, projectId);
        lqw.orderByDesc(LmPhoto::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmPhotoVo queryById(Long photoId) {
        return baseMapper.selectVoById(photoId);
    }

    @Override
    public void insert(LmPhotoBo bo) {
        LmPhoto entity = MapstructUtils.convert(bo, LmPhoto.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmPhotoBo bo) {
        LmPhoto entity = MapstructUtils.convert(bo, LmPhoto.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long photoId) {
        baseMapper.deleteById(photoId);
    }
}

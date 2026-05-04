package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmMaterial;
import org.dromara.life.domain.bo.LmMaterialBo;
import org.dromara.life.domain.vo.LmMaterialVo;
import org.dromara.life.mapper.LmMaterialMapper;
import org.dromara.life.service.ILmMaterialService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmMaterialServiceImpl implements ILmMaterialService {

    private final LmMaterialMapper baseMapper;

    @Override
    public List<LmMaterialVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmMaterial> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmMaterial::getProjectId, projectId);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmMaterialVo queryById(Long materialId) {
        return baseMapper.selectVoById(materialId);
    }

    @Override
    public void insert(LmMaterialBo bo) {
        LmMaterial entity = MapstructUtils.convert(bo, LmMaterial.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmMaterialBo bo) {
        LmMaterial entity = MapstructUtils.convert(bo, LmMaterial.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long materialId) {
        baseMapper.deleteById(materialId);
    }
}

package org.dromara.life.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.core.utils.MapstructUtils;
import org.dromara.life.domain.LmNote;
import org.dromara.life.domain.bo.LmNoteBo;
import org.dromara.life.domain.vo.LmNoteVo;
import org.dromara.life.mapper.LmNoteMapper;
import org.dromara.life.service.ILmNoteService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 笔记 服务层实现
 */
@RequiredArgsConstructor
@Service
public class LmNoteServiceImpl implements ILmNoteService {

    private final LmNoteMapper baseMapper;

    @Override
    public List<LmNoteVo> queryList(Long projectId) {
        LambdaQueryWrapper<LmNote> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmNote::getProjectId, projectId);
        lqw.orderByDesc(LmNote::getCreateTime);
        return baseMapper.selectVoList(lqw);
    }

    @Override
    public LmNoteVo queryById(Long noteId) {
        return baseMapper.selectVoById(noteId);
    }

    @Override
    public void insert(LmNoteBo bo) {
        LmNote entity = MapstructUtils.convert(bo, LmNote.class);
        baseMapper.insert(entity);
    }

    @Override
    public void update(LmNoteBo bo) {
        LmNote entity = MapstructUtils.convert(bo, LmNote.class);
        baseMapper.updateById(entity);
    }

    @Override
    public void deleteWithValid(Long noteId) {
        baseMapper.deleteById(noteId);
    }
}

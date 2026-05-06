package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmNote;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmNoteOverviewVo;
import com.funmylife.fml.interfaces.vo.LmNoteVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 笔记服务 — 查询笔记列表和详情
 */
@Service
@RequiredArgsConstructor
public class NoteService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询笔记概览，返回 {total, list} 结构
     */
    public LmNoteOverviewVo queryOverview(Long projectId) {
        List<LmNote> list = lifeDataRepository.findNotes(projectId);
        LmNoteOverviewVo vo = new LmNoteOverviewVo();
        vo.setTotal(list.size());
        vo.setList(list.stream().map(this::toVo).toList());
        return vo;
    }

    /** 按笔记 ID 查询详情，未找到返回 null */
    public LmNoteVo queryById(Long projectId, Long noteId) {
        LmNote entity = lifeDataRepository.findNoteById(noteId);
        if (entity == null || !projectId.equals(entity.getProjectId())) {
            return null;
        }
        return toVo(entity);
    }

    private LmNoteVo toVo(LmNote entity) {
        LmNoteVo vo = new LmNoteVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

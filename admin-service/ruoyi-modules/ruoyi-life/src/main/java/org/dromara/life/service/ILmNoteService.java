package org.dromara.life.service;

import org.dromara.life.domain.bo.LmNoteBo;
import org.dromara.life.domain.vo.LmNoteVo;

import java.util.List;

/**
 * 笔记 服务层
 */
public interface ILmNoteService {

    /**
     * 查询笔记列表
     *
     * @param projectId 项目ID
     * @return 笔记列表
     */
    List<LmNoteVo> queryList(Long projectId);

    /**
     * 查询笔记详情
     *
     * @param noteId 笔记ID
     * @return 笔记信息
     */
    LmNoteVo queryById(Long noteId);

    /**
     * 新增笔记
     *
     * @param bo 笔记信息
     */
    void insert(LmNoteBo bo);

    /**
     * 修改笔记
     *
     * @param bo 笔记信息
     */
    void update(LmNoteBo bo);

    /**
     * 删除笔记
     *
     * @param noteId 笔记ID
     */
    void deleteWithValid(Long noteId);
}

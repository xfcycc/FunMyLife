package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmNote;
import org.apache.ibatis.annotations.Mapper;

/** lm_note 表 Mapper */
@Mapper
public interface LmNoteMapper extends BaseMapper<LmNote> {
}

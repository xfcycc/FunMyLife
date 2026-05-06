package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmAlbum;
import org.apache.ibatis.annotations.Mapper;

/** lm_album 表 Mapper */
@Mapper
public interface LmAlbumMapper extends BaseMapper<LmAlbum> {
}

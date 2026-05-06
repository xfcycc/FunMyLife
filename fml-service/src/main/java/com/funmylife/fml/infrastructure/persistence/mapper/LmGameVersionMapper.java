package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmGameVersion;
import org.apache.ibatis.annotations.Mapper;

/** lm_game_version 表 Mapper */
@Mapper
public interface LmGameVersionMapper extends BaseMapper<LmGameVersion> {
}

package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmProject;
import org.apache.ibatis.annotations.Mapper;

/** lm_project 表 Mapper */
@Mapper
public interface LmProjectMapper extends BaseMapper<LmProject> {
}

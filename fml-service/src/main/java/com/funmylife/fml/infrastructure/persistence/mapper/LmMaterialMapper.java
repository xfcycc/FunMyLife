package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmMaterial;
import org.apache.ibatis.annotations.Mapper;

/** lm_material 表 Mapper */
@Mapper
public interface LmMaterialMapper extends BaseMapper<LmMaterial> {
}

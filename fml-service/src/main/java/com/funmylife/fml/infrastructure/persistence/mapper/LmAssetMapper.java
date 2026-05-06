package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmAsset;
import org.apache.ibatis.annotations.Mapper;

/** lm_asset 表 Mapper */
@Mapper
public interface LmAssetMapper extends BaseMapper<LmAsset> {
}

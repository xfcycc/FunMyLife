package com.funmylife.fml.infrastructure.persistence.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.funmylife.fml.infrastructure.persistence.entity.LmPhoto;
import org.apache.ibatis.annotations.Mapper;

/** lm_photo 表 Mapper */
@Mapper
public interface LmPhotoMapper extends BaseMapper<LmPhoto> {
}

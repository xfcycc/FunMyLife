package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmGameActivity;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmGameActivityVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏活动服务 — 查询版本内的活动列表
 */
@Service
@RequiredArgsConstructor
public class GameActivityService {

    private final LifeDataRepository lifeDataRepository;

    /** 查询指定项目下所有活动 */
    public List<LmGameActivityVo> queryList(Long projectId) {
        return lifeDataRepository.findGameActivities(projectId).stream().map(this::toVo).toList();
    }

    private LmGameActivityVo toVo(LmGameActivity entity) {
        LmGameActivityVo vo = new LmGameActivityVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

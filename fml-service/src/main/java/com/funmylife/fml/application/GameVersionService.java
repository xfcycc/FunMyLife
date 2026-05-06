package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmGameVersion;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmGameVersionVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 游戏版本服务 — 查询版本列表和当前活跃版本
 */
@Service
@RequiredArgsConstructor
public class GameVersionService {

    private final LifeDataRepository lifeDataRepository;

    /** 查询指定项目下所有版本 */
    public List<LmGameVersionVo> queryList(Long projectId) {
        return lifeDataRepository.findGameVersions(projectId).stream().map(this::toVo).toList();
    }

    /** 查询当前活跃版本（status='active'），未找到返回 null */
    public LmGameVersionVo queryCurrent(Long projectId) {
        LmGameVersion entity = lifeDataRepository.findCurrentGameVersion(projectId);
        return entity == null ? null : toVo(entity);
    }

    private LmGameVersionVo toVo(LmGameVersion entity) {
        LmGameVersionVo vo = new LmGameVersionVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

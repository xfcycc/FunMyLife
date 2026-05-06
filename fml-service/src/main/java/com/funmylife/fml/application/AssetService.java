package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmAsset;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmAssetOverviewVo;
import com.funmylife.fml.interfaces.vo.LmAssetVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 账号资产服务 — 查询资产列表和详情
 */
@Service
@RequiredArgsConstructor
public class AssetService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询资产概览，返回 {total, list} 结构
     */
    public LmAssetOverviewVo queryOverview(Long projectId) {
        List<LmAsset> list = lifeDataRepository.findAssets(projectId);
        LmAssetOverviewVo vo = new LmAssetOverviewVo();
        vo.setTotal(list.size());
        vo.setList(list.stream().map(this::toVo).toList());
        return vo;
    }

    /** 按资产 ID 查询详情，未找到返回 null */
    public LmAssetVo queryById(Long projectId, Long assetId) {
        LmAsset entity = lifeDataRepository.findAssetById(assetId);
        if (entity == null || !projectId.equals(entity.getProjectId())) {
            return null;
        }
        return toVo(entity);
    }

    private LmAssetVo toVo(LmAsset entity) {
        LmAssetVo vo = new LmAssetVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

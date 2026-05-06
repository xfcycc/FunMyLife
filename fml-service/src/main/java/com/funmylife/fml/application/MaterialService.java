package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmMaterial;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmMaterialOverviewVo;
import com.funmylife.fml.interfaces.vo.LmMaterialVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 素材服务 — 查询素材收集概览
 */
@Service
@RequiredArgsConstructor
public class MaterialService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询素材概览，返回 {total, list} 结构
     * 前端 service.ts 通过 data?.list ?? data?.records 取列表
     */
    public LmMaterialOverviewVo queryOverview(Long projectId) {
        List<LmMaterial> list = lifeDataRepository.findMaterials(projectId);
        LmMaterialOverviewVo vo = new LmMaterialOverviewVo();
        vo.setTotal(list.size());
        vo.setList(list.stream().map(this::toVo).toList());
        return vo;
    }

    private LmMaterialVo toVo(LmMaterial entity) {
        LmMaterialVo vo = new LmMaterialVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

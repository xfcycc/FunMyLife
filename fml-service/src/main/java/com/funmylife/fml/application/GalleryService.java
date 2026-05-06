package com.funmylife.fml.application;

import com.funmylife.fml.domain.model.LmAlbum;
import com.funmylife.fml.domain.model.LmPhoto;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmAlbumVo;
import com.funmylife.fml.interfaces.vo.LmGalleryOverviewVo;
import com.funmylife.fml.interfaces.vo.LmPhotoVo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 图册服务 — 查询图册和照片概览
 */
@Service
@RequiredArgsConstructor
public class GalleryService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 查询图册概览，返回 {albums, photos} 结构
     * 前端 service.ts 通过 data?.albums 和 data?.photos 分别取值
     */
    public LmGalleryOverviewVo queryOverview(Long projectId) {
        List<LmAlbum> albums = lifeDataRepository.findAlbums(projectId);
        List<LmPhoto> photos = lifeDataRepository.findPhotos(projectId);

        LmGalleryOverviewVo vo = new LmGalleryOverviewVo();
        vo.setAlbums(albums.stream().map(this::toAlbumVo).toList());
        vo.setPhotos(photos.stream().map(this::toPhotoVo).toList());
        return vo;
    }

    private LmAlbumVo toAlbumVo(LmAlbum entity) {
        LmAlbumVo vo = new LmAlbumVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }

    private LmPhotoVo toPhotoVo(LmPhoto entity) {
        LmPhotoVo vo = new LmPhotoVo();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}

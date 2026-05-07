package com.funmylife.fml.application.capability;

import com.funmylife.fml.domain.capability.*;
import com.funmylife.fml.domain.model.LmPhoto;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.domain.rule.SummaryRule;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 媒体记录能力。
 *
 * <p>该能力负责解释截图、搭配照片、活动照片等媒体记录，目前从 lm_photo 读取照片，
 * 并通过仓储统计 lm_album 图册数量。在概览场景下，它支持 source=gallery 的摘要规则，
 * 例如 sum-gallery-recent。</p>
 *
 * <p>暂不在这里处理图片上传、图片存储和相册同步；这些应由图册应用服务或后续媒体基础设施承接。</p>
 */
@Component
@RequiredArgsConstructor
public class MediaRecordCapability extends AbstractSummaryCapability implements LifeCapability, SummaryContributor {

    private final LifeDataRepository lifeDataRepository;

    @Override
    public CapabilityKey key() {
        return CapabilityKey.of("media_record");
    }

    @Override
    public CapabilityMetadata metadata() {
        return metadata("媒体记录", "保存截图、搭配照片和活动照片，并为概览提供最近图册摘要。", List.of("summary", "timeline", "ai-context"));
    }

    @Override
    public List<CapabilitySummary> buildSummaries(SummaryBuildContext context) {
        List<LmPhoto> photos = lifeDataRepository.findPhotos(context.getProjectId());
        return context.getRules().stream()
            .filter(rule -> acceptsSource(rule, "gallery"))
            .map(rule -> buildGallerySummary(context.getProjectId(), rule, photos))
            .toList();
    }

    private CapabilitySummary buildGallerySummary(Long projectId, SummaryRule rule, List<LmPhoto> photos) {
        CapabilitySummary summary = baseSummary(rule, "gallery");
        summary.setValue(photos.size() + "张");
        summary.setDescription(lifeDataRepository.countAlbums(projectId) + " 个图册");
        summary.setItems(photos.stream()
            .limit(maxItems(rule))
            .map(photo -> item(String.valueOf(photo.getPhotoId()), photo.getCaption() == null ? "照片记录" : photo.getCaption(), "photo_uploaded", "gallery"))
            .toList());
        return summary;
    }
}

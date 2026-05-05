package org.dromara.life.service.impl;

import cn.hutool.core.lang.Dict;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.life.domain.LmAlbum;
import org.dromara.life.domain.LmAbilityConfig;
import org.dromara.life.domain.LmAsset;
import org.dromara.life.domain.LmGameActivity;
import org.dromara.life.domain.LmGameTarget;
import org.dromara.life.domain.LmGameVersion;
import org.dromara.life.domain.LmMaterial;
import org.dromara.life.domain.LmPhoto;
import org.dromara.life.domain.LmTimelineEvent;
import org.dromara.life.domain.vo.LmAbilityConfigVo;
import org.dromara.life.domain.vo.LmAssetVo;
import org.dromara.life.domain.vo.LmGameActivityVo;
import org.dromara.life.domain.vo.LmGameTargetVo;
import org.dromara.life.domain.vo.LmGameVersionVo;
import org.dromara.life.domain.vo.LmMaterialVo;
import org.dromara.life.domain.vo.LmOverviewSummaryVo;
import org.dromara.life.domain.vo.LmPhotoVo;
import org.dromara.life.domain.vo.LmTimelineEventVo;
import org.dromara.life.mapper.LmAbilityConfigMapper;
import org.dromara.life.mapper.LmAlbumMapper;
import org.dromara.life.mapper.LmAssetMapper;
import org.dromara.life.mapper.LmGameActivityMapper;
import org.dromara.life.mapper.LmGameTargetMapper;
import org.dromara.life.mapper.LmGameVersionMapper;
import org.dromara.life.mapper.LmMaterialMapper;
import org.dromara.life.mapper.LmPhotoMapper;
import org.dromara.life.mapper.LmTimelineEventMapper;
import org.dromara.life.service.ILmOverviewService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 概览摘要 服务层实现
 *
 * Phase 1: 基于 ability_config 中 blockKey='overview' 的 summaryRules 生成摘要。
 * 解析 summaryRules JSON，为每个启用的规则创建一个 LmOverviewSummaryVo。
 * 后续阶段将接入真实数据源（目标、活动、时间轴等）填充摘要内容。
 */
@RequiredArgsConstructor
@Service
public class LmOverviewServiceImpl implements ILmOverviewService {

    private final LmAbilityConfigMapper abilityConfigMapper;
    private final LmGameVersionMapper gameVersionMapper;
    private final LmGameTargetMapper gameTargetMapper;
    private final LmGameActivityMapper gameActivityMapper;
    private final LmMaterialMapper materialMapper;
    private final LmAlbumMapper albumMapper;
    private final LmPhotoMapper photoMapper;
    private final LmAssetMapper assetMapper;
    private final LmTimelineEventMapper timelineEventMapper;

    @Override
    public List<LmOverviewSummaryVo> getOverviewSummaries(Long projectId) {
        // 查询 overview 功能块配置
        LambdaQueryWrapper<LmAbilityConfig> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAbilityConfig::getProjectId, projectId);
        lqw.eq(LmAbilityConfig::getBlockKey, "overview");
        LmAbilityConfigVo config = abilityConfigMapper.selectVoOne(lqw);

        List<LmOverviewSummaryVo> summaries = new ArrayList<>();
        if (config == null) {
            return summaries;
        }

        List<Dict> rules = JsonUtils.parseArrayMap(config.getSummaryRules());
        if (rules == null || rules.isEmpty()) {
            return summaries;
        }

        List<LmGameTargetVo> targets = queryTargets(projectId);
        List<LmGameActivityVo> activities = queryActivities(projectId);
        List<LmMaterialVo> materials = queryMaterials(projectId);
        List<LmPhotoVo> photos = queryPhotos(projectId);
        List<LmAssetVo> assets = queryAssets(projectId);
        List<LmTimelineEventVo> timelineEvents = queryTimelineEvents(projectId);

        rules.stream()
            .filter(this::isEnabled)
            .sorted(Comparator.comparingInt(rule -> getInt(rule, "priority", 99)))
            .forEach(rule -> {
                LmOverviewSummaryVo summary = buildSummary(projectId, rule, targets, activities, materials, photos, assets, timelineEvents);
                if (summary != null) {
                    summaries.add(summary);
                }
            });

        return summaries;
    }

    private LmOverviewSummaryVo buildSummary(Long projectId,
                                             Dict rule,
                                             List<LmGameTargetVo> targets,
                                             List<LmGameActivityVo> activities,
                                             List<LmMaterialVo> materials,
                                             List<LmPhotoVo> photos,
                                             List<LmAssetVo> assets,
                                             List<LmTimelineEventVo> timelineEvents) {
        String ruleId = getString(rule, "id", "");
        int maxItems = getInt(rule, "maxItems", 3);

        return switch (ruleId) {
            case "sum-current-version" -> buildCurrentVersionSummary(projectId, rule);
            case "sum-today-targets" -> buildTargetSummary(rule, targets, "daily", "targets", maxItems);
            case "sum-weekly-targets" -> buildTargetSummary(rule, targets, "weekly", "targets", maxItems);
            case "sum-ending-activities" -> buildEndingActivitySummary(rule, activities, maxItems);
            case "sum-material-progress" -> buildMaterialSummary(rule, materials, maxItems);
            case "sum-gallery-recent" -> buildGallerySummary(projectId, rule, photos, maxItems);
            case "sum-asset-risk" -> buildAssetSummary(rule, assets, maxItems);
            case "sum-recent-timeline" -> buildTimelineSummary(rule, timelineEvents, maxItems);
            default -> null;
        };
    }

    private LmOverviewSummaryVo buildCurrentVersionSummary(Long projectId, Dict rule) {
        LambdaQueryWrapper<LmGameVersion> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameVersion::getProjectId, projectId);
        lqw.eq(LmGameVersion::getStatus, "active");
        LmGameVersionVo version = gameVersionMapper.selectVoOne(lqw);

        LmOverviewSummaryVo summary = baseSummary(rule, "version_activity");
        summary.setValue(version == null ? "暂无当前版本" : version.getName());
        if (version != null) {
            summary.setDescription(version.getTitle());
        }
        return summary;
    }

    private LmOverviewSummaryVo buildTargetSummary(Dict rule,
                                                   List<LmGameTargetVo> targets,
                                                   String type,
                                                   String targetRoute,
                                                   int maxItems) {
        List<LmGameTargetVo> filteredTargets = targets.stream()
            .filter(target -> type.equals(target.getType()))
            .toList();
        long doneCount = filteredTargets.stream().filter(target -> "done".equals(target.getStatus())).count();

        LmOverviewSummaryVo summary = baseSummary(rule, targetRoute);
        summary.setValue(doneCount + "/" + filteredTargets.size());
        if ("daily".equals(type)) {
            summary.setDescription("今日还有 " + (filteredTargets.size() - doneCount) + " 项待完成");
        }
        summary.setItems(toItems(filteredTargets.stream()
            .filter(target -> !"done".equals(target.getStatus()))
            .limit(maxItems)
            .map(target -> item(String.valueOf(target.getTargetId()), target.getTitle(), target.getStatus(), targetRoute))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo buildEndingActivitySummary(Dict rule,
                                                           List<LmGameActivityVo> activities,
                                                           int maxItems) {
        Set<String> endingStatuses = Set.of("ending", "pending_archive");
        List<LmGameActivityVo> endingActivities = activities.stream()
            .filter(activity -> endingStatuses.contains(activity.getStatus()))
            .toList();

        LmOverviewSummaryVo summary = baseSummary(rule, "version_activity");
        summary.setValue(endingActivities.size() + "个");
        summary.setDescription("优先处理临近结束的活动目标");
        summary.setItems(toItems(endingActivities.stream()
            .limit(maxItems)
            .map(activity -> item(String.valueOf(activity.getActivityId()), activity.getTitle(), activity.getStatus(), "version_activity"))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo buildMaterialSummary(Dict rule,
                                                     List<LmMaterialVo> materials,
                                                     int maxItems) {
        List<LmMaterialVo> activeMaterials = materials.stream()
            .filter(material -> !"archived".equals(material.getStatus()))
            .toList();
        long completedCount = activeMaterials.stream().filter(material -> "completed".equals(material.getStatus())).count();

        LmOverviewSummaryVo summary = baseSummary(rule, "targets");
        summary.setValue(completedCount + "/" + activeMaterials.size());
        summary.setDescription("套装、素材和代币收集进度");
        summary.setItems(toItems(activeMaterials.stream()
            .filter(material -> !"completed".equals(material.getStatus()))
            .limit(maxItems)
            .map(material -> item(String.valueOf(material.getMaterialId()), material.getName(), material.getStatus(), "targets"))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo buildGallerySummary(Long projectId,
                                                    Dict rule,
                                                    List<LmPhotoVo> photos,
                                                    int maxItems) {
        LmOverviewSummaryVo summary = baseSummary(rule, "gallery");
        summary.setValue(photos.size() + "张");
        summary.setDescription(queryAlbumCount(projectId) + " 个图册");
        summary.setItems(toItems(photos.stream()
            .limit(maxItems)
            .map(photo -> item(String.valueOf(photo.getPhotoId()), photo.getCaption() == null ? "照片记录" : photo.getCaption(), "photo_uploaded", "gallery"))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo buildAssetSummary(Dict rule,
                                                  List<LmAssetVo> assets,
                                                  int maxItems) {
        Set<String> riskStatuses = Set.of("pending", "expired");
        List<LmAssetVo> riskAssets = assets.stream()
            .filter(asset -> riskStatuses.contains(asset.getStatus()))
            .toList();

        LmOverviewSummaryVo summary = baseSummary(rule, "assets");
        summary.setValue(riskAssets.isEmpty() ? "正常" : riskAssets.size() + "个");
        summary.setDescription(riskAssets.isEmpty() ? "账号资产暂无异常" : "存在待处理或过期资产");
        summary.setItems(toItems(riskAssets.stream()
            .limit(maxItems)
            .map(asset -> item(String.valueOf(asset.getAssetId()), asset.getName(), asset.getStatus(), "assets"))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo buildTimelineSummary(Dict rule,
                                                     List<LmTimelineEventVo> timelineEvents,
                                                     int maxItems) {
        List<LmTimelineEventVo> visibleEvents = timelineEvents.stream()
            .filter(event -> isTrue(event.getDisplayInOverview()))
            .toList();

        LmOverviewSummaryVo summary = baseSummary(rule, "timeline");
        summary.setValue(visibleEvents.size() + "条");
        summary.setItems(toItems(visibleEvents.stream()
            .limit(maxItems)
            .map(event -> item(String.valueOf(event.getEventId()), event.getTitle(), event.getType(), "timeline"))
            .toList()));
        return summary;
    }

    private LmOverviewSummaryVo baseSummary(Dict rule, String targetRoute) {
        String ruleId = getString(rule, "id", "");
        LmOverviewSummaryVo summary = new LmOverviewSummaryVo();
        summary.setId("overview-" + ruleId);
        summary.setRuleId(ruleId);
        summary.setTitle(getString(rule, "title", ""));
        summary.setTargetRoute(targetRoute);
        return summary;
    }

    private Map<String, String> item(String id, String label, String status, String targetRoute) {
        Map<String, String> item = new LinkedHashMap<>();
        item.put("id", id);
        item.put("label", label == null ? "" : label);
        item.put("status", status);
        item.put("targetRoute", targetRoute);
        return item;
    }

    private String toItems(List<Map<String, String>> items) {
        return items.isEmpty() ? null : JsonUtils.toJsonString(items);
    }

    private List<LmGameTargetVo> queryTargets(Long projectId) {
        LambdaQueryWrapper<LmGameTarget> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameTarget::getProjectId, projectId);
        return gameTargetMapper.selectVoList(lqw);
    }

    private List<LmGameActivityVo> queryActivities(Long projectId) {
        LambdaQueryWrapper<LmGameActivity> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmGameActivity::getProjectId, projectId);
        return gameActivityMapper.selectVoList(lqw);
    }

    private List<LmMaterialVo> queryMaterials(Long projectId) {
        LambdaQueryWrapper<LmMaterial> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmMaterial::getProjectId, projectId);
        return materialMapper.selectVoList(lqw);
    }

    private int queryAlbumCount(Long projectId) {
        LambdaQueryWrapper<LmAlbum> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAlbum::getProjectId, projectId);
        return albumMapper.selectCount(lqw).intValue();
    }

    private List<LmPhotoVo> queryPhotos(Long projectId) {
        LambdaQueryWrapper<LmPhoto> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmPhoto::getProjectId, projectId);
        lqw.orderByDesc(LmPhoto::getCreateTime);
        return photoMapper.selectVoList(lqw);
    }

    private List<LmAssetVo> queryAssets(Long projectId) {
        LambdaQueryWrapper<LmAsset> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmAsset::getProjectId, projectId);
        return assetMapper.selectVoList(lqw);
    }

    private List<LmTimelineEventVo> queryTimelineEvents(Long projectId) {
        LambdaQueryWrapper<LmTimelineEvent> lqw = Wrappers.lambdaQuery();
        lqw.eq(LmTimelineEvent::getProjectId, projectId);
        lqw.orderByDesc(LmTimelineEvent::getOccurredAt);
        return timelineEventMapper.selectVoList(lqw);
    }

    private boolean isEnabled(Dict rule) {
        Object enabled = rule.get("enabled");
        return enabled == null || Boolean.TRUE.equals(enabled) || "1".equals(enabled) || "Y".equals(enabled) || "true".equals(enabled);
    }

    private boolean isTrue(String value) {
        return "1".equals(value) || "Y".equals(value) || "true".equals(value);
    }

    private int getInt(Dict rule, String key, int defaultValue) {
        Object value = rule.get(key);
        if (value instanceof Number number) {
            return number.intValue();
        }
        if (value instanceof String text) {
            try {
                return Integer.parseInt(text);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    private String getString(Dict rule, String key, String defaultValue) {
        Object value = rule.get(key);
        return value == null ? defaultValue : String.valueOf(value);
    }
}

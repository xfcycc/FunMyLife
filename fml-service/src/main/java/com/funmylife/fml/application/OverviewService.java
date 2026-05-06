package com.funmylife.fml.application;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.funmylife.fml.domain.model.*;
import com.funmylife.fml.domain.repository.LifeDataRepository;
import com.funmylife.fml.interfaces.vo.LmOverviewSummaryItemVo;
import com.funmylife.fml.interfaces.vo.LmOverviewSummaryVo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 概览摘要服务 — 从 lm_ability_config 读取 summaryRules，聚合各表数据生成摘要卡片。
 * 移植自 admin-service 的 LmOverviewServiceImpl，用 Hutool JSON 替代 RuoYi JsonUtils。
 */
@Service
@RequiredArgsConstructor
public class OverviewService {

    private final LifeDataRepository lifeDataRepository;

    /**
     * 获取概览摘要列表
     *
     * 流程：
     * 1. 查 lm_ability_config 中 blockKey='overview' 的配置行
     * 2. 解析 summaryRules JSON 数组
     * 3. 预加载各表数据（targets/activities/materials/photos/assets/timelineEvents）
     * 4. 按规则 ID 分派到对应的 build* 方法，生成摘要卡片
     */
    public List<LmOverviewSummaryVo> getOverviewSummaries(Long projectId) {
        LmAbilityConfig config = lifeDataRepository.findAbilityConfig(projectId, "overview");

        List<LmOverviewSummaryVo> summaries = new ArrayList<>();
        if (config == null) {
            return summaries;
        }

        List<JSONObject> rules = parseJsonArray(config.getSummaryRules());
        if (rules == null || rules.isEmpty()) {
            return summaries;
        }

        // 预加载所有相关数据，避免在循环中重复查询
        List<LmGameTarget> targets = queryTargets(projectId);
        List<LmGameActivity> activities = queryActivities(projectId);
        List<LmMaterial> materials = queryMaterials(projectId);
        List<LmPhoto> photos = queryPhotos(projectId);
        List<LmAsset> assets = queryAssets(projectId);
        List<LmTimelineEvent> timelineEvents = queryTimelineEvents(projectId);

        // 按 priority 排序，逐条构建摘要
        rules.stream()
            .filter(this::isEnabled)
            .sorted(Comparator.comparingInt(rule -> rule.getInt("priority", 99)))
            .forEach(rule -> {
                LmOverviewSummaryVo summary = buildSummary(projectId, rule, targets, activities, materials, photos, assets, timelineEvents);
                if (summary != null) {
                    summaries.add(summary);
                }
            });

        return summaries;
    }

    /**
     * 根据规则 ID 分派到对应的构建方法
     */
    private LmOverviewSummaryVo buildSummary(Long projectId,
                                             JSONObject rule,
                                             List<LmGameTarget> targets,
                                             List<LmGameActivity> activities,
                                             List<LmMaterial> materials,
                                             List<LmPhoto> photos,
                                             List<LmAsset> assets,
                                             List<LmTimelineEvent> timelineEvents) {
        String ruleId = rule.getStr("id", "");
        int maxItems = rule.getInt("maxItems", 3);

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

    // ========== 各规则的构建方法 ==========

    /** 当前版本摘要 — 显示活跃版本的名称和标题 */
    private LmOverviewSummaryVo buildCurrentVersionSummary(Long projectId, JSONObject rule) {
        LmGameVersion version = lifeDataRepository.findCurrentGameVersion(projectId);

        LmOverviewSummaryVo summary = baseSummary(rule, "version_activity");
        summary.setValue(version == null ? "暂无当前版本" : version.getName());
        if (version != null) {
            summary.setDescription(version.getTitle());
        }
        return summary;
    }

    /** 目标完成摘要（每日/每周） — 显示 done/total 和未完成条目 */
    private LmOverviewSummaryVo buildTargetSummary(JSONObject rule,
                                                   List<LmGameTarget> targets,
                                                   String type,
                                                   String targetRoute,
                                                   int maxItems) {
        List<LmGameTarget> filteredTargets = targets.stream()
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

    /** 即将结束的活动摘要 */
    private LmOverviewSummaryVo buildEndingActivitySummary(JSONObject rule,
                                                           List<LmGameActivity> activities,
                                                           int maxItems) {
        Set<String> endingStatuses = Set.of("ending", "pending_archive");
        List<LmGameActivity> endingActivities = activities.stream()
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

    /** 素材收集进度摘要 */
    private LmOverviewSummaryVo buildMaterialSummary(JSONObject rule,
                                                     List<LmMaterial> materials,
                                                     int maxItems) {
        List<LmMaterial> activeMaterials = materials.stream()
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

    /** 图册摘要 — 照片数量 + 图册数量 */
    private LmOverviewSummaryVo buildGallerySummary(Long projectId,
                                                    JSONObject rule,
                                                    List<LmPhoto> photos,
                                                    int maxItems) {
        LmOverviewSummaryVo summary = baseSummary(rule, "gallery");
        summary.setValue(photos.size() + "张");
        summary.setDescription(lifeDataRepository.countAlbums(projectId) + " 个图册");
        summary.setItems(toItems(photos.stream()
            .limit(maxItems)
            .map(photo -> item(String.valueOf(photo.getPhotoId()), photo.getCaption() == null ? "照片记录" : photo.getCaption(), "photo_uploaded", "gallery"))
            .toList()));
        return summary;
    }

    /** 资产风险摘要 — 标记 pending/expired 状态的资产 */
    private LmOverviewSummaryVo buildAssetSummary(JSONObject rule,
                                                  List<LmAsset> assets,
                                                  int maxItems) {
        Set<String> riskStatuses = Set.of("pending", "expired");
        List<LmAsset> riskAssets = assets.stream()
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

    /** 最近时间轴事件摘要 */
    private LmOverviewSummaryVo buildTimelineSummary(JSONObject rule,
                                                     List<LmTimelineEvent> timelineEvents,
                                                     int maxItems) {
        List<LmTimelineEvent> visibleEvents = timelineEvents.stream()
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

    // ========== 工具方法 ==========

    /** 创建摘要基础对象（id、ruleId、title、targetRoute） */
    private LmOverviewSummaryVo baseSummary(JSONObject rule, String targetRoute) {
        String ruleId = rule.getStr("id", "");
        LmOverviewSummaryVo summary = new LmOverviewSummaryVo();
        summary.setId("overview-" + ruleId);
        summary.setRuleId(ruleId);
        summary.setTitle(rule.getStr("title", ""));
        summary.setTargetRoute(targetRoute);
        return summary;
    }

    /** 构建摘要条目 */
    private LmOverviewSummaryItemVo item(String id, String label, String status, String targetRoute) {
        LmOverviewSummaryItemVo item = new LmOverviewSummaryItemVo();
        item.setId(id);
        item.setLabel(label == null ? "" : label);
        item.setStatus(status);
        item.setTargetRoute(targetRoute);
        return item;
    }

    /** 空条目按 null 返回，保持前端可选语义 */
    private List<LmOverviewSummaryItemVo> toItems(List<LmOverviewSummaryItemVo> items) {
        return items.isEmpty() ? null : items;
    }

    private List<LmGameTarget> queryTargets(Long projectId) {
        return lifeDataRepository.findGameTargets(projectId);
    }

    private List<LmGameActivity> queryActivities(Long projectId) {
        return lifeDataRepository.findGameActivities(projectId);
    }

    private List<LmMaterial> queryMaterials(Long projectId) {
        return lifeDataRepository.findMaterials(projectId);
    }

    private List<LmPhoto> queryPhotos(Long projectId) {
        return lifeDataRepository.findPhotos(projectId);
    }

    private List<LmAsset> queryAssets(Long projectId) {
        return lifeDataRepository.findAssets(projectId);
    }

    private List<LmTimelineEvent> queryTimelineEvents(Long projectId) {
        return lifeDataRepository.findTimelineEvents(projectId);
    }

    /** 解析 JSON 数组字符串，失败返回 null */
    private List<JSONObject> parseJsonArray(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }
        try {
            return JSONUtil.toList(json, JSONObject.class);
        } catch (Exception e) {
            return null;
        }
    }

    /** 判断规则是否启用（兼容 true/1/Y/布尔值） */
    private boolean isEnabled(JSONObject rule) {
        Object enabled = rule.get("enabled");
        return enabled == null || Boolean.TRUE.equals(enabled) || "1".equals(enabled) || "Y".equals(enabled) || "true".equals(enabled);
    }

    /** 判断字符串是否为真值（1/Y/true） */
    private boolean isTrue(String value) {
        return "1".equals(value) || "Y".equals(value) || "true".equals(value);
    }
}

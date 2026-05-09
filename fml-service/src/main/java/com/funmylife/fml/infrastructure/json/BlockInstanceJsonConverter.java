package com.funmylife.fml.infrastructure.json;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.funmylife.fml.domain.block.*;
import com.funmylife.fml.domain.capability.CapabilityKey;
import com.funmylife.fml.domain.model.LmBlockInstanceConfig;
import com.funmylife.fml.domain.rule.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * 功能块实例 JSON 转换器。
 *
 * <p>当前数据库使用 lm_block_instance_config 保存功能块实例配置，其中 capabilities、navigation、
 * summaryRules、timeline、aiRules、security 等字段都是 JSON 字符串。该转换器负责把这些
 * 持久化字段转换为明确的领域对象，避免 application 层继续直接处理 JSONObject。</p>
 *
 * <p>第一阶段会保留原始 JSON 字符串，方便接口返回和保存仍与前端当前结构对齐。后续规则完全
 * 类型化后，可以逐步减少 rawJson 字段的使用。</p>
 */
@Component
public class BlockInstanceJsonConverter {

    /**
     * 把 LmBlockInstanceConfig 领域模型转换为 BlockInstance 领域对象。
     *
     * @param config 从 lm_block_instance_config 表读取出的配置模型
     * @return 功能块实例；入参为空时返回 null
     */
    public BlockInstance toBlockInstance(LmBlockInstanceConfig config) {
        if (config == null) {
            return null;
        }

        BlockInstance block = new BlockInstance();
        block.setBlockInstanceId(config.getConfigId());
        block.setProjectId(config.getProjectId());
        block.setBlockKey(BlockKey.of(config.getBlockKey()));
        block.setDisplayName(config.getDisplayName());
        block.setEnabled(isTrue(config.getEnabled()));
        block.setCapabilities(parseCapabilities(config.getCapabilities()));
        block.setNavigation(parseNavigation(config.getNavigation()));
        block.setSummaryRules(parseSummaryRules(config.getSummaryRules()));
        block.setTimelineRules(parseTimelineRules(config.getTimeline()));
        block.setArchiveRule(parseArchiveRule(config.getBehavior()));
        block.setAiRule(parseAiRule(config.getAiRules()));
        block.setSecurityRule(parseSecurityRule(config.getSecurity()));
        block.setFieldsRaw(config.getFields());
        block.setBehaviorRaw(config.getBehavior());
        block.setCapabilitiesRaw(config.getCapabilities());
        block.setNavigationRaw(config.getNavigation());
        block.setSummaryRulesRaw(config.getSummaryRules());
        block.setTimelineRaw(config.getTimeline());
        block.setAiRulesRaw(config.getAiRules());
        block.setSecurityRaw(config.getSecurity());
        block.setRemark(config.getRemark());
        block.setCreateBy(config.getCreateBy());
        block.setCreateTime(config.getCreateTime());
        return block;
    }

    /**
     * 把 BlockInstance 领域对象转换回功能块实例配置持久化模型。
     *
     * @param block 功能块实例领域对象
     * @return 功能块实例配置持久化领域模型
     */
    public LmBlockInstanceConfig toBlockInstanceConfig(BlockInstance block) {
        if (block == null) {
            return null;
        }
        validateRawJson(block);

        LmBlockInstanceConfig config = new LmBlockInstanceConfig();
        config.setConfigId(block.getBlockInstanceId());
        config.setProjectId(block.getProjectId());
        config.setBlockKey(block.getBlockKey() == null ? null : block.getBlockKey().asString());
        config.setDisplayName(block.getDisplayName());
        config.setEnabled(block.isEnabled() ? "1" : "0");
        config.setCapabilities(block.getCapabilitiesRaw());
        config.setNavigation(block.getNavigationRaw());
        config.setSummaryRules(block.getSummaryRulesRaw());
        config.setFields(block.getFieldsRaw());
        config.setBehavior(block.getBehaviorRaw());
        config.setTimeline(block.getTimelineRaw());
        config.setAiRules(block.getAiRulesRaw());
        config.setSecurity(block.getSecurityRaw());
        config.setRemark(block.getRemark());
        return config;
    }

    /**
     * 校验功能块实例中的原始 JSON 配置。
     *
     * <p>保存接口仍然接收前端传来的 JSON 字符串，但这些字符串不能未经校验直接落库。
     * 这里会复用读取侧解析逻辑，确保 capabilities、navigation、summaryRules 等字段
     * 至少能被转换为领域对象。</p>
     *
     * @param block 待保存的功能块实例
     */
    public void validateRawJson(BlockInstance block) {
        parseCapabilities(block.getCapabilitiesRaw());
        parseNavigation(block.getNavigationRaw());
        parseSummaryRules(block.getSummaryRulesRaw());
        parseTimelineRules(block.getTimelineRaw());
        parseArchiveRule(block.getBehaviorRaw());
        parseAiRule(block.getAiRulesRaw());
        parseSecurityRule(block.getSecurityRaw());
        validateJson(block.getFieldsRaw(), "fields");
    }

    private List<CapabilityRef> parseCapabilities(String json) {
        if (isBlank(json)) {
            return List.of();
        }

        JSONArray array = parseArray(json, "capabilities");
        List<CapabilityRef> refs = new ArrayList<>();
        for (Object item : array) {
            CapabilityRef ref = new CapabilityRef();
            if (item instanceof JSONObject object) {
                ref.setCapabilityKey(CapabilityKey.of(object.getStr("capabilityKey", object.getStr("key"))));
                ref.setDisplayName(object.getStr("displayName", object.getStr("name")));
                ref.setEnabled(object.getBool("enabled", true));
                ref.setRoleInBlock(object.getStr("roleInBlock", object.getStr("reason")));
                ref.setConfigRaw(toJsonOrNull(object.get("config")));
            } else {
                ref.setCapabilityKey(CapabilityKey.of(String.valueOf(item)));
            }
            refs.add(ref);
        }
        return refs;
    }

    private BlockNavigation parseNavigation(String json) {
        BlockNavigation navigation = new BlockNavigation();
        navigation.setRawJson(json);
        if (isBlank(json)) {
            navigation.setVisible(true);
            navigation.setOrder(0);
            return navigation;
        }

        JSONObject object = parseObject(json, "navigation");
        navigation.setVisible(object.getBool("visible", true));
        navigation.setOrder(object.getInt("order", 0));
        return navigation;
    }

    private List<SummaryRule> parseSummaryRules(String json) {
        if (isBlank(json)) {
            return List.of();
        }

        JSONArray array = parseArray(json, "summaryRules");
        List<SummaryRule> rules = new ArrayList<>();
        for (Object item : array) {
            if (!(item instanceof JSONObject object)) {
                continue;
            }
            SummaryRule rule = new SummaryRule();
            rule.setId(object.getStr("id"));
            rule.setSource(object.getStr("source"));
            rule.setEnabled(object.getBool("enabled", true));
            rule.setTitle(object.getStr("title"));
            rule.setMaxItems(object.getInt("maxItems", 3));
            rule.setPriority(object.getInt("priority", 99));
            rule.setFiltersRaw(toJsonOrNull(object.get("filters")));
            rule.setDisplayMode(object.getStr("displayMode"));
            rule.setTargetTab(object.getStr("targetTab"));
            rule.setRawJson(object.toString());
            rules.add(rule);
        }
        return rules;
    }

    private List<TimelineRule> parseTimelineRules(String json) {
        if (isBlank(json)) {
            return List.of();
        }

        JSONObject object = parseObject(json, "timeline");
        Object ruleObject = object.get("defaultWriteRule");
        if (!(ruleObject instanceof JSONObject ruleJson)) {
            return List.of();
        }

        TimelineRule rule = new TimelineRule();
        rule.setId("default-write-rule");
        rule.setWriteMode(ruleJson.getStr("mode"));
        rule.setDisplayInOverview(ruleJson.getBool("displayInOverview", false));
        rule.setAiReadable(ruleJson.getBool("aiReadable", false));
        rule.setRawJson(ruleJson.toString());
        return List.of(rule);
    }

    private ArchiveRule parseArchiveRule(String behaviorJson) {
        if (isBlank(behaviorJson)) {
            return null;
        }

        JSONObject behavior = parseObject(behaviorJson, "behavior");
        Object archiveObject = behavior.get("archiveRule");
        if (!(archiveObject instanceof JSONObject archiveJson)) {
            return null;
        }

        ArchiveRule rule = new ArchiveRule();
        rule.setTrigger(archiveJson.getStr("trigger"));
        rule.setScope(archiveJson.getStr("scope"));
        rule.setKeepSummary(archiveJson.getBool("keepSummary", false));
        rule.setRawJson(archiveJson.toString());
        return rule;
    }

    private AiRule parseAiRule(String json) {
        if (isBlank(json)) {
            return null;
        }

        JSONObject object = parseObject(json, "aiRules");
        AiRule rule = new AiRule();
        rule.setReadable(object.getBool("readable", false));
        rule.setWritableAfterConfirm(object.getBool("writableAfterConfirm", true));
        JSONArray allowedUse = object.getJSONArray("allowedUse");
        if (allowedUse != null) {
            rule.setAllowedUse(allowedUse.stream().map(String::valueOf).toList());
        }
        rule.setRawJson(json);
        return rule;
    }

    private SecurityRule parseSecurityRule(String json) {
        if (isBlank(json)) {
            return null;
        }

        JSONObject object = parseObject(json, "security");
        SecurityRule rule = new SecurityRule();
        rule.setSensitivity(object.getStr("sensitivity", "normal"));
        rule.setMaskInOverview(object.getBool("maskInOverview", false));
        rule.setRequireConfirmBeforeExternalWrite(object.getBool("requireConfirmBeforeExternalWrite", true));
        rule.setRawJson(json);
        return rule;
    }

    private JSONArray parseArray(String json, String fieldName) {
        try {
            return JSONUtil.parseArray(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " JSON");
        }
    }

    private JSONObject parseObject(String json, String fieldName) {
        try {
            return JSONUtil.parseObj(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " JSON");
        }
    }

    private String toJsonOrNull(Object value) {
        return value == null ? null : JSONUtil.toJsonStr(value);
    }

    private void validateJson(String json, String fieldName) {
        if (isBlank(json)) {
            return;
        }
        try {
            JSONUtil.parse(json);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid " + fieldName + " JSON");
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private boolean isTrue(String value) {
        return "1".equals(value) || "Y".equals(value) || "true".equalsIgnoreCase(value);
    }
}

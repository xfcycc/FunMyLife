SET NAMES utf8mb4;

-- ----------------------------
-- Life Manager 无限暖暖模拟数据
-- 适用表结构：fml-service/src/main/resources/db/schema/lm_life.sql
-- 项目ID：1001
--
-- 建模口径：
-- 1. 项目管理页读取 lm_project + lm_block_instance_config。
-- 2. 概览页读取 overview 功能块的 summary_rules，再由后端能力聚合版本、目标、活动、素材、图册、资产和时间轴。
-- 3. 活动目标必须关联 version_id / activity_id，不把版本活动任务当成孤立普通任务。
-- 4. 账号资产默认脱敏，敏感内容不进入概览、不允许 AI 默认读取。
-- ----------------------------

-- ----------------------------
-- 0. 清理本项目旧模拟数据，可重复执行
-- ----------------------------
DELETE FROM lm_timeline_event WHERE project_id = 1001;
DELETE FROM lm_photo WHERE project_id = 1001;
DELETE FROM lm_album WHERE project_id = 1001;
DELETE FROM lm_note WHERE project_id = 1001;
DELETE FROM lm_asset WHERE project_id = 1001;
DELETE FROM lm_material WHERE project_id = 1001;
DELETE FROM lm_game_target WHERE project_id = 1001;
DELETE FROM lm_game_activity WHERE project_id = 1001;
DELETE FROM lm_game_version WHERE project_id = 1001;
DELETE FROM lm_block_instance_config WHERE project_id = 1001;
DELETE FROM lm_project_scheme_override WHERE project_id = 1001;
DELETE FROM lm_project WHERE project_id = 1001;

DELETE FROM lm_scheme_version WHERE scheme_version_id = 100001;
DELETE FROM lm_scheme WHERE scheme_id = 'game.infinity-nikki';

-- ----------------------------
-- 1. 方案与项目基础信息
-- ----------------------------
INSERT INTO lm_scheme (
    scheme_id, scheme_name, scheme_type, cycle_type, current_version, status, scheme_source,
    summary, target_user, usage_frequency, complexity, not_for,
    required_on_create, focus_points, capabilities, safety_boundary, example_data,
    remark, create_time, update_time, del_flag
) VALUES (
    'game.infinity-nikki',
    '无限暖暖方案',
    'game',
    'long_running',
    '0.1.0',
    'active',
    'official',
    '面向长期游玩无限暖暖的项目方案，重点管理任务、版本活动、素材图册、账号资产和长期时间轴。',
    '长期玩无限暖暖，希望把日常、活动、拍照、素材和账号资料放在一个项目里管理的用户。',
    '每日查看，活动和版本节点前高频使用。',
    'standard',
    '不适合只想保存单条游戏笔记，且不需要版本、活动、目标和资产管理的场景。',
    '[{"key":"server","label":"服务器","required":false},{"key":"timezone","label":"时区","required":true,"defaultValue":"Asia/Shanghai"}]',
    '[{"id":"today-and-weekly-targets","title":"今日和本周应该完成什么","priority":"high"},{"id":"version-activity-deadline","title":"版本活动和倒计时","priority":"high"},{"id":"photos-and-materials","title":"素材、搭配和图册记录","priority":"medium"},{"id":"account-assets","title":"账号资产安全","priority":"medium"},{"id":"long-term-timeline","title":"长期游玩回顾","priority":"high"}]',
    '[{"key":"summary_reminder","name":"摘要提醒"},{"key":"target_system","name":"目标系统"},{"key":"cycle_version","name":"周期与版本"},{"key":"activity_countdown","name":"活动与倒计时"},{"key":"collection_growth","name":"收集与养成"},{"key":"media_record","name":"媒体记录"},{"key":"asset_profile","name":"资料资产"},{"key":"timeline_review","name":"时间轴回顾"},{"key":"ai_suggestion","name":"AI 总结建议"}]',
    '{"sensitiveFields":["账号","UID","支付凭证","兑换码","外部渠道 Token"],"aiReadableByDefault":false,"externalWritePolicy":"confirm_required","notes":["账号资产只在异常时进入概览。","AI 可以总结目标和活动，但不能无确认写入敏感资产。"]}',
    '{"type":"seed","projectId":1001,"projectName":"无限暖暖"}',
    '无限暖暖最小闭环模拟方案',
    '2026-04-20 10:00:00',
    '2026-05-09 10:00:00',
    '0'
);

INSERT INTO lm_scheme_version (
    scheme_version_id, scheme_id, scheme_version, status, protocol_json, change_log,
    published_at, create_time, update_time, del_flag
) VALUES (
    100001,
    'game.infinity-nikki',
    '0.1.0',
    'active',
    '{"schemeId":"game.infinity-nikki","version":"0.1.0","navigation":["overview","targets","version_activity","gallery","timeline","assets"],"managementSections":["ability-configs","project-base","game-rules","media-ai"]}',
    '首版无限暖暖项目闭环：版本活动、目标、素材、图册、资产、时间轴和项目管理配置。',
    '2026-04-20 10:00:00',
    '2026-04-20 10:00:00',
    '2026-05-09 10:00:00',
    '0'
);

INSERT INTO lm_project (
    project_id, project_name, description, cover_src, cover_alt, status,
    scheme_id, scheme_version_id, scheme_version, scheme_type, cycle_type,
    reminder_channels, ai_enabled, security_policy, tags, stats, remark,
    tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES (
    1001,
    '无限暖暖',
    '收集美好的瞬间，搭配无限的可能',
    'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&q=80&w=800&h=400',
    '无限暖暖项目封面',
    'active',
    'game.infinity-nikki',
    100001,
    '0.1.0',
    'game',
    'long_running',
    '["app","feishu"]',
    '1',
    '{"maskSensitiveInOverview":true,"assetExternalWrite":"confirm_required","aiReadableSensitiveAsset":false}',
    '[{"label":"游戏"},{"label":"进行中","tone":"success"},{"label":"换装"},{"label":"日常"},{"label":"活动"},{"label":"收集"}]',
    '[{"label":"创建时间","value":"2024-12-15"},{"label":"今日任务进度","value":"5/8"},{"label":"本周完成率","value":"57%"},{"label":"项目笔记","value":"12"},{"label":"图册数量","value":"8"}]',
    '记录游戏日常、活动提醒、版本更新、抽卡计划、搭配笔记等内容。',
    '000000',
    NULL,
    1,
    '2024-12-15 09:00:00',
    1,
    '2026-05-09 10:00:00',
    '0'
);

-- ----------------------------
-- 2. 功能块实例配置：项目管理页事实源
-- ----------------------------
INSERT INTO lm_block_instance_config (
    config_id, project_id, scheme_id, scheme_version_id, scheme_instance_id, block_key, display_name,
    enabled, capabilities, navigation, summary_rules, fields, behavior, timeline, ai_rules, security,
    config_source, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(
    100101, 1001, 'game.infinity-nikki', 100001, 'cfg-overview', 'overview', '概览',
    '1',
    '["summary_reminder","ai_suggestion"]',
    '{"visible":true,"order":1}',
    '[{"id":"sum-current-version","projectId":"1001","source":"version","enabled":true,"title":"当前版本","maxItems":1,"priority":1,"filters":{},"displayMode":"metric","targetTab":"version_activity"},{"id":"sum-today-targets","projectId":"1001","source":"targets","enabled":true,"title":"今日目标","maxItems":3,"priority":2,"filters":{"targetTypes":["daily"]},"displayMode":"metric","targetTab":"targets"},{"id":"sum-weekly-targets","projectId":"1001","source":"targets","enabled":true,"title":"本周目标","maxItems":3,"priority":3,"filters":{"targetTypes":["weekly"]},"displayMode":"compact","targetTab":"targets"},{"id":"sum-ending-activities","projectId":"1001","source":"activities","enabled":true,"title":"即将结束活动","maxItems":3,"priority":4,"filters":{"activityStatuses":["ending","pending_archive"],"withinHours":72},"displayMode":"list","targetTab":"version_activity"},{"id":"sum-material-progress","projectId":"1001","source":"materials","enabled":true,"title":"素材收集","maxItems":3,"priority":5,"filters":{},"displayMode":"compact","targetTab":"targets"},{"id":"sum-gallery-recent","projectId":"1001","source":"gallery","enabled":true,"title":"图册记录","maxItems":3,"priority":6,"filters":{},"displayMode":"compact","targetTab":"gallery"},{"id":"sum-asset-risk","projectId":"1001","source":"assets","enabled":true,"title":"资产风险","maxItems":2,"priority":7,"filters":{},"displayMode":"metric","targetTab":"assets"},{"id":"sum-recent-timeline","projectId":"1001","source":"timeline","enabled":true,"title":"最近时间轴","maxItems":3,"priority":8,"filters":{},"displayMode":"timeline","targetTab":"timeline"}]',
    NULL,
    NULL,
    NULL,
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["概览建议","当前重点整理"]}',
    NULL,
    'scheme_default', '概览只展示摘要，不承载完整管理。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100102, 1001, 'game.infinity-nikki', 100001, 'cfg-targets', 'targets', '任务',
    '1',
    '["target_system","summary_reminder","timeline_review"]',
    '{"visible":true,"order":2}',
    '[]',
    '[{"key":"title","label":"目标名称","type":"text","required":true},{"key":"type","label":"目标类型","type":"select","required":true,"options":["日常","周常","活动","自定义"]},{"key":"progress","label":"进度","type":"progress"}]',
    '{"resetRules":[{"type":"daily","time":"04:00"},{"type":"weekly","weekday":1,"time":"04:00"},{"type":"activity","inheritFromActivity":true}],"archiveRule":{"type":"on_version_end","includeTargets":true,"includePhotos":false,"includeNotes":false,"generateTimelineSummary":true}}',
    '{"enabled":true,"defaultWriteRule":{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["今日建议","周回顾","活动目标提醒"]}',
    NULL,
    'scheme_default', '目标系统承载日常、周常、活动目标和自定义目标。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100103, 1001, 'game.infinity-nikki', 100001, 'cfg-version-activity', 'version_activity', '活动与版本',
    '1',
    '["cycle_version","activity_countdown","target_system","timeline_review"]',
    '{"visible":true,"order":3}',
    '[]',
    '[{"key":"title","label":"活动名称","type":"text","required":true},{"key":"startAt","label":"开始时间","type":"date","required":true},{"key":"endAt","label":"结束时间","type":"date","required":true}]',
    '{"reminderRules":[{"enabled":true,"channels":["app","feishu"],"beforeMinutes":[4320,1440,120]}],"archiveRule":{"type":"on_activity_end","includeTargets":true,"includePhotos":true,"includeNotes":true,"generateTimelineSummary":true}}',
    '{"enabled":true,"defaultWriteRule":{"mode":"detail","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["活动倒计时","活动归档复盘","版本总结"]}',
    NULL,
    'scheme_default', '版本活动是活动目标、素材、图册和笔记的上层容器。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100104, 1001, 'game.infinity-nikki', 100001, 'cfg-materials', 'materials', '素材收集',
    '1',
    '["collection_growth","summary_reminder","timeline_review"]',
    '{"visible":false,"order":4}',
    '[]',
    '[{"key":"name","label":"素材/套装名称","type":"text","required":true},{"key":"type","label":"收集类型","type":"select","required":true,"options":["套装","素材","代币","收集项"]},{"key":"progress","label":"收集进度","type":"progress","required":true}]',
    NULL,
    '{"enabled":true,"defaultWriteRule":{"mode":"detail","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["素材缺口提醒","活动兑换建议"]}',
    NULL,
    'scheme_default', '第一阶段不进主导航，但参与概览摘要和 AI 复盘。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100105, 1001, 'game.infinity-nikki', 100001, 'cfg-gallery', 'gallery', '图册',
    '1',
    '["media_record","timeline_review","ai_suggestion"]',
    '{"visible":true,"order":4}',
    '[]',
    '[{"key":"albumId","label":"所属图册","type":"select","required":true},{"key":"caption","label":"图片说明","type":"text"},{"key":"takenAt","label":"拍摄时间","type":"date"}]',
    NULL,
    '{"enabled":true,"defaultWriteRule":{"mode":"detail","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["截图整理","搭配复盘","活动照片归档"]}',
    NULL,
    'scheme_default', '图册承载截图、搭配照片和活动照片。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100106, 1001, 'game.infinity-nikki', 100001, 'cfg-timeline', 'timeline', '时间轴',
    '1',
    '["timeline_review","ai_suggestion"]',
    '{"visible":true,"order":5}',
    '[]',
    NULL,
    NULL,
    '{"enabled":true,"defaultWriteRule":{"mode":"detail","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["日回顾","周回顾","版本复盘"]}',
    NULL,
    'scheme_default', '长期项目回顾，不只是最近动态。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100107, 1001, 'game.infinity-nikki', 100001, 'cfg-assets', 'assets', '账号资产',
    '1',
    '["asset_profile","timeline_review"]',
    '{"visible":true,"order":6}',
    '[]',
    '[{"key":"name","label":"资产名称","type":"text","required":true},{"key":"type","label":"资产类型","type":"select","required":true,"options":["官方账号","辅助账号","Switch 账号","支付凭证","兑换码"]},{"key":"status","label":"安全状态","type":"select","required":true,"options":["已保护","已绑定","待处理","已过期"]}]',
    NULL,
    '{"enabled":true,"defaultWriteRule":{"mode":"exception_only","displayInOverview":false,"aiReadable":false}}',
    NULL,
    '{"sensitivity":"sensitive","maskInOverview":true,"requireConfirmBeforeExternalWrite":true}',
    'scheme_default', '账号、UID、兑换码和支付凭证默认不暴露到概览。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
),
(
    100108, 1001, 'game.infinity-nikki', 100001, 'cfg-ai', 'ai', 'AI 助手',
    '1',
    '["ai_suggestion","summary_reminder","timeline_review"]',
    '{"visible":false,"order":99}',
    '[]',
    NULL,
    NULL,
    '{"enabled":true,"defaultWriteRule":{"mode":"exception_only","displayInOverview":true,"aiReadable":true}}',
    '{"readable":true,"writableAfterConfirm":true,"allowedUse":["概览建议","时间轴总结","版本复盘"]}',
    NULL,
    'scheme_default', 'AI 只生成建议，关键写入仍需确认。', '000000', NULL, 1, '2026-04-20 10:00:00', 1, '2026-05-09 10:00:00', '0'
);

-- ----------------------------
-- 3. 版本与活动
-- ID 映射：5.1=5101，5.2=5201，5.0=5001
-- ----------------------------
INSERT INTO lm_game_version (
    version_id, project_id, block_key, name, title, start_at, end_at, status,
    highlights, summary, archived_at, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(5101, 1001, 'version_activity', '5.1', '5.1 繁花季节', '2026-04-18 05:00:00', '2026-05-20 05:00:00', 'active', '["繁花季节限时活动","奇迹之旅第二章","晨曦花园套装"]', NULL, NULL, '当前版本，支撑概览当前版本摘要。', '000000', NULL, 1, '2026-04-18 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(5201, 1001, 'version_activity', '5.2', '5.2 心愿巡礼', '2026-05-20 05:00:00', '2026-06-26 05:00:00', 'upcoming', '["前瞻特别节目","新区域预告","搭配主题更新"]', NULL, NULL, '下个版本预告。', '000000', NULL, 1, '2026-05-01 09:00:00', 1, '2026-05-09 10:00:00', '0'),
(5001, 1001, 'version_activity', '5.0', '5.0 星愿启程', '2026-03-12 05:00:00', '2026-04-18 05:00:00', 'archived', '["主线剧情更新","星愿套装收集","心愿原野探索"]', '完成主线剧情与心愿原野探索，保留 42 张截图和 3 篇攻略笔记。', '2026-04-18 22:30:00', '历史版本归档。', '000000', NULL, 1, '2026-03-12 05:00:00', 1, '2026-04-18 22:30:00', '0');

INSERT INTO lm_game_activity (
    activity_id, project_id, block_key, version_id, title, description, start_at, end_at,
    status, priority, cover, reminder_rule, archived_at, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(510101, 1001, 'version_activity', 5101, '奇迹之旅·第二章', '完成奇迹之旅第二章全部关卡，获取限定套装奖励。', '2026-04-24 05:00:00', '2026-05-12 05:00:00', 'ending', 'high', 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=100&h=100&q=80', '{"enabled":true,"channels":["app","feishu"],"beforeMinutes":[4320,1440,120]}', NULL, '倒计时活动，关联活动目标、截图和笔记。', '000000', NULL, 1, '2026-04-24 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(510102, 1001, 'version_activity', 5101, '繁花季节 限时活动', '春季限定活动，收集花瓣兑换专属奖励。', '2026-04-28 05:00:00', '2026-05-12 05:00:00', 'ending', 'high', 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=100&h=100&q=80', '{"enabled":true,"channels":["app"],"beforeMinutes":[4320,1440]}', NULL, '素材和活动目标重点活动。', '000000', NULL, 1, '2026-04-28 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(510103, 1001, 'version_activity', 5101, '累计充值返利', '累计充值达到指定金额可领取返利奖励，支付相关信息仅记录为资产状态，不在概览展示明文。', '2026-04-18 05:00:00', '2026-05-20 05:00:00', 'ending', 'normal', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=100&h=100&q=80', '{"enabled":false,"channels":["app"],"beforeMinutes":[1440]}', NULL, '活动倒计时但无活动目标。', '000000', NULL, 1, '2026-04-18 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(500101, 1001, 'version_activity', 5001, '星愿收集季', '5.0 版本主活动，已完成并归档。', '2026-03-12 05:00:00', '2026-04-18 05:00:00', 'archived', 'normal', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=100&h=100&q=80', '{"enabled":true,"channels":["app"],"beforeMinutes":[1440]}', '2026-04-18 22:30:00', '历史归档活动。', '000000', NULL, 1, '2026-03-12 05:00:00', 1, '2026-04-18 22:30:00', '0');

-- ----------------------------
-- 4. 目标系统：日常 5/8，本周 4/7，活动目标挂在活动下
-- ----------------------------
INSERT INTO lm_game_target (
    target_id, project_id, block_key, type, title, description, status,
    progress_current, progress_target, reset_rule, version_id, activity_id, due_at, priority,
    pinned_to_overview, timeline_rule, archived_at, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1001001, 1001, 'targets', 'daily', '每日签到', '登录并领取每日签到奖励。', 'done', 1, 1, '{"type":"daily","time":"04:00"}', NULL, NULL, '2026-05-09 04:00:00', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 08:00:00', '0'),
(1001002, 1001, 'targets', 'daily', '心愿原野探索', '完成心愿原野日常探索。', 'done', 1, 1, '{"type":"daily","time":"04:00"}', NULL, NULL, '2026-05-09 04:00:00', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 08:20:00', '0'),
(1001003, 1001, 'targets', 'daily', '搭配竞技场挑战', '完成今日搭配竞技场挑战。', 'done', 1, 1, '{"type":"daily","time":"04:00"}', NULL, NULL, '2026-05-09 04:00:00', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 09:00:00', '0'),
(1001004, 1001, 'targets', 'daily', '每日灵感任务', '完成今日灵感任务。', 'done', 1, 1, '{"type":"daily","time":"04:00"}', NULL, NULL, '2026-05-09 04:00:00', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 09:20:00', '0'),
(1001005, 1001, 'targets', 'daily', '采集素材', '采集花瓣、鱼类和基础素材。', 'done', 50, 50, '{"type":"daily","time":"04:00"}', 5101, NULL, '2026-05-09 04:00:00', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 09:40:00', '0'),
(1001006, 1001, 'targets', 'daily', '捕捉奇想星', '今日捕捉 3 个奇想星。', 'todo', 0, 3, '{"type":"daily","time":"04:00"}', 5101, NULL, '2026-05-09 23:59:59', 'normal', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 10:00:00', '0'),
(1001007, 1001, 'targets', 'daily', '愿望梦境挑战', '完成 2 次愿望梦境挑战。', 'todo', 0, 2, '{"type":"daily","time":"04:00"}', 5101, NULL, '2026-05-09 23:59:59', 'high', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 10:00:00', '0'),
(1001008, 1001, 'targets', 'daily', '体力消耗', '今日体力消耗到安全阈值。', 'todo', 120, 180, '{"type":"daily","time":"04:00"}', 5101, NULL, '2026-05-09 23:59:59', 'high', '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-09 04:10:00', 1, '2026-05-09 10:00:00', '0'),
(1001101, 1001, 'targets', 'weekly', '完成搭配评选 3 次', '本周搭配评选参与次数。', 'done', 3, 3, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'normal', '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-08 21:00:00', '0'),
(1001102, 1001, 'targets', 'weekly', '通关心愿梦境 10 次', '本周心愿梦境挑战次数。', 'todo', 6, 10, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'normal', '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-09 10:00:00', '0'),
(1001103, 1001, 'targets', 'weekly', '收集奇想星 80 个', '本周探索奇想星收集目标。', 'done', 80, 80, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'normal', '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-08 20:00:00', '0'),
(1001104, 1001, 'targets', 'weekly', '完成灵感任务 14 次', '本周累计灵感任务。', 'done', 14, 14, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'normal', '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-08 20:00:00', '0'),
(1001105, 1001, 'targets', 'weekly', '参与搭配赛 2 次', '本周搭配赛参与。', 'todo', 1, 2, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'high', '0', '{"mode":"exception_only","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-09 10:00:00', '0'),
(1001106, 1001, 'targets', 'weekly', '提升搭配师等级', '本周提升搭配师经验。', 'todo', 38, 50, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'high', '0', '{"mode":"exception_only","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-09 10:00:00', '0'),
(1001107, 1001, 'targets', 'weekly', '收集套装部件 30 个', '本周收集套装部件目标。', 'done', 30, 30, '{"type":"weekly","weekday":1,"time":"04:00"}', 5101, NULL, '2026-05-12 04:00:00', 'normal', '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-05-05 04:00:00', 1, '2026-05-08 20:00:00', '0'),
(1001201, 1001, 'targets', 'activity', '奇迹之旅第二章·完成 5 个关卡', '活动「奇迹之旅·第二章」关卡目标。', 'done', 5, 5, '{"type":"activity","inheritFromActivity":true}', 5101, 510101, '2026-05-12 05:00:00', 'high', '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-04-24 05:00:00', 1, '2026-05-08 18:30:00', '0'),
(1001202, 1001, 'targets', 'activity', '繁花季节·收集花瓣', '收集繁花花瓣兑换专属奖励。', 'todo', 38, 100, '{"type":"activity","inheritFromActivity":true}', 5101, 510102, '2026-05-12 05:00:00', 'high', '1', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-04-28 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(1001203, 1001, 'targets', 'activity', '繁花季节·完成挑战', '完成繁花季节全部挑战。', 'todo', 2, 5, '{"type":"activity","inheritFromActivity":true}', 5101, 510102, '2026-05-12 05:00:00', 'high', '1', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-04-28 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(1001301, 1001, 'targets', 'custom', '本周拍照计划：晨曦花园套装', '围绕晨曦花园套装完成三组拍照。', 'todo', 1, 3, '{"type":"custom"}', 5101, NULL, '2026-05-12 23:59:59', 'normal', '1', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', NULL, '', '000000', NULL, 1, '2026-04-29 10:00:00', 1, '2026-05-09 10:00:00', '0');

-- ----------------------------
-- 5. 素材、笔记、资产
-- ----------------------------
INSERT INTO lm_material (
    material_id, project_id, block_key, name, type, description, status, current, target,
    version_id, activity_id, target_id, timeline_rule, note, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1003001, 1001, 'materials', '晨曦花园套装', 'outfit', '5.1 版本新增五星套装。', 'collecting', 7, 10, 5101, NULL, 1001301, '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '本周拍照计划的主套装。', '', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1003002, 1001, 'materials', '繁花花瓣', 'material', '繁花季节活动兑换材料。', 'collecting', 38, 100, 5101, 510102, 1001202, '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', '优先兑换限定部件。', '', '000000', NULL, 1, '2026-04-28 05:00:00', 1, '2026-05-09 10:00:00', '0'),
(1003003, 1001, 'materials', '心愿梦境代币', 'currency', '愿望梦境挑战代币。', 'collecting', 120, 300, 5101, NULL, 1001007, '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', '优先换限定染色材料。', '', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1003004, 1001, 'materials', '5.0 星愿收集', 'collection', '5.0 版本星愿收集归档。', 'completed', 42, 42, 5001, 500101, NULL, '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '历史版本已完成。', '', '000000', NULL, 1, '2026-03-12 08:00:00', 1, '2026-04-15 10:00:00', '0');

INSERT INTO lm_note (
    note_id, project_id, block_key, title, content, type, category, category_label,
    version_id, activity_id, target_id, pinned_to_overview, timeline_rule, remark,
    tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1006001, 1001, 'notes', '5.1 版本新增套装分析与抽取建议', '5.1 版本新增了 3 套五星套装和 5 套四星套装，其中「晨曦花园」套装最适合当前拍照计划。', 'guide', 'strategy', '攻略', 5101, NULL, NULL, '1', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-09 08:00:00', 1, '2026-05-09 08:00:00', '0'),
(1006002, 1001, 'notes', '心愿原野探索路线规划', '心愿原野推荐从北向南探索，每个区域安排 3 到 5 个奇想星收集点。', 'note', 'explore', '探索', 5101, NULL, 1001006, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-08 14:00:00', 1, '2026-05-08 14:00:00', '0'),
(1006003, 1001, 'notes', '搭配赛主题：春日茶会灵感整理', '本周搭配赛主题为春日茶会，推荐浅色系、花朵配饰和晨曦花园部件。', 'note', 'coordination', '搭配', 5101, 510102, 1001105, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-07 09:00:00', 1, '2026-05-07 09:00:00', '0'),
(1006004, 1001, 'notes', '素材收集清单汇总', '当前版本主要素材：繁花花瓣 100、星光碎片 50、梦境之羽 30。', 'note', 'material', '素材', 5101, 510102, 1001202, '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-06 16:00:00', 1, '2026-05-06 16:00:00', '0'),
(1006005, 1001, 'notes', '5.0 版本剧情回顾与彩蛋整理', '5.0 版本主线剧情隐藏彩蛋整理，已归档到版本回顾。', 'review', 'story', '剧情', 5001, 500101, NULL, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-04-20 11:00:00', 1, '2026-04-25 20:00:00', '0'),
(1006006, 1001, 'notes', '奇迹之旅第二章通关要点', '第二章后半段优先提高典雅和清新评分，注意保存通关截图。', 'guide', 'activity', '活动', 5101, 510101, 1001201, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-05 12:00:00', 1, '2026-05-05 12:00:00', '0'),
(1006007, 1001, 'notes', '体力消耗路线', '体力优先用于愿望梦境挑战，再补基础素材缺口。', 'note', 'daily', '日常', 5101, NULL, 1001008, '0', '{"mode":"daily_summary","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-05 21:00:00', 1, '2026-05-05 21:00:00', '0'),
(1006008, 1001, 'notes', '晨曦花园拍照构图', '套装拍摄建议使用花园、晨光和低饱和背景。', 'note', 'photo', '拍照', 5101, NULL, 1001301, '1', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-04 20:00:00', 1, '2026-05-04 20:00:00', '0'),
(1006009, 1001, 'notes', '5.2 前瞻待确认内容', '前瞻节目时间、版本维护窗口和新区域资料待确认。', 'note', 'version', '版本', 5201, NULL, NULL, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-03 19:30:00', 1, '2026-05-03 19:30:00', '0'),
(1006010, 1001, 'notes', '账号安全检查记录', '账号资产检查只记录状态，不写入账号明文。', 'note', 'security', '资产', 5101, NULL, NULL, '0', '{"mode":"exception_only","displayInOverview":false,"aiReadable":false}', '', '000000', NULL, 1, '2026-05-02 18:00:00', 1, '2026-05-02 18:00:00', '0'),
(1006011, 1001, 'notes', '本周遗漏风险', '愿望梦境和搭配赛进度偏慢，需要安排集中时间。', 'review', 'weekly', '周报', 5101, NULL, NULL, '0', '{"mode":"weekly_summary","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-05-01 22:00:00', 1, '2026-05-01 22:00:00', '0'),
(1006012, 1001, 'notes', '活动归档检查清单', '归档活动时带上目标、照片、笔记和素材进度，再生成时间轴总结。', 'guide', 'archive', '归档', 5101, 510101, NULL, '0', '{"mode":"detail","displayInOverview":true,"aiReadable":true}', '', '000000', NULL, 1, '2026-04-30 10:00:00', 1, '2026-04-30 10:00:00', '0');

INSERT INTO lm_asset (
    asset_id, project_id, block_key, name, type, category, value, status, status_label,
    sensitivity, icon, description, linked_url, expires_at, notes, remark, timeline_rule, ai_readable, mask_in_overview, archived_at,
    tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1007001, 1001, 'assets', '官方账号（主）', 'official_account', 'account', '10086xxxx', 'protected', '已保护', 'sensitive', 'material-symbols:lock-outline-rounded', '主要游戏账号，已绑定手机和邮箱。', '', NULL, '概览只展示安全状态，不展示明文。', '', '{"mode":"exception_only","displayInOverview":false,"aiReadable":false}', '0', '1', NULL, '000000', NULL, 1, '2025-04-10 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1007002, 1001, 'assets', '小号-搭配测试', 'sub_account', 'account', '', 'protected', '已保护', 'normal', 'material-symbols:face-outline-rounded', '用于测试搭配方案的辅助账号。', '', NULL, '', '', '{"mode":"exception_only","displayInOverview":false,"aiReadable":false}', '0', '1', NULL, '000000', NULL, 1, '2025-06-15 08:00:00', 1, '2026-04-20 10:00:00', '0'),
(1007003, 1001, 'assets', 'Nintendo Switch 账号', 'switch_account', 'account', '', 'protected', '已保护', 'normal', 'material-symbols:sports-esports-outline-rounded', 'Switch 平台关联账号。', 'https://accounts.nintendo.com', NULL, '', '', '{"mode":"exception_only","displayInOverview":false,"aiReadable":false}', '0', '1', NULL, '000000', NULL, 1, '2025-08-20 08:00:00', 1, '2026-03-15 10:00:00', '0');

-- ----------------------------
-- 6. 图册与照片：8 个图册，覆盖概览最近图册和活动归属
-- ----------------------------
INSERT INTO lm_album (
    album_id, project_id, block_key, name, cover_url, remark,
    tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1004001, 1001, 'gallery', '心愿原野探索', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', '探索截图主图册。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004002, 1001, 'gallery', '搭配竞技场', 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', '搭配赛与竞技场截图。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004003, 1001, 'gallery', '奇迹之旅', 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', '奇迹之旅活动截图。', '000000', NULL, 1, '2026-04-24 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004004, 1001, 'gallery', '繁花季节', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80', '繁花季节限时活动图册。', '000000', NULL, 1, '2026-04-28 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004005, 1001, 'gallery', '时装展示', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=150&h=100&q=80', '套装展示。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004006, 1001, 'gallery', '风景截图', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', '风景截图。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004007, 1001, 'gallery', '活动记录', 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', '活动过程记录。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0'),
(1004008, 1001, 'gallery', '其他', 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', '其他截图。', '000000', NULL, 1, '2026-04-20 08:00:00', 1, '2026-05-09 10:00:00', '0');

INSERT INTO lm_photo (
    photo_id, project_id, block_key, album_id, url, thumbnail, caption, taken_at,
    version_id, activity_id, target_id, sensitivity, ai_readable, remark,
    tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1005001, 1001, 'gallery', 1004001, 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', '心愿原野全景', '2026-05-09 21:40:00', 5101, NULL, 1001002, 'normal', '1', '', '000000', NULL, 1, '2026-05-09 21:40:00', 1, '2026-05-09 21:40:00', '0'),
(1005002, 1001, 'gallery', 1004002, 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1518709268805-4e9042af9f23?auto=format&fit=crop&w=150&h=100&q=80', '竞技场精彩瞬间', '2026-05-09 18:00:00', 5101, NULL, 1001003, 'normal', '1', '', '000000', NULL, 1, '2026-05-09 18:00:00', 1, '2026-05-09 18:00:00', '0'),
(1005003, 1001, 'gallery', 1004003, 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', '奇迹之旅关卡', '2026-05-08 20:00:00', 5101, 510101, 1001201, 'normal', '1', '', '000000', NULL, 1, '2026-05-08 20:00:00', 1, '2026-05-08 20:00:00', '0'),
(1005004, 1001, 'gallery', 1004004, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80', '繁花季节场景', '2026-05-07 15:30:00', 5101, 510102, 1001202, 'normal', '1', '', '000000', NULL, 1, '2026-05-07 15:30:00', 1, '2026-05-07 15:30:00', '0'),
(1005005, 1001, 'gallery', 1004005, 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1550745165-9bc0b252726f?auto=format&fit=crop&w=150&h=100&q=80', '晨曦花园套装', '2026-05-06 19:00:00', 5101, NULL, 1001301, 'normal', '1', '', '000000', NULL, 1, '2026-05-06 19:00:00', 1, '2026-05-06 19:00:00', '0'),
(1005006, 1001, 'gallery', 1004001, 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', '原野日落', '2026-05-05 21:00:00', 5101, NULL, 1001002, 'normal', '1', '', '000000', NULL, 1, '2026-05-05 21:00:00', 1, '2026-05-05 21:00:00', '0'),
(1005007, 1001, 'gallery', 1004006, 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1500530855697-b586d89ba3ee?auto=format&fit=crop&w=150&h=100&q=80', '花园晨光', '2026-05-04 08:30:00', 5101, NULL, NULL, 'normal', '1', '', '000000', NULL, 1, '2026-05-04 08:30:00', 1, '2026-05-04 08:30:00', '0'),
(1005008, 1001, 'gallery', 1004007, 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1500534314209-a25ddb2bd429?auto=format&fit=crop&w=150&h=100&q=80', '活动兑换记录', '2026-05-03 20:00:00', 5101, 510102, 1001202, 'normal', '1', '', '000000', NULL, 1, '2026-05-03 20:00:00', 1, '2026-05-03 20:00:00', '0'),
(1005009, 1001, 'gallery', 1004008, 'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1519681393784-d120267933ba?auto=format&fit=crop&w=150&h=100&q=80', '夜景留念', '2026-05-02 22:00:00', 5101, NULL, NULL, 'normal', '1', '', '000000', NULL, 1, '2026-05-02 22:00:00', 1, '2026-05-02 22:00:00', '0'),
(1005010, 1001, 'gallery', 1004005, 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=150&h=100&q=80', '春日茶会搭配', '2026-05-01 17:00:00', 5101, 510102, 1001105, 'normal', '1', '', '000000', NULL, 1, '2026-05-01 17:00:00', 1, '2026-05-01 17:00:00', '0'),
(1005011, 1001, 'gallery', 1004003, 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1490750967868-88cb44cb2754?auto=format&fit=crop&w=150&h=100&q=80', '第二章终章截图', '2026-04-30 21:00:00', 5101, 510101, 1001201, 'normal', '1', '', '000000', NULL, 1, '2026-04-30 21:00:00', 1, '2026-04-30 21:00:00', '0'),
(1005012, 1001, 'gallery', 1004001, 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=400&h=300&q=80', 'https://images.unsplash.com/photo-1618331835717-801e976710b2?auto=format&fit=crop&w=150&h=100&q=80', '旧版本心愿原野', '2026-04-15 20:00:00', 5001, 500101, NULL, 'normal', '1', '', '000000', NULL, 1, '2026-04-15 20:00:00', 1, '2026-04-15 20:00:00', '0');

-- ----------------------------
-- 7. 时间轴：概览最近动态和长期回顾事实源
-- ----------------------------
INSERT INTO lm_timeline_event (
    event_id, project_id, occurred_at, type, title, description, source_block_key,
    version_id, activity_id, target_id, sensitivity, display_in_overview, ai_readable,
    write_mode, confirm_required, remark, tenant_id, create_dept, create_by, create_time, update_by, update_time, del_flag
) VALUES
(1008001, 1001, '2026-05-09 09:00:00', 'target_done', '完成了每日目标', '搭配竞技场挑战', 'targets', 5101, NULL, 1001003, 'normal', '1', '1', 'daily_summary', '0', '', '000000', NULL, 1, '2026-05-09 09:00:00', 1, '2026-05-09 09:00:00', '0'),
(1008002, 1001, '2026-05-09 08:00:00', 'note_created', '新增攻略笔记', '5.1 版本新增套装分析与抽取建议', 'timeline', 5101, NULL, NULL, 'normal', '1', '1', 'detail', '0', '', '000000', NULL, 1, '2026-05-09 08:00:00', 1, '2026-05-09 08:00:00', '0'),
(1008003, 1001, '2026-05-08 21:40:00', 'photo_uploaded', '上传了 6 张截图', '心愿原野探索记录', 'gallery', 5101, NULL, NULL, 'normal', '1', '1', 'daily_summary', '0', '', '000000', NULL, 1, '2026-05-08 21:40:00', 1, '2026-05-08 21:40:00', '0'),
(1008004, 1001, '2026-05-08 18:30:00', 'activity_ending', '活动进度更新', '奇迹之旅·第二章进入最后阶段', 'version_activity', 5101, 510101, 1001201, 'normal', '1', '1', 'detail', '0', '', '000000', NULL, 1, '2026-05-08 18:30:00', 1, '2026-05-08 18:30:00', '0'),
(1008005, 1001, '2026-05-07 20:30:00', 'material_completed', '素材目标更新', '繁花花瓣收集进度达到 38/100', 'materials', 5101, 510102, 1001202, 'normal', '1', '1', 'daily_summary', '0', '', '000000', NULL, 1, '2026-05-07 20:30:00', 1, '2026-05-07 20:30:00', '0'),
(1008006, 1001, '2026-05-06 19:00:00', 'photo_uploaded', '拍照计划更新', '晨曦花园套装完成第 1 组照片', 'gallery', 5101, NULL, 1001301, 'normal', '1', '1', 'detail', '0', '', '000000', NULL, 1, '2026-05-06 19:00:00', 1, '2026-05-06 19:00:00', '0'),
(1008007, 1001, '2026-05-05 22:00:00', 'ai_summary_generated', '生成本周遗漏风险', '愿望梦境和搭配赛进度偏慢，建议安排集中时间。', 'ai', 5101, NULL, NULL, 'normal', '1', '1', 'detail', '1', 'AI 生成候选总结，需确认后写入。', '000000', NULL, 1, '2026-05-05 22:00:00', 1, '2026-05-05 22:00:00', '0'),
(1008008, 1001, '2026-04-18 22:30:00', 'version_archived', '归档 5.0 星愿启程', '保留 42 张截图、3 篇笔记和 1 份版本总结。', 'version_activity', 5001, 500101, NULL, 'normal', '0', '1', 'detail', '0', '', '000000', NULL, 1, '2026-04-18 22:30:00', 1, '2026-04-18 22:30:00', '0');

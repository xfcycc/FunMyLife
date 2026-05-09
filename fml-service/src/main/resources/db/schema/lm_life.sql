SET NAMES utf8mb4;

-- ----------------------------
-- Life Manager schema
-- fml-service 是 Life Manager 业务数据的唯一事实源。
-- 这份 SQL 放在 fml-service 下维护，不再以 admin-service 的脚本作为表结构来源。
-- ----------------------------

-- ----------------------------
-- 1. 方案表
-- 方案不是用户数据，而是创建项目时的默认搭法。
-- ----------------------------
DROP TABLE IF EXISTS lm_scheme;
CREATE TABLE lm_scheme (
    scheme_id          VARCHAR(100) NOT NULL                    COMMENT '方案ID，如 game.infinity-nikki',
    scheme_name        VARCHAR(100) NOT NULL                    COMMENT '方案名称',
    scheme_type        VARCHAR(50)  NOT NULL                    COMMENT '所属类型（game/travel/insurance等）',
    cycle_type         VARCHAR(30)  NOT NULL                    COMMENT '默认周期属性（long_running/staged/hybrid）',
    current_version    VARCHAR(50)  DEFAULT ''                  COMMENT '当前可用方案版本号',
    status             VARCHAR(20)  NOT NULL DEFAULT 'draft'    COMMENT '状态（draft/active/archived）',
    scheme_source      VARCHAR(30)  NOT NULL DEFAULT 'official' COMMENT '来源（official/custom/community/ai）',
    summary            VARCHAR(500) DEFAULT ''                  COMMENT '一句话说明',
    target_user        VARCHAR(500) DEFAULT ''                  COMMENT '目标用户',
    usage_frequency    VARCHAR(100) DEFAULT ''                  COMMENT '使用频率',
    complexity         VARCHAR(30)  DEFAULT 'standard'          COMMENT '默认复杂度（light/standard/advanced）',
    not_for            VARCHAR(500) DEFAULT ''                  COMMENT '不适用场景',
    required_on_create JSON                                      COMMENT '创建项目时需要补充的信息',
    focus_points       JSON                                      COMMENT '关注点列表',
    capabilities       JSON                                      COMMENT '默认能力清单',
    safety_boundary    JSON                                      COMMENT '安全与AI边界',
    example_data       JSON                                      COMMENT '示例数据，必须标记为mock或seed',
    remark             VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    create_time        DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_time        DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag           CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (scheme_id),
    KEY idx_scheme_type (scheme_type),
    KEY idx_scheme_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-方案表';

-- ----------------------------
-- 2. 方案版本表
-- 保存完整方案协议快照，用于方案升级和项目继承追踪。
-- ----------------------------
DROP TABLE IF EXISTS lm_scheme_version;
CREATE TABLE lm_scheme_version (
    scheme_version_id BIGINT(20)   NOT NULL                    COMMENT '方案版本ID',
    scheme_id         VARCHAR(100) NOT NULL                    COMMENT '方案ID',
    scheme_version    VARCHAR(50)  NOT NULL                    COMMENT '方案版本号',
    status            VARCHAR(20)  NOT NULL DEFAULT 'draft'    COMMENT '状态（draft/active/archived）',
    protocol_json     JSON                                      COMMENT '方案协议完整快照',
    change_log        VARCHAR(1000) DEFAULT ''                 COMMENT '版本变更说明',
    published_at      DATETIME     DEFAULT NULL                COMMENT '发布时间',
    create_time       DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_time       DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag          CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (scheme_version_id),
    UNIQUE KEY uk_scheme_version (scheme_id, scheme_version),
    KEY idx_scheme_id (scheme_id),
    KEY idx_scheme_version_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-方案版本表';

-- ----------------------------
-- 3. 项目表
-- 项目是用户真正维护的生活对象。
-- ----------------------------
DROP TABLE IF EXISTS lm_project;
CREATE TABLE lm_project (
    project_id        BIGINT(20)   NOT NULL                    COMMENT '项目ID',
    project_name      VARCHAR(100) NOT NULL                    COMMENT '项目名称',
    description       VARCHAR(500) DEFAULT ''                  COMMENT '项目描述',
    cover_src         VARCHAR(500) DEFAULT ''                  COMMENT '封面图URL',
    cover_alt         VARCHAR(200) DEFAULT ''                  COMMENT '封面图alt',
    status            VARCHAR(20)  NOT NULL DEFAULT 'active'   COMMENT '状态（active/paused/archived）',
    scheme_id         VARCHAR(100) DEFAULT ''                  COMMENT '项目创建时使用的方案ID',
    scheme_version_id BIGINT(20)   DEFAULT NULL                COMMENT '项目创建或最近同步的方案版本ID',
    scheme_version    VARCHAR(50)  DEFAULT ''                  COMMENT '项目创建或最近同步的方案版本号',
    scheme_type       VARCHAR(50)  DEFAULT ''                  COMMENT '方案类型（game/travel/study等）',
    cycle_type        VARCHAR(30)  DEFAULT ''                  COMMENT '周期属性（long_running/staged/hybrid）',
    reminder_channels JSON                                      COMMENT '默认提醒渠道',
    ai_enabled        CHAR(1)      DEFAULT '1'                 COMMENT '是否启用AI（0否 1是）',
    security_policy   JSON                                      COMMENT '项目级敏感信息策略',
    tags              JSON                                      COMMENT '标签 [{label, tone}]',
    stats             JSON                                      COMMENT '统计数据 [{label, value}]',
    remark            VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id         VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept       BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by         BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time       DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by         BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time       DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag          CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (project_id),
    KEY idx_scheme_id (scheme_id),
    KEY idx_scheme_version_id (scheme_version_id),
    KEY idx_scheme_type (scheme_type),
    KEY idx_project_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-项目表';

-- ----------------------------
-- 4. 项目方案覆盖表
-- 区分方案默认配置和用户覆盖配置，避免方案升级时覆盖用户修改。
-- ----------------------------
DROP TABLE IF EXISTS lm_project_scheme_override;
CREATE TABLE lm_project_scheme_override (
    override_id       BIGINT(20)   NOT NULL                    COMMENT '覆盖配置ID',
    project_id        BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    scheme_id         VARCHAR(100) DEFAULT ''                  COMMENT '来源方案ID',
    scheme_version_id BIGINT(20)   DEFAULT NULL                COMMENT '来源方案版本ID',
    override_type     VARCHAR(50)  NOT NULL                    COMMENT '覆盖类型（project_base/block_instance/summary_rule/timeline_rule/ai_rule/security/business_config）',
    target_key        VARCHAR(100) NOT NULL                    COMMENT '覆盖目标key，如blockKey或ruleId',
    override_json     JSON                                      COMMENT '覆盖内容JSON',
    override_source   VARCHAR(30)  NOT NULL DEFAULT 'user_override' COMMENT '来源（scheme_default/user_override/ai_suggested/imported）',
    enabled           CHAR(1)      DEFAULT '1'                 COMMENT '是否启用（0否 1是）',
    remark            VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    create_time       DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_time       DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag          CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (override_id),
    UNIQUE KEY uk_project_override (project_id, override_type, target_key),
    KEY idx_project_id (project_id),
    KEY idx_scheme_version_id (scheme_version_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-项目方案覆盖表';

-- ----------------------------
-- 5. 功能块实例配置表
-- 一个项目下的一个功能块实例，通过 capabilities 引用能力。
-- ----------------------------
DROP TABLE IF EXISTS lm_ability_config;
DROP TABLE IF EXISTS lm_block_instance_config;
CREATE TABLE lm_block_instance_config (
    config_id         BIGINT(20)   NOT NULL                    COMMENT '配置ID',
    project_id        BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    scheme_id         VARCHAR(100) DEFAULT ''                  COMMENT '来源方案ID',
    scheme_version_id BIGINT(20)   DEFAULT NULL                COMMENT '来源方案版本ID',
    scheme_instance_id VARCHAR(100) DEFAULT ''                 COMMENT '方案内功能块实例ID',
    block_key         VARCHAR(50)  NOT NULL                    COMMENT '功能块类型',
    display_name      VARCHAR(100) NOT NULL                    COMMENT '显示名称',
    enabled           CHAR(1)      DEFAULT '1'                 COMMENT '是否启用（0否 1是）',
    capabilities      JSON                                      COMMENT '能力列表',
    navigation        JSON                                      COMMENT '导航配置 {visible, order}',
    summary_rules     JSON                                      COMMENT '概览摘要规则',
    fields            JSON                                      COMMENT '字段配置',
    behavior          JSON                                      COMMENT '行为规则 {resetRules, reminderRules, archiveRule}',
    timeline          JSON                                      COMMENT '时间轴配置 {enabled, defaultWriteRule}',
    ai_rules          JSON                                      COMMENT 'AI规则 {readable, writableAfterConfirm, allowedUse}',
    security          JSON                                      COMMENT '安全配置 {sensitivity, maskInOverview}',
    config_source     VARCHAR(30)  NOT NULL DEFAULT 'scheme_default' COMMENT '配置来源（scheme_default/user_override/ai_suggested/imported）',
    remark            VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id         VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept       BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by         BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time       DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by         BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time       DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag          CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (config_id),
    UNIQUE KEY uk_project_block (project_id, block_key),
    KEY idx_project_id (project_id),
    KEY idx_scheme_version_id (scheme_version_id),
    KEY idx_block_key (block_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-功能块实例配置表';

-- ----------------------------
-- 6. 待确认候选表
-- AI、Webhook、MCP、外部渠道导入内容先进入候选状态，确认后再写入项目数据。
-- ----------------------------
DROP TABLE IF EXISTS lm_inbox_candidate;
CREATE TABLE lm_inbox_candidate (
    candidate_id       BIGINT(20)   NOT NULL                    COMMENT '候选内容ID',
    project_id         BIGINT(20)   DEFAULT NULL                COMMENT '候选归属项目ID，未归属时为空',
    source_type        VARCHAR(50)  NOT NULL                    COMMENT '来源类型（ai/webhook/mcp/import/manual）',
    source_ref         VARCHAR(200) DEFAULT ''                  COMMENT '来源引用ID',
    target_block_key   VARCHAR(50)  DEFAULT ''                  COMMENT '建议写入的功能块',
    title              VARCHAR(200) NOT NULL                    COMMENT '候选标题',
    content            TEXT                                      COMMENT '候选正文',
    payload            JSON                                      COMMENT '候选结构化内容',
    status             VARCHAR(20)  NOT NULL DEFAULT 'pending'  COMMENT '状态（pending/confirmed/rejected/archived）',
    sensitivity        VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/private/sensitive）',
    ai_readable        CHAR(1)      DEFAULT '0'                 COMMENT '是否AI可读（0否 1是）',
    confirm_required   CHAR(1)      DEFAULT '1'                 COMMENT '写入前是否需要确认（0否 1是）',
    confirmed_at       DATETIME     DEFAULT NULL                COMMENT '确认时间',
    confirmed_by       BIGINT(20)   DEFAULT NULL                COMMENT '确认人',
    remark             VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    create_time        DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_time        DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag           CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (candidate_id),
    KEY idx_project_id (project_id),
    KEY idx_status (status),
    KEY idx_source_type (source_type),
    KEY idx_target_block_key (target_block_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-待确认候选表';

-- ----------------------------
-- 7. 游戏版本表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_version;
CREATE TABLE lm_game_version (
    version_id      BIGINT(20)   NOT NULL                    COMMENT '版本ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'version_activity'  COMMENT '所属功能块实例',
    name            VARCHAR(50)  NOT NULL                    COMMENT '版本号',
    title           VARCHAR(200) NOT NULL                    COMMENT '版本标题',
    start_at        DATETIME     DEFAULT NULL                COMMENT '开始时间',
    end_at          DATETIME     DEFAULT NULL                COMMENT '结束时间',
    status          VARCHAR(20)  NOT NULL DEFAULT 'upcoming' COMMENT '状态（upcoming/active/ending/ended/archived）',
    highlights      JSON                                      COMMENT '版本亮点',
    summary         TEXT                                      COMMENT '归档总结',
    archived_at     DATETIME     DEFAULT NULL                COMMENT '归档时间',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (version_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-游戏版本表';

-- ----------------------------
-- 8. 游戏活动表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_activity;
CREATE TABLE lm_game_activity (
    activity_id     BIGINT(20)   NOT NULL                    COMMENT '活动ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'version_activity'  COMMENT '所属功能块实例',
    version_id      BIGINT(20)   NOT NULL                    COMMENT '所属版本ID',
    title           VARCHAR(200) NOT NULL                    COMMENT '活动标题',
    description     VARCHAR(1000) DEFAULT ''                 COMMENT '活动描述',
    start_at        DATETIME     DEFAULT NULL                COMMENT '开始时间',
    end_at          DATETIME     DEFAULT NULL                COMMENT '结束时间',
    status          VARCHAR(20)  NOT NULL DEFAULT 'upcoming' COMMENT '状态',
    priority        VARCHAR(20)  DEFAULT 'normal'            COMMENT '优先级（low/normal/high）',
    cover           VARCHAR(500) DEFAULT ''                  COMMENT '封面URL',
    reminder_rule   JSON                                      COMMENT '提醒规则 {enabled, channels, beforeMinutes}',
    archived_at     DATETIME     DEFAULT NULL                COMMENT '归档时间',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (activity_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_version_id (version_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-游戏活动表';

-- ----------------------------
-- 9. 游戏目标表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_target;
CREATE TABLE lm_game_target (
    target_id          BIGINT(20)   NOT NULL                    COMMENT '目标ID',
    project_id         BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key          VARCHAR(50)  DEFAULT 'targets'           COMMENT '所属功能块实例',
    type               VARCHAR(20)  NOT NULL DEFAULT 'daily'   COMMENT '类型（daily/weekly/activity/custom）',
    title              VARCHAR(200) NOT NULL                    COMMENT '目标标题',
    description        VARCHAR(1000) DEFAULT ''                 COMMENT '目标描述',
    status             VARCHAR(20)  NOT NULL DEFAULT 'todo'    COMMENT '状态（todo/done/skipped/expired/archived）',
    progress_current   INT          DEFAULT 0                   COMMENT '当前进度',
    progress_target    INT          DEFAULT 0                   COMMENT '目标进度',
    reset_rule         JSON                                      COMMENT '重置规则 {type, time, weekday}',
    version_id         BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id        BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    due_at             DATETIME     DEFAULT NULL                COMMENT '截止时间',
    priority           VARCHAR(20)  DEFAULT 'normal'            COMMENT '优先级',
    pinned_to_overview CHAR(1)      DEFAULT '0'                 COMMENT '是否置顶概览（0否 1是）',
    timeline_rule      JSON                                      COMMENT '时间轴写入规则',
    archived_at        DATETIME     DEFAULT NULL                COMMENT '归档时间',
    remark             VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id          VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept        BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by          BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time        DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by          BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time        DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag           CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (target_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_version_id (version_id),
    KEY idx_activity_id (activity_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-游戏目标表';

-- ----------------------------
-- 10. 时间轴事件表
-- ----------------------------
DROP TABLE IF EXISTS lm_timeline_event;
CREATE TABLE lm_timeline_event (
    event_id            BIGINT(20)   NOT NULL                    COMMENT '事件ID',
    project_id          BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    occurred_at         DATETIME     NOT NULL                    COMMENT '发生时间',
    type                VARCHAR(50)  NOT NULL                    COMMENT '事件类型',
    title               VARCHAR(200) NOT NULL                    COMMENT '事件标题',
    description         VARCHAR(1000) DEFAULT ''                 COMMENT '事件描述',
    source_block_key    VARCHAR(50)  NOT NULL                    COMMENT '来源功能块',
    version_id          BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id         BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    target_id           BIGINT(20)   DEFAULT NULL                COMMENT '关联目标ID',
    sensitivity         VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/private/sensitive）',
    display_in_overview CHAR(1)      DEFAULT '1'                 COMMENT '是否在概览显示',
    ai_readable         CHAR(1)      DEFAULT '1'                 COMMENT '是否AI可读',
    write_mode          VARCHAR(30)  DEFAULT 'detail'            COMMENT '写入模式（detail/daily_summary/weekly_summary/exception_only/confirmed）',
    confirm_required    CHAR(1)      DEFAULT '0'                 COMMENT '是否需要确认后写入（0否 1是）',
    remark              VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id           VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept         BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by           BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time         DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by           BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time         DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag            CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (event_id),
    KEY idx_project_id (project_id),
    KEY idx_source_block_key (source_block_key),
    KEY idx_occurred_at (occurred_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-时间轴事件表';

-- ----------------------------
-- 11. 素材表
-- ----------------------------
DROP TABLE IF EXISTS lm_material;
CREATE TABLE lm_material (
    material_id     BIGINT(20)   NOT NULL                    COMMENT '素材ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'targets'           COMMENT '所属功能块实例',
    name            VARCHAR(200) NOT NULL                    COMMENT '素材名称',
    type            VARCHAR(20)  DEFAULT 'other'             COMMENT '类型（outfit/material/currency/collection/recipe/other）',
    description     VARCHAR(500) DEFAULT ''                  COMMENT '描述',
    status          VARCHAR(20)  DEFAULT 'collecting'        COMMENT '状态（collecting/completed/archived）',
    current         INT          DEFAULT 0                   COMMENT '当前数量',
    target          INT          DEFAULT 0                   COMMENT '目标数量',
    version_id      BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id     BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    target_id       BIGINT(20)   DEFAULT NULL                COMMENT '关联目标ID',
    timeline_rule   JSON                                      COMMENT '时间轴写入规则',
    note            VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (material_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-素材表';

-- ----------------------------
-- 12. 笔记表
-- ----------------------------
DROP TABLE IF EXISTS lm_note;
CREATE TABLE lm_note (
    note_id             BIGINT(20)   NOT NULL                    COMMENT '笔记ID',
    project_id          BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key           VARCHAR(50)  DEFAULT 'notes'             COMMENT '所属功能块实例',
    title               VARCHAR(200) NOT NULL                    COMMENT '笔记标题',
    content             TEXT                                      COMMENT '笔记内容',
    type                VARCHAR(20)  DEFAULT 'note'              COMMENT '类型（note/guide/review）',
    category            VARCHAR(50)  DEFAULT ''                  COMMENT '分类',
    category_label      VARCHAR(50)  DEFAULT ''                  COMMENT '分类显示名',
    version_id          BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id         BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    target_id           BIGINT(20)   DEFAULT NULL                COMMENT '关联目标ID',
    pinned_to_overview  CHAR(1)      DEFAULT '0'                 COMMENT '是否置顶概览',
    timeline_rule       JSON                                      COMMENT '时间轴写入规则',
    remark              VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id           VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept         BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by           BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time         DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by           BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time         DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag            CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (note_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-笔记表';

-- ----------------------------
-- 13. 图册表
-- ----------------------------
DROP TABLE IF EXISTS lm_album;
CREATE TABLE lm_album (
    album_id        BIGINT(20)   NOT NULL                    COMMENT '图册ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'gallery'           COMMENT '所属功能块实例',
    name            VARCHAR(100) NOT NULL                    COMMENT '图册名称',
    cover_url       VARCHAR(500) DEFAULT ''                  COMMENT '封面URL',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (album_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-图册表';

-- ----------------------------
-- 14. 照片表
-- ----------------------------
DROP TABLE IF EXISTS lm_photo;
CREATE TABLE lm_photo (
    photo_id        BIGINT(20)   NOT NULL                    COMMENT '照片ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'gallery'           COMMENT '所属功能块实例',
    album_id        BIGINT(20)   NOT NULL                    COMMENT '所属图册ID',
    url             VARCHAR(500) NOT NULL                    COMMENT '原图URL',
    thumbnail       VARCHAR(500) DEFAULT ''                  COMMENT '缩略图URL',
    caption         VARCHAR(200) DEFAULT ''                  COMMENT '照片说明',
    taken_at        DATETIME     DEFAULT NULL                COMMENT '拍摄时间',
    version_id      BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id     BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    target_id       BIGINT(20)   DEFAULT NULL                COMMENT '关联目标ID',
    sensitivity     VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/private/sensitive）',
    ai_readable     CHAR(1)      DEFAULT '1'                 COMMENT '是否AI可读（0否 1是）',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (photo_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_album_id (album_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-照片表';

-- ----------------------------
-- 15. 资产表
-- ----------------------------
DROP TABLE IF EXISTS lm_asset;
CREATE TABLE lm_asset (
    asset_id        BIGINT(20)   NOT NULL                    COMMENT '资产ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  DEFAULT 'assets'            COMMENT '所属功能块实例',
    name            VARCHAR(200) NOT NULL                    COMMENT '资产名称',
    type            VARCHAR(50)  DEFAULT ''                  COMMENT '资产类型',
    category        VARCHAR(50)  DEFAULT ''                  COMMENT '资产分类',
    value           VARCHAR(500) DEFAULT ''                  COMMENT '资产值',
    status          VARCHAR(20)  DEFAULT 'pending'           COMMENT '状态（protected/bound/pending/expired/archived）',
    status_label    VARCHAR(50)  DEFAULT ''                  COMMENT '状态显示名',
    sensitivity     VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/sensitive/private）',
    icon            VARCHAR(200) DEFAULT ''                  COMMENT '图标',
    description     VARCHAR(500) DEFAULT ''                  COMMENT '描述',
    linked_url      VARCHAR(500) DEFAULT ''                  COMMENT '关联链接',
    expires_at      DATETIME     DEFAULT NULL                COMMENT '过期时间',
    notes           VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    timeline_rule   JSON                                      COMMENT '时间轴写入规则',
    ai_readable     CHAR(1)      DEFAULT '0'                 COMMENT '是否AI可读（0否 1是）',
    mask_in_overview CHAR(1)     DEFAULT '1'                 COMMENT '是否在概览脱敏（0否 1是）',
    archived_at     DATETIME     DEFAULT NULL                COMMENT '归档时间',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号，fml-service 当前不主动使用',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门，兼容历史字段',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (asset_id),
    KEY idx_project_id (project_id),
    KEY idx_block_key (block_key),
    KEY idx_status (status),
    KEY idx_expires_at (expires_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='生活管理-资产表';

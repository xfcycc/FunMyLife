-- ----------------------------
-- Life Manager 模块表
-- 通用表结构，不绑定任何具体方案，通过 project_id 隔离不同项目
-- ----------------------------

-- ----------------------------
-- 1. 项目表
-- ----------------------------
DROP TABLE IF EXISTS lm_project;
CREATE TABLE lm_project (
    project_id      BIGINT(20)   NOT NULL                    COMMENT '项目ID',
    project_name    VARCHAR(100) NOT NULL                    COMMENT '项目名称',
    description     VARCHAR(500) DEFAULT ''                  COMMENT '项目描述',
    cover_src       VARCHAR(500) DEFAULT ''                  COMMENT '封面图URL',
    cover_alt       VARCHAR(200) DEFAULT ''                  COMMENT '封面图alt',
    status          VARCHAR(20)  NOT NULL DEFAULT 'active'   COMMENT '状态（active/paused/archived）',
    scheme_id       VARCHAR(100) DEFAULT ''                  COMMENT '方案模板ID',
    scheme_type     VARCHAR(50)  DEFAULT ''                  COMMENT '方案类型（game/travel/study等）',
    tags            JSON                                      COMMENT '标签 [{label, tone}]',
    stats           JSON                                      COMMENT '统计数据 [{label, value}]',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (project_id)
) ENGINE=InnoDB COMMENT='生活管理-项目表';

-- ----------------------------
-- 2. 功能块实例配置表
-- ----------------------------
DROP TABLE IF EXISTS lm_ability_config;
CREATE TABLE lm_ability_config (
    config_id       BIGINT(20)   NOT NULL                    COMMENT '配置ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    block_key       VARCHAR(50)  NOT NULL                    COMMENT '功能块类型',
    display_name    VARCHAR(100) NOT NULL                    COMMENT '显示名称',
    enabled         CHAR(1)      DEFAULT '1'                 COMMENT '是否启用（0否 1是）',
    capabilities    JSON                                      COMMENT '能力列表',
    navigation      JSON                                      COMMENT '导航配置 {visible, order}',
    summary_rules   JSON                                      COMMENT '概览摘要规则',
    fields          JSON                                      COMMENT '字段配置',
    behavior        JSON                                      COMMENT '行为规则 {resetRules, reminderRules, archiveRule}',
    timeline        JSON                                      COMMENT '时间轴配置 {enabled, defaultWriteRule}',
    ai_rules        JSON                                      COMMENT 'AI规则 {readable, writableAfterConfirm, allowedUse}',
    security        JSON                                      COMMENT '安全配置 {sensitivity, maskInOverview}',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (config_id),
    UNIQUE KEY uk_project_block (project_id, block_key)
) ENGINE=InnoDB COMMENT='生活管理-功能块实例配置表';

-- ----------------------------
-- 3. 游戏版本表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_version;
CREATE TABLE lm_game_version (
    version_id      BIGINT(20)   NOT NULL                    COMMENT '版本ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    name            VARCHAR(50)  NOT NULL                    COMMENT '版本号',
    title           VARCHAR(200) NOT NULL                    COMMENT '版本标题',
    start_at        DATETIME     DEFAULT NULL                COMMENT '开始时间',
    end_at          DATETIME     DEFAULT NULL                COMMENT '结束时间',
    status          VARCHAR(20)  NOT NULL DEFAULT 'upcoming' COMMENT '状态（upcoming/active/ending/ended/archived）',
    highlights      JSON                                      COMMENT '版本亮点',
    summary         TEXT                                      COMMENT '归档总结',
    archived_at     DATETIME     DEFAULT NULL                COMMENT '归档时间',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (version_id),
    KEY idx_project_id (project_id)
) ENGINE=InnoDB COMMENT='生活管理-游戏版本表';

-- ----------------------------
-- 4. 游戏活动表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_activity;
CREATE TABLE lm_game_activity (
    activity_id     BIGINT(20)   NOT NULL                    COMMENT '活动ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
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
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (activity_id),
    KEY idx_project_id (project_id),
    KEY idx_version_id (version_id)
) ENGINE=InnoDB COMMENT='生活管理-游戏活动表';

-- ----------------------------
-- 5. 游戏目标表
-- ----------------------------
DROP TABLE IF EXISTS lm_game_target;
CREATE TABLE lm_game_target (
    target_id          BIGINT(20)   NOT NULL                    COMMENT '目标ID',
    project_id         BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
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
    tenant_id          VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept        BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by          BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time        DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by          BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time        DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag           CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (target_id),
    KEY idx_project_id (project_id),
    KEY idx_version_id (version_id),
    KEY idx_activity_id (activity_id)
) ENGINE=InnoDB COMMENT='生活管理-游戏目标表';

-- ----------------------------
-- 6. 时间轴事件表
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
    sensitivity         VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/private）',
    display_in_overview CHAR(1)      DEFAULT '1'                 COMMENT '是否在概览显示',
    ai_readable         CHAR(1)      DEFAULT '1'                 COMMENT '是否AI可读',
    remark              VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id           VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept         BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by           BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time         DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by           BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time         DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag            CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (event_id),
    KEY idx_project_id (project_id),
    KEY idx_occurred_at (occurred_at)
) ENGINE=InnoDB COMMENT='生活管理-时间轴事件表';

-- ----------------------------
-- 7. 素材表
-- ----------------------------
DROP TABLE IF EXISTS lm_material;
CREATE TABLE lm_material (
    material_id     BIGINT(20)   NOT NULL                    COMMENT '素材ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
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
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (material_id),
    KEY idx_project_id (project_id)
) ENGINE=InnoDB COMMENT='生活管理-素材表';

-- ----------------------------
-- 8. 笔记表
-- ----------------------------
DROP TABLE IF EXISTS lm_note;
CREATE TABLE lm_note (
    note_id             BIGINT(20)   NOT NULL                    COMMENT '笔记ID',
    project_id          BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
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
    tenant_id           VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept         BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by           BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time         DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by           BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time         DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag            CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (note_id),
    KEY idx_project_id (project_id)
) ENGINE=InnoDB COMMENT='生活管理-笔记表';

-- ----------------------------
-- 9. 图册表
-- ----------------------------
DROP TABLE IF EXISTS lm_album;
CREATE TABLE lm_album (
    album_id        BIGINT(20)   NOT NULL                    COMMENT '图册ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    name            VARCHAR(100) NOT NULL                    COMMENT '图册名称',
    cover_url       VARCHAR(500) DEFAULT ''                  COMMENT '封面URL',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (album_id),
    KEY idx_project_id (project_id)
) ENGINE=InnoDB COMMENT='生活管理-图册表';

-- ----------------------------
-- 10. 照片表
-- ----------------------------
DROP TABLE IF EXISTS lm_photo;
CREATE TABLE lm_photo (
    photo_id        BIGINT(20)   NOT NULL                    COMMENT '照片ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    album_id        BIGINT(20)   NOT NULL                    COMMENT '所属图册ID',
    url             VARCHAR(500) NOT NULL                    COMMENT '原图URL',
    thumbnail       VARCHAR(500) DEFAULT ''                  COMMENT '缩略图URL',
    caption         VARCHAR(200) DEFAULT ''                  COMMENT '照片说明',
    taken_at        DATETIME     DEFAULT NULL                COMMENT '拍摄时间',
    version_id      BIGINT(20)   DEFAULT NULL                COMMENT '关联版本ID',
    activity_id     BIGINT(20)   DEFAULT NULL                COMMENT '关联活动ID',
    target_id       BIGINT(20)   DEFAULT NULL                COMMENT '关联目标ID',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (photo_id),
    KEY idx_project_id (project_id),
    KEY idx_album_id (album_id)
) ENGINE=InnoDB COMMENT='生活管理-照片表';

-- ----------------------------
-- 11. 资产表
-- ----------------------------
DROP TABLE IF EXISTS lm_asset;
CREATE TABLE lm_asset (
    asset_id        BIGINT(20)   NOT NULL                    COMMENT '资产ID',
    project_id      BIGINT(20)   NOT NULL                    COMMENT '所属项目ID',
    name            VARCHAR(200) NOT NULL                    COMMENT '资产名称',
    type            VARCHAR(50)  DEFAULT ''                  COMMENT '资产类型',
    category        VARCHAR(50)  DEFAULT ''                  COMMENT '资产分类',
    value           VARCHAR(500) DEFAULT ''                  COMMENT '资产值',
    status          VARCHAR(20)  DEFAULT 'pending'           COMMENT '状态（protected/bound/pending/expired/archived）',
    status_label    VARCHAR(50)  DEFAULT ''                  COMMENT '状态显示名',
    sensitivity     VARCHAR(20)  DEFAULT 'normal'            COMMENT '敏感度（normal/sensitive）',
    icon            VARCHAR(200) DEFAULT ''                  COMMENT '图标',
    description     VARCHAR(500) DEFAULT ''                  COMMENT '描述',
    linked_url      VARCHAR(500) DEFAULT ''                  COMMENT '关联链接',
    expires_at      DATETIME     DEFAULT NULL                COMMENT '过期时间',
    notes           VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    remark          VARCHAR(500) DEFAULT ''                  COMMENT '备注',
    timeline_rule   JSON                                      COMMENT '时间轴写入规则',
    archived_at     DATETIME     DEFAULT NULL                COMMENT '归档时间',
    tenant_id       VARCHAR(20)  DEFAULT '000000'            COMMENT '租户编号',
    create_dept     BIGINT(20)   DEFAULT NULL                COMMENT '创建部门',
    create_by       BIGINT(20)   DEFAULT NULL                COMMENT '创建者',
    create_time     DATETIME     DEFAULT NULL                COMMENT '创建时间',
    update_by       BIGINT(20)   DEFAULT NULL                COMMENT '更新者',
    update_time     DATETIME     DEFAULT NULL                COMMENT '更新时间',
    del_flag        CHAR(1)      DEFAULT '0'                 COMMENT '删除标志（0存在 1删除）',
    PRIMARY KEY (asset_id),
    KEY idx_project_id (project_id)
) ENGINE=InnoDB COMMENT='生活管理-资产表';

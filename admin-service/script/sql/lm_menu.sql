-- ----------------------------
-- Life Manager 菜单注册
-- ----------------------------

-- 一级菜单：生活管理
INSERT INTO sys_menu VALUES (2000, '生活管理', '0', '6', 'life', NULL, '', 1, 0, 'M', '0', '0', '', 'life', 103, 1, sysdate(), NULL, NULL, '生活管理目录');

-- 二级菜单：项目管理
INSERT INTO sys_menu VALUES (2001, '项目管理', '2000', '1', 'project', 'life/project/index', '', 1, 0, 'C', '0', '0', 'life:project:list', 'project', 103, 1, sysdate(), NULL, NULL, '项目管理菜单');

-- 三级按钮权限
INSERT INTO sys_menu VALUES (2002, '项目查询', '2001', '1', '#', '', '', 1, 0, 'F', '0', '0', 'life:project:query', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2003, '项目新增', '2001', '2', '#', '', '', 1, 0, 'F', '0', '0', 'life:project:add', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2004, '项目修改', '2001', '3', '#', '', '', 1, 0, 'F', '0', '0', 'life:project:edit', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2005, '项目删除', '2001', '4', '#', '', '', 1, 0, 'F', '0', '0', 'life:project:remove', '#', 103, 1, sysdate(), NULL, NULL, '');

-- 功能块配置权限
INSERT INTO sys_menu VALUES (2010, '功能块配置', '2001', '5', '#', '', '', 1, 0, 'F', '0', '0', 'life:config:list', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2011, '配置修改', '2001', '6', '#', '', '', 1, 0, 'F', '0', '0', 'life:config:edit', '#', 103, 1, sysdate(), NULL, NULL, '');

-- 目标管理权限
INSERT INTO sys_menu VALUES (2020, '目标管理', '2001', '7', '#', '', '', 1, 0, 'F', '0', '0', 'life:target:list', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2021, '目标新增', '2001', '8', '#', '', '', 1, 0, 'F', '0', '0', 'life:target:add', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2022, '目标修改', '2001', '9', '#', '', '', 1, 0, 'F', '0', '0', 'life:target:edit', '#', 103, 1, sysdate(), NULL, NULL, '');
INSERT INTO sys_menu VALUES (2023, '目标删除', '2001', '10', '#', '', '', 1, 0, 'F', '0', '0', 'life:target:remove', '#', 103, 1, sysdate(), NULL, NULL, '');

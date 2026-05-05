SET NAMES utf8mb4;

-- ----------------------------
-- Life Manager 乱码修复脚本
-- ----------------------------
-- 适用场景：
-- 1. lm_life.sql 的 UTF-8 中文内容曾在 latin1 会话下导入 MySQL。
-- 2. 查询时看到类似 "å®Œæˆæ¯æ—¥ä»»åŠ¡" 的 mojibake 乱码。
--
-- 执行前请先备份：
-- docker exec mysql8.0.38 sh -c 'mysqldump --default-character-set=utf8mb4 -uroot -p123456 fml > /tmp/fml-before-lm-mojibake-fix.sql'
-- docker cp mysql8.0.38:/tmp/fml-before-lm-mojibake-fix.sql ./fml-before-lm-mojibake-fix.sql
--
-- 执行方式：
-- docker exec -i mysql8.0.38 mysql --default-character-set=utf8mb4 -uroot -p123456 fml < admin-service/script/sql/repair_lm_life_mojibake.sql
--
-- 说明：
-- - 只处理当前数据库中表名以 lm_ 开头的 Life Manager 表。
-- - 文本字段、JSON 字段、表注释和字段注释都会按同一类错码规则修复。
-- - WHERE 条件会先筛选包含 Latin-1 扩展字符的值，避免重复转换正常英文状态值、URL、图标名等内容。

SELECT 'before_data_suspicious_columns' AS stage;
SELECT
    c.table_name,
    c.column_name,
    c.data_type
FROM information_schema.columns c
WHERE c.table_schema = DATABASE()
  AND c.table_name LIKE 'lm\_%'
  AND c.data_type IN ('char', 'varchar', 'text', 'mediumtext', 'longtext', 'json')
ORDER BY c.table_name, c.ordinal_position;

SELECT 'before_comment_suspicious_items' AS stage;
SELECT
    'table' AS item_type,
    t.table_name,
    NULL AS column_name,
    t.table_comment AS current_comment,
    CONVERT(CAST(CONVERT(t.table_comment USING latin1) AS BINARY) USING utf8mb4) AS repaired_comment
FROM information_schema.tables t
WHERE t.table_schema = DATABASE()
  AND t.table_name LIKE 'lm\_%'
  AND t.table_comment REGEXP '[À-ÿ]'
UNION ALL
SELECT
    'column' AS item_type,
    c.table_name,
    c.column_name,
    c.column_comment AS current_comment,
    CONVERT(CAST(CONVERT(c.column_comment USING latin1) AS BINARY) USING utf8mb4) AS repaired_comment
FROM information_schema.columns c
WHERE c.table_schema = DATABASE()
  AND c.table_name LIKE 'lm\_%'
  AND c.column_comment REGEXP '[À-ÿ]'
ORDER BY item_type, table_name, column_name;

DELIMITER $$

DROP PROCEDURE IF EXISTS repair_lm_mojibake_data $$
CREATE PROCEDURE repair_lm_mojibake_data()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE v_table_name VARCHAR(128);
    DECLARE v_column_name VARCHAR(128);
    DECLARE v_data_type VARCHAR(64);

    DECLARE cur CURSOR FOR
        SELECT
            c.table_name,
            c.column_name,
            c.data_type
        FROM information_schema.columns c
        WHERE c.table_schema = DATABASE()
          AND c.table_name LIKE 'lm\_%'
          AND c.data_type IN ('char', 'varchar', 'text', 'mediumtext', 'longtext', 'json')
        ORDER BY c.table_name, c.ordinal_position;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    OPEN cur;

    read_loop: LOOP
        FETCH cur INTO v_table_name, v_column_name, v_data_type;
        IF done THEN
            LEAVE read_loop;
        END IF;

        IF v_data_type = 'json' THEN
            SET @sql = CONCAT(
                'UPDATE `', v_table_name, '` ',
                'SET `', v_column_name, '` = CAST(CONVERT(CAST(CONVERT(CAST(`', v_column_name, '` AS CHAR CHARACTER SET utf8mb4) USING latin1) AS BINARY) USING utf8mb4) AS JSON) ',
                'WHERE `', v_column_name, '` IS NOT NULL ',
                'AND CAST(`', v_column_name, '` AS CHAR CHARACTER SET utf8mb4) REGEXP ''[À-ÿ]'''
            );
        ELSE
            SET @sql = CONCAT(
                'UPDATE `', v_table_name, '` ',
                'SET `', v_column_name, '` = CONVERT(CAST(CONVERT(`', v_column_name, '` USING latin1) AS BINARY) USING utf8mb4) ',
                'WHERE `', v_column_name, '` IS NOT NULL ',
                'AND `', v_column_name, '` REGEXP ''[À-ÿ]'''
            );
        END IF;

        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        SET @affected_rows = ROW_COUNT();
        DEALLOCATE PREPARE stmt;

        IF @affected_rows > 0 THEN
            SELECT 'data_fixed' AS stage, v_table_name AS table_name, v_column_name AS column_name, @affected_rows AS affected_rows;
        END IF;
    END LOOP;

    CLOSE cur;
END $$

DROP PROCEDURE IF EXISTS repair_lm_mojibake_comments $$
CREATE PROCEDURE repair_lm_mojibake_comments()
BEGIN
    DECLARE done INT DEFAULT 0;
    DECLARE v_table_name VARCHAR(128);
    DECLARE v_table_comment TEXT;
    DECLARE v_column_name VARCHAR(128);
    DECLARE v_column_type TEXT;
    DECLARE v_data_type VARCHAR(64);
    DECLARE v_character_set_name VARCHAR(64);
    DECLARE v_collation_name VARCHAR(64);
    DECLARE v_is_nullable VARCHAR(3);
    DECLARE v_column_default TEXT;
    DECLARE v_extra TEXT;
    DECLARE v_column_comment TEXT;
    DECLARE v_column_definition TEXT;

    DECLARE table_cur CURSOR FOR
        SELECT
            t.table_name,
            t.table_comment
        FROM information_schema.tables t
        WHERE t.table_schema = DATABASE()
          AND t.table_name LIKE 'lm\_%'
          AND t.table_comment REGEXP '[À-ÿ]'
        ORDER BY t.table_name;

    DECLARE column_cur CURSOR FOR
        SELECT
            c.table_name,
            c.column_name,
            c.column_type,
            c.data_type,
            c.character_set_name,
            c.collation_name,
            c.is_nullable,
            c.column_default,
            c.extra,
            c.column_comment
        FROM information_schema.columns c
        WHERE c.table_schema = DATABASE()
          AND c.table_name LIKE 'lm\_%'
          AND c.column_comment REGEXP '[À-ÿ]'
        ORDER BY c.table_name, c.ordinal_position;

    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = 1;

    SET done = 0;
    OPEN table_cur;

    table_loop: LOOP
        FETCH table_cur INTO v_table_name, v_table_comment;
        IF done THEN
            LEAVE table_loop;
        END IF;

        SET @sql = CONCAT(
            'ALTER TABLE `',
            v_table_name,
            '` COMMENT = ',
            QUOTE(CONVERT(CAST(CONVERT(v_table_comment USING latin1) AS BINARY) USING utf8mb4))
        );
        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

        SELECT 'table_comment_fixed' AS stage, v_table_name AS table_name;
    END LOOP;

    CLOSE table_cur;

    SET done = 0;
    OPEN column_cur;

    read_loop: LOOP
        FETCH column_cur INTO
            v_table_name,
            v_column_name,
            v_column_type,
            v_data_type,
            v_character_set_name,
            v_collation_name,
            v_is_nullable,
            v_column_default,
            v_extra,
            v_column_comment;

        IF done THEN
            LEAVE read_loop;
        END IF;

        SET v_column_definition = CONCAT('`', v_column_name, '` ', v_column_type);

        IF v_character_set_name IS NOT NULL AND v_data_type <> 'json' THEN
            SET v_column_definition = CONCAT(
                v_column_definition,
                ' CHARACTER SET ',
                v_character_set_name,
                ' COLLATE ',
                v_collation_name
            );
        END IF;

        IF v_is_nullable = 'NO' THEN
            SET v_column_definition = CONCAT(v_column_definition, ' NOT NULL');
        ELSE
            SET v_column_definition = CONCAT(v_column_definition, ' NULL');
        END IF;

        IF v_column_default IS NULL THEN
            IF v_is_nullable = 'YES' THEN
                SET v_column_definition = CONCAT(v_column_definition, ' DEFAULT NULL');
            END IF;
        ELSE
            SET v_column_definition = CONCAT(v_column_definition, ' DEFAULT ', QUOTE(v_column_default));
        END IF;

        IF v_extra IS NOT NULL AND v_extra <> '' THEN
            SET v_column_definition = CONCAT(v_column_definition, ' ', v_extra);
        END IF;

        SET @sql = CONCAT(
            'ALTER TABLE `',
            v_table_name,
            '` MODIFY COLUMN ',
            v_column_definition,
            ' COMMENT ',
            QUOTE(CONVERT(CAST(CONVERT(v_column_comment USING latin1) AS BINARY) USING utf8mb4))
        );

        PREPARE stmt FROM @sql;
        EXECUTE stmt;
        DEALLOCATE PREPARE stmt;

        SELECT 'column_comment_fixed' AS stage, v_table_name AS table_name, v_column_name AS column_name;
    END LOOP;

    CLOSE column_cur;
END $$

CALL repair_lm_mojibake_data() $$
CALL repair_lm_mojibake_comments() $$

DROP PROCEDURE IF EXISTS repair_lm_mojibake_data $$
DROP PROCEDURE IF EXISTS repair_lm_mojibake_comments $$

DELIMITER ;

SELECT 'after_comment_suspicious_items' AS stage;
SELECT
    'table' AS item_type,
    t.table_name,
    NULL AS column_name,
    t.table_comment AS current_comment
FROM information_schema.tables t
WHERE t.table_schema = DATABASE()
  AND t.table_name LIKE 'lm\_%'
  AND t.table_comment REGEXP '[À-ÿ]'
UNION ALL
SELECT
    'column' AS item_type,
    c.table_name,
    c.column_name,
    c.column_comment AS current_comment
FROM information_schema.columns c
WHERE c.table_schema = DATABASE()
  AND c.table_name LIKE 'lm\_%'
  AND c.column_comment REGEXP '[À-ÿ]'
ORDER BY item_type, table_name, column_name;

SELECT 'after_lm_table_comments' AS stage;
SELECT
    t.table_name,
    t.table_comment
FROM information_schema.tables t
WHERE t.table_schema = DATABASE()
  AND t.table_name LIKE 'lm\_%'
ORDER BY t.table_name;

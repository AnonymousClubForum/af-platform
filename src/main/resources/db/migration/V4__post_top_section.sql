-- 添加 t_post 的 section_id 列
SET @exist = (SELECT COUNT(*)
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_SCHEMA = 'af_platform'
                AND TABLE_NAME = 't_post'
                AND COLUMN_NAME = 'section_id');
SET @query = IF(@exist > 0, 'SELECT ''Column section_id exists''',
                'ALTER TABLE t_post ADD COLUMN section_id BIGINT NOT NULL DEFAULT 0 COMMENT ''分区ID''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 t_post 的 is_top 列
SET @exist = (SELECT COUNT(*)
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_SCHEMA = 'af_platform'
                AND TABLE_NAME = 't_post'
                AND COLUMN_NAME = 'is_top');
SET @query = IF(@exist > 0, 'SELECT ''Column is_top exists''',
                'ALTER TABLE t_post ADD COLUMN is_top TINYINT NOT NULL DEFAULT 0 COMMENT ''是否置顶（置顶为1）''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

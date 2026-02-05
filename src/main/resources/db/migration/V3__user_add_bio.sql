-- 删除 t_user 的 avatar_thumb_nail_id 列
SET @exist = (SELECT COUNT(*)
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_SCHEMA = 'af_platform'
                AND TABLE_NAME = 't_user'
                AND COLUMN_NAME = 'avatar_thumb_nail_id');
SET @query = IF(@exist > 0, 'ALTER TABLE t_user DROP COLUMN avatar_thumb_nail_id',
                'SELECT ''Column avatar_thumb_nail_id does not exist''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 添加 t_user 的 bio 列
SET @exist = (SELECT COUNT(*)
              FROM INFORMATION_SCHEMA.COLUMNS
              WHERE TABLE_SCHEMA = 'af_platform'
                AND TABLE_NAME = 't_user'
                AND COLUMN_NAME = 'bio');
SET @query = IF(@exist > 0, 'SELECT ''Column bio exists''',
                'ALTER TABLE t_exam_result ADD COLUMN bio VARCHAR(255) NULL DEFAULT NULL COMMENT ''个人签名''');
PREPARE stmt FROM @query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

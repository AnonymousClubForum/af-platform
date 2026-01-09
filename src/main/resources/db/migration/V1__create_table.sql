CREATE TABLE IF NOT EXISTS `af_platform`.`t_user`
(
    `id`                   BIGINT NOT NULL,
    `username`             VARCHAR(50)  DEFAULT NULL COMMENT '用户名',
    `password`             VARCHAR(100) DEFAULT NULL COMMENT '密码(建议加密存储)',
    `gender`               VARCHAR(20)  DEFAULT '秘密' COMMENT '性别(如：男/女/秘密/其他)',
    `avatar_id`            BIGINT       DEFAULT NULL COMMENT '头像文件ID(关联文件表)',
    `avatar_thumb_nail_id` BIGINT       DEFAULT NULL COMMENT '头像缩略图文件ID(关联文件表)',
    `is_admin`             TINYINT      DEFAULT 0 COMMENT '是否为管理员',
    `ctime`                TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `utime`                TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='用户表';

CREATE TABLE IF NOT EXISTS `af_platform`.`t_post`
(
    `id`      BIGINT NOT NULL,
    `user_id` BIGINT       DEFAULT NULL COMMENT '发布用户ID(关联t_user表)',
    `title`   VARCHAR(255) DEFAULT NULL COMMENT '帖子标题',
    `content` TEXT         DEFAULT NULL COMMENT '帖子内容',
    `ctime`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP,
    `utime`   TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引(优化按用户查帖子的性能)'
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='帖子表';

CREATE TABLE IF NOT EXISTS `af_platform`.`t_user`
(
    `id`        BIGINT NOT NULL,
    `post_id`   BIGINT    DEFAULT NULL COMMENT '被评论的帖子ID',
    `parent_id` BIGINT    DEFAULT NULL COMMENT '被评论的评论ID',
    `user_id`   BIGINT NOT NULL COMMENT '评论用户ID',
    `content`   TEXT      DEFAULT NULL COMMENT '评论内容',
    `ctime`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `utime`     TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci COMMENT ='评论表';
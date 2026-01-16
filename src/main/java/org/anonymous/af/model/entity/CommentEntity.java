package org.anonymous.af.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.anonymous.af.common.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName(value = "t_comment")
public class CommentEntity extends BaseEntity {
    private Long postId;    // 被评论的帖子ID

    private Long parentId;  // 被评论的评论ID

    private Long userId;    // 评论用户ID

    private String content; // 评论内容
}

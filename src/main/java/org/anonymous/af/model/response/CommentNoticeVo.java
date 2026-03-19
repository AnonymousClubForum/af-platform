package org.anonymous.af.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CommentNoticeVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 被评论的帖子ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long postId;

    /**
     * 被评论的帖子标题
     */
    private String postTitle;

    /**
     * 被评论的评论ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long parentId;

    /**
     * 被评论的评论内容
     */
    private String parentContent;

    /**
     * 评论人ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    /**
     * 评论人昵称
     */
    private String username;

    /**
     * 评论人头像ID
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long avatarId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 评论时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;
}

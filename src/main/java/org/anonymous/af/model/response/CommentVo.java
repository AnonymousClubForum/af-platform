package org.anonymous.af.model.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class CommentVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long userId;

    private String username;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long avatarId;

    private String content;

    private ParentCommentVo parentComment;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date ctime;

    @Data
    public static class ParentCommentVo {
        @JsonSerialize(using = ToStringSerializer.class)
        private Long userId;

        private String username;

        private String content;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
        private Date ctime;
    }
}

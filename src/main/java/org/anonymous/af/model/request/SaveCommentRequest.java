package org.anonymous.af.model.request;

import lombok.Data;

@Data
public class SaveCommentRequest {
    private Long postId;
    private Long parentId;
    private String content;
}

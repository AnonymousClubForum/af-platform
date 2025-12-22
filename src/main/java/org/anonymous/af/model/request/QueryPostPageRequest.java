package org.anonymous.af.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QueryPostPageRequest {
    @NotNull
    private Long pageNum;

    @NotNull
    private Long pageSize;

    private String username;

    private String title;

    private String content;
}

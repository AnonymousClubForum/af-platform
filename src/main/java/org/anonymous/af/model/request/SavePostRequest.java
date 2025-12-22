package org.anonymous.af.model.request;

import lombok.Data;

@Data
public class SavePostRequest {
    private Long id;
    private String title;
    private String content;
}

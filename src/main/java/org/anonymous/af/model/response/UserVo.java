package org.anonymous.af.model.response;

import lombok.Data;

@Data
public class UserVo {
    private Long id;

    private String username;

    private String gender;

    private Long avatarId;

    private Long avatarThumbNailId;
}

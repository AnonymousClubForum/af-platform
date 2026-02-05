package org.anonymous.af.model.response;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

@Data
public class UserVo {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    private String username;

    private String gender;

    private String bio;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long avatarId;
}

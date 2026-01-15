package org.anonymous.af.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String token;
    private UserVo user;
}

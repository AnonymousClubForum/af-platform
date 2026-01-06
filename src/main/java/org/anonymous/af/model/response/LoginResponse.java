package org.anonymous.af.model.response;

import lombok.Data;
import org.anonymous.af.model.entity.UserEntity;

@Data
public class LoginResponse {
    private String token;
    private UserEntity user;
}

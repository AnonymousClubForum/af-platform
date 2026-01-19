package org.anonymous.af.model.request;

import lombok.Data;

@Data
public class SaveUserRequest {
    private String username;
    private String password;
    private String gender;
}

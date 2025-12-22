package org.anonymous.af.model.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SaveUserRequest {
    private String username;
    private String password;
    private String gender;
    private MultipartFile avatar;
    private Integer avatarHeight;
    private Integer avatarWidth;
}

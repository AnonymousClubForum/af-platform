package org.anonymous.af.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.model.response.LoginResponse;

/**
 * 用户服务接口
 */
public interface UserService extends IService<UserEntity> {
    UserEntity getByUsername(String username);

    LoginResponse login(String username, String password);

    void createUser(SaveUserRequest request);

    void updateUser(SaveUserRequest request);
}
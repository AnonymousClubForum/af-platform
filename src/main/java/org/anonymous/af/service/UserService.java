package org.anonymous.af.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.model.response.LoginResponse;
import org.anonymous.af.model.response.UserVo;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 用户服务接口
 */
@Service
public interface UserService extends IService<UserEntity> {
    IPage<UserVo> getPage(Long pageNum, Long pageSize, String username);

    LoginResponse login(String username, String password);

    void createUser(SaveUserRequest request);

    void updateUser(SaveUserRequest request);

    UserVo getUser(Long id);

    String uploadAvatar(MultipartFile file);
}
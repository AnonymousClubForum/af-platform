package org.anonymous.af.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.anonymous.af.constants.Gender;
import org.anonymous.af.exception.AfException;
import org.anonymous.af.mapper.UserMapper;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.model.request.remote.UploadImageRequest;
import org.anonymous.af.model.response.remote.UploadImageResponse;
import org.anonymous.af.service.PostService;
import org.anonymous.af.service.UserService;
import org.anonymous.af.service.remote.StorageService;
import org.anonymous.af.utils.PasswordEncoderUtil;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {
    @Resource
    private StorageService storageService;
    @Resource
    private PostService postService;

    /**
     * 根据用户名获取实体
     */
    public UserEntity getByUsername(String username) {
        if (StrUtil.isBlank(username)) {
            throw new IllegalArgumentException("用户名为空");
        }
        LambdaQueryWrapper<UserEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(UserEntity::getUsername, username);
        List<UserEntity> userEntityList = baseMapper.selectList(queryWrapper);
        if (CollUtil.isEmpty(userEntityList)) {
            return null;
        }
        return userEntityList.getFirst();
    }

    /**
     * 密码复杂度校验
     */
    private void checkPassword(String password) {
        String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
        if (!Pattern.matches(regex, password)) {
            throw new IllegalArgumentException("密码需8位以上，包含大小写字母、数字和特殊字符");
        }
    }

    /**
     * 用户登录
     */
    public void login(String username, String password) {
        UserEntity userEntity = getByUsername(username);
        if (userEntity == null) {
            throw new AfException("用户不存在");
        }
        if (!PasswordEncoderUtil.matches(password, userEntity.getPassword())) {
            throw new AfException("密码错误");
        }
        UserContextUtil.setUser(userEntity);
    }

    /**
     * 用户注册
     */
    public void createUser(SaveUserRequest request) {
        if (getByUsername(request.getUsername()) != null) {
            throw new IllegalArgumentException("用户名已被使用");
        }
        checkPassword(request.getPassword());
        if (Arrays.stream(Gender.values()).noneMatch(gender -> request.getGender().equals(gender.getValue()))) {
            throw new IllegalArgumentException("性别类型不可用");
        }
        UserEntity userEntity = new UserEntity();
        BeanUtil.copyProperties(request, userEntity, true);
        userEntity.setId(IdWorker.getId());
        // 加密密码（仅存储哈希值，不存储明文）
        userEntity.setPassword(PasswordEncoderUtil.encode(request.getPassword()));
        if (request.getAvatar() != null) {
            UploadImageRequest uploadImageRequest = new UploadImageRequest();
            uploadImageRequest.setFile(request.getAvatar());
            uploadImageRequest.setHeight(request.getAvatarHeight());
            uploadImageRequest.setWidth(request.getAvatarWidth());
            UploadImageResponse uploadImageResponse = storageService.uploadImage(uploadImageRequest);
            userEntity.setAvatarId(uploadImageResponse.getImageId());
            userEntity.setAvatarThumbNailId(uploadImageResponse.getThumbnailId());
        }

        this.save(userEntity);
    }

    /**
     * 更新用户信息
     */
    public void updateUser(SaveUserRequest request) {
        UserEntity userEntity = UserContextUtil.getUser();
        if (userEntity == null) {
            throw new AfException("用户不存在");
        }
        if (StrUtil.isNotBlank(request.getUsername())) {
            postService.updateUsername(userEntity.getUsername(), request.getUsername());
        }
        BeanUtil.copyProperties(request, userEntity, true);
        if (StrUtil.isNotBlank(request.getPassword())) {
            checkPassword(request.getPassword());
            userEntity.setPassword(PasswordEncoderUtil.encode(request.getPassword()));
        }
        if (StrUtil.isNotBlank(request.getGender()) && Arrays.stream(Gender.values()).noneMatch(gender -> request.getGender().equals(gender.getValue()))) {
            throw new IllegalArgumentException("性别类型不可用");
        }
        if (request.getAvatar() != null) {
            UploadImageRequest uploadImageRequest = new UploadImageRequest();
            uploadImageRequest.setFile(request.getAvatar());
            uploadImageRequest.setHeight(request.getAvatarHeight());
            uploadImageRequest.setWidth(request.getAvatarWidth());
            UploadImageResponse uploadImageResponse = storageService.uploadImage(uploadImageRequest);
            userEntity.setAvatarId(uploadImageResponse.getImageId());
            userEntity.setAvatarThumbNailId(uploadImageResponse.getThumbnailId());
        }
        this.updateById(userEntity);
        UserContextUtil.setUser(userEntity);
    }
}
package org.anonymous.af.controller;

import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.LoginRequest;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.model.response.LoginResponse;
import org.anonymous.af.model.response.UserVo;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<String> register(@RequestBody SaveUserRequest request) {
        userService.createUser(request);
        return BaseResponse.success("注册成功");
    }

    @PostMapping("/login")
    public BaseResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        return BaseResponse.success(userService.login(request.getUsername(), request.getPassword())); // 返回token
    }

    @PostMapping("/update")
    public BaseResponse<String> updateUser(@RequestBody SaveUserRequest request) {
        userService.updateUser(request);
        return BaseResponse.success("用户信息更新成功");
    }

    @GetMapping("/get")
    public BaseResponse<UserVo> getCurrentUser() {
        UserEntity user = UserContextUtil.getUser();
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return BaseResponse.success(userVo);
    }
}

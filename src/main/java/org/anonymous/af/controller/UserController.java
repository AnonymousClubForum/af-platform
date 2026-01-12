package org.anonymous.af.controller;

import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.request.LoginRequest;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.model.response.LoginResponse;
import org.anonymous.af.model.response.UserVo;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
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
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(userService.getById(UserContextUtil.getUser().getId()), userVo);
        return BaseResponse.success(userVo);
    }
}

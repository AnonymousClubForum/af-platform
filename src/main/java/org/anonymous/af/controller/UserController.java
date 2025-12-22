package org.anonymous.af.controller;

import org.anonymous.af.common.BaseResponse;
import org.anonymous.af.model.entity.UserEntity;
import org.anonymous.af.model.request.SaveUserRequest;
import org.anonymous.af.service.UserService;
import org.anonymous.af.utils.UserContextUtil;
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
    public BaseResponse<String> login(@RequestParam(value = "username") String username,
                                      @RequestParam(value = "password") String password) {
        userService.login(username, password);
        return BaseResponse.success("登录成功");
    }

    @PostMapping("/update")
    public BaseResponse<String> updatePassword(@RequestBody SaveUserRequest request) {
        userService.updateUser(request);
        return BaseResponse.success("用户信息更新成功");
    }

    @GetMapping("/get")
    public BaseResponse<UserEntity> getUser() {
        return BaseResponse.success(UserContextUtil.getUser());
    }
}

package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.service.UserService;
import com.forum.vo.LoginVO;
import com.forum.vo.UserVO;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
   private UserService userService;

    /* 注册 */
    @PostMapping("/register")
    public Result register(@RequestBody @Valid UserRegisterDTO dto){
        return userService.register(dto);
    }

    /* 登录 */
    @PostMapping("/login")
    public Result<LoginVO> login(@RequestBody @Valid UserLoginDTO dto){

        return Result.success("登录成功", userService.login(dto));
    }


    /* 确认当前用户 */
    @GetMapping("/current")
    public Result<UserVO> currentUser(Authentication authentication){
        System.out.println("current 接口的 authentication = " + authentication);

        if (authentication == null) {
            return Result.error("用户未登录");
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        System.out.println("当前登录用户ID = " + userId);

        return Result.success("获取当前用户成功", userService.getCurrentUser(userId));
    }
}

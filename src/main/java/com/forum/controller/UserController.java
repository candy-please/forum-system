package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
   private UserService userService;

    /* 注册 */
    @PostMapping("/register")
    public Result register(@RequestBody UserRegisterDTO dto){
        return userService.register(dto);
    }

    /* 登录 */
    @PostMapping("/login")
    public Result login(@RequestBody UserLoginDTO dto){

        return userService.login(dto);
    }


    /* 确认当前用户 */
    @GetMapping("/current")
    public Result currentUser(org.springframework.security.core.Authentication authentication){
        System.out.println("current 接口的 authentication = " + authentication);

        if (authentication == null) {
            return Result.error("用户未登录");
        }

        String userName = (String) authentication.getPrincipal();
        System.out.println("当前登录用户 = " + userName);

        return userService.getCurrentUser(userName);
    }
}

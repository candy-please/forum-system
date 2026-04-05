package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.Result;
import com.forum.common.exception.BusinessException;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.entity.User;
import com.forum.mapper.UserMapper;
import com.forum.service.UserService;
import com.forum.utils.JwtUtil;
import com.forum.vo.LoginVO;
import com.forum.vo.UserVO;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private BCryptPasswordEncoder encoder;

    @Override
    public Result register(UserRegisterDTO dto){
        User existUser=userMapper.selectOne(new QueryWrapper<User>().eq("user_name",dto.getUserName()));
        if (existUser!=null)
            return Result.error("用户名已存在！");
        User user=new User();
        user.setUserName(dto.getUserName());
        user.setPassword(encoder.encode(dto.getPassword()));
        user.setEmail(dto.getEmail());
        user.setCreateDate(new Date());
        user.setRole(1);
        user.setStatus(1);
        user.setDeleted(0);

        try {
            userMapper.insert(user);
        } catch (DuplicateKeyException e) {
            return Result.error("用户名已存在！");
        }
        return Result.success("注册成功");
    }

    @Override
    public LoginVO login(UserLoginDTO dto) {

        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("user_name", dto.getUserName())
        );

        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException("用户名或密码错误");
        }

        if (user.getStatus() == 0) {
            throw new BusinessException("账号已被禁用");
        }

        if (!encoder.matches(dto.getPassword(), user.getPassword())) {
            throw new BusinessException("用户名或密码错误");
        }
        String token = JwtUtil.createToken(String.valueOf(user.getId()));

        user.setLastLoginDate(new Date());
        userMapper.updateById(user);
        LoginVO loginVO=new LoginVO();
        loginVO.setUserId(user.getId());
        loginVO.setRole(user.getRole());
        loginVO.setToken(token);
        loginVO.setUserName(user.getUserName());
        loginVO.setEmail(user.getEmail());

        return loginVO;
    }

    @Override
    public UserVO getCurrentUser(Long userId){
        User user = userMapper.selectById(userId);

        if (user == null) {
            throw new BusinessException("用户不存在");
        }

        UserVO userVO = new UserVO();
        userVO.setId(user.getId());
        userVO.setUserName(user.getUserName());
        userVO.setEmail(user.getEmail());
        userVO.setAvatar(user.getAvatar());
        userVO.setRole(user.getRole());

        return userVO;
        }
    }


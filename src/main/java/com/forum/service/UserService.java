package com.forum.service;

import com.forum.common.Result;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import com.forum.vo.LoginVO;
import com.forum.vo.UserVO;
import org.springframework.security.core.Authentication;

public interface UserService {
    Result register(UserRegisterDTO dto);
    LoginVO login(UserLoginDTO dto);

    UserVO getCurrentUser(Long userId);

}

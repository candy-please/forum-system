package com.forum.service;

import com.forum.common.Result;
import com.forum.dto.UserLoginDTO;
import com.forum.dto.UserRegisterDTO;
import org.springframework.security.core.Authentication;

public interface UserService {
    Result register(UserRegisterDTO dto);
    Result login(UserLoginDTO dto);

    Result getCurrentUser(String userName);

}

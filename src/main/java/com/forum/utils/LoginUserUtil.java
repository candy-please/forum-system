package com.forum.utils;

import com.forum.config.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class LoginUserUtil {
    public static Long getUserId(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null || authentication.getPrincipal()==null)
            throw new RuntimeException("用户未登录");
        Object principal=authentication.getPrincipal();
        if(principal instanceof  Long)
            return (Long) principal;

        return Long.valueOf(principal.toString());
    }

    public static Long getCurrentUserIdOrNull() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();

        if ("anonymousUser".equals(principal)) {
            return null;
        }

        if (principal instanceof Long) {
            return (Long) principal;
        }

        try {
            return Long.valueOf(principal.toString());
        } catch (Exception e) {
            return null;
        }
    }
}

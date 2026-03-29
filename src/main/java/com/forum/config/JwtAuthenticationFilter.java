package com.forum.config;

import com.forum.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws javax.servlet.ServletException, java.io.IOException {

        System.out.println("进入 JwtAuthenticationFilter");
        System.out.println("请求路径: " + request.getRequestURI());

        try {
            String token = request.getHeader("Authorization");
            System.out.println("Authorization: " + token);

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
                System.out.println("截取后的token: " + token);

                Claims claims = JwtUtil.parseToken(token);
                String username = claims.getSubject();
                System.out.println("解析出的username: " + username);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, null);

                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("已设置到 SecurityContextHolder");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(401);
            return;
        }

        filterChain.doFilter(request, response);
    }
}

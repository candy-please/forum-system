package com.forum.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){

        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                // 开启跨域支持
                .cors()

                .and()
                .csrf().disable()

                .authorizeRequests()

                .antMatchers(
                        "/user/register",
                        "/user/login",
                        "/category/list",
                        "/article/list",
                        "/article/detail/**"
                ).permitAll()

                .anyRequest().authenticated()

                .and()
                .addFilterBefore(
                        new JwtAuthenticationFilter(),
                        UsernamePasswordAuthenticationFilter.class
                )

                .formLogin().disable()
                .httpBasic().disable();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*");

        configuration.addAllowedHeader("*");

        configuration.addAllowedMethod("*");

        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}

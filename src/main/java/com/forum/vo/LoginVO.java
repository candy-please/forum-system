package com.forum.vo;

import lombok.Data;

@Data
public class LoginVO {

    private Long userId;

    private String userName;

    private String email;

    private Integer role;

    private String token;
}

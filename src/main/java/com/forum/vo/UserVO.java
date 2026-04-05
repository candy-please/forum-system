package com.forum.vo;

import lombok.Data;

@Data
public class UserVO {

    private Long id;

    private String userName;

    private String email;

    private String avatar;

    private Integer role;
}

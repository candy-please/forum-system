package com.forum.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;
@Data
public class User {
    /* id */
    private Long id;

    /*用户名*/
    private String userName;

    /*用户密码*/
    private String password;

    private String email;

    private String phone;

    private String avatar;

    private Integer role;

    private Integer status;

    private Date createDate;

    private Date updateDate;

    private Date lastLoginDate;

    @TableLogic
    private Integer deleted;
}

package com.forum.vo;

import lombok.Data;

import java.util.Date;

@Data
public class CommentVO {

    private Long id;

    private Long articleId;

    private Long userId;

    private String userName;

    private String content;

    private Date createTime;
}

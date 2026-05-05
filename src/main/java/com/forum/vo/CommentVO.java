package com.forum.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class CommentVO {

    private Long id;
    private Long articleId;

    private Long userId;
    private String userName;

    private Long replyUserId;
    private String replyUserName;

    private String content;
    private Date createTime;

    private List<CommentVO> children;
}

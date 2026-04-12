package com.forum.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleDetailVO {

    private Long id;

    private String title;

    private String content;

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private String authorName;

    private Integer viewCount;

    private Integer likeCount;

    private Boolean isLiked;

    private Date createTime;
}

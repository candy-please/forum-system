package com.forum.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ArticleVO {

    private Long id;

    private String title;

    // 摘要（可以截取 content 前100字）
    private String summary;

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private String authorName;

    private Integer viewCount;

    private Integer likeCount;

    private Boolean isLiked;

    private Date createTime;
}

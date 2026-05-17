package com.forum.vo;

import lombok.Data;

import java.util.Date;

@Data
public class FavoriteArticleVO {

    private Long articleId;

    private String title;

    private String summary;

    private Long authorId;

    private String authorName;

    private Date favoriteTime;
}

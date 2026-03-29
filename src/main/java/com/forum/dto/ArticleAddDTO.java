package com.forum.dto;

import lombok.Data;

@Data
public class ArticleAddDTO {
    private String title;

    private String content;

    private Long categoryId;
}

package com.forum.dto;

import lombok.Data;

@Data
public class ArticleQueryDTO {

    private Long current = 1L;

    private Long size = 10L;

    private Long categoryId;
}

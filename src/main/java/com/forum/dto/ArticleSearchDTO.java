package com.forum.dto;

import lombok.Data;

@Data
public class ArticleSearchDTO {
    /**
     * 搜索关键词：标题或内容
     */
    private String keyword;

    /**
     * 分类ID
     */
    private Long categoryId;

    /**
     * 排序方式：
     * newest 最新
     * hot 热门
     */
    private String sort = "newest";

    private Long current = 1L;

    private Long size = 10L;
}

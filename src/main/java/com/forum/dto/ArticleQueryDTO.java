package com.forum.dto;

import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
public class ArticleQueryDTO {
    @NotNull(message = "当前页不能为空")
    @Min(value=1,message = "当前页不能小于1")
    private Long current = 1L;

    @NotNull(message = "每页页数不能为空")
    @Min(value=1,message = "每页条数不能小于1")
    private Long size = 10L;

    private Long categoryId;
}

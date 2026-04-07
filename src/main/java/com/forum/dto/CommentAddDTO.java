package com.forum.dto;



import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CommentAddDTO {
    @NotBlank(message = "文章id不能为空")
    private Long articleId;

    @NotBlank(message = "文章内容不能为空")
    private String content;
}

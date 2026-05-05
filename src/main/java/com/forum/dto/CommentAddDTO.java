package com.forum.dto;



import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CommentAddDTO {
    @NotNull(message = "文章ID不能为空")
    private Long articleId;

    @NotBlank(message = "文章内容不能为空")
    private String content;

    /**
     * 父评论ID
     * 0 = 一级评论
     */
    private Long parentId;

    /**
     * 被回复用户ID
     */
    private Long replyUserId;
}

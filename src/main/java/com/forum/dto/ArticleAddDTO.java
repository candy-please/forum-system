package com.forum.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@ApiModel("文章新增对象")
public class ArticleAddDTO {
    @ApiModelProperty("文章标题")
    @NotBlank(message = "文章标题不能为空")
    @Size(max = 100,message = "文章标题不能超过100个字符")
    private String title;

    @ApiModelProperty("文章内容")
    @NotBlank(message = "文章内容不能为空")
    private String content;

    @ApiModelProperty("分类ID")
    @NotBlank(message = "分类不能为空")
    private Long categoryId;
}

package com.forum.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("文章返回对象")
public class ArticleVO {
    @ApiModelProperty("文章ID")
    private Long id;

    @ApiModelProperty("文章标题")
    private String title;

    // 摘要（可以截取 content 前100字）
    @ApiModelProperty("文章摘要")
    private String summary;

    private Long categoryId;

    private String categoryName;

    private Long userId;

    private String authorName;

    private Integer viewCount;

    private Integer likeCount;

    private Boolean isLiked;

    private Boolean isFavorited;

    private Date createTime;
}

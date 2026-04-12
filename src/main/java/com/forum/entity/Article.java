package com.forum.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;


@Data
@TableName("article")
public class Article {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long categoryId;

    private Integer status;

    private Integer viewCount;

    private Integer likeCount;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;
}


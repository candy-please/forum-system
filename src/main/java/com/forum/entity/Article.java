package com.forum.entity;

import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;


@Data
public class Article {

    private Long id;

    private String title;

    private String content;

    private Long userId;

    private Long categoryId;

    private Integer status;

    private Integer viewCount;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;
}


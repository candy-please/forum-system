package com.forum.entity;



import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;

@Data
public class Comment {

    private Long id;

    private Long articleId;

    private Long userId;

    private String content;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;
}

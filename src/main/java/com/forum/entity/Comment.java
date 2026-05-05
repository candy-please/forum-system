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

    //母级评论ID
    private Long parentId;

    //回复者ID
    private Long replyUserId;

    private Date createTime;

    private Date updateTime;

    @TableLogic
    private Integer deleted;
}

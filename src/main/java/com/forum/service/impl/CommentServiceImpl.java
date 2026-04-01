package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.Result;
import com.forum.dto.CommentAddDTO;
import com.forum.entity.Article;
import com.forum.entity.Comment;
import com.forum.entity.User;
import com.forum.mapper.ArticleMapper;
import com.forum.mapper.CommentMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.CommentService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public Result add(CommentAddDTO dto, String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            return Result.error("未登录");
        }

        Long uid;
        try {
            uid = Long.valueOf(userId);
        } catch (NumberFormatException e) {
            return Result.error("用户信息异常");
        }

        User user = userMapper.selectById(uid);
        if (user == null) {
            return Result.error("用户不存在");
        }

        Article article = articleMapper.selectById(dto.getArticleId());
        if (article == null) {
            return Result.error("文章不存在");
        }

        Comment comment = new Comment();
        comment.setArticleId(dto.getArticleId());
        comment.setUserId(user.getId());
        comment.setContent(dto.getContent());
        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setDeleted(0);

        commentMapper.insert(comment);
        return Result.success("新增评论成功");
    }

    @Override
    public Result list(Long articleId) {
        List<Comment> list = commentMapper.selectList(
                new QueryWrapper<Comment>()
                        .eq("article_id", articleId)
                        .eq("deleted", 0)
                        .orderByDesc("create_time")
        );
        return Result.success("查询评论成功", list);
    }
}

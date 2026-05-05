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
import com.forum.vo.CommentVO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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


        if (dto.getParentId()==null)
            {comment.setParentId(0L);}
        else {
            comment.setParentId(dto.getParentId());
        }

        comment.setReplyUserId(dto.getReplyUserId());

        comment.setCreateTime(new Date());
        comment.setUpdateTime(new Date());
        comment.setDeleted(0);

        commentMapper.insert(comment);
        return Result.success("新增评论成功");
    }

    @Override
    public Result list(Long articleId) {
        List<Comment> parentList = commentMapper.selectList(
                new QueryWrapper<Comment>()
                        .eq("article_id", articleId)
                        .eq("parent_id",0)
                        .eq("deleted", 0)
                        .orderByDesc("create_time")
        );
        if (parentList.isEmpty()) {
            return Result.success("查询评论成功", new ArrayList<>());
        }

        // ===== 2. 收集父评论ID =====
        List<Long> parentIds = parentList.stream()
                .map(Comment::getId)
                .collect(Collectors.toList());

        // ===== 3. 查询子评论 =====
        List<Comment> childList = commentMapper.selectList(
                new QueryWrapper<Comment>()
                        .in("parent_id", parentIds)
                        .eq("deleted", 0)
        );

        // ===== 4. 收集所有 userId（关键🔥）=====
        Set<Long> userIds = new HashSet<>();

        parentList.forEach(c -> userIds.add(c.getUserId()));
        childList.forEach(c -> {
            userIds.add(c.getUserId());
            if (c.getReplyUserId() != null) {
                userIds.add(c.getReplyUserId());
            }
        });

        // ===== 5. 一次查用户 =====
        List<User> users = userMapper.selectBatchIds(userIds);

        Map<Long, String> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, User::getUserName));

        // ===== 6. child分组 =====
        Map<Long, List<Comment>> childMap = childList.stream()
                .collect(Collectors.groupingBy(Comment::getParentId));

        // ===== 7. 组装 VO =====
        List<CommentVO> result = new ArrayList<>();

        for (Comment parent : parentList) {

            CommentVO parentVO = buildVO(parent, userMap);

            List<Comment> children = childMap.get(parent.getId());

            if (children != null) {
                List<CommentVO> childVOList = children.stream()
                        .map(c -> buildVO(c, userMap))
                        .collect(Collectors.toList());

                parentVO.setChildren(childVOList);
            } else {
                parentVO.setChildren(new ArrayList<>());
            }

            result.add(parentVO);
        }

        return Result.success("查询评论成功", result);

    }

    private CommentVO buildVO(Comment comment, Map<Long, String> userMap) {

        CommentVO vo = new CommentVO();

        vo.setId(comment.getId());
        vo.setArticleId(comment.getArticleId());
        vo.setUserId(comment.getUserId());
        vo.setContent(comment.getContent());
        vo.setCreateTime(comment.getCreateTime());

        // 当前用户
        vo.setUserName(userMap.get(comment.getUserId()));

        // 回复用户
        if (comment.getReplyUserId() != null) {
            vo.setReplyUserId(comment.getReplyUserId());
            vo.setReplyUserName(userMap.get(comment.getReplyUserId()));
        }
        vo.setChildren(new ArrayList<>());
        return vo;
    }
}

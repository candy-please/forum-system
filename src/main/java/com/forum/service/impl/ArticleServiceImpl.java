package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.entity.Article;
import com.forum.entity.Category;
import com.forum.entity.User;
import com.forum.mapper.ArticleMapper;
import com.forum.mapper.CategoryMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.ArticleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CategoryMapper categoryMapper;

    public Result add(ArticleAddDTO dto, String userName){
        if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
            return Result.error("文章标题不能为空");
        }

        if (dto.getContent() == null || dto.getContent().trim().isEmpty()) {
            return Result.error("文章内容不能为空");
        }

        if (dto.getCategoryId() == null) {
            return Result.error("分类不能为空");
        }

        User user = userMapper.selectOne(
                new QueryWrapper<User>().eq("user_name", userName)
        );

        if (user == null) {
            return Result.error("当前用户不存在");
        }

        Category category = categoryMapper.selectById(dto.getCategoryId());
        if (category == null) {
            return Result.error("分类不存在");
        }

        Article article = new Article();
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setUserId(user.getId());
        article.setCategoryId(dto.getCategoryId());
        article.setStatus(1);
        article.setViewCount(0);
        article.setCreateTime(new Date());
        article.setUpdateTime(new Date());
        article.setDeleted(0);

        articleMapper.insert(article);

        return Result.success("发布文章成功");
    }

    @Override
    public Result list(ArticleQueryDTO dto) {

        Page<Article> page = new Page<>(dto.getCurrent(), dto.getSize());

        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .eq("status", 1)
                .orderByDesc("create_time");

        if (dto.getCategoryId() != null) {
            wrapper.eq("category_id", dto.getCategoryId());
        }

        Page<Article> resultPage = articleMapper.selectPage(page, wrapper);

        return Result.success("查询文章列表成功", resultPage);
    }

    @Override
    public Result detail(Long id) {
        Article article = articleMapper.selectById(id);

        if (article == null || article.getDeleted() == 1) {
            return Result.error("文章不存在");
        }
        Integer viewCount = article.getViewCount() == null ? 0 : article.getViewCount();
        article.setViewCount(viewCount + 1);
        article.setUpdateTime(new Date());
        articleMapper.updateById(article);

        return Result.success("查询文章详情成功", article);
    }
}

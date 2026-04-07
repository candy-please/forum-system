package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
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
import com.forum.vo.ArticleDetailVO;
import com.forum.vo.ArticleVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public Result add(ArticleAddDTO dto, String userName){
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

        List<ArticleVO> voList = resultPage.getRecords().stream().map(article -> {
            ArticleVO vo = new ArticleVO();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());

            String content = article.getContent();
            if (content != null && content.length() > 100) {
                vo.setSummary(content.substring(0, 100));
            } else {
                vo.setSummary(content);
            }

            vo.setCategoryId(article.getCategoryId());
            vo.setUserId(article.getUserId());
            vo.setViewCount(article.getViewCount());
            vo.setCreateTime(article.getCreateTime());

            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }

            User user = userMapper.selectById(article.getUserId());
            if (user != null) {
                vo.setAuthorName(user.getUserName());
            }

            return vo;
        }).collect(Collectors.toList());

        PageResult<ArticleVO> pageResult = new PageResult<>();
        pageResult.setTotal(resultPage.getTotal());
        pageResult.setRecords(voList);

        return Result.success("查询文章列表成功", pageResult);
    }

    @Override
    public Result detail(Long id) {

        Article article = articleMapper.selectById(id);

        if (article == null || article.getDeleted() == 1) {
            return Result.error("文章不存在");
        }

        // 1. Redis key 统一格式
        String key = "article:viewCount:" + id;

        // 2. 先判断 Redis 中有没有这个浏览量
        String redisValue = stringRedisTemplate.opsForValue().get(key);

        Integer viewCount;
        if (redisValue == null) {
            // 第一次访问：先把数据库中的浏览量放入 Redis
            stringRedisTemplate.opsForValue().set(key, String.valueOf(article.getViewCount()));
        }

        // 3. 再自增
        Long newCount = stringRedisTemplate.opsForValue().increment(key);
        viewCount = newCount.intValue();

        System.out.println("Redis当前值 = " + viewCount);

        // ===== VO封装开始 =====
        ArticleDetailVO vo = new ArticleDetailVO();
        vo.setId(article.getId());
        vo.setTitle(article.getTitle());
        vo.setContent(article.getContent());
        vo.setCategoryId(article.getCategoryId());
        vo.setUserId(article.getUserId());
        vo.setViewCount(viewCount);   // 这里必须用最新值
        vo.setCreateTime(article.getCreateTime());

        // 分类名称
        Category category = categoryMapper.selectById(article.getCategoryId());
        if (category != null) {
            vo.setCategoryName(category.getName());
        }

        // 作者名称
        User user = userMapper.selectById(article.getUserId());
        if (user != null) {
            vo.setAuthorName(user.getUserName());
        }

        return Result.success("查询文章详情成功", vo);
    }
}

package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.forum.common.PageResult;
import com.forum.common.Result;
import com.forum.common.exception.BusinessException;
import com.forum.constants.RedisKeyConstants;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.dto.ArticleUpdateDTO;
import com.forum.entity.Article;
import com.forum.entity.Category;
import com.forum.entity.User;
import com.forum.mapper.ArticleMapper;
import com.forum.mapper.CategoryMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.ArticleLikeService;
import com.forum.service.ArticleService;
import com.forum.utils.LoginUserUtil;
import com.forum.vo.ArticleDetailVO;
import com.forum.vo.ArticleVO;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
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

    @Resource
    private ArticleLikeService articleLikeService;

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
            vo.setLikeCount(article.getLikeCount());

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
            throw new BusinessException(400, "文章不存在");
        }

        // ===== 浏览量处理 =====
        String key = "article:viewCount:" + id;

        String redisValue = stringRedisTemplate.opsForValue().get(key);

        Long newCount;
        if (redisValue == null) {
            stringRedisTemplate.opsForValue().set(key, String.valueOf(article.getViewCount()));
            newCount = Long.valueOf(article.getViewCount());
        } else {
            newCount = stringRedisTemplate.opsForValue().increment(key);
        }

        Integer viewCount = newCount.intValue();

        // ===== VO封装 =====
        ArticleDetailVO vo = new ArticleDetailVO();
        vo.setId(article.getId());
        vo.setTitle(article.getTitle());
        vo.setContent(article.getContent());
        vo.setCategoryId(article.getCategoryId());
        vo.setUserId(article.getUserId());

        String likeCountKey = RedisKeyConstants.ARTICLE_LIKE_COUNT_PREFIX + id;
        String redisLikeCount = stringRedisTemplate.opsForValue().get(likeCountKey);

        Integer likeCount;
        if (redisLikeCount == null) {
            likeCount = article.getLikeCount() == null ? 0 : article.getLikeCount();
            stringRedisTemplate.opsForValue().set(likeCountKey, likeCount.toString());
        } else {
            likeCount = Integer.parseInt(redisLikeCount);
        }
        vo.setLikeCount(article.getLikeCount());


        vo.setViewCount(viewCount);
        vo.setCreateTime(article.getCreateTime());

        // ===== 是否点赞 =====
        Long currentUserId = LoginUserUtil.getCurrentUserIdOrNull();
        boolean isLiked = false;
        if (currentUserId != null) {
            isLiked = articleLikeService.hasLiked(article.getId(), currentUserId);
        }
        vo.setIsLiked(isLiked);

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

    @Override
    public Result update(ArticleUpdateDTO articleUpdateDTO, Long userId){
        Article article=articleMapper.selectById(articleUpdateDTO.getId());
        if(article==null || article.getDeleted()==-1) {
            return Result.error("文章不存在");
            }

        if(!article.getUserId().equals(userId)) {
            return Result.error(403,"无更改他人文章权限");
            }

        Category category=categoryMapper.selectById(article.getCategoryId());
        if (category==null){
            return Result.error("该分类不存在");
        }

        article.setTitle(articleUpdateDTO.getTitle());
        article.setContent(articleUpdateDTO.getContent());
        article.setCategoryId(articleUpdateDTO.getCategoryId());
        article.setUpdateTime(new Date());
        articleMapper.updateById(article);
        return Result.success("修改成功");



    }

    @Override
    public Result delete(Long id, Long userId) {
        Article article = articleMapper.selectById(id);

        if (article == null || article.getDeleted() == 1) {
            return Result.error("文章不存在");
        }

        if (!article.getUserId().equals(userId)) {
            throw new BusinessException(403, "无权限删除他人文章");
        }

        articleMapper.deleteById(id);

        return Result.success("删除文章成功");
    }

    @Override
    public Result hotList(Integer size) {

        Set<ZSetOperations.TypedTuple<String>> typedTuples =
                stringRedisTemplate.opsForZSet().reverseRangeWithScores(
                        RedisKeyConstants.ARTICLE_HOT_KEY, 0, size - 1
                );

        if (typedTuples == null || typedTuples.isEmpty()) {
            return Result.success("查询热门文章成功", Collections.emptyList());
        }

        List<ArticleVO> voList = new ArrayList<>();

        for (ZSetOperations.TypedTuple<String> tuple : typedTuples) {
            if (tuple == null || tuple.getValue() == null) {
                continue;
            }

            Long articleId = Long.valueOf(tuple.getValue());
            Article article = articleMapper.selectById(articleId);

            if (article == null || article.getDeleted() == 1 || article.getStatus() != 1) {
                continue;
            }

            ArticleVO vo = new ArticleVO();
            vo.setId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setCategoryId(article.getCategoryId());
            vo.setUserId(article.getUserId());
            vo.setViewCount(article.getViewCount());
            vo.setLikeCount(article.getLikeCount());
            vo.setCreateTime(article.getCreateTime());
            vo.setIsLiked(false);

            String content = article.getContent();
            if (content != null && content.length() > 100) {
                vo.setSummary(content.substring(0, 100));
            } else {
                vo.setSummary(content);
            }

            Category category = categoryMapper.selectById(article.getCategoryId());
            if (category != null) {
                vo.setCategoryName(category.getName());
            }

            User user = userMapper.selectById(article.getUserId());
            if (user != null) {
                vo.setAuthorName(user.getUserName());
            }

            voList.add(vo);
        }

        return Result.success("查询热门文章成功", voList);
    }
}

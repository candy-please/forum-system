package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.Result;
import com.forum.common.exception.BusinessException;
import com.forum.constants.RedisKeyConstants;
import com.forum.entity.Article;
import com.forum.entity.ArticleLike;
import com.forum.mapper.ArticleLikeMapper;
import com.forum.mapper.ArticleMapper;
import com.forum.service.ArticleLikeService;
import com.forum.service.ArticleService;
import com.forum.utils.LoginUserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Service
public class ArticleLikeServiceImpl implements ArticleLikeService {
    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private ArticleLikeMapper articleLikeMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    @Transactional
    public Result likeArticle(Long articleId) {

        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getDeleted() == 1) {
            throw new BusinessException(400, "文章不存在");
        }

        Long userId = LoginUserUtil.getUserId();
        String userKey=getLikeUsersKey(articleId);
        String countKey=getLikeCountKey(articleId);

        Boolean isMember=stringRedisTemplate.opsForSet()
                .isMember(userKey,userId.toString());
        if(Boolean.TRUE.equals(isMember)){
            throw new BusinessException(409,"你已经点过赞了");
        }
        if(Boolean.FALSE.equals(stringRedisTemplate.hasKey(countKey))){
            Integer dbLikeCount=article.getLikeCount()==null ? 0:article.getLikeCount();
            stringRedisTemplate.opsForValue().set(countKey,dbLikeCount.toString());
        }

        stringRedisTemplate.opsForSet().add(userKey,userId.toString());
        stringRedisTemplate.opsForValue().increment(countKey);
        return Result.success("点赞成功");
    }

    @Override
    @Transactional
    public Result unlikeArticle(Long articleId) {

        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getDeleted() == 1) {
            throw new BusinessException(400, "文章不存在");
        }

        Long userId = LoginUserUtil.getUserId();

        String usersKey = getLikeUsersKey(articleId);
        String countKey = getLikeCountKey(articleId);

        Boolean isMember = stringRedisTemplate.opsForSet()
                .isMember(usersKey, userId.toString());

        if (!Boolean.TRUE.equals(isMember)) {
            throw new BusinessException(400, "你还没有点赞");
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(countKey))) {
            Integer dbLikeCount = article.getLikeCount() == null ? 0 : article.getLikeCount();
            stringRedisTemplate.opsForValue().set(countKey, dbLikeCount.toString());
        }

        stringRedisTemplate.opsForSet().remove(usersKey, userId.toString());

        Long count = stringRedisTemplate.opsForValue().decrement(countKey);
        if (count != null && count < 0) {
            stringRedisTemplate.opsForValue().set(countKey, "0");
        }

        return Result.success("取消点赞成功");
    }

    @Override
    public Result isLiked(Long articleId) {
        Article article = articleMapper.selectById(articleId);
        if (article == null || article.getDeleted() == 1) {
            throw new BusinessException(400, "文章不存在");
        }

        Long userId = LoginUserUtil.getUserId();
        return Result.success(hasLiked(articleId, userId));
    }

    @Override
    public boolean hasLiked(Long articleId, Long userId) {
        if (userId == null) {
            return false;
        }

        String usersKey = getLikeUsersKey(articleId);

        Boolean isMember = stringRedisTemplate.opsForSet()
                .isMember(usersKey, userId.toString());

        if (Boolean.TRUE.equals(isMember)) {
            return true;
        }

        if (Boolean.FALSE.equals(stringRedisTemplate.hasKey(usersKey))) {
            QueryWrapper<ArticleLike> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("article_id", articleId)
                    .eq("user_id", userId);

            ArticleLike articleLike = articleLikeMapper.selectOne(queryWrapper);
            if (articleLike != null) {
                stringRedisTemplate.opsForSet().add(usersKey, userId.toString());
                return true;
            }
        }

        return false;
    }


    private String getLikeUsersKey(Long articleId) {
        return RedisKeyConstants.ARTICLE_LIKE_USERS_PREFIX + articleId;
    }

    private String getLikeCountKey(Long articleId) {
        return RedisKeyConstants.ARTICLE_LIKE_COUNT_PREFIX + articleId;
    }
}

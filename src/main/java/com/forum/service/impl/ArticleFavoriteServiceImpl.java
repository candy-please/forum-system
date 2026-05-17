package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.Result;
import com.forum.common.exception.BusinessException;
import com.forum.entity.Article;
import com.forum.entity.ArticleFavorite;
import com.forum.entity.User;
import com.forum.mapper.ArticleFavoriteMapper;
import com.forum.mapper.ArticleMapper;
import com.forum.mapper.UserMapper;
import com.forum.service.ArticleFavoriteService;
import com.forum.utils.LoginUserUtil;
import com.forum.vo.FavoriteArticleVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ArticleFavoriteServiceImpl implements ArticleFavoriteService {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleFavoriteMapper articleFavoriteMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    @Transactional
    public Result favoriteArticle(Long articleId) {

        checkArticleExists(articleId);

        Long userId = LoginUserUtil.getUserId();

        ArticleFavorite favorite =
                articleFavoriteMapper.selectByArticleIdAndUserIdIncludeDeleted(articleId, userId);

        if (favorite != null) {

            if (favorite.getDeleted() == 0) {
                articleFavoriteMapper.updateDeletedById(favorite.getId(), 1);
                return Result.success("取消收藏成功");
            }

            articleFavoriteMapper.updateDeletedById(favorite.getId(), 0);
            return Result.success("收藏成功");
        }

        ArticleFavorite newFavorite = new ArticleFavorite();
        newFavorite.setArticleId(articleId);
        newFavorite.setUserId(userId);
        newFavorite.setCreateTime(new Date());
        newFavorite.setUpdateTime(new Date());
        newFavorite.setDeleted(0);

        articleFavoriteMapper.insert(newFavorite);

        return Result.success("收藏成功");
    }

    @Override
    public Result isFavorited(Long articleId) {

        checkArticleExists(articleId);

        Long userId = LoginUserUtil.getUserId();

        QueryWrapper<ArticleFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                .eq("user_id", userId);

        Long count = articleFavoriteMapper.selectCount(queryWrapper);

        return Result.success("查询成功", count > 0);
    }

    private void checkArticleExists(Long articleId) {

        Article article = articleMapper.selectById(articleId);

        if (article == null || article.getDeleted() == 1) {
            throw new BusinessException(404, "文章不存在");
        }
    }

    @Override
    public boolean hasFavorited(Long articleId, Long userId) {
        if (userId == null) {
            return false;
        }

        QueryWrapper<ArticleFavorite> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("article_id", articleId)
                .eq("user_id", userId);

        return articleFavoriteMapper.selectCount(queryWrapper) > 0;
    }

    @Override
    public Result favoriteList() {

        Long userId = LoginUserUtil.getUserId();

        List<ArticleFavorite> favoriteList = articleFavoriteMapper.selectList(
                new QueryWrapper<ArticleFavorite>()
                        .eq("user_id", userId)
                        .orderByDesc("create_time")
        );

        if (favoriteList.isEmpty()) {
            return Result.success("查询收藏列表成功", new ArrayList<>());
        }

        List<Long> articleIds = favoriteList.stream()
                .map(ArticleFavorite::getArticleId)
                .collect(Collectors.toList());

        List<Article> articles = articleMapper.selectBatchIds(articleIds);

        Map<Long, Article> articleMap = articles.stream()
                .collect(Collectors.toMap(Article::getId, article -> article));

        Set<Long> authorIds = articles.stream()
                .map(Article::getUserId)
                .collect(Collectors.toSet());

        List<User> users = userMapper.selectBatchIds(authorIds);

        Map<Long, String> userMap = users.stream()
                .collect(Collectors.toMap(User::getId, User::getUserName));

        List<FavoriteArticleVO> result = new ArrayList<>();

        for (ArticleFavorite favorite : favoriteList) {

            Article article = articleMap.get(favorite.getArticleId());

            if (article == null) {
                continue;
            }

            FavoriteArticleVO vo = new FavoriteArticleVO();
            vo.setArticleId(article.getId());
            vo.setTitle(article.getTitle());
            vo.setSummary(article.getContent().length() > 50
                    ? article.getContent().substring(0, 50)
                    : article.getContent());
            vo.setAuthorId(article.getUserId());
            vo.setAuthorName(userMap.get(article.getUserId()));
            vo.setFavoriteTime(favorite.getCreateTime());

            result.add(vo);
        }

        return Result.success("查询收藏列表成功", result);
    }
}

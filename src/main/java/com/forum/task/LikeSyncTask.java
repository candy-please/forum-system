package com.forum.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.constants.RedisKeyConstants;
import com.forum.entity.Article;
import com.forum.entity.ArticleLike;
import com.forum.mapper.ArticleLikeMapper;
import com.forum.mapper.ArticleMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;

@Component
public class LikeSyncTask {

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleLikeMapper articleLikeMapper;

    @Scheduled(fixedRate = 60000)
    public void syncLikeData() {
        Set<String> countKeys = stringRedisTemplate.keys(RedisKeyConstants.ARTICLE_LIKE_COUNT_PREFIX + "*");
        if (countKeys == null || countKeys.isEmpty()) {
            return;
        }

        for (String countKey : countKeys) {
            Long articleId = Long.valueOf(countKey.replace(RedisKeyConstants.ARTICLE_LIKE_COUNT_PREFIX, ""));
            String redisCount = stringRedisTemplate.opsForValue().get(countKey);
            int likeCount = redisCount == null ? 0 : Integer.parseInt(redisCount);

            Article article = articleMapper.selectById(articleId);
            if (article != null && article.getDeleted() == 0) {
                article.setLikeCount(likeCount);
                articleMapper.updateById(article);
            }

            String usersKey = RedisKeyConstants.ARTICLE_LIKE_USERS_PREFIX + articleId;
            Set<String> userIds = stringRedisTemplate.opsForSet().members(usersKey);

            // 先删库里该文章的点赞关系，再按 Redis 重建
            QueryWrapper<ArticleLike> wrapper = new QueryWrapper<>();
            wrapper.eq("article_id", articleId);
            articleLikeMapper.delete(wrapper);

            if (userIds != null && !userIds.isEmpty()) {
                for (String userIdStr : userIds) {
                    ArticleLike articleLike = new ArticleLike();
                    articleLike.setArticleId(articleId);
                    articleLike.setUserId(Long.valueOf(userIdStr));
                    articleLikeMapper.insert(articleLike);
                }
            }
        }
    }
}

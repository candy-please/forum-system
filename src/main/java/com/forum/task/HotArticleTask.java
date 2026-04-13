package com.forum.task;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.constants.RedisKeyConstants;
import com.forum.entity.Article;
import com.forum.mapper.ArticleMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class HotArticleTask {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(fixedRate = 60000)
    public void syncHotArticles() {
        QueryWrapper<Article> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .eq("status", 1);

        List<Article> articleList = articleMapper.selectList(wrapper);
        if (articleList == null || articleList.isEmpty()) {
            return;
        }

        for (Article article : articleList) {
            Long articleId = article.getId();

            String viewKey = RedisKeyConstants.ARTICLE_VIEW_COUNT_PREFIX + articleId;
            String likeKey = RedisKeyConstants.ARTICLE_LIKE_COUNT_PREFIX + articleId;

            String redisViewCount = stringRedisTemplate.opsForValue().get(viewKey);
            String redisLikeCount = stringRedisTemplate.opsForValue().get(likeKey);

            int viewCount = redisViewCount == null ?
                    (article.getViewCount() == null ? 0 : article.getViewCount())
                    : Integer.parseInt(redisViewCount);

            int likeCount = redisLikeCount == null ?
                    (article.getLikeCount() == null ? 0 : article.getLikeCount())
                    : Integer.parseInt(redisLikeCount);

            double score = viewCount + likeCount * 10.0;

            stringRedisTemplate.opsForZSet().add(
                    RedisKeyConstants.ARTICLE_HOT_KEY,
                    articleId.toString(),
                    score
            );
        }

        System.out.println("热门文章排行同步完成");
    }
}

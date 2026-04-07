package com.forum.task;

import com.forum.entity.Article;
import com.forum.mapper.ArticleMapper;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
@Component
public class ViewCountSyncTask {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private ArticleMapper articleMapper;

    @Scheduled(fixedRate = 5*60*1000)
    public void syncViewCount(){
        System.out.println("===== 定时任务开始执行 =====");
        Set<String> keys=stringRedisTemplate.keys("article:viewCount:*");
        System.out.println("keys = " + keys);
        if (keys==null || keys.isEmpty()) {
            System.out.println("没有找到浏览量key");
            return;
        }

        for (String key :keys){
            String articleIdStr=key.replace("article:viewCount:","");
            Long articleId=Long.valueOf(articleIdStr);

            String value=stringRedisTemplate.opsForValue().get(key);
            System.out.println("key = " + key + ", value = " + value);
            if(value==null){
                continue;
            }
            Integer viewCount=Integer.valueOf(value);

            Article article=new Article();
            article.setId(articleId);
            article.setViewCount(viewCount);

            int rows=articleMapper.updateById(article);
            System.out.println("更新结果 rows = " + rows);

        }
    }
}

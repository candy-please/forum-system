package com.forum.task;

import com.forum.constants.RedisKeyConstants;
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

    @Scheduled(fixedDelay = 5*60*1000)
    public void syncViewCount(){
        try{
            System.out.println("===== 定时任务开始执行 =====");
            String pattern = RedisKeyConstants.ARTICLE_VIEW_COUNT_PREFIX+"*";

            Set<String> keys=stringRedisTemplate.keys(pattern);
            if (keys==null || keys.isEmpty()) {
                System.out.println("没有找到浏览量key");
                return;
            }

            for (String key :keys){
                String articleIdStr=key.replace(RedisKeyConstants.ARTICLE_VIEW_COUNT_PREFIX,"");
                Long articleId=Long.valueOf(articleIdStr);

                String value=stringRedisTemplate.opsForValue().get(key);
                if(value==null){
                    continue;
                }
                Integer viewCount=Integer.valueOf(value);

                Article article=new Article();
                article.setId(articleId);
                article.setViewCount(viewCount);

                int rows=articleMapper.updateById(article);
                System.out.println("文章ID：" + articleId + "，浏览量同步结果：" + rows);
                }
                System.out.println("===== 浏览量定时同步任务执行完成 =====");
            }catch (Exception e){
            e.printStackTrace();
        }
    }
}

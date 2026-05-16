package com.forum.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.forum.entity.ArticleFavorite;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ArticleFavoriteMapper extends BaseMapper<ArticleFavorite> {
    @Select("select * from article_favorite where article_id = #{articleId} and user_id = #{userId} limit 1")
    ArticleFavorite selectByArticleIdAndUserIdIncludeDeleted(@Param("articleId") Long articleId,
                                                             @Param("userId") Long userId);

    @Update("update article_favorite set deleted = #{deleted}, update_time = now() where id = #{id}")
    int updateDeletedById(@Param("id") Long id, @Param("deleted") Integer deleted);
}

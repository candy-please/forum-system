package com.forum.service;

import com.forum.common.Result;

public interface ArticleFavoriteService {
    Result favoriteArticle(Long articleId);

    Result isFavorited(Long articleId);
    boolean hasFavorited(Long articleId, Long userId);


}

package com.forum.service;

import com.forum.common.Result;

public interface ArticleLikeService {
    Result likeArticle(Long articleId);

    Result unlikeArticle(Long articleId);

    Result isLiked(Long articleId);

    boolean hasLiked(Long articleId, Long userId);
}

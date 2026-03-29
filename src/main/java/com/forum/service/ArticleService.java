package com.forum.service;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;

public interface ArticleService {
    Result add(ArticleAddDTO dto,String username);
    Result list(ArticleQueryDTO dto);
    Result detail(Long id);
}

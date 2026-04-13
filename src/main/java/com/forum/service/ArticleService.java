package com.forum.service;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.dto.ArticleUpdateDTO;

public interface ArticleService {
    Result add(ArticleAddDTO dto,String username);
    Result list(ArticleQueryDTO dto);
    Result detail(Long id);

    Result update(ArticleUpdateDTO articleUpdateDTO,Long userId);

    Result delete(Long id,Long userId);

    Result hotList(Integer size);
}

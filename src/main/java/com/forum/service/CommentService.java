package com.forum.service;


import com.forum.common.Result;
import com.forum.dto.CommentAddDTO;

public interface CommentService {

    Result add(CommentAddDTO dto, String userName);

    Result list(Long articleId);
}

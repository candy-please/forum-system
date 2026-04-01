package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.CommentAddDTO;
import com.forum.mapper.CommentMapper;
import com.forum.service.CommentService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public Result add(@RequestBody CommentAddDTO dto, Authentication authentication) {
        if (authentication == null) {
            return Result.error("未登录");
        }

        String userId = (String) authentication.getPrincipal();
        return commentService.add(dto, userId);
    }

    @GetMapping("/list")
    public Result list(@RequestParam Long articleId) {

        return commentService.list(articleId);
    }
}

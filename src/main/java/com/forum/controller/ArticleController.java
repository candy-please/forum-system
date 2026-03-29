package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.service.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/add")
    public Result add(@RequestBody ArticleAddDTO dto, Authentication authentication) {

        if (authentication == null) {
            return Result.error("用户未登录");
        }

        String userName = (String) authentication.getPrincipal();

        return articleService.add(dto, userName);
    }

    @GetMapping("/list")
    public Result list(ArticleQueryDTO dto) {
        return articleService.list(dto);
    }
}

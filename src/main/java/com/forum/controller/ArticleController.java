package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.dto.ArticleSearchDTO;
import com.forum.dto.ArticleUpdateDTO;
import com.forum.service.ArticleFavoriteService;
import com.forum.service.ArticleLikeService;
import com.forum.service.ArticleService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleLikeService articleLikeService;

    @Resource
    private ArticleFavoriteService articleFavoriteService;

    @PostMapping("/add")
    public Result add(@RequestBody ArticleAddDTO dto, Authentication authentication) {

        if (authentication == null) {
            return Result.error("用户未登录");
        }

        String userName = (String) authentication.getPrincipal();

        return articleService.add(dto, userName);
    }

    @GetMapping("/list")
    public Result list(@Valid ArticleQueryDTO dto) {
        return articleService.list(dto);
    }

    @GetMapping("/detail/{id}")
    public Result detail(@PathVariable Long id) {
        return articleService.detail(id);
    }

    @PutMapping("/update")
    public Result update(@RequestBody @Valid ArticleUpdateDTO articleUpdateDTO,Authentication authentication){
        if(authentication==null)
        {       return Result.error("用户未登录");
    }
        Long userId=Long.valueOf(authentication.getPrincipal().toString());

        return articleService.update(articleUpdateDTO,userId);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return Result.error("用户未登录");
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return articleService.delete(id, userId);
    }

    @PostMapping("/like/{id}")
    public Result like(@PathVariable Long id) {
        return articleLikeService.likeArticle(id);
    }

    @PostMapping("/unlike/{id}")
    public Result unlike(@PathVariable Long id) {
        return articleLikeService.unlikeArticle(id);
    }

    @GetMapping("/isLiked/{id}")
    public Result isLiked(@PathVariable Long id) {
        return articleLikeService.isLiked(id);
    }

    @PostMapping("/favorite/{id}")
    public Result favorite(@PathVariable Long id) {
        return articleFavoriteService.favoriteArticle(id);
    }

    @GetMapping("/isFavorited/{id}")
    public Result isFavorited(@PathVariable Long id) {
        return articleFavoriteService.isFavorited(id);
    }
    @GetMapping("/hot")
    public Result hotList(@RequestParam(defaultValue = "10") Integer size) {
        return articleService.hotList(size);
    }

    @GetMapping("/search")
    public Result search(@Valid ArticleSearchDTO dto) {
        return articleService.search(dto);
    }
    @GetMapping("/favorite/list")
    public Result favoriteList() {
        return articleFavoriteService.favoriteList();
    }
}

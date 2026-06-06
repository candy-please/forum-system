package com.forum.controller;

import com.forum.common.Result;
import com.forum.dto.ArticleAddDTO;
import com.forum.dto.ArticleQueryDTO;
import com.forum.dto.ArticleSearchDTO;
import com.forum.dto.ArticleUpdateDTO;
import com.forum.service.ArticleFavoriteService;
import com.forum.service.ArticleLikeService;
import com.forum.service.ArticleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

@RestController
@RequestMapping("/article")
@Api(tags = "文章管理接口")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleLikeService articleLikeService;

    @Resource
    private ArticleFavoriteService articleFavoriteService;

  /*  @PostMapping("/add")
    public Result add(@RequestBody ArticleAddDTO dto, Authentication authentication) {

        if (authentication == null) {
            return Result.error("用户未登录");
        }

        String userName = (String) authentication.getPrincipal();

        return articleService.add(dto, userName);
    }*/
  @ApiOperation("发布文章")
    @PostMapping("/add")
    public Result add(
          @ApiParam("文章新增参数") @RequestBody ArticleAddDTO dto) {

        String userId = SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal()
                .toString();

        return articleService.add(dto, userId);
    }
    @ApiOperation("分页查询文章列表")
    @GetMapping("/list")
    public Result list( @ApiParam("分页查询参数") @Valid ArticleQueryDTO dto) {
        return articleService.list(dto);
    }
    @ApiOperation("查询文章详情")
    @GetMapping("/detail/{id}")
    public Result detail(@ApiParam(value = "文章ID", example = "1") @PathVariable Long id) {
        return articleService.detail(id);
    }

    @ApiOperation("修改文章")
    @PutMapping("/update")
    public Result update(
            @ApiParam("文章修改参数")
            @RequestBody @Valid ArticleUpdateDTO articleUpdateDTO,Authentication authentication){
        if(authentication==null)
        {       return Result.error("用户未登录");
    }
        Long userId=Long.valueOf(authentication.getPrincipal().toString());

        return articleService.update(articleUpdateDTO,userId);
    }
    @ApiOperation("删除文章")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, Authentication authentication) {
        if (authentication == null) {
            return Result.error("用户未登录");
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());
        return articleService.delete(id, userId);
    }


    @PostMapping("/like/{id}")
    public Result like(@ApiParam(value="文章ID",example="1")
                           @PathVariable Long id) {
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

    @ApiOperation("文章关键词搜索")
    @GetMapping("/search")
    public Result search(  @ApiParam("搜索关键词")
                               @Valid ArticleSearchDTO dto) {
        return articleService.search(dto);
    }
    @GetMapping("/favorite/list")
    public Result favoriteList() {
        return articleFavoriteService.favoriteList();
    }
    @GetMapping("/myList")
    public Result myList(Authentication authentication) {
        if (authentication == null) {
            return Result.error("用户未登录");
        }

        Long userId = Long.valueOf(authentication.getPrincipal().toString());

        return articleService.myList(userId);
    }
}

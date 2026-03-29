package com.forum.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.forum.common.Result;
import com.forum.entity.Category;
import com.forum.mapper.CategoryMapper;
import com.forum.service.CategoryService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    @Override
    public Result list(){
        List<Category> list=categoryMapper.selectList(
                new QueryWrapper<Category>().eq("status",1)
                        .eq("deleted",0)
                        .orderByAsc("sort"));
                return Result.success("查询成功",list);

    }
}

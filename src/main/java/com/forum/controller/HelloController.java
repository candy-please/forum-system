package com.forum.controller;

import com.forum.service.CommentService;
import com.forum.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class HelloController {

    @GetMapping("/test/hello")
    public String HelloController(){
        return "forum system running";
    }
}

package com.forum;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication
@MapperScan("com.forum.mapper")
public class Application {
    public static void main(String []args){
        SpringApplication.run(Application.class,args);
    }
}


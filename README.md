# Forum System（论坛系统）

## 项目简介
本项目是一个基于 Spring Boot 开发的论坛系统后端，采用前后端分离架构，目前已完成用户模块开发。

## 技术栈
- Java 8
- Spring Boot
- MyBatis-Plus
- MySQL
- Spring Security
- JWT

## 已实现功能
- 用户注册（密码加密存储）
- 用户登录（JWT认证）
- 获取当前登录用户信息
- 权限控制（基于 Spring Security）

## 核心技术点
- 使用 BCrypt 实现密码加密与校验
- 使用 JWT 实现无状态认证
- 自定义过滤器实现 token 校验
- 使用 MyBatis-Plus 简化数据库操作

## 项目结构
- controller：接口层
- service：业务层
- mapper：数据访问层
- entity：实体类
- dto：数据传输对象
- config：配置类
- utils：工具类

## 后续计划
- 分类模块
- 文章模块
- 评论模块
- Vue前端页面

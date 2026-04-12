# Forum System（论坛系统）

## 项目简介
本项目是一个基于 Spring Boot 开发的论坛系统后端，采用前后端分离架构。

## 技术栈
- Java 8
- Spring Boot
- MyBatis-Plus
- MySQL
- Spring Security
- JWT
- Redis

## 已实现功能
- JSR303 参数校验
- 用户模块（注册 / 登录 / JWT）
- 文章模块（发布 / 列表 / 详情）
- 评论模块
- 点赞功能（MySQL + Redis优化）
- 浏览量统计（Redis + 定时同步）


## 核心技术点
- 使用 BCrypt 实现密码加密与校验
- 使用 SpringSecurity + JWT 实现无状态认证
- 自定义过滤器实现 token 校验
- 使用 MyBatis-Plus 简化数据库操作
- 定时任务同步 Redis 数据到 MySQL，保证最终一致性

## 项目结构
- controller：接口层
- service：业务层
- mapper：数据访问层
- entity：实体类
- dto：数据传输对象
- config：配置类
- utils：工具类

## 项目优化亮点
### ① VO 分层设计
### ② 统一接口返回结构 
### ③ 全局异常处理
### ④ 安全性优化
### ⑤ Redis缓存优化




```sql
-- ==========================================
-- Forum System Database Script
-- Author: candy-please
-- Description:
-- Spring Boot + Vue3 + Redis + JWT Forum System
-- ==========================================

CREATE DATABASE IF NOT EXISTS forum_system
DEFAULT CHARACTER SET utf8mb4
COLLATE utf8mb4_general_ci;

USE forum_system;

-- =========================
-- 用户表
-- =========================
DROP TABLE IF EXISTS user;

CREATE TABLE user
(
    id              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    user_name       VARCHAR(50) UNIQUE COMMENT '用户名',
    password        VARCHAR(100) COMMENT '密码',
    email           VARCHAR(100) COMMENT '邮箱',
    phone           VARCHAR(20) COMMENT '手机号',
    avatar          VARCHAR(255) COMMENT '头像地址',
    role            INT DEFAULT 1 COMMENT '角色：1普通用户 2管理员',
    status          INT DEFAULT 1 COMMENT '状态：1正常 0禁用',
    create_date     DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_date     DATETIME DEFAULT CURRENT_TIMESTAMP
                    ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    last_login_date DATETIME COMMENT '最后登录时间',
    deleted         INT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- =========================
-- 分类表
-- =========================
DROP TABLE IF EXISTS category;

CREATE TABLE category
(
    id          BIGINT PRIMARY KEY COMMENT '分类ID',
    name        VARCHAR(50) NOT NULL COMMENT '分类名称',
    sort        INT DEFAULT 0 COMMENT '排序值',
    status      INT DEFAULT 1 COMMENT '状态：1启用 0禁用',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     INT DEFAULT 0 COMMENT '逻辑删除'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章分类表';

-- =========================
-- 文章表
-- =========================
DROP TABLE IF EXISTS article;

CREATE TABLE article
(
    id          BIGINT PRIMARY KEY COMMENT '文章ID（雪花算法）',
    title       VARCHAR(100) NOT NULL COMMENT '文章标题',
    content     TEXT NOT NULL COMMENT '文章内容',
    user_id     BIGINT NOT NULL COMMENT '作者ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    status      INT DEFAULT 1 COMMENT '状态：1已发布 0草稿',
    view_count  INT DEFAULT 0 COMMENT '浏览量',
    like_count  INT DEFAULT 0 COMMENT '点赞数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     INT DEFAULT 0 COMMENT '逻辑删除',

    KEY idx_user_id (user_id),
    KEY idx_category_id (category_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- =========================
-- 评论表
-- =========================
DROP TABLE IF EXISTS comment;

CREATE TABLE comment
(
    id            BIGINT PRIMARY KEY COMMENT '评论ID',
    article_id    BIGINT NOT NULL COMMENT '文章ID',
    user_id       BIGINT NOT NULL COMMENT '评论用户ID',
    content       VARCHAR(500) NOT NULL COMMENT '评论内容',
    parent_id     BIGINT DEFAULT 0 COMMENT '父评论ID',
    reply_user_id BIGINT COMMENT '被回复用户ID',
    create_time   DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time   DATETIME DEFAULT CURRENT_TIMESTAMP
                  ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted       INT DEFAULT 0 COMMENT '逻辑删除',

    KEY idx_article_id (article_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='评论表';

-- =========================
-- 点赞表
-- =========================
DROP TABLE IF EXISTS article_like;

CREATE TABLE article_like
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    article_id  BIGINT NOT NULL COMMENT '文章ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT DEFAULT 0 COMMENT '逻辑删除',

    UNIQUE KEY uk_article_user (article_id, user_id),
    KEY idx_article_id (article_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章点赞表';

-- =========================
-- 收藏表
-- =========================
DROP TABLE IF EXISTS article_favorite;

CREATE TABLE article_favorite
(
    id          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键',
    article_id  BIGINT NOT NULL COMMENT '文章ID',
    user_id     BIGINT NOT NULL COMMENT '用户ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP
                ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    deleted     TINYINT DEFAULT 0 COMMENT '逻辑删除',

    UNIQUE KEY uk_user_article (user_id, article_id),
    KEY idx_article_id (article_id),
    KEY idx_user_id (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章收藏表';

-- =========================
-- 初始化分类数据
-- =========================
INSERT INTO category(id,name,sort,status)
VALUES
(1,'技术交流',1,1),
(2,'学习分享',2,1),
(3,'生活随笔',3,1);

```

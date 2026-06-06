# 🚀 基于 Spring Boot + Vue3 的论坛系统

## 📖 项目介绍

本项目是一个基于 **Spring Boot + Vue3** 的前后端分离论坛系统，实现了用户注册登录、文章发布、评论互动、点赞收藏、热门排行等核心论坛功能。

项目采用 **JWT + Spring Security** 实现身份认证与权限控制，使用 **Redis** 缓存浏览量和点赞数据，并通过定时任务同步数据到 MySQL，提高系统性能和并发处理能力。

---

## 🛠 技术栈

### 后端

* Java 8
* Spring Boot 2.7.18
* Spring Security
* JWT
* MyBatis-Plus 3.5.5
* MySQL 8.0
* Redis
* Knife4j（Swagger）
* Maven

### 前端

* Vue3
* Vue Router
* Element Plus
* Axios
* Vite

---

# ✨ 功能模块

## 👤 用户模块

* 用户注册
* 用户登录
* JWT身份认证
* 登录状态校验
* 退出登录

---

## 📝 文章模块

* 发布文章
* 编辑文章
* 删除文章
* 我的文章
* 文章详情
* 分页查询
* 关键词搜索

---

## 💬 评论模块

* 发表评论
* 查看评论列表

---

## 👍 点赞模块

* 点赞文章
* 取消点赞
* 查询点赞状态

---

## ⭐ 收藏模块

* 收藏文章
* 取消收藏
* 我的收藏
* 查询收藏状态

---

## 🔥 热门排行模块

根据：

```text
浏览量 + 点赞数 × 10
```

计算文章热度。

使用 Redis ZSet 实现热门文章排行榜。

---

# 🌟 项目亮点

## 1.JWT + Spring Security 权限认证

用户登录成功后生成 JWT Token：

```text
Authorization: Bearer Token
```

通过 Spring Security 过滤器进行统一认证。

---

## 2.Redis 浏览量缓存

文章详情访问时：

```text
MySQL → Redis
```

浏览量直接写入 Redis：

```text
article:viewCount:{articleId}
```

定时任务同步回数据库。

---

## 3.Redis 点赞优化

点赞数据缓存到 Redis：

```text
article:like:count:{articleId}

article:like:users:{articleId}
```

减少数据库访问压力。

---

## 4.Redis ZSet 热门排行

热门文章排行榜：

```text
article:hot
```

采用：

```java 
 score = viewCount + likeCount * 10
```

作为排序依据。

---

## 5.DTO + VO 分层设计

采用：

```text
Controller
↓
DTO
↓
Service
↓
Entity
↓
VO
```

实现数据隔离，避免直接暴露数据库实体。

---

## 6.全局异常处理

统一处理：

* 参数校验异常
* 业务异常
* 系统异常

返回统一 Result 结果。

---

## 7.Knife4j 接口文档

接口文档地址：

```text
http://localhost:8080/doc.html
```

支持：

* 接口调试
* 参数查看
* 响应预览

---

# 🗄 数据库设计

主要数据表：

```text
user
category
article
comment
article_like
article_favorite
```

---

## user

用户信息表

| 字段        | 说明   |
| --------- | ---- |
| id        | 用户ID |
| user_name | 用户名  |
| password  | 密码   |
| email     | 邮箱   |
| role      | 角色   |
| status    | 状态   |

---

## article

文章表

| 字段          | 说明   |
| ----------- | ---- |
| id          | 文章ID |
| title       | 标题   |
| summary     | 摘要   |
| content     | 内容   |
| category_id | 分类ID |
| user_id     | 作者ID |
| view_count  | 浏览量  |
| like_count  | 点赞数  |

---

## comment

评论表

| 字段         | 说明   |
| ---------- | ---- |
| id         | 评论ID |
| article_id | 文章ID |
| user_id    | 评论用户 |
| content    | 评论内容 |

---

# 🚀 Redis设计

浏览量：

```text
article:viewCount:{articleId}
```

点赞数：

```text
article:like:count:{articleId}
```

点赞用户：

```text
article:like:users:{articleId}
```

热门排行：

```text
article:hot
```

---

# 📂 项目结构

```text
com.forum
├── controller
├── service
│   └── impl
├── mapper
├── entity
├── dto
├── vo
├── config
├── common
├── task
├── utils
└── constants
```

---

# ⏰ 定时任务

## 浏览量同步

```java
ViewCountSyncTask
```

每5分钟同步 Redis 浏览量到 MySQL。

---

## 点赞同步

```java
LikeSyncTask
```

定时同步点赞数据。

---

## 热门排行同步

```java
HotArticleTask
```

定时刷新热门文章排行榜。

---


# ⚙️ 启动方式

## 1.配置 MySQL

修改：

```yaml
application.yml
```

配置数据库连接。

---

## 2.启动 Redis

```bash
redis-server
```

---

## 3.启动后端

```bash
mvn spring-boot:run
```

启动地址：

```text
http://localhost:8080
```

---

## 4.启动前端

```bash
npm install

npm run dev
```

启动地址：

```text
http://localhost:5173
```

---

# 📚 接口文档

启动项目后访问：

```text
http://localhost:8080/doc.html
```

---

# 🎯 后续优化方向

* 用户头像上传
* 用户个人中心
* 个性签名
* 富文本编辑器
* 图片上传
* 消息通知
* 用户关注
* 后台管理系统
* Docker部署
* Spring Boot 3升级

---

# 👨‍💻 作者

Candy_I

Java后端开发方向

欢迎交流学习。



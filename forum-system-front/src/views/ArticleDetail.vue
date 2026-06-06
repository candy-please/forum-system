<template>
  <div class="detail-container">
    <el-card>
      <h2>{{ article.title }}</h2>

      <div class="article-meta">
        <span>作者：{{ article.authorName }}</span>
        <span>分类：{{ article.categoryName }}</span>
        <span>浏览：{{ article.viewCount }}</span>

        <el-button
          type="primary"
          size="small"
          @click="likeArticle"
        >
          点赞：{{ article.likeCount }}
        </el-button>

        <el-button
          :type="isFavorited ? 'warning' : 'default'"
          size="small"
          @click="handleFavorite"
        >
          {{ isFavorited ? '已收藏' : '收藏' }}
        </el-button>
      </div>

      <el-divider />

      <div class="content">
        {{ article.content }}
      </div>
    </el-card>

    <el-divider />

    <h3>评论区</h3>

    <el-input
      v-model="commentContent"
      type="textarea"
      placeholder="请输入评论内容"
      :rows="3"
    />

    <el-button
      type="primary"
      style="margin-top: 10px"
      @click="submitComment"
    >
      发表评论
    </el-button>

  <el-card
  v-for="comment in commentList"
  :key="comment.id"
  class="comment-card"
>
  <div class="comment-header">
    <div class="avatar">
      {{ comment.userName ? comment.userName.charAt(0).toUpperCase() : 'U' }}
    </div>

    <div>
      <div class="comment-user">{{ comment.userName }}</div>
      <div class="comment-time">{{ formatTime(comment.createTime) }}</div>
    </div>
  </div>

  <div class="comment-content">
    {{ comment.content }}
  </div>
</el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  getArticleDetailApi,likeArticleApi,favoriteArticleApi,isFavoritedApi} from '@/api/article'
import { getCommentListApi, addCommentApi } from '@/api/comment'

const route = useRoute()
const articleId = route.params.id

const article = ref({})
const commentList = ref([])
const commentContent = ref('')
const isFavorited = ref(false)

const loadArticleDetail = async () => {
  const res = await getArticleDetailApi(articleId)

  if (res.code === 200) {
    article.value = res.data
  } else {
    ElMessage.error(res.message || '获取文章详情失败')
  }
}

const likeArticle = async () => {
  const res = await likeArticleApi(articleId)

  if (res.code === 200) {
    ElMessage.success(res.message || '操作成功')
    loadArticleDetail()
  } else {
    ElMessage.error(res.message || '操作失败')
  }
}

const loadFavoriteStatus = async () => {
  const res = await isFavoritedApi(articleId)

  if (res.code === 200) {
    isFavorited.value = res.data
  }
}

const handleFavorite = async () => {
  const res = await favoriteArticleApi(articleId)

  if (res.code === 200) {
    isFavorited.value = !isFavorited.value
    ElMessage.success(res.message || '操作成功')
    loadArticleDetail()
  } else {
    ElMessage.error(res.message || '操作失败')
  }
}

const loadCommentList = async () => {
  const res = await getCommentListApi(articleId)

  if (res.code === 200) {
    commentList.value = res.data || []
  }
}

const submitComment = async () => {
  if (!commentContent.value) {
    ElMessage.warning('请输入评论内容')
    return
  }

  const res = await addCommentApi({
    articleId,
    content: commentContent.value
  })

  if (res.code === 200) {
    ElMessage.success('评论成功')
    commentContent.value = ''
    loadCommentList()
  } else {
    ElMessage.error(res.message || '评论失败')
  }
}

const formatTime = (time) => {
  if (!time) return ''

  const date = new Date(time)

  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  const hour = String(date.getHours()).padStart(2, '0')
  const minute = String(date.getMinutes()).padStart(2, '0')
  const second = String(date.getSeconds()).padStart(2, '0')

  return `${year}-${month}-${day} ${hour}:${minute}:${second}`
}
onMounted(() => {
  loadArticleDetail()
  loadCommentList()
  loadFavoriteStatus()
})
</script>

<style scoped>
.detail-container {
  width: 900px;
  margin: 30px auto;
}

.article-meta {
  display: flex;
  gap: 20px;
  color: #888;
  font-size: 14px;
  margin-top: 10px;
  align-items: center;
  flex-wrap: wrap;
}

.content {
  line-height: 1.8;
  font-size: 16px;
  white-space: pre-wrap;
}

.comment-card {
  margin-top: 12px;
}

.comment-user {
  font-weight: bold;
  margin-bottom: 6px;
}

.comment-content {
  margin-bottom: 6px;
}

.comment-time {
  font-size: 13px;
  color: #999;
}

.comment-card {
  margin-top: 12px;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 10px;
}

.avatar {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #409eff;
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
}

.comment-user {
  font-weight: bold;
}

.comment-time {
  font-size: 13px;
  color: #999;
  margin-top: 4px;
}

.comment-content {
  margin-left: 48px;
  line-height: 1.6;
}
</style>

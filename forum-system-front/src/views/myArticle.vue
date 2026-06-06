<template>
  <div class="my-article-container">
    <div class="header">
      <h2>我的文章</h2>

      <div class="action-buttons">
        <el-button @click="goHome">
          返回首页
        </el-button>

        <el-button type="primary" @click="goPublish">
          发布文章
        </el-button>

        <el-button @click="goFavorite">
          我的收藏
        </el-button>

        <el-button type="danger" @click="logout">
          退出登录
        </el-button>
      </div>
    </div>

    <el-empty
      v-if="articleList.length === 0"
      description="暂无文章"
    />

    <el-card
      v-for="article in articleList"
      :key="article.id"
      class="article-card"
    >
      <h3>{{ article.title }}</h3>

      <p>{{ article.summary }}</p>

      <div class="article-info">
        <span>浏览：{{ article.viewCount }}</span>
        <span>点赞：{{ article.likeCount }}</span>
        <span>发布时间：{{ formatTime(article.createTime) }}</span>
      </div>

      <div class="article-actions">
        <el-button
          type="primary"
          size="small"
          @click="goEdit(article.id)"
        >
          编辑
        </el-button>

        <el-button
          type="danger"
          size="small"
          @click="deleteArticle(article.id)"
        >
          删除
        </el-button>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getMyArticleListApi,
  deleteArticleApi
} from '@/api/article'

const router = useRouter()
const articleList = ref([])

const loadMyArticleList = async () => {
  const res = await getMyArticleListApi()

  if (res.code === 200) {
    articleList.value = res.data || []
  } else {
    ElMessage.error(res.message || '获取我的文章失败')
  }
}

const goHome = () => {
  router.push('/')
}

const goPublish = () => {
  router.push('/article/edit')
}

const goFavorite = () => {
  router.push('/favorite')
}

const goEdit = (id) => {
  router.push(`/article/edit/${id}`)
}

const logout = () => {
  localStorage.removeItem('token')
  ElMessage.success('退出成功')
  router.push('/login')
}

const deleteArticle = async (id) => {
  try {
    await ElMessageBox.confirm(
      '确定要删除这篇文章吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteArticleApi(id)

    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadMyArticleList()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (e) {
    // 用户取消删除，不做处理
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
  loadMyArticleList()
})
</script>

<style scoped>
.my-article-container {
  width: 900px;
  margin: 30px auto;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.article-card {
  margin-bottom: 16px;
}

.article-info {
  display: flex;
  gap: 20px;
  color: #888;
  font-size: 14px;
  margin: 12px 0;
}

.article-actions {
  margin-top: 10px;
}
</style>
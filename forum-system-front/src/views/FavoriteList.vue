<template>
  <div class="favorite-container">
    <div class="favorite-header">
      <h2>我的收藏</h2>

      <el-button @click="goHome">
        返回首页
      </el-button>
    </div>

    <el-empty
      v-if="favoriteList.length === 0"
      description="暂无收藏文章"
    />

    <el-card
      v-for="article in favoriteList"
      :key="article.articleId"
      class="article-card"
      @click="goDetail(article.articleId)"
    >
      <h3>{{ article.title }}</h3>

      <p class="summary">
        {{ article.summary }}
      </p>

      <div class="meta">
        <span>作者：{{ article.authorName }}</span>
        <span>收藏时间：{{ formatTime(article.favoriteTime) }}</span>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { favoriteListApi } from '@/api/article'

const router = useRouter()
const favoriteList = ref([])

const loadFavoriteList = async () => {
  const res = await favoriteListApi()

  if (res.code === 200) {
    favoriteList.value = res.data || []
  } else {
    ElMessage.error(res.message || '获取收藏列表失败')
  }
}

const goDetail = (id) => {
  router.push(`/article/detail/${id}`)
}

const goHome = () => {
  router.push('/')
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
  loadFavoriteList()
})
</script>

<style scoped>
.favorite-container {
  width: 900px;
  margin: 30px auto;
}

.favorite-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.article-card {
  margin-bottom: 16px;
  cursor: pointer;
}

.article-card:hover {
  transform: translateY(-2px);
  transition: 0.2s;
}

.summary {
  color: #666;
  margin: 10px 0;
}

.meta {
  display: flex;
  gap: 20px;
  color: #999;
  font-size: 14px;
}
</style>
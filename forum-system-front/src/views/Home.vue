<template>
  <div class="home-container">

    <div class="home-header">
      <h2>论坛首页</h2>

      <div class="action-buttons">
  <el-button type="primary" @click="goPublish">
    发布文章
  </el-button>

  <el-button @click="goMyArticle">
    我的文章
  </el-button>

  <el-button @click="goFavorite">
    我的收藏
  </el-button>

  <el-button type="danger" @click="logout">
    退出登录
  </el-button>
</div>
    </div>
<div class="search-box">
  <el-input
    v-model="keyword"
    placeholder="请输入关键词搜索文章"
    clearable
    @clear="resetSearch"
  />

  <el-button type="primary" @click="searchArticle">
    搜索
  </el-button>
</div>
    <el-card
      v-for="article in articleList"
      :key="article.id"
      class="article-card"
      @click="goDetail(article.id)"
    >
      <h3>{{ article.title }}</h3>

      <p>{{ article.summary }}</p>

      <div class="article-info">
        <span>分类：{{ article.categoryName }}</span>
        <span>作者：{{ article.authorName }}</span>
        <span>浏览：{{ article.viewCount }}</span>
        <span>点赞：{{ article.likeCount }}</span>
      </div>
    </el-card>
<el-pagination
  v-model:current-page="page.current"
  v-model:page-size="page.size"
  :page-sizes="[5,10,20]"
  layout="total, sizes, prev, pager, next"
  :total="total"
  @current-change="handlePageChange"
  @size-change="handlePageChange"
/>
  </div>
</template>
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { getArticleListApi, searchArticleApi } from '@/api/article'

const router = useRouter()

const articleList = ref([])
const keyword = ref('')
const total = ref(0)

const page = reactive({
  current: 1,
  size: 10
})

const loadArticleList = async () => {
  const res = await getArticleListApi({
    current: page.current,
    size: page.size
  })

  if (res.code === 200) {
    articleList.value = res.data.records
    total.value = Number(res.data.total)
  }
}

const searchArticle = async () => {
  page.current = 1

  if (!keyword.value) {
    loadArticleList()
    return
  }

  const res = await searchArticleApi({
    keyword: keyword.value,
    current: page.current,
    size: page.size
  })

  if (res.code === 200) {
    articleList.value = res.data.records
    total.value = Number(res.data.total)
  }
}

const handlePageChange = () => {
  if (keyword.value) {
    searchArticle()
  } else {
    loadArticleList()
  }
}

const resetSearch = () => {
  keyword.value = ''
  page.current = 1
  loadArticleList()
}

const goDetail = (id) => {
  router.push(`/article/detail/${id}`)
}

const goPublish = () => {
  router.push('/article/edit')
}

const goMyArticle = () => {
  router.push('/my/article')
}

const goFavorite = () => {
  router.push('/favorite')
}

const logout = () => {
  localStorage.removeItem('token')
  ElMessage.success('退出成功')
  router.push('/login')
}

onMounted(() => {
  loadArticleList()
})
</script>

<style scoped>
.home-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}
.home-container {
  width: 900px;
  margin: 30px auto;
}

.article-card {
  margin-bottom: 16px;
  cursor: pointer;
}

.article-info {
  display: flex;
  gap: 20px;
  color: #888;
  font-size: 14px;
}
.search-box {
  display: flex;
  gap: 10px;
  margin-bottom: 20px;
}

.el-pagination {
  margin-top:20px;
  justify-content:center;
}
.action-buttons {
  display: flex;
  gap: 10px;
}
</style>

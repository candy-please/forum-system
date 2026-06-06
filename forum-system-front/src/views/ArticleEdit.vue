<template>
  <div class="edit-container">
    <el-card>
      <h2>{{ isEdit ? '编辑文章' : '发布文章' }}</h2>

      <el-form :model="articleForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="articleForm.title" placeholder="请输入文章标题" />
        </el-form-item>

        <el-form-item label="分类">
          <el-select v-model="articleForm.categoryId" placeholder="请选择分类" style="width: 100%">
            <el-option
              v-for="category in categoryList"
              :key="category.id"
              :label="category.name"
              :value="category.id"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="内容">
          <el-input
            v-model="articleForm.content"
            type="textarea"
            :rows="12"
            placeholder="请输入文章内容"
          />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitArticle">
  {{ isEdit ? '保存修改' : '发布文章' }}
</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted ,computed} from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter,useRoute } from 'vue-router'
import { addArticleApi ,updateArticleApi,getArticleDetailApi} from '@/api/article'
import { getCategoryListApi } from '@/api/category'

const router = useRouter()
const route=useRoute()
const categoryList = ref([])

const isEdit=computed(()=>!!route.params.id)

const articleForm = reactive({
    id:'',
  title: '',
  categoryId: '',
  content: ''
})

const loadCategoryList = async () => {
  const res = await getCategoryListApi()

  if (res.code === 200) {
    categoryList.value = res.data
  }
}

const loadArticleDetail=async()=>{
    if(!isEdit.value){
        return
    }
    const res=await getArticleDetailApi(route.params.id)
if(res.code===200){
    articleForm.id=res.data.id
    articleForm.title=res.data.title
    articleForm.categoryId=res.data.categoryId
    articleForm.content=res.data.content
}
}



const submitArticle = async () => {
  if (!articleForm.title || !articleForm.categoryId || !articleForm.content) {
    ElMessage.warning('请填写完整文章信息')
    return
  }

  let res
  if(isEdit.value){
    res=await updateArticleApi(articleForm)
  }else {
    res=await addArticleApi(articleForm)
  }

  if (res.code === 200) {
    ElMessage.success(isEdit.value? '修改成功':'发布成功')
    router.push('/my/article')
  } else {
    ElMessage.error(res.message)
  }
}


onMounted(() => {
  loadCategoryList()
  loadArticleDetail()
})
</script>

<style scoped>
.edit-container {
  width: 900px;
  margin: 30px auto;
}
</style>
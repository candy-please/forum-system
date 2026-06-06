<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>用户注册</h2>

      <el-form :model="form" label-width="80px">
        <el-form-item label="用户名">
          <el-input v-model="form.userName" placeholder="请输入用户名" />
        </el-form-item>

        <el-form-item label="密码">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" show-password />
        </el-form-item>

        <el-form-item label="邮箱">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>

        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="请输入手机号" />
        </el-form-item>

        <el-button type="primary" style="width: 100%" @click="handleRegister">
          注册
        </el-button>

        <div class="login-link" @click="goLogin">
          已有账号？去登录
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import request from '../api/request'

const router = useRouter()

const form = reactive({
  userName: '',
  password: '',
  email: '',
  phone: ''
})

const handleRegister = async () => {
  if (!form.userName || !form.password) {
    ElMessage.warning('用户名和密码不能为空')
    return
  }

  const res = await request.post('/user/register', form)

  if (res.code === 200) {
    ElMessage.success('注册成功，请登录')
    router.push('/login')
  } else {
    ElMessage.error(res.message || '注册失败')
  }
}

const goLogin = () => {
  router.push('/login')
}
</script>

<style scoped>
.register-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: #f5f7fa;
}

.register-card {
  width: 420px;
}

h2 {
  text-align: center;
  margin-bottom: 24px;
}

.login-link {
  margin-top: 16px;
  text-align: center;
  color: #409eff;
  cursor: pointer;
}
</style>
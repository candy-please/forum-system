<template>
  <div class="login-container">
    <el-card class="login-card">
      <h2>论坛系统登录</h2>

      <el-form :model="loginForm">
        <el-form-item>
          <el-input
            v-model="loginForm.userName"
            placeholder="请输入用户名"
          />
        </el-form-item>

        <el-form-item>
          <el-input
            v-model="loginForm.password"
            type="password"
            placeholder="请输入密码"
            show-password
          />
        </el-form-item>

        <el-form-item>
          <el-button
            type="primary"
            style="width: 100%"
            @click="login"
          >
            登录
          </el-button>
              <div class="register-link" @click="goRegister">
  没有账号？去注册
</div>
        </el-form-item>
      </el-form>
    </el-card>

  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { loginApi } from '@/api/user'
import { useRouter } from 'vue-router'
const goRegister = () => {
  router.push('/register')
}
const router = useRouter()

const loginForm = reactive({
  userName: '',
  password: ''
})

const login = async () => {
  if (!loginForm.userName || !loginForm.password) {
    ElMessage.warning('请输入用户名和密码')
    return
  }

  try {
    const res = await loginApi(loginForm)

    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      ElMessage.success('登录成功')
      router.push('/')
    } else {
      ElMessage.error(res.message)
    }
  } catch (error) {
    console.log(error)
  }
}
</script>

<style scoped>
.login-container {
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-card {
  width: 400px;
}
.register-link {
  margin-top: 16px;
  text-align: center;
  color: #409eff;
  cursor: pointer;
}
</style>

<template>
  <div class="login-container">
    <!-- 装饰背景球 (纯CSS实现) -->
    <div class="bg-circle circle-1"></div>
    <div class="bg-circle circle-2"></div>

    <div class="login-box">
      <div class="header">
        <div class="logo-circle">
          <el-icon :size="30" color="#00e5ff"><DataLine /></el-icon>
        </div>
        <h2 class="title">智慧校园能耗监测平台</h2>
        <p class="subtitle">Smart Campus IoT Energy System</p>
      </div>

      <el-form
          ref="formRef"
          :model="form"
          :rules="rules"
          size="large"
          class="login-form"
      >
        <el-form-item prop="username">
          <el-input
              v-model="form.username"
              placeholder="管理员账号"
              :prefix-icon="User"
              class="dark-input"
          />
        </el-form-item>

        <el-form-item prop="password">
          <el-input
              v-model="form.password"
              type="password"
              placeholder="密码"
              :prefix-icon="Lock"
              show-password
              @keyup.enter="handleLogin"
              class="dark-input"
          />
        </el-form-item>

        <el-form-item>
          <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="loading"
          >
            LOGIN SYSTEM
          </el-button>
        </el-form-item>
      </el-form>

      <div class="footer-tips">
        <span>© 2025 Smart Campus IoT</span>
        <span class="divider">|</span>
        <span>测试账号: admin / 123456</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { User, Lock, DataLine } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import type { FormInstance, FormRules } from 'element-plus'
import { login } from '@/api/user'

const router = useRouter()
const formRef = ref<FormInstance>()
const loading = ref(false)

const form = reactive({
  username: '',
  password: ''
})

const rules = reactive<FormRules>({
  username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
})

const handleLogin = async () => {
  if (!formRef.value) return
  await formRef.value.validate(async (valid) => {
    if (valid) {
      loading.value = true
      try {
        const data = await login(form)
        localStorage.setItem('token', data.token)
        ElMessage.success('身份验证通过')
        router.push('/dashboard')
      } catch (error) {
        console.error(error)
      } finally {
        loading.value = false
      }
    }
  })
}
</script>

<style scoped>
/* 方案A：科技深蓝主题样式 */
.login-container {
  height: 100vh;
  width: 100vw;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #0b1a38; /* 深蓝底色 */
  overflow: hidden;
}

/* 动态背景装饰 */
.bg-circle {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.6;
  z-index: 0;
}
.circle-1 {
  width: 400px;
  height: 400px;
  background: #00e5ff; /* 青色光晕 */
  top: -100px;
  left: -100px;
  animation: move 10s infinite alternate;
}
.circle-2 {
  width: 300px;
  height: 300px;
  background: #7b2cbf; /* 紫色光晕 */
  bottom: -50px;
  right: -50px;
  animation: move 8s infinite alternate-reverse;
}

@keyframes move {
  from { transform: translate(0, 0); }
  to { transform: translate(30px, 30px); }
}

.login-box {
  position: relative;
  z-index: 1;
  width: 420px;
  padding: 50px 40px;
  /* 玻璃拟态效果 */
  background: rgba(18, 30, 60, 0.7);
  backdrop-filter: blur(20px);
  border: 1px solid rgba(0, 229, 255, 0.2);
  border-radius: 16px;
  box-shadow: 0 0 40px rgba(0, 229, 255, 0.1);
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.logo-circle {
  width: 60px;
  height: 60px;
  margin: 0 auto 15px;
  background: rgba(0, 229, 255, 0.1);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  border: 1px solid rgba(0, 229, 255, 0.3);
  box-shadow: 0 0 15px rgba(0, 229, 255, 0.3);
}

.title {
  color: #fff;
  font-size: 26px;
  font-weight: 600;
  margin: 0;
  text-shadow: 0 0 10px rgba(0, 229, 255, 0.5);
}

.subtitle {
  color: #8fa0c0;
  font-size: 12px;
  margin-top: 8px;
  letter-spacing: 2px;
  text-transform: uppercase;
}

/* 覆盖 Element Plus 输入框样式以适配深色 */
:deep(.dark-input .el-input__wrapper) {
  background-color: rgba(0, 0, 0, 0.3);
  box-shadow: 0 0 0 1px #2c3e50 inset;
}
:deep(.dark-input .el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #00e5ff inset;
}
:deep(.dark-input .el-input__inner) {
  color: #fff;
}

.login-btn {
  width: 100%;
  height: 45px;
  background: linear-gradient(90deg, #00e5ff, #0099ff);
  border: none;
  font-weight: bold;
  letter-spacing: 1px;
  box-shadow: 0 4px 15px rgba(0, 229, 255, 0.4);
}
.login-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

.footer-tips {
  margin-top: 30px;
  text-align: center;
  color: #55688a;
  font-size: 12px;
}
.divider {
  margin: 0 10px;
  color: #334;
}
</style>
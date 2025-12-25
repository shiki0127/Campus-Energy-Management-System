<template>
  <div class="layout-container">
    <!-- 左侧侧边栏 -->
    <el-aside width="220px" class="sidebar">
      <div class="logo">
        <el-icon color="#00e5ff" size="24"><Platform /></el-icon>
        <span class="logo-text">智慧能源云</span>
      </div>

      <el-menu
          active-text-color="#00e5ff"
          background-color="#0b1a38"
          text-color="#a0b0c0"
          :default-active="activeRoute"
          class="el-menu-vertical"
          router
      >
        <el-menu-item index="/dashboard">
          <el-icon><Odometer /></el-icon>
          <span>监控大屏</span>
        </el-menu-item>

        <!-- 暂时都指向 Dashboard，后续再开发具体页面 -->
        <el-menu-item index="/monitor">
          <el-icon><TrendCharts /></el-icon>
          <span>实时能耗</span>
        </el-menu-item>

        <el-menu-item index="/devices">
          <el-icon><Cpu /></el-icon>
          <span>设备管理</span>
        </el-menu-item>

        <el-menu-item index="/alarms">
          <el-icon><WarnTriangleFilled /></el-icon>
          <span>告警记录</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容区 -->
    <el-container>
      <!-- 顶栏 -->
      <el-header class="header">
        <div class="breadcrumb">
          <span class="current-path">当前位置 / {{ routeName }}</span>
        </div>
        <div class="user-info">
          <el-avatar :size="32" icon="UserFilled" class="user-avatar" />
          <span class="username">Admin</span>
          <el-icon class="logout-icon" @click="logout"><SwitchButton /></el-icon>
        </div>
      </el-header>

      <!-- 主要内容 (Router View) -->
      <!-- 这里是显示 Dashboard 的地方 -->
      <el-main class="main-content">
        <router-view v-slot="{ Component }">
          <transition name="fade" mode="out-in">
            <component :is="Component" />
          </transition>
        </router-view>
      </el-main>
    </el-container>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { Platform, Odometer, TrendCharts, Cpu, WarnTriangleFilled, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 当前激活的菜单项
const activeRoute = computed(() => route.path)

// 获取当前路由名称
const routeName = computed(() => {
  const nameMap: Record<string, string> = {
    '/dashboard': '监控大屏',
    '/monitor': '实时能耗',
    '/devices': '设备管理',
    '/alarms': '告警记录'
  }
  return nameMap[route.path] || '首页'
})

const logout = () => {
  ElMessageBox.confirm('确定要退出登录吗?', '系统提示', {
    confirmButtonText: '退出',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    localStorage.removeItem('token')
    router.push('/login')
    ElMessage.success('已安全退出')
  })
}
</script>

<style scoped>
.layout-container {
  height: 100vh;
  display: flex;
  background-color: #060c18; /* 整体深黑背景 */
}

/* 侧边栏样式 */
.sidebar {
  background-color: #0b1a38;
  border-right: 1px solid rgba(0, 229, 255, 0.1);
  display: flex;
  flex-direction: column;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  background: rgba(0, 0, 0, 0.2);
}

.logo-text {
  margin-left: 10px;
  letter-spacing: 1px;
}

.el-menu-vertical {
  border-right: none;
  background-color: transparent !important;
}

/* 菜单项样式 */
:deep(.el-menu-item) {
  margin: 4px 0;
}
:deep(.el-menu-item.is-active) {
  background-color: rgba(0, 229, 255, 0.15) !important;
  border-right: 3px solid #00e5ff;
  color: #fff;
}
:deep(.el-menu-item:hover) {
  background-color: rgba(255, 255, 255, 0.05) !important;
}

/* 顶栏样式 */
.header {
  height: 60px;
  background-color: #0b1a38;
  border-bottom: 1px solid rgba(0, 229, 255, 0.1);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  color: #fff;
}

.current-path {
  font-size: 14px;
  color: #00e5ff;
  opacity: 0.8;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.username {
  font-size: 14px;
  color: #ddd;
}

.logout-icon {
  cursor: pointer;
  color: #666;
  font-size: 18px;
  transition: color 0.3s;
}
.logout-icon:hover {
  color: #ff4d4f;
}

/* 内容区 */
.main-content {
  background-color: #060c18; /* 内容区纯黑底 */
  padding: 20px;
  overflow-y: auto;
}

/* 路由切换动画 */
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
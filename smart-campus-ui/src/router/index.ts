import { createRouter, createWebHistory } from 'vue-router'
// 引入页面组件
import Login from '../views/Login.vue'
import MainLayout from '../layout/MainLayout.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 1. 登录页 (独立路由，不带侧边栏)
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    // 2. 主布局路由 (包含侧边栏和顶栏)
    {
      path: '/',
      component: MainLayout,
      redirect: '/dashboard', // 访问根路径时自动跳到大屏
      children: [
        {
          path: 'dashboard',
          name: 'dashboard',
          component: () => import('../views/Dashboard.vue')
        },
        {
          path: 'monitor',
          name: 'monitor',
          // 指向真实的 Monitor.vue
          component: () => import('../views/Monitor.vue')
        },
        {
          path: 'devices',
          name: 'devices',
          // 指向真实的 DeviceList.vue
          component: () => import('../views/DeviceList.vue')
        },
        {
          path: 'alarms',
          name: 'alarms',
          // 指向真实的 AlarmList.vue
          component: () => import('../views/AlarmList.vue')
        }
      ]
    },
    // 3. 404 路由 (防止乱输地址白屏)
    {
      path: '/:pathMatch(.*)*',
      redirect: '/dashboard'
    }
  ]
})

// 路由守卫：检查 Token，防止未登录直接访问内部页面
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  if (to.name !== 'login' && !token) {
    next({ name: 'login' })
  } else {
    next()
  }
})

export default router
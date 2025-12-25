import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    port: 5173,
    open: true, // 启动时自动打开浏览器
    proxy: {
      '/api': {
        // 关键修改：把 'localhost' 改成 '127.0.0.1'
        // 这能强制使用 IPv4，解决 Node.js 高版本的解析 Bug
        target: 'http://127.0.0.1:8080',
        changeOrigin: true,
        // rewrite: 去掉路径中的 /api 前缀，因为后端接口没有 /api
        rewrite: (path) => path.replace(/^\/api/, '')
      }
    }
  }
})
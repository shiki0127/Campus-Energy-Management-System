import axios from 'axios'
import type { AxiosInstance, AxiosResponse } from 'axios'
import { ElMessage } from 'element-plus'
import type { ApiResponse } from '@/types/api'

// 创建 axios 实例
const service: AxiosInstance = axios.create({
    baseURL: '/api', // 这里配合 vite.config.ts 的 proxy 转发到 localhost:8080
    timeout: 5000    // 请求超时时间
})

// --- 请求拦截器：发送请求前 ---
service.interceptors.request.use(
    (config) => {
        // 从本地存储获取 Token
        const token = localStorage.getItem('token')
        if (token && config.headers) {
            // 放入请求头 Authorization
            config.headers['Authorization'] = token
        }
        return config
    },
    (error) => {
        return Promise.reject(error)
    }
)

// --- 响应拦截器：收到数据后 ---
service.interceptors.response.use(
    (response: AxiosResponse<ApiResponse>) => {
        const res = response.data

        // 根据后端约定，code '200' 代表成功
        if (res.code === '200') {
            return res.data // 脱壳，只把 Result.data 返回给页面
        } else {
            // 业务错误 (如密码错误)
            ElMessage.error(res.msg || '系统接口异常')
            return Promise.reject(new Error(res.msg || 'Error'))
        }
    },
    (error) => {
        // 网络错误 (如后端没启动)
        ElMessage.error(error.message || '网络连接失败')
        return Promise.reject(error)
    }
)

export default service
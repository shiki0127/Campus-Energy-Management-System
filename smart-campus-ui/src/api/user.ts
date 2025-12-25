import request from '@/utils/request'
import type { LoginResult } from '@/types/api'

// 登录接口
// data 参数包含 username 和 password
export function login(data: any) {
    return request<any, LoginResult>({
        url: '/user/login',
        method: 'post',
        data
    })
}
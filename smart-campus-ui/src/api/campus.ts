import request from '@/utils/request'

// 获取所有设备列表
export function getDevices() {
    return request<any, any[]>({
        url: '/campus/devices',
        method: 'get'
    })
}

// 获取所有告警记录
export function getAlarms() {
    return request<any, any[]>({
        url: '/campus/alarms',
        method: 'get'
    })
}
import request from '@/utils/request'

// 1. 获取指定设备的图表数据 (折线图用)
export function getChartData(deviceId: number) {
  return request<any, { categories: string[], series: number[] }>({
    url: '/energy/chart',
    method: 'get',
    params: { deviceId }
  })
}

// 2. 获取能耗排名 (Top 5 排行榜用)
export function getRanking() {
  return request<any, any[]>({
    url: '/energy/ranking',
    method: 'get'
  })
}

// 3. 获取大屏 KPI (新增)
export function getDashboardKPI() {
  return request<any, { totalPower: number, todayEnergy: number }>({
    url: '/energy/kpi',
    method: 'get'
  })
}
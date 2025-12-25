<template>
  <div class="dashboard">
    <!-- 顶部 KPI 数据卡片 -->
    <div class="kpi-row">
      <!-- 卡片1: 实时总功率 -->
      <div class="kpi-card blue">
        <div class="icon-wrapper">
          <el-icon><Lightning /></el-icon>
        </div>
        <div class="content">
          <div class="label">实时总功率</div>
          <div class="number">
            {{ kpiData.totalPower }} <span class="unit">kW</span>
          </div>
        </div>
      </div>

      <!-- 卡片2: 在线设备 -->
      <div class="kpi-card green">
        <div class="icon-wrapper">
          <el-icon><Cpu /></el-icon>
        </div>
        <div class="content">
          <div class="label">在线设备</div>
          <div class="number">
            {{ kpiData.deviceCount }} <span class="unit">台</span>
          </div>
        </div>
      </div>

      <!-- 卡片3: 今日能耗 -->
      <div class="kpi-card orange">
        <div class="icon-wrapper">
          <el-icon><Timer /></el-icon>
        </div>
        <div class="content">
          <div class="label">今日能耗</div>
          <div class="number">
            {{ kpiData.todayEnergy }} <span class="unit">kWh</span>
          </div>
        </div>
      </div>

      <!-- 卡片4: 历史告警 (显示真实总数) -->
      <div class="kpi-card red">
        <div class="icon-wrapper">
          <el-icon><WarnTriangleFilled /></el-icon>
        </div>
        <div class="content">
          <div class="label">历史告警</div>
          <div class="number">
            {{ kpiData.alarmCount }} <span class="unit">次</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 中间图表区域 -->
    <div class="charts-section">
      <!-- 左侧: 实时功率趋势图 (ECharts) -->
      <div class="chart-box main-chart">
        <div class="chart-header">
          <span class="title">实时功率趋势 - {{ currentDeviceName }}</span>
          <el-tag size="small" effect="dark" type="success" v-if="!loading">实时监控中</el-tag>
          <el-tag size="small" effect="dark" type="info" v-else>连接中...</el-tag>
        </div>
        <!-- ECharts 挂载点 -->
        <div id="trendChart" class="chart-body" ref="chartRef"></div>
      </div>

      <!-- 右侧: 能耗排名 (真实数据列表) -->
      <div class="chart-box sub-chart">
        <div class="chart-header">
          <span class="title">平均功率排名 (Top 5)</span>
        </div>
        <div class="chart-body rank-list">
          <!-- 渲染真实排名数据 -->
          <div class="rank-item" v-for="(item, index) in rankingList" :key="index">
            <span class="rank-num" :class="'top-'+(index+1)">{{ index + 1 }}</span>
            <span class="rank-name">{{ item.name }}</span>
            <span class="rank-val">{{ item.value }} W</span>
          </div>

          <!-- 空状态 -->
          <div v-if="rankingList.length === 0" class="empty-tip">
            暂无数据
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive, ref, onUnmounted } from 'vue'
import { Lightning, Cpu, Timer, WarnTriangleFilled } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { ElMessage } from 'element-plus'
// 引入 API
import { getDevices } from '@/api/campus'
// 注意：这里引入了 getDashboardKPI
import { getChartData, getRanking, getDashboardKPI } from '@/api/energy'

// --- ECharts 配置 ---
const getOption = (xData: string[], yData: number[]) => {
  return {
    backgroundColor: 'transparent',
    tooltip: { trigger: 'axis' },
    grid: { top: '15%', left: '3%', right: '4%', bottom: '3%', containLabel: true },
    xAxis: {
      type: 'category', boundaryGap: false, data: xData,
      axisLine: { lineStyle: { color: 'rgba(255,255,255,0.3)' } },
      axisLabel: { color: '#8fa0c0' }
    },
    yAxis: {
      type: 'value', name: '功率(W)',
      splitLine: { lineStyle: { color: 'rgba(255,255,255,0.05)' } },
      axisLine: { show: false },
      axisLabel: { color: '#8fa0c0' },
      nameTextStyle: { color: '#8fa0c0' }
    },
    series: [{
      name: '实时功率', type: 'line', smooth: true, symbol: 'none',
      lineStyle: { width: 3, shadowColor: 'rgba(0, 229, 255, 0.5)', shadowBlur: 10, color: '#00e5ff' },
      areaStyle: {
        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
          { offset: 0, color: 'rgba(0, 229, 255, 0.4)' },
          { offset: 1, color: 'rgba(0, 229, 255, 0)' }
        ])
      },
      data: yData
    }]
  }
}

// --- 响应式数据 ---
const kpiData = reactive({
  totalPower: '0.0',
  deviceCount: 0,
  todayEnergy: '0.0',
  alarmCount: 0
})

const rankingList = ref<any[]>([])
const currentDeviceName = ref('加载中...')
const loading = ref(false)
const chartRef = ref<HTMLElement>()
let myChart: echarts.ECharts | null = null
let timer: number | null = null
let targetDeviceId: number | null = null

// --- 生命周期 ---
onMounted(async () => {
  await initDashboard()
  // 启动定时刷新 (5秒)
  timer = setInterval(refreshRealtimeData, 5000)
  window.addEventListener('resize', handleResize)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
  if (myChart) myChart.dispose()
  window.removeEventListener('resize', handleResize)
})

const handleResize = () => myChart?.resize()

// --- 初始化 ---
const initDashboard = async () => {
  loading.value = true
  try {
    // 1. 获取设备信息
    const devices = await getDevices()
    kpiData.deviceCount = devices.length

    // 2. 获取真实 KPI (包含告警总数)
    const kpiRes = await getDashboardKPI()
    kpiData.totalPower = kpiRes.totalPower.toString()
    kpiData.todayEnergy = kpiRes.todayEnergy.toString()
    kpiData.alarmCount = kpiRes.alarmCount

    // 3. 初始化图表
    if (chartRef.value) {
      myChart = echarts.init(chartRef.value)
      myChart.setOption(getOption([], []))
    }

    // 4. 默认选中第一个设备
    if (devices.length > 0) {
      targetDeviceId = devices[0].id
      currentDeviceName.value = devices[0].name
      await refreshRealtimeData()
    } else {
      currentDeviceName.value = '暂无在线设备'
    }

  } catch (error) {
    console.error(error)
    ElMessage.error('无法连接到服务器')
  } finally {
    loading.value = false
  }
}

// --- 刷新数据 ---
const refreshRealtimeData = async () => {
  // 刷新排名
  try {
    const ranks = await getRanking()
    if (ranks) rankingList.value = ranks
  } catch (e) {}

  // 刷新图表
  if (targetDeviceId && myChart) {
    try {
      const res = await getChartData(targetDeviceId)
      if (res && res.categories) {
        myChart.setOption({
          xAxis: { data: res.categories },
          series: [{ data: res.series }]
        })
      }
    } catch (e) {}
  }

  // 刷新 KPI (确保告警数实时跳动)
  try {
    const kpiRes = await getDashboardKPI()
    kpiData.totalPower = kpiRes.totalPower.toString()
    kpiData.todayEnergy = kpiRes.todayEnergy.toString()
    kpiData.alarmCount = kpiRes.alarmCount
  } catch(e) {}
}
</script>

<style scoped>
.dashboard { color: #fff; }
.kpi-row { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 20px; }
.kpi-card { background: rgba(18, 30, 60, 0.7); backdrop-filter: blur(10px); border: 1px solid rgba(255, 255, 255, 0.05); border-radius: 8px; padding: 24px 20px; display: flex; align-items: center; position: relative; overflow: hidden; transition: transform 0.3s; }
.kpi-card:hover { transform: translateY(-5px); box-shadow: 0 8px 20px rgba(0,0,0,0.3); }
.kpi-card::before { content: ''; position: absolute; top: 0; left: 0; width: 100%; height: 3px; }
.blue::before { background: #00e5ff; box-shadow: 0 0 10px #00e5ff; }
.green::before { background: #42b983; box-shadow: 0 0 10px #42b983; }
.orange::before { background: #f29f3f; box-shadow: 0 0 10px #f29f3f; }
.red::before { background: #ff4d4f; box-shadow: 0 0 10px #ff4d4f; }
.icon-wrapper { width: 56px; height: 56px; border-radius: 12px; display: flex; justify-content: center; align-items: center; font-size: 28px; margin-right: 20px; background: rgba(255,255,255,0.05); }
.blue .icon-wrapper { color: #00e5ff; background: rgba(0, 229, 255, 0.1); }
.green .icon-wrapper { color: #42b983; background: rgba(66, 185, 131, 0.1); }
.orange .icon-wrapper { color: #f29f3f; background: rgba(242, 159, 63, 0.1); }
.red .icon-wrapper { color: #ff4d4f; background: rgba(255, 77, 79, 0.1); }
.content { flex: 1; }
.label { font-size: 13px; color: #8fa0c0; margin-bottom: 8px; }
.number { font-size: 28px; font-weight: bold; font-family: 'DIN Alternate', sans-serif; }
.unit { font-size: 14px; font-weight: normal; color: #666; margin-left: 4px; }
.charts-section { display: grid; grid-template-columns: 2fr 1fr; gap: 20px; height: 500px; }
.chart-box { background: rgba(18, 30, 60, 0.7); backdrop-filter: blur(10px); border: 1px solid rgba(0, 229, 255, 0.1); border-radius: 8px; padding: 20px; display: flex; flex-direction: column; }
.chart-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; padding-left: 10px; border-left: 4px solid #00e5ff; }
.title { font-size: 16px; font-weight: bold; color: #fff; }
.chart-body { flex: 1; min-height: 0; position: relative; }
.rank-list { padding: 10px; overflow-y: auto; }
.rank-item { display: flex; align-items: center; padding: 12px 0; border-bottom: 1px solid rgba(255,255,255,0.05); }
.rank-num { width: 24px; height: 24px; text-align: center; line-height: 24px; border-radius: 4px; background: rgba(255,255,255,0.1); margin-right: 15px; font-size: 12px; font-weight: bold; color: #fff; }
.top-1 { background: #ff4d4f; }
.top-2 { background: #f29f3f; }
.top-3 { background: #f2c94c; }
.rank-name { flex: 1; color: #d9d9d9; font-size: 14px; }
.rank-val { color: #00e5ff; font-weight: bold; font-family: 'DIN Alternate'; }
.empty-tip { position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%); color: #556; }
</style>
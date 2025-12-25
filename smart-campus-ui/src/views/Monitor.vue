<template>
  <div class="monitor-container">
    <!-- 左侧设备列表 -->
    <div class="device-list-panel">
      <div class="panel-header">设备列表</div>
      <el-input v-model="searchText" placeholder="搜索设备..." prefix-icon="Search" class="search-input" />
      <div class="list-content">
        <div
            v-for="item in filteredDevices"
            :key="item.id"
            class="device-item"
            :class="{ active: currentDeviceId === item.id }"
            @click="selectDevice(item)"
        >
          <div class="d-name">{{ item.name }}</div>
          <div class="d-sn">{{ item.sn }}</div>
          <el-tag size="small" type="success" class="d-status">在线</el-tag>
        </div>
      </div>
    </div>

    <!-- 右侧图表详情 -->
    <div class="chart-panel">
      <div class="panel-header" v-if="currentDevice">
        <span>正在监控：{{ currentDevice.name }}</span>
        <el-tag type="warning" effect="plain">额定功率: {{ currentDevice.pMax }}W</el-tag>
      </div>

      <div id="monitorChart" class="chart-box"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref, computed, onUnmounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import { getDevices } from '@/api/campus'
import { getChartData } from '@/api/energy'

const devices = ref<any[]>([])
const searchText = ref('')
const currentDeviceId = ref<number | null>(null)
const currentDevice = ref<any>(null)
let myChart: echarts.ECharts | null = null
let timer: number | null = null

// 过滤搜索
const filteredDevices = computed(() => {
  return devices.value.filter(d => d.name.includes(searchText.value))
})

onMounted(async () => {
  // 1. 加载设备列表
  devices.value = await getDevices()

  // 2. 默认选中第一个
  if (devices.value.length > 0) {
    selectDevice(devices.value[0])
  }

  // 3. 启动定时刷新
  timer = setInterval(refreshChart, 5000)
  window.addEventListener('resize', () => myChart?.resize())
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
  if (myChart) myChart.dispose()
})

const selectDevice = (device: any) => {
  currentDeviceId.value = device.id
  currentDevice.value = device
  refreshChart()
}

const refreshChart = async () => {
  if (!currentDeviceId.value) return

  // 初始化图表（如果还没初始化）
  const dom = document.getElementById('monitorChart')
  if (dom && !myChart) {
    myChart = echarts.init(dom)
  }

  // 获取数据
  try {
    const res = await getChartData(currentDeviceId.value)

    const option = {
      title: { text: '实时功率曲线', left: 'center' },
      tooltip: { trigger: 'axis' },
      xAxis: { type: 'category', data: res.categories },
      yAxis: { type: 'value', name: '功率(W)' },
      series: [{
        data: res.series,
        type: 'line',
        smooth: true,
        areaStyle: { color: 'rgba(64, 158, 255, 0.2)' },
        lineStyle: { color: '#409eff' }
      }]
    }
    myChart?.setOption(option)
  } catch (e) {
    console.error(e)
  }
}
</script>

<style scoped>
.monitor-container {
  display: flex;
  height: 85vh;
  gap: 20px;
}

.device-list-panel {
  width: 280px;
  background: #fff;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.chart-panel {
  flex: 1;
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  flex-direction: column;
}

.panel-header {
  padding: 15px;
  border-bottom: 1px solid #eee;
  font-weight: bold;
  font-size: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.search-input {
  margin: 10px;
  width: auto;
}

.list-content {
  flex: 1;
  overflow-y: auto;
}

.device-item {
  padding: 15px;
  border-bottom: 1px solid #f0f0f0;
  cursor: pointer;
  transition: background 0.2s;
  position: relative;
}

.device-item:hover { background: #f5f7fa; }
.device-item.active { background: #ecf5ff; border-left: 3px solid #409eff; }

.d-name { font-weight: bold; font-size: 14px; }
.d-sn { font-size: 12px; color: #999; margin-top: 4px; }
.d-status { position: absolute; right: 15px; top: 15px; }

.chart-box {
  flex: 1;
  width: 100%;
}
</style>
<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="title">系统告警日志</h2>
      <el-tag type="danger">实时监控中</el-tag>
    </div>

    <el-table :data="tableData" style="width: 100%" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />

      <!-- 新增：设备名称列 -->
      <el-table-column label="报警设备" min-width="180">
        <template #default="scope">
          <span style="font-weight: bold">{{ scope.row.deviceName }}</span>
          <br/>
          <span style="font-size: 12px; color: #999">{{ scope.row.roomNo }}</span>
        </template>
      </el-table-column>

      <el-table-column label="告警类型" width="120">
        <template #default="scope">
          <el-tag v-if="scope.row.type === 'OVERLOAD'" type="danger" effect="dark">
            <el-icon><Lightning /></el-icon> 过载
          </el-tag>
          <el-tag v-else-if="scope.row.type === 'VOLTAGE_ERR'" type="warning" effect="dark">
            <el-icon><Warning /></el-icon> 电压
          </el-tag>
        </template>
      </el-table-column>

      <el-table-column label="异常值" width="120">
        <template #default="scope">
          <span class="value-text">{{ scope.row.value }}</span>
          <span class="unit-text" v-if="scope.row.type === 'OVERLOAD'"> W</span>
          <span class="unit-text" v-else> V</span>
        </template>
      </el-table-column>

      <el-table-column prop="detail" label="详情" min-width="200" show-overflow-tooltip />

      <el-table-column prop="createTime" label="时间" width="180">
        <template #default="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getAlarms } from '@/api/campus'
import { Lightning, Warning } from '@element-plus/icons-vue'

const tableData = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getAlarms()
    tableData.value = res // 后端现在返回的是 Map，包含 deviceName
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
})

const formatTime = (timeStr: string) => {
  if (!timeStr) return '-'
  return timeStr.replace('T', ' ').split('.')[0]
}
</script>

<style scoped>
.page-container { padding: 20px; background: #fff; border-radius: 8px; min-height: 85vh; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.title { margin: 0; font-size: 20px; color: #303133; }
.value-text { font-weight: bold; color: #f56c6c; font-size: 16px; }
.unit-text { color: #909399; font-size: 12px; }
</style>
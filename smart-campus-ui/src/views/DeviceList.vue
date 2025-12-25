<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="title">设备资产管理</h2>
      <el-button type="primary" plain>+ 新增设备</el-button>
    </div>

    <!-- 设备表格 -->
    <el-table :data="tableData" style="width: 100%" class="custom-table" v-loading="loading">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="设备名称" min-width="150" />
      <el-table-column prop="sn" label="设备序列号 (SN)" min-width="180" />
      <el-table-column prop="roomNo" label="所在房间" width="120" />

      <el-table-column label="额定功率" width="120">
        <template #default="scope">
          <el-tag type="warning" effect="plain">{{ scope.row.pMax }} W</el-tag>
        </template>
      </el-table-column>

      <el-table-column label="状态" width="100">
        <template #default="scope">
          <el-tag v-if="scope.row.status === 'ONLINE'" type="success" effect="dark">在线</el-tag>
          <el-tag v-else type="info">离线</el-tag>
        </template>
      </el-table-column>

      <el-table-column prop="createTime" label="入网时间" width="180">
        <template #default="scope">
          {{ formatTime(scope.row.createTime) }}
        </template>
      </el-table-column>

      <el-table-column label="操作" width="150" fixed="right">
        <template #default>
          <el-button link type="primary" size="small">编辑</el-button>
          <el-button link type="danger" size="small">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script setup lang="ts">
import { onMounted, ref } from 'vue'
import { getDevices } from '@/api/campus'
import { ElMessage } from 'element-plus'

const tableData = ref([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const res = await getDevices()
    tableData.value = res
  } catch (error) {
    ElMessage.error('获取设备列表失败')
  } finally {
    loading.value = false
  }
})

// 简单的时间格式化
const formatTime = (timeStr: string) => {
  if (!timeStr) return '-'
  return timeStr.replace('T', ' ').split('.')[0]
}
</script>

<style scoped>
.page-container {
  padding: 20px;
  background: #fff;
  border-radius: 8px;
  min-height: 85vh;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #eee;
}

.title {
  margin: 0;
  font-size: 20px;
  color: #303133;
}

/* 如果需要在深色布局下适配表格，可以加这些 */
/* :deep(.el-table) { background-color: transparent; } */
</style>
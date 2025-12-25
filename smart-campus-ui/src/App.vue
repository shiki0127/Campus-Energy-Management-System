<template>
  <!-- 根路由出口：根据 URL 显示 Login 或 MainLayout -->
  <router-view />
</template>

<style>
/* 全局重置样式 */
html, body {
  margin: 0;
  padding: 0;
  height: 100%;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
  background-color: #f0f2f5;
}
#app {
  height: 100%;
}
</style>
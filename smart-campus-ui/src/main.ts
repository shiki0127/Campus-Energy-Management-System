import { createApp } from 'vue'
import App from './App.vue'
import router from './router'

// 1. 引入 Element Plus 样式 (必须)
import 'element-plus/dist/index.css'
// 2. 引入 Element Plus 组件库
import ElementPlus from 'element-plus'
// 3. 引入图标
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// 4. 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}

app.use(ElementPlus)
app.use(router)

app.mount('#app')
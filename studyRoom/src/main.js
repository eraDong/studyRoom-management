import './assets/main.css'

import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'

//引入ElementPlus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

// 持久化

import piniaPluginPersistedstate from 'pinia-plugin-persistedstate' 
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

const app = createApp(App)

app.use(pinia)

app.use(router)

app.use(ElementPlus).mount('#app')

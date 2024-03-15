import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import axios from "axios";

import 'flag-icon-css/css/flag-icons.min.css'
import 'element-plus/theme-chalk/dark/css-vars.css'
import '@/assets/css/element.less'
//配置服务端默认地址
axios.defaults.baseURL = 'http://localhost:8080'

const app = createApp(App)

app.use(router)

app.mount('#app')

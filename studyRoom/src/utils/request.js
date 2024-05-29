import axios from 'axios'
import { ElMessage } from 'element-plus'
import 'element-plus/theme-chalk/el-message.css'
const baseURL = 'http://127.0.0.1:8080'

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000
})

instance.interceptors.request.use(
  (config) => {

    return config
  },
  (err) => {
    return Promise.reject(err)
  }
)

instance.interceptors.response.use(
  (res) => {
    return res
  },
  (err) => {
    ElMessage.error(err.message || '请求失败，请稍后重试')
    return Promise.reject(err)
  }
)

export default instance
export { baseURL }

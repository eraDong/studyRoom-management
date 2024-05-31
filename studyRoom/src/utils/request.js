/*
 * @Author: eraDong 115410761+eraDong@users.noreply.github.com
 * @Date: 2024-05-31 08:41:00
 * @LastEditors: eraDong 115410761+eraDong@users.noreply.github.com
 * @LastEditTime: 2024-05-31 10:01:35
 * @FilePath: \studyRoom\src\utils\request.js
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import axios from 'axios';
import { useUserStore } from '@/stores/user';
import { ElMessage } from 'element-plus';
import 'element-plus/theme-chalk/el-message.css';

const baseURL = 'http://127.0.0.1:8080';

const instance = axios.create({
  // TODO 1. 基础地址，超时时间
  baseURL,
  timeout: 10000,
});

instance.interceptors.request.use(
  (config) => {
    const userStore = useUserStore();
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`;
    }
    return config;
  },
  (err) => {
    ElMessage.error('请求发送失败，请稍后重试');
    return Promise.reject(err);
  }
);

instance.interceptors.response.use(
  (res) => {
    return res;
  },
  (err) => {
    let message = '请求失败，请稍后重试';
    if (err.response) {
      switch (err.response.status) {
        case 400:
          message = '请求错误，请检查输入参数';
          break;
        case 401:
          message = '未授权，请重新登录';
          break;
        case 403:
          message = '拒绝访问，您没有权限';
          break;
        case 404:
          message = '请求的资源未找到';
          break;
        case 500:
          message = '服务器内部错误，请稍后重试';
          break;
        default:
          message = `请求失败，状态码：${err.response.status}`;
      }
    } else if (err.request) {
      message = '请求未发送，请检查网络连接';
    }
    ElMessage.error(message);
    return Promise.reject(err);
  }
);

export default instance;
export { baseURL };

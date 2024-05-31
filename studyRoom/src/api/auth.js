// utils/auth.js
import request from '@/utils/request';

// 登录请求
export const login = (data) =>
{
    const formData = new FormData();
    for (const key in data) {
      formData.append(key, data[key]);
    }
    return request.post('/api/auth/login', formData)
}

// 注册请求
export const register = (data) =>
{
    
    const formData = new FormData();
    for (const key in data) {
      formData.append(key, data[key]);
    }
    return request.post('/api/auth/register', formData)
}
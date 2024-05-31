import { defineStore } from 'pinia';
import { login, register } from '@/api/auth';
import axios from 'axios';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    token: null,
    isAdmin: false,
    studentId: null,
  }),
  actions: {
    async login(username, password) {
      try {
        const response = await login({ username, password });
        this.user = response.data.username;
        this.token = response.data.token;
        this.isAdmin = response.data.isAdmin;
        this.studentId = response.data.studentId;
        axios.defaults.headers.common['Authorization'] = `Bearer ${this.token}`;
        return { success: true };
      } catch (err) {
        return { success: false, message: err.response?.data?.error || '登录失败，请重试。' };
      }
    },
    async register(name, username, password, isAdmin) {
      try {
        const response = await register({ name, username, password, isAdmin });
        return { success: true, message: response.data.message };
      } catch (err) {
        return { success: false, message: err.response?.data?.error || '注册失败，请重试。' };
      }
    },
    logout() {
      this.user = null;
      this.token = null;
      this.isAdmin = false;
      this.studentId = null;
      delete axios.defaults.headers.common['Authorization'];
    },
  },
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'user',
        storage: localStorage,
      },
    ],
  },
});

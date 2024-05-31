<script setup>
import { computed } from 'vue';
import { useUserStore } from '@/stores/user';
import { useRouter } from 'vue-router';

const userStore = useUserStore();
const router = useRouter();

// 计算属性，用于获取用户名和身份信息
const username = computed(() => userStore.user);
const isAdmin = computed(() => userStore.isAdmin);

// 登出函数
const logout = () => {
  userStore.logout();
  router.push({ name: 'login' });
};
</script>

<template>
    <div class="nav-head">
        <div class="jum-btn">
            <router-link to="/index">首页</router-link>
            <router-link to="/studyroom">自习室</router-link>
            <router-link to="/seat">座位预定</router-link>
            <router-link v-if="isAdmin" to="/manage">后台</router-link>
        </div>
        
        <div class="welcome">
            <span v-if="username">{{ username }}（{{ isAdmin ? '管理员' : '学生' }}）
            </span>
            <router-link v-else to="/login">未登录</router-link>
            欢迎使用自习室预订系统！
            <a @click="logout" v-if="userStore.token">退出</a>
        </div>
    </div>
    <div class="main-content">
        <router-view></router-view>
    </div>
</template>

<style lang="less" scoped>
body {
    background-color: #e6f9ed; /* 整个页面的背景颜色 */
    margin: 0;
    padding: 0;
    font-family: Arial, sans-serif;
}

.nav-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #d4f5dd; /* 导航栏的背景颜色 */
    padding: 10px 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

    .jum-btn {
        a {
            padding: 10px 15px;
            margin: 0 10px;
            font-weight: bold;
            color: #28a745;
            border: 2px solid #28a745;
            border-radius: 5px;
            transition: all 0.3s ease;

            &:hover {
                background-color: #28a745;
                color: white;
            }
        }
    }

    .welcome {
        font-weight: 700;
        font-size: 18px;
        color: #333;

        a {
            margin-left: 10px;
            cursor: pointer;
            color: #28a745;
            text-decoration: underline;
            transition: color 0.3s ease;

            &:hover {
                color: #60c78a;
            }
        }
    }

    margin-bottom: 50px;
}

.main-content {
    padding: 20px;
    background-color: #e6f9ed; /* 主内容区域的背景颜色 */
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}
</style>

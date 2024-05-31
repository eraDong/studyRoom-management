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
    <div>
        <router-view></router-view>
    </div>
</template>

<style lang="less">
.nav-head {
    display: flex;
    justify-content: space-between;

    a {
        padding: 10px;
        cursor: pointer;
    }

    .welcome {
        font-weight: 700;
        font-size: 18px;

        a {
            margin-left: 10px;
        }
    }

    margin-bottom: 50px;
}
</style>

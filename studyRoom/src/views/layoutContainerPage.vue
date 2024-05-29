<script setup></script>
<template>
    <div class="nav-head">
        <div class="jum-btn">
            <router-link to="index">首页</router-link>
            <router-link to="studyroom">自习室</router-link>
            <router-link to="seat">座位预定</router-link>
            <router-link to="manage">后台</router-link>
        </div>
        <div class="welcome">欢迎使用自习室预订系统！</div>
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
    }

    .welcome {
        font-weight: 700;
        font-size: 18px;
    }

    margin-bottom: 50px;
}

</style>
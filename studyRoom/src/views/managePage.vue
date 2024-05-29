<script setup></script>
<template>
    <div class="nav-head">
        <div class="jum-btn">
            <router-link to="managestudyroom">管理自习室</router-link>
            <router-link to="manageseat">管理座位</router-link>
            <router-link to="managestudent">管理学生</router-link>
            <router-link to="index">前台</router-link>
        </div>
        <div class="welcome">欢迎使用自习室管理系统！</div>
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
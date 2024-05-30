<!--
 * @Author: eraDong 115410761+eraDong@users.noreply.github.com
 * @Date: 2024-05-30 09:13:54
 * @LastEditors: eraDong 115410761+eraDong@users.noreply.github.com
 * @LastEditTime: 2024-05-30 10:27:32
 * @FilePath: \studyRoom\src\views\managePage.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<script setup></script>
<template>
    <div class="nav-head">
        <div class="jum-btn">
            <router-link to="managestudyroom">管理自习室</router-link>
            <router-link to="managestudent">管理学生</router-link>
            <router-link to="manageseat">管理座位</router-link>
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
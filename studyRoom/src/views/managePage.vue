<!--
 * @Author: eraDong 115410761+eraDong@users.noreply.github.com
 * @Date: 2024-05-31 08:41:00
 * @LastEditors: eraDong 115410761+eraDong@users.noreply.github.com
 * @LastEditTime: 2024-05-31 10:03:59
 * @FilePath: \studyRoom\src\views\managePage.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<script setup></script>

<template>
    <div class="admin-container">
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
    </div>
</template>

<style lang="less" scoped>
.admin-container {
    background-color: #f5f7fa;  /* 添加背景颜色 */
    padding: 20px;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.nav-head {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 50px;
    padding: 10px 20px;
    background-color: #ffffff;
    border-radius: 10px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);

    a {
        padding: 10px 15px;
        margin: 0 10px;
        font-weight: bold;
        color: #409eff;
        border: 2px solid #409eff;
        border-radius: 5px;
        transition: all 0.3s ease;

        &:hover {
            background-color: #409eff;
            color: white;
        }
    }

    .welcome {
        font-weight: 700;
        font-size: 18px;
        color: #333;
    }
}
</style>

<script setup>
import {ref} from 'vue'
import {getAllStudents,deleteStudentById} from '@/api/student'

let stuArr = ref([])

const renderStudent = async () => {
    stuArr.value = await getAllStudents()
}
renderStudent()

const formatIsAdmin = (row, column, cellValue) => {
    return cellValue ? "是" : "否"
}

const handleDelete = async (item) => {

    await deleteStudentById(item.studentId)
    renderStudent()
}

</script>
<template>
    <div class="main">
        <el-table :data="stuArr.data" border style="width: 100%; height: 660px;">
            <el-table-column prop="name" label="姓名"></el-table-column>
            <el-table-column prop="username" label="用户名"></el-table-column>
            <el-table-column prop="password" label="密码"></el-table-column>
            <el-table-column prop="admin" label="是否为管理员" :formatter="formatIsAdmin"></el-table-column>
            <el-table-column fixed="right" label="操作" width="130">
                <template #default="scope">
                    <div style="display: flex">
                        <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
                        <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>
    </div>


</template>
<style lang="less" scoped>
</style>
<!--
 * @Author: eraDong 115410761+eraDong@users.noreply.github.com
 * @Date: 2024-05-30 09:13:54
 * @LastEditors: eraDong 115410761+eraDong@users.noreply.github.com
 * @LastEditTime: 2024-05-30 09:32:26
 * @FilePath: \studyRoom\src\views\manageStudentPage.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllStudents, deleteStudentById, updateStudents, addStudent } from '@/api/student'

let stuArr = ref([])
let editDialogVisible = ref(false)
let addDialogVisible = ref(false)
let currentEditItem = reactive({
    name: '',
    username: '',
    password: '',
    admin: false,
    studentId: null // Ensure you have an ID field to identify the student
})
let newStudentItem = reactive({
    name: '',
    username: '',
    password: '',
    admin: false
})

const renderStudent = async () => {
    stuArr.value = await getAllStudents()
}

onMounted(() => {
    renderStudent()
})

const formatIsAdmin = (row, column, cellValue) => {
    return cellValue ? "是" : "否"
}

const handleEdit = (row) => {
    Object.assign(currentEditItem, row) // Clone the row data into currentEditItem
    editDialogVisible.value = true
}

const handleEditSave = async () => {
    const formData = new FormData()
    formData.append('name', currentEditItem.name || '')
    formData.append('username', currentEditItem.username || '')
    formData.append('password', currentEditItem.password || '')
    formData.append('isAdmin', currentEditItem.admin || false)
    formData.append('studentId', currentEditItem.studentId || 0)

    await updateStudents(currentEditItem.studentId, formData)
    editDialogVisible.value = false
    await renderStudent()
}

const handleDelete = async (item) => {
    await deleteStudentById(item.studentId)
    await renderStudent()
}

const handleAdd = () => {
    newStudentItem.name = ''
    newStudentItem.username = ''
    newStudentItem.password = ''
    newStudentItem.admin = false
    addDialogVisible.value = true
}

const handleAddSave = async () => {
    const formData = new FormData()
    formData.append('name', newStudentItem.name || '')
    formData.append('username', newStudentItem.username || '')
    formData.append('password', newStudentItem.password || '')
    formData.append('isAdmin', newStudentItem.admin || false)

    await addStudent(formData)
    addDialogVisible.value = false
    await renderStudent()
}
</script>
<template>
    <div class="main">
        <el-button class="add-btn" @click="handleAdd">添加学生</el-button>
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

    <!-- edit dialog -->
    <el-dialog class="editDialog" v-model="editDialogVisible" title="编辑学生信息" width="800">
            <div>
                <el-form :model="currentEditItem">
                    <el-form-item label="姓名">
                        <el-input v-model="currentEditItem.name"></el-input>
                    </el-form-item>
                    <el-form-item label="用户名">
                        <el-input v-model="currentEditItem.username"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="currentEditItem.password"></el-input>
                    </el-form-item>
                    <el-form-item label="是否为管理员">
                        <el-switch v-model="currentEditItem.admin"></el-switch>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleEditSave">保存</el-button>
            </div>
        </el-dialog>

     <!-- Add Dialog -->
     <el-dialog class="addDialog" v-model="addDialogVisible" title="添加学生信息" width="800">
            <div>
                <el-form :model="newStudentItem">
                    <el-form-item label="姓名">
                        <el-input v-model="newStudentItem.name"></el-input>
                    </el-form-item>
                    <el-form-item label="用户名">
                        <el-input v-model="newStudentItem.username"></el-input>
                    </el-form-item>
                    <el-form-item label="密码">
                        <el-input v-model="newStudentItem.password"></el-input>
                    </el-form-item>
                    <el-form-item label="是否为管理员">
                        <el-switch v-model="newStudentItem.admin"></el-switch>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddSave">保存</el-button>
            </div>
        </el-dialog>

</template>
<style lang="less" scoped>
.main {
    .add-btn {
        margin-bottom: 10px;
    }

    .el-table {
        border-radius: 15px;
    }

    .el-dialog {
        img {
            width: 100%;
            height: 250px;
        }
    }

}
</style>
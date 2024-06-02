<script setup>
import { ref, reactive, onMounted, computed, watch } from 'vue';
import { getAllStudents, deleteStudentById, updateStudents, addStudent } from '@/api/student';
import { ElMessage } from 'element-plus';

let stuArr = ref({ data: [] });
let editDialogVisible = ref(false);
let addDialogVisible = ref(false);
let searchQuery = ref('');
let currentPage = ref(1);
let pageSize = ref(10);
let total = ref(0);

let currentEditItem = reactive({
    name: '',
    username: '',
    password: '',
    admin: false,
    studentId: null, // Ensure you have an ID field to identify the student
});

let newStudentItem = reactive({
    name: '',
    username: '',
    password: '',
    admin: false,
});

const renderStudent = async () => {
    const response = await getAllStudents();
    stuArr.value = response;
    total.value = response.data.length; // Update the total count
};

onMounted(() => {
    renderStudent();
});

const formatIsAdmin = (row, column, cellValue) => {
    return cellValue ? '是' : '否';
};

const handleEdit = (row) => {
    Object.assign(currentEditItem, row); // Clone the row data into currentEditItem
    editDialogVisible.value = true;
};

const handleEditSave = async () => {
    const formData = new FormData();
    formData.append('name', currentEditItem.name || '');
    formData.append('username', currentEditItem.username || '');
    formData.append('password', currentEditItem.password || '');
    formData.append('isAdmin', currentEditItem.admin || false);
    formData.append('studentId', currentEditItem.studentId || 0);

    await updateStudents(currentEditItem.studentId, formData);
    editDialogVisible.value = false;
    await renderStudent();
};

const handleDelete = async (item) => {
    await deleteStudentById(item.studentId);
    await renderStudent();
};

const handleAdd = () => {
    newStudentItem.name = '';
    newStudentItem.username = '';
    newStudentItem.password = '';
    newStudentItem.admin = false;
    addDialogVisible.value = true;
};

const handleAddSave = async () => {
    if (!newStudentItem.name || !newStudentItem.username || !newStudentItem.password) {
        ElMessage.error('姓名、用户名和密码不能为空');
        return;
    }

    const formData = new FormData();
    formData.append('name', newStudentItem.name);
    formData.append('username', newStudentItem.username);
    formData.append('password', newStudentItem.password);
    formData.append('isAdmin', newStudentItem.admin || false);
    
    await addStudent(formData);
    addDialogVisible.value = false;
    await renderStudent();
};

// Watcher for search query
watch(searchQuery, async (newQuery, oldQuery) => {
    if (newQuery !== oldQuery) {
        currentPage.value = 1; // Reset to first page on new search
        await renderStudent();
    }
});

// Computed property for filtered and paginated students
const paginatedStudents = computed(() => {
    let filteredStudents = stuArr.value.data.filter(student =>
        student.name?.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
        student.username?.toLowerCase().includes(searchQuery.value.toLowerCase())
    );

    total.value = filteredStudents.length; // Update the total count for pagination

    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;

    return filteredStudents.slice(start, end);
});

const handlePageChange = (page) => {
    currentPage.value = page;
};
</script>


<template>
    <div class="main">
        <div class="header">
            <el-input 
                v-model="searchQuery"
                placeholder="搜索姓名或用户名"
                class="search-input"
            ></el-input>
            <el-button class="add-btn" @click="handleAdd">添加学生</el-button>
        </div>
        <el-table :data="paginatedStudents" border style="width: 100%; height: 445px;">
            <el-table-column prop="studentId" label="学生ID"></el-table-column>
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

        <el-pagination
            v-model:current-page="currentPage"
            :page-size="pageSize"
            :total="total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
            class="pagination"
        ></el-pagination>
    </div>

    <!-- Edit Dialog -->
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
    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 20px;

        .search-input {
            width: 300px;
            border-radius: 25px;
            border: 1px solid #dcdfe6;
            padding: 10px;
            transition: all 0.3s ease;
            
            &:focus {
                border-color: #409eff;
                box-shadow: 0 0 5px rgba(64, 158, 255, 0.5);
            }

            input {
                border: none;
                outline: none;
            }
        }

        .add-btn {
            background-color: #409eff;
            color: white;
            border-radius: 25px;
            padding: 10px 20px;
            transition: all 0.3s ease;

            &:hover {
                background-color: #66b1ff;
            }
        }
    }

    .el-table {
        border-radius: 15px;
    }

    .pagination {
        margin: 20px 0;
        display: flex;
        justify-content: center;
    }

    .el-dialog {
        img {
            width: 100%;
            height: 250px;
        }
    }
}
</style>
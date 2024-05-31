<script setup>
import { ref, reactive, onMounted, watch, computed } from 'vue';
import { getAllStudyRooms, updateStudyRoom } from '@/api/studyRoom';
import { useUserStore } from '@/stores/user';
import { getStudentById } from '@/api/student';
import { ElMessage } from 'element-plus';

let studyRooms = ref([]);
let reserveDialogVisible = ref(false);
let currentReserveItem = reactive({
    id: null,
    studentId: null,
    name: null,
    location: null,
    isBooked: null,
    introduction: null,
    image: null,
});
let searchQuery = ref('');

const currentPage = ref(1);
const pageSize = ref(10);
const total = ref(0);

const fetchStudentName = async (studentId) => {
    if (studentId) {
        const { data } = await getStudentById(studentId);
        return data.name;
    }
    return null;
};

const renderStudyRooms = async () => {
    const roomsData = await getAllStudyRooms();
    const roomsWithStudentNames = await Promise.all(
        roomsData.data.map(async room => {
            if (room.studentId) {
                room.studentName = await fetchStudentName(room.studentId);
            } else {
                room.studentName = '未预约';
            }
            return room;
        })
    );
    studyRooms.value = roomsWithStudentNames;
    total.value = studyRooms.value.length;
};

onMounted(() => {
    renderStudyRooms();
});

const formatIsBooked = (row, column, cellValue) => {
    return row.studentId !== 0 ? '是' : '否';
};

const cancelReservation = async (row) => {
    const formData = new FormData();
    formData.append('name', row.name);
    formData.append('location', row.location);
    formData.append('isBooked', false);
    formData.append('introduction', row.introduction);
    formData.append('image', row.image);
    formData.append('studentId', 0);

    await updateStudyRoom(row.id, formData);
    await renderStudyRooms();
};

const handleReserve = (row) => {
    currentReserveItem.id = row.id;
    currentReserveItem.studentId = '';
    currentReserveItem.name = row.name;
    currentReserveItem.location = row.location;
    currentReserveItem.introduction = row.introduction;
    currentReserveItem.image = row.image;
    reserveDialogVisible.value = true;
};

const handleReserveSave = async () => {
    // Check if studentId exists
    const studentExists = await getStudentById(currentReserveItem.studentId)
        .then(response => {
            return response.data ? true : false;
        })
        .catch(() => {
            return false;
        });

    // If the student exists, proceed to save the reservation
    if (studentExists) {
        const formData = new FormData();
        formData.append('name', currentReserveItem.name);
        formData.append('location', currentReserveItem.location);
        formData.append('isBooked', true);
        formData.append('introduction', currentReserveItem.introduction);
        formData.append('image', currentReserveItem.image);
        formData.append('studentId', currentReserveItem.studentId);

        await updateStudyRoom(currentReserveItem.id, formData);
        reserveDialogVisible.value = false;
        await renderStudyRooms();
    } else {
        ElMessage.error('请输入已存在ID');
    }
};


// Watcher for search query
watch(searchQuery, async (newQuery, oldQuery) => {
    if (newQuery !== oldQuery) {
        currentPage.value = 1; // Reset to the first page when the search query changes
        await renderStudyRooms();
    }
});

// Computed property for filtered and paginated study rooms
const paginatedStudyRooms = computed(() => {
    let filteredRooms = studyRooms.value;

    if (searchQuery.value) {
        filteredRooms = filteredRooms.filter(room =>
            room.name.toLowerCase().includes(searchQuery.value.toLowerCase())
        );
    }

    total.value = filteredRooms.length;

    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;

    return filteredRooms.slice(start, end);
});

const handlePageChange = (newPage) => {
    currentPage.value = newPage;
};
</script>

<template>
    <div class="main">
        <div class="header">
            <el-input 
                v-model="searchQuery"
                placeholder="搜索自习室"
                class="search-input"
            ></el-input>
        </div>

        <el-table :data="paginatedStudyRooms" border style="width: 100%; height: 445px;">
            <el-table-column prop="name" label="自习室名称"></el-table-column>
            <el-table-column prop="studentId" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column prop="studentName" label="预约人"></el-table-column>
            <el-table-column fixed="right" label="操作" width="200">
                <template #default="scope">
                    <div style="display: flex; justify-content: space-between;">
                        <el-button v-if="scope.row.studentId !== 0" @click="cancelReservation(scope.row)" type="text" size="small">取消预约</el-button>
                        <el-button v-else @click="handleReserve(scope.row)" type="text" size="small">帮助预约</el-button>
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

        <!-- Reserve Dialog -->
        <el-dialog class="reserveDialog" v-model="reserveDialogVisible" title="帮助预约" width="400">
            <div>
                <el-form :model="currentReserveItem">
                    <el-form-item label="学生ID">
                        <el-input v-model="currentReserveItem.studentId"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer">
                <el-button @click="reserveDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleReserveSave">保存</el-button>
            </div>
        </el-dialog>
    </div>
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

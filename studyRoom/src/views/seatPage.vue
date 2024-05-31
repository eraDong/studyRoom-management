<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue';
import { getAllStudyRooms, reserveStudyRoom } from '@/api/studyRoom';
import { getStudentById } from '@/api/student';
import { useUserStore } from '@/stores/user';

const studyRooms = ref([]);
const reserveDialogVisible = ref(false);
const searchQuery = ref('');
const currentPage = ref(1);
const pageSize = ref(10);
const totalRooms = ref(0);

let selectedRoom = reactive({
    id: null,
    name: '',
    location: '',
    booked: false,
    studentId: null,
});

const renderStudyRooms = async () => {
    const roomsData = await getAllStudyRooms();
    // 为每个房间增加预约人用户名
    for (const room of roomsData.data) {
        if (room.booked) {
            const studentData = await getStudentById(room.studentId);
            room.studentName = studentData.data.name;
        } else {
            room.studentName = '';
        }
    }
    studyRooms.value = roomsData;
    totalRooms.value = roomsData.data.length;
};

onMounted(() => {
    renderStudyRooms();
});

const formatIsBooked = (row, column, cellValue) => {
    return cellValue ? "已预约" : "空闲";
};

const handleReserve = (row) => {
    Object.assign(selectedRoom, row);
    reserveDialogVisible.value = true;
};

const confirmReserve = async () => {
    const userStore = useUserStore();
    await reserveStudyRoom(selectedRoom.id, { studentId: userStore.studentId }); // 获取实际的 student ID
    reserveDialogVisible.value = false;
    await renderStudyRooms();
};

watch(searchQuery, async (newQuery, oldQuery) => {
    await renderStudyRooms();
});

// Computed property for filtered and paginated study rooms
const filteredStudyRooms = computed(() => {
    if (!searchQuery.value) {
        return studyRooms.value.data;
    }
    return studyRooms.value.data.filter(room =>
        room.name.toLowerCase().includes(searchQuery.value.toLowerCase())
    );
});

const paginatedStudyRooms = computed(() => {
    const start = (currentPage.value - 1) * pageSize.value;
    const end = start + pageSize.value;
    return filteredStudyRooms.value? filteredStudyRooms.value.slice(start, end):null;
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
                placeholder="搜索自习室"
                class="search-input"
            ></el-input>
        </div>

        <el-table :data="paginatedStudyRooms" border style="width: 100%; height: 445px;">
            <el-table-column prop="name" label="自习室"></el-table-column>
            <el-table-column prop="location" label="位置"></el-table-column>
            <el-table-column prop="booked" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column prop="studentName" label="预约人"></el-table-column>
            <el-table-column fixed="right" label="操作" width="150">
                <template #default="scope">
                    <el-button 
                        v-if="!scope.row.booked" 
                        type="primary" 
                        size="small" 
                        @click="handleReserve(scope.row)">
                        预约
                    </el-button>
                    <span v-else>已预约</span>
                </template>
            </el-table-column>
        </el-table>

        <div class="pagination">
            <el-pagination
                background
                layout="prev, pager, next"
                :total="totalRooms"
                :page-size="pageSize"
                :current-page.sync="currentPage"
                @current-change="handlePageChange">
            </el-pagination>
        </div>

        <!-- Reserve Dialog -->
        <el-dialog class="reserveDialog" v-model="reserveDialogVisible" title="确认预约" width="400">
            <div>
                <p>您确认要预约该座位吗？</p>
            </div>
            <div slot="footer">
                <el-button @click="reserveDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="confirmReserve">确认</el-button>
            </div>
        </el-dialog>
    </div>
</template>


<style>
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

    .el-dialog {
        img {
            width: 100%;
            height: 350px;
        }
    }
}
</style>

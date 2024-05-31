<script setup>
import { ref, reactive, onMounted } from 'vue';
import { getAllStudyRooms, reserveStudyRoom } from '@/api/studyRoom';
import { getStudentById } from '@/api/student';
import { useUserStore } from '@/stores/user';

const studyRooms = ref([]);
const reserveDialogVisible = ref(false);
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
</script>

<template>
    <div class="main">
        <el-table :data="studyRooms.data" border style="width: 100%; height: 660px;">
            <el-table-column prop="name" label="自习室"></el-table-column>
            <el-table-column prop="location" label="位置"></el-table-column>
            <el-table-column prop="booked" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column prop="studentName" label="预约人" v-if="true"></el-table-column>
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

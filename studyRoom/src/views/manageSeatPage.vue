<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllStudyRooms, updateStudyRoom } from '@/api/studyRoom'

let studyRooms = ref([])
let reserveDialogVisible = ref(false)
let currentReserveItem = reactive({
    id: null,
    studentId: null,
    name:null,
    location:null,
    isBooked:null,
    introduction:null,
    image:null
    // formData.append('name', currentReserveItem.name)
    // formData.append('location', currentReserveItem.location)
    // formData.append('isBooked', true)
    // formData.append('introduction', currentReserveItem.introduction)
    // formData.append('image', currentReserveItem.image)
    // formData.append('studentId', currentReserveItem.studentId)
})

const renderStudyRooms = async () => {
    studyRooms.value = await getAllStudyRooms()
}

onMounted(() => {
    renderStudyRooms()
})

const formatIsBooked = (row, column, cellValue) => {
    return cellValue ? "是" : "否"
}

const cancelReservation = async (row) => {
    const formData = new FormData()
    console.log(row)
    formData.append('name', row.name)
    formData.append('location', row.location)
    formData.append('isBooked', false)
    formData.append('introduction', row.introduction)
    formData.append('image', row.image)
    formData.append('studentId', 0)

    await updateStudyRoom(row.id, formData)
    await renderStudyRooms()
}

const handleReserve = (row) => {
    currentReserveItem.id = row.id
    // currentReserveItem.studentId = row.studentId
    currentReserveItem.name = row.name
    currentReserveItem.location = row.location
    currentReserveItem.isBooked = row.isBooked
    currentReserveItem.introduction = row.introduction
    currentReserveItem.image = row.image
    reserveDialogVisible.value = true
}

const handleReserveSave = async () => {
    const formData = new FormData()
    formData.append('name', currentReserveItem.name)
    formData.append('location', currentReserveItem.location)
    formData.append('isBooked', true)
    formData.append('introduction', currentReserveItem.introduction)
    formData.append('image', currentReserveItem.image)
    formData.append('studentId', currentReserveItem.studentId)

    await updateStudyRoom(currentReserveItem.id, formData)
    reserveDialogVisible.value = false
    await renderStudyRooms()
}
</script>


<template>
    <div class="main">
        <el-table :data="studyRooms.data" border style="width: 100%; height: 660px;">
            <el-table-column prop="name" label="自习室名称"></el-table-column>
            <el-table-column prop="booked" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column prop="studentName" label="预约人"></el-table-column>
            <el-table-column fixed="right" label="操作" width="200">
                <template #default="scope">
                    <div style="display: flex; justify-content: space-between;">
                        <el-button v-if="scope.row.booked" @click="cancelReservation(scope.row)" type="text" size="small">取消预约</el-button>
                        <el-button v-else @click="handleReserve(scope.row)" type="text" size="small">帮助预约</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

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

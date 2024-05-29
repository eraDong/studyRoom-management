<script setup>
import {ref} from 'vue'
import {getAllStudyRooms,getStudyRoomById} from '@/api/studyRoom'

let studyRoomArr = ref([])
let imgSrc = ref('')
let studyIntoduction = ref('')
let dialogTableVisible = ref(false)

const studyRoomRender = async () => {
    studyRoomArr.value = await getAllStudyRooms()
}
studyRoomRender()

const formatIsBooked = (row,column,cellValue) => {
    return cellValue ? "是" : "否"
}

const handleClick = (item) => {
    dialogTableVisible.value = !dialogTableVisible.value
    imgSrc.value = item.image
    
    studyIntoduction.value = item.introduction
}


</script>
<template>
    <div class="main">
        <el-table :data="studyRoomArr.data" border style="width: 100%;height: 660px;">
            <el-table-column prop="name" label="自习室"></el-table-column>
            <el-table-column prop="location" label="位置"></el-table-column>
            <el-table-column prop="booked" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column fixed="right" label="操作" width="100">
                <template #default="scope">
                    <el-button @click="handleClick(scope.row)" type="text" size="small"
                    >查看</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogTableVisible" title="自习室详情" width="800">
            <div>
                <!-- {{ studyRoomById.data.image }} -->
                
                <img :src="imgSrc" alt="">
            </div>
            <div>
                {{ studyIntoduction }}
            </div>
        </el-dialog>

    </div>

</template>
<style lang="less">
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
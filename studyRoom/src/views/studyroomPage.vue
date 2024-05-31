<script setup>
import { ref,reactive,computed,onMounted} from 'vue'
import { getAllStudyRooms } from '@/api/studyRoom'

let studyRoomArr = ref([])
let dialogVisible = ref(false)
let total = ref(0)
let currentStudyRoom = ref({
    name: '',
    introduction: ''
})
let pagination = reactive({
    currentPage: 1,
    pageSize: 8
})

const studyRoomRender = async () => {
    studyRoomArr.value = await getAllStudyRooms()
    total.value = studyRoomArr.value.data.length
}

onMounted(() => {
    studyRoomRender()
})

const handleClick = (item) => {
    currentStudyRoom.value = item
    dialogVisible.value = true
}

// Pagination handlers
const handleSizeChange = (pageSize) => {
    pagination.pageSize = pageSize
}

const handleCurrentChange = (currentPage) => {
    pagination.currentPage = currentPage
}

// Computed property for paginated study rooms
const paginatedStudyRooms = computed(() => {
   
    
    const start = (pagination.currentPage - 1) * pagination.pageSize
    const end = start + pagination.pageSize
   
    return studyRoomArr.value.data?studyRoomArr.value.data.slice(start, end):null
})
</script>
<template>
    <div class="main">
        <div class="study-room-cards">
            <el-row :gutter="20" v-if="total!==0">
            <el-col :span="6" v-for="item in paginatedStudyRooms" :key="item.id">
                <el-card shadow="always" @click="handleClick(item)">
                    <img :src="item.image" class="image">
                    <div style="padding: 14px;">
                        <span>{{ item.name }}</span>
                        <div class="bottom clearfix">
                            <el-tag class="float-right">{{ item.booked ? '已预约' : '未预约' }}</el-tag>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-empty v-else description="暂时没有自习室" />
        </div>

        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-sizes="[4, 8, 12]"
            :page-size="pagination.pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

        <el-dialog v-model="dialogVisible" title="自习室详情" width="800">
            <div>
                <p>{{ currentStudyRoom.introduction }}</p>
            </div>
        </el-dialog>
    </div>
</template>

<style lang="less">
.main {
    .study-room-cards {
        padding: 20px;
    }

    .el-card {
        cursor: pointer;
        margin: 5px;
        transition: transform 0.3s;
        &:hover {
            transform: scale(1.05);
        }
    }

    .image {
        width: 100%;
        height: 200px;
        object-fit: cover;
    }

    .el-dialog {
        h3 {
            margin: 0 0 20px 0;
        }
    }
}
</style>

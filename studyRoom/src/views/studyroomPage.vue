<script setup>
import { ref, reactive, computed, onMounted, watch } from 'vue'
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

let searchQuery = ref('')

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

// Watcher for search query
watch(searchQuery, async (newQuery, oldQuery) => {
    await studyRoomRender();
});

// Computed property for paginated and filtered study rooms
const paginatedStudyRooms = computed(() => {

    const filteredRooms = studyRoomArr.value.data.filter(room =>
        room.name?.toLowerCase().includes(searchQuery.value.toLowerCase())
    ) || [];

    const start = (pagination.currentPage - 1) * pagination.pageSize;
    const end = start + pagination.pageSize;

    if (!searchQuery.value) {
        return studyRoomArr.value.data.slice(start,end);
    }

    return filteredRooms.slice(start, end);
});

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

        <div class="study-room-cards">
            <el-row :gutter="20" v-if="total !== 0">
                <el-col :span="6" v-for="item in paginatedStudyRooms" :key="item.id">
                    <el-card shadow="always" @click="handleClick(item)">
                        <img :src="item.image" class="image">
                        <div style="padding: 14px;">
                            <span>{{ item.name }}</span>
                            <div class="bottom clearfix">
                                <el-tag class="float-right">{{ item.booked ? '已预约' : '未预约' }}</el-tag>
                                <el-tag>位置：{{ item.location }}</el-tag>
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

    .pagination {
        margin: 20px 0;
        display: flex;
        justify-content: center;
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


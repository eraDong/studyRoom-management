<script setup>
import { ref,reactive,computed, watch } from 'vue'
import { getAllStudyRooms, deleteStudyRoomById, updateStudyRoom, addStudyRoom } from '@/api/studyRoom'
import { ElMessage } from 'element-plus';

let studyRoomArr = ref([])
let imgSrc = ref('')
let studyIntroduction = ref('')
let dialogVisible = ref(false)
let editDialogVisible = ref(false)
let addDialogVisible = ref(false)
let currentEditItem = ref(null)
let total = ref(0)
let currentAddItem = ref({
  name: '',
  location: '',
  booked: false,
  image: '',
  introduction: '',
  studentId:0,
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
studyRoomRender()

const handleDelete = async (item) => {
    await deleteStudyRoomById(item.id)
    studyRoomRender()
}

const handleEdit = (item) => {
    currentEditItem.value = { ...item }
    editDialogVisible.value = true
}

const handleAdd = () => {
    currentAddItem.value.name = ''
    currentAddItem.value.location = ''
    currentAddItem.value.image = ''
    currentAddItem.value.booked = ''
    currentAddItem.value.introduction = ''
    currentAddItem.value.studentId = ''
    addDialogVisible.value = true
}

const handleEditSave = async () => {
    const formData = new FormData()
    formData.append('name', currentEditItem.value.name || '')
    formData.append('location', currentEditItem.value.location || '')
    formData.append('isBooked', currentEditItem.value.booked || false)
    if (currentEditItem.value.booked === false) currentEditItem.value.studentId = 0
    formData.append('studentId', currentEditItem.value.studentId || 0)
    formData.append('introduction', currentEditItem.value.introduction || '')
    formData.append('image', currentEditItem.value.image || '')
    await updateStudyRoom(currentEditItem.value.id, formData)
    editDialogVisible.value = false
    studyRoomRender()
}

const handleAddSave = async () => {
    // 验证 name 和 location 是否为空
    if (!currentAddItem.value.name || !currentAddItem.value.location) {
        ElMessage.error('名称和位置不能为空。请填写完整信息后再保存。');
        return;
    }

    const formData = new FormData();
    formData.append('name', currentAddItem.value.name);
    formData.append('location', currentAddItem.value.location);
    formData.append('isBooked', currentAddItem.value.booked || false);
    formData.append('studentId', currentAddItem.value.studentId || 0);
    formData.append('introduction', currentAddItem.value.introduction || '');
    formData.append('image', currentAddItem.value.image || '');

    await addStudyRoom(formData);
    addDialogVisible.value = false;
    studyRoomRender();
};


const handleFileChange = (event) => {
    const file = event.target.files[0]
    if (file) {
        if(editDialogVisible.value)currentEditItem.value.image = `src/assets/images/${file.name}`
        if(addDialogVisible.value)currentAddItem.value.image = `src/assets/images/${file.name}`
    }
}

const handleClick = (item) => {
    imgSrc.value = item.image
    studyIntroduction.value = item.introduction
    dialogVisible.value = true
}

// Pagination handlers
const handleSizeChange = (pageSize) => {
    pagination.pageSize = pageSize
}

const handleCurrentChange = (currentPage) => {
    pagination.currentPage = currentPage
}

watch(searchQuery, async (newQuery, oldQuery) => {
    await studyRoomRender();
});

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
            <el-button class="add-btn" @click="handleAdd">添加自习室</el-button>
        </div>
        <el-row :gutter="20" v-if="total!==0">
            <el-col :span="6" v-for="item in paginatedStudyRooms" :key="item.id">
                <el-card shadow="always" @click="handleClick(item)">
                    <img :src="item.image" class="image">
                    <div style="padding: 14px;">
                        <span>{{ item.name }}</span>
                        <div class="bottom clearfix">
                            <el-tag class="float-right">{{ item.booked ? '已预约' : '未预约' }}</el-tag>
                            <el-tag>位置：{{ item.location }}</el-tag>
                        </div>
                        <div class="actions">
                            <el-button type="text" @click.stop="handleEdit(item)">编辑</el-button>
                            <el-button type="text" @click.stop="handleDelete(item)">删除</el-button>
                        </div>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-empty v-else description="暂时没有自习室" />

        <el-dialog v-model="dialogVisible" title="自习室详情" width="800">
            <div>
                {{ studyIntroduction }}
            </div>
        </el-dialog>

        <el-dialog v-model="editDialogVisible" title="编辑自习室" width="800">
            <el-form :model="currentEditItem">
                <el-form-item label="自习室名称">
                    <el-input v-model="currentEditItem.name"></el-input>
                </el-form-item>
                <el-form-item label="位置">
                    <el-input v-model="currentEditItem.location"></el-input>
                </el-form-item>
                <el-form-item label="图片">
                    <input type="file" @change="handleFileChange">
                    <img v-if="currentEditItem.image" :src="currentEditItem.image" alt="uploaded image" style="width: 100%; margin: 10px;">
                </el-form-item>
                <el-form-item label="简介">
                    <el-input type="textarea" v-model="currentEditItem.introduction"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleEditSave">保存</el-button>
            </div>
        </el-dialog>

        <el-dialog v-model="addDialogVisible" title="增加自习室" width="800">
            <el-form :model="currentAddItem">
                <el-form-item label="自习室名称">
                    <el-input v-model="currentAddItem.name"></el-input>
                </el-form-item>
                <el-form-item label="位置">
                    <el-input v-model="currentAddItem.location"></el-input>
                </el-form-item>
                <el-form-item label="图片">
                    <input type="file" @change="handleFileChange">
                    <img v-if="currentAddItem.image" :src="currentAddItem.image" alt="uploaded image" style="width: 100%; margin: 10px;">
                </el-form-item>
                <el-form-item label="简介">
                    <el-input type="textarea" v-model="currentAddItem.introduction"></el-input>
                </el-form-item>
            </el-form>
            <div slot="footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddSave">增加</el-button>
            </div>
        </el-dialog>

        <!-- Pagination -->
        <el-pagination
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
            :current-page="pagination.currentPage"
            :page-sizes="[4, 8, 12]"
            :page-size="pagination.pageSize"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper">
        </el-pagination>

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

    .el-card {
        margin: 5px;
        cursor: pointer;
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

    .actions {
        margin-top: 10px;
        display: flex;
        justify-content: space-between;
    }

    .el-dialog {
        img {
            width: 100%;
            height: 250px;
        }
    }
}
</style>

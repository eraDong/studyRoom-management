<script setup>
import { ref } from 'vue'
import { getAllStudyRooms, deleteStudyRoomById,updateStudyRoom,addStudyRoom } from '@/api/studyRoom'

let studyRoomArr = ref([])
let imgSrc = ref('')
let studyIntroduction = ref('')
let dialogTableVisible = ref(false)
let editDialogVisible = ref(false)
let addDialogVisible = ref(false)
let currentEditItem = ref(null)
const currentAddItem = ref({
  name: '',
  location: '',
  booked: false,
  image: '',
  introduction: ''
})

const studyRoomRender = async () => {
    studyRoomArr.value = await getAllStudyRooms()
}
studyRoomRender()

const formatIsBooked = (row, column, cellValue) => {
    return cellValue ? "是" : "否"
}

const handleDelete = async (item) => {
    await deleteStudyRoomById(item.id)
    studyRoomRender()
}

const handleEdit = (item) => {
    currentEditItem.value = { ...item }
    editDialogVisible.value = true
}

const handleAdd = () => {
    addDialogVisible.value = true
}

const handleEditSave = async () => {
    const formData = new FormData()

    formData.append('name', currentEditItem.value.name || '')
    formData.append('location', currentEditItem.value.location || '')
    formData.append('isBooked', currentEditItem.value.booked || false)
    formData.append('studentId', currentEditItem.value.studentId || 0)
    formData.append('introduction', currentEditItem.value.introduction || '')
    formData.append('image', currentEditItem.value.image || '')


    await updateStudyRoom(currentEditItem.value.id, formData)
    editDialogVisible.value = false
    studyRoomRender()

}

const handleAddSave = async () => {
    const formData = new FormData()

    formData.append('name', currentAddItem.value.name || '')
    formData.append('location', currentAddItem.value.location || '')
    formData.append('isBooked', currentAddItem.value.booked || false)
    formData.append('studentId', currentAddItem.value.studentId || 0)
    formData.append('introduction', currentAddItem.value.introduction || '')
    formData.append('image', currentAddItem.value.image || '')


    await addStudyRoom(formData)
    addDialogVisible.value = false
    studyRoomRender()
}

const handleFileChange = (event) => {
    const file = event.target.files[0]
    if (file) {
        // 保存文件名
        currentEditItem.value.fileName = file.name;
        // 设置图片的相对路径
        currentEditItem.value.image = `src/assets/images/${file.name}`;
    }
}

</script>



<template>
    <div class="main">
        <el-button class="add-btn" @click="handleAdd()">增加自习室</el-button>
        <el-table :data="studyRoomArr.data" border style="width: 100%; height: 660px;">
            <el-table-column prop="name" label="自习室"></el-table-column>
            <el-table-column prop="location" label="位置"></el-table-column>
            <el-table-column prop="booked" label="是否被预约" :formatter="formatIsBooked"></el-table-column>
            <el-table-column fixed="right" label="操作" width="130">
                <template #default="scope">
                    <div style="display: flex">
                        <el-button @click="handleEdit(scope.row)" type="text" size="small">编辑</el-button>
                        <el-button @click="handleDelete(scope.row)" type="text" size="small">删除</el-button>
                    </div>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogTableVisible" title="自习室详情" width="800">
            <div>
                <img :src="imgSrc" alt="">
            </div>
            <div>
                {{ studyIntroduction }}
            </div>
        </el-dialog>

        <el-dialog class="editDialog" v-model="editDialogVisible" title="编辑自习室" width="800">
            <div>
                <el-form :model="currentAddItem">
                    <el-form-item label="自习室名称">
                        <el-input v-model="currentEditItem.name"></el-input>
                    </el-form-item>
                    <el-form-item label="位置">
                        <el-input v-model="currentEditItem.location"></el-input>
                    </el-form-item>
                    <el-form-item label="是否被预约">
                        <el-switch v-model="currentEditItem.booked"></el-switch>
                    </el-form-item>
                    <el-form-item label="图片">
                        <input type="file" @change="handleFileChange" />
                        <img v-if="currentEditItem.image" :src="currentEditItem.image" alt="uploaded image" style=" width: 100%; margin: 10px;">
                    </el-form-item>
                    <el-form-item label="简介">
                        <el-input type="textarea" v-model="currentEditItem.introduction"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleEditSave">保存</el-button>
            </div>
        </el-dialog>

        <el-dialog class="addDialog" v-model="addDialogVisible" title="增加自习室" width="800">
            <div>
                <el-form :model="currentAddItem">
                    <el-form-item label="自习室名称">
                        <el-input v-model="currentAddItem.name"></el-input>
                    </el-form-item>
                    <el-form-item label="位置">
                        <el-input v-model="currentAddItem.location"></el-input>
                    </el-form-item>
                    <el-form-item label="是否被预约">
                        <el-switch v-model="currentAddItem.booked"></el-switch>
                    </el-form-item>
                    <el-form-item label="图片">
                        <input type="file" @change="handleFileChange" />
                        <img v-if="currentAddItem.image" :src="currentAddItem.image" alt="uploaded image" style=" width: 100%; margin: 10px;">
                    </el-form-item>
                    <el-form-item label="简介">
                        <el-input type="textarea" v-model="currentAddItem.introduction"></el-input>
                    </el-form-item>
                </el-form>
            </div>
            <div slot="footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="handleAddSave">增加</el-button>
            </div>
        </el-dialog>
    </div>
</template>


<style lang="less">
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
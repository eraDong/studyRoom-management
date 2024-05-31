<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/user';
import router from '@/router';

const form = ref({
  name: '',
  username: '',
  password: '',
  isAdmin: false
});

const rules = ref({
  name: [
    { 
      required: true, 
      message: '姓名不能为空', 
      trigger: 'blur' 
    },
    { 
      max: 12, 
      message: '姓名长度不能超过12个字', 
      trigger: 'blur' 
    }
  ],
  username: [
    { 
      required: true, 
      message: '用户名不能为空', 
      trigger: 'blur' 
    },
    { 
      min: 5, 
      max: 15, 
      message: '用户名长度必须在5到15个字符之间', 
      trigger: 'blur' 
    }
  ],
  password: [
    { 
      required: true, 
      message: '密码不能为空', 
      trigger: 'blur' 
    },
    { 
      min: 6, 
      message: '密码长度不能少于6个字符', 
      trigger: 'blur' 
    }
  ]
});

const message = ref('');
const error = ref('');

const userStore = useUserStore();

const jmpToLogin = () => {
  router.push({ name: "login" });
};

const register = async (formEl) => {
  if (!formEl) return;
  await formEl.validate(async (valid) => {
    if (valid) {
      const response = await userStore.register(
        form.value.name,
        form.value.username,
        form.value.password,
        form.value.isAdmin
      );
      if (response.success) {
        message.value = response.message;
        error.value = '';
        // 重定向到登录页
        router.push({ name: 'login' });
      } else {
        message.value = '';
        error.value = response.message;
      }
    } else {
      message.value = '';
      error.value = '请正确填写表单';
    }
  });
};
</script>

<template>
  <div class="register-container">
    <h1>用户注册</h1>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="姓名" prop="name">
        <el-input v-model="form.name"></el-input>
      </el-form-item>
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="() => register($refs.formRef)">注册</el-button>
        <el-button type="text" @click="jmpToLogin">返回</el-button>
      </el-form-item>
    </el-form>
    <div v-if="message" class="message">{{ message }}</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<style lang="less" scoped>
.register-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  max-width: 400px;
  margin: 0 auto;
  padding: 20px;
  border: 1px solid #dcdcdc;
  border-radius: 8px;

  .el-form-item {
    margin-bottom: 15px;
    width: 100%;
  }

  .el-input {
    width: 100%;
  }

  .message {
    color: green;
    margin-top: 15px;
  }

  .error {
    color: red;
    margin-top: 15px;
  }
}
</style>

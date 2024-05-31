<script setup>
import { ref } from 'vue';
import { useUserStore } from '@/stores/user';
import router from '@/router';

const form = ref({
  username: '',
  password: ''
});

const rules = ref({
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

const jmpToRegister = () => {
  router.push({ name: "register" });
};

const login = async (formEl) => {
  if (!formEl) return;
  await formEl.validate(async (valid) => {
    if (valid) {
      const response = await userStore.login(form.value.username, form.value.password)
      if (response.success) {
        message.value = '登录成功！';
        error.value = '';
        // 重定向到主页或其他页面
        router.push({ name: 'index' }); // 假设主页的路由名称是'home'
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
  <div class="login-container">
    <h1>用户登录</h1>
    <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
      <el-form-item label="用户名" prop="username">
        <el-input v-model="form.username"></el-input>
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input type="password" v-model="form.password"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="() => login($refs.formRef)">登录</el-button>
        <el-button type="text" @click="jmpToRegister">若没有账号，请注册</el-button>
      </el-form-item>
    </el-form>
    <div v-if="message" class="message">{{ message }}</div>
    <div v-if="error" class="error">{{ error }}</div>
  </div>
</template>

<style lang="less" scoped>
.login-container {
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

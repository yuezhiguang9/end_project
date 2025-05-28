<template>
  <div class="upload-container">
    <!-- 学生数据上传 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">学生数据导入</div>
      </template>
      <el-upload
          action="#"
          :auto-upload="false"
          :on-change="handleStudentFile"
          accept=".txt,.csv"
          drag
      >
        <el-icon class="el-icon--upload">
          <upload-filled/>
        </el-icon>
        <div class="el-upload__text">拖拽文件或<em>点击上传</em></div>
        <template #tip>
          <div class="el-upload__tip">
            请上传文本文件（每行格式：学号,姓名,性别,年龄,系部）
          </div>
        </template>
      </el-upload>
      <el-button
          type="primary"
          @click="submitStudents"
          :disabled="!studentFile"
      >
        导入数据
      </el-button>
    </el-card>

    <!-- 文章上传 -->
    <el-card class="box-card">
      <template #header>
        <div class="card-header">文章上传</div>
      </template>
      <el-form :model="articleForm" label-width="80px">
        <el-form-item label="作者">
          <el-input v-model="articleForm.author"/>
        </el-form-item>
        <el-form-item label="文章类型">
          <el-select v-model="articleForm.type" placeholder="请选择">
            <el-option label="PDF" value="PDF"/>
            <el-option label="DOCX" value="DOCX"/>
            <el-option label="DOC" value="DOC"/>
          </el-select>
        </el-form-item>
        <el-upload
            action="#"
            :auto-upload="false"
            :on-change="handleArticleFile"
            accept=".pdf,.docx,.doc"
            drag
        >
          <el-icon class="el-icon--upload">
            <upload-filled/>
          </el-icon>
          <div class="el-upload__text">拖拽文件或<em>点击上传</em></div>
          <template #tip>
            <div class="el-upload__tip">
              仅支持 PDF/DOCX/DOC 格式文件
            </div>
          </template>
        </el-upload>
      </el-form>
      <el-button
          type="primary"
          @click="submitArticle"
          :disabled="!articleForm.file || !articleForm.author || !articleForm.type"
      >
        提交文章
      </el-button>
    </el-card>
    <el-progress :percentage="uploadProgress"/>
  </div>
</template>

<script setup>
import {ref} from 'vue';
import {ElMessage} from 'element-plus';
import {UploadFilled} from '@element-plus/icons-vue';
import axios from 'axios';

const studentFile = ref(null);
const articleForm = ref({
  author: '',
  type: '',
  file: null
});

const handleStudentFile = (file) => {
  studentFile.value = file.raw;
};

const handleArticleFile = (file) => {
  articleForm.value.file = file.raw;
};

const submitStudents = async () => {
  const formData = new FormData();
  formData.append('file', studentFile.value);

  try {
    const response = await axios.post('/api/upload/students', formData, {
      headers: {'Content-Type': 'multipart/form-data'}
    });
    ElMessage.success(response.data);
    studentFile.value = null;
  } catch (error) {
    ElMessage.error('导入失败: ' + error.response?.data || error.message);
  }
};

const submitArticle = async () => {
  const formData = new FormData();
  formData.append('file', articleForm.value.file);
  formData.append('author', articleForm.value.author);
  formData.append('type', articleForm.value.type);

  try {
    const response = await axios.post('/api/upload/article', formData, {
      headers: {'Content-Type': 'multipart/form-data'}
    });
    ElMessage.success('文章上传成功');
    articleForm.value = {author: '', type: '', file: null};
  } catch (error) {
    ElMessage.error('上传失败: ' + error.response?.data || error.message);
  }
};
</script>

<style scoped>
.upload-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.box-card {
  margin-bottom: 20px;
}
</style>
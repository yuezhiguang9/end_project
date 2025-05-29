<template>
  <div id="app" class="container">
    <h1 class="title">文件上传与下载</h1>

    <div class="upload-section card">
      <h2 class="card-title">上传文件</h2>
      <div class="form-group">
        <input type="text" placeholder="请输入作者" v-model="author" class="form-control author-input">

        <select name="type" id="filetype" v-model="fileType" class="form-control filetype-select">
          <option value="">请选择文件类型</option>
          <option value="pdf">PDF</option>
          <option value="txt">TXT</option>
          <option value="doc">DOC</option>
          <option value="xls">XLS</option>
          <option value="ppt">PPT</option>
          <option value="pptx">PPTX</option>
        </select>

        <input type="file" ref="fileInput" @change="handleFileChange" class="form-control-file">

        <button @click="uploadFile" :disabled="isUploading" class="btn btn-primary upload-btn">
          {{ isUploading ? '上传中...' : '上传文件' }}
        </button>

        <p v-if="uploadMessage" class="message">{{ uploadMessage }}</p>
      </div>
    </div>

    <div class="file-list-section card">
      <h2 class="card-title">文件列表</h2>
      <button @click="fetchFileList" class="btn btn-secondary refresh-btn">刷新列表</button>

      <div class="table-responsive">
        <table class="table table-striped table-hover">
          <thead>
          <tr>
            <th>作者</th>
            <th>文件名</th>
            <th>大小</th>
            <th>保存路径</th>
            <th>修改时间</th>
            <th>操作</th>
          </tr>
          </thead>
          <tbody>
          <tr v-for="file in fileList" :key="file.name">
            <td>{{ file.author }}</td>
            <td>{{ file.fileName }}</td>
            <td>{{ file.fileLength }}</td>
            <td>{{ file.filePath }}</td>
            <td>{{ formatFileSize(file.fileLength) }}</td>
            <td>{{ formatDate(file.lastModified) }}</td>
            <td>
              <button @click="downloadFile(file.fileName)" class="btn btn-success btn-sm download-btn">下载</button>
            </td>
          </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue';
import axios from 'axios';

const fileInput = ref(null);
const isUploading = ref(false);
const uploadMessage = ref('');
const fileList = ref([]);

// 获取文件列表
const fetchFileList = async () => {
  try {
    const response = await axios.get('http://localhost:8080/fileList');
    fileList.value = response.data;
  } catch (error) {
    console.error('获取文件列表失败:', error);
    alert('获取文件列表失败: ' + (error.response?.data?.message || error.message));
  }
};

onMounted(() => {
  fetchFileList();
});

const handleFileChange = () => {
  uploadMessage.value = '';
};

const author = ref('');
const fileType = ref('');

const uploadFile = async () => {
  if (!fileInput.value.files.length) {
    uploadMessage.value = '请先选择文件';
    return;
  }
  if (!author.value.trim()) {
    uploadMessage.value = '请输入作者';
    return;
  }
  if (!fileType.value) {
    uploadMessage.value = '请选择文件类型';
    return;
  }

  isUploading.value = true;
  uploadMessage.value = '';

  const formData = new FormData();
  formData.append('file', fileInput.value.files[0]);

  try {
    const response = await axios.post('http://localhost:8080/upload', formData, {
      params: {
        author: author.value,
        fileType: fileType.value,
      },
      headers: {'Content-Type': 'multipart/form-data'}
    });

    uploadMessage.value = response.data.message;

    if (response.data.message === "文件为空") {
      alert("文件为空");
    } else if (response.data.message === "上传成功") {
      alert("上传成功");
    }

    fetchFileList();
  } catch (error) {
    uploadMessage.value = '上传失败: ' + (error.response?.data?.message || error.message);
  } finally {
    isUploading.value = false;
  }
};

const downloadFile = async (filename) => {
  try {
    const response = await axios.get(`http://localhost:8080/download?fileName=${filename}`, {
      responseType: 'blob'
    });

    const url = window.URL.createObjectURL(new Blob([response.data]));
    const link = document.createElement('a');
    link.href = url;
    link.setAttribute('download', filename);
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    alert('下载失败: ' + (error.response?.data?.message || error.message));
  }
};

// 文件大小格式化函数
const formatFileSize = (bytes) => {
  if (bytes === 0) return '0 B';
  const k = 1024;
  const sizes = ['B', 'KB', 'MB', 'GB'];
  const i = Math.floor(Math.log(bytes) / Math.log(k));
  return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
};

// 日期格式化函数
const formatDate = (timestamp) => {
  const date = new Date(timestamp);
  return date.toLocaleString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit',
    hour: '2-digit',
    minute: '2-digit',
    second: '2-digit'
  });
};
</script>

<style scoped>
.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 2rem;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.title {
  text-align: center;
  color: #333;
  margin-bottom: 2rem;
}

.card {
  background-color: #fff;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  padding: 1.5rem;
  margin-bottom: 2rem;
}

.card-title {
  color: #444;
  margin-top: 0;
  margin-bottom: 1.5rem;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #eee;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.author-input {
  height: 40px;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  border: 1px solid #ddd;
  transition: border-color 0.3s ease;
}

.author-input:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.filetype-select {
  height: 40px;
  padding: 0.5rem 1rem;
  border-radius: 5px;
  border: 1px solid #ddd;
  transition: border-color 0.3s ease;
}

.filetype-select:focus {
  outline: none;
  border-color: #007bff;
  box-shadow: 0 0 0 2px rgba(0, 123, 255, 0.25);
}

.form-control-file {
  padding: 0.5rem 1rem;
  border: 1px solid #ddd;
  border-radius: 5px;
  background-color: #f9f9f9;
}

.upload-btn {
  width: 120px;
  padding: 0.5rem 1rem;
  font-weight: bold;
  transition: all 0.3s ease;
}

.btn {
  cursor: pointer;
  border: none;
  border-radius: 5px;
  padding: 0.6rem 1.2rem;
  font-size: 1rem;
  font-weight: 500;
  text-align: center;
  white-space: nowrap;
  vertical-align: middle;
  user-select: none;
  display: inline-block;
  line-height: 1.5;
  background-color: #007bff;
  color: #fff;
}

.btn-primary {
  background-color: #007bff;
}

.btn-primary:hover {
  background-color: #0056b3;
}

.btn-secondary {
  background-color: #6c757d;
}

.btn-secondary:hover {
  background-color: #545b62;
}

.btn-success {
  background-color: #28a745;
}

.btn-success:hover {
  background-color: #1e7e34;
}

.btn-sm {
  padding: 0.3rem 0.6rem;
  font-size: 0.875rem;
}

.message {
  color: #dc3545;
  font-weight: 500;
  margin-top: 0.5rem;
}

.table-responsive {
  overflow-x: auto;
  -webkit-overflow-scrolling: touch;
}

.table {
  width: 100%;
  margin-bottom: 1rem;
  background-color: transparent;
  border-collapse: collapse;
}

.table th,
.table td {
  padding: 1rem;
  vertical-align: top;
  border-top: 1px solid #dee2e6;
}

.table thead th {
  vertical-align: bottom;
  background-color: #f8f9fa;
  color: #495057;
  font-weight: 600;
}

.table-striped tbody tr:nth-of-type(odd) {
  background-color: rgba(0, 123, 255, 0.05);
}

.table-hover tbody tr:hover {
  background-color: rgba(0, 123, 255, 0.1);
}

.refresh-btn {
  margin-bottom: 1rem;
}
</style>
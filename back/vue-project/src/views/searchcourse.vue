<template>
  <div class="nav">
    请输入课程号：<input type="text" v-model="cno" />
    请输入课程名：
    <input type="text" v-model="cname" />
    <button @click="searchcourse" :disabled="loading">
      {{ loading ? "查询中..." : "查询" }}
    </button>
  </div>

  <div class="content">
    <pre>{{ JSON.stringify(response, null, 2) }}</pre>
  </div>
</template>
<script setup>
  import { ref, watch } from "vue";
  import axios from "axios";
  var cno = ref(null);
  var cname = ref(null);
  var response = ref(null);
  const loading = ref(false);

  // 侦测数据变化，以便检查
  watch([cno, cname], ([newcno, newcname]) => {
    console.log("参数变化：", {
      cno: newcno,
      cname: newcname,
    });
  });

  // 定义方法
  function searchcourse() {
    loading.value = true;
    axios
      .get("/searchcourse", {
        params: {
          cno: cno.value,
          cname: cname.value,
        },
      })
      .then((res) => {
        response.value = res.data;
      })
      .catch((err) => {
        response.value = { error: err.message };
      })
      .finally(() => {
        loading.value = false;
      });
  }
</script>

<style>
  .nav {
    width: 100%;
    height: 60px;
    background-color: aquamarine;
    font-size: 30px;
    position: relative;
  }
  .nav input {
    height: 30px;
  }
  .nav button {
    width: 80px;
    height: 55px;
    position: absolute;
    right: 25px;
    top: 2px;
  }
</style>

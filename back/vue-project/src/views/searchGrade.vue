<template>
  <!--  选项区-->
  <div class="search-box">
    <div class="input-group">
      <label>学号：</label>
      <input id="sno" type="number" v-model="sno" placeholder="请输入学号"/>
    </div>

    <div class="input-group">
      <label>姓名：</label>
      <input id="sname" type="text" v-model="cno" placeholder="请输入姓名"/>
    </div>

    <div class="input-group">
      <label>系部：</label>
      <input id="sdept" type="text" v-model="sdept" placeholder="请输入系部"/>
    </div>

    <div class="input-group">
      <label>课程名：</label>
      <input id="cname" type="text" v-model="cname" placeholder="请输入课程名"/>
    </div>

    <button @click="searchGrade" :disabled="loading" class="search-btn">
      {{ loading ? "查询中..." : "开始查询" }}
      <span v-if="loading" class="loading-icon">⌛</span>
    </button>
  </div>

  <div v-if="response?.error" class="error-message">❌ 请求错误：{{ response.error }}</div>

  <div v-else-if="response?.length === 0" class="no-data">📭 未查询到相关数据</div>

  <table v-else-if="response?.length > 0" class="data-table">
    <thead>
    <tr>
      <th>学号</th>
      <th>姓名</th>
      <th>系部</th>
      <th>课程名</th>
      <th @click="handleSort">成绩
        <div>
        <span>
          {{ sortOrder === "asc" ? "↑" : "↓" }}
        </span>
        </div>
      </th>
      <th>操作</th>

    </tr>
    </thead>
    <tbody>
    <tr v-for="(item, index) in response" :key="index" @click="toggleSelect(item.sno)">
      <td>{{ item.sno || "-" }}</td>
      <td>{{ item.sname || "-" }}</td>
      <td>{{ item.sdept || "-" }}</td>
      <td>{{ item.cname || "-" }}</td>
      <td>{{ item.grade || "-" }}</td>
      <td>
        <button @click="openEditDialog(item)">修改</button>
      </td>
    </tr>
    </tbody>
  </table>

  <!--      分页功能-->
  <div class="pagination">
    <button @click="prevPage" :disabled="Current === 1">上一页</button>
    <span>第 {{ Current }} 页 / 共 {{ totalPages }} 页</span>
    <button @click="nextPage" :disabled="Current >= totalPages">下一页</button>

    <select v-model="Size" @change="handlePageSizeChange" class="Size">
      <option value="5">5 条/页</option>
      <option value="10">10 条/页</option>
      <option value="20">20 条/页</option>
    </select>
    <span class="total">共 {{ total }} 条数据</span>
  </div>


  <!-- 编辑弹窗 -->
  <div v-if="showEditDialog" class="dialog">
    <div class="dialog-content">
      <h3>仅可修改成绩</h3>
      <form @submit.prevent="saveEdit">
        <label>学号：<input type="text" readonly v-model="editingItem.sno"/></label>
        <label>姓名：<input type="text" readonly v-model="editingItem.sname"/></label>
        <label>系部：<input type="text" readonly v-model="editingItem.sdept"/></label>
        <label>课程名：<input type="text" readonly v-model="editingItem.cname"/></label>
        <label>分数：<input type="number" v-model="editingItem.grade"/></label>
        <button type="submit">保存</button>
        <button @click="showEditDialog = false">取消</button>
      </form>
    </div>
  </div>
</template>

<script setup lang="js">
import {computed, ref} from "vue";
import axios from "axios";
import "@/style/seachStudent.css"


const openEditDialog = (item) => {
  editingItem.value = JSON.parse(JSON.stringify(item)); // 使用深拷贝
  oldcno.value = item.cno;
  console.log(oldcno.value);
  showEditDialog.value = true;
};


// 查询和分页
const sno = ref("")
const sdept = ref("")
const cname = ref("")
const cno = ref("")
const grade = ref("")
const Current = ref(1)
const Size = ref(5)
const total = ref(0);
var totalPages = ref(0);
const selectedRows = ref([]);


const sortOrder = ref("asc")

// 排序
function handleSort() {
  sortOrder.value = sortOrder.value === "asc" ? "desc" : "asc";//切换
  searchGrade();//刷新页面
}

// 修改搜索方法
function searchGrade() {
  loading.value = true;

  // 应该先获取分页数据再获取总数
  axios.all([
    axios.get("http://localhost:8080/searchGrade", {
      params: {
        sno: sno.value,
        sname: sname.value,
        sdept: sdept.value,
        cname: cname.value,
        sortOrder: sortOrder.value,
        current: Current.value,
        size: Size.value,
      }
    }),
    axios.get("http://localhost:8080/GradegetTotals", {
      params: {
        sno: sno.value,
        sname: sname.value,
        sdept: sdept.value,
        cname: cname.value,
      }
    })
  ]).then(axios.spread((pageRes, totalRes) => {
    response.value = pageRes.data.records;
    total.value = totalRes.data.length;
    totalPages.value = Math.ceil(total.value / Size.value);
  })).finally(() => {
    loading.value = false;
  });
}


// 新增分页方法
const prevPage = () => {
  if (Current.value > 1) {
    Current.value--;
    searchGrade();
  }
};

const nextPage = () => {
  if (Current.value < totalPages.value) {
    Current.value++;
    searchGrade();
  }
};

const handlePageSizeChange = () => {
  Current.value = 1; // 重置到第一页
  searchGrade();
};


const oldsno = ref("");

const oldcno = ref("");

const response = ref("");

const loading = ref(false);

const selectedSnoRows = ref([]); // 选中的行(存储sno）


const showEditDialog = ref(false); // 是否显示编辑弹窗

const editingItem = ref({
  sno: "",
  sname: "",
  sdept: "",
  cno: "",
  grade: "",
}); // 正在编辑的数据

// 计算属性：是否全选
const allSelected = computed(() => {
  return selectedSnoRows.value.length === response.value.length;
});

// 全选/取消全选
const toggleAll = (e) => {
  if (e.target.checked) {
    selectedSnoRows.value = response.value.map((item) => item.sno);
  } else {
    selectedSnoRows.value = [];
  }
};

// 单选切换
const toggleSelect = (sno, cno) => {
  const index = selectedSnoRows.value.indexOf(sno);
  if (index > -1) {
    selectedSnoRows.value.splice(index, 1);
  } else {
    selectedSnoRows.value.push(sno);
  }
};


// 修改
function saveEdit() {
  axios
      .put("http://localhost:8080/reviseGrade", editingItem.value, {
        headers: {
          "Content-type": "application/json",
        },
        params: {
          sortOrder: sortOrder.value,
        }
      })
      .then(() => {
        console.log("修改成功", editingItem.value);
        searchGrade(); //刷新页面
      })
      .catch((err) => {
        console.log("修改失败", err.response?.data || err.message);
        console.log("老sno：" + oldcno.value);
      });
  showEditDialog.value = false; //关闭弹窗
}
</script>


<style scoped>

</style>
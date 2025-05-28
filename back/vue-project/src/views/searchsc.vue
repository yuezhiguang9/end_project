<template>
  <div class="container">
    <!-- 查询条件区域 -->
    <div class="search-box">
      <div class="input-group">
        <label>学号：</label>
        <input id="sno" type="text" v-model="sno" placeholder="请输入学号"/>
      </div>

      <div class="input-group">
        <label>课程名：</label>
        <input id="cno" type="text" v-model="cno" placeholder="请输入课程名"/>
      </div>
      <div class="input-group">
        <label>学分：</label>
        <input id="grade" type="text" v-model="grade" placeholder="请输入成绩"/>
      </div>
      <button @click="searchSc" :disabled="loading" class="search-btn">
        {{ loading ? "查询中..." : "开始查询" }}
        <span v-if="loading" class="loading-icon">⌛</span>
      </button>

      <!--      添加学生按钮-->
      <button @click="showAdd = true" class="search-btn">
        添加课程
      </button>

      <button @click="deleteItem" class="search-btn" v-show="selectedSnoRows.length>0">
        删除所选课程
      </button>
    </div>

    <!-- 结果展示区域 -->
    <div class="result-container">
      <div v-if="response?.error" class="error-message">❌ 请求错误：{{ response.error }}</div>

      <div v-else-if="response?.length === 0" class="no-data">📭 未查询到相关数据</div>

      <table v-else-if="response?.length > 0" class="data-table">
        <thead>
        <tr>
          <th><input type="checkbox" :checked="allSelected" @change="toggleAll"/></th>
          <th>学号</th>
          <th>课程号</th>
          <th @click="handleSort">分数
            <div>
              <span>
                {{ sortOrder === 'asc' ? '↑' : '↓' }}
              </span>
            </div>
          </th>
          <th>操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in response" :key="index" @click="toggleSelect(item.sno,item.cno)">
          <td>
            <input
                type="checkbox"
                :checked="selectedSnoRows.includes(item.sno)&&selectedCnoRows.includes(item.cno)"
                @change="toggleSelect(item.cno,item.cno)"
            />
          </td>
          <td>{{ item.sno || "-" }}</td>
          <td>{{ item.cno || "-" }}</td>
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
    </div>

    <!--    添加学生的弹窗-->
    <!-- 弹窗容器 -->
    <div class="modal" v-if="showAdd">
      <div class="modal-content">
        <!-- 弹窗标题 -->
        <div class="modal-header">
          <h2>添加课程</h2>
          <button class="close-btn" @click="showAdd = false">&times;</button>
        </div>

        <!-- 表单内容 -->
        <div class="form-group">
          <label>学号</label>
          <input type="text" v-model="sc.sno" placeholder="请输入学号">
        </div>

        <div class="form-group">
          <label>课程号</label>
          <input type="number" v-model="sc.cno" placeholder="请输入课程号">
        </div>


        <div class="form-group">
          <label>分数</label>
          <input type="number" v-model="sc.grade" min="0" max="100" placeholder="请输入分数">
        </div>


        <!-- 操作按钮 -->
        <div class="modal-footer">
          <button class="btn cancel-btn" @click="showAdd = false">取消</button>
          <button class="btn confirm-btn" @click="addSc">确认添加</button>
        </div>
      </div>
    </div>

    <!-- 编辑弹窗 -->
    <div v-if="showEditDialog" class="dialog">
      <div class="dialog-content">
        <h3>修改学生信息</h3>
        <form @submit.prevent="saveEdit">
          <label>学号：<input v-model="editingItem.sno"/></label>
          <label>课程号：<input v-model="editingItem.cno"/></label>
          <label>分数：<input type="number" v-model="editingItem.grade"/></label>
          <button type="submit">保存</button>
          <button @click="showEditDialog = false">取消</button>
        </form>
      </div>
    </div>

  </div>
</template>

<script setup>
import {ref, computed} from "vue";
import axios from "axios";
import "@/style/seachStudent.css"

// 响应式数据
const oldsno = ref("");

const oldcno = ref("");

const response = ref("");

const loading = ref(false);

const selectedSnoRows = ref([]); // 选中的行(存储sno）

const selectedCnoRows = ref([]);//  选中的行(存储cno）)

const showEditDialog = ref(false); // 是否显示编辑弹窗

const editingItem = ref({
  sno: "",
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
    selectedCnoRows.value = response.value.map((item) => item.cno);
  } else {
    selectedSnoRows.value = [];
    selectedCnoRows.value = [];
  }
};

// 单选切换
const toggleSelect = (sno, cno) => {
  const index1 = selectedSnoRows.value.indexOf(sno);
  const index2 = selectedCnoRows.value.indexOf(cno);
  if (index1 > -1 && index2 > -1) {
    selectedSnoRows.value.splice(index1, 1);
    selectedCnoRows.value.splice(index2, 1);
  } else {
    selectedSnoRows.value.push(sno);
    selectedCnoRows.value.push(cno);
  }
};

// 删除条目
function deleteItem() {
  if (confirm("确定删除所选学生吗？")) {
    axios
        .delete("http://localhost:8080/deleteSc", {
          data: {
            snoList: selectedSnoRows.value,
            cnoList: selectedCnoRows.value,// 因为删除时，sno和cno是唯一的，所以可以这样写
          },
          headers: {
            "Content-Type": "application/json", // 明确 JSON 格式
          },
        })
        .then((res) => {
          console.log("删除成功:", res.data);
          searchSc();
          // 可选：刷新数据或更新界面
        })
        .catch((err) => {
          console.error("删除失败:", err.response?.data || err.message);
        });
  }
}

// 打开编辑弹窗
const openEditDialog = (item) => {
  editingItem.value = JSON.parse(JSON.stringify(item)); // 使用深拷贝
  oldsno.value = item.sno;
  oldcno.value = item.cno;
  console.log(oldcno.value);
  showEditDialog.value = true;
};

// 保存修改
function saveEdit() {
  console.log("传入老sno：" + oldsno.value);
  console.log("传入老cno：" + oldcno.value);
  axios
      .put("http://localhost:8080/reviseSc", editingItem.value, {
        headers: {
          "Content-type": "application/json",
        },
        params: {
          oldsno: oldsno.value,
          oldcno: oldcno.value,//因为主键是sno和cno，所以可以这样写
        },
      })
      .then(() => {
        console.log("修改成功", editingItem.value);
        searchSc(); //刷新页面
      })
      .catch((err) => {
        console.log("修改失败", err.response?.data || err.message);
        console.log("老sno：" + oldcno.value);
      });
  showEditDialog.value = false; //关闭弹窗
}


//添加学生功能
const sc = ref({
  sno: "",
  cno: "",
  grade: "",
})
const showAdd = ref(false);

async function addSc() {
  if (sc.value.sno === "" || sc.value.cno === "") {
    alert("请至少输入学号、课程号！");
    return;
  }

  try {
    // 添加 await 等待请求完成
    await axios.put("http://localhost:8080/addSc", sc.value, {
      headers: {
        "Content-Type": "application/json",
      },
    });

    showAdd.value = false; // 请求成功后才关闭弹窗
    searchSc();            // 刷新数据
    alert("添加成功");
  } catch (err) {
    // 捕获请求失败（网络错误、400/500 状态码）
    alert("添加失败: " + (err.response?.data || err.message));
  }
}


//   分页功能
const sno = ref("")
const cno = ref("")
const grade = ref("")
const Current = ref(1)
const Size = ref(5)
const total = ref(0);
var totalPages = ref(0);
const sortOrder = ref("asc");

function handleSort() {
  sortOrder.value = sortOrder.value === "asc" ? "desc" : "asc";
  searchSc();//刷新页面
}

// 修改搜索方法
function searchSc() {
  loading.value = true;

  // 应该先获取分页数据再获取总数
  axios.all([
    axios.get("http://localhost:8080/searchSc", {
      params: {
        sno: sno.value,
        cno: cno.value,
        grade: grade.value,
        sortOrder: sortOrder.value, // 新增排序顺序参数
        current: Current.value,
        size: Size.value,
      }
    }),
    axios.get("http://localhost:8080/ScgetTotals", {
      params: {
        sno: sno.value,
        cno: cno.value,
        grade: grade.value,
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
    searchSc();
  }
};

const nextPage = () => {
  if (Current.value < totalPages.value) {
    Current.value++;
    searchSc();
  }
};

const handlePageSizeChange = () => {
  Current.value = 1; // 重置到第一页
  searchSc();
};
</script>
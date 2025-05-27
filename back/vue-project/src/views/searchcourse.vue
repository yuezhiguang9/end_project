<template>
  <div class="container">
    <!-- 查询条件区域 -->
    <div class="search-box">
      <div class="input-group">
        <label>课程号：</label>
        <input id="cno" type="text" v-model="cno" placeholder="请输入课程号" />
      </div>

      <div class="input-group">
        <label>课程名：</label>
        <input id="cname" type="text" v-model="cname" placeholder="请输入课程名" />
      </div>
      <div class="input-group">
        <label>学分：</label>
        <input id="cpon" type="text" v-model="cpon" placeholder="请输入学号" />
      </div>
      <button @click="searchcourse" :disabled="loading" class="search-btn">
        {{ loading ? "查询中..." : "开始查询" }}
        <span v-if="loading" class="loading-icon">⌛</span>
      </button>

      <!--      添加学生按钮-->
      <button @click="showAdd = true" class="search-btn">
        添加课程
      </button>

      <button @click="deleteItem" class="search-btn" v-show="selectedRows.length>0">
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
          <th><input type="checkbox" :checked="allSelected" @change="toggleAll" /></th>
          <th>课程号</th>
          <th>课程名</th>
          <th>学分</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="(item, index) in response" :key="index" @click="toggleSelect(item.cno)">
          <td>
            <input
                type="checkbox"
                :checked="selectedRows.includes(item.cno)"
                @change="toggleSelect(item.cno)"
            />
          </td>
          <td>{{ item.cno || "-" }}</td>
          <td>{{ item.cname || "-" }}</td>
          <td>{{ item.cpon || "-" }}</td>
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
          <label>课程号</label>
          <input type="text" v-model="course.cno" placeholder="请输入课程号">
        </div>

        <div class="form-group">
          <label>课程名</label>
          <input type="text" v-model="course.cname" placeholder="请输入课程名">
        </div>


        <div class="form-group">
          <label>学分</label>
          <input type="number" v-model="course.cpon" min="1" max="100" placeholder="请输入学分">
        </div>


        <!-- 操作按钮 -->
        <div class="modal-footer">
          <button class="btn cancel-btn" @click="showAdd = false">取消</button>
          <button class="btn confirm-btn" @click="addCourse">确认添加</button>
        </div>
      </div>
    </div>

    <!-- 编辑弹窗 -->
    <div v-if="showEditDialog" class="dialog">
      <div class="dialog-content">
        <h3>修改学生信息</h3>
        <form @submit.prevent="saveEdit">
          <label>课程号：<input v-model="editingItem.cno" /></label>
          <label>课程名：<input v-model="editingItem.cname" /></label>
          <label>学分：<input type="number" v-model="editingItem.cpon" /></label>
          <button type="submit">保存</button>
          <button @click="showEditDialog = false">取消</button>
        </form>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import axios from "axios";
import "@/style/seachStudent.css"

// 响应式数据
const oldcno = ref("");
const response = ref("");
const loading = ref(false);
const selectedRows = ref([]); // 选中的行（存储课程号）
const showEditDialog = ref(false); // 是否显示编辑弹窗
const editingItem = ref({
  cno: null,
  cname: null,
  cpon: null,
}); // 正在编辑的数据

// 计算属性：是否全选
const allSelected = computed(() => {
  return selectedRows.value.length === response.value.length;
});

// 全选/取消全选
const toggleAll = (e) => {
  if (e.target.checked) {
    selectedRows.value = response.value.map((item) => item.cno);
  } else {
    selectedRows.value = [];
  }
};

// 单选切换
const toggleSelect = (cno) => {
  const index = selectedRows.value.indexOf(cno);
  if (index > -1) {
    selectedRows.value.splice(index, 1);
  } else {
    selectedRows.value.push(cno);
  }
};

// 删除条目
function deleteItem() {
  if (confirm("确定删除所选学生吗？")) {
    axios
        .delete("http://localhost:8080/deleteCourse", {
          data: selectedRows.value, // 直接传递数组
          headers: {
            "Content-Type": "application/json", // 明确 JSON 格式
          },
        })
        .then((res) => {
          console.log("删除成功:", res.data);
          searchcourse();
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
  oldcno.value = item.cno;
  console.log(oldcno.value);
  showEditDialog.value = true;
};

// 保存修改
function saveEdit() {
  console.log("传入老sno：" + oldcno.value);
  axios
      .put("http://localhost:8080/reviseCourse", editingItem.value, {
        headers: {
          "Content-type": "application/json",
        },
        params: {
          oldcno: oldcno.value,
        },
      })
      .then(() => {
        console.log("修改成功", editingItem.value);
        searchcourse(); //刷新页面
      })
      .catch((err) => {
        console.log("修改失败", err.response?.data || err.message);
        console.log("老sno：" + oldcno.value);
      });
  showEditDialog.value = false; //关闭弹窗
}


//添加学生功能
const course = ref({
  cno:"",
  cname:"",
  cpon:"",
})
const showAdd = ref(false);
function addCourse(){
  if(course.value.cno===""||course.value.cname===""){
    alert("请至少输入课程号、课程名两个！！！！！");
  }
  else{
    try {
      axios.put("http://localhost:8080/addCourse",course.value,{
        headers:{
          "Content-Type":"application/json",
        }
      });
      showAdd.value = false;//关闭弹窗
      searchcourse();
      alert("添加成功")
    }catch (err){
      alert("添加失败",err)
    }
  }
}



//   分页功能
const cno=ref("")
const cname=ref("")
const cpon=ref("")
const Current=ref(1)
const Size=ref(5)
const total = ref(0);
var totalPages = ref(0);

// 修改搜索方法
function searchcourse() {
  loading.value = true;

  // 先获取总数
  axios.get("http://localhost:8080/getTotals", {
    params: {
      cno:cno.value,
      cname:cname.value,
      cpon:cpon.value,
    },
  }).then((res) => {
    const data = res.data;
    if(Array.isArray(data)){
      total.value = data.length; // 总记录数
      totalPages.value = Math.ceil(data.length / Size.value); //
      console.log("totalPages"+totalPages.value)
    }
  });

  // 再获取分页数据
  axios.get("http://localhost:8080/searchcourse", {
    params: {
      cno:cno.value,
      cname:cname.value,
      cpon:cpon.value,
      current:Current.value,
      size:Size.value,
    }
  }).then((res) => {
    response.value = res.data.records;
    console.log("response:",response.value);
  }).catch((err) => {
    console.log(err)
  }).finally(() => {
    loading.value = false;
  });
}

// 新增分页方法
const prevPage = () => {
  if (Current.value > 1) {
    Current.value--;
    searchcourse();
  }
};

const nextPage = () => {
  if (Current.value < totalPages.value) {
    Current.value++;
    searchcourse();
  }
};

const handlePageSizeChange = () => {
  Current.value = 1; // 重置到第一页
  searchcourse();
};
</script>
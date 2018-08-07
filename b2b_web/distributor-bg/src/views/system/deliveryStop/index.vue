<template>
  <div class="table-list">
    <el-row class="header">
      <el-col :span="2">停发列表</el-col>
    </el-row>
    <div class="function">
      <button class="mini-search-btn" @click="handleAdd()">添加</button>
    </div>
    <div v-loading="loading">
      <el-table
        :data="tableDatas"
        header-row-class-name="header-row"
        border
        class="tr-header"
        max-height="550"
      >
        <el-table-column
          label="序号"
          prop="id"
          align="center"
          :width="100"
        ></el-table-column>
        <el-table-column label="地点名称" prop="placeName" align="center">
          <template slot-scope="scope">
            <input
              class="input-edit"
              v-model="scope.row.placeName"
              placeholder="请输入"
              v-show="scope.row.isEdit"
            />
            <span v-show="!scope.row.isEdit">{{ scope.row.placeName }}</span>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" align="center">
        </el-table-column>
        <el-table-column label="更新时间" prop="updateTime" align="center">
          <template slot-scope="scope">
            <span>{{ scope.row.updateTime || "--" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" :width="220" align="center">
          <template slot-scope="scope">
            <el-button
              class="mini-search-btn"
              @click="handleSave(scope.$index)"
              style="background-color: #21b8cb"
              v-if="scope.row.isEdit"
              >保存</el-button
            >
            <button
              class="mini-search-btn"
              @click="handleEdit(tableDatas[scope.$index])"
              v-else
            >
              编辑
            </button>
            <el-button
              size="mini"
              @click="handleCancel(scope.$index)"
              v-if="scope.row.isEdit"
              >取消</el-button
            >
            <el-button
              v-else
              class="mini-delete-btn"
              @click="handleDelete(scope.$index)"
              >删除</el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <page
        :page="pageInfo.page"
        :total="total"
        @sizeChange="sizeChange"
        @currentChange="currentChange"
      ></page>
    </div>

    <!-- 添加弹窗 -->
    <el-dialog
      title="添加地点名称"
      :modal-append-to-body="false"
      :visible="dialogShow"
      :width="'500px'"
      :before-close="handleClose"
    >
      <input
        class="input-edit"
        v-model="newPlaceName"
        placeholder="请输入地点名称"
      />
      <div class="btn-footer">
        <el-button class="mini-search-btn check_btn" @click="handleConfirm"
          >确定</el-button
        >
        <el-button class="check_back_btn" size="mini" @click="handleClose"
          >返回</el-button
        >
      </div>
    </el-dialog>
  </div>
</template>

<script>
import page from "@/components/pagination";
export default {
  name: "deliveryStopList",
  components: { page },
  data() {
    return {
      tableDatas: [],
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
      },
      total: 0,
      dialogShow: false,
      newPlaceName: "",
    };
  },
  mounted() {
    this.initData();
  },
  methods: {
    // 添加
    handleAdd() {
      this.dialogShow = true;
    },
    // 初始化表格数据
    initData() {
      this.loading = true;
      this.$http.getDeliveryStopList(this, this.pageInfo).then((res) => {
        this.tableDatas.splice(0, this.tableDatas.length);
        if (res.success) {
          res.data.list.forEach((item) => {
            this.tableDatas.push({
              id: item.id,
              placeName: item.placeName,
              createTime: item.createTime,
              updateTime: item.updateTime,
              isEdit: false,
            });
          });
          this.total = res.data.total;
        }
        res.success ? (this.loading = false) : (this.looking = false);
      });
    },
    sizeChange(size) {
      this.pageInfo.size = size;
      this.initData();
    },
    currentChange(page) {
      this.pageInfo.page = page;
      this.initData();
    },
    // 删除
    handleDelete(index) {
      if (this.tableDatas[index].id != undefined) {
        this.$confirm("该操作会删除此停发地点，是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
          center: true,
        }).then(() => {
          this.$http
            .deleteDeliveryStop(this, { id: this.tableDatas[index].id })
            .then((res) => {
              if (res.success) {
                this.$message.success({
                  message: "删除成功",
                  duration: 3 * 1000,
                  onClose: () => {},
                });
                this.tableDatas.splice(index, 1);
              } else {
                this.$confirm(item.id + "此停发地点删除失败", "提示", {
                  center: true,
                  showClose: false,
                  showCancelButton: false,
                });
              }
            });
        });
      } else {
        this.tableDatas.splice(index, 1);
      }
    },
    // 保存
    handleSave(index) {
      if ((index === "add" && !this.newPlaceName) || (index !== "add" && !this.tableDatas[index].placeName)) {
        this.$message.error("请先输入停发地点！");
      } else {
        if (index === "add") {
          const params = {
            placeName: this.newPlaceName,
          };
          this.$http.addDeliveryStop(this, params).then((res) => {
            if (res.success) {
              this.$message.success("新增成功！");
              this.handleClose();
              this.initData();
            }
          });
        } else {
          const params = {
            id: this.tableDatas[index].id,
            placeName: this.tableDatas[index].placeName,
          };
          this.$http.editDeliveryStop(this, params).then((res) => {
            if (res.success) {
              this.$message.success("保存成功！");
              this.initData();
            }
          });
        }
      }
    },
    // 取消
    handleCancel(index) {
      this.tableDatas[index].isEdit = false;
      if (this.tableDatas[index].id == undefined) {
        this.tableDatas.splice(index, 1);
      } else {
        this.initData();
      }
    },
    // 编辑
    handleEdit(obj) {
      let editLen = 0;
      this.tableDatas.forEach((item) => {
        if (item.isEdit) {
          editLen++;
        }
      });
      if (editLen <= 0) {
        obj.isEdit = true;
      } else {
        this.$message.error("请先保存编辑中的地址名称！");
      }
    },
    handleConfirm() {
      this.handleSave("add");
    },
    handleClose() {
      this.dialogShow = false;
      this.newPlaceName = "";
    }
  },
};
</script>

<style lang="scss" scoped>
.table-list {
  background-color: white;
  min-height: 100%;
  .header {
    background-color: $lakeBlue;
    line-height: 40px;
    color: white;
    padding-left: 20px;
  }
}
.function {
  padding-left: 20px;
  padding-bottom: 10px;
  padding-top: 10px;
  background-color: white;
  .btn-delete {
    color: #fff;
    background-color: #e6a23c;
    border-radius: 6px;
  }
  .btn-add {
    color: #fff;
    background-color: #21b8cb;
    border-radius: 6px;
  }
}

.input-edit {
  padding: 0 15px;
  height: 30px;
  font-size: 14px;
  border-radius: 5px;
  border: 1px solid #ccc;
  font-weight: bold;
  width: 100%;
}

.btn-footer {
  margin-top: 20px;
  text-align: center;
}
</style>
<style>
.el-input__suffix {
  right: 14px;
}
</style>




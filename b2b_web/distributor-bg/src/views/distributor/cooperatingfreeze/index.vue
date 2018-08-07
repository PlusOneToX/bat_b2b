<template>
    <div class="category-list">
      <header>
        <h4>已冻结的分销商列表</h4>
      </header>

      <div class="function">
        <div class="Fheader">
          <div class="Fsearch">
            <el-select size="mini" change="el-select"  v-model="pageInfo.contentType" placeholder="分销商用户名" style="width: 140px;" clearable>
              <el-option label="分销商用户名" value="1"> </el-option>
              <el-option label="客户名称" value="2"> </el-option>
              <el-option label="分销商ID" value="3"> </el-option>
              <el-option label="手机号" value="5"> </el-option>
            </el-select>
            <el-input size="mini" v-model.trim="mytext"  placeholder="请输入关键词搜索" class="box-input" @change="contentChange" @keyup.enter.native="Csearch()" clearable></el-input>
            <el-button class="mini-search-btn box-btn" @click="Csearch()">搜索</el-button>
          </div>
        </div>
      </div>
      <el-row>
        <el-table :data="tableData" header-row-class-name="header-row" border @selection-change="handleSelectionChange" class="tableCenter" ref="multipleTable" v-loading="loading" :height="tableHeight">
          <el-table-column align="center" sortable label="冻结时间" prop="freezeTime" :formatter="formatTime" :min-width="150"></el-table-column>
          <el-table-column align="center" label="分销商用户名" prop="name" :min-width="120"></el-table-column>
          <!-- <el-table-column align="center" label="分销商等级" prop="gradeName" :min-width="120"></el-table-column> -->
          <!-- <el-table-column align="center" label="分销商区域" prop="salesAreaNames" :min-width="120"></el-table-column> -->
          <el-table-column align="center" label="分销商备注" prop="comment" :min-width="120"></el-table-column>
          <el-table-column align="center" label="操作" :min-width="150">
            <template slot-scope="scope">
              <el-button class="mini-freeze-btn" @click="handleThaw(scope.$index, scope.row)">解冻</el-button>
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
              <el-button type="danger" size="mini" @click="handleDelete(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
      </div>
</template>

<script type="text/javascript">
import page from "@/components/pagination";
import { timeFormat } from "@/utils/timeFormat.js";
export default {
  name: "Dcooperatingfreeze",
  components: { page },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        freezeStatus: 2,
        content: this.mytext,
        contentType: ''
      },
      total: 0,
      tableData: [],
      mytext: "",
      formData: { name: "", description: "" },
    };
  },
  created() {
    const documentHeight = document.body.clientHeight;
    this.tableHeight = parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
  activated() {
    this.dataForamt();
  },
  methods: {
    // ======== 操作 ========
    Csearch() { // 搜索操作
      this.pageInfo.content = this.mytext;
      this.pageInfo.status = this.status;
      this.dataForamt();
    },
    contentChange (val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },
    handleEdit(index, row) { // 查看操作
      this.$router.push({ name: 'distributorcooperatingadd', query:{ id: row.id, checkMsg: 4,node: 1}})
    },

    handleDelete(row) { // 删除操作
      this.$confirm('此操作将永久删除该改冻结分销商，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.$http.distributorDelete(this, { id: row.id}).then(res => {
          if (res.success) {
            this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => {
              }
            });
            this.pageInfo.page = 1
            this.dataForamt();
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    // ======== 数据 ========
    dataForamt() { // 详情
      this.loading = true;
      this.$http.getDistributorFList(this, this.pageInfo).then(res => {
        if(res.success) {
          let ary = [];
          res.data.list.forEach(item => {
            item.children = [];
            ary.push(item);
          });
          ary.sort((a, b) => {
            return a.sort - b.sort > 0;
          });
          this.tableData = ary;
          this.total = res.data.total
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },

    sizeChange(size) { // 分页页数
      this.pageInfo.size = size;
      this.dataForamt();
    },
    
    currentChange(page) { // 分页总数
      this.pageInfo.page = page;
      this.dataForamt();
    },

    handleSelectionChange(val) { // table选中的数据值
      this.multipleTable = val;
    },
    // ======== 转换 ========
    formatTime(row, col, val) { // 时间格式化
      return timeFormat(val);
    },
    // 解冻按钮
    handleThaw(index, row) {
      this.$confirm('此操作将解冻该分销商，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
      }).then(_ => {
        this.$http.distributorStatus(this, { id: row.id, freezeStatus: 1 }).then(res => {
          if (res.success) {
             this.$message({
                message: '成功解冻',
                type: 'success',
                duration: 3 * 1000,
                onClose: () => {
                }
            })
            this.pageInfo.page = 1
            this.dataForamt();
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消解冻'
        })
      })  
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.el-table .cell {
  overflow: hidden;
  white-space:nowrap;
}
.category-list {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    h4 {
      display: inline-block;
      font-weight: 350;
      font-size: 16px;
      margin: 0 20px;
    } 
  }
}
.function {
  background-color: white;
  .Fheader {
    overflow: hidden;
    display: inline-flex;
    margin: 10px;
    display: flex !important;
    justify-content: flex-end !important;
    align-items: center !important;
    .Fsearch {
      display: inherit;
      .el-select{
        /deep/.el-input__inner{
          width: 130px;
          border-top-right-radius: 0;
          border-bottom-right-radius: 0;
        }
      }
      .box-input {
        margin-right: 5px;
        /deep/.el-input__inner{
          border-top-left-radius: 0;
          border-bottom-left-radius: 0;
          border-left-width: 0;
        }
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
  }
}
</style>

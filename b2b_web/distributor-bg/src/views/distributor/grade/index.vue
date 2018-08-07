<template>
  <div class="category-list">
    <header>
      <h4>销售区域列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="addpuls">
        添加销售区域
      </el-button>
    </header>

    <div class="function">
      <div class="Fheader">
          <el-select class="choose" @change="handleStatus" v-model="states" placeholder="请选择" size="mini" clearable>
            <el-option v-for="item in state" :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
          <div class="f-search">
            <el-input v-model.trim="mytext" placeholder="销售区域名称" size="mini" class="box-search" @keyup.enter.native="Csearch(states)"></el-input>
            <button class="mini-search-btn box-btn" @click.prevent="Csearch(states)">搜索</button>
          </div>
      </div>
      <el-row v-loading="loading">
        <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" :height="tableHeight">
          <el-table-column align="center" label="销售区域名称" prop="name" :min-width="120"></el-table-column>
          <el-table-column align="center" label="区域描述" prop="description" :min-width="120"></el-table-column>
          <el-table-column align="center" label="状态" :min-width="120">
            <template slot-scope="scope">
              {{scope.row.openFlag == 1 ? '启用':'停用'}}
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作" :min-width="170">
            <template slot-scope="scope">
              <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
              <el-button class="mini-freeze-btn" @click="handleStop(scope.row)" v-if="scope.row.openFlag == 1" :loading="loading">停用</el-button>
              <el-button class="mini-tableadd-btn" @click="handleStart(scope.row)" v-else>启用</el-button>
              <el-button class="mini-delete-btn" @click="handleDelete(scope.row)" v-if="scope.row.openFlag == 0">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
    </div>
  </div>
</template>

<script type="text/javascript">
import treeTable from "@/components/TreeTable";
import page from "@/components/pagination";
export default {
  name: "distributorlist",
  components: { treeTable, page },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      checkdDistributor: null,
      states: undefined,
      pageInfo: {
        page: 1,
        size: 10
      },
      total: 0,
      tableData: [],
      state: [
        { value: '1', label: '启用' },
        { value: '0', label: '停用' }
      ],
      mytext: "",
      formData: {
        name: "",
        description: "",
        id: ""
      },
      rules: {
        name: [{ required: true, message: "请输入销售区域名称", trigger: "blur" }]
      },
      editedAccount: {}
    }
  },
  created() {
    const documentHeight  =  document.body.clientHeight;
    this.tableHeight  =  parseInt(documentHeight  *  0.8  -  100); // 计算表高度，固定表头
  },
  activated() {
    this.dataForamt();
  },
  methods: {
    handleStatus (val) {
      if (val==='' || val===undefined || val===null) {
        this.states = undefined
      }
    },    
    // ======== 操作 ========
    addpuls() { // 添加销售区域
      this.$router.push({name: 'distributorgraderedact'})
    },

    handleEdit(index, row) { // 查看操作
      this.$router.push({ name: 'distributorgraderedact',query: {id: row.id}})
    },
    
    handleStop(row) { // 停用操作
      this.$confirm('此操作将停用该销售区域，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.loading = true
        this.$http.salesareaStatus(this, { id: row.id, openFlag: 0 }).then(res => {
          if (res.success) {
            this.$message.success({
              message: "停用成功",
              duration: 3 * 1000,
              onClose: () => {
              }
            })
             this.dataForamt()
          }
          this.loading = false
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消停用'
        })
      })
    },
    
    handleStart(row) { // 启用操作
      this.$confirm('此操作将启用该销售区域，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        center: true
      }).then(_ => {
        this.$http.salesareaStatus(this, { id: row.id, openFlag: 1 }).then(res => {
          if (res.success) {
            this.dataForamt()
          }
        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消启用'
        })
      })  
    },

    handleDelete(row) { // 删除操作
      this.$confirm('此操作将永久删除该销售区域，是否继续？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        // deleteData(this, { id: row.id }).then(res => {
        //   if (res.code == 0) {
        //     this.$message.success({
        //       message: "删除成功",
        //       duration: 3 * 1000,
        //       onClose: () => {
        //       }
        //     });
        //     this.dataForamt();
        //   }
        // })
        this.$http.deleteSalesarea(this, { id: row.id }).then(res => {
            if (res.success) {
              this.$message.success({
              message: "删除成功",
              duration: 3 * 1000,
              onClose: () => {
              }
            });
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

    Csearch(states) { // 搜索操作
      this.pageInfo.page = 1
      this.pageInfo.content = this.mytext;
      this.pageInfo.openFlag = this.states;
      this.dataForamt(states);
    },
    
    // ======== 数据 ========
    dataForamt(states) { // 详情
      this.loading = true;
      this.pageInfo.openFlag = states
      this.pageInfo.content = this.mytext;
      this.$http.getSalesareaList(this, this.pageInfo).then(res => {
          if (res.success) {
            let ary = [];
            res.data.list.forEach(item => {
              item.children = []
              ary.push(item)
            })
            ary.sort((a, b) => {
              return a.sort - b.sort > 0
            })
            this.tableData = ary
            this.total = res.data.total
            this.loading = false
          } else {
            this.looking = false
          }
        })
    },

    sizeChange(size) { // 分页页数
      this.pageInfo.count = size;
      this.dataForamt();
    },
    
    currentChange(page) { // 分页总数
      this.pageInfo.page = page;
      this.dataForamt();
    }    
  },
  watch: {
    states: function() { // 启用停用
      if(this.states == 1) {
        this.pageInfo.page = 1
        this.dataForamt(1);
      }else if(this.states == 0) {
        this.pageInfo.page = 1
        this.dataForamt(0);
      }else {
        this.pageInfo.page = 1
        this.dataForamt()
      }
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
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
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
  }
  .function {
    background-color: white;
    .choose {
      position: relative;
    }
    .Fheader {
      width: 98%;
      margin: 10px auto;
      display: flex !important;
      justify-content: space-between !important;
      align-items: center !important;
      overflow: hidden;
      .f-search {
        float: right;
        .box-search {
          width: 140px;
        }
        .box-btn {
          position: relative;
          top: -1px;
        }
      }
    }
  }
}
</style>

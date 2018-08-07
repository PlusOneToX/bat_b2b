<template>
  <div class="role-list">
    <header>
      <h4>角色管理</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleAdd()"> 添加角色 </el-button>
    </header>
    <div class="role-header">
      <div class="role-block">
        <el-input class="box-input" size="mini" @change="contentChange" @keyup.enter.native="filter()" placeholder="角色名称" v-model.trim="name" clearable></el-input>
        <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
      </div>
    </div>
    <el-table :data="tableData" border style="width:100%"  v-loading="loading" header-row-class-name="header-row" class="tableCenter">
      <el-table-column align="center" label="角色编号" prop="id" show-overflow-tooltip width="100"></el-table-column>
      <el-table-column align="center" label="角色名称" prop="roleName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="角色描述" prop="roleDescription" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="操作" fixed="right" width="200">
        <template slot-scope="scope">
          <el-button class="mini-search-btn" @click="handleEdit(scope.$index, scope.row)">查看</el-button>
          <el-button class="mini-delete-btn" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <page :page="pageInfo.page" :total="total" @sizeChange="sizeChange" @currentChange="currentChange"></page>
  </div>
</template>

<script>
import page from '@/components/pagination'
export default {
  name: 'rolelist',
  data() {
    return {
      tableData: [],
      tableHeadData: [],
      total: 0,
      pageInfo: {
        page: 1,
        size: 10,
      },
      loading: false,
      name:''
    }
  },
  components: {
    page,
  },
  created() {
    this.init()
  },
  methods: {
    // ======== 操作 ========
    filter(){ // 搜索操作
      if(this.name.trim() === ''){
        this.pageInfo.name = undefined
      }else{
        this.pageInfo.name = this.name
      }
      this.pageInfo.page = 1
      this.init()
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filter()
      }
    },
    handleEdit(index, row) { // 查看操作
      this.$router.push({ name: 'roleedit',params:{id:row.id} })
    },

    handleAdd() { // 添加操作
      this.$router.push({ name: 'roleadd'});
    },
    
    handleDelete(index, row) { // 删除操作
      this.$confirm('此操作将永久删除该角色, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.deleteRole(this, { id: row.id }).then(res => {
          if(res.success){
            this.$message({
              message: '删除成功',
              type: 'success',
              duration: 3 * 1000,
            })
            this.pageInfo.page = 1;
            this.init()
          }
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },
    
    // ======== 数据 ========
    init() { // 列表数据
      this.loading = true;
      this.$http.getRoleList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
          this.loading = false
        } else {
          this.looking = false
        }
      })
    },

    sizeChange(size) {
      this.pageInfo.size = size;
    },

    currentChange(page) {
      this.pageInfo.page = page;
    },
  },
  watch: {
    pageInfo: {
      handler() {
        this.init()
      },
      deep: true
    },
    tableHead() {
      this.tableHeadInit()
    }

  }
}

</script>
<style rel="stylesheet/scss" lang="scss" >
.role-list {
  background-color: white;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  } 
  .header {
    height: 50px;
    line-height: 50px;
    border-bottom: 1px solid #ccc;
    button {
      margin-left: 10px;
    }
  }
  .role-header {
    width:100%;
    overflow: hidden;
    .role-block {
      float: right;
      margin: 10px 10px;
      .box-input {
        width:140px;
      }
      .box-btn {
        position: relative;
        top: -1px;
      }
    }
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

</style>

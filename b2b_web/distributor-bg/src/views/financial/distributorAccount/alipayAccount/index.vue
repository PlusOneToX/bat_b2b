<template>
  <div class="alipayAccount-list">
    <header>
      <h4>支付宝账户列表</h4>
      <el-button class="mini-add-btn btn-home" icon="el-icon-plus" @click="handleAdd()"> 添加支付宝账户 </el-button>
    </header>
    <div class="alipayAccount-header">
      <div class="alipayAccount-block">
        <el-select
          class="content_select"
          placeholder="选择类型"
          size="mini"
          style="width:140px;"
          v-model="pageInfo.contentType"
          clearable
        >
          <el-option
            v-for="item in contentTypes"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          ></el-option>
        </el-select>
        <el-input class="box-input" size="mini" clearable @change="contentChange" placeholder="用户名/分销商公司名/应用名称/AppId" v-model.trim="pageInfo.content"></el-input>
        <button class="mini-search-btn box-btn" size="mini" @click.prevent="filter()">搜索</button>
      </div>
    </div>
    <el-table :data="tableData" border style="width:100%"  v-loading="loading" header-row-class-name="header-row" class="tableCenter">
      <el-table-column align="center" label="用户名" prop="distributorName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="分销商公司名" prop="distributorCompanyName" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="AppId" prop="appId" show-overflow-tooltip></el-table-column>
      <el-table-column align="center" label="应用名称" prop="accountName" show-overflow-tooltip></el-table-column>
      <!-- <el-table-column align="center" label="是否公司账户" prop="company" :formatter="formatStatus" show-overflow-tooltip></el-table-column> -->
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
        userId: 10000,
        contentType: undefined,
        content: undefined
      },
      loading: false,
      content:'',
      contentTypes: [
        { value: 4, label: '用户名' },
        { value: 5, label: '分销商公司名' },
        { value: 1, label: '应用名称' },
        { value: 3, label: 'AppId' },
      ]
    }
  },
  components: {
    page,
  },
  created() {
    this.init()
  },
  methods: {
    formatStatus(row, col, val) {
      // 是否公司账户
      switch (val) {
        case 0:
          return "否";
          break;
        case 1:
          return "是";
          break;
      }
    },
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.init()
      }
    },
    // ======== 操作 ========
    filter(){ // 搜索操作
      this.pageInfo.page = 1
      this.init()
    },

    handleEdit(index, row) { // 查看操作
      this.$router.push({ name: 'editAddAlipay',query: {id:row.appId} })
    },

    handleAdd() { // 添加操作
      this.$router.push({ name: 'editAddAlipay'});
    },
    
    handleDelete(index, row) { // 删除操作
      this.$confirm('此操作将永久删除该支付宝账户, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(() => {
        this.$http.delAccountAlipay(this, { id: row.id }).then(res => {  
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
      this.$http.accountAlipayList(this, this.pageInfo).then(res => {    
        if(res.success){
          this.tableData = res.data.list
          this.total = res.data.total
        }
        this.loading = false
      })
    },

    sizeChange(size) {
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.init()
    },

    currentChange(page) {
      this.pageInfo.page = page
      this.init()
    },
  },
  watch: {

  }
}

</script>
<style rel="stylesheet/scss" lang="scss" >
.alipayAccount-list {
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
  .alipayAccount-header {
    width:100%;
    overflow: hidden;
    .alipayAccount-block {
      float: right;
      margin: 10px 10px;
      .box-input {
        width:300px;
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

<template>
    <div class="category-list">
      <header>
        <h4>分账业务员列表</h4>
      </header>

      <div class="function">
        <div class="Fheader">
          <div class="Fleft"></div>
          <div class="Fsearch">
            <button class="mini-search-btn box-btn" @click="Csearch()">搜索</button>
            <el-input v-model.trim="pageInfo.content" size="mini" clearable @change="contentChange" @keyup.enter.native="Csearch()" placeholder="请输入关键词搜索" class="box-input"></el-input>
            <el-select size="mini"  v-model="pageInfo.searchType" placeholder="分销商用户名" style="width: 140px;" clearable>
              <el-option label="分销商用户名" value="1"> </el-option>
              <el-option label="业务员姓名" value="2"> </el-option>
              <el-option label="业务员手机号" value="3"> </el-option>
            </el-select>
          </div>
        </div>
      </div>
      
      <el-row v-loading="loading">
        <el-table :data="tableData" header-row-class-name="header-row" border class="tableCenter"  :height="tableHeight">
            <el-table-column align="center" label="归属分销商" prop="distributorName" :min-width="120"></el-table-column>
          <el-table-column  label="业务员名称" align="center" prop="salemanName" :min-width="120"></el-table-column>
            <el-table-column label="类型" align="center" prop="type" :formatter="formatType" :min-width="120"></el-table-column>
            <el-table-column label="手机号" align="center" prop="mobile" :min-width="120"></el-table-column>
            <el-table-column label="级别" align="center" prop="levelName" :min-width="120"></el-table-column>
            <el-table-column label="上级业务员名称" align="center" prop="parentSalemanName" :min-width="140">
              <template slot-scope="scope">
                <div>{{scope.row.parentSalemanName?scope.row.parentSalemanName:'--'}}</div>
              </template>
            </el-table-column>
            <el-table-column label="openid/商户号" align="center" prop="merchantNumber" :min-width="120"></el-table-column>
            <el-table-column label="状态" align="center" prop="openFlag" :formatter="formatStatus" :min-width="120"></el-table-column>
            <el-table-column label="创建时间" align="center" prop="createTime" :min-width="140"></el-table-column>
        </el-table>
        <page :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></page>
      </el-row>
    </div>
</template>

<script type="text/javascript">
import page from '@/components/pagination'
export default {
  components: { page },
  created() {
    const documentHeight = document.body.clientHeight
    this.tableHeight = parseInt(documentHeight * 0.8 - 100) // 计算表高度，固定表头
  },
  activated() {
    this.dataFot()
  },
  data() {
    return {
      tableHeight: 600,
      loading: false,
      pageInfo: {
        page: 1,
        size: 10,
        searchType: undefined,
        content: undefined
      },
      total: 0,
      tableData: []
    }
  },
  methods: {
    contentChange (val){
      if(val === undefined || val === '' || val === null){
        this.Csearch()
      }
    },

    Csearch() { // 搜索操作
      this.pageInfo.page = 1
      this.dataFot()
    },
    // ======== 数据 ========
    dataFot() { // 数据列表
      this.loading = true
      this.$http.subAccountSaleList(this, this.pageInfo).then(res => {
        if (res.success) {
            this.tableData = res.data.list
            this.total = res.data.total
            this.loading = false
        } else {
            this.looking = false
        }
      })
    },

    sizeChange(size) { // 条数
      this.pageInfo.size = size
      this.pageInfo.page = 1
      this.dataFot()
    },

    currentChange(page) { // 页数
      this.pageInfo.page = page
      this.dataFot()
    },
    /****** 状态转换 ******/
    formatType(row, col, val) {
      switch (val) {
        case 1:
          return '企业'
        case 2:
          return '个人'
      }
    },
    formatStatus(row, col, val) {
      switch (val) {
        case 1:
          return '启用'
        case 2:
          return '禁用'
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .function {
    background-color: white;
    .Fheader {
        margin: 10px;
        display: flex !important;
        justify-content: space-between;
        align-items: center !important;
        .Fleft {
            overflow: hidden;
            float: left;
        }
        .Fsearch {
            overflow: hidden;
            float: right;
            .box-input {
                width: 215px;
                float: right;
            }
            .box-btn {
                float: right;
                margin-left: 5px;
            }
            /deep/.el-input{
              .el-input__inner{
                border-top-right-radius: 0;
                border-bottom-right-radius: 0;
              }
            }
            /deep/.box-input{
              .el-input__inner{
                border-top-left-radius: 0;
                border-bottom-left-radius: 0;
                border-left-width: 0;
              }
            }
        }
    }
}
</style>
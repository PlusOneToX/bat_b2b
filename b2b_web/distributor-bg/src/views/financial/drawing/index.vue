<template>
  <div class="draw-cash">
    <header>
      <h4>提现列表</h4>
    </header>

    <div class="main-wrapper">
      <div class="Fheader">
        <el-select v-model="pageInfo.applyStatus" size="mini" style="width:120px" clearable>
          <el-option label="申请中" :value="1"> </el-option>
          <el-option label="已确认" :value="2"> </el-option>
          <el-option label="已拒绝" :value="3"> </el-option>
        </el-select>
        <el-select size="mini" v-model="pageInfo.withdrawAccountType" clearable placeholder="提现方式" style="width:120px;">
          <el-option label="支付宝" :value="1"></el-option>
          <el-option label="微信" :value="2"></el-option>
          <el-option label="银行卡" :value="3"></el-option>
        </el-select>
        <div class="f-search">
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
            <el-input v-model.trim="pageInfo.content" @keyup.enter.native="onSearch()" placeholder="请输入关键词搜索" size="mini" class="box-search"></el-input>
            <button class="mini-search-btn box-btn" @click.prevent="onSearch()">搜索</button>
        </div>
      </div>
    </div>

    <el-row>
      <el-table :data="tableData" border header-row-class-name="header-row" class="tableCenter" max-height="700" v-loading="loading">
        <el-table-column align="center" label="提现单编号" prop="id" :min-width="110">
          <!-- <template slot-scope="scope">
            <el-button type="text" @click="onEdit(scope.row, scope.$index)">
              {{scope.row.id}}
            </el-button>
          </template> -->
        </el-table-column>
        <el-table-column align="center" prop="distributorName" label="分销商用户名"  :min-width="110"></el-table-column>
        <el-table-column  align="center" prop="withdrawAccountType" label="提现方式" :formatter="withdrawTypeFormatter" :min-width="110"></el-table-column>
        <el-table-column align="center" label="提现金额" :min-width="110">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ scope.row.withdrawAmount| NumFormat}}
          </template>
        </el-table-column>
        <!-- <el-table-column align="center" prop="bankName" label="开户行" :min-width="110"></el-table-column>
        <el-table-column align="center" prop="payee" label="开户名" :min-width="110"></el-table-column> -->
        <el-table-column align="center" prop="payee" label="收款账号姓名" :min-width="110"></el-table-column>
        <el-table-column align="center" prop="cardNo" label="收款账号" :min-width="110"></el-table-column>
        <el-table-column align="center" prop="applyStatus" label="提现状态" :formatter="confirmTypeFormatter" :min-width="110"></el-table-column>
        <el-table-column align="center" prop="remark" label="备注" :min-width="110"></el-table-column>
        <!-- <el-table-column align="center" prop="createTime" label="申请时间" :formatter="timeFormatter" :min-width="120"></el-table-column> -->
        <el-table-column align="center" label="操作" fixed="right">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="onEdit(scope.row, scope.$index)" >
              查看
            </el-button>
          </template>
        </el-table-column>  
      </el-table>
      <pagination :total="total" :page="pageInfo.page" @sizeChange="sizeChange" @currentChange="currentChange"></pagination>
    </el-row>

  </div>
</template>

<script>
  import {parseTime} from '@/utils/index'
  import pagination from '@/components/pagination/index'
  import {getWithdrawList, getWithdrawListCount} from '@/views/financial/financialData'
  import {withdrawTypeFormatter, confirmTypeFormatter} from '@/views/financial/financialUtils'
  export default {
    name: 'withdrawDetail',
    data() {
      return {
        tableData: [],
        search:{
          content:'',
        },
        pageInfo: { 
          page: 1,
          size: 10,
          userId: 10000,
          applyStatus: undefined,
          withdrawAccountType: undefined,
          contentType: undefined,
          content: undefined
        },
        total: 0,
        loading: false,
        contentTypes: [
          { value: 1, label: '分销商用户名' },
          { value: 2, label: '提现单编号' }
        ]
      }
    },
    activated() {
      this.updateMainData()
    },
    components: {
      pagination
    },
    methods: {
      // ================ 操作 ================
      onSearch() { // 搜索操作
        const params = {};
        this.pageInfo.page = 1
        this.updateMainData()
        // Object.assign(params, this.pageInfo, this.search); //拼装请求参数

        // getWithdrawListCount(this, params).then(res => {
        //   this.total = res.count
        // })

        // return getWithdrawList(this, params).then(res => {
        //   this.tableData = res.withdraws
        // })
      },
      onEdit(row, index){ // 查看操作
        this.$router.push({ name: 'withdrawDetail', query: {id: row.id,applyStatus: row.applyStatus}})
      },

      // ================ 转换 ================
      timeFormatter(row, column, cellValue){ // 时间格式化
        return parseTime(row.createTime)
      },
      withdrawTypeFormatter(row, column, cellValue){ // 提现方式
        return withdrawTypeFormatter(cellValue)
      },
      confirmTypeFormatter(row, column, cellValue){ // 提现状态
        return confirmTypeFormatter(cellValue)
      },

      // ================ 数据 =================
      updateMainData() { // 详情
        this.loading = true;
        this.$http.withdrawal(this, this.pageInfo).then(res => {
          if (res.success) {
            this.tableData = res.data.list
            this.total = res.data.total
          }
          res.success ? this.loading = false : this.loading = false
        })
      },
      sizeChange(size) {
        this.pageInfo.size = size;
        this.updateMainData();
      },
      currentChange(page) {
        this.pageInfo.page = page;
        this.updateMainData();
      },
      
    },
    watch: {
      'pageInfo.applyStatus': function() {
        // this.pageInfo.applyStatus = this.search.applyStatus;
        this.pageInfo.page = 1
        this.updateMainData()
      },
      'pageInfo.withdrawAccountType': function() {
        // this.pageInfo.applyStatus = this.search.applyStatus;
        this.pageInfo.page = 1
        this.updateMainData()
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .draw-cash{
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
    .main-wrapper {
      background-color: white;
      overflow: hidden;
      .Fheader {
        width: 98%;
        margin: 10px auto;
        // display: flex !important;
        // justify-content: space-between !important;
        // align-items: center !important;
      }
      .f-search {
        float: right;
        .box-search {
          width: 220px;
        }
        .box-btn {
          position: relative;
          top: -1px;
        }
      }
    }
  }
</style>
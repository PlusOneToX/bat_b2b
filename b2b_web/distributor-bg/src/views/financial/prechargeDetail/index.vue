<template>
  <main class="precharge-details">
    <header>
      <span>明细账</span>
    </header>
    <section class="main-page-wrapper">
      <div class="main-page">
        <el-select class="main-screening" v-model="pageInfo.businessType" clearable placeholder="业务类型" size="mini">
          <el-option label="充值" :value="1"></el-option>
          <el-option label="提现" :value="2"></el-option>
          <el-option label="订单消费" :value="3"></el-option>
          <el-option label="订单退款" :value="4"></el-option>
          <el-option label="人工调整" :value="5"></el-option>
          <el-option label="ERP增量变化" :value="6"></el-option>
          <el-option label="ERP全量变化" :value="7"></el-option>
          <el-option label="分销佣金" :value="8"></el-option>
        </el-select>
        <div class="main-header">
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
          <el-input size="mini" class="box-search" @change="contentChange" clearable @keyup.enter.native="onSearch()" placeholder="分销商/金额" v-model.trim="content"></el-input>
          <button class="mini-search-btn box-btn" @click.prevent="onSearch()">搜索</button>
        </div>
      </div>
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" label="流水号" prop="id" width="80"></el-table-column>
        <el-table-column align="center" label="分销商用户名" prop="distributorName"></el-table-column>
        <el-table-column align="center" label="业务类型" prop="businessType" :formatter="operationMold"></el-table-column>
        <el-table-column align="center" label="交易方式" prop="payWay" :formatter="paytypeFormatter"></el-table-column>
        <el-table-column align="center" label="业务单号" prop="businessId" :formatter="orderFormatter"></el-table-column>
        <el-table-column align="center" label="变化前金额">
          <template slot-scope="scope">
            <i v-if="scope.row.beforeDepositAmount !== undefined && scope.row.beforeDepositAmount !== null" class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.beforeDepositAmount, 2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="变化后金额">
          <template slot-scope="scope">
            <i v-if="scope.row.afterDepositAmount !== undefined && scope.row.afterDepositAmount !== null" class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.afterDepositAmount, 2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="变化类型" prop="changeType" :formatter="changeTypeFormatter"></el-table-column>
        <el-table-column align="center" label="变化金额">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.amount, 2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="交易时间" prop="createTime" width="160" :formatter="timeFormatter"></el-table-column>
        <el-table-column align="center" label="备注" prop="remark"></el-table-column>
      </el-table>
      <pagination :total="total" :page="pageInfo.page" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </section>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
import {rechargeTypeFormatter, chargeOperateFormatter, operationMold} from '@/views/financial/financialUtils'
import { fomatFloat } from '@/utils/common'

export default {
  name: 'prechargeDetails',
  created(){
    this.updateMainData()
  },
  components: { pagination } ,
  data(){
    return {
      colsShownState: {},
      tableData: [],
       pageInfo: {
        page: 1,
        size: 10,
        userId: 10000,
        businessType: undefined,
        contentType: undefined,
        content: undefined
      },
      content: '',
      businessType: '',
      total: 1,
      loading: false,
      contentTypes: [
        { value: 1, label: '分销商用户名' },
        { value: 2, label: '变化金额' }
      ]
    }
  },
  methods:{
    fomatFloat(num,n){
      return fomatFloat(num,n)
    },
    // ======== 操作 ========
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    onSearch(){ // 搜索操作
      this.pageInfo.page = 1
      this.updateMainData()
    },

    // ======== 数据 ========
    updateMainData(){ // 详情
      this.loading = true;
      this.pageInfo.content = this.content // 搜索内容
      this.$http.depositDetailList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
        res.success ? this.loading = false : this.loading = false
      })
    },
    
    onSizeCHange(val){ // 分页方法
      this.pageInfo.size = val;
      this.updateMainData();
    },

    onCurrentChange(val){ // 分页方法
      this.pageInfo.page = val;
      this.updateMainData();
    },
    
    // ======== 转换 ========
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(cellValue)
    },

    chargeOperateFormatter(row, column, cellValue){ // 操作类型
      return chargeOperateFormatter(cellValue)
    },

    paytypeFormatter(row, column, cellValue){ // 支付方式
      return rechargeTypeFormatter(row.payWay)
    },
    operationMold(row, column, cellValue){ // 业务类型
      return operationMold(row.businessType,cellValue)
    },
    orderFormatter(row, column, cellValue){ // 业务单据
      if(cellValue === 0 || cellValue === '0') {
       return '-'
      } else {
        return cellValue
      }
    },
    changeTypeFormatter(row, column, cellValue){ // 变化类型
      switch (cellValue) {
          case 1:
              return '增加'
          case 2:
              return '减少'
          default:
              return '-'
      }
    }
  },
  watch: {
    'pageInfo.businessType'() {
      this.pageInfo.page = 1
      this.updateMainData()
    }
  },
  computed: {}
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .precharge-details{
    height: 100%;
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-home{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    .main-page-wrapper{
      .main-page{
        padding: 10px;
        display: flex;
        justify-content: space-between;
        align-items: center;
        background-color: #fff;
        .main-screening {
          width: 120px;
        }
        .main-header {
          overflow: hidden;
          .box-search {
            width: 200px;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
      .el-form{
        width: 700px;
        padding-top: 30px;
      }
      div.el-tabs__nav{
        margin-left: 30px;
      }
    }
  }
</style>

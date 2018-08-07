<template>
    <div class="warehouse-list">
      <header>
        <span>收款单列表</span>
      </header>

      <div class="function">
        <div class="Fheader">
          <div class="f-left">
            <el-select v-model="pageInfo.voucherStatus" size="mini" placeholder="状态" style="width:120px" clearable>
              <el-option label="待确认" :value="1"> </el-option>
              <el-option label="已确认" :value="2"> </el-option>
              <el-option label="已取消" :value="3"> </el-option>
            </el-select>
            <el-select size="mini" clearable  v-model="pageInfo.payWay" placeholder="支付方式" style="width: 120px;">
              <el-option label="支付宝" :value="1"> </el-option>
              <el-option label="微信" :value="2"> </el-option>
              <el-option label="区间结算" :value="3"> </el-option>
              <el-option label="线下转账" :value="4"> </el-option>
              <el-option label="余额支付" :value="5"> </el-option>
              <el-option label="快钱支付" :value="6"> </el-option>
            </el-select>
             <el-select v-model="pageInfo.businessType" size="mini" placeholder="业务类型" style="width:120px" clearable>
              <el-option label="订单收款" :value="1"> </el-option>
              <el-option label="在线充值" :value="2"> </el-option>
            </el-select>
            <el-select size="mini" v-model="pageInfo.syncErpFlag" placeholder="是否同步ERP" style="width: 120px;" clearable>
              <el-option label="已同步" value="1"></el-option>
              <el-option label="未同步" value="0"></el-option>
            </el-select>
             <el-date-picker
              size="mini"
              v-model="time"
              style="width: 330px;"
              type="datetimerange"
              value-format="timestamp"
              range-separator="至"
              start-placeholder="下单开始日期"
              end-placeholder="下单结束日期"
            ></el-date-picker>
          </div>
          <div class="f-search">
            <button class="mini-search-btn box-btn" @click.prevent="filterBill()">搜索</button>
            <el-input v-model.trim="pageInfo.content" @change="contentChange" clearable @keyup.enter.native="filterBill()" 
              placeholder="请输入关键词搜索" size="mini" class="box-search"></el-input>
            <el-select
              class="content_select"
              placeholder="选择类型"
              size="mini"
              style="width:140px;float:right;"
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
          </div>
        </div>
      </div> 

      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" label="收款单号" prop="id" fixed :min-width="90"></el-table-column>
        <el-table-column align="center" label="ERP收款单号" prop="voucherErpNo" :formatter="erpNoFormatter" :min-width="150"></el-table-column>
        <!-- <el-table-column align="center" label="支付货币" prop="coinType" :formatter="payTypeFormatter" :min-width="90"></el-table-column> -->
        <el-table-column align="center" label="业务单号" prop="businessId" :min-width="90"></el-table-column>
        <el-table-column align="center" label="收款时间" prop="updateTime" :formatter="timeFormatter" :min-width="180"></el-table-column>
        <el-table-column align="center" label="分销商用户名" prop="distributorName" :min-width="120"></el-table-column>
        <el-table-column align="center" label="业务类型" prop="businessType" :formatter="formFormatter" :min-width="100"></el-table-column>
        <el-table-column align="center" label="支付方式" prop="payWay" :formatter="payFormatter" :min-width="100"></el-table-column>
        <el-table-column align="center" label="收款金额" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ scope.row.amount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="支付凭证ID" prop="outTradeNo" :min-width="90"></el-table-column>
        <el-table-column align="center" label="状态" prop="voucherStatus" :formatter="statusFormatter" :min-width="100"></el-table-column>
        <el-table-column label="备注" prop="remark" :min-width="120"></el-table-column>
        <el-table-column align="center" label="操作" :min-width="80" >
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="preview(scope.$index, scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </div>
</template>
<script>
import pagination from '@/components/pagination/index';
import { parseTime } from '@/utils/index'
import {timeFormat} from '@/utils/timeFormat.js'

export default {
  name: 'receipt',
  components: { pagination },
  activated() {
    this.getTableData()
  },
  data() {
    return {
      pageInfo: {
        page: 1,
        size: 10,
        userId: 10000,
        voucherStatus: undefined,
        payWay: undefined,
        businessType: undefined,
        syncErpFlag: undefined,
        contentType: undefined,
        content: undefined,
      },
      content: '',
      time:[],
      mytext: '',
      endTime: '',
      total: 1,
      tableData: [],
      action: 1,
      selectTime: false,
      selectDistributor: false,
      distributorList: [],
      loading: false,
      contentTypes: [
        { value: 1, label: '分销商用户名' },
        { value: 2, label: '收款单号' },
        { value: 3, label: 'ERP收款单号' },
        { value: 4, label: '支付凭证ID' }
      ]
    }
  },
  methods: {
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.filterBill()
      }
    },
    // ======== 操作 ========
    preview(index, row) { // 查看操作
      this.$router.push({ name: 'receiptDetail', query: { id: row.id }})
    },

    filterBill() { // 搜索操作
      this.pageInfo.page = 1
      this.getTableData()
    },

    // ======== 数据 ========
    getTableData() { //  收款单列表数据
      this.loading = true
      this.$http.voucherList(this, this.pageInfo).then(res => {
        if (res.success) {
          this.tableData = res.data.list
          this.total = res.data.total
        }
        res.success ? this.loading = false : this.loading = false
      })
    },

    onSizeCHange(val) { // 条数
      this.pageInfo.page = 1
      this.pageInfo.size = val
      this.getTableData()
    },

    onCurrentChange(val) { // 页数
      this.pageInfo.page = val
      this.getTableData()
    },

    // ======== 转换 ========
    payTypeFormatter(r, c, v) { // 币种
      switch (v) {
        case 1:
          return '人民币'
          break;
        default:
          return '-'
      }
    },
    payFormatter(r, c, v) { // 支付方式
      // 微信支付、支付宝支付、银行汇款
      switch (v) {
        case 1:
          return '支付宝'
          break;
        case 2:
          return '微信'
          break;
        case 3:
          return '区间结算'
          break;
        case 4:
          return '线下转账'
          break;
        case 5:
          return '余额支付'
          break;
        case 6:
          return '快钱支付'
        default:
          return '-'
      }
    },
    erpNoFormatter(row, column, cellValue){ // erp收款单号
      if(cellValue === 0 || cellValue === '0' || cellValue === '' || cellValue === undefined) {
       return '-'
      } else {
        return cellValue
      }
    },
    orderFormatter(row, column, cellValue){ // 业务单据
      if(cellValue === 0 || cellValue === '0' || cellValue === '' || cellValue === undefined) {
       return '-'
      } else {
        return cellValue
      }
    },
    formFormatter(r, c, v) { // 业务类型（订单、充值）
      switch (v) {
        case 1:
          return '订单收款'
        case 2:
          return '在线充值'
        default:
          return '-'
      }
    },
    statusFormatter(r, c, v) { // 状态
      switch (v) {
        case 1:
          return '待确认'
          break;
        case 2:
          return '已确认'
          break;
        case 3:
          return '已取消'
          break;
        default:
          return '-'
      }
    },
    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    }
  },
  watch: {
    "pageInfo.voucherStatus": { // 状态
      handler() {
        this.filterBill()
      },
      deep: true
    },
    "pageInfo.payWay": { // 支付方式
      handler() {
        this.filterBill()
      },
      deep: true
    },
    "pageInfo.businessType": { // 业务类型
       handler() {
        this.filterBill()
      },
      deep: true
    },
    "pageInfo.syncErpFlag": { // 是否同步erp
       handler() {
        this.filterBill()
      },
      deep: true
    },
    time: {
      handler() {
        if(this.time != null && this.time instanceof Array && this.time.length>0){
          // this.pageInfo.startTime = this.time[0]
          // this.pageInfo.endTime = this.time[1]
          this.pageInfo.startTime = timeFormat(this.time[0])
          this.pageInfo.endTime = timeFormat(this.time[1])
        }else {
          this.pageInfo.startTime = undefined
          this.pageInfo.endTime = undefined
          this.pageInfo.page = 1
          this.filterBill()
        }
      },
      deep: true
    }
  },
}

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.warehouse-list {
  background-color: white;
  height: 100%;
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
    span {
      margin-left: 30px;
    }
  }
  .function {
    background-color: white;
    .Fheader {
      display: inline-block;
      width:100%;
      padding: 10px 0;
      .f-left{
        display: inline-block;
      }
      .f-search {
        display: inline-block;
        .box-search {
          width: 180px;
          float:right;
        }
        .box-btn {
          float: right;
          margin-left: 5px;
        }
      }
    }
  }
}

</style>

<template>
  <main class="precharge-details">
    <header>
      <span>余额表</span>
    </header>
    <section class="main-page-wrapper">
      <div class="function">
        <el-dropdown trigger="click" :hide-on-click="false">
          <button class="mini-batch-btn" style="float: left;margin-right:10px;">
            列项<i class="el-icon-arrow-down el-icon--right"></i>
          </button>
          <el-button :loading="syncBalanceLoading" class="mini-search-btn"  @click="syncBalance()">余额同步</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="distributorName">分销商用户名 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="treeNode">分销商等级 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="distributorAncestorName">上级分销商 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="createTime">创建时间 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="rechargeAmount">充值总金额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="commissionAmount">分销所得佣金 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="withdrawAmount">提现金额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="consumerAmount">已消费金额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="refundAmount">退款金额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="accountBalance">账户余额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="accountAvailable">可用余额 </el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox v-model="showedHeads" label="freezingAmount">已冻结金额 </el-checkbox>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
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
          <el-input placeholder="请输入关键词搜索" @keyup.enter.native="onSearch()" @change="contentChange" clearable v-model.trim="content" size="mini" class="box-search"></el-input>
          <button class="mini-search-btn box-btn" @click.prevent="onSearch()">搜索</button>
        </div>
      </div>
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" class="tableCenter" v-loading="loading">
        <el-table-column align="center" label="分销商用户名" prop="distributorName" v-if="colSwitches.distributorName" fixed="left" :min-width="150">
        </el-table-column>
        <el-table-column align="center" label="分销商级数" prop="treeNode" :min-width="120">
        </el-table-column>
        <el-table-column align="center" label="上级分销商" prop="distributorAncestorName" v-if="colSwitches.treeNode > 1" :min-width="120">
        </el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" v-if="colSwitches.createTime" :formatter="timeFormatter" :min-width="180">
        </el-table-column>
        <el-table-column align="center" label="充值总金额" v-if="colSwitches.rechargeAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.rechargeAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="分销所得佣金" v-if="colSwitches.commissionAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.commissionAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="提现金额" v-if="colSwitches.withdrawAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.withdrawAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="已消费金额" v-if="colSwitches.consumerAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.consumerAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="退款金额" v-if="colSwitches.refundAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.refundAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="账户余额" v-if="colSwitches.accountBalance" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.accountBalance| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="可用余额" v-if="colSwitches.accountAvailable" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.accountAvailable| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="已冻结金额" v-if="colSwitches.freezingAmount" :min-width="120">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i> {{ scope.row.freezingAmount| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column label="操作" :min-width="120" align="center">
          <template slot-scope="scope">
            <el-button class="mini-search-btn" @click="lookLog(scope.row)">查看日志</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" :page="pageInfo.page" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
    </section>
    <!-- 日志 -->
    <el-dialog :visible="lookLogShow" width="70%" :before-close="closeLog">
      <el-table border :data="lookLogs" max-height="600" style="width: 100%" header-row-class-name="header-row" class="tableCenter" v-loading="lookLogLoading">
        <el-table-column align="center" label="业务类型" prop="businessType" :formatter="operationMold"></el-table-column>
        <el-table-column align="center" label="交易方式" prop="payWay" :formatter="paytypeFormatter"></el-table-column>
        <el-table-column align="center" label="业务单号" prop="businessId" :formatter="orderFormatter"></el-table-column>
        <el-table-column align="center" label="变化前金额">
          <template slot-scope="scope">
            <i v-if="scope.row.beforeDepositAmount !== undefined && scope.row.beforeDepositAmount !== null" class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.beforeDepositAmount,2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="变化后金额">
          <template slot-scope="scope">
            <i v-if="scope.row.afterDepositAmount !== undefined && scope.row.afterDepositAmount !== null" class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.afterDepositAmount,2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column align="center" label="变化类型" prop="changeType" :formatter="changeTypeFormatter"></el-table-column>
        <el-table-column align="center" label="变化金额">
          <template slot-scope="scope">
            <i class="asmd">￥:&nbsp;</i>
            {{ fomatFloat(scope.row.amount,2)| NumFormat}}
          </template>
        </el-table-column>
        <el-table-column label="下单时间" align="center" prop="createTime" :formatter="timeFormatter" :min-width="160"> </el-table-column>
      </el-table>
      <pagination :total="logTotal" :page="logPageInfo.page" @sizeChange="onLogSizeCHange" @currentChange="onLogCurrentChange"></pagination>
    </el-dialog>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
import {getBalanceList, getBalanceListCount} from '@/views/financial/financialData'
import {confirmCreator} from '@/views/order/orderUtils'
import {sexFormatter,rechargeTypeFormatter, operationMold} from '@/views/financial/financialUtils'
import { fomatFloat } from '@/utils/common'

export default {
  name: 'prechargeDetails',
  created(){
    this.updateMainData();
    this.showedHeads = Object.keys(this.colSwitches); // 确保它们完全一致
  },
  components: { pagination } ,
  data(){
    return {
      colSwitches: {
        distributorName: true,
        treeNode: true,
        createTime: true,
        rechargeAmount: true,
        commissionAmount: true,
        consumerAmount: true,
        refundAmount: true,
        withdrawAmount: true,
        accountBalance: true,
        accountAvailable: true,
        freezingAmount: true
      },
      showedHeads: [],
      colsShownState: {},
      tableData: [],
      pageInfo: {
        page: 1,
        size: 10,
        userId: 10000,
        contentType: undefined,
        content: undefined
      },
      logPageInfo: {
        page: 1,
        size: 10,
        distributorId:undefined
      },
      total: 0,
      logTotal: 0,
      content: '',
      loading: false,
      lookLogShow: false,
      lookLogLoading:false,
      lookLogs:[],
      syncBalanceLoading: false,
      contentTypes: [
        { value: 1, label: '分销商用户名' },
        { value: 2, label: '上级分销商' }
      ]
    }
  },
  methods:{
    fomatFloat(num,n){
      return fomatFloat(num,n)
    },
    // 余额同步
    syncBalance(){
      this.syncBalanceLoading = true
      this.$http.depositAvailableSync(this).then(res => {  
        this.syncBalanceLoading = false
        if(res.success){
          this.$message.success({
            message: "发起同步请求成功，请您耐心等待",
            duration: 3 * 1000,
            onClose: () => { }
          })
        }
      })
    },
    closeLog(){
      this.lookLogShow = false
    },
    lookLog(row){
      this.lookLogShow = true
      this.logPageInfo.distributorId = row.distributorId
      this.updateLogData()
      // this.getLogDataCount()
    },
    // ======== 操作 ========
    contentChange(val){
      if(val === undefined || val === '' || val === null){
        this.onSearch()
      }
    },
    onSearch(){ // 搜索操作
      this.pageInfo.page = 1
      this.updateMainData();
    },
    // ======== 数据 ========
    updateMainData(){ // 余额列表
      this.loading = true;
      this.pageInfo.content = this.content
      this.$http.depositAvailableList(this, this.pageInfo).then(res => {
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
    onLogSizeCHange(val){ // 分页方法
      this.logPageInfo.size = val;
      this.updateLogData()
    },

    onLogCurrentChange(val){ // 分页方法
      this.logPageInfo.page = val;
      this.updateLogData()
    },
    // 查看日志
    updateLogData(){
      this.lookLogLoading = true
      this.$http.depositAvailableDetail(this, this.logPageInfo).then(res => {
        if (res.success) {
          this.lookLogs = res.data.list
          this.logTotal = res.data.total
        }
        res.success ? this.lookLogLoading = false : this.lookLogLoading = false
      })
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
    },
    // ======== 转换 ========
    timeFormatter(row, column, cellValue){ // 时间格式化
      return parseTime(cellValue)
    },

    sexFormatter(row, column, cellValue){ // 时间格式化
      return sexFormatter(cellValue)
    },
  },
  watch:{
    showedHeads: function(){
      for(let prop in this.colSwitches){ // 先把colSwitch全部切为false
        this.colSwitches[prop] = false;
      }
      this.showedHeads.forEach(head => { // 把showedHeads中存有的string对应的colSwitch切为true
        this.colSwitches[head] = true;
      })
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
      background-color: white;
      height: 100%;
      min-width: 1000px;
      .function{
        padding: 10px;
        .btn-export{
          background-color: lighten(grey, 40%);
        }
        .f-search{
          overflow: hidden;
          float: right;
          .box-search{
            width: 220px;
          }
          .box-btn {
            float: right;
            margin-left: 5px;
          }
        }
      }
      .el-table .cell{
        overflow: hidden; 
        white-space: nowrap;
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

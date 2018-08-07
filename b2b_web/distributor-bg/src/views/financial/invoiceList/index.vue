<template>
  <main class="invoice-list">
    <header>
      <span>发票管理</span>
    </header>
    <section class="main-page-wrapper">
      <div class="function clearfix">
        <el-dropdown trigger="click" :hide-on-click="false">
          <el-button size="mini">
            列项<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item>
              <el-checkbox label="distributorName" v-model="showedHeads">分销商用户名</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="invoiceNo" v-model="showedHeads">发票号码</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="createTime" v-model="showedHeads">创建时间</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="invoiceType" v-model="showedHeads">发票类型</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="invoiceAmount" v-model="showedHeads">发票金额</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="paidAmount" v-model="showedHeads">应收金额</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="unpaidAmount" v-model="showedHeads">未收金额</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="bbbbbb" v-model="showedHeads">付款状态</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="invoiceTitle" v-model="showedHeads">发票抬头</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="taxpayerNumber" v-model="showedHeads">纳税人识别码</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="receiveName" v-model="showedHeads">收票人姓名</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="receiveMobile" v-model="showedHeads">收票人电话</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="distributorId" v-model="showedHeads">物流单号</el-checkbox>
            </el-dropdown-item>
            <el-dropdown-item>
              <el-checkbox label="remark" v-model="showedHeads">发票备注</el-checkbox>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-button type="primary" size="mini" @click="dialogVisible = true" style="margin-left:20px">开票</el-button>
        <div class="search">
          <el-select size="mini" v-model="search.payState" placeholder="请选择">
            <el-option label="全部" :value="0"></el-option>
            <el-option label="未付款" :value="1"></el-option>
            <el-option label="部分付款" :value="2"></el-option>
            <el-option label="已付款" :value="3"></el-option>
          </el-select>
          <el-input placeholder="请输入内容" v-model="search.content" size="mini" class="box-search"></el-input>
          <el-button size="mini" class="btn-search" @click="onSearch">搜索</el-button>
        </div>
      </div>
      <el-table :data="tableData" border max-height="700" style="width:100%" header-row-class-name="header-row" @selection-change="onSelectionChange">
        <el-table-column align="center" type="index" width="50">
        </el-table-column>
        <el-table-column align="center" label="分销商用户名" prop="distributorName" v-if="colSwitches.distributorName">
        </el-table-column>
        <el-table-column align="center" label="发票号码" prop="invoiceNo" v-if="colSwitches.invoiceNo">
        </el-table-column>
        <el-table-column align="center" label="创建时间" prop="createTime" :formatter="timeFormatter" v-if="colSwitches.createTime">
        </el-table-column>
        <el-table-column align="center" label="发票类型" prop="invoiceType" :formatter="invoiceTypeFormatter" v-if="colSwitches.invoiceType">
        </el-table-column>
        <el-table-column align="center" label="发票金额" prop="invoiceAmount" v-if="colSwitches.invoiceAmount">
        </el-table-column>
        <el-table-column align="center" label="应收金额" prop="paidAmount" v-if="colSwitches.paidAmount">
        </el-table-column>
        <el-table-column align="center" label="未收金额" prop="unpaidAmount" v-if="colSwitches.unpaidAmount">
        </el-table-column>
        <el-table-column align="center" label="付款状态" prop="bbbbbb" v-if="colSwitches.bbbbbb">
        </el-table-column>
        <el-table-column align="center" label="发票抬头" prop="invoiceTitle" v-if="colSwitches.invoiceTitle">
        </el-table-column>
        <el-table-column align="center" label="纳税人识别码" prop="taxpayerNumber" v-if="colSwitches.taxpayerNumber">
        </el-table-column>
        <el-table-column align="center" label="收票人姓名" prop="receiveName" v-if="colSwitches.receiveName">
        </el-table-column>
        <el-table-column align="center" label="收票人电话" prop="receiveMobile" v-if="colSwitches.receiveMobile">
        </el-table-column>
        <el-table-column align="center" label="物流单号" prop="distributorId" v-if="colSwitches.distributorId">
        </el-table-column>
        <el-table-column align="center" label="发票备注" prop="remark" v-if="colSwitches.remark">
        </el-table-column>
        <el-table-column align="center" label="操作" width="230" fixed="right">
          <template slot-scope="scope">
            <el-button type="success" size="mini" @click="handleCheck(scope.row)">查看</el-button>
            <el-button type="primary" size="mini" @click="onCancel(scope.$index, scope.row)">取消</el-button>
            <el-button type="primary" size="mini" @click="onReceive(scope.$index, scope.row)">收款</el-button>
          </template>
        </el-table-column>
      </el-table>
      <pagination :total="total" @sizeChange="onSizeCHange" @currentChange="onCurrentChange"></pagination>
      <el-dialog
        :modal-append-to-body="false"
        center
        title="选择开票分销商"
        :visible.sync="dialogVisible"
        width="900px">
        <choseDistr @createInvoice="handleCreate"></choseDistr>
      </el-dialog>
    </section>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import {parseTime} from '@/utils/index'
import {getInvoices, getInvoicesCount, cancelInvoice, receiveMoney} from '@/views/financial/financialData'
import {confirmCreator} from '@/views/order/orderUtils'
import {invoiceTypeFormatter} from '@/views/financial/financialUtils'
// import {sexFormatter} from '@/views/financial/financialUtils'
import choseDistr from '@/views/financial/invoiceList/components/choseDistr'

export default {
  name: 'invoiceList',
  created(){
    this.updateMainData();
    this.showedHeads = Object.keys(this.colSwitches); // 确保它们完全一致
  },
  components: { pagination, choseDistr } ,
  data(){
    return {
      // pageState: 'rootPage',
      dialogVisible: false,
      colSwitches: {
        distributorName: true,
        invoiceNo: true,
        createTime: true,
        invoiceType: true,
        invoiceAmount: true,
        paidAmount: true,
        unpaidAmount: true,
        bbbbbb: true,
        invoiceTitle: true,
        taxpayerNumber: true,
        receiveName: true,
        receiveMobile: true,
        distributorId: true,
        remark: true,
        bbbbbb: true
      },
      showedHeads: [],
      tableData: [],
      pageInfo: { //分页信息
        page: 1,
        count: 10
      },
      total: 1, //分页信息
      search:{
        payState: 0,
        content: null, // 搜索用关键词
      },
      distToCreInv: null,
      checkedInvoice: null,
    }
  },
  methods:{
    onSearch(){
      this.updateMainData();
    },
    onSelectionChange(){},
    updateMainData(){
      const params = Object.assign({}, this.pageInfo, this.search)
      getInvoicesCount(this, params)
      .then(res => {this.total = res.count})

      return getInvoices(this, params)
      .then(res => {this.tableData = res.invoices})
    },
    handleCreate(row){ // 开具发票组件
      this.dialogVisible = false;
      this.distToCreInv = row;
    },
    handleCheck(row){ // 查看操作
      this.$router.push({name: 'checkInvoice', params: {invoId: row.id}})
    },
    onCancel(){
      
    },
    onReceive(){
      
    },
    onSizeCHange(val) { // 分页方法
      this.pageInfo.count = val;
    },
    onCurrentChange(val) { // 分页方法
      this.pageInfo.page = val;
    },
    timeFormatter(row, column, cellValue) { // 时间格式化
      return parseTime(cellValue)
    },
    invoiceTypeFormatter(row, column, cellValue) { // 时间格式化
      return invoiceTypeFormatter(cellValue)
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
    },
    'search.payState': function(){
      this.updateMainData()
    },
  },
  // computed: {}
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .invoice-list{
    height: 100%;
    // min-width: 1050px;
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
    .el-dialog__body{
      padding-top: 0;
    }
    .main-page-wrapper{
      background-color: white;
      height: 100%;
      min-width: 1000px;
      .function{
        padding: 16px;
        // background-color: white;
        .btn-export{
          background-color: lighten(grey, 40%);
        }
        .search{
          float: right;
        }
        .box-search{
          width: 140px;
        }
        .btn-search{
          background-color: $lakeBlue;
          color: white;
        }
      }

      /*background-color: blue;*/
      .header-row {
        background-color: $table-header-bg;
        th {
          padding: 5px;
          text-align: center;
        }
      }
      .btns{
        text-align: center;
      }
      td {
        text-align: center;
      }
      .table-cell{
        text-align: center;
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

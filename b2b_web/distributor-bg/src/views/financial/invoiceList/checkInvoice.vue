<template>

  <main class="create-invoice">
    <header>
      <span>发票管理</span>
    </header>
    <div class="box-has-border">
      <div class="text-center title">
        <span>基本信息</span>
      </div>
      <el-form label-width="280px">
        <el-form-item label="发票类型">
          lx
        </el-form-item>
        <el-form-item label="发票抬头">{{invoDetail.invoiceTitle}}</el-form-item>
        <el-form-item label="纳税人识别码">{{invoDetail.taxpayerNumber}}</el-form-item>
        <el-form-item label="发票注册地址">{{invoDetail.registerAddress}}</el-form-item>
        <el-form-item label="发票注册电话">{{invoDetail.registerPhone}}</el-form-item>
        <el-form-item label="发票开户银行">{{invoDetail.bankDepositName}}</el-form-item>
        <el-form-item label="发票银行账户">{{invoDetail.bankAccount}}</el-form-item>
        <el-form-item label="开票区域">
          {{invoDetail.invoiceArea}}
        </el-form-item>
        <el-form-item label="发票号码">
          {{invoDetail.invoiceNo}}
        </el-form-item>
      </el-form>
    </div>
    <div class="box-has-border" v-if="invoDetail.bills">
      <div class="text-center title">
        <span>开票账单</span>
      </div>
      <el-table border :data="invoDetail.bills" style="width: 100%" header-row-class-name="header-row">
        <el-table-column align="center"
          prop="billAmount"
          label="对账单号">
        </el-table-column>
        <el-table-column align="center"
          prop="createTime"
          label="对账时间">
        </el-table-column>
        <el-table-column align="center"
          prop="payType"
          :formatter="billPayTpFormatter"
          label="支付方式">
        </el-table-column>
        <el-table-column align="center"
          prop="payState"
          :formatter="billPayStFormatter"
          label="付款状态">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="对账金额">
        </el-table-column>
      </el-table>
    </div>
    <div class="box-has-border" v-if="invoDetail.receive">
      <div class="text-center title">
        <span>收票信息</span>
      </div>
      <div class="half-width">
        <el-form ref="chosenData" label-width="190px">
          <el-form-item label="收件人">
            {{invoDetail.receive.name}}
          </el-form-item>
          <el-form-item label="邮编">
            {{invoDetail.receive.zipCode}}
          </el-form-item>
          <el-form-item label="配送方式">
            {{invoDetail.receive.distributionName}}
          </el-form-item>
        </el-form>
      </div>
      <div class="half-width">
        <el-form ref="chosenData" label-width="190px">
          <el-form-item label="收件地址">
            {{invoDetail.receive.address}}
          </el-form-item>
          <el-form-item label="联系电话">
            {{invoDetail.receive.mobile}}
          </el-form-item>
          <el-form-item label="物流单号">
            {{invoDetail.receive.logisticsNo}}
          </el-form-item>
        </el-form>
      </div>
    </div>
    <div class="box-has-border" v-if="invoDetail.payment">
      <div class="text-center title">
        <span>收款信息</span>
      </div>
      <el-form label-width="280px">
        <el-form-item label="发票金额">
          {{invoDetail.payment.invoiceAmount}}
        </el-form-item>
        <el-form-item label="已收金额">
          {{invoDetail.payment.paidAmount}}
        </el-form-item>
        <el-form-item label="未收金额">
          {{invoDetail.payment.unpaidAmount}}
        </el-form-item>
      </el-form>
    </div>
    <div class="box-has-border">
      <console parent="CheckInvoice" :invoId="invoId.toString()" :invoLog="invoDetail.logs || null"></console>
    </div>
  </main>
</template>

<script>
import pagination from '@/components/pagination/index'
import console from '@/views/financial/invoiceList/components/console'
import choseDistribution from '@/views/financial/invoiceList/components/choseDistribution'
import { dateToNum } from '@/views/warehouse/warehousesUtils'
import {parseTime} from '@/utils/index'
import { getInvoDetail } from '@/views/financial/financialData'
import { confirmCreator } from '@/views/order/orderUtils'
import { billPayTpFormatter, billPayStFormatter, invoAreaFormatter } from '@/views/financial/financialUtils'

export default {
  name: 'checkInvoice',
  created(){
    this.invoId = this.$route.params.invoId;
    this.updateMainData()
  },
  components: { pagination, console, choseDistribution },
  data(){
    return {
      invoId: null,
      invoDetail: {},
    }
  },
  methods:{
    // 开票账单
    updateMainData(){
      getInvoDetail(this, {id: this.invoId})
      .then(res => {this.invoDetail = res.invoice})
      .catch(e => console.log(e))
    },
    billPayTpFormatter(row, column, cellValue){
      return billPayTpFormatter(cellValue)
    },
    billPayStFormatter(row, column, cellValue){
      return billPayStFormatter(cellValue)
    },
    
  },
  watch:{
    
  },
  computed: {
   
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  .create-invoice{
    header{
      color: white;
      height: $lineHight;
      line-height: $lineHight;
      background-color: $lakeBlue;
      span{
        margin-left: 30px;
      }
      .btn-add{
        float: right;
        padding:5px;
        margin-top:7px;
        margin-right:8px;
      }
    }
    background-color: white;
    padding-bottom: 30px;
    form.el-form{
      margin-top: 0;
    }
    .el-form div.el-form-item{
      margin-bottom: 5px;
    }
    .half-width{
      width: 50%;
      box-sizing: border-box;
      float: left;
    }
    .text-center{
      text-align: center;
    }
    .text-center.title{
      background-color: $bg;
      padding-top: 10px;
      padding-bottom: 10px;
      border-bottom: 1px solid $tableColor;
      border-top: 1px solid $tableColor;
      
    }
    .box-btn-top{
      padding: 20px;
    }
    .box-has-border{
      overflow: hidden;
      .cost-line{
        padding-bottom: 10px;
        padding-top: 10px;
        border-bottom: 1px solid $tableColor;
        padding-left: 30px;
        span.cost-info{
          margin-left: 5px;
        }
        span.cost-info:last-child{
          margin-right: 35px;
        }
      }
      .cost-line:last-child{
        border-bottom: none;
      }

      .align-right{
        text-align: right;
      }
      div.form{
        margin-top: 30px;
        margin-bottom: 40px;
        form.el-form{
          margin-right: 0;
          width: 80%;
          min-width: 800px;
          max-width: 1000px;
        }
      }
    }
    .function{
      padding: 16px;
      background-color: white;
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
    td {
      text-align: center;
    }
    .table-cell{
      text-align: center;
    }
    div.el-tabs__nav{
      margin-left: 30px;
    }
  }
</style>

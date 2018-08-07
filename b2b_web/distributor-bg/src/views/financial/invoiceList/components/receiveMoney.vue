<template>
  <main class="receive-money">
    <header>
      <span>发票收款</span>
    </header>
    
    <div class="form">
      <el-form label-width="180px">
        <el-form-item label="操作备注">
          <el-input
            type="textarea"
            :rows="5"
            placeholder="请输入内容"
            v-model="remark"
            >
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="box-has-border">
      <div class="text-center title">
        <span>收款信息</span>
      </div>
      <el-table border :data="tableData" style="width: 100%" header-row-class-name="header-row">
        <el-table-column align="center"
          prop="billAmount"
          label="订单号">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="下单时间">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="发货单号">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="支付方式">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="付款状态">
        </el-table-column>
        <el-table-column align="center"
          prop="billAmount"
          label="订单价格">
        </el-table-column>
      </el-table>
    </div>
    <div class="function">
      <el-button @click="onConfirm">确认</el-button>
      <el-button @click="onCancel">取消</el-button>
    </div>
  </main>
</template>
<script>
import { getWithdrawlogs, confirmWithdraw, cancelInvoice, receiveMoney } from '@/views/financial/financialData'
import { confirmTypeFormatter } from '@/views/financial/financialUtils'
import { confirmCreator } from '@/views/order/orderUtils'
// import {parseTime} from '@/utils/index'

export default {
  name: 'receive-money',
  components: {},
  created(){
    this.invoId = this.$route.params.invoId
  },
  data(){
    return {
      invoId: '',
      tableData: [], // 主要数据
      remark: null,
    }
  },
  props: {
    
  },
  methods:{
    onConfirm(){
      const req = _ => receiveMoney(this, {id: this.invoId, receiveMoney: 1});
      confirmCreator(this)('确认收款', req)
    },
    onCancel(){
      this.$router.back()
    }
  },
  computed: {},
  watch: {}
}
</script>
<style rel="stylesheet/scss" lang="scss">
  .receive-money{
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
    // height: 100%;
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
      // margin-bottom: 25px;
      background-color: $bg;
      padding-top: 10px;
      padding-bottom: 10px;
      border-bottom: 1px solid $tableColor;
      border-top: 1px solid $tableColor;
      
    }
    .box-btn-top{
      padding: 20px;
    }
    div.form{
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form{
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        // display: inline-block;
        // max-width: 1000px;
      }
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
      // .operation{
      //   padding-bottom: 50px;
      //   .operation-part:first-child{
      //     margin-left: 100px;
      //   }
      //   .operation-part:last-child{
      //     float: right;
      //     margin-right: 80px;
      //   }
      // }
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
    .function{
      text-align: center
    } 
  }
</style>
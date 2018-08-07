<template>
  <main class="edit-other-info">
    <header>
      <h4>订单详情</h4>
      <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="onRevert">
          返回订单详情
        </el-button>
    </header>
    <el-form :model="formData" label-width="180px">
      <el-form-item label="是否开票" prop="warehouseInId">
        <el-radio v-model="formData.isInvoice" :label="1">是</el-radio>
        <el-radio v-model="formData.isInvoice" :label="0">否</el-radio>
      </el-form-item>
      <el-form-item label="是否加急" prop="warehouseInId">
        <el-radio v-model="formData.isUrgent" :label="1">是</el-radio>
        <el-radio v-model="formData.isUrgent" :label="0">否</el-radio>
      </el-form-item>
      <el-form-item label="发票类型" v-if="formData.invoice" prop="warehouseInId">
        <el-select v-model="formData.invoice.invoiceType" placeholder="请选择">
          <el-option label="普通" :value="1"> </el-option>
          <el-option label="增值税" :value="2"> </el-option>
        </el-select>
      </el-form-item>
      <el-form-item label="发票抬头" v-if="formData.invoice" prop="warehouseInId">
        <el-input v-model="formData.invoice.invoiceTitle"></el-input>
      </el-form-item>
      <el-form-item label="发票注册地址" v-if="formData.invoice" prop="warehouseInId">
        <el-input v-model="formData.invoice.registerAddress"></el-input>
      </el-form-item>
      <el-form-item label="发票注册电话" v-if="formData.invoice" prop="warehouseInId">
        <el-input v-model="formData.invoice.registerPhone"></el-input>
      </el-form-item>
      <el-form-item label="发票银行" v-if="formData.invoice" prop="warehouseInId">
        <el-input v-model="formData.invoice.bankAccountName"></el-input>
      </el-form-item>
      <el-form-item label="发票银行账户" v-if="formData.invoice" prop="warehouseInId">
        <el-input v-model="formData.invoice.bankAccount"></el-input>
      </el-form-item>

    </el-form>
    <div class="btns">
      <el-button class="mini-search-btn" @click="onConfirm" type="primary">确认</el-button>
      <!-- <el-button class="mini-back-btn" @click="onRevert">取消</el-button> -->
    </div>
  </main>
    <!-- <success
    :expire="5"
    :goHome="onRevert"
    @alreadyGoHome="_ => {this.successState = false;}"
    v-if="successState">
    </success> -->
</template>

<script>
// import eventBus from '@/views/order/eventBus'
import {confirmCreator, chooseBus} from '@/views/order/orderUtils'
import success from '@/views/order/components/oprationSuccess'
import {editOrder} from '@/views/order/orderData'

export default {
  name: 'editOtherInfo',
  created(){
    const currOrderId = this.$route.query.orderId;
    if(this.orderId != currOrderId){
      this.$store.dispatch('updateOrderId', currOrderId)
      .then(_ => {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail));
      })
    }else{
      this.formData = JSON.parse(JSON.stringify(this.orderDetail));
    }
  },
  components: {success} ,
  // 内部需要的数据：发票类型list, 
  data(){
    return {
      // successState: false, // 控制页面显示,当它为true时，切换至成功页面
      formData: {},
    }
  },
  computed: {
    orderId() {
      return this.$store.getters.orderId
    },
    orderDetail() {
      return this.$store.getters.orderDetail
    },
  },
  methods:{
    // 点击事件*2
    onRevert(){
      // this.$router.push({
      //   name: 'refundOrderDetail',
      //   query: {orderId: this.orderId},
      // })
      this.$router.go(-1)
    },
    onConfirm(){
      // confirmThenDo :: String,Function -> Promise(确认》请求》响应)
      return this.confirmThenDo('提交更改', _ => {
        return this.editOrderReq()
        .then(res => {
          if(res.code == 0){
            this.successState = true;
          }
          return res
        })
      })
    },
    // 操作前询问是否确定，防止误操作
    confirmThenDo(operation, callback){
      // confirmCreator :: this -> Function 柯里化
      // confirmCreator 为修饰器，为函数增加运行前询问的功能
      this.confirmThenDo = confirmCreator(this); // 该函数首次运行
      return this.confirmThenDo(operation, callback); // 等价于confirmCreator(this)(operation, callback)
    },
    // 抓取需要的参数>发出编辑的请求
    editOrderReq(){
      const invoice = this.formData.invoice;
      return editOrder(this, {
        id: this.formData.id,
        isUrgent: this.formData.isInvoice,
        isInvoice: this.formData.isInvoice,
        invoice: {
          // taxpayerNumber: this.formData.invoice.,
          // name: this.formData.invoice.,
          invoiceType: invoice.invoiceType,
          invoiceTitle: invoice.invoiceTitle,
          registerAddress: invoice.registerAddress,
          registerPhone: invoice.registerPhone,
          bankAccountName: invoice.bankAccountName,
          bankAccount: invoice.bankAccount,
        },
      });
    },

  },
  props: {
    currentDistributionId: Number, 
    chosenData: Object,
  },
  watch: {
    orderDetail(){
      this.formData = JSON.parse(JSON.stringify(this.orderDetail));
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @function calculate-width ($col-span) {
    @return 100% / $col-span
  }

  .edit-other-info{
    height: 100%;
    min-width: 1050px;
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
    .btn-home{
      float: right;
      padding: 5px;
      margin-top: 8px;
      margin-right: 8px;
      margin-left: 0;
      font-size: 12px;
    }
    form.el-form{
      width: 500px;
      margin-top: 30px;
    }
    // table{
    //   width: 100%;
    //   border-collapse: collapse;
    //   tr{
    //     border-bottom: 1px solid $tableColor;
    //     th{
    //       padding: 20px 0;
    //       width:  calculate-width(4);
    //     }
    //     // th.radio{
    //     //   width: 60px;
    //     // }
    //     td{
    //       padding: 20px 0;
    //       text-align: center;
    //     }
    //   }
    // }
    div.btns{
      text-align: center;
      margin-top: 30px;
    }
  }
</style>

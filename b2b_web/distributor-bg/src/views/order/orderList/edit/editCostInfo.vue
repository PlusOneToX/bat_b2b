<template>
  <main class="edit-cost">
    <header>
      <span>编辑费用信息</span>
    </header>
    <div>
      <section class="clearfix">
        <div class="half-width">
          <el-form ref="form" label-width="180px">
            <el-form-item label="商品总金额："> {{formData.goodsAmount}} </el-form-item>
            <el-form-item label="发票税额：">
              <el-input :disabled="true" v-model="formData.invoiceTax" style="width:200px" size="mini"> </el-input>
            </el-form-item>
            <el-form-item label="配送费用：">
              <el-input :disabled="true" v-model="formData.distributionMoney" style="width:200px" size="mini"> </el-input>
            </el-form-item>
            <el-form-item label="保价费用：">
              <el-input :disabled="true" v-model="formData.insurance" style="width:200px" size="mini"> </el-input>
            </el-form-item>
            <el-form-item label="支付费用：">
              <el-input :disabled="true" v-model="formData.aaaaa" style="width:200px" size="mini"> </el-input>
            </el-form-item>
            <el-form-item label="包装费用：">
              <el-input :disabled="true" v-model="formData.packingCost" style="width:200px" size="mini"> </el-input>
            </el-form-item>
          </el-form>
        </div>
        <div class="half-width">
          <el-form ref="form" label-width="100px">
            <el-form-item label="折扣：">
              <el-input v-model="formData.orderDiscount" style="width:200px" size="mini">
              </el-input>
            </el-form-item>
            <el-form-item label="订单总金额："> {{formData.orderAmount}} </el-form-item>
            <el-form-item label="已付款金额："> {{formData.paidAmount}}
            </el-form-item>
            <el-form-item label="使用余额：">
              <el-input :disabled="true" v-model="formData.depositAmount" style="width:200px" size="mini"> </el-input>
            </el-form-item>
            <el-form-item label="应付款金额："> {{formData.payAmount}} </el-form-item>
          </el-form>
        </div>
      </section>
      <div class="btns">
        <el-button size="mini" @click="onConfirm" type="primary">确认</el-button>
        <el-button size="mini" @click="onRevert">取消</el-button>
      </div>
    </div>
  </main>
</template>

<script>
import {parseTime} from '@/utils/index'
import {editOrder} from '@/views/order/orderData'
import {confirmCreator, chooseBus} from '@/views/order/orderUtils'

let eventBus = {};

export default {
  name: 'editCost',
   props: {
    chosenData: Object,
  },
  created(){
    const currOrderId = this.$route.query.orderId;
    if(this.orderId != currOrderId){
      this.$store.dispatch('updateOrderId', currOrderId).then(_ => {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail.cost));
      })
    }else{
      this.formData = JSON.parse(JSON.stringify(this.orderDetail.cost));
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
  data(){
    return {
      discountType: 2, // 1, 申请货品折扣, 2, 申请订单折扣
      formData: {},
    }
  },
  methods:{
    onRevert(){
      this.$router.push({
        name: 'orderDetail',
        params: {orderId: this.$route.query.orderId},
      })
    },
    onConfirm(){
      confirmCreator(this)('提交更改', _ => {
        this.editOrderReq()
      })
    },
    editOrderReq(){
      // return editOrder(this, {
      //   id: this.chosenData.id,
      //   discountType: 2,
      //   orderDiscount: parseFloat(this.formData.orderDiscount),
      // });
      return editOrder(this, {
        id: this.orderId,
        discounts: [
          {
            discountType: this.discountType,
            orderDiscount: this.formData.orderDiscount,
          }
        ]
      });

    },
  },
  watch: {
    chosenData(){
      this.formData = JSON.parse(JSON.stringify(this.chosenData.cost));
    },
    'formData.orderDiscount': function(){
      this.formData.payAmount = parseFloat(this.formData.orderAmount - this.formData.orderDiscount - this.formData.paidAmount - this.formData.depositAmount);
    },
    'formData.orderDiscount': function(){
      this.formData.orderDiscount = parseFloat(this.formData.orderDiscount);
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  // @function calculate-width ($col-span) {
  //   @return 100% / $col-span
  // }
  .edit-cost{
    height: 100%;
    min-width: 1050px;
    background-color: white;
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
    .half-width{
      padding-top: 50px;
      width: 50%;
      box-sizing: border-box;
      float: left;
    }
    div.btns{
      text-align: center;
      margin-top: 30px;
    }
    }
</style>

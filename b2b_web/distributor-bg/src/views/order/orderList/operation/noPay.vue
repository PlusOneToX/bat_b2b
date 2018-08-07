<template>
  <div class="wraper">
    <header>
      <span>设为未付款</span>
    </header>
    <el-form ref="formData" :model="formData" label-width="180px" class="el-form">
      <el-form-item label="操作备注" prop="remark">
        <el-input type="textarea" v-model="formData.remark" :autosize="{ minRows: 5, maxRows: 8}"></el-input>
      </el-form-item>
      <el-form-item label="退款方式" prop="remark">
        <el-radio v-model="refundWay" :label="1">退回支付账户</el-radio>
        <el-radio v-model="refundWay" :label="2">退回用户余额</el-radio>
        <el-radio v-model="refundWay" :label="3">生成退款单</el-radio>
        <el-radio v-model="refundWay" :label="4">不处理，无操作时选择此项</el-radio>
      </el-form-item>
      <el-form-item label="退款说明" prop="remark">
        <el-input type="textarea" v-model="formData.remark" :autosize="{ minRows: 5, maxRows: 8}"></el-input>
      </el-form-item>

      <el-form-item>
        <el-button size="mini" type="primary" @click="onConfirm">确定</el-button>
        <el-button size="mini" @click="onCancel">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import {operateOrder} from '@/views/order/orderData'
import {confirmCreator} from '@/views/order/orderUtils'

export default {
  name: 'noPay',
  props: {},
  mounted(){
    const currOrderId = this.$route.query.orderId;
    if(this.orderId != currOrderId){
      this.$store.dispatch('updateOrderId', currOrderId).then(_ => {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail));
      })
    }else{
      this.formData = JSON.parse(JSON.stringify(this.orderDetail));
    }
  },
  data(){
    return {
      formData: { },
      refundWay: 1,
      salesAreas: [],
    }
  },
  methods: {
    onConfirm(){ // 点击事件，生成发货单
      confirmCreator(this)('设为未付款', _ => {
        return operateOrder(this,{id: this.orderId, operateType: 3})
      }).then(_ => 0); // 操作成功后 更新根数据
    },
    onCancel(){
      this.$router.go(-1)
    },
  },
  computed: {
    orderId() {
      return this.$store.getters.orderId
    },
    orderDetail() {
      return this.$store.getters.orderDetail
    },
  },
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .wraper{
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
    .btn-add{
      float: right;
      padding:5px;
      margin-top:7px;
      margin-right:8px;
    }
    .el-form-item{
      padding: 15px 0;
    }
    background-color: white;
    padding-bottom: 100px;
    position: relative;
    .el-form{
      width: 900px;
      margin-top: 30px;
    }
  }
</style>

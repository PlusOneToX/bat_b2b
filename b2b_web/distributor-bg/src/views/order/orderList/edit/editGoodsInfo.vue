<!-- 因为有“上一条”“下一条”功能，故不选择路由id传参，而是props传参 -->
<template>
  <main class="edit-distribution">
    <header>
      <span>编辑收货信息</span>
    </header>
    <div>
      <table>
        <tr>
          <th>商品名称</th>
          <th>货号</th>
          <th>价格</th>
          <th>价格折扣</th>
          <th>数量</th>
          <th>小计</th>
          <th>操作</th>
        </tr>
        <tr v-for="(data, index) in formData" :key="data.id">
          <td>{{data.goodsName}}</td>
          <td>{{data.itemCode}}</td>
          <td>{{data.salePrice}}</td>
          <td>
            <el-input v-model="data.itemDiscount" @change="watchDiscount(data)"></el-input>
          </td>
          <td>{{data.itemCount}}</td>
          <td>{{data.actualTotalAmount}}</td>
          <td>
            <el-button @click="formData.splice(index, 1);">删除</el-button>
          </td>
        </tr>
        <tr>
          <td></td>
          <td></td>
          <td></td>
          <td></td>
          <td>合计</td>
          <td>{{formData.map(goods => goods.actualTotalAmount).reduce((prevNum, nextNum) => prevNum + nextNum, 0)}}</td>
          <td>
            <el-button @click="formData = JSON.parse(JSON.stringify(chosenData.goods))">更新</el-button>
          </td>
        </tr>

      </table>
      <div class="btns">
        <el-button size="mini" @click="onConfirm" type="primary">确认</el-button>
        <el-button size="mini" @click="onRevert">取消</el-button>
      </div>
    </div>
    <!-- <success
    :expire="5"
    :goHome="onRevert"
    @alreadyGoHome="_ => {this.successState = false;}"
    v-if="successState">
    </success> -->
  </main>
</template>

<script>
import {parseTime} from '@/utils/index'
import {editOrder} from '@/views/order/orderData'
import {confirmCreator, chooseBus} from '@/views/order/orderUtils'
import success from '@/views/order/components/oprationSuccess'

let eventBus = {};

export default {
  name: 'editGoods',
  created(){
    const currOrderId = this.$route.query.orderId;
    if(this.orderId != currOrderId){
      this.$store.dispatch('updateOrderId', currOrderId)
      .then(_ => {
        this.formData = JSON.parse(JSON.stringify(this.orderDetail.goods));
      })
    }else{
      this.formData = JSON.parse(JSON.stringify(this.orderDetail.goods));
    }
  },
  components: {success} ,
  data(){
    return {
      successState: false, // 控制页面显示,当它为true时，切换至成功页面
      formData: [],
      // ----------------------
      discountType: 1, // 1, 申请货品折扣, 2, 申请订单折扣
      // ----------------------
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
      this.$router.push({
        name: 'orderDetail',
        params: {orderId: this.$route.query.orderId},
      })
    },
    onConfirm(){
      // confirmThenDo :: String,Function -> Promise(确认》请求》响应)
      confirmCreator(this)('提交更改', _ => {
        this.editOrderReq()
        .then(res => {
          if(res.code == 0){
            this.successState = true;
          }
        })
      })
    },
    // 监控折扣数据编辑
    watchDiscount(data){
      if(data.salePrice - data.itemDiscount <= 0){
        this.$message({
          type: 'warning',
          message: '折扣必须小于价格!'
        });
        data.itemDiscount = 0;
      }else{
        data.actualTotalAmount = (data.salePrice - data.itemDiscount) * data.itemCount
      }
    },
    // 发出编辑的请求
    editOrderReq(){
      const discounts = [];

      this.formData.forEach(item => {
        discounts.push({
            discountType: this.discountType, // 1, 申请货品折扣, 2, 申请订单折扣
            itemId: item.itemId,
            itemDiscount: parseFloat(item.itemDiscount),
        })
      })

      return editOrder(this, {
        id: this.orderId,
        discounts,
      });
    },
  },
  props: {
    chosenData: Object,
  },
  watch: {
    chosenData(){
      this.formData = JSON.parse(JSON.stringify(this.chosenData.goods));
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @function calculate-width ($col-span) {
    @return 100% / $col-span
  }

    .edit-distribution{
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
      table{
        width: 100%;
        border-collapse: collapse;
        tr{
          border-bottom: 1px solid $tableColor;
          th{
            padding: 20px 0;
            width:  calculate-width(7);
          }
          td{
            padding: 20px 0;
            text-align: center;
          }
        }
      }
    }
    div.btns{
      text-align: center;
      margin-top: 30px;
    }
</style>

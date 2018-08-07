<template>
  <main class="edit-distribution">
    <header>
      <span>编辑配送方式</span>
    </header>
    <div>
      <table>
        <tr>
          <th class="radio">选择</th>
          <th>名称</th>
          <th>描述</th>
          <th>配送费</th>
        </tr>
        <tr v-for="distribution in distributions" :key="distribution.id">
          <td>
            <el-radio v-model="distrId" :label="distribution.id"></el-radio>
          </td>
          <td>{{distribution.name}}</td>
          <td>{{distribution.description}}</td>
          <td>{{distribution.amount}}</td>
        </tr>
      </table>
      <div class="btns">
        <el-button size="mini" @click="onConfirm" type="primary">确认</el-button>
        <el-button size="mini" @click="onRevert">取消</el-button>
      </div>
    </div>
  </main>
</template>

<script>
import {confirmCreator} from '@/views/order/orderUtils'
import {getOrderDistributions, editOrder} from '@/views/order/orderData'

export default {
  name: 'editDistributionMode',
  created(){
    this.orderId = this.$route.query.orderId;
    this.distrId = parseInt(this.$route.query.distribution);

    getOrderDistributions(this, {id: this.orderId}).then(res => {
      this.distributions = res.distributions;
      return res.distributions;
    })
  },
  data(){
    return {
      distributions: [],
      orderId: 0,
      distrId: null,
    }
  },
  computed: {
    chosenDistr: function(){
      let chosen = null;
      this.distributions.forEach(distr => {
        if(distr.id == this.distrId){
          chosen = distr
        }
      })
      return chosen;
    }
  },
  methods:{
    
    onRevert(){ // 点击事件 *2
      this.$router.push({
        name: 'orderDetail',
        params: {orderId: this.orderId},
      })
    },
    confirmThenDo(operation, callback){
      // confirmCreator :: this -> Function 柯里化
      // confirmCreator 为修饰器，为函数增加运行前询问的功能
      this.confirmThenDo = confirmCreator(this); // 该函数首次运行
      return this.confirmThenDo(operation, callback); // 等价于confirmCreator(this)(operation, callback)
    },
    onConfirm(){
      // confirmThenDo :: String,Function -> Promise(确认》请求》响应)
      this.confirmThenDo('提交更改', _ => {
        this.editOrderReq().then(res => {
          if(res.code == 0){
            this.successState = true;
          }
        })
      })
    },
    // 发出编辑的请求
    editOrderReq(){
      return editOrder(this, {
        id: this.orderId,
        distribution: {
          distributionMode: this.distrId,
          distributionMoney: this.chosenDistr.amount ,
          orderMessage: this.orderMessage ? this.distributionMoney : undefined ,
        }
      });
    },

  },
  watch: {
    chosenData(){
      this.orderId = this.chosenData.distribution.id;  
    },
    successState(){
      
    }
  },
}
</script>

<style rel="stylesheet/scss" lang="scss">
  // @function calculate-width ($col-span) {
  //   @return 100% / $col-span
  // }
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
          width:  calculate-width(4);
        }
        td{
          padding: 20px 0;
          text-align: center;
        }
      }
    }
    div.btns{
      text-align: center;
      margin-top: 30px;
    }
  }
</style>

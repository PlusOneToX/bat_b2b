<template>
  <main class="chose-distribution">
    <table>
      <tr>
        <th class="radio">选择</th>
        <th>名称</th>
        <!-- <th>描述</th>
        <th>配送费</th> -->
      </tr>
      <tr v-for="distribution in distributions" :key="distribution.id">
        <td>
          <el-radio v-model="chosenId" :label="distribution.id"></el-radio>
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
  </main>
</template>
<script>
import {parseTime} from '@/utils/index'
import success from '@/views/order/components/oprationSuccess'
// ========================
import {confirmCreator, chooseBus} from '@/views/order/orderUtils'
import { getDistributions } from '@/views/financial/financialData'

export default {
  name: 'chose-distribution',
  components: {},
  mounted(){
    getDistributions(this, {page: 1, count: 10000})
    .then(res => {
      this.distributions = res.distributions;
      return res.distributions;
    })
    .then(_ => {this.chosenId = this.distributions[0].id})
    .catch(e => console.log(e))
  },
  components: {success} ,
  data(){
    return {
      successState: false, // 控制页面显示,当它为true时，切换至成功页面
      distributions: [], // 配送模式
      chosenId: 0, // number 主要数据
      distributionMoney: null,
      orderMessage: null,
    }
  },
  computed: {
    chosenDis(){
      return this.distributions.filter(dis => dis.id == this.chosenId)[0]
    }
  },
  methods:{
    // 点击事件 *2
    onRevert(){
      // this.$emit('revert');
    },
    confirmThenDo(operation, callback){
      this.$emit('choseDistribution')
    },
    // 发出编辑的请求
    editOrderReq(){},
    onConfirm(){
      this.$emit('chosen', this.chosenDis)
    },
  }

}
</script>
<style rel="stylesheet/scss" lang="scss">
.chose-distribution{
  table{
    width: 100%;
    border-collapse: collapse;
    tr{
      border-bottom: 1px solid $tableColor;
      th{
        padding: 20px 0;
        width:  calculate-width(4);
      }
      // th.radio{
      //   width: 60px;
      // }
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
  span.el-radio__label{
    visibility: hidden;
  }
}
</style>
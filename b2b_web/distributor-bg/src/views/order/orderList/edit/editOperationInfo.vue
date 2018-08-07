<!-- 因为有“上一条”“下一条”功能，故不选择路由id传参，而是props传参 -->
<template>
  <div class="edit-distribution">
    <table>
      <tr>
        <th class="radio">选择</th>
        <th>名称</th>
        <th>描述</th>
        <th>配送费</th>
      </tr>
      <tr v-for="(distribution, index) in distributions" :key="index">
        <td>
          <el-radio v-model="chosenIndex" :label="index"></el-radio>
        </td>
        <td>{{distribution.name}}</td>
        <td>{{distribution.bbbbbb}}</td>
        <td>{{distribution.bbbbbb}}</td>
      </tr>
    </table>
    <div class="btns">
      <el-button size="mini" @click="onConfirm" type="primary">确认</el-button>
      <el-button size="mini" @click="onRevert">取消</el-button>
    </div>
  </div>
</template>

<script>
import {parseTime} from '@/utils/index'
import eventBus from '@/views/order/eventBus'
import {filter, curry, compose} from 'ramda'

import {getdistributions} from '@/views/order/orderData'

let cuFilter = curry(filter);

export default {
  name: 'editDistributionMode',
  props: {
    chosenData: Object,
  },
  mounted(){
    getdistributions(this).then(res => {
      this.distributions = res.distributions;
      return res.distributions;
    }).then(distributions => { // 查出当前选中的配送方式在distributions中对应的index
      distributions.forEach((value, index, arr) => {
        if(value.id == this.chosenData.distribution.id){
          this.chosenIndex = index;
        }
      })
    })

  },
  data(){
    return {
      distributions: [],
      chosenIndex: 0,
    }
  },
  methods:{
    onRevert(){
      eventBus.$emit('switchPageState', 'orderDetail');
    },
    onConfirm(){
      eventBus.$emit('switchPageState', 'orderDetail');
      eventBus.$emit('changeRemoteData');
    },

  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  // @function calculate-width ($col-span) {
  //   @return 100% / $col-span
  // }
  .warehouse-list-wrap{
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

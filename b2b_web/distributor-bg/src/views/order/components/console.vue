<template>
  <section class="console">
    <div class="text-center title">
      <span>操作信息</span>
    </div>
    <div class="form">
      <el-form label-width="180px">
        <el-form-item label="操作备注">
          <el-input type="textarea" :rows="5" placeholder="请输入内容"> </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="operation">
      <span class="instruction"> 当前可执行操作： </span>
      <span class="operation-part" v-if="!inApprovalContext">
        <el-button size="mini" @click="operateOrder('现款支付的订单需要先付款后才能确认，请先进行付款操作', 1)">确认订单</el-button>
        <el-button size="mini" @click="operateOrder('作废订单', 7)">作废订单</el-button>
        <!-- <el-button size="mini" @click="$router.push({name:'noPay',query:{orderId:chosenData.id}})">设为未付款</el-button>
        <el-button size="mini" @click="$router.push({name:'createDelivery',query:{orderId:chosenData.id}})">生成发货单</el-button>
        <el-button size="mini" @click="operateOrder('取消发货单', 5)">取消发货单</el-button> -->
        <el-button size="mini" @click="operateOrder('设为已付款', 6)">设为已付款</el-button>
        <el-button size="mini" @click="$router.push({name:'invalid',query:{orderId:chosenData.id}})">取消订单</el-button>
      </span>
      <span class="operation-part" v-else>
        <el-button size="mini" @click="operateOrder('同意', 1)">同意</el-button>
        <el-button size="mini" @click="operateOrder('拒绝', 7)">拒绝</el-button>
      </span>
    </div>
    <el-table border :data="operateLog" style="width: 100%" header-row-class-name="header-row" v-if="!inApprovalContext">
      <el-table-column align="center" prop="operateName" label="操作者"> </el-table-column>
      <el-table-column align="center" prop="createTime" label="操作时间"> </el-table-column>
      <el-table-column align="center" prop="operateType" label="操作类型"> </el-table-column>
      <!-- <el-table-column prop="aaaa" label="订单状态"> </el-table-column>
      <el-table-column prop="aaaa" label="付款状态"> </el-table-column>
      <el-table-column prop="aaaa" label="发货状态"> </el-table-column> -->
      <el-table-column align="center" prop="operateRemark" label="备注"> </el-table-column>
    </el-table>
  </section>
</template>
<script>
// 引入eventBus是为了完成编辑后通知上层组件更新数据
import eventBus from '@/views/order/eventBus'
import {operateOrder, orderOperateLog} from '@/views/order/orderData'
import {confirmCreator} from '@/views/order/orderUtils'
// 组件
import createDelivery from '@/views/order/orderList/operation/createDelivery'

export default {
  props: {
    chosenData: Object,
    inApprovalContext: Boolean,
  },
  mounted(){
    // this.updateMainData();
  },
  data() {
    return {
      tableData:[],
      operateLog: [],
    }
  },
  methods: {
    updateMainData(){
      // 请求操作日志
      orderOperateLog(this, {id: this.chosenData.orderId || this.chosenData.id})
      .then(res => {this.operateLog = res.logs})
    },
    // 操作订单 * n
    //  operateType: 1确认, 2付款, 3设为未付款, 4生成发货单, 5取消发货单, 6完成, 7取消, 8无效
    /**
     * 订单操作
     * @param  {String} whisper     出现在弹出框中的信息，内容为操作名
     * @param  {Number} operateType 请求参数，代表操作类型
     * @return {[type]}             [description]
     */
    operateOrder(whisper, operateType){
      confirmCreator(this)(whisper, _ => {
        return operateOrder(this,{id: this.chosenData.orderId || this.chosenData.id, operateType})
      })
      // 或许应更新详情中的数据
    },
    onSwitchPageState(state){ //@state:string name of pageState
      eventBus.$emit('switchPageState', state)
    },
  },
  watch:{
    'chosenData.id': function(){
      this.updateMainData();
    }
  },
  components: {
    createDelivery
  },
}

</script>
<style lang="scss" scoped>
.console{
  .operation{
    .instruction{
      padding-left: 50px;
    }
      padding-bottom: 50px;
    }

}

</style>

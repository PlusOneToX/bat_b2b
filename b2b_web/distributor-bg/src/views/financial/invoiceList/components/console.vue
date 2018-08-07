<template>
  <main class="withdraw-console">
    <div class="text-center title">
      <span>操作信息</span>
    </div>
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
    <div class="operation">
      <span class="instruction"> 当前可执行操作： </span>
      <span class="operation-part">
        <el-button size="mini" @click="handleCreate" v-if="inCreator" >开票</el-button>
        <el-button size="mini" @click="_ => 0" v-if="inCreator">取消</el-button>
        <el-button size="mini" @click="goAccept" v-if="inCheck">收款</el-button>
        <!-- <el-button size="mini" @click="han$router.push({name: ''})dleCancelAcc" v-if="inCheck">取消收款</el-button> -->
      </span>
    </div>
    <el-table border :data="invoLog" style="width: 100%" header-row-class-name="header-row" v-if="inCheck">
      <el-table-column align="center"
      prop="operationName"
      label="操作者">
      </el-table-column>
      <el-table-column align="center"
      prop="createTime"
      label="操作时间">
      </el-table-column >
      <!-- <el-table-column
      prop="status"
      label="开票状态">
      </el-table-column> -->
      <el-table-column align="center"
      prop="operationType"
      label="操作">
      </el-table-column>
      <el-table-column align="center"
      prop="operationRemark"
      label="备注">
      </el-table-column>
    </el-table>
  </main>
</template>
<script>
import { getWithdrawlogs, confirmWithdraw, cancelInvoice, receiveMoney } from '@/views/financial/financialData'
import { confirmTypeFormatter } from '@/views/financial/financialUtils'
import { confirmCreator } from '@/views/order/orderUtils'
// import {parseTime} from '@/utils/index'

export default {
  name: 'invoice-console',
  components: {},
  created(){
    this.updateMainData();
  },
  data(){
    return {
      tableData: [], // 主要数据
      remark: null,
    }
  },
  props: {
    parent: String,
    params: Object,
    invoId: String,
    invoLog: Array
  },
  methods:{
    updateMainData(){
      // return getWithdrawlogs(this, {id: this.withDrawId})
      // .then(res => {this.tableData = res.logs})
    },
    createInvoice(){
      const params = {remark: this.remark};
      Object.assign(params, this.params)
      return createInvoice(this, params);
    },
    handleCreate(){
      const req = this.createInvoice;
      confirmCreator(this)('开票' , req);
    },
    goAccept(){
      this.$router.push({name: 'receiveMoney', params: {invoId: this.invoId}})
    },
    handleCancelAcc(){
    }
  },
  computed: {
    inCreator: function(){
      return this.parent == 'createInvoice'
    },
    inCheck: function(){
      return this.parent == 'CheckInvoice'
    },
  },
  watch: {
    'withDrawId': function(){
      this.getWithdrawlogs()
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
.withdraw-console{
  .operation{
    .instruction{
      padding-left: 50px;
    }
      padding-bottom: 50px;
    }

}
</style>
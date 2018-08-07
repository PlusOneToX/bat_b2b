<template>
  <main class="withdraw-console">
    <div class="text-center title">
      <span>操作信息</span>
    </div>
    <div class="form">
      <el-form label-width="180px">
        <el-form-item label="操作备注:">
          <el-input type="textarea" :rows="5" placeholder="请输入内容">
          </el-input>
        </el-form-item>
      </el-form>
    </div>
    <div class="operation">
      <span class="instruction"> 当前可执行操作:</span>
      <span class="operation-part">
        <el-button size="mini" @click="operate('确认提现', 2)">确认付款</el-button>
        <el-button size="mini" @click="operate('拒绝', 3)">拒绝</el-button>
      </span>
    </div>
    <el-table border :data="tableData" style="width: 100%" header-row-class-name="header-row">
      <el-table-column align="center" prop="operationName" label="操作者"> </el-table-column>
      <el-table-column align="center" prop="createTime" label="操作时间"> </el-table-column>
      <el-table-column align="center" prop="status" label="状态"> </el-table-column>
      <el-table-column align="center" prop="remark" label="备注"> </el-table-column>
    </el-table>
  </main>
</template>
<script>
import { getWithdrawlogs, confirmWithdraw } from '@/views/financial/financialData'
import { confirmTypeFormatter } from '@/views/financial/financialUtils'
import { confirmCreator } from '@/views/order/orderUtils'
// import {parseTime} from '@/utils/index'

export default {
  name: 'withdraw-console',
  components: {},
  created(){
    this.updateMainData();
  },
  data(){
    return {
      tableData: [], // 主要数据
    }
  },
  props: {
    withDrawId: Number
  },
  methods:{
    updateMainData(){
      return getWithdrawlogs(this, {id: this.withDrawId})
      .then(res => {this.tableData = res.logs})
    },
    /**
     * 询问 》 执行
     * @param  {string} whisper     操作名，询问时出现
     * @param  {number} confirmType 表示操作类型 2 确认通过 3 不通过
     * @return {object}             promise
     */
    operate(whisper, confirmType){
      const req = _ => confirmWithdraw(this, {id: this.withDrawId, confirmType});

      confirmCreator(this)(whisper, req);
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
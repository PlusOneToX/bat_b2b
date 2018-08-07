<template>
  <main class="execute-frozen">
    <el-table :data="tableData" border max-height="700" @selection-change="onSelectionChange">
      <el-table-column align="center" prop="name" label="用户"></el-table-column>
      <el-table-column align="center" prop="registerName" label="姓名"></el-table-column>
      <el-table-column align="center" prop="accountBalance" label="账号余额"></el-table-column>
      <el-table-column align="center" prop="accountAvailable" label="可用余额"></el-table-column>
      <el-table-column align="center" prop="freezingAmount" label="已冻结金额"></el-table-column>
      <el-table-column align="center" label="冻结金额">
        <template slot-scope="scope">
          <el-input v-model="scope.row.frozenAmount" class="box-search" 
          @input="scope.row.frozenAmount=/^\d+\.?\d{0,2}$/.test(scope.row.frozenAmount)||scope.row.frozenAmount == '' ? scope.row.frozenAmount:Number(scope.row.frozenAmount.toString().match(/^\d+(?:\.\d{0,1})?/))"  
          type="number" min="0" step="0.01" size='mini'></el-input>
        </template>
      </el-table-column>
      <el-table-column align="center" label="备注">
        <template slot-scope="scope">
          <el-input v-model="scope.row.remark" size="mini" class="box-search"></el-input>
        </template>
      </el-table-column>
    </el-table>
    <div style="text-align:center;padding-top: 20px">
      <el-button type="primary" @click="onConfirm" >确定</el-button>
    </div>
  </main>
</template>

<script>
import eventBus from '@/views/order/eventBus'
import pagination from '@/components/pagination/index'
import { freezeCash, getPrechargesByIds, addFreezings} from '@/views/financial/financialData'
import {confirmCreator} from '@/views/order/orderUtils'

export default {
  name: 'execute-frozen',
  mounted(){
    if(this.givenItems && this.givenItems[0]){
      this.tableData = JSON.parse(JSON.stringify(this.givenItems));
      this.tableData.forEach(obj => { // 初始化冻结金额和备注
        this.$set(obj, 'frozenAmount', 0);
        this.$set(obj, 'remark', '');
      });
      this.updateMainData()
    }
  },
  components: { pagination },
  props: {
    givenItems: Array,
  },
  data(){
    return {
      tableData: [], //主要数据
      total: 0,
      foreeze: {
        frozenAmount: '',
        remark: ''
      },
      loading:''
    }
  },
  methods:{
    // ======== 操作 ========
    onConfirm(){ // 发出冻结请求
      const freezings = this.tableData.map(distributor => {
        return {
          distributorId: distributor.id,
          freezingAmount: distributor.frozenAmount,
          remark: distributor.remark
        }
      });
      this.$confirm('确定要冻结该用户，是否继续？','提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(_ => {
        this.loading = this.$loading({
          lock: true,
          text: '拼命冻结中，请稍等...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        });
        addFreezings(this, {freezings: freezings}).then(res => {
          if(res.code == 0) {
            this.$message.success({
              message: '操作成功',
              duration: 3 * 1000,
            })
            this.loading.close();
            eventBus.$emit('addFreezingsShow');
            // this.$emit('confirmedFrozen')
          }else {
            this.loading.close()
          }
        })
      })
    },
    
    // ======== 数据 ========
    updateMainData(){  // 确定后的预存款余额详情
      const ids = this.givenItems.map(item => item.id)
        .reduce((sum, cur, index) => {
          return sum + (index == 0? '' : ',') + cur
        }, '');

      if(ids == '') return;

      return getPrechargesByIds(this, {ids}).then(res => { // 得到与tableData一一对应的accounts数组
        return this.tableData.map(data => data.id).map(id => {
          for(let i = 0, len = res.accounts.length; i < len; i++){
            if(res.accounts[i].distributorId == id){
              return res.accounts[i]
            }
          }
        });
      }).then(accounts => { // 拼装数据
        this.tableData.forEach((obj, index) => {
          if(accounts[index]){
            this.$set(obj, 'accountBalance', accounts[index].accountBalance);
            this.$set(obj, 'accountAvailable', accounts[index].accountAvailable);
            this.$set(obj, 'freezingAmount', accounts[index].freezingAmount);
          }
        })
      })
    },
    
    onSelectionChange(val){ // 选中的规格值
      this.chosenItems = val;
    },
  },
  watch:{
    givenItems: function(){
      this.tableData = JSON.parse(JSON.stringify(this.givenItems));
      this.tableData.forEach(obj => { // 初始化冻结金额和备注
        this.$set(obj, 'frozenAmount', 0);
        this.$set(obj, 'remark', '');
      });
      this.updateMainData();
    },
   
  }
}
</script>
<style rel="stylesheet/scss" lang="scss">
  .frozen-window{
    th{
      text-align: center;
    }
    .el-dialog__body{
      padding-top: 0;
    }
  }
</style>
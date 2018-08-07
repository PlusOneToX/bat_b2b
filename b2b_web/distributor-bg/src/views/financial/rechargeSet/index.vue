<template>
  <main class="preCharge">
    <header>
      <span>预存款设置</span>
    </header>
      <el-form label-width="200px" >
        <el-form-item label="前台是否显示预存款项:">
          <el-radio-group v-model="formData.isShowPrestore">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否开启线上充值:" v-if="depositShow">
          <el-radio-group v-model="formData.isOpenOnlineTopup">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="线上用户最小额度:" style="margin: 0" v-if="limitShow" >
          <el-input v-model="formData.onlineMinAmount" type="number" step="0.1" min="0" size="mini"  auto-complete="off"/>
          <span class="place-holder">不填或填“0”都表示不限制用户充值最小金额；金额货币单位为默认货币单位。</span>
        </el-form-item>
        <el-form-item label="是否开启线下充值:" v-if="depositShow">
          <el-radio-group v-model="formData.isOpenOfflineTopup">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="是否开启提现:" v-if="depositShow">
          <el-radio-group v-model="formData.isOpenWithdrawal">
              <el-radio :label="1">是</el-radio>
              <el-radio :label="0">否</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div class="btns">
        <el-button size="mini" type="primary" @click="onConfirm">确认</el-button>
      </div>
  </main>
</template>

<script>
// import eventBus from '@/views/order/eventBus'
import { confirmCreator, chooseBus } from '@/views/order/orderUtils'
import { editPrecharge, getSettings } from '@/views/financial/financialData'

export default {
  name: 'chargeSet',
  components: {},
  data() {
    return {
      formData: {
        isShowPrestore: 1,
        isOpenOnlineTopup: 1,
        onlineMinAmount: '',
        isOpenOfflineTopup: 1,
        isOpenWithdrawal: 1
      },
      limitShow: true, // 线上用户最小额度显示
      depositShow: true, // 前台食肉显示预存款项
    }
  },
  activated() {
    this.updateSettings()
  },
  methods: {
    onConfirm() { // 确定操作，更新预存款设置
      this.$confirm('确定要保存当前预存款设置吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
      }).then(res => {
        // this.formData.id = 
        editPrecharge(this, this.formData).then(res => {

        })
      }).catch(_ => {
        this.$message({
          type: 'info',
          message: '已取消设置'
        })
      })
    },
    // editPrecharge(){
    //   return editPrecharge(this, this.formData);
    // },
    updateSettings() { // 已设置的预存款设置
      getSettings(this).then(res => {
        this.formData = res.deposit
        if(res.deposit.isShowPrestore == null) {
          this.formData.isShowPrestore = 0
          this.formData.isOpenOnlineTopup = 0
        }
      })
    }
    // onConfirm(){
    //   confirmCreator(this)('更改设置', this.editPrecharge)
    //   .then(_ => this.updateSettings());
    // },
  },
  watch: {
    'formData.isShowPrestore': function() { // 前台是否显示预存款项
      if(this.formData.isShowPrestore === 0) {
        this.depositShow = false
        this.limitShow = false
      }else {
        this.depositShow = true
        this.formData.isOpenOfflineTopup = 1 // 是否开启线下充值
        this.formData.isOpenWithdrawal = 1 // 是否开启提现
        if(this.formData.isOpenOnlineTopup = 1) { // 是否开启线上充值
          this.limitShow = true 
        }
      }
    },
    'formData.isOpenOnlineTopup': function() { // 是否开启线上充值
      if(this.formData.isOpenOnlineTopup === 0) {
        this.limitShow = false
      }else {this.limitShow = true}
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
  @function calculate-width ($col-span) {
    @return 100% / $col-span
  }

  .preCharge{
    height: 100%;
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
    .place-holder {
      color: #ccc;
      font-size: 12px;
  	}
    form.el-form{
      width: 620px;
      margin-top: 30px;
    }
    div.btns{
      text-align: center;
      margin-top: 30px;
      margin-bottom: 30px;
    }
  }
</style>

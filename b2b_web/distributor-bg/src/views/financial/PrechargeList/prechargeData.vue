<template>
  <main class="precharge-detail">
    <section>
      <header>
        <h4>充值详情</h4>
        <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
          返回充值详情列表
        </el-button>
      </header>

      <!-- 详情主体 -->
      <div v-loading="loading2">
        <div class="box-has-border">
          <div class="text-center title">
            <span>基本信息</span>
          </div>
          <div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="支付凭证序号:"> {{formData.rechargeOrderId}} </el-form-item>
              <el-form-item label="充值金额:">
                <i class="asmd">￥:&nbsp;</i>
                {{formData.rechargeAmount | NumFormat}}
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="充值状态:"> {{rechargeFormatter(formData.confirmStatus)}} </el-form-item>
              <el-form-item label="记录时间:"> {{timeFormatter(0, 0, formData.createTime)}} </el-form-item>
              <el-form-item label="充值方式:"> {{topUpWay(formData.rechargeType)}} </el-form-item>
            </el-form>
          </div>

        </div>

        <div class="box-has-border" v-if="formData.deposit && formData.confirmStatus == 2">
          <div class="text-center title">
            <span>充值账户</span>
          </div>
          <div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="分销商用户名:"> {{formData.deposit.distributorName}} </el-form-item>
              <el-form-item label="分销商姓名:"> {{formData.deposit.registerName}} </el-form-item>
              <el-form-item label="分销商留言:"> {{formData.remark}} </el-form-item>
            </el-form>
          </div><div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="电子邮件:"> {{formData.deposit.email}} </el-form-item>
              <el-form-item label="手机:"> {{formData.deposit.mobile}} </el-form-item>
            </el-form>
          </div>

        </div>

        <div class="box-has-border" v-if="formData.bankAccount">
          <div class="text-center title">
            <span>收款账户</span>
          </div>
          <div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="账户类型:"> {{formData.bankAccount.bbb}} </el-form-item>
              <el-form-item label="开户名/姓名:"> {{formData.bankAccount.payee}} </el-form-item>
              <el-form-item label="支付凭证:">
                <viewer :image="formData.payVoucherUrl">
                  <img class="half_width_img" :src="formData.payVoucherUrl">
                </viewer>
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form label-width="280px">
              <el-form-item label="开户行/网上支付方式:"> {{formData.bankAccount.bankName}} </el-form-item>
              <el-form-item label="账号:"> {{formData.bankAccount.cardNo}} </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="box-has-border" v-if="checkRechargeType == 3 && checkConfirmStatus == 1">  <!-- formData.bankAccount -->
          <div class="text-center title">
            <span>操作信息</span>
          </div>
          <div class="form">
            <el-form label-width="180px">
              <el-form-item label="操作备注:">
                <el-input type="textarea" v-model="remark" :rows="5" placeholder="请输入内容" > </el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="operation">
            <span class="instruction"> 当前可执行操作： </span>
            <span class="operation-part">
              <el-button class="mini-search-btn" @click="toggleConfirm(2)" :loading="loading">确认到账</el-button>
              <el-button class="mini-search-btn" @click="toggleConfirm(3)" :loading="loadings">未到账</el-button>
            </span>
          </div>
        </div>
        
        <el-table class="tableCenter" border :data="operationLogs" header-row-class-name="header-row">
          <el-table-column align="center" label="操作者" prop="operationName"> </el-table-column>
          <el-table-column align="center" label="操作时间" prop="createTime" :formatter="formatTime"> </el-table-column>
          <el-table-column align="center" label="操作" prop="operationType" :formatter="forstatus"> </el-table-column>
          <el-table-column align="center" prop="remark" label="备注"> </el-table-column>
        </el-table>
      </div>
    </section>

  </main>
</template>
<script>
import {getPrechargeDetail, getChargeLogs, confirmPrecharge} from '@/views/financial/financialData'
import { rechargeFormatter, topUpWay } from '@/views/financial/financialUtils'
import { parseTime } from '@/utils/index'
import { timeFormat } from "@/utils/timeFormat";

export default {
  name: 'prechargeDetail',
  activated() {
    this.updateMainData()
  },
  data() {
    return {
      formData: {},
      operationLogs: [],
      remark:"",
      loading: false,
      loading2: false,
      loadings: false
    }
  },
  computed: {
    checkRechargeType() { // 充值方式 1、支付宝 2、微信 3、线下汇款
      return this.$route.query.rechargeType
    },
    checkConfirmStatus() { // 充值确认状态 1、待确认 2、确认 3、未通过
      return this.formData.confirmStatus
    }
  },
  methods: {
    cancel() { // 返回收款列表操作
      this.$router.push({ name: 'PrechargeList'})
    },
    updateMainData(){ // 充值详情
      this.loading2 = true;
      getChargeLogs(this, {id: this.$route.query.id}).then(res => { // 操作日志数据
        this.operationLogs = res.logs
      });
      getPrechargeDetail(this, {id: this.$route.query.id}).then(res => { // 充值详情数据
        this.formData = res.recharge
        res.code == 0 ? this.loading2 = false : this.loading2 = false
      }).catch(e => console.log(e))
    },
    toggleConfirm(operateCode){ // 2.确认通过, 3.不通过
      if(operateCode == 2) {
        this.$confirm('确定已经收到此款项？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
        }).then(_ => {
          this.loading = true
          confirmPrecharge(this, {id: this.formData.id, remark:this.remark, confirmType: operateCode}).then(res => {
            if (res.code == 0) {
              this.$message.success({
                message: "操作成功",
                duration: 3 * 1000,
              });
              this.loading = false
              this.updateMainData();
            }else {this.loading = false}
          })
        }).catch(_ => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      }else {
        this.$confirm('确定未收到款项，关闭此单据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        center: true
        }).then(_ => {
          this.loadings = true
          confirmPrecharge(this, {id: this.formData.id, remark:this.remark, confirmType: operateCode}).then(res => {
            if (res.code == 0) {
              this.$message.success({
                message: "操作成功",
                duration: 3 * 1000,
                onClose: () => { }
              });
              this.loadings = false
              this.updateMainData();
            }else {this.loadings = false}
          });
        }).catch(_ => {
          this.$message({
            type: 'info',
            message: '已取消操作'
          })
        })
      }
    },
    formatTime(row, col, val) { // 表格时间格式化
      return timeFormat(val)
    },
    timeFormatter(row, column, cellValue) { // 详情时间格式化
			return parseTime(cellValue)
    },
    rechargeFormatter(stateCode){ // 充值详情  > 充值状态的switch语句
			this.rechargeFormatter = rechargeFormatter;
			return this.rechargeFormatter(stateCode);
    },
    topUpWay(stateCode){ // 充值信息  > 充值方式的switch语句
			this.topUpWay = topUpWay;
			return this.topUpWay(stateCode);
    },
    forstatus(row) { // 日志操作记录
      switch(row.operationType) {
        case 1:
          return '充值创建';
        case 2: 
          return '充值确认通过';
        case 3:
          return '充值不通过';
      }
    },
  }
}

</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.precharge-detail{
  header {
    color: white;
    height: $lineHight;
    line-height: $lineHight;
    background-color: $lakeBlue;
  }
  h4 {
    display: inline-block;
    font-weight: 350;
    font-size: 16px;
    margin: 0 20px;
  } 
  .btn-home{
    float: right;
    padding: 5px;
    margin-top: 8px;
    margin-right: 8px;
    margin-left: 0;
    font-size: 12px;
  }
  .el-form div.el-form-item{
    margin-bottom: 5px;
  }
 .operation{
  .instruction{
    padding-left: 50px;
  }
    padding-bottom: 50px;
  }
  background-color: white;
  padding-bottom: 30px;
  form.el-form{
    margin-top: 0;
  }
  .half-width{
    width: 50%;
    box-sizing: border-box;
    float: left;
    .half_width_img {
      width: 210px;
      height: 140px;
    }
  }
  .text-center{
    text-align: center;
  }
  .box-btn-top{
    padding: 20px;
  }
  .box-has-border{
    overflow: hidden;
    .cost-line{
      padding-bottom: 10px;
      padding-top: 10px;
      border-bottom: 1px solid $tableColor;
      padding-left: 30px;
      span.cost-info{
        margin-left: 5px;
      }
      span.cost-info:last-child{
        margin-right: 35px;
      }
    }
    .cost-line:last-child{
      border-bottom: none;
    }
    .align-right{
      text-align: right;
    }
    div.form{
      margin-top: 30px;
      margin-bottom: 40px;
      form.el-form{
        margin-right: 0;
        width: 80%;
        min-width: 800px;
        max-width: 1000px;
      }
    }
  }
  .function{
    padding: 0px 16px 16px 16px;
    background-color: white;
    .btn-export{
      background-color: lighten(grey, 40%);
    }
    .search{
      float: right;
    }
    .box-search{
      width: 140px;
    }
    .btn-search{
      background-color: $lakeBlue;
      color: white;
    }
  }
  .header-row {
    background-color: $table-header-bg;
    th {
      padding: 5px;
      text-align: center;
    }
  }
  td {
    text-align: center;
  }
  .table-cell{
    text-align: center;
  }
  div.el-tabs__nav{
    margin-left: 30px;
  }
}

</style>
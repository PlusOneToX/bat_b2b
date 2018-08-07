<template>
  <main>
    <section class="draw-detail">
      <header>
        <h4>提现详情</h4>
        <el-button class="mini-back-btn btn-home" icon="el-icon-d-arrow-left" @click="cancel">
          返回提现详情列表
        </el-button>
      </header>
      
      <div v-loading="loading2">
        <div class="box-has-border">
          <div class="text-center title">
            <span>基本信息</span>
          </div>
          <div class="half-width" style="float: left">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="提现编号:"> {{tableData.id}} </el-form-item>
              <el-form-item label="分销商用户名:"> {{tableData.distributorName}} </el-form-item>
              <el-form-item label="电子邮件:" v-if="tableData.deposit"> {{tableData.deposit.email}} </el-form-item>
              <el-form-item label="分销商留言:"> {{tableData.remark}} </el-form-item>
            </el-form>
          </div>
          <div class="half-width" style="float: left">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="申请时间:"> {{timeFormatter(tableData.createTime)}} </el-form-item>
              <el-form-item label="提现状态:"> {{confirmTypeFormatter(tableData.confirmStatus)}} </el-form-item>
              <el-form-item label="手机:" v-if="tableData.deposit">{{tableData.deposit.mobile}} </el-form-item>
            </el-form>
          </div>
        </div>

        <div class="box-has-border">
          <div class="text-center title">
            <span>提现信息</span>
          </div>
          <div class="half-width" v-if="tableData.deposit" style="float: left">
            <!--  withdrawType 1,银行汇款， 2，网上支付-->
            <el-form ref="chosenData" label-width="280px" v-if="tableData.withdrawType == 1">
              <el-form-item label="支付方式:">{{payType(tableData.withdrawType)}} </el-form-item>
              <el-form-item label="账户名:" >{{tableData.deposit.registerName}} </el-form-item>
              <el-form-item label="开户行:"> {{tableData.bankAccount.bankName}} </el-form-item>
              <el-form-item label="收款账号:"> {{tableData.withdrawType == 1 ? tableData.bankAccount.cardNo : tableData.bankAccount.lineAccount}} </el-form-item>
            </el-form>

            <!-- <el-form ref="chosenData" label-width="280px" v-if="tableData.withdrawType == 2">
              <el-form-item label="支付方式:">{{payType(tableData.withdrawType)}} </el-form-item>
              <el-form-item label="收款账号:"> {{tableData.bankAccount.lineAccount}} </el-form-item>
            </el-form> -->

          </div>
          <div class="half-width" v-if="tableData.deposit" style="float: left">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="账户余额:">
                <i class="asmd">￥:&nbsp;</i>
                {{tableData.deposit.accountBalance | NumFormat}}
              </el-form-item>
              <el-form-item label="已冻结金额:">
                <i class="asmd">￥:&nbsp;</i>
                {{tableData.deposit.freezingAmount | NumFormat}}
              </el-form-item>
              <el-form-item label="可用金额:">
                <i class="asmd">￥:&nbsp;</i>
                {{tableData.deposit.accountAvailable | NumFormat}}
              </el-form-item>
              <el-form-item label="提现金额:"> 
                <i class="asmd">￥:&nbsp;</i>
                {{tableData.withdrawAmount | NumFormat}}
              </el-form-item>
              
            </el-form>
          </div>
        </div>

        <!-- <div class="box-has-border" v-if="tableData.bankAccount">
          <div class="text-center title">
            <span>收款账户</span>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="开户名/姓名:"> {{tableData.bankAccount.payee}} </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="开户行/网上支付方式:">
                {{tableData.bankAccount.bankName || tableData.bankAccount.lineAccount}}
              </el-form-item>
              <el-form-item label="账号">
                {{tableData.bankAccount.cardNo}}
              </el-form-item>
            </el-form>
          </div>
        </div> -->

        <!-- <div class="box-has-border" v-if="tableData.deposit">
          <div class="text-center title">
            <span>提现信息</span>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="280px">
              <el-form-item label="账号余额:"> {{tableData.deposit.accountBalance}} </el-form-item>
              <el-form-item label="已冻结金额:"> {{tableData.deposit.freezingAmount}} </el-form-item>
            </el-form>
          </div>
          <div class="half-width">
            <el-form ref="chosenData" label-width="190px">
              <el-form-item label="可用金额:"> {{tableData.deposit.accountAvailable}} </el-form-item>
              <el-form-item label="提现金额:"> {{tableData.withdrawAmount}} </el-form-item>
            </el-form>
          </div>
        </div> -->

        <!-- <console :withDrawId="withDrawId"></console> -->
        <div class="box-has-border">
          <div class="text-center title" v-if="checkConfirmStatus == 1">
            <span>操作信息</span>
          </div>
          <div class="form" v-if="checkConfirmStatus == 1">
            <el-form label-width="180px">
              <el-form-item label="操作备注:">
                <el-input type="textarea" v-model="remark" :rows="5" placeholder="请输入内容">
                </el-input>
              </el-form-item>
            </el-form>
          </div>
          <div class="half-width" v-if="tableData.confirmStatus == 1">
            <span class="instruction"> 当前可执行操作:</span>
            <span class="operation-part">
              <el-button class="mini-search-btn" @click="operate(2)" :disabled="disabledShow">确认付款</el-button>
              <el-button class="mini-search-btn" @click="operate(3)" :disabled="disabledShow">拒绝</el-button>
            </span>
          </div>
          <el-table :data="tableDataLogs" border style="width: 100%" header-row-class-name="header-row" class="tableCenter">
            <el-table-column align="center" prop="operationName" label="操作者"> </el-table-column>
            <el-table-column align="center" prop="createTime" :formatter="timeFormat" label="操作时间"> </el-table-column>
            <el-table-column align="center" prop="operationType" :formatter="operationTypeLogs" label="状态"> </el-table-column>
            <el-table-column align="center" prop="remark" label="备注"> </el-table-column>
          </el-table>
        </div>

        <el-dialog :visible="CardNoShow" :before-close="closeCardNoShow" width="30%" title="确认付款" center>
          <div style="text-align: center;">
            <span>请选择付款账户：</span>
            <el-select v-if="CardNoShow" v-model="companyCardNo" size="mini" clearable filterable style="width: 320px;">
              <el-option v-for="item in bAccountList" :key="item.id" :label="item.bankNoStr" :value="item.cardNo">
              </el-option>
            </el-select>
          </div>
          <div style="text-align: center;margin: 35px 0 0px;">
            <el-button class="mini-search-btn" @click="operateSure(2)" :disabled="disabledShow">确认付款</el-button>
            <el-button class="mini-search-btn" @click="operateSure(3)" :disabled="disabledShow">取消</el-button>
          </div>
        </el-dialog>
      </div>

    </section>
  </main>
</template>
<script>
import { getWithdrawDetail, getWithdrawlogs } from '@/views/financial/financialData'
import { confirmTypeFormatter, payType } from '@/views/financial/financialUtils'
import {parseTime} from '@/utils/index'
import { timeFormat } from "@/utils/timeFormat"

export default {
  name: 'withdrawDetail',
  components: { console } ,
  activated(){
    this.updateMainData() // 提现详情
  },
  data(){
    return {
      tableData: [], // 主要数据
      tableDataLogs: [], // 日志主要数据
      remark: '',
      disabledShow: false,
      loading: '',
      loading1: '',
      CardNoShow: false,
      bAccountList: [],
      companyCardNo: '',
      loading2: false,
    }
  },
  computed: {
    checkConfirmStatus() {
      return this.tableData.confirmStatus
    }
  },
  methods:{
    closeCardNoShow() {
      this.CardNoShow = false;
    },
    bankAccountList() {
			this.$api.get(this,'admin/u/po/bankAccount/list').then(res => {
        if (res.success) {
          this.bAccountList = res.allBankAccounts
        }
			})
		},
    operate(type) { // 确认操作
      if(type == 2) {
        if(this.tableData.withdrawType == 2) { // 网上支付（不变）
          this.sureConfirm(type)
        }else if(this.tableData.withdrawType == 1) { // 银行汇款（弹窗）
          this.bankAccountList()
          this.CardNoShow = true;
        }
      }else {
        this.sureConfirm(type)
      }
    },
    operateSure(type) { // 弹窗选择汇款人的银行汇款确认
      if(type == 3) {
        this.closeCardNoShow()
      }else if(type == 2) {
        this.$confirm('确认该操作，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(_ => {
          this.loading = this.$loading({
            lock: true,
            text: '确认操作中...请稍等',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          setTimeout(() => { // 无法收到反馈则6S结束动画
            this.loading.close();
          }, 6000);
          this.$api.put(this,'admin/u/p/withdraw/confirm',{id: this.$route.query.id, confirmType: type, remark: this.remark,companyCardNo:this.companyCardNo}).then(res => {
            this.disabledShow = false;
            if(res.code == 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 3 * 1000,
              })
              this.CardNoShow = false
              this.loading.close()
              this.updateMainData()
            }else if(res.code === 1088){
              this.updateMainData()
            }
          })
        })

      }
    },
    sureConfirm(type){ // 网上支付支付确认
      if(type == 2) { // 同意
        this.$confirm('确认该操作，是否继续？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true
        }).then(_ => {
          this.loading = this.$loading({
            lock: true,
            text: '确认操作中...请稍等',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          setTimeout(() => { // 无法收到反馈则6S结束动画
            this.loading.close();
          }, 6000);
          this.$api.put(this,'admin/u/p/withdraw/confirm',{id: this.$route.query.id, confirmType: type, remark: this.remark}).then(res => {
            this.disabledShow = false;
            if(res.code == 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 3 * 1000,
                onClose: () => {}
              })
              this.loading.close();
              this.updateMainData()
            }else if(res.code === 1088){
              this.updateMainData()
            }
          })
        })
      }else {
        this.$confirm('确定拒绝该提现申请?','提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
          center: true,
        }).then(_ => {
          this.loading1 = this.$loading({
            lock: true,
            text: '拒绝中...请稍等',
            spinner: 'el-icon-loading',
            background: 'rgba(0, 0, 0, 0.7)'
          });
          setTimeout(() => { // 无法收到反馈则6S结束动画
            this.loading1.close();
          }, 6000);
          this.$api.put(this,'admin/u/p/withdraw/confirm',{id: this.$route.query.id, confirmType: type, remark: this.remark}).then(res => {
            this.disabledShow = false;
            if(res.code == 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 3 * 1000,
                onClose: () => {}
              })
              this.loading1.close();
              this.updateMainData()
            }else if(res.code === 1088){
              this.updateMainData()
            }
          })
        })
      }
    },
    cancel() { // 返回操作
      this.$router.push({ name: 'drawing'})
    },
    updateMainData(){ // 提现详情数据接口
      this.loading2 = true;
      getWithdrawDetail(this, {id: this.$route.query.id}).then(res => {
        this.tableData = res.withdraw
        res.code == 0 ? this.loading2 = false : this.loading2 = false
      })
      this.$http.withdrawApplyLogList(this, {page: 1, size: 1000}).then(res => {
      // getWithdrawlogs(this,{id: this.$route.query.id}).then(res => {
        this.tableDataLogs = res.data.list
      })
    },
    timeFormatter(cellValue){ // 时间格式化
      return parseTime(cellValue)
    },
    confirmTypeFormatter(cellValue){ // 提现状态格式化
      return confirmTypeFormatter(cellValue)
    },
    payType(cellValue) { // 支付方式格式化
      return payType(cellValue)
    },
    operationTypeLogs(row, col, val) {// 操作日志状态
      switch(val) {
        case 1:
          return "提现创建";
          break;
        case 2:
          return "提现确认支付";
          break;
        case 3:
          return "提现拒绝";
          break;
      }
    },
    timeFormat(row, col, val) { // 时间戳转换
      return timeFormat(val)
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss">
.draw-detail{
  background-color: white;
  padding-bottom: 30px;
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
  .box-has-border{
    overflow: hidden;
      .text-center{
        text-align: center;
      }
     .half-width{
      width: 50%;
      box-sizing: border-box;
      margin-bottom: 20px;
      .instruction{
        padding-left: 50px;
      }
    }
  }
  .header-row {
    background-color: $table-header-bg;
    th {
      padding: 5px;
      text-align: center;
    }
  }
  div.el-tabs__nav{
    margin-left: 30px;
  }
}
</style>
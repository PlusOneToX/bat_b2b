<template>
  <div>
    <div class="user rl-margin-zero">
        <!--公共头部-->
      <Header :userState="userState"></Header>
        <!--主内容-->
        <div class="user-main rl-clear rl-margin-zero">
          <!--公共左边-->
          <Left></Left>
          <div class="user-right rl-fr rl-padding-horizontal-lllg rl-bg-white rl-padding-top-default rl-padding-bottom-double">
            <div class="content">
              <div class="title rl-bg-gray-xm">
                <div class="rl-padding-left-lllg title-cons rl-text-white rl-text-mid">{{$t('UserCenter.Cash')}}</div>
              </div>
              <div class="get-money rl-padding-top-lllg rl-bdb-gray-sm">
                <div class="item rl-clear">
                  <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('UserCenter.AccountMoney')}}</span>
                  <div class="rl-fl rl-margin-left-double">￥{{depositDetail.accountBalance}}</div>
                </div>
                <div class="item rl-clear">
                  <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('UserCenter.AvailableBalance')}}</span>
                  <div class="rl-fl rl-margin-left-double">￥{{depositDetail.accountAvailable}}</div>
                </div>
                <div class="item rl-clear">
                  <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('UserCenter.CashWithdrawalAmount')}}</span>
                  <div class="enterInput rl-fl rl-margin-left-double">￥<input class="common-input" type="text" @blur="changeSum" placeholder="请输入" v-model="rechargeAmount" onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"></div>
                </div>
                <div class="item rl-clear rl-padding-top-xxxxs">
                  <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('UserCenter.Remarkes')}}</span>
                  <div class="enterText rl-fl rl-margin-left-double">
                    <textarea class="common-text rl-text-mid" name="" id="" cols="30" rows="10" v-model="remark"></textarea>
                  </div>
                </div>
              </div>
              <div class="pays">
                <div class="pay-style rl-padding-top-default rl-padding-bottom-default">
                  <!--<div class="rl-fl rl-margin-right-default rl-margin-left-default"><input type="radio" checked="checked" class="chenked unchecked"><span class="rl-margin-left-xxxs rl-text-bold">网上支付</span></div>
                  <div class="rl-fl rl-margin-left-default"><input type="radio" checked="checked" class="chenked unchecked"><span class="rl-margin-left-xxxs rl-text-bold">银行汇款</span></div>-->
                  <div class="rl-fl" v-for="item in isPayTypeList" :key="item.id"><!--<span @click="getIsTaxpayer(item.index)" name="showpri" checked="checked" class="chenked" :class="{'haschecked':(item.index+1) === gather}"></span>--><span class="rl-margin-right-mid">{{item.text}}</span></div>
                </div>
                <div class="pay-account rl-padding-top-xxxs rl-padding-bottom-xxxxs rl-bg-gray-xm rl-margin-bottom-default">
                  <div class="item rl-clear" v-if="this.gather === 2">
                    <span class="rl-fl enterLeft rl-tr rl-text-bold">提款账号</span>
                    <div class="enterInput rl-fl rl-margin-left-double"><input v-model="lineAccount" class="common-input" type="text" placeholder="支付宝，财付通账号" onkeyup="this.value=this.value.replace(/\D/g,'')"></div>
                  </div>
                  <div class="item rl-clear" v-if="this.gather === 1">
                    <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('Register.BankAccountName')}}</span>
                    <div class="enterInput rl-fl rl-margin-left-double"><input v-model="userInfo.bankAccountName" class="common-input" type="text" placeholder="请输入" readonly></div>
                  </div>
                  <div class="item rl-clear" v-if="this.gather === 1">
                    <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('Register.BankDeposit')}}</span>
                    <div class="enterInput rl-fl rl-margin-left-double"><input v-model="userInfo.bankDepositName" class="common-input" type="text" placeholder="请输入" readonly></div>
                  </div>
                  <div class="item rl-clear" v-if="this.gather === 1">
                    <span class="rl-fl enterLeft rl-tr rl-text-bold">{{$t('Register.BankAccount')}}</span>
                    <div class="enterInput rl-fl rl-margin-left-double"><input v-model="userInfo.bankAccount" class="common-input" type="text" placeholder="请输入" readonly></div>
                  </div>
                </div>
              </div>
              <div class="submit">
                <div class="submit-cons rl-clear rl-margin-zero">
                  <div @click="submitApply" class="apply rl-fl rl-tc rl-bg-blue-xs rl-text-white cursor-pointer"  v-if="onceAgain === true">{{$t('UserCenter.SubmitApplication')}}</div>
                  <div class="apply rl-fl rl-tc rl-bg-blue-xs rl-text-white cursor-pointer"  v-else>{{$t('UserCenter.SubmitApplication')}}</div>
                  <div @click="resetTable" class="reset rl-fr rl-tc rl-bg-blue-xs rl-text-white cursor-pointer">{{$t('UserCenter.ResetForm')}}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
    </div>
  </div>
</template>

<script>
import Header from '@/components/Header.vue'
import Left from '@/components/Left.vue'
import {loginOut} from '@/assets/js/common.js'
import GD from '@/assets/js/globalData'
export default {
  name: 'Withdrawal',
  components: {
    Header,
    Left
  },
  data () {
    return {
      userState: 2,
      userInfo: {},
      rechargeAmount: '',
      remark: '',
      gather: 1, // 1'银行汇款 2 网上支付
      isPayTypeList: [ // 支付类型
        {index: 0, text: this.$t('UserCenter.CashWithdrawalAccount')}
        // {index: 1, text: '网上支付'}
      ],
      lineAccount: '', // 提款账号
      cardNo: '', // 银行账号
      payee: '', // 收款人姓名
      bankName: '', // 开户行全称
      depositDetail: {},
      onceAgain: true, // 控制多次点击
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: 'zh-RMB' // 语种
    }
  },
  methods: {
    fLangChange (value) {
      window.localStorage.setItem('bLang', value);
      this.$i18n.locale = value.split('-')[0];
    },
    changeSum () {
      this.rechargeAmount = Number(this.rechargeAmount).toFixed(2)
    },
    // 信息选择按钮框
    getIsTaxpayer (index) {
      this.gather = index + 1
    },
    // 提交申请
    submitApply () {
      this.onceAgain = false
      if (this.rechargeAmount > this.depositDetail.accountBalance) {
        this.onceAgain = true
        this.$message.warning('提现金额不能大于账户余额')
        return false
      }
      if (this.rechargeAmount > this.depositDetail.accountAvailable) {
        this.onceAgain = true
        this.$message.warning('可用余额不足')
        return false
      }
      if (this.gather === 1) {
        if (this.rechargeAmount === '') {
          this.onceAgain = true
          this.$message.warning('提现金额不能为空')
          return false
        }
        var jsonp = {withdrawAmount: this.rechargeAmount, withdrawType: this.gather, remark: this.remark, cardNo: this.userInfo.bankAccount, payee: this.userInfo.bankAccountName, bankName: this.userInfo.bankDepositName}
        this.$api.post(this, 'user/u/deposit/withdraw', jsonp).then(res => {
          if (res.code === 0) {
            this.onceAgain = true
            this.$message.success('申请成功！')
            this.resetTable()
          } else if (res.code === 3) {
           
          } else {
            this.onceAgain = true
          }
        })
      } else {
        if (this.lineAccount === '') {
          this.onceAgain = true
          this.$message.warning('提款账号不能为空')
          return false
        }
        var json = {withdrawAmount: Number(this.rechargeAmount), withdrawType: this.gather, remark: this.remark, lineAccount: this.lineAccount}
        this.$api.post(this, 'user/u/deposit/withdraw', json).then(res => {
          if (res.code === 0) {
            this.onceAgain = true
            this.$message.success('申请成功！')
            this.resetTable()
          } else {
            this.onceAgain = true
          }
        })
      }
    },
    // 重置表单
    resetTable () {
      this.rechargeAmount = ''
      this.remark = ''
      this.gather = 1
      this.lineAccount = ''
      this.cardNo = ''
      this.payee = ''
      this.bankName = ''
    },
    // 获取账户余额
    getDeposit () {
      this.$api.get(this, 'user/u/deposit').then(res => {
        if (res.code === 0) {
          this.depositDetail = res.deposit
        } else if (res.code === 3) {
         
        }
      })
    },
    getUserInfo () {
      var myDate = new Date()
      this.$api.get(this, 'user/u/user?' + myDate.getMinutes() + myDate.getSeconds()).then(res => {
        if (res.code === 0) {
          this.userInfo = res.user
        }
      })
    }
  },
  created () {
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem('bLang') ? window.localStorage.getItem('bLang') : 'zh-RMB';
    this.getDeposit()
    this.getUserInfo()
  }
}
</script>

<style scoped="scoped" lang='less'>
  .user{width: 100%;}
  .user-main{width: 1190px}
  .user-right {
    width: 960px;
    .content {
      .title {
        width: 100%;
        .title-cons {
          width: 85px;
          height: 35px;
          line-height: 35px;
          background: url("../../assets/images/jiao-blue1.png") center center no-repeat;
          img {
            vertical-align: -3px;
          }
        }
      }
      .get-money{
        padding-left: 75px;
        padding-bottom: 70px;
        .item{
          margin-bottom: 10px;
          height: 30px;
          line-height: 30px;
          .enterLeft{min-width: 65px}
          .enterInput{
            width: 300px;
            height: 30px;
            .common-input{
              margin-left: 3px;
              padding-left: 20px;
              border: 1px solid #ccc;
              width: 235px;
              height: 30px;
              line-height: 30px;
            }
          }
          .enterText{
            width: 565px;
            height: 85px;
            .common-text{
              padding-left: 20px;
              width: 543px;
              height: 80px;
              border: 1px solid #ccc;
            }
          }
        }
      }
      .pays{
        .pay-style{
          margin-left: 20px;
          width: 255px;
          height: 30px;
          .chenked{
            cursor: pointer;
            width: 19px;
            height: 19px;
            display: inline-block;
            vertical-align: -5px;
            -webkit-appearance: none;
            appearance: none;
            outline: none;
            margin-right: 5px;
            font-size: 14px;
            color: #333;
            background: url(../../assets/images/un-selected.png) no-repeat center center;
          }
          .haschecked{
            background: url(../../assets/images/selected.png) no-repeat center center;
          }
        }
        .pay-account{
          border:1px solid #ccc;
          .item{
            margin-left: 64px;
            margin-bottom: 10px;
            height: 30px;
            line-height: 30px;
            .enterLeft{min-width: 75px}
            .enterInput{
              width: 255px;
              height: 30px;
              border: 1px solid #ccc;
              .common-input{
                padding-left: 20px;
                border: 0;
                width: 235px;
                height: 30px;
                line-height: 30px;
              }
            }
            .enterText{
              width: 565px;
              height: 85px;
              border: 1px solid #ccc;
              .common-text{
                padding-left: 20px;
                width: 543px;
                height: 80px;
                border: 0;
              }
            }
          }
        }
      }
      .submit{
        width: 100%;
        .submit-cons{
          width: 242px;
          div{
            width: 105px;
            height: 35px;
            line-height: 35px;
            border-radius: 5px;
          }
        }
      }
    }
  }
</style>

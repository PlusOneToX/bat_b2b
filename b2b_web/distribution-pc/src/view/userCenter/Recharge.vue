<template>
  <div>
    <div class="user rl-margin-zero">
      <!--公共头部-->
      <Header :userState="userState"></Header>
      <!--主内容-->
      <div class="user-main rl-clear rl-margin-zero">
        <!--公共左边-->
        <Left></Left>
        <div class="user-right rl-fr rl-bg-white rl-margin-top-default rl-padding-bottom-double">
          <div class="content">
            <h6 class="user-right-title">{{$t('UserCenter.Recharge')}}</h6>
            <div class="get-money rl-padding-top-default">
              <div class="item rl-clear">
                <span class="rl-fl enterLeft">{{$t('UserCenter.AccountMoney')}}</span>
                <div class="rl-fl price">
                  <span>{{$i18n.locale === 'zh' ? '￥' : '$'}}</span>
                  {{$i18n.locale === 'zh' ?depositDetail.accountBalance:(Number(depositDetail.accountBalance)*exchange).toFixed(2)}}
                </div>
              </div>
              <!-- 充值金额 -->
              <div class="item rl-clear rl-relative">
                <span class="rl-fl enterLeft">{{$t('UserCenter.rechargeMmount')}}</span>
                <div class="enterInput rl-fl">
                  {{$i18n.locale === 'zh' ? '￥' : '$'}}
                  <input
                    class="common-input"
                    type="text"
                    @focus="inputSum"
                    @blur="changeSum"
                    v-model="rechargeAmount"
                    placeholder="请输入金额"
                    onkeyup="value=value.replace(/[^\d{1,}\.\d{1,}|\d{1,}]/g,'')"
                  />
                </div>
              </div>
              <!-- 备注 -->
              <div class="item rl-clear">
                <span class="rl-fl enterLeft">{{$t('UserCenter.Remarkes')}}</span>
                <div class="enterText rl-fl">
                  <textarea
                    class="common-text"
                    placeholder="请输入......（最多200字）"
                    maxlength="200"
                    cols="30"
                    rows="10"
                    v-model="remark"
                  ></textarea>
                </div>
              </div>
              <!-- 支付方式 -->
              <div class="item rl-clear pay-money">
                <div class="rl-fl enterLeft">{{$t('Foot.PaymentMethod')}}</div>
                <div class="rl-fl">
                  <ul>
                    <li  @click="changePayStyle($event,1)" class="zhi-pay"
                      :class="{'current':this.rechargeType === 1}" >{{$t('P.Alipay')}}</li>  <!--支付宝-->
                    <li @click="changePayStyle($event,2)" class="wechat-pay"
                      :class="{'current':this.rechargeType === 2}" >{{$t('P.WechatPay')}}</li><!--微信-->
                  </ul>
                </div>
              </div>
            </div>
               
            <!--提交表单 -->
            <div class="submit rl-tc">
              <span @click="submitApply" class="apply rl-tc rl-bg-blue-xs rl-text-white cursor-pointer" v-if="onceAgain === true">{{$t('UserCenter.SubmitApplication')}}</span>
              <span class="apply rl-tc rl-bg-gray-sm rl-text-white" v-else>{{$t('UserCenter.SubmitApplication')}}</span>
              <span @click="resetTable" class="reset rl-tc rl-bg-blue-xs rl-text-white cursor-pointer">{{$t('UserCenter.ResetForm')}}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!--支付二维码-->
    <div class="cover" v-if="showCode"></div>
    <div v-show="showCode" class="ewm">
      <div
        class="rl-margin-bottom-default rl-padding-top-xxxs rl-padding-bottom-xxxs rl-padding-left-xxxs rl-tl"
      >
        <span @click="shutLog" class="shut cursor-pointer"></span>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === true">
        <p class="rl-tc">
          距离二维码过期还剩
          <span
            class="rl-text-blue-xs rl-margin-right-xxxxs rl-margin-left-xxxxs"
          >{{qrCodeTime}}</span>秒，
        </p>
        <p class="rl-tc">过期后请刷新页面重新获取二维码。</p>
      </div>
      <div class="rl-padding-bottom-lllg time-state" v-show="qrCodeState === false">
        二维码已过期，请
        <span class="rl-text-orange-sm rl-margin-right-xxxxs rl-margin-left-xxxxs">刷新</span>二维码重新获取
      </div>
      <div class="qrcode">
        <div id="qrcode"></div>
        <div class="state" v-show="qrCodeState === false"></div>
        <div class="state-other" v-show="qrCodeState === true"></div>
        <div class="fair-img cursor-pointer" @click="submitApply(2)" v-show="qrCodeState === false">
          <img src="../../assets/images/refresh.png" alt />
        </div>
      </div>
      <div class="zhifu rl-clear">
        <div class="icon rl-margin-right-mid rl-fl" v-if="this.rechargeType === 1">
          <img src="../../assets/images/zhifubao.png" alt />
        </div>
        <div class="rl-fl" v-if="this.rechargeType === 1">
          <p class="rl-tl spc-p">
            请使用
            <span class="rl-text-orange-sm">支付宝</span>扫一扫
          </p>
          <p class="rl-tl">扫描二维码支付</p>
        </div>
        <div class="icon rl-margin-right-mid rl-fl" v-if="this.rechargeType === 2">
          <img src="../../assets/images/wechat.png" alt />
        </div>
        <div class="rl-fl" v-if="this.rechargeType === 2">
          <p class="rl-tl spc-p">
            请使用
            <span class="rl-text-orange-sm">微信</span>扫一扫
          </p>
          <p class="rl-tl">扫描二维码支付</p>
        </div>
      </div>
    </div>
    <!--支付成功-->
    <div class="cover" v-if="showPay"></div>
    <div v-show="showPay" class="pay-result">
      <div
        class="rl-margin-bottom-default rl-bdb-gray-sm rl-padding-top-xxxs rl-padding-bottom-xxxs rl-bg-gray-mm rl-tc"
      >支付结果</div>
      <span @click="shutPay" class="shut cursor-pointer"></span>
      <div class="rl-margin-top-double rl-margin-bottom-default">
        <img src="../../assets/images/pay-success.png" alt />
      </div>
      <p class="rl-tc rl-text-lg rl-text-gray">支付成功</p>
      <div class="result">
        <span
          @click="shutPay"
          class="rl-tc rl-bg-blue-mm rl-text-white rl-text-mid cursor-pointer"
        >确定</span>
      </div>
    </div>
    <!--点击图片放大-->
    <div class="cover" v-if="showImgurl"></div>
    <div class="pro-cover-img rl-relative" v-if="showImgurl">
      <img width="100%" :src="imgurl" alt />
      <span @click="shutLogImg" class="shut cursor-pointer"></span>
    </div>
  </div>
</template>

<script>
import Header from "@/components/Header.vue";
import Left from "@/components/Left.vue";
import UpLoad from "@/components/UpLoad.vue";
import QRCode from "qrcodejs2";
import {  isNumber } from "@/assets/js/common.js";
import GD from "@/assets/js/globalData";
import {  fomatFloat } from '@/assets/js/common.js';
// liu--
import {userDeposit,depositRecharge,depositRechargeStatus} from '@/apiService/api'
export default {
  name: "Recharge",
  components: {
    Header,
    Left,
    UpLoad,
  },
  data() {
    return {
      userState: 2,
      rechargeAmount: "",  //充值金额
      imgurl: "",
      remark: "",  //备注
      gather: 0,
      rechargeType: 1,
      dialogImageUrl: "",
      dialogVisible: false,
      depositDetail: {},
      showCode: false,
      showPay: false,
      showImgurl: false,
      outTradeNo: "",
      qrCodeState: true, // 二维码状态
      qrCodeTime: 300, // 二维码失效时间
      bankAccountList: [],
      payOnlyCode: "", // 支付唯一吗
      onceAgain: true, // 控制多次点击
     
      useLang: false, // 是否使用多语种
      langList: GD.langList, // 语种列表
      lang: "zh-RMB", // 语种
      exchange:1,  //汇率
    };
  },
  methods: {
    
   
    shutLog() {
      // 关闭支付弹框
      this.qrCodeTime = 300;
      this.showCode = false;
      this.qrCodeState = true;
      this.resetTable();
      this.paySuccessState(1);
    },
    shutPay() {
      this.showPay = false;
    },
   
    shutLogImg() {
      this.showImgurl = false;
    },
    changeSum() {
      if (this.rechargeAmount > 100000 || this.rechargeAmount < 0.1) {
        if (
          isNumber(this.rechargeAmount) &&
          (this.rechargeAmount > 100000 || this.rechargeAmount < 0.1)
        ) {
          this.$message.warning("单次充值金额限制：0.10-100000.00！");
        }
        this.rechargeAmount = "";
        this.onceAgain = false;
      } else {
        this.rechargeAmount = fomatFloat(Number(this.rechargeAmount),2);
        this.onceAgain = true;
      }
    },
    inputSum() {
      this.onceAgain = true;
    },
   
    // 改变支付方式
    changePayStyle(event, type) {
      this.rechargeType = type;   
    },
    // 提交申请
    submitApply(type) {
      this.onceAgain = false;
      if (this.rechargeAmount === "") {
        this.onceAgain = true;
        this.$message.warning(this.$t("UserCenter.RechargeAmount"));
        return false;
      }
      // 重新获取二维码
      if(type==2){
          this.qrCodeTime = 300;
          this.qrCodeState = true;
          clearInterval(this.intervalids);
      }
      let id=localStorage.getItem('userId');
      let rechargeAmount=this.$i18n.locale === 'zh'? this.rechargeAmount:(Number(this.rechargeAmount)/this.exchange).toFixed(2)
      let params={
        amount:rechargeAmount,  // 充值金额
        distributorId:id,  //分销商id
        payWay:this.rechargeType,  //支付类型：1.支付宝,2.微信
        remark:this.remark,
      }
      depositRecharge(params).then(res=>{
           if(res.success){
             this.onceAgain = true;
             if(params.payWay===1){
                this.Url = res.data.alipayResp.codeUrl;
                this.outTradeNo = res.data.alipayResp.outTradeNo;
             }else if(params.payWay===2){
                this.Url = res.data.wxResp.codeUrl;
                this.outTradeNo = res.data.wxResp.outTradeNo;
             }
              this.intervalids = setInterval(() => {
                this.qrCodeTime--;
                if (this.qrCodeTime === 0) {
                  this.qrCodeState = false;
                  clearInterval(this.intervalids);
                }
              }, 1000);
              this.makeCodes();
           }else{
              this.onceAgain = true;
           }
      })
      
    },
   
    // 生成二维码--Y
    makeCodes() {
      this.showCode = true;
      document.getElementById("qrcode").innerHTML = "";
      var qrcode = new QRCode(document.getElementById("qrcode"), {
        width: 250,
        height: 250,
      });
      qrcode.makeCode(this.Url);
      this.paySuccessState();
    },


    // 监听支付成功状态
    paySuccessState(num) {
      var secondsArea = 600; // 设置支付时间10分钟
      
      if (num === 1) {
        // 关闭支付窗口
        clearInterval(this.intervalid);
      } else {
        clearInterval(this.intervalid); // 先关闭之前的定时器
        this.intervalid = setInterval(() => {
          secondsArea--; 
          if (secondsArea <= 600 && secondsArea > 540) {
            if (secondsArea % 5 === 0) {
              this.checkOrderFun();
            }
          } else if (secondsArea <= 540 && secondsArea > 300) {
            if (secondsArea % 10 === 0) {
                this.checkOrderFun()
            }
          } else if (secondsArea <= 300 && secondsArea > 0) {
            if (secondsArea % 20 === 0) {
               this.checkOrderFun();
            }
          } else {
            this.$message.error(this.$t("UserCenter.PaymentTimedOut"));
            clearInterval(this.intervalid);
          }
        }, 1000);
      }
    },
    // 查询订单状态
    checkOrderFun(){
        var secondsAreas = 5; // 设置支付时间10分钟
        let id=localStorage.getItem('userId');
        let params={
          distributorId:id,
          outTradeNo:this.outTradeNo,
          payWay:this.rechargeType
        }
        depositRechargeStatus(params).then((res) => {
          if (res.success) {
            if (res.data) {
              // 支付成功
              this.$message.success(
                this.$t("UserCenter.PaymentSuccessful")
              );
              this.showCode = false;
              this.showPay = true;
              this.resetTable();
              this.getDeposit();
              this.intervalids = setInterval(() => {
                secondsAreas--;
                if (secondsAreas === 0) {
                  this.showPay = false;
                  clearInterval(this.intervalids);
                }
              }, 1000);
              clearInterval(this.intervalid);
            }
          }
        });
    },
    // 重置表单
    resetTable() {
      this.rechargeAmount = "";
      this.remark = "";
      this.payOnlyCode = "";
      this.imgurl = "";
    },
    
    
    // 获取账户余额-Y
    getDeposit() {
      let id=localStorage.getItem('userId');
        userDeposit({id:id}).then((res) => {
          if (res.success) {
            console.log('账户余额：',res.data);
            this.depositDetail = res.data;
          } 
        });
    },
   
  },
  created() {
    this.exchange=Number( localStorage.getItem('exchange'));
    this.useLang = this.$b2bConfig.lang;
    this.lang = window.localStorage.getItem("bLang")
      ? window.localStorage.getItem("bLang")
      : "zh-RMB";
    this.getDeposit();
  },
};
</script>

<style scoped="scoped" lang='less'>
@import url("../../assets/less/variable.less");
.user {
  width: 100%;
}
.user-main {
  width: 1200px;
}
.min-img {
  width: 110px;
  img {
    width: 100%;
    height: 110px;
  }
}
.user-right {
  width: 1030px;
  .user-right-title {
    padding-bottom: 10px;
    border-bottom: 1px solid @bdLightColor;
    font-size: 20px;
  }
  .content {
    padding: 24px 40px 0;
    border: 2px solid @bdLightColor;
    border-radius: 8px;
  }
  .get-money {
    .item {
      margin-bottom: 20px;
      line-height: 30px;
      .enterLeft {
        min-width: 105px;
        font-size: 12px;
        color: @lighterBlack;
      }
      .price {
        font-size: 24px;
        color: @red;
        span {
          font-size: 18px;
        }
      }
      .enterInput {
        width: 300px;
        height: 30px;
        .common-input {
          margin-left: 3px;
          padding-left: 10px;
          width: 235px;
          height: 30px;
          line-height: 30px;
          border: 1px solid @bdLightColor;
        }
      }
      .state {
        position: absolute;
        top: 0;
        right: 160px;
      }
      .enterText {
        width: 565px;
        .common-text {
          padding: 8px 10px;
          width: 543px;
          height: 80px;
          border: 1px solid @bdLightColor;
          border-radius: 4px;
        }
      }
    }
    .pay-money {
      margin-top: 20px;
      line-height: 40px;
      overflow: hidden;
      ul {
        li {
          float: left;
          width: 130px;
          height: 40px;
          padding-left: 35px;
          line-height: 40px;
          font-size: 14px;
          color: @lighterBlack;
          text-align: center;
          border: 1px solid @payBd;
          border-radius: 4px;
          cursor: pointer;
          & + li {
            margin-left: 30px;
          }
          &.zhi-pay {
            padding-left: 7px;
            background: url("../../assets/images/order/zhi-pay.png") no-repeat
              14px center;
          }
          &.wechat-pay {
            padding-left: 14px;
            background: url("../../assets/images/order/wechat-pay.png")
              no-repeat 14px center;
          }
          &.bank-pay {
            padding-left: 14px;
            background: url("../../assets/images/order/bank-pay.png") no-repeat
              14px center;
          }
        }
        li:hover,
        li.current {
          position: relative;
          border: 1px solid @blue;
          overflow: hidden;
          &::after {
            content: "";
            position: absolute;
            right: 0;
            bottom: 0;
            width: 30px;
            height: 27px;
            background: url("../../assets/images/order/pay-active.png")
              no-repeat;
          }
        }
      }
    }
  }
  .collect {
    width: 100%;
    .pay-style {
      margin-left: 40px;
      ul {
        li {
          width: 614px;
          font-size: 13px;
          .chenked {
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
            background: url(../../assets/images/un-selected.png) no-repeat
              center center;
          }
        }
        li.current {
          .has-chenk {
            background: url(../../assets/images/selected.png) no-repeat center
              center;
          }
        }
      }
    }
    .account {
      padding-top: 20px;
      margin-left: 64px;
      margin-bottom: 10px;
      .enterLeft {
        min-width: 75px;
      }
      .enterInput {
        height: 30px;
        border: 1px solid #ccc;
        .common-input {
          padding-left: 20px;
          border: 0;
          width: 235px;
          height: 30px;
          line-height: 30px;
        }
        .file-input {
          border: 0;
        }
      }
      .fileInput {
        border: 0;
      }
      .enterText {
        width: 565px;
        height: 85px;
        border: 1px solid #ccc;
        .common-text {
          padding-left: 20px;
          width: 543px;
          height: 80px;
          border: 0;
        }
      }
    }
    .item {
      margin-left: 64px;
      margin-bottom: 10px;
      height: 30px;
      line-height: 30px;
      .enterLeft {
        min-width: 84px;
      }
      .enterInput {
        height: 30px;
        border: 1px solid #ccc;
        .common-input {
          padding-left: 20px;
          border: 0;
          width: 235px;
          height: 30px;
          line-height: 30px;
        }
        .file-input {
          border: 0;
        }
      }
      .paystyle {
        padding-left: 30px;
        padding-right: 40px;
        li {
          display: inline-block;
          border: 1px solid #c8c7cd;
        }
        li.current {
          color: #00c9dc;
          border: 2px solid #00c9dc;
        }
        .styles {
          margin-bottom: 35px;
          width: 130px;
          padding-top: 35px;
          padding-bottom: 35px;
        }
      }
      .fileInput {
        border: 0;
      }
      .enterText {
        width: 565px;
        height: 85px;
        border: 1px solid #ccc;
        .common-text {
          padding: 8px 10px;
          width: 340px;
          height: 80px;
          border: 0;
        }
      }
    }
  }
  .submit {
    width: 100%;
    padding: 30px 0;
    span {
      display: inline-block;
      margin: 0 10px;
      min-width: 100px;
      height: 40px;
      line-height: 40px;
      border-radius: 4px;
      padding: 0 20px;
      box-sizing: border-box;
    }
  }
}
/*弹框*/
.cover {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: #000;
  z-index: 99;
  opacity: 0.5;
}
.ewm {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: block;
  width: 500px;
  height: 490px;
  z-index: 99;
  margin: auto;
  background: #fff;
  text-align: center;
  border-radius: 10px;
  .shut {
    position: absolute;
    top: 10px;
    right: 10px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../../src/assets/images/wrong-de.png") no-repeat center
      center;
    background-size: 100%;
  }
  .time-state {
    width: 280px;
    height: 38px;
    margin: 0 auto;
    font-size: 15px;
  }
}
.pay-result {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  right: 0;
  display: block;
  width: 600px;
  height: 460px;
  z-index: 99;
  margin: auto;
  background: #fff;
  text-align: center;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../../src/assets/images/shut.png") no-repeat center
      center;
  }
  .result {
    margin-top: 60px;
    span {
      display: inline-block;
      width: 120px;
      height: 35px;
      line-height: 35px;
      border-radius: 5px;
    }
  }
}
#qrcode {
  position: relative;
  margin: 0 auto;
  width: 250px;
  height: 250px;
}
.qrcode {
  position: relative;
  margin: 0 auto;
  width: 280px;
  height: 280px;
  .state {
    position: absolute;
    top: -15px;
    left: 0;
    background: #b2b2b2;
    z-index: 99;
    opacity: 0.95;
    width: 280px;
    height: 280px;
  }
  .state-other {
    position: absolute;
    top: -15px;
    left: 0;
    width: 275px;
    height: 275px;
    border: 3px solid #e0e0e0;
  }
  .fair-img {
    position: absolute;
    top: 35%;
    left: 40%;
    z-index: 999;
    width: 49px;
  }
}
.zhifu {
  padding-top: 10px;
  width: 280px;
  margin: 0 auto;
  .icon {
    margin-left: 55px;
    width: 40px;
    img {
      width: 100%;
    }
  }
  .spc-p {
    padding-top: 2px;
  }
}
/*弹框图片放大*/
.pro-cover-img {
  position: fixed;
  top: 20%;
  left: 0;
  bottom: 0;
  right: 0;
  margin: auto;
  width: 400px;
  z-index: 99;
  .shut {
    position: absolute;
    top: -8px;
    right: -8px;
    display: block;
    width: 18px;
    height: 18px;
    background: url("../../../src/assets/images/shut.png") no-repeat center
      center;
  }
}
</style>

<template>
  <view class="distributorAppli">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>分销商申请{{ testText }}</view>
      </view>
    </view>
    <view class="distributorAppli-form">
      <view class="Appli-formPhone"
        ><input type="number" placeholder="请输入手机号" v-model="phone"
      /></view>
      <view class="Appli-formCode">
        <input type="number" placeholder="请输入验证码" v-model="code" />
        <text @click="getCode" v-if="times == 60">获取验证码</text>
        <text v-if="times < 60">{{ times }}S重新获取</text>
      </view>
    </view>
    <view class="apply-btn" @click="nextClick">下一步</view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  phoneVerify,
  distributorNextApply,
  commonConfig,
} from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  data() {
    return {
      phone: "",
      code: "",
      times: 60,
      distributorId: "",
      tipTextShow: false,
      tipText: "",
      timeinter: "",
      testText: "",
    };
  },
  onShow() {
    let that = this;
    const accountInfo = uni.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    console.log("小程序appid：", appId); // 小程序 appId

    uni.setStorageSync("appId", appId);
    uni.setStorageSync("themeColor", "#0076A5");
    let params = {
      gainUrlType: 6,
      wxProgramAppId: appId,
    };

    let baseUrl = "";
	
	// 测试
	baseUrl = "https://test.bat.com/"; 
	
	// 正式
	//baseUrl = "https://api.bat.com/";

    uni
      .request({
        method: "get",
        url: baseUrl + "platform/v1/web/tenant/url",
        data: params,

        dataType: "json",
      })
      .then((res) => {
        uni.removeStorageSync("tenantId");
        uni.removeStorageSync("tenantNo");
        uni.removeStorageSync("tenantHost");
        if (res[1].data.success) {
          let data = res[1].data.data;
          uni.setStorageSync("tenantId", data.tenantId);
          uni.setStorageSync("tenantNo", data.tenantNo);
          uni.setStorageSync("tenantHost", data.host);
          // 获取平台配置
          commonConfig({ id: data.tenantId }).then((res) => {
            if (res.success) {
              console.log(res.data.colour);
              uni.setStorageSync("themeColor", res.data.colour);
            }
          });
        }
      });
  },
  onLoad(option) {
    if (isH5) {
      this.distributorId = option.distributorId;
    } else {
      let url = decodeURIComponent(option.q);
      this.distributorId = url.split("?")[1].split("=")[1];
    }
  },
  methods: {
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 2000);
    },
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    nextClick() {
      let myreg = /^[1][2,3,4,5,7,6,8,9][0-9]{9}$/;
      let phone = this.phone;
      let code = this.code;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      if (code == "") {
        this.tipFun("验证码不能为空");
        return;
      }
      phoneVerify("PUT", { codeType: 1, phone: phone, code: code }).then(
        (res) => {
          if (res.success) {
            uni.navigateTo({
              url:
                "distributorDetails?phone=" +
                this.phone +
                "&distributorId=" +
                this.distributorId,
            });
          } else {
            this.tipFun(res.errMessage);
          }
        }
      );
    },

    // 获取验证码
    getCode() {
      this.times = 60;

      let that = this;
      clearInterval(that.timeinter);
      let phone = this.phone;
      let myreg = /^[1][2,3,4,5,7,6,8,9][0-9]{9}$/;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      uni.clearStorageSync("Font-Token");

      phoneVerify("POST", { codeType: 1, phone: phone }).then((res) => {
        if (res.success) {
          that.code = res.data.code;
          that.tipFun("验证码发送成功");

          that.timeinter = setInterval(() => {
            that.times--;
            if (that.times == 0) {
              clearInterval(that.timeinter);
              that.times = 60;
            }
          }, 1000);
        } else {
          that.tipFun(res.errMessage);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.distributorAppli {
  font-size: 28rpx;
  .distributorAppli-form {
    padding-top: calc(116rpx + var(--status-bar-height));
    /* #ifdef H5 */
    padding-top: 104rpx;
    /* #endif*/
    .Appli-formPhone {
      background: #fff;
      border-bottom: 1rpx solid $opacity-border;
      padding: 30rpx;
      input {
        font-size: 28rpx;
      }
    }
    .Appli-formCode {
      position: relative;
      background: #fff;
      display: flex;
      align-items: center;
      justify-content: space-between;
      input {
        padding: 30rpx;
        font-size: 28rpx;
        width: 420rpx;
      }
      text {
        position: absolute;
        right: 30rpx;
        color: $base-color1;
      }
    }
  }
  .apply-btn {
    width: 690rpx;
    height: 100rpx;
    line-height: 100rpx;
    text-align: center;
    background: $bg-gradient-btn;
    border-radius: 50rpx;
    margin: 100rpx auto;
    font-size: 32rpx;
    color: #fff;
  }
}
</style>

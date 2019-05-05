<template>
  <view class="register">
    <view class="register-box">
      <view class="register-title">注册</view>
      <view class="register-form">
        <view
          ><input
            class="register-phone"
            input
            placeholder="手机号"
            maxlength="11"
            v-model="phone"
            placeholder-style="color:#B9B9B9;"
            @input="ListeningIsHover"
        /></view>
        <view>
          <input
            class="register-password"
            placeholder="输入验证码"
            v-model="code"
            placeholder-style="color:#B9B9B9;"
            @input="ListeningIsHover"
          />
          <text @click="getCode" v-if="times == 60">获取验证码</text>
          <text v-if="times < 60">{{ times }}S</text>
        </view>
      </view>
      <view
        class="register-btn"
        @click="nextClick"
        :style="{ background: isHover ? themeColor : '#B9B9B9' }"
        >下一步</view
      >
    </view>
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { phoneVerify } from "../../common/api.js";
export default {
  data() {
    return {
      themeColor: "",
      providerList: [],
      phone: "",
      times: 60,
      code: "", //获取的验证码
      tipTextShow: false,
      tipText: "",
      isHover: false,
      timeinter: "",
    };
  },
  onLoad() {
    this.themeColor = uni.getStorageSync("themeColor");
  },
  methods: {
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 监听是否填写齐全
    ListeningIsHover() {
      if (this.phone != "" && this.code != "") {
        this.isHover = true;
      } else {
        this.isHover = false;
      }
    },
    nextClick() {
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
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
              url: "registerInform?phone=" + phone,
            });
          } else {
            this.tipFun(res.errMessage);
          }
        }
      );
    },

    // 获取验证码
    getCode() {
      clearInterval(this.timeinter);
      this.times = 60;
      let that = this;
      let phone = this.phone;
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      phoneVerify("POST", { codeType: 1, phone: phone }).then((res) => {
        if (res.success) {
          this.code = res.data.code;
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
.register {
  background: #fff;
  height: 100vh;
  font-family: PingFangSC-Regular, PingFang SC;
  .register-box {
    border-top: 1rpx solid #f3f4f8;
    padding: 56rpx 54rpx;
    .register-title {
      font-size: 62rpx;
      color: #000;
    }
    .register-form {
      margin: 20rpx auto;
      view {
        width: 622rpx;
        margin: 0 auto;
        display: flex;
        align-items: center;
        margin-top: 32rpx;
        border-bottom: 2rpx solid #f3f4f8;
        input {
          width: 622rpx;
          text-align: left;
          padding: 28rpx 0;
          font-size: 40rpx;
        }
        .register-password {
          width: 500rpx;
        }
        text {
          color: #333;
          font-size: 28rpx;
          text-align: right;
          width: 150rpx;
        }
      }
    }
    .register-btn {
      width: 622rpx;
      height: 100rpx;
      line-height: 100rpx;
      background: #b9b9b9;
      text-align: center;
      font-size: 36rpx;
      color: #fff;
      margin-top: 114rpx;
    }
  }
}
</style>

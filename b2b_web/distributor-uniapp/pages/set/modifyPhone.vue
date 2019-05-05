<template>
  <view class="modifyPhone">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>修改手机号</view>
      </view>
    </view>

    <view class="modifyPhone-module">
      <view class="modifyPhone-oldPhone">
        <text>当前手机号</text>
        <text>{{ oldPhone }}</text>
      </view>
      <view class="modifyPhone-newPhone">
        <input
          type="number"
          placeholder="请输入新手机号"
          maxlength="11"
          v-model="newPhone"
        />
      </view>
      <view class="modifyPhone-code">
        <input type="number" placeholder="请输入验证码" v-model="code" />
        <text @click="getCode" v-if="times == 60">获取验证码</text>
        <text v-if="times < 60">{{ times }}S重新获取</text>
      </view>
    </view>

    <view class="modifyPhone-btn" @click="confirmClick">提交</view>
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { phoneVerify, changePhone } from "../../common/api.js";
export default {
  data() {
    return {
      newPhone: "",
      oldPhone: "",
      code: "",
      times: 60,
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {
    this.oldPhone = uni.getStorageSync("userPhone");
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },

    toShopping() {
      uni.navigateTo({
        url: "../index/index",
      });
    },
    confirmClick() {
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      let phone = this.newPhone;
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
      let id = uni.getStorageSync("userId");
      let params = {
        codeType: 3,
        code: code,
        newPhone: this.newPhone,
        oldPhone: this.oldPhone,
      };
      changePhone(params).then((res) => {
        if (res.success) {
          this.toback();
          uni.setStorageSync("userPhone", this.oldPhone);
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 获取验证码
    getCode() {
      this.times = 60;
      let that = this;
      let phone = this.oldPhone;
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      phoneVerify("POST", { codeType: 3, phone: phone }).then((res) => {
        if (res.success) {
          that.code = res.data.code;
          let times = setInterval(() => {
            that.times--;
            if (that.times == 0) {
              clearInterval(times);
              that.times = 60;
            }
          }, 1000);
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.modifyPhone {
  .modifyPhone-module {
    background: #fff;
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    font-size: 28rpx;
    view {
      display: flex;
      justify-content: space-between;
      align-items: center;
    }
    .modifyPhone-oldPhone {
      padding: 30rpx;
      border-top: 20rpx solid $opacity-border;
      border-bottom: 1px solid $opacity-border;
    }
    .modifyPhone-newPhone {
      input {
        padding: 25rpx 30rpx;
        width: 690rpx;
        font-size: 28rpx;
      }
      border-bottom: 1px solid $opacity-border;
    }
    .modifyPhone-code {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 0 30rpx;
      input {
        padding: 25rpx 0;
        width: 500rpx;
        font-size: 28rpx;
      }
      text {
        color: $base-color1;
        font-size: 28rpx;
      }
    }
  }
  .modifyPhone-btn {
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

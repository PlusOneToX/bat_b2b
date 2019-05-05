<template>
  <view class="modifyPassword">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>修改密码</view>
      </view>
    </view>

    <view class="password-tip">
      <image src="../../static/img/safety_icon.png"></image>
      <view><text>为了您的帐号安全</text><text>请先验证手机号</text></view>
    </view>

    <view class="password-module">
      <view class="password-module-linePhone">
        <input type="" :value="phone" maxlength="11" />
      </view>
      <view class="password-module-lineCode">
        <input type="number" placeholder="请输入验证码" v-model="code" />
        <text @click="getCode" v-if="times == 60" :style="{ color: themeColor }"
          >获取验证码
        </text>
        <text v-if="times < 60 && times > 0" :style="{ color: themeColor }"
          >{{ times }}S重新获取
        </text>
      </view>
      <view class="password-module-linePsd">
        <text>新密码</text>
        <input
          type="number"
          password="true"
          placeholder="请输入6~16位数字或字母密码"
          maxlength="16"
          v-model="password"
        />
      </view>
      <view class="password-module-linePsd">
        <text>确认密码</text>
        <input
          type="number"
          password="true"
          placeholder="请输入6~16位数字或字母密码"
          maxlength="16"
          v-model="password2"
        />
      </view>
    </view>

    <view
      class="modifyPhone-btn"
      :style="{ background: themeColor }"
      @click="confirmClick"
      >确认修改
    </view>
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { phoneVerify, changePassword } from "../../common/api.js";
import md5 from "js-md5";
export default {
  data() {
    return {
      themeColor: "",
      phone: "",
      code: "",
      password: "",
      password2: "",
      times: 60,
      timeinter: "",
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {
    console.log(this.$store.state.phone);
    this.themeColor = uni.getStorageSync("themeColor");
    this.phone = uni.getStorageSync("userPhone");
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
    confirmClick() {
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      let phone = this.phone;
      let code = this.code;
      if (code == "") {
        this.tipFun("验证码不能为空");
        return;
      }
      if (this.password == "") {
        this.tipFun("密码不能为空");
        return;
      }

      if (this.password2 == "") {
        this.tipFun("请再次输入密码");
        return;
      }
      if (
        this.password != "" &&
        this.password2 != "" &&
        this.password != this.password2
      ) {
        this.tipFun("请确认两次输入密码保证一致");
        return;
      }

      let id = uni.getStorageSync("userId");
      let params = {
        accountType: 2,
        changeType: 2,
        codeType: 2,
        code: this.code,
        id: id,
        newPassword: md5(this.password),
        phone: this.phone,
      };
      changePassword(params).then((res) => {
        if (res.success) {
          uni.navigateTo({
            url: "/pages/login/login",
          });
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 获取验证码
    getCode() {
      clearInterval(this.timeinter);
      this.times = 60;
      let that = this;
      that.timeinter = setInterval(() => {
        that.times--;
        if (that.times < 1) {
          clearInterval(that.timeinter);
          this.times = 60;
        }
      }, 1000);

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
      phoneVerify("POST", { codeType: 2, phone: phone }).then((res) => {
        if (res.success) {
          that.code = res.data.code;
          that.timeinter = setInterval(() => {
            that.times--;
            if (that.times < 1) {
              clearInterval(that.timeinter);
              this.times = 60;
            }
          }, 1000);
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    toShopping() {
      uni.navigateTo({
        url: "../index/index",
      });
    },
  },
};
</script>

<style lang="scss">
.modifyPassword {
  font-size: 26rpx;
  .password-tip {
    display: flex;
    align-items: center;
    justify-content: center;
    background: #fff;
    padding: calc(116rpx + var(--status-bar-height)) 0 30rpx;
    // #ifdef H5
    padding: 104rpx 0 30rpx;
    // #endif
    image {
      margin-top: 50rpx;
      width: 160rpx;
      height: 160rpx;
    }
    view {
      color: #666;
      text-align: center;
      margin-left: 20rpx;
      text {
        display: block;
      }
    }
  }
  .password-module {
    background: #fff;
    font-size: 28rpx;
    input {
      padding: 25rpx 30rpx;
      font-size: 28rpx;
    }
    view {
      border-bottom: 1rpx solid $opacity-border;
    }
    view:last-child {
      border: none;
    }
    .password-module-lineCode {
      display: flex;
      align-items: center;
      justify-content: space-between;
      input {
        width: 500rpx;
      }
      text {
        margin-right: 30rpx;
        font-size: 28rpx;
      }
    }
    .password-module-linePsd {
      display: flex;
      align-items: center;
      padding-left: 30rpx;
      text {
        width: 150rpx;
      }
      input {
        width: 520rpx;
        padding-left: 50rpx;
      }
    }
  }

  .modifyPhone-btn {
    width: 690rpx;
    height: 100rpx;
    line-height: 100rpx;
    text-align: center;
    margin: 100rpx auto;
    font-size: 32rpx;
    color: #fff;
  }
}
</style>

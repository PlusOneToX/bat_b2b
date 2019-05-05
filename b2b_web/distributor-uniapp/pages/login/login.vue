<template>
  <view class="login">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toFun"></image>
        <view class="classifyLisy-title">登录</view>
      </view>
    </view>
    <view class="login-tab">
      <text
        :class="type == 1 ? 'login-tabHover' : ''"
        :style="{
          color: type == 1 ? themeColor : '#333',
          'border-bottom': type == 1 ? 'none' : '2rpx solid' + themeColor,
        }"
        @click="type = 1"
        >账号密码登陆</text
      >
      <text
        :class="type == 2 ? 'login-tabHover' : ''"
        :style="{
          color: type == 2 ? themeColor : '#333',
          'border-bottom': type == 2 ? 'none' : '2rpx solid' + themeColor,
        }"
        @click="type = 2"
        >手机号验证码登陆</text
      >
    </view>
    <view class="login-inputView">
      <view class="login-inputBox" v-if="type == 1">
        <input
          placeholder="用户名/手机号"
          placeholder-style="color:#B9B9B9;"
          v-model="formData.userName"
          class="login-input"
          @input="ListeningIsHover"
        />
        <input
          :password="passwordHide"
          placeholder="请输入密码"
          placeholder-style="color:#B9B9B9;"
          v-model="password"
          class="login-input"
          @input="ListeningIsHover"
        />
        <image
          :src="
            '../../static/imgs/' +
            (passwordHide ? 'icon_Eye-hide.png' : 'icon_Eye-show.png')
          "
          @click.stop="passwordHide = !passwordHide"
        ></image>
      </view>
      <view class="login-inputPosition" v-if="type == 2">
        <view class="login-inputBox">
          <input
            placeholder="请输入手机号"
            maxlength="11"
            placeholder-style="color:#B9B9B9;"
            v-model="phoneForm.phone"
            class="login-input"
            @input="ListeningIsHover"
          />
          <input
            placeholder="请输入验证码"
            placeholder-style="color:#B9B9B9;"
            v-model="phoneForm.code"
            class="login-input"
            @input="ListeningIsHover"
          />
        </view>
        <view class="login-code" @click="getCode" v-if="codeType == 1">{{
          codeText
        }}</view>
        <view class="login-code" v-if="codeType == 2">{{ times }}s</view>
      </view>
    </view>
    <view
      class="login-btn"
      :style="{ background: isHover ? themeColor : '#B9B9B9' }"
      @click="loginFun"
      >登录</view
    >
    <view class="login-threeOperate">
      <!-- <text @click="registerClick">立即注册</text>
		   <text>|</text> -->
      <text @click="forgetClick">忘记密码</text>
    </view>
    <!-- 第三方账号登录 -->
    <view class="login-threeLogin" v-if="!isH5">
      <view class="login-threeLogin-text">
        <text></text>
        <text>第三方账号登陆</text>
        <text></text>
      </view>
      <view class="login-threeLogin-img">
        <text></text>
        <image src="../../static/imgs/wechat_icon.png"></image>
        <button
          open-type="getPhoneNumber"
          @getphonenumber="getPhoneNumber"
          class="login-wechatButton"
        ></button>
        <text></text>
      </view>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import md5 from "js-md5";
import {
  login,
  userInfo,
  programMiniOpenId,
  phoneVerify,
  wechatLogin,
  mimiOpenid,
  loginByPhoneAndSmsCode,
  updateOpenId,
} from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  data() {
    return {
      themeColor: "",
      passwordHide: true,
      formData: {
        password: "",
        userName: "",
        openId: "",
      },
      password: "",
      isHover: false,
      type: 1, //账号密码：1， 手机号登录：2
      phoneForm: {
        phone: "",
        code: "",
      },
      isH5: isH5,
      times: 60,
      codeText: "获取验证码",
      codeType: 1, //1:可获取  2：倒计时进行中
      tipText: "", //轻提示
      tipTextShow: false,
      codeOne: "",
      timeInter: "",
    };
  },

  onLoad(option) {
    this.themeColor = uni.getStorageSync("themeColor");
    let that = this;
    uni.login({
      provider: "weixin",
      success: function (loginRes) {
        // console.log('登录：===',loginRes);
        that.codeOne = loginRes.code;
      },
    });
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
    toFun() {
      uni.switchTab({
        url: "/pages/personalCenter/personalCenter",
      });
    },
    // 监听是否填写齐全
    ListeningIsHover() {
      if (this.type == 1) {
        if (this.formData.userName != "" && this.password != "") {
          this.isHover = true;
        } else {
          this.isHover = false;
        }
      } else {
        if (this.phoneForm.phone != "" && this.phoneForm.code != "") {
          this.isHover = true;
        } else {
          this.isHover = false;
        }
      }
    },
    // 登录
    loginFun() {
      let that = this;
      if (!this.isHover) {
        return;
      }
      if (isH5) {
        if (that.type == 1) {
          that.accountLoginfun("");
        } else {
          that.phoneLoginFun("");
        }
      } else {
        uni.login({
          provider: "weixin",
          success: function (loginRes) {
            mimiOpenid({
              appId: uni.getStorageSync("appId"),
              code: loginRes.code,
            }).then((res) => {
              if (res.success) {
                console.log("获取openid：", res.data);
                uni.setStorageSync("openId", res.data);
                if (that.type == 1) {
                  that.accountLoginfun(res.data);
                } else {
                  that.phoneLoginFun(res.data);
                }
              }
            });
          },
        });
      }
    },

    // 账号登录
    accountLoginfun(openId) {
      if (this.formData.userName == "") {
        this.tipFun("请输入用户名或者手机号");
        return;
      }
      if (this.password == "") {
        this.tipFun("请输入密码");
        return;
      }

      this.formData.password = md5(this.password);
      this.formData.openId = openId;
      login(this.formData).then((res) => {
        console.log("登录", res);
        let data = res.data;
        if (res.success) {
          uni.setStorageSync("userId", res.data.id);
          uni.setStorageSync("userName", res.data.userName); //分销商名称
          uni.setStorageSync("autoDelivery", res.data.autoDelivery); // 是否直发客户：1 是，0 否
          uni.setStorageSync("companyName", res.data.companyName); //分销商公司名称
          uni.setStorageSync("onWayFlag", res.data.onWayFlag); //是否支持在途库存 1是 0否 默认是1
          uni.setStorageSync("capitalStatus", res.data.applyStatus); //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
          uni.setStorageSync("freezeStatus", res.data.freezeStatus); //冻结状态 2,冻结  1,未冻结
          uni.setStorageSync("distributionFlag", res.data.distributionFlag); //是否开启分销模式 0 不开启, 1 开启
          uni.reLaunch({
            url: "/pages/index/index",
          });
          this.userInfoFun(res.data.id);

          // 更新当前分销商openId
          this.handleUpdateOpenId(res.data.id, openId);
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 查看分销商个人信息
    userInfoFun(id) {
      userInfo({ id: id }).then((res) => {
        if (res.success) {
          // this.$store.commit('setPhone',res.data.phone)  //登录的手机号
          uni.setStorageSync("userPhone", res.data.phone);
        }
      });
    },

    // 忘记密码
    forgetClick() {
      uni.navigateTo({
        url: "modifyPassword",
      });
    },

    // 注册
    registerClick() {
      uni.navigateTo({
        url: "register",
      });
    },
    // 验证手机号
    verifyPhone() {
      let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
      let phone = this.phoneForm.phone;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      return true;
    },

    // 获取验证码
    getCode() {
      let that = this;
      if (this.verifyPhone()) {
        phoneVerify("POST", { codeType: 4, phone: this.phoneForm.phone }).then(
          (res) => {
            if (res.success) {
              that.codeType = 2;
              that.times = 60;
              that.timsFun();
              this.tipFun("验证码发送成功！");
            } else {
              this.tipFun(res.errMessage);
            }
          }
        );
      }
    },
    // 60s倒计时
    timsFun() {
      let that = this;
      clearInterval(that.timeInter);
      this.timeInter = setInterval(() => {
        if (this.times > 1) {
          that.times--;
        } else {
          that.codeType = 1;
          that.codeText = "重新获取";
          clearInterval(that.timeInter);
        }
      }, 1000);
    },
    // 验证验证码
    verifyCode() {
      let params = {
        codeType: 4,
        code: this.phoneForm.code,
        phone: this.phoneForm.phone,
      };
      phoneVerify("PUT", params).then((res) => {
        if (res.success) {
          this.phoneLoginFun();
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 手机号快捷登录
    phoneLoginFun(openId) {
      if (this.verifyPhone()) {
        if (this.phoneForm.code == "") {
          this.tipFun("请输入验证码！");
          return;
        }
        let params = {
          codeType: 4,
          code: this.phoneForm.code,
          phone: this.phoneForm.phone,
          openId: openId,
        };
        loginByPhoneAndSmsCode(params).then((res) => {
          let data = res.data;
          if (res.success) {
            uni.setStorageSync("userId", res.data.id);
            uni.setStorageSync("userName", res.data.userName); //分销商名称
            uni.setStorageSync("autoDelivery", res.data.autoDelivery); // 是否直发客户：1 是，0 否
            uni.setStorageSync("companyName", res.data.companyName); //分销商公司名称
            uni.setStorageSync("onWayFlag", res.data.onWayFlag); //是否支持在途库存 1是 0否 默认是1
            uni.setStorageSync("capitalStatus", res.data.applyStatus); //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
            uni.setStorageSync("freezeStatus", res.data.freezeStatus); //冻结状态 2,冻结  1,未冻结
            uni.setStorageSync("distributionFlag", res.data.distributionFlag); //是否开启分销模式 0 不开启, 1 开启
            uni.reLaunch({
              url: "/pages/index/index",
            });
            this.userInfoFun(res.data.id);

            // 更新当前分销商openId
            this.handleUpdateOpenId(res.data.id, openId);
          } else {
            this.tipFun(res.errMessage);
          }
        });
      }
    },

    // 微信登录授权获取信息
    getPhoneNumber: function (e) {
      console.log("手机号获取：", e.detail);
      let that = this;
      // that.openIdLoginFun(e.detail,that.codeOne);
      if (e.detail.errMsg == "getPhoneNumber:ok") {
        uni.login({
          provider: "weixin",
          success: function (loginRes) {
            console.log("登录：===", loginRes);
            that.openIdLoginFun(e.detail, loginRes.code);
          },
        });
      }
    },

    // 小程序授权登录
    openIdLoginFun(detail, code) {
      let params = {
        appId: uni.getStorageSync("appId"),
        code: code,
        encryptedData: detail.encryptedData,
        iv: detail.iv,
      };
      wechatLogin(params).then((res) => {
        console.log("授权登录");
        let data = res.data;
        if (res.success) {
          uni.setStorageSync("userId", res.data.id);
          uni.setStorageSync("userName", res.data.userName); //分销商名称
          uni.setStorageSync("autoDelivery", res.data.autoDelivery); // 是否直发客户：1 是，0 否
          uni.setStorageSync("companyName", res.data.companyName); //分销商公司名称
          uni.setStorageSync("onWayFlag", res.data.onWayFlag); //是否支持在途库存 1是 0否 默认是1
          uni.setStorageSync("capitalStatus", res.data.applyStatus); //'资质状态 0,未提交 1,申请中,2,合作中 3,申请失败',
          uni.setStorageSync("freezeStatus", res.data.freezeStatus); //冻结状态 2,冻结  1,未冻结
          uni.setStorageSync("distributionFlag", res.data.distributionFlag); //是否开启分销模式 0 不开启, 1 开启
          uni.setStorageSync("openId", res.data.contact.openId);
          uni.setStorageSync("contactId", res.data.contact.id);
          uni.reLaunch({
            url: "/pages/index/index",
          });

          // 更新当前分销商openId
          this.handleUpdateOpenId(res.data.id, res.data.contact.openId);

          this.userInfoFun(res.data.id);
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },

    // 更新当前分销商openId
    handleUpdateOpenId(id, openId) {
      updateOpenId({
        id: id,
        openId: openId,
      }).then((res) => {
        console.log(res);
      });
    },
  },
};
</script>

<style lang="scss">
.phcolor {
  color: red !important;
}
.login {
  font-family: PingFangSC-Regular, PingFang SC;
  background: #fff;
  height: 100vh;
  border-top: 1rpx solid #f3f4f8;
  box-sizing: border-box;
  .login-tab {
    padding-left: 64rpx;
    display: flex;
    align-items: flex-end;
    margin-top: calc(216rpx + var(--status-bar-height));
    // #ifdef H5
    margin-top: 204rpx;
    // #endif
    text {
      font-size: 28rpx;
      font-weight: 400;
    }
    text:nth-child(2) {
      margin-left: 14rpx;
    }
    .login-tabHover {
      font-size: 38rpx;
      font-weight: 500;
    }
  }
  .login-inputView {
    padding: 52rpx 64rpx 0 64rpx;

    .login-inputBox {
      position: relative;
      .login-input {
        background: #fff;
        font-size: 32rpx;
        font-weight: 400;
        color: #333;
        padding: 26rpx 0;
        border-bottom: 1rpx solid #f3f4f8;
      }
      .login-input:nth-child(2) {
        margin-top: 20rpx;
      }
      image {
        width: 44rpx;
        height: 44rpx;
        position: absolute;
        bottom: 28rpx;
        right: 0;
        z-index: 999;
      }
    }
    .login-inputPosition {
      position: relative;
    }
    .login-code {
      z-index: 9999;
      font-size: 24rpx;
      color: #333;
      position: absolute;
      bottom: 30rpx;
      right: 0px;
      width: 122rpx;
      text-align: center;
    }
  }
  .login-btn {
    margin: 90rpx auto 0;
    width: 622rpx;
    height: 100rpx;
    background: #b9b9b9;
    line-height: 100rpx;
    text-align: center;
    font-size: 32rpx;
    color: #fff;
  }
  .login-threeLogin {
    margin-top: 78rpx;
    .login-threeLogin-text {
      display: flex;
      align-items: center;
      justify-content: center;
      font-size: 24rpx;
      text:nth-child(1),
      text:nth-child(3) {
        width: 200rpx;
        height: 2rpx;
        background: #f5f5f9;
      }
      text:nth-child(2) {
        margin: 0 24rpx;
      }
    }
    .login-threeLogin-img {
      margin-top: 50rpx;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      text {
        width: 64rpx;
        height: 64rpx;
        background: #f9f9f9;
        border: 2rpx solid #f5f5f9;
        border-radius: 100%;
      }
      image {
        width: 80rpx;
        height: 80rpx;
        margin: 0 62rpx;
      }
      .login-wechatButton {
        top: 0;
        left: 50%;
        position: absolute;
        width: 90rpx;
        height: 80rpx;
        background-color: transparent;
        border: 0 !important;
        transform: translateX(-50%);
      }
      .login-wechatButton:after {
        border: 0 !important;
      }
    }
  }
  .login-threeOperate {
    margin-top: 46rpx;
    text-align: center;
    font-size: 28rpx;
    text:nth-child(2) {
      padding: 0 34rpx;
    }
  }
}
</style>

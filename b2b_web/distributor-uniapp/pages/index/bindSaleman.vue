<template>
  <view class="saleman">
    <view class="top-moudle">
      <view class="status_bar"></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>业务员绑定</view>
      </view>
    </view>
    <view class="top-tip">
      <view>
        <text>{{ distributorName }}</text>
      </view>
      <view class="remark">
        <text>请确认绑定的分销商无误后</text>
        <text>输入您的手机号进行绑定</text>
      </view>
    </view>
    <view class="saleman-module">
      <view class="item-module">
        <text>手机号</text>
        <input
          type=""
          v-model="phone"
          placeholder="请输入手机号码"
          maxlength="11"
        />
      </view>
      <view class="item-module">
        <text>验证码</text>
        <input
          class="code"
          type="text"
          v-model="code"
          placeholder="手机验证码"
        />
        <text class="tip" v-if="times == 60" @click="getCode">获取验证码</text>
        <text class="tip" v-if="times < 60">{{ times }}S重新获取</text>
      </view>
    </view>
    <view class="btn-submit" @click="confirmClick">确认并绑定</view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  phoneVerify,
  getInfoByDistributorId,
  bindWechat,
  getOpenId,
  mimiOpenid,
} from "../../common/api.js";
export default {
  data() {
    return {
      providerList: [],
      distributorId: "",
      distributorName: "",
      openId: "",
      phone: "",
      code: "", //获取的验证码
      times: 60,
      tipTextShow: false,
      tipText: "",
      appId: "",
      webViewUrl: "",
    };
  },

  onLoad(option) {
    let url = decodeURIComponent(option.scene);
    console.log("00--", url);
    let scene = url.split("_");
    this.distributorId = scene[0];
    // this.distributorId =2770;
    this.appId = scene[1];
    this.getInfo(scene[0]);
    // this.getInfo('2770');
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
    getMessageOpenId(e) {
      console.log("openID信息--", e);
    },

    getInfo(distributorId) {
      this.isComit = distributorId;
      let that = this;
      getInfoByDistributorId({ id: distributorId }).then((res) => {
        that.datall = JSON.stringify(res);
        if (res.success) {
          console.log("分销商名字", res.data.name);
          that.distributorName = res.data.name;
        }
      });
    },

    // 确认并绑定
    confirmClick() {
      let that = this;
      let myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
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
      // 获取openid
      uni.login({
        provider: "weixin",
        success: function (loginRes) {
          mimiOpenid({
            appId: uni.getStorageSync("appId"),
            code: loginRes.code,
          }).then((res) => {
            if (res.success) {
              console.log("获取openid：", res.data);

              bindWechat({
                openId: res.data,
                codeType: 6,
                phone: phone,
                code: code,
                distributorId: that.distributorId,
              }).then((res) => {
                if (res.success) {
                  let username = res.data;
                  uni.navigateTo({
                    url:
                      "bindSuccess?distributor=" +
                      that.distributorName +
                      "&phone=" +
                      phone +
                      "&username=" +
                      username,
                  });
                } else {
                  that.tipFun(res.errMessage);
                }
              });
            }
          });
        },
      });
    },
    // 获取验证码
    getCode() {
      this.times = 60;
      let that = this;
      let phone = this.phone;
      let myreg = /^[1][3,4,5,7,8][0-9]{9}$/;
      if (phone == "") {
        this.tipFun("请输入手机号");
        return;
      }
      if (phone != "" && !myreg.test(phone)) {
        this.tipFun("手机号格式不正确");
        return;
      }
      phoneVerify("POST", { codeType: 6, phone: phone, skipCheck: true }).then(
        (res) => {
          if (res.success) {
            this.code = res.data.code;
            let times = setInterval(() => {
              that.times--;
              if (that.times == 0) {
                clearInterval(times);
                that.times = 60;
              }
            }, 1000);
          } else {
            that.tipFun(res.errMessage);
          }
        }
      );
    },
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
  },
};
</script>

<style lang="scss">
.saleman {
  font-size: 26rpx;
  .top-tip {
    margin-bottom: 16rpx;
    background: #fff;
    padding: calc(116rpx + var(--status-bar-height)) 0 40rpx;
    /* #ifdef H5 */
    padding: 104rpx 0 40rpx;
    /* #endif*/
    text-align: center;
    image {
      width: 200rpx;
      height: 200rpx;
    }
    view {
      color: #666;
      text-align: center;
      margin-left: 20rpx;
      text {
        display: block;
        font-size: 36rpx;
        font-weight: bold;
      }
    }
    .remark {
      margin-top: 80rpx;
      text {
        display: block;
        font-size: 30rpx;
      }
    }
  }
  .saleman-module {
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
    .item-module {
      display: flex;
      align-items: center;
      padding-left: 30rpx;
      text {
        width: 150rpx;
      }
      input {
        width: 480rpx;
        padding-left: 50rpx;
      }
      .code {
        flex: 1;
      }
      .tip {
        display: inline-block;
        color: $base-color1;
        font-size: 24rpx;
      }
    }
  }

  .btn-submit {
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


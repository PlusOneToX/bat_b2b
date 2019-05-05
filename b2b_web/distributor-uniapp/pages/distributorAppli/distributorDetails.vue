<template>
  <view class="distributorDetails">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title" s>
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>分销商申请</view>
      </view>
    </view>

    <!-- <view class="dsb-details-oneLine"><text>所属分销商</text><text>{{distributorName}}</text></view> -->
    <view class="dsb-details">
      <view class="dsb-details-line dsb-details-oneLine">
        <view
          ><text>商户名称</text
          ><input placeholder="请输入名称" v-model="formInfo.companyName"
        /></view>
        <view
          ><text>商户联系人</text
          ><input placeholder="请输入商户联系人" v-model="formInfo.accountName"
        /></view>
        <view
          ><text>联系人电话</text
          ><input placeholder="请输入联系人电话" v-model="formInfo.phone"
        /></view>

        <view
          ><text>登录账号</text
          ><input placeholder="请输入登录账号" v-model="formInfo.name"
        /></view>
        <view
          ><text>登录密码</text
          ><input
            placeholder="请输入登录密码"
            maxlength="16"
            password="true"
            v-model="password1"
        /></view>
        <view
          ><text>重复密码</text
          ><input
            placeholder="请重复输入密码"
            maxlength="16"
            password="true"
            v-model="password2"
        /></view>
      </view>

      <view class="dsb-details-allpy" @click="applyClick">提交申请</view>
      <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
    </view>
  </view>
</template>

<script>
import md5 from "js-md5";
import { distributorNextApply, userInfo } from "../../common/api.js";
export default {
  data() {
    return {
      providerList: [],
      distributorName: "",
      formInfo: {
        distributorId: "", //上级分销商id
        companyName: "", //商户名称
        accountName: "", //商户联系人
        phone: "",
        name: "", //登录账号
        password: "",
      },
      password1: "",
      password2: "", //确认密码
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad(option) {
    this.formInfo.distributorId = option.distributorId;
    this.formInfo.phone = option.phone;
    // this.userInfoFun(option.distributorId);
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
      }, 2000);
    },

    // 获取个人信息
    userInfoFun(id) {
      let that = this;
      userInfo({ id: id }).then((res) => {
        if (res.success) {
          that.distributorName = res.data.companyName;
        }
      });
    },

    //申请
    applyClick() {
      let params = this.formInfo;
      if (params.accountName == "") {
        this.tipFun("商户联系人名称不能为空");
        return;
      }
      if (params.name == "") {
        this.tipFun("登录账号不能为空");
        return;
      }
      if (this.password1 == "") {
        this.tipFun("登录密码不能为空");
        return;
      }
      if (this.password2 == "") {
        this.tipFun("请输入确认密码");
        return;
      }
      if (
        this.password1 != "" &&
        this.password2 != "" &&
        this.password1 != this.password2
      ) {
        this.tipFun("两次输入的密码不一致");
        return;
      }
      params.password = md5(this.password1);

      distributorNextApply(params).then((res) => {
        if (res.success) {
          this.tipFun("提交申请成功");
          uni.navigateTo({
            url: "applySuccess",
          });
        } else {
          this.tipFun(res.errMessage);
        }
      });
    },
  },
};
</script>

<style lang="scss">
.distributorDetails {
  font-size: 26rpx;
  .dsb-details {
    padding-top: calc(116rpx + var(--status-bar-height));
		/* #ifdef H5 */
    padding-top: 104rpx;
    /* #endif*/
  }
  .dsb-details-line {
    background: #fff;

    view {
      padding: 0 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      display: flex;
      align-items: center;
      text {
        width: 150rpx;
      }
      input {
        font-size: 26rpx;
        padding: 30rpx 25rpx;
        width: 500rpx;
        text-align: right;
      }
    }
    view:last-child {
      border: 0 !important;
    }
  }
  .dsb-details-oneLine {
    background: #fff;
    /* #ifdef H5 */
    margin-top: 0 !important;
    /* #endif*/
  }
  .dsb-details-allpy {
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

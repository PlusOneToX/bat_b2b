<!--
 * @Author: yaowei
 * @Date: 2019-12-09 18:47:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-25 14:58:51
-->
<template>
  <view class="registerSuccess">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>提交成功</view>
      </view>
    </view>

    <view class="rgS-img">
      <text class="iconfont icon-Checkthe succesIcon"></text>
      <view class="rgS-tip">提交成功</view>
      <view class="rgS-tip">正在审核中，请耐心等候</view>
      <view class="rgS-btn" @click="toIndex">进入商城首页</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      providerList: [],
      phoneNumber: "",
    };
  },
  onLoad() {},
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    fillInformFun() {
      uni.navigateTo({
        url: "registerInform",
      });
    },
    // 进入首页
    toIndex() {
      uni.switchTab({
        url: "/pages/index/index",
      });
    },
  },
};
</script>

<style lang="scss">
.registerSuccess {
  text-align: center;
  .rgS-img {
    padding-top: calc(216rpx + var(--status-bar-height));
    /* #ifdef H5 */
    padding-top: 204rpx;
    /* #endif*/

    .succesIcon {
      font-size: 200rpx;
      color: $base-color1;
    }
  }
  .rgS-name {
    color: #333;
    font-size: 28rpx;
    margin: 30rpx 0;
  }
  .rgS-tip {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
  }
  .rgS-btn {
    width: 600rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: $bg-gradient-btn;
    border-radius: 50rpx;
    color: #fff;
    text-align: center;
    font-size: 32rpx;
    margin: 100rpx auto 0;
  }
}
</style>

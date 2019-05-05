<!--
 * @Author: yaowei
 * @Date: 2019-12-09 18:47:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-25 15:01:13
-->
<template>
  <view class="applySuccess">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>提交成功</view>
      </view>
    </view>
    <view>
      <view class="apply-success-top">
        <text class="iconfont icon-Checkthe succesIcon"></text>
        <view>提交注册成功</view>
        <view style="margin-top: 15rpx">请耐心等待审核</view>
      </view>
      <view class="toShopping-btn" @click="toShopping">前往商城</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {};
  },
  onLoad() {},
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    toShopping() {
      uni.switchTab({
        url: "/pages/index/index",
      });
    },
  },
};
</script>

<style lang="scss">
.applySuccess {
  .apply-success-top {
    text-align: center;
    padding-top: calc(216rpx + var(--status-bar-height));
    /* #ifdef H5 */
    padding-top: 204rpx;
    /* #endif*/

    .succesIcon {
      font-size: 200rpx;
      color: $base-color1;
    }

    view {
      font-size: 28rpx;
      color: #333;
      font-weight: 400;
    }
  }
  .toShopping-btn {
    width: 600rpx;
    height: 80rpx;
    line-height: 80rpx;
    text-align: center;
    background: $bg-gradient-btn;
    border-radius: 50rpx;
    margin: 100rpx auto;
    font-size: 32rpx;
    color: #fff;
  }
}
</style>

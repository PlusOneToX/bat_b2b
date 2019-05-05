<!--
 * @Author: yaowei
 * @Date: 2019-12-09 18:47:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-25 15:01:53
-->
<template>
  <view class="bindSuccess">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>绑定成功</view>
      </view>
    </view>

    <view class="bS-img">
      <text class="iconfont icon-Checkthe succesIcon"></text>
      <view class="bS-tip">绑定成功</view>
      <view class="bS-name">分销商：{{ distributor }}</view>
      <view class="bS-name">姓名：{{ username }}</view>
      <view class="bS-name">手机号：{{ phone }}</view>
      <view class="bS-btn" @click="btnNext">进入商城首页</view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      distributor: "",
      phone: "",
      username: "",
    };
  },
  onLoad(option) {
    this.distributor = option.distributor;
    this.phone = option.phone;
    this.username = option.username;
  },
  methods: {
    btnNext() {
      uni.switchTab({
        url: "/pages/index/index",
      });
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
.bindSuccess {
  text-align: center;
  .bS-img {
    padding-top: calc(216rpx + var(--status-bar-height));
    /* #ifdef H5 */
    padding-top: 204rpx;
    /* #endif*/

    .succesIcon {
      font-size: 200rpx;
      color: $base-color1;
    }
  }
  .bS-name {
    color: #333;
    font-size: 28rpx;
    margin: 20rpx 0;
  }
  .bS-tip {
    font-size: 24rpx;
    color: #999;
    margin-top: 10rpx;
    margin-bottom: 30px;
  }
  .bS-btn {
    width: 600rpx;
    height: 80rpx;
    line-height: 80rpx;
    background: $bg-gradient-btn;
    border-radius: 50rpx;
    color: #fff;
    text-align: center;
    font-size: 32rpx;
    margin: 100rpx auto 0 auto;
  }
}
</style>

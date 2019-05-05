<template>
  <view class="logisticsInform">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>物流轨迹</view>
      </view>
    </view>
    <view class="lg-top"></view>
    <view class="inform-num">
      <view>快递公司：{{ shiplist.orderDeliverBill.distributionName }}</view>
      <view>快递单号：{{ shiplist.orderDeliverBill.logisticsNo }}</view>
    </view>
    <view
      class="shipping"
      v-if="
        shiplist.orderDeliveryTrace && shiplist.orderDeliveryTrace.length > 0
      "
    >
      <view class="linehh" :style="'left:' + xiantiaoLeft + 'px'"></view>
      <view
        class="ul"
        v-for="(item, index) in shiplist.orderDeliveryTrace"
        :key="index"
      >
        <view class="li">
          <view class="time">{{ item.acceptTime }}</view>
          <view class="mmmm">
            <view
              class="yuan"
              :class="{
                act: index != 0,
                first: index == 0,
                end: index == shiplist.orderDeliveryTrace.length - 1,
              }"
            >
              <view
                class="yuan-center"
                :class="{ centerAct: index != 0 }"
              ></view>
            </view>
          </view>
          <view class="desc" :class="{ black: index == 0 }">
            <text>{{ item.acceptStation }}</text>
          </view>
        </view>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      xiantiaoLeft: 0,
      shiplist: {},
    };
  },
  onLoad() {
    var that = this;
    setTimeout(function () {
      that.getHeight();
    }, 2000);
    this.shiplist = JSON.parse(uni.getStorageSync("orderDeliveryTrace"));
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 跳转详情页
    toDetails() {
      uni.navigateTo({
        url: "orderDetails?id=1",
      });
    },

    getHeight() {
      const query = uni.createSelectorQuery();
      query.select(".first").boundingClientRect();
      query.select(".end").boundingClientRect();
      var that = this;
      query.exec((res) => {
        that.xiantiaoLeft = res[0].left + 6;
      });
    },
  },
};
</script>

<style lang="scss">
.logisticsInform {
  font-size: 26rpx;
  .lg-top {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
  }
  .inform-num {
    background: #fff;
    padding: 30rpx;
    text-align: left;
    view:nth-child(2) {
      margin-top: 10rpx;
    }
    border-bottom: 1rpx solid $opacity-border;
  }
  // 物流列表
  .shipping {
    position: relative;
    background-color: #ffffff;
    padding: 0 30rpx 30rpx 30rpx;
    .linehh {
      position: absolute;
      width: 2rpx;
      border-left: 2rpx solid #f2f3f8;
      top: 5rpx;
      bottom: 30rpx;
    }
    .ul {
      .li {
        width: 100%;
        display: flex;
        align-items: center;
        height: auto;
        .time {
          width: 21%;
          color: #999;
          font-size: 24rpx;
          text-align: center;
        }
        .mmmm {
          position: relative;
          width: 10%;
          // display: flex;
          // flex-direction: column;
          // justify-content: center;
          // align-items: center;
          .yuan {
            position: absolute;
            left: 15rpx;
            width: 20rpx;
            height: 20rpx;
            background: #fff;
            border-radius: 50%;
            border: 3rpx solid $base-color1;
            .yuan-center {
              width: 16rpx;
              height: 16rpx;
              background: $base-color1;
              border-radius: 50%;
              margin-left: 2rpx;
              margin-top: 2rpx;
            }
            &.act {
              border: 3rpx solid #e6e6e6;
            }
            .centerAct {
              background-color: #e6e6e6;
            }
          }
        }
        .desc {
          width: 70%;
          padding: 33upx 0upx;
          margin-left: 20upx;
          font-size: 26upx;
          color: #999999;
          &.black {
            color: $base-color1;
          }
        }
      }
    }
  }
}
</style>

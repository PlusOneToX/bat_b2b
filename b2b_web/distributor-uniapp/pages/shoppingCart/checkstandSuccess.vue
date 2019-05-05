<template>
  <view class="checkstandSuccess">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <view>收银台</view>
      </view>
    </view>

    <view class="cts-top">
      <text class="iconfont icon-Checkthe succesIcon"></text>
      <view class="cts-top-text">订单{{ orderStatus }}</view>
      <view class="cts-top-btn">
        <text @click="toIndexFun">前往首页</text>
        <text @click="toOrderFun">查看订单</text>
      </view>
    </view>

    <!-- 商品 -->
    <view class="cts-listModule">
      <view class="cts-list-title">
        <image src="../../static/img/logo_icon.png" class="iconLogo"></image>
        <text>更多商品</text>
      </view>
      <scroll-view>
        <view class="cts-list">
          <waterfallsFlow
            ref="waterfalls_flow_nav"
            :list="list"
            :offset="15"
            @wapper-lick="toGoodsDetails"
          >
            <!--  #ifdef  MP-WEIXIN -->
            <view
              v-for="(item, index) of list"
              :key="index"
              slot="slot{{index}}"
            >
              <view class="cts-list-cnt">
                <view class="cts-list-title">{{ item.title }}</view>
                <view class="cts-list-text">
                  <text>￥</text>{{ item.price }}
                </view>
              </view>
            </view>
            <!--  #endif -->

            <!-- #ifndef  MP-WEIXIN -->
            <template v-slot:default="item">
              <view class="cts-list-cnt">
                <view class="cts-list-title">{{ item.title }}</view>
                <view class="cts-list-text">
                  <text>￥</text>{{ item.price }}
                </view>
              </view>
            </template>
            <!-- #endif -->
          </waterfallsFlow>
        </view>
      </scroll-view>
    </view>
  </view>
</template>

<script>
import waterfallsFlow from "@/components/maramlee-waterfalls-flow/maramlee-waterfalls-flow.vue";
import { payQueryTrade } from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  components: { waterfallsFlow },
  data() {
    return {
      list: [
        {
          id: 1,
          image_url:
            "https://dss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=3892521478,1695688217&fm=26&gp=0.jpg",
          title: "iPad/iPad Pro/iPad Air 4 IP彩印保护套-哆啦A",
          price: 45,
        },
        {
          id: 2,
          image_url:
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599476083909&di=a5debff35edec5de105bc105d6fdbce3&imgtype=0&src=http%3A%2F%2Fa2.att.hudong.com%2F77%2F77%2F01300000336597125202779973172.jpg",
          title: "iPad/iPad Pro/iPad Air 4 IP彩印保护套-哆啦A",
          price: 45,
        },
        {
          id: 3,
          image_url:
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1599475934834&di=7a37b8d628252c4aced6ed0decba9442&imgtype=0&src=http%3A%2F%2Fa3.att.hudong.com%2F43%2F74%2F01300000164151121808741085971.jpg",
          title: "iPad/iPad Pro/iPad Air 4 IP彩印保护套-哆啦A",
          price: 45,
        },
      ],
      payMethod: "",
      orderId: "",
      orderStatus: "",
    };
  },
  onLoad(option) {
    this.orderId = option.orderId;
    this.payMethod = option.payMethod;

    this.payQueryTradeFun();
  },
  /**
   * 上拉加载
   */
  onReachBottom() {
    /**
     * 这里的 waterfalls_flow_nav 值记得与你定义的 ref 值对应
     */
    this.getList();
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },

    // 跳转小程序
    toGoodsDetails() {
      uni.navigateTo({
        url: "",
      });
    },

    // 加载分页
    getList() {
      console.log("加载更多");
    },
    // 跳转首页
    toIndexFun() {
      uni.switchTab({
        url: "/pages/index/index",
      });
    },
    // 跳转订单
    toOrderFun() {
      uni.navigateTo({
        url: "/pages/order/order",
      });
    },

    // 查询支付状态
    payQueryTradeFun() {
      let payMethod = "";
      if (this.payMethod == 1) {
        payMethod = "alipay_wap";
      } else if (this.payMethod == 2) {
        payMethod = isH5 ? "wxpay_h5" : "wxpay_mini_program";
      } else if (this.payMethod == 5) {
        payMethod = "balance";
      } else if (this.payMethod == 6) {
        payMethod = "kuaiqian_merchant";
      }

      payQueryTrade({ payMethod: payMethod, orderId: this.orderId }).then(
        (res) => {
          console.log("支付状态：", res.data.tradeState);
          if (res.data.tradeState == "SUCCESS") {
            this.orderStatus = "支付成功！";
          }
          if (res.data.tradeState == "REFUND") {
            this.orderStatus = "转入退款！";
          }
          if (res.data.tradeState == "NOTPAY") {
            this.orderStatus = "未支付！";
          }
          if (res.data.tradeState == "CLOSED") {
            this.orderStatus = "已关闭！";
          }
          if (res.data.tradeState == "REVOKED") {
            this.orderStatus = "已撤销！";
          }
          if (res.data.tradeState == "USERPAYING") {
            this.orderStatus = "用户支付中！";
          }
          if (res.data.tradeState == "PAYERROR") {
            this.orderStatus = "支付失败！";
          }
          if (res.data.tradeState == "ACCEPT") {
            this.orderStatus = "已接收，等待扣款！";
          }
        }
      );
    },
  },
};
</script>

<style lang="scss">
.checkstandSuccess {
  position: fixed;
  width: 100%;
  height: 100%;
  top: 0;
  left: 0;
  font-size: 28rpx;
  background: #fff;
  .cts-top {
    position: absolute;
    top: 50%;
    left: 50%;
    text-align: center;
    transform: translate(-50%, -50%);
    text-align: center;
    .succesIcon {
      font-size: 160rpx;
      color: $base-color1;
    }
    .cts-top-text {
      font-size: 28rpx;
      margin-top: 20rpx;
    }
    .cts-top-btn {
      display: flex;
      align-items: center;
      justify-content: center;
      margin-top: 100rpx;
      text {
        width: 220rpx;
        height: 80rpx;
        line-height: 80rpx;
        font-size: 28rpx;
        border-radius: 40rpx;
        text-align: center;
        display: block;
      }
      text:nth-child(1) {
        border: 2rpx solid $base-color1;
        color: $base-color1;
      }

      text:nth-child(2) {
        background: $bg-gradient-btn;
        color: #fff;
        margin-left: 30rpx;
      }
    }
  }
  .cts-listModule {
    margin-top: 20rpx;
    background: #fff;
    display: none;
    .cts-list-title {
      padding: 30rpx;
      background: #fff;
      text {
        margin-left: 25rpx;
      }
    }
    .cts-list {
      background: #fff;
      // padding:0 30rpx 30rpx 30rpx;
      .cts-list-cnt {
        .cts-list-title {
          font-size: 26rpx;
          color: #333;
          padding: 15rpx;
        }
        .cts-list-text {
          font-size: 30rpx;
          color: $base-color2;
          margin: 0 0 25rpx 25rpx;
          text {
            font-size: 22rpx;
          }
        }
      }
    }
  }
}
</style>

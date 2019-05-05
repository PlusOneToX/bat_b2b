<template>
  <view class="checkstand">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>收银台</view>
      </view>
    </view>

    <view class="ct-top">
      <text>￥</text>
      <text>{{ orderParams.orderAmount }}</text>
    </view>

    <view class="ct-list">
      <template v-for="(item, index) in PaymentType">
        <view
          class="ct-list-line"
          :key="index"
          v-if="
            item.isH5 &&
            ((item.id == 3 && userTradeType == 2) ||
              (item.id == 5 && userTradeType == 1) ||
              item.id == 1 ||
              item.id == 2 ||
              item.id == 4)
          "
          @click="choicePayment(item)"
        >
          <view class="ct-list-lineLf">
            <image :src="item.img" :class="item.className"></image>
            <text>{{ item.name }}</text>
          </view>
          <view class="ct-line-Rg">
            <text v-if="item.id == 5">可用余额{{ balancePrice }}元</text>
            <view
              class="iconfont"
              :class="
                PayWay == item.id
                  ? 'icon-icon_selected'
                  : 'icon-icon_selected_def'
              "
              :style="PayWay == item.id ? 'color: ' + themeColor : ''"
            ></view>
          </view>
        </view>
      </template>
    </view>

    <view
      class="ct-btn"
      :class="!PayWay ? 'readonly' : ''"
      :style="{ 'background-color': themeColor }"
      @click="checkstandSuccess"
      v-if="onceAgain == true"
    >
      {{ PayWayName }} ￥{{ orderParams.orderAmount }}
    </view>
    <view class="ct-btn" :style="{ 'background-color': themeColor }" v-else>
      {{ PayWayName }} ￥{{ orderParams.orderAmount }}
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  paymentMode,
  userDeposit,
  deleteShoppingcart,
  payCreateTrade,
  placeOrder,
  versionBaseSetting,
} from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  data() {
    return {
      themeColor: "",
      balancePrice: 0, //余额支付
      //1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,-Y
      PaymentType: [
        {
          id: 2,
          name: "微信支付",
          img: require("../../static/img/wechat_icon.png"),
          className: "wechatIcon",
          isH5: true,
        },
        {
          id: 5,
          name: "余额支付",
          img: require("../../static/img/balance_icon.png"),
          className: "balanceIcon",
          isH5: isH5,
        },
        {
          id: 3,
          name: "区间结算（月结）",
          img: require("../../static/img/qujian_icon.png"),
          className: "balanceIcon",
          isH5: true,
        },
        {
          id: 4,
          name: "银行转账（转账）",
          img: require("../../static/img/bank_icon.png"),
          className: "bankIocn",
          isH5: true,
        },
        // {id:6,name:'网银支付',img:require('../../static/img/ebank_icon.png'),className:'ebankIcon',isH5:isH5},
        {
          id: 1,
          name: "支付宝支付",
          img: require("../../static/img/alipay_icon.png"),
          className: "alipayIcon",
          isH5: isH5,
        },
      ],
      PayWay: "",
      PayWayName: "",
      orderParams: {}, //下单参数集合
      isTwoWay: false,
      onceAgain: true,
      version: false, //版本号
      userTradeType: 1,
      tipTextShow: false,
      tipText: "",
      codeOne: "",
      tmplIds: [], // 订阅消息模板ID
    };
  },
  onShow() {
    this.themeColor = uni.getStorageSync("themeColor");
    let orderParams = uni.getStorageSync("orderParams");
    if (orderParams && orderParams != "") {
      this.orderParams = JSON.parse(orderParams);
    } else {
      uni.switchTab({
        url: "shoppingCart",
      });
    }

    this.getDeposit();
    this.getUserTradeType();

    // #ifdef MP-WEIXIN
    // 订阅消息模板ID
    const accountInfo = uni.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    let tmplIds = []; // 订阅消息模板ID
    // 订单状态通知，订单发货通知，订单未支付提醒
    if (appId === "wx1f43eac1f6d7a750") { 
      tmplIds = [
        "zOED8tpPB7HkERlsuuzEo2A_u1TuAxVOmsN5VJJlwgA",
        "zD0Lz627YMAxQw8shmpjMr0yqBBmphJyjuLPWO07TWo",
        "x5dkQhLWO2nUrfC38MMcsyOd0QifJ8Eyex1ki1E-ajE",
      ];
    }
    this.tmplIds = tmplIds;
    // #endif
  },
  onLoad(option) {
    let orderParams = uni.getStorageSync("orderParams");
    if (orderParams && orderParams != "") {
      this.orderParams = JSON.parse(orderParams);
    } else {
      uni.switchTab({
        url: "shoppingCart",
      });
    }
    //
    console.log("平台类型H5", isH5);
    console.log("平台类型小程序", isMpWeixin);
    this.isTwoWay = option.isTwoWay;

    this.versionBaseSettingFun();
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
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 获取版本号
    versionBaseSettingFun() {
      let Timestamp = 1147;
      // #ifdef MP-WEIXIN
      const accountInfo = uni.getAccountInfoSync();
      let appId = accountInfo.miniProgram.appId;
      if (appId == "wx1f43eac1f6d7a750") {
        Timestamp = 1147;
      } else {
        Timestamp = 109;
      }
      // #endif

      let that = this;
      versionBaseSetting({ key: "build_number" }).then((res) => {
        console.log("版本号：", res);
        if (res.success && res.data.length > 0) {
          let val = "";
          res.data.forEach((item) => {
            if (item.key === "build_number") {
              val = item.value;
            }
          });
          let num = parseInt(val.split(".").join(""));
          that.PaymentType.forEach((item) => {
            if (
              !isH5 &&
              Timestamp > num &&
              (item.id == 5 || item.id == 4 || item.id == 3)
            ) {
              that.$set(item, "isH5", false);
            } else {
              if (item.id !== 1) {
                // 非支付宝平台（支付宝平台，小程序不显示）
                that.$set(item, "isH5", true);
              }
            }
          });
        }
        console.log(that.PaymentType);
      });
    },
    // 获取账户余额--y
    getDeposit() {
      let that = this;
      let id = uni.getStorageSync("userId");
      userDeposit({ id: id }).then((res) => {
        if (res.success) {
          that.balancePrice = res.data.accountBalance;
        }
      });
    },
    // 获取用户预存款方式--Y
    getUserTradeType() {
      paymentMode().then((res) => {
        if (res.success) {
          // 1为现款支付，2为期间结算
          this.userTradeType = res.data.payWay;
        }
      });
    },

    // 选择支付方式
    choicePayment(item) {
      if (
        item.id == 5 &&
        Number(this.balancePrice) < Number(this.orderParams.orderAmount)
      ) {
        this.tipFun("余额不足，请选择其它支付方式");
        return;
      }
      this.PayWay = item.id;
      this.PayWayName = item.name;
      this.orderParams.payWay = item.id;
    },

    // 支付成功
    checkstandSuccess() {
      if (!this.PayWay) {
        this.tipFun("请选择选择支付方式");
        return;
      }
      this.onceAgain = false;

      // openId
      let openId = uni.getStorageSync("openId");
      if (openId) {
        this.orderParams.openId = openId;
      }

      console.log("下单的参数", this.orderParams);
      uni.removeStorageSync("orderParams");
      // uni.navigateTo({
      // 	url: 'checkstandSuccess'
      // });
      let payMethod = "";
      if (this.PayWay == 1) {
        payMethod = "alipay_wap";
      } else if (this.PayWay == 2) {
        payMethod = isH5 ? "wxpay_h5" : "wxpay_mini_program";
      } else if (this.PayWay == 5) {
        payMethod = "balance";
      } else if (this.PayWay == 6) {
        payMethod = "kuaiqian_merchant";
      }

      let that = this;
	  // 先注释掉订阅消息
      // // #ifdef MP-WEIXIN
      // uni.requestSubscribeMessage({
      //   tmplIds: that.tmplIds,
      //   success(res) {
      //     // #endif
          
      //     // #ifdef MP-WEIXIN
      //   },
      // });
      // // #endif
	  
	  //1.支付宝,2.微信,3.区间结算,4.线下转账,5.余额支付,6.快钱支付,-Y
	  placeOrder(that.orderParams).then((res) => {
	    if (res.success) {
	      let delegetIds = JSON.parse(uni.getStorageSync("delegetIds"));
	      that.deleteShoppingcartFun(delegetIds);
	      let ids = res.data.ids.join(",");
	      let userId = uni.getStorageSync("userId");
	      let userName = uni.getStorageSync("userName");
	      // 快钱支付--4余额--3网银
	      if (that.PayWay != 4 && that.PayWay != 3) {
	        let payParams = {
	          customerFlag: 0,
	          orderId: ids,
	          payMethod: payMethod,
	          payerId: userId,
	          payerName: userName,
	          quitUrl:
	            that.PayWay == 1
	              ? "https://www.bat.com/h5/#/pages/shoppingCart/checkstandSuccess?orderId=" +
	                res.data.ids[0] +
	                "&payMethod=" +
	                payMethod
	              : "",
	          redirectUrl:
	            that.PayWay == 1 || that.PayWay == 2
	              ? "https://www.bat.com/h5/#/pages/shoppingCart/checkstandSuccess?orderId=" +
	                res.data.ids[0] +
	                "&payMethod=" +
	                payMethod
	              : "",
	          appId: !isH5 ? uni.getStorageSync("appId") : "",
	          platformUserId: !isH5 ? uni.getStorageSync("openId") : "",
	        };
	  
	        payCreateTrade(payParams)
	          .then((payRes) => {
	            if (payRes.success) {
	              if (that.PayWay == 6) {
	                window.location.href = payRes.data.kuaiQianReap.h5Url; // 跳转链接
	              } else if (that.PayWay == 5) {
	                uni.navigateTo({ url: "checkstandSuccess2" });
	              } else if (that.PayWay == 2) {
	                //微信
	                if (isH5) {
	                  let url = payRes.data.wxResp.h5Url;
	                  console.log(url)
	                  window.location.href = url;
	                } else {
	                  let wxResp = payRes.data.wxResp;
	                  uni.requestPayment({
	                    provider: "wxpay",
	                    timeStamp: wxResp.timeStamp,
	                    nonceStr: wxResp.nonceStr,
	                    package: wxResp.prepayId,
	                    signType: wxResp.signType,
	                    paySign: wxResp.paySign,
	                    success: function (res) {
	                      console.log("success:" + JSON.stringify(res));
	                      uni.navigateTo({
	                        url: "checkstandSuccess2",
	                      });
	                    },
	                    fail: function (err) {
	                      console.log("fail:" + JSON.stringify(err));
	  
	                      that.tipFun("支付失败");
	                      uni.switchTab({
	                        url: "shoppingCart",
	                      });
	                    },
	                  });
	                }
	              } else if (that.PayWay == 1) {
	                let resData = payRes.data.alipayResp.from;
	                const div = document.createElement("div");
	                div.id = "alipay";
	                div.innerHTML = resData;
	                document.body.appendChild(div);
	                document.querySelector("#alipay").children[0].submit(); // 执行后会唤起支付宝
	              }
	            } else {
	              that.tipFun(payRes.errMessage);
	            }
	          })
	          .catch((err) => {
	            console.log("交易异常：", err);
	            that.tipFun("系统异常");
	  
	            uni.switchTab({
	              url: "shoppingCart",
	            });
	          });
	      } else if (that.PayWay == 3) {
	        uni.navigateTo({ url: "checkstandSuccess2?status=1" }); //直接跳转成功页
	      } else {
	        uni.navigateTo({ url: "checkstandSuccess2" }); //直接跳转成功页
	      }
	    } else {
	      that.tipFun(res.errMessage);
	      if (res.errCode && res.errCode == "B_PROMOTION_ERROR") {
	        let list1 = uni.getStorageSync("shopOrderList");
	        let orderList = JSON.parse(list1);
	        orderList.forEach((item) => {
	          that.$set(item, "goodsPromotionId", "no");
	          that.$set(item, "groupSeckillId", "");
	          that.$set(item, "conditionsId", "");
	          that.$set(item, "lastPrice", item.itemCount * item.salePrice);
	        });
	  
	        uni.setStorageSync("shopOrderList", JSON.stringify(orderList));
	        uni.navigateTo({
	          // url: 'confirmOrder?isTwoWay='+that.isTwoWay
	          url: "shoppingCart",
	        });
	      }
	    }
	  });
    },
    // 删除货品
    deleteShoppingcartFun(ids) {
      deleteShoppingcart({ ids: ids }).then((res) => {
        if (res.success) {
        }
      });
    },
  },
};
</script>

<style lang="scss">
.checkstand {
  font-size: 28rpx;
  .ct-top {
    color: #ed5307;
    padding: calc(173rpx + var(--status-bar-height)) 0 57rpx;
    /* #ifdef H5 */
    padding: 161rpx 0 57rpx;
    /* #endif*/
    background: #fff;
    text-align: center;
    text:nth-child(1) {
      font-size: 36rpx;
    }
    text:nth-child(2) {
      font-size: 60rpx;
    }
  }
  .ct-list {
    margin-top: 20rpx;
    .ct-list-line {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background: #fff;
      border-bottom: 1rpx solid $opacity-border;
      padding: 30rpx;
      .wechatIcon {
        width: 38rpx;
        height: 35rpx;
      }
      .balanceIcon {
        width: 38rpx;
        height: 35rpx;
      }
      .bankIocn {
        width: 42rpx;
        height: 39rpx;
      }
      .ebankIcon {
        width: 44rpx;
        height: 29rpx;
      }
      .alipayIcon {
        width: 44rpx;
        height: 44rpx;
      }
      .ct-list-lineLf {
        display: flex;
        align-items: center;
        image {
          margin-right: 30rpx;
        }
      }
      .ct-line-Rg {
        display: flex;
        align-items: center;
        font-size: 24rpx;
        color: #999;
        text {
          margin-right: 15rpx;
        }
        .iconfont {
          font-size: 40rpx;
        }
      }
    }
  }
  .ct-btn {
    position: fixed;
    width: 690rpx;
    height: 100rpx;
    line-height: 100rpx;
    border-radius: 50rpx;
    color: #fff;
    font-size: 32rpx;
    text-align: center;
    left: 30rpx;
    bottom: 40rpx;

    &.readonly {
      opacity: 0.5;
    }
  }
}
</style>

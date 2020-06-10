<template>
  <div class="orderDetail" :class="{ hiddenHeader: isMiniProgram }">
    <THeader class="header-wrap" :title="'订单详情'" :hasBg="true"></THeader>
    <div
      class="container"
      :class="{
        hasBtn:
          (orderCustomerData.frontOrderStatus === 1 && time) ||
          (orderSource !== 'GF60006' &&
            orderSource !== 'GF60007' &&
            orderSource !== 'GF60008' &&
            canShared &&
            (orderCustomerData.frontOrderStatus === 2 ||
              orderCustomerData.frontOrderStatus === 4 ||
              orderCustomerData.frontOrderStatus === 6)),
      }"
      v-show="orderDetail"
    >
      <div class="tr-header">
        <span class="text">{{
          orderStatus(orderCustomerData.frontOrderStatus)
        }}</span>
        <i
          class="sprite-icon"
          :class="iconStatus(orderCustomerData.frontOrderStatus)"
        ></i>
      </div>
      <div
        class="item-box item-tips"
        @click="openLogistics(orderCustomerData.frontOrderStatus)"
        v-show="
          (orderCustomerData.frontOrderStatus === 1 && time) ||
          orderCustomerData.frontOrderStatus === 2 ||
          orderCustomerData.frontOrderStatus === 4 ||
          orderCustomerData.frontOrderStatus === 6
        "
      >
        <!---待付款--->
        <div
          class="tr-info obligation"
          v-show="orderCustomerData.frontOrderStatus === 1"
        >
          <div class="text" v-show="time">
            剩余支付时间：&nbsp;{{ time }}&nbsp;
          </div>
        </div>
        <!---待发货--->
        <div
          class="tr-info shipping"
          v-show="orderCustomerData.frontOrderStatus === 2"
        >
          <div class="text">您的订单已提交至工厂制作，请耐心等待！</div>
        </div>
        <!---待收货--->
        <div
          class="tr-info shipped"
          v-show="orderCustomerData.frontOrderStatus === 4"
        >
          <div class="text-desc" v-if="expressNo && expressName">
            <div class="time">快递单号：{{ expressNo }}</div>
            <div class="text">服务商：{{ expressName }}</div>
          </div>
          <div class="text-desc" v-else>
            <div class="text">暂无物流信息</div>
          </div>
          <van-icon name="arrow" v-if="expressNo && expressName" />
        </div>
        <!---已完成--->
        <div
          class="tr-info complete"
          v-show="orderCustomerData.frontOrderStatus === 6"
        >
          <div class="text-desc" v-if="expressNo && expressName">
            <div class="time">快递单号：{{ expressNo }}</div>
            <div class="text">服务商：{{ expressName }}</div>
          </div>
          <van-icon name="arrow" v-if="expressNo && expressName" />
        </div>
      </div>
      <div class="item-box">
        <div class="address-detail">
          <span class="sprite-icon icon-location"></span>
          <div class="top">
            <div class="name">{{ orderDelivery.userName }}</div>
            <div class="tel">{{ orderDelivery.mobile }}</div>
          </div>
          <div class="bottom">
            <div class="address">
              {{
                orderDelivery.provinceName +
                orderDelivery.cityName +
                orderDelivery.districtName
              }}
            </div>
            <div class="detail">{{ orderDelivery.address }}</div>
          </div>
        </div>
      </div>
      <div
        class="item-box"
        v-for="(order, index) in orderDetail.orderInfoDetailGoods"
        :key="index"
      >
        <div class="order-item">
          <div class="order-con">
            <div class="order-img">
              <img
                v-lazy="
                  order.orderGoodsDiy && order.orderGoodsDiy.previewImage
                    ? order.orderGoodsDiy.previewImage
                    : ''
                "
                alt=""
                class="img"
              />
            </div>
            <div class="order-info">
              <div class="tr-box">
                <span class="name">{{ order.orderGoods.goodsName }}</span>
                <span class="price" v-if="orderSource !== 28"
                  >¥{{
                    order.orderGoodsCustomerCost.salePrice | fomatFloat
                  }}</span
                >
              </div>
              <div class="model">
                <div class="pro-spe">
                  <span>规格：</span>
                  <p>
                    {{
                      order.orderGoodsDiy && order.orderGoodsDiy.modelName
                        ? order.orderGoodsDiy.modelName
                        : ""
                    }}
                  </p>
                  <p>
                    {{
                      order.orderGoodsDiy && order.orderGoodsDiy.materialName
                        ? order.orderGoodsDiy.materialName
                        : ""
                    }}
                  </p>
                  <span class="count"
                    ><i>X</i
                    >{{
                      order.orderGoods && order.orderGoods.itemCount
                        ? order.orderGoods.itemCount
                        : 0
                    }}</span
                  >
                </div>
                <!-- 金科 -->
                <div class="pro-spe" v-show="distributorId === 1217">
                  <span>图片ID：</span>
                  <p>
                    {{
                      order.orderGoodsDiy && order.orderGoodsDiy.pictureId
                        ? order.orderGoodsDiy.pictureId
                        : ""
                    }}
                  </p>
                </div>
                <div
                  class="pro-spe pro-code"
                  v-if="order.secretCodeList && order.secretCodeList.length > 0"
                >
                  <span>兑换码：</span>
                  <div class="code-r">
                    <p
                      v-for="(code, index) in order.secretCodeList"
                      :key="index"
                    >
                      {{ code }}
                    </p>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="item-box padding-d">
        <div class="order-item">
          <div class="order-tr">
            <div class="name">订单编号：{{ orderInfo.orderNo }}</div>
          </div>
          <div class="order-tr" v-show="orderInfo.createTime">
            <div class="name">
              创建时间：{{ orderInfo.createTime | formatDate }}
            </div>
          </div>
          <div
            class="order-tr"
            v-show="
              (orderCustomerData.frontOrderStatus === 2 ||
                orderCustomerData.frontOrderStatus === 4 ||
                orderCustomerData.frontOrderStatus === 6) &&
              orderCustomerData.payTime
            "
          >
            <div class="name">
              付款时间：{{ orderCustomerData.payTime | formatDate }}
            </div>
          </div>
          <div
            class="order-tr"
            v-show="
              (orderCustomerData.frontOrderStatus === 4 ||
                orderCustomerData.frontOrderStatus === 6) &&
              orderInfo.deliverTime
            "
          >
            <div class="name">
              发货时间：{{ orderInfo.deliverTime | formatDate }}
            </div>
          </div>
        </div>
      </div>
      <div class="item-box padding-d" v-if="orderSource === 28">
        <div class="order-tr">
          <div class="name">运费</div>
          <!-- 暂时取的商品总价（邮费 distributionAmount 为 0） -->
          <div class="price orange" v-if="orderCustomerCost.payAmount">
            ¥<em>{{ orderCustomerCost.payAmount | fomatFloat }}</em>
          </div>
          <div class="price" v-else>包邮</div>
        </div>
      </div>
      <div class="item-box padding-d" v-else>
        <div class="order-tr">
          <div class="name">运费</div>
          <div class="price orange">
            ¥<em>{{ orderCustomerCost.distributionAmount | fomatFloat }}</em>
          </div>
        </div>
        <div class="order-tr">
          <div class="name">优惠</div>
          <div class="price orange">
            -¥<em>{{ orderCustomerCost.orderCouponAmount | fomatFloat }}</em>
          </div>
        </div>
        <div class="order-tr">
          <div class="name">总价</div>
          <div
            class="price orange"
            :class="orderCustomerData.frontOrderStatus === 5 ? '' : 'red'"
          >
            ¥<em>{{ orderCustomerCost.payAmount | fomatFloat }}</em>
          </div>
        </div>
      </div>

      <div
        class="btn-wrap"
        v-show="orderCustomerData.frontOrderStatus === 1 && time"
      >
        <p class="btn border-btn" @click="cancelOrder">取消订单</p>
        <p
          class="btn confirm-btn"
          :class="isDisable ? 'readonly' : ''"
          @click="submitOrder"
        >
          立即支付
        </p>
      </div>
      <div
        class="btn-wrap"
        v-show="
          orderSource !== 'GF60006' &&
          orderSource !== 'GF60007' &&
          orderSource !== 'GF60008' &&
          canShared &&
          (orderCustomerData.frontOrderStatus === 2 ||
            orderCustomerData.frontOrderStatus === 4 ||
            orderCustomerData.frontOrderStatus === 6)
        "
      >
        <p class="btn confirm-btn" @click="handleShare">
          {{ exchangeShareData.forwardButtonText }}
        </p>
      </div>
    </div>
    <Loading v-show="isLoading" :message="message"></Loading>

    <!-- 分享弹窗 -->
    <div class="share-wrap" v-show="showShare" @click="showShare = false">
      <div class="share-arrow"></div>
      <div class="share-text">
        <p>分享给好友</p>
        <p>点击屏幕右上角<span class="share-icon"></span>将本页面分享给好友</p>
      </div>
      <div class="share-img"></div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
import Loading from "components/loading/loading";
// js
import { formatDate, fomatFloat, countDown } from "common/js/common";
import { aliPay } from "common/js/pay";
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  name: "orderDetail",
  data() {
    return {
      title: "订单详情",
      message: "",
      distributorId: "", // 分销商ID
      orderSource: "", // 订单来源
      expressName: "", // 当前物流公司名称
      expressNo: "", // 当前快递单号
      payType: 0, // 支付方式
      orderId: 0,
      orderDetail: {},
      orderInfo: {},
      orderDelivery: {},
      orderCustomerCost: {},
      orderCustomerData: {},
      time: "",
      protocol: false,
      loading: true,
      temp: null,
      themeList: [],
      isDisable: false,
      weixin: {
        appid: "",
        timeStamp: "",
        nonceStr: "",
        prepayId: "",
        signType: "",
        paySign: "",
      },
      isLoading: false,
      isMiniProgram: false, // 是否是小程序
      orderTime: 0, // 订单失效时间
      showShare: false, // 是否显示分享弹窗
      canShared: false, // 是否可分享
      exchangeShareData: {}, // 转发活动
    };
  },
  created() {
    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.message = "载入中";
    this.isLoading = true;
    this.orderId = this.$route.query.id;
    this.distributorId = parseInt(localStorage.getItem("distributorId"));
    this.orderSource = localStorage.getItem("orderSource") || 28;
    // 获取订单详情
    this.getOrderDetail();
    // 获取订单失效时间
    this.getOrderTime();
  },
  methods: {
    // 获取订单详情
    getOrderDetail() {
      this.$api
        .get(this, api.getOrderDetail, {
          id: this.orderId,
        })
        .then((res) => {
          if (res.success) {
            this.orderDetail = res.data;

            this.orderInfo = res.data.orderInfo;

            this.payType = res.data.orderCustomerData.payWay; // 2 微信，1 支付宝

            this.orderCustomerData = res.data.orderCustomerData;
            if (this.orderCustomerData.frontOrderStatus === 1) {
              // 未付款
              this.getTimer();
            }

            // 物流单号
            if (
              this.orderDetail.orderDeliverDetail &&
              this.orderDetail.orderDeliverDetail.length > 0
            ) {
              this.expressNo =
                this.orderDetail.orderDeliverDetail[0].orderDeliverBill.logisticsNo;
              this.expressName =
                this.orderDetail.orderDeliverDetail[0].orderDeliverBill.distributionName;
            }

            this.orderDelivery = this.orderDetail.orderDelivery;

            this.orderCustomerCost = this.orderDetail.orderCustomerCost;

            // 兑换码
            let codeIdList = [];
            if (
              this.orderDetail.orderInfoDetailGoods &&
              this.orderDetail.orderInfoDetailGoods.length > 0
            ) {
              this.orderDetail.orderInfoDetailGoods.forEach((item) => {
                if (
                  item.orderGoodsExchangeCode &&
                  item.orderGoodsExchangeCode.secretCode
                ) {
                  let arr = item.orderGoodsExchangeCode.secretCode.split(",");
                  codeIdList = codeIdList.concat(arr);
                  this.$set(item, "secretCodeList", arr);
                }
              });
            }
            // 判断是否有邮费
            if (Number(this.orderCustomerCost.payAmount) > 0) {
              this.findExchangeShare(codeIdList);
            }

          } else {
            this.$toast.fail(res.errMessage);
          }

          this.message = "";
          this.isLoading = false;
        });
    },
    // 订单状态
    orderStatus(val) {
      switch (val) {
        case 1:
          return "待付款";
        case 2:
          return "待发货";
        case 4:
          return "待收货";
        case 5:
          return "已关闭";
        case 6:
          return "已完成";
      }
    },
    // 支付
    submitOrder() {
      var host = window.location.host;
      let redirectUrl = "";
      let appId = "";
      if (host === "diy.bat.com") {
        // BAT
        redirectUrl = "https://diy.bat.com/wx/exchange/orderList";
        appId = "...";
      } else {
        // 测试
        redirectUrl =
          "https://test.bat.com/minih5/orderList";
        appId = "...";
      }

      let platform = localStorage.getItem("platform");
      let openid = localStorage.getItem("openId");
      let userId = localStorage.getItem("userId");

      this.isDisable = true;
      setTimeout(() => {
        this.isDisable = false;
      }, 5000);
      // 2 微信，10 字节
      if (this.payType === 2 || this.payType === 10) {
        // 查看平台，判断微信支付方式
        if (
          platform === "1" ||
          platform === "28" ||
          platform === "GF60006" ||
          platform === "GF60007" ||
          platform === "GF60008"
        ) {
          // 微信公众号平台支付
          if (openid) {
            let info = {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_jsapi", // 交易方式
              orderId: this.orderId,
              payerId: userId,
              platformUserId: openid,
              redirectUrl: redirectUrl,
            };

            if (Number(platform) === 28) {
              // 兑换
              info = {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_mini_program", // 交易方式
                orderId: this.orderId,
                payerId: userId,
                platformUserId: openid,
                appId: appId,
              };
            } else if (platform === "GF60006") {
              // 字节小程序
              info = {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "toutiao", // 交易方式
                orderId: this.orderId,
                payerId: userId,
                platformUserId: openid,
                appId: "tt754703faea7f02c301",
              };
            }

            this.$api.post(this, api.handlePayment, info).then((res) => {
              if (res.success) {
                if (platform === "GF60006") {
                  // 字节小程序
                  let touTiaoResp = res.data.touTiaoResp.data;

                  var enterParams = JSON.stringify({
                    orderId: touTiaoResp.orderId,
                    orderToken: touTiaoResp.orderToken.replace(
                      /\=/g,
                      "123456789"
                    ), // 处理等号（等号去小程序解析会报错）
                    orderPrice: this.orderCustomerCost.payAmount,
                  });
                  tt.miniProgram.redirectTo({
                    url:
                      "/pages/order/payment/payment?enterFlag=payment&enterParams=" +
                      enterParams,
                  });
                } else {
                  let wxConfig = res.data.wxResp;
                  this.weixin.appid = wxConfig.appId;
                  this.weixin.nonceStr = wxConfig.nonceStr;
                  this.weixin.prepayId = wxConfig.prepayId;
                  this.weixin.signType = wxConfig.signType;
                  this.weixin.timeStamp = wxConfig.timeStamp;
                  this.weixin.paySign = wxConfig.paySign;

                  if (Number(this.orderSource) === 28) {
                    // 兑换，跳转小程序订单支付页面
                    var enterParams = JSON.stringify({
                      timeStamp: wxConfig.timeStamp,
                      nonceStr: wxConfig.nonceStr,
                      package: wxConfig.prepayId.replace(/\=/g, "123456789"), // 处理等号（等号去小程序解析会报错）
                      signType: wxConfig.signType,
                      paySign: wxConfig.paySign.replace(/\=/g, "123456789"), // 处理等号（等号去小程序解析会报错）
                      orderNo: wxConfig.outTradeNo,
                      orderPrice: this.orderCustomerCost.payAmount,
                    });
                    wx.miniProgram.redirectTo({
                      url:
                        "/pages/order/payment/payment?enterFlag=payment&enterParams=" +
                        enterParams,
                    });
                  } else {
                    if (typeof WeixinJSBridge === "undefined") {
                      if (document.addEventListener) {
                        document.addEventListener(
                          "WeixinJSBridgeReady",
                          this.onBridgeReady(redirectUrl),
                          false
                        );
                      } else if (document.attachEvent) {
                        document.attachEvent(
                          "WeixinJSBridgeReady",
                          this.onBridgeReady(redirectUrl)
                        );
                        document.attachEvent(
                          "onWeixinJSBridgeReady",
                          this.onBridgeReady(redirectUrl)
                        );
                      }
                    } else {
                      this.onBridgeReady(redirectUrl);
                    }
                  }
                }
              } else {
                this.$toast(res.errMessage);
              }
            });
          } else {
            this.$toast("微信未授权");
          }
        } else {
          // 其它非微信公众号平台，h5平台
          this.$api
            .post(this, api.handlePayment, {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_h5", // 交易方式
              orderId: this.orderId,
              payerId: userId,
              redirectUrl: redirectUrl,
            })
            .then((res) => {
              if (res.success) {
                let url = res.data.wxResp.h5Url;
                window.location.href = url;
              } else {
                this.$toast.fail(res.errMessage);
                this.$router.replace("/orderList");
              }
            });
        }
      } else {
        // 支付宝
        let info = {
          businessType: 1, // 业务类型：1 订单收款
          customerFlag: 1, // 客户标志：1 C端客户
          payMethod: "alipay_wap", // 交易方式
          orderId: this.orderId,
          payerId: userId,
          redirectUrl: redirectUrl,
        };
        if (platform === "GF60007") {
          // 支付宝小程序（BAT小程序）
          info = {
            businessType: 1, // 业务类型：1 订单收款
            customerFlag: 1, // 客户标志：1 C端客户
            payMethod: "alipay_common", // 交易方式
            orderId: this.orderId,
            payerId: userId,
            platformUserId: openid,
            appId: "",
          };
        }
        if (platform === "GF60008") {
          // 支付宝小程序（其他）
          info = {
            businessType: 1, // 业务类型：1 订单收款
            customerFlag: 1, // 客户标志：1 C端客户
            payMethod: "alipay_common", // 交易方式
            orderId: this.orderId,
            payerId: userId,
            platformUserId: openid,
            appId: "",
          };
        }
        this.$api.post(this, api.handlePayment, info).then((res) => {
          if (res.success) {
            let data = res.data.alipayResp;
            if (platform === "GF60007" || platform === "GF60008") {
              // 支付宝小程序
              var enterParams = JSON.stringify({
                tradeNo: data.tradeNo,
                orderPrice: this.orderCustomerCost.payAmount,
              });
              my.redirectTo({
                url:
                  "/pages/order/payment/payment?enterFlag=payment&enterParams=" +
                  enterParams,
              });
            } else {
              aliPay(data.from);
            }
          } else {
            this.$toast.fail(res.errMessage);
            this.$router.replace("/orderList");
          }
        });
      }
    },
    onBridgeReady(redirectUrl) {
      /* eslint-disable */
      WeixinJSBridge.invoke(
        "getBrandWCPayRequest",
        {
          appId: this.weixin.appid, // 公众号名称，由商户传入
          timeStamp: this.weixin.timeStamp, // 时间戳，自1970年以来的秒数
          nonceStr: this.weixin.nonceStr, // 随机串
          package: this.weixin.prepayId,
          signType: this.weixin.signType, // 微信签名方式：
          paySign: this.weixin.paySign, // 微信签名
        },
        function (res) {
          if (res.err_msg === "get_brand_wcpay_request:ok") {
            window.location.href = redirectUrl;
          } else {
            window.location.href = redirectUrl;
          }
        }
      );
    },
    // 取消订单
    cancelOrder() {
      this.$dialog
        .confirm({
          message: "您确定取消订单吗？",
          className: "confirm-v-dialog",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          this.$api
            .put(this, api.handleCancelOrder, {
              id: this.orderId,
              remark: "用户主动取消订单",
            })
            .then((res) => {
              if (res.success) {
                this.$router.push("/orderList");
              } else {
                this.$toast.fail(res.errMessage);
              }
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 倒计时
    getTimer() {
      let vm = this;
      if (vm.temp !== null) {
        return;
      }
      vm.temp = setInterval(() => {
        // 未付款
        let timeStr = this.orderInfo.createTime.replace(/-/g, "/");
        let date = new Date(timeStr).getTime();
        let time = countDown(date + this.orderTime * 60 * 1000);
        if (time === "") {
          this.time = "";
          vm.destroyed();
        } else {
          this.time = "00:" + time;
        }
      }, 1000);
    },
    // 获取订单失效时间
    getOrderTime() {
      this.$api.get(this, api.getOrderTime).then((res) => {
        if (res.success) {
          this.orderTime = res.data;
        }
      });
    },
    destroyed() {
      clearInterval(this.temp);
      this.temp = null;
    },
    // 图标状态
    iconStatus(val) {
      switch (val) {
        case 1:
          return "icon-shipPay";
        case 2:
          return "icon-ship";
        case 4:
          return "icon-shipping";
        case 5:
          return "已关闭";
        case 6:
          return "icon-shipped";
      }
    },
    // 物流信息
    openLogistics(status) {
      // 待收货或已完成
      if (status === 4 || status === 6) {
        this.$router.push({
          path: "/logisticsDetail",
          query: { id: this.orderId },
        });
      }
    },
    // 查看转发活动
    findExchangeShare(secretCodeList) {
      if (secretCodeList && secretCodeList.length > 0 && this.canShared) {
        this.$api
          .post(this, api.exchangeShareFind, {
            activityPlatform: 1, // 活动所属平台：1 兑换商城，2 定制商城
            seat: 2, // 活动位置：1 确认订单页，2 订单详情页
            secretCodeList: secretCodeList,
          })
          .then((res) => {
            if (res.success) {
              if (res.data) {
                this.canShared = true;
                this.exchangeShareData = res.data;
              }
            } else {
              this.$toast.fail(res.errMessage);
            }
          });
      }
    },
    handleShare() {
      this.showShare = true;
      let distributorId = this.exchangeShareData.distributorIds[0];
      let postData = {
        distributorId: distributorId,
        id: this.exchangeShareData.id,
      };
      wx.miniProgram.postMessage({ data: JSON.stringify(postData) });
    },
  },
  filters: {
    formatDate(time) {
      if (time) {
        let timeStr = time.replace(/-/g, "/");
        let date = new Date(timeStr);
        return formatDate(date, "yyyy-MM-dd hh:mm:ss");
      }
    },
    fomatFloat(num, n) {
      return fomatFloat(num, 2);
    },
  },
  components: {
    THeader,
    Loading,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.orderDetail {
  position: fixed;
  width: 100%;
  top: 44px;
  bottom: 0;
  background-color: $color-bg;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .container {
    position: relative;
    padding: 0 15px 50px;
    height: 100%;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &.hasBtn {
      padding-bottom: 100px;
    }

    &::-webkit-scrollbar {
      display: none;
    }

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 100%;
      height: 210px;
      background: linear-gradient(360deg, #DFDFDF 0%, #F8F0C3 16%, #FFDA01 100%);
      opacity: 0.3;
    }

    .tr-header {
      position: relative;
      height: 96px;
      font-size: 20px;
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 0 80px;

      .icon {
      }

      .text {
        color: $color-font-base;
      }
    }

    .item-box {
      position: relative;
      display: inline-block;
      width: 100%;
      margin-bottom: $spacing-base;
      padding: $spacing-lg;
      background-color: $color-bg-white;
      border-radius: $radius-base;

      &.item-tips {
        padding: $spacing-sm $spacing-lg;
      }

      &.padding-d {
        padding: 28px $spacing-base;
      }

      .address-detail {
        padding: 7px $spacing-sm 4px 35px;

        .sprite-icon {
          position: absolute;
          top: 24px;
          left: $spacing-base;
        }

        .top {
          font-size: $font-lg;
          color: $color-font-base;

          .name {
            display: inline-block;
          }

          .tel {
            display: inline-block;
            margin-left: $spacing-sm;
            color: $color-font-grey;
          }
        }

        .bottom {
          line-height: 20px;

          .address {
            margin-top: 12px;
            font-size: $font-sm;
            color: $color-font-grey;
          }

          .detail {
            font-size: $font-base;
          }
        }
      }

      .tr-box {
        display: flex;
        align-items: center;

        .name, .text {
          display: inline-block;
        }

        .text {
          flex: 1;
          text-align: right;
        }

        .input {
          flex: 1;
          margin-left: 12px;

          >>>.el-input__inner {
            height: 34px;
            line-height: 34px;
          }
        }
      }

      .tr-info {
        position: relative;
        font-size: $font-base;

        &.obligation {
          .text {
            padding: $spacing-sm 0;
            font-size: $font-xm;
            color: $color-orange;
            align(center);
          }
        }

        &.shipping {
          .text {
            padding: $spacing-sm 0;
          }
        }

        &.shipped {
          display: flex;

          .text-desc {
            flex: 1;

            .text {
              font-size: $font-sm;
              margin-top: 8px;
            }
          }
        }

        &.complete {
          display: flex;

          .text-desc {
            flex: 1;

            .text {
              font-size: $font-sm;
              margin-top: 8px;
            }
          }
        }

        .van-icon {
          position: absolute;
          top: 50%;
          right: 0;
          transform: translateY(-50%);
        }
      }

      .order-item {
        position: relative;

        .order-con {
          display: flex;
          display: -webkit-flex;

          .order-img {
            position: relative;
            display: inline-block;
            width: 68px;
            height: 127px;
            margin-right: 20px;
            overflow: hidden;

            .img {
              position: absolute;
              top: 50%;
              left: 50%;
              max-width: 100%;
              max-height: 100%;
              transform: translate(-50%, -50%);
            }
          }

          .order-info {
            position: relative;
            display: inline-block;
            flex: 1;
            padding-top: 12px;

            .tr-box {
              display: flex;
              align-items: center;

              .name {
                display: -webkit-box;
                flex: 1;
                font-size: $font-base;
                overflow: hidden;
                text-overflow: ellipsis;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
              }

              .price {
                display: inline-block;
                font-size: $font-sm;
                float: right;
              }
            }

            .model {
              .pro-spe {
                position: relative;
                margin-top: $spacing-sm;
                padding-left: 50px;
                padding-right: 2em;
                font-size: $font-sm;
                color: $color-font-grey;
                line-height: 16px;

                span {
                  position: absolute;
                  top: 0;
                  left: 0;
                }

                .count {
                  right: 0;
                  left: inherit;

                  i {
                    font-size: $font-sm;
                    font-style: normal;
                  }
                }

                &.pro-code {
                  .code-r {
                    p {
                      display: inline-block;

                      & + p {
                        margin-left: $spacing-sm;
                      }
                    }
                  }
                }
              }
            }
          }
        }

        .btn {
          width: 86px;
          height: 26px;
          margin-top: 10px;
          line-height: 26px;
          text-align: center;
          font-size: $font-size-small;
          font-weight: 600;
          border-radius: 14px;
          border: 1px solid #69CFE8;
          background-color: $color-theme;
          float: right;
        }
      }

      .order-tr {
        display: block;
        font-size: $font-xm;

        & + .order-tr {
          margin-top: 28px;
        }

        .name {
          display: inline-block;
          text-align: left;
        }

        .price {
          display: inline-block;
          float: right;

          em {
            margin-left: 5px;
            font-style: normal;
          }

          &.orange {
            color: $color-orange;
          }
        }

        .order-btn {
          display: inline-block;
          margin-top: 14px;
          width: 116px;
          height: 30px;
          line-height: 30px;
          text-align: center;
          font-size: $font-size-small;
          color: #ffffff;
          background-color: #50C5E0;
          border-radius: 16px;
        }
      }
    }

    .btn-wrap {
      position: fixed;
      right: 0;
      bottom: 0;
      left: 0;
      display: flex;
      padding: $spacing-sm $spacing-base;
      background-color: $color-bg-white;
      box-shadow: 0px -1px 5px -3px rgbaMain(0.5);
      z-index: 100;

      .btn {
        flex: 1;
        lineHeight(45px);
        font-size: $font-lg;
        align(center);
        border-radius: 45px;

        & + .btn {
          margin-left: $spacing-base;
        }

        &.readonly {
          pointer-events: none;
        }
      }
    }
  }
}

// 分享弹窗
.share-wrap {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: $color-mask;
  z-index: 100;

  .share-arrow {
    position: absolute;
    left: 50%;
    width: 91px;
    height: 85px;
    bg-image('share-arrow');
    background-size: 100% 100%;
  }

  .share-text {
    position: relative;
    top: 92px;
    font-size: 16px;
    color: $color-font-white;
    text-align: center;
    line-height: 24px;
  }

  .share-icon {
    display: inline-block;
    margin: 0 10px;
    width: 32px;
    height: 8px;
    bg-image('share-icon');
    background-size: 100% 100%;
  }

  .share-img {
    position: relative;
    top: 141px;
    left: 50%;
    width: 260px;
    height: 126px;
    bg-image('share-img');
    background-size: 100% 100%;
    transform: translateX(-50%);
  }
}
</style>

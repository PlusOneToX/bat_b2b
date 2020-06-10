<template>
  <div class="orderList" :class="{ hiddenHeader: isMiniProgram }">
    <THeader
      class="header-wrap"
      :title="'我的订单'"
      :hasBg="true"
      @back="goBack"
    ></THeader>
    <div class="container">
      <van-tabs
        v-model="active"
        @click="handleClickv"
        :class="{ 'bg-white': orderList.length <= 0 }"
      >
        <van-tab
          v-for="(tab, k) in orderSource !== 28 ? orderTabs1 : orderTabs2"
          :key="k"
          :title="tab.name"
          :name="tab.id"
        >
          <van-pull-refresh v-model="isLoading" @refresh="onRefresh">
            <div class="order-list" v-show="orderList.length > 0">
              <div
                class="order-item"
                :class="tab.id === 4 ? 'disable' : ''"
                v-for="(order, index) in orderList"
                :key="index"
              >
                <div @click="orderDetail(order.orderInfo.id)">
                  <div class="order-top">
                    <div class="order-no">
                      订单编号：{{ order.orderInfo.orderNo }}
                    </div>
                    <div class="order-tag">
                      {{
                        orderStatus(order.orderCustomerData.frontOrderStatus)
                      }}
                    </div>
                  </div>
                  <div
                    class="order-con"
                    v-for="item in order.orderInfoDetailGoods"
                    :key="item.id"
                  >
                    <div class="order-img">
                      <img
                        v-lazy="
                          item.orderGoodsDiy && item.orderGoodsDiy.previewImage
                            ? item.orderGoodsDiy.previewImage
                            : ''
                        "
                        alt=""
                        class="img"
                      />
                    </div>
                    <div class="order-info">
                      <div class="tr-box">
                        <span class="name">{{
                          item.orderGoods.goodsName
                        }}</span>
                        <span class="price" v-if="orderSource !== 28"
                          >¥{{
                            item.orderGoodsCustomerCost.salePrice | fomatFloat
                          }}</span
                        >
                      </div>
                      <div class="text">
                        <div class="model">
                          <div class="pro-spe">
                            <span>规格：</span>
                            <p>
                              {{
                                item.orderGoodsDiy &&
                                item.orderGoodsDiy.modelName
                                  ? item.orderGoodsDiy.modelName
                                  : ""
                              }}
                            </p>
                            <p>
                              {{
                                item.orderGoodsDiy &&
                                item.orderGoodsDiy.materialName
                                  ? item.orderGoodsDiy.materialName
                                  : ""
                              }}
                            </p>
                            <span class="count"
                              ><i>X</i
                              >{{
                                item.orderGoods && item.orderGoods.itemCount
                                  ? item.orderGoods.itemCount
                                  : 0
                              }}</span
                            >
                          </div>
                          <!-- 金科 -->
                          <div class="pro-spe" v-show="distributorId === 1217">
                            <span>图片ID：</span>
                            <p>
                              {{
                                item.orderGoodsDiy &&
                                item.orderGoodsDiy.pictureId
                                  ? item.orderGoodsDiy.pictureId
                                  : ""
                              }}
                            </p>
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div
                  class="order-pay"
                  v-if="
                    order.orderCustomerData.frontOrderStatus === 1 &&
                    order.orderCustomerCost.payAmount > 0
                  "
                >
                  <div class="price-count">
                    <span class="text"
                      >共 {{ order.totalCount }} 件商品，{{
                        orderSource === "GF60006" || orderSource === "GF60007" || orderSource === "GF60008"
                          ? "费用"
                          : "邮费"
                      }}合计：<em>¥</em
                      ><strong>{{
                        order.orderCustomerCost.payAmount | fomatFloat
                      }}</strong></span
                    >
                  </div>
                  <div
                    class="order-btn"
                    :class="isDisable ? 'readonly' : ''"
                    @click="
                      submitOrder(
                        order.orderInfo.id,
                        order.orderCustomerData.payWay,
                        order.orderCustomerCost.payAmount
                      )
                    "
                  >
                    立即支付 {{ order.dayTime }}
                  </div>
                </div>
              </div>
              <div
                class="load-more"
                v-show="orderList.length < totalPage"
                @click="handleMore"
              >
                点击加载更多
              </div>
              <!-- 底线 -->
              <Divider
                ref="divider"
                class="divider-wrap"
                :text="'你看到我的底线啦'"
                v-show="orderList.length >= totalPage"
              ></Divider>
            </div>
          </van-pull-refresh>
          <!--无订单-->
          <div class="order-list none" v-show="orderList.length <= 0">
            <NoData :flagType="'no-order'" :showBtn="true"></NoData>
          </div>
        </van-tab>
      </van-tabs>

      <Loading v-if="loading" :message="message"></Loading>
    </div>
  </div>
</template>
<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
import Loading from "components/loading/loading";
import NoData from "components/noData/noData";
import Divider from "components/divider/divider";
// js
import { fomatFloat, countDown } from "common/js/common";
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  data() {
    return {
      title: "我的订单",
      userId: 0, // 用户ID
      phone: "", // 用户手机号
      distributorId: "", // 分销商
      orderSource: "", // 订单来源
      orderTabs1: [
        {
          id: 0,
          name: "全部",
        },
        {
          id: 1,
          name: "待支付",
        },
        {
          id: 2,
          name: "待发货",
        },
        {
          id: 4,
          name: "待收货",
        },
        {
          id: 6,
          name: "已完成",
        },
        {
          id: 5,
          name: "已关闭",
        },
      ],
      orderTabs2: [
        {
          id: 0,
          name: "全部",
        },
        {
          id: 1,
          name: "待支付",
        },
        {
          id: 2,
          name: "待发货",
        },
        {
          id: 4,
          name: "待收货",
        },
        {
          id: 6,
          name: "已完成",
        },
      ],
      orderList: [], // 订单列表
      active: 0, // 默认订单状态
      page: 1, // 数量倍数
      size: 5, // 页显示数量
      totalPage: 0, // 订单总数
      temp: null, // 倒计时
      isLoading: true, // 下拉刷新加载
      msg: "",
      loading: true, // 加载
      message: "数据加载中", // 加载内容
      weixin: {
        appid: "",
        timeStamp: "",
        nonceStr: "",
        prepayId: "",
        signType: "",
        paySign: "",
      },
      isDisable: false,
      isMiniProgram: false, // 是否是小程序
      orderTime: 0, // 订单失效时间
    };
  },
  mounted() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "orderList") {
      // 小程序进入
      var params = this.getQueryVariable("enterParams");
      if (params) {
        var enterParams = JSON.parse(params);
        localStorage.setItem("userId", enterParams.userId);
        localStorage.setItem("phone", enterParams.phone);
        localStorage.setItem("userNo", enterParams.userNo);
        localStorage.setItem("auth", enterParams.auth);
        localStorage.setItem("openId", enterParams.openid);
        localStorage.setItem("distributorId", enterParams.distributorId);
        localStorage.setItem("exchangeId", enterParams.exchangeId);
        localStorage.setItem("platform", enterParams.platform);
        localStorage.setItem("orderSource", enterParams.orderSource);

        // 状态ID
        let sid = enterParams.sid;
        if (sid) {
          this.active = Number(sid);
        }

        this.isMiniProgram = true;
        sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
      }
    } else {
      // 状态ID
      let sid = this.getQueryVariable("sid");
      if (sid) {
        this.active = Number(sid);
      }
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    // 获取用户信息
    this.userId = localStorage.getItem("userId");
    this.phone = localStorage.getItem("phone");
    this.distributorId = parseInt(localStorage.getItem("distributorId"));
    this.orderSource = localStorage.getItem("orderSource") || 28;

    // 获取全部订单
    this.getOrderList(this.size);
    // 获取订单失效时间
    this.getOrderTime();
  },
  methods: {
    getQueryVariable(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      } else {
        return null;
      }
    },
    // 加载更多
    handleMore() {
      this.page++;
      let size = this.page * this.size;
      this.getOrderList(size);
    },
    // 获取订单列表
    getOrderList(size) {
      this.loading = true;
      this.message = "数据加载中";

      // 获取订单列表
      this.$api
        .get(this, api.getOrderList, {
          orderSource: Number(this.orderSource) === 28 ? "28,58" : this.orderSource, // 58 为苏宁订单（兑换商城28共用订单列表）
          frontOrderStatus: this.active,
          customerId: this.userId,
          page: 1,
          size: size,
        })
        .then((res) => {
          if (res.success) {
            this.orderList = res.data.list;
            this.totalPage = res.data.total;

            this.orderList.forEach((order) => {
              let totalCount = 0;
              order.orderInfoDetailGoods.forEach((item) => {
                totalCount += item.orderGoods.itemCount;
              });
              this.$set(order, "totalCount", totalCount);
            });

            if (this.orderList.length > 0) {
              this.timer();
            }
          } else {
            this.$toast.fail(res.errMessage);
          }

          this.loading = false;
          this.message = "";
        });
    },
    // 订单状态
    orderStatus(val) {
      switch (val) {
        case 1:
          return "待支付";
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
    // 跳转订单详情
    orderDetail(id) {
      this.$router.push({ path: "/orderDetail", query: { id: id } });
    },
    // 切换订单状态
    handleClickv(val, title) {
      this.page = 1;
      this.active = val;
      this.getOrderList(this.size);
    },
    // 倒计时
    timer() {
      let vm = this;
      if (this.temp !== null) {
        return;
      }
      this.temp = setInterval(() => {
        this.orderList.forEach((item, index) => {
          // 待支付
          if (item.orderCustomerData.frontOrderStatus === 1) {
            let timeStr = item.orderCustomerCost.createTime.replace(/-/g, "/");
            let date = new Date(timeStr).getTime();
            let time = countDown(date + this.orderTime * 60 * 1000);
            if (time === "") {
              vm.$set(item, "dayTime", "");
              vm.destroyed();
            } else {
              vm.$set(item, "dayTime", time);
              vm.$set(
                this.orderList,
                item.dayTime,
                countDown(date + this.orderTime * 60 * 1000)
              );
            }
          }
        });
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
    // 倒序
    dataDown(x, y) {
      return y.createTime - x.createTime;
    },
    // 下拉刷新
    onRefresh() {
      setTimeout(() => {
        this.getOrderList(this.size);
        this.$toast("刷新成功");
        this.isLoading = false;
      }, 1000);
    },
    // 支付
    submitOrder(ids, payType, orderPrice) {
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
      let orderId = ids.toString();

      this.isDisable = true;
      setTimeout(() => {
        this.isDisable = false;
      }, 5000);
      // 2 微信，10 字节
      if (Number(payType) === 2 || Number(payType) === 10) {
        // 查看平台，判断微信支付方式
        if (platform === "1" || platform === "28" || platform === "GF60006") {
          // 微信公众号平台支付
          if (openid) {
            let info = {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_jsapi", // 交易方式
              orderId: orderId,
              payerId: this.userId,
              platformUserId: openid,
              redirectUrl: redirectUrl,
            };

            if (Number(platform) === 28) {
              // 兑换
              info = {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_mini_program", // 交易方式
                orderId: orderId,
                payerId: this.userId,
                platformUserId: openid,
                appId: appId,
              };
            } else if (platform === "GF60006") {
              // 字节小程序
              info = {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "toutiao", // 交易方式
                orderId: orderId,
                payerId: this.userId,
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
                    orderPrice: orderPrice,
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
                      orderPrice: orderPrice,
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
          let orderId = ids.toString();
          this.$api
            .post(this, api.handlePayment, {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_h5", // 交易方式
              orderId: orderId,
              payerId: this.userId,
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
          orderId: orderId,
          payerId: this.userId,
          redirectUrl: redirectUrl,
        };
        if (platform === "GF60007") {
          // 支付宝小程序（BAT小程序）
          info = {
            businessType: 1, // 业务类型：1 订单收款
            customerFlag: 1, // 客户标志：1 C端客户
            payMethod: "alipay_common", // 交易方式
            orderId: orderId,
            payerId: this.userId,
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
            orderId: orderId,
            payerId: this.userId,
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
                orderPrice: orderPrice,
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
    // 返回我的
    goBack() {
      this.$router.replace("/mine");
    },
  },
  filters: {
    fomatFloat(num, n) {
      return fomatFloat(num, 2);
    },
  },
  components: {
    THeader,
    Loading,
    NoData,
    Divider,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.orderList {
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
    height: 100%;

    >>>.van-tabs {
      height: 100%;

      &.bg-white {
        background-color: $color-bg-white;
      }

      .van-tabs__wrap {
        padding: 0;
        height: 62px;

        .van-tabs__nav {
          display: flex;
          padding: 0 $spacing-base;
          height: 62px;
          background-color: transparent;
        }

        .van-tab {
          display: inline-block;
          flex: 1;
          padding: 0;
          height: 62px;
          line-height: 62px;
          text-align: center;
          font-size: $font-xm;
          color: $color-font-grey;

          &:nth-child(2) {
            padding-left: 0;
          }

          &.van-tab--active, &:hover {
            font-size: $font-lg;
            color: $color-font-base;
          }
        }

        .van-tabs__line {
          position: absolute;
          bottom: 12px;
          width: 20px;
          height: 10px;
          bg-image('home-tab');
          background-color: transparent;
          background-size: 100% 100%;
        }

        &::after {
          background-color: transparent;
        }
      }

      .van-tabs__content {
        height: 100%;
        padding-bottom: 62px;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;

        &::-webkit-scrollbar {
          display: none;
        }

        .order-list {
          display: inline-block;
          width: 100%;
          padding: 0 $spacing-base;

          .order-item {
            position: relative;
            padding: $spacing-base $spacing-base $spacing-lg;
            box-sizing: border-box;
            border-radius: $radius-base;
            background-color: $color-bg-white;

            & + .order-item {
              margin-top: $spacing-sm;
            }

            .order-top {
              display: block;
              font-size: $font-base;
              line-height: 20px;

              .order-no {
                display: inline-block;
              }

              .order-tag {
                display: inline-block;
                color: $color-orange;
                float: right;
              }
            }

            .order-con {
              display: flex;
              display: -webkit-flex;
              padding: $spacing-lg 0 0;

              .order-img {
                position: relative;
                display: inline-block;
                width: 68px;
                height: 127px;
                margin-right: 28px;
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
                padding-top: 16px;

                .tr-box {
                  display: flex;
                  align-items: center;

                  .name {
                    display: -webkit-box;
                    flex: 1;
                    font-size: $font-lg;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                  }

                  .price {
                    display: inline-block;
                    font-size: $font-base;
                    font-weight: 400;
                    float: right;
                  }
                }

                .text {
                  padding-top: 14px;
                  color: $color-font-grey;

                  .model {
                    .pro-spe {
                      position: relative;
                      margin-top: $spacing-sm;
                      padding-left: 54px;
                      padding-right: 2em;
                      font-size: $font-base;
                      color: $color-font-grey;
                      line-height: 20px;

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
                    }
                  }
                }
              }
            }

            .order-pay {
              display: inline-block;
              width: 100%;
              margin-top: 12px;
              text-align: right;

              .price-count {
                display: block;
                color: $color-font-base;

                .text {
                  display: inline-block;
                  font-size: $font-xm;

                  em {
                    display: inline-block;
                    font-style: normal;
                    font-size: $font-base;
                    color: $color-orange;
                  }

                  strong {
                    display: inline-block;
                    margin-left: 2px;
                    font-size: $font-base;
                    color: $color-orange;
                  }
                }
              }

              .order-btn {
                display: inline-block;
                margin-top: 16px;
                margin-bottom: 4px;
                width: 165px;
                lineHeight(45px);
                text-align: center;
                font-size: $font-lg;
                color: $color-font-base;
                background-color: $color-main;
                border-radius: 45px;

                &.readonly {
                  pointer-events: none;
                }
              }
            }

            .order-status {
              position: absolute;
              top: 0;
              right: 0;
              display: inline-block;
              width: 64px;
              height: 26px;
              line-height: 26px;
              text-align: center;
              font-size: 12px;
              border-radius: 0 10px 0 10px;
              color: #4A4A4A;
              background-color: rgba(105, 207, 232, 0.2);
            }

            .btn {
              width: 86px;
              height: 26px;
              margin-top: 10px;
              line-height: 26px;
              text-align: center;
              font-size: 12px;
              font-weight: 600;
              border-radius: 14px;
              color: #69CFE8;
              border: 1px solid #69CFE8;
              float: right;
            }

            &.disable {
              color: #737373;

              .order-tag, em, strong {
                color: #737373 !important;
              }
            }
          }

          &.none {
            position: absolute;
            width: 100%;
            height: 100%;
            background-color: $color-bg-white;
          }

          .load-more {
            margin-top: 30px;
            text-align: center;
          }

          .divider-wrap {
            padding: 35px 0;
            background-color: $color-bg;

            .van-divider {
              margin: 0;
            }

            >>>.text {
              font-size: $font-sm;
              color: $color-font-grey;
              transform: scale(0.9);
            }
          }
        }
      }
    }
  }
}
</style>

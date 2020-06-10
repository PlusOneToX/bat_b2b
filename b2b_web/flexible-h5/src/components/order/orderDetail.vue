<template>
  <div
    class="orderDetail"
    :class="{
      'ry-style': Number(distributorId) === 4378,
      'no-tabbar': pageFlag && pageFlag === 'orderQuery',
    }"
  >
    <v-header :back="true" :title="title"></v-header>
    <div class="container" v-show="orderDetail">
      <div
        class="item-box"
        @click="openLogistics(orderCustomerData.frontOrderStatus)"
      >
        <div class="tr-header">
          <i
            class="iconfont icon"
            :class="iconStatus(orderCustomerData.frontOrderStatus)"
          ></i>
          <span class="text">{{
            orderStatus(orderCustomerData.frontOrderStatus)
          }}</span>
        </div>
        <!--待付款-->
        <div
          class="tr-info obligation"
          v-show="orderCustomerData.frontOrderStatus === 1 && time"
        >
          <div class="text">订单已提交，请尽快完成支付！</div>
          <div class="remind" v-show="time">
            剩余时间&nbsp;{{ time }}&nbsp;秒，超时订单将自动关闭
          </div>
        </div>
        <!--待发货-->
        <div
          class="tr-info shipping"
          v-show="orderCustomerData.frontOrderStatus === 2"
        >
          <div class="text">您的订单已提交至工厂制作，请耐心等待！</div>
        </div>
        <!--待收货-->
        <div
          class="tr-info shipped"
          v-show="orderCustomerData.frontOrderStatus === 4"
        >
          <div class="text-desc" v-if="orderDeliverBill">
            <div class="time">快递单号：{{ orderDeliverBill.logisticsNo }}</div>
            <div class="text">
              服务商：{{ orderDeliverBill.distributionName }}
            </div>
          </div>
          <div class="text-desc" v-else>
            <div class="text">暂无物流信息</div>
          </div>
          <i class="icon icon-right" v-if="orderDeliverBill"></i>
        </div>
        <!--已完成-->
        <div
          class="tr-info complete"
          v-show="orderCustomerData.frontOrderStatus === 6"
        >
          <div class="text-desc" v-if="orderDeliverBill">
            <div class="time">快递单号：{{ orderDeliverBill.logisticsNo }}</div>
            <div class="text">
              服务商：{{ orderDeliverBill.distributionName }}
            </div>
          </div>
          <i class="icon icon-right" v-if="orderDeliverBill"></i>
        </div>
      </div>
      <div class="item-box">
        <div class="address-info">
          <i class="iconfont icon icon-c-location"></i>
          <div class="top">
            <span class="name">{{ orderDelivery.userName }}</span>
            <span class="tel">{{ orderDelivery.mobile }}</span>
          </div>
          <div class="bottom address">
            {{ orderDelivery.detail }}
          </div>
        </div>
      </div>
      <div class="item-box">
        <div class="order-item">
          <div
            class="order-con"
            :class="
              orderDetail.orderInfoDetailGoods.length > 1 ? 'border-b' : ''
            "
            v-for="(order, index) in orderDetail.orderInfoDetailGoods"
            :key="index"
          >
            <div class="order-img">
              <img
                :src="
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
                <span class="price" v-show="showPrice"
                  >¥{{
                    order.orderGoodsCustomerCost.salePrice | fomatFloat
                  }}</span
                >
              </div>
              <div class="spec">
                <div class="text">
                  <div>
                    规格：{{
                      order.orderGoodsDiy && order.orderGoodsDiy.modelName
                        ? order.orderGoodsDiy.modelName
                        : ""
                    }}
                    -
                    {{
                      order.orderGoodsDiy && order.orderGoodsDiy.materialName
                        ? order.orderGoodsDiy.materialName
                        : ""
                    }}
                  </div>
                  <!--金科-->
                  <div v-show="distributorId === 1217">
                    图片ID：{{
                      order.orderGoodsDiy && order.orderGoodsDiy.pictureId
                        ? order.orderGoodsDiy.pictureId
                        : ""
                    }}
                  </div>
                </div>
                <span class="count"
                  >X{{
                    order.orderGoods && order.orderGoods.itemCount
                      ? order.orderGoods.itemCount
                      : 0
                  }}</span
                >
              </div>
              <div
                class="btn btn-submit"
                v-for="theme in themeList"
                :key="theme.id"
                v-show="
                  theme.id === order.orderGoodsDiy.pictureId &&
                  themeList.length > 0 &&
                  orderCustomerData.frontOrderStatus !== 1 &&
                  orderCustomerData.frontOrderStatus !== 5
                "
                @click="handleUrl(theme.themeUrl)"
              >
                主题下载
              </div>
            </div>
          </div>
          <div class="order-tr" v-show="showPrice">
            <div class="name">运费</div>
            <div class="price">
              ¥<em>{{ deliveryFee | fomatFloat }}</em>
            </div>
          </div>
          <div class="order-tr" v-show="showPrice">
            <div class="name">优惠</div>
            <div class="price">
              -¥<em>{{
                (orderCustomerCost.orderCouponAmount + deliveryFee) | fomatFloat
              }}</em>
            </div>
          </div>
          <div class="order-tr" v-show="showPrice">
            <div class="name">总价</div>
            <div
              class="price font-m"
              :class="orderCustomerData.frontOrderStatus === 5 ? '' : 'red'"
            >
              ¥<em>{{ orderCustomerCost.payAmount | fomatFloat }}</em>
            </div>
          </div>
        </div>
      </div>
      <div class="item-box padding-d">
        <div class="tr-header">订单信息</div>
        <div class="order-item">
          <div class="order-tr">
            <div class="name">订单编号：{{ orderInfo.orderNo }}</div>
          </div>
		  <div class="order-tr" v-show="orderCustomerData.remark">
			<div class="name">订单备注：{{ orderCustomerData.remark }}</div>
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
      <div
        class="btn-box"
        v-show="orderCustomerData.frontOrderStatus === 1 && time"
      >
        <div class="btn btn-cancel" @click="cancelOrder">取消订单</div>
        <div
          class="btn btn-submit"
          :class="isDisable ? 'readonly' : ''"
          @click="submitOrder"
        >
          立即支付
        </div>
      </div>
    </div>
	
    <tabs
      v-show="showTabbar"
      :curTab="'mine'"
      :curVersion="curVersion"
      :userNo="userNo"
    ></tabs>
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import Tabs from "../../view/components/tabs.vue";
import Loading from "base/loading/loading";
import api from "common/js/allApi.js";
import { formatDate, fomatFloat, countDown,getLocalStorageItem } from "common/js/common";
import { aliPay } from "common/js/pay";

export default {
  name: "orderDetail",
  data() {
    return {
      title: "订单详情",
      message: "",
      distributorId: "", // 分销商ID
      pictureIds: "",
      payType: 0, // 支付方式
      orderId: 0,
      orderDetail: {},
      orderInfo: {},
      orderDelivery: {},
      orderCustomerCost: {},
      orderCustomerData: {},
      orderDeliverBill: {},
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
      curVersion: "",
      userNo: "",
      showPrice: true, // 是否显示价格
      showTabbar: true, // 是否显示底部tab
      orderTime: 0, // 订单失效时间
      deliveryFee: 0, // 运费
      pageFlag: "" // 页面标识
    };
  },
  created() {
    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本
    this.userNo = getLocalStorageItem("userNo");
	  this.distributorId = parseInt(localStorage.getItem("distributorId"));
    this.message = "载入中";
    this.isLoading = true;
    this.orderId = this.$route.query.id;
    if(this.orderId){
      localStorage.setItem("comingFlag", "orderDetail");
      localStorage.setItem("orderId", this.$route.query.id);
    }
	  this.platform = this.$route.query.platform;
    if (
      this.userNo !== "" &&
      this.userNo !== null &&
      this.userNo !== undefined
    ){
      console.log("已登录 = " + this.orderId);
      localStorage.setItem("id_out_detail", this.orderId);
      localStorage.setItem("is_out_detail", "no");
    }else{
      console.log("未登录 = " + this.orderId);
      // 未登录
      this.curVersion = "new";
      this.platform = 7;
      this.distributorId = 2529;
      localStorage.setItem("curVersion", this.curVersion);
      localStorage.setItem("platform", this.platform);
      localStorage.setItem("distributorId", this.distributorId);
      localStorage.setItem("id_out_detail", this.orderId);
      localStorage.setItem("is_out_detail", "yes");
    }
	
    if (Number(this.distributorId) === 4378) {
      // 荣耀
      this.showPrice = false; // 是否显示价格
      this.showTabbar = false; // 是否显示底部tab
    } else {
      this.showPrice = true; // 是否显示价格
      this.showTabbar = true; // 是否显示底部tab
    }

    // 获取订单详情
    this.getOrderDetail();
    // 获取订单失效时间
    this.getOrderTime();

    this.pageFlag = this.$route.query.pageFlag;
    if (this.pageFlag && this.pageFlag === "orderQuery") {
      // 判断进入是否带有页面标识，有就不显示底部tabbar
      this.showTabbar = false;
    }
    document.addEventListener( "plusready", onPlusReady);
  },
  methods: {
    // 获取订单详情
    getOrderDetail() {
      this.$api
        .get(this, api.getOrderDetail, { id: this.orderId })
        .then((res) => {
		   console.log(res);
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
              this.orderDeliverBill =
                this.orderDetail.orderDeliverDetail[0].orderDeliverBill;

              // 已签收状态，核销订单
              let deliveryStatus = this.orderDeliverBill.logisticsStatus;
              if (
                Number(this.distributorId) === 4378 &&
                Number(deliveryStatus) === 3
              ) {
                this.handleWriteOffOrder();
              }
            }

            this.orderDelivery = this.orderDetail.orderDelivery;
            let detail =
              this.orderDelivery.provinceName +
              this.orderDelivery.cityName +
              this.orderDelivery.districtName +
              this.orderDelivery.address;
            this.$set(this.orderDelivery, "detail", detail); // 详细地址（包含省市区）

            this.orderCustomerCost = this.orderDetail.orderCustomerCost;

            this.orderDetail.orderInfoDetailGoods.forEach((item, idx) => {
              if (item.orderGoodsDiy) {
                this.pictureIds += item.orderGoodsDiy.pictureId;
                if (idx < this.orderDetail.orderInfoDetailGoods.length - 1) {
                  this.pictureIds += ",";
                }
              }

              if (
                item.orderGoodsCustomerCost &&
                Number(item.orderGoodsCustomerCost.couponMethod) === 3
              ) {
                // 兑换
                if (Number(item.orderGoodsCustomerCost.deliveryFeeFlag) === 1) {
                  // 收邮费
                  this.deliveryFee += item.orderGoodsCustomerCost.actualPrice;
                }
              }
            });

            let platform = localStorage.getItem("platform");
            if (Number(platform) === 7) {
              this.getThemeUrl();
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
    testApp(url, openUrl) {
      // 已经安装App，下面的this.url是需要跳转到app的schema链接。
      window.location.href = openUrl;
      // const t = 1000
      // var t1 = Date.now();
      // var ifr = document.createElement("iframe");
      // // 下面的this.url 是需要跳转到app的schema链接
      // ifr.setAttribute("src", url);
      // ifr.setAttribute("style", "display:none");
      // document.body.appendChild(ifr);
      // setTimeout(function() {
      // 	// 启动app时间较长处理
      //   const t2 = Date.now()
      //   document.body.removeChild(ifr)
      //   if (t2 - t1 < t + 100) {
      //     this.$toast("您没有安装微信，请先安装微信!");
      //     this.$router.push("/orderList");
      //   } else {
      //     已经安装App，下面的this.url是需要跳转到app的schema链接。
      //     window.location.href = openUrl;
      //   }
      // }, t)
    },
    // 支付
    submitOrder() {
      this.isDisable = true;
      setTimeout(() => {
        this.isDisable = false;
      }, 5000);
      
	  let redirectUrl = "https://test.bat.com/diyh5/orderList";
	  if (process.env.NODE_ENV === "production") {
	  	  redirectUrl = "https://diy.bat.com/orderList";
	  }
      if (this.payType === 2) {
        // 查看平台，判断微信支付方式
        let platform = localStorage.getItem("platform");
        if (platform === "1") {
          // 微信公众号平台支付
          let openid = localStorage.getItem("openId");
          let appId = localStorage.getItem("appid");
          let userId = getLocalStorageItem("userId");
          if (openid) {
            this.$api
              .post(this, api.handlePayment, {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_jsapi", // 交易方式
                orderId: this.orderId,
                payerId: userId,
                platformUserId: openid,
                appId: appId,
                redirectUrl: redirectUrl,
              })
              .then((res) => {
                if (res.success) {
                  let wxConfig = res.data.wxResp;
                  this.weixin.appid = wxConfig.appId;
                  this.weixin.nonceStr = wxConfig.nonceStr;
                  this.weixin.prepayId = wxConfig.prepayId;
                  this.weixin.signType = wxConfig.signType;
                  this.weixin.timeStamp = wxConfig.timeStamp;
                  this.weixin.paySign = wxConfig.paySign;
      
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
                } else {
                  this.$toast(res.errMessage);
                }
              });
          } else {
            this.$toast("微信未授权");
          }
        } else {
          let userId = getLocalStorageItem("userId");
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
                // 判断当前环境
                var ua = navigator.userAgent.toLowerCase();
                let url = res.data.wxResp.h5Url;
                if (ua.match(/MicroMessenger/i) == "micromessenger") {
                  // 微信浏览器
                  window.location.href = url;
                } else {
                  if (url) {
                    this.testApp("weixin://", url);
                  }
                }
              } else {
                this.$toast.fail(res.errMessage);
              }
            })
            .catch((error) => {
              console.log(error);
            });
        }
      } else {
        // 支付宝
        let userId = getLocalStorageItem("userId");
        this.$api
          .post(this, api.handlePayment, {
            businessType: 1, // 业务类型：1 订单收款
            customerFlag: 1, // 客户标志：1 C端客户
            payMethod: "alipay_wap", // 交易方式
            orderId: this.orderId,
            payerId: userId,
            redirectUrl: redirectUrl,
          })
          .then((res) => {
            if (res.success) {
              let data = res.data.alipayResp.from;
              if (data) {
                aliPay(data);
              }
            } else {
              this.$toast.fail(res.errMessage);
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    onBridgeReady(redirectUrl) {
      /* eslint-disable */
      let that = this;
      WeixinJSBridge.invoke(
        "getBrandWCPayRequest",
        {
          appId: that.weixin.appid, // 公众号名称，由商户传入
          timeStamp: that.weixin.timeStamp, // 时间戳，自1970年以来的秒数
          nonceStr: that.weixin.nonceStr, // 随机串
          package: that.weixin.prepayId,
          signType: that.weixin.signType, // 微信签名方式：
          paySign: that.weixin.paySign, // 微信签名
        },
        function (res) {
          if (res.err_msg === "get_brand_wcpay_request:ok") {
          } else {
            that.$toast(res.err_msg);
          }
          window.location.href = redirectUrl;
        }
      );
    },
    // 取消订单
    cancelOrder() {
      this.$dialog
        .confirm({
          message: "您确定取消订单吗？",
          className: "confirm-v-dialog",
          confirmButtonColor: "#31B8D8",
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
          return "icon-c-payment";
        case 2:
          return "icon-c-ship";
        case 4:
          return "icon-c-shipping";
        case 5:
          return "已关闭";
        case 6:
          return "icon-c-shipped";
      }
    },
    // 物流信息
    openLogistics(status) {
      // 待收货或已完成
      if (status === 4 || status === 6) {
        this.$router.push({
          path: "/logisticsDetail",
          query: { id: this.orderId, pageFlag: this.pageFlag },
        });
      }
    },
    // 核销订单
    handleWriteOffOrder() {
      let ryFlowId = sessionStorage.getItem("ryFlowId");

      this.$api
        .put(this, api.writeOffOrder, { code: ryFlowId })
        .then((res) => {
          if (res.success) {
            this.$dialog
              .confirm({
                title: "温馨提示",
                message: "快递已签收，当前权益履约完成。退出后可开始新的权益。",
                className: "confirm-v-dialog tl",
                confirmButtonText: "关闭",
                showCancelButton: false,
              })
              .then(() => {})
              .catch((error) => {
                console.log(error);
              });
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getThemeUrl() {
      this.$api
        .get(this, api.getThemeUrl, {
          idList: this.pictureIds,
        })
        .then((res) => {
          if (res.success) {
            let themeData = res.data;
            themeData.forEach((item) => {
              if (item.themeUrl) {
                this.themeList.push(item);
              }
            });
          }
        });
    },
    handleUrl(url) {
      this.$dialog
        .alert({
          title: "提醒",
          message: "此主题只有限定机型才可使用",
          className: "confirm-v-dialog",
          confirmButtonColor: "#0076A5",
        })
        .then(() => {
          window.location.href = url;
        });
    }
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
    VHeader,
    Tabs,
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
  top: 45px;
  bottom: 49px;
  background-color: $color-background;

  &.no-tabbar {
    bottom: 0;
  }

  .container {
    height: 100%;
    padding: 15px 15px 69px;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .item-box {
      display: inline-block;
      width: 100%;
      margin-bottom: 15px;
      padding: 15px;
      color: $color-text;
      box-sizing: border-box;
      border-radius: 8px;
      background-color: #ffffff;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      &.padding-d {
        padding: 18px 10px;

        .tr-header {
          margin-bottom: 16px;
        }
      }

      .address-info {
        position: relative;
        left: -5px;
        padding-left: 30px;
        color: #4A4A4A;

        .top {
          font-size: 16px;
          line-height: 25px;
          font-weight: 600;

          .tel {
            font-size: 18px;
            margin-left: 24px;
          }
        }

        .bottom {
          margin-top: 9px;
          line-height: 20px;
          font-size: 14px;
        }

        .icon {
          position: absolute;
          font-size: 22px;

          &.icon-c-location {
            top: 2px;
            left: 0;
            color: #1CB8CE;
          }

          &.icon-right {
            top: 50%;
            right: 0;
            font-size: 26px;
            color: #9B9B9B;
            transform: translateY(-50%);
          }
        }
      }

      .order-info {
        position: relative;
        display: flex;

        .img {
          display: inline-block;
          width: 90px;
          height: 90px;
          border-radius: 6px;
          background-color: #999;
        }

        .order-detail {
          flex: 1;
          margin: 8px 0 8px 8px;

          .name {
            display: inline-block;
            text-align: left;
            font-size: 14px;
            font-weight: 600;
          }

          .price {
            display: inline-block;
            color: #D0021B;
            font-size: 14px;
            float: right;

            em {
              display: inline-block;
              font-size: $font-size-medium-x;
              font-style: normal;
            }
          }

          .text {
            position: absolute;
            bottom: 0;
            display: flex;
            display: -webkit-flex;
            color: $color-text-d;
            vertical-align: text-bottom;

            .model {
              display: -webkit-box;
              flex: 1;
              font-size: $font-size-small;
              line-height: 1.2;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .count {
              display: inline-block;
              width: 50px;
              line-height: 1.2;
              font-size: $font-size-small;
              text-align: right;
            }
          }
        }
      }

      .tr-box {
        display: flex;
        align-items: center;
        margin-top: 2px;

        .name, .text {
          display: inline-block;
          font-size: 14px;
          font-weight: 600;
          color: #333333;
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

      .tr-header {
        font-size: $font-size-medium-x;
        font-weight: bold;
        color: $color-text-d;
        line-height: 20px;

        .icon {
          position: relative;
          top: 1px;
          display: inline-block;
          margin-right: 5px;
          font-size: 20px;
          color: $color-theme;
        }

        .text {
          font-size: $font-size-medium-x;
          font-weight: bold;
          color: $color-text-d;
        }
      }

      .tr-info {
        padding: 14px 0 0 28px;
        box-sizing: border-box;

        &.obligation {
          .text {
            font-size: $font-size-medium;
            color: $color-text-d;
          }

          .remind {
            margin-top: 8px;
            font-size: $font-size-small;
            color: $color-text-r;
          }
        }

        &.shipping {
          .text {
            font-size: $font-size-medium;
            color: $color-text-r;
          }
        }

        &.shipped {
          display: flex;

          .text-desc {
            flex: 1;

            .text {
              display: -webkit-box;
              line-height: 1.5;
              font-size: $font-size-medium;
              color: $color-text-d;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .time {
              margin-top: 10px;
              font-size: $font-size-small;
              color: $color-text-g;
            }
          }

          .icon {
            display: inline-block;
            font-size: $font-size-large-xl;
            color: $color-text-l;
          }
        }

        &.complete {
          display: flex;

          .text-desc {
            flex: 1;

            .text {
              display: -webkit-box;
              line-height: 1.5;
              font-size: $font-size-small;
              color: $color-text-d;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .time {
              margin-top: 10px;
              font-size: $font-size-small;
              color: $color-text-g;
            }
          }

          .icon {
            display: inline-block;
            font-size: $font-size-large-xl;
            color: $color-text-l;
          }
        }
      }

      .order-item {
        position: relative;
        margin-top: 2px;
        box-sizing: border-box;
        border-radius: 8px;
        background-color: #ffffff;

        .order-top {
          display: block;

          .order-no {
            display: inline-block;
            font-size: 14px;
            color: $color-text-d;
          }

          .order-tag {
            display: inline-block;
            font-size: $font-size-medium;
            color: #D0021B;
            float: right;
          }
        }

        .order-con {
          display: flex;
          display: -webkit-flex;
          padding: 10px 0 18px;

          &.border-b {
            border-bottom: 1px solid #F6F6F6;
          }

          .order-img {
            position: relative;
            display: inline-block;
            width: 90px;
            height: 90px;
            margin-right: 10px;
            border-radius: 10px;
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
            display: inline-block;
            flex: 1;

            .tr-box {
              display: flex;
              align-items: center;

              .name {
                display: -webkit-box;
                flex: 1;
                font-size: $font-size-medium;
                font-weight: 500;
                color: $color-text-b;
                line-height: 1.5;
                overflow: hidden;
                text-overflow: ellipsis;
                -webkit-line-clamp: 2;
                -webkit-box-orient: vertical;
              }

              .price {
                display: inline-block;
                margin-left: 26px;
                font-size: $font-size-medium;
                font-weight: 400;
                color: $color-text-b;
                float: right;
              }
            }

            .spec {
              display: flex;
              margin-top: 16px;
              font-size: $font-size-small;
              align-items: center;

              .text {
                flex: 1;
                line-height: 1.5;
                font-weight: 400;
                color: $color-text-d;
              }

              .count {
                display: inline-block;
                margin-left: 30px;
                float: right;
                color: $color-text-b;
              }
            }
          }
        }

        .order-tr {
          display: block;
          margin-top: 14px;

          .name {
            display: inline-block;
            text-align: left;
            font-size: $font-size-medium;
            color: $color-text-d;
          }

          .price {
            display: inline-block;
            font-size: $font-size-medium;
            color: $color-text-g;
            float: right;

            em {
              margin-left: 5px;
              font-style: normal;
            }

            &.font-m {
              font-size: $font-size-medium-x;
            }

            &.red {
              color: $color-text-r;
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
            background-color: #0076A5;
            border-radius: 16px;
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
          font-size: $font-size-small;
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
          font-size: $font-size-small;
          font-weight: 600;
          border-radius: 14px;
          color: $color-text-w;
          border: 1px solid #0076A5;
          background-color: $color-theme;
          float: right;
        }
      }
    }

    .btn-box {
      margin-top: 14px;
      text-align: right;

      .btn {
        display: inline-block;
        margin-left: 6px;
        width: 110px;
        height: 30px;
        line-height: 30px;
        font-size: $font-size-medium;
        text-align: center;
        border-radius: 15px;

        &.btn-cancel {
          display: inline-block;
          color: $color-text-g;
          background-color: $color-background-g;
        }

        &.btn-submit {
          display: inline-block;
          color: $color-text-w;
          background-color: $color-theme;

          &.readonly {
            pointer-events: none;
          }
        }
      }
    }
  }
}

.flexible-dialog{
  	position: fixed;
  	top: 0;
  	left: 0;
  	width: 100%;
  	height: 100%;
  	background-color: rgba(0, 0, 0, 0.5);
  	z-index: 1000;
	display: flex;
	justify-content center;
	align-items center;
	.flexible-dialog-content {
	  position: absolute;
	  width: 90%;
	  background-size: 100% 100%;
	  height: 390px;
	  background-color: #ffffff;
	  border-radius: 12px
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  flex-direction: column;
	  .flexible-cotent-top{
		  width: 90%;
		  height: 100px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  .flexible-cotent-top-img{
			  width: 100%;
			  height: auto;
		  }
	  }
	  
	  .flexible-cotent-middle-title{
		  width: 85%;
		  height: 50px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  margin-top: 20px;
		  font-size: 18px;
		  color: #333333;
		  font-weight: bold;
	  }
	  
	  .flexible-cotent-middle-info{
		  width: 85%;
	      display: flex;
		  justify-content space-between;
		  font-size: 15px;
		  color: #666666;
		  letter-spacing: 0.8px;
		  margin-top: 5px;
		  line-height: 22.5px;
		  text-align: center;
	  }
	  
	  .flexible-cotent-bottom{
		  width: 195px;
		  height: 43px;
		  line-height: 43px;
		  text-align: center;
	      display: flex;
		  justify-content: center;
		  font-size: 18px;
		  color: #ffffff;
		  background-color: #0076A5;
		  border-radius: 21.5px
		  margin-top: 40px;
	  }
	}
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;

// 荣耀
.ry-style {
  top: 0;
  bottom: 0;
  background-color: $color-bg;

  .vheader {
    display: none;
  }

  .container {
    padding: 8px 16px;

    .item-box {
      margin-bottom: 8px;

      .address-info {
        .icon {
          &.icon-c-location {
            color: $color-main;
          }
        }
      }

      .tr-header {
        .icon {
          color: $color-main;
        }
      }

      .order-item {
        position: relative;
        margin-top: 2px;
        box-sizing: border-box;
        border-radius: 8px;
        background-color: #ffffff;

        .order-tr {
          .order-btn {
            background-color: $color-main;
          }
        }

        .btn {
          border: 1px solid $color-main;
          background-color: $color-main;
        }
      }
    }

    .btn-box {
      .btn {
        &.btn-submit {
          background-color: $color-main;
        }
      }
    }
  }
}
</style>
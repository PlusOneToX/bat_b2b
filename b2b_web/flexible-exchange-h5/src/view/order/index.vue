<template>
  <div class="index" :class="{ hiddenHeader: isMiniProgram }">
    <THeader
      class="header-wrap"
      :title="'提交订单'"
      :hasBg="true"
      @back="goBack"
    ></THeader>
    <div class="container">
      <!----公告----->
      <div class="notice-box" v-if="showNotice">
        <Notice></Notice>
      </div>
      <div class="item-box address-wrap" @click="handleAddress(curAddrId)">
        <div v-show="addArr > 0">
          <div class="address-detail">
            <span class="sprite-icon icon-location"></span>
            <div class="top">
              <p class="name">
                <span class="tag" v-if="addForm.defaultFlag === 1">默认</span>
                {{ addForm.userName }}
              </p>
              <p class="tel">{{ addForm.mobile }}</p>
            </div>
            <div class="bottom">
              <p class="address">{{ addForm.detail }}</p>
              <p class="detail">{{ addForm.address }}</p>
            </div>
          </div>
        </div>
        <div v-show="addArr <= 0">
          <div class="no-address">
            <span class="sprite-icon icon-location"></span>
            <span class="text">请填写收货地址</span>
          </div>
        </div>
        <van-icon name="arrow" />
      </div>
      <div class="order-wrap" v-for="(goods, pIndex) in mGoods" :key="goods">
        <div class="item-box" v-for="(good, index) in goods" :key="index">
          <div class="order-info">
            <div class="pro-img">
              <img :src="good.diy.previewImage" alt="" class="img" />
            </div>
            <div class="order-detail">
              <div class="tbox">
                <span class="name">{{ good.itemName }}</span>
                <span class="price" v-if="orderSource !== 28"
                  >¥<em
                    :class="
                      couponMethod === 3 &&
                      code &&
                      code.indexOf(good.itemCode) > -1 &&
                      index === 0 &&
                      good.itemCount === 1
                        ? 'line'
                        : ''
                    "
                    >{{ good.salePrice }}</em
                  ></span
                >
              </div>
              <div class="text">
                <div class="model">
                  <div class="pro-spe">
                    <span>规格：</span>
                    <p>{{ good.diy.modelName }}</p>
                    <p>{{ good.diy.materialName }}</p>
                    <span class="count"><i>X</i>{{ good.itemCount }}</span>
                  </div>
                  <!-- 金科 -->
                  <div class="pro-spe" v-show="distributorId === 1217">
                    <span>图片ID：</span>
                    <p>{{ good.diy.pictureId }}</p>
                  </div>
                </div>
              </div>

              <div
                class="code-wrap"
                :class="{
                  hasCode: good.secretCodes,
                }"
                @click="handleCard(good, pIndex, index, good.secretCodes)"
                v-if="orderSource === 28"
              >
                <span class="title">兑换卡</span>
                <div class="secret-wrap">
                  <p class="secret" v-show="good.secretCodes">
                    {{ good.secretCodes }}
                  </p>
                  <span class="no-secret" v-show="!good.secretCodes"
                    >无可用，点击添加</span
                  >
                </div>
                <p class="need-secret" v-show="good.curNeedNum">
                  <span>还需{{ good.curNeedNum }}张兑换卡</span>
                  <van-icon name="arrow" />
                </p>
                <van-icon name="arrow" />
              </div>
            </div>
          </div>
        </div>
        <div class="item-box double" v-if="orderSource === 28">
          <div class="tr-box">
            <div class="left">
              配送方式<span>{{
                goods.deliveryPrice > 0 ? "普通快递" : ""
              }}</span>
            </div>
            <div class="text">{{ express }}</div>
            <div class="price" :class="goods.deliveryPrice > 0 ? 'sale' : ''">
              <span class="desc" v-show="goods.deliveryPrice > 0">运费：</span>
              {{
                goods.hasCode
                  ? goods.deliveryPrice === 0
                    ? "包邮"
                    : "¥ " + goods.deliveryPrice
                  : ""
              }}
            </div>
          </div>
          <div class="tr-box">
            <div class="left">订单备注：</div>
            <van-field
              class="input"
              v-model="message"
              placeholder="您还有什么要求，请在这里说"
              clearable
            />
          </div>
        </div>
        <div class="item-box double" v-else>
          <div class="tr-box" @click="openExpressage">
            <div class="left">配送方式</div>
            <div class="text">{{ express }}</div>
            <div class="price" :class="goods.deliveryPrice > 0 ? 'sale' : ''">
              <span class="desc" v-show="goods.deliveryPrice > 0">物流费</span>
              {{
                goods.deliveryPrice === 0 ? "包邮" : "¥ " + goods.deliveryPrice
              }}
            </div>
            <van-icon name="arrow" />
          </div>
          <div class="tr-box">
            <div class="left">费用总计</div>
            <div class="price sale right">
              ¥<em>{{ goods.orderPrice }}</em>
            </div>
          </div>
        </div>
      </div>
      <div class="item-box double" v-if="orderSource !== 28">
        <div class="tr-box" @click="openSale">
          <div class="left">商品优惠</div>
          <div class="text"></div>
          <div
            class="price"
            :class="saleFlag ? 'sale' : 'nosale'"
            v-show="salePrice > 0 || saleDiscount > 0"
          >
            {{ saleValue }}
          </div>
          <van-icon name="arrow" />
        </div>
        <div class="tr-box">
          <div class="left">买家留言：</div>
          <van-field
            class="input"
            v-model="message"
            placeholder="您还有什么要求，请在这里说"
            clearable
          />
        </div>
      </div>
      <!-- <div class="item-box pay">
        <div class="tr-header">支付方式</div>
        <div class="tr-box">
          <i class="icon icon-pay-wechat"></i>
          <span class="pname">微信支付</span>
          <input
            type="radio"
            class="radio"
            name="pay"
            value="2"
            v-model="payType"
            checked
          />
        </div>
        <div class="tr-box" v-if="!(platform === 1 || shopId)">
          <i class="icon icon-alipay"></i>
          <span class="pname">支付宝</span>
          <input
            type="radio"
            class="radio"
            name="pay"
            value="1"
            v-model="payType"
          />
        </div>
      </div> -->
      <div class="protocol-box">
        <span class="checkbox"
          ><van-checkbox
            v-model="checked"
            @change="handleCheck(checked)"
          ></van-checkbox
        ></span>
        <div class="text">
          兑换即同意<span class="point" @click="openProtocol"
            >《定制产品兑换协议》</span
          >
        </div>
      </div>
    </div>
    <div class="btn-box">
      <div class="total" v-if="orderSource !== 28">
        合计：<span class="price"
          >¥<em>{{ totalPrice }}</em></span
        >
      </div>
      <div class="total" v-else>
        共使用<span class="num">{{ usedNum }}</span
        >张兑换卡
      </div>
      <div
        class="btn"
        :class="isDisable ? 'readonly' : ''"
        @click="getDeliveryStopStatus"
      >
        {{
          orderSource === 28
            ? hasMailFee
              ? "提交订单"
              : "立即兑换"
            : "支付订单"
        }}
      </div>
    </div>
    <!----配送方式------>
    <van-popup v-model="showExpressage" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showExpressage = false"
        @confirm="onConfirm"
      />
    </van-popup>
    <!----商品优惠------>
    <van-popup
      class="salePop"
      v-model="showSale"
      round
      position="bottom"
      :style="{ height: '80%' }"
    >
      <div class="pop-header">
        <div class="title">商品优惠</div>
        <i class="icon icon-close" @click="showSale = false"></i>
      </div>

      <div class="pop-info">
        <van-tabs v-model="tabActive" swipeable @click="handleTab">
          <van-tab
            :title="tab.title"
            :name="tab.id"
            v-for="tab in couponTabs"
            :key="tab.id"
          >
            <Coupon
              v-if="status === tab.id"
              :status="status"
              :page="page"
              nouserShow="true"
              :size="size"
              :tabTitle="tab.title"
              :totalPage="totalPage"
              :tabValue="tab.value"
              :couponList="couponList"
              @cancel="cancelCoupon"
              @more="getMore"
              @item="handleItem"
              @refresh="refresh"
            ></Coupon>
          </van-tab>
        </van-tabs>
      </div>
    </van-popup>
    <!---协议---->
    <div class="protocol-wrapper" :class="fadeProtocol ? 'show' : ''">
      <Protocol :timeCount="timeCount" @agree="handleAgree"></Protocol>
    </div>

    <div class="mask" v-show="showMask" @click="closeMask"></div>

    <transition name="fadeDialog">
      <div class="card-dialog" v-show="cardListShow">
        <!-- 已有兑换卡 -->
        <div class="card-wrap card-list-wrap">
          <div class="title">
            <h6 class="text">
              兑换卡
              <span
                >所需 {{ needNum }} 张兑换卡/已选
                {{ choosedNum }} 张兑换卡</span
              >
            </h6>
            <span class="close" @click="closeDialog">确定</span>
          </div>

          <ul class="card-list">
            <li
              v-for="(code, index) in cardList"
              :key="index"
              @click="handleSelect(code)"
            >
              <div class="card-t">
                <img :src="code.headImg" :alt="code.codeName" />
              </div>
              <div
                class="mail-fee"
                v-if="Number(code.mailSetting) === 2 && code.mailFee"
              >
                <p>（需支付 {{ code.mailFee }} 元运费）</p>
              </div>
              <div class="card-b">
                <span class="time"
                  >有效期至：{{ code.endTime | formatDate }}</span
                >
                <span class="code">{{ code.secretCode }}</span>
              </div>

              <span class="checkbox" v-if="code.canSelect">
                <van-checkbox v-model="code.hasNSelect"></van-checkbox>
              </span>
              <span class="checkbox isStock" v-else></span>
            </li>
          </ul>

          <div class="btn-wrap">
            <span class="btn confirm-btn" @click="goBindCard">绑定兑换卡</span>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>
<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
import Notice from "components/notice/notice";
import Coupon from "components/coupon/coupon";
import Protocol from "components/protocol/protocol";
// js
import { formatDate } from "common/js/common";
import { aliPay } from "common/js/pay";
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  data() {
    return {
      title: "确认订单",
      userId: "", // 用户id
      platform: 0, // 平台
      distributorId: "", // 分销商ID
      orderSource: "", // 订单来源
      message: "",
      userNo: "", // 用户编码
      phone: "", // 手机号
      goods: [],
      mGoods: [], // 购物车拆单商品
      goodItems: [],
      orderItems: [],
      orderIds: [],
      addArr: 0,
      // 收货地址信息
      addForm: {
        expressId: 0,
        distributionMoney: 0,
        mobile: "",
        phone: "",
        address: "",
        provinceId: 0,
        cityId: 0,
        districtId: 0,
        provinceName: "",
        cityName: "",
        districtName: "",
        userName: "",
        id: "",
      },
      payType: 2, // 2-微信支付（默认）, 1-支付宝
      express: "",
      exchange: "", // 兑换码编号或优惠券
      columns: [],
      salePrice: 0,
      saleValue: "",
      saleFlag: false,
      saleDiscount: 1,
      totalPrice: 0,
      protocol: false,
      showSale: false,
      showExpressage: false,
      showPay: false,
      isDef: false,
      isDisable: false, // 防止重复点击
      page: 1,
      size: 6,
      status: 3,
      totalPage: 0,
      unTotalPage: 0,
      couponList: [],
      // 优惠券tab
      couponTabs: [
        {
          id: 1,
          title: "待领取",
          value: "立即领取",
        },
        {
          id: 3,
          title: "未使用",
          value: "未使用",
        },
        {
          id: 4,
          title: "已使用",
          value: "已使用",
        },
        {
          id: 5,
          title: "已过期",
          value: "已过期",
        },
      ],
      couponItems: [],
      itemPrice: 0,
      distributionValue: "", // 配送方式
      // 微信配置信息
      weixin: {
        appid: "",
        timeStamp: "",
        nonceStr: "",
        prepayId: "",
        signType: "",
        paySign: "",
      },
      addrId: "", // 地址ID
      curAddrId: "", // 当前地址ID
      fadeProtocol: false, // 协议显示
      timeCount: 0, // 协议倒计时
      checked: true, // 是否勾选协议
      shopId: null, // 店铺ID
      code: "", // 优惠券编码
      price: "",
      couponMethod: "",
      showNotice: false, // 显示公告
      cardListShow: false, // 兑换卡列表弹窗
      cardList: [], // 兑换卡列表
      needNum: 0, // 需要兑换卡数量
      curPOrder: 0, // 当前父操作订单（匹配兑换卡)
      curOrder: 0, // 当前操作订单（匹配兑换卡）
      showMask: false, // 蒙版
      hasChoosedCode: [], // 已选中兑换码列表
      hasMailFee: false, // 是否收邮费（兑换）
      totalMailFee: 0, // 总运费
      isMiniProgram: false, // 是否是小程序
    };
  },
  filters: {
    formatDate(time) {
      if (time) {
        let date = new Date(time);
        return formatDate(date, "yyyy-MM-dd");
      } else {
        return "";
      }
    },
  },
  mounted() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "submitOrder") {
      this.isMiniProgram = true;
      sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
    }
    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.addrId = this.$route.query.addrId;
    this.userId = localStorage.getItem("userId");
    this.userNo = localStorage.getItem("userNo");
    this.phone =
      localStorage.getItem("phone") && localStorage.getItem("phone") !== "null"
        ? localStorage.getItem("phone")
        : "";

    // shopId
    this.shopId = localStorage.getItem("shopId");
    // 平台
    this.platform = parseInt(localStorage.getItem("platform"));
    // 分销商ID
    let disId = parseInt(localStorage.getItem("distributorId"));
    if (disId) {
      this.distributorId = disId;
    }
    this.orderSource = parseInt(localStorage.getItem("orderSource"));

    // 获取地址信息
    this.getAddress();

    // 获取订单信息
    this.getOrderInfo();
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
    // 协议弹框
    openProtocol() {
      this.fadeProtocol = true;
    },
    // 同意协议
    handleAgree() {
      this.fadeProtocol = false;
      this.checked = true;
      this.isDisable = false;
    },
    // 同意协议
    handleCheck(checked) {
      if (checked) {
        this.isDisable = false;
      } else {
        this.isDisable = true;
      }
    },
    // 根据商品列表获取最优优惠券
    getOptimalCoupon() {
      this.$api
        .post(this, api.getOptimalCoupon, {
          id: this.userId,
          orderItems: this.couponItems,
        })
        .then((res) => {
          if (res.status === 200) {
            if (res.data) {
              this.code = res.data.itemCodes;
              this.deliveryPrice = res.data.deliveryFee;
              // 兑换邮费
              this.exchangDelivery(res.data);
              this.getCoupontInfo(res.data);
              this.saleFlag = true;
            } else {
              // 无可用优惠券
              this.saleValue = "无可用优惠券";
              this.saleFlag = false;
            }
          } else {
            this.$toast(res.error);
          }
        });
    },
    exchangDelivery(data) {
      let flag = false;
      this.mGoods.forEach((item) => {
        let deliveryPrice = 0;
        let price = 0;
        let salePrice = 0;
        item.forEach((pro) => {
          // 物流费
          if (
            data &&
            data.isDeliveryFee &&
            data.isDeliveryFee === 1 &&
            this.code &&
            this.code.indexOf(pro.itemCode) > -1 &&
            !flag
          ) {
            flag = true;
            salePrice = pro.salePrice;
            deliveryPrice = this.deliveryPrice;
            this.addForm.distributionMoney = deliveryPrice;
          }
          price += pro.salePrice * pro.itemCount;
        });
        price = price + deliveryPrice - salePrice;
        this.$set(item, "deliveryPrice", deliveryPrice);
        this.$set(item, "orderPrice", price);
      });
    },
    // 计算价格
    calucatePrice() {
      let price =
        this.itemPrice * this.saleDiscount +
        this.addForm.distributionMoney -
        this.salePrice;
      this.totalPrice = price.toFixed(2) > 0 ? price.toFixed(2) : 0;
    },
    // 配送方式显示
    getDistributionValue(price) {
      let platform = localStorage.getItem("platform");
      if (
        platform === "7" ||
        platform === "6" ||
        platform === "1" ||
        platform === "28"
      ) {
        // 所有都包邮
        this.addForm.distributionMoney = 0;
        this.distributionValue = price === 0 ? "包邮" : "¥ " + price;
      } else {
        this.distributionValue = price > 0 ? "¥ " + price : "包邮";
      }
    },
    // 订单支付
    payMent(id) {
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

      if (parseInt(this.payType) === 2) {
        // 查看平台，判断微信支付方式
        let platform = localStorage.getItem("platform");
        // 查看是否店铺扫码进入/兑换
        if (platform === "1" || platform === "28" || this.shopId) {
          // 微信公众号平台
          let openid = localStorage.getItem("openId");
          if (openid) {
            let info = {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_jsapi", // 交易方式
              orderId: id,
              payerId: this.userId,
              platformUserId: openid,
              redirectUrl: redirectUrl,
            };

            if (Number(platform) === 28) {
              info = {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_mini_program", // 交易方式
                orderId: id,
                payerId: this.userId,
                platformUserId: openid,
                appId: appId,
              };
            }

            this.$api.post(this, api.handlePayment, info).then((res) => {
              if (res.success) {
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
                    orderPrice: this.totalMailFee,
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
              } else {
                this.$toast(res.errMessage);
                this.$router.replace("/orderList");
              }
            });
          } else {
            this.$toast("微信未授权");
          }
        } else {
          this.$api
            .post(this, api.handlePayment, {
              businessType: 1, // 业务类型：1 订单收款
              customerFlag: 1, // 客户标志：1 C端客户
              payMethod: "wxpay_h5", // 交易方式
              orderId: id,
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
        this.$api
          .post(this, api.handlePayment, {
            businessType: 1, // 业务类型：1 订单收款
            customerFlag: 1, // 客户标志：1 C端客户
            payMethod: "alipay_wap", // 交易方式
            orderId: id,
            payerId: this.userId,
            redirectUrl: redirectUrl,
          })
          .then((res) => {
            if (res.success) {
              let data = res.data.alipayResp.from;
              aliPay(data);
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
    // 获取地址信息
    getAddress() {
      if (this.userId) {
        this.$api
          .get(this, api.getAddrList, {
            id: this.userId,
            page: 1,
            size: 10,
          })
          .then((res) => {
            if (res.success) {
              if (res.data.list && res.data.list.length > 0) {
                this.addArr = res.data.total;
                let arr = res.data.list;
                if (this.addrId) {
                  arr.forEach((item) => {
                    if (this.addrId == item.id) {
                      this.setAddForm(item);
                      this.isDef = true;
                    } else {
                      if (!this.isDef && item.defaultFlag === 1) {
                        this.setAddForm(item);
                        this.isDef = true;
                      }
                      if (this.addArr > 0 && !this.isDef) {
                        arr.forEach((item, index) => {
                          if (index === 0) {
                            this.setAddForm(item);
                          }
                        });
                      }
                    }
                  });
                } else {
                  arr.forEach((item) => {
                    if (item.defaultFlag === 1) {
                      this.setAddForm(item);
                      this.isDef = true;
                    }
                  });
                  if (this.addArr > 0 && !this.isDef) {
                    arr.forEach((item, index) => {
                      if (index === 0) {
                        this.setAddForm(item);
                      }
                    });
                  }
                }
                // 获取配送方式
                this.getDistributionValue(this.addForm.distributionMoney);
              }
            } else if (res.status === 500) {
              this.$toast.fail(res.error);
            }
          });
      }
    },
    setAddForm(item) {
      this.addForm.userName = item.userName;
      this.addForm.mobile = item.phone;
      this.addForm.address = item.address;
      this.addForm.detail =
        item.provinceName + item.cityName + item.districtName;
      this.addForm.provinceId = item.provinceId;
      this.addForm.cityId = item.cityId;
      this.addForm.districtId = item.districtId;
      this.addForm.provinceName = item.provinceName;
      this.addForm.cityName = item.cityName;
      this.addForm.districtName = item.districtName;
      this.addForm.defaultFlag = item.defaultFlag;
      this.curAddrId = item.id;
    },
    // 获取订单信息
    getOrderInfo() {
      // 获取订单信息
      let goods = JSON.parse(localStorage.getItem("goods"));
      if (goods) {
        this.goods = goods.sort(this.dataUp);
        // 根据工厂分组
        this.mGoods = this.arrayGroupBy(this.goods, "manufactors");
        this.mGoods.forEach((item, index) => {
          // 分别计算每组的价格
          let price = 0;
          let manufactors = "";
          item.forEach((pro) => {
            price += pro.salePrice * pro.itemCount;
            manufactors = pro.diy.manufactors || "";
          });
          this.$set(item, "orderPrice", price);
          this.$set(item, "manufactors", manufactors);
          // 物流费
          this.$set(item, "deliveryPrice", 0);
          this.getDelivery(manufactors, price, item);
        });

        this.goods.forEach((item) => {
          this.$set(item, "itemMtoCount", 0); // 预售数量
          this.$set(item, "itemInCount", item.itemCount); // 在库数量（实际商品数量）
          this.$set(item, "itemOnWayCount", 0); // 在途数量
          this.$set(item, "mtoType", 0); // 是否预售：1 是，0 否
          this.$set(item, "oversoldFlag", 0); // 是否支持超卖：1 支持，0 不支持

          this.itemPrice += item.salePrice * item.itemCount;
          // 配送方式传参
          this.goodItems.push({
            itemCode: item.itemCode,
            itemCount: item.itemCount,
          });
          // 优惠券传参数组
          this.couponItems.push({
            price: item.salePrice,
            count: item.itemCount,
            modelId: item.diy.modelId,
            materialId: item.diy.materialId,
          });

          // 保存商品id数组
          this.orderIds.push(item.id);
          // 保存商品数组
          this.orderItems.push({
            modelId: item.diy.modelId,
            brandId: item.diy.brandId,
            materialId: item.diy.materialId,
            modelName: item.diy.modelName,
            brandName: item.diy.brandName,
            materialName: item.diy.materialName,
            pictureName: item.diy.pictureName,
            pictureId: item.diy.pictureId,
            previewImage: item.diy.previewImage,
            generateImage: item.diy.generateImage,
            itemName: item.itemName,
            itemCode: item.itemCode,
            salePrice: item.salePrice,
            itemCount: item.itemCount,
            manufactors: item.diy.manufactors,
          });

          this.getCardData(item);
        });

        // this.getOptimalCoupon();
        // 计算价格
        this.calucatePrice();
      }
    },
    // 地址管理
    handleAddress(id) {
      this.$router.push({
        path: "/address",
        query: {
          orderFlag: "order",
          addrId: id,
        },
      });
    },
    // 支付订单
    submitOrder() {
      // 收货地址信息
      let delivery = {
        address: this.addForm.address,
        cityId: this.addForm.cityId,
        cityName: this.addForm.cityName,
        countryId: 37,
        countryName: "中国",
        districtId: this.addForm.districtId,
        districtName: this.addForm.districtName,
        provinceId: this.addForm.provinceId,
        provinceName: this.addForm.provinceName,
        mobile: this.addForm.mobile,
        userName: this.addForm.userName,
      };
      // 物流费用(总费用)
      let distributionAmount = 0;
      // 商品结算金额(优惠后总金额)，兑换商品默认价格0
      let orderAmount = 0;
      // 配送方式列表
      let logisticss = [];
      this.mGoods.forEach((item) => {
        distributionAmount += item.deliveryPrice;

        logisticss.push(item.orderDelivery);
      });

      // 兑换码
      this.goods.forEach((item) => {
        let codes = item.secretCodes.split(", ");
        this.$set(item, "codes", codes);
      });

      let info = {
        delivery: delivery,
        distributionAmount: distributionAmount,
        orderAmount: orderAmount + distributionAmount,
        goodss: this.goods,
        logisticss: logisticss,
        payWay: this.payType, // 付款方式：1 支付宝，2 微信
        remark: this.message,
        invoiceFlag: 0, // 是否开具发票：1 是，0 否
      };

      this.$api
        .post(this, api.addOrder, info)
        .then((res) => {
          if (res.success) {
            // 订单编号
            let ids = res.data.ids.toString();
            let hasPrice = false;
            this.mGoods.forEach((item) => {
              // 金额大于0
              if (Number(item.deliveryPrice) > 0) {
                hasPrice = true;
              }
            });
            if (hasPrice) {
              // 大于0，调用支付
              this.payMent(ids);
            } else {
              // 跳转兑换成功
              wx.miniProgram.redirectTo({
                url: "/pages/order/result/result?enterFlag=orderList",
              });
            }

            // 删除购物车数据
            this.delShopData(this.orderIds);
          } else {
            this.isDisable = false;
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          console.log("error");
        });
    },
    getArrByStr(str) {
      var string = str.toUpperCase();
      var temp = string.split(/[\n\s+,，]/g);
      for (var i = 0; i < temp.length; i++) {
        if (temp[i] == "") {
          // 删除数组中空值
          temp.splice(i, 1);
          i--;
        }
      }
      return temp;
    },
    // 删除购物车数据
    delShopData(ids) {
      this.$api
        .delete(this, api.deleteShopcart, {
          ids: ids,
        })
        .then((res) => {
          if (res.success) {
            // 删除缓存数据
            localStorage.removeItem("goods");
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch(() => {});
    },
    // 选择配送方式
    openExpressage() {
      if (
        this.addForm.provinceId === 0 ||
        this.addForm.cityId === 0 ||
        this.addForm.districtId === 0
      ) {
        this.$toast("请选择收货地址");
      } else if (this.columns.length <= 0) {
        this.showExpressage = false;
      } else {
        this.showExpressage = true;
      }
    },
    // 选择优惠券
    handleItem(item) {
      this.code = item.itemCodes;
      this.deliveryPrice = item.deliveryFee;
      this.exchangDelivery(item);
      this.getCoupontInfo(item);
      this.showSale = false;
    },
    // 获取优惠券数据
    getCoupontInfo(item) {
      this.exchange = item.id;
      this.couponMethod = item.couponMethod;
      if (item.couponMethod === 1) {
        // 满减
        this.saleDiscount = 1;
        this.salePrice = Number(item.reduction);
        this.saleValue = "-¥" + this.salePrice;
        this.addForm.distributionMoney = 0;
      } else if (item.couponMethod === 2) {
        // 折扣
        this.salePrice = 0;
        this.saleDiscount = Number(item.discount) / 100;
        this.saleValue = this.saleDiscount * 10 + "折";
        this.addForm.distributionMoney = 0;
      } else {
        // 商品兑换
        this.saleDiscount = 1;
        if (item.isDeliveryFee === 1) {
          // 开启物流费
          // this.addForm.distributionMoney = 0
          let flag = false;
          this.goods.forEach((good) => {
            if (this.code && this.code.indexOf(good.itemCode) > -1 && !flag) {
              flag = true;
              this.salePrice = good.salePrice;
              this.saleValue = "-¥ " + good.salePrice;
            }
          });
        } else {
          // let randomPrice = this.orderItems[0].salePrice
          // this.salePrice = randomPrice
          // this.saleValue = '-¥' + this.salePrice
        }
      }
      this.calucatePrice();
    },
    // 打开商品优惠
    openSale() {
      this.getOrderCouponList(this.size, this.status);
      this.getOrderCouponCount();
      this.showSale = true;
    },
    // 根据商品列表获取优惠券列表
    getOrderCouponList(size, status) {
      this.$api
        .post(this, api.getGoodsCoupn, {
          page: 1,
          size: size,
          statuss: status,
          goodss: this.couponItems,
        })
        .then((res) => {
          if (res.success) {
            this.couponList = res.data.list;
            this.couponList.forEach((item, idx) => {
              if (item.couponExplainArr !== "") {
                item.couponExplainArr = item.couponExplain
                  .trim()
                  .split(/[\r|\n]/);
              }
            });
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 根据商品列表获取优惠券列表总数
    getOrderCouponCount() {
      this.$api
        .post(this, api.getGoodsCoupnCount, {
          statuss: this.status,
          goodss: this.couponItems,
        })
        .then((res) => {
          if (res.success) {
            this.totalPage = res.data;
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取更多优惠券
    getMore(status) {
      this.page++;
      let size = this.size * this.page;
      this.getOrderCouponList(size, status);
    },
    // 取消优惠券
    cancelCoupon() {
      this.salePrice = 0;
      this.saleDiscount = 1;
      this.addForm.distributionMoney = 0;
      this.deliveryPrice = 0;
      this.code = null;
      this.saleValue = "不使用优惠券";
      this.exchange = "";
      this.exchangDelivery(null);
      this.calucatePrice();
      this.showSale = false;
    },
    // 重新获取
    refresh(status) {
      this.getOrderCouponList(this.size, status);
      this.getOrderCouponCount();
    },
    // 选择配送方式
    onConfirm(value) {
      if (value) {
        this.addForm.expressId = value.id;
        this.express = value.text;
        this.addForm.distributionMoney = parseInt(value.price);
        this.getDistributionValue(this.addForm.distributionMoney);
      }
      this.calucatePrice();
      this.showExpressage = false;
    },
    // 切换tab
    handleTab(val) {
      this.status = val;
      this.getOrderCouponList(this.size, this.status);
      this.getOrderCouponCount();
    },
    // 分组函数
    groupBy(array, f) {
      let groups = {};
      array.forEach((o) => {
        var group = JSON.stringify(f(o));
        groups[group] = groups[group] || [];
        groups[group].push(o);
      });
      return Object.keys(groups).map((group) => {
        return groups[group];
      });
    },
    arrayGroupBy(list, groupId) {
      let sorted = this.groupBy(list, (item) => {
        return [item.diy[groupId]];
      });
      return sorted;
    },
    // 根据工厂获取配送方式
    getDelivery(manufactor, price, item) {
      this.$api
        .post(this, api.getCalculations, {
          distributorId: this.distributorId,
          billingMethod: 1, // 订单结算方式：1重量计费，2体积计费
          countryId: 37,
          provinceId: this.addForm.provinceId,
          cityId: this.addForm.cityId,
          useRange: 2, // 适用范围：1.普通商品，2.定制商品 3 普通商品和定制商品
          price: price,
          manufactors: manufactor,
          weight: 0,
        })
        .then((res) => {
          if (res.success && res.data && res.data.length > 0) {
            let logisticId = res.data[0].id;

            if (logisticId) {
              this.getLogisticDetail(logisticId, item);
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 查询单个配送
    getLogisticDetail(id, item) {
      this.$api
        .get(this, api.getLogistics, {
          id: id,
        })
        .then((res) => {
          if (res.success && res.data) {
            this.$set(item, "delivery", res.data);
            this.$set(item, "orderDelivery", {});
            this.$set(item.orderDelivery, "logisticsId", res.data.id);
            this.$set(item.orderDelivery, "logisticsName", res.data.name);
            this.$set(item.orderDelivery, "logisticsType", 2); // 配送方式类型：1 普通商品（标品） 2 定制工厂
            this.$set(item.orderDelivery, "manufactors", res.data.manufactors);
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 获取兑换卡
    getCardData(item) {
      let num = item.itemCount;
      this.$api
        .post(this, api.getCardData, {
          userId: this.userId,
          materialModels: [
            {
              materialId: item.diy.materialId,
              modelId: item.diy.modelId,
              pictureId: item.diy.pictureId,
              num: num,
            },
          ],
        })
        .then((res) => {
          if (res.success) {
            // 获取对应兑换卡列表
            if (
              res.data.exchangeCodeBeans &&
              res.data.exchangeCodeBeans.length > 0
            ) {
              let codeData = res.data.exchangeCodeBeans;
              codeData = codeData.sort(this.dataUp);
              this.$set(item, "cardList", codeData);

              if (this.hasChoosedCode && this.hasChoosedCode.length > 0) {
                codeData.forEach((code) => {
                  this.$set(code, "hasNSelect", 0);
                  if (code.canSelect && !code.hasNSelect) {
                    // 判断已选中兑换码列表是否与当前码相同
                    this.hasChoosedCode.forEach((choosed) => {
                      if (choosed === code.secretCode) {
                        this.$set(code, "hasNSelect", 1);
                      }
                    });
                  }
                });
              }

              // 判断当前商品需要兑换卡数量
              let selects = [];
              codeData.forEach((item) => {
                if (item.canSelect && !item.hasNSelect) {
                  if (selects.length < num) {
                    this.$set(item, "hasNSelect", 1);
                    selects.push(item);

                    this.hasChoosedCode = [
                      ...new Set(this.hasChoosedCode.concat(item.secretCode)),
                    ];
                  }
                }
              });

              // 显示兑换卡码
              if (selects) {
                let secretCodes = "";
                // 计算邮费
                let mailFee = 0;
                for (let i = 0; i < selects.length; i++) {
                  if (i === 0) {
                    secretCodes += selects[i].secretCode;
                  } else {
                    secretCodes += ", " + selects[i].secretCode;
                  }
                  if (selects[i].mailSetting === 2) {
                    mailFee += selects[i].mailFee;
                  }
                }
                this.$set(item, "secretCodes", secretCodes);
                this.$set(item, "mailFee", mailFee);
              }
              // 设置已选数量
              this.$set(item, "selects", selects);

              // 获取当前所需兑换卡
              let curNeedNum = num - selects.length;
              this.$set(item, "curNeedNum", selects.length ? curNeedNum : 0);
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 排序（根据材质id）
    dataUpMaterial(x, y) {
      return Number(y.diy.materialId) - Number(x.diy.materialId);
    },
    // 排序（根据是否可用）
    dataUp(x, y) {
      return Number(y.canSelect) - Number(x.canSelect);
    },
    // 兑换卡弹窗
    handleCard(item, pIndex, index, secretCodes) {
      // 将当前所选兑换码转数组
      let secretList = this.getArrByStr(secretCodes);
      this.curPOrder = pIndex;
      this.curOrder = index;

      let cardList = item.cardList;
      if (secretList && secretList.length > 0) {
        if (cardList && cardList.length > 0) {
          cardList.forEach((code) => {
            this.$set(code, "hasNSelect", 0);
            // 将所有已选中兑换码列表设为不可选
            this.hasChoosedCode.forEach((choosed) => {
              if (code.secretCode === choosed) {
                this.$set(code, "canSelect", 0);
                this.$set(code, "hasNSelect", 1);
              }
            });

            // 将当前匹配兑换码列表设为可选
            secretList.forEach((secret) => {
              if (code.secretCode === secret) {
                this.$set(code, "canSelect", 1);
              }
            });
          });

          this.cardList = cardList;
        }
        this.cardListShow = true;
        this.showMask = true;
      } else {
        this.goBindCard();
      }
      this.needNum = item.itemCount;
    },
    // 选择兑换卡
    handleSelect(code) {
      let secretCodes = "";
      let mainFee = 0;
      this.cardList.forEach((item) => {
        if (this.needNum === 1) {
          // 一张兑换卡，直接切换选择
          if (item.id === code.id && code.canSelect) {
            this.$set(item, "hasNSelect", 1);
          } else {
            this.$set(item, "hasNSelect", 0);
          }
        } else {
          // 多张兑换卡
          if (item.id === code.id && code.canSelect) {
            this.$set(
              item,
              "hasNSelect",
              Number(item.hasNSelect) === 1 ? 0 : 1
            );
          }
        }

        // 当前可选
        if (item.canSelect) {
          this.hasChoosedCode.forEach((choosed, index) => {
            // 选择的元素与已选兑换码列表匹配，移除
            if (item.secretCode === choosed) {
              this.hasChoosedCode.splice(index, 1);
            }
          });
        }

        if (Number(item.hasNSelect) === 1 && item.canSelect) {
          secretCodes += ", " + item.secretCode;
          if (item.mailSetting === 2) {
            mainFee += item.mailFee;
          }
          this.hasChoosedCode = [
            ...new Set(
              this.hasChoosedCode.concat(this.getArrByStr(secretCodes))
            ),
          ];
        }
      });

      // 去除前面,空格
      this.mGoods[this.curPOrder][this.curOrder].secretCodes = secretCodes
        .replace(/^, +/, "")
        .replace(/, +$/, "");

      // 计算运费
      this.mGoods[this.curPOrder][this.curOrder].mailFee = mainFee;
      let totalMailFee = 0; // 总运费
      this.mGoods.forEach((item, index) => {
        // 计算运费
        let mailFee = 0;
        let hasCode = false; // 是否有兑换码
        item.forEach((pro) => {
          if (pro.secretCodes) {
            hasCode = true;
          }
          // 如果当前定制商品有运费，显示提交订单
          if (pro.mailFee) {
            this.hasMailFee = true;
            mailFee += pro.mailFee;
          }
        });

        totalMailFee += mailFee; // 总运费
        // 运费
        this.$set(item, "deliveryPrice", mailFee);
        this.$set(item, "hasCode", hasCode);
      });
      this.totalMailFee = totalMailFee; // 总运费
    },
    closeDialog() {
      if (this.choosedNum > 0) {
        if (this.needNum !== this.choosedNum) {
          this.$toast("兑换码数量与商品数量不匹配");
        }
        this.cardListShow = false;
        this.showMask = false;
      } else {
        if (this.cardList.length <= 0) {
          this.cardListShow = false;
          this.showMask = false;
        } else {
          this.$toast("请选择兑换卡");
        }
      }
    },
    // 关闭蒙版
    closeMask() {
      this.cardListShow = false;
      this.showMask = false;
    },
    // 绑定兑换卡
    goBindCard() {
      wx.miniProgram.navigateTo({
        url:
          "/pages/code/bindCode/bindCode?enterFlag=submitOrder&userId=" +
          this.userId,
      });
    },
    // 返回购物车
    goBack() {
      this.$router.push("/shopcart");
    },
    // 查询该地址是否停发
    getDeliveryStopStatus() {
      if (this.addArr <= 0) {
        this.$toast("请选择收货地址");
        return;
      }

      this.isDisable = true;
      // 兑换码
      for (let i = 0; i < this.goods.length; i++) {
        if (
          this.goods[i].secretCodes === "" ||
          this.getArrByStr(this.goods[i].secretCodes).length !=
            this.goods[i].itemCount
        ) {
          this.isDisable = false;
          this.$toast("兑换码数量与商品数量不匹配");
          return;
        }
        for (let j = 0; j < this.orderItems.length; j++) {
          if (this.goods[i].id === this.orderItems[j].mid) {
            this.orderItems[j].secretCodeList = this.getArrByStr(
              this.goods[i].secretCodes
            );
          }
        }
      }

      let content = this.addForm.detail + this.addForm.address;
      this.$api
        .get(this, api.findMatchDeliveryStop, {
          content: content,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              // 按钮颜色
              let buttonColor = "#15BED6";
              if (Number(this.distributorId) === 4378) {
                // 荣耀
                buttonColor = "#256FFF";
              }
              // 匹配，弹窗提示用户该地址停发
              this.$dialog
                .confirm({
                  title: "温馨提示",
                  message:
                    "尊敬的用户你好，该地区可能是疫情停发区，发货可能会有影响，可正常提交订单待疫情解封可恢复发货，不便之处敬请谅解。",
                  className: "confirm-v-dialog tl",
                  confirmButtonText: "确认提交",
                  confirmButtonColor: buttonColor,
                  cancelButtonText: "我再想想",
                })
                .then(() => {
                  // 支付订单
                  this.submitOrder();
                })
                .catch((error) => {
                  this.isDisable = false;
                });
            } else {
              // 不匹配，直接下单
              // 支付订单
              this.submitOrder();
            }
          } else {
            this.$toast.fail(res.errMessage);
            this.isDisable = false;
          }
        });
    },
  },
  watch: {
    mGoods(arr) {
      let totalMailFee = 0; // 总运费
      arr.forEach((item, index) => {
        // 分别计算每组的价格
        let price = 0;
        // 计算运费
        let mailFee = 0;
        let hasCode = false; // 是否有兑换码
        item.forEach((pro) => {
          if (pro.secretCodes) {
            hasCode = true;
          }
          price += pro.salePrice * pro.itemCount;

          // 如果当前定制商品有运费，显示提交订单
          if (pro.mailFee) {
            this.hasMailFee = true;
            mailFee += pro.mailFee;
          }
        });

        totalMailFee += mailFee; // 总运费

        this.$set(item, "orderPrice", price);
        // 运费
        this.$set(item, "deliveryPrice", mailFee ? mailFee : 0);
        this.$set(item, "hasCode", hasCode);
      });
      this.totalMailFee = totalMailFee; // 总运费

      return arr;
    },
  },
  computed: {
    // 已选兑换卡数量
    choosedNum() {
      let count = 0;
      this.cardList.forEach((item) => {
        // 获取已选数量
        if (Number(item.hasNSelect) === 1 && item.canSelect) {
          count++;
        }
      });
      return count;
    },
    // 共使用兑换卡数量
    usedNum() {
      // 获取兑换码
      let secretCodes = "";
      this.goods.forEach((item) => {
        secretCodes += item.secretCodes + ", ";
      });
      // 获取兑换码个数
      let length = this.getArrByStr(secretCodes).length;
      return length;
    },
  },
  components: {
    THeader,
    Notice,
    Coupon,
    Protocol,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.index {
  position: relative;
  width: 100%;
  top: 44px;
  bottom: 65px;
  background-color: $color-bg;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .container {
    position: relative;
    height: 100%;
    overflow-y: scroll;

    &::-webkit-scrollbar {
      display: none;
    }

    .order-wrap {
      margin-bottom: $spacing-sm;
      overflow: hidden;

      .item-box {
        margin-bottom: 0;
        border-radius: 0;

        &.double {
          margin-top: $spacing-sm;
        }
      }
    }

    .notice-box {
      padding: $spacing-sm $spacing-base;
      background-color: rgbaMain(0.2);
    }

    .address-wrap {
      position: relative;

      .van-icon {
        position: absolute;
        top: 50%;
        right: $spacing-sm;
        transform: translateY(-50%);
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
        margin-top: 12px;
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

      .address-detail {
        padding: 7px $spacing-sm 4px 35px;

        .tag {
          font-size: $font-sm;
          margin-right: 5px;
          padding: 4px 7px;
          background: rgbaMain(0.2);
          border-radius: $radius-xs;
        }

        .sprite-icon {
          position: absolute;
          top: 24px;
          left: $spacing-base;
        }
      }

      .no-address {
        padding: $spacing-sm 0 $spacing-sm 42px;
        font-size: $font-lg;
        text-align: left;

        .sprite-icon {
          position: absolute;
          top: 50%;
          left: $spacing-base;
          transform: translateY(-50%);
        }
      }
    }

    .item-box {
      display: inline-block;
      width: 100%;
      margin-bottom: $spacing-sm;
      padding: $spacing-lg $spacing-base;
      color: $color-font-base;
      background-color: $color-bg-white;

      &.double {
        .tr-box {
          lineHeight(35px);
        }
      }

      &.pay {
        padding: 16px 22px 12px;

        .tr-box {
          height: 50px;
        }
      }

      .tr-header {
        display: block;
        padding-bottom: 12px;
        padding-left: 6px;
        margin-bottom: 10px;
        font-size: $font-size-medium-x;
        font-weight: 500;
        color: #4A4A4A;
        border-bottom: 1px solid #EBEBEB;
      }

      .order-info {
        display: flex;
        padding-left: 8px;

        .pro-img {
          position: relative;
          left: 0;
          display: inline-block;
          width: 91px;
          height: 168px;
        }

        .img {
          position: absolute;
          display: inline-block;
          top: 50%;
          left: 50%;
          max-width: 100%;
          max-height: 100%;
          transform: translate(-50%, -50%);
        }

        .order-detail {
          position: relative;
          flex: 1;
          padding-left: $spacing-lg;

          .tbox {
            display: flex;
            display: -webkit-flex;
            align-items: center;
            padding-top: 16px;

            .name {
              display: -webkit-box;
              flex: 1;
              text-align: left;
              font-size: $font-lg;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .price {
              display: inline-block;
              margin-left: $spacing-base;
              font-size: $font-sm;

              em {
                display: inline-block;
                margin-left: 5px;
                font-size: $font-base;
                font-style: normal;

                &.line {
                  text-decoration: line-through;
                }
              }
            }
          }

          .text {
            padding-top: 14px;
            color: $color-font-grey;

            .model {
              .pro-spe {
                position: relative;
                margin-top: $spacing-sm;
                padding-left: 3.5em;
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

      .tr-box {
        display: flex;
        align-items: center;
        font-size: $font-base;

        .name {
          display: inline-block;
          color: $color-font-base;
        }

        .left {
          display: inline-block;
          color: $color-font-base;

          span {
            margin-left: $spacing-base;
            color: $color-font-darkGrey;
          }
        }

        .text {
          flex: 1;
          margin-left: 14px;
          color: $color-font-grey;
        }

        .price {
          display: inline-block;
          align(right);
          lineHeight(36px);

          .desc {
            font-size: $font-base;
          }

          &.right {
            flex: 1;
          }

          &.sale {
            color: $color-orange;

            em {
              display: inline-block;
              font-size: $font-lg;
              font-style: normal;
            }
          }

          &.nosale {
            color: $color-font-darkGrey;
          }
        }

        .input {
          flex: 1;
          padding-left: 14px;

          &.van-cell {
            padding: 6px 0;
          }

          >>>.van-field__body {
            padding: 0 10px;
          }
        }

        .van-icon {
          display: inline-block;
        }

        .pname {
          display: inline-block;
          width: 80%;
          text-align: left;
          font-size: $font-xm;
          color: $color-font-base;
        }

        .radio {
          check-style();
        }
      }
    }

    .code-wrap {
      position: relative;
      margin-top: 27px;
      line-height: 16px;

      .title {
        position: absolute;
        top: 0;
        left: 0;
        font-size: $font-base;
        color: $color-font-base;
      }

      .secret-wrap {
        padding: 0 $spacing-base 0 60px;
        align(right);
      }

      .need-secret {
        position: relative;
        color: $color-orange;

        .van-icon {
          color: $color-orange !important;
        }
      }

      .van-icon {
        position: absolute;
        top: 2px;
        right: 0;
        font-size: $font-sm;
        color: $color-orange;
      }

      .no-secret {
        color: $color-orange;
      }

      .secret {
        font-size: $font-base;
      }

      &.hasCode {
        .van-icon {
          color: $color-font-base;
        }
      }
    }

    .protocol-box {
      padding: 16px;
      margin-bottom: 65px;
      text-align: center;
      font-size: $font-sm;
      color: $color-font-grey;

      .checkbox {
        position: relative;
        top: -1px;
        display: inline-block;

        .van-checkbox {
          position: absolute;
          top: -10px;
        }
      }

      .text {
        display: inline-block;
        margin-left: 22px;
        vertical-align: middle;

        .point {
          color: $color-orange;
        }
      }
    }
  }

  .btn-box {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: $spacing-sm $spacing-base;
    text-align: right;
    background-color: $color-bg-white;
    box-shadow: $color-bt-shadow;
    z-index: 99;

    .total {
      display: inline-block;
      margin-right: $spacing-lg;
      width: calc(100% - 190px);
      font-size: $font-sm;
      color: $color-font-grey;
      align(center);

      .price {
        color: $color-orange;

        em {
          font-style: normal;
        }
      }

      .num {
        padding: 0 3px;
        color: $color-orange;
      }
    }

    .btn {
      display: inline-block;
      width: 165px;
      lineHeight(45px);
      align(center);
      font-size: $font-lg;
      background: $color-main;
      border-radius: 45px;

      &.readonly {
        pointer-events: none;
        color: $color-font-grey;
        background: rgbaMain(0.3);
      }
    }
  }

  .mask {
    mask();
    z-index: 100;
  }

  .card-dialog {
    mask();
    height: 400px;
    top: auto;
    bottom: 0;
    background-color: transparent;
    z-index: 101;

    .card-wrap {
      position: absolute;
      bottom: 0;
      left: 0;
      width: 100%;
      height: 400px;
      background-color: $color-bg-white;
      border-radius: $radius-base $radius-base 0 0;
      overflow: hidden;
    }

    .card-list-wrap {
      position: absolute;
      bottom: 0;
      left: 0;
      height: 500px;
      padding: 0 $spacing-base;

      .title {
        padding: 24px 13px 20px 17px;
        line-height: 25px;

        .text {
          display: inline-block;
          font-size: $font-lg;
          color: $color-font-base;
          font-weight: 500;

          span {
            padding-left: $spacing-sm;
            font-size: $font-sm;
            color: $color-font-grey;
          }
        }

        .close {
          display: inline-block;
          font-size: $font-base;
          float: right;
        }
      }

      .card-list {
        position: absolute;
        top: 69px;
        right: $spacing-lg;
        bottom: 65px;
        left: $spacing-lg;
        padding: $spacing-lg 0;
        overflow-y: scroll;

        &::-webkit-scrollbar {
          display: none;
        }

        li {
          position: relative;
          padding-right: 40px;

          .card-t {
            border-radius: $radius-xm $radius-xm 0 0;
            overflow: hidden;

            img {
              width: 100%;
            }
          }

          .mail-fee {
            padding-top: 9px;
            align(center);
            background-color: rgba(183, 183, 183, 0.1);

            p {
              font-size: $font-sm;
              color: $color-font-grey;
              line-height: 15px;
              transform: scale(0.95);
            }

            & + .card-b {
              padding: 6px $spacing-base 12px;
            }
          }

          .card-b {
            display: flex;
            justify-content: space-between;
            padding: 12px $spacing-base;
            font-size: $font-sm;
            line-height: 16px;
            background-color: rgba(183, 183, 183, 0.1);
            border-radius: 0 0 $radius-xm $radius-xm;
          }

          & + li {
            margin-top: $spacing-base;
          }
        }

        .checkbox {
          position: absolute;
          right: 0;
          top: 50%;
          transform: translateY(-50%);

          &:checked {
            position: absolute;
          }

          &.isStock {
            check-style();
            background-color: $color-bg;
            border: 1px solid $color-bg;
          }
        }
      }

      .btn-wrap {
        position: absolute;
        bottom: 0;
        left: 0;
        width: 100%;
        padding: $spacing-sm 30px;
        display: flex;

        .btn {
          flex: 1;
          font-size: $font-lg;
          align(center);
          lineHeight(45px);
          border-radius: 45px;
        }
      }
    }
  }

  .salePop {
    display: flex;
    flex-flow: column;
    padding: 20px 0 0;
    box-sizing: border-box;

    .pop-header {
      display: block;
      height: 20px;
      line-height: 20px;
      border-left: 2px solid #15BED6;

      .title {
        display: inline-block;
        font-size: $font-size-medium;
        margin-left: 22px;
        color: $color-text-d;
      }
    }

    .pop-info {
      flex: 1;
      margin-top: 12px;
      overflow: hidden;

      >>>.van-tabs {
        display: flex;
        display: -webkit-flex;
        height: 100%;
        flex-flow: column;
        overflow: hidden;

        .van-tabs__wrap {
          display: block;
          height: 46px;
          line-height: 46px;

          &.van-hairline--top-bottom {
            &::after {
              border-width: 0;
              border: none;
            }
          }

          .van-tabs__line {
            width: 30px !important;
            background-color: $color-theme;
          }

          .van-tab {
            .van-ellipsis {
              font-size: $font-size-medium;
              font-weight: bold;
              color: $color-text-d;
            }
          }
        }

        .van-tabs__content {
          flex: 1;
          padding: 10px 15px 0;
          box-sizing: border-box;
          overflow: hidden;

          .van-tab__pane {
            width: 100%;
            height: 100%;
            padding-bottom: 40px;
            overflow-y: scroll;

            &::-webkit-scrollbar {
              display: none;
            }
          }
        }
      }
    }
  }

  >>>.van-picker {
    .van-picker__cancel, .van-picker__confirm {
      color: $color-theme;
    }
  }

  .protocol-wrapper {
    position: fixed;
    top: 0;
    bottom: 0;
    left: 0;
    right: 0;
    text-align: center;
    background-color: $color-bg-white;
    transform: translate3d(100%, 0, 0);
    animation: all 10s linear 1;
    z-index: 100;

    &.show {
      transform: translate3d(0, 0, 0);
    }
  }
}

.fadeDialog-enter-active, .fadeDialog-leave-active {
  transition: all 0.3s;
}

.fadeDialog-enter, .fadeDialog-leave-to {
  transform: translate3d(0, 100%, 0);
}

.van-checkbox {
  color: $color-font-grey;

  >>>.van-checkbox__icon {
    font-size: $font-lg;

    &.van-checkbox__icon--checked {
      .van-icon {
        color: $color-font-base;
        background-color: transparent;
        border-color: $color-font-base;
      }
    }

    .van-icon {
      width: 16px;
      height: 16px;
    }
  }
}
</style>

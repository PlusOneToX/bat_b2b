<template>
  <div class="index" :class="{ 'ry-style': Number(distributorId) === 4378 }">
    <v-header :back="true" :title="title" @back="toBack"></v-header>
    <div class="container">
      <!--公告-->
      <div class="notice-box" v-if="showNotice">
        <Notice></Notice>
      </div>
      <div class="item-box" @click="handleAddress(curAddrId)">
        <div class="address-wrap" v-show="addArr > 0">
          <i class="iconfont icon icon-c-location"></i>
          <div class="top">
            <span class="name">{{ addForm.userName }}</span>
            <span class="tel">{{ addForm.mobile }}</span>
          </div>
          <div class="bottom address">
            {{ addForm.wholeAddr }}
          </div>
          <i class="icon icon-right"></i>
        </div>
        <div class="no-address" v-show="addArr <= 0">
          <van-icon name="plus" color="#979797" />
          <span class="text">您还没有收货地址，请点击添加</span>
        </div>
      </div>
      <div class="order-wrap" v-for="goods in mGoods" :key="goods">
        <div class="item-box" v-for="(good, index) in goods" :key="index">
          <div class="order-info">
            <div class="pro-img">
              <img :src="good.diy.previewImage" alt="" class="img" />
            </div>
            <div class="order-detail">
              <div class="tbox">
                <span class="name">{{ good.itemName }}</span>
                <span class="price" v-show="showPrice"
                  >¥{{ good.salePrice }}</span
                >
              </div>
              <div class="text">
                <div class="model">
                  <div>
                    规格：{{ good.diy.modelName }} - {{ good.diy.materialName }}
                  </div>
                  <!--金科-->
                  <div v-show="distributorId === 1217">
                    图片ID：{{ good.diy.pictureId }}
                  </div>
                </div>
                <span class="count">X{{ good.itemCount }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="item-box double" v-show="showPrice">
          <div class="tr-box" @click="openExpressage">
            <div class="left">配送方式</div>
            <div class="text">{{ express }}</div>
            <!-- <div class="price" v-if="">{{distributionValue}}</div> -->
            <!-- <div class="price">{{distributionValue}}</div> -->
            <div class="price" :class="goods.deliveryPrice > 0 ? 'sale' : ''">
              <span class="desc" v-show="goods.deliveryPrice > 0">物流费</span>
              {{
                goods.deliveryPrice === 0 ? "包邮" : "¥ " + goods.deliveryPrice
              }}
            </div>
            <i class="icon icon-right" v-show="goods.deliveryPrice"></i>
          </div>
          <div class="tr-box" v-show="showPrice">
            <div class="left">费用总计</div>
            <div class="price sale right">
              ¥<em>{{ goods.orderPrice }}</em>
            </div>
          </div>
        </div>
      </div>
      <div class="item-box double">
        <div class="tr-box" @click="openSale" v-if="showPrice">
          <div class="left">商品优惠</div>
          <div class="text"></div>
          <div
            class="price"
            :class="saleFlag ? 'sale' : 'nosale'"
            v-show="salePrice > 0 || saleDiscount > 0"
          >
            {{ saleValue }}
          </div>
          <i class="icon icon-right"></i>
        </div>

        <div class="tr-box" @click="openExpressage" v-else>
          <div class="left">配送方式</div>
          <div class="text">{{ express }}</div>
          <div class="price">包邮</div>
        </div>
        <div class="tr-box">
          <div class="left">买家留言</div>
          <van-field
            class="input"
            v-model="message"
            placeholder="您还有什么要求请在这里说"
            clearable
          />
        </div>
      </div>
      <div class="item-box pay" v-show="showPrice">
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
      </div>
      <!-- 2502 不显示注册协议 -->
      <div
        class="protocol-box"
        v-show="showPrice && Number(distributorId) !== 2502"
      >
        <input
          type="checkbox"
          class="checkbox"
          checked
          @click="handleCheck($event)"
        />
        <div class="text">
          点击表示同意<span class="point" @click="openProtocol"
            >《用户协议》</span
          >
        </div>
      </div>
    </div>
    <div class="btn-box" v-if="showPrice">
      <div class="total">
        合计：<span class="price"
          >¥<em>{{ totalPrice }}</em></span
        >
      </div>
      <div
        class="btn btn-pay"
        :class="isDisable ? 'readonly' : ''"
        @click="getDeliveryStopStatus(1)"
      >
        支付订单
      </div>
    </div>
    <div class="btn-box no-price" v-else>
      <div
        class="btn btn-pay"
        :class="isDisable ? 'readonly' : ''"
        @click="getDeliveryStopStatus(2)"
      >
        完成兑换
      </div>
    </div>
    <!--配送方式-->
    <van-popup v-model="showExpressage" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showExpressage = false"
        @confirm="onConfirm"
      />
    </van-popup>
    <!--商品优惠-->
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
        <van-tabs v-model="status" swipeable @change="handleTab">
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
    <!--协议-->
    <div class="protocol-wrapper" :class="fadeProtocol ? 'show' : ''">
      <!-- 家有点点协议 -->
      <ProtocolJYDD
        v-if="Number(distributorId) === 3850"
        :timeCount="0"
        :back="true"
        @close="fadeProtocol = false"
      ></ProtocolJYDD>
      <!-- 其他 -->
      <Protocol
        v-else
        :timeCount="0"
        :back="true"
        @close="fadeProtocol = false"
      ></Protocol>
    </div>
	
	<!-- 柔性关闭弹窗 -->
	<div class="flexible-dialog" v-show="showFlexible">
	  <div class="flexible-dialog-content">
	      <div class="flexible-cotent-top">
	      	<img class="flexible-cotent-top-img" src="../../assets/images/flexible-logo.png" mode="widthFix"/>
	      </div>
		  
		  <div class="flexible-cotent-middle-title">
		  	 通知提醒
		  </div>
		  <div class="flexible-cotent-middle-info">
		  	 尊敬的客户，本商城将于6月30日停止运营，仅保留订单查询服务。感谢您的信任和支持。
		  </div>
		  <div class="flexible-cotent-bottom" @click="clickFlexible">
		  	 知道了
		  </div>
	  </div>
	</div>
	
  </div>
</template>
<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import Coupon from "base/coupon/coupon";
import Protocol from "components/protocol/protocol";
import ProtocolJYDD from "components/protocol/protocol_jydd";
import Notice from "base/notice/notice";
import api from "common/js/allApi.js";
import { aliPay } from "common/js/pay";
import { getLocalStorageItem } from "common/js/common";

import axios from "axios";
export default {
  data() {
    return {
      title: "确认订单",
      userId: "",
      platform: 0,
      distributorId: "", // 分销商ID
      message: "",
      redeemCode: "",
      userNo: "",
      phone: "",
      goods: [],
      mGoods: [], // 购物车拆单商品
      goodItems: [],
      orderItems: [],
      orderIds: [],
      addArr: 0,
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
      itemPrice: 0, // 货品总价格
      disItemPrice: 0, // 已优惠货品价格
      distributionValue: "", // 配送方式
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
      shopId: null, // 店铺ID
      shopCode: "", // 店铺编码
      shopName: "", // 店铺名称
      code: "", // 优惠券编码
      price: "",
      couponMethod: "",
      showPrice: true, // 是否显示价格
      goodsEnable: "", // 商品是否能用：1 能用，0 不能用
      amountEnable: "", // 金额是否到达要求：1 达到，0 未达到
	  
	  showFlexible: false //是否显示柔性关闭弹窗
    };
  },
  mounted() {
    this.addrId = this.$route.query.addrId;
    this.userId = getLocalStorageItem("userId");
    this.userNo = getLocalStorageItem("userNo");
    this.phone =
      getLocalStorageItem("phone") && getLocalStorageItem("phone") !== "null"
        ? getLocalStorageItem("phone")
        : "";
    // shopId/shopCode/shopName
    this.shopId = sessionStorage.getItem("shopId");
    this.shopCode = sessionStorage.getItem("shopCode") || "";
    this.shopName = sessionStorage.getItem("shopName") || "";
    // 平台
    this.platform = parseInt(localStorage.getItem("platform"));
    // 分销商ID
    let disId = parseInt(localStorage.getItem("distributorId"));
    if (disId) {
      this.distributorId = disId;
    }

    if (Number(this.distributorId) === 4378) {
      // 荣耀
      this.showPrice = false; // 是否显示价格
    } else {
      this.showPrice = true; // 是否显示价格
    }

    // 获取地址信息
    this.getAddress();

    // 获取订单信息
    this.getOrderInfo();
  },
  methods: {
    // 排序（根据材质id）
    dataUp(x, y) {
      return Number(y.salePrice) - Number(x.salePrice);
    },
    // 协议弹框
    openProtocol() {
      this.fadeProtocol = true;
    },
    // 同意协议
    handleCheck(event) {
      if (event.currentTarget.checked) {
        this.isDisable = false;
      } else {
        this.isDisable = true;
      }
    },
    // 根据商品列表获取最优优惠券
    getOptimalCoupon() {
      this.$api
        .post(this, api.getOptimalCoupon, {
          goodss: this.couponItems,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              this.goodsEnable = 1;
              this.amountEnable = 1;
              this.deliveryPrice = res.data.deliveryFee;
              // 兑换邮费
              this.exchangDelivery(res.data);
              this.getCoupontInfo(res.data);
              this.saleFlag = true;
            } else {
              this.goods.forEach((good) => {
                this.itemPrice += good.salePrice * good.itemCount;
              });
              // 计算价格
              this.calucatePrice();

              // 无可用优惠券
              this.saleValue = "无可用优惠券";
              this.saleFlag = false;
            }
          } else {
            this.$toast.fail(res.errMessage);
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
            data.deliveryFeeFlag &&
            data.deliveryFeeFlag === 1 &&
            this.goodsEnable &&
            this.amountEnable &&
            !flag
          ) {
            flag = true;
            salePrice = pro.salePrice;
            deliveryPrice = this.deliveryPrice;
            this.addForm.distributionMoney = deliveryPrice;
          }
          price += pro.salePrice * pro.itemCount;
        });
        price = price + deliveryPrice;

        this.deliveryPrice = deliveryPrice;
        this.addForm.distributionMoney = deliveryPrice;
        this.$set(item, "deliveryPrice", deliveryPrice);
        this.$set(item, "orderPrice", price);
      });
    },
    // 计算价格
    calucatePrice() {
      let price =
        this.itemPrice -
        this.disItemPrice +
        this.addForm.distributionMoney -
        this.salePrice;

      this.totalPrice = price.toFixed(2) > 0 ? price.toFixed(2) : 0;
    },
    // 配送方式显示
    getDistributionValue(price) {
      let platform = localStorage.getItem("platform");
      if (platform === "7" || platform === "6" || platform === "1") {
        // 所有都包邮
        this.addForm.distributionMoney = price;
        this.distributionValue = price === 0 ? "包邮" : "¥ " + price;
      } else {
        this.distributionValue = price > 0 ? "¥ " + price : "包邮";
      }
    },
    // 订单支付
    payMent(id) {
	  let redirectUrl = "https://test.bat.com/diyh5/orderList";
	  if (process.env.NODE_ENV === "production") {
	  	  redirectUrl = "https://diy.bat.com/orderList";
	  }
		
      if (parseInt(this.payType) === 2) {
        // 查看平台，判断微信支付方式
        let platform = localStorage.getItem("platform");
        // 查看是否店铺扫码进入
        if (platform === "1" || this.shopId) {
          // 微信公众号平台
          let openid = localStorage.getItem("openId");
          let appId = localStorage.getItem("appid");
          if (openid) {
            this.$api
              .post(this, api.handlePayment, {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_jsapi", // 交易方式
                orderId: id,
                payerId: this.userId,
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
                  this.$toast.fail(res.errMessage);
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
                this.$router.push("/orderList");
              }
            });
        }
      } else {
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
      this.addForm.wholeAddr =
        item.provinceName + item.cityName + item.districtName + item.address;
      this.addForm.provinceId = item.provinceId;
      this.addForm.cityId = item.cityId;
      this.addForm.districtId = item.districtId;
      this.addForm.provinceName = item.provinceName;
      this.addForm.cityName = item.cityName;
      this.addForm.districtName = item.districtName;
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
        });
        // 根据商品列表获取最优优惠券（荣耀不获取）
        if (Number(this.distributorId) !== 4378) {
          this.getOptimalCoupon();
        }
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
    testApp(url, openUrl) {
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
    // 支付订单
    submitOrder() {
      this.isDisable = true;
      setTimeout(() => {
        this.isDisable = false;
      }, 5000);

      let platform = localStorage.getItem("platform");
      let samShopCode = sessionStorage.getItem("samShopCode")
        ? sessionStorage.getItem("samShopCode")
        : "";
      let samShopName = sessionStorage.getItem("samShopName")
        ? sessionStorage.getItem("samShopName")
        : "";
      let shopCode = "";
      let shopName = "";
      if (Number(platform) === 7 && samShopCode) {
        shopCode = samShopCode;
        shopName = samShopName;
      } else {
        shopCode = this.shopCode;
        shopName = this.shopName;
      }

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
      // 配送方式列表
      let logisticss = [];
      this.mGoods.forEach((item) => {
        distributionAmount += item.deliveryPrice;

        logisticss.push(item.orderDelivery);
      });

      let info = {
        delivery: delivery,
        distributionAmount: distributionAmount,
        orderAmount: this.totalPrice, // 商品结算金额(优惠后总金额)
        goodss: this.goods,
        logisticss: logisticss,
        payWay: this.payType, // 付款方式：1 支付宝，2 微信
        remark: this.message,
        invoiceFlag: 0, // 是否开具发票：1 是，0 否
        shopCode: shopCode, // 店铺编码
        shopName: shopName, // 店铺名称
      };

      this.$api
        .post(this, api.addOrder, info)
        .then((res) => {
          if (res.success) {
            // 订单编号
            let ids = res.data.ids.toString();

            if (this.totalPrice) {
              // 大于0，调用支付
              this.payMent(ids);
            } else {
              // 直接跳转
              this.$router.replace("/orderList");
            }

            // 删除购物车数据
            this.delShopData(this.orderIds);
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          console.log("error");
        });
    },
    // 兑换订单
    exchangeOrder() {
      this.isDisable = true;
      setTimeout(() => {
        this.isDisable = false;
      }, 5000);

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
      let ryFlowId = sessionStorage.getItem("ryFlowId");
      this.goods.forEach((item) => {
        this.$set(item, "codes", ryFlowId);
      });

      let info = {
        delivery: delivery,
        distributionAmount: distributionAmount,
        orderAmount: orderAmount,
        goodss: this.goods,
        logisticss: logisticss,
        payWay: this.payType, // 付款方式：1 支付宝，2 微信
        remark: this.message,
        invoiceFlag: 0, // 是否开具发票：1 是，0 否
      };

      this.$api
        .post(this, api.thirdAddOrder, info)
        .then((res) => {
          if (res.success) {
            // 订单编号
            let ids = res.data.ids.toString();

            // 直接跳转
            this.$router.replace({
              path: "/orderDetail",
              query: { id: ids },
            });

            // 删除购物车数据
            this.delShopData(this.orderIds);
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          console.log("error");
        });
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
      this.goodsEnable = item.goodsEnable;
      this.amountEnable = item.amountEnable;
      this.deliveryPrice = item.deliveryFee;
      this.exchangDelivery(item);
      this.getCoupontInfo(item);
      this.showSale = false;
    },
    // 获取优惠券数据
    getCoupontInfo(item) {
      if (item) {
        this.exchange = item.couponNo;

        this.couponMethod = item.couponMethod;
        if (item.couponMethod === 1) {
          // 满减
          this.saleDiscount = 1;
          this.salePrice = Number(item.reduction);
          this.saleValue = "-¥" + this.salePrice;
          this.addForm.distributionMoney = 0;

          this.calcCoupon(item);
        } else if (item.couponMethod === 2) {
          // 折扣
          this.salePrice = 0;
          this.saleDiscount = Number(item.discount) / 100;
          this.saleValue = this.saleDiscount * 10 + "折";
          this.addForm.distributionMoney = 0;

          this.calcCoupon(item);
        } else {
          // 商品兑换
          this.saleDiscount = 1;
          this.disItemPrice = 0; // 已优惠货品价格
          this.itemPrice = 0; // 货品总价格
          // 开启物流费
          let flag = true;
          this.goods.forEach((good) => {
            this.$set(good, "couponNo", ""); // 清空优惠券
            this.itemPrice += good.salePrice * good.itemCount;
            if (flag) {
              if (Number(item.materialScope) === 2) {
                // 指定材质
                item.materialIds.forEach((material) => {
                  if (good.diy.materialId === material) {
                    if (Number(item.modelScope) === 2) {
                      // 同时指定型号
                      item.modelIds.forEach((model) => {
                        if (good.diy.modelId === model) {
                          flag = false;
                          this.salePrice = good.salePrice;
                          this.saleValue = "-¥ " + good.salePrice;
                          this.$set(good, "couponNo", this.exchange); // 优惠券id
                        }
                      });
                    } else {
                      flag = false;
                      this.salePrice = good.salePrice;
                      this.saleValue = "-¥ " + good.salePrice;
                      this.$set(good, "couponNo", this.exchange); // 优惠券id
                    }
                  }
                });
              } else {
                let randomPrice = 0;
                let curItem = {};
                if (Number(good.salePrice) >= Number(item.orderMoney)) {
                  flag = false;
                  randomPrice = good.salePrice;
                  curItem = good;
                } else {
                  randomPrice = this.orderItems[0].salePrice;
                  curItem = this.orderItems[0];
                }
                this.salePrice = randomPrice;
                this.saleValue = "-¥" + this.salePrice;
                this.$set(curItem, "couponNo", this.exchange); // 优惠券id
              }
            }
          });
        }
      }
      this.calucatePrice();
    },
    calcCoupon(item) {
      this.disItemPrice = 0; // 已优惠货品价格
      this.itemPrice = 0; // 货品总价格
      this.goods.forEach((good) => {
        this.$set(good, "couponNo", ""); // 优惠券id
        if (Number(item.materialScope) === 2) {
          this.itemPrice += good.salePrice * good.itemCount; // 货品总价格
          // 指定材质
          item.materialIds.forEach((material) => {
            if (good.diy.materialId === material) {
              if (Number(item.modelScope) === 2) {
                // 同时指定型号
                item.modelIds.forEach((model) => {
                  if (good.diy.modelId === model) {
                    this.$set(good, "couponNo", this.exchange); // 优惠券id

                    this.disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
                  }
                });
              } else {
                this.$set(good, "couponNo", this.exchange); // 优惠券id

                this.disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
              }
            }
          });
        } else if (Number(item.modelScope) === 2) {
          this.itemPrice += good.salePrice * good.itemCount; // 货品总价格
          // 指定型号
          item.modelIds.forEach((model) => {
            if (good.diy.modelId === model) {
              if (Number(item.materialScope) === 2) {
                // 同时指定材质
                item.materialIds.forEach((material) => {
                  if (good.diy.materialId === material) {
                    this.$set(good, "couponNo", this.exchange); // 优惠券id

                    this.disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
                  }
                });
              } else {
                this.$set(good, "couponNo", this.exchange); // 优惠券id

                this.disItemPrice += good.salePrice * good.itemCount; // 已优惠货品价格
              }
            }
          });
        } else {
          // 未指定，货品总优惠
          this.$set(good, "couponNo", this.exchange); // 优惠券id

          this.itemPrice += good.salePrice * good.itemCount * this.saleDiscount; // 货品总价格
        }
      });

      this.disItemPrice =
        this.disItemPrice - this.disItemPrice * this.saleDiscount;
    },
    // 打开商品优惠
    openSale() {
      this.getOrderCouponList(this.size, this.status);
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
            this.totalPage = res.data.total;
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
    // 获取更多优惠券
    getMore(status) {
      this.page++;
      let size = this.size * this.page;
      this.getOrderCouponList(size, status);
    },
    // 取消优惠券
    cancelCoupon() {
      // 重置货品总价格、已优惠货品价格
      this.itemPrice = 0;
      this.disItemPrice = 0;
      this.goods.forEach((good) => {
        this.itemPrice += good.salePrice * good.itemCount;
        this.$set(good, "couponNo", ""); // 清空优惠券
      });

      this.salePrice = 0;
      this.saleDiscount = 1;
      this.addForm.distributionMoney = 0;
      this.deliveryPrice = 0;
      this.goodsEnable = "";
      this.amountEnable = "";
      this.saleValue = "不使用优惠券";
      this.exchange = "";

      this.exchangDelivery(null);
      this.calucatePrice();
      this.showSale = false;
    },
    // 重新获取
    refresh(status) {
      this.getOrderCouponList(this.size, status);
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
      this.couponList = [];
      this.status = val;
      this.getOrderCouponList(this.size, this.status);
    },
    // 返回
    toBack() {
      this.$router.back("-1");
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
            this.$toast.fail("获取配送方式失败~");
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
    // 查询该地址是否停发
    getDeliveryStopStatus(type) {
		
      if (this.addArr <= 0) {
        this.$toast("请选择收货地址");
        return;
      }
      
      let content = this.addForm.wholeAddr;
      this.$api
        .get(this, api.findMatchDeliveryStop, {
          content: content,
        })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              // 按钮颜色
              let buttonColor = "#0076A5";
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
                  if (type === 1) {
                    // 支付订单
                    this.submitOrder();
                  } else if (type === 2) {
                    // 兑换订单
                    this.exchangeOrder();
                  }
                })
                .catch((error) => {
                });
            } else {
              // 不匹配，直接下单
              if (type === 1) {
                // 支付订单
                this.submitOrder();
              } else if (type === 2) {
                // 兑换订单
                this.exchangeOrder();
              }
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
			
    },
	
	//点击知道了隐藏柔性关闭弹窗
	clickFlexible(){
	  this.showFlexible = false;
	}
  },
  beforeRouteEnter(to, from, next) {
    let distributorId = localStorage.getItem("distributorId");
    if (from.name === null && Number(distributorId) !== 4378) {
      next({
        path: "/orderList",
      });
    } else {
      next();
    }
  },
  components: {
    VHeader,
    Coupon,
    Protocol,
    ProtocolJYDD,
    Notice,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.index {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 56px;
  background-color: $color-background;

  .container {
    position: relative;
    height: 100%;
    padding: 15px;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .order-wrap {
      margin-bottom: 15px;
      border-radius: 8px;
      overflow: hidden;

      .item-box {
        margin-bottom: 0;
        border-radius: 0;
      }
    }

    .notice-box {
      margin-top: -15px;
    }

    .item-box {
      display: inline-block;
      width: 100%;
      margin-bottom: 15px;
      padding: 16px 10px;
      color: $color-text;
      box-sizing: border-box;
      border-radius: 8px;
      background-color: $color-background-white;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      &.double {
        padding: 10px 22px;

        .tr-box {
          height: 36px;
          line-height: 36px;
        }
      }

      &.pay {
        padding: 16px 22px 12px;
        margin-bottom: 30px;

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

      .address-wrap {
        position: relative;
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
          font-size: 24px;

          &.icon-c-location {
            top: 0;
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

      .no-address {
        padding: 4px 0;
        text-align: center;

        .van-icon {
          font-size: $font-size-medium-x;
          color: $color-text-h;
          margin-right: 10px;
          vertical-align: middle;
        }

        .text {
          display: inline-block;
          font-size: $font-size-medium;
          color: $color-text;
          vertical-align: middle;
        }
      }

      .order-info {
        display: flex;

        .pro-img {
          position: relative;
          display: inline-block;
          width: 90px;
          height: 90px;
          border-radius: 6px;
          overflow: hidden;
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
          margin: 4px 0 0 8px;

          .tbox {
            display: flex;
            display: -webkit-flex;
            align-items: center;

            .name {
              display: -webkit-box;
              flex: 1;
              text-align: left;
              font-size: 14px;
              font-weight: 600;
              line-height: 1.5;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;
            }

            .price {
              display: inline-block;
              margin-left: 20px;
              font-size: 14px;

              em {
                display: inline-block;
                margin-left: 5px;
                font-size: $font-size-medium-x;
                font-style: normal;

                &.line {
                  text-decoration: line-through;
                }
              }
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
        font-size: $font-size-medium;

        .name {
          display: inline-block;
          font-weight: 600;
          color: $color-text-d;
        }

        .left {
          display: inline-block;
          font-weight: 400;
          color: $color-text;
        }

        .text {
          flex: 1;
          margin-left: 14px;
          color: #9B9B9B;
          text-align: left;
        }

        .price {
          display: inline-block;
          color: $color-text-d;
          text-align: right;
          height: 36px;
          line-height: 36px;

          .desc {
            margin-right: 10px;
            font-size: 12px;
            color: #4a4a4a;
          }

          &.right {
            flex: 1;
          }

          &.sale {
            color: #D0021B;

            em {
              display: inline-block;
              margin-left: 5px;
              font-size: $font-size-large;
              font-style: normal;
            }
          }

          &.nosale {
            color: #737373;
          }
        }

        .input {
          flex: 1;
          padding: 10px 0 10px 16px;

          >>>.van-field__body {
            padding: 0 10px;
            height: 26px;
            line-height: 26px;
            background-color: #F3F3F3;

            input {
              text-align: center;
            }
          }
        }

        .icon {
          display: inline-block;
          margin-right: 10px;
          font-size: 26px;

          &.icon-pay-wechat {
            color: #07BF62;
          }

          &.icon-alipay {
            color: #0076A5;
          }
        }

        .icon-right {
          display: inline-block;
          margin-right: -5px;
          height: 36px;
          line-height: 36px;
        }

        .pname {
          display: inline-block;
          width: 80%;
          text-align: left;
          font-size: $font-size-medium;
          color: $color-text;
        }

        .radio {
          check-style();
        }
      }
    }

    .protocol-box {
      margin-bottom: 60px;
      text-align: center;
      font-size: 10px;
      color: $color-text-d;

      .checkbox {
        position: relative;
        display: inline-block;
        width: 16px;
        height: 16px;
        border: 1px solid #ccc;
        border-radius: 50%;
        vertical-align: middle;

        &:checked {
          position: relative;
          border-color: $color-theme;
          background-color: $color-theme;
        }

        &::after {
          content: '';
          position: absolute;
          top: 3px;
          left: 3px;
          display: inline-block;
          width: 6px;
          height: 3px;
          border-bottom: 2px solid $color-text-w;
          border-left: 2px solid $color-text-w;
          transform: rotate(-45deg);
        }
      }

      .text {
        display: inline-block;
        margin-left: 5px;
        font-size: $font-size-small;
        color: $color-text-b;
        vertical-align: middle;

        .point {
          color: $color-theme;
        }
      }

      >>>.el-checkbox {
        .el-checkbox__input {
          &.is-focus {
            .el-checkbox__inner {
              border-color: #1CB8CE;
            }
          }
        }

        &.is-checked {
          .el-checkbox__inner {
            background-color: #1CB8CE;
            border-color: #1CB8CE;
          }

          .el-checkbox__label {
            color: #1CB8CE;
          }
        }
      }
    }
  }

  .btn-box {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 0 15px;
    box-sizing: border-box;
    height: 56px;
    line-height: 56px;
    text-align: right;
    background-color: $color-background-white;
    box-shadow: 0 2px 12px 0 rgba(235, 232, 232, 0.5);
    z-index: 99;

    &.no-price {
      padding: 8px 15px;

      .btn {
        display: block;
        margin-left: 0;
        width: 100%;
      }
    }

    .total {
      display: inline-block;
      font-size: $font-size-medium;
      color: $color-text;

      .price {
        font-size: $font-size-small;
        color: #D10720;

        em {
          font-size: $font-size-large;
          font-style: normal;
        }
      }
    }

    .btn {
      display: inline-block;
      margin-left: 20px;
      width: 120px;
      height: 40px;
      line-height: 40px;
      text-align: center;
      font-size: $font-size-medium-x;
      color: $color-background-white;
      background: $color-theme;
      border-radius: 70px;

      &.readonly {
        pointer-events: none;
        color: $color-text-dd;
        background: $color-background-l;
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
      border-left: 2px solid #0076A5;

      .title {
        display: inline-block;
        font-size: $font-size-medium;
        margin-left: 22px;
        color: $color-text-d;
      }

      .icon {
        display: inline-block;
        padding: 10px 15px;
        margin-top: -10px;
        font-size: $font-size-large;
        color: $color-text-d;
        float: right;
      }
    }

    .pop-top {
      display: flex;
      padding: 0 20px;
      margin-top: 28px;

      .input {
        flex: 1;
        font-size: $font-size-medium;
        color: $color-text-l;
        background-color: $color-background-d;
        border-radius: 4px;

        >>>.van-field__control {
          text-align: center;
        }
      }

      .btn {
        display: inline-block;
        margin-left: 15px;
        width: 88px;
        height: 40px;
        line-height: 40px;
        font-size: $font-size-medium-x;
        text-align: center;
        color: $color-text-w;
        background-color: $color-theme;
        border-radius: 4px;
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
          height: 54px;
          line-height: 54px;

          &.van-hairline--top-bottom {
            &::after {
              border-width: 0;
              border: none;
            }
          }

          .van-tabs__line {
            bottom: 22px;
            width: 30px;
            height: 4px;
            background-color: $color-theme;
            border-radius: 4px;
          }

          .van-tab {
            .van-ellipsis {
              font-size: $font-size-medium;
              font-weight: bold;
              color: $color-text-d;
            }

            &.van-tab--active{
              font-size: 16px;
              color: $color-theme;
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
            -webkit-overflow-scrolling: touch;

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
    box-sizing: border-box;
    text-align: center;
    background-color: #F6F6F6;
    transform: translate3d(100%, 0, 0);
    animation: all 10s linear 1;
    z-index: 100;

    &.show {
      transform: translate3d(0, 0, 0);
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
  background-color: $color-bg;

  .container {
    padding: 8px 16px;

    .item-box {
      margin-bottom: 8px;

      .address-wrap {
        .icon {
          &.icon-c-location {
            color: $color-main;
          }
        }
      }

      .tr-box {
        .radio:checked {
          border-color: $color-main;
          background-color: $color-main;
        }
      }
    }

    .order-wrap {
      margin-bottom: 8px;
    }

    .protocol-box {
      .checkbox:checked {
        border-color: $color-main;
        background-color: $color-main;
      }

      .text .point {
        color: $color-main;
      }
    }
  }

  .btn-box .btn {
    background: $color-main;
  }
}
</style>
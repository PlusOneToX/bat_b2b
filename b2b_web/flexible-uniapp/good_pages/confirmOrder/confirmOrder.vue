<template>
    <view class="index" :class="{ 'ry-style': Number(distributorId) === 4378 }">
        <view class="container">
            <view class="item-box" @click="handleAddress(1)">
                <view class="address-wrap" v-if="addArr > 0">
                    <image class="address_icon" src="../../static/images/url2.png"></image>
                    <view class="top">
                        <span class="name">{{ addForm.userName }}</span>
                        <span class="tel">{{ addForm.mobile }}</span>
                    </view>
                    <view class="bottom address">
                        {{ addForm.wholeAddr }}
                    </view>
                    <i class="icon icon-right"></i>
                </view>
                <view class="no-address" v-else style="display: flex;align-items: center;">
                    <view style="width: 80rpx;">
                        <uni-icons type="plusempty" size="30" color="#979797"></uni-icons>
                    </view>
                    <view class="text">您还没有收货地址，请点击添加</view>
                </view>
            </view>
            <view class="co-listModule" v-for="goods in mGoods" :key="goods">
                <view style="display: flex;align-items: center;justify-content: space-between;" @click="openShop">
                    <view class="co-listModule-line">
                        <view class="co-listModule-lineBg" v-if="(sIndex<=2)" v-for="(item, sIndex) in goods" :key="sIndex">
                            <image :src="(item.diy.previewImage)+'?x-oss-process=image/resize,h_400,l_400/'" mode="aspectFit"></image>
                        </view>
                    </view>
                    <view class="summation_totalCount">
                        共计{{gettotalCount(goods)}} 件
                        <!-- {{totalCount}} -->

                        <uni-icons type="right" size="20"></uni-icons>
                    </view>
                </view>
                <view style="border-bottom: 1rpx dotted rgba(232, 232, 232, 1);width: 640rpx;margin: 0 auto;"></view>
                <view class="item_box double" v-show="showPrice">
                    <view class="tr-box" @click="openExpressage">
                        <view class="left">配送方式</view>
                        <view class="text">
                            <!-- {{ express}} -->
                        </view>
                        <view style="line-height: 30rpx;">
                            <view class="text">{{express}}</view>
                            <view class="text" style="text-align: right;">{{goods.deliveryPrice === 0||goods.deliveryPrice==undefined ? "包邮" : "¥ " +  goods.deliveryPrice}}</view>
                        </view>
                        <i class="icon icon-right" v-show="goods.deliveryPrice"></i>
                    </view>
                </view>
                <!-- <view class="co-inform">
                    <view class="co-inform-line2">
                        <view class="co-inform-title">买家留言</view>
                        <view class="co-inform-RG">
                            <input placeholder="您还有什么要求请在这里说" v-model="message" />
                        </view>
                    </view>
                </view> -->
            </view>

            <view class="item-box double">
                <view class="tr-box">
                    <view class="left">商品金额</view>
                    <view class="text"></view>
                    <view class="price" style="color:#FC7E1B">
                        ￥{{ orderPrice.toFixed(2) }}
                    </view>
                </view>
                <view class="tr-box">
                    <view class="left">运费</view>
                    <view class="text"></view>
                    <view class="price" style="color:#FC7E1B">
                        ￥{{ deliveryPrice.toFixed(2) }}
                    </view>
                </view>
                <view class="tr-box" @click="openSale" v-if="showPrice">
                    <view class="left">商品优惠</view>
                    <view class="text"></view>
                    <view style="color:#FC7E1B" class="price" :class="saleFlag ? 'sale' : 'nosale'" v-show="salePrice > 0 || saleDiscount > 0">
                        {{ saleValue }}
                    </view>
                    <uni-icons type="forward" size="18"></uni-icons>
                </view>
            </view>
            <view class="item-box pay" v-show="showPrice">
                <view class="tr-header">支付方式</view>
                <radio-group>
                    <view class="tr-box" v-if="(payWayType == undefined || payWayType == null || payWayType == '' || payWayType == 0 || payWayType == 2)">
                        <image class="pay_img" src="../../static/images/wechat_icon.png"></image>
                        <span class="pname">微信支付</span>
                        <radio value="2" :checked="(payWay ==2)" style="transform:scale(0.7)" @click="chock(2)" />
                    </view>
                    <!-- #ifdef H5 -->
                    <view class="tr-box" v-if="(payWayType == undefined || payWayType == null || payWayType == '' || payWayType == 0 || payWayType == 1)">
                        <image class="pay_img" src="../../static/images/alipay_icon.png"></image>
                        <span class="pname">支付宝</span>
                        <radio value="1" :checked="(payWay ==1)" style="transform:scale(0.7)" @click="chock(1)" />
                    </view>
                    <!-- #endif -->
                    <view class="tr-box" v-if="payWayType != undefined && payWayType == 4">
                        <image class="pay_img" src="../../static/images/pay.png"></image>
                        <span class="pname">线下付款</span>
                        <radio value="2" :checked="(payWay ==4)" style="transform:scale(0.7)" @click="chock(4)" />
                    </view>
                </radio-group>
            </view>
        </view>

        <view class="co-btm" v-if="showPrice">
            <view class="co-btm-Lf">
                <text>实付</text><text>(含运费)</text>
                <text>￥{{totalPrice}}</text>
            </view>
            <!-- <view class="co-btm-rg" @click="getDeliveryStopStatus(1)" v-if="isDisable == false">提交订单
            </view> -->
            <view class="co-btm-rg" @click="OrderPopup=!OrderPopup" v-if="isDisable == false">提交订单
            </view>
            <view class="co-btm-rg co-btm-btnNo" v-if="isDisable == true">提交订单
            </view>
        </view>
        <view class="co-btm" v-else>
            <view class="co-btm-Lf">

            </view>
            <view class="co-btm-rg" @click="getDeliveryStopStatus(2)">完成兑换
            </view>
        </view>
        <!-- 确认订单  -->
        <ConfirmOrder @goAgreement="goAgreement1()" :OrderPopup="OrderPopup" :addForm="addForm" @getDeliveryStopStatus="getDeliveryStopStatus(1)"></ConfirmOrder>
        <!-- 查看商品 -->
        <uni-popup ref="popup" type="bottom" :safeArea="false">
            <viewShop :splitItemList="goods" @shut_down="shutDown"></viewShop>
        </uni-popup>
        <!-- 优惠券 -->
        <uni-popup ref="discount_popup" type="bottom" :safeArea="false">
            <discount :couponList="couponList" @shut_down="shutDown" @clickItem="handleItem"></discount>
        </uni-popup>
        <view style="height: 100rpx;"></view>

    </view>
</template>
<script type="text/ecmascript-6">
import api from "common/js/allApi.js";
import { aliPay } from "common/js/pay";
import viewShop from "../../components/viewShop";
import discount from "../confirmOrder/components/discount";
import ConfirmOrder from "../confirmOrder/components/confirmOrder";
export default {
    components: {
        viewShop, discount, ConfirmOrder
    },
    data() {
        return {
            OrderPopup: false,
            orderPrice: 0,
            title: "确认订单",
            userId: "",
            platform: 0,
            payWay: 2,
            payWayType: 2,
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

            showFlexible: false, //是否显示柔性关闭弹窗
            totalCount: 0,
            deliveryPrice: 0.00,
        };
    },
    mounted() {

    },
    onLoad(parameter) {
        if (parameter.goodsDate != undefined) {
            uni.setStorageSync("goods", parameter.goodsDate)
        }
        this.addrId = uni.getStorageSync("addrId");
        this.userId = uni.getStorageSync("userId");
        this.userNo = uni.getStorageSync("userNo");
        this.phone =
            uni.getStorageSync("phone") && uni.getStorageSync("phone") !== "null"
                ? uni.getStorageSync("phone")
                : "";
        // shopId/shopCode/shopName
        this.shopId = uni.getStorageSync("shopId");
        this.shopCode = uni.getStorageSync("shopCode") || "";
        this.shopName = uni.getStorageSync("shopName") || "";
        // 平台
        this.platform = uni.getStorageSync("platform");
        // 分销商ID
        let disId = uni.getStorageSync("distributorId");
        // 付款方式

        this.payWayType = uni.getStorageSync("payWay");
        if (this.payWayType == 0) {
            this.payWay = 2;
        } else {
            this.payWay = this.payWayType;
        }
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
        goAgreement1() {
            uni.navigateTo({
                url: '../theme/agreement',
                complete: function (e) {
                    console.log(e);
                }
            });
        },
        gettotalCount(e) {
            var Count = 0;
            e.forEach(item => {
                Count += item.itemCount
            });
            return Count;
        },
        chock(i) {
            this.payWay = i;
        },
        shutDown() {
            this.$refs.discount_popup.close('bottom');
            this.$refs.popup.close('bottom')
        },
        openShop() {
            this.$refs.popup.open('bottom')
        },
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
            this.$api.post(this, api.getOptimalCoupon, {
                goodss: this.couponItems,
            }).then((res) => {
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
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
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
            console.log("订单列表：", this.mGoods);
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
            let platform = uni.getStorageSync("platform");
            if (platform === "7" || platform === "6" || platform === "1" || platform == "DZXCX") {
                // 所有都包邮
                this.addForm.distributionMoney = price;
                this.distributionValue = price === 0 ? "包邮" : "¥ " + price;
            } else {
                this.distributionValue = price > 0 ? "¥ " + price : "包邮";
            }
        },
        // 订单支付
        payMent(id) {
            // #ifdef H5
            var url = window.location.pathname;
            // var arrUrl = url.split("//");
            // let redirectUrl = 'https://' + window.location.host + '/' + arrUrl[0].split("/")[1] + "/good_pages/order/orderList";
            let redirectUrl = 'https://diy.bat.com/good_pages/order/orderList'
            if (this.payWay == 2) {
                this.$api.post(this, api.handlePayment, {
                    businessType: 1, // 业务类型：1 订单收款
                    customerFlag: 1, // 客户标志：1 C端客户
                    payMethod: "wxpay_h5", // 交易方式
                    orderId: id,
                    payerId: this.userId,
                    redirectUrl: redirectUrl,
                }).then((res) => {
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
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000,
                        });
                        uni.redirectTo({
                            url: '../order/orderList'
                        });
                    }
                });
            } else {
                this.$api.post(this, api.handlePayment, {
                    businessType: 1, // 业务类型：1 订单收款
                    customerFlag: 1, // 客户标志：1 C端客户
                    payMethod: "alipay_wap", // 交易方式
                    orderId: id,
                    payerId: this.userId,
                    redirectUrl: redirectUrl,
                }).then((res) => {
                    if (res.success) {
                        let data = res.data.alipayResp.from;
                        aliPay(data);
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000,
                        });
                        uni.redirectTo({
                            url: '../order/orderList'
                        });
                    }
                });
            }
            // #endif
            // #ifdef MP-WEIXIN
            this.$api.post(this, api.handlePayment, {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_mini_program", // 交易方式
                orderId: id,
                payerId: this.userId,
                platformUserId: uni.getStorageSync("openId"),
                appId: uni.getAccountInfoSync().miniProgram.appId
            }).then((res) => {
                if (res.success) {
                    let wxResp = res.data.wxResp;
                    uni.requestPayment({
                        provider: "wxpay",
                        timeStamp: wxResp.timeStamp,
                        nonceStr: wxResp.nonceStr,
                        package: wxResp.prepayId,
                        signType: wxResp.signType,
                        paySign: wxResp.paySign,
                        success: function (res) {
                            uni.showToast({
                                title: '支付成功',
                                icon: "none",
                                duration: 2000,
                            });
                            uni.redirectTo({
                                url: '../order/orderList'
                            });
                        },
                        fail: function (err) {
                            uni.showToast({
                                title: '支付失败',
                                icon: "none",
                                duration: 2000,
                            });
                            uni.redirectTo({
                                url: '../order/orderList'
                            });
                        },
                    });
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                    uni.redirectTo({
                        url: "../order/orderList?active=0",
                    });
                }
            });
            // #endif
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
                            uni.showToast({
                                icon: 'none',
                                title: res.error,
                                duration: 2000
                            });
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
            let goods = JSON.parse(uni.getStorageSync("goods"));
            console.log("商品信息：", goods);
            this.totalCount = uni.getStorageSync("totalCount");
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
                    this.orderPrice += price;
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
            uni.redirectTo({
                url: '../address/address?addrId=' + id
            });
        },
        testApp(url, openUrl) {
            window.location.href = openUrl;

        },
        // 支付订单
        submitOrder() {
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
                districtId: this.addForm.districtId || this.addForm.cityId,
                districtName: this.addForm.districtName || this.addForm.cityName,
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

            var staffCode = '';
            if (uni.getStorageSync("superiorCode") != '{4}') {
                staffCode = uni.getStorageSync("superiorCode");
            }

            let info = {
                delivery: delivery,
                distributionAmount: distributionAmount,
                orderAmount: this.totalPrice, // 商品结算金额(优惠后总金额)
                goodss: this.goods,
                logisticss: logisticss,
                payWay: this.payWay, // 付款方式：1 支付宝，2 微信
                remark: this.message,
                invoiceFlag: 0, // 是否开具发票：1 是，0 否
                shopCode: this.shopCode, // 店铺编码
                shopName: this.shopName, // 店铺名称
                staffCode: uni.getStorageSync("staffType") == 3 ? staffCode : uni.getStorageSync("userNo")
            };

            this.$api.post(this, api.addOrder, info).then((res) => {
                if (res.success) {
                    // 订单编号
                    let ids = res.data.ids.toString();
                    if (this.totalPrice && this.payWay != 4) {
                        // 大于0，调用支付
                        this.payMent(ids);
                    } else {
                        // 直接跳转
                        uni.redirectTo({
                            url: "../order/orderList?active=0",
                        });
                    }

                    // 删除购物车数据
                    this.delShopData(this.orderIds);
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            }).catch((error) => {
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
                districtId: this.addForm.districtId || this.addForm.cityId,
                districtName: this.addForm.districtName || this.addForm.cityName,
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
            let ryFlowId = uni.getStorageSync("ryFlowId");

            this.goods.forEach((item) => {
                this.$set(item, "codes", ryFlowId);
            });

            let info = {
                delivery: delivery,
                distributionAmount: distributionAmount,
                orderAmount: orderAmount,
                goodss: this.goods,
                logisticss: logisticss,
                payWay: this.payWay, // 付款方式：1 支付宝，2 微信
                remark: this.message,
                invoiceFlag: 0, // 是否开具发票：1 是，0 否
            };
            this.$api.post(this, api.thirdAddOrder, info).then((res) => {
                if (res.success) {
                    // 订单编号
                    let ids = res.data.ids.toString();
                    uni.redirectTo({
                        url: "../order/orderList?active=0",
                    });
                    // 删除购物车数据
                    this.delShopData(this.orderIds);
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            }).catch((error) => {
                console.log("error：", error);
            });
        },
        // 删除购物车数据
        delShopData(ids) {
            this.$api.delete(this, api.deleteShopcart, {
                ids: ids,
            }).then((res) => {
                if (res.success) {
                    // 删除缓存数据
                    uni.removeStorage("goods");
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            })
                .catch(() => { });
        },
        // 选择配送方式
        openExpressage() {
            if (
                this.addForm.provinceId === 0 ||
                this.addForm.cityId === 0 ||
                this.addForm.districtId === 0
            ) {
                uni.showToast({
                    icon: 'none',
                    title: '请选择收货地址~',
                    duration: 2000
                });
            } else if (this.columns.length <= 0) {
                this.showExpressage = false;
            } else {
                this.showExpressage = true;
            }
        },
        // 选择优惠券
        handleItem(item) {
            //满减券
            if (item.couponMethod == 1 && this.orderPrice < item.orderMoney) {
                uni.showToast({
                    icon: 'none',
                    title: '订单金额未满' + item.orderMoney + '元',
                    duration: 2000
                });
            }
            else if (item.couponMethod == 2 && this.orderPrice < item.orderMoney) {
                uni.showToast({
                    icon: 'none',
                    title: '订单金额未满' + item.orderMoney + '元',
                    duration: 2000
                });
            } else {
                this.goodsEnable = item.goodsEnable;
                this.amountEnable = item.amountEnable;
                this.deliveryPrice = item.deliveryFee;
                this.exchangDelivery(item);
                this.getCoupontInfo(item);
                this.$refs.discount_popup.close('bottom');
            }
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
                            if (item.materials != undefined) {
                                // this.salePrice = good.salePrice;
                                // this.saleValue = "-¥ " + good.salePrice;
                                // this.$set(good, "couponNo", this.exchange); // 优惠券id

                                console.log("材质：", item);
                                // 指定材质
                                item.materials.forEach((material) => {
                                    if (good.diy.materialId === material.materialId) {
                                        if (item.models != undefined) {
                                            // 同时指定型号
                                            item.models.forEach((model) => {
                                                if (good.diy.modelId === model.modelId) {
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


                if (item.materials != undefined) {
                    this.itemPrice += good.salePrice * good.itemCount; // 货品总价格
                    // 指定材质
                    item.materials.forEach((material) => {
                        if (good.diy.materialId === material.materialId) {
                            if (item.models != undefined) {
                                // 同时指定型号
                                item.models.forEach((model) => {
                                    if (good.diy.modelId === model.modelId) {
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
                } else if (item.models != undefined) {
                    this.itemPrice += good.salePrice * good.itemCount; // 货品总价格
                    // 指定型号
                    item.models.forEach((model) => {
                        if (good.diy.modelId === model.modelId) {
                            if (Number(item.materialScope) === 2) {
                                // 同时指定材质
                                item.materialIds.forEach((material) => {
                                    if (good.diy.materialId === material.materialId) {
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
            if (this.saleValue != '无可用优惠券') {
                this.getOrderCouponList(this.size, this.status);
            }
            // this.showSale = true;
        },
        // 根据商品列表获取优惠券列表
        getOrderCouponList(size, status) {
            this.$api.post(this, api.getGoodsCoupn, {
                page: 1,
                size: size,
                statuss: status,
                goodss: this.couponItems,
            }).then((res) => {
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
                    this.$refs.discount_popup.open('bottom')
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
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
            this.$api.post(this, api.getCalculations, {
                distributorId: this.distributorId,
                billingMethod: 1, // 订单结算方式：1重量计费，2体积计费
                countryId: 37,
                provinceId: this.addForm.provinceId,//130000,
                cityId: this.addForm.cityId,//130300
                useRange: 2, // 适用范围：1.普通商品，2.定制商品 3 普通商品和定制商品
                price: price,
                manufactors: manufactor,
                weight: 0,
            }).then((res) => {
                if (res.success && res.data && res.data.length > 0) {
                    let logisticId = res.data[0].id;
                    if (logisticId) {
                        this.getLogisticDetail(logisticId, item);
                    }
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: '获取配送方式失败~',
                        duration: 2000
                    });
                }
            });
        },
        // 查询单个配送
        getLogisticDetail(id, item) {
            this.$api.get(this, api.getLogistics, {
                id: id,
            }).then((res) => {
                if (res.success && res.data) {
                    this.$set(item, "delivery", res.data);
                    this.$set(item, "orderDelivery", {});
                    this.$set(item.orderDelivery, "logisticsId", res.data.id);
                    this.$set(item.orderDelivery, "logisticsName", res.data.name);
                    this.express = res.data.name;
                    this.$set(item.orderDelivery, "logisticsType", 2); // 配送方式类型：1 普通商品（标品） 2 定制工厂
                    this.$set(item.orderDelivery, "manufactors", res.data.manufactors);
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            });
        },
        // 查询该地址是否停发
        getDeliveryStopStatus(type) {

            if (this.addArr <= 0) {
                uni.showToast({
                    icon: 'none',
                    title: '请选择收货地址',
                    duration: 2000
                });
                return;
            }
            let content = this.addForm.wholeAddr;
            this.$api.get(this, api.findMatchDeliveryStop, {
                content: content,
            }).then((res) => {
                if (res.success) {
                    if (res.data) {
                        // 按钮颜色
                        let buttonColor = "#0076A5";
                        if (Number(this.distributorId) === 4378) {
                            // 荣耀
                            buttonColor = "#256FFF";
                        }
                        // 匹配，弹窗提示用户该地址停发

                        if (type === 1) {
                            // 支付订单
                            this.submitOrder();
                        } else if (type === 2) {
                            // 兑换订单
                            this.exchangeOrder();
                        }

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
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            });

        },

        //点击知道了隐藏柔性关闭弹窗
        clickFlexible() {
            this.showFlexible = false;
        }
    },
    beforeRouteEnter(to, from, next) {
        let distributorId = uni.getStorageSync("distributorId");
        if (from.name === null && Number(distributorId) !== 4378) {
            next({
                path: "/orderList",
            });
        } else {
            next();
        }
    },
};
</script>
<!-- lang="stylus" -->
<style scoped lang="stylus">
.co-btm {
    position: fixed;
    width: 100%;
    bottom: 0;
    left: 0;
    background: #fff;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx 0rpx 20rpx 30rpx;
    box-sizing: border-box;
    z-index: 99;
    border-top: 1rpx solid rgba(247, 247, 247, 1);

    .co-btm-Lf {
        font-size: 22rpx;
        color: rgba(16, 16, 16, 1);
        font-family: PingFangSC-regular;

        text:nth-child(3) {
            font-size: 34rpx;
            color: rgba(252, 126, 27, 1);
            // color: #000;
            font-weight: 600;
            margin-left: 15rpx;
            font-family: PingFangSC-regular;
        }
    }

    .co-btm-rg {
        width: 200rpx;
        height: 70rpx;
        line-height: 70rpx;
        border-radius: 50rpx 0rpx 0rpx 50rpx;
        background-color: rgba(252, 126, 27, 1);
        font-size: 24rpx;
        font-family: Arial;
        color: #fff;
        text-align: center;
    }

    .co-btm-btnNo {
        color: #999;
        background: #ebebeb;
    }
}

.pay_img {
    width: 40rpx;
    height: 40rpx;
    margin-right: 20rpx;
}

.co-inform {
    // background: #fff;
    color: rgba(16, 16, 16, 1);
    font-size: 24rpx;
    text-align: left;
    font-family: PingFangSC-regular;
    height: 100rpx;

    .co-inform-line2 {
        display: flex;

        .co-inform-title {
            font-size: 28rpx;
            width: 120rpx;
            margin-left: 40rpx;
            height: 70rpx;
            line-height: 70rpx;
        }
    }
}

.co-inform-RG {
    display: flex;
    align-items: center;
    color: #ed5307;
    width: 500rpx;
    border-radius: 20rpx;
    background-color: rgba(239, 239, 239, 1);
    color: rgba(154, 154, 154, 1);
    border: 1rpx solid rgba(247, 247, 247, 1);
    height: 70rpx;
    display: flex;
    justify-content: center;
    align-items: center;

    input {
        width: 450rpx;
        display: block;
        font-size: 28rpx;
        font-family: PingFangSC-regular;
        color: #000000;
    }
}

.co-listModule {
    width: 690rpx;
    border: 1rpx solid rgba(239, 239, 239, 1);
    margin: 0 auto;
    margin-bottom: 30rpx;
    border-radius: 30rpx;
    background: #fff;

    .co-listModule-line {
        display: flex;
        align-items: center;

        .co-listModule-lineTitle {
            padding: 25rpx 30rpx;
            border-bottom: 1rpx solid $opacity-border;
            font-size: 30rpx;
            color: $uni-text-color;
            font-weight: bold;
        }

        .co-listModule-lineBg {
            margin: 20rpx 0rpx;
            margin-left: 10rpx;
            width: 150rpx;
            height: 150rpx;
            display: flex;
            justify-content: center;
            align-items: center;

            image {
                width: 84rpx;
                height: 150rpx;
            }
        }

        .co-listModule-lineSm {
            display: flex;
            justify-content: space-between;
            padding: 30rpx;

            .co-smText {
                font-size: 22rpx;
                color: #999;
                display: block;
            }

            .co-smText:nth-child(2), .co-smText:nth-child(3) {
                margin-top: 6rpx;
            }

            .co-lineSm-Lf {
                display: flex;

                image {
                    width: 111rpx;
                    height: 111rpx;
                    margin-right: 25rpx;
                }
            }

            .co-lineSm-Rg {
                .co-smTextPrice {
                    font-size: 30rpx;
                    font-weight: 400;

                    text {
                        font-size: 20rpx;
                    }
                }
            }
        }
    }

    .summation_totalCount {
        margin-right: 20rpx;
        color: rgba(16, 16, 16, 1);
        font-size: 24rpx;
        font-family: PingFangSC-regular;
        display: flex;
        align-items: center;

        .toIcon {
            margin-left: 10rpx;
        }
    }
}

.item_box {
    display: inline-block;
    width: 100%;
    padding: 32rpx 20rpx;
    color: rgba(16, 16, 16, 1);
    box-sizing: border-box;

    &.double {
        padding: 20rpx 44rpx;

        .tr-box {
            height: 72rpx;
            line-height: 72rpx;
        }
    }

    &.pay {
        padding: 32rpx 44rpx 24rpx;
        margin-bottom: 60rpx;

        .tr-box {
            height: 100rpx;
        }
    }

    .tr-header {
        display: block;
        padding-bottom: 24rpx;
        padding-left: 12rpx;
        margin-bottom: 20rpx;
        font-size: $font-size-medium-x;
        font-weight: 500;
        color: #4A4A4A;
        border-bottom: 1rpx solid #EBEBEB;
    }

    .address-wrap {
        position: relative;
        padding-left: 60rpx;
        color: #4A4A4A;

        .address_icon {
            width: 50rpx;
            height: 50rpx;
            position: absolute;
            top: 0;
            left: 0;
        }

        .top {
            font-size: 28rpx;
            font-weight: bold;
            line-height: 50rpx;
        }

        .name {
            margin-left: 20rpx;
        }

        .tel {
            margin-left: 40rpx;
        }

        .bottom {
            // margin-top: 18rpx;
            line-height: 40rpx;
            font-size: 28rpx;
            color: #666;
        }
    }

    .no-address {
        .uni-icons {
            font-size: 28rpx;
            color: rgba(16, 16, 16, 1);
            margin-right: 20rpx;
            vertical-align: middle;
        }

        .text {
            font-size: 28rpx;
            color: #666;
            vertical-align: middle;
        }
    }

    .order-info {
        display: flex;

        .pro-img {
            position: relative;
            display: inline-block;
            width: 180rpx;
            height: 180rpx;
            border-radius: 12rpx;
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
            margin: 8rpx 0 0 16rpx;

            .tbox {
                display: flex;
                display: -webkit-flex;
                align-items: center;

                .name {
                    display: -webkit-box;
                    flex: 1;
                    text-align: left;
                    font-size: 28rpx;
                    font-weight: 600;
                    line-height: 1.5;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                }

                .price {
                    display: inline-block;
                    margin-left: 40rpx;
                    font-size: 28rpx;

                    em {
                        display: inline-block;
                        margin-left: 10rpx;
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
                    width: 100rpx;
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
            margin-left: 28rpx;
            color: #9B9B9B;
            text-align: left;
        }

        .price {
            display: inline-block;
            color: $color-text-d;
            text-align: right;
            height: 72rpx;
            line-height: 72rpx;

            .desc {
                margin-right: 20rpx;
                font-size: 24rpx;
                color: #4a4a4a;
            }

            &.right {
                flex: 1;
            }

            &.sale {
                color: #D0021B;

                em {
                    display: inline-block;
                    margin-left: 10rpx;
                    font-size: $font-size-large;
                    font-style: normal;
                }
            }

            &.nosale {
                color: #737373;
            }
        }

        .icon {
            display: inline-block;
            margin-right: 20rpx;
            font-size: 52rpx;

            &.icon-pay-wechat {
                color: #07BF62;
            }

            &.icon-alipay {
                color: #0076A5;
            }

            &.icon-fukuan {
                color: #0076A5;
            }
        }

        .icon-right {
            display: inline-block;
            margin-right: -10rpx;
            height: 72rpx;
            line-height: 72rpx;
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

.index {
    // position: fixed;
    width: 100%;
    background-color: $color-background;

    .container {
        position: relative;
        height: 100%;
        padding: 30rpx;
        box-sizing: border-box;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;

        &::-webkit-scrollbar {
            display: none;
        }

        .order-wrap {
            margin-bottom: 30rpx;
            border-radius: 30rpx;
            overflow: hidden;

            .item-box {
                margin-bottom: 0;
                border-radius: 0;
            }
        }

        .notice-box {
            margin-top: -30rpx;
        }

        .item-box {
            display: inline-block;
            width: 100%;
            margin-bottom: 30rpx;
            padding: 32rpx 20rpx;
            color: rgba(16, 16, 16, 1);
            box-sizing: border-box;
            background-color: #FFFFFF;
            border-radius: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            border: 1rpx solid rgba(239, 239, 239, 1);

            &.double {
                padding: 20rpx 44rpx;

                .tr-box {
                    height: 72rpx;
                    line-height: 72rpx;
                }
            }

            &.pay {
                padding: 32rpx 44rpx 24rpx;
                margin-bottom: 60rpx;

                .tr-box {
                    height: 100rpx;
                }
            }

            .tr-header {
                display: block;
                padding-bottom: 24rpx;
                padding-left: 12rpx;
                margin-bottom: 20rpx;
                font-size: $font-size-medium-x;
                font-weight: 500;
                color: #4A4A4A;
                border-bottom: 1rpx solid #EBEBEB;
            }

            .address-wrap {
                position: relative;
                padding-left: 60rpx;
                color: #4A4A4A;

                .address_icon {
                    width: 50rpx;
                    height: 50rpx;
                    position: absolute;
                    top: 0;
                    left: 0;
                }

                .top {
                    font-size: 28rpx;
                    font-weight: bold;
                    line-height: 50rpx;
                }

                .name {
                    margin-left: 20rpx;
                }

                .tel {
                    margin-left: 40rpx;
                }

                .bottom {
                    // margin-top: 18rpx;
                    line-height: 40rpx;
                    font-size: 28rpx;
                    color: #666;
                }
            }

            .no-address {
                padding: 8rpx 0;
                text-align: center;

                .uni-icons {
                    font-size: 28rpx;
                    color: rgba(16, 16, 16, 1);
                    margin-right: 20rpx;
                    vertical-align: middle;
                }

                .text {
                    display: inline-block;
                    font-size: 28rpx;
                    color: #666;
                    vertical-align: middle;
                }
            }

            .order-info {
                display: flex;

                .pro-img {
                    position: relative;
                    display: inline-block;
                    width: 180rpx;
                    height: 180rpx;
                    border-radius: 12rpx;
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
                    margin: 8rpx 0 0 16rpx;

                    .tbox {
                        display: flex;
                        display: -webkit-flex;
                        align-items: center;

                        .name {
                            display: -webkit-box;
                            flex: 1;
                            text-align: left;
                            font-size: 28rpx;
                            font-weight: 600;
                            line-height: 1.5;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            -webkit-line-clamp: 2;
                            -webkit-box-orient: vertical;
                        }

                        .price {
                            display: inline-block;
                            margin-left: 40rpx;
                            font-size: 28rpx;

                            em {
                                display: inline-block;
                                margin-left: 10rpx;
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
                            width: 100rpx;
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
                    margin-left: 28rpx;
                    color: #9B9B9B;
                    text-align: left;
                }

                .price {
                    display: inline-block;
                    color: $color-text-d;
                    text-align: right;
                    height: 72rpx;
                    line-height: 72rpx;

                    .desc {
                        margin-right: 20rpx;
                        font-size: 24rpx;
                        color: #4a4a4a;
                    }

                    &.right {
                        flex: 1;
                    }

                    &.sale {
                        color: #D0021B;

                        em {
                            display: inline-block;
                            margin-left: 10rpx;
                            font-size: $font-size-large;
                            font-style: normal;
                        }
                    }

                    &.nosale {
                        color: #737373;
                    }
                }

                .icon {
                    display: inline-block;
                    margin-right: 20rpx;
                    font-size: 52rpx;

                    &.icon-pay-wechat {
                        color: #07BF62;
                    }

                    &.icon-alipay {
                        color: #0076A5;
                    }

                    &.icon-fukuan {
                        color: #0076A5;
                    }
                }

                .icon-right {
                    display: inline-block;
                    margin-right: -10rpx;
                    height: 72rpx;
                    line-height: 72rpx;
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
            margin-bottom: 120rpx;
            text-align: center;
            font-size: 20rpx;
            color: $color-text-d;

            .checkbox {
                position: relative;
                display: inline-block;
                width: 32rpx;
                height: 32rpx;
                border: 1rpx solid #ccc;
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
                    top: 6rpx;
                    left: 6rpx;
                    display: inline-block;
                    width: 12rpx;
                    height: 6rpx;
                    border-bottom: 4rpx solid $color-text-w;
                    border-left: 4rpx solid $color-text-w;
                    transform: rotate(-45deg);
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
        padding: 0 30rpx;
        box-sizing: border-box;
        height: 112rpx;
        line-height: 112rpx;
        text-align: right;
        background-color: $color-background-white;
        box-shadow: 0 4rpx 24rpx 0 rgba(235, 232, 232, 0.5);
        z-index: 99;

        &.no-price {
            padding: 16rpx 30rpx;

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
            margin-left: 40rpx;
            width: 240rpx;
            height: 80rpx;
            line-height: 80rpx;
            text-align: center;
            font-size: $font-size-medium-x;
            color: $color-background-white;
            background: $color-theme;
            border-radius: 140rpx;

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
        padding: 40rpx 0 0;
        box-sizing: border-box;

        .pop-header {
            display: block;
            height: 40rpx;
            line-height: 40rpx;
            border-left: 4rpx solid #0076A5;

            .title {
                display: inline-block;
                font-size: $font-size-medium;
                margin-left: 44rpx;
                color: $color-text-d;
            }

            .icon {
                display: inline-block;
                padding: 20rpx 30rpx;
                margin-top: -20rpx;
                font-size: $font-size-large;
                color: $color-text-d;
                float: right;
            }
        }

        .pop-top {
            display: flex;
            padding: 0 40rpx;
            margin-top: 56rpx;

            .input {
                flex: 1;
                font-size: $font-size-medium;
                color: $color-text-l;
                background-color: $color-background-d;
                border-radius: 8rpx;

                >>>.van-field__control {
                    text-align: center;
                }
            }

            .btn {
                display: inline-block;
                margin-left: 30rpx;
                width: 176rpx;
                height: 80rpx;
                line-height: 80rpx;
                font-size: $font-size-medium-x;
                text-align: center;
                color: $color-text-w;
                background-color: $color-theme;
                border-radius: 8rpx;
            }
        }

        .pop-info {
            flex: 1;
            margin-top: 24rpx;
            overflow: hidden;

            >>>.van-tabs {
                display: flex;
                display: -webkit-flex;
                height: 100%;
                flex-flow: column;
                overflow: hidden;

                .van-tabs__wrap {
                    display: block;
                    height: 108rpx;
                    line-height: 108rpx;

                    &.van-hairline--top-bottom {
                        &::after {
                            border-width: 0;
                            border: none;
                        }
                    }

                    .van-tabs__line {
                        bottom: 44rpx;
                        width: 60rpx;
                        height: 8rpx;
                        background-color: $color-theme;
                        border-radius: 8rpx;
                    }

                    .van-tab {
                        .van-ellipsis {
                            font-size: $font-size-medium;
                            font-weight: bold;
                            color: $color-text-d;
                        }

                        &.van-tab--active {
                            font-size: 32rpx;
                            color: $color-theme;
                        }
                    }
                }

                .van-tabs__content {
                    flex: 1;
                    padding: 20rpx 30rpx 0;
                    box-sizing: border-box;
                    overflow: hidden;

                    .van-tab__pane {
                        width: 100%;
                        height: 100%;
                        padding-bottom: 80rpx;
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

.flexible-dialog {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 1000;
    display: flex;
    justify-content: center;
    align-items: center;

    .flexible-dialog-content {
        position: absolute;
        width: 90%;
        background-size: 100% 100%;
        height: 780rpx;
        background-color: #ffffff;
        border-radius: 24rpx;
        display: flex;
        align-items: center;
        justify-content: center;
        flex-direction: column;

        .flexible-cotent-top {
            width: 90%;
            height: 200rpx;
            display: flex;
            align-items: center;
            justify-content: center;

            .flexible-cotent-top-img {
                width: 100%;
                height: auto;
            }
        }

        .flexible-cotent-middle-title {
            width: 85%;
            height: 100rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            margin-top: 40rpx;
            font-size: 36rpx;
            color: #333333;
            font-weight: bold;
        }

        .flexible-cotent-middle-info {
            width: 85%;
            display: flex;
            justify-content: space-between;
            font-size: 30rpx;
            color: #666666;
            letter-spacing: 1.6rpx;
            margin-top: 10rpx;
            line-height: 45rpx;
            text-align: center;
        }

        .flexible-cotent-bottom {
            width: 390rpx;
            height: 86rpx;
            line-height: 86rpx;
            text-align: center;
            display: flex;
            justify-content: center;
            font-size: 36rpx;
            color: #ffffff;
            background-color: #0076A5;
            border-radius: 43rpx;
            margin-top: 80rpx;
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
        padding: 16rpx 32rpx;

        .item-box {
            margin-bottom: 16rpx;

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
            margin-bottom: 16rpx;
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
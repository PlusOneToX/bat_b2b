<template>
    <view class="orderDetail">
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar left-icon="left" :border="false" @clickLeft="back" dark :fixed="false" background-color="transparent" color="#ffffff" status-bar title="订单详情" />
        <view class="top_bg_box" :style="{marginTop: globalData.navHeight+'rpx'}">
            <!-- #endif -->
            <!-- #ifdef H5 -->
            <view class="top_bg_box" :style="{marginTop: globalData.navHeight+'rpx'}">
                <view class="h5_nav_bar">
                    <view style="width: 30rpx;" @click="back">
                        <uni-icons type="back" size="24" color="#ffffff"></uni-icons>
                    </view>
                    <view>订单详情</view>
                    <view style="width: 30rpx;"></view>
                </view>
                <!-- #endif -->
                <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>
                <!-- #ifdef H5 -->
                <view class="decoration1"></view>
                <!-- #endif -->
                <!-- #ifdef MP-WEIXIN -->
                <view class="decoration"></view>
                <!-- #endif -->
                <view class="the_order_status_box">
                    <view style="display: flex;">
                        <view class="status_img">
                            <image class="status_img_txt" src="../../static/images/order.png"></image>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="(orderCustomerData.frontOrderStatus == 1&&orderCustomerData.payWay!=4)">
                            <view class="status_name">待付款</view>
                            <view class="status_describe">您的订单已提交，请尽快完成支付</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus == 7&&platform!=7">
                            <view class="status_name">待审核</view>
                            <view class="status_describe">您的订单已提交，请等待审核通过</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus==7&&platform==7">
                            <view class="status_name">待发货</view>
                            <view class="status_describe">您的订单已支付，请耐心等待仓库发货</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus == 2">
                            <view class="status_name">待发货</view>
                            <view class="status_describe">您的订单已支付，请耐心等待仓库发货</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus == 4">
                            <view class="status_name">已发货</view>
                            <view class="status_describe">您的订单已发货，请注意签收商品</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus == 5">
                            <view class="status_name">已关闭</view>
                            <view class="status_describe">您的订单已关闭，请重新下单</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="orderCustomerData.frontOrderStatus == 6">
                            <view class="status_name">已完成</view>
                            <view class="status_describe">您的订单已完成，请注意签收商品</view>
                        </view>
                        <view style="margin-left: 30rpx;" v-if="(orderCustomerData.frontOrderStatus == 8)">
                            <view class="status_name">已拒绝</view>
                            <view class="status_describe">您的订单已拒绝，请重新下单</view>
                        </view>
                    </view>
                    <view></view>
                </view>
                <view class="shipping_address_box">
                    <view class="shipping_address_title_box">
                        <image class="shipping_address_icon" src="../../static/images/url2.png"></image>
                        <view>{{ orderDelivery.userName }}</view>
                        <view>{{ orderDelivery.mobile }}</view>
                    </view>
                    <view class="address">
                        {{ orderDelivery.countryName }}
                        {{ orderDelivery.provinceName }}
                        {{ orderDelivery.cityName }}
                        {{ orderDelivery.districtName ? orderDelivery.districtName : "" }}
                        {{ orderDelivery.address }}
                    </view>
                </view>
            </view>
            <view class="orderDetails-inform">
                <view class="goods_meu_list_box" v-for="(item, oIndex) in orderDetail.orderInfoDetailGoods" :key="oIndex">
                    <image @tap.stop="preview(item.orderGoodsDiy.previewImage)" mode="aspectFit" class="goods_meu_list_imageurl" :src="item.orderGoodsDiy.previewImage"></image>
                    <view class="commodity_information_box">
                        <view class="order_goods_name">{{item.orderGoods.itemName}}</view>
                        <!-- <view class="order_goods_itemcode">
                            <span style="color: #333;">编/条码：</span>{{item.orderGoods.itemCode}}/{{item.orderGoods.barCode}}
                        </view> -->
                        <view class="order_goods_specifications_box">
                            <view v-if="item.orderGoodsDiy && item.orderGoodsDiy.materialName">
                                <span style="color: #333;">材质：</span> {{ item.orderGoodsDiy.materialName }}
                            </view>
                        </view>
                        <view class="order_goods_specifications_box">
                            <view v-if="item.orderGoodsDiy && item.orderGoodsDiy.modelName">
                                <span style="color: #333;">机型：</span> {{ item.orderGoodsDiy.modelName }}
                            </view>
                        </view>
                        <view class="order_goods_specifications_box">
                            <view class="actual_price">￥{{(item.orderGoodsCustomerCost.salePrice)}}</view>
                            <view class="item_count">x{{  item.orderGoods && item.orderGoods.itemCount? item.orderGoods.itemCount: 0}}</view>
                        </view>
                    </view>
                </view>
                <view class="orderDetails-inform-line">
                    <view class="inform-line">
                        <view>数量总计</view>
                        <view>{{totalCount}}件</view>
                    </view>

                </view>
            </view>
            <view class="orderDetails-inform">
                <view class="price_class_box">
                    <view>合计金额</view>
                    <view class="total_amount">¥ {{orderCustomerCost.goodsAmount.toFixed(2) }} </view>
                </view>
                <view class="price_class_box">
                    <view>配送费用</view>
                    <view class="total_amount" style="text-align: right;">¥{{ deliveryFee }}
                    </view>
                </view>

                <view class="price_class_box">
                    <view>优惠金额</view>
                    <view class="deductible_amount">
                        -¥{{ (orderCustomerCost.orderCouponAmount + deliveryFee )}}
                    </view>
                </view>
                <view class="price_class_box">
                    <view>应付金额</view>
                    <view class="amount_payable">¥ {{orderCustomerCost.payAmount.toFixed(2) }}</view>
                </view>
            </view>
            <view class="orderDetails-goodsInform">
                <view><text class="the_title">订单编号：</text><text>{{  orderInfo.orderNo }}</text></view>
                <view><text class="the_title">下单时间：</text><text>{{  orderInfo.createTime  }}</text></view>
                <view>
                    <text class="the_title">支付方式：</text>
                    <text v-if="orderCustomerData.payWay == 1">支付宝</text>
                    <text v-if="orderCustomerData.payWay == 2">微信</text>
                    <text v-if="orderCustomerData.payWay == 3">区间结算</text>
                    <text v-if="orderCustomerData.payWay == 4">线下转账</text>
                    <text v-if="orderCustomerData.payWay == 5">余额支付</text>
                    <text v-if="orderCustomerData.payWay == 6">快钱支付</text>
                </view>
                <view><text class="the_title">店铺编号：</text><text>{{orderCustomerData.shopCode||'-'}}</text></view>
                <view><text class="the_title">店铺名称：</text><text>{{orderCustomerData.shopName||'-'}}</text></view>
            </view>
            <view>
                <view class="add-address" v-if="orderCustomerData.frontOrderStatus == 2||orderInfo.cancelFlag == 1||orderCustomerData.frontOrderStatus === 4||orderCustomerData.frontOrderStatus === 6||(orderCustomerData.frontOrderStatus === 1 && time&&(orderCustomerData.payWay!=4))||(orderCustomerData.frontOrderStatus === 1 && time)">
                    <text class="list_btn" v-if="orderCustomerData.frontOrderStatus === 4||orderCustomerData.frontOrderStatus === 6" @click.stop="toMyParcel()">查看物流</text>
                    <!-- <text class="list_btn" @click.stop="tohome()">首页</text> -->
                    <!-- <text class="list_btn" @click.stop="tohome()">清理缓存</text> -->
                    <text class="btn-pay" @click="submitOrder" v-if="orderCustomerData.frontOrderStatus === 1 && time&&(orderCustomerData.payWay!=4)">立即支付</text>
                    <text class="btn-cancel" v-if="(orderCustomerData.frontOrderStatus === 1 && time)||orderInfo.cancelFlag==1" @click="cancelOrder">取消订单</text>
                    <view class="inform-line" v-for="theme in themeList" :key="theme.id" v-if="(orderCustomerData.frontOrderStatus == 2||orderCustomerData.frontOrderStatus == 4||orderCustomerData.frontOrderStatus == 6)&&platform==7">
                        <view></view>
                        <view @click="handleUrl(theme.themeUrl)" class="list_btn">主题下载</view>
                    </view>
                </view>
            </view>
            <!-- <view class="add-address" v-if="(orderCustomerData.frontOrderStatus == 2||orderCustomerData.frontOrderStatus == 4||orderCustomerData.frontOrderStatus == 6)&&platform==7">
                <view class="inform-line" v-for="theme in themeList" :key="theme.id">
                    <view></view>
                    <view @click="handleUrl(theme.themeUrl)" class="list_btn">主题下载</view>
                </view>
            </view> -->
            <uni-popup ref="message1" type="dialog">
                <uni-popup-dialog type="info" cancelText="我再想想" confirmText="确认取消" title="温馨提示" content="您确定要取消该订单吗?" @confirm="orderConfirm"></uni-popup-dialog>
            </uni-popup>
            <uni-popup ref="message" type="dialog">
                <uni-popup-dialog type="info" cancelText="返回" confirmText="确认" title="提醒" content="此主题只有限定机型才可使用，若无法使用请联系客服。" @confirm="onExportJSON"></uni-popup-dialog>
            </uni-popup>
        </view>
</template>

<script>
import api from "common/js/allApi.js";
import { formatDate, countDown, getLocalStorageItem } from "common/js/common";
import { aliPay } from "common/js/pay";
export default {
    name: "orderDetail",
    data() {
        return {
            order_status: 0,
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
            curVersion: "",
            userNo: "",
            showPrice: true, // 是否显示价格
            showTabbar: true, // 是否显示底部tab
            orderTime: 0, // 订单失效时间
            deliveryFee: 0, // 运费
            pageFlag: "", // 页面标识
            totalCount: 0,
            globalData: {
                statusBarHeight: 0, // 状态导航栏高度
                navHeight: 0, // 总体高度
                navigationBarHeight: 0, // 导航栏高度(标题栏高度)
            },
            ThemeUrlImage: '',
            platform: 0
        };
    },
    created() {

    },
    onLoad(parameter) {
        // #ifdef H5
        this.platform = uni.getStorageSync("platform");
        if (this.platform == 7) {
            let userId = uni.getStorageSync("userId");
            if (userId == null || userId == undefined || userId == "") {
                uni.setStorageSync("orderId", parameter.id);
                // 未登录跳转登录
                uni.reLaunch({
                    url: '/good_pages/login/login?platform=7&distributorId=2529&sa=2'
                });
            }
        }
        // #endif
        var ratio = uni.getStorageSync("ratio") * -1;//比例
        // #ifdef MP-WEIXIN
        // 状态栏高度
        this.globalData.statusBarHeight = uni.getSystemInfoSync().statusBarHeight
        // 获取微信胶囊的位置信息 width,height,top,right,left,bottom
        const custom = uni.getMenuButtonBoundingClientRect()
        // 导航栏高度(标题栏高度) = 胶囊高度 + (顶部距离 - 状态栏高度) * 2
        this.globalData.navigationBarHeight = custom.height + (custom.top - this.globalData.statusBarHeight) * 2
        // 总体高度 = 状态栏高度 + 导航栏高度
        this.globalData.navHeight = (this.globalData.navigationBarHeight + this.globalData.statusBarHeight + 4) * ratio;
        // #endif

        this.order_status = parameter.frontOrderStatus;
        this.orderId = parameter.id;
        this.distributorId = uni.getStorageSync("distributorId");
        // 获取订单详情
        this.getOrderDetail();
        // 获取订单失效时间
        this.getOrderTime();
    },
    methods: {
        tohome() {
            // uni.switchTab({
            //     url: '/src/components/index/index'
            // });
            uni.setStorageSync("userId", '')
        },
        //预览图片
        preview(img) {
            uni.previewImage({
                urls: [img],
            });
        },
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
        // 我的包裹
        toMyParcel() {
            uni.navigateTo({
                url: "./myParcel?id=" + this.orderId,
            });
        },
        // 获取订单详情
        getOrderDetail() {
            this.$api.get(this, api.getOrderDetail, { id: this.orderId }).then((res) => {
                if (res.success) {
                    this.orderDetail = res.data;
                    this.orderInfo = res.data.orderInfo;
                    this.payType = res.data.orderCustomerData.payWay; // 2 微信，1 支付宝
                    this.orderCustomerData = res.data.orderCustomerData;
                    console.log("订单详情：", this.orderCustomerData);
                    if (this.orderCustomerData.frontOrderStatus === 1) {
                        // 未付款
                        this.getTimer();
                    }
                    // 物流单号
                    if (this.orderDetail.orderDeliverDetail && this.orderDetail.orderDeliverDetail.length > 0) {
                        this.orderDeliverBill = this.orderDetail.orderDeliverDetail[0].orderDeliverBill;
                        // 已签收状态，核销订单
                        let deliveryStatus = this.orderDeliverBill.logisticsStatus;
                        if (Number(this.distributorId) === 4378 && Number(deliveryStatus) === 3) {
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
                        this.totalCount += item.orderGoods.itemCount;
                        if (item.orderGoodsDiy) {
                            this.pictureIds += item.orderGoodsDiy.pictureId;
                            if (idx < this.orderDetail.orderInfoDetailGoods.length - 1) {
                                this.pictureIds += ",";
                            }
                        }
                        if (item.orderGoodsCustomerCost && Number(item.orderGoodsCustomerCost.couponMethod) === 3) {
                            // 兑换
                            if (Number(item.orderGoodsCustomerCost.deliveryFeeFlag) === 1) {
                                // 收邮费
                                this.deliveryFee += item.orderGoodsCustomerCost.actualPrice;
                            }
                        }
                    });

                    let platform = uni.getStorageSync("platform");
                    if (platform === 7 && this.pictureIds != 0 && this.pictureIds != 999999) {
                        this.getThemeUrl();
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
                case 8:
                    return "已拒绝";
            }
        },
        testApp(url, openUrl) {
            // 已经安装App，下面的this.url是需要跳转到app的schema链接。
            window.location.href = openUrl;
        },
        // 支付
        submitOrder() {
            // #ifdef H5
            let redirectUrl = "https://test.bat.com/diyh5/good_pages/order/orderDetails?id=" + this.orderId;
            if (this.payType == 2) {
                this.$api.post(this, api.handlePayment, {
                    businessType: 1, // 业务类型：1 订单收款
                    customerFlag: 1, // 客户标志：1 C端客户
                    payMethod: "wxpay_h5", // 交易方式
                    orderId: this.orderId,
                    payerId: uni.getStorageSync("userId"),
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
                    }
                });
            } else {
                this.$api.post(this, api.handlePayment, {
                    businessType: 1, // 业务类型：1 订单收款
                    customerFlag: 1, // 客户标志：1 C端客户
                    payMethod: "alipay_wap", // 交易方式
                    orderId: this.orderId,
                    payerId: uni.getStorageSync("userId"),
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
                    }
                });
            }
            // #endif
            // #ifdef MP-WEIXIN
            this.$api.post(this, api.handlePayment, {
                businessType: 1, // 业务类型：1 订单收款
                customerFlag: 1, // 客户标志：1 C端客户
                payMethod: "wxpay_mini_program", // 交易方式
                orderId: this.orderId,
                payerId: uni.getStorageSync("userId"),
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
                            // 获取订单详情
                            this.getOrderDetail();
                            // 获取订单失效时间
                            this.getOrderTime();
                        },
                        fail: function (err) {
                            uni.showToast({
                                title: '支付失败',
                                icon: "none",
                                duration: 2000,
                            });

                        },
                    });
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000,
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
        orderConfirm() {
            this.$api.put(this, api.handleCancelOrder, {
                id: this.orderId,
                remark: "用户主动取消订单",
            }).then((res) => {
                if (res.success) {
                    uni.navigateTo({
                        url: "../order/orderList",
                    });

                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            });
        },
        // 取消订单
        cancelOrder() {
            this.$refs.message1.open();
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
                case 8:
                    return "已拒绝";
                case 6:
                    return "icon-c-shipped";
            }
        },
        // 核销订单
        handleWriteOffOrder() {
            let ryFlowId = uni.getStorageSync("ryFlowId");

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
                            .then(() => { })
                            .catch((error) => {
                                console.log(error);
                            });
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        getThemeUrl() {
            this.$api.get(this, api.getThemeUrl, {
                idList: this.pictureIds,
            }).then((res) => {
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
        onExportJSON() {
            window.location.href = this.ThemeUrlImage;
        },
        handleUrl(url) {
            this.$refs.message.open();
            this.ThemeUrlImage = url;
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
    },
};
</script>
<style lang="scss" scoped>
.goods_meu_list_box {
    width: 690rpx;
    margin: 0 auto;
    height: 200rpx;
    margin-top: 20rpx;
    display: flex;
    justify-content: space-between;
    align-items: center;
    .goods_meu_list_imageurl {
        height: 200rpx;
        width: 200rpx;
    }
    .commodity_information_box {
        width: 470rpx;
        height: 180rpx;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .order_goods_name {
            width: 470rpx;
            height: 65rpx;
            overflow: hidden;
            word-break: break-all;
            display: -webkit-box;
            text-overflow: ellipsis;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 2;
            color: rgba(16, 16, 16, 1);
            font-size: 24rpx;
            text-align: left;
            font-family: PingFangSC-regular;
        }
        .order_goods_itemcode {
            width: 470rpx;
            overflow: hidden;
            word-break: break-all;
            display: -webkit-box;
            text-overflow: ellipsis;
            -webkit-box-orient: vertical;
            -webkit-line-clamp: 1;
            color: #999999;
            font-size: 22rpx;
            text-align: left;
            font-family: PingFangSC-regular;
        }
        .order_goods_specifications_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 460rpx;
            view {
                color: #999999;
                font-size: 22rpx;
                text-align: left;
                font-family: PingFangSC-regular;
            }
            .actual_price {
                color: rgba(252, 126, 27, 1);
                font-size: 24rpx;
                text-align: left;
                font-family: PingFangSC-semiBold;
            }
            .item_count {
                color: rgba(16, 16, 16, 1);
                font-size: 24rpx;
                text-align: left;
                font-family: PingFangSC-regular;
            }
        }
    }
}
.top_bg_box {
    .h5_nav_bar {
        height: 88rpx;
        width: 750rpx;
        position: absolute;
        z-index: 1;
        line-height: 88rpx;
        color: #ffffff;
        font-size: 26rpx;
        display: flex;
        justify-content: space-between;
    }
    image {
        width: 750rpx;
        height: 490rpx;
        display: block;
    }
    .decoration {
        position: relative;
        height: 100rpx;
        border-radius: 50rpx;
        background-color: #f5f5f5;
        margin-top: -40rpx;
        z-index: 10;
    }
    .decoration1 {
        position: relative;
        height: 100rpx;
        border-radius: 50rpx;
        background-color: #efeff4;
        margin-top: -40rpx;
        z-index: 10;
    }
    .the_order_status_box {
        position: relative;
        height: 160rpx;
        width: 640rpx;
        margin: 0 auto;
        margin-top: -350rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .status_img {
            width: 80rpx;
            height: 80rpx;
            background-color: rgba(255, 255, 255, 1);
            border-radius: 40rpx;
            display: flex;
            justify-content: center;
            align-items: center;
            .status_img_txt {
                width: 60rpx;
                height: 60rpx;
            }
        }
        .status_name {
            color: rgba(255, 255, 255, 1);
            font-size: 36rpx;
            text-align: left;
            font-weight: 600;
            font-family: PingFangSC-regular;
        }
        .status_describe {
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: left;
            font-family: SourceHanSansSC-regular;
            margin-top: 10rpx;
        }
    }
    .shipping_address_box {
        position: relative;
        z-index: 10;
        width: 710rpx;
        height: 190rpx;
        border-radius: 30rpx;
        background-color: rgba(255, 255, 255, 1);
        border: 1rpx solid rgba(239, 239, 239, 1);
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        margin-top: 10rpx;
        margin: 0 auto;
        padding: 30rpx;
        box-sizing: border-box;
        .shipping_address_title_box {
            display: flex;
            align-items: center;
            .shipping_address_icon {
                width: 50rpx;
                height: 50rpx;
            }
            view {
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                text-align: left;
                font-family: PingFangSC-regular;
                margin-left: 30rpx;
                font-weight: 600;
            }
        }
        .address {
            color: rgba(138, 138, 138, 1);
            font-size: 24rpx;
            text-align: left;
            font-family: SourceHanSansSC-regular;
            margin-top: 10rpx;
        }
    }
}
.wuliuIcon {
    font-size: 45rpx;
}
.locationIcon {
    font-size: 45rpx;
    color: #0076a5;
}
.orderDetail {
    padding-bottom: 130rpx;
    font-size: 26rpx;
    .top-order-moudle {
        background: #fff;
        top: 0;
        // position: fixed;
        .top-bg {
            width: 750rpx;
            height: 332rpx;
            // background: url(../../static/img/oderDetail_bg.png);
            background-size: 750rpx 332rpx;
            .top-titleD {
                display: flex;
                align-items: center;
                padding: 30rpx 30rpx 0 30rpx;
                /* #ifdef H5 */
                padding: 15rpx 30rpx 0 30rpx;
                /* #endif*/
                image {
                    width: 55rpx;
                    height: 55rpx;
                }
                view {
                    font-size: 32rpx;
                    font-weight: 400;
                    width: 650rpx;
                    height: 80rpx;
                    line-height: 80rpx;
                    text-align: center;
                }
                /* #ifdef H5 */
                view {
                    height: 88rpx;
                    line-height: 88rpx;
                }
                /*#endif*/
            }
            .topBg-tip {
                display: flex;
                align-items: center;
                justify-content: center;
                // margin-top: 25rpx;
                text {
                    font-size: 40rpx;
                    color: #0076a5;
                    font-weight: 500;
                }
                image {
                    width: 180rpx;
                    height: 180rpx;
                    margin-left: 90rpx;
                }
            }
        }
        .top-logistics {
            display: flex;
            padding: 30rpx 30rpx 50rpx;
            image {
                width: 49rpx;
                height: 49rpx;
            }
            .top-logistics-ifm {
                font-size: 26rpx;
                margin-left: 20rpx;
                .top-logistics-namePhone {
                    text:nth-child(2) {
                        color: #999999;
                        margin-left: 20rpx;
                    }
                }
                .top-logistics-address {
                    margin-top: 10rpx;
                }
            }
        }
        .orderDetail-delivery {
            padding: 50rpx 30rpx 30rpx;
            border-bottom: 1rpx solid rgba(241, 242, 249, 0.5);
            .delivery-infom {
                display: flex;
                align-items: center;

                .icon-233 {
                    color: #0076a5;
                }

                .delivery-cart {
                    width: 45rpx;
                    height: 36rpx;
                }
                .delivery-right {
                    width: 12rpx;
                    height: 22rpx;
                    margin-left: 20rpx;
                }
                view {
                    margin: 0 21rpx;
                }
            }
            .delivery-date {
                font-size: 22rpx;
                font-weight: 400;
                color: #999;
                margin-left: 65rpx;
                margin-top: 10rpx;
            }
        }
    }
    .orderDetails-inform {
        width: 710rpx;
        margin: 0 auto;
        border-radius: 30rpx;
        background: #fff;
        border: 1rpx solid rgba(239, 239, 239, 1);
        padding: 30rpx 0;
        margin-top: 20rpx;
        .price_class_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 660rpx;
            height: 80rpx;
            margin: 0 auto;
            color: rgba(138, 138, 138, 1);
            font-size: 24rpx;
            font-family: PingFangSC-regular;
            .total_amount {
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                font-family: SourceHanSansSC-regular;
            }
            .deductible_amount {
                color: rgba(252, 126, 27, 1);
                font-size: 28rpx;
                font-family: SourceHanSansSC-regular;
            }
            .amount_payable {
                color: rgba(252, 126, 27, 1);
                font-size: 36rpx;
                font-weight: 600;
                font-family: SourceHanSansSC-regular;
            }
        }
        .orderDetails-inform-line {
            .inform-line {
                display: flex;
                align-items: center;
                justify-content: space-between;
                padding: 30rpx 30rpx 0 30rpx;
                color: #999;
                font-size: 24rpx;
                text {
                    font-size: 20rpx;
                }
                .inform-line-num {
                    font-size: 28rpx;
                }
            }
            .inform-total {
                font-weight: bold;
                padding: 30rpx;
                text-align: right;
                .total-num {
                    color: #ed5307;
                }
                .total-text {
                    font-size: 22rpx;
                }
            }
        }
    }
    .orderDetails-goodsInform {
        background: #fff;
        margin: 0 auto;
        border-radius: 30rpx;
        border: 1rpx solid rgba(239, 239, 239, 1);
        margin-top: 20rpx;
        width: 710rpx;
        padding-bottom: 50rpx;
        .remark_box {
            padding: 20rpx 10rpx;
            width: 480rpx;
            border-radius: 30rpx;
            background-color: rgba(247, 247, 247, 1);
            color: rgba(97, 96, 96, 1);
            font-size: 20rpx;
            text-align: center;
            font-family: Arial;
        }
        .the_title {
            color: rgba(138, 138, 138, 1);
            font-size: 24rpx;
            text-align: left;
            font-family: PingFangSC-regular;
        }
        view {
            display: flex;
            align-items: center;
            padding: 50rpx 30rpx 0 30rpx;
            color: #101010;
            font-size: 24rpx;
            text-align: left;
            font-family: PingFangSC-regular;
            text {
                margin-right: 20rpx;
            }
        }
    }

    .add-address {
        width: 750rpx;
        height: 80rpx;
        background-color: #ffffff;
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        position: fixed;
        bottom: 0;
        // padding: 20rpx 0;
        padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
        padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
        text {
            display: block;
            font-size: 28rpx;
            border-radius: 40rpx;
            width: 220rpx;
            height: 60rpx;
            line-height: 60rpx;
            text-align: center;
        }
        .list_btn {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(255, 255, 255, 1);
            color: rgba(97, 96, 96, 1);
            border: 1rpx solid rgba(97, 96, 96, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
            margin-left: 20rpx;
            margin-right: 20rpx;
        }
        .btn-cancel {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(0, 118, 164, 1);
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
            margin-left: 20rpx;
            margin-right: 20rpx;
        }
        .btn-pay {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(252, 126, 27, 1);
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
        }
    }

    .orderDetails-btn {
        height: 100rpx;
        display: flex;
        align-items: center;
        justify-content: flex-end;
        background: #fff;
        border-top: 1rpx solid #f2f3f8;
        position: fixed;
        bottom: 0;
        width: 750rpx;
        padding-top: 20rpx;
        // box-sizing: border-box;
        padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
        padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
        text {
            display: block;
            font-size: 28rpx;
            border-radius: 40rpx;
            width: 220rpx;
            height: 60rpx;
            line-height: 60rpx;
            text-align: center;
        }
        .list_btn {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(255, 255, 255, 1);
            color: rgba(97, 96, 96, 1);
            border: 1rpx solid rgba(97, 96, 96, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
            margin-left: 20rpx;
        }
        .btn-cancel {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(0, 118, 164, 1);
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
            margin-left: 20rpx;
            margin-right: 20rpx;
        }
        .btn-pay {
            width: 160rpx;
            height: 60rpx;
            line-height: 60rpx;
            border-radius: 50rpx;
            background-color: rgba(252, 126, 27, 1);
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
        }
    }
}
</style>

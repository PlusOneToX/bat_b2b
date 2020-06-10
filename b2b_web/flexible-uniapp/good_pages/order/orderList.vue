<template>
    <view class="order">
        <!-- 顶部导航 -->
        <!-- #ifdef H5 -->
        <scroll-view scroll-x="true" class="order-topNav1">
            <!-- #endif -->
            <!-- #ifdef MP-WEIXIN -->
            <scroll-view scroll-x="true" class="order-topNav">
                <!-- #endif -->
                <view class="top-nav">
                    <text v-for="item in typeList" :key="item.id" :class="active == item.id ? 'topNav-hover' : ''" @click="handleClickv(item.id)">{{ item.name }}
                    </text>
                </view>
            </scroll-view>

            <!-- 列表 -->
            <view class="order-list" v-if="orderList.length > 0">
                <view class="order-list-view">
                    <!-- 一模块 -->
                    <template v-for="(item, itemIndex) in orderList">
                        <view class="order-list-line" :key="itemIndex" @click="orderDetail(item.orderInfo.id)">
                            <view class="list-line-top">
                                <text>订单编号： {{item.orderInfo.orderNo}}</text>
                                <!-- <text>下单时间： {{ item.orderInfo.createTime }}</text> -->
                                <text v-if="item.orderCustomerData.frontOrderStatus === 1&&item.orderCustomerData.payWay==4">待审核</text>
                                <text v-else>{{ orderStatus(item.orderCustomerData.frontOrderStatus) }}</text>
                            </view>
                            <view v-if="item.orderInfoDetailGoods.length==1">
                                <view class="list-line-moudle" v-for="(items, itemsIndex) in item.orderInfoDetailGoods" :key="itemsIndex">
                                    <image @tap.stop="preview(items.orderGoodsDiy.previewImage)" mode="aspectFit" :src="(items.orderGoodsDiy.previewImage)+'?x-oss-process=image/resize,h_400,l_400/'" class="list-img"></image>
                                    <view class="line-moudle-center">
                                        <view class="moudle-center-name">
                                            {{items.orderGoods.itemName}}
                                        </view>
                                        <view style="height: 34rpx;"></view>
                                        <view class="moudle-center-color" v-if="
                    items.orderGoods.colorName &&
                    items.orderGoods.colorName != ''">
                                            <span style="color: #333;">颜色：</span>
                                            {{items.orderGoods.colorName ? items.orderGoods.colorName : ""}}
                                        </view>
                                        <view class="moudle-center-color" v-if="
                    items.orderGoods.specsName &&
                    items.orderGoods.specsName != ''">
                                            <span style="color: #333;">规格：</span>{{ items.orderGoods.specsName }}
                                        </view>
                                        <view class="moudle-center-color" v-if="items.orderGoodsDiy && items.orderGoodsDiy.materialName">
                                            <span style="color: #333;">材质：</span>{{ items.orderGoodsDiy.materialName }}
                                        </view>
                                        <view class="moudle-center-color" v-if="items.orderGoodsDiy && items.orderGoodsDiy.modelName">
                                            <span style="color: #333;">机型：</span>{{ items.orderGoodsDiy.modelName }}
                                        </view>
                                    </view>
                                    <view class="list-line-btm">
                                        <view class="line-btm-price">
                                            ￥{{item.orderCustomerCost.payAmount}}
                                        </view>
                                        <view class="line-btm-num">
                                            共计{{item.totalCount}}件
                                        </view>
                                    </view>
                                </view>
                            </view>
                            <view class="order_list_box" v-else>
                                <scroll-view class="list_line_moudle" scroll-x="true">
                                    <image mode="aspectFit" v-for="(items, itemsIndex) in item.orderInfoDetailGoods" :key="itemsIndex" :src="items.orderGoodsDiy.previewImage" class="list-img"></image>
                                </scroll-view>
                                <view class="order_information_box">
                                    <view class="the_total_amount">¥ {{item.orderCustomerCost.payAmount }}</view>
                                    <view class="the_number_of">共计{{item.totalCount}}件</view>
                                </view>
                            </view>
                            <view class="operation_border_box">
                                <view></view>
                                <view class="operation_border_mew">
                                    <view class="list-btn buy" v-if="(item.orderCustomerData.frontOrderStatus === 1&&item.orderCustomerData.payWay!=4)">
                                        待支付{{ item.dayTime||'' }}
                                    </view>
                                    <view class="list-btn immediately" v-if="item.orderCustomerData.frontOrderStatus === 1&&item.orderCustomerData.payWay==4">
                                        待审核 {{ item.dayTime }}
                                    </view>
                                </view>
                            </view>
                        </view>
                    </template>
                </view>
                <!-- <view class="no-more" v-show="orderList.length < totalPage" @click="handleMore">点击加载更多</view> -->
                <view class="no-more" v-show="orderList.length >= totalPage">没有更多了</view>
                <!-- </scroll-view> -->
            </view>
            <view v-if="orderList.length == 0" class="order-noData-line">
                <image src="../../static/images/no-dataIcon.png"></image>
                <view>暂无数据</view>
            </view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
import { countDown } from "common/js/common";
export default {
    data() {
        return {
            typeList: [
                { id: 0, name: "全部" },
                { id: 1, name: "待付款" },
                { id: 2, name: "待发货" },
                { id: 4, name: "待收货" },
                { id: 6, name: "已完成" },
                { id: 5, name: "已关闭" },
            ],
            // 荣耀订单状态
            orderTabsRy: [
                { id: 0, name: "全部" },
                { id: 2, name: "待发货" },
                { id: 4, name: "待收货" },
                { id: 6, name: "已完成" },
            ],
            title: "我的订单",
            userId: 0, // 用户ID
            phone: "", // 用户手机号
            distributorId: "", // 分销商
            orderTabs: [], // 订单状态
            orderList: [], // 订单列表
            active: 0, // 默认订单状态
            page: 1, // 数量倍数
            size: 10, // 页显示数量
            totalPage: 0, // 订单总数
            temp: null, // 倒计时
            isLoading: true, // 下拉刷新加载
            msg: "",
            message: "", // loading提示
            curVersion: "",
            userNo: "",
            showPrice: true, // 是否显示价格
            showTabbar: true, // 是否显示底部tab
            orderTime: 0, // 订单失效时间
            pageFlag: "", // 页面标识
        };
    },
    onPullDownRefresh() {
        this.handleClickv(this.active);
    },
    onReachBottom() {
        if (this.orderList.length < this.totalPage) {
            this.handleMore();
        }
    },
    onLoad(option) {
        this.active = option.active || 0;
        this.distributorId = uni.getStorageSync("distributorId");
        // 获取用户信息
        this.userId = uni.getStorageSync("userId");// 9441;//
        if (Number(this.distributorId) === 4378) {
            // 荣耀
            this.typeList = this.orderTabsRy; // 订单状态
            this.showPrice = false; // 是否显示价格
        }
        if (this.userId != '') {
            // 获取全部订单
            uni.showLoading({
                title: '加载中'
            });
            setTimeout(() => {
                this.getOrderList();
                // 获取订单失效时间
                this.getOrderTime();
            }, 500);
        }
        else {
            uni.navigateTo({
                url: '../login/login',
            });
        }
    },
    methods: {
        //预览图片
        preview(img) {
            uni.previewImage({
                urls: [img],
            });
        },
        // 加载更多
        handleMore() {
            uni.showLoading({
                title: '加载中'
            });
            this.page++;
            this.size = this.page * this.size;
            this.getOrderList();
        },
        // 获取订单列表
        getOrderList(size) {
            // 获取订单列表
            this.$api.get(this, api.getOrderList, {
                frontOrderStatus: this.active,
                customerId: this.userId,
                page: 1,
                size: this.size,
                shopCode: uni.getStorageSync("shopCode"),
            }).then((res) => {
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
                    uni.hideLoading();
                    if (this.orderList.length > 0) {
                        this.timer();
                    }
                } else {
                    uni.hideLoading();
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
                uni.stopPullDownRefresh()
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
                case 7:
                    if (uni.getStorageSync("platform") == 7) {
                        return "待发货";
                    } else {
                        return "待审核";
                    }

                case 8:
                    return "已拒绝";
            }
        },

        // 跳转订单详情
        orderDetail(id) {
            // this.$router.push({
            //     path: "/orderDetail",
            //     query: { id: id, pageFlag: this.pageFlag },
            // });
            uni.navigateTo({
                url: "../order/orderDetails?id=" + id,
            });
        },
        // 切换订单状态
        handleClickv(val) {
            uni.showLoading({
                title: '加载中'
            });
            this.page = 1;
            this.active = val;
            this.getOrderList();
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
                this.getOrderList();
                uni.showToast({
                    icon: 'none',
                    title: '刷新成功',
                    duration: 2000
                });
                this.isLoading = false;
            }, 1000);
        },
    }
};
</script>

<style lang="scss" scoped>
.no-more {
    text-align: center;
    height: 150rpx;
    line-height: 150rpx;
}
.operation_border_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-sizing: border-box;
    padding: 0 20rpx;
    margin-top: 20rpx;
    .more_and_more {
        color: rgba(97, 96, 96, 1);
        font-size: 24rpx;
        text-align: left;
        font-family: PingFangSC-regular;
    }
    .operation_border_mew {
        display: flex;
        align-items: center;
        .buy {
            color: rgba(0, 118, 164, 1);
            border: 1rpx solid rgba(0, 118, 164, 1);
        }
        .again {
            color: rgba(97, 96, 96, 1);
            border: 1rpx solid rgba(97, 96, 96, 1);
        }
        .immediately {
            color: rgba(252, 126, 27, 1);
            border: 1rpx solid rgba(252, 126, 27, 1);
        }
        .list-btn {
            padding: 0 10rpx;
            width: auto;
            height: 40rpx;
            line-height: 40rpx;
            border-radius: 100rpx;
            background-color: rgba(255, 255, 255, 1);
            font-size: 20rpx;
            text-align: center;
            font-family: Arial;
            font-weight: 600;
            margin-left: 20rpx;
        }
    }
}
.order_list_box {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20rpx;
    .order_information_box {
        width: 180rpx;
        text-align: right;
        box-sizing: border-box;
        padding-right: 20rpx;
        .the_total_amount {
            color: rgba(252, 126, 27, 1);
            font-size: 24rpx;
            font-family: PingFangSC-regular;
        }
        .the_number_of {
            color: rgba(16, 16, 16, 1);
            font-size: 20rpx;
            font-family: PingFangSC-regular;
            margin-top: 10rpx;
        }
    }
    .list_line_moudle {
        width: 510rpx;
        white-space: nowrap;
        margin-left: 10rpx;
        image {
            width: 160rpx;
            height: 160rpx;
            margin-right: 10rpx;
        }
    }
}
.order {
    color: #333333;
    .top-title {
        padding: 0 30rpx;
        display: flex;
        align-items: center;
        background: #fff;

        .search-view {
            display: flex;
            align-items: center;
            // justify-content: center;
            margin-left: 30rpx;
            border-bottom: 2rpx solid #0076a5;
            width: 450rpx;
            input {
                width: 400rpx;
                padding: 10rpx;
                font-size: 26rpx;
                margin-left: 20rpx;
            }
        }
        image {
            width: 55rpx;
            height: 55rpx;
        }
        .top-textTitle {
            width: 650rpx;
            text-align: center;
            color: #333333;
            font-size: 32rpx;
            font-weight: 400;
        }
    }
    scroll-view ::-webkit-scrollbar {
        display: none;
        width: 0;
        height: 0;
        color: transparent;
    }
    .order-topNav {
        padding: 10rpx 0;
        background: #fff;
        border-top: 1px solid #f2f3f8;
        box-sizing: border-box;
        position: fixed;
        z-index: 900;
        top: 0;
    }
    .order-topNav1 {
        padding: 10rpx 0;
        background: #fff;
        border-top: 1px solid #f2f3f8;
        box-sizing: border-box;
        position: fixed;
        z-index: 900;
        top: 100;
    }
    .uni-scroll-view {
        padding: 0 30rpx;
    }
    .top-nav {
        display: flex;

        text {
            display: block;
            padding: 0 30rpx;
            color: #999999;
            font-size: 24rpx;
            font-weight: 300;
            white-space: nowrap;
            height: 80rpx;
            line-height: 80rpx;
        }
        .topNav-hover {
            position: relative;
            color: #0076a5;
            font-size: 28rpx;
            font-weight: 600;
            &::after {
                content: "";
                position: absolute;
                bottom: 0;
                left: 50%;
                width: 100rpx;
                height: 6rpx;
                background: #0076a5;
                border-radius: 6rpx;
                transform: translateX(-50%);
            }
        }
    }

    .order-list {
        // height: (100vh-16);
        // background-color: #efeff4;
        padding-top: 30rpx;
        .order-list-view {
            margin-top: 100rpx;
            .order-list-line {
                background: #fff;
                border: 1rpx solid rgba(239, 239, 239, 1);
                border-radius: 30rpx;
                margin: 0 auto;
                margin-top: 20rpx;
                padding-bottom: 20rpx;
                width: 690rpx;

                .list-line-top {
                    height: 83rpx;
                    line-height: 83rpx;
                    padding: 0 30rpx;
                    display: flex;
                    justify-content: space-between;
                    font-size: 26rpx;
                    border-bottom: 1rpx solid rgba(241, 242, 249, 0.5);
                    text:nth-child(2) {
                        color: #0076a5;
                    }
                }
                .list-line-moudle {
                    position: relative;
                    display: flex;
                    padding: 0 30rpx;
                    margin-top: 40rpx;
                    .list-img {
                        width: 160rpx;
                        height: 160rpx;
                        border-radius: 10rpx;
                        overflow: hidden;
                    }
                    .line-moudle-center {
                        width: 380rpx;
                        margin-left: 20rpx;
                        .moudle-center-name {
                            font-size: 26rpx;
                            font-weight: bold;
                            margin-bottom: 20rpx;
                            text-overflow: -o-ellipsis-lastline;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            display: -webkit-box;
                            -webkit-line-clamp: 2;
                            line-clamp: 2;
                            -webkit-box-orient: vertical;
                        }
                        .moudle-center-color {
                            font-size: 22rpx;
                            color: #999999;
                            margin-top: 5rpx;
                        }
                    }
                    .list-line-btm {
                        position: absolute;
                        right: 30rpx;
                        margin-left: 20rpx;
                        text-align: right;
                        .line-btm-price {
                            color: rgba(252, 126, 27, 1);
                            font-size: 24rpx;
                            font-family: PingFangSC-regular;
                        }
                        .line-btm-num {
                            color: rgba(16, 16, 16, 1);
                            font-size: 20rpx;
                            font-family: PingFangSC-regular;
                            margin-top: 10rpx;
                        }
                    }
                }
                .total-num {
                    font-size: 26rpx;
                    font-weight: bold;
                    margin-top: 40rpx;
                    text-align: right;
                    padding: 0 30rpx;
                    text {
                        color: #ed5307;
                        padding: 0 15rpx;
                    }
                }
            }
        }
    }

    .order-noData-line {
        image {
            width: 311rpx;
            height: 300rpx;
        }
        padding-top: 200rpx;
        font-size: 30rpx;
        color: #666;
        text-align: center;
        font-weight: 400rpx;
        view {
            margin-top: 47rpx;
        }
    }
}
</style>

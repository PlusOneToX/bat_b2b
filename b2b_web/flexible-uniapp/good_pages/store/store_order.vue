<template>
    <page-meta :page-style="'overflow:'+(show?'hidden':'visible')">
        <view class="order">
            <!-- 顶部导航 -->
            <scroll-view scroll-x="true" class="order-topNav">
                <view class="top-nav">
                    <text v-for="item in orderTabsAll" :key="item.id" :class="active == item.id ? 'topNav-hover' : ''" @click="handleClickv(item.id)">{{ item.name }}
                    </text>
                </view>
            </scroll-view>
            <!-- 列表 -->
            <view class="order-list" v-if="orderList.length > 0">
                <view class="order-list-view">
                    <!-- 一模块 -->
                    <template v-for="(item, itemIndex) in orderList">
                        <view class="order-list-line" :key="itemIndex" @click="orderDetail(item.id,item.frontOrderStatus)">
                            <view class="list-line-top">
                                <text>订单编号： {{item.orderNo}}</text>
                                <text v-if="(item.frontOrderStatus == 1||item.frontOrderStatus == 7)&&item.payWay==4">待审核</text>
                                <text v-else>{{orderStatus(item.frontOrderStatus) }}</text>
                            </view>
                            <view v-if="item.goodss.length==1">
                                <view class="list-line-moudle" v-for="(items, itemsIndex) in item.goodss" :key="itemsIndex">
                                    <image mode="aspectFit" @tap.stop="preview(items.imageUrl)" :src="items.imageUrl" class="list-img"></image>
                                    <view class="line-moudle-center">
                                        <view class="moudle-center-name">
                                            {{items.itemName}}
                                        </view>
                                        <view style="height: 34rpx;"></view>

                                        <view class="moudle-center-color" v-if="items.colorName"> <span style="color: #333;">材质：</span>{{ items.colorName }}
                                        </view>
                                        <view class="moudle-center-color" v-if="items.specsName"> <span style="color: #333;">机型：</span>{{ items.specsName }}
                                        </view>
                                    </view>
                                    <view class="list-line-btm">
                                        <view class="line-btm-price">
                                            ￥{{items.salePrice}}
                                        </view>
                                        <view class="line-btm-num">
                                            共计{{items.itemCount}}件
                                        </view>
                                    </view>
                                </view>
                            </view>
                            <view class="order_list_box" v-else>
                                <scroll-view class="list_line_moudle" scroll-x="true">
                                    <image @tap.stop="preview(items.imageUrl)" mode="aspectFit" v-for="(items, itemsIndex) in item.goodss" :key="itemsIndex" :src="items.imageUrl" class="list-img"></image>
                                </scroll-view>
                                <view class="order_information_box">
                                    <view class="the_total_amount">¥ {{item.payAmount }}</view>
                                    <view class="the_number_of">共计{{item.totalCount}}件</view>
                                </view>
                            </view>
                            <view class="operation_border_box">
                                <view class="distributor_box" style="font-size: 24rpx;">
                                    <view class="distributor_icon_box">
                                        <image src="../../static/icons/my/order/order_form.png"></image>
                                    </view>
                                    {{item.shopName}}
                                </view>
                                <view></view>
                            </view>
                            <!-- 分销员列表 -->
                            <view class="operation_border_box" v-for="(user,ind) in item.commissions" :key="ind" v-if="user.staffType==1&&(customerShopCheck==1)">
                                <view class="distributor_box">
                                    <view class="distributor_icon_box">
                                        <image src="../../static/icons/my/order/distributor.png"></image>
                                    </view>
                                    <view>{{user.staffType==1?'店长':'导购员'}}</view>
                                    ：{{user.staffName}}
                                </view>
                                <view class="distributor_box">
                                    <view class="distributor_icon_box">
                                        <image src="../../static/icons/my/order/finance.png"></image>
                                    </view>
                                    奖金：<view class="bonus">￥{{user.amount}}</view>
                                </view>
                            </view>
                            <view class="operation_border_box" v-for="(user,ind) in item.commissions" :key="ind" v-if="user.staffType==2&&(customerShopCheck==2)"">
                                <view class=" distributor_box">
                                <view class="distributor_icon_box">
                                    <image src="../../static/icons/my/order/distributor.png"></image>
                                </view>
                                <view>{{user.staffType==1?'店长':'导购员'}}</view>
                                ：{{user.staffName}}
                            </view>
                            <view class="distributor_box">
                                <view class="distributor_icon_box">
                                    <image src="../../static/icons/my/order/finance.png"></image>
                                </view>
                                奖金：<view class="bonus">￥{{user.amount}}</view>
                            </view>
                        </view>
                        <view class="operation_border_box" v-if="shopOrder==1">
                            <view></view>
                            <view class="operation_border_mew" v-if="(item.frontOrderStatus != 7)">
                                <view v-if="staffType==1&&item.commissions==undefined&&item.frontOrderStatus==2" class="list-btn buy" @click.stop="allocation(item.id)">分配订单</view>
                                <view v-if="staffType==1&&item.commissions!=undefined&&item.frontOrderStatus==2" class="list-btn buy" @click.stop="allocation(item.id)">重新分配订单</view>
                                <view class="list-btn immediately" v-if="customerShopCheck==1&&item.payWay==4&&item.frontOrderStatus==1" @click.stop="auditFun(item.id)">
                                    审核订单 {{ item.dayTime||'' }}
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
<uni-popup ref="message" type="dialog" @maskClick="close">
    <uni-popup-dialog type="info" cancelText="拒绝" confirmText="确定" title="提示" content="确定通过审核？" @confirm="through" @close="refused"></uni-popup-dialog>
</uni-popup>
<uni-popup ref="popup" type="center" @maskClick="closeAllocation">
    <view class="allocation_box">
        <view class="allocation_head_title">
            <view class="head_title">分配订单</view>
            <view @click="closeAllocation">
                <uni-icons type="closeempty" size="24"></uni-icons>
            </view>
        </view>
        <scroll-view class="distributor_list_box" scroll-y="true">
            <radio-group @change="radioChange">
                <view class="distributor_list_meu" v-for="item in allocationUser" :key="item.id">
                    <radio color="#5677FC" style="transform:scale(0.9)" :value="item.staffCode" :checked="item.staffCode === allocationUserId" />
                    <view>
                        <image src="../../static/images/index_login.png"></image>
                    </view>
                    <view>{{item.name}}</view>
                </view>
            </radio-group>
        </scroll-view>
        <view class="bit_ok" @click="allocationOk">确定分配</view>
    </view>
</uni-popup>
</view>
</page-meta>

</template>
  <script type="text/ecmascript-6">
import api from "common/js/allApi.js";
import { countDown } from "common/js/common";

export default {
    data() {
        return {
            show: false,
            isaudit: false,
            allCheckFlag: false, //全选
            title: "店铺订单",
            userId: 0, // 用户ID
            phone: "", // 用户手机号
            distributorId: "", // 分销商
            orderTabs: [], // 订单状态
            // 所有订单状态
            orderTabsAll: [
                {
                    id: 0,
                    name: "全部",
                },
                {
                    id: 7,
                    name: "待审核",
                },
                {
                    id: 2,
                    name: "已通过",
                },
                {
                    id: 6,
                    name: "已完成",
                },
                {
                    id: 8,
                    name: "已拒绝",
                },
                {
                    id: 5,
                    name: "已关闭",
                },
            ],
            // 荣耀订单状态
            orderTabsRy: [
                {
                    id: 0,
                    name: "全部",
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
            userNo: "",
            showPrice: true, // 是否显示价格
            showTabbar: true, // 是否显示底部tab
            orderTime: 0, // 订单失效时间
            pageFlag: "", // 页面标识
            customerShopCheck: 0,//店铺审核：0 否 1.是;无返回或为零都为否
            ids: '',
            allocationId: '',
            allocationUser: [],
            allocationUserId: '',
            shopOrder: 1,//是否是店铺订单
            staffType: 3,
        };
    },
    onPullDownRefresh() {
        this.handleClickv(this.active);
    },
    created() {
    },
    onLoad(open) {
        this.shopOrder = open.shopOrder;
        if (this.shopOrder == 1) {
            uni.setNavigationBarTitle({
                title: '店铺订单'
            });
        } else {
            uni.setNavigationBarTitle({
                title: '分销订单'
            });
        }
        this.staffType = uni.getStorageSync("staffType");  //uni.getStorageSync("customerShopCheck");
        this.customerShopCheck = uni.getStorageSync("customerShopCheck");
        this.userNo = uni.getStorageSync("userNo");
        // 获取用户信息
        this.userId = uni.getStorageSync("userId");
        this.phone = uni.getStorageSync("phone");
        this.distributorId = uni.getStorageSync("distributorId");;

        if (Number(this.distributorId) === 4378) {
            // 荣耀
            this.orderTabs = this.orderTabsRy; // 订单状态
            this.showPrice = false; // 是否显示价格
            this.showTabbar = false; // 是否显示底部tab
        } else {
            this.orderTabs = this.orderTabsAll; // 订单状态
            this.showPrice = true; // 是否显示价格
            this.showTabbar = true; // 是否显示底部tab
        }

        uni.showLoading({
            title: '加载中'
        });
        // 获取全部订单
        this.getOrderList(this.size);
        // 获取订单失效时间
        this.getOrderTime();
    },
    onReachBottom() {
        if (this.orderList.length < this.totalPage) {
            this.handleMore();
        }
    },
    methods: {
        //预览图片
        preview(img) {
            uni.previewImage({
                urls: [img],
            });
        },
        //确定分配
        allocationOk() {
            if (this.allocationUserId != '') {
                this.$api.put(this, api.staff, {
                    id: this.allocationId,
                    staffCode: this.allocationUserId,
                }).then((res) => {
                    this.closeAllocation();
                    uni.showToast({
                        icon: 'none',
                        title: '订单分配成功，正在计算奖金..',
                        duration: 2000
                    });
                    let timer1 = setInterval(() => {
                        this.page = 1;
                        this.orderList = [];
                        this.getOrderList(this.size);
                        clearTimeout(timer1)
                    }, 2000);
                })
            } else {
                uni.showToast({
                    icon: 'none',
                    title: '请选择分销员',
                    duration: 1000
                });
            }
        },
        radioChange(e) {
            this.allocationUserId = e.detail.value;
        },
        closeAllocation() {
            this.allocationUserId = '';
            this.show = false;
            this.$refs.popup.close()
        },
        // 分配订单
        allocation(id) {
            this.allocationId = id;
            this.show = true;
            this.$refs.popup.open('center');
            this.$api.get(this, api.getdistributorUser, {
                page: 1,
                size: 999,
                shopCode: uni.getStorageSync("shopCode"),
                status: 2//申请状态 1申请中 2申请通过 3申请失败
            }).then((res) => {
                this.allocationUser = res.data.list;
            })
        },
        radioOrder(id) {
            this.orderList.forEach((order, index) => {
                if (order.id == id) {
                    order.choose = !order.choose;
                    this.$forceUpdate()
                }
            });
        },
        allradio() {
            this.allCheckFlag = !this.allCheckFlag;
            this.orderList.forEach((order) => {
                order.choose = this.allCheckFlag;
            });
        },
        // 审核通过
        through() {
            this.$api.put(this, api.auditpua, {
                ids: [this.ids], orderStatus: 2
            }).then((res) => {
                this.isaudit = false;
                if (res.success) {
                    this.close();
                    this.orderList = [];
                    this.getOrderList(this.size);
                    uni.showToast({
                        icon: 'none',
                        title: '审核通过！',
                        duration: 2000
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
        //拒绝审核
        refused() {
            this.$api.put(this, api.auditpua, {
                ids: [this.ids], orderStatus: 3
            }).then((res) => {
                this.isaudit = false;
                if (res.success) {
                    this.close();
                    this.getOrderList(this.size);
                    uni.showToast({
                        icon: 'none',
                        title: '已拒绝！',
                        duration: 2000
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
        // 立即审核
        auditFun(id) {
            this.ids = id;
            this.isaudit = true;
            this.$refs.message.open();
        },
        close() {
            this.$refs.message.close();
        },
        // 加载更多
        handleMore() {
            uni.showLoading({
                title: '加载中'
            });
            this.page++;
            let size = this.page * this.size;
            this.getOrderList(size);
        },
        // 跳转定制页面
        handleSubmit() {
            this.$router.push("/phone");
        },
        // 获取订单列表
        getOrderList(size) {
            // 获取订单列表
            var data = {
                frontOrderStatus: this.active,
                // customerId: this.userId,shopCode
                // shopCode: uni.getStorageSync("shopCode"),
                staffCode: this.userNo,
                page: 1,
                size: size,
            }
            if (this.shopOrder == 1) {
                data = {
                    frontOrderStatus: this.active,
                    shopCode: uni.getStorageSync("shopCode"),
                    page: 1,
                    size: size,
                }
            }
            this.$api.get(this, api.cgetOrderList, data).then((res) => {
                if (res.success) {
                    this.orderList = res.data.list;
                    this.totalPage = res.data.total;
                    this.orderList.forEach((order) => {
                        order.choose = false;
                        var totalCount = 0;
                        order.goodss.forEach(item => {
                            totalCount += item.itemCount;
                        })
                        order.totalCount = totalCount;
                    });
                    console.log("订单列表：", this.orderList);
                    if (this.orderList.length > 0) {
                        this.timer();
                    }
                    uni.hideLoading();

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
                    return "待审核";
                case 2:
                    return "待发货";
                case 4:
                    return "待收货";
                case 5:
                    return "已关闭";
                case 6:
                    return "已完成";
                case 7:
                    return "待付款"; //已同意
                case 8:
                    return "已拒绝"; //已同意
                case 9:
                    return "出库中"; //出库中
            }
        },
        // 跳转订单详情
        orderDetail(id, frontOrderStatus) {
            uni.navigateTo({
                url: "../order/orderDetails?id=" + id + "&frontOrderStatus=" + frontOrderStatus,
            });
        },
        // 切换订单状态
        handleClickv(val, title) {
            uni.showLoading({
                title: '加载中'
            });
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
                    if (item.frontOrderStatus === 1) {
                        let timeStr = item.createTime.replace(/-/g, "/");
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
    }
};
  </script>
  <style lang="scss" scoped>
.allocation_box {
    width: 600rpx;
    height: 1000rpx;
    background-color: #fff;
    border-radius: 20rpx;
    .allocation_head_title {
        width: 600rpx;
        height: 80rpx;
        padding: 0 30rpx;
        box-sizing: border-box;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .head_title {
            color: rgba(16, 16, 16, 1);
            font-size: 32rpx;
            font-weight: 600;
        }
    }
    .distributor_list_box {
        width: 600rpx;
        height: 800rpx;
        padding: 0 30rpx;
        box-sizing: border-box;
        .distributor_list_meu {
            height: 120rpx;
            display: flex;
            align-items: center;
            font-size: 28rpx;
            color: rgba(59, 59, 59, 1);
            image {
                border-radius: 60rpx;
                width: 120rpx;
                height: 120rpx;
                margin-left: 30rpx;
                margin-right: 20rpx;
            }
        }
    }
    .bit_ok {
        width: 500rpx;
        height: 70rpx;
        line-height: 70rpx;
        border-radius: 50rpx;
        background-color: rgba(0, 118, 164, 1);
        color: #ffffff;
        font-size: 28rpx;
        text-align: center;
        margin: 0 auto;
        margin-top: 20rpx;
    }
}
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
    .distributor_box {
        display: flex;
        align-items: center;
        font-size: 20rpx;
        color: rgba(56, 51, 74, 1);
        .bonus {
            color: rgba(0, 118, 165, 1);
        }
    }
    .distributor_icon_box {
        width: 40rpx;
        height: 40rpx;
        background-color: #e9f4ff;
        border-radius: 20rpx;
        text-align: center;
        margin-right: 20rpx;
        image {
            width: 30rpx;
            height: 30rpx;
            margin-top: 5rpx;
        }
    }
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
        z-index: 9000;
        top: 0;
    }
    .uni-scroll-view {
        padding: 0 30rpx;
    }
    .top-nav {
        display: flex;
        justify-content: space-between;
        padding: 0 40rpx;
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
  
  
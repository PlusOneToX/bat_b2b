<template>
    <view>
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar left-icon="left" :border="false" @clickLeft="back" dark :fixed="false" background-color="transparent" color="#ffffff" status-bar title="分销员详情" />
        <!-- #endif -->
        <view class="top_bg_box" :style="{marginTop: globalData.navHeight+'rpx'}">
            <!-- #ifdef H5 -->
            <view class="h5_nav_bar">
                <view style="width: 30rpx;" @click="back">
                    <uni-icons type="back" size="24" color="#ffffff"></uni-icons>
                </view>
                <view>分销员详情</view>
                <view style="width: 30rpx;"></view>
            </view>
            <!-- #endif -->
            <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>
            <view class="distribution_list_box">
                <view class="distribution_user_list_box">
                    <view class="user_message_box">
                        <image src="../../../static/images/index_login.png"></image>
                        <view class="user_informationize_box">
                            <view class="user_name">{{distributorDate.name}}</view>
                            <view class="user_name">用户id：<span class="user_span">{{distributorDate.id}}</span></view>
                            <view class="user_name">手机号：<span class="user_span">{{distributorDate.phone}}</span></view>
                            <view class="user_name">合作时间：<span class="user_span">{{distributorDate.updateTime}}</span></view>
                        </view>
                    </view>
                    <view class="check_and_return" @click="audit()">
                        <uni-icons type="trash-filled" size="24" color="#FF7E3E"></uni-icons>清退
                    </view>
                </view>
                <view class="order_box">
                    <view class="order_title_box">
                        <image style="width: 50rpx;height: 50rpx;" src="../../../static/icons/my/order/off_the_stocks.png"></image>
                        订单
                    </view>
                    <view class="order_item_box">
                        <view class="order_item">
                            <view class="order_item_title">总订单数</view>
                            <view class="order_item_num">{{shopOrder.totalCount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">有效订单</view>
                            <view class="order_item_num">{{shopOrder.validTotalCount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">无效订单</view>
                            <view class="order_item_num">{{shopOrder.unValidTotalCount}}</view>
                        </view>
                    </view>
                </view>
                <view class="order_box">
                    <view class="order_title_box">
                        <image style="width: 50rpx;height: 50rpx;" src="../../../static/icons/my/order/earnings.png"></image>
                        业绩
                    </view>
                    <view class="order_item_box">
                        <view class="order_item">
                            <view class="order_item_title">总营业金额</view>
                            <view class="order_item_num">{{shopAmount.totalAmount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">有效金额</view>
                            <view class="order_item_num">{{shopAmount.validTotalAmount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">无效金额</view>
                            <view class="order_item_num">{{shopAmount.unValidTotalAmount}}</view>
                        </view>
                    </view>
                    <view class="linq"></view>
                    <view class="order_item_box">
                        <view class="order_item">
                            <view class="order_item_title">总销售奖金</view>
                            <view class="order_item_num">{{statisticsAmount.totalCommissionAmount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">已发奖金</view>
                            <view class="order_item_num">{{statisticsAmount.issuedTotalCommissionAmount}}</view>
                        </view>
                        <view class="order_item">
                            <view class="order_item_title">未发奖金</view>
                            <view class="order_item_num">{{statisticsAmount.unIssuedTotalCommissionAmount}}</view>
                        </view>
                    </view>
                </view>
            </view>
        </view>
        <uni-popup ref="message" type="dialog">
            <uni-popup-dialog type="info" cancelText="返回" confirmText="确定" title="清退确认" content="请确认是否清退该分销员？" @confirm="passConfirm()"></uni-popup-dialog>
        </uni-popup>
    </view>
</template>

<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            globalData: {
                statusBarHeight: 0, // 状态导航栏高度
                navHeight: 0, // 总体高度
                navigationBarHeight: 0, // 导航栏高度(标题栏高度)
            },
            distributorId: '',
            staffCode: '',
            distributorDate: [],
            shopOrder: {
                totalCount: 0,//订单总数
                unValidTotalCount: 0,//无效订单
                validTotalCount: 0,//有效订单数
            },
            shopAmount: {
                totalAmount: 0,//总营业额
                unValidTotalAmount: 0,//无效金额
                validTotalAmount: 0,//有效金额
            },
            statisticsAmount: {
                issuedTotalCommissionAmount: 0,//已发奖金
                totalCommissionAmount: 0,//总销售奖金
                unIssuedTotalCommissionAmount: 0,//未发奖金
            },
        };
    },
    onLoad(parameter) {
        this.distributorId = parameter.id;
        this.staffCode = parameter.staffCode;
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
        this.getdistributorDate();
        this.getShopOrderList();
        this.shopAmountStatistics();
        this.shopStatistics();
    },
    methods: {
        //获取分销员奖金统计
        shopStatistics() {
            this.$api.get(this, api.shopStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: this.staffCode
            }).then((res) => {
                this.statisticsAmount = res.data;
            });
        },
        //获取店铺金额统计
        shopAmountStatistics() {
            this.$api.get(this, api.shopAmountStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: this.staffCode
            }).then((res) => {
                this.shopAmount = res.data;
            });
        },
        //获取店铺订单统计
        getShopOrderList() {
            this.$api.get(this, api.shopOrderStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: this.staffCode
            }).then((res) => {
                this.shopOrder = res.data;
            });
        },
        //获取分销商详情
        getdistributorDate() {
            this.$api.get(this, api.applyforsub, {
                distributorId: uni.getStorageSync("distributorId"),//需传分销商id
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: this.staffCode
            }).then((res) => {
                this.distributorDate = res.data;
            })
        },
        passConfirm() {
            var that = this;
            this.$api.delete(this, api.applyforsub, {
                id: this.distributorId,
            }).then((res) => {
                if (res.success) {
                    uni.showToast({
                        title: '清退成功',
                        icon: "none",
                        duration: 2000,
                        complete: function () {
                            that.back();
                        }
                    });
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
            })
        },
        audit(id) {
            this.$refs.message.open();
        },
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
    },

};
</script>
<style lang="scss" scoped>
.linq {
    width: 628rpx;
    margin: 0 auto;
    height: 0rpx;
    border: 1rpx solid rgba(245, 245, 245, 1);
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
    .distribution_list_box {
        min-height: 200px;
        max-height: 100%;
        width: 750rpx;
        margin-top: -240rpx;
        z-index: 10;
        border-radius: 50rpx 50rpx 0rpx 0rpx;
        background-color: #efeff4;
        padding-top: 40rpx;
        .distribution_user_list_box {
            width: 680rpx;
            height: 200rpx;
            border-radius: 30rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 0 auto;
            margin-bottom: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            padding: 0 30rpx;
            box-sizing: border-box;
            .user_message_box {
                display: flex;
                height: 160rpx;
                align-items: center;
                image {
                    width: 100rpx;
                    height: 100rpx;
                    border-radius: 100rpx;
                }
                .user_informationize_box {
                    margin-left: 20rpx;
                    height: 140rpx;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    .user_name {
                        color: rgba(59, 59, 59, 1);
                        font-size: 24rpx;
                        font-family: PingFangSC-regular;
                        .user_span {
                            color: #bbbbbb;
                            font-size: 20rpx;
                        }
                    }
                }
            }
            .check_and_return {
                color: rgba(255, 126, 62, 1);
                font-size: 24rpx;
                display: flex;
                align-items: center;
                margin-top: -100rpx;
            }
        }
        .order_box {
            width: 680rpx;
            height: auto;
            border-radius: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            margin: 0 auto;
            margin-bottom: 30rpx;
            .order_title_box {
                height: 90rpx;
                width: 680rpx;
                padding: 0 30rpx;
                box-sizing: border-box;
                border: 1rpx solid rgba(245, 245, 245, 1);
                display: flex;
                align-items: center;
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                font-weight: 600;
            }
            .order_item_box {
                width: 600rpx;
                height: 210rpx;
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin: 0 auto;
                .order_item {
                    text-align: center;
                    width: 180rpx;
                    height: 100rpx;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    .order_item_title {
                        color: rgba(129, 126, 141, 1);
                        font-size: 24rpx;
                        font-weight: 600;
                    }
                    .order_item_num {
                        color: rgba(16, 16, 16, 1);
                        font-size: 32rpx;
                        font-weight: 600;
                    }
                }
            }
        }
    }
}
</style>

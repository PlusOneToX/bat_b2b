<template>
    <view>
        <view v-show="false" class="remaining_sum_box">
            <view class="remaining_sum_headline">
                <view>店铺余额</view>
                <view @click="toClick('../finance/detail')">
                    明细
                    <uni-icons type="right" size="16" color="#ffffff"></uni-icons>
                </view>
            </view>
            <view class="remaining_sum_detail_box">
                <view class="balance">￥{{balance}}</view>
                <view class="recharge" @click="toClick('../finance/recharge')">充值</view>
            </view>
        </view>
        <view class="order_box">
            <view class="order_title_box">
                <view>
                    <image style="width: 50rpx;height: 50rpx;margin-right: 10rpx;" src="../../../static/icons/my/order/wait_for_receiving.png"></image>订单
                </view>
                <view @click="toClick('../../store/store_order?shopOrder=1')">
                    明细
                    <uni-icons type="right" size="16"></uni-icons>
                </view>
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
                <view>
                    <image style="width: 50rpx;height: 50rpx;margin-right: 10rpx;" src="../../../static/icons/my/order/finance.png"></image>
                    资金
                </view>
                <view>

                </view>
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
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
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
            balance: 0.00
        };
    },
    onLoad() {
        this.getShopOrderList();
        this.shopAmountStatistics();
        this.userDeposit();
        this.shopStatistics();
    },

    methods: {
        //获取店铺奖金统计
        shopStatistics() {
            this.$api.get(this, api.shopStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
            }).then((res) => {
                this.statisticsAmount = res.data;
            });
        },
        //获取店铺订单统计
        getShopOrderList() {
            this.$api.get(this, api.shopOrderStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
            }).then((res) => {
                this.shopOrder = res.data;
            });
        },
        //获取店铺金额统计
        shopAmountStatistics() {
            this.$api.get(this, api.shopAmountStatistics, {
                distributorId: uni.getStorageSync("distributorId"),
                shopCode: uni.getStorageSync("shopCode"),
            }).then((res) => {
                this.shopAmount = res.data;
            });
        },
        //获取店铺余额
        userDeposit() {
            this.$api.get(this, api.userDeposit, {
                id: uni.getStorageSync("distributorId"),
            }).then((res) => {
                this.balance = res.data.accountAvailable;
            });
        },
        // 跳转
        toClick(name) {
            uni.navigateTo({
                url: name,
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.remaining_sum_box {
    width: 690rpx;
    height: 200rpx;
    border-radius: 30rpx;
    background-color: rgba(0, 118, 164, 1);
    margin: 0 auto;
    margin-top: 30rpx;
    box-sizing: border-box;
    padding: 0 40rpx;
    margin-bottom: 30rpx;
    .remaining_sum_headline {
        height: 80rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 610rpx;
        color: rgba(255, 255, 255, 1);
        font-size: 24rpx;
        view {
            height: 80rpx;
            display: flex;
            align-items: center;
        }
    }
    .remaining_sum_detail_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 610rpx;
        height: 80rpx;
        .balance {
            color: rgba(255, 255, 255, 1);
            font-size: 40rpx;
            font-weight: 600;
        }
        .recharge {
            width: 70rpx;
            height: 34rpx;
            line-height: 34rpx;
            border-radius: 10rpx;
            background-color: rgba(253, 253, 253, 1);
            color: rgba(0, 118, 164, 1);
            font-size: 20rpx;
            text-align: center;
        }
    }
}
.order_box {
    width: 680rpx;
    height: auto;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    margin: 0 auto;
    margin-top: 30rpx;
    .order_title_box {
        height: 90rpx;
        width: 680rpx;
        padding: 0 30rpx;
        box-sizing: border-box;
        border: 1rpx solid rgba(245, 245, 245, 1);
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        font-weight: 600;
        view {
            display: flex;
            align-items: center;
        }
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
</style>

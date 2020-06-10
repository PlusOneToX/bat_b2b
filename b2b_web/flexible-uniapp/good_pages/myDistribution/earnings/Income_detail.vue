<template>
    <view>
        <view class="balance_detail_box">
            <view class="balance_detail_head">
                <view :class="settleStatus==0?'select':''" @click="choice(0)">未结算</view>
                <view :class="settleStatus==1?'select':''" @click="choice(1)">已结算</view>
                <view :class="settleStatus==2?'select':''" @click="choice(2)">无效订单</view>
            </view>
            <scroll-view scroll-y="true" class="balance_detail_y" @scrolltolower="ReachBottom()">
                <view class="balance_detail_list_box" v-for="item in detailsList" :key="item.id">
                    <view class="title_box">
                        <view>
                            {{businessType(item.businessType)}}
                            <span class="money">{{item.commissionType==1?'+':'-'}} ¥ {{item.amount}}</span>
                        </view>
                        <view class="state" v-if="settleStatus==0">待到账</view>
                        <view class="state" v-else-if="settleStatus==1">已结算</view>
                        <view class="state1" v-else>无效订单</view>
                    </view>
                    <view class="serial_number_box">
                        <view>订单编号：{{item.businessId}}</view>
                        <view>{{item.payWay==4?'线下付款':item.payWay==2?'微信支付':'支付宝支付'}}</view>
                    </view>
                </view>
                <view v-if="detailsList.length==0" style="text-align: center;line-height: 400rpx;">暂无收益记录~</view>
            </scroll-view>
        </view>
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            page: 1,
            size: 10,
            totalCount: 0,
            detailsList: [],
            settleStatus: 0
        };
    },
    onShow() {
        this.subcommissionStatisticsList();
    },
    onPullDownRefresh() {
        this.page = 1;
        this.subcommissionStatisticsList();
    },
    methods: {
        choice(e) {
            this.settleStatus = e;
            this.page = 1;
            this.subcommissionStatisticsList();
        },
        ReachBottom() {
            if (this.detailsList.length < this.totalCount) {
                this.page += 1;
                this.subcommissionStatisticsList();
            }
        },
        subcommissionStatisticsList() {
            this.$api.get(this, api.subcommissionStatisticsList, {
                businessType: 1,
                page: this.page,
                size: this.size,
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: uni.getStorageSync("userNo"),
                settleStatus: this.settleStatus
            }).then((res) => {
                if (res.success) {
                    this.totalCount = res.data.total;
                    if (this.page !== 1) {
                        this.detailsList = [...that.detailsList, ...res.data.list];
                    } else {
                        this.detailsList = res.data.list;
                    }
                }
                uni.stopPullDownRefresh()
            });
        },
        businessType(val) {
            // 1.订单分佣 2.订单售后 3.佣金提现 4.订单消费 5.店铺分佣 6.分佣调整
            switch (val) {
                case 1:
                    return "订单分佣";
                case 2:
                    return "订单售后";
                case 3:
                    return "佣金提现";
                case 4:
                    return "订单消费";
                case 5:
                    return "店铺分佣";
                case 6:
                    return "分佣调整";
            }
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
.balance_detail_box {
    height: 100vh;
    width: 750rpx;
    background-color: #ffffff;
    margin: 0 auto;
    .balance_detail_y {
        height: 93vh;
    }
    .balance_detail_head {
        height: 80rpx;
        width: 690rpx;
        margin: 0 auto;
        color: rgba(16, 16, 16, 1);
        font-size: 24rpx;
        font-weight: 600;
        display: flex;
        align-items: center;
        border-bottom: 1rpx solid rgba(245, 245, 245, 1);
        view {
            margin-right: 20rpx;
        }
        .select {
            color: rgba(255, 126, 62, 1);
            border-bottom: 4rpx solid rgba(255, 126, 62, 1);
        }
    }
    .balance_detail_list_box {
        width: 690rpx;
        height: 120rpx;
        margin: 0 auto;
        border-bottom: 1rpx solid rgba(245, 245, 245, 1);
        .title_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 690rpx;
            height: 40rpx;
            color: rgba(59, 59, 59, 1);
            font-size: 24rpx;
            margin-top: 30rpx;
            padding: 0 20rpx;
            box-sizing: border-box;
            .money {
                color: rgba(0, 118, 164, 1);
                font-size: 24rpx;
                margin-left: 20rpx;
            }
            .state {
                color: #ff7e3e;
                font-size: 24rpx;
            }
            .state1 {
                color: #888888;
                font-size: 24rpx;
            }
        }
        .serial_number_box {
            display: flex;
            justify-content: space-between;
            align-items: center;
            width: 690rpx;
            height: 40rpx;
            color: rgba(136, 136, 136, 1);
            font-size: 20rpx;
            padding: 0 20rpx;
            box-sizing: border-box;
        }
    }
}
</style>
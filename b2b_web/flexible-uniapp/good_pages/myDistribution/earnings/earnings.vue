<template>
    <view>
        <view class="earnings_box">
            <view class="remaining_sum_headline">
                <view @click="openhint">
                    预估总收益
                    <uni-icons type="help" size="20" color="#ffffff"></uni-icons>
                </view>
                <view @click="toClick('../earnings/Income_detail')">
                    明细
                    <uni-icons type="right" size="16" color="#ffffff"></uni-icons>
                </view>
            </view>
            <view class="remaining_sum_detail_box">
                ￥{{(earningsAmount.withdrawAmount+earningsAmount.unsettleAmount+earningsAmount.settleAmount).toFixed(2)}}
            </view>
            <view class="earnings_state_box">
                <view class="earnings_state_mue">
                    <view class="hint">
                        已到账奖金
                    </view>
                    <view class="earnings_state_mue_money">¥ {{earningsAmount.withdrawAmount}}</view>
                </view>
                <view class="linq"></view>
                <view class="earnings_state_mue">
                    <view>已结未到奖金</view>
                    <view class="earnings_state_mue_money">¥ {{earningsAmount.settleAmount}}</view>
                </view>
                <view class="linq"></view>
                <view class="earnings_state_mue">
                    <view class="hint">
                        预估未结奖金
                    </view>
                    <view class="earnings_state_mue_money">¥ {{(earningsAmount.unsettleAmount).toFixed(2)}}</view>
                </view>
            </view>
        </view>
        <view class="time_box">
            <view :class="index==1?'select_tiem':''" @click="getDateTime(1)">今日</view>
            <view :class="index==2?'select_tiem':''" @click="getDateTime(2)">昨日</view>
            <view :class="index==3?'select_tiem':''" @click="getDateTime(3)">本月</view>
            <view :class="index==4?'select_tiem':''" @click="getDateTime(4)">上月</view>
        </view>
        <view class="order_box">
            <view class="order_title_box">
                <view>
                    <image style="width: 50rpx;height: 50rpx;margin-right: 10rpx;" src="../../../static/icons/my/order/wait_for_receiving.png"></image>
                    有效订单
                </view>
            </view>
            <view class="order_item_box">
                <view class="order_item">
                    <view class="order_item_title">订单数</view>
                    <view class="order_item_num">{{earningsAmountDetail.totalCount}}</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">订单金额（元）</view>
                    <view class="order_item_num">{{earningsAmountDetail.totalOrderAmount}}</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">预估收益（元）</view>
                    <view class="order_item_num">{{earningsAmountDetail.totalCommissionAmount}}</view>
                </view>
            </view>
        </view>
        <uni-popup ref="popup" type="center">
            <view class="hint_box">
                <view class="headline">提示</view>
                <view class="content_box">
                    <view class="content_title">预估总收益</view>
                    <view class="content_meu">包含现金、线下付款等所有支付类型提交的有效订单，所产生的预估收益之和；</view>
                    <view class="content_title">已结已到奖金</view>
                    <view class="content_meu">已生成有效订单（已支付或审核），且已结算订单（已完成），并通过现金形式实际发放至用户收款账户的奖金之和；</view>
                    <view class="content_title">已结未到奖金</view>
                    <view class="content_meu">已生成有效订单，且已结算订单，但并未实际到账的奖金之和；</view>
                    <view class="content_title">未结预估奖金</view>
                    <view class="content_meu">已生成有效订单，但暂未结算的订单奖金之和；</view>
                    <!-- <view class="content_title">有效订单</view>
                    <view class="content_meu">成功完成支付或通过店铺审核的订单；</view> -->
                </view>
                <view class="content_close_box" @click="close">确定</view>
            </view>
        </uni-popup>
        <!-- <view class="order_box">
            <view class="order_title_box">
                <view>
                    <uni-icons type="list" size="26"></uni-icons>自用订单
                </view>

            </view>
            <view class="order_item_box">
                <view class="order_item">
                    <view class="order_item_title">订单数</view>
                    <view class="order_item_num">545</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">订单金额</view>
                    <view class="order_item_num">545</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">预估收益</view>
                    <view class="order_item_num">545</view>
                </view>
            </view>
        </view>
        <view class="order_box">
            <view class="order_title_box">
                <view>
                    <uni-icons type="list" size="26"></uni-icons>推广订单
                </view>
            </view>
            <view class="order_item_box">
                <view class="order_item">
                    <view class="order_item_title">订单数</view>
                    <view class="order_item_num">545</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">订单金额</view>
                    <view class="order_item_num">545</view>
                </view>
                <view class="order_item">
                    <view class="order_item_title">预估收益</view>
                    <view class="order_item_num">545</view>
                </view>
            </view>
        </view> -->
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            earningsAmount: {
                freezeAmount: 0,//已冻结佣金(提现中的佣金)
                settleAmount: 0,//佣金余额
                totalSettleAmount: 0,//获得总佣金
                unsettleAmount: 0,//未结算佣金(订单还未发货收货确认)
                withdrawAmount: 0,//已提现金额
            },
            earningsAmountDetail: {
                totalCommissionAmount: 0,//预估收益
                totalCount: 0,//订单总数量
                totalOrderAmount: 0,//订单总金额
            },
            startTime: '',
            startEnd: '',
            index: 1
        };
    },
    onLoad() {
        this.getDateTime(1);
        this.userDeposit();
    },
    onPullDownRefresh() {
        this.getDateTime(this.index);
        this.userDeposit();
    },
    methods: {
        openhint() {
            this.$refs.popup.open('center')
        },
        close() {
            this.$refs.popup.close()
        },
        GetDateStr(AddDayCount) {
            var dd = new Date();
            dd.setDate(dd.getDate() + AddDayCount);//获取AddDayCount天后的日期
            var y = dd.getFullYear();
            var m = dd.getMonth() + 1;//获取当前月份的日期
            var d = dd.getDate();
            return y + "/" + m + "/" + d;
        },
        getDateTime(e) {
            this.index = e;
            var date = new Date();
            var year = date.getFullYear(); //年
            var month = date.getMonth() + 1; //月
            var day = date.getDate(); //日
            //今天
            if (e == 1) {
                this.startTime = year + '/' + month + '/' + day + ' 00:00:00';
                this.startEnd = year + '/' + month + '/' + day + ' 23:59:59';
            }
            //昨天
            else if (e == 2) {
                this.startTime = this.GetDateStr(-1) + ' 00:00:00';
                this.startEnd = this.GetDateStr(-1) + ' 23:59:59';
            }
            //本月
            else if (e == 3) {
                this.startTime = year + '/' + month + '/01 00:00:00';
                this.startEnd = year + '/' + month + '/31 23:59:59';
            }
            //上月
            else {
                month = month == 1 ? 12 : month;
                this.startTime = year + '/' + (month - 1) + '/01 00:00:00';
                this.startEnd = year + '/' + (month - 1) + '/31 23:59:59';
            }
            this.subcommissionStatisticsDetail();
        },
        subcommissionStatisticsDetail() {
            this.$api.get(this, api.subcommissionStatisticsDetail, {
                staffCode: uni.getStorageSync("userNo"),
                startTime: this.startTime,
                endTime: this.startEnd,
                businessType: 1,
            }).then((res) => {
                this.earningsAmountDetail = res.data;
            });
        },
        userDeposit() {
            this.$api.get(this, api.subcommissionStatistics, {
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: uni.getStorageSync("userNo")
            }).then((res) => {
                this.earningsAmount = res.data;
                uni.stopPullDownRefresh()
            });
        },
        // 跳转
        toClick(name) {
            uni.navigateTo({
                url: name,
            });
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
.hint_box {
    width: 600rpx;
    height: 850rpx;
    background-color: #ffffff;
    border-radius: 30rpx;
    .content_box {
        padding: 20rpx 40rpx;
        box-sizing: border-box;
        .content_title {
            font-size: 28rpx;
            color: #252525;
            font-weight: 600;
            margin-bottom: 10rpx;
        }
        .content_meu {
            font-weight: 24rpx;
            color: #404040;
            margin-bottom: 20rpx;
        }
    }
    .content_close_box {
        width: 500rpx;
        height: 70rpx;
        line-height: 70rpx;
        color: #ffffff;
        font-size: 26rpx;
        background-color: #ff7e3e;
        border-radius: 40rpx;
        margin: 0 auto;
        text-align: center;
    }
    .headline {
        width: 600rpx;
        text-align: center;
        height: 80rpx;
        line-height: 80rpx;
        font-size: 30rpx;
        color: #000;
        font-weight: 600;
    }
}
.linq {
    height: 80rpx;
    border-left: 1rpx solid rgba(237, 236, 236, 1);
}
.earnings_box {
    width: 690rpx;
    height: auto;
    margin: 0 auto;
    margin-top: 30rpx;
    border-radius: 30rpx;
    background-color: rgba(255, 126, 62, 1);
    .remaining_sum_headline {
        height: 80rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 610rpx;
        margin: 0 auto;
        color: rgba(255, 255, 255, 1);
        font-size: 24rpx;
        view {
            height: 80rpx;
            display: flex;
            align-items: center;
        }
    }
    .remaining_sum_detail_box {
        width: 610rpx;
        margin: 0 auto;
        color: rgba(255, 255, 255, 1);
        font-size: 40rpx;
        font-weight: 600;
    }
    .earnings_state_box {
        width: 610rpx;
        height: 160rpx;
        margin: 0 auto;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .earnings_state_mue {
            width: 200rpx;
            color: rgba(255, 255, 255, 1);
            font-size: 24rpx;
            text-align: center;
            .hint {
                display: flex;
                align-items: center;
                justify-content: center;
            }
            view {
                margin-bottom: 10rpx;
            }
            .earnings_state_mue_money {
                font-weight: 600;
                font-size: 26rpx;
            }
        }
    }
}
.time_box {
    width: 600rpx;
    height: 120rpx;
    margin: 0 auto;
    display: flex;
    justify-content: space-between;
    align-items: center;
    color: rgba(56, 51, 74, 1);
    font-size: 24rpx;
    .select_tiem {
        color: rgba(255, 126, 62, 1);
        border-bottom: 4rpx solid rgba(255, 126, 62, 1);
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

<template>
    <view style="background-color: #ffffff;height: 1100rpx;padding-top: 20rpx; border-radius: 30rpx 30rpx 0 0;">
        <view class="sjop_title_box">
            <view>优惠券列表</view>
            <veiw class="the_number_of">
                <uni-icons @click="shut_down" type="closeempty" size="30"></uni-icons>
            </veiw>
        </view>
        <scroll-view scroll-y="true" class="vouchers_list_box">
            <view class="vouchers_box_meu" :class="item.amountEnable==1&&item.goodsEnable==1?'vouchers_meu_box_default':'vouchers_meu_box_no'" v-for="(item,index) in couponList" :key="index" @click="clickItem(item)">
                <view class="vouchers_meu_let" :class="item.amountEnable==1&&item.goodsEnable==1?'default':'do_not_use'">
                    <view class="filleted_corner"></view>
                    <view class="filleted_corner_bottom"></view>
                    <view v-if="(item.couponMethod==1)">
                        <view>
                            ￥<span class="the_amount_of">{{item.reduction}}</span>
                        </view>
                        <view class="the_title" :class="item.amountEnable==1&&item.goodsEnable==1?'the_title_default':'the_title_do_not_use'">
                            满减券
                        </view>
                    </view>
                    <view v-if="(item.couponMethod==2)">
                        <view>
                            <span class="the_amount_of">{{item.discount / 10 }}折</span>
                        </view>
                        <view class="the_title" :class="item.amountEnable==1&&item.goodsEnable==1?'the_title_default':'the_title_do_not_use'">
                            满折
                        </view>
                    </view>
                    <view v-if="(item.couponMethod==3)">
                        <view class="the_title" :class="item.amountEnable==1&&item.goodsEnable==1?'the_title_default':'the_title_do_not_use'">
                            商品兑换
                        </view>
                    </view>
                </view>
                <view class="vouchers_meu_right">
                    <view class="vouchers_meu_name">{{item.couponName}}</view>
                    <view class="use_box" :class="item.amountEnable==1&&item.goodsEnable==1?'use_box_default':'use_box_do_not_use'">

                        <view class="ticket_no" v-if="item.orderMoney > 0&&item.couponType!=4">
                            满{{ item.orderMoney }}元可用
                        </view>
                        <view class="ticket_no" v-else-if="item.couponType==4">
                            新人可用
                        </view>
                        <view class="ticket_no" v-else-if="item.couponMethod==3">
                            商品兑换券
                        </view>

                        <!-- <view class="immediate_use" v-if="(status==1)" @click.stop="getCouponBtn(item)">待领取</view> -->
                        <!-- v-else-if="(status==3)" @click="gohome" -->
                        <view class="immediate_use" v-if="item.amountEnable==1&&item.goodsEnable==1">立即使用</view>
                        <!-- <view class="has_been_used" v-else-if="(status==4)">已使用</view> -->
                        <view class="has_been_used" v-else>暂不可用</view>
                    </view>
                    <view class="the_period_of_validity">有效期：{{dateToNum(item.startTime)}}至{{dateToNum(item.endTime)}}</view>
                </view>
            </view>
        </scroll-view>
    </view>
</template>
<script type="text/ecmascript-6">
export default {
    name: "couponList",
    props: {
        'couponList': {
            type: Array,
            default: []
        },
    },
    data() {
        return {
            status: 3
        };
    },
    mounted() {

    },

    methods: {
        clickItem(item) {
            if (item.amountEnable == 0) {
                uni.showToast({
                    icon: 'none',
                    title: '当前材质不可用',
                    duration: 2000
                });
            } else if (item.goodsEnable == 0) {
                uni.showToast({
                    icon: 'none',
                    title: '当前商品不可用',
                    duration: 2000
                });
            } else {
                this.$emit('clickItem', item)
            }
        },
        shut_down() {
            this.$emit('shut_down', 1)
        },
        dateToNum(val) {
            return val.substring(0, 10)
        },

    },
};
</script>
<style lang="scss" scoped>
.filleted_corner {
    position: absolute;
    left: 204rpx;
    margin-top: -218rpx;
    width: 32rpx;
    height: 32rpx;
    background-color: #ffffff;
    border-radius: 16rpx;
}
.filleted_corner_bottom {
    position: absolute;
    left: 204rpx;
    margin-top: 218rpx;
    width: 32rpx;
    height: 32rpx;
    background-color: #ffffff;
    border-radius: 16rpx;
}
.sjop_title_box {
    width: 700rpx;
    height: 60rpx;
    margin: 0 auto;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 28rpx;
    font-family: PingFangSC-regular;
    .the_number_of {
        display: flex;
        align-items: center;
        font-size: 24rpx;
    }
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
.vouchers_list_box {
    width: 690rpx;
    height: 1000rpx;
    margin: 0 auto;
    margin-top: 40rpx;
    .vouchers_meu_box_default {
        background-color: #ffeaea;
    }
    .vouchers_meu_box_no {
        background-color: #eeeeee;
    }
    .vouchers_box_meu {
        width: 690rpx;
        height: 220rpx;
        border-radius: 20rpx;
        display: flex;
        margin-bottom: 20rpx;
        .default {
            background: linear-gradient(to top left, #ff933f, #ff4a65);
            border-right: 2rpx dotted #fd2a56;
        }
        .do_not_use {
            background: #a4a4a4;
        }
        .vouchers_meu_let {
            width: 220rpx;
            height: 220rpx;
            border-radius: 20rpx 0rpx 0rpx 20rpx;
            display: flex;
            align-items: center;
            justify-content: center;
            text-align: center;
            view {
                color: #ffffff;
            }
            .the_amount_of {
                font-size: 50rpx;
                font-weight: 600;
            }
            .the_title_default {
                background-color: #ffc5be;
            }
            .the_title_do_not_use {
                background-color: #545353;
            }
            .the_title {
                width: 120rpx;
                height: 40rpx;
                font-size: 26rpx;
                font-weight: 600;
                border-radius: 30rpx;
                text-align: center;
                line-height: 40rpx;
                margin: 0 auto;
            }
        }
        .vouchers_meu_right {
            width: 470rpx;
            height: 220rpx;
            padding: 10px 10px 10px 10px;
            box-sizing: border-box;
            .vouchers_meu_name {
                width: 420rpx;
                height: 40rpx;
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                text-align: left;
                font-family: SourceHanSansSC-bold;
                font-weight: 600;
                overflow: hidden;
                text-overflow: ellipsis;
                display: -webkit-box;
                -webkit-line-clamp: 1;
                -webkit-box-orient: vertical;
            }
            .use_box_default {
                border-bottom: 1rpx dotted #fd2a56;
            }
            .use_box_do_not_use {
                border-bottom: 1rpx dotted #e1e1e1;
            }
            .use_box {
                width: 420rpx;
                height: 90rpx;
                display: flex;
                align-items: center;
                justify-content: space-between;
                .ticket_no {
                    color: rgba(84, 83, 83, 1);
                    font-size: 22rpx;
                    text-align: left;
                    font-family: SourceHanSansSC-bold;
                    width: 280rpx;
                }
                .has_been_used {
                    cursor: pointer;
                    width: 120rpx;
                    height: 40rpx;
                    text-align: center;
                    line-height: 40rpx;
                    font-size: 24rpx;
                    background-color: #545353;
                    border-radius: 30rpx;
                    color: #ffffff;
                }
                .immediate_use {
                    cursor: pointer;
                    width: 120rpx;
                    height: 40rpx;
                    text-align: center;
                    line-height: 40rpx;
                    font-size: 24rpx;
                    background-image: linear-gradient(
                        to right,
                        #ff933f,
                        #ff4a65
                    );
                    border-radius: 30rpx;
                    color: #ffffff;
                }
            }
            .the_period_of_validity {
                height: 60rpx;
                width: 450rpx;
                line-height: 60rpx;
                color: rgba(164, 164, 164, 1);
                font-size: 20rpx;
                text-align: left;
                font-family: SourceHanSansSC-regular;
            }
        }
    }
}
</style>
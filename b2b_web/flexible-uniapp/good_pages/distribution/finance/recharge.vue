<template>
    <view>
        <view class="ct_box">
            <view class="ct-top">
                <view class="pending_payments">账户余额（元）</view>
                <view>{{balance}}</view>
                <view class="pending_payments">余额可用于订单支付使用，暂不支持提现</view>
            </view>
            <view class="ct-list">
                <view class="method_of_payment">充值</view>
                <view class="top_up_amount_list">
                    <view @click="chooseamount=100" :class="chooseamount==100?'chooseamount':'amount_meu'">充值100元</view>
                    <view @click="chooseamount=200" :class="chooseamount==200?'chooseamount':'amount_meu'">充值200元</view>
                    <view @click="chooseamount=500" :class="chooseamount==500?'chooseamount':'amount_meu'">充值500元</view>
                </view>
                <view class="top_up_amount_list">
                    <view @click="chooseamount=1000" :class="chooseamount==1000?'chooseamount':'amount_meu'">充值1000元</view>
                    <view @click="chooseamount=2000" :class="chooseamount==2000?'chooseamount':'amount_meu'">充值2000元</view>
                    <view @click="chooseamount=5000" :class="chooseamount==5000?'chooseamount':'amount_meu'">充值5000元</view>
                </view>
                <view class="amount_input" @click="chooseamount=6">
                    <input type="number" placeholder="请输入金额" v-model="amount" />
                </view>
                <view class="top_up" @click="checkstandSuccess">充值</view>
            </view>
        </view>
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            chooseamount: 100,
            amount: '',
            balance: 0.00
        };
    },
    onReachBottom() {

    },
    onLoad() {
        this.userDeposit();
    },
    methods: {
        // 充值
        checkstandSuccess() {
            if (this.chooseamount == 6 && this.amount == '') {
                uni.showToast({
                    title: '请输入充值金额',
                    icon: "none",
                    duration: 2000
                });
                return;
            }
            else {
                if (this.chooseamount != 6) {
                    this.amount = this.chooseamount;
                }
                var that = this;
                this.$api.post(this, api.depositRecharge, {
                    amount: this.amount,  // 充值金额
                    distributorId: uni.getStorageSync("distributorId"),  //分销商id
                    payWay: 2,  //支付类型：1.支付宝,2.微信
                    remark: '',
                    payMethod: 'wxpay_mini_program',
                    appId: uni.getAccountInfoSync().miniProgram.appId,
                    platformUserId: uni.getStorageSync("openId"),
                }).then((payRes) => {
                    if (payRes.success) {
                        let wxResp = payRes.data.wxResp;
                        uni.requestPayment({
                            provider: "wxpay",
                            timeStamp: wxResp.timeStamp,
                            nonceStr: wxResp.nonceStr,
                            package: wxResp.prepayId,
                            signType: wxResp.signType,
                            paySign: wxResp.paySign,
                            success: function (res) {
                                that.userDeposit();
                                uni.showToast({
                                    title: '充值成功',
                                    icon: "none",
                                    duration: 2000
                                });
                            },
                            fail: function (err) {
                                uni.showToast({
                                    title: '充值失败',
                                    icon: "none",
                                    duration: 2000
                                });
                            },
                        });
                    }
                    else {
                        uni.showToast({
                            title: payRes.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                    }
                });


            }
        },
        //获取店铺余额
        userDeposit() {
            this.$api.get(this, api.userDeposit, {
                id: uni.getStorageSync("distributorId"),
            }).then((res) => {
                this.balance = res.data.accountAvailable;
            });
        },
    }
};
</script>
<style lang="scss">
.ct_box {
    height: 100vh;
    width: 750rpx;
    background-color: #ffffff;
    padding-top: 50rpx;
    box-sizing: border-box;
}
.chooseamount {
    width: 200rpx;
    height: 100rpx;
    line-height: 100rpx;
    border-radius: 20rpx;
    color: #fc7e1b;
    font-size: 24rpx;
    text-align: center;
    font-family: Arial;
    margin-bottom: 24rpx;
    font-size: 500;
    background-color: rgba(252, 126, 27, 0.1);
    border: 1rpx solid rgba(252, 126, 27, 1);
}
.ct-top {
    width: 620rpx;
    height: 234rpx;
    margin: 0 auto;
    border-radius: 50rpx 50rpx 0rpx 0rpx;
    background-color: rgba(252, 126, 27, 1);
    color: #ffffff;
    padding-top: 35rpx;
    box-sizing: border-box;
    .pending_payments {
        color: rgba(255, 255, 255, 1);
        font-size: 26rpx;
        font-weight: 500;
        font-family: PingFangSC-regular;
        text-align: center;
    }
    view:nth-child(2) {
        color: rgba(255, 255, 255, 1);
        font-size: 56rpx;
        font-weight: 600;
        font-family: PingFangSC-regular;
        margin-top: 10rpx;
        margin-bottom: 10rpx;
        text-align: center;
    }
}
.ct-list {
    width: 690rpx;
    background: #f5f5f5;
    margin: 0 auto;
    border-radius: 30rpx;
    border: 1rpx solid rgba(239, 239, 239, 1);
    .method_of_payment {
        color: #3b3b3b;
        font-size: 30rpx;
        font-weight: 600;
        text-align: left;
        font-family: PingFangSC-regular;
        width: 660rpx;
        margin: 0 auto;
        height: 120rpx;
        line-height: 120rpx;
    }
    .top_up_amount_list {
        width: 640rpx;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        .amount_meu {
            width: 200rpx;
            height: 100rpx;
            line-height: 100rpx;
            border-radius: 20rpx;
            background-color: rgba(255, 255, 255, 1);
            color: rgba(16, 16, 16, 1);
            font-size: 24rpx;
            text-align: center;
            font-family: Arial;
            border: 1rpx solid transparent;
            margin-bottom: 24rpx;
            font-size: 500;
        }
    }
    .amount_input {
        width: 640rpx;
        height: 80rpx;
        border-radius: 100rpx;
        background-color: rgba(255, 255, 255, 1);
        color: rgba(136, 136, 136, 1);
        font-size: 24rpx;
        text-align: left;
        font-family: Arial;
        margin: 0 auto;
        box-sizing: border-box;
        padding: 15rpx 20rpx;
    }
    .top_up {
        width: 640rpx;
        height: 70rpx;
        line-height: 70rpx;
        margin: 0 auto;
        margin-top: 80rpx;
        margin-bottom: 40rpx;
        border-radius: 100rpx;
        background-color: rgba(252, 126, 27, 1);
        color: rgba(255, 255, 255, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Microsoft Yahei;
    }
}
</style>

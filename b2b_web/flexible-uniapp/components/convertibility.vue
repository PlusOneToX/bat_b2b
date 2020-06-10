<template>
    <uni-popup ref="popup" type="bottom" :safeArea="false" :is-mask-click="false">
        <view class="convertibility_box" v-show="false">
            <view class="input_meu">
                <input :adjust-position="false" placeholder="请输入兑换券兑换码" v-model="value" />
            </view>
            <view class="conversion_button_box">兑换</view>
        </view>
        <view class="succeed_box">
            <view class="bar_code_box">
                <view class="voucher_information_box">
                    <view class="voucher_information_title">定制壳免费兑换券</view>
                    <view>2019.08.15-2019.09.15</view>
                </view>
                <view class="quantity">
                    1
                    <view style="font-size: 28rpx;font-weight: 500;padding-top: 10rpx;">张</view>
                </view>
            </view>
            <view class="conversion_button_box">立即定制</view>
        </view>
        <view class="close_box" @click="close">
            <uni-icons type="closeempty" size="30" color=""></uni-icons>
        </view>
    </uni-popup>
</template>
<script>
// import api from "../../../common/js/allApi";
export default {
    props: {
        IsconvertibilityPopup: {
            type: Boolean,
            default: false,
        }
    },
    data() {
        return {
            value: '',

        };
    },
    watch: {
        IsCommodityPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.open();
                }
            },
            immediate: true
        }
    },
    mounted() {
        // this.$refs.popup.open('center')
    },
    methods: {
        open() {
            this.$refs.popup.open('center')
        },
        close() {
            this.$refs.popup.close('center')
        }
    },
};
</script>
<style lang="scss" scoped>
.succeed_box {
    width: 600rpx;
    height: 600rpx;
    padding-top: 230rpx;
    box-sizing: border-box;
    background-image: url("https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684216818731.png");
    background-size: 600rpx 600rpx;
    .bar_code_box {
        width: 500rpx;
        height: 160rpx;
        margin: 0 auto;
        background-image: url("https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684217101142.png");
        background-size: 500rpx 160rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        .voucher_information_box {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            height: 160rpx;
            width: 340rpx;
            padding: 40rpx 0;
            padding-left: 30rpx;
            box-sizing: border-box;
            color: rgba(17, 18, 17, 0.6);
            font-size: 24rpx;
            .voucher_information_title {
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                font-weight: 600;
            }
        }
        .quantity {
            width: 160rpx;
            height: 160rpx;
            color: #000;
            font-size: 48rpx;
            font-weight: 600;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }
}
.convertibility_box {
    width: 600rpx;
    height: 500rpx;
    padding-top: 180rpx;
    box-sizing: border-box;
    background-image: url("https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684216544023.png");
    background-size: 600rpx 500rpx;
    .input_meu {
        width: 500rpx;
        height: 100rpx;
        background-color: #f3f3f3;
        border-radius: 20rpx;
        margin: 0 auto;
        padding: 0rpx 20rpx;
        display: flex;
        align-items: center;
    }
}
.conversion_button_box {
    width: 500rpx;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 100rpx;
    background-color: rgba(0, 118, 164, 1);
    color: #ffffff;
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    margin: 0 auto;
    margin-top: 80rpx;
}
.close_box {
    width: 100rpx;
    height: 100rpx;
    background-color: rgba(255, 255, 255, 1);
    text-align: center;
    margin: 0 auto;
    margin-top: 50rpx;
    border-radius: 50rpx;
    line-height: 100rpx;
}
</style>
<template>
    <view>
        <uni-popup ref="popup" type="center" :is-mask-click="false">
            <view class="authorization_box">
                <view class="title_box">提示</view>
                <view class="succeed">授权成功！</view>
                <navigator open-type="exit" target="miniProgram" class="close">确定</navigator>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "common/js/allApi.js";
export default {

    data() {
        return {
        }
    },
    onShow() {
        uni.showLoading({
            title: '加载中',
            mask: true
        });

    },
    onLoad(options) {
        console.log("公众号返回参数：", options);
        uni.setStorageSync("h5userId", options.h5userId);
        uni.setStorageSync("h5openId", options.h5openId);
        if (uni.getStorageSync("staffCode") != undefined && uni.getStorageSync("staffCode") != '') {
            this.$api.put(this, api.authorization, {
                distributorId: uni.getStorageSync("distributorId"),
                openIds: [
                    {
                        appId: uni.getAccountInfoSync().miniProgram.appId,
                        openId: uni.getStorageSync("openId"),
                        openType: 2//1 微信公众号 2 微信小程序 3 支付宝
                    },
                    {
                        appId: 'wx7f20d1112a4e3559',
                        openId: uni.getStorageSync("h5openId") || 1,
                        openType: 1//1 微信公众号 2 微信小程序 3 支付宝
                    }
                ],
                shopCode: uni.getStorageSync("shopCode"),
                staffCode: uni.getStorageSync("staffCode")
            }).then((res) => {
                uni.hideLoading()
                //如果是分享申请进入
                if (uni.getStorageSync("applyfor")) {
                    uni.reLaunch({
                        url: '/good_pages/distribution/user/apply_for'
                    });
                } else {
                    this.$refs.popup.open('center');
                    uni.removeStorage("staffCode")
                }
            })
        } else {
            uni.hideLoading()
            //如果是分享申请进入
            if (uni.getStorageSync("applyfor")) {
                uni.reLaunch({
                    url: '/good_pages/distribution/user/apply_for'
                });
            }
            else {
                uni.switchTab({
                    url: '/src/components/index/index'
                })
            }
        }

    },
    methods: {

    }
}
</script>
<style scoped lang="scss">
.authorization_box {
    width: 500rpx;
    height: 300rpx;
    background-color: #ffffff;
    border-radius: 20rpx;
    .title_box {
        width: 500rpx;
        line-height: 80rpx;
        text-align: center;
        font-size: 36rpx;
        font-weight: 600;
    }
    .succeed {
        line-height: 120rpx;
        text-align: center;
        width: 500rpx;
        font-size: 30rpx;
    }
    .close {
        text-align: center;
        height: 100rpx;
        line-height: 100rpx;
        border-top: 1rpx solid #f5f5f5;
    }
}
</style>

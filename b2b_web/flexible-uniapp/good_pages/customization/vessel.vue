
<template>
    <view>
        <web-view :src="web_view_url"></web-view>
    </view>
</template>
<script>

export default {
    data() {
        return {
            web_view_url: '',
            windowHeight: 0,
            modelHeight: 0,
            ratio: 1,
            parameterImage: '',
            phoneName: '',
            Isimage: true,
        }
    },
    onShow() {
        uni.hideLoading()
    },
    onLoad(parameter) {

        wx.showShareMenu({
            withShareTicket: true,
            //设置下方的Menus菜单，才能够让发送给朋友与分享到朋友圈两个按钮可以点击
            menus: ["shareAppMessage", "shareTimeline"]
        })

        this.windowHeight = uni.getStorageSync("nakedHeight") - 140;
        this.modelHeight = this.windowHeight - 200;
        this.ratio = uni.getStorageSync("ratio");
        //首页 tabBar栏定制索引
        uni.setStorageSync("index", 1)
        if (parameter.image != undefined) {
            this.parameterImage = parameter.image;
        }
        this.phoneName = uni.getStorageSync("modelNoOrName");
        // https://diy.bat.com/wxh5/
        // https://test.bat.com/
        this.web_view_url = 'https://diy.bat.com/wxh5/good_pages/customization/webView' +
            '?windowHeight=' + this.windowHeight +
            '&modelHeight=' + this.modelHeight +
            '&ratio=' + this.ratio +
            '&image=' + this.parameterImage +
            '&phoneName=' + this.phoneName +
            '&distributorId=' + uni.getStorageSync("distributorId") +
            '&platform=' + uni.getStorageSync("platform") +
            '&devicePixelRatio=' + uni.getStorageSync("devicePixelRatio") +
            '&auth=' + uni.getStorageSync("auth") +
            '&appId=' + uni.getAccountInfoSync().miniProgram.appId;
        console.log(this.web_view_url);
    },
    methods: {

    }
}
</script>
<style scoped lang="scss">
.default_image {
    position: absolute;
    height: 296rpx;
    z-index: 1;
    max-width: 200rpx;
}
.btn {
    padding: 10rpx 20rpx;
    float: left;
    margin: 10rpx;
    border: solid 1px #dfdfdf;
    border-radius: 10rpx;
}
.home {
    width: 100vw;
    height: 100vh;
    display: flex;
    flex-direction: column;
    justify-content: space-around;
}
.operation_type_box {
    width: 750rpx;
    height: 120rpx;
    // background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    display: flex;
    justify-content: space-between;
    align-items: center;
    position: absolute;
    bottom: 100rpx;
    // padding: 0 50rpx;
    // box-sizing: border-box;
    padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
    padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
    .intelligibility_box {
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        font-family: PingFangSC-regular;
        width: 80rpx;
        height: 80rpx;
        background-color: rgba(255, 255, 255, 0.25);
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        border-radius: 40rpx;
        padding-top: 10rpx;
        box-sizing: border-box;
        image {
            width: 40rpx;
            height: 40rpx;
        }
        view {
            margin-top: -14rpx;
            -webkit-transform: scale(0.8);
        }
    }
    .phone_box {
        width: 440rpx;
        height: 80rpx;
        border-radius: 100rpx;
        background-color: rgba(255, 255, 255, 0.5);
        color: rgba(44, 44, 44, 1);
        font-size: 24rpx;
        text-align: center;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
        font-family: Arial;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20rpx;
        box-sizing: border-box;
        .phone_name {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 340rpx;
            .phone_name1 {
                overflow: hidden;
                white-space: nowrap;
                text-overflow: ellipsis;
                -o-text-overflow: ellipsis;
                width: 320rpx;
            }
        }
        .phone_price {
            width: 60rpx;
            color: rgba(252, 126, 27, 1);
            font-size: 20rpx;
            text-align: center;
            font-family: PingFangSC-regular;
        }
    }
}
.drawing_board_box {
    width: 750rpx;
    // background-color: #f5f5f5;
    margin: 0 auto;
}
</style>
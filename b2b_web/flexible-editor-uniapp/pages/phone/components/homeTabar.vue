<template>
    <view class="tab_bar_box">
        <view class="tab_bor_meu" @click="skip('/src/components/index/index')">
            <image src="../../../static/icons/home_page/home.png"></image>
            <view>主页</view>
        </view>
        <view class="tab_bor_meu" @click="skip('/src/tabBar/works/works')">
            <image src="../../../static/icons/home_page/production.png"></image>
            <view>作品</view>
        </view>
        <view class="tab_bor_meu">
            <image src="../../../static/icons/home_page/customization_select.png"></image>
            <view>定制</view>
        </view>
        <view class="tab_bor_meu" @click="skip('/src/tabBar/shopping/shopping')">
            <image src="../../../static/icons/home_page/shopping.png"></image>
            <view>购物</view>
        </view>
        <view class="tab_bor_meu" @click="skip('/src/tabBar/my/my')">
            <image src="../../../static/icons/home_page/my.png"></image>
            <view>我的</view>
        </view>
    </view>
</template>
<script>
export default {
    data() {
        return {}
    },
    methods: {
        skip(url) {
            uni.setStorageSync("index", 0)
            uni.switchTab({
                url: url
            });
        },
    }
}
</script>
<style scoped lang="scss">
.tab_bar_box {
    bottom: 0;
    position: fixed;
    width: 750rpx;
    height: 84rpx;
    border-top: 1rpx solid rgba(255, 255, 255, 0.25);
    background-color: rgba(255, 255, 255, 1);
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
    padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2
    .tab_bor_meu {
        width: 150rpx;
        text-align: center;
        // display: flex;
        // flex-direction: column;
        // justify-content: space-between;
        color: rgba(44, 44, 44, 1);
        font-size: 10px;
        font-family: PingFangSC-regular;
        image {
            width: 44rpx;
            height: 44rpx;
        }
    }
}
</style>
<template>
    <view>
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar left-icon="left" :border="false" @clickLeft="back" dark :fixed="false" background-color="transparent" color="#ffffff" status-bar title="我的团队" />
        <!-- #endif -->
        <view class="top_bg_box" :style="{marginTop: globalData.navHeight+'rpx'}">
            <!-- #ifdef H5 -->
            <view class="h5_nav_bar">
                <view style="width: 30rpx;" @click="back">
                    <uni-icons type="back" size="24" color="#ffffff"></uni-icons>
                </view>
                <view>我的团队</view>
                <view style="width: 30rpx;"></view>
            </view>
            <!-- #endif -->
            <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>

        </view>
        <view class="team_box">
            <view class="team_title_box">
                <view>已合作店铺</view>
                <view class="apply_for">
                    <uni-icons type="upload-filled" size="14" color="#59ADF8"></uni-icons>
                    申请
                </view>
            </view>
            <scroll-view scroll-y="true" class="balance_detail_y">
                <view class="team_item_box" v-for="item in 20" :key="item">
                    <view class="shop_information">
                        <view class="shop_header_box">
                            <image class="shop_img" src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>
                            <view class="shop_name_box">
                                <view>vivo深圳南山官方旗舰店</view>
                                <view class="shop_code">店铺号：5461615</view>
                            </view>
                        </view>
                        <view>
                            <uni-icons type="color" size="30"></uni-icons>
                        </view>
                    </view>
                    <view class="principal_box">
                        <view>负责人：唐*生</view>
                        <view>联系电话：155****5651</view>
                    </view>
                </view>
            </scroll-view>
        </view>
    </view>
</template>
<script>

export default {
    data() {
        return {
            globalData: {
                statusBarHeight: 0, // 状态导航栏高度
                navHeight: 0, // 总体高度
                navigationBarHeight: 0, // 导航栏高度(标题栏高度)
            },
        };
    },
    onShow() {
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
    },
    methods: {
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.team_box {
    width: 750rpx;
    height: 84vh;
    background-color: #efeff4;
    border-radius: 50rpx 50rpx 0 0;
    margin-top: -250rpx;
    .team_title_box {
        width: 690rpx;
        height: 116rpx;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        color: rgba(16, 16, 16, 1);
        font-size: 24rpx;
        .apply_for {
            display: flex;
            align-items: center;
            color: rgba(89, 173, 248, 1);
            font-size: 24rpx;
        }
    }
    .balance_detail_y {
        height: 78vh;
        width: 690rpx;
        margin: 0 auto;
        .team_item_box {
            width: 690rpx;
            height: 240rpx;
            border-radius: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            margin-bottom: 30rpx;
            .shop_information {
                width: 590rpx;
                height: 160rpx;
                margin: 0 auto;
                display: flex;
                align-items: center;
                justify-content: space-between;
                border-bottom: 1rpx solid rgba(245, 245, 245, 1);
                .shop_header_box {
                    display: flex;
                    align-items: center;
                    .shop_img {
                        width: 90rpx;
                        height: 90rpx;
                        border-radius: 20rpx;
                        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
                    }
                    .shop_name_box {
                        margin-left: 30rpx;
                        color: rgba(16, 16, 16, 1);
                        font-size: 28rpx;
                        .shop_code {
                            color: rgba(129, 126, 141, 1);
                            font-size: 24rpx;
                        }
                    }
                }
            }
            .principal_box {
                width: 590rpx;
                height: 80rpx;
                margin: 0 auto;
                display: flex;
                align-items: center;
                justify-content: space-between;
                color: rgba(136, 136, 136, 1);
                font-size: 20rpx;
            }
        }
    }
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
}
</style>

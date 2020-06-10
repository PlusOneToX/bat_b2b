<template>
    <view class="tab_bar_box">
        <view class="tab_bor_meu" @click="mapdepot">
            <image src="../../../static/icons/map_depot.png"></image>
            <view>图库</view>
        </view>
        <view class="tab_bor_meu" @click="onAddImage">
            <image src="../../../static/icons/uploading.png"></image>
            <view>传图</view>
        </view>
        <view class="tab_bor_meu" @click="handleCanva" v-if="platform==7">
            <image src="../../../static/icons/template.png"></image>
            <view>模板</view>
        </view>
        <view class="tab_bor_meu" @click="tags" v-if="platform!=7">
            <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com//diy/h51677064273287.png"></image>
            <view>贴纸</view>
        </view>
        <view class="tab_bor_meu" @click="onAddtext">
            <image src="../../../static/icons/text.png"></image>
            <view>文字</view>
        </view>
        <view class="tab_bor_meu" @click="onCoverage">
            <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com//diy/h51677064235519.png"></image>
            <view>图层</view>
        </view>
    </view>
</template>
<script>
export default {
    data() {
        return {
            platform: 1,
        }
    },
    mounted() {
        this.platform = uni.getStorageSync("platform");
    },
    methods: {
        //获取贴纸
        tags() {
            this.$emit("Iscanvas", false);
            this.$emit("ontags", '');
        },
        //获取图库
        mapdepot() {
            this.$emit("Iscanvas", false);
            this.$emit("mapdepot", '');
        },
        //上传图片
        onAddImage() {
            this.$emit("Iscanvas", false);
            this.$emit("addImag", '');
        },
        //选择模板
        handleCanva() {
            uni.setStorageSync("setText", false);
            this.$emit("Iscanvas", false);
            this.$emit("handleCanva", '');
        },
        //上传文字
        onAddtext() {
            this.$emit("Iscanvas", false);
            this.$emit("onAddtext", '');
        },
        //编辑图层
        onCoverage() {
            // this.$emit("Iscanvas", false);
            this.$emit("onCoverage", '');
        }
    }
}
</script>
<style scoped lang="scss">
.tab_bar_box {
    z-index: 21;
    bottom: 0;
    position: fixed;
    width: 750rpx;
    height: 90rpx;
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
        view {
            margin-top: -4rpx;
        }
    }
}
</style>
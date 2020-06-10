<template>
    <view>
        <uni-popup ref="popup" type="center" :safeArea="false" @maskClick="close">
            <view class="center_box">
                <view class="center_title">图片效果说明</view>
                <view class="center_txt">
                    <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com//diy/h51677064141353.png"></image>当前图片质量模糊，不便用于印刷
                </view>
                <view class="center_txt">
                    <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com//diy/h51677064127694.png"></image>当前图片质量一般，建议调整优化
                </view>
                <view class="center_txt">
                    <image src="https://bat-com.oss-cn-shenzhen.aliyuncs.com//diy/h51677064106059.png"></image>当前图片质量清晰，适合用于印刷
                </view>
            </view>
            <view style="text-align: center;margin-top: 60rpx;">
                <uni-icons type="clear" color="#FFFFFF" size="60" @click="close"></uni-icons>
            </view>
        </uni-popup>
    </view>
</template>
<script>
export default {
    props: {
        ResultPopup: {
            type: Boolean,
            default: '',
        }
    },
    data() {
        return {
            data: []
        }
    },
    watch: {
        ResultPopup: {
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
            this.$refs.popup.close();
            this.$emit("Iscanvas", true);
        }
    }
}
</script>
<style scoped lang="scss">
.center_box {
    width: 640rpx;
    height: 540rpx;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    border: 1rpx solid rgba(239, 239, 239, 1);
    .center_title {
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
        text-align: center;
        font-weight: 600;
        height: 120rpx;
        line-height: 120rpx;
    }
    .center_txt {
        display: flex;
        align-items: center;
        margin: 0 auto;
        width: 500rpx;
        color: #595959;
        font-size: 28rpx;
        height: 120rpx;
        line-height: 120rpx;
        image {
            width: 50rpx;
            height: 50rpx;
            margin-right: 20rpx;
        }
    }
}
</style>
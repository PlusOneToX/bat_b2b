<template>
    <view>
        <view class="picture_List_box">
            <view class="picture_meu_box" v-for="(item,index) in quantity" :key="item" @click="changeImage(index)">
                <image mode="scaleToFill" class="picture_meu_img" :src="'../../../static/canva/'+item+'.jpg'"></image>
            </view>
        </view>
        <view class="no_more">没有更多了哦 ~</view>
    </view>
</template>
<script>
export default {
    data() {
        return {
            quantity: 10,
            designList: [
                { id: 'EAFd5j3ipqM' },
                { id: 'EAFecn6_K_U' },
                { id: 'EAFecqXPmzc' },
                { id: 'EAFd0ScOeAM' },
                { id: 'EAFd0Qg4EtI' },
                { id: 'EAFecuJXTnU' },
                { id: 'EAFecplKBi8' },
                { id: 'EAFecuuweJM' },
                { id: 'EAFecjGf9X8' },
                { id: 'EAFeclcUOXU' },
                { id: 'EAFd0cn0HXM' },
                { id: 'EAFd0dg6xJA' },
                { id: 'EAFd0SVA2CI' },
                { id: 'EAFd0aOsKvQ' },
                { id: 'EAFecmTnE2c' },
                { id: 'EAFeclmubyU' },
                { id: 'EAFeckvacy4' },
                { id: 'EAFecqC6qlc' },
                { id: 'EAFd0Qe200Y' },
                { id: 'EAFechnL2RQ' },
                { id: 'EAFecjj05CU' },
                { id: 'EAFecqwRBGw' },
                { id: 'EAFechOZC9s' },
                { id: 'EAFd0dXmzGA' },
                { id: 'EAFd0TROFSw' },
                { id: 'EAFecnpNzFo' },
                { id: 'EAFecsW4r2A' },
                { id: 'EAFecu_KvVg' },
                { id: 'EAFectg9D1Y' },
                { id: 'EAFecgQ5eGs' }
            ]
        }
    },
    onLoad(option) {

    },
    onReachBottom() {
        if (this.quantity < 30) {
            this.quantity += 4;
        }
        this.bottomOut = !this.bottomOut;
    },
    methods: {
        changeImage(index) {
            uni.navigateTo({
                url: '/good_pages/customization/index?designId=' + this.designList[index].id
            });
        }
    }
}
</script>
<style scoped lang="scss">
.picture_meu_img {
    width: 160rpx;
    height: 296rpx;
    border-radius: 20rpx;
    box-shadow: 0rpx 4rpx 10rpx #828282;
}
.no_more {
    text-align: center;
    margin-top: 50rpx;
    font-size: 24rpx;
    color: rgba(59, 59, 59, 0.72);
    font-family: OPPOSans-bold;
    height: 300rpx;
}
.picture_List_box {
    width: 680rpx;
    margin: 0 auto;
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;

    .picture_meu_box {
        margin-top: 30rpx;
        width: 320rpx;
        height: 400rpx;
        border-radius: 30rpx;
        background-color: rgba(255, 255, 255, 1);
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.08);
        font-family: Arial;
        border: 1rpx solid rgba(239, 239, 239, 0.19);
        display: flex;
        justify-content: center;
        align-items: center;
    }
}
</style>
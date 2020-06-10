<template>
    <view class="picture_List_box">
        <view class="picture_meu_box" v-for="(item,index) in imgList" :key="index" @click="clickImg(item)">
            <image v-if="withFillingImg=='6'||withFillingImg==''" mode="scaleToFill" :src="item.originImage+'?x-oss-process=image/resize,h_200,l_200'" class="picture_meu_img" :style="{height:imagehigh+'rpx',width:imagewide+'rpx'}"></image>
            <image v-else mode="scaleToFill" :src="item.originImage+'?x-oss-process=image/resize,h_200,l_200'" :style="{height:imagehigh+'rpx',width:imagewide+'rpx'}"></image>
            <image mode="scaleToFill" :src="defaultImage" class="default_image" :style="{height:modelhigh+'rpx',width:modelwide+'rpx',zIndex: 9}"></image>
            <image mode="scaleToFill" :src="withFillingImg" class="default_image" :style="{height:modelhigh+'rpx',width:modelwide+'rpx',zIndex: 8}"></image>
        </view>
        <view class="no_more">没有更多了哦 ~</view>
    </view>
</template>
<script>
import api from "../common/js/allApi.js";
export default {
    props: {
        imgTypeId: {
            type: Number,
            default: 0,
        }
    },
    data() {
        return {
            withFillingImg: '',
            imgList: [],
            defaultImage: '',
            aspectRatio: 1,
            frameRatio: 1,
            modelhigh: '',//模型高
            modelwide: '',//模型宽
            imagehigh: '300',
            imagewide: '160',
            leftFrame: 0,
            topFrame: 0
        }
    },
    watch: {
        imgTypeId: {
            handler(newName, oldName) {
                if (newName != 0) {
                    this.withFillingImg = uni.getStorageSync("withFillingImg");
                    this.defaultImage = uni.getStorageSync("defaultImage");
                    this.aspectRatio = uni.getStorageSync("aspectRatio");
                    this.frameRatio = uni.getStorageSync("frameRatio");
                    this.leftFrame = (uni.getStorageSync("leftFrame") * 2) * this.frameRatio;
                    this.topFrame = (uni.getStorageSync("topFrame") * 2) * this.frameRatio;
                    if (this.aspectRatio != '') {
                        this.modelhigh = 300;
                        this.modelwide = 300 / this.aspectRatio;
                        this.imagehigh = 300 + 1 - this.topFrame;
                        this.imagewide = (this.modelwide + 1) - this.topFrame;
                    }
                    this.getimgList(newName);
                }
            },
            immediate: true
        }
    },
    methods: {
        clickImg(e) {
            // #ifdef MP-WEIXIN
            uni.navigateTo({
                url: '/good_pages/customization/vessel?image=' + e.originImage
            });
            // #endif
            // #ifdef H5
            uni.navigateTo({
                url: '/good_pages/customization/index?image=' + e.originImage + '&pictureId=' + e.id
            });
            // #endif
        },
        getimgList(id) {
            this.$api.get(this, api.getImageList, {
                page: 1,
                size: 999,
                categoryId: id
            }).then((res) => {
                this.imgList = res.data.list;
                uni.hideLoading()
            })
        },
    }
}
</script>
<style scoped lang="scss">
.default_image {
    position: absolute;
    // height: 296rpx;
    max-width: 250rpx;
}
.picture_meu_img {
    border-radius: 20rpx;
    // height: 296rpx;
    // max-width: 150rpx;
    box-shadow: 0rpx 4rpx 10rpx #828282;
}
.no_more {
    width: 680rpx;
    text-align: center;
    margin-top: 100rpx;
    height: 300rpx;
    font-size: 24rpx;
    color: rgba(59, 59, 59, 0.72);
    font-family: OPPOSans-bold;
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
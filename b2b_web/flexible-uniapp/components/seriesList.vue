<template>
    <view class="seriesList_box">
        <view class="seriesList_head_box">
            <view class="seriesList_head_title">{{item.themeName}}</view>
            <view class="seriesList_head_number" @click="jump(ThemeDateil,item.themeName)">共{{ThemeDateil.length}}款 &nbsp; <uni-icons type="forward" size="20"></uni-icons>
            </view>
        </view>
        <view class="seriesList_img_box">
            <view class="picture_meu_box" v-for="(item,index) in ThemeDateil" :key="index" v-if="(index<3)" @click="clickImg(item)">
                <image v-if="withFillingImg=='6'||withFillingImg==''" mode="scaleToFill" :src="item.originImage+'?x-oss-process=image/resize,h_200,l_200'" class="picture_meu_img" :style="{height:imagehigh+'rpx',width:imagewide+'rpx'}"></image>
                <image v-else mode="scaleToFill" :src="item.originImage+'?x-oss-process=image/resize,h_200,l_200'" :style="{height:imagehigh+'rpx',width:imagewide+'rpx'}"></image>
                <image mode="scaleToFill" :src="defaultImage" class="default_image" :style="{height:modelhigh+'rpx',width:modelwide+'rpx',zIndex: 9}"></image>
                <image mode="scaleToFill" :src="withFillingImg" class="default_image" :style="{height:modelhigh+'rpx',width:modelwide+'rpx',zIndex: 8}"></image>
            </view>
        </view>
    </view>
</template>
<script>
import api from "../common/js/allApi.js";
export default {
    props: {
        themeId: {
            type: Number,
            default: 0,
        },
        item: {
            type: Object,
            default: {},
        },
    },
    data() {
        return {
            withFillingImg: '',
            ThemeDateil: [],
            defaultImage: '',
            aspectRatio: 1,
            frameRatio: 1,
            modelhigh: '200',//模型高
            modelwide: '100',//模型宽
            imagehigh: '200',
            imagewide: '100',
            topFrame: 0
        }
    },
    mounted() {
        this.withFillingImg = uni.getStorageSync("withFillingImg");
        this.defaultImage = uni.getStorageSync("defaultImage");
        if (this.defaultImage != '') {
            this.aspectRatio = uni.getStorageSync("aspectRatio");
            this.frameRatio = uni.getStorageSync("frameRatio");
            this.topFrame = (uni.getStorageSync("topFrame") * 2) * this.frameRatio;
            if (this.aspectRatio != '') {
                this.modelhigh = 200;
                this.modelwide = 200 / this.aspectRatio;
                this.imagehigh = (200 + 6) - this.topFrame;
                this.imagewide = (this.modelwide + 4) - this.topFrame;
            }
        }



        if (this.themeId != 0) {
            this.getThemeDateil();
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
        jump(item, title) {
            uni.navigateTo({
                url: '/good_pages/theme/themeList?item=' + JSON.stringify(item) + '&title=' + title
            });
        },
        getThemeDateil() {
            this.$api.get(this, api.getThemeDateil, {
                seriesId: this.themeId,
                page: 1,
                size: 999
            }).then((res) => {
                this.ThemeDateil = res.data.list;
                uni.hideLoading()
            })
        },
    }
}
</script>
<style scoped lang="scss">
.picture_meu_box {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 108rpx;
    height: 200rpx;
    margin-right: 50rpx;
    margin-left: 55rpx;
    .picture_meu_img {
        // max-height: 200rpx;
        // max-width: 100rpx;
        border-radius: 10rpx;
        box-shadow: 0rpx 4rpx 10rpx #828282;
    }
    .default_image {
        position: absolute;
        // max-height: 200rpx;
        max-width: 100rpx;
    }
}

.seriesList_box {
    width: 680rpx;
    height: 340rpx;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    text-align: center;
    box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.08);
    margin: 0 auto;
    margin-top: 30rpx;
    padding: 20rpx;
    box-sizing: border-box;

    .seriesList_img_box {
        height: 250rpx;
        width: 640rpx;
        display: flex;
        align-items: flex-end;

        // image {
        //     // width: 108rpx;
        //     height: 200rpx;
        //     margin-right: 50rpx;
        //     margin-left: 55rpx;
        // }
    }

    .seriesList_head_box {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .seriesList_head_title {
            color: rgba(59, 59, 59, 1);
            font-size: 28rpx;
            text-align: left;
            font-family: OPPOSans-bold;
        }

        .seriesList_head_number {
            color: rgba(59, 59, 59, 0.72);
            font-size: 24rpx;
            text-align: left;
            font-family: OPPOSans-bold;
            display: flex;
            align-items: center;
        }
    }
}
</style>
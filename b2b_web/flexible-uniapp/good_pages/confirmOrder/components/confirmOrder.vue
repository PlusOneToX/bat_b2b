<template>
    <view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" @maskClick="close">
            <view class="sele_img_box">
                <view class="sele_img_title_box">
                    <view>确认信息</view>
                    <uni-icons type="closeempty" size="26" @click="close"></uni-icons>
                </view>
                <view class="uni-margin-wrap">
                    <swiper class="swiper" @change="changeSwiper" indicator-color="#CACACA" indicator-active-color="#545353" circular :indicator-dots="indicatorDots" :autoplay="autoplay" :interval="interval" :duration="duration">
                        <swiper-item class="swiper_item" v-for="(item,index) in BannerList" :key="index">
                            <image mode="heightFix" :src="(item.diy.previewImage)+'?x-oss-process=image/resize,h_400,l_400/'"></image>
                        </swiper-item>
                    </swiper>
                </view>
                <view class="order_message">
                    <view style="display: flex;align-items: center;">
                        <image class="address_icon" src="../../../static/icons/phone.png"></image>
                        {{BannerList[current].diy.modelName}}
                    </view>
                    <view style="display: flex;align-items: center;">
                        <image class="address_icon" src="../../../static/icons/coverage.png"></image>
                        {{BannerList[current].diy.materialName}}
                    </view>
                </view>
                <view class="address_box">
                    <view class="address_title_box">
                        <image class="address_icon" src="../../../static/images/url2.png"></image>
                        {{ addFormData.provinceName ? addFormData.provinceName : "" }}
                        {{ addForm.cityName ? addForm.cityName : "" }}
                        {{ addForm.districtName ? addForm.districtName : "" }}
                    </view>
                    <view class="detailed_address">
                        {{ addForm.address }}
                    </view>
                    <view class="linkman_box">
                        {{ addForm.userName }} &nbsp&nbsp&nbsp&nbsp {{ addForm.mobile }}
                    </view>
                </view>
                <view class="check_box">请核对信息，定制商品制作后不支持修改及无理由退换货</view>

                <view class="check_box" style="margin-top: 10rpx;">
                    <view style="color: #515151;" @click="agreement=!agreement">
                        <radio value="r1" :checked="agreement" style="transform:scale(0.7)" color="#0076A4"></radio>
                    </view>
                    <view style="color: #515151;" @click="agreement=!agreement">
                        勾选即代表您同意
                    </view>
                    <view @click="goAgreement">《BAT商城服务协议》</view>
                </view>

                <view class="ack_button">
                    <view class="bottom_button_box">
                        <view class="return" @click="close">返回</view>
                        <view class="be_confirmed" @click="confirmed">确认无误</view>
                    </view>
                </view>
            </view>
        </uni-popup>
    </view>
</template>
<script>
export default {
    props: {
        OrderPopup: {
            type: Boolean,
            default: '',
        },
        addForm: {
            type: Object,
            default: {},
        }
    },
    data() {
        return {
            agreement: false,
            indicatorDots: true,
            autoplay: false,
            interval: 2000,
            duration: 500,
            BannerList: [],
            current: 0,
            addFormData: ''
        }
    },
    watch: {
        OrderPopup: {
            handler(newName, oldName) {
                if (oldName != undefined) {
                    this.agreement = false;
                    this.open();
                }
            },
            immediate: true
        }
    },
    mounted() {
        this.BannerList = JSON.parse(uni.getStorageSync("goods"));
    },
    methods: {
        goAgreement() {
            this.$emit("goAgreement", '');
        },
        confirmed() {
            if (this.agreement) {
                this.$refs.popup.close('bottom');
                this.$emit("getDeliveryStopStatus", true);
            } else {
                uni.showToast({
                    icon: 'none',
                    title: '请阅读并勾选服务协议',
                    duration: 2000
                });
            }
        },
        changeSwiper(e) {
            this.current = e.detail.current;
        },
        open() {

            this.addFormData = this.addForm;
            this.$refs.popup.open('bottom')
        },
        close() {
            this.$refs.popup.close('bottom')
        }
    }
}
</script>
<style scoped lang="scss">
.sele_img_box {
    width: 750rpx;
    height: 1000rpx;
    border-radius: 40rpx 40rpx 0rpx 0rpx;
    background-color: rgba(253, 253, 253, 1);
    color: rgba(16, 16, 16, 1);
    font-family: Arial;
    border-top: 1rpx solid rgba(232, 232, 232, 0.5);
    .ack_button {
        width: 750rpx;
        height: 80rpx;
        position: absolute;
        bottom: 20px;
        .bottom_button_box {
            width: 700rpx;
            height: 80rpx;
            margin: 0 auto;
            display: flex;
            align-items: center;
            .return {
                width: 350rpx;
                height: 80rpx;
                line-height: 80rpx;
                border-radius: 50rpx 0rpx 0rpx 50rpx;
                background-color: rgba(0, 118, 164, 1);
                color: #ffffff;
                font-size: 28rpx;
                text-align: center;
            }
            .be_confirmed {
                width: 350rpx;
                height: 80rpx;
                line-height: 80rpx;
                border-radius: 0rpx 50rpx 50rpx 0rpx;
                background-color: #fc7e1b;
                color: #ffffff;
                font-size: 28rpx;
                text-align: center;
            }
        }
    }
    .check_box {
        color: rgba(0, 118, 164, 1);
        font-size: 24rpx;
        margin: 0 auto;
        margin-top: 20rpx;
        text-align: center;
        display: flex;
        justify-content: center;
        align-items: center;
        // height: 80rpx;
        // line-height: 80rpx;
    }
    .address_box {
        width: 710rpx;
        height: auto;
        border-radius: 30rpx;
        background-color: rgba(253, 253, 253, 1);
        color: rgba(16, 16, 16, 1);
        border: 1rpx solid rgba(232, 232, 232, 0.5);
        margin: 0 auto;
        margin-top: 20rpx;
        padding: 20rpx;
        box-sizing: border-box;
        .linkman_box {
            margin-top: 10rpx;
            color: rgba(164, 164, 164, 1);
            font-size: 24rpx;
        }
        .detailed_address {
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            margin-top: 10rpx;
        }
        .address_title_box {
            display: flex;
            align-items: center;
            color: rgba(84, 83, 83, 1);
            font-size: 24rpx;
            .address_icon {
                width: 50rpx;
                height: 50rpx;
                margin-right: 20rpx;
            }
        }
    }
    .order_message {
        width: 710rpx;
        height: 92rpx;
        border-radius: 30rpx;
        background-color: rgba(253, 253, 253, 1);
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        border: 1rpx solid rgba(232, 232, 232, 0.5);
        color: rgba(97, 96, 96, 1);
        font-size: 24rpx;
        margin: 0 auto;
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 0 20rpx;
        box-sizing: border-box;
        .address_icon {
            width: 40rpx;
            height: 40rpx;
            margin-right: 20rpx;
        }
    }
    .sele_img_title_box {
        display: flex;
        justify-content: space-between;
        align-items: center;
        width: 750rpx;
        height: 80rpx;
        padding: 0 40rpx;
        box-sizing: border-box;
        color: rgba(59, 59, 59, 1);
        font-size: 32rpx;
    }
    .uni-margin-wrap {
        width: 100%;
        margin-bottom: 30rpx;

        /deep/ .uni-swiper-dots {
            // 指示点整个区域的位置
            top: 420rpx;
        }

        .swiper {
            height: 350rpx;

            .swiper_item {
                height: 330rpx;
                text-align: center;
                image {
                    height: 296rpx;
                    max-width: 150rpx;
                }
            }
        }
    }
}
</style>
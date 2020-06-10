<template>
    <view>
        <uni-nav-bar leftIcon="back" @clickLeft="back" dark :fixed="true" :border="false" background-color="transparent" color="#ffffff" status-bar title="定制您的专属手机壳" />
        <view class="centre_box">
            <image mode="aspectFit" class="centre_logo" src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684375609922.png"></image>
            <view class="redeem_code_box">
                <view class="redeem_title">定制手机壳权益验证</view>
                <view class="input_meu">
                    <input :adjust-position="false" placeholder="请输入短信中的验证码" v-model="value" />
                </view>
                <view class="conversion_button_box" @click="certificate">立即验证</view>
            </view>
            <view class="redeem_code_box">
                <view class="redeem_title">服务说明</view>
                <view class="exchange">
                    <view>1.每个验证码仅可使用一次，验证成功后，可在线传图订制，包邮到家。</view>
                    <view>2.如您已验证权益，可至小程序-我的-优惠券板块中查看并使用权益。</view>
                    <view> 3.在您提交定制订单后，订单不支持退款，有任何疑问可咨询在线客服。</view>
                    <view> 4.请您在易购下单后90天内完成手机壳定制，超期将无法验证且无法退款。</view>
                </view>
            </view>
        </view>
        <uni-popup ref="popup" type="bottom" :safeArea="false" :is-mask-click="false">
            <view class="succeed_box">
                <view class="bar_code_box">
                    <view class="voucher_information_box">
                        <view class="voucher_information_title">{{codeDate.couponDesc}}</view>
                        <view>{{dateToNum(codeDate.effectiveStartTime)}}-{{dateToNum(codeDate.effectiveEndTime)}}</view>
                    </view>
                    <view class="quantity">
                        1<view style="font-size: 28rpx;font-weight: 500;padding-top: 10rpx;">张</view>
                    </view>
                </view>
                <view class="hint_box">
                    注意:该兑换券只适用于部分机型、材质:
                    <view style="display: flex; flex-wrap: wrap;">
                        <view v-for="(phone,index) in codeDate.models" :key="phone.modelId">
                            {{index!=0?'、':''}}{{phone.modelName}}</view>
                        <view v-if="codeDate.models!=undefined&&codeDate.materials!=undefined">；</view>
                        <view v-for="(materials,inx) in codeDate.materials" :key="materials.materialId">
                            {{inx>0?'、':''}}
                            {{materials.materialName}}</view>
                        <view v-if="codeDate.models!=undefined||codeDate.materials!=undefined">；</view>
                    </view>
                </view>
                <view class="conversion_button_box" @click="goDiy">立即定制</view>
            </view>
            <view class="close_box" @click="close">
                <uni-icons type="closeempty" size="30" color=""></uni-icons>
            </view>
        </uni-popup>
    </view>
</template>
<script>
import api from "../../common/js/allApi";
export default {
    data() {
        return {
            value: '',
            codeDate: {}
        };
    },

    mounted() {
        // this.open();
    },
    methods: {
        goDiy() {
            uni.reLaunch({
                url: '/src/tabBar/custom/custom'
            });
        },
        dateToNum(val) {
            return val.substring(0, 10)
        },
        back() {
            uni.reLaunch({
                url: "/src/components/index/index",
            });
        },
        certificate() {
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            if (this.value == '') {
                uni.hideLoading()
                uni.showToast({
                    icon: 'none',
                    title: '请输入兑换码',
                    duration: 1500
                });
            }
            else {
                // 您已验证过权益，请至小程序-我的-优惠券板块中查看并使用权益。
                this.$api.post(this, api.suningVerifycode, {
                    verifyCode: this.value,
                }).then((res) => {
                    uni.hideLoading()
                    if (res.success) {
                        this.codeDate = res.data;
                        this.open();
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 1500,
                        });
                    }
                })
            }
        },
        open() {
            this.$refs.popup.open('center')
        },
        close() {
            this.$refs.popup.close('center')
        }
    },
};
</script>
<style lang="scss" scoped>
.close_box {
    width: 100rpx;
    height: 100rpx;
    background-color: rgba(255, 255, 255, 1);
    text-align: center;
    margin: 0 auto;
    margin-top: 50rpx;
    border-radius: 50rpx;
    line-height: 100rpx;
}
.succeed_box {
    width: 600rpx;
    height: 740rpx;
    padding-top: 230rpx;
    box-sizing: border-box;
    background-image: url("https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684377312135.png");
    background-size: 600rpx 740rpx;
    .hint_box {
        width: 500rpx;
        height: 40rpx;
        margin: 0 auto;
        font-size: 20rpx;
        // display: flex;
    }
    .bar_code_box {
        width: 500rpx;
        height: 160rpx;
        margin: 0 auto;
        background-image: url("https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684217101142.png");
        background-size: 500rpx 160rpx;
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-top: 130rpx;

        .voucher_information_box {
            display: flex;
            flex-direction: column;
            justify-content: space-between;
            height: 160rpx;
            width: 340rpx;
            padding: 40rpx 0;
            padding-left: 30rpx;
            box-sizing: border-box;
            color: rgba(17, 18, 17, 0.6);
            font-size: 24rpx;
            .voucher_information_title {
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                font-weight: 600;
            }
        }
        .quantity {
            width: 160rpx;
            height: 160rpx;
            color: #000;
            font-size: 48rpx;
            font-weight: 600;
            display: flex;
            justify-content: center;
            align-items: center;
        }
    }
    .conversion_button_box {
        width: 500rpx;
        height: 80rpx;
        line-height: 80rpx;
        border-radius: 100rpx;
        background-color: #fc6b13;
        color: #ffffff;
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        margin: 0 auto;
        margin-top: 50rpx;
    }
}
.centre_box {
    width: 750rpx;
    height: 100vh;
    position: absolute;
    top: 0;
    background-color: #fecf1d;
    .centre_logo {
        height: 160rpx;
        margin-top: 300rpx;
        margin-left: 75rpx;
    }
    .redeem_code_box {
        width: 690rpx;
        height: auto;
        background-color: #ffffff;
        border-radius: 20rpx;
        margin: 0 auto;
        padding: 50rpx 40rpx;
        box-sizing: border-box;
        margin-bottom: 40rpx;
        .exchange {
            color: rgba(17, 18, 17, 0.6);
            font-size: 30rpx;
            margin-top: 10rpx;
            view {
                margin-top: 10rpx;
            }
        }
        .redeem_title {
            font-size: 34rpx;
            font-weight: 600;
            color: #fb8204;
        }
        .input_meu {
            width: 600rpx;
            height: 100rpx;
            border: 1px solid rgba(255, 204, 123, 1);
            border-radius: 20rpx;
            padding: 0rpx 20rpx;
            display: flex;
            align-items: center;
            margin-top: 60rpx;
            box-sizing: border-box;
        }
        .conversion_button_box {
            width: 600rpx;
            height: 90rpx;
            line-height: 90rpx;
            border-radius: 100rpx;
            background-color: rgba(252, 107, 19, 1);
            color: #ffffff;
            font-size: 28rpx;
            text-align: center;
            font-family: Arial;
            margin-top: 60rpx;
        }
    }
}
</style>
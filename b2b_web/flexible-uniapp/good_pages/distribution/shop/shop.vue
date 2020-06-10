<template>
    <view>
        <view class="shop_box">
            <view class="shop_information">
                <view class="shop_header_box">
                    <image class="shop_img" :src="shopInfo.url"></image>
                    <view class="shop_name_box">
                        <view>{{shopInfo.shopName}}</view>
                        <view class="shop_code">店铺号：{{shopInfo.shopCode}}</view>
                    </view>
                </view>
                <view>
                    <!-- <uni-icons type="color" size="30"></uni-icons> -->
                </view>
            </view>
            <view class="shop_item_box">
                <view>负责人</view>
                <view class="shop_item">{{shopInfo.name}}</view>
            </view>
            <view class="shop_item_box">
                <view>手机号</view>
                <view class="shop_item">{{shopInfo.phone}}</view>
            </view>
        </view>
        <!-- <view class="shop_box">
            <view class="shop_item_box">
                <view>分销员自动审核</view>
                <view class="shop_item">
                    <switch @change="clickSwitch" :checked="customerShopCheck" color="#88BFFF" style="transform:scale(0.7)" />
                </view>
            </view>
            <view class="shop_item_box">
                <view>订单自动审核</view>
                <view class="shop_item">
                    <switch @change="clickAutoFlag" :checked="distributionAutoFlag" color="#88BFFF" style="transform:scale(0.7)" />
                </view>
            </view>
        </view> -->
        <view class="bom_sub" @click="loginOut" v-show="platform!=7">退出当前账号</view>
    </view>
</template>
<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            shopInfo: [],
            platform: 1,
            distributionAutoFlag: false,
            customerShopCheck: false
        };
    },
    onLoad() {
        this.platform = uni.getStorageSync("platform")
        this.ShopStatus();
        this.distributorSet();
    },
    methods: {
        clickSwitch(e) {
            this.customerShopCheck = e.detail.value;
            this.$api.put(this, api.distributorSet, {
                id: uni.getStorageSync("distributorId"),
                customerShopCheck: e.detail.value ? 1 : 0
            }).then((res) => {
            });
        },
        clickAutoFlag(e) {
            this.distributionAutoFlag = e.detail.value;
            this.$api.put(this, api.distributorSet, {
                id: uni.getStorageSync("distributorId"),
                distributionAutoFlag: e.detail.value ? 1 : 0
            }).then((res) => {
            });
        },
        loginOut() {
            uni.removeStorage("staffType");
            uni.removeStorage("userId");
            uni.removeStorage("phone");
            uni.removeStorage("customerShopCheck");//店铺审核：0 否 1.是;无返回或为零都为否
            uni.removeStorage("rxShopSwitch");//是否启用店铺，1启用 0 不启用;无返回或为零都为否
            uni.removeStorage("payWay");
            uni.removeStorage('goods');
            uni.removeStorage('auth');
            uni.removeStorage("aliAuthorize");//是否绑定支付宝
            uni.removeStorage("wxAuthorize");//是否绑定微信 
            uni.redirectTo({
                url: '../../login/login'
            });
        },
        //获取店铺信息
        ShopStatus() {
            this.$api.get(this, api.getShopStatus, {
                id: uni.getStorageSync("shopId"),
            }).then((res) => {
                this.shopInfo = res.data;
            });
        },
        distributorSet() {
            this.$api.get(this, api.distributorSet, {
                distributorId: uni.getStorageSync("distributorId"),
            }).then((res) => {
                console.log("店铺设置：", res);
                this.distributionAutoFlag = res.data.distributionAutoFlag == 1 ? true : false
                this.customerShopCheck = res.data.customerShopCheck == 1 ? true : false
            });
        }
    },
};
</script>

<style lang="scss" scoped>
.shop_box {
    width: 690rpx;
    height: auto;
    border-radius: 30rpx;
    background-color: rgba(255, 255, 255, 1);
    margin: 0 auto;
    margin-top: 30rpx;
    border: 1rpx solid rgba(239, 239, 239, 1);
    .shop_information {
        width: 590rpx;
        height: 180rpx;
        margin: 0 auto;
        display: flex;
        align-items: center;
        justify-content: space-between;
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
                width: 420rpx;
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
    .shop_item_box {
        height: 120rpx;
        width: 590rpx;
        margin: 0 auto;
        border-top: 1rpx solid rgba(239, 239, 239, 1);
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 20rpx;
        box-sizing: border-box;
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        .shop_item {
            color: rgba(154, 154, 154, 1);
            font-size: 24rpx;
        }
    }
}
.bom_sub {
    width: 600rpx;
    height: 80rpx;
    line-height: 80rpx;
    border-radius: 50rpx;
    background-color: rgba(0, 118, 164, 1);
    color: rgba(255, 255, 255, 1);
    font-size: 28rpx;
    text-align: center;
    font-family: Arial;
    border: 1rpx solid rgba(239, 239, 239, 1);
    position: absolute;
    bottom: 40rpx;
    left: 75rpx;
}
</style>

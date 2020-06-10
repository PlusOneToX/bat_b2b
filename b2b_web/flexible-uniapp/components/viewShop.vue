<template>
    <view class="shop_box">
        <view class="sjop_title_box">
            <view>商品清单</view>
            <veiw class="the_number_of">共计商品{{splitItemList.length}}件
                <uni-icons @click="shut_down" type="closeempty" size="30"></uni-icons>
            </veiw>
        </view>
        <scroll-view class="goods_list_box" scroll-y="true">
            <veiw class="goods_meu_list" v-for="(item, index) in splitItemList" :key="index">
                <view class="sc_list_lineImgBox">
                    <image :src="(item.diy.previewImage)+'?x-oss-process=image/resize,h_400,l_400/'" mode="aspectFit"></image>
                </view>
                <view class="sc-Rg">
                    <view class="sc-RgLf">
                        <view class="sc-list-bgRgName">
                            {{ item.itemName }}
                        </view>
                        <!-- <view class="sc-RgLf-info">
                            <text>编码：</text>
                            <text>{{ item.itemCode }}</text>
                        </view> -->
                        <view class="sc-RgLf-info" v-if="item.specsName && item.specsName != ''">
                            <text>规格：</text>
                            <text>{{ item.specsName }}</text>
                        </view>
                        <view class="sc-RgLf-info" v-if="item.colorName">
                            <text>颜色：</text>
                            <text>{{ item.colorName }}</text>
                        </view>
                        <view class="sc-RgLf-info" v-if="item.diy && item.diy.materialName">
                            <text>材质：</text>
                            <text>{{ item.diy.materialName }}</text>
                        </view>
                        <view class="sc-RgLf-info" v-if="item.diy && item.diy.modelName">
                            <text>机型：</text>
                            <text>{{ item.diy.modelName }}</text>
                        </view>
                        <view class="sc_list_lineItemBtm">
                            <view class="the_price">￥{{ fomatFloat(Number(item.salePrice), 2) }}</view>
                            <view>x{{item.count||item.itemCount}}</view>
                        </view>
                    </view>

                </view>
            </veiw>
        </scroll-view>
    </view>
</template>
<script>
export default {
    props: {
        'splitItemList': {
            type: Array,
            default: []
        },
    },
    data() {
        return {
            number: 0
        };
    },
    methods: {
        shut_down() {
            this.$emit('shut_down', 1)
        },
        fomatFloat(num, n) {
            var f = parseFloat(num);
            if (isNaN(f)) {
                return false;
            }
            // 判断第三位是否是五
            let ls = Math.round(4.225 * 1000)
                .toString()
                .split(".");
            let threeNum = ls[0].charAt(ls[0].length - 1);
            if (threeNum == 5) {
                num = (num * 1000 + 1) / 1000;
            }
            f = Math.round(num * Math.pow(10, n)) / Math.pow(10, n); // n 幂
            var s = f.toString();
            var rs = s.indexOf(".");

            // 判定如果是整数，增加小数点再补0
            if (rs < 0) {
                rs = s.length;
                s += ".";
            }
            while (s.length <= rs + n) {
                s += "0";
            }
            return s;
        },
    }
};
</script>

<style  lang="scss">
.shop_box {
    width: 750rpx;
    height: 1100rpx;
    background-color: #ffffff;
    z-index: 999999;
    border-radius: 30rpx 30rpx 0 0;
    padding-top: 20rpx;
    .sjop_title_box {
        width: 700rpx;
        height: 60rpx;
        margin: 0 auto;
        display: flex;
        align-items: center;
        justify-content: space-between;
        font-size: 28rpx;
        font-family: PingFangSC-regular;
        .the_number_of {
            display: flex;
            align-items: center;
            font-size: 24rpx;
        }
    }
    .goods_list_box {
        margin: 0 auto;
        margin-top: 20rpx;
        width: 690rpx;
        height: 1000rpx;
        .goods_meu_list {
            width: 690rpx;
            height: auto;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20rpx;
            .sc_list_lineImgBox {
                width: 200rpx;
                height: 200rpx;
                image {
                    width: 200rpx;
                    height: 200rpx;
                }
            }
            .sc-Rg {
                margin-left: 28rpx;
                width: 460rpx;
                display: flex;
                flex-direction: column;
                justify-content: space-between;
                .sc-RgLf {
                    height: auto;
                    display: flex;
                    flex-direction: column;
                    justify-content: space-between;
                    .sc-list-bgRgName {
                        font-size: 24rpx;
                        width: 460rpx;
                        display: -webkit-box;
                        -webkit-box-orient: vertical;
                        -webkit-line-clamp: 2;
                        overflow: hidden;
                    }
                    .sc-RgLf-info {
                        font-size: 20rpx;
                        display: flex;
                        margin-top: 8rpx;
                        text:nth-child(1) {
                            color: rgb(48, 48, 48);
                        }
                        text:nth-child(2) {
                            color: #999;
                            width: 320rpx;
                            overflow: hidden;
                            text-overflow: ellipsis;
                            white-space: nowrap;
                        }
                    }
                }
                .sc_list_lineItemBtm {
                    margin-top: 8rpx;
                    display: flex;
                    justify-content: space-between;
                    align-items: center;
                    width: 460rpx;
                    color: rgba(16, 16, 16, 1);
                    font-size: 20rpx;
                    text-align: left;
                    font-family: PingFangSC-regular;
                    .the_price {
                        font-size: 24rpx;
                        text-align: left;
                        font-family: PingFangSC-semiBold;
                        color: rgba(252, 126, 27, 1);
                    }
                }
            }
        }
    }
}
</style>
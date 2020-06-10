<template>
    <view class="myParcel">
        <!-- <view class="top_bg_box">
            <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/goods/2019-11-15/8xi67N_1668474680820.png"></image>
            <view class="decoration"></view>
        </view>-->
        <view class="myParcel-tip">
        </view>
        <!-- 包裹1 -->
        <view class="myParcel-list" v-for="(item, index) in orderDeliverDetail" :key="index" @click="toLogisticsInform(item)">
            <view class="myParcel-list-tip">
                <text class="myParcel-list-tipLf">{{ item.orderDeliverBill.distributionName }}</text>
                <view class="myParcel-list-tipRg">
                    <text>单号：{{ item.orderDeliverBill.logisticsNo }}</text>
                </view>
            </view>
            <view class="myParcel-list-wuliuInform">
                <view class="myparcel_state">
                    <view style="color: rgba(0, 118, 164, 1);"><text v-if="item.orderDeliverBill.logisticsStatus == 2">运输中</text>
                        <text v-if="item.orderDeliverBill.logisticsStatus == 3">签收</text>
                        <text v-if="item.orderDeliverBill.logisticsStatus == 4">问题件</text>
                    </view>
                    <view>{{item.orderDeliveryTrace[0].acceptTime||''}}</view>
                </view>
                <view class="logistics_status" v-if="item.orderDeliveryTrace && item.orderDeliveryTrace.length > 0">{{ item.orderDeliveryTrace[0].acceptStation }}</view>
            </view>
            <!-- list -->
            <view class="myParcel-listGood">
                <!-- 大类 -->
                <template v-for="(gItem, gIndex) in item.orderDeliverBillDetails">
                    <view class="goods_list_box" :key="gIndex" v-if="gIndex <= 3">
                        <image mode="aspectFit" class="goods_list_img" :src="gItem.orderGoods.imageUrl"></image>
                        <view class="commodity_information_box">
                            <view class="commodity_information_meu">
                                <view class="commodity_name">{{ gItem.orderGoods.itemName }}</view>
                                <view class="specifications">
                                    <view v-if="gItem.orderGoods.specsName">{{ gItem.orderGoods.specsName }}</view>
                                    <view v-if="gItem.orderGoods.colorName">{{gItem.orderGoods.colorName}}</view>
                                </view>
                                <view class="the_number_of">x{{ gItem.orderGoods.deliverCount ? gItem.orderGoods.deliverCount: 0}}</view>
                            </view>
                            <view class="the_barcode">编/条码：{{ gItem.orderGoods.itemCode }}</view>
                        </view>
                    </view>
                    <view class="goods_list_box" :key="gIndex" v-if="gIndex > 3 && showMore">
                        <image mode="aspectFit" class="goods_list_img" :src="gItem.orderGoods.imageUrl"></image>
                        <view class="commodity_information_box">
                            <view class="commodity_information_meu">
                                <view class="commodity_name">{{ gItem.orderGoods.itemName }}</view>
                                <view class="specifications">
                                    <view v-if="gItem.orderGoods.specsName">{{ gItem.orderGoods.specsName }}</view>
                                    <view v-if="gItem.orderGoods.colorName">{{gItem.orderGoods.colorName}}</view>
                                </view>
                                <view class="the_number_of">x{{ gItem.orderGoods.deliverCount ? gItem.orderGoods.deliverCount: 0}}</view>
                            </view>
                            <view class="the_barcode">编/条码：{{ gItem.orderGoods.itemCode }}</view>
                        </view>
                    </view>
                </template>
            </view>
            <view class="myParcel-showMore" v-if="item.orderDeliverBillDetails.length > 3" @click="dowmOrUpFun">
                <text>{{ dowmOrUp }}</text>
                <text class="iconfont icon-Packup" v-if="!showMore"></text>
                <text class="iconfont icon-an" v-if="showMore"></text>
            </view>
        </view>
        <view class="no-more">没有更多啦~</view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            dowmOrUp: "展开",
            showMore: false, //展开or收起
            orderDeliverDetail: [], //物流
            isF: false
        };
    },
    onLoad(option) {
        this.orderMbDetailFun(option.id);
    },
    onPageScroll(e) {
        this.isF = false
        if (e.scrollTop > 13) {
            this.isF = true
        } else {
            this.isF = false
        }
    },
    methods: {
        //返回
        toback() {
            uni.navigateBack({
                delta: 1,
            });
        },
        // 订单详情
        orderMbDetailFun(id) {
            let that = this;
            let params = {
                distributorId: uni.getStorageSync("userId"),
                orderId: id,
            };
            this.$api.get(this, api.orderDeliverBillDetail, params).then((res) => {
                if (res.success) {
                    that.orderDeliverDetail = res.data;
                }
            });
        },

        // 跳转物流轨迹页
        toLogisticsInform(item) {
            uni.setStorageSync("orderDeliveryTrace", JSON.stringify(item));
            uni.navigateTo({
                url: "./logisticsInform",
            });
        },
        // 展开--收起
        dowmOrUpFun() {
            if (this.showMore) {
                this.dowmOrUp = "展开";
            } else {
                this.dowmOrUp = "收起";
            }

            this.showMore = !this.showMore;
        },
    },
};
</script>

<style lang="scss">
.no-more {
    text-align: center;
}
.goods_list_box {
    width: 640rpx;
    height: 160rpx;
    margin: 0 auto;
    border-bottom: 1rpx dotted rgba(0, 0, 0, 0.1);
    display: flex;
    align-items: center;
    .goods_list_img {
        width: 100rpx;
        height: 100rpx;
    }
    .commodity_information_box {
        width: 530rpx;
        margin-left: 10rpx;
        height: 100rpx;
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        .commodity_information_meu {
            display: flex;
            .commodity_name {
                width: 260rpx;
                height: 60rpx;
                color: rgba(59, 59, 59, 1);
                font-size: 24rpx;
                text-align: left;
                font-family: SourceHanSansSC-regular;
                word-break: break-all;
                display: -webkit-box;
                overflow: hidden;
                text-overflow: ellipsis;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 2;
            }
            .specifications {
                width: 150rpx;
                height: 58rpx;
                color: rgba(38, 37, 37, 1);
                font-size: 24rpx;
                text-align: left;
                font-family: SourceHanSansSC-regular;
                view {
                    word-break: break-all;
                    display: -webkit-box;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    -webkit-box-orient: vertical;
                    -webkit-line-clamp: 1;
                }
            }
            .the_number_of {
                width: 120rpx;
                color: rgba(16, 16, 16, 1);
                font-size: 24rpx;
                text-align: right;
                font-family: SourceHanSansSC-regular;
            }
        }
        .the_barcode {
            width: 530rpx;
            color: rgba(164, 164, 164, 1);
            font-size: 20rpx;
            text-align: left;
            font-family: SourceHanSansSC-regular;
        }
    }
}
.top_bg_box {
    position: absolute;
    z-index: -1;
    image {
        width: 750rpx;
        height: 490rpx;
        display: block;
    }
    .decoration {
        height: 100rpx;
        border-radius: 50rpx;
        background-color: #fafafc;
        margin-top: -40rpx;
        z-index: 99999;
    }
}
.myParcel {
    font-size: 26rpx;
    .top-bg {
        background: #fff;
        .top-title {
            display: flex;
            align-items: center;
            padding: 0 30rpx;
            background: #fff;
            image {
                width: 18rpx;
                height: 30rpx;
            }
            view {
                font-size: 32rpx;
                font-weight: 400;
                width: 650rpx;
                height: 80rpx;
                line-height: 80rpx;
                text-align: center;
            }
            /* #ifdef H5 */
            view {
                height: 88rpx;
                line-height: 88rpx;
            }
            /*#endif*/
        }
    }
    .myParcel-tip {
        font-size: 24rpx;
        color: #fff;
        padding-top: 30rpx;
        display: flex;
        align-items: center;
        image {
            width: 32rpx;
            height: 31rpx;
            margin-right: 20rpx;
        }
    }
    .myParcel-list {
        width: 690rpx;
        border-radius: 30rpx;
        margin: 0 auto;
        background: #fff;
        & + .myParcel-list {
            margin-top: 20rpx;
        }
        .myParcel-list-tip {
            width: 690rpx;
            display: flex;
            // align-items: center;
            justify-content: space-between;
            padding: 30rpx;
            box-sizing: border-box;
            .myParcel-list-tipLf {
                width: 240rpx;
                color: #101010;
                font-size: 24rpx;
            }
            .myParcel-list-tipRg {
                width: 345rpx;
                text-align: right;
                color: #101010;
                font-size: 24rpx;
                text {
                    font-size: 26rpx;
                    margin-right: 10rpx;
                }
                .toIcon {
                    width: 24rpx;
                    height: 24rpx;
                }
            }
        }
        .myParcel-list-wuliuInform {
            border-bottom: 1rpx solid rgba(241, 242, 249, 0.5);
            width: 640rpx;
            height: auto;
            border-radius: 20rpx;
            background-color: rgba(247, 247, 247, 1);
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            text-align: center;
            font-family: Arial;
            margin: 0 auto;
            padding: 20rpx;
            box-sizing: border-box;
            .myparcel_state {
                display: flex;
                justify-content: space-between;
                align-items: center;
                color: rgba(59, 59, 59, 1);
                font-size: 24rpx;
                font-family: SourceHanSansSC-regular;
            }
            .logistics_status {
                color: rgba(97, 96, 96, 1);
                font-size: 20rpx;
                text-align: left;
                font-family: SourceHanSansSC-regular;
                margin-top: 10rpx;
            }
            // view {
            //     display: flex;
            //     align-items: top;
            //     padding: 30rpx 30rpx 0 30rpx;
            //     text:nth-child(1) {
            //         font-size: 24rpx;
            //         color: #666;
            //         width: 120rpx;
            //     }
            //     text:nth-child(2) {
            //         font-size: 26rpx;
            //         color: #333;
            //         margin-left: 30rpx;
            //         width: 500rpx;
            //     }
            // }
        }
        .myParcel-listGood {
            .myParcel-listGood-small {
                padding: 30rpx;
                display: flex;
                image {
                    width: 111rpx;
                    height: 111rpx;
                }
                .listGood-small-center {
                    margin-left: 25rpx;
                    color: #999999;
                    font-size: 22rpx;
                    width: 350rpx;
                    view:nth-child(2),
                    view:nth-child(3) {
                        margin-top: 8rpx;
                    }
                }
                .listGood-small-rg {
                    text-align: right;
                    width: 200rpx;
                    view:nth-child(1) {
                        text:nth-child(1) {
                            font-size: 22rpx;
                        }
                        text:nth-child(2) {
                            font-size: 32rpx;
                        }
                    }
                    view:nth-child(2),
                    view:nth-child(3) {
                        margin-top: 8rpx;
                        color: #999;
                        font-size: 22rpx;
                    }
                }
            }
        }
    }
    .myParcel-showMore {
        color: #0076a5;
        display: flex;
        align-items: center;
        justify-content: center;
        padding: 30rpx 0;
        text {
            margin-right: 10rpx;
        }
        .iconfont {
            position: relative;
            top: 3rpx;
            font-size: 22rpx !important;
        }
    }
}
</style>

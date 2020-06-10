<template>
    <view class="addressList">
        <view class="address-list" v-if="addressList.length > 0">
            <scroll-view :scroll-y="true" @scrolltolower="ReachBottom" style="height: 1500rpx">
                <uni-swipe-action>
                    <uni-swipe-action-item v-for="(item,index) in addressList" :key="item.id">
                        <view class="address-list-line" @touchstart="startHandle($event,index)" @touchmove="moveHandle($event,index)" @touchend="endHandle($event,index)" :style="{ right: item.right + 'rpx'}">
                            <view class="address-list-lineBox" v-if="isParams" @click="selectAddress(item.id)">
                                <view class="address-list-lineTop">
                                    <view class="address-provinces">
                                        <image class="shipping_address_icon" src="../../static/images/url2.png"></image> {{ item.userName }}
                                        {{ item.provinceName ? item.provinceName : "" }}
                                        {{ item.cityName ? item.cityName : "" }}
                                        {{ item.districtName ? item.districtName : "" }}
                                        <view class="the_default" v-if=" item.defaultFlag == 1">默认</view>
                                    </view>
                                    <view class="address-icon">
                                        <image src="../../static/images/edit_icon.png" @click.stop="addressEdit(2, item)"></image>
                                    </view>
                                </view>
                                <view class="address-detail">{{ item.address }}</view>
                                <view class="address-list-lineBtm">
                                    <view>
                                        <text>{{ item.userName }}</text>
                                        <text>{{ item.phone }}</text>
                                    </view>
                                </view>
                            </view>
                            <view class="address-list-lineBox" v-else>
                                <view class="address-list-lineTop">
                                    <view class="address-provinces">
                                        <image class="shipping_address_icon" src="../../static/images/url2.png"></image> {{ item.userName }}
                                        {{ item.provinceName ? item.provinceName : "" }}
                                        {{ item.cityName ? item.cityName : "" }}
                                        {{ item.districtName ? item.districtName : "" }}
                                        <view class="the_default" v-if=" item.defaultFlag == 1">默认</view>
                                    </view>
                                    <view class="address-icon">
                                        <view>
                                            <image src="../../static/images/edit_icon.png" @click.stop="addressEdit(2, item)"></image>
                                        </view>
                                    </view>
                                </view>
                                <view class="address-detail">{{ item.address }}</view>
                                <view class="address-list-lineBtm">
                                    <view>
                                        <text>{{ item.userName }}</text>
                                        <text>{{ item.phone }}</text>
                                    </view>
                                </view>
                            </view>
                            <view v-if=" item.defaultFlag != 1" class="address-defaultIcon" @click="setDefault(item)">
                                <text class="iconfont" :class="
              item.defaultFlag == 1
                ? 'icon-Checkthe choice-icon'
                : 'icon-uncheck'
            "></text>
                                <text>设为默认地址</text>
                            </view>
                        </view>
                        <!-- v-if="(index!=0)" -->
                        <template v-slot:right>
                            <view class="del-btn" @click.stop="deleteAdress(item)"><text>删除</text></view>
                        </template>
                    </uni-swipe-action-item>
                </uni-swipe-action>
            </scroll-view>
        </view>

        <view class="address-noData" v-if="addressList.length == 0">
            <image src="../../static/images/noAddress_img.png"></image>
            <view>暂无收货地址</view>
        </view>
        <view style="height: 100rpx;"></view>
        <view class="add-address" @click="addressEdit(1)">
            <view>新增收货地址</view>
        </view>

    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            addressList: [],
            isTwoWay: false,
            isParams: false,
            tipTextShow: false,
            tipText: "",
            windowHeight: 0,//可视区域的高度
            delBtnWidth: 140,
            startX: "", //开始移动坐标
            page: 1,
            size: 10,
            totalPage: 0,
        };
    },
    onLoad(option) {
        this.isTwoWay = option.isTwoWay ? option.isTwoWay : false;
        this.isParams = option.addrId == 1 ? true : false;
    },
    onShow() {
        uni.setStorageSync("setText", false);
        this.addressList = [];
        uni.showLoading({
            title: '加载中',
            mask: true
        });
        this.getAddressList();
    },

    methods: {
        back() {
            uni.navigateBack({
                delta: 1
            })
        },
        ReachBottom() {
            if (this.page < this.totalPage) {
                this.page += 1;
                uni.showLoading({
                    title: '加载中',
                    mask: true
                });
                this.getAddressList();
            }
        },
        startHandle(e, index) {
            var touch = e.touches[0];
            for (var index in this.addressList) {
                this.addressList[index].right = 0;
            }
            this.startX = touch.clientX;
        },
        moveHandle(e, index) {
            var touch = e.touches[0];
            var disX = this.startX - touch.clientX; //计算移动距离
            if (disX >= 20) {
                if (disX > this.delBtnWidth) {
                    disX = this.delBtnWidth;
                }
                this.addressList[index].right = disX;
            } else {
                this.addressList[index].right = 0;
            }
        },
        endHandle(e, index) {
            var good = this.addressList[index];
            if (good.right >= this.delBtnWidth / 2) {
                this.addressList[index].right = this.delBtnWidth;
            } else {
                this.addressList[index].right = 0;
            }
        },
        delHandle(id) {
            this.addressList = this.addressList.filter(item => item.id !== id);
        },
        // 返回
        // 轻提示弹框
        tipFun(text) {
            let that = this;
            this.tipText = text;
            this.tipTextShow = true;
            setTimeout(function () {
                that.tipTextShow = false;
            }, 3000);
        },
        // 获取地址列表
        getAddressList() {
            let that = this;
            let id = uni.getStorageSync("userId");
            let params = {
                id: id,
                page: that.page,
                size: that.size
            };
            this.$api.get(this, api.getAddrList, params).then((res) => {
                if (res.success) {
                    that.totalPage = res.data.pages;
                    let list = res.data.list;
                    that.addressList = that.page == 1 ? list : [...that.addressList, ...list];
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
                uni.hideLoading()
            });
        },

        // 删除地址
        deleteAdress(item) {
            this.$api.delete(this, api.updateAddr, { id: item.id }).then((res) => {
                if (res.success) {
                    uni.removeStorageSync("orderAddress");
                    uni.showToast({
                        icon: 'none',
                        title: '删除成功',
                        duration: 2000
                    });
                    this.addressList = [];
                    this.getAddressList();
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            });
        },
        // 设为默认地址
        setDefault(item) {
            let that = this;
            let list = this.addressList;
            this.$api.put(this, api.setDefaultAddr, { id: item.id }).then((res) => {
                if (res.success) {
                    uni.showToast({
                        title: '设置成功',
                        duration: 2000
                    });
                    this.getAddressList();
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            });
        },

        // 新增or编辑收货地址
        addressEdit(type, item) {
            let info = JSON.stringify(item);
            let length = this.addressList.length;
            uni.navigateTo({
                url: './addressEdit?type=' + type + "&info=" + info + '&length=' + length
            });
        },

        // 订单过来选择地址
        selectAddress(item) {
            uni.setStorageSync("addrId", item);
            uni.redirectTo({
                url: '../confirmOrder/confirmOrder'
            });
        },
    },
};
</script>

<style lang="scss" scoped>
.the_default {
    width: 70rpx;
    height: 30rpx;
    line-height: 30rpx;
    border-radius: 100rpx;
    background-color: rgba(0, 118, 164, 1);
    color: rgba(255, 255, 255, 1);
    font-size: 20rpx;
    text-align: center;
    font-family: Arial;
    margin-left: 20rpx;
}
.shipping_address_icon {
    width: 40rpx;
    height: 40rpx;
    margin-right: 20rpx;
}
.del-btn {
    // position: absolute;
    // top: 0;
    // // right: -140rpx;
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 20;
    width: 140rpx;
    height: 100%;
    background: #0076a5;
    color: #fff;
}
.addressList {
    .address-noData {
        font-size: 30rpx;
        text-align: center;
        padding-top: 300rpx;
        image {
            width: 268rpx;
            height: 258rpx;
            margin-bottom: 20rpx;
            color: #666;
        }
    }
    .address-list {
        padding-top: 30rpx;
        .address-list-line {
            background: #fff;
            margin-top: 2rpx;
            padding: 30rpx;
            position: relative;
            .address-defaultIcon {
                position: absolute;
                right: 30rpx;
                bottom: 30rpx;
                font-size: 24rpx;
                color: #999;
                display: flex;
                align-items: center;
                .iconfont {
                    position: relative;
                    top: 3rpx;
                    font-size: 28rpx;
                }

                text {
                    margin-right: 5rpx;
                }
                .icon-uncheck {
                    color: #999;
                }
                .icon-Checkthe {
                    color: #0076a5;
                }
            }
            .address-list-lineTop {
                display: flex;
                justify-content: space-between;
                align-items: center;
                .address-provinces {
                    display: flex;
                    align-items: center;

                    color: rgba(84, 83, 83, 1);
                    font-size: 24rpx;
                    text-align: left;
                    font-family: PingFangSC-regular;
                }
                .address-icon {
                    display: flex;
                    align-items: center;
                    view {
                        width: 50rpx;
                        text-align: center;
                        cursor: pointer;
                    }
                    image:nth-child(1) {
                        width: 28rpx;
                        height: 27rpx;
                    }
                    image:nth-child(2) {
                        width: 30rpx;
                        height: 26rpx;
                        margin-left: 30rpx;
                    }
                }
            }
            .address-detail {
                font-weight: 400;
                margin-top: 18rpx;
                color: rgba(16, 16, 16, 1);
                font-size: 28rpx;
                text-align: left;
                font-family: PingFangSC-regular;
            }
            .address-list-lineBtm {
                color: rgba(164, 164, 164, 1);
                font-size: 24rpx;
                text-align: left;
                font-family: PingFangSC-regular;

                display: flex;
                align-items: center;
                justify-content: space-between;
                margin-top: 20rpx;
                view:nth-child(1) {
                    text:nth-child(1) {
                        margin-right: 20rpx;
                    }
                }
                view:nth-child(2) {
                    display: flex;
                    align-items: center;
                    icon {
                        font-size: 36rpx;
                        margin-right: 10rpx;
                    }
                    .choice-icon {
                        color: #0076a5;
                    }
                }
            }
        }
    }
    .add-address {
        width: 750rpx;
        // height: 70rpx;
        background-color: #ffffff;
        color: rgba(16, 16, 16, 1);
        font-size: 28rpx;
        text-align: center;
        font-family: Arial;
        display: flex;
        justify-content: space-between;
        align-items: center;
        position: fixed;
        bottom: 0;
        padding: 20rpx 0;
        padding-bottom: constant(safe-area-inset-bottom); // 兼容 IOS<11.2
        padding-bottom: env(safe-area-inset-bottom); // 兼容 IOS>=11.2

        view {
            width: 90%;
            height: 70rpx;
            line-height: 70rpx;
            text-align: center;
            color: #fff;
            font-size: 28rpx;
            background: linear-gradient(10deg, #0076a5 0%, #0076a5 100%);
            border-radius: 40rpx;
            margin: 0 auto;
            margin-bottom: 10px;
        }
    }
}
</style>

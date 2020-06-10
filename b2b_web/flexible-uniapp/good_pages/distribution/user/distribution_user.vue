<template>
    <view>
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar left-icon="left" :border="false" @clickLeft="back" dark :fixed="false" background-color="transparent" color="#ffffff" status-bar title="分销员管理" />
        <!-- #endif -->
        <view class="top_bg_box" :style="{marginTop: globalData.navHeight+'rpx'}">
            <!-- #ifdef H5 -->
            <view class="h5_nav_bar">
                <view style="width: 30rpx;" @click="back">
                    <uni-icons type="back" size="24" color="#ffffff"></uni-icons>
                </view>
                <view>分销员管理</view>
                <view style="width: 30rpx;"></view>
            </view>
            <!-- #endif -->
            <image src="https://bat.oss-cn-shenzhen.aliyuncs.com/diy/h51684390031375.jpg"></image>
            <view class="distribution_list_box">
                <view class="distribution_mue_box">
                    <view class="distribution_user_state_box">
                        <view :class="IsState?'select_user_state':''" @click="cilckState(1,true)">待审核</view>
                        <view :class="!IsState?'select_user_state':''" @click="cilckState(2,false)">合作中</view>
                    </view>
                    <view class="invitation_box" @click="invitation()">
                        <view class="invitation_icon_box">
                            <uni-icons type="upload-filled" size="14" color="#FF7E3E"></uni-icons>
                        </view>
                        邀请
                    </view>
                </view>
                <view class="distribution_user_list_box" v-for="item in userList" :key="item">
                    <view class="user_message_box">
                        <image src="../../../static/images/index_login.png"></image>
                        <view class="user_informationize_box">
                            <view class="user_name">{{item.name}}</view>
                            <view class="user_phone">
                                <!-- <uni-icons type="staff-filled" size="24"></uni-icons> -->
                                <image style="width: 40rpx;height: 40rpx;" src="../../../static/icons/my/order/identity.png"></image>
                                {{item.staffType==1?'店长':'导购员'}}
                                {{item.staffCode}}
                            </view>
                            <view class="user_phone">
                                <image style="width: 40rpx;height: 40rpx;margin-right: 10rpx;" src="../../../static/icons/my/order/phone.png"></image>
                                {{item.phone}}
                            </view>
                        </view>
                    </view>
                    <view class="" @click="audit(item.id)" v-if="IsState">审核</view>
                    <uni-icons v-else type="right" size="26" color="#38334A" @click="particulars(item)"></uni-icons>
                </view>
                <view v-if="userList.length==0" class="no_information_yet">暂无分销员~</view>
            </view>
        </view>
        <uni-popup ref="message" type="dialog" @maskClick="close">
            <uni-popup-dialog :before-close="true" type="info" cancelText="拒绝" confirmText="通过" title="审核确认" content="请确认是否通过已选分销员？" @confirm="passConfirm(2)" @close="passConfirm(3)"></uni-popup-dialog>
        </uni-popup>
    </view>
</template>

<script>
import api from "../../../common/js/allApi";
export default {
    data() {
        return {
            IsState: true,
            globalData: {
                statusBarHeight: 0, // 状态导航栏高度
                navHeight: 0, // 总体高度
                navigationBarHeight: 0, // 导航栏高度(标题栏高度)
            },
            userList: [],
            page: 1,
            size: 10,
            nextPage: 0,
            distributorId: ''
        };
    },
    onShow() {
        uni.showLoading({
            title: '加载中',
            mask: true
        });
        this.getdistributorUser(this.IsState ? 1 : 2);
    },
    onLoad() {
        var ratio = uni.getStorageSync("ratio") * -1;//比例
        // #ifdef MP-WEIXIN
        // 状态栏高度
        this.globalData.statusBarHeight = uni.getSystemInfoSync().statusBarHeight
        // 获取微信胶囊的位置信息 width,height,top,right,left,bottom
        const custom = uni.getMenuButtonBoundingClientRect()
        // 导航栏高度(标题栏高度) = 胶囊高度 + (顶部距离 - 状态栏高度) * 2
        this.globalData.navigationBarHeight = custom.height + (custom.top - this.globalData.statusBarHeight) * 2
        // 总体高度 = 状态栏高度 + 导航栏高度
        this.globalData.navHeight = (this.globalData.navigationBarHeight + this.globalData.statusBarHeight + 4) * ratio;
        // #endif

    },
    onReachBottom() {
        if (this.page < this.nextPage) {
            this.page += 1;
            uni.showLoading({
                title: '加载中',
                mask: true
            });
            if (this.IsState) {
                this.getdistributorUser(1);
            }
            else {
                this.getdistributorUser(2);
            }
        }
    },
    methods: {
        cilckState(status, IsState) {
            this.IsState = IsState;
            this.page = 1;
            this.userList = [];
            this.getdistributorUser(status);
        },
        //分销员列表
        getdistributorUser(status) {
            this.$api.get(this, api.getdistributorUser, {
                page: this.page,
                size: this.size,
                shopCode: uni.getStorageSync("shopCode"),
                status: status//申请状态 1申请中 2申请通过 3申请失败
            }).then((res) => {
                this.nextPage = res.data.pages;
                let list = res.data.list;
                this.userList = this.page == 1 ? list : [...this.userList, ...list];
                uni.hideLoading()
            })
        },
        invitation() {
            uni.navigateTo({
                url: "../user/invitation?type=2",
            });
        },
        particulars(item) {
            uni.navigateTo({
                url: "../user/distribution_user_detaild?id=" + item.id + "&staffCode=" + item.staffCode,
            });
        },
        close() {
            this.$refs.message.close();
        },
        // 审核通过
        passConfirm(status) {
            var that = this;
            this.$api.put(this, api.checkDistributor, {
                id: this.distributorId,
                status: status,//1申请中 2申请通过 3申请失败
            }).then((res) => {
                if (res.success) {
                    uni.showToast({
                        title: status == 2 ? '申请成功' : '拒绝成功',
                        icon: "none",
                        duration: 2000,
                        complete: function () {
                            that.page = 1;
                            that.size = 10;
                            that.nextPage = 0;
                            that.getdistributorUser(1);
                        }
                    });
                }
                else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000
                    });
                }
                this.close();
            })
        },
        //审核
        audit(id) {
            this.distributorId = id;
            this.$refs.message.open();
        },
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
    },

};
</script>
<style lang="scss" scoped>
.no_information_yet {
    height: 300rpx;
    line-height: 300rpx;
    text-align: center;
}

.top_bg_box {
    .h5_nav_bar {
        height: 88rpx;
        width: 750rpx;
        position: absolute;
        z-index: 1;
        line-height: 88rpx;
        color: #ffffff;
        font-size: 26rpx;
        display: flex;
        justify-content: space-between;
    }
    image {
        width: 750rpx;
        height: 490rpx;
        display: block;
    }
    .distribution_list_box {
        min-height: 200px;
        max-height: 100%;
        width: 750rpx;
        margin-top: -240rpx;
        z-index: 10;
        border-radius: 50rpx 50rpx 0rpx 0rpx;
        background-color: #efeff4;
        .distribution_user_list_box {
            width: 680rpx;
            height: 160rpx;
            border-radius: 30rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 0 auto;
            margin-bottom: 20rpx;
            background-color: rgba(255, 255, 255, 1);
            padding: 0 30rpx;
            box-sizing: border-box;
            .user_message_box {
                display: flex;
                height: 160rpx;
                align-items: center;
                image {
                    width: 100rpx;
                    height: 100rpx;
                    border-radius: 100rpx;
                }
                .user_informationize_box {
                    margin-left: 20rpx;
                    .user_name {
                        color: rgba(59, 59, 59, 1);
                        font-size: 24rpx;
                        font-family: PingFangSC-regular;
                    }
                    .user_phone {
                        color: rgba(187, 187, 187, 1);
                        font-size: 20rpx;
                        display: flex;
                        align-items: center;
                    }
                }
            }
        }
        .distribution_mue_box {
            width: 680rpx;
            height: 140rpx;
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin: 0 auto;
            .distribution_user_state_box {
                font-family: PingFangSC-regular;
                color: rgba(16, 16, 16, 1);
                font-size: 24rpx;
                display: flex;
                align-items: flex-end;
                justify-content: space-between;
                width: 180rpx;
            }
            .select_user_state {
                color: rgba(0, 118, 164, 1);
                font-size: 28rpx;
                border-bottom: 2rpx solid #0076a4;
                font-weight: 600;
            }
            .invitation_box {
                color: rgba(255, 126, 62, 1);
                font-size: 24rpx;
                font-family: PingFangSC-regular;
                display: flex;
                align-items: center;
                .invitation_icon_box {
                    display: flex;
                    justify-content: center;
                    align-items: center;
                    background-color: #fff1dc;
                    width: 40rpx;
                    height: 40rpx;
                    border-radius: 50%;
                    margin-right: 10rpx;
                }
            }
        }
    }
}
</style>

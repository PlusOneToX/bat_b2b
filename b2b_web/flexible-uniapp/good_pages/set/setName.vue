<template>
    <view class="modifyPhone">
        <!-- <uni-nav-bar left-icon="left" title="修改昵称" shadow @clickLeft="back" /> -->
        <view class="modifyPhone-module">
            <view class="modifyPhone-newPhone">
                <input placeholder="请输入新昵称" v-model="nikeName" />
            </view>
        </view>

        <view class="modifyPhone-btn" @click="confirmClick">提交</view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            nikeName: "",
            code: "",
            times: 60,
            userInfo: {
                id: 0,
                name: "",
                nikeName: "游客",
                sex: 0,
                birthday: "",
                headPortrait: "",
                phone: "",
                password: "",
                platform: 0
            },
        };
    },
    onLoad() {
        this.userId = parseInt(uni.getStorageSync("userId"));
        this.$api.get(this, api.getUserInfo, {
            id: this.userId,
        }).then((res) => {
            this.userInfo = { ...res.data };
        })

    },
    methods: {
        back() {
            uni.navigateBack({
                delta: 1
            });
        },

        confirmClick() {
            if (this.nikeName == "") {
                uni.showToast({
                    icon: 'none',
                    title: '请输入昵称',
                    duration: 2000
                });
                return;
            }
            this.userInfo.nikeName = this.nikeName;
            this.$api.put(this, api.updateUserInfo, this.userInfo).then((res) => {
                if (res.success) {
                    uni.showToast({
                        icon: 'none',
                        title: '修改成功',
                        duration: 500,
                        complete: function () {
                            uni.navigateBack({
                                delta: 1
                            });
                        }
                    });
                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 1000
                    });
                }
            })

        }
    },
};
</script>

<style lang="scss" scoped>
.phone_set {
    width: 600rpx;
    margin: 0 auto;
    padding: 50rpx 0rpx;
    color: rgba(16, 16, 16, 1);
    font-size: 28rpx;
    text-align: left;
    font-family: PingFangSC-regular;
}
.modifyPhone {
    .modifyPhone-module {
        padding-top: 40rpx;

        font-size: 28rpx;
        view {
            display: flex;
            justify-content: space-between;
            align-items: center;
        }
        .modifyPhone-oldPhone {
            padding: 30rpx;
            border-top: 20rpx solid rgba(241, 242, 249, 0.5);
            border-bottom: 1px solid rgba(241, 242, 249, 0.5);
        }
        .modifyPhone-newPhone {
            width: 600rpx;
            height: 100rpx;
            border-radius: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            font-family: Arial;
            border: 1rpx solid rgba(239, 239, 239, 1);
            margin: 0 auto;
            input {
                padding: 25rpx 30rpx;
                width: 690rpx;
                font-size: 28rpx;
            }
            border-bottom: 1px solid rgba(241, 242, 249, 0.5);
        }
        .modifyPhone-code {
            display: flex;
            align-items: center;
            justify-content: space-between;
            width: 600rpx;
            height: 100rpx;
            border-radius: 30rpx;
            background-color: rgba(255, 255, 255, 1);
            color: rgba(16, 16, 16, 1);
            font-size: 28rpx;
            font-family: Arial;
            border: 1rpx solid rgba(239, 239, 239, 1);
            margin: 0 auto;
            margin-top: 40rpx;
            padding: 0 40rpx;
            box-sizing: border-box;
            input {
                width: 300rpx;
                font-size: 28rpx;
            }
            text {
                color: #0076a5;
                font-size: 28rpx;
            }
        }
    }
    .modifyPhone-btn {
        width: 600rpx;
        height: 70rpx;
        line-height: 70rpx;
        text-align: center;
        background: linear-gradient(10deg, #0076a5 0%, #0076a5 100%);
        border-radius: 50rpx;
        margin: 200rpx auto;
        font-size: 32rpx;
        color: #fff;
    }
}
</style>

<template>
    <view class="modifyPhone">
        <view class="modifyPhone-module">
            <view class="phone_set">微信解绑使用绑定手机号验证。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                当前手机：{{userInfo.phone||''}}
            </view>
            <!-- <view v-else class="phone_set">用户当前没有绑定的手机号</view>
            <view class="modifyPhone-newPhone">
                <input type="number" placeholder="请输入新手机号" maxlength="11" v-model="oldPhone" />
            </view> -->
            <view class="modifyPhone-code">
                <input type="number" placeholder="请输入验证码" v-model="code" />
                <text @click="getCode" v-if="times == 60">获取验证码</text>
                <text v-if="times < 60">{{ times }}S重新获取</text>
            </view>
        </view>
        <view class="modifyPhone-btn" @click="confirmClick">确定</view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            newPhone: "",
            oldPhone: "",
            code: "",
            times: 60,
            userInfo: {},
            Istime: true,
            openIdDTOS: [],
            type: 'wx'
        };
    },
    onLoad(options) {
        this.openIdDTOS = JSON.parse(options.openIdDTOS);
        this.type = options.type;
        this.userId = uni.getStorageSync("userId");
        this.$api.get(this, api.getUserInfo, {
            id: this.userId,
        }).then((res) => {
            this.userInfo = res.data;
            this.oldPhone = res.data.phone;
        })
    },
    methods: {
        back() {
            uni.redirectTo({
                url: './set'
            })
        },
        //返回
        toback() {
            uni.navigateBack({
                delta: 1,
            });
        },
        confirmClick() {
            let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
            let phone = this.oldPhone;
            let code = this.code;
            if (phone == "") {
                uni.showToast({
                    icon: 'none',
                    title: '请输入手机号',
                    duration: 2000
                });
                return;
            }
            if (phone != "" && !myreg.test(phone)) {
                uni.showToast({
                    icon: 'none',
                    title: '手机号格式不正确',
                    duration: 2000
                });
                return;
            }
            if (code == "") {
                uni.showToast({
                    icon: 'none',
                    title: '验证码不能为空',
                    duration: 2000
                });
                return;
            }
            var openIds = [];
            this.openIdDTOS.forEach(item => {
                if (this.type == 'wx' && (item.openType == 1 || item.openType == 2)) {
                    openIds.push(item.openId);
                }
                if (this.type == 'ali' && item.openType == 3) {
                    openIds.push(item.openId);
                }
            });
            this.$api.get(this, api.GetwxId, {
                appType: 1,//应用类型：1 微信小程序 2 微信公众号 3 支付宝
                distributorId: uni.getStorageSync("distributorId"),
            }).then((res1) => {
                this.$api.put(this, api.unbind, {
                    openIds: openIds,
                    code: code,
                    codeType: 7,//分销员解绑
                    distributorId: uni.getStorageSync("distributorId"),
                    phone: phone,
                    shopCode: uni.getStorageSync("shopCode"),
                    staffCode: uni.getStorageSync("userNo"),
                }).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            icon: 'none',
                            title: '解绑成功',
                            duration: 1000,
                            complete: function () {
                                setTimeout(function () {
                                    uni.redirectTo({
                                        url: './set'
                                    })
                                }, 1000);
                            }
                        });

                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                })
            })


        },

        // 获取验证码
        getCode() {
            if (this.Istime) {
                this.Istime = false;
                let that = this;
                let phone = this.oldPhone;
                let myreg = /^[1][3,4,5,6,7,8,9][0-9]{9}$/;
                if (phone == "") {
                    uni.showToast({
                        icon: 'none',
                        title: '请输入手机号',
                        duration: 2000
                    });
                    return;
                }
                if (phone != "" && !myreg.test(phone)) {
                    uni.showToast({
                        icon: 'none',
                        title: '手机号格式不正确',
                        duration: 2000
                    });
                    return;
                }
                this.$api.post(this, api.getVerify, {
                    codeType: 6,
                    phone: phone
                }).then((res) => {
                    if (res.success) {
                        // that.code = res.data.code;
                        let times = setInterval(() => {
                            that.times--;
                            if (that.times == 0) {
                                clearInterval(times);
                                that.times = 60;
                                this.Istime = true;
                            }
                        }, 1000);
                    } else {
                        uni.showToast({
                            icon: 'none',
                            title: res.errMessage,
                            duration: 2000
                        });
                    }
                });
            }
        },
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

<template>
    <view class="modifyPhone">
        <!-- <uni-nav-bar left-icon="left" title="修改手机" shadow @clickLeft="back" /> -->
        <view class="modifyPhone-module">
            <view class="phone_set">绑定手机后，下次登录将可用手机号登录。</view>
            <view class="modifyPhone-newPhone">
                <input type="number" placeholder="请输入手机号" maxlength="11" v-model="oldPhone" />
            </view>
            <view class="modifyPhone-code">
                <input type="number" placeholder="请输入验证码" v-model="code" />
                <text @click="getCode" v-if="times == 60">获取验证码</text>
                <text v-if="times < 60">{{ times }}S重新获取</text>
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
            newPhone: "",
            oldPhone: "",
            code: "",
            times: 60,
            userInfo: {},
            Istime: true
        };
    },
    onLoad() {
        this.userId = uni.getStorageSync("userId");
        this.$api.get(this, api.getUserInfo, {
            id: this.userId,
        }).then((res) => {
            this.userInfo = res.data;
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
            this.userInfo.phone = this.oldPhone;
            this.$api.put(this, api.updateUserInfo, this.userInfo).then((res) => {
                if (res.success) {
                    // uni.setStorageSync("h5userId", res.data.id);

                    // #ifdef MP-WEIXIN
                    this.openidlogin();
                    // #endif
                    // #ifdef H5
                    uni.setStorageSync("phone", res.data.phone);
                    uni.showToast({
                        icon: 'none',
                        title: '手机号绑定成功',
                        duration: 1000,
                        complete: function () {
                            setTimeout(function () {
                                uni.redirectTo({
                                    url: './set'
                                })
                            }, 1000);
                        }
                    });
                    // #endif

                } else {
                    uni.showToast({
                        icon: 'none',
                        title: res.errMessage,
                        duration: 2000
                    });
                }
            })

        },
        //openid登录
        openidlogin() {
            this.h5userId = uni.getStorageSync("h5userId");
            var that = this;
            that.$api.post(that, api.openidlogin, {
                customerId: this.h5userId,//C端客户第三方登录
                openId: uni.getStorageSync("openId"),
                otherUid: "",//第三方系统其他标识码
                phone: ''
            }).then((res) => {
                if (res.success) {
                    uni.setStorageSync("staffType", res.data.staffType);//店铺员类型 1.店长 2.导购员 3.消费者
                    uni.setStorageSync("userId", res.data.id);
                    uni.setStorageSync("userNo", res.data.no);
                    uni.setStorageSync("phone", res.data.phone);
                    uni.setStorageSync("customerShopCheck", res.data.customerShopCheck || 0);//店铺审核：0 否 1.是;无返回或为零都为否
                    uni.setStorageSync("rxShopSwitch", res.data.rxShopSwitch || 0);//是否启用店铺，1启用 0 不启用;无返回或为零都为否
                    uni.setStorageSync("aliAuthorize", res.data.aliAuthorize);//是否绑定支付宝
                    uni.setStorageSync("wxAuthorize", res.data.wxAuthorize);//是否绑定微信

                    if (res.data.payWay) {
                        uni.setStorageSync("payWay", res.data.payWay);
                    }
                    uni.showToast({
                        icon: 'none',
                        title: '手机号绑定成功',
                        duration: 1000,
                        complete: function () {
                            setTimeout(function () {
                                uni.redirectTo({
                                    url: './set'
                                })
                            }, 1000);
                        }
                    });
                }

            })
        },
        loginOut() {
            let platform = uni.getStorageSync('platform')
            let distributorId = uni.getStorageSync("distributorId");
            if (platform) {
                uni.removeStorage('userId')
                uni.removeStorage('phone')
                uni.removeStorage('goods')
                uni.removeStorage('auth')
                uni.removeStorage('userNo')
                if (platform !== '7') {
                    uni.removeStorage('uid')
                    uni.removeStorage('authenticateUserID')
                    let sa = uni.getStorageSync("sa");
                    // this.$router.replace({
                    //     name: "login",
                    //     query: {
                    //         platform: platform,
                    //         distributorId: distributorId,
                    //         sa: sa,
                    //     },
                    // });
                }
                uni.redirectTo({
                    url: '../login/login'
                });
            } else {
                uni.removeStorage('userId')
                uni.removeStorage('phone')
                uni.removeStorage('goods')
                uni.removeStorage('uid')
                uni.removeStorage('authenticateUserID')
                uni.removeStorage('auth')
                uni.removeStorage('userNo')
                uni.removeStorage('sa')
                uni.redirectTo({
                    url: '../login/login'
                });
            }
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
                // 18825230246
                this.$api.post(this, api.getVerify, {
                    codeType: 3,
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

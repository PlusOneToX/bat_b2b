<template>
    <view class="apply_for_box">
        <view class="top_bg_box">
            <button v-if="Form.phone==''&&IsPhone" open-type="getPhoneNumber" @getphonenumber="getPhoneNumber" class="login-wechatButton">
                <view class="content_box">
                    <image src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681976633830.png"></image>
                    <view class="div-box code">
                        <input v-model="Form.nikeName" placeholder="请输入真实姓名" :left-icon="'sprite-icon login_icon_phone'" clearable />
                    </view>
                    <view class="div-box code">
                        <input type="digit" maxlength="11" v-model="Form.phone" placeholder="请输入手机号" @blur="validatePhone" :left-icon="'sprite-icon login_icon_phone'" clearable />

                    </view>
                    <view class="div-box code" v-if="loginIndex == 1">
                        <input type="digit" v-model="Form.code" placeholder="请输入手机验证码" maxlength="6" :left-icon="'sprite-icon login_icon_verification'" clearable />
                        <view class="login_code" @click="getCode" v-if="loginIndex == 1">
                            {{str}}
                        </view>
                    </view>
                </view>
            </button>
            <view class="content_box" v-else>
                <image src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681976633830.png"></image>
                <view class="div-box code">
                    <input v-model="Form.nikeName" placeholder="请输入真实姓名" :left-icon="'sprite-icon login_icon_phone'" clearable />
                </view>
                <view class="div-box code">
                    <input type="digit" maxlength="11" v-model="Form.phone" placeholder="请输入手机号" @blur="validatePhone" :left-icon="'sprite-icon login_icon_phone'" clearable />
                </view>
                <view class="div-box code" v-if="loginIndex == 1">
                    <input type="digit" v-model="Form.code" placeholder="请输入手机验证码" maxlength="6" :left-icon="'sprite-icon login_icon_verification'" clearable />
                    <view class="login_code" @click="getCode" v-if="loginIndex == 1">
                        {{str}}
                    </view>
                </view>
            </view>
            <image @click="applyFor" class="btn-normal" mode="aspectFit" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681977095845.png"></image>
            <!-- <view class="btn btn-normal"></view> -->
        </view>
    </view>
</template>

<script>
import api from "../../../common/js/allApi";
const TIME_COUNT = 60;
export default {
    data() {
        return {
            str: "获取验证码",
            loginIndex: 1,

            Form: {
                phone: "",
                shopCode: '',
                code: "",
                nikeName: ''
            },
            timer: null,
            count: 0,
            nikeName: '微信用户',
            type: 3,//:1 微信 2 支付宝 3 H5
            openIds: [],
            h5userId: '',
            IsPhone: true,
            code: ''
        };
    },
    onShow() {

        var that = this;
        uni.login({
            provider: "weixin",
            success: function (loginRes) {
                that.code = loginRes.code;
            },
        });
    },
    onLoad(options) {
        if (options.q != undefined) {
            var qrCode = decodeURIComponent(options.q);
            var data = qrCode.split('?')[1];
            data.split('&').forEach(item => {
                if (item.split('=')[0] == 'distributorId') {
                    uni.setStorageSync("distributorId", item.split('=')[1]);
                }
                else if (item.split('=')[0] == 'platform') {
                    if (uni.getAccountInfoSync().miniProgram.appId == 'wx05cb4496de7a20d7') {
                        uni.setStorageSync("platform", item.split('=')[1] == 1 ? 'DZXCX' : item.split('=')[1]);
                    }
                    else {
                        uni.setStorageSync("platform", item.split('=')[1] == 1 ? 'JKWXAPP' : item.split('=')[1]);
                    }
                }
                else if (item.split('=')[0] == 'shopId') {
                    uni.setStorageSync("shopId", item.split('=')[1]);
                }
                else if (item.split('=')[0] == 'shopCode') {
                    uni.setStorageSync("shopCode", item.split('=')[1]);
                }
                else if (item.split('=')[0] == 'staffCode') {
                    uni.setStorageSync("superiorCode", item.split('=')[1]);
                }
            });
        }
        if (options.distributorId != undefined) {
            uni.setStorageSync("distributorId", options.distributorId);
        }
        if (options.shopId != undefined) {
            uni.setStorageSync("shopId", options.shopId);
        }
        if (options.shopCode != undefined) {
            uni.setStorageSync("shopCode", options.shopCode);
        }
        if (options.staffCode != undefined) {
            uni.setStorageSync("superiorCode", options.staffCode);
        }
        if (options.platform != undefined) {
            if (uni.getAccountInfoSync().miniProgram.appId == 'wx05cb4496de7a20d7') {
                uni.setStorageSync("platform", options.platform == 1 ? 'DZXCX' : options.platform);
            } else {
                uni.setStorageSync("platform", options.platform == 1 ? 'JKWXAPP' : options.platform);
            }
        }

        if (!uni.getStorageSync("applyfor") || uni.getStorageSync("applyfor") == null || uni.getStorageSync("applyfor") == undefined) {
            uni.setStorageSync("applyfor", true)
            uni.navigateTo({
                url: '../../../src/getuser/web_userid'
            });
        } else {
            uni.hideLoading()
            uni.setStorageSync("applyfor", '')
            if (uni.getStorageSync("openId") == '' || uni.getStorageSync("openId") == null || uni.getStorageSync("openId") == undefined) {
                this.getOpenid();
            }
            else {
                this.openidlogin();
            }
        }
    },
    methods: {
        // 获取店铺信息
        shopStatus() {
            this.$api.get(this, api.getShopStatusByShopCode, {
                shopCode: uni.getStorageSync("shopCode"),
                distributorId: uni.getStorageSync("distributorId"),
            }).then((res) => {
                if (res.success) {
                    uni.setStorageSync("shopName", res.data.shopName);
                } else {
                    uni.showToast({
                        title: res.errMessage,
                        icon: "none",
                        duration: 2000,
                    });
                }
            });
        },
        getPhoneNumber: function (e) {
            this.IsPhone = false;
            var that = this;
            console.log("手机号获取：", e.detail);
            if (e.detail.errMsg == "getPhoneNumber:ok") {
                // uni.login({
                //     provider: "weixin",
                //     success: function (loginRes) {
                that.openIdLoginFun(e.detail, that.code);
                // },
                // });
            }
        },
        // 小程序授权登录
        openIdLoginFun(detail, code) {
            let params = {
                appId: uni.getAccountInfoSync().miniProgram.appId,
                code: code,
                encryptedData: detail.encryptedData,
                iv: detail.iv,
                distributorId: uni.getStorageSync("distributorId")
            };
            this.$api.post(this, api.wechatLogin, params).then(res => {
                if (!res.success) {

                } else {
                    this.Form.phone = res.data;
                    this.IsPhone = false;
                }
            })
        },
        applyFor() {
            if (this.validateForm()) {
                this.openIds = [{
                    appId: uni.getAccountInfoSync().miniProgram.appId,
                    openId: uni.getStorageSync("openId"),
                    openType: 2//1 微信公众号 2 微信小程序 3 支付宝
                }]
                if (uni.getAccountInfoSync().miniProgram.appId == 'wx05cb4496de7a20d7') {
                    var obj = {
                        appId: 'wx7f20d1112a4e3559',
                        openId: uni.getStorageSync("h5openId"),
                        openType: 1//1 微信公众号 2 微信小程序 3 支付宝
                    }
                    this.openIds.push(obj);
                }
                this.$api.post(this, api.applyforsub, {
                    staffCode: uni.getStorageSync("userNo"),
                    openIds: this.openIds,
                    codeType: 6,
                    code: this.Form.code,
                    distributorId: uni.getStorageSync("distributorId"),
                    name: this.Form.nikeName,//申请人
                    openId: uni.getStorageSync("openId"),
                    phone: this.Form.phone,
                    platform: uni.getStorageSync("platform"),
                    shopCode: uni.getStorageSync("shopCode"),
                    shopName: uni.getStorageSync("shopName"),
                    type: this.type//申请平台类型:1 微信 2 支付宝 3 H5
                }).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            title: '申请成功',
                            icon: "none",
                            duration: 2000,
                            complete: function () {
                                setTimeout(function () {
                                    uni.switchTab({
                                        url: '/src/tabBar/my/my'
                                    })
                                }, 2000);
                            }
                        });
                    }
                    else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2500
                        });
                    }
                })
            }
        },

        validateForm() {
            if (this.validatePhone()) {
                if (this.loginIndex == 1) {
                    if (this.Form.nikeName == "") {
                        uni.showToast({
                            title: '请输入真实姓名',
                            icon: "none",
                            duration: 2000
                        });
                        return false;
                    }
                    // 验证码登录
                    if (this.Form.code == "") {
                        uni.showToast({
                            title: '请输入手机验证码',
                            icon: "none",
                            duration: 2000
                        });
                        return false;
                    }
                }
                return true;
            }
        },
        getCode() {
            let data = {
                phone: this.Form.phone,
                codeType: 6,
                skipCheck: false
            };
            if (this.validatePhone()) {
                // 获取验证码
                this.$api.post(this, api.getVerify, data).then((res) => {
                    if (res.success) {
                        uni.showToast({
                            title: '验证码已发送',
                            icon: "none",
                            duration: 2000
                        });
                        // 倒计时
                        if (!this.timer) {
                            this.count = TIME_COUNT;
                            this.timer = setInterval(() => {
                                if (this.count > 0 && this.count <= TIME_COUNT) {
                                    this.count--;
                                    this.str = "剩余 " + this.count + "s";
                                } else {
                                    clearInterval(this.timer);
                                    this.timer = null;
                                    this.str = "获取验证码";
                                }
                            }, 1000);
                        }
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                    }
                });
            }
        },
        validatePhone() {
            let reg = /^1[3456789]\d{9}$/;
            if (this.Form.phone == "") {
                uni.showToast({
                    title: '请输入手机号',
                    icon: "none",
                    duration: 2000
                });
                return false;
            } else {
                if (!reg.test(this.Form.phone)) {
                    uni.showToast({
                        title: '请输入正确的手机号码',
                        icon: "none",
                        duration: 2000
                    });
                    this.Form.phone = "";
                    return false;
                } else {
                    return true;
                }
            }
        },
        back() {
            uni.navigateBack({
                delta: 1
            });
        },
        //获取openid
        getOpenid() {
            var that = this;
            uni.login({
                provider: 'weixin',
                success: function (loginRes) {
                    that.$api.post(that, api.getOpenId, {
                        appId: uni.getAccountInfoSync().miniProgram.appId,
                        code: loginRes.code,
                        platformType: 2,//微信平台类型：1 公众号 2 小程序(不填默认为公众号)
                        distributorId: 8815,
                    }).then((res) => {
                        uni.setStorageSync("openId", res.data)
                        that.openidlogin();
                    })
                },
                fail: function (err) {
                }
            });
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
                    uni.setStorageSync("h5userId", '');
                    uni.setStorageSync("staffType", res.data.staffType);//店铺员类型 1.店长 2.导购员 3.消费者
                    uni.setStorageSync("userId", res.data.id);
                    uni.setStorageSync("userNo", res.data.no);
                    uni.setStorageSync("phone", res.data.phone);
                    this.Form.phone = res.data.phone || '';
                    uni.setStorageSync("customerShopCheck", res.data.customerShopCheck || 0);//店铺审核：0 否 1.是;无返回或为零都为否
                    uni.setStorageSync("rxShopSwitch", res.data.rxShopSwitch || 0);//是否启用店铺，1启用 0 不启用;无返回或为零都为否
                    uni.setStorageSync("aliAuthorize", res.data.aliAuthorize);//是否绑定支付宝
                    uni.setStorageSync("wxAuthorize", res.data.wxAuthorize);//是否绑定微信
                    if (this.Form.phone != '') {
                        this.IsPhone = false;
                    }
                    if (res.data.payWay) {
                        uni.setStorageSync("payWay", res.data.payWay);
                    }
                    this.shopStatus();

                }

            })
        },
    },

};
</script>
<style scoped lang="stylus">
.login-wechatButton {
    text-align: left;
    font-size: 24rpx;
}

.login-wechatButton:after {
    border: 0 !important;
}

.content_box {
    width: 690rpx;
    height: 690rpx;
    position: fixed;
    bottom: 200rpx;
    background-color: #4C9EE7;
    border-radius: 40rpx;
    left: 30rpx;

    image {
        width: 690rpx;
        height: 300rpx;
    }

    .div-box {
        margin-left: 30rpx;
        width: 630rpx;
        height: 80rpx;
        line-height: 80rpx;
        box-sizing: border-box;
        color: $gray;
        background-color: #ffffff;
        border-radius: 90rpx;
        padding-left: 30rpx;

        /deep/.uni-input-placeholder {
            padding-left: 30rpx;
            font-size: 28rpx;
        }

        /deep/.uni-input-input {
            padding: 0 30rpx;
            font-size: 28rpx;
        }

        &.code {
            display: flex;
            margin-top: 30rpx;
            flex-direction: row;
            flex-wrap: nowrap;
            justify-items: center;
            align-items: center;

            input {
                flex: 1;
            }

            .text {
                display: inline-block;
                width: 200rpx;
                height: 48rpx;
                line-height: 48rpx;
                margin-left: 30rpx;
                font-size: 28rpx;
            }
        }
    }

    .login_code {
        margin-right: 20rpx;
    }
}

.btn-normal {
    // margin: 90rpx auto 0;
    width: 400rpx;
    height: 100rpx;
    // background-image: url('https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681977095845.png') ;
    // background-position: top !important;
    // background-size: 60%;
    left: 175rpx;
    position: fixed;
    bottom: 50rpx;
}

.apply_for_box {
    width: 750rpx;
    height: 100vh;
    background-image: url('https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51681975979440.jpg');
    background-position: top !important;
    background-size: 100% 100vh;
}

.top_bg_box {
    position: fixed;
    bottom: 200rpx;
    width: 750rpx;
}
</style>

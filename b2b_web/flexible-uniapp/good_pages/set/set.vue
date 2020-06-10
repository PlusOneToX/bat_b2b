<template>
    <view>
        <!-- <uni-nav-bar left-icon="left" title="设置" shadow @clickLeft="back" /> -->
        <view style="height: 40rpx;"></view>
        <view class="set-box">
            <view class="set-module">
                <view class="set-module-line" @click="file_img">
                    <view class="set-line-lf">
                        <image :src="userInfo.headPortrait" class="headerIcon"></image>
                    </view>
                    <view class="set-line-rg">
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
                <view class="set-module-line" @click="toClick1">
                    <view class="set-line-lf">昵称</view>
                    <view class="set-line-rg">
                        <text>{{userInfo.nikeName}}</text>
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
                <!-- '../../pages/set/modifyPhone' -->
                <view class="set-module-line" @click="toClick">
                    <view class="set-line-lf">手机号码</view>
                    <view class="set-line-rg">
                        <text>{{userInfo.phone||''}}</text>
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
            </view>
        </view>
        <view class="set-box">
            <view class="set-module">
                <view class="set-module-line" @click="goAgreement">
                    <view class="set-line-lf">用户协议</view>
                    <view class="set-line-rg">
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
            </view>
        </view>
        <!-- #ifdef H5 -->
        <view class="set-box" v-if="staffType!=3&&platform!=7">
            <view class="set-module">
                <view class="set-module-line">
                    第三方账号绑定
                </view>
                <view class="set-module-line">
                    <view class="set-line-lf">微信</view>
                    <view class="set-line-rg" @click="wxpay()">
                        <text>{{wxAuthorize==0?'未绑定':'已绑定'}}</text>
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
                <view class="set-module-line">
                    <view class="set-line-lf">支付宝</view>
                    <view class="set-line-rg" @click="alipay()">
                        <text>{{aliAuthorize==0?'未绑定':'已绑定'}}</text>
                        <uni-icons type="forward" size="24" class="icon_set" style="color: rgba(154, 154, 154, 1);"></uni-icons>
                    </view>
                </view>
            </view>
        </view>
        <!-- #endif -->
        <view class="set-loginOut" @click="loginOut" v-show="platform!=7">退出登录</view>
    </view>
</template>

<script>
import api from "common/js/allApi.js";
export default {
    data() {
        return {
            userInfo: {
                id: 0,
                name: "",
                nikeName: "游客",
                sex: 0,
                birthday: "",
                headPortrait: "",
                phone: "",
                password: "",

            },
            userId: '',
            appId: 'wxd111f057f262d51b',   // wx05cb4496de7a20d7 正式
            aliAuthorize: 0,//是否绑定支付宝
            wxAuthorize: 0,//是否绑定微信
            staffType: 3,
            platform: 0,
        };
    },
    onShow() {
        this.staffType = uni.getStorageSync("staffType");
        this.$api.get(this, api.getUserInfo, {
            id: this.userId,
            shopCode: uni.getStorageSync("shopCode") || ''
        }).then((res) => {
            if (res.data.payWay) {
                uni.setStorageSync("payWay", res.data.payWay);
            }
            if (res.data.openIdDTOS != undefined) {
                res.data.openIdDTOS.forEach(item => {
                    if (item.openType == 1 || item.openType == 2) {
                        this.aliAuthorize = 1;
                    }
                    if (item.openType == 3) {
                        this.wxAuthorize = 1;
                    }
                });
            }

            this.userInfo = { ...res.data };
            // 设置默认头像
            if (this.userInfo.headPortrait == "" ||
                this.userInfo.headPortrait == null ||
                this.userInfo.headPortrait == undefined
            ) {
                this.userInfo.headPortrait = "/static/images/index_login.png";
            }
        })
    },
    onLoad() {
        uni.setStorageSync("setText", true);
        this.platform = uni.getStorageSync("platform")
        this.userId = parseInt(uni.getStorageSync("userId"));

    },
    methods: {
        goAgreement() {
            uni.navigateTo({
                url: '../theme/agreement'
            });
        },
        wxpay() {
            if (this.wxAuthorize == 1) {
                uni.navigateTo({
                    url: './unbind?type=wx&openIdDTOS=' + JSON.stringify(this.userInfo.openIdDTOS)
                });
            } else {
                this.$api.get(this, api.GetwxId, {
                    appType: 2,//应用类型：1 微信小程序 2 微信公众号 3 支付宝
                    distributorId: uni.getStorageSync("distributorId"),
                }
                ).then((res1) => {
                    this.$api.post(this, api.getMiniappUrl, {
                        officialAppId: res1.data,//公众号id
                        userUnique: '',//用户标识：可以是用户的openId(微信内部扫描二维码时，必传)
                        urlType: 1,//url类型 1 微信小程序URL scheme 2 微信小程序URL Link 3 微信小程序Short Link 4 H5链接
                        shopId: uni.getStorageSync("shopId"),
                        platform: this.platform == 1 ? 'JKWXAPP' : this.platform,
                        distributorId: uni.getStorageSync("distributorId"),
                        miniAppId: this.appId,
                        shopCode: uni.getStorageSync("shopCode"),
                        staffCode: uni.getStorageSync("userNo")
                    }
                    ).then((res) => {
                        if (res.success) {
                            window.location.replace(res.data);
                        }
                        console.log("链接返回参数：", res);
                    })
                })
            }
        },
        //支付宝授权
        alipay() {
            if (this.aliAuthorize == 1) {
                uni.navigateTo({
                    url: './unbind?type=ali'
                });
            } else {
                this.$api.get(this, api.GetAlipayId, {
                    appType: 3,//应用类型：1 微信小程序 2 微信公众号 3 支付宝
                    distributorId: uni.getStorageSync("distributorId"),
                }
                ).then((res) => {
                    let url = encodeURIComponent('http://test.bat.com/diyh5/good_pages/set/authorization?distributorId=' + uni.getStorageSync("distributorId") + '&shopCode=' + uni.getStorageSync("shopCode") + '&staffCode=' + uni.getStorageSync("userNo"));
                    let alipayUrl = 'https://openauth.alipay.com/oauth2/publicAppAuthorize.htm?app_id=' + res.data + '&scope=auth_user&redirect_uri=' + url;
                    let openURL = 'alipays://platformapi/startapp?appId=20000067&url=' + encodeURIComponent(alipayUrl);
                    window.location.replace(openURL)
                })
            }
        },
        getUrlCode() {
            let url = location.search;
            /* eslint-disable */
            let theRequest = new Object();
            if (url.indexOf("?") !== -1) {
                let str = url.substr(1);
                let strs = str.split("&");
                for (let i = 0; i < strs.length; i++) {
                    theRequest[strs[i].split("=")[0]] = strs[i].split("=")[1];
                }
                return theRequest;
            }
        },
        // 跳转
        toClick1() {
            uni.navigateTo({
                url: './setName',
            });
        },
        toClick() {
            if (this.userInfo.phone != '' && this.userInfo.phone != undefined) {
                uni.navigateTo({
                    url: './modifyPhone'
                });
            } else {
                uni.navigateTo({
                    url: './binding'
                });
            }

        },
        file_img() {
            var that = this;
            uni.chooseImage({
                count: 1, //默认9
                sizeType: ['compressed'], //可以指定是原图还是压缩图，默认二者都有
                sourceType: ['album'], //从相册选择
                success: function (res) {
                    uni.showLoading({
                        title: '更新中',
                        mask: true
                    });
                    let file = res.tempFiles[0];
                    // 随机命名
                    let randomName = that.random_string(6) + "_" + new Date().getTime() + "." + file.path.split(".").pop();
                    that.$api.get(that, api.getOSSSts, { dir: 'diy/' }).then((result) => {
                        const index = file.path.lastIndexOf("\.");
                        const imgExtension = file.path.substring(index + 1, file.length);
                        const imgPath = 'diy/h5' + new Date().getTime() + "." + imgExtension;
                        uni.uploadFile({
                            url: result.data.hostname,
                            filePath: file.path,
                            name: 'file',
                            formData: {
                                name: randomName,
                                key: imgPath,
                                policy: result.data.policy,
                                OSSAccessKeyId: result.data.accessKeyId,
                                success_action_status: "200",
                                signature: result.data.signature,
                                'x-oss-security-token': result.data.securityToken
                            },
                            success: (uploadFileRes) => {
                                // 更新头像
                                that.userInfo.headPortrait = result.data.hostname + imgPath;
                                that.updateUserInfo(that.userInfo, 4);
                                uni.hideLoading()
                            }, fail: function (res) {
                                console.log("失败了", res)
                                uni.hideLoading()
                            }
                        });

                    })
                }
            });
        },
        updateUserInfo(userInfo, type) {
            console.log("信息：", userInfo);
            this.$api.put(this, api.updateUserInfo, userInfo).then((res) => {

            })
        },
        back() {
            uni.switchTab({
                url: '../../src/tabBar/my/my'
            })
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
            // uni.redirectTo({
            //     url: '../login/login?platform=' + uni.getStorageSync("platform") + '&distributorId=' + uni.getStorageSync("distributorId")
            // });
            uni.redirectTo({
                url: '../login/login'
            });
        },
        // 随机生成文件名
        random_string(len) {
            len = len || 32;
            let chars = "ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678";
            let maxPos = chars.length;
            let pwd = "";
            for (let i = 0; i < len; i++) {
                pwd += chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        },
    },
};

</script>

<style lang="scss" scoped>
.headerIcon {
    width: 100rpx;
    height: 100rpx;
    border-radius: 50%;
    box-shadow: 0rpx 4rpx 12rpx 0rpx rgba(0, 0, 0, 0.4);
    border: 1rpx solid rgba(232, 232, 232, 1);
}
.set-loginOut {
    width: 86%;
    height: 80rpx;
    background: #b9b9b9;
    line-height: 80rpx;
    text-align: center;
    font-size: 36rpx;
    color: #fff;
    left: 7%;
    bottom: 40rpx;
    position: fixed;
}
.set-box {
    width: 710rpx;
    background-color: rgba(255, 255, 255, 1);
    border: 1rpx solid rgba(239, 239, 239, 1);
    margin: 0 auto;
    border-radius: 30rpx;
    margin-bottom: 20rpx;
    .set-module {
        // margin-top: 20rpx;
        .set-module-line {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 30rpx;
            border-bottom: 1rpx solid rgba(241, 242, 249, 0.5);
            font-size: 28rpx;
            .set-line-lf {
                font-weight: 400;
                color: rgba(16, 16, 16, 1);
                font-size: 26rpx;
                text-align: left;
                font-family: PingFangSC-regular;
            }
            .set-line-rg {
                color: rgba(154, 154, 154, 1);
                font-size: 24rpx;
                font-family: PingFangSC-regular;
                width: 500rpx;
                display: flex;
                justify-content: flex-end;
                align-items: center;
                .icon_set {
                    margin-left: 15rpx;
                }
            }
        }
        .set-module-line:last-child {
            border: none;
        }
    }
}
</style>
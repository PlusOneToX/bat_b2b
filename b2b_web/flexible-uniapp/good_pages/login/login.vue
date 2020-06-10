<template>
    <view class="login-wrapper" v-show="showLogin">
        <!-- #ifdef MP-WEIXIN -->
        <uni-nav-bar dark :fixed="true" background-color="#1275d1" status-bar title="登录" />
        <!-- #endif -->
        <view class="login-bg">
            <h6 class="title">BAT</h6>
            <p class="intro">定制你的专属手机壳</p>
            <img src="../../static/images/login_bg.png" alt="" />
        </view>
        <view class="main" v-show="showLogin">
            <span class="sprite-icon login_logo"></span>
            <view class="div-box code">
                <input type="digit" maxlength="11" v-model="loginForm.phone" placeholder="请输入手机号" @blur="validatePhone" :left-icon="'sprite-icon login_icon_phone'" clearable />
            </view>

            <view class="div-box code" v-if="loginIndex == 1">
                <input type="digit" v-model="loginForm.code" placeholder="请输入手机验证码" maxlength="6" :left-icon="'sprite-icon login_icon_verification'" clearable />
                <view class="login_code" @click="getCode" v-if="loginIndex == 1">
                    {{str}}
                </view>
            </view>
            <view class="div-box code" v-else>
                <input v-model="loginForm.password" type="password" placeholder="请输入密码" :right-icon="passwordShow ? 'eye-o' : 'closed-eye'" @click-right-icon="onClickIcon" :left-icon="'sprite-icon login_icon_password'" clearable />
            </view>
            <view class="btn btn-normal" @click="login">登录</view>
        </view>
        <view class="login-choose">
            <view class="login-toggle" v-if="loginIndex == 1" @click="toggle">
                {{ pwdValue }}
            </view>
            <view class="login-toggle" v-else @click="toggle">{{ codeValue }}</view>
        </view>
    </view>
</template>

<script type="text/ecmascript-6">
/* eslint-disable */
import md5 from "js-md5";
// api
import api from "./../../common/js/allApi";
import {
    getXsaUid,
    getClientOsVersion,
    getClientModel,
    getPackageName,
    getPackageVersion,
} from "common/js/saApi";
import { getXmlNode } from "common/js/common";
import { Base64 } from "js-base64";
const TIME_COUNT = 60;
export default {
    name: "login",
    data() {
        return {
            name: "",
            title: "登录",
            isBack: true, // 登录返回按钮
            saCoupon: 0, // 是否从优惠券专题跳转，1：Note20  2：S20 3:S21 4:A52
            platform: "",
            distributorId: "",
            authUid: "",
            uid: "",
            otherUid: "",
            loginForm: {
                phone: "",
                password: "",
                code: "",
            },
            userData: {}, // 登录的用户信息
            loginIndex: 1,
            pwdValue: "用密码登录",
            codeValue: "用验证码登录",
            show: true,
            str: "获取验证码",
            count: 0,
            timer: null,
            passwordShow: false,
            firstEnter: false,
            keyboardShow: false,
            isSaLogin: false,
            showLogin: false,
            isLoading: false,
            message: "", // loading提示
            comingFlag: "", // 标识进入页面
            samFlag: "",
            couponIds: "",
            hasQualified: false,
            couponType: "",
            appid: "",
            pid: "",
            pUrl: "",
            pType: "",
            orderId: "",
            fcp_shopId: '',
            status: '',
            country: '',
            authToken: '',
            enterParams: '',
            comingType: ''
        };
    },
    onLoad(parameter) {
        this.saCoupon = parameter.sa;
        this.fcp_shopId = parameter.fcp_shopId;
        this.status = parameter.status;
        this.country = parameter.country;
        this.authToken = parameter.authToken;
        this.enterParams = parameter.enterParams;
        this.comingType = parameter.comingType;

        this.samFlag = parameter.samFlag;
        this.couponIds = parameter.ids;
        this.comingFlag = parameter.comingFlag;
        if (this.comingFlag == undefined || !this.comingFlag || this.comingFlag == null || this.comingFlag == "" || this.comingFlag == "null") {
            this.comingFlag = uni.getStorageSync("comingFlag")
        }
        uni.removeStorage('comingFlag')
        this.hasQualified = parameter.hasQualified;
        // 从优惠券专题页跳转
        this.couponType = parameter.couponType;
        this.appid = parameter.appid || uni.getStorageSync("appid");

        // 从专题活动页跳转
        let sa = parameter.sa;
        if (sa) {
            uni.setStorageSync("sa", sa);
        } else {
            uni.removeStorage("sa");
        }

        // 定制信息（登录跳转定制页）
        this.pid = parameter.pid;
        if (this.pid == undefined || !this.pid) {
            this.pid = uni.getStorageSync("picId")
        }
        uni.removeStorage('picId')
        this.pUrl = parameter.url;
        if (this.pUrl == undefined || !this.pUrl) {
            this.pUrl = uni.getStorageSync("pUrl")
        }
        uni.removeStorage('pUrl')
        this.pType = parameter.pType || parameter.type;
        if (this.pType == undefined || !this.pType) {
            this.pType = uni.getStorageSync("pType")
        }
        uni.removeStorage('pType')

        // // 订单详情（登录跳转订单详情页面）
        // this.orderId = parameter.id
        // if (this.orderId == undefined || !this.orderId) {
        //     uni.setStorageSync("orderId", this.orderId)
        // }


        // 3-判断是否第三方登录
        let plat = parameter.platform;
        let distr = parameter.distributorId;
        let platform = uni.getStorageSync("platform");
        let distributorId = uni.getStorageSync("distributorId");
        if (plat && distr) {
            this.platform = plat;
            this.distributorId = distr;
            if (plat == 7) {
                // 用于新旧版本tabs显示
                uni.setStorageSync("curVersion", "");
                if (sa != "2") {
                    uni.setStorageSync("sa", "1");
                }
            } else {
                this.showLogin = true;
            }
        } else if (platform && distributorId) {
            this.platform = platform;
            this.distributorId = distributorId;
            if (platform == 7) {
                // 用于新旧版本tabs显示
                uni.setStorageSync("curVersion", "");
                if (sa != "2") {
                    uni.setStorageSync("sa", "1");
                }
            } else {
                this.showLogin = true;
            }
        } else {
            this.showLogin = true;
            this.platform = "6";
            this.distributorId = "2601";
        }
        uni.setStorageSync("platform", this.platform);
        uni.setStorageSync("distributorId", this.distributorId);
        if (this.platform && this.platform == 7) {
            let shopId = parameter.fcp_shopId;
            if (shopId) {
                // 获取店铺状态
                this.ShopStatus(shopId);
            } else {
                uni.setStorageSync("loginSan", 71)
                let isLowerVersion = uni.getStorageSync("isLowerVersion") || "";
                if (!isLowerVersion) {
                    window["onSamLogin"] = (saInfo) => {
                        console.log("saInfo对象:", saInfo);
                        this.onSamLogin(saInfo);
                    };
                    window.SamsungAccount.login(false, "onSamLogin");
                }
            }
        }
    },
    methods: {
        onSamLogin(saInfo) {
            let obj = JSON.parse(saInfo);
            let shopId = this.fcp_shopId;

            if (obj.loginStatus == "login") {
                // 第三方帐号已登录
                let status = this.status;
                let country = this.country;
                let authToken = this.authToken;
                // 1.判断是否授权
                let isLogin = "true";
                if (isLogin == "true") {
                    // 2.判断是否已经登录，userNo 是否存在
                    let userNo = uni.getStorageSync("userNo").val || uni.getStorageSync("userNo");
                    let authUid = uni.getStorageSync("authenticateUserID").val || uni.getStorageSync("authenticateUserID");
                    let otherUid = uni.getStorageSync("uid").val || uni.getStorageSync("uid");


                    if (userNo != null && userNo != undefined && userNo != "") {
                        this.showLogin = false;
                        let userAgent = navigator.userAgent;
                        let uid = getXsaUid(userAgent);
                        console.log("uid:", uid);
                        if (uid == otherUid) {
                            console.log("authUid：", authUid);
                            console.log("otherUid：", otherUid);
                            this.loginOpenId(authUid, otherUid);
                        } else {
                            // 不是同一个帐户登录
                            this.clearData();
                            let sa = this.saCoupon;//uni.getStorageSync("sa");
                            if (
                                this.samFlag == "samS20Flag" ||
                                this.samFlag == "S21Coupon" ||
                                this.samFlag == "samMemberCoupon" ||
                                this.samFlag == "samNewCoupon" ||
                                this.samFlag == "storeCoupon" ||
                                this.samFlag == "christmas" ||
                                this.samFlag == "samNote20Flag" ||
                                this.samFlag == "samA52Flag" ||
                                this.samFlag == "samS23Flag"
                            ) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    "&samFlag=" +
                                    this.samFlag +
                                    "&ids=" +
                                    this.couponIds +
                                    "&hasQualified=" +
                                    this.hasQualified +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else if (this.pid) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    "&pid=" +
                                    this.pid +
                                    "&url=" +
                                    this.pUrl +
                                    "&pType=" +
                                    this.pType +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else if (this.orderId) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    "&id=" +
                                    this.orderId +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else if (shopId) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    "&samFlag=storeCoupon" +
                                    "&fcp_shopId=" +
                                    shopId +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            }
                        }
                    } else {
                        this.message = "载入中";
                        this.isLoading = true;
                        // this.showLogin = true
                        // 3. 判断是否有authenticateUserID
                        console.log("判断是否有authenticateUserID:", authUid, otherUid);
                        if (authUid && otherUid) {
                            // 3.1用户通过第三方openId登录
                            // 判断是否设置了密码
                            let isflag = uni.getStorageSync("isflag");
                            if (isflag == 1 || isflag == "1") {
                                uni.removeStorage("isflag");
                            } else {
                                this.loginOpenId(authUid, otherUid);
                            }
                        } else {
                            console.log("判断是否有授权回调信息:", authToken, status, country);
                            // 3.2 判断是否有授权回调信息
                            if (authToken && status && country) {
                                let userAgent = navigator.userAgent;
                                let uid = getXsaUid(userAgent);
                                let API_URL;
                                uni.setStorageSync("uid", uid.val);
                                if (status == "signInSuccess") {
                                    if (country == "CN") {
                                        API_URL = api.cnApiSam;
                                    } else {
                                        API_URL = api.ApiSam;
                                    }
                                    // token验证
                                    this.getApi(authToken, API_URL, userAgent);
                                } else {
                                    window.location.href = "action://samsung.account.login.fail";
                                }
                            } else {
                                let sa = this.saCoupon;// uni.getStorageSync("sa");
                                if (sa) {
                                    if (
                                        this.samFlag == "samS20Flag" ||
                                        this.samFlag == "S21Coupon" ||
                                        this.samFlag == "samMemberCoupon" ||
                                        this.samFlag == "samNewCoupon" ||
                                        this.samFlag == "storeCoupon" ||
                                        this.samFlag == "christmas" ||
                                        this.samFlag == "samNote20Flag" ||
                                        this.samFlag == "samA52Flag" ||
                                        this.samFlag == "samS23Flag"
                                    ) {
                                        window.location.href =
                                            "action://samsung.account.login?cp=JieKeTe&callback=" +
                                            window.location.href.split("?")[0] +
                                            "?distributorId=" +
                                            this.distributorId +
                                            "&platform=" +
                                            this.platform +
                                            "&sa=" +
                                            sa +
                                            "&samFlag=" +
                                            this.samFlag +
                                            "&ids=" +
                                            this.couponIds +
                                            "&hasQualified=" +
                                            this.hasQualified +
                                            (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                                    } else if (this.pid) {
                                        window.location.href =
                                            "action://samsung.account.login?cp=JieKeTe&callback=" +
                                            window.location.href.split("?")[0] +
                                            "?distributorId=" +
                                            this.distributorId +
                                            "&platform=" +
                                            this.platform +
                                            "&sa=" +
                                            sa +
                                            "&pid=" +
                                            this.pid +
                                            "&url=" +
                                            this.pUrl +
                                            "&pType=" +
                                            this.pType +
                                            (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                                    } else if (this.orderId) {
                                        window.location.href =
                                            "action://samsung.account.login?cp=JieKeTe&callback=" +
                                            window.location.href.split("?")[0] +
                                            "?distributorId=" +
                                            this.distributorId +
                                            "&platform=" +
                                            this.platform +
                                            "&sa=" +
                                            sa +
                                            "&id=" +
                                            this.orderId +
                                            (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                                    } else if (shopId) {
                                        window.location.href =
                                            "action://samsung.account.login?cp=JieKeTe&callback=" +
                                            window.location.href.split("?")[0] +
                                            "?distributorId=" +
                                            this.distributorId +
                                            "&platform=" +
                                            this.platform +
                                            "&sa=" +
                                            sa +
                                            "&samFlag=storeCoupon" +
                                            "&fcp_shopId=" +
                                            shopId +
                                            (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                                    } else {
                                        window.location.href =
                                            "action://samsung.account.login?cp=JieKeTe&callback=" +
                                            window.location.href.split("?")[0] +
                                            "?distributorId=" +
                                            this.distributorId +
                                            "&platform=" +
                                            this.platform +
                                            "&sa=" +
                                            sa +
                                            (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                // 第三方帐号未登录，清除缓存并跳出
                this.clearData();
                window.location.href = "action://samsung.account.login.fail";
            }
        },
        // 清空缓存
        clearData() {
            uni.removeStorage("staffType");
            uni.removeStorage("customerShopCheck");
            uni.removeStorage("rxShopSwitch");
            uni.removeStorage("shopCode");
            uni.removeStorage("shopName");
            uni.removeStorage("userId");
            uni.removeStorage("userNo");
            uni.removeStorage("phone");
            uni.removeStorage("goods");
            uni.removeStorage("uid");
            uni.removeStorage("auth");
            uni.removeStorage("authenticateUserID");
            uni.removeStorage("aliAuthorize");//是否绑定支付宝
            uni.removeStorage("wxAuthorize");//是否绑定微信 
        },
        onInput(key) {
            this.loginForm.phone = this.loginForm.phone + "" + key;
        },
        login() {
            let shopId = this.fcp_shopId;
            if (this.validateForm()) {
                if (this.platform == 7) {
                    // 判断是否有authenticateUserID
                    let auth = uni.getStorageSync("authenticateUserID").val || uni.getStorageSync("authenticateUserID");
                    let uid = uni.getStorageSync("uid").val || uni.getStorageSync("uid");
                    if (auth && uid) {
                        this.loginType();
                    } else {
                        // 重新获取authenticateUserID
                        let phone = this.loginForm.phone;
                        let code;
                        if (this.loginIndex == 1) {
                            code = this.loginForm.code;
                        } else {
                            code = this.loginForm.password;
                        }
                        let sa = this.saCoupon;// uni.getStorageSync("sa");
                        if (sa) {
                            if (
                                this.samFlag == "samS20Flag" ||
                                this.samFlag == "S21Coupon" ||
                                this.samFlag == "S23Coupon" ||
                                this.samFlag == "samMemberCoupon" ||
                                this.samFlag == "samNewCoupon" ||
                                this.samFlag == "storeCoupon" ||
                                this.samFlag == "christmas" ||
                                this.samFlag == "samNote20Flag" ||
                                this.samFlag == "samA52Flag" ||
                                this.samFlag == "samS23Flag"
                            ) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&type=" +
                                    this.loginIndex +
                                    "&phone=" +
                                    phone +
                                    "&code=" +
                                    code +
                                    "&sa=" +
                                    sa +
                                    "&samFlag=" +
                                    this.samFlag +
                                    "&ids=" +
                                    this.couponIds +
                                    "&hasQualified=" +
                                    this.hasQualified +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else if (this.pid) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&type=" +
                                    this.loginIndex +
                                    "&phone=" +
                                    phone +
                                    "&code=" +
                                    code +
                                    "&sa=" +
                                    sa +
                                    "&pid=" +
                                    this.pid +
                                    "&url=" +
                                    this.pUrl +
                                    "&pType=" +
                                    this.pType +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else if (this.orderId) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&sa=" +
                                    sa +
                                    "&id=" +
                                    this.orderId +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null ? "&comingFlag=" + this.comingFlag : "");
                            } else if (shopId) {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&type=" +
                                    this.loginIndex +
                                    "&phone=" +
                                    phone +
                                    "&code=" +
                                    code +
                                    "&sa=" +
                                    sa +
                                    "&samFlag=storeCoupon" +
                                    "&ids=" +
                                    this.couponIds +
                                    "&hasQualified=" +
                                    this.hasQualified +
                                    "&fcp_shopId=" +
                                    shopId +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            } else {
                                window.location.href =
                                    "action://samsung.account.login?cp=JieKeTe&callback=" +
                                    window.location.href.split("?")[0] +
                                    "?distributorId=" +
                                    this.distributorId +
                                    "&platform=" +
                                    this.platform +
                                    "&type=" +
                                    this.loginIndex +
                                    "&phone=" +
                                    phone +
                                    "&code=" +
                                    code +
                                    "&sa=" +
                                    sa +
                                    (this.comingFlag != undefined && this.comingFlag != "" && this.comingFlag != null && this.comingFlag != "null" ? "&comingFlag=" + this.comingFlag : "");
                            }
                        }
                    }
                } else {
                    // 直接登录
                    this.loginType();
                }
            }
        },
        loginType() {
            // 获取第三方的uid
            let loginInfo;
            let otherId = uni.getStorageSync("uid").val || uni.getStorageSync("uid");
            let openId = uni.getStorageSync("authenticateUserID").val || uni.getStorageSync("authenticateUserID");
            if (this.loginIndex == 1) {
                if (this.platform && this.platform == "7") {
                    loginInfo = {
                        phone: this.loginForm.phone,
                        code: this.loginForm.code,
                        otherUid: otherId,
                        openId: openId,
                        codeType: 4, // 验证码登录
                    };
                } else {
                    loginInfo = {
                        phone: this.loginForm.phone,
                        code: this.loginForm.code,
                        codeType: 4, // 验证码登录
                    };
                }

                // 验证码登录
                this.$api.post(this, api.verifyLogin, loginInfo).then((res) => {
                    if (res.success) {
                        this.validateLogin(res.data);
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                    }
                });
            } else {
                if (this.platform && this.platform == "7") {
                    loginInfo = {
                        phone: this.loginForm.phone,
                        password: md5(this.loginForm.password.toString()),
                        otherUid: otherId,
                        openId: openId,
                    };
                } else {
                    loginInfo = {
                        phone: this.loginForm.phone,
                        password: md5(this.loginForm.password),
                    };
                }

                // 密码登录
                this.$api.post(this, api.passwordLogin, loginInfo).then((res) => {
                    if (res.success) {
                        this.validateLogin(res.data);
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
        // 存储用户信息
        validateLogin(data) {
            var payWay = '' + data.payWay;
            this.message = "";
            this.isLoading = false;
            uni.setStorageSync("staffType", data.staffType);//店铺员类型 1.店长 2.导购员 3.消费者
            uni.setStorageSync("userId", data.id);
            uni.setStorageSync("userNo", data.no);
            uni.setStorageSync("phone", data.phone);
            uni.setStorageSync("customerShopCheck", data.customerShopCheck || 0);//店铺审核：0 否 1.是;无返回或为零都为否
            uni.setStorageSync("rxShopSwitch", data.rxShopSwitch || 0);//是否启用店铺，1启用 0 不启用;无返回或为零都为否
            uni.setStorageSync("aliAuthorize", data.aliAuthorize);//是否绑定支付宝
            uni.setStorageSync("wxAuthorize", data.wxAuthorize);//是否绑定微信 
            let shopCode = uni.getStorageSync("shopCode");
            let shopName = uni.getStorageSync("shopName");
            if (shopCode != undefined && shopCode == "") {
                uni.setStorageSync("shopCode", data.shopCode != undefined && data.shopCode != "" ? data.shopCode : '');
            }
            if (shopName != undefined && shopName == "") {
                uni.setStorageSync("shopName", data.shopName != undefined && data.shopName != "" ? data.shopName : '');
            }
            shopCode = uni.getStorageSync("shopCode");
            shopName = uni.getStorageSync("shopName");
            if (shopCode != undefined && shopCode == "") {
                uni.setStorageSync("shopCode", data.shopCode != undefined && data.shopCode != "" ? data.shopCode : '');
            }
            if (shopName != undefined && shopName == "") {
                uni.setStorageSync("shopName", data.shopName != undefined && data.shopName != "" ? data.shopName : '');
            }
            // if (data.payWay) {
            uni.setStorageSync("payWay", payWay);
            // }
            this.pushTo();
        },
        pushTo() {
            // 在C页面内 navigateBack，将返回A页面
            // uni.switchTab({
            //     url: '/src/components/index/index'
            // });
            this.toback();
        },
        validateForm() {
            if (this.validatePhone()) {
                if (this.loginIndex == 1) {
                    // 验证码登录
                    if (this.loginForm.code == "") {
                        uni.showToast({
                            title: '请输入手机验证码',
                            icon: "none",
                            duration: 2000
                        });
                        return false;
                    }
                } else {
                    // 密码登录
                    if (this.loginForm.password == "") {
                        uni.showToast({
                            title: '请输入密码',
                            icon: "none",
                            duration: 2000
                        });
                        return false;
                    }
                }
                return true;
            }
        },
        validatePhone() {
            let reg = /^1[3456789]\d{9}$/;
            if (this.loginForm.phone == "") {
                uni.showToast({
                    title: '请输入手机号',
                    icon: "none",
                    duration: 2000
                });
                return false;
            } else {
                if (!reg.test(this.loginForm.phone)) {
                    uni.showToast({
                        title: '请输入正确的手机号码',
                        icon: "none",
                        duration: 2000
                    });
                    this.loginForm.phone = "";
                    return false;
                } else {
                    return true;
                }
            }
        },
        getCode() {
            let data = {
                phone: this.loginForm.phone,
                codeType: 4, // 验证码登录
            };
            this.clearData();

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
                            this.show = false;
                            this.timer = setInterval(() => {
                                if (this.count > 0 && this.count <= TIME_COUNT) {
                                    this.count--;
                                    this.str = "剩余 " + this.count + "s";
                                } else {
                                    this.show = true;
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
        onClickIcon() {
            this.passwordShow = !this.passwordShow;
        },
        loginOpenId(authUid, otherUid) {
            uni.removeStorage("auth");
            // 用户通过第三方openId登录
            this.$api.post(this, api.thirdpartyLogin, {
                openId: authUid,
                otherUid: otherUid,
            }).then((res) => {
                if (res.success) {
                    this.validateLogin(res.data);
                } else {
                    this.message = "";
                    this.isLoading = false;
                    this.showLogin = true;
                }
            });
        },
        getApi(authToken, url, ua) {
            // AppId: yyli4t740o
            // AppSecret: CF5D3D8AB06BDDFD3715669798DAA76E
            // AppId: ok6rqzl8gj
            // AppSecret: 1290909EC1CDA758F25E4850789738CC
            // 加密
            let authorization =
                "Basic " + Base64.encode("yyli4t740o:CF5D3D8AB06BDDFD3715669798DAA76E");
            let appId = "yyli4t740o";
            let clientosversion = getClientOsVersion(ua);
            let clientmodel = getClientModel(ua);
            let packagename = getPackageName();
            let packageversion = getPackageVersion(ua);
            this.$api.post(this, api.isAuthToken, {
                authToken: authToken,
                authorization: authorization,
                XOspAppId: appId,
                XOspClientosversion: clientosversion,
                XOspClientmodel: clientmodel,
                XOspPackagename: packagename,
                XOspPackageversion: packageversion,
            })
                .then((res) => {
                    if (res.success) {
                        // 获取状态
                        let status = getXmlNode(res.data, "authorizeDesc");
                        // 获取authenticateUserID
                        if (status && status[0] == "SUCCESS") {
                            let authUid = getXmlNode(res.data, "authenticateUserID");
                            if (authUid) {
                                let userAgent = navigator.userAgent;
                                let uid = getXsaUid(userAgent);
                                uni.setStorageSync("uid", uid);
                                uni.setStorageSync("authenticateUserID", authUid[0]);
                                this.loginOpenId(authUid[0], uid);
                            }
                        } else {
                            uni.showToast({
                                title: res.errMessage,
                                icon: "none",
                                duration: 2000
                            });
                        }
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                        window.location.href = "action://samsung.account.login.fail";
                    }
                })
                .catch((error) => {
                    uni.showToast({
                        title: error,
                        icon: "none",
                        duration: 2000
                    });
                });
        },
        toback() {

            if (this.saCoupon == 3) {
                // S20 专题页
                uni.reLaunch({
                    url: '/good_pages/samsung/samsungActivePartition'
                });
            }
            else if (this.saCoupon == 4) {
                // 订单详情
                uni.reLaunch({
                    url: '/good_pages/samsung/samsungActivity'
                });
            }
            else if (this.saCoupon == 5) {
                // 订单详情
                uni.reLaunch({
                    url: '/good_pages/discount_coupon/discount'
                });
            }
            else if (this.saCoupon == 2) {
                this.orderId = uni.getStorageSync("orderId");
                uni.removeStorage('orderId')
                // 订单详情
                uni.reLaunch({
                    url: "/good_pages/order/orderDetails?id=" + this.orderId,
                });
            }
            else {
                uni.switchTab({
                    url: '/src/components/index/index'
                });
            }
        },
        toggle() {
            if (this.loginIndex == 1) {
                this.loginIndex = 2;
                this.loginForm.code = "";
            } else {
                this.loginIndex = 1;
                this.loginForm.password = "";
            }
        },

        // 根据店铺编码获取店铺信息
        ShopStatus(id) {
            this.$api.get(this, api.getShopStatusByShopCode, {
                distributorId: this.distributorId,
                shopCode: id,
            })
                .then((res) => {
                    if (res.success) {
                        uni.setStorageSync("loginSan", 71)
                        this.name = res.data.shopName;
                        uni.setStorageSync("shopId", res.data.id);
                        uni.setStorageSync("shopCode", res.data.shopCode);
                        uni.setStorageSync("shopName", res.data.shopName);
                        uni.setStorageSync("qrUrl", res.data.qrUrl);
                        uni.setStorageSync("thirdQrUrl", res.data.thirdQrUrl);

                        if (Number(res.data.openFlag) != 1) {
                            this.name = res.data.shopName;
                            this.toback();
                        } else {
                            window["onSamLogin"] = (saInfo) => {
                                this.onSamLogin(saInfo);
                            };
                            window.SamsungAccount.login(false, "onSamLogin");
                        }
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000
                        });
                    }
                });
        },
    },

};
</script>
<style scoped lang="stylus">
$bg = #F6F6F6;
$white = #fff;
$darkBlue = #009ddb;
$lightBlue = #1275d1;
$input-bg = #F7F7F7;
$border = #ECECEC;
$blue = #0076A5;
$placeholder = #C2C2C2;
$gray = #5A5A5A;
$light-gray = #959595;

.login_code {
    z-index: 9999;
    font-size: 24rpx;
    color: #333;
    position: absolute;
    bottom: 240rpx;
    right: 40rpx;
    width: 200rpx;
    text-align: center;
}

.login-wrapper {
    position: fixed;
    width: 100%;
    top: 0;
    bottom: 0;
    background-color: $bg;
    overflow: hidden;

    .login-bg {
        position: absolute;
        left: -50%;
        width: 200%;
        color: $white;
        height: 520rpx;
        text-align: center;
        background: linear-gradient(0deg, $darkBlue, $lightBlue);
        border-radius: 0 0 50% 50%;

        .title {
            margin-top: 14rpx;
            font-size: 78rpx;
        }

        .intro {
            position: relative;
            margin-top: 10px;
            font-size: 32rpx;
            z-index: 2;
        }

        img {
            position: relative;
            top: -16rpx;
            width: 38%;
            z-index: 1;
        }
    }

    .main {
        position: relative;
        top: 400rpx;
        left: 30rpx;
        padding: 120rpx 50rpx 60rpx;
        width: calc(100% - 60rpx);
        background-color: $white;
        box-shadow: 0rpx 4rpx 42rpx 0rpx rgba(21, 189, 215, 0.18);
        border-radius: 32rpx;
        box-sizing: border-box;
        z-index: 2;

        .login_logo {
            position: absolute;
            top: -80rpx;
            left: 50%;
            transform: translateX(-50%);
        }

        .div-box {
            width: 100%;
            height: 100rpx;
            line-height: 100rpx;
            box-sizing: border-box;
            color: $gray;
            background-color: $input-bg;
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
                margin-top: 44rpx;
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

        .btn-normal {
            margin: 90rpx auto 0;
            width: 80%;
            height: 90rpx;
            line-height: 90rpx;
            text-align: center;
            font-size: 30rpx;
            color: $white;
            background: $blue;
            border-radius: 90rpx;
            border: none;
        }
    }

    .login-choose {
        position: relative;
        top: 400rpx;
        left: 60rpx;
        width: calc(100% - 60px);
        padding: 44rpx 0;
        background-color: rgba(255, 255, 255, 0.89);
        border-radius: 0 0 32rpx 32rpx;
        z-index: 1;

        .login-toggle {
            font-size: 28rpx;
            color: $light-gray;
            text-align: center;
        }
    }

    .protocol-wrapper {
        position: fixed;
        top: 0;
        bottom: 0;
        left: 0;
        right: 0;
        box-sizing: border-box;
        text-align: center;
        background-color: #F6F6F6;
        transform: translate3d(100%, 0, 0);
        animation: all 10s linear 1;
        z-index: 100;

        &.show {
            transform: translate3d(0, 0, 0);
        }
    }
}
</style>

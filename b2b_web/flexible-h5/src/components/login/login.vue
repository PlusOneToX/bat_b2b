<template>
  <div class="login-wrapper" v-show="showLogin">
    <c-header
      v-show="showLogin"
      :back="isBack"
      @back="toback"
      :darkIcon="true"
    ></c-header>
    <div class="login-bg">
      <h6 class="title">BAT</h6>
      <p class="intro">定制你的专属手机壳</p>
      <img src="../../assets/images/background/login_bg.png" alt="" />
    </div>
    <div class="main" v-show="showLogin">
      <span class="sprite-icon login_logo"></span>
      <div class="div-box">
        <van-field
          type="digit"
          maxlength="11"
          v-model="loginForm.phone"
          placeholder="请输入手机号"
          @blur="validatePhone"
          @touchstart.native.stop="keyboardShow = true"
          :left-icon="'sprite-icon login_icon_phone'"
          clearable
        >
        </van-field>
      </div>
      <div class="div-box code" v-if="loginIndex === 1">
        <van-field
          type="digit"
          v-model="loginForm.code"
          placeholder="请输入手机验证码"
          maxlength="6"
          :left-icon="'sprite-icon login_icon_verification'"
          clearable
        >
          <template #button v-if="loginIndex === 1">
            <van-button size="small" type="primary" @click="getCode">{{
              str
            }}</van-button>
          </template>
        </van-field>
      </div>
      <div class="div-box pwd" v-else>
        <van-field
          v-model="loginForm.password"
          :type="passwordShow ? 'text' : 'password'"
          placeholder="请输入密码"
          :right-icon="passwordShow ? 'eye-o' : 'closed-eye'"
          @click-right-icon="onClickIcon"
          :left-icon="'sprite-icon login_icon_password'"
          clearable
        />
      </div>
      <div class="btn btn-normal" @click="login">登录</div>
    </div>
    <div class="login-choose">
      <div class="login-toggle" v-if="loginIndex === 1" @click="toggle">
        {{ pwdValue }}
      </div>
      <div class="login-toggle" v-else @click="toggle">{{ codeValue }}</div>
    </div>
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import CHeader from "../../view/components/cHeader.vue";
import Protocol from "base/protocol/protocol";
import Loading from "base/loading/loading";
import md5 from "js-md5";
// api
import api from "common/js/allApi.js";
import {
  getXsaUid,
  getClientOsVersion,
  getClientModel,
  getPackageName,
  getPackageVersion,
} from "common/js/saApi";
import { getXmlNode,setLocalStorageItem,getLocalStorageItem } from "common/js/common";
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
      orderId: ""
    };
  },
  created() {
    this.samFlag = this.$route.query.samFlag;
    this.couponIds = this.$route.query.ids;
    this.comingFlag = this.$route.query.comingFlag;
    if(this.comingFlag === undefined || !this.comingFlag || this.comingFlag === null || this.comingFlag === "" || this.comingFlag === "null"){
      this.comingFlag = localStorage.getItem("comingFlag")
    }
    localStorage.removeItem('comingFlag')
    this.hasQualified = this.$route.query.hasQualified;
    // 从优惠券专题页跳转
    this.couponType = this.$route.query.couponType;
    this.appid = this.$route.query.appid || localStorage.getItem("appid");

    // 从专题活动页跳转
    let sa = this.$route.query.sa;
    if (sa) {
      localStorage.setItem("sa", sa);
    } else {
      localStorage.removeItem("sa");
    }

    // 定制信息（登录跳转定制页）
    this.pid = this.$route.query.pid;
    if(this.pid === undefined || !this.pid){
      this.pid = localStorage.getItem("picId")
    }
    localStorage.removeItem('picId')
    this.pUrl = this.$route.query.url;
    if(this.pUrl === undefined || !this.pUrl){
      this.pUrl = localStorage.getItem("pUrl")
    }
    localStorage.removeItem('pUrl')
    this.pType = this.$route.query.pType || this.$route.query.type;
    if(this.pType === undefined || !this.pType){
      this.pType = localStorage.getItem("pType")
    }
    localStorage.removeItem('pType')

    // 订单详情（登录跳转订单详情页面）
    this.orderId = this.$route.query.id
    if(this.orderId === undefined || !this.orderId){
      this.orderId = localStorage.getItem("orderId")
    }
    localStorage.removeItem('orderId')

    // 3-判断是否第三方登录
    let plat = this.$route.query.platform;
    let distr = this.$route.query.distributorId;
    let platform = localStorage.getItem("platform");
    let distributorId = localStorage.getItem("distributorId");
    if (plat && distr) {
      this.platform = plat;
      this.distributorId = distr;
      if (parseInt(plat) === 7) {
        // 用于新旧版本tabs显示
        localStorage.setItem("curVersion", "");
        if (sa !== "2") {
          localStorage.setItem("sa", "1");
        }
      } else {
        this.showLogin = true;
      }
    } else if (platform && distributorId) {
      this.platform = platform;
      this.distributorId = distributorId;
      if (parseInt(platform) === 7) {
        // 用于新旧版本tabs显示
        localStorage.setItem("curVersion", "");
        if (sa !== "2") {
          localStorage.setItem("sa", "1");
        }
      } else {
        this.showLogin = true;
      }
    } else {
      this.showLogin = true;
      this.platform = "6";
      this.distributorId = "2601";
    }
    localStorage.setItem("platform", this.platform);
    localStorage.setItem("distributorId", this.distributorId);

    if (this.platform && this.platform === "7") {
      let samShopId = this.$route.query.fcp_shopId;
      if (samShopId) {
        // 获取店铺状态
        this.ShopStatus(samShopId);
      } else {
        let isLowerVersion = sessionStorage.getItem("isLowerVersion") || "";
        if (!isLowerVersion) {
          window["onSamLogin"] = (saInfo) => {
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
      let samShopId = this.$route.query.fcp_shopId;
      if (obj.loginStatus === "login") {
        // 第三方帐号已登录
        let status = this.$route.query.status;
        let country = this.$route.query.country;
        let authToken = this.$route.query.authToken;
        // 1.判断是否授权
        let isLogin = "true";
        if (isLogin === "true") {
          // 2.判断是否已经登录，userNo 是否存在
          let userNo = getLocalStorageItem("userNo");
          let authUid = getLocalStorageItem("authenticateUserID");
          let otherUid = getLocalStorageItem("uid");
          if (userNo !== null && userNo !== undefined && userNo !== "") {
            this.showLogin = false;
            let userAgent = navigator.userAgent;
            let uid = getXsaUid(userAgent);
            if (uid === otherUid) {
              this.loginOpenId(authUid, otherUid);
            } else {
              // 不是同一个帐户登录
              this.clearData();
              let sa = localStorage.getItem("sa");
              if (
                this.samFlag === "samS20Flag" ||
                this.samFlag === "S21Coupon" ||
                this.samFlag === "samMemberCoupon" ||
                this.samFlag === "samNewCoupon" ||
                this.samFlag === "storeCoupon" ||
                this.samFlag === "christmas" ||
                this.samFlag === "samNote20Flag" ||
                this.samFlag === "samA52Flag"
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
              } else if (samShopId) {
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
                  samShopId +
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
              }
            }
          } else {
            this.message = "载入中";
            this.isLoading = true;
            // this.showLogin = true
            // 3. 判断是否有authenticateUserID
            if (authUid && otherUid) {
              // 3.1用户通过第三方openId登录
              // 判断是否设置了密码
              let isflag = localStorage.getItem("isflag");
              if (isflag === 1 || isflag === "1") {
                localStorage.removeItem("isflag");
              } else {
                this.loginOpenId(authUid, otherUid);
              }
            } else {
              // 3.2 判断是否有授权回调信息
              if (authToken && status && country) {
                let userAgent = navigator.userAgent;
                let uid = getXsaUid(userAgent);
                let API_URL;
                setLocalStorageItem("uid", uid);
                if (status === "signInSuccess") {
                  if (country === "CN") {
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
                let sa = localStorage.getItem("sa");
                if (sa) {
                  if (
                    this.samFlag === "samS20Flag" ||
                    this.samFlag === "S21Coupon" ||
                    this.samFlag === "samMemberCoupon" ||
                    this.samFlag === "samNewCoupon" ||
                    this.samFlag === "storeCoupon" ||
                    this.samFlag === "christmas" ||
                    this.samFlag === "samNote20Flag" ||
                    this.samFlag === "samA52Flag"
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
                      (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                      (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                    (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
                } else if (samShopId) {
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
                      samShopId +
                      (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                      (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
      localStorage.removeItem("userId");
      localStorage.removeItem("userNo");
      localStorage.removeItem("phone");
      localStorage.removeItem("goods");
      localStorage.removeItem("uid");
      localStorage.removeItem("auth");
      localStorage.removeItem("authenticateUserID");
    },
    onInput(key) {
      this.loginForm.phone = this.loginForm.phone + "" + key;
    },
    login() {
      let samShopId = this.$route.query.fcp_shopId;
      if (this.validateForm()) {
        if (parseInt(this.platform) === 7) {
          // 判断是否有authenticateUserID
          let auth = getLocalStorageItem("authenticateUserID");
          let uid = getLocalStorageItem("uid");
          if (auth && uid) {
            this.loginType();
          } else {
            // 重新获取authenticateUserID
            let phone = this.loginForm.phone;
            let code;
            if (this.loginIndex === 1) {
              code = this.loginForm.code;
            } else {
              code = this.loginForm.password;
            }
            let sa = localStorage.getItem("sa");
            if (sa) {
              if (
                this.samFlag === "samS20Flag" ||
                this.samFlag === "S21Coupon" ||
                this.samFlag === "samMemberCoupon" ||
                this.samFlag === "samNewCoupon" ||
                this.samFlag === "storeCoupon" ||
                this.samFlag === "christmas" ||
                this.samFlag === "samNote20Flag" ||
                this.samFlag === "samA52Flag"
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                    (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null ? "&comingFlag=" + this.comingFlag : "");
              } else if (samShopId) {
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
                samShopId +
                (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
                  (this.comingFlag !== undefined && this.comingFlag !== "" && this.comingFlag !== null && this.comingFlag !== "null" ? "&comingFlag=" + this.comingFlag : "");
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
      let otherId = getLocalStorageItem("uid");
      let openId = getLocalStorageItem("authenticateUserID");
      if (this.loginIndex === 1) {
        if (this.platform && this.platform === "7") {
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
            this.$toast.fail(res.errMessage);
          }
        });
      } else {
        if (this.platform && this.platform === "7") {
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
            this.$toast.fail(res.errMessage);
          }
        });
      }
    },
    // 存储用户信息
    validateLogin(data) {
      this.message = "";
      this.isLoading = false;
      setLocalStorageItem("userId", data.id);
      setLocalStorageItem("userNo", data.no);
      setLocalStorageItem("phone", data.phone);

      this.pushTo();
    },
    pushTo() {
	    console.log("到了这里没有");
      if (this.pid) {
        // 跳转定制页
        this.$router.push({
          path: "/phone",
          query: {
            pid: this.pid,
            url: this.pUrl,
            type: this.pType,
            enterFlag: "login",
          },
        });
      } else if (this.orderId) {
        // 跳转订单详情页
        this.$router.push({
          path: "/orderDetail",
          query: {
            id: this.orderId,
            enterFlag: "login",
          },
        });
      } else {
        if (this.samFlag === "samS20Flag") {
          if (this.hasQualified === "true") {
            if (this.couponIds) {
              this.getCoupon(this.couponIds);
            }
          } else {
            this.$router.replace("/samsungS20Coupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "S21Coupon") {
          if (this.hasQualified === "true") {
            if (this.couponIds) {
              this.getCoupon(this.couponIds);
            }
          } else {
            this.$router.replace("/samsungS21Coupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "samMemberCoupon") {
          if (this.hasQualified === "true") {
            if (this.couponIds) {
              this.getCoupon(this.couponIds);
            }
          } else {
            this.$router.replace("/samMemberCoupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "samNewCoupon") {
          if (this.hasQualified === "true") {
            if (this.couponIds) {
              this.getCoupon(this.couponIds);
            }
          } else {
            this.$router.replace("/samNewCoupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag === "activityCoupon") {
          if (this.couponIds) {
            this.getCoupon(this.couponIds);
          } else {
			  this.$router.replace({
			  	path: "/recommend",
			  	query: {
			  	  distributorId: this.distributorId,
			  	  platform: this.platform,
			  	  appid: this.appid,
			  	}
			  });
          }
        } else if (this.samFlag === "samA52Flag") {
          if (this.hasQualified === "true" && this.couponIds) {
            this.getCoupon(this.couponIds);
          } else {
            this.$router.replace("/samsungA52Coupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "christmas") {
          if (this.hasQualified === "true" && this.couponIds) {
            this.getCoupon(this.couponIds);
          } else {
            this.$router.replace("/christmas?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "samNote20Flag") {
          if (this.hasQualified === "true" && this.couponIds) {
            this.getCoupon(this.couponIds);
          } else {
            this.$router.replace("/samsungcoupon?samFlag=" + this.samFlag);
            this.$router.go(-1);
          }
        } else if (this.samFlag === "storeCoupon") {
          if (this.hasQualified === "true" && this.couponIds) {
            this.getCoupon(this.couponIds);
          } else {
            let samShopId = this.$route.query.fcp_shopId;
            if (samShopId) {
              this.$router.replace(
                "/storeCoupon?samFlag=" +
                  this.samFlag +
                  "&fcp_shopId=" +
                  samShopId
              );
            } else {
              this.$router.replace("/storeCoupon?samFlag=" + this.samFlag);
            }
            this.$router.go(-1);
          }
        } else {
          if (this.firstEnter) {
            // 登录成功 - 跳转首页
            if (this.platform === 7) {
              if(this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag){
                this.$router.replace({
                  path: "/" + this.comingFlag,
                  query: {
                  samFlag: "samFlag",
                  },
                });
              }else{
                this.$router.replace({
                  path: "/recommend",
                  query: {
                  samFlag: "samFlag",
                  },
                });
              }
            } else {
              this.$router.replace("/index");
            }
          } else {
            if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag) {
              // 预览进入 携带参数
              var enterParams = this.$route.query.enterParams;
              if (this.comingFlag !== undefined && this.comingFlag === "preview") {
                let params = JSON.parse(enterParams);
                params.materialName = decodeURI(params.material);
                params.modelName = decodeURI(params.model);
                params.brandName = decodeURI(params.brandName);
                params.pictureName = decodeURI(params.picture);

                let diyCmd = {
                  categoryId: params.categoryId,
                  categoryName: params.categoryName,
                  brandId: params.brandId,
                  brandName: params.brandName,
                  generateImage: params.generateImage,
                  materialId: params.materialId,
                  materialName: params.materialName,
                  modelId: params.modelId,
                  modelName: params.modelName,
                  pictureId: params.pictureId,
                  pictureName: this.pictureName,
                  previewImage: this.previewImage,
                  manufactors: params.manufactor,
                };

                let comingType = this.$route.query.comingType;
                if (comingType === "buyNow") {
                  // 立即购买
                  this.$api
                    .post(this, api.addToShopcart, {
                      diy: diyCmd,
                      itemCode: params.itemCode,
                      itemCount: params.itemCount,
                      itemType: params.itemType,
                      salePrice: params.salePrice, // 价格
                    })
                    .then((res) => {
                      if (res.success) {
                        let goodsArr = [];
                        goodsArr.push(params);
                        localStorage.setItem("goods", JSON.stringify(goodsArr));

                        this.$router.push("/order");
                      } else {
                        this.$toast(res.error);
                      }
                    });
                } else {
                  // 加入购物车
                  this.$api
                    .post(this, api.addToShopcart, {
                      diy: diyCmd,
                      itemCode: params.itemCode,
                      itemCount: params.itemCount,
                      itemType: params.itemType,
                      salePrice: params.salePrice, // 价格
                    })
                    .then((res) => {
                      if (res.success) {
                        this.message = "";
                        this.loading = false;
                        let pid = res.data.id;
                        // 跳转购物车
                        this.$router.replace({
                          path: "/shopcart",
                          query: {
                            pid: pid,
                          },
                        });
                      } else {
                        this.$toast(res.error);
                      }
                    });
                }
              } else {
                // 其他页面进入
                console.log("这里?");
                this.$router.replace("/" + this.comingFlag);
              }
            } else {
              console.log("还是这里?");
              if(localStorage.getItem("is_out_detail") === "yes"){
                // 跳转订单详情页
                this.$router.push({
                  path: "/orderDetail",
                  query: { id: localStorage.getItem("id_out_detail")},
                });
              }else{
                this.$router.replace("/index");
              }
            }
          }
        }
      }
    },
    validateForm() {
      if (this.validatePhone()) {
        if (this.loginIndex === 1) {
          // 验证码登录
          if (this.loginForm.code === "") {
            this.$toast("请输入手机验证码");
            return false;
          }
        } else {
          // 密码登录
          if (this.loginForm.password === "") {
            this.$toast("请输入密码");
            return false;
          }
        }
        return true;
      }
    },
    validatePhone() {
      let reg = /^1[3456789]\d{9}$/;
      if (this.loginForm.phone === "") {
        this.$toast("请输入手机号");
        return false;
      } else {
        if (!reg.test(this.loginForm.phone)) {
          this.$toast("请输入正确的手机号码");
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
            this.$toast("验证码已发送");
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
            this.$toast.fail(res.errMessage);
          }
        });
      }
    },
    onClickIcon() {
      this.passwordShow = !this.passwordShow;
    },
    loginOpenId(authUid, otherUid) {
      localStorage.removeItem("auth");
      // 用户通过第三方openId登录
      this.$api
        .post(this, api.thirdpartyLogin, {
          openId: authUid,
          otherUid: otherUid,
        })
        .then((res) => {
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
      this.$api
        .post(this, api.isAuthToken, {
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
            if (status && status[0] === "SUCCESS") {
              let authUid = getXmlNode(res.data, "authenticateUserID");
              if (authUid) {
                let userAgent = navigator.userAgent;
                let uid = getXsaUid(userAgent);
                setLocalStorageItem("uid", uid);
                setLocalStorageItem("authenticateUserID", authUid[0]);
                this.loginOpenId(authUid[0], uid);
              }
            } else {
              this.$toast.fail(res.errMessage);
            }
          } else {
            this.$toast.fail(res.errMessage);
            window.location.href = "action://samsung.account.login.fail";
          }
        })
        .catch((error) => {
          this.$toast(error);
        });
    },
    toback() {
      if (this.saCoupon === 1) {
        // Note20 专题页
        this.$router.push("/samsungcoupon");
      } else if (this.saCoupon === 2) {
        // S20 专题页
        this.$router.push("/samsungS20Coupon");
      } else if (this.saCoupon === 3) {
        // S21 专题页
        this.$router.push("/samsungS21Coupon");
      } else if (this.saCoupon === 4) {
        // A52 专题页
        this.$router.push("/samsungA52Coupon");
      } else if (this.saCoupon === 5) {
        // A53 免费券
        this.$router.push("/samMemberCoupon");
      } else if (this.saCoupon === 6) {
        // A53 折扣券
        this.$router.push("/samNewCoupon");
      } else {
        this.$router.go(-1);
      }
    },
    toggle() {
      if (this.loginIndex === 1) {
        this.loginIndex = 2;
        this.loginForm.code = "";
      } else {
        this.loginIndex = 1;
        this.loginForm.password = "";
      }
    },
    // 领取优惠券
    getCoupon(ids) {
      let couponIds = ids.split(",");
      this.$api
        .post(this, api.receiveCoupon, { ids: couponIds })
        .then((res) => {
          if (res.success) {
            // 领取成功 - 跳转首页
            this.$toast.success("领取成功！");
            if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag === "activityCoupon") {
              this.$router.replace({
                path: "/recommend",
                query: {
                  distributorId: this.distributorId,
                  platform: this.platform,
                  appid: this.appid,
                },
              });
            } else {
              this.$router.push({
                path: "/recommend",
                query: {
                  samFlag: this.samFlag,
                  hasRecieved: "recieved",
                },
              });
            }
          } else {
            this.$toast(res.errMessage);
            if (this.comingFlag !== undefined && this.comingFlag !== null && this.comingFlag !== "null" && this.comingFlag !== "" && this.comingFlag === "activityCoupon") {
              this.$router.push({
                path: "/" + this.comingFlag,
                query: {
                  couponType: this.couponType,
                  distributorId: this.distributorId,
                  platform: this.platform,
                  appid: this.appid,
                },
              });
            } else {
              let path;
              if (this.samFlag === "samS20Flag") {
                path = "/samsungS20Coupon";
              } else if (this.samFlag === "S21Coupon") {
                path = "/samsungS21Coupon";
              } else if (this.samFlag === "samMemberCoupon") {
                path = "/samMemberCoupon";
              } else if (this.samFlag === "samNewCoupon") {
                path = "/samNewCoupon";
              } else if (this.samFlag === "samA52Flag") {
                path = "/samsungA52Coupon";
              } else if (this.samFlag === "christmas") {
                path = "/christmas";
              } else if (this.samFlag === "storeCoupon") {
                path = "/storeCoupon";
              } else if (this.samFlag === "samNote20Flag") {
                path = "/samsungcoupon";
              }
              this.$router.push({
                path: path,
              });
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 根据店铺编码获取店铺信息
    ShopStatus(shopCode) {
      this.$api
        .get(this, api.getShopStatusByShopCode, {
          distributorId: this.distributorId,
          shopCode: shopCode,
        })
        .then((res) => {
          if (res.success) {
            this.name = res.data.shopName;
            sessionStorage.setItem("samShopId", res.data.id);
            sessionStorage.setItem("samShopCode", res.data.shopCode);
            sessionStorage.setItem("samShopName", res.data.shopName);
            if (Number(res.data.openFlag) !== 1) {
              this.name = res.data.shopName;
              this.$router.push({ name: "error", query: { name: this.name } });
            } else {
              window["onSamLogin"] = (saInfo) => {
                this.onSamLogin(saInfo);
              };
              window.SamsungAccount.login(false, "onSamLogin");
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
  },
  beforeRouteEnter(to, from, next) {
    if (from.name === "samsungcoupon") {
      // Note20 专题页
      next((vm) => {
        vm.saCoupon = 1;
        vm.firstEnter = true;
      });
    } else if (from.name === "samsungS20Coupon") {
      // S20 专题页
      next((vm) => {
        vm.saCoupon = 2;
        vm.firstEnter = true;
      });
    } else if (from.name === "samsungS21Coupon") {
      // S21 专题页
      next((vm) => {
        vm.saCoupon = 3;
        vm.firstEnter = true;
      });
    } else if (from.name === "samsungA52Coupon") {
      // 52 专题页
      next((vm) => {
        vm.saCoupon = 4;
        vm.firstEnter = true;
      });
    } else if (from.name === "samMemberCoupon") {
      // A53 免费券
      next((vm) => {
        vm.saCoupon = 5;
        vm.firstEnter = true;
      });
    } else if (from.name === "samNewCoupon") {
      // A53 折扣券
      next((vm) => {
        vm.saCoupon = 6;
        vm.firstEnter = true;
      });
    } else if (from.name === "index") {
      let platform = localStorage.getItem("platform");
      let userNo = getLocalStorageItem("userNo");
      if (platform && platform === "7" && userNo) {
        window.location.href = "action://back";
      } else {
        next();
      }
    } else {
      next((vm) => {
        vm.firstEnter = false;
      });
    }
  },
  components: {
    Protocol,
    Loading,
    CHeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';

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
    height: 260px;
    text-align: center;
    background: linear-gradient(0deg, $darkBlue, $lightBlue);
    border-radius: 0 0 50% 50%;

    .title {
      margin-top: 70px;
      font-size: 39px;
    }

    .intro {
      position: relative;
      margin-top: 10px;
      font-size: 16px;
      z-index: 2;
    }

    img {
      position: relative;
      top: -8px;
      width: 38%;
      z-index: 1;
    }
  }

  .main {
    position: relative;
    top: 200px;
    left: 15px;
    padding: 60px 25px 30px;
    width: calc(100% - 30px);
    background-color: $white;
    box-shadow: 0px 2px 21px 0px rgba(21, 189, 215, 0.18);
    border-radius: 16px;
    box-sizing: border-box;
    z-index: 2;

    .login_logo {
      position: absolute;
      top: -40px;
      left: 50%;
      transform: translateX(-50%);
    }

    .div-box {
      width: 100%;
      height: 50px;
      line-height: 50px;
      box-sizing: border-box;
      color: $gray;
      background-color: $input-bg;
      border-radius: 45px;

      >>>.van-cell {
        padding: 0 20px;
        background-color: $input-bg;
        border-radius: 45px;

        .van-button--primary {
          display: inline-block;
          height: 28px;
          padding: 0 15px;
          background-color: $input-bg;
          border: none;
          border-left: 1px solid $border;
          color: $blue;
        }

        .van-icon-sprite-icon {
          top: 14px;
          margin-right: 2px;
        }
      }

      >>>.van-field__control {
        margin: 10px 0;
        width: 100%;
        height: 30px;
        line-height: 30px;
        box-sizing: border-box;
        text-align: left;
        font-size: 14px;
        border: none;
      }

      input {
        display: inline-block;
        width: 100%;
        height: 100%;
        box-sizing: border-box;
        text-align: left;
        border: none;
        background: none;
        outline: none;
        -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
      }

      &.pwd {
        margin-top: 22px;
      }

      &.code {
        display: flex;
        margin-top: 22px;
        flex-direction: row;
        flex-wrap: nowrap;
        justify-items: center;
        align-items: center;

        input {
          flex: 1;
        }

        .text {
          display: inline-block;
          width: 100px;
          height: 24px;
          line-height: 24px;
          margin-left: 15px;
          font-size: 14px;
        }
      }
    }

    .btn-normal {
      margin: 45px auto 0;
      width: 80%;
      height: 45px;
      line-height: 45px;
      text-align: center;
      font-size: 15px;
      color: $white;
      background: $blue;
      border-radius: 45px;
      border: none;
    }
  }

  .login-choose {
    position: relative;
    top: 200px;
    left: 30px;
    width: calc(100% - 60px);
    padding: 22px 0;
    background-color: rgba(255, 255, 255, 0.89);
    border-radius: 0 0 16px 16px;
    z-index: 1;

    .login-toggle {
      font-size: 14px;
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

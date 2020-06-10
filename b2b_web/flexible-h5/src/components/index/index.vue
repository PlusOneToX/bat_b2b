<template>
  <div class="index" :class="{ 'ry-style': Number(distributorId) === 4378 }" v-if="showContent">
    <!-- BATt.com -->
    <div class="container">
      <div class="logo-box">
        <div class="logo-img" :class="getLogo()"></div>
        <div class="text-box" :class="getLogo()">
          <div class="text-img" :class="getLogo()"></div>
        </div>
        <div class="remark" :class="getLogo()">
          打开脑洞，创作独一无二的作品
        </div>
        <!-- 公告 -->
        <div class="notice-container" v-if="showNotice">
          <Notice></Notice>
        </div>
      </div>
      <div class="main-container">
        <div class="phone">
          <div class="phone-img"></div>
        </div>
        <div class="btn-box" v-show="Number(distributorId) !== 4378">
          <div class="btn btn-normal-blue" @click="open">
            <van-icon class="icon icon-start" color="#ffffff" />开始定制
          </div>
        </div>
      </div>
    </div>
	
	<!-- 柔性关闭弹窗 -->
	<div class="flexible-dialog" v-show="showFlexible">
	  <div class="flexible-dialog-content">
	      <div class="flexible-cotent-top">
	      	<img class="flexible-cotent-top-img" src="../../assets/images/flexible-logo.png" mode="widthFix"/>
	      </div>
		  
		  <div class="flexible-cotent-middle-title">
		  	 通知提醒
		  </div>
		  <div class="flexible-cotent-middle-info">
		  	 尊敬的客户，本商城将于6月30日停止运营，仅保留订单查询服务。感谢您的信任和支持。
		  </div>
		  <div class="flexible-cotent-bottom" @click="clickFlexible">
		  	 知道了
		  </div>
	  </div>
	</div>
	
	
    <!-- 底部tab -->
    <tabs v-show="showTabbar" :curTab="'index'" :userNo="userNo"></tabs>
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import { formatDate,setLocalStorageItem,getLocalStorageItem } from "common/js/common";
import VHeader from "components/v-header/v-header";
import Tabs from "../../view/components/tabs.vue";
import api from "common/js/allApi.js";
import Loading from "base/loading/loading";
import Notice from "base/notice/notice";
export default {
  name: "index",
  data() {
    return {
      name: "", // 标题名字
      title: "",
      platform: "",
      distributorId: "",
      picData: [],
      isLoading: false,
      message: "", // loading提示
      userId: "",
      userNo: "",
      pageFlag: "", // 页面标识
      shopId: "",
      showNotice: true,
      showTabbar: true, // 是否显示底部tab
      showContent: false, // 是否显示页面内容
	  
	    showFlexible: false//是否显示柔性关闭弹窗
	  
    };
  },
  created() {
    // 公告通知
    let d2 = "2019-06-06 00:00:00";
    let now = new Date();
    let endTime = new Date(Date.parse(d2.replace(/-/g, "/")));
    if (now >= endTime) {
      this.showNotice = false;
    }
    // 获取shopId
    this.shopId = this.$route.query.shopId;
    if (this.shopId) {
      // 获取店铺状态
      this.ShopStatus(this.shopId);
    }

    let enterParams = window.location.href.split("?")[1];
    if (enterParams) {
      sessionStorage.setItem("enterParams", enterParams);
    }
	
  },
  mounted() {
    // 用于新旧版本tabs显示
    localStorage.setItem("curVersion", "");

    this.userNo = getLocalStorageItem("userNo");
    this.userId = getLocalStorageItem("userId");

    this.pageFlag = this.$route.query.pageFlag;
    if (!this.pageFlag) {
      // 判断进入是否带有页面标识，没有正常显示页面内容
      this.showContent = true;
    }
    // 判断是否从第三方进入,获取平台和分销商ID
    let plat = this.$route.query.platform;
    let distrId = this.$route.query.distributorId;
    let orderSource = this.$route.query.orderSource;
    let platform = localStorage.getItem("platform");
    let distributorId = localStorage.getItem("distributorId");
	
    if (distrId && (plat || orderSource)) {
      this.platform = plat || orderSource;
      this.distributorId = distrId;
    } else if (platform && distributorId) {
      this.platform = platform;
      this.distributorId = distributorId;
    } else {
      this.platform = "6";
      this.distributorId = "2601";
    }
    localStorage.setItem("platform", this.platform);
    localStorage.setItem("distributorId", this.distributorId);
    if (orderSource) {
      localStorage.setItem("orderSource", orderSource);
    } else {
      localStorage.setItem("orderSource", this.platform);
    }

    // 荣耀
    if (Number(this.distributorId) === 4378) {
      this.showTabbar = false;
      let thirdUserId = this.$route.query.userId;
      let flowId = this.$route.query.flowId;
      sessionStorage.setItem("ryFlowId", flowId);

      if (thirdUserId) {
        this.loginOpenId(thirdUserId, flowId);
      }
      return;
    }

    if (this.platform && this.platform === "7") {
      this.$router.push({ name: "recommend" });
    } else if (this.platform === "1" || this.shopId) {
      // 微信公众号登录授权
      if (this.$route.query.appid && !sessionStorage.getItem("hasAuth")) {
        this.weixinLogin();
      } else {
        let openId = localStorage.getItem("openId");
        this.loginOpenId(openId);
      }
    }
	this.showFlexible = false;
  },
  methods: {
    loginOpenId(thirdUserId, flowId) {
      localStorage.removeItem("auth");
      // 用户通过第三方openId登录
      this.$api
        .post(this, api.thirdpartyLogin, {
          openId: thirdUserId,
        })
        .then((res) => {
          if (res.success) {
            setLocalStorageItem("userId", res.data.id);
            setLocalStorageItem("userNo", res.data.no);
            setLocalStorageItem("phone", res.data.phone);

            // 根据第三方兑换码获取订单信息
            if (flowId) {
              this.getCodeStatus(flowId);
            }
            
            if (this.pageFlag === "orderQuery") {
              // 判断页面标识，订单查询跳转订单列表
              this.$router.replace({
                path: "/orderList",
                query: {
                  pageFlag: this.pageFlag
                }
              })
            }
          } else {
            this.message = "";
            this.isLoading = false;
            this.showLogin = true;
          }
        });
    },
    // 根据第三方兑换码获取订单信息
    getCodeStatus(flowId) {
      this.$api
        .get(this, api.getCodeStatus, {
          code: flowId,
        })
        .then((res) => {
          if (res.success) {
            // 获取订单id
            if (res.data) {
              let orderId = res.data.orderId;
              if (orderId) {
                // 跳转订单详情
                this.$router.replace({
                  path: "/orderDetail",
                  query: { id: orderId },
                });
              } else {
                // 跳转定制
                this.$router.replace({
                  path: "/phone",
                  query: {
                    materialNo: this.$route.query.materialsNo,
                  },
                });
              }
            } else {
              // 跳转定制
              this.$router.replace({
                path: "/phone",
                query: {
                  materialNo: this.$route.query.materialsNo,
                },
              });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },

    // 获取店铺状态
    ShopStatus(id) {
      this.$api
        .get(this, api.getShopStatus, {
          id: id,
        })
        .then((res) => {
          if (res.success) {
            this.name = res.data.shopName;
            sessionStorage.setItem("shopId", this.shopId);
            sessionStorage.setItem("shopCode", res.data.shopCode);
            sessionStorage.setItem("shopName", res.data.shopName);
            if (Number(res.data.openFlag) !== 1) {
              this.name = res.data.shopName;
              this.$router.push({ name: "error", query: { name: this.name } });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 根据分销商显示logo
    getLogo() {
      let title, rname;
      if (this.distributorId === "1457") {
        title = "标题名字";
        rname = "title";
      } else {
        // bat.com
        title = "BAT商城";
        rname = "bat";
      }
      document.title = sessionStorage.getItem("shopName") ? sessionStorage.getItem("shopName") : title;
      return rname;
    },
    // 选择图片
    handlePic(item) {
      this.$router.push({
        path: "/phone",
        query: { pid: item.id, url: item.originImage, type: item.type },
      });
    },
    // 微信授权获取openId、accessToken
    weixinLogin() {
      let APPID;
      if (this.$route.query.appid) {
        // 判断缓存ID是否与
        if (
          localStorage.getItem("appid") &&
          localStorage.getItem("appid") === this.$route.query.appid
        ) {
          APPID = localStorage.getItem("appid");
        } else {
          APPID = this.$route.query.appid;
          localStorage.setItem("appid", APPID);
        }
      } else {
        APPID = localStorage.getItem("appid");
      }
      let code = this.getUrlCode() ? this.getUrlCode().code : "";
      if (!code) {
        let scope = "snsapi_base";
        let url = window.location.href;
        // 未授权，需要重新授权获取code
        let authUrl =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
          APPID +
          "&redirect_uri=" +
          encodeURIComponent(url) +
          "&response_type=code&scope=" +
          scope +
          "&state=STATE&connect_redirect=1#wechat_redirect";
        window.location.replace(authUrl);
      } else {
        // 通过code换取网页授权access_token
        this.$api
          .post(this, api.wxLogin, {
            appId: APPID,
            code: code,
          })
          .then((res) => {
            if (res.success) {
              // 用户唯一标识
              if (res.data) {
                localStorage.setItem("openId", res.data.openId);
                setLocalStorageItem("userId", res.data.id);
                setLocalStorageItem("phone", res.data.phone);
                setLocalStorageItem("userNo", res.data.no);
                this.userNo = res.data.no;
                sessionStorage.setItem("hasAuth", true);

                if (this.pageFlag === "orderQuery") {
                  // 判断页面标识，订单查询跳转订单列表
                  this.$router.replace({
                    path: "/orderList",
                    query: {
                      pageFlag: this.pageFlag
                    }
                  })
                }
              }
            } else {
              this.$toast(res.errMessage);
            }
          })
          .catch((error) => {
            console.log(error);
          });
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
    open() {
	  //如果是仁青则直接关闭
	   this.$router.push({ path: "/phone", query: { key: "-1" } });
    },
    closeDialog() {
      this.show = false;
    },
    // 判断到期日期是否为当天
    isToday(date) {
      if (new Date(date).toDateString() === new Date().toDateString()) {
        return true;
      } else {
        return false;
      }
    },
    closeDialog() {
      this.show = false;
    },
	
	//点击知道了隐藏柔性关闭弹窗
	clickFlexible(){
	  this.showFlexible = false;
	},
  },
  filters: {
    formatDate(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy.MM.dd");
    },
    formatTime(time) {
      let date = new Date(time);
      return formatDate(date, "hh:mm:ss");
    },
  },
  components: {
    VHeader,
    Tabs,
    Loading,
    Notice,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '../../common/styles/mixin.styl';
@import '../../common/styles/variable.styl';

.index {
  position: fixed;
  flex-direction: column;
  width: 100%;
  top: 0;
  bottom: 50px;
  background-color: $color-background-white;

  .container {
    height: 100%;

    .logo-box {
      padding-top: calc(100vh * 0.03);
      text-align: center;

      .logo-img {
        display: block;
        margin: 0 auto;
        width: 124px;
        height: 20px;
        bg-image('logo');
        background-size: 124px 20px;

        &.zhuanz {
          width: 144px;
          height: 34px;
          bg-image('logozlj');
          background-size: 144px 34px;
        }

        &.linzhi {
          width: 160px;
          height: 43px;
          bg-image('logolinzhi');
          background-size: 160px 43px;
        }

        &.jydd {
          width: 144px;
          height: 47px;
          background-image: url('../../common/images/logojydd.png');
          background-repeat: no-repeat;
          background-position: center;
          background-size: 144px 47px;
        }

        &.jinke {
          padding-top: 15px;
          display: none;
        }

        &.kuguofen {
          display: none;
        }

        &.lnhl {
          display: none;
        }
      }

      .text-box {
        display: inline-block;
        width: 184px;
        height: 24px;
        border-bottom: 10px solid #1EB6CC;

        &.zhuanz {
          border-color: #FF5859;
        }

        &.linzhi {
          border-color: #088F00;
        }

        &.jydd {
          border-color: #008fd1;
        }

        &.kuguofen {
          width: 209px;
          height: 56px;
          border-color: #0066CC;
        }

        .text-img {
          position: relative;
          display: block;
          margin: 8px auto;
          width: 150px;
          height: 24px;
          background: url('../../common/images/stylecharacter.png') no-repeat center;
          background-size: 150px 24px;

          &.kuguofen {
            width: 209px;
            height: 52px;
            background: url('../../common/images/kuguofen@2x.png') no-repeat center;
            background-size: 209px 52px;
          }
        }
      }

      .remark {
        margin-top: 10px;
        font-size: 12px;
        color: #4A4A4A;

        &.kuguofen {
          display: none;
        }
      }

      .notice-container {
        margin: 5px 40px 0;
      }
    }

    .main-container {
      position: absolute;
      top: 58%;
      width: 100%;
      transform: translate3d(0, -50%, 0);
      text-align: center;

      .phone {
        display: inline-block;
        width: 100%;
        height: 100%;
        background-size: 100% auto;
        // background: url('../../common/images/bg.png') no-repeat center;

        .phone-img {
          display: inline-block;
          width: 150px;
          height: 293px;
          background: url('../../common/images/phone.gif') no-repeat center;
          background-size: 150px 293px;
        }
      }
    }

    .btn-box {
      padding: 27px 30px 0;
      box-sizing: border-box;

      .btn {
        display: inline-block;
        width: 180px;
        height: 40px;
        line-height: 40px;
        font-size: 14px;
        text-align: center;
        color: #ffffff;
        border-radius: 70px;

        .icon {
          display: inline-block;
          margin-right: 12px;
          font-size: 20px;
          vertical-align: middle;
        }
      }

      .btn-normal-blue {
        background: #0076A5;
      }

      .btn-normal-red {
        background: linear-gradient(90deg, rgba(245, 95, 109, 1) 0%, rgba(217, 9, 40, 1) 100%);
      }
    }

    .msg-dialog {
      display: inline-block;
      width: 90%;
      padding: 18px 20px;
      text-align: center;
      box-sizing: border-box;
      border-radius: 8px;

      .title {
        display: inline-block;
        font-size: 18px;
        font-weight: bold;
        color: #4A4A4A;
        font-size: 18px;
        text-align: center;

        .icon {
          display: inline-block;
          width: 20px;
          height: 20px;
          bg-image('remind');
          background-size: 20px 20px;
        }
      }

      .info {
        display: inline-block;
        margin: 16px 0 20px;

        .text {
          display: inline-block;
          color: #000000;
          font-size: 14px;
          line-height: 1.5;
          text-align: justify;
        }
      }

      >>>.van-dialog__confirm {
        color: #0076A5;
      }
    }

    .title {
      text-align: center;
      font-size: $font-size-large;
      padding: 20px 0 10px;
    }

    .pics-ul {
      height: 100%;
      display: flex;
      display: -webkit-flex;
      flex-direction: row;
      flex-wrap: wrap;
      padding: 20px 15px 40px;
      box-sizing: border-box;
      overflow: scroll;
      -webkit-overflow-scrolling: touch;

      &::-webkit-scrollbar {
        display: none;
      }

      .pic-li {
        display: block;
        flex: 0 0 33.33%;
        text-align: center;
        margin-bottom: 10px;

        .pic-img {
          position: relative;
          width: 110px;
          height: 150px;
          margin: 0 auto;
          overflow: hidden;
          border-radius: 8px;

          .img {
            position: absolute;
            left: 50%;
            top: 50%;
            width: 100%;
            height: auto;
            transform: translate3d(-50%, -50%, 0);
          }
        }

        .name {
          display: inline-block;
          width: 100%;
          margin: 12px 0 20px;
          text-align: center;
          font-size: $font-size-medium;
          color: $color-text;
        }
      }
    }
  }
}

.coupon-dialog {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;

  .coupon-dialog-content {
    position: absolute;
    top: 45%;
    left: 11%;
    width: 78%;
    bg-image('samsung/coupon-bg-b');
    background-size: 100% 100%;

    &::before {
      content: '';
      position: absolute;
      top: -195px;
      left: 0;
      width: 100%;
      height: 195px;
      bg-image('samsung/coupon-bg-t');
      background-size: 100% 100%;
    }

    .van-icon {
      position: absolute;
      top: -200px;
      right: 0;
      font-size: 30px;
      color: $color-text-w;
    }

    .btn-wrap {
      position: absolute;
      left: -4%;
      bottom: -20px;
      width: 108%;
      height: 150px;
      bg-image('samsung/coupon-bg-btn');
      background-size: 100% 100%;

      &.recieve-btn {
        bg-image('samsung/coupon-bg-recieve');
        background-size: 100% 100%;
      }
    }
  }

  .coupon-list {
    position: relative;
    top: -95px;
    padding: 0 15px;
  }

  .coupon-li {
    position: relative;
    display: flex;
    margin-bottom: 10px;
    border-radius: 6px;
    overflow: hidden;
    color: #FF560B;
    background-image: radial-gradient(circle at 8px 8px, transparent 0%, transparent 8px, #ffffff 8px, #ffffff 100%);
    background-position: 71px -8px;
    box-shadow: 0px 2px 4px 0px rgba(0, 0, 0, 0.5);

    .coupon-l {
      position: relative;
      display: inline-block;
      width: 80px;
      padding: 16px 0;
      box-sizing: border-box;
      text-align: center;

      &::before {
        content: '';
        width: 3px;
        background: linear-gradient(to bottom, #FF0540 0%, #FF0540 40%, transparent 40%);
        background-size: 1px 6px;
        background-repeat: repeat-y;
        position: absolute;
        right: -2px;
        top: 6px;
        bottom: 6px;
        margin: auto;
      }

      .sale-price {
        font-size: $font-size-small;

        em {
          font-size: 32px;
          font-weight: bold;
          font-style: normal;
        }

        .exchange {
          margin-top: 10px;
          display: inline-block;
          font-size: $font-size-large;
          font-weight: bold;
        }
      }

      .over-price {
        margin-top: 8px;
        font-size: $font-size-small;
        transform: scale(0.8);
      }
    }

    .coupon-r {
      flex: 1;
      padding: 16px 8px;
      text-align: center;

      .name {
        display: -webkit-box;
        font-size: $font-size-medium;
        font-weight: bold;
        min-height: 30px;
        line-height: 1.3;
        letter-spacing: 0;
        overflow: hidden;
        text-overflow: ellipsis;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
      }

      .datetime {
        width: 120%;
        margin-top: 5px;
        margin-left: -10%;
        font-size: $font-size-small;
        color: #F97B2A;
        transform: scale(0.8);
      }
    }
  }
 
}

.flexible-dialog{
  	position: fixed;
  	top: 0;
  	left: 0;
  	width: 100%;
  	height: 100%;
  	background-color: rgba(0, 0, 0, 0.5);
  	z-index: 1000;
	  display: flex;
	  justify-content center;
	  align-items center;
	.flexible-dialog-content {
	  position: absolute;
	  width: 90%;
	  background-size: 100% 100%;
	  height: 390px;
	  background-color: #ffffff;
	  border-radius: 12px
	  display: flex;
	  align-items: center;
	  justify-content: center;
	  flex-direction: column;
	  .flexible-cotent-top{
		  width: 90%;
		  height: 100px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  .flexible-cotent-top-img{
			  width: 100%;
			  height: auto;
		  }
	  }
	  
	  .flexible-cotent-middle-title{
		  width: 85%;
		  height: 50px;
	      display: flex;
		  align-items: center;
		  justify-content: center;
		  margin-top: 20px;
		  font-size: 18px;
		  color: #333333;
		  font-weight: bold;
	  }
	  
	  .flexible-cotent-middle-info{
		  width: 85%;
	      display: flex;
		  justify-content space-between;
		  font-size: 15px;
		  color: #666666;
		  letter-spacing: 0.8px;
		  margin-top: 5px;
		  line-height: 22.5px;
		  text-align: center;
	  }
	  
	  .flexible-cotent-bottom{
		  width: 195px;
		  height: 43px;
		  line-height: 43px;
		  text-align: center;
	      display: flex;
		  justify-content: center;
		  font-size: 18px;
		  color: #ffffff;
		  background-color: #0076A5;
		  border-radius: 21.5px
		  margin-top: 40px;
	  }
	}
}


</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;
$color-main-opacity = rgba(37, 111, 255, 0.2);

// 荣耀
.ry-style {
  bottom: 0;
  display: none;

  .container {
    .logo-box {
      .logo-img {
        background: transparent;
      }

      .text-box {
        border-bottom: 10px solid $color-main-opacity;
      }
    }

    .main-container {
      top: 56%;

      .phone {
        background: url('../../assets/images/ry/bg.png') no-repeat center;
        background-size: 100% auto;

        .phone-img {
          width: 167px;
          height: 326px;
          background-size: 167px 326px;
        }
      }
    }

    .btn-box {
      padding-top: 40px;

      .btn-normal-blue {
        font-size: 16px;
        background: $color-main;
      }
    }
  }
}
</style>
<template>
  <div class="recommend">
    <!-- 轮播图  @click="goPath(banner, {}, banner.type)"-->
    <van-swipe :autoplay="3000" class="banner-list">
      <van-swipe-item
        v-for="(banner, index) in bannerList"
        :key="index"
      >
        <img :src="banner.bannerUrl" />
      </van-swipe-item>
    </van-swipe>

    <ul class="menu-list">
      <li><span class="sprite-icon home_menu_customized"></span>个性定制</li>
      <li><span class="sprite-icon" :class="menuIcon"></span>{{ menuName }}</li>
      <li><span class="sprite-icon home_menu_texture"></span>品质材质</li>
      <li><span class="sprite-icon home_menu_rapidly"></span>急速发货</li>
    </ul>
    <!-- 公告 -->
    <div class="notice-box" v-if="showNotice">
      <Notice></Notice>
    </div>

    <!-- 系列展示 -->
    <div
      class="category-wrap"
      v-for="(category, index) in categoryData"
      :key="index"
    >
      <div class="category-banner">
        <img :src="category.categoryImage" alt="" />
      </div>
      <div class="category-item-wrap">
        <theme-item
          class="category-item"
          v-for="(category_item, index) in category.pictureDTOList"
          :key="index"
          :itemData="category_item"
        ></theme-item>
      </div>

      <p
        class="more-category"
        @click="
          goPath('/category', {
            seriesId: category.seriesId,
            categoryImage: category.categoryImage,
          })
        "
      >
        查看更多
        <van-icon name="arrow" />
      </p>
    </div>

    <!-- 推荐 -->
    <div
      class="recommend-wrap"
      v-if="recommendData && recommendData.length > 0"
    >
      <div class="recommend-content">
        <h6 class="recommend-title">
          <span class="text">为你推荐</span>
          <span class="sprite-icon home_title_recommended"></span>
        </h6>

        <van-list
          v-model="loading"
          :finished="finished"
          :immediate-check="false"
          @load="onLoadRecommend"
          :offset="10"
        >
          <theme-item
            class="recommend-item"
            v-for="(recommend, index) in recommendData"
            :key="index"
            :itemData="recommend"
            :class="[
              {
                'no-border-top': index === 0 || index === 1,
              },
              { 'no-border': recommendData.length === 1 },
              { 'first-item': index === 0 },
              {
                'second-last-item': index === recommendData.length - 2,
              },
              {
                'last-item': index === recommendData.length - 1,
              },
            ]"
          ></theme-item>
        </van-list>
      </div>
    </div>
    <bottom-tips></bottom-tips>

    <div class="coupon-dialog" v-show="couponShow && pageFlag !== 'samCoupon'">
      <div class="coupon-dialog-content">
        <van-icon name="close" @click="couponShow = false" />
        <ul class="coupon-list">
          <li class="coupon-li" v-for="(coupon, idx) in couponList" :key="idx">
            <div class="coupon-l">
              <div class="sale-price" v-show="coupon.couponMethod === 1">
                ¥<em>{{ coupon.reduction }}</em>
              </div>
              <div class="sale-price" v-show="coupon.couponMethod === 2">
                <em>{{ coupon.discount / 10 }}</em
                >折
              </div>
              <div class="sale-price" v-show="coupon.couponMethod === 3">
                <span class="exchange">商品兑换</span>
              </div>
              <div class="over-price" v-show="coupon.reduction === 6">
                新人专享
              </div>
              <div class="over-price" v-show="coupon.reduction !== 6">
                满{{ coupon.orderMoney }}元可用
              </div>
            </div>
            <div class="coupon-r">
              <div class="name">{{ coupon.couponName.trim() }}</div>
              <div class="datetime today" v-show="isToday(coupon.endTime)">
                今天到期&nbsp;({{ coupon.endTime | formatTime }})
              </div>
              <div class="datetime" v-show="!isToday(coupon.endTime)">
                有效期：{{ coupon.startTime | formatDate }}~{{
                  coupon.endTime | formatDate
                }}
              </div>
            </div>
          </li>
        </ul>
        <div
          class="btn-wrap recieve-btn"
          v-if="couponShowUnlogin"
          @click="handleSubmit"
        ></div>
        <div class="btn-wrap" v-else @click="couponShow = false"></div>
      </div>
    </div>

    <tabs
      :curTab="'recommend'"
      :curVersion="curVersion"
      :userNo="userNo"
    ></tabs>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
import Loading from "base/loading/loading";
import { formatDate,setLocalStorageItem,getLocalStorageItem } from "common/js/common";
import BottomTips from "../components/bottomTips.vue";
import Tabs from "../components/tabs.vue";
import themeItem from "../components/themeItem.vue";
import Notice from "base/notice/notice";
import api from "common/js/allApi.js";

export default {
  components: {
    Loading,
    themeItem,
    BottomTips,
    Tabs,
    Notice,
  },
  name: "recommend",
  data() {
    return {
      bannerList: [], // 轮播图
      categoryData: [], // 系列展示
      // 推荐
      recommendData: [],
      recommendPage: 1,
      recommendCount: 4,
      recommendTotal: 0,
      loading: false,
      finished: false,
      distributorId: "", // 分销商id
      platform: "", // 平台
      distributor: "", // 分销商
      menuIcon: "",
      menuName: "",
      curVersion: "",
      couponShow: false, // 已领取优惠券弹窗
      couponShowUnlogin: false, // 未登录优惠券弹窗
      couponList: [], // 优惠券列表
      pageFlag: "",
      showNotice: true,
      userNo: "",
      isLoading: true,
      message: "载入中"
    };
  },
  
  created() {
  	
  },
  
  mounted() {
    // 公告通知
    let d2 = "2019-05-07 00:00:00";
    let now = new Date();
    let endTime = new Date(Date.parse(d2.replace(/-/g, "/")));
    if (now >= endTime) {
      this.showNotice = false;
    }
    // 用于新旧版本tabs显示
    localStorage.setItem("curVersion", "new");
    this.userNo = getLocalStorageItem("userNo");
    this.userId = getLocalStorageItem("userId");
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
    if (this.$route.query.orderSource) {
      let orderSource = this.$route.query.orderSource;
      localStorage.setItem("orderSource", orderSource);
    } else {
      localStorage.setItem("orderSource", this.platform);
    }

    this.getDistributor();

    if (parseInt(this.platform) === 7) {
      this.menuIcon = "home_menu_theme";
      this.menuName = "赠送主题";
    } else {
      this.menuIcon = "home_menu_gallery";
      this.menuName = "海量图库";
    }
    this.curVersion = "new";
    localStorage.setItem("curVersion", this.curVersion);

    // 微信公众号登录授权
    if (this.$route.query.appid && !sessionStorage.getItem("hasAuth")) {
      this.weixinLogin();
    } else {
      if (parseInt(this.platform) === 7 && this.userId) {
        this.getSam();
      }
    }

    this.getBannerList();
	
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
  methods: {
    getSam() {
      this.pageFlag = this.$route.query.pageFlag;
      var samFlag = this.$route.query.samFlag;
      var hasRecieved = this.$route.query.hasRecieved || "";
      if (hasRecieved === "recieved") {
        // 专题页 - 登录成功进入
        if (samFlag === "samS20Flag") {
          // S20 - 获取已领取优惠券列表
          this.getSamCoupon("3,4", "2");
        } else if (samFlag === "samNote20Flag") {
          // Note20 - 获取已领取优惠券列表
          this.getSamCoupon("2,4", "2");
        } else if (samFlag === "samS21Flag") {
          // S21 - 获取已领取优惠券列表
          this.getSamCoupon("8,4", "2");
        } else if (samFlag === "samA52Flag") {
          // A52 - 获取已领取优惠券列表
          this.getSamCoupon("9,4", "2");
        } else {
          // 新人券 - 已领取
          this.getSamCoupon("4", "2");
        }
      } else {
        // 其他方式进入
        if (this.userId) {
          // 已登录 - 获取已领取优惠券列表
          let firstLoginIndex = sessionStorage.getItem("firstLoginIndex");
          if (
            (firstLoginIndex && firstLoginIndex !== "1") ||
            !firstLoginIndex
          ) {
            this.getSamCoupon("4", "1"); // 新人券 - 未领取
            sessionStorage.setItem("firstLoginIndex", "1");
            this.couponShowUnlogin = true;
          }
        } else {
          // 未登录 - 显示未领取优惠券
          this.getSamCoupon("4", "1"); // 新人券
          this.couponShowUnlogin = true;
        }
      }
    },
    // 获取专题活动已领取优惠券列表
    getSamCoupon(couponTypes, statuss) {
      this.$api
        .get(this, api.getSpecialCoupon, {
          couponTypes: couponTypes,
          statuss: statuss,
        })
        .then((res) => {
          if (res.success) {
            this.couponList = res.data;
            if (res.data.length > 0) {
              this.couponShow = true;
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getDistributor() {
      if (this.distributorId === "1811") {
        document.title = "诚讯智能生活";
        this.distributor = "chengxun";
      } else {
        this.distributor = "bat";
      }
      localStorage.setItem("distributor", this.distributor);
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
        // 未授权，需要重新授权获取code
        let authUrl =
          "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" +
          APPID +
          "&redirect_uri=" +
          encodeURIComponent(window.location.href) +
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
      /* eslint-disable */
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
    // 页面跳转
    goPath(path, params, type) {
      if (type) {
        // type：1 首页IP系列， 2 活动页链接
        if (type === 2) {
          window.location.href = path.externalLink;
        } else {
          this.$router.push({
            path: "/category",
            query: {
              bannerId: path.id,
              seriesId: path.seriesId,
              categoryImage: path.bannerUrl,
            },
          });
        }
      } else {
        // 路由跳转
        this.$router.push({
          path: path,
          query: params,
        });
      }
    },
    // 获取轮播图
    getBannerList() {
      this.$api
        .get(this, api.getBanner, {
          distributorId: this.distributorId,
        })
        .then((res) => {
          if (res.success) {
            this.bannerList = res.data;

            this.getCategoryList();
            this.getRecommendList();
          } else {
            this.$toast.fail(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        })
        .catch((error) => {
          if (Number(this.platform) !== 7 && error.errCode === "B_AUTH_FAIL") {
            localStorage.removeItem("auth");
            this.weixinLogin();
          } else {
            this.$toast.fail(error.errMessage);
          }
        });
    },
    // 获取系列展示
    getCategoryList() {
      this.$api
        .get(this, api.getCategory, {
          distributorId: this.distributorId,
          seriesNum: 5, // 系列展示数量
          pictureNum: 4, // 每个系列展示图片数量
        })
        .then((res) => {
          this.message = "";
          this.isLoading = false;
          if (res.success) {
            this.categoryData = res.data;
          } else {
            this.$toast.fail(res.errMessage);
          }
        })
        .catch((error) => {
          this.$toast.fail(error);
        });
    },
    // 获取推荐
    getRecommendList() {
      this.$api
        .get(this, api.getRecommend, {
          distributorId: this.distributorId,
          page: this.recommendPage,
          count: this.recommendCount,
        })
        .then((res) => {
          if (res.success) {
            this.loading = false;

            let rows = res.data.list;
            if (rows == null || rows.length === 0) {
              // 加载结束
              this.finished = true;
              return;
            }

            // 将新数据与老数据进行合并
            this.recommendData = this.recommendData.concat(rows);

            // 如果列表数据条数>=总条数，不再触发滚动加载
            this.recommendTotal = res.data.total;
            if (this.recommendData.length >= this.recommendTotal) {
              this.finished = true;
            }
          } else {
            this.$toast.fail(res.errMessage);
            this.message = "";
            this.isLoading = false;
          }
        })
        .catch((error) => {
          this.$toast.fail(error);
        });
    },
    // 判断到期日期是否为当天
    isToday(date) {
      if (new Date(date).toDateString() === new Date().toDateString()) {
        return true;
      } else {
        return false;
      }
    },
    onLoadRecommend() {
      this.recommendPage++;
      this.getRecommendList();
    },
    handleSubmit() {
      // 判断是否登录
      let userId = getLocalStorageItem("userId");
      if (userId !== null && userId !== undefined && userId !== "") {
        // 登录后立即领取
        let ids = "";
        this.couponList.forEach((item) => {
          ids += item.id + ",";
        });
        this.getCoupon(ids);
      } else {
        // 未登录跳转登录
        this.$router.push({
          path: "/login",
          query: {
            platform: this.platform,
            distributorId: this.distributorId,
            sa: "2",
          },
        });
      }
    },
    // 领取优惠券
    getCoupon(ids) {
      this.$api
        .post(this, api.getCoupon, {
          userId: this.userId,
          couponIds: ids,
        })
        .then((res) => {
          if (res.status === 200) {
            this.$toast("领取成功~");
            this.couponShow = false;
          } else {
            this.$toast(res.error);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
  },
  beforeRouteEnter(to, from, next) {
    if (from.name === "login" && from.query.samFlag === "samA52Flag") {
      next("/samsungA52Coupon");
    } else {
      next();
    }
  },
  beforeRouteLeave(to, from, next) {
    if (Number(this.platform) === 7 && to.name === "index") {
      window.location.href = "action://finish";
    } else {
      next();
    }
  },
};
</script>

<style lang="stylus" scoped>
@import '../../common/styles/mixin.styl';

$white = #fff;
$dark = #000;
$gray = #959595;
$light-gray = #B9B9B9;
$border-color = #ECECEC;

.recommend {
  padding-bottom: 50px;
}

.banner-list {
  width: 100%;
  text-align: center;

  img {
    max-width: 100%;
    max-height: 100%;
  }

  >>>.van-swipe__indicator {
    width: 4px;
    height: 4px;
    border-radius: 4px;
    background-color: #fff;
    opacity: 1;

    &.van-swipe__indicator--active {
      width: 14px;
      background-color: #fff;
    }
  }
}

.menu-list {
  display: flex;
  padding: 15px 5px;
  background-color: $white;

  li {
    flex: 1;
    font-size: 14px;
    color: $gray;
    text-align: center;
    line-height: 15px;

    .sprite-icon {
      margin-right: 2px;
    }
  }
}

.notice-box {
  background-color: $white;
  padding: 0 15px;
}

.category-wrap {
  padding: 20px 15px 0;

  .category-banner {
    display: flex;
    align-items: center;
    width: 100%;
    max-height: 108px;
    border-radius: 5px;
    overflow: hidden;

    img {
      width: 100%;
    }
  }

  .category-item-wrap {
    .category-item {
      margin-top: 20px;
      width: 47.5%;
      border-radius: 5px;

      &:nth-child(2n) {
        margin-left: 5%;
      }
    }
  }

  .more-category {
    margin-top: 20px;
    font-size: 14px;
    color: $light-gray;
    text-align: center;

    .van-icon {
      top: 2px;
    }
  }
}

.recommend-wrap {
  margin-top: 20px;
  padding: 0 15px;

  .recommend-content {
    background-color: $white;
  }

  .recommend-title {
    position: relative;
    padding: 25px 0 15px;
    font-size: 18px;
    color: $dark;
    text-align: center;

    .text {
      position: absolute;
      top: 25px;
      left: 50%;
      transform: translateX(-50%);
    }
  }

  .recommend-item {
    width: 50%;
    border-top: 1px solid $border-color;
    box-sizing: border-box;

    &.first-item {
      position: relative;

      &::after {
        display: block;
        content: '';
        position: absolute;
        top: 0;
        right: -1px;
        width: 1px;
        height: 30px;
        background: linear-gradient(0, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 100%);
        z-index: 1;
      }
    }

    &.last-item {
      position: relative;

      &::after {
        display: block;
        content: '';
        position: absolute;
        bottom: 0;
        left: -1px;
        width: 1px;
        height: 30px;
        background: linear-gradient(-180deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 100%);
      }
    }

    &:nth-child(odd) {
      position: relative;
      border-right: 1px solid $border-color;

      &::before {
        display: block;
        content: '';
        position: absolute;
        left: 0;
        top: -1px;
        width: 30px;
        height: 1px;
        background: linear-gradient(-90deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 100%);
      }

      &.last-item {
        &::after {
          display: block;
          left: auto;
          right: -1px;
        }
      }
    }

    &:nth-child(even) {
      position: relative;

      &::before {
        display: block;
        content: '';
        position: absolute;
        top: -1px;
        right: 0;
        width: 30px;
        height: 1px;
        background: linear-gradient(90deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 100%);
      }

      &.second-last-item {
        border-bottom: 1px solid $border-color;

        &::after {
          display: block;
          content: '';
          position: absolute;
          bottom: -1px;
          right: 0;
          width: 30px;
          height: 1px;
          background: linear-gradient(90deg, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 100%);
        }
      }
    }

    &.no-border-top {
      border-top: none;

      &::before {
        display: none;
      }
    }

    &.no-border {
      border: none;
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
</style>

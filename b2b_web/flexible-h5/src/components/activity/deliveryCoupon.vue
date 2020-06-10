<template>
  <div class="delivery-coupon">
    <div class="container">
      <div class="header-img">
        <img class="img" src="../../common/images/activity.png" alt="" />
      </div>
      <!----优惠券列表---->
      <div class="coupon-list">
        <div class="coupon-ul" v-if="couponList.length > 0">
          <div
            class="coupon-li"
            :class="coupon.status === 4 || coupon.status === 3 ? 'used' : ''"
            v-for="(coupon, idx) in couponList"
            :key="idx"
            @click="coupon.status === 1 ? handleSubmit(1) : ''"
          >
            <div class="coupon-l"></div>
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
              <div class="tag" :class="getClass(coupon.status)">
                <div class="text">{{ getStatus(coupon.status) }}</div>
              </div>
            </div>
          </div>
        </div>
        <div class="load-more" v-else>暂无可用优惠券</div>
        <div
          class="btn-submit"
          v-show="status === 1 && couponList.length > 0"
          @click="handleSubmit(1)"
        >
          <span class="text">立即领取</span><i class="icon-arrow-right"></i>
        </div>
        <div
          class="btn-submit"
          v-show="status === 2 && couponList.length > 0"
          @click="handleSubmit(2)"
        >
          <span class="text">去使用</span><i class="icon-arrow-right"></i>
        </div>
        <div
          class="btn-submit"
          v-show="status === 3 || status === 4 || couponList.length <= 0"
          @click="handleSubmit(3)"
        >
          <span class="text">去定制</span><i class="icon-arrow-right"></i>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { formatDate,setLocalStorageItem,getLocalStorageItem } from "common/js/common";
import api from "common/js/allApi.js";
export default {
  data() {
    return {
      userId: "",
      distributorId: "",
      platform: "",
      appid: "",
      couponType: "",
      status: 1, // 优惠券状态，默认所有为 1-未领取 2-已领取 4-已使用 3-已过期
      couponList: [],
      ids: "",
    };
  },
  created() {
    this.couponType = this.getQueryString("couponType");
    this.distributorId = this.getQueryString("distributorId");
    this.platform = this.getQueryString("platform");
    this.appid = this.getQueryString("appid");
    if (this.distributorId) {
      localStorage.setItem("distributorId", this.distributorId);
    }
    if (this.platform && this.appid) {
      localStorage.setItem("platform", this.platform);
      localStorage.setItem("appid", this.appid);
    }
    localStorage.setItem("orderSource", this.platform);

    // 微信公众号登录授权
    if (this.appid && !sessionStorage.getItem("hasAuth")) {
      this.weixinLogin();
    } else {
      this.userId = getLocalStorageItem("userId");
      let statuss = "1,2";
      this.getCouponList(this.couponType, statuss);
    }
  },
  filters: {
    formatDate(time) {
      if (time) {
        let timeStr = time.replace(/-/g, "/");
        let date = new Date(timeStr);
        return formatDate(date, "yyyy.MM.dd");
      }
    },
    formatTime(time) {
      if (time) {
        let timeStr = time.replace(/-/g, "/");
        let date = new Date(timeStr);
        return formatDate(date, "hh:mm:ss");
      }
    },
  },
  methods: {
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
                sessionStorage.setItem("hasAuth", true);

                this.userId = res.data.id;
                let statuss = "1,2";
                this.getCouponList(this.couponType, statuss);
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
    handleSubmit(type) {
      // 判断用户是否登录
      if (type === 1) {
        // 登录后立即领取
        this.getCoupon(this.ids);
      } else {
        this.$router.push({
          path: "/index",
          query: {
            platform: this.platform,
            distributorId: this.distributorId,
            appid: this.appid,
          },
        });
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
            this.$router.push({
              path: "/index",
              query: {
                platform: this.platform,
                appid: this.appid,
              },
            });
          } else {
            this.$toast(res.errMessage);
          }
        })
        .catch((error) => {
          console.log(error);
        });
    },
    getClass(status) {
      if (status === 1) {
        return "";
      } else if (status === 2) {
        return "received";
      } else if (status === 3 || status === 4) {
        return "used";
      }
    },
    // 优惠券状态
    getStatus(status) {
      if (status === 1) {
        return "待领取";
      } else if (status === 2) {
        return "已领取";
      } else if (status === 4) {
        return "已使用";
      } else if (status === 3) {
        return "已过期";
      }
    },
    // 判断到期日期是否为当天
    isToday(date) {
      if (new Date(date).toDateString() === new Date().toDateString()) {
        return true;
      } else {
        return false;
      }
    },
    // 获取优惠数据
    getCouponList(couponTypes, statuss) {
      this.$api
        .get(this, api.getSpecialCoupon, {
          couponTypes: couponTypes,
          statuss: statuss,
        })
        .then((res) => {
          if (res.success) {
            this.couponList = res.data;

            let num = 0;
            let stau = "";
            this.couponList.forEach((item) => {
              if (item.couponStatus === 1 || item.couponStatus === 2) {
                // 未开始/进行中
                if (item.receivedFlag === 1) {
                  // 已领取
                  this.$set(item, "status", 2); // 已领取
                } else {
                  // 未领取
                  this.$set(item, "status", 1); // 待领取
                }
              } else if (item.couponStatus === 3) {
                // 已过期
                this.$set(item, "status", 3); // 已过期
              } else if (item.couponStatus === 6) {
                // 已使用
                this.$set(item, "status", 4); // 已使用
              }

              if (item.status === 1) {
                // 待领取
                this.status = 1;
                this.ids += item.couponId + ",";
                stau += this.status + ",";
              } else if (item.status === 2) {
                // 待使用
                this.status = 2;
                stau += this.status + ",";
              } else if (item.status === 4 || item.status === 3) {
                // 已使用或已过期
                num++;
              }
            });
            if (stau.indexOf("1") > -1) {
              // 待领取
              this.status = 1;
            } else if (stau.indexOf("2") > -1 && stau.indexOf("1") <= 0) {
              this.status = 2;
            } else if (num === this.couponList.length) {
              this.status = 3;
            }
          } else {
            this.$toast(res.errMessage);
          }
        });
    },
    getQueryString(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) return decodeURIComponent(r[2]);
      return null;
    },
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.delivery-coupon {
  position: fixed;
  flex-direction: column;
  width: 100%;
  top: 0;
  bottom: 0;
  background: #fccace;

  .container {
    position: relative;
    width: 100%;
    height: 100%;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .header-img {
      display: block;
      width: 100%;
      height: auto;

      .img {
        width: 100%;
        height: 100%;
      }
    }

    .coupon-list {
      position: relative;
      margin: -10px 26px 40px;
      box-sizing: border-box;
      border-radius: 8px;
      text-align: center;

      .title {
        margin-bottom: 14px;
        font-size: $font-size-medium-x;
        color: $color-text-b;
        text-align: left;

        .title-name {
          display: inline-block;
        }
      }

      .coupon-ul {
        display: block;

        .coupon-li {
          position: relative;
          display: flex;
          width: 314px;
          height: 122px;
          margin-bottom: 22px;
          border-radius: 6px;
          overflow: hidden;
          color: #E4443C;
          background-image: url('../../common/images/coupon.png');
          background-repeat: no-repeat;
          background-size: 314px 122px;

          .coupon-l {
            position: relative;
            display: inline-block;
            width: 30%;
            padding: 30px 0;
            box-sizing: border-box;
            text-align: center;

            &.used {
              &::before {
                background-image: linear-gradient(to bottom, #ddd 0%, #ddd 40%, transparent 40%);
              }
            }

            .sale-price {
              font-size: $font-size-small;

              em {
                margin-left: 3px;
                font-size: 30px;
                font-weight: Medium;
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
              margin-top: 15px;
              font-size: $font-size-small;
            }
          }

          .coupon-r {
            position: absolute;
            left: 30%;
            top: 6px;
            right: 1px;
            bottom: 5px;
            display: inline-block;
            padding: 25px 16px;
            overflow: hidden;

            .tag {
              position: absolute;
              top: -10px;
              right: -6px;
              font-size: $font-size-small;
              color: indianred;
              width: 49px;
              height: 49px;
              padding: 2px;
              box-sizing: border-box;
              text-align: center;
              border-radius: 50%;
              border: 1px solid #FFBC53;

              .text {
                position: absolute;
                top: 50%;
                left: 50%;
                display: inline-block;
                width: 41px;
                height: 41px;
                line-height: 46px;
                border-radius: 50%;
                border: 1px solid #FFBC53;
                color: #ffffff;
                background-color: #FFBC53;
                transform: translate3d(-50%, -50%, 0) rotate(30deg);
              }

              &.received {
                border: 1px solid #f67d75;

                .text {
                  border: 1px solid #f67d75;
                  background-color: #f67d75;
                }
              }
            }

            &.used {
              border: 1px solid #C7C7C7;

              .text {
                border: 1px solid #C7C7C7;
                background-color: #C7C7C7;
              }
            }

            .name {
              display: -webkit-box;
              width: 88%;
              font-size: 18px;
              font-weight: 500;
              color: #E4443C;
              min-height: 30px;
              line-height: 1.3;
              text-align: justify;
              letter-spacing: 0;
              overflow: hidden;
              text-overflow: ellipsis;
              -webkit-line-clamp: 2;
              -webkit-box-orient: vertical;

              &.invalid {
                width: 96%;
              }
            }

            .datetime {
              margin-top: 10px;
              margin-left: -10px;
              font-size: $font-size-small;
              text-align: left;
              transform: scale(0.9);

              &.today {
                color: #E4443C;
              }
            }
          }

          &.used {
            background-image: url('../../common/images/coupon2.png');

            .name {
              color: #7C7C7C;
            }

            .today {
              color: #7C7C7C !important;
            }

            .tag {
              border-color: #b5b5b5;

              .text {
                color: #ffffff;
                border-color: #b5b5b5;
                background-color: #b5b5b5;
              }
            }
          }
        }
      }

      .load-more {
        margin: 20px 0 40px;
        text-align: center;
        font-size: $font-size-medium;
        color: #fff;
      }

      .btn-submit {
        position: relative;
        display: block;
        width: 170px;
        height: 60px;
        line-height: 50px;
        margin: 0 auto;
        box-sizing: border-box;
        text-align: center;
        border-radius: 20px;
        background-image: url('../../common/images/btn_coupon.png');
        background-repeat: no-repeat;
        background-size: 175px 60px;

        .text {
          display: inline-block;
          margin-right: 6px;
          height: 36px;
          font-size: $font-size-medium;
          color: #E4443C;
          line-height: 36px;
          vertical-align: middle;
        }

        .icon-arrow-right {
          position: relative;
          display: inline-block;
          width: 15px;
          height: 15px;
          line-height: 36px;
          border-radius: 50%;
          background-color: #E4443C;
          vertical-align: middle;

          &::after {
            position: absolute;
            content: '';
            display: inline-block;
            left: 5px;
            top: 5px;
            width: 0;
            height: 0;
            border-top: 3px solid transparent;
            border-left: 6px solid #fcce81;
            border-bottom: 3px solid transparent;
          }
        }
      }
    }
  }
}
</style>

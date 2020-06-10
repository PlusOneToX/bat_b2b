<template>
    <view class="coupon">
        <view class="container-wrapper">
            <view class="header-img"></view>
            <!----优惠券列表---->
            <view class="coupon-list">
                <view class="title">
                    <view class="title-name">Galaxy S23 FE 5G用户定制专享</view>
                </view>
                <view class="coupon-ul" v-show="couponList.length > 0">
                    <view class="coupon-li" :class="
                coupon.status === 4 ||
                coupon.status === 3 ||
                (!hasQualified && userId && coupon.status === 1)
                  ? 'used'
                  : ''
              " v-for="(coupon, idx) in couponList" :key="idx">
                        <view class="coupon-l">
                            <view class="sale-price" v-show="coupon.couponMethod === 1">
                                ¥<em>{{ coupon.reduction }}</em>
                            </view>
                            <view class="sale-price" v-show="coupon.couponMethod === 2">
                                <em>{{ coupon.discount / 10 }}</em>折
                            </view>
                            <view class="sale-price" v-show="coupon.couponMethod === 3">
                                <span class="exchange">商品兑换</span>
                            </view>
                            <view class="over-price" v-show="coupon.reduction === 6">
                                新人专享
                            </view>
                            <view class="over-price" v-show="coupon.reduction !== 6">
                                满{{ coupon.orderMoney }}元可用
                            </view>
                        </view>
                        <view class="coupon-r">
                            <view class="name">{{ coupon.couponName.trim() }}</view>
                            <view class="datetime today" v-show="isToday(coupon.endTime)">
                                今天到期&nbsp;({{ coupon.endTime | formatTime }})
                            </view>
                            <view class="datetime" v-show="!isToday(coupon.endTime)">
                                有效期：{{ coupon.startTime | formatDate }}~{{
                    coupon.endTime | formatDate
                  }}
                            </view>
                        </view>
                        <view class="tag" :class="{ unreceived: coupon.status === 1 }">
                            <view class="text">
                                {{
                    !hasQualified && userId && coupon.status === 1
                      ? "不可领"
                      : getStatus(coupon.status)
                  }}
                            </view>
                        </view>
                    </view>
                </view>
                <view class="load-more" v-show="couponList.length <= 0">
                    暂无可用优惠券
                </view>
                <view class="btn-wrap">
                    <view class="btn-submit btn-receive" v-show="
                (!userId && couponList.length > 0) ||
                (userId && hasQualified && status === 1 && couponList.length > 0)
              " @click="handleSubmit(1)">
                        一键领取优惠券
                    </view>
                    <view class="btn-submit btn-receive" v-show="userId && status === 2 && couponList.length > 0" @click="handleSubmit(2)">
                        去使用
                    </view>
                    <view class="btn-submit btn-custom" v-show="status === 3 || status === 4 || couponList.length === 0" @click="handleSubmit(3)">
                        去体验定制
                    </view>
                </view>
            </view>
            <!-----定制效果展示----->
            <view class="pic-list">
                <view class="title">
                    <view class="line left"></view>
                    <view class="name">定制效果展示</view>
                    <view class="line right"></view>
                </view>
                <view class="desc">
                    <span class="line two left"></span><span class="line one left"></span>
                    <view class="text">亮彩玻璃 高清防摔 赠送主题 极速发货</view>
                    <span class="line one right"></span><span class="line two right"></span>
                </view>
                <view class="pic-ul">
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454736351.blob:http://localhost:8080/95ce4542-e4e3-4f75-8616-0f0258cb039b" />
                    </view>
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454747232.blob:http://localhost:8080/fa9fbe66-12b0-44e9-ab54-4adf9a9b2c4a" />
                    </view>
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454757242.blob:http://localhost:8080/897ac825-351f-4000-b848-35bec3c9a3ef" />
                    </view>
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454768820.blob:http://localhost:8080/6fbf67df-9a99-4443-8bbf-5b4a607101d8" />
                    </view>
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454783332.blob:http://localhost:8080/871488c6-5280-4d67-9c9b-750d260ea14f" />
                    </view>
                    <view class="pic-li">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454794682.blob:http://localhost:8080/b95aef7e-c87b-40d0-9425-4c3af28fd4dd" />
                    </view>
                </view>
            </view>
            <!-- 热门定制图案一览-->
            <view class="pic-list">
                <view class="title">
                    <view class="line left"></view>
                    <view class="name">热门定制图案一览</view>
                    <view class="line right"></view>
                </view>
                <view class="pic-ul">
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454822204.blob:http://localhost:8080/6b6f5134-26c9-4f84-a613-32cd998327b0" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454834465.blob:http://localhost:8080/a3d1b382-ebd6-40f2-8a48-dc04bb59c58f" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454844779.blob:http://localhost:8080/eeb728fb-2ac2-4fde-a4ec-e58bfd3263f3" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454854896.blob:http://localhost:8080/018b1422-2911-4241-bd02-4695c18cf678" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454864429.blob:http://localhost:8080/38ce6d9e-ed73-46ab-a4d3-541bcaea5085" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454875748.blob:http://localhost:8080/29b6b0e0-18bd-4390-a3f5-b25c47834cf1" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454885692.blob:http://localhost:8080/12a66f39-22a3-4e2f-aa8c-691d3a479edf" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454896830.blob:http://localhost:8080/fe08283a-35d6-4e51-9b96-8d7240041a6a" />
                    </view>
                    <view class="pic-li three">
                        <img class="img" src="https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454907101.blob:http://localhost:8080/c6ce5e8f-58da-4476-81d3-b92726401e6c" />
                    </view>
                </view>
            </view>
        </view>
        <!--提示-->
        <view class="tip none" id="tip">网络异常，请检查您的网络是否连接正常</view>
    </view>
</template>
  
  <script type="text/ecmascript-6">
import { formatDate } from "common/js/common";
import api from "common/js/allApi.js";
import { getXsaUid } from "common/js/saApi";
export default {
    data() {
        return {
            platform: "",
            distributorId: "",
            userId: "", // 用户ID
            status: 1, // 优惠券状态，默认所有为 1-未领取 2-已领取 4-已使用 3-已过期
            couponList: [],
            ids: "",
            type: 0,
            isUser: false,
            isLine: true,
            hasQualified: true, // 是否有资质
        };
    },
    mounted() {
        window.addEventListener("offline", function () {
            document.getElementById("tip").classList.remove("none");
        });
        window.addEventListener("online", function () {
            document.getElementById("tip").classList.add("none");
        });
    },
    created() {
        // 用于新旧版本tabs显示
        uni.setStorageSync("curVersion", "");

        this.platform = "7";
        this.distributorId = "2529";
        this.userId = uni.getStorageSync("userId")
            ? uni.getStorageSync("userId")
            : "";
        uni.setStorageSync("platform", this.platform);
        uni.setStorageSync("distributorId", this.distributorId);
        uni.setStorageSync("orderSource", this.platform);
        this._initDate('9,4', '1,2')
        window["onSamLogin"] = (saInfo) => {
            this.onSamLogin(saInfo);
        };
        window.SamsungAccount.login(false, "onSamLogin");
    },
    methods: {
        // 判断是否有资质领券
        getInvalidCoupon(xSaUid) {
            this.$api
                .get(this, api.getValidCoupon, {
                    accountId: xSaUid, 
                })
                .then((res) => {
                    if (res.success) {
                        if (Number(res.data) === 1) {
                            this.hasQualified = true;
                            // this._initDate('9,4', '1,2')
                        } else {
                            this.hasQualified = false;
                        }
                    } else {
                        this.hasQualified = true;
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000,
                        });
                    }
                })
                .catch((error) => {
                    this.hasQualified = true;
                    console.log(error);
                });
        },
        // 获取专题活动优惠券列表
        _initDate(couponTypes, statuss) {
            if (this.userId) {
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
                            uni.showToast({
                                title: res.errMessage,
                                icon: "none",
                                duration: 2000,
                            });
                        }
                    });
            } else {
                uni.navigateTo({
                    url: "../login/login?platform=" + this.platform + "&distributorId=" + this.distributorId + "&sa=2&ids=" + this.ids + "&samFlag=samA52Flag&hasQualified=" + this.hasQualified
                });
            }
        },
        onSamLogin(saInfo) {
            let obj = JSON.parse(saInfo);
            if (obj.loginStatus === "login") {
                // 第三方帐号已登录
                let userAgent = navigator.userAgent;
                let otherUid = uni.getStorageSync("uid");
                let uid = getXsaUid(userAgent);
                if (uid) {
                    // 判断是否有资质领券
                    this.getInvalidCoupon(uid);
                } else {
                    this.hasQualified = true;
                }
                if (uid !== otherUid) {
                    // 清空数据
                    this.clearData();
                    this.userId = "";
                }
            } else {
                // 未登录跳转登录
                uni.navigateTo({
                    url: "../login/login?platform=" + this.platform + "&distributorId=" + this.distributorId + "&sa=2&ids=" + this.ids + "&samFlag=samA52Flag&hasQualified=" + this.hasQualified
                });
            }
            this._initDate("9,4", "1,2");
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
            localStorage.removeItem("sa");
        },
        // 判断到期日期是否为当天
        isToday(date) {
            if (new Date(date).toDateString() === new Date().toDateString()) {
                return true;
            } else {
                return false;
            }
        },
        // 确定
        handleSubmit(type) {
            this.type = type;
            let userNo = uni.getStorageSync("userNo");
            if (this.type === 1) {
                if (userNo !== null && userNo !== undefined && userNo !== "") {
                    // 登录后立即领取
                    this.getCoupon(this.ids);
                } else {
                    // 未登录跳转登录
                    uni.navigateTo({
                        url: "../login/login?platform=" + this.platform + "&distributorId=" + this.distributorId + "&sa=2&ids=" + this.ids + "&samFlag=samA52Flag&hasQualified=" + this.hasQualified
                    });
                }
            } else if (this.type === 2 || this.type === 3) {
                // 立即使用
                if (userNo !== null && userNo !== undefined && userNo !== "") {
                    uni.setStorageSync("sa", "1");
                    uni.switchTab({
                        url: '/src/components/index/index?platform=7&distributorId=2529'
                    });

                } else {
                    // 未登录跳转登录
                    uni.navigateTo({
                        url: "../login/login?platform=" + this.platform + "&distributorId=" + this.distributorId + "&sa=2&ids=" + this.ids + "&samFlag=samA52Flag&hasQualified=" + this.hasQualified
                    });
                }
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
                        uni.setStorageSync("sa", "1");
                        uni.switchTab({
                            url: '/src/components/index/index?platform=7&distributorId=2529'
                        });
                    } else {
                        uni.showToast({
                            title: res.errMessage,
                            icon: "none",
                            duration: 2000,
                        });
                    }
                })
                .catch((error) => {
                    console.log(error);
                });
        },
        // 优惠券状态
        getStatus(status) {
            if (status === 1) {
                return "未领取";
            } else if (status === 2) {
                return "已领取";
            } else if (status === 4) {
                return "已使用";
            } else if (status === 3) {
                return "已过期";
            }
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
};
  </script>
  
  <style scoped lang="stylus" rel="stylesheet/stylus">
  .coupon {
      position: fixed;
      width: 100%;
      top: 0;
      bottom: 0;
      background-color: #FFFFFF;

      .container-wrapper {
          position: relative;
          width: 100%;
          height: 100%;
          padding-bottom: 50px;
          box-sizing: border-box;
          overflow-y: scroll;
          -webkit-overflow-scrolling: touch;

          &::-webkit-scrollbar {
              display: none;
          }

          .header-img {
              display: block;
              width: 100%;
              height: 230px;
              background-image: url('https://bat.oss-cn-shenzhen.aliyuncs.com//diy/h51676454655545.blob:http://localhost:8080/a1b28e4a-5c0a-4e7b-8337-e8978aee01ae');
              background-position: top !important;
              background-size: 100% auto;
          }

          .coupon-list {
              position: relative;
              margin: -95px 12px 22px;
              padding: 16px 12px 30px;
              box-sizing: border-box;
              border-radius: 8px;
              text-align: center;
              box-shadow: 0px 4px 8px 0px rgba(99, 177, 171, 0.2);
              background-color: #ffffff;

              .title {
                  margin-bottom: 14px;
                  font-size: 32rpx;
                  color: #000000;
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
                      margin-bottom: 15px;
                      border-radius: 6px;
                      overflow: hidden;
                      color: #FFFFFF;
                      background: linear-gradient(90deg, #7ED3B9 0%, #62B0AA 100%);
                      box-shadow: 0px 2px 4px 0px rgba(203, 203, 203, 0.5);

                      &::before {
                          content: '';
                          position: absolute;
                          top: -6px;
                          left: calc(30% - 6px);
                          display: block;
                          width: 12px;
                          height: 12px;
                          border-radius: 50%;
                          background-color: #ffffff;
                          z-index: 2;
                      }

                      &::after {
                          content: '';
                          position: absolute;
                          bottom: -6px;
                          left: calc(30% - 6px);
                          display: block;
                          width: 12px;
                          height: 12px;
                          border-radius: 50%;
                          background-color: #FFFFFF;
                      }

                      .tag {
                          position: absolute;
                          top: -12px;
                          right: -8px;
                          font-size: 24rpx;
                          color: indianred;
                          width: 52px;
                          height: 52px;
                          padding: 2px;
                          box-sizing: border-box;
                          text-align: center;
                          border-radius: 50%;
                          border: 1px solid #4E938E;

                          .text {
                              position: absolute;
                              top: 50%;
                              left: 50%;
                              display: inline-block;
                              width: 46px;
                              height: 46px;
                              line-height: 52px;
                              border-radius: 50%;
                              color: #FFFFFF;
                              background-color: #4E938E;
                              transform: translate3d(-50%, -50%, 0) rotate(30deg);
                          }

                          &.unreceived {
                              border-color: #FFBA01;

                              .text {
                                  background-color: #FFBA01;
                              }
                          }
                      }

                      .coupon-l {
                          position: relative;
                          display: inline-block;
                          width: 30%;
                          padding: 20px 0;
                          box-sizing: border-box;
                          text-align: center;

                          &::before {
                              content: '';
                              width: 3px;
                              background-image: linear-gradient(to bottom, #ffffff 0%, #ffffff 40%, transparent 40%);
                              background-size: 1px 6px;
                              background-repeat: repeat-y;
                              position: absolute;
                              right: -2px;
                              top: 6px;
                              bottom: 6px;
                              margin: auto;
                          }

                          &.used {
                              &::before {
                                  background-image: linear-gradient(to bottom, #ddd 0%, #ddd 40%, transparent 40%);
                              }
                          }

                          .sale-price {
                              font-size: 24rpx;

                              em {
                                  font-size: 34px;
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
                              margin-top: 10px;
                              font-size: 24rpx;
                          }
                      }

                      .coupon-r {
                          flex: 1;
                          display: inline-block;
                          padding: 20px 12px;

                          .name {
                              display: -webkit-box;
                              width: 80%;
                              font-size: 28rpx;
                              font-weight: bold;
                              color: #ffffff;
                              min-height: 30px;
                              line-height: 1.3;
                              text-align: center;
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
                              margin-top: 14px;
                              margin-left: -10px;
                              font-size: 24rpx;
                              text-align: left;
                              transform: scale(0.9);

                              &.today {
                                  color: #D0021B;
                              }
                          }
                      }

                      &.used {
                          color: #4A4A4A;
                          background: linear-gradient(90deg, #E7E7E7 0%, #BBBBBB 100%);
                          box-shadow: 0px 2px 4px 0px rgba(203, 203, 203, 0.5);

                          .name {
                              color: #4A4A4A;
                          }

                          .today {
                              color: #4A4A4A !important;
                          }

                          .tag {
                              border-color: #CCCCCC;

                              .text {
                                  color: #979797;
                                  background-color: #CCCCCC;
                              }
                          }
                      }
                  }
              }

              .load-more {
                  margin: 40px 0 30px;
                  text-align: center;
                  font-size: 28rpx;
              }

              .btn-wrap {
                  padding: 0 18px;
              }

              .btn-submit {
                  position: relative;
                  height: 40px;
                  line-height: 40px;
                  text-align: center;
                  font-size: 28rpx;
                  color: #FFFFFF;
                  background: linear-gradient(359deg, #62b1ab 0%, #7ed3b9 100%);
                  border-radius: 20px;

                  &::before {
                      content: '';
                      position: absolute;
                      top: 4px;
                      left: 12px;
                      width: 60px;
                      height: 6px;
                      // background: linear-gradient(90deg, rgba(255, 255, 255, 0.51) 0%, rgba(255, 255, 255, 0) 100%);
                      border-radius: 3px;
                  }

                  &.btn-receive {
                      animation: scale-btn 0.5s linear infinite alternate;
                  }

                  @keyframes scale-btn {
                      0% {
                          transform: scale(0.95);
                      }

                      100% {
                          transform: scale(1);
                      }
                  }

                  &.btn-custom {
                      width: 50%;
                      margin: 20px auto 0;
                      color: #4A4A4A;
                      border: 1px solid #6DB9B1;
                      background: #FFFFFF;
                      box-shadow: none;
                      box-sizing: border-box;
                  }
              }
          }

          .pic-list {
              margin-top: 24px;
              padding: 0 20px;
              text-align: center;
              box-sizing: border-box;

              .title {
                  display: inline-block;

                  .line {
                      display: inline-block;
                      width: 66px;
                      height: 1px;
                      background-color: #62b0aa;

                      &.left {
                          position: relative;

                          &::after {
                              position: absolute;
                              content: '';
                              top: -5px;
                              right: -11px;
                              width: 9px;
                              height: 9px;
                              border-radius: 50%;
                              border: 1px solid #62b0aa;
                          }
                      }

                      &.right {
                          position: relative;

                          &::after {
                              position: absolute;
                              content: '';
                              top: -5px;
                              left: -11px;
                              width: 9px;
                              height: 9px;
                              border-radius: 50%;
                              border: 1px solid #62b0aa;
                          }
                      }
                  }

                  .name {
                      display: inline-block;
                      padding: 0 20px;
                      font-size: 28rpx -x;
                      font-weight: 500;
                      color: #62b0aa;
                      vertical-align: middle;
                  }
              }

              .desc {
                  margin-top: 8px;
                  display: block;
                  font-size: 28rpx;
                  color: #E89000;
                  text-align: center;
                  text-align: center;
                  vertical-align: middle;

                  .text {
                      position: relative;
                      display: inline-block;

                      &:after {
                          content: '';
                          position: absolute;
                          left: 0;
                          bottom: 0;
                          width: 100%;
                          height: 4px;
                          background: linear-gradient(180deg, #62b0aa 0%, #86DCAC 100%);
                          opacity: 0.25;
                      }
                  }

                  .line {
                      display: inline-block;
                      height: 2px;
                      border-radius: 2px;
                      background-color: #FF7271;
                      transform: rotate(55deg);
                      vertical-align: middle;

                      &.one {
                          width: 12px;
                          height: 2px;
                          background-color: #FF7271;
                      }

                      &.two {
                          width: 8px;
                          background-color: #62b0aa;
                      }
                  }

                  .line-left2 {
                      display: inline-block;
                      width: 8px;
                      height: 2px;
                      border-radius: 2px;
                      background-color: #62b0aa;
                      transform: rotate(55deg);
                  }
              }

              .pic-ul {
                  display: flex;
                  margin-top: 22px;
                  flex-direction: row;
                  flex-wrap: wrap;

                  .pic-li {
                      display: inline-block;
                      margin: 0 auto 12px;
                      width: 48%;
                      height: auto;

                      &.three {
                          width: 31%;
                      }

                      .img {
                          display: inline-block;
                          width: 100%;
                          height: auto;
                      }
                  }
              }
          }
      }

      .tip {
          position: absolute;
          top: 50%;
          left: 50%;
          transform: translate3d(-50%, -50%, 0);
          display: inline-block;
          width: 70%;
          padding: 3px 5px;
          line-height: 1.5;
          color: #ffffff;
          font-size: 14px;
          text-align: center;
          border-radius: 16px;
          background-color: rgba(0, 0, 0, 0.6);

          &.none {
              visibility: hidden;
          }
      }
  }
</style>
  
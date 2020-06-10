<template>
  <div class="coupon">
    <!-- 优惠券列表 -->
    <div class="tab-ul">
      <div
        class="tab-li"
        @click="handleItem(item)"
        v-for="(item, index) in couponList"
        :key="index"
      >
        <!-- 
            优惠券状态 couponStatus: 0 未发布，1 未开始，2 进行中，3 已过期，4 提前结束，5 已作废，6 已使用
            领券状态 status: 0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
            商品是否可用 goodsEnable: 1 可用，0 不可用
            是否已领取 receivedFlag: 1 已领取，0 未领取
           -->
        <div
          class="coupon-main"
          :class="
            (item.receivedFlag && status === 3) || status === 1 ? '' : 'used'
          "
        >
          <div
            class="coupon-l"
            :class="
              (item.receivedFlag && status === 3) || status === 1 ? '' : 'used'
            "
          >
            <div class="sale-price" v-show="item.couponMethod === 1">
              ¥<em>{{ item.reduction }}</em>
            </div>
            <div class="sale-price" v-show="item.couponMethod === 2">
              <em>{{ item.discount / 10 }}</em
              >折
            </div>
            <div class="sale-price" v-show="item.couponMethod === 3">
              <span class="exchange">商品兑换</span>
            </div>
            <div class="over-price" v-if="item.orderMoney > 0">
              满{{ item.orderMoney }}元可用
            </div>
            <div class="over-price" v-else>新人可用</div>
          </div>
          <div class="coupon-m">
            <div class="name">{{ item.couponName }}</div>
            <div class="datetime today" v-show="isToday(item.endTime)">
              今天到期&nbsp;({{ item.endTime | formatTime }})
            </div>
            <div class="datetime" v-show="!isToday(item.endTime)">
              {{ item.startTime | formatDate }}-{{
                item.endTime | formatDate
              }}有效
            </div>
          </div>
          <!-- 未开始/进行中，未领取 -->
          <div
            class="btn"
            v-show="
              (item.couponStatus === 1 || item.couponStatus === 2) &&
              !item.receivedFlag
            "
            @click.stop="getCouponBtn(item)"
          >
            {{ tabValue }}
          </div>
          <!-- 未使用/已使用/已过期 -->
          <div
            class="tag"
            v-show="status === 3 || status === 4 || status === 5"
          >
            <div class="text">{{ tabValue }}</div>
          </div>
          <i
            class="icon icon-down"
            :id="`icon` + item.couponId"
            v-show="item.couponExplain"
            @click.stop="toggle(item.couponId)"
          ></i>
        </div>
        <div class="coupon-info none" :id="`info` + item.couponId">
          <div class="title" v-show="status !== 1">
            领取时间：{{ item.createTime | formatDateTime }}
          </div>
          <div class="info-ul" v-show="item.couponExplainArr.length > 0">
            <div
              class="info-li"
              v-for="(k, idx) in item.couponExplainArr"
              :key="idx"
            >
              {{ k }}
            </div>
          </div>
        </div>
      </div>
      <div
        class="load-more"
        v-show="couponList.length < totalPage"
        @click="handleMore"
      >
        点击加载更多
      </div>
      <div
        class="load-more"
        v-show="couponList.length >= totalPage && couponList.length > 0"
      >
        没有更多了
      </div>
      <div class="load-more" v-show="couponList.length <= 0">
        暂无{{ tabTitle }}优惠券
      </div>
      <div
        class="nonuse"
        v-show="status === 3 && nouserShow && couponList.length > 0"
        @click="cancelCoupon"
      >
        不使用优惠券
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import { formatDate } from "common/js/common";
import api from "api/allApi.js";

export default {
  name: "coupon",
  props: {
    tab: {
      type: Number,
      default: 0,
    },
    status: {
      type: Number,
      default: 0,
    },
    tabValue: {
      type: String,
      default: "",
    },
    tabTitle: {
      type: String,
      default: "",
    },
    couponList: {
      type: Array,
      default: null,
    },
    totalPage: {
      type: Number,
      default: 0,
    },
    nouserShow: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {
      curItem: null,
      isFlag: false, // 是否有不可用优惠券
    };
  },
  methods: {
    // 领取优惠券
    getCouponBtn(item) {
      // status: 1 未领取
      if (this.status === 1) {
        this.$api
          .post(this, api.receiveCoupon, { ids: [item.couponId] })
          .then((res) => {
            if (res.success) {
              this.$toast("领取成功");
              // 刷新
              this.$emit("refresh", this.status);
            } else {
              this.$toast.fail(res.errMessage);
            }
          })
          .catch((error) => {
            console.log(error);
          });
      }
    },
    // 取消优惠券
    cancelCoupon() {
      this.$emit("cancel");
    },
    // 展开收缩切换
    toggle(id) {
      let str = document.getElementById("info" + id);
      if (str.getAttribute("class").indexOf("none") !== -1) {
        str.className = "coupon-info";
      } else {
        str.className = "coupon-info none";
      }
    },
    getNextNode(ele) {
      return ele.nextElementSibling || ele.nextSibling;
    },
    // 加载更多
    handleMore() {
      this.$emit("more", this.status);
    },
    // 判断到期日期是否为当天
    isToday(date) {
      if (new Date(date).toDateString() === new Date().toDateString()) {
        return true;
      } else {
        return false;
      }
    },
    // 点击优惠券
    handleItem(item) {
      if (this.status === 3 && item.goodsEnable === 1) {
        // 未使用状态中，可用
        this.$emit("item", item);
      }
    },
  },
  filters: {
    formatDate(time) {
      let date = new Date(time);
      return formatDate(date, "MM月dd日");
    },
    formatDateTime(time) {
      let date = new Date(time);
      return formatDate(date, "yyyy-MM-dd hh:mm:ss");
    },
    formatTime(time) {
      let date = new Date(time);
      return formatDate(date, "hh:mm:ss");
    },
  },
};
</script>

<style scoped  lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';

.coupon {
  width: 100%;
  height: 100%;

  .tab-ul {
    display: block;
    margin-bottom: 60px;
    text-align: center;

    .title {
      font-size: $font-size-medium;
      text-align: center;
    }

    .tab-li {
      display: block;
      margin-bottom: 10px;

      .coupon-main {
        position: relative;
        display: flex;
        display: -webkit-flex;
        border-radius: 8px;
        overflow: hidden;
        z-index: 5;

        &::before {
          content: '';
          position: absolute;
          top: -6px;
          left: calc(28% - 6px);
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
          left: calc(28% - 6px);
          display: block;
          width: 12px;
          height: 12px;
          border-radius: 50%;
          background-color: $color-background-white;
        }

        &.used {
          .tag {
            position: absolute;
            top: -16px;
            right: -14px;
            font-size: $font-size-small;
            color: $color-background-l;
            width: 60px;
            height: 60px;
            padding: 4px;
            box-sizing: border-box;
            text-align: center;
            border-radius: 50%;
            border: 1px solid $color-background-l;

            .text {
              position: absolute;
              top: 50%;
              left: 50%;
              display: inline-block;
              width: 52px;
              height: 52px;
              line-height: 68px;
              border-radius: 50%;
              border: 1px solid $color-background-l;
              transform: translate3d(-50%, -50%, 0) rotate(35deg);
            }
          }

          .icon {
            color: $color-background-l;
          }

          .btn {
            color: $color-background-l;
            border-color: $color-background-l;
          }
        }

        .btn {
          position: absolute;
          top: 12px;
          right: 10px;
          padding: 5px 12px;
          box-sizing: border-box;
          color: #ffa021;
          font-size: $font-size-small;
          transform: scale(0.9);
          border: 1px solid #ffa021;
          border-radius: 20px;
        }

        .icon {
          position: absolute;
          right: 5px;
          bottom: 0;
          padding: 10px;
          font-size: $font-size-large-x;
          color: #ffa021;
          transform: all 0.4s;
        }

        .coupon-l {
          position: relative;
          display: inline-block;
          width: 28%;
          padding: 20px 0;
          box-sizing: border-box;
          text-align: center;
          color: $color-background-white;
          background-color: #ffa021;

          &::before {
            content: '';
            width: 3px;
            background-image: linear-gradient(to bottom, #ffa021 0%, #ffa021 40%, transparent 40%);
            background-size: 3px 16px;
            background-repeat: repeat-y;
            position: absolute;
            right: -2px;
            top: 12px;
            bottom: 6px;
            margin: auto;
          }

          &.used {
            &::before {
              background-image: linear-gradient(to bottom, #ddd 0%, #ddd 40%, transparent 40%);
            }
          }

          .sale-price {
            font-size: $font-size-small;

            em {
              font-size: $font-size-large-xl;
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
            margin-top: 12px;
            font-size: $font-size-small;
          }
        }

        .coupon-m {
          flex: 1;
          display: inline-block;
          padding: 12px 10px 0 10px;
          background-color: #FFF2D7;

          .name {
            display: -webkit-box;
            width: 68%;
            font-size: $font-size-medium;
            font-weight: bold;
            color: $color-text-d;
            text-align: justify;
            min-height: 30px;
            line-height: 1.3;
            letter-spacing: 0;
            overflow: hidden;
            text-overflow: ellipsis;
            -webkit-line-clamp: 3;
            -webkit-box-orient: vertical;

            &.invalid {
              width: 96%;
            }
          }

          .datetime {
            display: block;
            margin-top: 10px;
            margin-left: -10px;
            text-align: left;
            font-size: $font-size-small;
            color: $color-text;
            transform: scale(0.9);

            &.today {
              color: #D0021B;
            }
          }
        }

        &.used {
          .coupon-l {
            background-color: $color-background-l;
          }

          .coupon-m {
            background-color: #F6F6F6;

            .today {
              color: $color-text;
            }
          }
        }
      }

      .coupon-info {
        margin-top: -5px;
        background-color: $color-background-white;
        padding: 15px 10px;
        font-size: $font-size-small;
        color: $color-text;
        border-radius: 0 0 8px 8px;
        border: 1px solid $color-background;
        border-top: none;
        text-align: left;
        z-index: 2;

        &.none {
          display: none;
        }

        .title {
          margin-bottom: 30px;
          font-size: $font-size-small;
          color: $color-text;
          text-align: left;
        }

        .info-ul {
          .info-li {
            position: relative;
            padding-left: 12px;
            line-height: 1.5;
            font-size: $font-size-small;
            color: $color-text;

            &:after {
              content: ' ';
              position: absolute;
              left: 0;
              top: 8px;
              width: 4px;
              height: 4px;
              transform: translateY(-50%);
              background-color: $color-text-b;
              border-radius: 100px;
            }
          }
        }
      }
    }

    .load-more {
      margin-top: 30px;
      text-align: center;
      font-size: $font-size-medium;
    }

    .nonuse {
      display: inline-block;
      width: 210px;
      height: 40px;
      margin-top: 30px;
      font-size: $font-size-medium;
      text-align: center;
      line-height: 40px;
      border-radius: 30px;
      color: $color-text;
      background-color: $color-background-white;
      border: 1px solid #ddd;
    }
  }
}
</style>

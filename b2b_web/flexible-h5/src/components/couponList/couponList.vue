<template>
  <div class="coupon-list" v-if="this.userId">
    <v-header :back="true" :title="title"></v-header>
    <div class="container">
      <div class="pop-info">
        <van-tabs v-model="status" swipeable @change="handleTab">
          <van-tab
            :title="tab.title"
            :name="tab.id"
            v-for="tab in couponTabs"
            :key="tab.id"
          >
            <Coupon
              v-if="status === tab.id"
              :status="status"
              :page="page"
              :size="size"
              :totalPage="totalPage"
              :tabTitle="tab.title"
              :tabValue="tab.value"
              :couponList="couponList"
              @more="getMore"
              @item="handleItem"
              @refresh="refresh"
            ></Coupon>
          </van-tab>
        </van-tabs>
      </div>
    </div>

    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import Coupon from "base/coupon/coupon";
import Loading from "base/loading/loading";
import api from "common/js/allApi.js";
import { getLocalStorageItem } from "common/js/common";
export default {
  name: "couponList",
  data() {
    return {
      title: "优惠券",
      platform: "",
      distributorId: "",
      // 优惠券tab
      couponTabs: [
        {
          id: 1,
          title: "待领取",
          value: "立即领取",
        },
        {
          id: 3,
          title: "未使用",
          value: "未使用",
        },
        {
          id: 4,
          title: "已使用",
          value: "已使用",
        },
        {
          id: 5,
          title: "已过期",
          value: "已过期",
        },
      ],
      userId: "",
      page: 1, // 页码
      size: 5, // 页数
      status: 1, // 0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
      totalPage: 0, // 总数
      couponList: [], // 优惠列表
      isLoading: false,
      message: "",
    };
  },
  mounted() {
    // 判断是否是第三方进入
    let plat = this.$route.query.platform;
    let distrId = this.$route.query.distributorId;
    let platform = localStorage.getItem("platform");
    let distributorId = localStorage.getItem("distributorId");
    if (plat && distrId) {
      this.platform = plat;
      this.distributorId = distrId;
    } else {
      this.platform = platform;
      this.distributorId = distributorId;
    }
    localStorage.setItem("platform", this.platform);
    localStorage.setItem("distributorId", this.distributorId);
    localStorage.setItem("orderSource", this.platform);
    // 初始化优惠券
    this.userId = getLocalStorageItem("userId");
    if (this.userId) {
      // 已登录
      this.initData();
    } else {
      // 未登录
      this.$router.replace({
        path: "/login",
        query: {
          comingFlag: "couponList",
        },
      });
    }
  },
  methods: {
    initData() {
      // 获取优惠券列表
      this.getCouponList(this.size, this.status);
    },
    getMore(status) {
      this.page++;
      let size = this.size * this.page;
      this.getCouponList(size, status);
    },
    // 获取优惠券列表
    getCouponList(size, status) {
      this.message = "载入中";
      this.isLoading = true;

      // 优惠券状态：0 全部，1 待领取，2 已领取，3 未使用，4 已使用，5 已过期
      this.$api
        .get(this, api.getCouponList, {
          page: 1,
          size: size,
          statuss: status,
        })
        .then((res) => {
		  console.log(res);
          if (res.success) {
            this.totalPage = res.data.total;
            this.couponList = res.data.list;
            this.couponList.forEach((item) => {
              // item.isShow = false
              if (item.couponExplain !== "") {
                item.couponExplainArr = item.couponExplain
                  .trim()
                  .split(/[\r|\n]/);
              }
            });
          } else {
            this.$toast.fail(res.errMessage);
          }
          this.message = "";
          this.isLoading = false;
        });
    },
    // 切换tab
    handleTab(val, title) {
	  console.log("切换tab");
	  console.log("val = " + val);
	  console.log("title = " + title);
      this.couponList = [];
      this.status = val;
      this.getCouponList(this.size, this.status);
    },
    // 点击优惠券
    handleItem() {
      if (this.status === 1) {
        this.$router.push("/recommend");
      }
    },
    // 重新获取
    refresh(status) {
      this.getCouponList(this.size, status);
    },
    // 下拉展开收缩切换
    toggle(id) {
      this.couponList.map((item) => {
        if (item.id === id) {
          item.show = !item.show;
        }
      });
    },
  },
  components: {
    VHeader,
    Coupon,
    Loading,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';

.coupon-list {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  background-color: $color-background-white;

  .container {
    height: 100%;
    display: flex;
    flex-flow: column;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .pop-top {
      display: flex;
      padding: 0 20px;
      margin-bottom: 12px;

      .input {
        flex: 1;
        font-size: $font-size-medium;
        color: $color-text-l;
        background-color: $color-background-d;
        border-radius: 4px;

        >>>.van-field__control {
          text-align: center;
        }
      }

      .btn {
        display: inline-block;
        margin-left: 15px;
        width: 88px;
        height: 40px;
        line-height: 40px;
        font-size: $font-size-medium-x;
        text-align: center;
        color: $color-text-w;
        background-color: $color-theme;
        border-radius: 4px;
      }
    }

    .pop-info {
      flex: 1;
      overflow: hidden;

      >>>.van-tabs {
        display: flex;
        display: -webkit-flex;
        height: 100%;
        flex-flow: column;
        overflow: hidden;

        .van-tabs__wrap {
          display: block;
          height: 54px;
          line-height: 54px;

          &.van-hairline--top-bottom {
            &::after {
              border-width: 0;
              border: none;
            }
          }

          .van-tabs__line {
            bottom: 22px;
            width: 30px;
            height: 4px;
            background-color: $color-theme;
            border-radius: 4px;
          }

          .van-tab {
            .van-ellipsis {
              font-size: $font-size-medium;
              font-weight: bold;
              color: $color-text-d;
            }

            &.van-tab--active {
              font-size: 16px;
              color: $color-theme;
            }
          }
        }

        .van-tabs__content {
          flex: 1;
          padding: 10px 15px 0;
          box-sizing: border-box;
          overflow: hidden;

          .van-tab__pane {
            width: 100%;
            height: 100%;
            padding-bottom: 40px;
            overflow-y: scroll;
            -webkit-overflow-scrolling: touch;

            &::-webkit-scrollbar {
              display: none;
            }
          }
        }
      }
    }
  }
}
</style>

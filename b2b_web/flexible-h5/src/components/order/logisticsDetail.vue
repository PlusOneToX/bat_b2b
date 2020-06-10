<template>
  <div
    class="logisticsDetail"
    :class="{
      'ry-style': Number(distributorId) === 4378,
      'no-tabbar': pageFlag && pageFlag === 'orderQuery',
    }"
  >
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="container">
      <div class="item-box">
        <div class="tr-header">
          <i class="iconfont icon" :class="iconStatus(deliveryStatus)"></i>
          <span class="text">{{ getStatus(deliveryStatus) }}</span>
        </div>
        <div class="tr-info" v-show="orderDeliverBill">
          <div class="text">物流单号：{{ orderDeliverBill.logisticsNo }}</div>
          <div class="text" v-show="orderDeliverBill.distributionName">
            承运人：{{ orderDeliverBill.distributionName }}
          </div>
        </div>
      </div>
      <!----物流信息----->
      <div class="item-box" v-if="deliveryTraces.length > 0">
        <van-steps class="step" direction="vertical" :active="0">
          <van-step v-for="(item, idx) in deliveryTraces" :key="idx">
            <h3 class="title">{{ item.acceptTime | formatDate }}</h3>
            <p class="info">{{ item.acceptStation }}</p>
          </van-step>
        </van-steps>
      </div>
      <!----无物流信息----->
      <div class="item-box" v-else>
        <van-steps class="step none" direction="vertical" :active="0">
          <van-step v-show="false"> </van-step>
          <van-step>
            <h3 class="title">待收货</h3>
            <p>暂无物流信息</p>
          </van-step>
        </van-steps>
      </div>
    </div>
    <tabs v-show="showTabbar" :curVersion="curVersion" :userNo="userNo"></tabs>
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
import VHeader from "components/v-header/v-header";
import Tabs from "../../view/components/tabs.vue";
import Loading from "base/loading/loading";
import api from "common/js/allApi.js";
import { formatDate,getLocalStorageItem } from "common/js/common";
export default {
  name: "logisticsDetail",
  data() {
    return {
      title: "物流详情",
      orderId: "",
      orderDeliverBill: {},
      deliveryTraces: [],
      message: "",
      isLoading: false,
      curVersion: "",
      userNo: "",
      deliveryStatus: "",
      distributorId: "", // 分销商ID
      showTabbar: true, // 是否显示底部tab
      pageFlag: "", // 页面标识
    };
  },
  created() {
    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本
    this.userNo = getLocalStorageItem("userNo");
    this.message = "载入中";
    this.isLoading = true;

    this.distributorId = parseInt(localStorage.getItem("distributorId"));
    if (Number(this.distributorId) === 4378) {
      // 荣耀
      this.showTabbar = false; // 是否显示底部tab
    } else {
      this.showTabbar = true; // 是否显示底部tab
    }

    // 获取物流信息
    this.orderId = this.$route.query.id;
    this.getdeliveryTraces();

    this.pageFlag = this.$route.query.pageFlag;
    if (this.pageFlag && this.pageFlag === "orderQuery") {
      // 判断进入是否带有页面标识，有就不显示底部tabbar
      this.showTabbar = false;
    }
  },
  methods: {
    // 物流状态
    getStatus(val) {
      if (Number(val) === 2) {
        return "在途中";
      } else if (Number(val) === 3) {
        return "已签收";
      } else if (Number(val) === 4) {
        return "问题件";
      }
    },
    // 图标状态
    iconStatus(val) {
      switch (val) {
        // 在途
        case 2:
          return "icon-c-shipping";
        // 已签收
        case 3:
          return "icon-c-shipped";
      }
    },
    // 获取物流信息
    getdeliveryTraces() {
      this.$api
        .get(this, api.getOrderDetail, { id: this.orderId })
        .then((res) => {
          if (res.success) {
            if (res.data) {
              if (
                res.data.orderDeliverDetail &&
                res.data.orderDeliverDetail.length > 0
              ) {
                // 物流单号
                this.orderDeliverBill =
                  res.data.orderDeliverDetail[0].orderDeliverBill;
                this.deliveryStatus = this.orderDeliverBill.logisticsStatus;

                // 物流详情
                if (
                  res.data.orderDeliverDetail[0].orderDeliveryTrace &&
                  res.data.orderDeliverDetail[0].orderDeliveryTrace.length > 0
                ) {
                  this.deliveryTraces =
                    res.data.orderDeliverDetail[0].orderDeliveryTrace.sort(
                      this.dataDown
                    );
                }
              }
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
          this.message = "";
          this.isLoading = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 根据时间降序
    dataDown(x, y) {
      return new Date(y.acceptTime) - new Date(x.acceptTime);
    },
  },
  filters: {
    formatDate(time) {
      if (time) {
        let timeStr = time.replace(/-/g, "/");
        let date = new Date(timeStr);
        return formatDate(date, "yyyy-MM-dd hh:mm:ss");
      }
    },
  },
  components: {
    VHeader,
    Tabs,
    Loading,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.logisticsDetail {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 49px;
  background-color: $color-background;

  &.no-tabbar {
    bottom: 0;
  }

  .container {
    height: 100%;
    padding: 15px 15px 69px;
    box-sizing: border-box;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    padding-right: 20px;

    &::-webkit-scrollbar {
      display: none;
    }

    .item-box {
      display: inline-block;
      width: 100%;
      margin-bottom: 15px;
      padding: 15px;
      color: $color-text;
      box-sizing: border-box;
      border-radius: 8px;
      background-color: #ffffff;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      .tr-header {
        font-size: $font-size-medium-x;
        font-weight: bold;
        color: $color-text-d;

        .icon {
          display: inline-block;
          margin-right: 10px;
          font-size: $font-size-large;
          color: $color-text-d;
        }

        .text {
          font-size: $font-size-medium-x;
          font-weight: bold;
          color: $color-text-d;
        }
      }

      .tr-info {
        padding: 18px 0 4px;
        box-sizing: border-box;

        .icon {
          display: inline-block;
          width: 30px;
          height: 30px;
          margin-right: 14px;
          background-color: #999999;
          border-radius: 5px;
          vertical-align: middle;
        }

        .text {
          display: block;
          padding-left: 32px;
          font-size: $font-size-medium;
          color: $color-text-d;
          vertical-align: middle;

          &.text {
            margin-top: 5px;
          }
        }
      }

      .step {
        display: inline-block;

        >>>.van-step--vertical {
          .van-step__title {
            font-size: $font-size-small;

            .title {
              margin-bottom: 8px;
              font-size: $font-size-medium;
              color: $color-text-b !important;
            }

            .info {
              margin-top: 8px;
              color: $color-text-b !important;
            }

            .date {
              color: $color-text-d !important;
            }
          }

          .van-step__circle-container {
            .van-step__circle {
              display: inline-block;
              width: 10px;
              height: 10px;
            }

            .van-icon {
              font-size: $font-size-small;
              color: $color-text-b !important;
            }
          }
        }

        >>>.van-step--process {
          .van-step__title {
            color: $color-theme !important;

            .title, .info, .date {
              color: $color-theme !important;
            }
          }

          .van-step__circle-container {
            .van-icon {
              font-size: $font-size-large;
              color: $color-theme !important;
            }
          }
        }
      }
    }
  }
}
</style>

<style scoped lang="stylus" rel="stylesheet/stylus">
$color-bg = #F1F3F5;
$color-main = #256FFF;

// 荣耀
.ry-style {
  bottom: 0;
  background-color: $color-bg;

  .container {
    padding: 8px 16px;

    .item-box {
      margin-bottom: 8px;

      .tr-header {
        .icon {
          color: $color-main;
        }
      }

      .step {
        >>>.van-step--process {
          .van-step__title {
            color: $color-main !important;

            .title, .info, .date {
              color: $color-main !important;
            }
          }

          .van-step__circle-container {
            .van-icon {
              color: $color-main !important;
            }
          }
        }
      }
    }
  }
}
</style>
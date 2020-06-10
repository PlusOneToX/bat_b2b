<template>
  <div class="logisticsDetail" :class="{ hiddenHeader: isMiniProgram }">
    <THeader class="header-wrap" :title="'物流详情'" :hasBg="true"></THeader>
    <div class="container">
      <div class="item-box">
        <i class="sprite-icon" :class="iconStatus(deliveryStatus)"></i>
        <div class="tr-info" v-show="orderDeliverBill">
          <div class="tr-header">
            <span class="text">{{ getStatus(deliveryStatus) }}</span>
          </div>
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
    <Loading v-show="isLoading" :message="message"></Loading>
  </div>
</template>

<script>
// 组件
import THeader from "components/tHeader/tHeader";
import Loading from "components/loading/loading";
// js
import { formatDate } from "common/js/common";
// api
import api from "api/allApi.js";
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
      deliveryStatus: "",
      isMiniProgram: false, // 是否是小程序
    };
  },
  created() {
    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本
    this.message = "载入中";
    this.isLoading = true;
    // 获取物流信息
    this.orderId = this.$route.query.id;
    this.getdeliveryTraces();
  },
  methods: {
    // 物流状态
    getStatus(val) {
      if (Number(val) === 2) {
        return "在途中";
      } else if (Number(val) === 3) {
        return "已签收";
      } else {
        return "待收货";
      }
    },
    // 图标状态
    iconStatus(val) {
      if (Number(val) === 2) {
        return "icon-shipping";
      } else if (Number(val) === 3) {
        return "icon-shipped";
      } else {
        return "icon-shipping";
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
    // 降序
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
    THeader,
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
  top: 44px;
  bottom: 0;
  background-color: $color-bg;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .container {
    height: 100%;
    padding: $spacing-base;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .item-box {
      position: relative;
      display: inline-block;
      width: 100%;
      margin-bottom: 15px;
      padding: 15px;
      border-radius: 8px;
      background-color: $color-bg-white;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      .sprite-icon {
        position: absolute;
        top: 50%;
        transform: translateY(-50%);
      }

      .tr-header {
        font-size: $font-base;
        font-weight: bold;

        .text {
          font-size: $font-base;
          color: $color-font-base;
        }
      }

      .tr-info {
        .text {
          display: block;
          padding-left: 60px;
          font-size: $font-base;
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
            font-size: $font-sm;

            .title {
              margin-bottom: 8px;
              font-size: $font-base;
              color: $color-font-darkGrey !important;
            }

            .info {
              margin-top: 8px;
              color: $color-font-grey !important;
            }

            .date {
              color: $color-font-base !important;
            }
          }

          .van-step__circle-container {
            .van-step__circle {
              display: inline-block;
              width: 10px;
              height: 10px;
            }

            .van-icon {
              font-size: $font-base;
              color: $color-font-base !important;
            }
          }
        }

        >>>.van-step--process {
          .van-step__title {
            color: $color-orange !important;

            .title, .info, .date {
              color: $color-orange !important;
            }
          }

          .van-step__circle-container {
            .van-icon {
              font-size: $font-lg;
              color: $color-orange !important;
            }
          }
        }
      }
    }
  }
}
</style>

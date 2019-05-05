<template>
  <view class="voucher-wrap">
    <view class="voucher-top">
      <view class="total-wrap">
        <view class="total">
          <text class="label">总金额：</text>
          <text class="amount">¥{{ voucherAmount }}</text>
        </view>
      </view>
      <view class="tab-wrap">
        <view
          class="tab-item"
          v-for="tab in tabList"
          :key="tab.id"
          @click="handleTab(tab)"
        >
          <text :class="{ active: curTab === tab.id }"
            >{{ tab.value
            }}{{ tab.count > 0 ? "(" + tab.count + ")" : "" }}</text
          >
        </view>
      </view>
    </view>

    <!-- 代金券列表 -->
    <view class="voucher-list" v-if="voucherList && voucherList.length > 0">
      <view
        class="voucher-list-item"
        :class="{ disabled: curTab === 2 }"
        v-for="item in voucherList"
        :key="item.id"
      >
        <view class="item-t">
          <view class="item-t-l">
            <view class="value">面值: ¥{{ item.faceValue }}</view>
            <view class="time"
              >有效期: <text>{{ item.startTime | formatDate }}</text> 至
              <text>{{ item.endTime | formatDate }}</text></view
            >
            <view class="name">{{ item.name }}</view>
            <view class="bg"></view>
          </view>
          <view class="item-t-r">
            <view class="history" @click="handleHistory(item)">
              消费记录
              <image
                src="../../static/imgs/voucher_more.png"
                class="icon-more"
              ></image>
            </view>
            <view class="amount">¥{{ item.balance }}</view>
          </view>
        </view>
        <view class="item-b">
          <text class="no">{{ item.voucherNo }}</text>
          <text class="tips">仅可用于下单时抵扣</text>
          <!-- 立即使用 -->
          <text class="btn" v-if="curTab === 1" @click="goIndex">立即使用</text>
          <!-- 已用完 -->
          <text class="status" v-if="curTab === 2 && item.voucherStatus === 9"
            >已用完</text
          >
          <!-- 已过期 -->
          <text class="status" v-if="curTab === 2 && item.voucherStatus === 7"
            >已过期</text
          >
        </view>
      </view>
    </view>

    <view class="no-more">没有更多了~</view>

    <!-- 消费记录 -->
    <uni-popup ref="historyPopup" type="center" class="history-popup">
      <view class="history-popup-wrap">
        <view class="history-popup-title">
          <text>代金券消费记录</text>
          <image
            src="../../static/imgs/voucher_close.png"
            class="popup-close"
            @click="closeHistoryPopup"
          ></image>
        </view>
        <view class="voucher-no">
          <text>券号: {{ curVoucherNo }}</text>
          <image
            src="../../static/imgs/voucher_copy.png"
            class="popup-copy"
            @click="handleCopy(curVoucherNo)"
          ></image>
        </view>
        <view class="voucher-history-wrap">
          <view class="voucher-history-title">消费记录:</view>
          <view
            class="voucher-history-item"
            v-for="item in historyData"
            :key="item.id"
          >
            <view class="history-item-t">
              <text>订单: {{ item.orderNo }}</text>
              <image
                src="../../static/imgs/voucher_copy.png"
                class="popup-copy"
                @click="handleCopy(item.orderNo)"
              ></image>
              <text class="fr"
                >{{ item.amountFlag }}{{ item.useAmount.toFixed(2)
                }}{{ item.amountFlag === "+" ? "(退还)" : "" }}</text
              >
            </view>
            <view class="history-item-b">
              <text>{{ item.useTime }}</text>
              <text class="fr">余: {{ item.balance.toFixed(2) }}</text>
            </view>
          </view>
        </view>

        <view class="btn" @click="closeHistoryPopup">我知道了</view>
      </view>
    </uni-popup>

    <!-- 轻提示 -->
    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { formatDate } from "../../common/common.js";
import {
  voucherList,
  voucherAmount,
  getVoucherUsedList,
} from "../../common/api.js";
export default {
  data() {
    return {
      tipTextShow: false,
      tipText: "",
      tabList: [
        {
          id: 1,
          value: "可用",
          count: 0,
        },
        {
          id: 2,
          value: "不可用",
          count: 0,
        },
      ],
      curTab: 1, // 状态（可用 1，不可用 2）
      voucherStatus: "5", // 代金券状态
      cur_page: 1,
      pagesize: 10,
      totalCount: 0,
      voucherAmount: 0, // 代金券总额
      voucherList: [],
      // 消费记录
      curVoucherNo: "", // 券号
      historyDialogVisible: false, // 使用记录弹窗
      historyData: [],
      historyTotal: 0,
      historyInfo: {
        rebateVoucherId: "",
        page: 1,
        size: 20,
      },
    };
  },
  onReachBottom() {
    if (this.voucherList.length < this.totalCount) {
      this.cur_page += 1;
      this.getListData();
    }
  },
  onLoad() {
    this.getListData();
    this.getVoucherAmount(); // 代金券总额
  },
  filters: {
    formatDate(time) {
      var date = new Date(time);
      return formatDate(date, "yyyy-MM-dd");
    },
  },
  methods: {
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // 明细列表
    getListData() {
      let userId = uni.getStorageSync("userId");
      let parmas = {
        page: this.cur_page,
        size: this.pagesize,
        voucherStatusStr: this.voucherStatus,
        distributorId: userId,
      };
      voucherList(parmas).then((res) => {
        if (res.success) {
          this.totalCount = res.data.total;
          // 可用数量
          if (this.curTab === 1) {
            this.tabList[0].count = this.totalCount;
          }

          if (this.cur_page !== 1) {
            this.voucherList = [...that.voucherList, ...res.data.list];
          } else {
            this.voucherList = res.data.list;
          }
        }
      });
    },
    // 代金券总额
    getVoucherAmount() {
      let userId = uni.getStorageSync("userId");
      voucherAmount({
        distributorId: userId,
      }).then((res) => {
        if (res.success) {
          this.voucherAmount = res.data;
        }
      });
    },
    handleTab(item) {
      this.curTab = item.id;
      this.paginationShow = false;
      if (this.curTab === 1) {
        this.voucherStatus = "5";
      } else if (this.curTab === 2) {
        this.voucherStatus = "7,9";
      }
      this.cur_page = 1;
      this.getListData();
      this.$nextTick(function () {
        this.paginationShow = true;
      });
    },
    // 消费记录
    handleHistory(item) {
      this.$refs.historyPopup.open();
      this.curVoucherNo = item.voucherNo;
      this.historyInfo.rebateVoucherId = item.id;
      this.handleHistoryData(true);
    },
    handleHistoryData(type) {
      getVoucherUsedList(this.historyInfo).then((res) => {
        if (res.success) {
          if (res.data.list && res.data.list.length > 0) {
            let historyData = res.data.list;
            historyData.forEach((item) => {
              this.$set(item, "amountFlag", "+");
              if (item.useAmount < 0) {
                this.$set(item, "amountFlag", "-");
                this.$set(item, "useAmount", -item.useAmount);
              }
            });

            this.historyData = historyData;
            this.historyTotal = res.data.total;
            if (type) {
              // 点击使用记录按钮时，弹窗显示
              this.$refs.historyPopup.open();
            }
          } else {
            this.tipFun("暂无使用记录~");
          }
        }
      });
    },
    closeHistoryPopup() {
      this.$refs.historyPopup.close();
    },
    // 立即使用
    goIndex() {
      uni.switchTab({
        url: "/pages/index/index",
      });
    },
    // 单号-复制
    handleCopy(data) {
      uni.setClipboardData({
        data: data,
        success: function () {
        },
      });
    },
  },
};
</script>

<style lang="scss">
.voucher-wrap {
  .voucher-top {
    height: 276rpx;
    background-color: $uni-text-color-inverse;
  }

  .total-wrap {
    padding-top: 48rpx;
    padding-left: 44rpx;
    width: 100%;
    height: 176rpx;
    background: url("../../static/imgs/voucher-bg.png") no-repeat;
    background-size: 100% auto;
    box-sizing: border-box;
    .total {
      position: relative;
      padding-left: 3.8em;
      font-size: 28rpx;
      color: $voucher-color;
      .label {
        position: absolute;
        top: 50%;
        left: 0;
        transform: translateY(-50%);
      }
      .amount {
        font-size: 52rpx;
        font-style: italic;
      }
    }
  }

  .tab-wrap {
    display: flex;
    height: 100rpx;
    line-height: 100rpx;
    .tab-item {
      flex: 1;
      text-align: center;
      text {
        display: inline-block;
        padding: 0 10rpx;
        height: 100rpx;
        line-height: 100rpx;
        font-size: 32rpx;
        color: $uni-text-color-grey;
        border-bottom: 4rpx solid $uni-text-color-inverse;
        box-sizing: border-box;
        &.active {
          color: $uni-text-color;
          border-bottom: 4rpx solid #494949;
        }
      }
    }
  }

  .voucher-list {
    padding: 40rpx 30rpx 0;

    .voucher-list-item {
      background: $uni-text-color-inverse;
      box-shadow: 0 10rpx 16rpx 0 rgba(159, 159, 159, 0.33);
      border-radius: 16rpx;
      overflow: hidden;

      & + .voucher-list-item {
        margin-top: 30rpx;
      }

      .item-t {
        position: relative;
        .item-t-l {
          position: relative;
          padding: 18rpx 0 26rpx 20rpx;
          height: 204rpx;
          display: inline-block;
          font-size: 22rpx;
          color: $uni-text-color-inverse;
          line-height: 28rpx;
          background-color: $voucher-color;
          box-sizing: border-box;

          &::after {
            content: "";
            position: absolute;
            bottom: -6rpx;
            left: 0;
            width: calc(100% + 4rpx);
            height: 2rpx;
            background-color: $voucher-color;
          }
        }
        .bg {
          position: absolute;
          top: -28rpx;
          right: -42rpx;
          width: 100rpx;
          height: 220rpx;
          background-color: $voucher-color;
          transform: rotate(20deg);
          z-index: 1;

          &::after {
            content: "";
            position: absolute;
            top: 0;
            right: -6rpx;
            width: 2rpx;
            height: 224rpx;
            background-color: $voucher-color;
          }
        }
        .value {
          position: relative;
          z-index: 2;
        }
        .time {
          position: relative;
          margin-top: 20rpx;
          z-index: 2;
        }
        .name {
          position: relative;
          margin-top: 28rpx;
          font-size: 44rpx;
          color: rgba(255, 255, 255, 0.63);
          font-weight: 500;
          line-height: 60rpx;
          z-index: 2;
        }

        .item-t-r {
          position: absolute;
          top: 18rpx;
          right: 20rpx;
        }
        .history {
          position: relative;
          font-size: 24rpx;
          color: $voucher-color;
          padding-right: 24rpx;
          .icon-more {
            position: absolute;
            top: 50%;
            width: 24rpx;
            height: 34rpx;
            transform: translateY(-50%);
          }
        }
        .amount {
          margin-top: 54rpx;
          font-size: 40rpx;
          color: $voucher-color;
          font-style: italic;
        }
      }

      .item-b {
        position: relative;
        padding: 30rpx 30rpx 24rpx 20rpx;
        .no {
          padding: 2rpx 8rpx;
          font-size: 24rpx;
          color: $uni-text-color;
          line-height: 34rpx;
          border-radius: 4rpx;
          border: 1rpx solid rgba(248, 168, 93, 0.73);
        }
        .tips {
          margin-left: 12rpx;
          font-size: 20rpx;
          color: #898989;
        }
        .btn {
          position: absolute;
          top: 2rpx;
          right: 30rpx;
          padding: 6rpx 32rpx;
          font-size: 28rpx;
          color: $uni-text-color-inverse;
          background-color: $voucher-color;
          border-radius: 26rpx;
        }
        .status {
          position: absolute;
          right: -10px;
          bottom: -11px;
          width: 68px;
          height: 68px;
          font-size: 14px;
          color: #9c9c9c;
          padding-left: 11px;
          line-height: 68px;
          background-color: #e6e6e6;
          border-radius: 50%;
          transform: rotate(40deg);
          box-sizing: border-box;
        }
      }

      &.disabled {
        .item-t {
          .item-t-l {
            color: #b5b5b5;
            background-color: #e6e6e6;

            &::after {
              background-color: #e6e6e6;
            }
          }
          .bg {
            background-color: #e6e6e6;

            &::after {
              background-color: #e6e6e6;
            }
          }
          .name {
            color: #9c9c9c;
          }
          .amount {
            color: #898989;
          }
        }

        .item-b {
          .no {
            color: #9c9c9c;
            border: 1rpx solid rgba(223, 223, 223, 0.73);
          }
        }
      }
    }
  }

  .history-popup-wrap {
    margin: 0 auto;
    padding-bottom: 42rpx;
    width: 690rpx;
    max-height: 792rpx;
    background: $uni-text-color-inverse;
    border-radius: 16rpx;
    box-sizing: border-box;

    .history-popup-title {
      position: relative;
      padding: 30rpx 40rpx;
      font-size: 36rpx;
      color: $uni-text-color;
      text-align: center;

      .popup-close {
        position: absolute;
        top: 50%;
        right: 40rpx;
        width: 40rpx;
        height: 40rpx;
        transform: translateY(-50%);
      }
    }

    .voucher-no {
      position: relative;
      padding: 18rpx 50rpx 24rpx;
      font-size: 32rpx;
      color: $uni-text-color;

      &::after {
        content: "";
        position: absolute;
        right: 14rpx;
        bottom: 0;
        left: 14rpx;
        height: 2rpx;
        background-color: #f3f4f8;
      }
    }

    .popup-copy {
      position: relative;
      top: 10rpx;
      margin-left: 32rpx;
      width: 40rpx;
      height: 40rpx;
    }

    .voucher-history-wrap {
      padding: 26rpx 50rpx;
      max-height: 440rpx;
      font-size: 24rpx;
      line-height: 34rpx;
      overflow-y: scroll;
      box-sizing: border-box;
    }

    .voucher-history-item {
      padding: 26rpx 0;
      overflow: hidden;

      .fr {
        float: right;
      }

      .history-item-t {
        .fr {
          color: #f94021;
        }
      }

      .history-item-b {
        color: #b9b9b9;
      }
    }

    .btn {
      margin: 36rpx auto 0;
      width: 590rpx;
      height: 80rpx;
      line-height: 80rpx;
      font-size: 32rpx;
      color: $uni-text-color-inverse;
      text-align: center;
      background: #0076A5;
      border-radius: 80rpx;
    }
  }
}
</style>

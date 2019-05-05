<template>
  <view class="myAssets">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>我的资产</view>
      </view>
    </view>

    <view class="myAssets-top">
      <view class="myAssets-top-bg">
        <view>当前余额</view>
        <view>{{ currentBalance }}</view>
      </view>
    </view>

    <view class="myAssets-content">
      <view class="myAssets-content-title">
        <text>资产明细</text>
        <picker
          mode="date"
          fields="month"
          :value="startDate"
          @change="bindDateChange"
        >
          <image src="../../static/img/date_icon.png"></image>
        </picker>
      </view>
      <view class="myAssets-content-type">
        <text
          v-for="item in typeList"
          :key="item.id"
          :class="typeIndex == item.id ? 'myAssets-typeHover' : ''"
          @click="tabClick(item)"
          >{{ item.name }}
        </text>
      </view>
      <view class="myAssets-dateView">
        <text>{{ startDate }}</text>
        <text>总收入：</text>
        <text>{{
          totalAssets.totalRevenue ? "￥" + totalAssets.totalRevenue : "0"
        }}</text>
        <text>总支出：</text>
        <text>{{
          totalAssets.totalExpenditure
            ? "￥" + totalAssets.totalExpenditure
            : "0"
        }}</text>
      </view>
      <view class="myAssets-list">
        <view
          class="myAssets-list-line"
          v-for="item in detailsList"
          :key="item.id"
        >
          <view class="myAssets-list-lineLf">
            <image
              src="../../static/img/cz_icon.png"
              class="cz_iconImg"
              v-if="item.businessType == 1"
            ></image>
            <image
              src="../../static/img/tx_icon.png"
              class="tx_iconImg"
              v-if="item.businessType == 2"
            ></image>
            <image
              src="../../static/img/zf_icon.png"
              class="zf_iconImg"
              v-if="item.businessType == 3"
            ></image>
            <image
              src="../../static/img/tk_icon.png"
              class="tk_iconImg"
              v-if="item.businessType == 4"
            ></image>

            <view>
              <text v-if="item.businessType == 1">充值</text>
              <text v-if="item.businessType == 2">提现</text>
              <text v-if="item.businessType == 3">支付</text>
              <text v-if="item.businessType == 4">退款</text>
              <text>{{ item.createTime }}</text>
            </view>
          </view>
          <!--changeType 1.增加，2.减少 -->
          <view class="myAssets-list-lineRG">
            <text
              class="subtractColor"
              :class="item.changeType == 1 ? 'green' : 'red'"
              >{{ item.changeType == 1 ? "+" : "-" }}{{ item.amount }}</text
            ><!-- subtractColor -->
            <text>余额：{{ item.afterDepositAmount }}</text>
          </view>
        </view>
      </view>
    </view>
    <view class="no-more">没有更多了~</view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  userDeposit,
  depositDetailList,
  depositDetailSummary,
} from "../../common/api.js";
export default {
  data() {
    const currentDate = this.getDate({
      format: true,
    });
    const endDate = this.getDate("end");
    console.log(currentDate);
    return {
      typeList: [
        { id: 0, name: "全部" },
        { id: 1, name: "充值" },
        { id: 2, name: "提现" },
        { id: 3, name: "支付" },
        { id: 4, name: "退款" },
      ],
      typeIndex: 0,
      startDate: currentDate,
      endDate: endDate,
      currentBalance: 0,
      page: 1,
      size: 10,
      totalPage: 0,
      businessType: 0, //明细类型
      detailsList: [], //明细列表
      totalAssets: {}, //总资产
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {
    this.getDopositData();
    this.getDopsitDataDetailList();
    this.getAssets();
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 轻提示弹框
    tipFun(text) {
      let that = this;
      this.tipText = text;
      this.tipTextShow = true;
      setTimeout(function () {
        that.tipTextShow = false;
      }, 3000);
    },
    // tab选择
    tabClick(item) {
      this.typeIndex = item.id;
      this.getDopsitDataDetailList();
    },
    // 获取余额
    getDopositData() {
      let that = this;
      let id = uni.getStorageSync("userId");
      userDeposit({ id: id }).then((res) => {
        console.log(res.data);
        that.currentBalance = res.data.accountBalance;
      });
    },

    // 获取明细列表
    getDopsitDataDetailList() {
      let that = this;
      let id = uni.getStorageSync("userId");
      let businessType = "";
      if (this.typeIndex > 0) {
        businessType = this.typeIndex;
      }
      let parmas = {
        page: this.page,
        size: this.size,
        distributorId: id,
        businessType: businessType,
        startTime: this.startDate + "-01" + " " + " 00:00:00",
        endTime: this.endDate + "-01" + " " + "00:00:00",
      };
      depositDetailList(parmas).then((res) => {
        console.log("资产明细", res.data);
        if (res.success) {
          that.totalPageres = res.data.pages;
          that.detailsList = res.data.list;
        }
      });
    },

    // 按月查资产明细
    getAssets() {
      let that = this;
      let id = uni.getStorageSync("userId");
      let params = {
        distributorId: id,
        startTime: this.startDate + "-01" + " " + " 00:00:00",
        endTime: this.endDate + "-01" + " " + "00:00:00",
      };
      depositDetailSummary(params).then((res) => {
        if (res.success) {
          that.totalAssets = res.data;
        }
      });
    },

    // 跳转详情
    toDetailClick() {
      uni.navigateTo({
        url: "myDistributorsDetail?id=1",
      });
    },

    // 日历选择
    bindDateChange: function (e) {
      this.startDate = e.target.value;
      console.log(this.startDate.split("-")[1].startsWith("0"));
      let endTime =
        this.startDate.split("-")[0] +
        "-" +
        (Number(this.startDate.split("-")[1]) + 1);
      if (this.startDate.split("-")[1].startsWith("0")) {
        let num = Number(this.startDate.split("-")[1].substr(1)) + 1;
        if (num < 10) {
          num = "0" + num;
        }

        endTime = this.startDate.split("-")[0] + "-" + num;
      }
      this.endDate = endTime;
      this.getDopsitDataDetailList();
      this.getAssets();
    },

    getDate(type) {
      const date = new Date();
      let year = date.getFullYear();
      let month = date.getMonth() + 1;
      let day = date.getDate();

      if (type === "start") {
        month = month;
      } else if (type === "end") {
        month = month + 1;
      }
      month = month > 9 ? month : "0" + month;
      day = day > 9 ? day : "0" + day;
      // return `${year}-${month}-${day}`;
      return `${year}-${month}`;
    },
  },
};
</script>

<style lang="scss">
.myAssets {
  font-size: 26rpx;
  .myAssets-top {
    background: #fff;
    padding: calc(146rpx + var(--status-bar-height)) 30rpx 30rpx 30rpx;
    /* #ifdef H5 */
    padding: 134rpx 30rpx 30rpx 30rpx;
    /* #endif*/
    .myAssets-top-bg {
      width: 100%;
      height: 160rpx;
      background: url(../../static/img/myAssetsBg.png) no-repeat;
      background-size: 100% 100%;
      text-align: center;
      color: #0076A5;
      padding-top: 30rpx;
      view:nth-child(1) {
        font-size: 28rpx;
        padding-top: 30rpx;
      }
      view:nth-child(2) {
        font-size: 48rpx;
        font-weight: bold;
      }
    }
  }
  .myAssets-content {
    background: #fff;
    margin-top: 20rpx;
    .myAssets-content-title {
      font-size: 30rpx;
      font-weight: 400;
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 51rpx 30rpx 19rpx 30rpx;
      image {
        width: 39rpx;
        height: 34rpx;
      }
    }
    .myAssets-content-type {
      display: flex;
      align-items: center;
      justify-content: space-around;
      padding: 20rpx 0 10rpx;
      text {
        color: #999999;
        display: block;
        height: 55rpx;
        padding: 0 10rpx;
      }
      .myAssets-typeHover {
        color: $base-color1;
        font-size: 30rpx;
        border-bottom: 6rpx solid $base-color1;
      }
    }
    .myAssets-dateView {
      background: $opacity-border;
      padding: 30rpx;
      font-size: 24rpx;
      text:nth-child(1) {
        margin-right: 30rpx;
        font-size: 26rpx;
      }

      text:nth-child(3),
      text:nth-child(5) {
        color: $base-color2;
        font-size: 28rpx;
        margin-right: 30rpx;
      }
    }
    .myAssets-list {
      .myAssets-list-line {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 30rpx;
        border-bottom: 1rpx solid $opacity-border;
        .myAssets-list-lineLf {
          display: flex;
          align-items: center;
          view {
            margin-left: 20rpx;
            text {
              display: block;
            }
            text:nth-child(1) {
              font-size: 30rpx;
            }
            text:nth-child(2) {
              font-size: 24rpx;
              color: #999;
              margin-top: 10rpx;
            }
          }
        }
        .cz_iconImg {
          width: 36rpx;
          height: 28rpx;
        }
        .zf_iconImg {
          width: 33rpx;
          height: 38rpx;
        }
        .tk_iconImg {
          width: 32rpx;
          height: 33rpx;
        }
        .tx_iconImg {
          width: 35rpx;
          height: 28rpx;
        }
        .myAssets-list-lineRG {
          text-align: right;
          text {
            display: block;
          }
          text:nth-child(1) {
            font-size: 32rpx;
          }
          text:nth-child(2) {
            font-size: 24rpx;
            color: #999;
            margin-top: 5rpx;
          }
          .addColor {
            color: #50bc6c;
          }
          .subtractColor {
            &.green {
              color: #50bc6c;
            }

            &.red {
              color: #ed5307;
            }
          }
        }
      }
    }
  }
}
</style>

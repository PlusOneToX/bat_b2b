<template>
  <view class="distributorOrder">
    <!-- 顶部标题 -->
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toBack"></image>
        <view>我的分销订单</view>
      </view>
    </view>

    <!-- 顶部导航 -->
    <scroll-view scroll-x="true" class="order-topNav">
      <view class="top-nav">
        <text
          v-for="item in typeList"
          :key="item.id"
          :class="typeStatus == item.id ? 'topNav-hover' : ''"
          @click="orderStatusClick(item)"
        >
          {{ item.name }}
        </text>
      </view>
    </scroll-view>

    <!-- 列表 -->
    <scroll-view
      scroll-y="true"
      class="distributorOrder-list"
      @scrolltolower="pageScrollClick"
      v-if="orderList.length > 0"
    >
      <view class="order-list-view">
        <!-- 一模块 -->
        <view
          class="order-list-line"
          v-for="(item, index) in orderList"
          :key="index"
        >
          <view
            class="list-line-top"
            @click="
              toDetails(
                item.orderDistributorCost.orderId,
                item.orderDistributorData.distributorId
              )
            "
          >
            <view>
              <text>订单号 {{ item.orderInfo.orderNo }}</text>
              <text
                >下单账号：{{ item.orderDistributorData.distributorName }}</text
              >
            </view>
            <text class="distributorOrder-status">{{
              confirmOrderStatus(item.frontOrderStatus)
            }}</text>
          </view>
          <view
            class="list-line-moudle"
            v-for="(items, itemsIndex) in item.orderInfoDetailGoods"
            :key="itemsIndex"
          >
            <image
              :src="items.orderGoods.imageUrl"
              class="list-img"
              @click="toGoodDetails(items.orderGoods.goodsId)"
            ></image>
            <view
              class="line-moudle-center"
              @click="
                toDetails(
                  item.orderDistributorCost.orderId,
                  item.orderDistributorData.distributorId
                )
              "
            >
              <view class="moudle-center-name">
                {{ items.orderGoods.itemName }}
              </view>
              <view class="moudle-center-color">
                颜色：{{
                  items.orderGoods.colorName ? items.orderGoods.colorName : ""
                }}
              </view>
              <view
                class="moudle-center-color"
                v-if="items.orderGoods.specsName"
                >规格：{{ items.orderGoods.specsName }}</view
              >
            </view>
            <view
              class="list-line-btm"
              @click="
                toDetails(
                  item.orderDistributorCost.orderId,
                  item.orderDistributorData.distributorId
                )
              "
            >
              <view class="line-btm-price">
                <text>￥</text>
                <text>{{
                  items.orderGoodsDistributorCost
                    ? items.orderGoodsDistributorCost.actualPrice
                    : 0
                }}</text>
              </view>
              <view class="line-btm-num">
                x {{ items.orderGoods.itemCount }}
              </view>
            </view>
          </view>
          <view class="total-num" v-if="item.orderDistributorCost">
            共<text>{{ item.orderDistributorCost.totalCount }}</text
            >件 实付总额（含运费）：
            <text>￥{{ item.orderDistributorCost.payAmount }}</text>
          </view>
          <view
            class="list-btn"
            v-if="item.frontOrderStatus == 1"
            @click="auditFun(item.orderDistributorData.orderId)"
          >
            <text>立即审核</text>
          </view>
        </view>
      </view>
      <view class="no-more">没有更多啦~</view>
    </scroll-view>

    <view v-if="orderList.length == 0" class="order-noData-line">
      <image src="../../static/img/no-dataIcon.png"></image>
      <view>暂无数据</view>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import { orderMbnextList, orderChecktOrder } from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  data() {
    return {
      isSearch: false,
      typeList: [
        { name: "全部", id: 0 },
        { name: "待审核", id: 1 },
        { name: "已同意", id: 2 },
        { name: "已拒绝", id: 3 },
      ],
      typeStatus: 0,
      orderList: [], //列表数据
      page: 1,
      size: 5,
      totalPage: 0,
      tipTextShow: false,
      tipText: "",
    };
  },
  onLoad() {
    this.orderMbListFun(1);
  },
  methods: {
    // 返回个人中心
    toBack() {
      uni.switchTab({
        url: "/pages/personalCenter/personalCenter",
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
    // 订单状态--Y
    confirmOrderStatus(row) {
      //  前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货（已发货） 5已关闭 6已完成 7 待付款 9 出库中
      switch (row) {
        case 1:
          return "待审核"; //1.待审核
        case 2:
          return "已同意"; //已同意
        case 3:
          return "已同意"; //已拒绝
        case 4:
          return "已同意"; //已拒绝
        case 5:
          return "已关闭"; //已同意
        case 6:
          return "已同意"; //已同意
        case 7:
          return "待审核"; //已同意
        case 9:
          return "出库中"; //出库中
      }
    },
    //获取列表数据
    orderMbListFun(page) {
      let that = this;
      if (page == 1) {
        this.orderList = [];
      }
      let params = {
        deliverStatus: 0, //发货状态
        orderStatus: this.typeStatus, //前端订单状态 0.全部 1.待审核 2已同意 3已拒绝 4已拒绝 5已同意
        distributorId: uni.getStorageSync("userId"),
        page: page,
        size: this.size,
      };
      orderMbnextList(params).then((res) => {
        if (res.success) {
          res.data.list.forEach((item) => {
            if (item.orderDistributorCost) {
              let count = 0;
              item.orderInfoDetailGoods.forEach((items) => {
                count += items.orderGoods.itemCount;
              });
              that.$set(item.orderDistributorCost, "totalCount", count);
            }
          });

          if (that.page != 1) {
            that.orderList = [...that.orderList, ...res.data.list];
          } else {
            that.orderList = res.data.list;
          }
          console.log("d订单列表：", that.orderList);
          that.totalPage = res.data.pages;
        }
      });
    },

    // 选择订单状态
    orderStatusClick(item) {
      this.typeStatus = item.id;
      this.page = 1;
      this.orderMbListFun(1);
    },

    // 上拉分页
    pageScrollClick() {
      if (this.page < this.totalPage) {
        this.page += 1;
        this.orderMbListFun(this.page);
      }
    },

    // 跳转订单详情页
    toDetails(id, distributorId) {
      uni.navigateTo({
        url:
          "myDistributorsOrderDetails?id=" +
          id +
          "&distributorId=" +
          distributorId,
      });
    },
    // 跳转商品详情
    toGoodDetails(id) {
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + id,
      });
    },
    // 立即审核
    auditFun(id) {
      let that = this;
      uni.showModal({
        title: "审核",
        content: "是否审核通过？",
        cancelText: "拒绝", // 取消按钮的文字
        confirmText: "通过", // 确认按钮文字
        showCancel: true, // 是否显示取消按钮，默认为 true
        confirmColor: "#09D5EE",
        cancelColor: "#999999",
        success: (res) => {
          if (res.confirm) {
            // 通过
            orderChecktOrder({ ids: [id], orderStatus: 2 }).then((res) => {
              if (res.success) {
                that.tipFun("审核通过！");
                that.orderMbListFun(1);
              } else {
                that.tipFun(res.errMessage);
              }
            });
          } else if (res.cancel) {
            // 拒绝
            orderChecktOrder({ ids: [id], orderStatus: 3 }).then((res) => {
              if (res.success) {
                that.tipFun("审核拒绝！");
                that.orderMbListFun(1);
              } else {
                that.tipFun(res.errMessage);
              }
            });
          }
        },
      });
    },
  },
};
</script>

<style lang="scss">
.distributorOrder {
  color: #333333;
  .top-title {
    padding: 0 30rpx;
    display: flex;
    align-items: center;
    background: #fff;

    .search-view {
      display: flex;
      align-items: center;
      // justify-content: center;
      margin-left: 30rpx;
      border-bottom: 2rpx solid $base-color1;
      width: 450rpx;
      input {
        width: 400rpx;
        padding: 10rpx;
        font-size: 26rpx;
        margin-left: 20rpx;
      }
    }
    image {
      width: 55rpx;
      height: 55rpx;
    }
    .top-textTitle {
      width: 650rpx;
      text-align: center;
      color: #333333;
      font-size: 32rpx;
      font-weight: 400;
    }
  }
  .order-topNav {
    padding-top: calc(116rpx + var(--status-bar-height));
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
  }
  .top-nav {
    background: #fff;
    border-top: 1px solid #f2f3f8;
    width: 100%;
    display: flex;
    justify-content: space-around;
    text {
      display: block;
      padding: 0 30rpx;
      color: #999999;
      font-size: 24rpx;
      font-weight: 300;
      height: 97rpx;
      line-height: 97rpx;
    }
    .topNav-hover {
      position: relative;
      color: $base-color1;
      font-size: 30rpx;
      &::after {
        content: "";
        position: absolute;
        bottom: 0;
        left: 50%;
        width: 100rpx;
        height: 6rpx;
        background: $base-color1;
        border-radius: 6rpx;
        transform: translateX(-50%);
      }
    }
  }

  .distributorOrder-list {
    height: (100vh-16);
    padding-top: 0rpx;
    /* #ifdef H5 */
    padding-top: 0;
    /* #endif*/
    .order-list-view {
      .order-list-line {
        background: #fff;
        margin-top: 20rpx;
        padding-bottom: 40rpx;
        .list-line-top {
          // height: 83rpx;
          // line-height:83rpx;
          padding: 15rpx 30rpx;
          display: flex;
          justify-content: space-between;
          align-items: center;
          font-size: 26rpx;
          border-bottom: 1rpx solid $opacity-border;
          view {
            text {
              display: block;
            }
            text:nth-child(2) {
              margin-top: 5rpx;
            }
          }
          .distributorOrder-status {
            color: $base-color1;
          }
        }
        .list-line-moudle {
          display: flex;
          padding: 0 30rpx;
          margin-top: 40rpx;
          .list-img {
            width: 160rpx;
            height: 160rpx;
            border-radius: 10rpx;
            overflow: hidden;
          }
          .line-moudle-center {
            width: 380rpx;
            margin-left: 20rpx;
            .moudle-center-name {
              font-size: 26rpx;
              font-weight: bold;
              margin-bottom: 20rpx;
              text-overflow: -o-ellipsis-lastline;
              overflow: hidden;
              text-overflow: ellipsis;
              display: -webkit-box;
              -webkit-line-clamp: 2;
              line-clamp: 2;
              -webkit-box-orient: vertical;
            }
            .moudle-center-color {
              font-size: 22rpx;
              color: #999999;
              margin-top: 5rpx;
            }
          }
          .list-line-btm {
            margin-left: 20rpx;
            text-align: right;
            .line-btm-price {
              text:nth-child(1),
              text:nth-child(3) {
                font-size: 20rpx;
              }
              text:nth-child(2) {
                font-size: 30rpx;
                font-weight: bold;
              }
            }
            .line-btm-num {
              color: #999999;
              font-size: 22rpx;
              margin-top: 10rpx;
            }
          }
        }
        .total-num {
          font-size: 26rpx;
          font-weight: bold;
          margin-top: 40rpx;
          text-align: right;
          padding: 0 30rpx;
          text {
            color: $base-color2;
            padding: 0 15rpx;
          }
        }
        .list-btn {
          display: flex;
          justify-content: flex-end;
          padding: 20rpx 30rpx 0 30rpx;
          text {
            width: 200rpx;
            height: 70rpx;
            line-height: 70rpx;
            background: $bg-gradient-btn;
            border-radius: 40rpx;
            color: #fff;
            text-align: center;
            font-size: 28rpx;
          }
        }
      }
    }
  }
  .order-noData-line {
    image {
      width: 311rpx;
      height: 300rpx;
    }
    padding-top: 200rpx;
    font-size: 30rpx;
    color: #666;
    text-align: center;
    font-weight: 400rpx;
    view {
      margin-top: 47rpx;
    }
  }
}
</style>

<template>
  <view class="myParcel">
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toback"></image>
        <view>我的包裹</view>
      </view>
    </view>
    <view class="myParcel-tip">
      <image src="../../static/img/notice_icon.png"></image>
      <view>当前订单已拆分成{{ orderDeliverDetail.length }}个包裹发货</view>
    </view>
    <!-- 包裹1 -->
    <view
      class="myParcel-list"
      v-for="(item, index) in orderDeliverDetail"
      :key="index"
    >
      <view class="myParcel-list-tip" @click="toLogisticsInform(item)">
        <text class="myParcel-list-tipLf">包裹{{ index + 1 }}</text>
        <view class="myParcel-list-tipRg">
          <text v-if="item.orderDeliverBill.logisticsStatus == 2">运输中</text>
          <text v-if="item.orderDeliverBill.logisticsStatus == 3">签收</text>
          <text v-if="item.orderDeliverBill.logisticsStatus == 4">问题件</text>
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <view class="myParcel-list-wuliuInform">
        <view
          ><text>物流公司：</text
          ><text>{{ item.orderDeliverBill.distributionName }}</text></view
        >
        <view
          ><text>物流单号：</text
          ><text>{{ item.orderDeliverBill.logisticsNo }}</text></view
        >
        <view
          ><text>最新进度：</text
          ><text
            class="jinduInform"
            v-if="item.orderDeliveryTrace && item.orderDeliveryTrace.length > 0"
            >{{ item.orderDeliveryTrace[0].acceptStation }}</text
          ></view
        >
      </view>

      <!-- list -->
      <view class="myParcel-listGood">
        <!-- 大类 -->
        <template v-for="(gItem, gIndex) in item.orderDeliverBillDetails">
          <view class="myParcel-listGood-big" :key="gIndex" v-if="gIndex <= 3">
            <image :src="gItem.orderGoods.imageUrl"></image>
            <view class="listGood-big-rg">
              <view class="listGood-name">{{ gItem.orderGoods.itemName }}</view>
              <view class="listGood-big-rgLine">
                <view>商品编号：{{ gItem.orderGoods.itemCode }}</view>
                <view class="big-rgLine-price">
                  <text>￥</text>{{ gItem.orderGoodsDistributorCost.salePrice }}
                </view>
              </view>
              <view class="listGood-big-rgLine">
                <view>
                  <text v-if="gItem.orderGoods.colorName">
                    颜色：{{ gItem.orderGoods.colorName }}
                  </text>
                  <text v-if="gItem.orderGoods.specsName">
                    规格：{{ gItem.orderGoods.specsName }}
                  </text>
                  <!-- <text>￥100/件</text> -->
                </view>
                <view>
                  发货数量：{{
                    gItem.orderGoods.deliverCount
                      ? gItem.orderGoods.deliverCount
                      : 0
                  }}
                </view>
              </view>
            </view>
          </view>
          <view
            class="myParcel-listGood-big"
            :key="gIndex"
            v-if="gIndex > 3 && showMore"
          >
            <image :src="gItem.orderGoods.imageUrl"></image>
            <view class="listGood-big-rg">
              <view class="listGood-name">{{ gItem.orderGoods.itemName }}</view>
              <view class="listGood-big-rgLine">
                <view>商品编号：{{ gItem.orderGoods.itemCode }}</view>
                <view class="big-rgLine-price">
                  <text>￥</text>{{ gItem.orderGoodsDistributorCost.salePrice }}
                </view>
              </view>
              <view class="listGood-big-rgLine">
                <view>
                  <text v-if="gItem.orderGoods.colorName">
                    颜色：{{ gItem.orderGoods.colorName }}
                  </text>
                  <text v-if="gItem.orderGoods.specsName">
                    规格：{{ gItem.orderGoods.specsName }}
                  </text>
                  <!-- <text>￥100/件</text> -->
                </view>
                <view>
                  发货数量：{{
                    gItem.orderGoods.deliverCount
                      ? gItem.orderGoods.deliverCount
                      : 0
                  }}
                </view>
              </view>
            </view>
          </view>
        </template>
      </view>
      
			<view class="myParcel-showMore" v-if="item.orderDeliverBillDetails.length > 3" @click="dowmOrUpFun">
				<text>{{ dowmOrUp }}</text>
				<text class="iconfont icon-Packup" v-if="!showMore"></text>
				<text class="iconfont icon-an" v-if="showMore"></text>
			</view>
    </view>

    <view class="no-more">没有更多啦~</view>
  </view>
</template>

<script>
import { orderMbDetail, orderDeliverBillDetail } from "../../common/api.js";
export default {
  data() {
    return {
      dowmOrUp: "展开",
      showMore: false, //展开or收起
      orderDeliverDetail: [], //物流
    };
  },
  onShow(option) {},
  onLoad(option) {
    this.orderMbDetailFun(option.id);
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 订单详情
    orderMbDetailFun(id) {
      let that = this;
      let params = {
        distributorId: uni.getStorageSync("userId"),
        orderId: id,
      };
      orderDeliverBillDetail(params).then((res) => {
        if (res.success) {
          that.orderDeliverDetail = res.data;
        }
      });
    },

    // 跳转物流轨迹页
    toLogisticsInform(item) {
      uni.setStorageSync("orderDeliveryTrace", JSON.stringify(item));
      uni.navigateTo({
        url: "logisticsInform",
      });
    },
    // 展开--收起
    dowmOrUpFun() {
      if (this.showMore) {
        this.dowmOrUp = "展开";
      } else {
        this.dowmOrUp = "收起";
      }

      this.showMore = !this.showMore;
    },
  },
};
</script>

<style lang="scss">
.myParcel {
  font-size: 26rpx;
  .top-bg {
    background: #fff;
    .top-title {
      display: flex;
      align-items: center;
      padding: 0 30rpx;
      background: #fff;
      image {
        width: 18rpx;
        height: 30rpx;
      }
      view {
        font-size: 32rpx;
        font-weight: 400;
        width: 650rpx;
        height: 80rpx;
        line-height: 80rpx;
        text-align: center;
      }
      /* #ifdef H5 */
      view {
        height: 88rpx;
        line-height: 88rpx;
      }
      /*#endif*/
    }
  }
  .myParcel-tip {
    font-size: 24rpx;
    color: #fff;
    background: $base-color1;
    height: 80rpx;
    line-height: 80rpx;
    padding: calc(116rpx + var(--status-bar-height)) 30rpx 0;
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    display: flex;
    align-items: center;
    image {
      width: 32rpx;
      height: 31rpx;
      margin-right: 20rpx;
    }
  }
  .myParcel-list {
    background: #fff;
		& + .myParcel-list {
			margin-top: 20rpx;
		}
    .myParcel-list-tip {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      border-bottom: 1px solid $opacity-border;
      .myParcel-list-tipRg {
        display: flex;
        align-items: center;
        color: $base-color1;
        text {
          font-size: 26rpx;
          margin-right: 10rpx;
        }
        .toIcon {
					width: 24rpx;
					height: 24rpx;
        }
      }
    }
    .myParcel-list-wuliuInform {
      padding-bottom: 30rpx;
      border-bottom: 1rpx solid $opacity-border;
      view {
        display: flex;
        align-items: top;
        padding: 30rpx 30rpx 0 30rpx;
        text:nth-child(1) {
          font-size: 24rpx;
          color: #666;
          width: 120rpx;
        }
        text:nth-child(2) {
          font-size: 26rpx;
          color: #333;
          margin-left: 30rpx;
          width: 500rpx;
        }
      }
    }
    .myParcel-listGood {
      .myParcel-listGood-big {
        display: flex;
        margin: 0 30rpx;
        padding: 30rpx 0;
				& + .myParcel-listGood-big {
        	border-top: 1rpx solid #f1f2f9;
				}
        image {
          width: 160rpx;
          height: 160rpx;
          border-radius: 10rpx;
          overflow: hidden;
        }
        .listGood-name {
          text-overflow: -o-ellipsis-lastline;
          overflow: hidden;
          text-overflow: ellipsis;
          display: -webkit-box;
          -webkit-line-clamp: 2;
          line-clamp: 2;
          -webkit-box-orient: vertical;
        }
        .listGood-big-rg {
          margin-left: 25rpx;
          width: 500rpx;
          .listGood-big-rgLine {
            color: #999999;
            font-size: 22rpx;
            margin-top: 20rpx;
            display: flex;
            justify-content: space-between;
            line-height: 30rpx;
            & + .listGood-big-rgLine {
              margin-top: 0;
            }
            view:nth-child(1) {
              text {
                display: block;
                margin-top: 3rpx;
              }
            }
            .big-rgLine-price {
              color: #ed5307;
              font-size: 34rpx;
              text {
                font-size: 24rpx;
              }
            }
          }
        }
      }
      .myParcel-listGood-small {
        padding: 30rpx;
        display: flex;
        image {
          width: 111rpx;
          height: 111rpx;
        }
        .listGood-small-center {
          margin-left: 25rpx;
          color: #999999;
          font-size: 22rpx;
          width: 350rpx;
          view:nth-child(2),
          view:nth-child(3) {
            margin-top: 8rpx;
          }
        }
        .listGood-small-rg {
          text-align: right;
          width: 200rpx;
          view:nth-child(1) {
            text:nth-child(1) {
              font-size: 22rpx;
            }
            text:nth-child(2) {
              font-size: 32rpx;
            }
          }
          view:nth-child(2),
          view:nth-child(3) {
            margin-top: 8rpx;
            color: #999;
            font-size: 22rpx;
          }
        }
      }
    }
  }
  .myParcel-showMore {
    color: $base-color1;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 30rpx 0;
    text {
      margin-right: 10rpx;
    }
    .iconfont {
      position: relative;
      top: 3rpx;
      font-size: 22rpx !important;
    }
  }
}
</style>

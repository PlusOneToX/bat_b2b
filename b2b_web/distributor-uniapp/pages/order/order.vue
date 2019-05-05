<template>
  <view class="order">
    <!-- 顶部标题 -->
    <view class="top-moudle">
      <view class="status_bar"> <!-- 这里是状态栏 --></view>
      <view class="top-title">
        <image src="../../static/img/back_icon.png" @click="toBack"></image>
        <view>我的订单</view>
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
          >{{ item.name }}
        </text>
      </view>
    </scroll-view>

    <!-- 列表 -->
    <!-- <scroll-view scroll-y="true" class="order-list" @scrolltolower="pageScrollClick" v-if="orderList.length>0"> -->
    <div class="order-list" v-if="orderList.length > 0">
      <view class="order-list-view">
        <!-- 一模块 -->
        <template v-for="(item, itemIndex) in orderList">
          <view
            class="order-list-line"
            :key="itemIndex"
            v-if="item.orderDistributorCost.payAmount != 0"
          >
            <view
              class="list-line-top"
              @click="toDetails(item.orderDistributorCost.orderId)"
            >
              <!-- <text>订单号 {{item.orderInfo.orderNo}}</text> -->
              <text>下单时间： {{ item.orderInfo.createTime }}</text>
              <text>{{ confirmOrderStatus(item.frontOrderStatus) }}</text>
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
                @click="toDetails(item.orderDistributorCost.orderId)"
              >
                <view class="moudle-center-name">{{
                  items.orderGoods.itemName
                }}</view>
                <view
                  class="moudle-center-color"
                  v-if="
                    items.orderGoods.colorName &&
                    items.orderGoods.colorName != ''
                  "
                  >颜色：{{
                    items.orderGoods.colorName ? items.orderGoods.colorName : ""
                  }}
                </view>
                <view
                  class="moudle-center-color"
                  v-if="
                    items.orderGoods.specsName &&
                    items.orderGoods.specsName != ''
                  "
                  >规格：{{ items.orderGoods.specsName }}
                </view>
                <view
                  class="moudle-center-color"
                  v-if="items.orderGoodsDiy && items.orderGoodsDiy.materialName"
                  >材质：{{ items.orderGoodsDiy.materialName }}
                </view>
                <view
                  class="moudle-center-color"
                  v-if="items.orderGoodsDiy && items.orderGoodsDiy.modelName"
                  >机型：{{ items.orderGoodsDiy.modelName }}
                </view>
              </view>
              <view
                class="list-line-btm"
                @click="toDetails(item.orderDistributorCost.orderId)"
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
            <view class="total-num">
              共<text>{{ item.orderDistributorCost.totalCount }}</text
              >件 实付总额（含运费）：
              <text>￥{{ item.orderDistributorCost.payAmount }}</text>
            </view>
            <view
              class="list-btn"
              v-if="item.frontOrderStatus == 7"
              @click="
                payFun(
                  item.orderDistributorData.payWay,
                  item.orderDistributorData.orderId
                )
              "
            >
              <text>立即支付</text>
            </view>
          </view>
        </template>
      </view>
      <!-- <view class="no-more">没有更多啦~</view> -->
      <!-- </scroll-view> -->
    </div>

    <view v-if="orderList.length == 0" class="order-noData-line">
      <image src="../../static/img/no-dataIcon.png"></image>
      <view>暂无数据</view>
    </view>

    <view class="tipText" v-show="tipTextShow">{{ tipText }}</view>
  </view>
</template>

<script>
import {
  orderMbList,
  payCreateTrade,
  programMiniOpenId,
} from "../../common/api.js";
import { isH5, isMpWeixin } from "../../common/common.js";
export default {
  data() {
    return {
      isSearch: false,
      typeList: [
        { name: "全部", id: 0 },
        { name: "待付款", id: 7 },
        { name: "待确认", id: 1 },
        { name: "待发货", id: 2 },
        { name: "出库中", id: 9 },
        { name: "部分发货", id: 3 },
        { name: "已发货", id: 4 },
        { name: "已关闭", id: 5 },
        { name: "已完成", id: 6 },
      ],
      typeStatus: 0,
      orderList: [], //列表数据
      page: 1,
      size: 10,
      totalPage: 0,
      nextPage: "",
      tipTextShow: false,
      tipText: "",
    };
  },
  onReachBottom() {
    if (this.orderList.length < this.totalPage) {
      this.page += 1;
      this.orderMbListFun(this.page);
    }
  },
  onLoad(option) {
    if (option.typeStatus && option.typeStatus != "undefined") {
      this.typeStatus = option.typeStatus;
      this.orderMbListFun(option.typeStatus);
    } else {
      this.orderMbListFun(1);
    }
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
          return "待确认"; //待确认
        case 2:
          return "待发货"; //待发货
        case 9:
          return "出库中"; //出库中
        case 3:
          return "部分发货"; //部分发货
        case 4:
          return "已发货"; //已发货
        case 5:
          return "已关闭"; //已关闭
        case 6:
          return "已完成"; //已完成
        case 7:
          return "待付款"; //待付款
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
        frontOrderStatus: this.typeStatus, //前端订单状态 0.全部 1.待确认 2待发货 3部分发货 4待收货 5已关闭 6已完成 7 待付款
        distributorId: uni.getStorageSync("userId"),
        page: page,
        size: this.size,
      };
      orderMbList(params).then((res) => {
        if (res.success) {
          res.data.list.forEach((item) => {
            let count = 0;

            if (item.orderDistributorCost == undefined) {
              that.$set(item, "orderDistributorCost", {});
              that.$set(item.orderDistributorCost, "payAmount", 0);
            }

            item.orderInfoDetailGoods.forEach((items) => {
              count += items.orderGoods.itemCount;
            });

            that.$set(item.orderDistributorCost, "totalCount", count);
          });

          if (that.page != 1) {
            that.orderList = [...that.orderList, ...res.data.list];
          } else {
            that.orderList = res.data.list;
          }
          that.totalPage = res.data.total;
        }
      });
    },

    // 获取微信小程序登录授权
    programMiniLoginFun() {
      uni.login({
        provider: "weixin",
        success: (res) => {
          // console.log('登录成功：', res);
          //获取临时登录凭证code
          let Code = res.code;
          let params = {
            appId: uni.getStorageSync("appId"),
            code: res.code,
          };
          programMiniOpenId(params).then((res) => {
            console.log("微信获取openid：", res);
            if (res.success) {
              uni.setStorageSync("openId", res.data); //是否开启分销模式 0 不开启, 1 开启
            }
          });
        },
        fail: (err) => {
          console.log("登录失败：", err);
        },
      });
    },

    // 立即支付
    payFun(PayWay, ids) {
      let payMethod = "";
      if (PayWay == 1) {
        payMethod = "alipay_wap";
      } else if (PayWay == 2) {
        payMethod = isH5 ? "wxpay_h5" : "wxpay_mini_program";
      } else if (PayWay == 5) {
        payMethod = "balance";
      } else if (PayWay == 6) {
        payMethod = "kuaiqian_merchant";
      }
      if (
        !isH5 &&
        (uni.getStorageSync("openId") == null ||
          uni.getStorageSync("openId") == "undefined" ||
          uni.getStorageSync("openId") == "")
      ) {
        this.tipFun("获取openId失败，请重新点击支付");
        this.programMiniLoginFun();
      }
      let payParams = {
        customerFlag: 0,
        orderId: ids,
        payMethod: payMethod,
        payerId: uni.getStorageSync("userId"),
        payerName: uni.getStorageSync("userName"),
        quitUrl:
          PayWay == 1
            ? "https://www.bat.com/h5/#/pages/shoppingCart/checkstandSuccess?orderId=" +
              ids[0] +
              "&payMethod=" +
              payMethod
            : "",
        redirectUrl:
          PayWay == 1 || PayWay == 2
            ? "https://www.bat.com/h5/#/pages/shoppingCart/checkstandSuccess?orderId=" +
              ids[0] +
              "&payMethod=" +
              payMethod
            : "",
        appId: !isH5 ? uni.getStorageSync("appId") : "",
        platformUserId: !isH5 ? uni.getStorageSync("openId") : "",
      };
      console.log(payParams);

      //           let payParams={
      // customerFlag:0,
      // orderId:ids,
      // payMethod:payMethod,
      // payerId:uni.getStorageSync('userId'),
      // payerName:uni.getStorageSync('userName'),
      //           }
      payCreateTrade(payParams)
        .then((payRes) => {
          let that = this;
          if (payRes.success) {
            if (PayWay == 6) {
              //快钱支付
              window.location.href = payRes.data.kuaiQianReap.h5Url; // 跳转链接
            } else if (PayWay == 2) {
              //微信
              if (isH5) {
                let url = payRes.data.wxResp.h5Url;
                console.log(url)
                window.location.href = url;
              } else {
                let wxResp = payRes.data.wxResp;
                uni.requestPayment({
                  provider: "wxpay",
                  timeStamp: wxResp.timeStamp,
                  nonceStr: wxResp.nonceStr,
                  package: wxResp.prepayId,
                  signType: wxResp.signType,
                  paySign: wxResp.paySign,
                  success: function (res) {
                    console.log("success:" + JSON.stringify(res));
                    uni.switchTab({
                      url: "/pages/shoppingCart/checkstandSuccess2",
                    });
                  },
                  fail: function (err) {
                    console.log("fail:" + JSON.stringify(err));

                    that.tipFun("支付失败");
                    uni.switchTab({
                      url: "/pages/shoppingCart/shoppingCart",
                    });
                  },
                });
              }
            } else if (PayWay == 1) {
              //支付宝
              let resData = payRes.data.alipayResp.from;
              const div = document.createElement("div");
              div.id = "alipay";
              div.innerHTML = resData;
              document.body.appendChild(div);
              document.querySelector("#alipay").children[0].submit(); // 执行后会唤起支付宝
            }
          } else {
            that.tipFun(payRes.errMessage);
          }
        })
        .catch((err) => {
          that.tipFun("系统异常");
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

    // 跳转详情页
    toDetails(id) {
      uni.navigateTo({
        url: "orderDetails?id=" + id,
      });
    },
    // 跳转商品详情
    toGoodDetails(id) {
      uni.navigateTo({
        url: "/pages/classify/goodsDetails?id=" + id,
      });
    },
  },
};
</script>

<style lang="scss">
.order {
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
    padding: calc(116rpx + var(--status-bar-height)) 30rpx 0;
    // #ifdef H5
    padding-top: 104rpx;
    // #endif
    background: #fff;
    border-top: 1px solid #f2f3f8;
    box-sizing: border-box;
  }
  .uni-scroll-view {
    padding: 0 30rpx;
  }
  .top-nav {
    display: flex;

    text {
      display: block;
      padding: 0 30rpx;
      color: #999999;
      font-size: 24rpx;
      font-weight: 300;
      white-space: nowrap;
      height: 97rpx;
      line-height: 97rpx;
    }
    .topNav-hover {
      position: relative;
      color: #0076A5;
      font-size: 30rpx;
      &::after {
        content: "";
        position: absolute;
        bottom: 0;
        left: 50%;
        width: 100rpx;
        height: 6rpx;
        background: #0076A5;
        border-radius: 6rpx;
        transform: translateX(-50%);
      }
    }
  }

  .order-list {
    height: (100vh-16);
    .order-list-view {
      .order-list-line {
        background: #fff;
        margin-top: 20rpx;
        padding-bottom: 40rpx;
        .list-line-top {
          height: 83rpx;
          line-height: 83rpx;
          padding: 0 30rpx;
          display: flex;
          justify-content: space-between;
          font-size: 26rpx;
          border-bottom: 1rpx solid $opacity-border;
          text:nth-child(2) {
            color: #0076A5;
          }
        }
        .list-line-moudle {
          position: relative;
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
            position: absolute;
            right: 30rpx;
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
          padding: 24rpx 30rpx 0 30rpx;
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

<template>
  <!-- :style="{ background: themeColor }"  -->
  <view class="personalCenter">
    <view class="personalCenter-newTop" style="background: linear-gradient(359deg, #0dc0f4 0%, #0dc0f4 30%, #168bdc 100%);">
      <view class="personalCenter-newTop-info">
        <image src="../../static/imgs/index_login.png" class="headerIcon"></image>
        <view v-if="userId == ''">
          <text @click="toClick('/pages/login/login')">立即登录</text>
        </view>
        <view v-else>{{ userName }}</view>
      </view>
    </view>
    <!-- 我的订单 -->
    <view class="personalCenter-order">
      <view class="personalCenter-orderAll" @click="toOrderFun()">
        <view>我的订单</view>
        <view
          ><text>全部订单</text
          ><image src="../../static/imgs/personalMore.png"></image
        ></view>
      </view>

      <view class="personalCenter-order-status">
        <view @click="toOrderFun(7)" class="personalCenter-order-line">
          <image
            src="../../static/imgs/daiPay_icon.png" 
            class="daiPay_icon"
          ></image>
          <view class="personal-tab-lineText">待付款</view>   
        </view>
        <view @click="toOrderFun(2)" class="personalCenter-order-line">
          <image
            src="../../static/imgs/fahuo.png"
            class="queren_icon"
          ></image>
          <view class="personal-tab-lineText">待发货</view>
        </view>
        <view @click="toOrderFun(4)" class="personalCenter-order-line">
          <image
            src="../../static/imgs/daishouhuo.png" 
            class="queren_icon"
          ></image>
          <view class="personal-tab-lineText">待收货</view>
        </view>
        <view @click="toOrderFun(6)" class="personalCenter-order-line">
          <image
            src="../../static/imgs/finish.png"
            class="queren_icon"
          ></image>
          <view class="personal-tab-lineText">已完成</view>
        </view>
        <view @click="toOrderFun(5)" class="personalCenter-order-line">
          <image
            src="../../static/imgs/close.png"
            class="queren_icon"
          ></image>
          <view class="personal-tab-lineText">已关闭</view>
        </view>
      </view>
    </view>

    <!-- tab -->

    <view class="personal-List">
      <view
        class="personal-List-line"
        @click="toClick('/pages/personalCenter/messageCenter')"
        v-if="isLogin == 1"
      >
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/newsImg.png"></image>
          <text>消息通知</text>
        </view>
        <view class="personal-List-lineRg">
          <text style="color: red" v-if="newNum >= 1">{{ newNum }}</text>
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <view
        class="personal-List-line"
        @click="toClick('myQrCode')"
        v-if="isLogin == 1 && isOpenCode"
      >
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/TwoCode_icon.png"></image>
          <text>二维码</text>
        </view>
        <view class="personal-List-lineRg">
          <text>专属二维码</text>
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <!-- &&distributionFlag==1 -->
      <view
        class="personal-List-line"
        v-if="isLogin == 1 && distributionFlag == 1"
        @click="toClick('../myDistributors/myDistributors')"
      >
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/fenxiao_icon.png"></image>
          <text>下级分销商</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <view
        class="personal-List-line"
        v-if="isLogin == 1 && distributionFlag == 1"
        @click="toClick('../myDistributors/myDistributorsOrder')"
      >
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/fenxiao_order_icon.png"></image>
          <text>分销订单</text>
        </view>
        <view class="personal-List-lineRg">
          <text style="color: red" v-if="mbNextCount >= 1">{{
            mbNextCount
          }}</text>
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <!-- <view class="personal-List-line" @click="toClick('myAssets')">
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/zichang_icon.png"></image>
          <text>我的资产</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <view class="personal-List-line" @click="toClick('voucher')">
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/voucher_icon.png"></image>
          <text>我的代金券</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view> -->
      <view class="personal-List-line" @click="toClick('myCollection')">
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/collection_icon.png"></image>
          <text>收藏商品</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon" 
          ></image>
        </view>
      </view>
      <view class="personal-List-line" @click="handleWechat" v-if="showWechat">
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/chat_icon.png"></image>
          <text>客服电话</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
      <view class="personal-List-line" @click="toClick('/pages/set/set')">
        <view class="personal-List-lineLf">
          <image src="../../static/imgs/set_icon.png"></image>
          <text>设置</text>
        </view>
        <view class="personal-List-lineRg">
          <image
            src="../../static/imgs/personalMore.png"
            class="toIcon"
          ></image>
        </view>
      </view>
    </view>

    <!-- 底部导航 -->
    <!-- <tabBar :themeColor="themeColor" :pageIndex="5"></tabBar> -->
  </view>
</template>

<script>
import {
  distributorQrcode,
  orderMbCount,
  msgcenterNotRead,
  orderMbNextCount,
  getWechatInfo,
} from "../../common/api.js";
import tabBar from "../../components/myComponents/tabBar.vue";
export default {
  components: { tabBar },
  data() {
    return {
      themeColor: "",
      isLogin: "",
      userName: "",
      isOpenCode: false,
      distributionFlag: 0,
      userId: "", //分销商id
      daiFuCount: 0, //待付款
      daiQueRenCount: 0, //待确认
      daiFaCount: 0, //待发货
      bufenCount: 0, //部分发货
      yifahuoCount: 0, //已发货
      newNum: 0, //消息未读数目
      tmplIds: [], // 订阅消息模板ID
      mbNextCount: 0, // 待审核数量
      tipTextShow: false,
      tipText: "",
      showWechat: false, // 显示联系客服
    };
  },
  onLoad() {
    this.themeColor = uni.getStorageSync("themeColor");
  },
  onShow() {
    this.userId = "";
    let userId = uni.getStorageSync("userId");
    if (userId && userId != "" && userId != "undefined") {
      this.userId = userId;
      this.getQrcode();
      this.orderMbCountFun();
      this.msgcenterNotReadFun(); // 查询当前未读的消息的数量
      this.orderMbNextCountFun(); // 获取下级分销订单总数 - 待审核数量
    }
    this.isLogin = uni.getStorageSync("loginStatus");
    this.userName = uni.getStorageSync("userName");
    this.distributionFlag = uni.getStorageSync("distributionFlag"); //用户是否开启分销模式

    // #ifdef H5
    this.showWechat = true;
    // #endif

    // #ifdef MP-WEIXIN
    // 订阅消息模板ID
    const accountInfo = uni.getAccountInfoSync();
    let appId = accountInfo.miniProgram.appId;
    let tmplIds = []; // 订阅消息模板ID
    // 分销商审核通知，新订单提醒
    if (appId === "wx1f43eac1f6d7a750") {
      tmplIds = [
        "mqsm3IP8QWVRupl8FLB1gQY_-5bBQC2D9b5oWr7Y_80",
        "aVO1VIDYGPV18YYEwg-9oql3JXaQyJhzXvyu9omGXTs",
      ];

      this.showWechat = true;
    }
    this.tmplIds = tmplIds;
    // #endif
  },
  methods: {
    //返回
    toback() {
      uni.navigateBack({
        delta: 1,
      });
    },
    // 查询当前未读的消息的数量
    msgcenterNotReadFun() {
      msgcenterNotRead().then((res) => {
        this.newNum = res.data;
      });
    },
    // 获取下级分销订单总数 - 待审核数量
    orderMbNextCountFun() {
      orderMbNextCount({
        deliverStatus: 0, //发货状态
        orderStatus: 1, //前端订单状态 0.全部 1.待审核 2已同意 3已拒绝 4已拒绝 5已同意
        distributorId: uni.getStorageSync("userId"),
      }).then((res) => {
        this.mbNextCount = res.data;
      });
    },
    // 跳转订单
    toOrderFun(typeStatus) {
      // #ifdef MP-WEIXIN
      // 订阅消息模板ID
      const accountInfo = uni.getAccountInfoSync();
      let appId = accountInfo.miniProgram.appId;
      let tmplIds = []; // 订阅消息模板ID
      // 订单状态通知，订单发货通知，订单未支付提醒
      if (appId === "wx1f43eac1f6d7a750") {
        tmplIds = [
          "zOED8tpPB7HkERlsuuzEo2A_u1TuAxVOmsN5VJJlwgA",
          "zD0Lz627YMAxQw8shmpjMr0yqBBmphJyjuLPWO07TWo",
          "x5dkQhLWO2nUrfC38MMcsyOd0QifJ8Eyex1ki1E-ajE",
        ];
      }
      uni.requestSubscribeMessage({
        tmplIds: tmplIds,
        success(res) {
          if (typeStatus) {
            uni.navigateTo({
              url: "/pages/order/order?typeStatus=" + typeStatus,
            });
          } else {
            uni.navigateTo({
              url: "/pages/order/order",
            });
          }
        },
        fail() {
          if (typeStatus) {
            uni.navigateTo({
              url: "/pages/order/order?typeStatus=" + typeStatus,
            });
          } else {
            uni.navigateTo({
              url: "/pages/order/order",
            });
          }
        },
        fail() {
          if (typeStatus) {
            uni.navigateTo({
              url: "/pages/order/order?typeStatus=" + typeStatus,
            });
          } else {
            uni.navigateTo({
              url: "/pages/order/order",
            });
          }
        },
      });
      // #endif

      // #ifdef H5
      if (typeStatus) {
        uni.navigateTo({
          url: "/pages/order/order?typeStatus=" + typeStatus,
        });
      } else {
        uni.navigateTo({
          url: "/pages/order/order",
        });
      }
      // #endif
    },
    // tab选择
    tabClick(item) {
      this.tabIndex = item.id;
    },
    // 跳转
    toClick(name) {
      let that = this;
      if (
        name == "../myDistributors/myDistributors" ||
        name == "../myDistributors/myDistributorsOrder"
      ) {
        // #ifdef MP-WEIXIN
        uni.requestSubscribeMessage({
          tmplIds: that.tmplIds,
          success(res) {
            uni.navigateTo({
              url: name,
            });
          },
          fail() {
            uni.navigateTo({
              url: name,
            });
          },
        });
        // #endif

        // #ifdef H5
        uni.navigateTo({
          url: name,
        });
        // #endif
      } else {
        uni.navigateTo({
          url: name,
        });
      }
    },
    // 获取二维码
    getQrcode() {
      let that = this;
      let id = uni.getStorageSync("userId");
      distributorQrcode({ distributorId: id }).then((res) => {
        if (res.success) {
          that.isOpenCode = true;
        } else {
          that.isOpenCode = false;
        }
      });
    },
    //获取列表数据
    orderMbCountFun(page) {
      let userId = uni.getStorageSync("userId");
      orderMbCount({ distributorId: userId, frontOrderStatus: 0 }).then(
        (res) => {
          if (res.success) {
            res.data.forEach((item) => {
              if (item.frontOrderStatus == 7) {
                this.daiFuCount = item.count; //待付款
              } else if (item.frontOrderStatus == 1) {
                this.daiQueRenCount = item.count; //待确认
              } else if (item.frontOrderStatus == 2) {
                this.daiFaCount = item.count; //待发货
              } else if (item.frontOrderStatus == 3) {
                this.bufenCount = item.count; //部分发货
              } else if (item.frontOrderStatus == 4) {
                this.yifahuoCount = item.count; //已发货
              }
            });
          }
        }
      );
    },
    // 联系客服
    handleWechat() {
      let _this = this;
      let url = ""; // 客服链接
      let corpId = ""; // 企业id
	  console.log("0755-21011282");
	  uni.showModal({
	  	content:"客服电话：0755-21011282",
	  	showCancel:false
	  })
      // getWechatInfo("?key=enterprise_id&key=b2b_customer_service_url").then(
      //   (res) => {
      //     if (res.success && res.data.length > 0) {
      //       res.data.forEach((item) => {
      //         if (item.key === "enterprise_id") {
      //           corpId = item.value;
      //         }
      //         if (item.key === "b2b_customer_service_url") {
      //           url = item.value;
      //         }
      //       });
      //       // #ifdef MP-WEIXIN
      //       wx.openCustomerServiceChat({
      //         extInfo: {
      //           url: url,
      //         },
      //         corpId: corpId,
      //         success(res) {
      //           console.log(res);
      //         },
      //         fail(err) {
      //           _this.tipFun(err);
      //         },
      //       });
      //       // #endif

      //       // #ifdef H5
      //       window.location.href = url;
      //       // #endif
      //     } else {
      //       _this.tipFun(res.errMessage);
      //     }
      //   }
      // );
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
  },
};
</script>

<style lang="scss">
.personalCenter {
  overflow-x: hidden;
  font-size: 26rpx;
  font-family: PingFangSC-Regular, PingFang SC;

  .personalCenter-newTop {
    position: relative;
    width: 750rpx;
    height: 424rpx;
    z-index: 1;
    .personalCenter-newTop-info {
      position: absolute;
      z-index: 999;
      font-size: 34rpx;
      font-family: PingFangSC-Medium, PingFang SC;
      font-weight: 500;
      color: #ffffff;
      top: 104rpx;
      left: 50%;
      transform: translateX(-50%);
      display: flex;
      justify-content: center;
      flex-direction: column;
      align-items: center;
      image {
        width: 130rpx;
        height: 130rpx;
        border-radius: 50%;
      }
      view {
        text-align: center;
        margin-top: 12rpx;
        text:nth-child(2) {
          margin: 0 10rpx;
        }
      }
    }
  }
  // 订单
  .personalCenter-order {
    background: #fff;
    width: calc(100% - 60rpx);
    border-radius: 8rpx;
    position: relative;
    top: -88rpx;
    left: 30rpx;
    z-index: 99;
    .personalCenter-orderAll {
      border-bottom: 1rpx solid #f3f4f8;
      padding: 28rpx;
      display: flex;
      align-items: center;
      justify-content: space-between;
      view:nth-child(2) {
        image {
          width: 24rpx;
          height: 24rpx;
        }
      }
    }
    .personalCenter-order-status {
      display: flex;
      align-items: center;
      padding: 24rpx 14rpx;
      .personalCenter-order-line {
        flex: 1;
        text-align: center;
        font-size: 28rpx;
        image { 
          width: 110rpx;
          height: 110rpx;
        }
      }
    }
  }
  // tab
  .personal-List {
    position: relative;
    top: -68rpx;
    width: calc(100% - 60rpx);
    margin-left: 30rpx;
    border-radius: 8rpx;
    background: #fff;
    font-size: 28rpx;
    .personal-List-line {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 30rpx;
      .personal-List-lineLf {
        display: flex;
        align-items: center;
        font-size: 28rpx;
        color: #555;
        image {
          width: 48rpx;
          height: 48rpx;
          margin-right: 15rpx;
        }
      }
      .personal-List-lineRg {
        display: flex;
        align-items: center;
        text {
          margin-right: 15rpx;
        }
        image {
          width: 24rpx;
          height: 24rpx;
        }
      }
    }
  }
}
</style>

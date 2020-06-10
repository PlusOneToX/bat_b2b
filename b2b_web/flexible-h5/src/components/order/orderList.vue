<template>
  <div
    class="orderList"
    :class="{
      'ry-style': Number(distributorId) === 4378,
      'no-tabbar': pageFlag && pageFlag === 'orderQuery',
    }"
  >
    <v-header
      :back="pageFlag && pageFlag === 'orderQuery' ? false : true"
      :title="title"
      @back="toback"
    ></v-header>
    <div class="container">
      <van-tabs v-model="active" @click="handleClickv">
        <van-tab
          v-for="(tab, k) in orderTabs"
          :key="k"
          :title="tab.name"
          :name="tab.id"
        >
          <van-pull-refresh v-model="isLoading" @refresh="onRefresh">
            <div class="order-list" v-show="orderList.length > 0">
              <div
                class="order-item"
                :class="
                  tab.id === 5 || order.orderCustomerData.frontOrderStatus === 5
                    ? 'disable'
                    : ''
                "
                v-for="(order, index) in orderList"
                :key="index"
                @click="orderDetail(order.orderInfo.id)"
              >
                <div class="order-top">
                  <div
                    v-if="
                      order.orderCustomerData &&
                      order.orderCustomerData.shopName
                    "
                  >
                    {{ order.orderCustomerData.shopName }}
                  </div>
                  <div class="order-no">
                    订单编号：{{ order.orderInfo.orderNo }}
                  </div>
                  <div
                    class="order-tag"
                    :class="{
                      'is-shop':
                        order.orderCustomerData &&
                        order.orderCustomerData.shopName,
                    }"
                  >
                    {{ orderStatus(order.orderCustomerData.frontOrderStatus) }}
                  </div>
                </div>
                <div
                  class="order-con"
                  v-for="(item, index) in order.orderInfoDetailGoods"
                  :key="index"
                >
                  <div class="order-img">
                    <img
                      v-lazy="
                        item.orderGoodsDiy && item.orderGoodsDiy.previewImage
                          ? item.orderGoodsDiy.previewImage
                          : ''
                      "
                      alt=""
                      class="img"
                    />
                  </div>
                  <div class="order-info">
                    <div class="tr-box">
                      <span class="name">{{ item.orderGoods.goodsName }}</span>
                      <span class="price" v-show="showPrice"
                        >¥{{
                          item.orderGoodsCustomerCost.salePrice | fomatFloat
                        }}</span
                      >
                    </div>
                    <div class="spec">
                      <div class="text">
                        <div>
                          规格：{{
                            item.orderGoodsDiy && item.orderGoodsDiy.modelName
                              ? item.orderGoodsDiy.modelName
                              : ""
                          }}
                          -
                          {{
                            item.orderGoodsDiy &&
                            item.orderGoodsDiy.materialName
                              ? item.orderGoodsDiy.materialName
                              : ""
                          }}
                        </div>
                        <!----金科------>
                        <div v-show="distributorId === 1217">
                          图片ID：{{
                            item.orderGoodsDiy && item.orderGoodsDiy.pictureId
                              ? item.orderGoodsDiy.pictureId
                              : ""
                          }}
                        </div>
                      </div>
                      <span class="count"
                        >X{{
                          item.orderGoods && item.orderGoods.itemCount
                            ? item.orderGoods.itemCount
                            : 0
                        }}</span
                      >
                    </div>
                  </div>
                </div>
                <div class="order-pay">
                  <div class="price-count">
                    <span class="text" v-if="showPrice"
                      >共{{ order.totalCount }}件商品，合计<em>¥</em
                      ><strong>{{
                        order.orderCustomerCost.payAmount | fomatFloat
                      }}</strong></span
                    >
                    <span class="text" v-else
                      >共{{ order.totalCount }}件商品</span
                    >
                  </div>
                  <div
                    class="order-btn"
                    v-show="order.orderCustomerData.frontOrderStatus === 1"
                  >
                    立即支付 {{ order.dayTime }}
                  </div>
                </div>
              </div>
              <div
                class="load-more"
                v-show="orderList.length < totalPage"
                @click="handleMore"
              >
                点击加载更多
              </div>
              <div class="load-more" v-show="orderList.length >= totalPage">
                没有更多了
              </div>
            </div>
          </van-pull-refresh>
          <!--无订单-->
          <div class="order-list none" v-show="orderList.length <= 0">
            <div class="img"></div>
            <div class="text">暂无订单</div>
            <div class="btn btn-submit" @click="handleSubmit">去定制</div>
          </div>
        </van-tab>
      </van-tabs>
    </div>
    <tabs
      v-show="showTabbar"
      :curTab="'mine'"
      :curVersion="curVersion"
      :userNo="userNo"
    ></tabs>
  </div>
</template>
<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
import Loading from "base/loading/loading";
import api from "common/js/allApi.js";
import { fomatFloat, countDown,getLocalStorageItem } from "common/js/common";
import Tabs from "../../view/components/tabs.vue";

export default {
  data() {
    return {
      title: "我的订单",
      userId: 0, // 用户ID
      phone: "", // 用户手机号
      distributorId: "", // 分销商
      orderTabs: [], // 订单状态
      // 所有订单状态
      orderTabsAll: [
        {
          id: 0,
          name: "全部",
        },
        {
          id: 1,
          name: "待付款",
        },
        {
          id: 2,
          name: "待发货",
        },
        {
          id: 4,
          name: "待收货",
        },
        {
          id: 6,
          name: "已完成",
        },
        {
          id: 5,
          name: "已关闭",
        },
      ],
      // 荣耀订单状态
      orderTabsRy: [
        {
          id: 0,
          name: "全部",
        },
        {
          id: 2,
          name: "待发货",
        },
        {
          id: 4,
          name: "待收货",
        },
        {
          id: 6,
          name: "已完成",
        },
      ],
      orderList: [], // 订单列表
      active: 0, // 默认订单状态
      page: 1, // 数量倍数
      size: 5, // 页显示数量
      totalPage: 0, // 订单总数
      temp: null, // 倒计时
      isLoading: true, // 下拉刷新加载
      msg: "",
      message: "", // loading提示
      curVersion: "",
      userNo: "",
      showPrice: true, // 是否显示价格
      showTabbar: true, // 是否显示底部tab
      orderTime: 0, // 订单失效时间
      pageFlag: "", // 页面标识
    };
  },
  created() {
    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本
    this.userNo = getLocalStorageItem("userNo");
    // 获取用户信息
    this.userId = getLocalStorageItem("userId");
    this.phone = getLocalStorageItem("phone");
    this.distributorId = parseInt(localStorage.getItem("distributorId"));

    if (Number(this.distributorId) === 4378) {
      // 荣耀
      this.orderTabs = this.orderTabsRy; // 订单状态
      this.showPrice = false; // 是否显示价格
      this.showTabbar = false; // 是否显示底部tab
    } else {
      this.orderTabs = this.orderTabsAll; // 订单状态
      this.showPrice = true; // 是否显示价格
      this.showTabbar = true; // 是否显示底部tab
    }

    // 状态ID
    let sid = Number(this.$route.query.sid);
    if (sid) {
      this.active = sid;
    }

    // 获取全部订单
    this.getOrderList(this.size);
    // 获取订单失效时间
    this.getOrderTime();

    this.pageFlag = this.$route.query.pageFlag;
    if (this.pageFlag && this.pageFlag === "orderQuery") {
      // 判断进入是否带有页面标识，有就不显示底部tabbar
      this.showTabbar = false;
    }
  },
  methods: {
    // 加载更多
    handleMore() {
      this.page++;
      let size = this.page * this.size;
      this.getOrderList(size);
    },
    // 跳转定制页面
    handleSubmit() {
      this.$router.push("/phone");
    },
    // 获取订单列表
    getOrderList(size) {
      // 获取订单列表
      this.$api
        .get(this, api.getOrderList, {
          frontOrderStatus: this.active,
          customerId: this.userId,
          page: 1,
          size: size,
        })
        .then((res) => {
          if (res.success) {
            this.orderList = res.data.list;
            this.totalPage = res.data.total;

            this.orderList.forEach((order) => {
              let totalCount = 0;
              order.orderInfoDetailGoods.forEach((item) => {
                totalCount += item.orderGoods.itemCount;
              });
              this.$set(order, "totalCount", totalCount);
            });

            if (this.orderList.length > 0) {
              this.timer();
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 订单状态
    orderStatus(val) {
      switch (val) {
        case 1:
          return "待付款";
        case 2:
          return "待发货";
        case 4:
          return "待收货";
        case 5:
          return "已关闭";
        case 6:
          return "已完成";
      }
    },
    // 跳转订单详情
    orderDetail(id) {
      this.$router.push({
        path: "/orderDetail",
        query: { id: id, pageFlag: this.pageFlag },
      });
    },
    // 切换订单状态
    handleClickv(val, title) {
      this.page = 1;
      this.active = val;
      this.getOrderList(this.size);
    },
    // 倒计时
    timer() {
      let vm = this;
      if (this.temp !== null) {
        return;
      }
      this.temp = setInterval(() => {
        this.orderList.forEach((item, index) => {
          // 待支付
          if (item.orderCustomerData.frontOrderStatus === 1) {
            let timeStr = item.orderCustomerCost.createTime.replace(/-/g, "/");
            let date = new Date(timeStr).getTime();
            let time = countDown(date + this.orderTime * 60 * 1000);
            if (time === "") {
              vm.$set(item, "dayTime", "");
              vm.destroyed();
            } else {
              vm.$set(item, "dayTime", time);
              vm.$set(
                this.orderList,
                item.dayTime,
                countDown(date + this.orderTime * 60 * 1000)
              );
            }
          }
        });
      }, 1000);
    },
    // 获取订单失效时间
    getOrderTime() {
      this.$api.get(this, api.getOrderTime).then((res) => {
        if (res.success) {
          this.orderTime = res.data;
        }
      });
    },
    destroyed() {
      clearInterval(this.temp);
      this.temp = null;
    },
    // 倒序
    dataDown(x, y) {
      return y.createTime - x.createTime;
    },
    // 下拉刷新
    onRefresh() {
      setTimeout(() => {
        this.getOrderList(this.size);
        this.$toast("刷新成功");
        this.isLoading = false;
      }, 1000);
    },
    toback() {
      if (Number(this.distributorId) === 4378) {
        // 荣耀
        this.$router.push("/index");
      } else {
        this.$router.push("/mine");
      }
    },
  },
  filters: {
    fomatFloat(num, n) {
      return fomatFloat(num, 2);
    },
  },
  components: {
    VHeader,
    Loading,
    Tabs,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.orderList {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 56px;
  font-size: $font-size-small;
  background-color: $color-background;

  &.no-tabbar {
    bottom: 0;
  }

  .container {
    height: 100%;

    >>>.van-tabs {
      height: 100%;

      .van-tabs__wrap {
        padding: 0;
        height: 54px;

        .van-tabs__nav {
          display: flex;
          height: 54px;
        }

        .van-tab {
          display: inline-block;
          flex: 1;
          padding: 0;
          height: 54px;
          line-height: 54px;
          text-align: center;
          font-size: 13px;
          color: #4A4A4A;

          &:nth-child(2) {
            padding-left: 0;
          }

          &.van-tab--active, &:hover {
            font-size: 15px;
            color: #0076A5;
          }
        }

        .van-tabs__line {
          bottom: 22px;
          width: 30px;
          height: 4px;
          background-color: #0076A5;
          border-radius: 4px;
        }

        &::after {
          background-color: transparent;
        }
      }

      .van-tabs__content {
        height: 100%;
        box-sizing: border-box;
        padding-bottom: 100px;
        overflow-y: scroll;
        -webkit-overflow-scrolling: touch;
        box-sizing: border-box;

        &::-webkit-scrollbar {
          display: none;
        }

        .order-list {
          display: inline-block;
          width: 100%;
          padding: 0 15px;
          box-sizing: border-box;

          .order-item {
            position: relative;
            padding: 24px 15px 15px 20px;
            margin-top: 8px;
            box-sizing: border-box;
            border-radius: 8px;
            color: #4A4A4A;
            background-color: #ffffff;
            box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

            .order-top {
              position: relative;
              padding-right: 80px;

              .shop-name {
                margin-bottom: 8px;
                height: 20px;
                font-size: 16px;
                color: #333333;
                white-space: nowrap;
                text-overflow: ellipsis;
                overflow: hidden;
              }

              .order-no {
                display: inline-block;
                font-size: 14px;
                color: #737373;
              }

              .order-tag {
                position: absolute;
                top: 0;
                right: 0;
                font-size: 14px;
                color: #D0021B;

                &.is-shop {
                  top: 2px;
                  color: #333333;
                }
              }
            }

            .order-con {
              display: flex;
              display: -webkit-flex;
              padding: 10px 0;

              .order-img {
                position: relative;
                display: inline-block;
                width: 90px;
                height: 90px;
                margin-right: 10px;
                border-radius: 10px;
                overflow: hidden;

                .img {
                  position: absolute;
                  top: 50%;
                  left: 50%;
                  max-width: 100%;
                  max-height: 100%;
                  transform: translate(-50%, -50%);
                }
              }

              .order-info {
                position: relative;
                display: inline-block;
                flex: 1;

                .tr-box {
                  display: flex;
                  align-items: center;

                  .name {
                    display: -webkit-box;
                    flex: 1;
                    font-size: 14px;
                    font-weight: 500;
                    line-height: 1.5;
                    overflow: hidden;
                    text-overflow: ellipsis;
                    -webkit-line-clamp: 2;
                    -webkit-box-orient: vertical;
                  }

                  .price {
                    display: inline-block;
                    margin-left: 26px;
                    font-size: 14px;
                    font-weight: 400;
                    float: right;
                  }
                }

                .spec {
                  position: absolute;
                  bottom: 0;
                  display: flex;
                  align-items: center;

                  .text {
                    flex: 1;
                    line-height: 1.2;
                    font-size: 12px;
                    font-weight: 400;
                  }

                  .count {
                    display: inline-block;
                    margin-left: 30px;
                    float: right;
                  }
                }
              }
            }

            .order-pay {
              display: inline-block;
              width: 100%;
              margin-top: 14px;
              text-align: right;

              .price-count {
                display: block;

                .text {
                  display: inline-block;
                  font-size: 12px;

                  em {
                    display: inline-block;
                    margin-left: 5px;
                    font-size: 12px;
                    font-style: normal;
                    color: #D0021B;
                  }

                  strong {
                    display: inline-block;
                    margin-left: 2px;
                    font-size: 16px;
                    font-weight: bold;
                    color: #D0021B;
                  }
                }
              }

              .order-btn {
                display: inline-block;
                margin-top: 14px;
                width: 116px;
                height: 30px;
                line-height: 30px;
                text-align: center;
                font-size: 12px;
                color: #ffffff;
                background-color: #0076A5;
                border-radius: 16px;
              }
            }

            .order-status {
              position: absolute;
              top: 0;
              right: 0;
              display: inline-block;
              width: 64px;
              height: 26px;
              line-height: 26px;
              text-align: center;
              font-size: 12px;
              border-radius: 0 10px 0 10px;
              color: #4A4A4A;
              background-color: rgba(105, 207, 232, 0.2);
            }

            .btn {
              width: 86px;
              height: 26px;
              margin-top: 10px;
              line-height: 26px;
              text-align: center;
              font-size: 12px;
              font-weight: 600;
              border-radius: 14px;
              color: #0076A5;
              border: 1px solid #0076A5;
              float: right;
            }

            &.disable {
              color: #737373;

              .order-tag, em, strong {
                color: #737373 !important;
              }
            }
          }

          &.none {
            width: 100%;
            margin-top: 75px;
            text-align: center;

            .img {
              display: inline-block;
              width: 235px;
              height: 140px;
              background: url('../../common/images/no-order.png') no-repeat center;
              background-size: 235px 140px;
            }

            .text {
              margin-top: 16px;
              font-size: 16px;
            }

            .btn {
              margin-top: 67px;
              btn-submit();
            }
          }

          .load-more {
            margin-top: 30px;
            text-align: center;
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
    >>>.van-tabs {
      .van-tabs__wrap {
        .van-tab {
          &.van-tab--active, &:hover {
            color: $color-main;
          }
        }

        .van-tabs__line {
          background-color: $color-main;
        }
      }

      .van-tabs__content {
        padding-bottom: 68px;

        .order-list {
          padding: 0 16px;

          .order-item .order-pay .order-btn {
            background-color: $color-main;
          }

          &.none {
            .img {
              background: url('../../assets/images/ry/no-order.png') no-repeat center;
              background-size: 174px 135px;
            }

            .btn {
              width: 180px;
              background-color: $color-main;
            }
          }
        }
      }
    }
  }
}
</style>
<template>
  <div class="address-wrapper" :class="{ hiddenHeader: isMiniProgram }" v-clock>
    <THeader class="header-wrap" :title="'地址管理'" :hasBg="true"></THeader>
    <div class="content" v-if="addressList.length > 0">
      <div class="address-list">
        <div
          class="address-item"
          v-for="item in addressList"
          :key="item.id"
        >
          <div class="address-t" @click="chooseAddr(item.id)">
            <div class="name-tel">
              <p class="name">
                <span class="tag" v-if="item.defaultFlag">默认</span>
                {{ item.userName }}
              </p>
              <p class="tel">{{ item.phone }}</p>
            </div>
            <div class="address-detail">
              <p class="address">{{ item.detail }}</p>
              <p class="detail">{{ item.address }}</p>
            </div>
          </div>
          <div class="address-b">
            <span class="radio-wrap" @click.stop="handleDefault(item.id)">
              <van-checkbox v-model="item.defaultFlag"
                >设为默认地址</van-checkbox
              >
            </span>
            <div class="icon-wrap">
              <span
                class="sprite-icon icon-dashbox"
                @click.stop="onDelete(item.id)"
              ></span>
              <span
                class="sprite-icon icon-edit"
                @click.stop="onEdit(item)"
              ></span>
            </div>
          </div>
        </div>
      </div>
      <div class="btn-add" @click="onAdd">
        <p class="btn confirm-btn">添加收货地址</p>
      </div>
    </div>
    <!----无数据---->
    <div class="no-content" v-else>
      <NoData :flagType="'no-address'" @addAddr="onAdd"></NoData>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
import NoData from "components/noData/noData";
// js
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  data() {
    return {
      title: "地址管理",
      userId: "", // 用户id
      addressList: [], // 地址列表
      orderFlag: "", // 进入路径：order 订单进入
      addrId: "", // 地址ID
      page: 1,
      size: 10,
      isMiniProgram: false, // 是否是小程序
    };
  },
  mounted() {
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "address") {
      var params = this.getQueryVariable("enterParams");
      var enterParams = JSON.parse(params);
      localStorage.setItem("userId", enterParams.userId);
      localStorage.setItem("phone", enterParams.phone);
      localStorage.setItem("userNo", enterParams.userNo);
      localStorage.setItem("auth", enterParams.auth);
      localStorage.setItem("openId", enterParams.openid);
      localStorage.setItem("distributorId", enterParams.distributorId);
      localStorage.setItem("exchangeId", enterParams.exchangeId);
      localStorage.setItem("platform", enterParams.platform);
      localStorage.setItem("orderSource", enterParams.orderSource);

      this.orderFlag = enterParams.orderFlag;
      this.addrId = enterParams.addrId;

      this.isMiniProgram = true;
      sessionStorage.setItem("isMiniProgram", this.isMiniProgram);
    } else {
      this.orderFlag = this.$route.query.orderFlag;
      this.addrId = this.$route.query.addrId;
    }

    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.userId = localStorage.getItem("userId");
    if (this.userId) {
      this.initData();
    }
  },
  methods: {
    getQueryVariable(name) {
      var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
      var r = window.location.search.substr(1).match(reg);
      if (r != null) {
        return unescape(r[2]);
      } else {
        return null;
      }
    },
    initData() {
      this.$api
        .get(this, api.getAddrList, {
          id: this.userId,
          page: this.page,
          size: this.size,
        })
        .then((res) => {
          if (res.success) {
            this.addressList = [];
            if (res.data.list && res.data.list.length > 0) {
              res.data.list.forEach((item) => {
                this.$set(
                  item,
                  "detail",
                  item.provinceName + item.cityName + item.districtName
                );
                if (this.orderFlag === "order" && this.addrId == item.id) {
                  this.addressList.unshift(item);
                } else {
                  this.addressList.push(item);
                }
              });
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 添加收货地址
    onAdd() {
      this.$router.push({
        path: "/editAddress",
        query: {
          orderFlag: this.orderFlag,
        },
      });
    },
    // 编辑
    onEdit(item) {
      this.$router.push({
        path: "/editAddress",
        query: {
          id: item.id,
          orderFlag: this.orderFlag,
          addrInfo: JSON.stringify(item),
        },
      });
    },
    // 删除
    onDelete(id) {
      this.$dialog
        .confirm({
          message: "此操作将删除, 是否继续?",
        })
        .then(() => {
          this.$api
            .delete(this, api.updateAddr, {
              id: id,
            })
            .then((res) => {
              if (res.success) {
                this.$toast.success("删除成功");
                this.initData();
              } else {
                this.$toast.fail(res.errMessage);
              }
            });
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 设为默认
    handleDefault(id) {
      this.$api
        .put(this, api.setDefaultAddr, {
          id: id,
        })
        .then((res) => {
          if (res.success) {
            this.$toast.success("设置成功");
            this.initData();
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    },
    // 选择地址
    chooseAddr(id) {
      if (this.orderFlag === "order") {
        if (this.isMiniProgram) {
          let platform = localStorage.getItem("platform");
          if (platform === "GF60006") {
            // 字节小程序
            tt.miniProgram.redirectTo({
              url: "/pages/order/order/order?addrId=" + id,
            });
          } else if (platform === "GF60007" || platform === "GF60008") {
            // 支付宝小程序
            my.redirectTo({
              url: "/pages/order/order/order?addrId=" + id,
            });
          } else if (Number(platform) === 28) {
            wx.miniProgram.redirectTo({
              url: "/pages/order/order/order?addrId=" + id,
            });
          }
        } else {
          this.$router.push({
            path: "/order",
            query: {
              addrId: id,
            },
          });
        }
      }
    },
  },
  components: {
    THeader,
    NoData,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.address-wrapper {
  position: fixed;
  width: 100%;
  top: 44px;
  bottom: 0;
  background-color: $color-bg;
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: none;
  -ms-overflow-style: none;

  &::-webkit-scrollbar {
    display: none;
  }

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .content {
    width: 100%;
    height: 100%;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;

    &::-webkit-scrollbar {
      display: none;
    }

    .address-list {
      width: 100%;
      padding: 0 0 75px;

      .address-item {
        width: 100%;
        background: $color-bg-white;

        & + .address-item {
          margin-top: $spacing-sm;
        }
      }

      .address-t {
        padding: 24px $spacing-base 12px;

        .name-tel {
          font-size: $font-lg;
          color: $color-font-base;
          line-height: 22px;

          .tag {
            font-size: $font-sm;
            margin-right: 5px;
            padding: 4px 7px;
            background: rgbaMain(0.2);
            border-radius: $radius-xs;
          }

          .name {
            display: inline-block;
          }

          .tel {
            display: inline-block;
            margin-left: $spacing-sm;
            color: $color-font-grey;
          }
        }

        .address-detail {
          margin-top: 12px;
          line-height: 20px;

          .address {
            margin-top: 12px;
            font-size: $font-sm;
            color: $color-font-grey;
          }

          .detail {
            font-size: $font-base;
          }
        }
      }

      .address-b {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: $spacing-sm $spacing-base;
        border-top: 1px solid $color-border;

        .radio-wrap {
          color: $color-font-grey;
        }

        .icon-wrap {
          .sprite-icon {
            opacity: 0.7;

            & + .sprite-icon {
              margin-left: 40px;
            }
          }
        }
      }
    }

    .btn-add {
      position: fixed;
      left: 0;
      bottom: 0;
      width: 100%;
      padding: $spacing-sm 30px;
      background-color: $color-bg-white;

      .btn {
        font-size: $font-lg;
        align(center);
        lineHeight(45px);
        border-radius: 45px;
      }
    }
  }

  .no-content {
    position: relative;
    width: 100%;
    height: 100%;
    padding: 0 $spacing-base;
    text-align: center;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;
    scrollbar-width: none;
    -ms-overflow-style: none;
    background-color: $color-bg-white;

    &::-webkit-scrollbar {
      display: none;
    }

    .context {
      padding: 56px 30px 0;
      align(center);

      .empty-img {
        display: inline-block;
        width: 174px;
        height: 144px;
        bg-image('no-address');
        background-size: 100% 100%;
      }

      .text {
        display: block;
        margin-top: 50px;
        font-size: $font-lg;
      }

      .btn-add {
        margin: 100px auto;
        width: 100%;
        lineHeight(55px);
        border-radius: 55px;
        font-size: $font-lg;
        align(center);
        background-color: $color-main;
        vertical-align: middle;
      }
    }
  }
}

.van-checkbox {
  color: $color-font-grey;

  >>>.van-checkbox__icon {
    font-size: $font-lg;

    &.van-checkbox__icon--checked {
      .van-icon {
        color: $color-font-base;
        background-color: transparent;
        border-color: $color-font-base;
      }
    }

    .van-icon {
      width: 16px;
      height: 16px;
    }
  }
}
</style>

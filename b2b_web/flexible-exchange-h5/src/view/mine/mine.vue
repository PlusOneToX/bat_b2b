<!--
 * @Author: yaowei
 * @Date: 2019-11-24 19:21:01
 * @LastEditors: yaowei
 * @LastEditTime: 2019-08-13 09:22:07
-->
<template>
  <div class="manager">
    <div class="container">
      <div class="container-t-wrap">
        <div class="container-t">
          <div class="order-wrap">
            <div class="avatar-box">
              <!-- <div class="avatar-img" @click="goPath('/user', 'user')">
                <img :src="avatarImg" alt="" />
              </div>
              <div class="name" @click="goPath('/user', 'user')">
                {{ nikeName }}
              </div> -->
              <div class="avatar-img">
                <img :src="avatarImg" alt="" />
              </div>
              <div class="name">
                {{ nikeName }}
              </div>
            </div>
            <div class="all-order" @click="goPath('/orderList', 'orderList')">
              <span>我的订单</span>
              <span class="arrow-right">全部<van-icon name="arrow" /></span>
            </div>
            <ul class="item-ul">
              <li
                class="item-li"
                @click="goPath('/orderList', 'orderList', { sid: 1 })"
              >
                <span class="sprite-icon icon-my_pay"></span>
                <div class="text">待支付</div>
              </li>
              <li
                class="item-li"
                @click="goPath('/orderList', 'orderList', { sid: 2 })"
              >
                <span class="sprite-icon icon-my_ship"></span>
                <div class="text">待发货</div>
              </li>
              <li
                class="item-li"
                @click="goPath('/orderList', 'orderList', { sid: 4 })"
              >
                <span class="sprite-icon icon-my_shipping"></span>
                <div class="text">待收货</div>
              </li>
              <li
                class="item-li"
                @click="goPath('/orderList', 'orderList', { sid: 6 })"
              >
                <span class="sprite-icon icon-my_shipped"></span>
                <div class="text">已完成</div>
              </li>
            </ul>
          </div>
        </div>
      </div>
      <div class="service-list">
        <ul class="item-ul">
          <!-- <li class="item-li" @click="goPath('/couponList', 'couponList')">
            <span class="sprite-icon my_icon_coupon"></span>
            <span class="text">优惠券</span>
            <van-icon name="arrow" />
          </li> -->
          <li class="item-li" @click="goPath('/shopcart', 'shopcart')">
            <span class="sprite-icon icon-my_cart"></span>
            <span class="text">购物车</span>
            <van-icon name="arrow" />
          </li>
          <!-- <li class="item-li" @click="onClickCollect">
            <span class="sprite-icon icon-my_collect"></span>
            <span class="text">收藏的主题</span>
            <van-icon name="arrow" />
          </li> -->
          <li class="item-li" @click="goPath('/address', 'address')">
            <span class="sprite-icon icon-my_address"></span>
            <span class="text">地址管理</span>
            <van-icon name="arrow" />
          </li>
          <li class="item-li" @click="onClickService">
            <span class="sprite-icon icon-my_chat"></span>
            <span class="text">在线客服</span>
            <van-icon name="arrow" />
          </li>
        </ul>
      </div>
    </div>

    <Tabs :curTab="'mine'"></Tabs>

    <!-----在线客服弹框------>
    <van-popup v-model="serviceShow" position="bottom">
      <div class="service-box">
        <div class="service-item" @click="callPhone">
          <span class="sprite-icon icon-phone"></span>
          <span class="text">客服热线</span>
        </div>
        <div class="service-item" @click="wechat">
          <span class="sprite-icon icon-wechat"></span>
          <span class="text">微信客服</span>
        </div>
      </div>
      <div class="btn-cancel" @click="serviceShow = false">取消</div>
    </van-popup>
  </div>
</template>

<script type="text/ecmascript-6">
// 组件
import Tabs from "components/tabs/tabs";
// api
import api from "api/allApi.js";

export default {
  name: "mine",
  data() {
    return {
      userNo: "", // 用户编码
      phone: "", // 手机号
      userId: "", // 用户id
      nikeName: "游客", // 昵称
      avatarImg: require("../../common/images/user.png"), // 头像
      phoneNumber: "17302671334", // 客服电话
      wechatStr: "w-666357", // 微信客服
      serviceShow: false, // 在线客服弹窗
    };
  },
  mounted() {
    // 获取用户信息
    var enterFlag = this.getQueryVariable("enterFlag");
    if (enterFlag === "mine") {
      var params = this.getQueryVariable("enterParams");
      var enterParams = JSON.parse(params);
      this.userNo = enterParams.userNo || "";
      this.phone = enterParams.phone;
      this.userId = enterParams.userId;

      localStorage.setItem("userId", enterParams.userId);
      localStorage.setItem("phone", enterParams.phone);
      localStorage.setItem("userNo", enterParams.userNo);
      localStorage.setItem("auth", enterParams.auth);
      localStorage.setItem("openId", enterParams.openid);
    } else {
      this.userNo = localStorage.getItem("userNo") || "";
      this.phone = localStorage.getItem("phone");
      this.userId = localStorage.getItem("userId");
    }
    if (
      this.userNo !== "" &&
      this.userNo !== null &&
      this.userNo !== undefined
    ) {
      this.$api
        .get(this, api.getUserInfo, {
          id: this.userId,
        })
        .then((res) => {
          if (res.success) {
            this.userNo = res.data.no;
            this.phone = res.data.phone;
            if (
              res.data.nikeName !== null &&
              res.data.nikeName !== "" &&
              res.data.nikeName !== undefined
            ) {
              this.nikeName = res.data.nikeName;
            }
            if (
              res.data.headPortrait !== null &&
              res.data.headPortrait !== "" &&
              res.data.headPortrait !== undefined
            ) {
              this.avatarImg = res.data.headPortrait;
            }
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
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
    // 客服热线
    callPhone() {
      this.$dialog
        .confirm({
          title: "拨打热线",
          message: this.phoneNumber,
          className: "pop-dialog",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          window.location.href = "tel://" + this.phoneNumber;
          this.serviceShow = false;
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 微信客服
    wechat() {
      this.$dialog
        .confirm({
          title: "客服微信号：" + this.wechatStr,
          message: "是否复制微信号？",
          className: "pop-dialog",
          confirmButtonText: "复制",
          confirmButtonColor: "#333333",
          cancelButtonColor: "#999999",
        })
        .then(() => {
          let input = document.createElement("input");
          input.value = this.wechatStr;
          document.body.appendChild(input);
          input.select();
          input.setSelectionRange(0, input.value.length);
          document.execCommand("Copy");
          document.body.removeChild(input);
          this.$toast.success("复制成功");
        })
        .catch((error) => {
          console.log(error);
        });
    },
    // 在线客服
    onClickService() {
      this.serviceShow = true;
    },
    // 页面跳转
    goPath(path, flag, query) {
      if (query) {
        this.$router.push({
          path: path,
          query: query,
        });
      } else {
        this.$router.push(path);
      }
    },
  },
  components: {
    Tabs,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.manager {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 49px;
  background-color: $color-bg;

  .container {
    position: relative;
    width: 100%;
    height: 100%;
    overflow: hidden;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .container-t-wrap {
      background-color: $color-bg-white;
    }

    .container-t {
      position: relative;
      top: 0;
      left: -50%;
      width: 200%;
      height: 268px;
      background: linear-gradient(136deg, rgba(255, 234, 45, 0.9) 0%, rgba(255, 218, 1, 0.9) 40%, rgba(255, 205, 0, 0.9) 100%);
      background-position: center;
      background-size: 100% auto;
      border-radius: 0 0 50% 50%;
      overflow: hidden;

      &::before {
        content: '';
        position: absolute;
        left: 50%;
        bottom: 0;
        width: calc(50% - 30px);
        height: 153px;
        background-color: rgbaWhite(0.5);
        border-radius: $radius-lg $radius-lg 0 0;
        transform: translateX(-50%);
      }

      .order-wrap {
        position: relative;
        left: 25%;
        padding: 0 30px;
        width: 50%;
        height: 100%;
        background-color: transparent;

        &::before {
          content: '';
          position: absolute;
          left: 30px;
          bottom: 0;
          width: calc(100% - 60px);
          height: 188px;
          background-color: #FFFDF3;
          border-radius: $radius-base $radius-base 0 0;
        }

        .all-order {
          position: relative;
          margin-top: 18px;
          padding: 0 $spacing-base;
          display: flex;
          justify-content: space-between;
          font-size: $font-sl;

          .arrow-right {
            font-size: $font-base;
            color: $color-font-grey;
          }

          .van-icon {
            margin-left: 8px;
            top: 2px;
          }
        }

        .item-ul {
          margin-top: 11px;
          padding: 7px 0 3px;
          height: 65px;
          display: flex;
          flex-direction: row;

          .item-li {
            position: relative;
            flex: 1;
            font-size: $font-base;
            text-align: center;

            .icon-my_pay {
              margin-top: 2px;
            }
          }

          .text {
            position: absolute;
            left: 50%;
            bottom: 3px;
            white-space: nowrap;
            transform: translateX(-50%);
          }
        }
      }
    }

    .avatar-box {
      position: relative;
      text-align: center;

      .avatar-img {
        display: inline-block;
        width: 65px;
        height: 65px;
        margin-top: 48px;
        border-radius: 50%;
        border: 2px solid $color-bg-white;
        overflow: hidden;

        img {
          width: 100%;
          height: 100%;
        }
      }

      .name {
        margin-top: 12px;
        font-size: $font-lg;
        font-weight: 500;
        color: $color-font-base;
      }
    }

    .service-list {
      position: relative;
      padding-top: 12px;
      background-color: $color-bg-white;

      .item-ul {
        padding: 0 $spacing-base;

        .text {
          font-size: $font-sl;
          color: $color-font-base;
        }
      }

      .item-li {
        position: relative;
        display: block;
        padding-left: 34px;
        width: 100%;
        lineHeight(65px);
        font-size: $font-sl;

        .sprite-icon {
          position: absolute;
          top: 50%;
          left: 0;
          transform: translateY(-50%);
        }

        & + .item-li {
          border-top: 1px solid $color-border;
        }

        .van-icon {
          position: absolute;
          top: 50%;
          right: 0;
          transform: translateY(-50%);
        }
      }
    }
  }

  .van-popup {
    width: 100%;
    border-radius: $radius-lg $radius-lg 0 0;

    .service-box {
      padding: 0 $spacing-base;
      align(center);

      .service-item {
        lineHeight(65px);
        border-bottom: 1px solid $color-border;
      }

      .sprite-icon {
        display: inline-block;
        top: 5px;
      }

      .text {
        margin-left: 12px;
        font-size: $font-base;
      }
    }

    .btn-cancel {
      lineHeight(65px);
      font-size: $font-base;
      align(center);
    }
  }

  .wDialog {
    width: 80%;
    height: auto;
    padding: 20px 0 0;
    box-sizing: border-box;
    text-align: center;
    border-radius: 10px;

    .title {
      height: 30px;
      line-height: 30px;
      color: $color-text;
      font-size: $font-size-medium-x;
    }

    .text {
      height: 30px;
      line-height: 30px;
      color: $color-text;
      font-size: $font-size-medium-x;
    }

    .btn-box {
      margin-top: 15px;
      display: flex;
      overflow: hidden;
      -webkit-user-select: none;
      user-select: none;
      border-top: 1px solid #ebedf0;

      .btn {
        flex: 1;
        height: 50px;
        line-height: 50px;
        font-size: $font-size-large;
        text-align: center;

        &.cancel {
          color: $color-text;
          border-right: 1px solid #ebedf0;
        }

        &.submit {
          color: $color-theme;
        }
      }
    }
  }
}
</style>
<style lang="stylus" rel="stylesheet/stylus">
@media (prefers-color-scheme: dark) {
  .manager {
    background-color: #ffffff;
  }
}

.pop-dialog {
  .van-dialog__header {
    display: block;
    font-size: 16px;
    text-align: center;
    color: #4A4A4A;
  }

  .van-dialog__message {
    display: block;
    text-align: center;
    font-size: 16px;
    font-weight: bold;
    color: #4A4A4A;
  }
}
</style>

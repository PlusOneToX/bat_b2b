<!--
 * @Author: yaowei
 * @Date: 2019-11-24 19:21:01
 * @LastEditors: yaowei
 * @LastEditTime: 2019-08-13 09:21:25
-->
<template>
  <div class="manager">
    <div class="container">
      <div class="avatar-box">
        <!-- <div class="avatar-img" @click="goPath('/user', 'user')">
          <img :src="avatarImg" alt="" />
        </div>
        <div v-if="show" class="name">{{ nikeName }}</div>
        <div v-else class="name" @click="goPath('/user', 'user')">点击登录</div> -->
        <div class="avatar-img">
          <img :src="avatarImg" alt="" />
        </div>
        <div class="name">{{ nikeName ? nikeName : (phone ? phone : '') }}</div>
      </div>
      <div class="item-box order-wrap">
        <div class="tr-box">
          <span class="name">我的订单</span>
          <span class="text" @click="goPath('/orderList', 'orderList')">
            全部订单
            <van-icon name="arrow" />
          </span>
        </div>
        <ul class="item-ul">
          <li class="item-li">
            <div @click="goPath('/orderList', 'orderList', { sid: 1 })">
              <span class="sprite-icon my_button_pay"></span>
              <div class="text">待付款</div>
            </div>
          </li>
          <li class="item-li">
            <div @click="goPath('/orderList', 'orderList', { sid: 2 })">
              <span class="sprite-icon my_button_send"></span>
              <div class="text">待发货</div>
            </div>
          </li>
          <li class="item-li">
            <div @click="goPath('/orderList', 'orderList', { sid: 4 })">
              <span class="sprite-icon my_button_Receiving"></span>
              <div class="text">待收货</div>
            </div>
          </li>
          <li class="item-li">
            <div @click="goPath('/orderList', 'orderList', { sid: 6 })">
              <span class="sprite-icon my_button_finish"></span>
              <div class="text">已完成</div>
            </div>
          </li>
        </ul>
      </div>
      <div class="item-box service-list">
        <div class="tr-box">
          <span class="name">我的服务</span>
        </div>
        <ul class="item-ul">
          <li class="item-li" @click="goPath('/couponList', 'couponList')">
            <span class="sprite-icon my_icon_coupon"></span>
            <span class="text">优惠券</span>
            <van-icon name="arrow" />
          </li>
          <li class="item-li" @click="onClickService">
            <span class="sprite-icon my_icon_service"></span>
            <span class="text">在线客服</span>
            <van-icon name="arrow" />
          </li>
          <li class="item-li" @click="goPath('/address', 'address')">
            <span class="sprite-icon my_icon_address"></span>
            <span class="text">地址管理</span>
            <van-icon name="arrow" />
          </li>
        </ul>
      </div>
    </div>
    <!-----在线客服弹框------>
    <van-popup v-model="serviceShow" closeable position="bottom">
      <div class="service-box">
        <div class="service-item" @click="callPhone">
          <i class="icon icon-phone"></i>
          <div class="text">客服热线</div>
        </div>
        <div class="service-item" @click="wechat">
          <i class="icon icon-wechat"></i>
          <div class="text">微信客服</div>
          <div class="wechat" v-show="false">{{ wechatStr }}</div>
        </div>
      </div>
      <div class="btn-cancel" @click="serviceShow = false">取消</div>
    </van-popup>
    <tabs :curTab="'mine'" :curVersion="curVersion" :userNo="userNo"></tabs>
  </div>
</template>

<script type="text/ecmascript-6">
import Tabs from "../components/tabs.vue";
import { getLocalStorageItem } from "common/js/common";
// api
import api from "common/js/allApi.js";
export default {
  name: "mine",
  data() {
    return {
      userNo: "",
      phone: "",
      userId: "",
      nikeName: "",
      avatarImg: require("../../assets/images/user.png"),
      phoneNumber: "15994794151",
      wechatStr: "YYff-33",
      show: false,
      serviceShow: false,
      curVersion: "",
    };
  },
  mounted() {
    this.curVersion = localStorage.getItem("curVersion"); // 获取当前进入版本

    // 获取用户信息
    this.userNo = getLocalStorageItem("userNo") || "";
    this.userId = parseInt(getLocalStorageItem("userId"));
    if (
      this.userNo !== "" &&
      this.userNo !== null &&
      this.userNo !== undefined
    ) {
      this.show = true;
      // 获取用户信息
      this.$api
        .get(this, api.getUserInfo, {
          id: this.userId,
        })
        .then((res) => {
          if (res.success) {
            this.userNo = res.data.no;
            this.phone = res.data.phone;
            if (
              res.data.phone !== null &&
              res.data.phone !== "" &&
              res.data.phone !== undefined
            ) {
              this.phone = res.data.phone;
            }
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
            this.show = true;
          } else {
            this.$toast.fail(res.errMessage);
          }
        });
    }
  },
  methods: {
    // 客服热线
    callPhone() {
      this.$dialog
        .confirm({
          title: "拨打热线",
          message: this.phoneNumber,
          className: "pop-dialog",
          confirmButtonColor: "#31B8D8",
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
          confirmButtonColor: "#31B8D8",
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
      if (
        this.userNo !== "" &&
        this.userNo !== null &&
        this.userNo !== undefined
      ) {
        // 已登录
        if (query) {
          this.$router.push({
            path: path,
            query: query,
          });
        } else {
          this.$router.push(path);
        }
      } else {
        // 未登录
        this.$router.push({
          path: "/login",
          query: {
            comingFlag: flag,
            samFlag: path
          },
        });
      }
    },
    // 返回
    toback() {
      this.$router.push("/index");
    },
  },
  components: {
    Tabs,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
$bg = #F1F1F1;
$darkBlue = #009ddb;
$lightBlue = #1275d1;
$dark = #000;
$gray = #5A5A5A;

.manager {
  position: fixed;
  width: 100%;
  top: 0;
  bottom: 49px;
  background-color: $bg;

  .container {
    height: 100%;
    overflow-y: scroll;
    -webkit-overflow-scrolling: touch;

    &::-webkit-scrollbar {
      display: none;
    }

    .avatar-box {
      position: relative;
      height: 240px;
      text-align: center;
      background: linear-gradient(0deg, $darkBlue, $lightBlue);

      &:before {
        content: '';
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background: url('../../assets/images/background/my_bg.png') no-repeat;
        background-position: center;
        background-size: 100% auto;
      }

      .avatar-img {
        position: relative;
        display: inline-block;
        width: 71px;
        height: 71px;
        padding: 4px;
        margin-top: 40px;
        border-radius: 50%;
        border: 1px solid rgba(255, 255, 255, 0.72);
        overflow: hidden;

        img {
          display: inline-block;
          width: 100%;
          height: 100%;
          border-radius: 50%;
        }
      }

      .name {
        position: relative;
        margin-top: 10px;
        font-size: 17px;
        font-weight: 500;
        color: #ffffff;
      }
    }

    .item-box {
      position: relative;
      top: -50px;
      width: calc(100% - 30px);
      margin: 0 15px 12px;
      padding: 15px;
      box-sizing: border-box;
      border-radius: 5px;
      background-color: #ffffff;
      box-shadow: 0 2px 4px 0 rgba(227, 223, 223, 0.5);

      .tr-box {
        font-size: 16px;
        font-weight: 500;
        line-height: 20px;
        color: $dark;

        .text {
          font-size: 14px;
          color: $gray;
          float: right;

          .van-icon {
            top: 2px;
          }
        }
      }

      .item-ul {
        margin-top: 14px;

        .text {
          font-size: 14px;
          color: $gray;
        }
      }

      &.order-wrap {
        .item-ul {
          display: flex;
          flex-direction: row;

          .item-li {
            flex: 1;
            text-align: center;
          }

          .text {
            position: relative;
            top: -6px;
          }
        }
      }

      &.service-list {
        padding-bottom: 0;

        .item-li {
          position: relative;
          display: block;
          width: 100%;
          height: 60px;
          font-size: 15px;
          line-height: 60px;

          .sprite-icon {
            top: 7px;
            margin-right: 10px;
          }

          &+.item-li {
            border-top: 1px solid $bg;
          }

          .van-icon {
            position: absolute;
            top: 50%;
            right: 0;
            transform: translateY(-50%);
            color: #999999;
          }
        }
      }
    }
  }

  .van-popup {
    width: 100%;
    padding: 40px 0 25px;
    box-sizing: border-box;
    border-radius: 8px 8px 0 0;

    .service-box {
      text-align: left;
      padding-bottom: 15px;
      border-bottom: 1px solid #E5E5E5;

      .service-item {
        display: inline-block;
        width: 30%;
        text-align: center;

        .icon {
          display: inline-block;
          width: 30px;
          height: 30px;
          font-size: 30px;

          &.icon-phone {
            color: #0076A5;
          }

          &.icon-wechat {
            color: #07BF62;
          }
        }

        .text {
          display: block;
          margin-top: 6px;
          font-size: 14px;
          color: #4A4A4A;
        }
      }
    }

    .btn-cancel {
      display: block;
      margin-top: 30px;
      font-size: 18px;
      line-height: 2;
      text-align: center;
      color: #4A4A4A;
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

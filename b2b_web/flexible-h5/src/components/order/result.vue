<!--
 * @Author: yaowei
 * @Date: 2019-07-20 14:43:31
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-20 15:04:24
-->
<template>
  <div
    class="payResult"
    :class="{ 'ry-style': Number(distributorId) === 4378 }"
  >
    <v-header :back="true" :title="title" @back="toback"></v-header>
    <div class="container">
      <div class="img img-success"></div>
      <div class="text">兑换成功啦~</div>
      <div class="remind">
        <div class="title">温馨提示</div>
        <div class="item-ul">
          <div class="item-li">您的订单已进入工厂备料，最迟48小时内发货</div>
          <div class="item-li">定制产品无质量问题不提供退换货服务</div>
        </div>
      </div>
      <div class="btn-box">
        <div class="btn btn-normal-blue" @click="goPath('/orderList')">
          查看订单
        </div>
        <p
          v-show="Number(distributorId) === 4378"
          class="ry-index"
          @click="goPath('/index')"
        >
          返回首页
        </p>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
import VHeader from "components/v-header/v-header";
export default {
  name: "payResult",
  data() {
    return {
      title: "兑换成功",
      distributorId: "",
    };
  },
  created() {
    this.distributorId = parseInt(localStorage.getItem("distributorId"));
  },
  methods: {
    goPath(path) {
      this.$router.push(path);
    },
    toback() {
      this.$router.push("/orderList");
    },
  },
  beforeRouteEnter(to, from, next) {
    if (from.name === "orderList" || from.name === "mine") {
      next({
        path: "/mine",
      });
    } else {
      next();
    }
  },
  components: {
    VHeader,
  },
};
</script>

<style scoped lang="stylus" rel="stylesheet/stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.payResult {
  position: fixed;
  width: 100%;
  top: 45px;
  bottom: 0;
  background-color: $color-background;

  .container {
    height: 100%;
    padding: 48px 28px 0;
    box-sizing: border-box;
    text-align: center;

    .img {
      display: inline-block;
      width: 240px;
      height: 140px;
      background-repeat: no-repeat;
      background-size: 240px 140px;
      background-position: center;

      &.img-success {
        background-image: url('../../common/images/pay-success.png');
      }
    }

    .text {
      display: block;
      margin-top: 8px;
      font-size: 16px;
      color: $color-theme;
    }

    .remind {
      margin-top: 40px;
      text-align: left;

      .title {
        font-size: $font-size-medium;
        font-weight: bold;
        color: $color-text;
      }

      .item-ul {
        margin-top: 12px;

        .item-li {
          position: relative;
          display: block;
          padding-left: 12px;
          line-height: 1.5;
          font-size: $font-size-small;
          color: $color-text;

          &:after {
            content: ' ';
            position: absolute;
            left: 0;
            top: 50%;
            width: 4px;
            height: 4px;
            transform: translateY(-50%);
            background-color: #D0021B;
            border-radius: 100px;
          }
        }
      }
    }

    .btn-box {
      padding: 100px 30px 0;
      box-sizing: border-box;

      .btn {
        btn-submit();

        .icon {
          display: inline-block;
          margin-right: 12px;
          font-size: 20px;
          vertical-align: middle;
        }
      }

      .btn-normal-blue {
        background: #0076A5;
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
  background-color: $color-bg;

  .container {
    padding-top: 21px;

    .img {
      width: 174px;
      height: 174px;
      background-size: 174px 174px;

      &.img-success {
        background-image: url('../../assets/images/ry/pay-success.png');
      }
    }

    .text {
      color: $color-main;
    }

    .remind {
      margin-top: 40px;
      text-align: left;

      .title {
        font-size: $font-size-medium;
        font-weight: bold;
        color: $color-text;
      }

      .item-ul {
        margin-top: 12px;

        .item-li {
          position: relative;
          display: block;
          padding-left: 12px;
          line-height: 1.5;
          font-size: $font-size-small;
          color: $color-text;

          &:after {
            content: ' ';
            position: absolute;
            left: 0;
            top: 50%;
            width: 4px;
            height: 4px;
            transform: translateY(-50%);
            background-color: #D0021B;
            border-radius: 100px;
          }
        }
      }
    }

    .btn-box {
      padding: 100px 30px 0;
      box-sizing: border-box;

      .btn {
        width: 180px;
      }

      .btn-normal-blue {
        background-color: $color-main;
      }

      .ry-index {
        margin-top: 24px;
        font-size: 16px;
        color: $color-main;
      }
    }
  }
}
</style>
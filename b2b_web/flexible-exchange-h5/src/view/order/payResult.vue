<template>
  <div class="payResult" :class="{ hiddenHeader: isMiniProgram }">
    <THeader class="header-wrap" :title="'兑换结果'" :hasBg="true"></THeader>
    <div class="container">
      <div class="img" :class="payFlag ? 'img-success' : 'img-fail'"></div>
      <div class="text" v-show="payFlag">
        {{ orderSource === 28 ? "兑换成功" : "订单提交成功哦～" }}
      </div>
      <div class="text" v-show="!payFlag">
        {{ orderSource === 28 ? "兑换失败" : "订单提交失败啦～" }}
      </div>
      <div class="remind">
        <div class="title">温馨提示</div>
        <div class="item-ul" v-show="payFlag">
          <div class="item-li">您的订单已进入工厂备料，最迟48小时内发货</div>
          <div class="item-li">定制产品无质量问题不提供退换货服务</div>
        </div>
        <div class="item-ul" v-show="!payFlag">
          <div class="item-li">您的订单支付失败，请稍后重新支付</div>
        </div>
      </div>
      <div class="btn-wrap">
        <div class="btn border-btn" @click="goPath('/index')">返回首页</div>
        <div class="btn confirm-btn" @click="goPath('/orderList')">
          查看订单
        </div>
      </div>
    </div>
  </div>
</template>

<script type="text/ecmascript-6">
// 组件
import THeader from "components/tHeader/tHeader";
// js
import api from "api/allApi.js";
export default {
  name: "payResult",
  data() {
    return {
      title: "支付结果",
      orderSource: "",
      payFlag: true,
      isMiniProgram: false, // 是否是小程序
    };
  },
  created() {
    this.isMiniProgram = sessionStorage.getItem("isMiniProgram") || false;

    this.orderSource = parseInt(localStorage.getItem("orderSource"));

    this.result();
  },
  methods: {
    goPath(path) {
      this.$router.push(path);
    },
    result() {
      let id = this.$route.query.id;
      // 查询订单状态
      this.$api
        .get(this, api.orderStatus, { ids: id })
        .then((res) => {
          if (res.status === 200) {
            let status = res.data;
            if (status === 1 || status === 7) {
              // 待付款或已取消，fail
              this.payFlag = false;
            } else if (status === 2) {
              // 待发货，success
              this.payFlag = true;
            }
          } else {
            this.$toast.fail(res.error);
          }
        })
        .catch((error) => {
          console.log(error);
        });
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
    THeader,
  },
};
</script>

<style scoped lang="stylus">
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.payResult {
  position: fixed;
  width: 100%;
  top: 44px;
  bottom: 0;
  background-color: $color-bg-white;

  &.hiddenHeader {
    top: 0;

    .header-wrap {
      display: none;
    }
  }

  .container {
    height: 100%;
    padding-top: 88px;
    align(center);

    .img {
      display: inline-block;
      width: 174px;
      height: 113px;
      background-repeat: no-repeat;
      background-size: 100% 100%;
      background-position: center;

      &.img-success {
        bg-image('pay-success');
      }

      &.img-fail {
        // background-image:url('../../common/images/pay-fail.png')
      }
    }

    .text {
      display: block;
      margin-top: 6px;
      font-size: $font-lg;
    }

    .remind {
      display: inline-block;
      margin-top: 40px;
      align(left);

      .title {
        font-size: $font-base;
        font-weight: bold;
        color: $color-text;
      }

      .item-ul {
        margin-top: $spacing-sm;

        .item-li {
          display: block;
          line-height: 1.5;
          font-size: $font-sm;
        }
      }
    }

    .btn-wrap {
      display: flex;
      padding: 64px $spacing-base;

      .btn {
        flex: 1;
        lineHeight(45px);
        font-size: $font-lg;
        align(center);
        border-radius: 45px;

        & + .btn {
          margin-left: $spacing-base;
        }
      }
    }
  }
}
</style>

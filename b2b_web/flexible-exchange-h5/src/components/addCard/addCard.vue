<!--
 * @Author: yaowei
 * @Date: 2019-05-17 09:05:36
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-05 10:35:15
-->
<template>
  <div class="add-card">
    <div class="title">
      <h6 class="text">绑定兑换卡</h6>
      <van-icon name="close" @click="closeDialog" />
    </div>

    <p class="input-title">请输入兑换码：</p>
    <div class="input-wrap">
      <van-field
        class="input"
        v-model="secretCode"
        placeholder="手动输入兑换卡码（区分大小写）"
        clearable
      ></van-field>
    </div>

    <div class="btn-wrap">
      <span class="btn confirm-btn" @click="handleAdd">绑定</span>
    </div>
  </div>
</template>

<script>
// js
import wx from "weixin-js-sdk";
// api
import api from "api/allApi.js";

export default {
  name: "AddCard",
  data() {
    return {
      userId: "", // 用户id
      secretCode: "", // 兑换卡码
      weixin: {
        appId: "",
        timestamp: "",
        nonceStr: "",
        signature: "",
      },
    };
  },
  created() {
    this.userId = localStorage.getItem("userId");
  },
  methods: {
    // 关闭
    closeDialog() {
      this.$emit("close");
    },
    // 绑定兑换卡
    handleAdd() {
      let secretCode = this.secretCode
        .toUpperCase()
        .replace(/(^\s*)|(\s*$)/g, "");
      this.$api
        .post(this, api.addCard, {
          userId: this.userId,
          secretCode: secretCode,
        })
        .then((res) => {
          if (res.code === 0) {
            this.$toast("绑定成功");
            this.secretCode = "";
            this.$emit("getCard");
          } else {
            this.$toast(res.error);
          }
        });
    },
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.add-card {
  padding: 0 $spacing-base;

  .title {
    padding: 24px 13px 20px 17px;
    line-height: 25px;
    border-bottom: 1px solid $color-border;

    .text {
      display: inline-block;
      font-size: $font-lg;
      color: $color-font-base;
      font-weight: 500;
    }

    .van-icon {
      display: inline-block;
      font-size: 25px;
      float: right;
    }
  }

  .input-title {
    padding: 32px 17px 28px;
    font-size: $font-base;
  }

  .input-wrap {
    position: relative;

    .input {
      padding: 0 $spacing-base;
      font-size: $font-base;
      lineHeight(50px);
      border-radius: 50px;
      background-color: $color-bg-input;

      >>>.van-icon {
      }
    }

    .van-icon {
      position: absolute;
      top: 50%;
      right: 40px;
      font-size: 25px;
      transform: translateY(-50%);
    }
  }

  .btn-wrap {
    padding: 60px $spacing-base 0;
    display: flex;

    .btn {
      flex: 1;
      font-size: $font-lg;
      align(center);
      lineHeight(55px);
      border-radius: 55px;
    }
  }
}
</style>
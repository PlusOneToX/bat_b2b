<!--
 * @Author: yaowei
 * @Date: 2018-05-20 08:59:07
 * @LastEditors: yaowei
 * @LastEditTime: 2018-05-25 09:28:21
-->
<template>
  <div class="intro-dialog">
    <div class="intro-content">
      <h6 class="title">{{($i18n.locale === 'zh') ? '产品介绍' : 'Product Introduction'}}</h6>
      <div class="content" v-if="$i18n.locale === 'zh'">
        <p>产品介绍文案</p>
      </div>
      <div class="content" v-else>
        <p>Product Introduction Copy</p>
      </div>
      <div class="btn-wrap">
        <span class="gray-btn" @click="cancel()">{{$t('P.Cancel')}}</span>
        <span class="blue-btn" @click="goNext()">{{$t('P.Next')}}</span>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "productIntro",
  data() {
    return {};
  },
  methods: {
    // 取消
    cancel() {
      this.$emit("closeIntro");
    },
    // 下一步
    goNext() {
      this.$router.push({ name: "VerifyMobile" });
    },
  },
};
</script>

<style lang="less" scoped>
@import url("../../assets/less/variable.less");
.intro-dialog {
  position: fixed;
  top: 0;
  right: 0;
  bottom: 0;
  left: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 101;
  .intro-content {
    position: absolute;
    top: 50%;
    left: 50%;
    padding-bottom: 50px;
    width: 548px;
    background-color: @white;
    border-radius: 8px;
    transform: translate(-50%, -50%);
    box-shadow: 0px 6px 17px 0px rgba(255, 255, 255, 0.5);
    .title {
      padding-left: 28px;
      height: 40px;
      line-height: 40px;
      font-size: 16px;
      color: @lightBlack;
      font-weight: 500;
      background-color: @entryBd;
      border-radius: 8px 8px 0 0;
    }
    .content {
      padding: 60px 54px;
      font-size: 14px;
      color: @lightBlack;
      text-indent: 2em;
      line-height: 26px;
    }
    .btn-wrap {
      font-size: 18px;
      text-align: center;
      span {
        display: inline-block;
        width: 180px;
        height: 50px;
        line-height: 50px;
        border-radius: 4px;
        cursor: pointer;
        & + span {
          margin-left: 15px;
        }
        &.gray-btn {
          color: @lightBlack;
          background-color: @entryBd;
        }
        &.blue-btn {
          color: @white;
          background-color: @blue;
        }
        &:hover {
          opacity: 0.8;
        }
      }
    }
  }
}
</style>
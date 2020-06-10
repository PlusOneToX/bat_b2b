<!--
 * @Author: yaowei
 * @Date: 2019-11-20 19:10:58
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-26 10:53:43
-->
<template>
  <div class="header" :class="{ hasBg: hasBg, darkIcon: darkIcon }">
    <van-icon name="arrow-left" @click="handleBack" />
    <h6 class="title">{{ title }}</h6>
  </div>
</template>

<script>
export default {
  name: "header",
  props: ["title", "back", "hasBg", "darkIcon"],
  methods: {
    // 返回上一页
    handleBack() {
      if (this.$listeners["back"]) {
        // 如果提供执行
        this.$emit("back");
      } else {
        this.$router.back("-1");
      }
    },
  },
};
</script>

<style lang="stylus" scoped>
$white = #fff;
$dark = #000;

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 44px;
  line-height: 44px;
  z-index: 100;

  &.hasBg {
    background: $white;
    box-shadow: 0px 0px 7px 0px rgba(237, 237, 237, 0.6);

    .van-icon-arrow-left {
      color: $dark;
    }

    .title {
      color: $dark;
    }
  }

  &.darkIcon {
    .van-icon-arrow-left {
      color: $dark;
    }
  }

  .van-icon-arrow-left {
    position: absolute;
    top: 50%;
    left: 10px;
    font-size: 20px;
    color: $white;
    transform: translateY(-50%);
  }

  .title {
    padding: 0 80px;
    font-size: 17px;
    color: $white;
    text-align: center;
    font-weight: 500;
  }
}
</style>
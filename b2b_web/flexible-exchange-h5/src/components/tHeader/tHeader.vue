<!--
 * @Author: yaowei
 * @Date: 2019-05-28 17:20:35
 * @LastEditors: yaowei
 * @LastEditTime: 2019-06-05 12:54:19
-->
<template>
  <div class="header" :class="{ hasBg: hasBg, darkIcon: darkIcon }">
    <van-icon name="arrow-left" @click="handleBack" />
    <h6 class="title">{{ title }}</h6>
  </div>
</template>

<script>
export default {
  name: "tHeader",
  props: {
    title: {
      type: String,
      default: "",
    },
    back: {
      type: Boolean,
      default: false,
    },
    hasBg: {
      type: Boolean,
      default: false,
    },
    darkIcon: {
      type: Boolean,
      default: true,
    },
  },
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
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  lineHeight(44px);
  z-index: 100;

  &.hasBg {
    background: $color-bg-white;
    box-shadow: 0px 0px 7px 0px rgba(237, 237, 237, 0.6);

    .van-icon-arrow-left {
      color: $color-font-base;
      background-color: transparent;
    }

    .title {
      color: $color-font-base;
    }
  }

  &.darkIcon {
    .van-icon-arrow-left {
      color: $color-font-base;
    }
  }

  .van-icon-arrow-left {
    position: absolute;
    top: 50%;
    left: $spacing-base;
    font-size: 20px;
    color: $color-bg-white;
    padding: 3px;
    background-color: rgbaBlack(0.3);
    border-radius: $radius-circle;
    transform: translateY(-50%);
    
    &::before {
      position: relative;
      left: -1px;
    }
  }

  .title {
    padding: 0 80px;
    font-size: $font-lg;
    color: $color-bg-white;
    align(center);
    font-weight: 500;
  }
}
</style>
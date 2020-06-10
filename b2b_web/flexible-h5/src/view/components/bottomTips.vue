<!--
 * @Author: yaowei
 * @Date: 2019-11-20 18:04:20
 * @LastEditors: yaowei
 * @LastEditTime: 2019-12-03 14:07:41
-->
<template>
  <p class="arrive-bottom" @click="backToTop">
    <span class="line l-left"></span>
    没有更多了，回顶部<span class="sprite-icon home_icon_top"></span>
    <span class="line l-right"></span>
  </p>
</template>

<script>
export default {
  name: "bottomTips",
  methods: {
    // 回到顶部
    backToTop() {
      window.scrollTo(0, 0);
    },
  },
};
</script>

<style lang="stylus" scoped>
$light-gray = #B9B9B9;
$line-color = #E1E1E1;

.arrive-bottom {
  margin: 20px 0;
  font-size: 14px;
  color: $light-gray;
  text-align: center;

  .line {
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 50%;
      margin-top: -1px;
      width: 64px;
      height: 2px;
      background: $line-color;
    }

    &.l-left {
      &::before {
        right: 12px;
      }
    }

    &.l-right {
      &::before {
        left: 12px;
      }
    }
  }

  .sprite-icon {
    top: 0;
    margin-left: 4px;
  }
}
</style>
<!--
 * @Author: yaowei
 * @Date: 2019-05-11 11:31:34
 * @LastEditors: yaowei
 * @LastEditTime: 2019-11-30 19:03:31
-->
<template>
  <div>
    <div class="bubble" :class="isDown ? 'down' : 'up'">
      <p>{{ text }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "Bubble",
  props: {
    text: {
      type: String,
      default: "",
    },
    isDown: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.bubble {
  padding: 0 8px;
  height: 28px;
  position: relative;
  background-color: $color-font-base;
  border-radius: $radius-xs;
  white-space: nowrap;

  p {
    color: $color-bg-white;
    transform: scale(0.9);
  }

  &:before, &:after {
    content: '';
    display: block;
    border-width: 5px;
    position: absolute;
    left: 50%;
    font-size: 0;
    line-height: 0;
    transform: translateX(-50%);
  }

  &.up {
    &:before {
      top: -9px;
      border-style: dashed dashed solid;
      border-color: transparent transparent $color-font-base;
    }
  }

  &.down {
    &:after {
      bottom: -9px;
      border-style: solid dashed dashed;
      border-color: $color-font-base transparent transparent;
    }
  }
}
</style>
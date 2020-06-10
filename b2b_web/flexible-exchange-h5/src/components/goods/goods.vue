<!--
 * @Author: yaowei
 * @Date: 2019-05-08 10:22:11
 * @LastEditors: yaowei
 * @LastEditTime: 2019-07-28 09:09:53
-->
<template>
  <div class="goods-wrap">
    <div
      class="goods-img-wrap"
      :style="{
        'background-image':
          'url(' +
          (goodsInfo.backgroundPreviewUrl
            ? goodsInfo.backgroundPreviewUrl
            : defaultImg) +
          ')',
      }"
    ></div>
    <!-- <p class="goods-name">
      {{ goodsInfo.pictureName ? goodsInfo.pictureName : goodsInfo.name }}
    </p> -->
    <p class="custom-btn">选我定制</p>
  </div>
</template>

<script>
export default {
  name: "Goods",
  props: {
    goodsInfo: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {
      defaultImg: require("../../common/images/default.png"), // 默认图片
    };
  },
  created() {},
  methods: {},
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.goods-wrap {
  position: relative;
  height: 245px;
  border-radius: $radius-xl;
  overflow: hidden;

  .goods-img-wrap {
    position: absolute;
    top: 50%;
    left: 50%;
    width: 100%;
    height: 100%;
    background-color: $default-bg;
    background-size: 100%;
    background-position: center center;
    background-repeat: no-repeat;
    transform: translate(-50%, -50%);

    .goods-img {
      min-width: 100%;
      max-height: 100%;
      position: absolute;
      top: 50%;
      left: 50%;
      transform: translate(-50%, -50%);
    }
  }

  .goods-name {
    position: absolute;
    bottom: 0;
    left: 0;
    width: 100%;
    lineHeight(30px);
    font-size: $font-base;
    align(center);
    background-color: rgbaWhite($opacity-base);
  }

  .custom-btn {
    position: absolute;
    left: 50%;
    bottom: 10px;
    width: 105px;
    height: 26px;
    line-height: 26px;
    font-size: $font-base;
    align(center);
    background-color: rgbaWhite($opacity-base);
    border-radius: 26px;
    transform: translateX(-50%);
  }
}
</style>

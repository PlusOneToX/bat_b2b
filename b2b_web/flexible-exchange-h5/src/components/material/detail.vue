<!--
 * @Author: yaowei
 * @Date: 2019-05-11 12:14:51
 * @LastEditors: yaowei
 * @LastEditTime: 2019-05-31 10:04:59
-->
<template>
  <div class="material-detail">
    <div class="detail-l">
      <img :src="materialInfo.image" alt="" />
    </div>
    <div class="detail-r">
      <h6>{{ materialInfo.subtitle }}</h6>
      <p>{{ materialInfo.description }}</p>
    </div>
  </div>
</template>

<script>
export default {
  name: "MaterialDetail",
  props: {
    materialInfo: {
      type: Object,
      default: {},
    },
  },
  data() {
    return {};
  },
  watch: {
    materialInfo(value) {
      return value;
    },
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.material-detail {
  position: relative;
  padding: 0 $spacing-sm;
  width: 100%;
  height: 210px;
  background-color: $color-bg-white;

  .detail-l {
    position: absolute;
    top: 50%;
    left: 37px;
    width: 123px;
    height: 100%;
    transform: translateY(-50%);
    overflow: hidden;

    img {
      position: absolute;
      top: 0;
      left: 50%;
      height: 100%;
      transform: translateX(-50%);
    }
  }

  .detail-r {
    padding-left: 50%;
    padding-top: 30px;
    padding-right: $spacing-base;
    height: 100%;
    display: flex;
    flex-direction: column;

    h6 {
      font-size: $font-base;
      color: $color-font-base;
      line-height: 20px;
    }

    p {
      margin-top: 23px;
      font-size: $font-sm;
      color: $color-font-darkGrey;
      line-height: 18px;
    }
  }
}
</style>
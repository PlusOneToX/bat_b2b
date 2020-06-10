<!--
 * @Author: yaowei
 * @Date: 2019-05-21 16:36:14
 * @LastEditors: yaowei
 * @LastEditTime: 2019-06-29 09:38:20
-->
<template>
  <div class="custom-intro">
    <div class="img-wrap">
      <p class="text">Be your own designer</p>
      <img class="text-img" src="../../common/images/custom-text.png" alt="" />
      <img class="bg-img" src="../../common/images/custom-bg.png" alt="" />
    </div>
    <div class="btn-wrap">
      <p class="btn" @click="handleCustom">立即定制</p>
    </div>

    <Tabs :curTab="'custom'"></Tabs>
  </div>
</template>

<script>
// 组件
import Tabs from "components/tabs/tabs";

export default {
  name: "CustomIntro",
  data() {
    return {};
  },
  methods: {
    // 立即定制
    handleCustom() {
      this.$router.push({
        name: "custom",
      });
    },
  },
  components: {
    Tabs,
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.img-wrap {
  position: fixed;
  top: 0;
  bottom: 40px;
  width: 100%;

  .text {
    position: absolute;
    top: 50px;
    left: 50%;
    transform: translateX(-50%);
    font-size: $font-sm;
    color: #005597;
    z-index: 2;
  }

  .text-img {
    position: absolute;
    top: 76px;
    left: 50%;
    height: 32px;
    transform: translateX(-50%);
    z-index: 2;
  }

  .bg-img {
    position: absolute;
    left: 50%;
    height: 100%;
    transform: translateX(-50%);
    z-index: 1;
  }
}

.btn-wrap {
  position: fixed;
  width: 100%;
  left: 0;
  bottom: 85px;
  display: flex;
  justify-content: center;

  .btn {
    btn-normal();
    width: 180px;
    lineHeight(45px);
  }
}
</style>
<!--
 * @Author: yaowei
 * @Date: 2019-05-20 10:49:55
 * @LastEditors: yaowei
 * @LastEditTime: 2019-09-01 15:20:39
-->
<template>
  <div class="no-data">
    <!-- 数据为空 -->
    <template v-if="flagType === 'no-order'">
      <div class="empty-img no-order"></div>
      <div class="text">暂无数据</div>
      <div class="btn small" @click="handleCustom" v-show="showBtn">去定制</div>
    </template>
    <!-- 购物车为空 -->
    <template v-if="flagType === 'no-cart'">
      <div class="empty-img no-cart"></div>
      <div class="text">购物车还是空的，快去定制吧~</div>
      <div class="btn small" @click="handleCustom">去定制</div>
    </template>
    <!-- 暂无收货地址 -->
    <template v-if="flagType === 'no-address'">
      <div class="empty-img no-address"></div>
      <div class="text">暂无收货地址</div>
      <div class="btn" @click="handleAdd">添加收货地址</div>
    </template>
    <!-- 暂无网络 -->
    <template v-if="flagType === 'no-network'">
      <div class="empty-img no-address"></div>
      <div class="text">
        <p>暂无网络</p>
        <p>请检查网络或刷新重试</p>
      </div>
      <div class="btn-border" @click="handleAdd">添加收货地址</div>
    </template>
  </div>
</template>

<script>
export default {
  name: "NoData",
  props: {
    flagType: {
      type: String,
      default: "no-order",
    },
    showBtn: {
      type: Boolean,
      default: false,
    },
  },
  data() {
    return {};
  },
  methods: {
    // 去定制
    handleCustom() {
      this.$router.push("/custom");
    },
    // 添加收货地址
    handleAdd() {
      this.$emit("addAddr");
    },
  },
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable';
@import '~common/styles/mixin.styl';

.no-data {
  position: absolute;
  width: 100%;
  padding: 0 30px;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  align(center);

  .empty-img {
    display: inline-block;
    width: 180px;
    height: 149px;

    &.no-order {
      height: 132px;
      bg-image('no-order');
      background-size: 100% 100%;

      .btn {
        margin-top: $spacing-lg;
      }
    }

    &.no-cart {
      bg-image('no-cart');
      background-size: 100% 100%;
    }

    &.no-address {
      width: 174px;
      height: 174px;
      bg-image('no-address');
      background-size: 100% 100%;
    }
  }

  .text {
    display: block;
    margin-top: $spacing-sm;
    font-size: $font-lg;
    color: $color-font-base;
    line-height: 45px;
  }

  .btn {
    margin: 60px auto;
    width: 100%;
    lineHeight(55px);
    border-radius: 55px;
    font-size: $font-lg;
    align(center);
    background-color: $color-main;
    vertical-align: middle;

    &.small {
      margin: 80px auto;
      width: 165px;
      lineHeight(45px);
    }
  }

  .btn-border {
    margin: 60px auto;
    width: 165px;
    lineHeight(45px);
    border-radius: $radius-sm;
    font-size: $font-lg;
    align(center);
    border: 2px solid $color-main;
    background-color: $color-bg-white;
    vertical-align: middle;
  }
}
</style>
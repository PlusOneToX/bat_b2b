<!--
 * @Author: yaowei
 * @Date: 2019-05-21 16:04:09
 * @LastEditors: yaowei
 * @LastEditTime: 2019-09-24 13:52:08
-->
<template>
  <div class="tabs-wrap">
    <router-link tag="div" class="tabs-item" to="/index">
      <span
        class="sprite-icon"
        :class="curTab === 'index' ? 'icon-tab_index_active' : 'icon-tab_index'"
      ></span>

      <p class="text" :class="{ active: curTab === 'index' }">首页</p>
    </router-link>
    <router-link tag="div" class="tabs-item" to="/theme">
      <span
        class="sprite-icon"
        :class="curTab === 'theme' ? 'icon-tab_theme_active' : 'icon-tab_theme'"
      ></span>

      <p class="text" :class="{ active: curTab === 'theme' }">官方主题</p>
    </router-link>
    <router-link tag="div" class="tabs-item" to="/custom-intro">
      <span
        class="sprite-icon"
        :class="
          curTab === 'custom' ? 'icon-tab_custom_active' : 'icon-tab_custom'
        "
      ></span>

      <p class="text" :class="{ active: curTab === 'custom' }">传图定制</p>
    </router-link>
    <router-link tag="div" class="tabs-item" to="/mine">
      <span
        class="sprite-icon"
        :class="curTab === 'mine' ? 'icon-tab_mine_active' : 'icon-tab_mine'"
      ></span>

      <p class="text" :class="{ active: curTab === 'mine' }">我的</p>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "Tabs",
  props: {
    curTab: {
      type: String,
      default: "",
    },
  },
  data() {
    return {};
  },
  methods: {},
};
</script>

<style lang="stylus" scoped>
@import '~common/styles/variable.styl';
@import '~common/styles/mixin.styl';

.tabs-wrap {
  display: flex;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 49px;
  background: $color-bg-white;
  box-shadow: 0px -1px 7px -2px rgba(189, 189, 189, 0.5);
  z-index: 88;

  .tabs-item {
    padding-top: 3px;
    display: flex;
    align-content: space-between;
    flex-direction: column;
    align-items: center;
    flex: 1;

    &.router-link-active {
      .tab-link, .icon {
        color: $color-font-base;
      }
    }

    .sprite-icon {
      width: 27px;
      height: 27px;
      top: 0;
    }

    .text {
      margin-top: 2px;
      font-size: $font-sm;
      color: $color-font-grey;

      &.active {
        color: $color-font-base;
      }
    }
  }
}
</style>
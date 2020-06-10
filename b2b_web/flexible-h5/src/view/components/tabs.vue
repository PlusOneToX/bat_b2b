<template>
  <div class="tabs">
    <router-link
      tag="div"
      class="tabs-item"
      to="/recommend"
      v-if="curVersion === 'new'"
    >
      <span
        class="sprite-icon"
        :class="curTab === 'recommend' ? ' tab_bat_pre_sam' : 'tab_bat_sam'"
      ></span>
      <p
        class="text"
        :class="{ active: curTab === 'recommend' }"
      >
        首页
      </p>
    </router-link>
    <router-link tag="div" class="tabs-item" to="/index" v-else>
      <span
        class="sprite-icon"
        :class="curTab === 'index' ? ' tab_bat_pre_sam' : 'tab_bat_sam'"
      ></span>
      <p class="text" :class="{ active: curTab === 'index' }">首页</p>
    </router-link>
    <router-link
      tag="div"
      class="tabs-item"
      :key="key"
      :to="curVersion && Number(platform) !== 7 ? '/phone?key=-1' : '/phone?key=-1'"
      v-if="curVersion || isShow"
    >
      <span
        class="sprite-icon"
        :class="curTab === 'custom' ? ' tab_customized_pre' : 'tab_customized'"
      ></span>
      <p class="text" :class="{ active: curTab === 'custom' }">定制</p>
    </router-link>
    <router-link v-if="userNo" tag="div" class="tabs-item" to="/shopcart">
      <span
        class="sprite-icon"
        :class="curTab === 'shopcart' ? ' tab_Cart_pre' : 'tab_Cart'"
      ></span>
      <p class="text" :class="{ active: curTab === 'shopcart' }">购物车</p>
    </router-link>
    <router-link
      v-else
      tag="div"
      class="tabs-item"
      :to="{ path: '/login', query: { comingFlag: 'shopcart' } }"
    >
      <span
        class="sprite-icon"
        :class="curTab === 'shopcart' ? ' tab_Cart_pre' : 'tab_Cart'"
      ></span>
      <p class="text" :class="{ active: curTab === 'shopcart' }">购物车</p>
    </router-link>
    <router-link tag="div" class="tabs-item" to="/mine">
      <span
        class="sprite-icon"
        :class="curTab === 'mine' ? ' tab_my_pre' : 'tab_my'"
      ></span>
      <p class="text" :class="{ active: curTab === 'mine' }">我的</p>
    </router-link>
  </div>
</template>

<script>
export default {
  name: "tabs",
  props: ["curTab", "curVersion", "userNo"],
  data() {
    return {
      platform: "",
      isShow: false,
    };
  },
  created() {
    this.platform = localStorage.getItem("platform");

    if (this.platform === "7") {
      this.isShow = true;
    } else {
      this.isShow = false;
    }
  },
  computed: {
    key() {
      return this.$route.name !== undefined
        ? this.$route.name + +new Date()
        : this.$route + +new Date();
    },
  },
};
</script>

<style lang="stylus" scoped>
$tab-color = #999999;
$blue = #0076A5;
$shawdow = #DDDDDD;

.tabs {
  display: flex;
  position: fixed;
  bottom: 0;
  left: 0;
  width: 100%;
  height: 49px;
  background: #FFFFFF;
  box-shadow: 0px -1px 0px 0px $shawdow;

  .tabs-item {
    flex: 1;
    text-align: center;

    .sprite-icon {
      top: 0;
      margin-top: 5px;
    }

    .text {
      margin-top: 4px;
      font-size: 13px;
      color: $tab-color;

      &.active {
        color: $blue;
      }
    }
  }
}
</style>

<template>
  <div>
    <div class="index rl-margin-zero">
      <!--公共头部-->
      <Header
        @add="addFood"
        @bdd="bddFood"
        :userState="userState"
        :nav_index="nav_index"
        :reLoad="reLoad"
        :logOuts="logOut"
        :totalCount="cartNum"
      ></Header>
      <!--主内容-->
      <commonGoodsList :cateFlag="'new'"></commonGoodsList>
      <!--公共底部-->
      <Footer></Footer>
    </div>
  </div>
</template>

<script>
import commonGoodsList from "@/components/commonGoodsList.vue";
import Footer from "@/components/Footer.vue";
import Header from "@/components/Header.vue";
export default {
  name: "NewProduct",
  components: { Footer, Header, commonGoodsList },
  data() {
    return {
      userState: 2,
      nav_index: Number(this.$route.query.nav_index) + 1,
      reLoad: true,
      logOut: true, // 被登出状态
      cartNum: 0,
    };
  },
  methods: {
    addFood(target) {
      // 切换栏目ID
      this.columnId = target;
      this.tabs = "showImg";
      this.getColumnGoodsList();
      this.getColumnGoodsSum();
    },
    bddFood(target) {
      // 栏目index
      this.nav_index = target + 1;
    },
  },
  created() {},
};
</script>

<style scoped="scoped" lang='less'>
.index {
  width: 100%;
}
</style>
